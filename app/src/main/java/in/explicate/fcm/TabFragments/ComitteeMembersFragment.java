package in.explicate.fcm.TabFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.adapter.CommitteeAdapter;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.CommitteApiModel;
import in.explicate.fcm.apimodels.CommitteeModel;
import in.explicate.fcm.util.MyUtility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by iFocus on 27-10-2015.
 */
public class ComitteeMembersFragment extends Fragment {


    private  SwipeRefreshLayout swipeRefreshLayout;

    private List<CommitteeModel> list;
    private CommitteeAdapter adapter;

    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fagment_comittee, container, false);
        setUpViews();
        return  rootView;
    }

    private void setUpViews() {

        swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                callApi();
            }
        });

        callApi();


    }

    private void callApi() {


            if(!MyUtility.isConnected(getActivity())){
                MyUtility.showAlertInternet(getActivity());
                return;
            }

            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

            Call<CommitteApiModel> call3 = apiInterface.getCommittees();
            call3.enqueue(new Callback<CommitteApiModel>() {
                @Override
                public void onResponse(Call<CommitteApiModel> call, Response<CommitteApiModel> response) {



                    switch (response.code()){

                        case 200:

                            if(response.body()!=null){

                                CommitteApiModel model=response.body();

                                if(model.status.equalsIgnoreCase("Success")){

                                    if(model.data.result.size() >0){

                                        list=new ArrayList<CommitteeModel>();

                                        for(int i =0;i<model.data.result.size();i++){

                                            CommitteeModel item=new CommitteeModel();
                                            item.setId(model.data.result.get(i).id);
                                            item.setName(model.data.result.get(i).name);
                                            item.setDesc(model.data.result.get(i).description);
                                            item.setEmail(model.data.result.get(i).email);

                                            list.add(item);
                                        }

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

                    }


                }

                @Override
                public void onFailure(Call<CommitteApiModel> call, Throwable t) {
                    call.cancel();
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


    }


    @Override public void onDestroyView() {
        super.onDestroyView();
    }

}
