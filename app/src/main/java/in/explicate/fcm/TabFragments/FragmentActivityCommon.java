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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.adapter.ActivityAdapter;
import in.explicate.fcm.adapter.NewsLetterAdapter;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.ActivityAPIModel;
import in.explicate.fcm.apiinterface.models.ActivityDetailAPIModel;
import in.explicate.fcm.apiinterface.models.NewsLetterAPIModel;
import in.explicate.fcm.apiinterface.models.StdProcDetailModel;
import in.explicate.fcm.model.ActivityModel;
import in.explicate.fcm.model.NewsLetterModel;
import in.explicate.fcm.model.StandardProcModel;
import in.explicate.fcm.ui.HomeActivity;
import in.explicate.fcm.util.MyUtility;
import in.explicate.fcm.util.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahesh on 07/12/17.
 */

public class FragmentActivityCommon  extends Fragment {


    private SwipeRefreshLayout swipeRefreshLayout;
    private List<ActivityModel> list;
    private ActivityAdapter adapter;
    private View rootView;
    private RelativeLayout loaderll;

    private TextView tvTitle;




    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fagment_activity_common, container, false);
        list = new ArrayList<>();
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

                callApi();
            }
        });


        FloatingActionButton btnRefresh=(FloatingActionButton)rootView.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callApi();

            }
        });


        callApi();

    }

    private void callApi() {

        if(!list.isEmpty()){
            list.clear();
            tvTitle.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }


        if(!MyUtility.isConnected(getActivity())){
            swipeRefreshLayout.setRefreshing(false);
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ActivityAPIModel> call3 = apiInterface.getCommonActivities();
        call3.enqueue(new Callback<ActivityAPIModel>() {
            @Override
            public void onResponse(Call<ActivityAPIModel> call, Response<ActivityAPIModel> response) {

                Log.e("Data:",new Gson().toJson(response.body()));
                swipeRefreshLayout.setRefreshing(false);


                hideLoader();

                if(response.isSuccessful()){

                    if(response.body()!=null){

                        ActivityAPIModel model=response.body();

                        if(model.status.equalsIgnoreCase("Success")){

                            if(model.data!=null) {

                                if (model.data.result.size() > 0) {


                                    for (int i = 0; i < model.data.result.size(); i++) {

                                        ActivityModel item = new ActivityModel();
                                        item.setId(model.data.result.get(i).id);
                                        item.setSubject(model.data.result.get(i).subject);
                                        item.setDate(model.data.result.get(i).date);
                                        list.add(item);
                                    }

                                    tvTitle.setVisibility(View.VISIBLE);
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


                }else{

                switch (response.code()){

                    case 200:



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

            }

            @Override
            public void onFailure(Call<ActivityAPIModel> call, Throwable t) {
                call.cancel();
                hideLoader();
                swipeRefreshLayout.setRefreshing(false);
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }

    private void showData(){

        RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutParams);

        adapter = new ActivityAdapter(list,getActivity());
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
                getDetailApiCall(list.get(position).getId()+"");
            }
        }));



    }

    private void callApiSearchApi(String text) {


        if(!list.isEmpty()){
            list.clear();
            tvTitle.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }


        if(!MyUtility.isConnected(getActivity())){
            swipeRefreshLayout.setRefreshing(false);
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ActivityAPIModel> call3 = apiInterface.searchCommonActivities(text);
        call3.enqueue(new Callback<ActivityAPIModel>() {
            @Override
            public void onResponse(Call<ActivityAPIModel> call, Response<ActivityAPIModel> response) {

                hideLoader();
                Log.e("Data:",new Gson().toJson(response.body()));

                swipeRefreshLayout.setRefreshing(false);

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            ActivityAPIModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null) {

                                    if (model.data.result.size() > 0) {

                                        if (!list.isEmpty()) {

                                            list.clear();
                                        }

                                        for (int i = 0; i < model.data.result.size(); i++) {

                                            ActivityModel item = new ActivityModel();
                                            item.setId(model.data.result.get(i).id);
                                            item.setSubject(model.data.result.get(i).subject);
                                            item.setDate(model.data.result.get(i).date);
                                            list.add(item);
                                        }

                                        tvTitle.setVisibility(View.VISIBLE);

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
            public void onFailure(Call<ActivityAPIModel> call, Throwable t) {
                call.cancel();
                swipeRefreshLayout.setRefreshing(false);
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }

    private void getDetailApiCall(String text) {


        if(!MyUtility.isConnected(getActivity())){
            swipeRefreshLayout.setRefreshing(false);
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ActivityDetailAPIModel> call3 = apiInterface.getActivityDetails(text);
        call3.enqueue(new Callback<ActivityDetailAPIModel>() {
            @Override
            public void onResponse(Call<ActivityDetailAPIModel> call, Response<ActivityDetailAPIModel> response) {

                Log.e("SPO:",new Gson().toJson(response.body()));
                hideLoader();

                swipeRefreshLayout.setRefreshing(false);

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            ActivityDetailAPIModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null) {

                                    if (model.data.result.size() > 0) {


                                        for (int i = 0; i < model.data.result.size(); i++) {

                                            ActivityModel item = new ActivityModel();
                                            item.setId(model.data.result.get(i).id);
                                            item.setSubject(model.data.result.get(i).subject);
                                            item.setDate(model.data.result.get(i).date);
                                            item.setType(model.data.result.get(i).type);
                                            item.setDetails(model.data.result.get(i).details);
                                            showDetailedDialog(item);


                                        }

                                    }

                                }else {

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
            public void onFailure(Call<ActivityDetailAPIModel> call, Throwable t) {
                call.cancel();
                swipeRefreshLayout.setRefreshing(false);
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }


    private void showDetailedDialog(ActivityModel item){


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_activity_details, null);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(alertLayout);

        dialog.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        final AlertDialog alert=dialog.create();


        TextView lld=(TextView)alertLayout. findViewById(R.id.lldetails);
        lld.setText(item.getSubject());

        TextView textView=(TextView)alertLayout. findViewById(R.id.tvSubject);
        textView.setText(item.getSubject());

        TextView tvDepa=(TextView)alertLayout. findViewById(R.id.tvDeaprtment);
        tvDepa.setText(item.getType());

        TextView tvDate=(TextView)alertLayout. findViewById(R.id.tvDate);
        tvDate.setText(item.getDate());


        TextView tvDetails=(TextView)alertLayout. findViewById(R.id.tvDetails);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            tvDetails.setText(Html.fromHtml(item.getDetails(), Html.FROM_HTML_MODE_COMPACT));
        }else{
            tvDetails.setText(Html.fromHtml(item.getDetails()));
        }

        alert.show();


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
