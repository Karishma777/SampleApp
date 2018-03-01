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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.adapter.CSMAutocompleteAdapter;
import in.explicate.fcm.adapter.InfraAutocompleteAdapter;
import in.explicate.fcm.adapter.MyCSMAdapter;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.CategoryApiModel;
import in.explicate.fcm.apiinterface.models.MyInfraInfoAPI;
import in.explicate.fcm.apiinterface.models.PendingComplaintAPIModel;
import in.explicate.fcm.apiinterface.models.ResidentialProjectApiModel;
import in.explicate.fcm.application.AppClass;
import in.explicate.fcm.model.ComplaintModel;
import in.explicate.fcm.model.DepartmentModel;
import in.explicate.fcm.model.MyInfraInfoModel;
import in.explicate.fcm.ui.HomeActivity;
import in.explicate.fcm.util.MyUtility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahesh on 12/12/17.
 */

public class FragmentOtherCSM extends Fragment  {


    private List<ComplaintModel> list;
    private View rootView;
    private RelativeLayout loaderll;
    private List<MyInfraInfoModel> infrList;
   // private String selectedId;
  //  private EditText editInfra;
    //   private LinearLayout llmain,llpending,llold;
   // private Button btnPending,btnOld;

    private ArrayList<DepartmentModel> categoryList;
    private ArrayList<DepartmentModel> categoryTypes;

    private ArrayAdapter aaType;

    EditText spinType,spin,spinInfra;


    private String selectedInfraId="",selectedCategoryId="",selectedTypeID="";


