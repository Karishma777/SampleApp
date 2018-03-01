package in.explicate.fcm.TabFragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.adapter.BadmintonAdapter;
import in.explicate.fcm.adapter.MyInfraInfoAdapter;
import in.explicate.fcm.adapter.NewsLetterAdapter;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.BadmintonAPIModel;
import in.explicate.fcm.apiinterface.models.DepartmentApiDetail;
import in.explicate.fcm.apiinterface.models.MyInfraInfoAPI;
import in.explicate.fcm.apiinterface.models.NewsLetterAPIModel;
import in.explicate.fcm.application.AppClass;
import in.explicate.fcm.model.BadmintonModel;
import in.explicate.fcm.model.DepartmentModel;
import in.explicate.fcm.model.MyInfraInfoModel;
import in.explicate.fcm.model.NewsLetterModel;
import in.explicate.fcm.ui.HomeActivity;
import in.explicate.fcm.util.MyUtility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by iFocus on 27-10-2015.
 */
public class MyInfrInfoFragment extends Fragment {


    private List<MyInfraInfoModel> list;
    private MyInfraInfoAdapter adapter;
    private View rootView;
    private RelativeLayout loaderll;
    private List<MyInfraInfoModel> infrList;
    private String selectedId;
    private EditText editInfra;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_my_infra_info, container, false);
        list=new ArrayList<>();
        infrList=new ArrayList<>();
        setUpViews();
        return  rootView;
    }

    private void setUpViews() {


        loaderll=(RelativeLayout)rootView.findViewById(R.id.loaderll);
        loaderll.setVisibility(View.GONE);

       // hideLoader();


        editInfra=(EditText)rootView.findViewById(R.id.editInfra);
        editInfra.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction()==MotionEvent.ACTION_UP){

                    if(infrList.size()>0){

                        bindDepartmentData();
                    }
                }

                return true;
            }
        });

        callApiToList();


    }

    private void callApi(String date) {



        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<MyInfraInfoAPI> call3 = apiInterface.getInfraInfo(date,AppClass.getUserId());
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

                                if(model.data!=null) {

                                    if(model.data.owners!=null){




                                    if (model.data.owners.size() > 0) {


                                        for (int i = 0; i < model.data.owners.size(); i++) {

                                            MyInfraInfoModel item = new MyInfraInfoModel();
                                            item.setInfra(model.data.owners.get(i).id+"");
                                            item.setName(model.data.owners.get(i).name);
                                            item.setType("Owner");
                                            list.add(item);
                                        }

                                    }

                                    }else{

                                        MyUtility.showAlertMessage(getActivity(),model.message);

                                    }

                                    if(model.data.occupant!=null) {

                                        if (model.data.occupant.size() > 0) {


                                            for (int i = 0; i < model.data.occupant.size(); i++) {

                                                MyInfraInfoModel item = new MyInfraInfoModel();
                                                item.setInfra(model.data.occupant.get(i).id + "");
                                                item.setName(model.data.occupant.get(i).name);
                                                item.setType("Occupant");
                                                list.add(item);
                                            }


                                        }
                                    }
                                    else{

                                        MyUtility.showAlertMessage(getActivity(),model.message);


                                    }

                                    showData();


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
            public void onFailure(Call<MyInfraInfoAPI> call, Throwable t) {
                call.cancel();
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }

    private void showData(){

        RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutParams);

        adapter = new MyInfraInfoAdapter(list,getActivity());
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
                                        infrList.add(item);
                                    }


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
            public void onFailure(Call<MyInfraInfoAPI> call, Throwable t) {
                call.cancel();
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }

    private void bindDepartmentData(){

        if(!list.isEmpty()){

            list.clear();
        }

        final String[]items=new String[infrList.size()];

        for(int i=0;i<infrList.size();i++){
            items[i]=infrList.get(i).getInfra();
        }

        AlertDialog.Builder ad=new AlertDialog.Builder(getActivity());
        ad.setTitle("Select Infra");


        ad.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                selectedId= infrList.get(i).getId();
                editInfra.setText(infrList.get(i).getInfra());
                callApi(selectedId+"");
                dialogInterface.dismiss();


            }
        });

        AlertDialog dialog=ad.create();
        dialog.show();

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


}