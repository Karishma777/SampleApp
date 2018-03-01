package in.explicate.fcm.TabFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.adapter.EmergencyContactAdapter;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.EmployeeAPIModel;
import in.explicate.fcm.database.DbHandler;
import in.explicate.fcm.model.EmergencyContact;
import in.explicate.fcm.ui.HomeActivity;
import in.explicate.fcm.util.MyUtility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EmergecyContactFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private List<EmergencyContact> list;
    private EmergencyContactAdapter adapter;
    private View rootView;
    private RelativeLayout loaderll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_emergency_contact, container, false);
        setUpViews();
        return  rootView;
    }

    private void setUpViews() {

        list=new ArrayList<>();

        loaderll=(RelativeLayout)rootView.findViewById(R.id.loaderll);
        loaderll.setVisibility(View.GONE);

        swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                callApi();
            }
        });

        callApi();

      //  callApiToModified();
       // callApiToNewData();

    }

    private void callApi() {



        final DbHandler dbHandler=new DbHandler(getActivity());
        dbHandler.open();
        list=dbHandler.getContacts();
        dbHandler.close();

        if(list.size()==0){


            if(!MyUtility.isConnected(getActivity())){
                swipeRefreshLayout.setRefreshing(false);
                MyUtility.showAlertInternet(getActivity());
                return;
            }


            showLoader();

            APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

            Call<EmployeeAPIModel> call3 = apiInterface.retrieveAllEmergencyContacts();
            call3.enqueue(new Callback<EmployeeAPIModel>() {
                @Override
                public void onResponse(Call<EmployeeAPIModel> call, Response<EmployeeAPIModel> response) {

                    Log.e("Calling",new Gson().toJson(response.code()));

                    hideLoader();


                    swipeRefreshLayout.setRefreshing(false);

                    switch (response.code()){

                        case 200:

                            if(response.body()!=null){

                                EmployeeAPIModel model=response.body();

                                if(model.status.equalsIgnoreCase("Success")){

                                    if(model.data!=null){

                                    if(model.data.result.size() >0){


                                        DbHandler dbHandler1=new DbHandler(getActivity());

                                        for(int i =0;i<model.data.result.size();i++){

                                            EmergencyContact item=new EmergencyContact();
                                            item.setId(model.data.result.get(i).id+"");
                                            item.setName(model.data.result.get(i).name);
                                            item.setEmail(model.data.result.get(i).email);
                                            item.setMobile(model.data.result.get(i).mobile);
                                            item.setDescription(model.data.result.get(i).description);
                                            item.setCreated(model.data.result.get(i).createdOn);
                                            item.setModified(model.data.result.get(i).modifiedOn);

                                            dbHandler1.open();
                                            dbHandler1.insertContactTable(item);
                                            dbHandler1.close();
                                        }

                                        dbHandler1.open();
                                        list=dbHandler1.getContacts();
                                        dbHandler1.close();
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
                public void onFailure(Call<EmployeeAPIModel> call, Throwable t) {
                    call.cancel();
                    hideLoader();
                    swipeRefreshLayout.setRefreshing(false);
                    MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

                }
            });

        }else{

            Log.e("At","Local");
            showData();
        }



    }

    private void showData(){

        RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutParams);

        adapter = new EmergencyContactAdapter(list,getActivity());
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

        callApiToCheckCount();

    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    /***Call API TO Check Count****/

    private void callApiToCheckCount() {


        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<JsonObject> call3 = apiInterface.retrieveTotalEmergencyContactsCount();
        call3.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e("Count",new Gson().toJson(response.body()));

                swipeRefreshLayout.setRefreshing(false);

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){


                            JsonObject jsonObject=response.body();


                            if(jsonObject.get("Status").getAsString().equalsIgnoreCase("Success")){

                                JsonObject data=jsonObject.getAsJsonObject("Data");
                                Integer count=data.get("EmergencyContactsCount").getAsInt();

                                Integer localCount=adapter.getItemCount();

                                if(count > localCount){

                                    Log.e("API","callApiToNewData");
                                    callApiToNewData();

                                }else{
                                    callApiToModified();
                                    Log.e("API",localCount+"");

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
            public void onFailure(Call<JsonObject> call, Throwable t) {
                call.cancel();
                swipeRefreshLayout.setRefreshing(false);
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);
            }
        });
    }

    private void callApiToModified() {

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<EmployeeAPIModel> call3 = apiInterface.retrieveModifiedEmergencyContacts();
        call3.enqueue(new Callback<EmployeeAPIModel>() {
            @Override
            public void onResponse(Call<EmployeeAPIModel> call, Response<EmployeeAPIModel> response) {

                Log.e("Modified",new Gson().toJson(response.body()));

                swipeRefreshLayout.setRefreshing(false);

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            EmployeeAPIModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null) {

                                    if (model.data.modifiedlist.size() > 0) {


                                        DbHandler dbHandler1 = new DbHandler(getActivity());

                                        for (int i = 0; i < model.data.modifiedlist.size(); i++) {


                                            EmergencyContact item = new EmergencyContact();
                                            item.setId(model.data.modifiedlist.get(i).id + "");
                                            item.setName(model.data.modifiedlist.get(i).name);
                                            item.setEmail(model.data.modifiedlist.get(i).email);
                                            item.setMobile(model.data.modifiedlist.get(i).mobile);
                                            item.setDescription(model.data.modifiedlist.get(i).description);
                                            item.setCreated(model.data.modifiedlist.get(i).createdOn);
                                            item.setModified(model.data.modifiedlist.get(i).modifiedOn);

                                            dbHandler1.open();
                                            dbHandler1.updateContact(item);
                                            dbHandler1.close();

                                            //update query

                                        }


                                        dbHandler1.open();
                                        List<EmergencyContact>  list = dbHandler1.getContacts();
                                        dbHandler1.close();

                                        updateList(list);

                                    }
                                }else{

                                    //
                                }



                            }else{

                               // MyUtility.showAlertMessage(getActivity(),MyUtility.NO_DATA_FOUND);
                            }
                        }else{

                           // MyUtility.showAlertMessage(getActivity(),MyUtility.FAILED_TO_GET_DATA);
                        }


                        break;

                    case 500 :

                       // MyUtility.showAlertMessage(getActivity(),MyUtility.ERROR_500);


                        break;


                    case 401:

                      //  MyUtility.showAlertMessage(getActivity(),MyUtility.ERROR_401);

                        break;

                    case 404:

                       // MyUtility.showAlertMessage(getActivity(),MyUtility.ERROR_404);

                        break;

                }
            }

            @Override
            public void onFailure(Call<EmployeeAPIModel> call, Throwable t) {
                call.cancel();
                swipeRefreshLayout.setRefreshing(false);
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });


    }

    private void callApiToNewData() {

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<EmployeeAPIModel> call3 = apiInterface.retrieveNewlyCreatedEmergencyContacts();
        call3.enqueue(new Callback<EmployeeAPIModel>() {
            @Override
            public void onResponse(Call<EmployeeAPIModel> call, Response<EmployeeAPIModel> response) {

                Log.e("New",new Gson().toJson(response.body()));

                callApiToModified();

                swipeRefreshLayout.setRefreshing(false);

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            EmployeeAPIModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null) {

                                    if (model.data.newClist.size() > 0) {

                                        DbHandler dbHandler1 = new DbHandler(getActivity());

                                        for (int i = 0; i < model.data.newClist.size(); i++) {


                                            EmergencyContact item = new EmergencyContact();
                                            item.setId(model.data.newClist.get(i).id + "");
                                            item.setName(model.data.newClist.get(i).name);
                                            item.setEmail(model.data.newClist.get(i).email);
                                            item.setMobile(model.data.newClist.get(i).mobile);
                                            item.setDescription(model.data.newClist.get(i).description);
                                            item.setCreated(model.data.newClist.get(i).createdOn);
                                            item.setModified(model.data.newClist.get(i).modifiedOn);

                                            dbHandler1.open();
                                            dbHandler1.insertContactTable(item);
                                            dbHandler1.close();
                                        }



                                        dbHandler1.open();
                                        List<EmergencyContact> list=dbHandler1.getContacts();
                                        dbHandler1.close();

                                        updateList(list);

                                    }
                                }else{

                                   // MyUtility.showAlertMessage(getActivity(),MyUtility);


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
            public void onFailure(Call<EmployeeAPIModel> call, Throwable t) {
                call.cancel();
                swipeRefreshLayout.setRefreshing(false);
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


    public void updateList(List<EmergencyContact> newlist) {
        list.clear();
        list.addAll(newlist);
        adapter.notifyDataSetChanged();
    }


}
