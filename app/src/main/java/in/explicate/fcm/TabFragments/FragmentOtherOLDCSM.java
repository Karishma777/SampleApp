package in.explicate.fcm.TabFragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.adapter.CSMAutocompleteAdapter;
import in.explicate.fcm.adapter.MyCSMAdapter;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.CategoryApiModel;
import in.explicate.fcm.apiinterface.models.PendingComplaintAPIModel;
import in.explicate.fcm.apiinterface.models.ResidentialProjectApiModel;
import in.explicate.fcm.application.AppClass;
import in.explicate.fcm.model.ComplaintModel;
import in.explicate.fcm.model.DepartmentModel;
import in.explicate.fcm.model.MyInfraInfoModel;
import in.explicate.fcm.ui.HomeActivity;
import in.explicate.fcm.util.MyUtility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahesh on 12/12/17.
 */

public class FragmentOtherOLDCSM extends Fragment implements DatePickerDialog.OnDateSetListener {


    private List<ComplaintModel> list;
    private View rootView;
    private RelativeLayout loaderll;

    private EditText fromEdit,toEdit;
    private Button btnSearch;


    private int dateFor=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_other_old_csm, container, false);
        list=new ArrayList<>();
        setUpViews();
        return  rootView;
    }

    private void setUpViews() {


        fromEdit=(EditText)rootView.findViewById(R.id.editFormDate);
        toEdit=(EditText)rootView.findViewById(R.id.editToDate);


        fromEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                dateFor=1;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        FragmentOtherOLDCSM.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                Calendar calendar = Calendar.getInstance();
                dpd.setMaxDate(calendar);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

            }
        });


        toEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateFor=2;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        FragmentOtherOLDCSM.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                Calendar calendar = Calendar.getInstance();
                dpd.setMaxDate(calendar);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");



            }
        });




        btnSearch=(Button)rootView.findViewById(R.id.btnsearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(fromEdit.getText().toString().equalsIgnoreCase("Click here to select Date")){

                    fromEdit.setError("Select From Date");

                }else if(toEdit.getText().toString().equalsIgnoreCase("Click here to select Date")){

                    toEdit.setError("Select To Date");

                }else{

                    callPendingList(fromEdit.getText().toString(),toEdit.getText().toString());


                }
            }
        });


        loaderll=(RelativeLayout)rootView.findViewById(R.id.loaderll);
        loaderll.setVisibility(View.GONE);

       // hideLoader();



    }

    private void callPendingList(String from,String to) {

        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<PendingComplaintAPIModel> call3 = apiInterface.getOtherOldComplaints(AppClass.getUserId(),from,to);
        call3.enqueue(new Callback<PendingComplaintAPIModel>() {
            @Override
            public void onResponse(Call<PendingComplaintAPIModel> call, Response<PendingComplaintAPIModel> response) {

                Log.e("Data:",new Gson().toJson(response.body()));

                hideLoader();


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            PendingComplaintAPIModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null) {

                                    if(model.data.resultOLD!=null){

                                        if (model.data.resultOLD.size() > 0) {


                                            for (int i = 0; i < model.data.resultOLD.size(); i++) {

                                                ComplaintModel item = new ComplaintModel();
                                                item.setId(model.data.resultOLD.get(i).id);
                                                item.setComplaintNo(model.data.resultOLD.get(i).csmNO);
                                                item.setComplaintType(model.data.resultOLD.get(i).csmType);
                                                item.setStatus(model.data.resultOLD.get(i).status);
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
            public void onFailure(Call<PendingComplaintAPIModel> call, Throwable t) {
                call.cancel();
                hideLoader();
                MyUtility.showAlertMessage(getActivity(),MyUtility.INTERNET_ERROR);

            }
        });

    }

    private void showData(){

        RecyclerView recyclerView=(RecyclerView)rootView.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutParams);

        MyCSMAdapter adapter = new MyCSMAdapter(list,getActivity());
        recyclerView.setAdapter(adapter);


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
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        if(dateFor==1){

            fromEdit.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);

        }else if(dateFor==2){

            toEdit.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
        }
    }
}
