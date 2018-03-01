package in.explicate.fcm.TabFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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

import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.adapter.BadmintonAdapter;
import in.explicate.fcm.adapter.NewsLetterAdapter;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.BadmintonAPIModel;
import in.explicate.fcm.apiinterface.models.NewsLetterAPIModel;
import in.explicate.fcm.model.BadmintonModel;
import in.explicate.fcm.model.NewsLetterModel;
import in.explicate.fcm.ui.HomeActivity;
import in.explicate.fcm.util.MyUtility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahesh on 11/12/17.
 */

public class FragmentBadminton extends Fragment implements DatePickerDialog.OnDateSetListener {


    private List<BadmintonModel> list;
    private BadmintonAdapter adapter;
    private View rootView;
    private RelativeLayout loaderll;
    private EditText editDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView= inflater.inflate(R.layout.fragment_badminton, container, false);
        setUpViews();
        return  rootView;
    }

    private void setUpViews() {


        loaderll=(RelativeLayout)rootView.findViewById(R.id.loaderll);
        loaderll.setVisibility(View.GONE);

      //  hideLoader();


        editDate=(EditText)rootView.findViewById(R.id.editDate);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        FragmentBadminton.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                Calendar calendar = Calendar.getInstance();
                dpd.setMinDate(calendar);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");

            }
        });
    }

    private void callApi(String date) {



        if(!MyUtility.isConnected(getActivity())){
            MyUtility.showAlertInternet(getActivity());
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<BadmintonAPIModel> call3 = apiInterface.getSelectedDateAvailableTimeSlots(date);
        call3.enqueue(new Callback<BadmintonAPIModel>() {
            @Override
            public void onResponse(Call<BadmintonAPIModel> call, Response<BadmintonAPIModel> response) {

                Log.e("Data:",new Gson().toJson(response.body()));

                hideLoader();


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            BadmintonAPIModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data.result.size() >0){

                                    list=new ArrayList<>();

                                    for(int i =0;i<model.data.result.size();i++){

                                        BadmintonModel item=new BadmintonModel();
                                        item.setDate(model.data.result.get(i).date);
                                        item.setName(model.data.result.get(i).courtName);
                                        item.setType(model.data.result.get(i).type);
                                        item.setTimeslot(model.data.result.get(i).timeSlot);
                                        list.add(item);
                                    }

                                    showData();

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
            public void onFailure(Call<BadmintonAPIModel> call, Throwable t) {
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

        adapter = new BadmintonAdapter(list,getActivity());
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
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog)getActivity().getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        editDate.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
        callApi(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
    }
}
