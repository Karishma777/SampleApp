package in.explicate.fcm.TabFragments;

import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.adapter.StandardProcAdapter;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.DepartmentApiDetail;
import in.explicate.fcm.apiinterface.models.StdProcDetailModel;
import in.explicate.fcm.model.DepartmentModel;
import in.explicate.fcm.model.StandardProcModel;
import in.explicate.fcm.ui.HomeActivity;
import in.explicate.fcm.util.MyUtility;
import in.explicate.fcm.util.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentStandartProcedure extends Fragment {


    private List<DepartmentModel> departmentModelList;
    private List<StandardProcModel> list;
    private StandardProcAdapter adapter;
    private View rootView;
    private Integer selectedId;
    private RelativeLayout loaderll;
    private EditText editDepartment;
    private  SwipeRefreshLayout swipeRefreshLayout;

    private TextView tvTitle;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_sproc, container, false);
        list=new ArrayList<>();
        departmentModelList=new ArrayList<>();
        setUpViews();
        return  rootView;
    }

    private void setUpViews() {

        loaderll=(RelativeLayout)rootView.findViewById(R.id.loaderll);
        loaderll.setVisibility(View.GONE);

        tvTitle=(TextView)rootView.findViewById(R.id.tvTitle);


        swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                callApiToGetDepartment();
            }
        });


        FloatingActionButton btnRefresh=(FloatingActionButton)rootView.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callApiToGetDepartment();

            }
        });


        editDepartment=(EditText)rootView.findViewById(R.id.editDepartment);
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

        if(!list.isEmpty()){
            list.clear();
            tvTitle.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }

        editDepartment.setText("Click here to select Department");

        if(!MyUtility.isConnected(getActivity())){
            swipeRefreshLayout.setRefreshing(false);
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<DepartmentApiDetail> call3 = apiInterface.getDepartments();
        call3.enqueue(new Callback<DepartmentApiDetail>() {
            @Override
            public void onResponse(Call<DepartmentApiDetail> call, Response<DepartmentApiDetail> response) {

                Log.e("Data:",new Gson().toJson(response.body()));
                swipeRefreshLayout.setRefreshing(false);
                hideLoader();


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            DepartmentApiDetail model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data.result.size() >0){


                                    for(int i =0;i<model.data.result.size();i++){

                                        DepartmentModel item=new DepartmentModel();
                                        item.setId(model.data.result.get(i).id);
                                        item.setName(model.data.result.get(i).name);
                                        departmentModelList.add(item);
                                    }


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
            public void onFailure(Call<DepartmentApiDetail> call, Throwable t) {
                call.cancel();
                hideLoader();
                swipeRefreshLayout.setRefreshing(false);
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }

    private void bindDepartmentData(){

        final String[]items=new String[departmentModelList.size()];

        for(int i=0;i<departmentModelList.size();i++){
            items[i]=departmentModelList.get(i).getName();
        }

        AlertDialog.Builder ad=new AlertDialog.Builder(getActivity());
        ad.setTitle("Select Department");

       /* ad.setSingleChoiceItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });
        */

        ad.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                selectedId= departmentModelList.get(i).getId();
                editDepartment.setText(departmentModelList.get(i).getName());
                callApi(selectedId+"",departmentModelList.get(i).getName());
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

        adapter = new StandardProcAdapter(list,getActivity());
        recyclerView.setAdapter(adapter);



        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                getDetailApiCall(list.get(position).getId()+"");

            }
        }));

        //in.magarpatta
    }


    private void getDetailApiCall(String text) {


        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<StdProcDetailModel> call3 = apiInterface.getStandardProcedureDetailsById(text);
        call3.enqueue(new Callback<StdProcDetailModel>() {
            @Override
            public void onResponse(Call<StdProcDetailModel> call, Response<StdProcDetailModel> response) {

                hideLoader();
                Log.e("SPO:",new Gson().toJson(response.body()));


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            StdProcDetailModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null) {

                                    if (model.data.result.size() > 0) {

                                        for (int i = 0; i < model.data.result.size(); i++) {

                                            StandardProcModel item = new StandardProcModel();
                                            item.setId(model.data.result.get(i).id);
                                            item.setName(model.data.result.get(i).subject);
                                            item.setDetails(model.data.result.get(i).details);
                                            item.setDepartment(model.data.result.get(i).department);
                                            showDetailedDialog(item);


                                        }

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
            public void onFailure(Call<StdProcDetailModel> call, Throwable t) {
                call.cancel();
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }



    private void callApiSearchApi(String text) {

        editDepartment.setText("Click here to Select Department");


        if(!list.isEmpty()){

            list.clear();
            tvTitle.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }

        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<StdProcDetailModel> call3 = apiInterface.getStandardProceduresBySearch(text);
        call3.enqueue(new Callback<StdProcDetailModel>() {
            @Override
            public void onResponse(Call<StdProcDetailModel> call, Response<StdProcDetailModel> response) {

                Log.e("SPO:",new Gson().toJson(response.body()));

                hideLoader();


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            StdProcDetailModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data.result.size() >0){

                                    if(!list.isEmpty()){
                                        list.clear();
                                    }

                                    for(int i =0;i<model.data.result.size();i++){

                                        StandardProcModel item=new StandardProcModel();
                                        item.setId(model.data.result.get(i).id);
                                        item.setName(model.data.result.get(i).subject);
                                        list.add(item);
                                    }

                                    tvTitle.setVisibility(View.VISIBLE);
                                    tvTitle.setText("Found Standard Procedures");

                                    showData();
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
            public void onFailure(Call<StdProcDetailModel> call, Throwable t) {
                call.cancel();
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }



    @Override public void onDestroyView() {
        super.onDestroyView();
    }


    private void callApi(String id, final String name) {

        if(!list.isEmpty()){

            list.clear();
            tvTitle.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }

        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<StdProcDetailModel> call3 = apiInterface.getStandardProceduresByDepartment(id);
        call3.enqueue(new Callback<StdProcDetailModel>() {
            @Override
            public void onResponse(Call<StdProcDetailModel> call, Response<StdProcDetailModel> response) {

                Log.e("SPO:",new Gson().toJson(response.body()));

                hideLoader();


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            StdProcDetailModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null)
                                {

                                if(model.data.result.size() >0){

                                    for(int i =0;i<model.data.result.size();i++){

                                        StandardProcModel item=new StandardProcModel();
                                        item.setId(model.data.result.get(i).id);
                                        item.setName(model.data.result.get(i).subject);
                                        list.add(item);
                                    }

                                    tvTitle.setVisibility(View.VISIBLE);
                                    tvTitle.setText("Standard Procedures of "+name);
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
            public void onFailure(Call<StdProcDetailModel> call, Throwable t) {
                call.cancel();
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });


    }

    private void showDetailedDialog(StandardProcModel item){


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_stand_details, null);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(alertLayout);

        dialog.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        final AlertDialog alert=dialog.create();

        TextView lldetails=(TextView)alertLayout. findViewById(R.id.lldetails);
        lldetails.setText(item.getName());


        TextView textView=(TextView)alertLayout. findViewById(R.id.tvSubject);
        textView.setText(item.getName());

        TextView tvDepa=(TextView)alertLayout. findViewById(R.id.tvDeaprtment);
        tvDepa.setText(item.getDepartment());


        TextView tvDetails=(TextView)alertLayout. findViewById(R.id.tvDetails);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            tvDetails.setText(Html.fromHtml(item.getDetails(), Html.FROM_HTML_MODE_COMPACT));
        }else{
            tvDetails.setText(Html.fromHtml(item.getDetails()));
        }


        alert.show();


    }

    private void showLoader(){
        //  loaderll.setVisibility(View.VISIBLE);

        ((HomeActivity)getActivity()).showPrgressBar();

    }

    private void hideLoader(){
        // loaderll.setVisibility(View.GONE);

        ((HomeActivity)getActivity()).hideProgressBar();
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setTitle("Search ...");
        SearchView sv = new SearchView(((HomeActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, sv);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                System.out.println("search query submit");

                if(query.length()!=0){

                    callApiSearchApi(query);


                }else{

                    MyUtility.showAlertMessage(getActivity(),"Enter something to search");
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("tap");
                return false;
            }
        });
    }


}
