package in.explicate.fcm.TabFragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.adapter.InfraAutocompleteAdapter;
import in.explicate.fcm.adapter.MyInfraInfoAdapter;
import in.explicate.fcm.adapter.MyInfraInfoListAdapter;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.MyInfraInfoAPI;
import in.explicate.fcm.apiinterface.models.ResidentialProjectApiModel;
import in.explicate.fcm.application.AppClass;
import in.explicate.fcm.model.DepartmentModel;
import in.explicate.fcm.model.MyInfraInfoModel;
import in.explicate.fcm.ui.HomeActivity;
import in.explicate.fcm.util.MyUtility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by iFocus on 27-10-2015.
 */
public class AddInfrInfoFragment extends Fragment {


    private List<MyInfraInfoModel> list;
    private MyInfraInfoListAdapter adapter;
    private View rootView;
    private RelativeLayout loaderll;
    private FloatingActionButton addInfra;
    private List<DepartmentModel> projectList;
    private String selectedId,selectedInfraId;
    private InfraAutocompleteAdapter adapterAuto;
    private AutoCompleteTextView tvDepa;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_add_infra_info, container, false);
        list=new ArrayList<>();
        projectList=new ArrayList<>();
        setUpViews();
        return  rootView;
    }

    private void setUpViews() {


        addInfra=(FloatingActionButton)rootView.findViewById(R.id.addInfra);
        addInfra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!projectList.isEmpty()){

                    projectList.clear();
                }

                callApitogetProjects();

            }
        });


        loaderll=(RelativeLayout)rootView.findViewById(R.id.loaderll);
        loaderll.setVisibility(View.GONE);

        //hideLoader();

        callApiToList();


    }

    private void callApitogetProjects() {

        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ResidentialProjectApiModel> call3 = apiInterface.getResidentialProjects();
        call3.enqueue(new Callback<ResidentialProjectApiModel>() {
            @Override
            public void onResponse(Call<ResidentialProjectApiModel> call, Response<ResidentialProjectApiModel> response) {

                Log.e("Data:",new Gson().toJson(response.body()));

                hideLoader();


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            ResidentialProjectApiModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null){

                                    if(model.data.result.size() >0){


                                        for(int i =0;i<model.data.result.size();i++){

                                            DepartmentModel item=new DepartmentModel();
                                            item.setId(model.data.result.get(i).id);
                                            item.setName(model.data.result.get(i).name);
                                            projectList.add(item);
                                        }

                                        showAddDialog();


                                    }

                                }
                                else{

                                    MyUtility.showAlertMessage(getActivity(),model.message);

                                }


                            }else{

                                MyUtility.showAlertMessage(getActivity(),model.message);
                            }


                        }else{

                            MyUtility.showAlertMessage(getActivity(),MyUtility.FAILED_TO_GET_DATA);
                        }


                        break;

                    case 500 :

                        MyUtility.showAlertMessage(getActivity(),MyUtility.ERROR_500);


                        break;


                    case 401:

                        MyUtility.showAlertMessage(getActivity(),MyUtility.ERROR_401);

                        break;

                    case 404:

                        MyUtility.showAlertMessage(getActivity(),MyUtility.ERROR_404);

                        break;
                }

            }

            @Override
            public void onFailure(Call<ResidentialProjectApiModel> call, Throwable t) {
                call.cancel();
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });
    }

    private void showAddDialog() {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.dialog_add_infra, null);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(alertLayout);


        final AlertDialog alert=dialog.create();


        final EditText textView=(EditText)alertLayout. findViewById(R.id.editProjects);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bindProjects(textView);
            }
        });

        tvDepa=(AutoCompleteTextView)alertLayout. findViewById(R.id.editInfra);
        tvDepa.setEnabled(false);
        tvDepa.setThreshold(1);
        adapterAuto = new InfraAutocompleteAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line);
        tvDepa.setAdapter(adapterAuto);

        //when autocomplete is clicked
        tvDepa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if(!adapterAuto.getItem(position).getId().toString().equalsIgnoreCase("0")){

                    String name = adapterAuto.getItem(position).getInfra();
                    selectedInfraId=adapterAuto.getItem(position).getId();
                    tvDepa.setText(name);

                }else{

                    selectedInfraId="";
                    tvDepa.setText("");
                }



               // String name = adapterAuto.getItem(position).getInfra();
               // selectedInfraId=adapterAuto.getItem(position).getId();
                //tvDepa.setText(name);
            }
        });

        final EditText tvDetails=(EditText)alertLayout. findViewById(R.id.editCitizenId);


        Button btnAdd=(Button)alertLayout.findViewById(R.id.addInfra);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedId !=null && selectedId.equalsIgnoreCase("")){


                }else if(selectedInfraId !=null && selectedInfraId.equalsIgnoreCase("")){


                }else if(tvDetails.getText().toString().equalsIgnoreCase("")){


                }else{

                    alert.dismiss();
                    callAddApi(selectedInfraId,tvDetails.getText().toString());
                }
            }
        });


        alert.show();
    }

    private void callAddApi(String selectedInfraId, String citizzenId) {

        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<JsonObject> call3 = apiInterface.requestUserInfra(AppClass.getUserId(),selectedInfraId,citizzenId);
        call3.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e("Data:",new Gson().toJson(response.body()));
                  hideLoader();
                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            JsonObject model=response.body();

                            if(model.get("Status").getAsString().equalsIgnoreCase("Success")){



                                MyUtility.showAlertMessage(getActivity(),model.get("Message").getAsString());





                            }else{

                                MyUtility.showAlertMessage(getActivity(),model.get("Message").getAsString());
                            }





                        }else{

                            MyUtility.showAlertMessage(getActivity(),MyUtility.FAILED_TO_GET_DATA);
                        }


                        break;

                    case 500 :

                        MyUtility.showAlertMessage(getActivity(),MyUtility.ERROR_500);


                        break;


                    case 401:

                        MyUtility.showAlertMessage(getActivity(),MyUtility.ERROR_401);

                        break;

                    case 404:

                        MyUtility.showAlertMessage(getActivity(),MyUtility.ERROR_404);

                        break;
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                call.cancel();
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }

    private void bindProjects(final EditText edit) {


        final String[]items=new String[projectList.size()];

        for(int i=0;i<projectList.size();i++){
            items[i]=projectList.get(i).getName();
        }

        AlertDialog.Builder ad=new AlertDialog.Builder(getActivity());
        ad.setTitle("Select Project");


        ad.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                tvDepa.setEnabled(true);
                selectedId= projectList.get(i).getId()+"";
                edit.setText(projectList.get(i).getName());
                adapterAuto.setSelectdProjectId(selectedId);
                dialogInterface.dismiss();


            }
        });

        AlertDialog dialog=ad.create();
        dialog.show();

    }


    private void showData(){

        RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutParams);

        adapter = new MyInfraInfoListAdapter(list,getActivity());
        recyclerView.setAdapter(adapter);
    }

    private void callApiToList() {


        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<MyInfraInfoAPI> call3 = apiInterface.getMyInfra(AppClass.getUserId());
        call3.enqueue(new Callback<MyInfraInfoAPI>() {
            @Override
            public void onResponse(Call<MyInfraInfoAPI> call, Response<MyInfraInfoAPI> response) {

                Log.e("Data:",new Gson().toJson(response.body()));

                hideLoader();


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            MyInfraInfoAPI model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null){

                                if(model.data.userInfra.size() >0){


                                    for(int i =0;i<model.data.userInfra.size();i++){

                                        MyInfraInfoModel item=new MyInfraInfoModel();
                                        item.setId(model.data.userInfra.get(i).id+"");
                                        item.setInfra(model.data.userInfra.get(i).infra);
                                        list.add(item);
                                    }


                                }

                                showData();

                                }
                                else{

                                    MyUtility.showAlertMessage(getActivity(),model.message);

                                }


                            }else{

                                    MyUtility.showAlertMessage(getActivity(),model.message);
                            }


                        }else{

                            MyUtility.showAlertMessage(getActivity(),MyUtility.FAILED_TO_GET_DATA);
                        }


                        break;

                    case 500 :

                        MyUtility.showAlertMessage(getActivity(),MyUtility.ERROR_500);


                        break;


                    case 401:

                        MyUtility.showAlertMessage(getActivity(),MyUtility.ERROR_401);

                        break;

                    case 404:

                        MyUtility.showAlertMessage(getActivity(),MyUtility.ERROR_404);

                        break;
                }

            }

            @Override
            public void onFailure(Call<MyInfraInfoAPI> call, Throwable t) {
                call.cancel();
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }


    private void showLoader(){
        //  loaderll.setVisibility(View.VISIBLE);

        ((HomeActivity)getActivity()).showPrgressBar();

    }

    private void hideLoader(){
        // loaderll.setVisibility(View.GONE);

        ((HomeActivity)getActivity()).hideProgressBar();
    }

}