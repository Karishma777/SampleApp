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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.skoumal.fragmentback.BackFragment;

import java.util.ArrayList;
import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.adapter.CommitteeAdapter;
import in.explicate.fcm.adapter.CommitteeMemberAdapter;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.CommitteApiModel;
import in.explicate.fcm.apiinterface.models.CommitteeMembarApi;
import in.explicate.fcm.apimodels.CommitteeModel;
import in.explicate.fcm.model.CommitteeMemberModel;
import in.explicate.fcm.ui.HomeActivity;
import in.explicate.fcm.util.MyUtility;
import in.explicate.fcm.util.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by iFocus on 27-10-2015.
 */
public class ComitteeFragment extends Fragment {


    private  SwipeRefreshLayout swipeRefreshLayout;
    private List<CommitteeModel> list;
    private CommitteeAdapter adapter;
    private List<CommitteeMemberModel> listMembers;

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
        rootView= inflater.inflate(R.layout.fragment_committee, container, false);
        listMembers=new ArrayList<>();
        list = new ArrayList<>();
        setUpViews();

        onBackPressed();
        
        return  rootView;
    }

    private void onBackPressed() {

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    getActivity();
                    return true;
                }
                return false;
            }
        } );
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

            Call<CommitteApiModel> call3 = apiInterface.getCommittees();
            call3.enqueue(new Callback<CommitteApiModel>() {
                @Override
                public void onResponse(Call<CommitteApiModel> call, Response<CommitteApiModel> response) {

                    hideLoader();

                    swipeRefreshLayout.setRefreshing(false);


                    switch (response.code()){

                        case 200:

                            if(response.body()!=null){

                                CommitteApiModel model=response.body();

                                if(model.status.equalsIgnoreCase("Success")){

                                    if(model.data!=null) {

                                        if (model.data.result.size() > 0) {


                                            for (int i = 0; i < model.data.result.size(); i++) {

                                                CommitteeModel item = new CommitteeModel();
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
                public void onFailure(Call<CommitteApiModel> call, Throwable t) {
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

            adapter = new CommitteeAdapter(list,getActivity());
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


    private void getDetailApiCall(String text, final String committee) {

        Log.e("id",text);

        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();
        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<CommitteeMembarApi> call3 = apiInterface.getCommitteeMembers(text);
        call3.enqueue(new Callback<CommitteeMembarApi>() {
            @Override
            public void onResponse(Call<CommitteeMembarApi> call, Response<CommitteeMembarApi> response) {
                Log.e("committess:",new Gson().toJson(response.body()));
                hideLoader();

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            CommitteeMembarApi model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data.result.size() >0){

                                    if(!listMembers.isEmpty()){
                                        listMembers.clear();
                                    }

                                    for(int i =0;i<model.data.result.size();i++){

                                        CommitteeMemberModel item=new CommitteeMemberModel();
                                        item.setId(model.data.result.get(i).id);
                                        item.setName(model.data.result.get(i).name);
                                        item.setInfra(model.data.result.get(i).infra);
                                        listMembers.add(item);

                                    }

                                    showMemberDialog(listMembers,committee);
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
            public void onFailure(Call<CommitteeMembarApi> call, Throwable t) {
                call.cancel();
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }

    private void showMemberDialog(List<CommitteeMemberModel> listMembers,String comiitee) {

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
        title.setText("Members - "+comiitee);
        title.setBackgroundResource(R.color.toolbarcommittee);



        RecyclerView recyclerView=(RecyclerView)alertLayout.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutParams);

        CommitteeMemberAdapter adapter = new CommitteeMemberAdapter(listMembers,getActivity());
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
