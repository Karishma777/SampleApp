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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.adapter.FeedbackMagarpattaAdapter;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.CommitteApiModel;
import in.explicate.fcm.apiinterface.models.DepartmentApiDetail;
import in.explicate.fcm.apiinterface.models.FeedbackAPIModel;
import in.explicate.fcm.apiinterface.models.FeedbackCommitteeAPIModel;
import in.explicate.fcm.apiinterface.models.FeedbackCommitteeDetailsAPIModel;
import in.explicate.fcm.apiinterface.models.FeedbackDetailsAPIModel;
import in.explicate.fcm.apiinterface.models.NewsLetterAPIModel;
import in.explicate.fcm.application.AppClass;
import in.explicate.fcm.model.DepartmentModel;
import in.explicate.fcm.model.FeedbackModel;
import in.explicate.fcm.model.NewsLetterModel;
import in.explicate.fcm.ui.HomeActivity;
import in.explicate.fcm.util.MyUtility;
import in.explicate.fcm.util.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahesh on 09/12/17.
 */

public class FragmentFeedbackCommittee  extends Fragment implements DatePickerDialog.OnDateSetListener {


    private SwipeRefreshLayout swipeRefreshLayout;
    private List<FeedbackModel> list;
    private FeedbackMagarpattaAdapter adapter;
    private View rootView;
    private FloatingActionButton btnAdd;
    private RelativeLayout loaderll;
    private String selectedDeaprtmentId="";
    private boolean isSearchByDate=false;

    private int dateFor=0;

