package in.explicate.fcm.TabFragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.adapter.ClassifiedCategoryAdapter;
import in.explicate.fcm.adapter.ClassifiedCategoryCountAdapter;
import in.explicate.fcm.adapter.StandardProcAdapter;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.ClassifiedApiModel;
import in.explicate.fcm.apiinterface.models.ClassifiedCategoryAPIModel;
import in.explicate.fcm.apiinterface.models.ClassifiedCountApiModel;
import in.explicate.fcm.apiinterface.models.ClassifiedDetailsAPIModel;
import in.explicate.fcm.apiinterface.models.DepartmentApiDetail;
import in.explicate.fcm.apiinterface.models.StdProcDetailModel;
import in.explicate.fcm.application.AppClass;
import in.explicate.fcm.model.*;
import in.explicate.fcm.ui.ClassifiedsByCategoryActivity;
import in.explicate.fcm.ui.HomeActivity;
import in.explicate.fcm.util.MyUtility;
import in.explicate.fcm.util.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahesh on 12/12/17.
 */

public class FragmentClassfiedMyClassifieds extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private List<ClassifiedModel> classsifiedCount;
    private ClassifiedCategoryCountAdapter adapter;
    private View rootView;
    private RelativeLayout loaderll;

    private String selectedDeaprtmentId="";
    private EditText editDepartmet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_classified, container, false);
        classsifiedCount=new ArrayList<>();
        setUpViews();
        return  rootView;
    }

    private void setUpViews() {

        loaderll=(RelativeLayout)rootView.findViewById(R.id.loaderll);
        loaderll.setVisibility(View.GONE);


        swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                callApiToGetCategoryCount();
            }
        });



        FloatingActionButton btnRefresh=(FloatingActionButton)rootView.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                callApiToGetCategoryCount();
            }
        });


        FloatingActionButton  btnAdd=(FloatingActionButton)rootView.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // add feedback
                // addFeedbackDialog();

                callApiToGetCategory();

            }
        });


        callApiToGetCategoryCount();

    }

    private void callApiToGetCategory() {

        if(!MyUtility.isConnected(getActivity())){
            swipeRefreshLayout.setRefreshing(false);
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ClassifiedCategoryAPIModel> call3 = apiInterface.getAllClassifiedsCategories();
        call3.enqueue(new Callback<ClassifiedCategoryAPIModel>() {
            @Override
            public void onResponse(Call<ClassifiedCategoryAPIModel> call, Response<ClassifiedCategoryAPIModel> response) {

                Log.e("Data:",new Gson().toJson(response.body()));

                hideLoader();
                swipeRefreshLayout.setRefreshing(false);

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            ClassifiedCategoryAPIModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null) {

                                    if (model.data.classifiedCategories.size() > 0) {

                                        ArrayList departmentModelList = new ArrayList<>();



                                        for (int i = 0; i < model.data.classifiedCategories.size(); i++) {

                                            DepartmentModel item = new DepartmentModel();
                                            item.setId(model.data.classifiedCategories.get(i).id);
                                            item.setName(model.data.classifiedCategories.get(i).categoryName);
                                            departmentModelList.add(item);
                                        }

                                        addClassfiedDialog(departmentModelList);


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
            public void onFailure(Call<ClassifiedCategoryAPIModel> call, Throwable t) {
                call.cancel();
                hideLoader();
                swipeRefreshLayout.setRefreshing(false);
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });
    }

    private void addClassfiedDialog(final ArrayList<DepartmentModel> departmentModelList) {


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_add_classified, null);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(alertLayout);


        final AlertDialog alert=dialog.create();


        final EditText esubject=(EditText)alertLayout.findViewById(R.id.editsubject);


        TextView tvTitle=(TextView)alertLayout.findViewById(R.id.tvTitle);
        tvTitle.setText("ADD MY CLASSIFIED");


        editDepartmet = (EditText) alertLayout. findViewById(R.id.editDepartment);
        editDepartmet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bindDepartmentData(departmentModelList);
            }
        });



        final EditText edesc=(EditText)alertLayout.findViewById(R.id.editdesc);

        Button btnAdd=(Button)alertLayout.findViewById(R.id.addFeedback);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(esubject.getText().toString().length()==0){

                    esubject.setError("Enter Subject");

                }else  if(selectedDeaprtmentId.equalsIgnoreCase("")){

                    editDepartmet.setError("Select Department");

                }else  if(edesc.getText().toString().length()==0){

                    edesc.setError("Enter Description");

                }else {

                    alert.dismiss();

                    Log.e("data",esubject.getText().toString()+selectedDeaprtmentId+edesc.getText().toString());

                    callApiAddClassfied(esubject.getText().toString(),selectedDeaprtmentId,edesc.getText().toString());

                }
            }
        });

        alert.show();



    }


    private void callApiAddClassfied(String subject,String departId,String desc) {

        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<JsonObject> call3 = apiInterface.addClassifieds(subject,desc,departId,AppClass.getUserId());
        call3.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e("AddFb:",new Gson().toJson(response.body()));

                swipeRefreshLayout.setRefreshing(false);

                hideLoader();

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            JsonObject model=response.body();
                            MyUtility.showAlertMessage(getActivity(),model.get("Message").getAsString());

                            callApiToGetCategoryCount();


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


    private void callApiToGetCategoryCount() {

        if(!classsifiedCount.isEmpty()){
            classsifiedCount.clear();
         }

        if(!MyUtility.isConnected(getActivity())){
            swipeRefreshLayout.setRefreshing(false);
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ClassifiedCountApiModel> call3 = apiInterface.getMyClassifiedsByCategoryCount(AppClass.getUserId());
        call3.enqueue(new Callback<ClassifiedCountApiModel>() {
            @Override
            public void onResponse(Call<ClassifiedCountApiModel> call, Response<ClassifiedCountApiModel> response) {

                Log.e("Data:",new Gson().toJson(response.body()));

                hideLoader();

                swipeRefreshLayout.setRefreshing(false);

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            ClassifiedCountApiModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null) {

                                    if (model.data.myClassifiedsCountsult.size() > 0) {

                                        for (int i = 0; i < model.data.myClassifiedsCountsult.size(); i++) {
                                            ClassifiedModel item = new ClassifiedModel();
                                            item.setId(model.data.myClassifiedsCountsult.get(i).id+"");
                                            item.setCategoryName(model.data.myClassifiedsCountsult.get(i).categoryName);
                                            item.setCount(model.data.myClassifiedsCountsult.get(i).count+"");
                                            classsifiedCount.add(item);
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
            public void onFailure(Call<ClassifiedCountApiModel> call, Throwable t) {
                call.cancel();
                swipeRefreshLayout.setRefreshing(false);
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }

    private void showData(){

        RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutParams);

        adapter = new ClassifiedCategoryCountAdapter(classsifiedCount,getActivity());
        recyclerView.setAdapter(adapter);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                swipeRefreshLayout.setEnabled(layoutParams.findFirstCompletelyVisibleItemPosition() == 0);

            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                getDetailApiCall(classsifiedCount.get(position).getId()+"");

            }
        }));

        //in.magarpatta
    }


    private void getDetailApiCall(String id) {

        Log.e("ID",id);

        if(!MyUtility.isConnected(getActivity())){
            swipeRefreshLayout.setRefreshing(false);
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ClassifiedApiModel> call3 = apiInterface.getMyClassifiedsByCategory(AppClass.getUserId(),id);
        call3.enqueue(new Callback<ClassifiedApiModel>() {
            @Override
            public void onResponse(Call<ClassifiedApiModel> call, Response<ClassifiedApiModel> response) {

                hideLoader();
                Log.e("SPO:",new Gson().toJson(response.body()));

                swipeRefreshLayout.setRefreshing(false);

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            ClassifiedApiModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null) {

                                    if (model.data.result.size() > 0) {

                                        ArrayList<ClassifiedModel> list=new ArrayList<ClassifiedModel>();

                                        for (int i = 0; i < model.data.result.size(); i++) {

                                            ClassifiedModel item = new ClassifiedModel();
                                            item.setId(model.data.result.get(i).id+"");
                                            item.setSubject(model.data.result.get(i).subject);
                                            item.setDescription(model.data.result.get(i).description);
                                            item.setResponseCount(model.data.result.get(i).responseCount+"");
                                            list.add(item);

                                        }
                                        showDetailedDialog(list);


                                    }else{
                                        MyUtility.showAlertMessage(getActivity(),model.message);

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
            public void onFailure(Call<ClassifiedApiModel> call, Throwable t) {
                call.cancel();
                hideLoader();
                swipeRefreshLayout.setRefreshing(false);
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }



    @Override public void onDestroyView() {
        super.onDestroyView();
    }


    private void showDetailedDialog(final ArrayList<ClassifiedModel> list){

        if(!list.isEmpty()){

            Intent i=new Intent(getActivity(), ClassifiedsByCategoryActivity.class);
            i.putExtra("title","title");
            i.putExtra("list",list);
            i.putExtra("type","my");
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.enter_transition,R.anim.exit_transition);

        }else{

            MyUtility.showAlertMessage(getActivity(),MyUtility.NO_DATA_FOUND);
        }

    }


    private void bindDepartmentData(final ArrayList<DepartmentModel> list){

        final String[]items=new String[list.size()];

        for(int i=0;i<list.size();i++){
            items[i]=list.get(i).getName();
        }

        AlertDialog.Builder ad=new AlertDialog.Builder(getActivity());
        ad.setTitle("Select Department");


        ad.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                selectedDeaprtmentId= list.get(i).getId()+"";
                editDepartmet.setText(list.get(i).getName());
                dialogInterface.dismiss();


            }
        });

        AlertDialog dialog=ad.create();
        dialog.show();

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
