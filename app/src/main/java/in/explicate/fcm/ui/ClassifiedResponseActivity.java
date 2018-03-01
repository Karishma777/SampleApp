package in.explicate.fcm.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

import in.explicate.fcm.R;
import in.explicate.fcm.adapter.ClassifiedResponseAdapter;
import in.explicate.fcm.adapter.MyCSMAdapter;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.application.AppClass;
import in.explicate.fcm.model.ClassifiedItemModel;
import in.explicate.fcm.model.ClassifiedResponseModel;
import in.explicate.fcm.util.MyUtility;
import in.explicate.fcm.util.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahesh on 09/12/17.
 */

public class ClassifiedResponseActivity extends AppCompatActivity {

    private ArrayList<ClassifiedResponseModel> list;

    private ArrayList<ClassifiedItemModel> finalList;

    public KProgressHUD progressHUD;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reposes_layout);

        finalList=new ArrayList<>();

        list=(ArrayList<ClassifiedResponseModel>)getIntent().getSerializableExtra("list");
        setUpViews();
    }

    private void setUpViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("CHANGE PASSWORD");

        TextView subject=(TextView)findViewById(R.id.txtSubject);
        TextView desc=(TextView)findViewById(R.id.txtSubject);
        TextView categoryName=(TextView)findViewById(R.id.txtSubject);


        if(!list.isEmpty()){
            subject.setText(list.get(0).getSubject());
            desc.setText(list.get(0).getDescription());
            categoryName.setText("Category: "+list.get(0).getCategoryName());



            for(int i=0;i<list.size();i++){

                if(list.get(i).getResponse()!=null){

                    for(int j=0;j<list.get(i).getResponse().size();j++){

                        ClassifiedItemModel model=new ClassifiedItemModel();
                        model.setId(list.get(i).getResponse().get(j).getId());
                        model.setResponse(list.get(i).getResponse().get(j).getResponse());
                        model.setResponseBy(list.get(i).getResponse().get(j).getResponseBy());
                        model.setDate(list.get(i).getResponse().get(j).getDate());
                        model.setType(list.get(i).getResponse().get(j).getType());

                        finalList.add(model);
                     }

                }

            }

            showData();


        }



    }

    private void showData(){

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutParams);

        ClassifiedResponseAdapter adapter = new ClassifiedResponseAdapter(finalList,this);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                showAddDialog(finalList.get(position).getId());

            }
        }));

    }

    private void showAddDialog(int id) {


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:

                onBackPressed();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void callApiAddResponse(String selectedResponseID,String reply) {

        if(!MyUtility.isConnected(this)){
            MyUtility.showAlertInternet(this);
            return;
        }

        Log.e("ADD",selectedResponseID+reply+ AppClass.getUserId());

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<JsonObject> call3 = apiInterface.addResponseReplyMyClassified(selectedResponseID,AppClass.getUserId(),reply);
        call3.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                Log.e("AddCSM:",new Gson().toJson(response.body()));

                hideLoader();

                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            JsonObject model=response.body();
                            MyUtility.showAlertMessage(ClassifiedResponseActivity.this,model.get("Message").getAsString());


                        }else{

                            MyUtility.showAlertMessage(ClassifiedResponseActivity.this,MyUtility.FAILED_TO_GET_DATA);
                        }


                        break;

                    case 500 :

                        MyUtility.showAlertMessage(ClassifiedResponseActivity.this,MyUtility.ERROR_500);


                        break;


                    case 401:

                        MyUtility.showAlertMessage(ClassifiedResponseActivity.this,MyUtility.ERROR_401);

                        break;

                    case 404:

                        MyUtility.showAlertMessage(ClassifiedResponseActivity.this,MyUtility.ERROR_404);

                        break;
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                call.cancel();
                hideLoader();
                MyUtility.showAlertMessage(ClassifiedResponseActivity.this,MyUtility.INTERNET_ERROR);

            }
        });

    }

    public void showLoader(){

        progressHUD =new KProgressHUD(this);
        progressHUD   //.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                // .setLabel("Please wait")
                // .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }


    public void hideLoader(){

        if(progressHUD.isShowing()){
            progressHUD.dismiss();

        }

    }

}