    private EditText dateFrom,dateTo, editDepartmet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.suggestion_to_magarpatta, container, false);
        list=new ArrayList<>();
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

                callApiretrieveAllFeedbacks();
            }
        });


        FloatingActionButton btnRefresh=(FloatingActionButton)rootView.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                callApiretrieveAllFeedbacks();
            }
        });


        btnAdd=(FloatingActionButton)rootView.findViewById(R.id.btnAdd);
        btnAdd.setVisibility(View.GONE);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // add feedback
                // addFeedbackDialog();

                callApiToGetDepartment();

            }
        });

        callApiretrieveAllFeedbacks();

        callApigetFeedbackStatus();


    }

    private void callApigetShowHideAddButton() {

        if(!MyUtility.isConnected(getActivity())){
            swipeRefreshLayout.setRefreshing(false);
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<JsonObject> call3 = apiInterface.isCommitteeFeedbackAllowed(AppClass.getUserId());
        call3.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e("Data:",new Gson().toJson(response.body()));

                swipeRefreshLayout.setRefreshing(false);

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            JsonObject model=response.body();

                            if(model.get("Status").getAsString().equalsIgnoreCase("Success")){

                                // show add button

                                btnAdd.setVisibility(View.VISIBLE);


                            }else{

                                MyUtility.showAlertMessage(getActivity(),model.get("Message").getAsString());
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


    private void callApiretrieveAllFeedbacks() {

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
        Call<FeedbackCommitteeAPIModel> call3 = apiInterface.retrieveAllCommitteeFeedbacks(AppClass.getUserId());
        call3.enqueue(new Callback<FeedbackCommitteeAPIModel>() {
            @Override
            public void onResponse(Call<FeedbackCommitteeAPIModel> call, Response<FeedbackCommitteeAPIModel> response) {

                Log.e("Data:",new Gson().toJson(response.body()));

                hideLoader();

                swipeRefreshLayout.setRefreshing(false);

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            FeedbackCommitteeAPIModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null){

                                    if(model.data.result.size() >0){


                                        for(int i =0;i<model.data.result.size();i++){

                                            FeedbackModel item=new FeedbackModel();
                                            item.setId(model.data.result.get(i).id);
                                            item.setSubject(model.data.result.get(i).subject);
                                            item.setDepartment(model.data.result.get(i).committeeName);
                                            item.setFeedbackDate(model.data.result.get(i).date);
                                            list.add(item);
                                        }

                                        showData();
                                    }

                                }else{

                                    //  MyUtility.showAlertMessage(getActivity(),model.message);

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
            public void onFailure(Call<FeedbackCommitteeAPIModel> call, Throwable t) {
                call.cancel();
                hideLoader();
                swipeRefreshLayout.setRefreshing(false);
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }


    private void callApigetFeedbackStatus() {



        if(!MyUtility.isConnected(getActivity())){
            swipeRefreshLayout.setRefreshing(false);
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<JsonObject> call3 = apiInterface.getCommitteeFeedbackStatus(AppClass.getUserId());
        call3.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e("Data:",new Gson().toJson(response.body()));

                callApigetShowHideAddButton();


                swipeRefreshLayout.setRefreshing(false);

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            JsonObject model=response.body();

                            if(model.get("Status").getAsString().equalsIgnoreCase("Success")){

                                if(model.getAsJsonObject("Data").get("FeedbackStatus").getAsString().length() >0){


                                    MyUtility.showAlertMessage(getActivity(),model.getAsJsonObject("Data").get("FeedbackStatus").getAsString());

                                }



                            }else{

                                MyUtility.showAlertMessage(getActivity(),model.get("Message").getAsString());
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


    private void showData(){

        RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutParams);

        adapter = new FeedbackMagarpattaAdapter(list,getActivity());
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


        if(!MyUtility.isConnected(getActivity())){
            swipeRefreshLayout.setRefreshing(false);
            MyUtility.showAlertInternet(getActivity());
            return;
        }
        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<FeedbackCommitteeAPIModel> call3 = apiInterface.searchMyAllCommitteeFeedbacks(AppClass.getUserId(),text);
        call3.enqueue(new Callback<FeedbackCommitteeAPIModel>() {
            @Override
            public void onResponse(Call<FeedbackCommitteeAPIModel> call, Response<FeedbackCommitteeAPIModel> response) {
                Log.e("Data:",new Gson().toJson(response.body()));
                hideLoader();
                swipeRefreshLayout.setRefreshing(false);

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            FeedbackCommitteeAPIModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null){

                                    if(model.data.result.size() >0){

                                        if(!list.isEmpty()){

                                            list.clear();
                                        }

                                        for(int i =0;i<model.data.result.size();i++){


                                            FeedbackModel item=new FeedbackModel();
                                            item.setId(model.data.result.get(i).id);
                                            item.setSubject(model.data.result.get(i).subject);
                                            item.setDepartment(model.data.result.get(i).committeeName);
                                            item.setFeedbackDate(model.data.result.get(i).date);
                                            list.add(item);
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
            public void onFailure(Call<FeedbackCommitteeAPIModel> call, Throwable t) {
                call.cancel();
                hideLoader();
                swipeRefreshLayout.setRefreshing(false);
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }


    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_feedback, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:

                showSearchDialog();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
    }

    private void getDetailApiCall(String text) {


        if(!MyUtility.isConnected(getActivity())){
            swipeRefreshLayout.setRefreshing(false);
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<FeedbackCommitteeDetailsAPIModel> call3 = apiInterface.retrieveCommitteeFeedbackDetails(text,AppClass.getUserId());
        call3.enqueue(new Callback<FeedbackCommitteeDetailsAPIModel>() {
            @Override
            public void onResponse(Call<FeedbackCommitteeDetailsAPIModel> call, Response<FeedbackCommitteeDetailsAPIModel> response) {

                Log.e("SPO:",new Gson().toJson(response.body()));

                swipeRefreshLayout.setRefreshing(false);

                hideLoader();

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            FeedbackCommitteeDetailsAPIModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null) {

                                    if (model.data.result.size() > 0) {

                                        for (int i = 0; i < model.data.result.size(); i++) {

                                            FeedbackModel item = new FeedbackModel();
                                            item.setId(model.data.result.get(i).id);
                                            item.setSubject(model.data.result.get(i).subject);
                                            item.setDepartment(model.data.result.get(i).committeeName);
                                            item.setFeedbackDate(model.data.result.get(i).date);
                                            item.setReply(model.data.result.get(i).reply);
                                            item.setReplydate(model.data.result.get(i).replyDate);
                                            showDetailedDialog(item);


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
            public void onFailure(Call<FeedbackCommitteeDetailsAPIModel> call, Throwable t) {
                call.cancel();
                swipeRefreshLayout.setRefreshing(false);
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }

    private void showDetailedDialog(FeedbackModel item){


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_feedback_details, null);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(alertLayout);

        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });

        final AlertDialog alert=dialog.create();


        TextView textView=(TextView)alertLayout. findViewById(R.id.tvSubject);
        textView.setText(item.getSubject());

        TextView tvDepa=(TextView)alertLayout. findViewById(R.id.tvDeaprtment);
        tvDepa.setText(item.getDepartment());


        TextView tvDetails=(TextView)alertLayout. findViewById(R.id.tvDetails);
        tvDetails.setText(item.getDescription());

        TextView tvDate=(TextView)alertLayout. findViewById(R.id.tvDate);
        tvDate.setText(item.getFeedbackDate());

        TextView tvReply=(TextView)alertLayout. findViewById(R.id.tvReply);

        if(item.getReply().length()>0){
            tvReply.setText(item.getReply());
        }else{
            tvReply.setText("No reply yet");
        }

        TextView tvReplyDate=(TextView)alertLayout. findViewById(R.id.tvReplyDate);
        tvReplyDate.setText(item.getReplydate());


        alert.show();


    }

    private void addFeedbackDialog(final ArrayList<DepartmentModel> departments){


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_add_feedback, null);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(alertLayout);


        final AlertDialog alert=dialog.create();


        final EditText esubject=(EditText)alertLayout.findViewById(R.id.editsubject);


        TextView tvTitle=(TextView)alertLayout.findViewById(R.id.tvTitle);
        tvTitle.setText("ADD COMMITTEE FEEDBACK");


        editDepartmet = (EditText) alertLayout. findViewById(R.id.editDepartment);
        editDepartmet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bindDepartmentData(departments);
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

                    callApiAddFeedback(esubject.getText().toString(),selectedDeaprtmentId,edesc.getText().toString());

                }
            }
        });

        alert.show();


    }

    private void bindDepartmentData(final ArrayList<DepartmentModel> listDe){

        final String[]items=new String[listDe.size()];

        for(int i=0;i<listDe.size();i++){
            items[i]=listDe.get(i).getName();
        }

        AlertDialog.Builder ad=new AlertDialog.Builder(getActivity());
        ad.setTitle("Select Department");


        ad.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                selectedDeaprtmentId= listDe.get(i).getId()+"";
                Log.e("ID",selectedDeaprtmentId);
                editDepartmet.setText(listDe.get(i).getName());
                dialogInterface.dismiss();
            }
        });

        AlertDialog dialog=ad.create();
        dialog.show();

    }



    private void callApiAddFeedback(String subject,String departId,String desc) {

        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<JsonObject> call3 = apiInterface.addCommitteeFeedback(AppClass.getUserId(),subject,desc,departId);
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

                            callApiretrieveAllFeedbacks();

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
    private void showLoader(){
        //  loaderll.setVisibility(View.VISIBLE);

        ((HomeActivity)getActivity()).showPrgressBar();

    }

    private void hideLoader(){
        // loaderll.setVisibility(View.GONE);

        ((HomeActivity)getActivity()).hideProgressBar();
    }


    /*****API TO GET DEPARTMENTS*****/

    private void callApiToGetDepartment() {


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

                Log.e("Data:",new Gson().toJson(response.body()));

                hideLoader();
                swipeRefreshLayout.setRefreshing(false);

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            CommitteApiModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null) {

                                    if (model.data.result.size() > 0) {

                                        ArrayList departmentModelList = new ArrayList<>();

                                        for (int i = 0; i < model.data.result.size(); i++) {

                                            DepartmentModel item = new DepartmentModel();
                                            item.setId(model.data.result.get(i).id);
                                            item.setName(model.data.result.get(i).name);
                                            departmentModelList.add(item);
                                        }

                                        addFeedbackDialog(departmentModelList);


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





    private void showSearchDialog(){


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_feedback_search, null);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(alertLayout);

        final AlertDialog alert=dialog.create();

        dateFrom=(EditText) alertLayout. findViewById(R.id.dateFrom);
        dateTo=(EditText)alertLayout. findViewById(R.id.dateTo);
        final EditText string=(EditText)alertLayout. findViewById(R.id.editString);

        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dateFor=1;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        FragmentFeedbackCommittee.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                Calendar calendar = Calendar.getInstance();
                dpd.setMaxDate(calendar);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");


            }
        });

        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateFor=2;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        FragmentFeedbackCommittee.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                Calendar calendar = Calendar.getInstance();
                dpd.setMaxDate(calendar);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");



            }
        });

        RadioGroup radioGroup=(RadioGroup)alertLayout.findViewById(R.id.radiogroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked)
                {
                    if(checkedRadioButton.getText().toString().contains("Date")){

                        string.setVisibility(View.GONE);

                        dateFrom.setVisibility(View.VISIBLE);
                        dateTo.setVisibility(View.VISIBLE);

                        isSearchByDate=true;

                    }else{

                        dateFrom.setVisibility(View.GONE);
                        dateTo.setVisibility(View.GONE);

                        string.setVisibility(View.VISIBLE);

                        isSearchByDate=false;


                    }
                }
            }
        });

        Button btnSearch=(Button) alertLayout. findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isSearchByDate){

                    if(dateFrom.getText().toString().equalsIgnoreCase("")){

                        dateFrom.setError("Select Date From");
                    }else if(dateTo.getText().toString().equalsIgnoreCase("")){

                        dateTo.setError("Select Date From");
                    }else{

                        alert.dismiss();
                        callApiSearchByDateApi(dateFrom.getText().toString(),dateTo.getText().toString());
                    }

                }else {

                    if (string.getText().toString().equalsIgnoreCase("")) {

                        string.setError("Enter String");
                    } else{

                        alert.dismiss();
                        callApiSearchApi(string.getText().toString());


                    }
                }
            }
        });


        alert.show();
    }

    private void callApiSearchByDateApi(String from,String to) {


        Log.e("Data:",AppClass.getUserId()+from+to);

        if(!MyUtility.isConnected(getActivity())){
            swipeRefreshLayout.setRefreshing(false);
            MyUtility.showAlertInternet(getActivity());
            return;
        }
        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<FeedbackCommitteeAPIModel> call3 = apiInterface.searchMyAllCommitteeFeedbacksByDate(AppClass.getUserId(),from,to);
        call3.enqueue(new Callback<FeedbackCommitteeAPIModel>() {
            @Override
            public void onResponse(Call<FeedbackCommitteeAPIModel> call, Response<FeedbackCommitteeAPIModel> response) {

                Log.e("Data:",new Gson().toJson(response.body()));

                hideLoader();

                swipeRefreshLayout.setRefreshing(false);

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            FeedbackCommitteeAPIModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if( model.data!=null ) {

                                    if (model.data.result.size() > 0) {

                                        if (!list.isEmpty()) {
                                            list.clear();
                                        }
                                        for (int i = 0; i < model.data.result.size(); i++) {

                                            FeedbackModel item = new FeedbackModel();
                                            item.setId(model.data.result.get(i).id);
                                            item.setSubject(model.data.result.get(i).subject);
                                            item.setDepartment(model.data.result.get(i).committeeName);
                                            item.setFeedbackDate(model.data.result.get(i).date);
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
            public void onFailure(Call<FeedbackCommitteeAPIModel> call, Throwable t) {
                call.cancel();
                hideLoader();
                swipeRefreshLayout.setRefreshing(false);
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = (com.wdullaer.materialdatetimepicker.date.DatePickerDialog)getActivity().getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if(dateFor==1){

            dateFrom.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);

        }else if(dateFor==2){

            dateTo.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);

        }
    }

}


