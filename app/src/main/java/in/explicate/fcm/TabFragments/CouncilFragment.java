package in.explicate.fcm.TabFragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.adapter.CounsilAdapter;
import in.explicate.fcm.adapter.CounsilMemberAdapter;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.CounsilApiModel;
import in.explicate.fcm.apiinterface.models.CounsilMemberApiModel;
import in.explicate.fcm.model.CounsilMemberModel;
import in.explicate.fcm.model.CounsilModel;
import in.explicate.fcm.ui.HomeActivity;
import in.explicate.fcm.util.MyUtility;
import in.explicate.fcm.util.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by iFocus on 27-10-2015.
 */
public class CouncilFragment extends Fragment {


    private SwipeRefreshLayout swipeRefreshLayout;
    private List<CounsilModel> list;
    private CounsilAdapter adapter;
    private List<CounsilMemberModel> listMembers;
    private View rootView;
    private RelativeLayout loaderll;
    private TextView tvTitle;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_council, container, false);
        listMembers=new ArrayList<>();
        list=new ArrayList<>();
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
        }


        if(!MyUtility.isConnected(getActivity())){
            swipeRefreshLayout.setRefreshing(false);
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<CounsilApiModel> call3 = apiInterface.getCouncils();
        call3.enqueue(new Callback<CounsilApiModel>() {
            @Override
            public void onResponse(Call<CounsilApiModel> call, Response<CounsilApiModel> response) {

                hideLoader();

                swipeRefreshLayout.setRefreshing(false);


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            CounsilApiModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null){

                                if(model.data.result.size() >0){


                                    for(int i =0;i<model.data.result.size();i++){

                                        CounsilModel item=new CounsilModel();
                                        item.setId(model.data.result.get(i).id);
                                        item.setName(model.data.result.get(i).name);
                                        item.setDesc(model.data.result.get(i).description);
                                        item.setEmail(model.data.result.get(i).email);
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
            public void onFailure(Call<CounsilApiModel> call, Throwable t) {
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

        adapter = new CounsilAdapter(list,getActivity());
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

                getDetailApiCall(list.get(position).getId()+"",list.get(position).getName());

            }
        }));

    }


    private void getDetailApiCall(String text, final String council) {
        Log.e("ID",text);

        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<CounsilMemberApiModel> call3 = apiInterface.getCouncilMembers(text);
        call3.enqueue(new Callback<CounsilMemberApiModel>() {
            @Override
            public void onResponse(Call<CounsilMemberApiModel> call, Response<CounsilMemberApiModel> response) {

                hideLoader();
                Log.e("SPO:",new Gson().toJson(response.body()));

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            CounsilMemberApiModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null){


                                if(model.data.result.size() >0){

                                    if(!listMembers.isEmpty()){
                                        listMembers.clear();
                                    }

                                    for(int i =0;i<model.data.result.size();i++){

                                        CounsilMemberModel item=new CounsilMemberModel();
                                        item.setId(model.data.result.get(i).id);
                                        item.setName(model.data.result.get(i).name);
                                        item.setInfra(model.data.result.get(i).infra);
                                        listMembers.add(item);

                                    }

                                    showMemberDialog(listMembers,council);
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
            public void onFailure(Call<CounsilMemberApiModel> call, Throwable t) {
                call.cancel();
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }

    private void showMemberDialog(List<CounsilMemberModel> listMembers,String council) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_committee_members, null);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(alertLayout);

        dialog.setPositiveButton("BACK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        final AlertDialog alert=dialog.create();

        TextView title=(TextView)alertLayout.findViewById(R.id.tvTitle);
        title.setText("Members - "+council);
        title.setBackgroundResource(R.color.toolbarcouncil);

        RecyclerView recyclerView=(RecyclerView)alertLayout.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutParams);

        CounsilMemberAdapter adapter = new CounsilMemberAdapter(listMembers,getActivity());
        recyclerView.setAdapter(adapter);
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



}