    private AutoCompleteTextView tvDepa;
    private CSMAutocompleteAdapter adapterAuto;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_other_csm_pending, container, false);
        list=new ArrayList<>();
        infrList=new ArrayList<>();
        categoryList=new ArrayList<>();
        categoryTypes=new ArrayList<>();
        setUpViews();
        return  rootView;
    }

    private void setUpViews() {

        loaderll=(RelativeLayout)rootView.findViewById(R.id.loaderll);
        loaderll.setVisibility(View.GONE);

       // hideLoader();

        Log.e("AT","OTHERCSM");


        FloatingActionButton btnAddCSM=(FloatingActionButton)rootView.findViewById(R.id.addCSM);
        btnAddCSM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callAPiToGetProjects();
            }
        });


        callPendingList();


    }

    private void callPendingList() {

        if(!list.isEmpty()){

            list.clear();
        }

        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<PendingComplaintAPIModel> call3 = apiInterface.getOtherPendingComplaints(AppClass.getUserId());
        call3.enqueue(new Callback<PendingComplaintAPIModel>() {
            @Override
            public void onResponse(Call<PendingComplaintAPIModel> call, Response<PendingComplaintAPIModel> response) {

                Log.e("Data:",new Gson().toJson(response.body()));

                hideLoader();


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            PendingComplaintAPIModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null) {

                                    if(model.data.otherPendingComplaints!=null){

                                        if (model.data.otherPendingComplaints.size() > 0) {


                                            for (int i = 0; i < model.data.otherPendingComplaints.size(); i++) {

                                                ComplaintModel item = new ComplaintModel();
                                                item.setId(model.data.otherPendingComplaints.get(i).id);
                                                item.setComplaintNo(model.data.otherPendingComplaints.get(i).csmNO);
                                                item.setComplaintType(model.data.otherPendingComplaints.get(i).csmType);
                                                item.setStatus(model.data.otherPendingComplaints.get(i).status);
                                                list.add(item);
                                            }

                                            showData();


                                        }

                                    }else{

                                        MyUtility.showAlertMessage(getActivity(),model.message);

                                    }




                                }else{

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
            public void onFailure(Call<PendingComplaintAPIModel> call, Throwable t) {
                call.cancel();
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }

    private void showData(){

        RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerViewPending);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutParams);

        MyCSMAdapter adapter = new MyCSMAdapter(list,getActivity());
        recyclerView.setAdapter(adapter);

    }


    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    private void showLoader(){
        //  loaderll.setVisibility(View.VISIBLE);

        ((HomeActivity)getActivity()).showPrgressBar();

    }

    private void hideLoader(){
        // loaderll.setVisibility(View.GONE);

        ((HomeActivity)getActivity()).hideProgressBar();
    }

    private void addCSM(){


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_add_other_csm, null);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(alertLayout);


        final AlertDialog alert=dialog.create();


        spinType = (EditText) alertLayout. findViewById(R.id.spinnerType);
        spinType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bindSpinTypeData();
            }
        });



        spin = (EditText) alertLayout. findViewById(R.id.spinnerCategories);
        spin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bindCategoryData();
            }
        });



        spinInfra = (EditText)alertLayout.findViewById(R.id.spinnerSelectInfra);
        spinInfra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bindInfrData();
            }
        });


        tvDepa=(AutoCompleteTextView)alertLayout. findViewById(R.id.autocompleteInfraSearch);
        tvDepa.setEnabled(false);
        tvDepa.setThreshold(1);
        adapterAuto = new CSMAutocompleteAdapter(getActivity(),android.R.layout.simple_dropdown_item_1line);
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


            }
        });



        final EditText edesc=(EditText)alertLayout.findViewById(R.id.editdesc);

        Button btnAdd=(Button)alertLayout.findViewById(R.id.addCSM);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(selectedInfraId.equalsIgnoreCase("")){

                    spinInfra.setError("Enter Project");

                }else  if(selectedCategoryId.equalsIgnoreCase("")){

                    spin.setError("Select Category");

                }else  if(selectedTypeID.equalsIgnoreCase("")){

                    spinType.setError("Enter Category Type");

                }else  if(edesc.getText().toString().equalsIgnoreCase("")){

                    spinType.setError("Enter Description");

                }else {

                    alert.dismiss();
                    callApiAddCSM(selectedTypeID,edesc.getText().toString(),selectedInfraId);

                }

            }
        });

        alert.show();


    }

    private void bindInfrData(){

        final String[]itemsInfra=new String[infrList.size()];
        for(int i=0;i<infrList.size();i++){
            itemsInfra[i]=infrList.get(i).getInfra();
        }
        AlertDialog.Builder ad=new AlertDialog.Builder(getActivity());
        ad.setTitle("Select Category");

        ad.setSingleChoiceItems(itemsInfra, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                spinInfra.setText(infrList.get(i).getInfra());
                tvDepa.setEnabled(true);
                adapterAuto.setSelectdProjectId(infrList.get(i).getId());

            }
        });

        AlertDialog dialog=ad.create();
        dialog.show();
    }

    private void bindCategoryData(){



        final String[]items=new String[categoryList.size()];
        for(int i=0;i<categoryList.size();i++){
            items[i]=categoryList.get(i).getName();
        }

        AlertDialog.Builder ad=new AlertDialog.Builder(getActivity());
        ad.setTitle("Select Category");

        ad.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                spin.setText(categoryList.get(i).getName());
                selectedCategoryId=categoryList.get(i).getId()+"";
                callApiToGetTypes(selectedCategoryId);
            }
        });

        AlertDialog dialog=ad.create();
        dialog.show();
    }


    private void bindSpinTypeData(){


        final String[]itemTypes=new String[categoryTypes.size()];
        for(int i=0;i<categoryTypes.size();i++){
            itemTypes[i]=categoryTypes.get(i).getName();
        }

        AlertDialog.Builder ad=new AlertDialog.Builder(getActivity());
        ad.setTitle("Select Category Type");

        ad.setSingleChoiceItems(itemTypes, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                selectedTypeID =categoryTypes.get(i).getId()+"";
                spinType.setText(categoryTypes.get(i).getName());
            }
        });

        AlertDialog dialog=ad.create();
        dialog.show();
    }




    private void callAPiToGetProjects() {



        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ResidentialProjectApiModel> call3 = apiInterface.getMAndResidentialProjects();
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

                                            MyInfraInfoModel item=new MyInfraInfoModel();
                                            item.setId(model.data.result.get(i).id+"");
                                            item.setInfra(model.data.result.get(i).name);
                                            infrList.add(item);
                                        }


                                    }

                                    callAPiToGetCategories();

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




    private void callAPiToGetCategories() {


        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }


        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<CategoryApiModel> call3 = apiInterface.retrieveComplaintCategories();
        call3.enqueue(new Callback<CategoryApiModel>() {
            @Override
            public void onResponse(Call<CategoryApiModel> call, Response<CategoryApiModel> response) {

                Log.e("Data:",new Gson().toJson(response.body()));

                hideLoader();

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            CategoryApiModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data.result.size() >0){


                                    for(int i =0;i<model.data.result.size();i++){

                                        DepartmentModel item=new DepartmentModel();
                                        item.setId(model.data.result.get(i).id);
                                        item.setName(model.data.result.get(i).name);
                                        categoryList.add(item);
                                     }

                                    addCSM();
                                }

                            }else{

                                MyUtility.showAlertMessage(getActivity(),MyUtility.NO_DATA_FOUND);
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
            public void onFailure(Call<CategoryApiModel> call, Throwable t) {
                call.cancel();
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }


    private void callApiToGetTypes(String id) {

        if(!categoryTypes.isEmpty()){

            categoryTypes.clear();
        }

        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }


        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<CategoryApiModel> call3 = apiInterface.retrieveComplaintTypes(id);
        call3.enqueue(new Callback<CategoryApiModel>() {
            @Override
            public void onResponse(Call<CategoryApiModel> call, Response<CategoryApiModel> response) {

                Log.e("Data:",new Gson().toJson(response.body()));


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            CategoryApiModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                           if(model.data.resultTypes!=null) {

                               if (model.data.resultTypes.size() > 0) {


                                   for (int i = 0; i < model.data.resultTypes.size(); i++) {

                                       DepartmentModel item = new DepartmentModel();
                                       item.setId(model.data.resultTypes.get(i).id);
                                       item.setName(model.data.resultTypes.get(i).name);
                                       categoryTypes.add(item);
                                   }


                                   final String[] itemTypes = new String[categoryTypes.size()];
                                   for (int i = 0; i < categoryTypes.size(); i++) {
                                       itemTypes[i] = categoryTypes.get(i).getName();
                                   }

                                   aaType = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,itemTypes);
                                   aaType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                   aaType.notifyDataSetChanged();


                               }

                           }

                                else{

                                    MyUtility.showAlertMessage(getActivity(),model.message);

                                }

                            }else{

                                MyUtility.showAlertMessage(getActivity(),MyUtility.NO_DATA_FOUND);
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
            public void onFailure(Call<CategoryApiModel> call, Throwable t) {
                call.cancel();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }

    private void callApiAddCSM(String complaintTypeID,String complaintDetails,String selectedInfraID) {

        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        Log.e("ADD",complaintTypeID+complaintDetails+selectedInfraID);

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<JsonObject> call3 = apiInterface.registerOthersComplaint(complaintTypeID,complaintDetails,selectedInfraID,AppClass.getUserId());
        call3.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e("AddCSM:",new Gson().toJson(response.body()));

                hideLoader();

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            JsonObject model=response.body();
                            MyUtility.showAlertMessage(getActivity(),model.get("Message").getAsString());

                            callPendingList();


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

}
