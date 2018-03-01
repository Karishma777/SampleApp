package in.explicate.fcm.TabFragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.explicate.fcm.MainActivity;
import in.explicate.fcm.R;
import in.explicate.fcm.adapter.StandardProcAdapter;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.ClubHouseAPIModel;
import in.explicate.fcm.apiinterface.models.ClubhouseEventApiModel;
import in.explicate.fcm.apiinterface.models.DepartmentApiDetail;
import in.explicate.fcm.application.AppClass;
import in.explicate.fcm.model.ClubHouseModel;
import in.explicate.fcm.model.DepartmentModel;
import in.explicate.fcm.ui.HomeActivity;
import in.explicate.fcm.util.MyUtility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by iFocus on 27-10-2015.
 */
public class FragmentClubhouse extends Fragment implements DatePickerDialog.OnDateSetListener {


    private List<ClubHouseModel> departmentModelList;
    private StandardProcAdapter adapter;
    private View rootView;
    private String selectedId="",selectedEventTypeId="";
    private RelativeLayout loaderll;
    private EditText editClubhouse,editDate;

    private EditText editDepartmet;

    private Button btnBook;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_clubhouse_availabilty, container, false);
        departmentModelList=new ArrayList<>();
        setUpViews();
        return  rootView;
    }

    private void setUpViews() {

        loaderll=(RelativeLayout)rootView.findViewById(R.id.loaderll);
        loaderll.setVisibility(View.GONE);

        editClubhouse=(EditText)rootView.findViewById(R.id.editClubHouse);
        editClubhouse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction()==MotionEvent.ACTION_UP){
                    if(departmentModelList.size()>0){
                        btnBook.setVisibility(View.GONE);
                        bindDepartmentData();
                    }
                }
                return false;
            }
        });



        editDate=(EditText)rootView.findViewById(R.id.editDate);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        FragmentClubhouse.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                Calendar calendar = Calendar.getInstance();
                dpd.setMinDate(calendar);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

            }
        });



        Button btnSearch=(Button)rootView.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(selectedId.equalsIgnoreCase("")){

                    MyUtility.showToast("Select Clubhouse",getActivity());

                }else if(editDate.getText().toString().equalsIgnoreCase("")){

                    MyUtility.showToast("Select Date",getActivity());

                }else{

                    callApiToGetStatus(selectedId,editDate.getText().toString());
                }
            }
        });

        btnBook=(Button)rootView.findViewById(R.id.btnBook);
        btnBook.setVisibility(View.GONE);
        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callApiToGetEvents();

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


                selectedId= departmentModelList.get(i).getId()+"";
                editClubhouse.setText(departmentModelList.get(i).getInfra());
                dialogInterface.dismiss();


            }
        });

        AlertDialog dialog=ad.create();
        dialog.show();

    }

    private void callApiToGetStatus(String id,String date) {

        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<JsonObject> call3 = apiInterface.retrieveAvailableClubHouse(date,id);
        call3.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e("Count",new Gson().toJson(response.body()));

                hideLoader();

                switch (response.code()){

                    case 200:

                        if(response.body()!=null) {


                            try {

                                JsonObject jsonObject = response.body();

                                if (jsonObject.get("Status").getAsString().equalsIgnoreCase("Success")) {

                                    Log.e("AT","Status");

                                    if (jsonObject.getAsJsonObject("Data") != null) {

                                        Log.e("AT","Data");

                                        JsonArray jsonArray = jsonObject.getAsJsonObject("Data").get("ClubhouseDetails").getAsJsonArray();


                                        if (jsonArray != null)

                                            Log.e("AT","jsonArray");

                                            for (int i = 0; i < jsonArray.size(); i++) {
                                                JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();

                                                Log.e("AT","jsonObject1");

                                                if (jsonObject1 != null) {

                                                    if (jsonObject1.get("Status").getAsBoolean()) {

                                                        Log.e("AT","Status");
                                                        //show booking dialog

                                                       btnBook.setVisibility(View.VISIBLE);



                                                    } else {

                                                        MyUtility.showAlertMessage(getActivity(), jsonObject1.get("AvailabilityDetails").getAsString().toString());

                                                    }

                                                }
                                            }


                                    } else {

                                        MyUtility.showAlertMessage(getActivity(), jsonObject.get("Message").getAsString().toString());
                                    }

                                } else {

                                    MyUtility.showAlertMessage(getActivity(), jsonObject.get("Message").getAsString().toString());
                                }

                            } catch (Exception e) {

                                e.printStackTrace();
                            }

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

    private void callApiToGetEvents() {

        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }


        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ClubhouseEventApiModel> call3 = apiInterface.getAllClubhouseEvents();
        call3.enqueue(new Callback<ClubhouseEventApiModel>() {
            @Override
            public void onResponse(Call<ClubhouseEventApiModel> call, Response<ClubhouseEventApiModel> response) {

                Log.e("Data:",new Gson().toJson(response.body()));

                hideLoader();

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            ClubhouseEventApiModel model=response.body();

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

                                        showBookingDialog(departmentModelList);


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
            public void onFailure(Call<ClubhouseEventApiModel> call, Throwable t) {
                call.cancel();
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });


    }

    private void showBookingDialog(final ArrayList<DepartmentModel> departmentModelList) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_booking, null);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(alertLayout);

        final AlertDialog alert=dialog.create();

        TextView tvTitle=(TextView)alertLayout.findViewById(R.id.tvTitle);
        tvTitle.setText("BOOKING");


        editDepartmet = (EditText) alertLayout. findViewById(R.id.editDepartment);
        editDepartmet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bindEventType(departmentModelList);
            }
        });



        final EditText edesc=(EditText)alertLayout.findViewById(R.id.editdate);
        edesc.setText(editDate.getText().toString());
        Button btnAdd=(Button)alertLayout.findViewById(R.id.addFeedback);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 if(selectedEventTypeId.equalsIgnoreCase("")){

                    editDepartmet.setError("Select Event Type");

                }else  if(edesc.getText().toString().length()==0){

                    edesc.setError("Enter Date");

                }else {

                    alert.dismiss();

                   // Log.e("data",esubject.getText().toString()+selectedDeaprtmentId+edesc.getText().toString());

                    callApiBook(selectedEventTypeId,edesc.getText().toString());

                }
            }
        });

        alert.show();
    }

    private void callApiBook(String selectedEventTypeId, String string) {

        Log.e("Data",selectedEventTypeId+string+AppClass.getUserId());

        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<JsonObject> call3 = apiInterface.clubhouseReservation(selectedId,selectedEventTypeId,AppClass.getUserId(),string);
        call3.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e("AddFb:",new Gson().toJson(response.body()));

                hideLoader();

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            JsonObject model=response.body();

                            if(model.get("Message").getAsString().contains("Successfully")){

                                MyUtility.showAlertSuccessMessage(getActivity(),model.get("Message").getAsString());


                            }else{

                                MyUtility.showAlertMessage(getActivity(),model.get("Message").getAsString());

                            }

                            editDate.setText("Click here to select Date");
                            editClubhouse.setText("Click here to select Clubhouse");
                            btnBook.setVisibility(View.GONE);


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

    private void bindEventType(final ArrayList<DepartmentModel> departmentModelList) {


        final String[]items=new String[departmentModelList.size()];

        for(int i=0;i<departmentModelList.size();i++){
            items[i]=departmentModelList.get(i).getName();
        }

        AlertDialog.Builder ad=new AlertDialog.Builder(getActivity());
        ad.setTitle("Select Event Type");


        ad.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                selectedEventTypeId= departmentModelList.get(i).getId()+"";
                editDepartmet.setText(departmentModelList.get(i).getName());
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
    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog)getActivity().getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        editDate.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
    }
    
    
}
