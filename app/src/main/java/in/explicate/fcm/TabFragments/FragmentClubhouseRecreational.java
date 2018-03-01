package in.explicate.fcm.TabFragments;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.adapter.ClubhouseRecreationalAdapter;
import in.explicate.fcm.adapter.StandardProcAdapter;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.ClassesAPIModel;
import in.explicate.fcm.apiinterface.models.ClassesDetailAPIModel;
import in.explicate.fcm.apiinterface.models.ClubHouseAPIModel;
import in.explicate.fcm.apiinterface.models.MyInfraInfoAPI;
import in.explicate.fcm.apiinterface.models.StdProcDetailModel;
import in.explicate.fcm.application.AppClass;
import in.explicate.fcm.model.ClassDetailModel;
import in.explicate.fcm.model.ClassModel;
import in.explicate.fcm.model.ClubHouseModel;
import in.explicate.fcm.model.MyInfraInfoModel;
import in.explicate.fcm.model.StandardProcModel;
import in.explicate.fcm.ui.HomeActivity;
import in.explicate.fcm.util.MyUtility;
import in.explicate.fcm.util.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by iFocus on 27-10-2015.
 */
public class FragmentClubhouseRecreational extends Fragment {


    private List<ClubHouseModel> departmentModelList;
    private List<ClassModel> listClasses;
    private List<ClassDetailModel> listDetails;

    private ClubhouseRecreationalAdapter adapter;
    private View rootView;
    private String selectedId;
    private RelativeLayout loaderll;
    private EditText editDepartment;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_clubhouse_recreational, container, false);
        listClasses=new ArrayList<>();
        departmentModelList=new ArrayList<>();
        listDetails=new ArrayList<>();
        setUpViews();
        return  rootView;
    }

    private void setUpViews() {

        loaderll=(RelativeLayout)rootView.findViewById(R.id.loaderll);
        loaderll.setVisibility(View.GONE);




        editDepartment=(EditText)rootView.findViewById(R.id.editClubhouse);
        //editDepartment.setEnabled(false);
        editDepartment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction()==MotionEvent.ACTION_UP){

                    if(departmentModelList.size()>0){

                        bindDepartmentData();
                    }

                }

                return false;
            }
        });



        callApiToGetDepartment();

    }

    private void callApiToGetDepartment() {


        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ClubHouseAPIModel> call3 = apiInterface.getAllClubhouses();
        call3.enqueue(new Callback<ClubHouseAPIModel>() {
            @Override
            public void onResponse(Call<ClubHouseAPIModel> call, Response<ClubHouseAPIModel> response) {

                Log.e("Data:",new Gson().toJson(response.body()));

                hideLoader();


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            ClubHouseAPIModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null) {

                                    if (model.data.result.size() > 0) {


                                        for (int i = 0; i < model.data.result.size(); i++) {

                                            ClubHouseModel item = new ClubHouseModel();
                                            item.setId(model.data.result.get(i).id+"");
                                            item.setInfra(model.data.result.get(i).infra);
                                            departmentModelList.add(item);
                                        }


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
            public void onFailure(Call<ClubHouseAPIModel> call, Throwable t) {
                call.cancel();
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }

    private void bindDepartmentData(){

        final String[]items=new String[departmentModelList.size()];

        for(int i=0;i<departmentModelList.size();i++){
            items[i]=departmentModelList.get(i).getInfra();
        }

        AlertDialog.Builder ad=new AlertDialog.Builder(getActivity());
        ad.setTitle("Select Clubhouse");

        ad.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                selectedId= departmentModelList.get(i).getId();
                editDepartment.setText(departmentModelList.get(i).getInfra());

                dialogInterface.dismiss();

                callApi(selectedId+"");


            }
        });

        AlertDialog dialog=ad.create();
        dialog.show();

    }

    private void showData(){

        RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutParams);

        adapter = new ClubhouseRecreationalAdapter(listClasses,getActivity());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                getDetailApiCall(listClasses.get(position).getId()+"");

            }
        }));

        //in.magarpatta
    }

    private void getDetailApiCall(String id) {



        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ClassesDetailAPIModel> call3 = apiInterface.getRecreationalClassesDetails(id);
        call3.enqueue(new Callback<ClassesDetailAPIModel>() {
            @Override
            public void onResponse(Call<ClassesDetailAPIModel> call, Response<ClassesDetailAPIModel> response) {

                Log.e("Data:",new Gson().toJson(response.body()));

                hideLoader();


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            ClassesDetailAPIModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null) {

                                    if(model.data.classes!=null){

                                        if (model.data.classes.size() > 0) {


                                            for (int i = 0; i < model.data.classes.size(); i++) {

                                                ClassDetailModel item = new ClassDetailModel();
                                                item.setId(model.data.classes.get(i).id+"");
                                                item.setEvent(model.data.classes.get(i).event);
                                                item.setDays(model.data.classes.get(i).days);
                                                item.setBookingDate(model.data.classes.get(i).date);
                                                item.setType("Class");
                                                listDetails.add(item);
                                            }

                                        }

                                    }else{

                                        MyUtility.showAlertMessage(getActivity(),model.message);

                                    }

                                    if(model.data.vendorDetails!=null) {

                                        if (model.data.vendorDetails.size() > 0) {


                                            for (int i = 0; i < model.data.vendorDetails.size(); i++) {

                                                ClassDetailModel item = new ClassDetailModel();
                                                item.setId(model.data.vendorDetails.get(i).id + "");
                                                item.setName(model.data.vendorDetails.get(i).name);
                                                item.setAddress(model.data.vendorDetails.get(i).address);
                                                item.setContact(model.data.vendorDetails.get(i).contact);
                                                item.setType("Vendor");
                                                listDetails.add(item);
                                            }


                                        }
                                    }
                                    else{

                                        MyUtility.showAlertMessage(getActivity(),model.message);


                                    }

                                    showDetailDialog();


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
            public void onFailure(Call<ClassesDetailAPIModel> call, Throwable t) {
                call.cancel();
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

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

    private void callApi(String id) {

        Log.e("ID",id);

        Log.e("AT","callApi");


        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ClassesAPIModel> call3 = apiInterface.getCurrentRecreationalClasses(id);
        call3.enqueue(new Callback<ClassesAPIModel>() {
            @Override
            public void onResponse(Call<ClassesAPIModel> call, Response<ClassesAPIModel> response) {

                hideLoader();
                Log.e("Data:",new Gson().toJson(response.body()));


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            ClassesAPIModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null){

                                    if(model.data.classes.size() >0){


                                        for(int i =0;i<model.data.classes.size();i++){

                                            ClassModel item=new ClassModel();
                                            item.setId(model.data.classes.get(i).id+"");
                                            item.setName(model.data.classes.get(i).name);
                                            item.setEvent(model.data.classes.get(i).event);
                                            item.setBookingDate(model.data.classes.get(i).bookingDate);
                                            listClasses.add(item);

                                        }

                                        showData();

                                    }
                                }else{

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
            public void onFailure(Call<ClassesAPIModel> call, Throwable t) {
                call.cancel();
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });


    }


    private void showDetailDialog(){


    }


}
