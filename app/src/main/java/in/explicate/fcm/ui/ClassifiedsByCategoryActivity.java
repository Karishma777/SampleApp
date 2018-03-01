package in.explicate.fcm.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

import in.explicate.fcm.R;
import in.explicate.fcm.adapter.ClassifiedCategoryAdapter;
import in.explicate.fcm.adapter.ClassifiedResponseAdapter;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.ClassifiedDetailsAPIModel;
import in.explicate.fcm.application.AppClass;
import in.explicate.fcm.model.ClassifiedItemModel;
import in.explicate.fcm.model.ClassifiedModel;
import in.explicate.fcm.model.ClassifiedResponseModel;
import in.explicate.fcm.util.MyUtility;
import in.explicate.fcm.util.RecyclerItemClickListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahesh on 17/12/17.
 */

public class ClassifiedsByCategoryActivity  extends AppCompatActivity {


    private ArrayList<ClassifiedModel> finalList;
    public KProgressHUD progressHUD;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classified_category);


        finalList=(ArrayList<ClassifiedModel>)getIntent().getSerializableExtra("list");
        setUpViews();
    }

    private void setUpViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));



        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutParams);


        ClassifiedCategoryAdapter adapter = new ClassifiedCategoryAdapter(finalList,this);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                getResponsesDetails(finalList.get(position).getId());
            }
        }));





    }


    private void getResponsesDetails(String id) {

        Log.e("ID",id);

        if(!MyUtility.isConnected(this)){
            MyUtility.showAlertInternet(this);
            return;
        }

        showLoader();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);

        Call<ClassifiedDetailsAPIModel> call3=null;

        if(getIntent().getStringExtra("type").equalsIgnoreCase("My")){

            call3 = apiInterface.getMyClassifiedDetails(AppClass.getUserId(),id);


        }else{

           call3 = apiInterface.getOthersClassifiedDetails(AppClass.getUserId(),id);

        }


        call3.enqueue(new Callback<ClassifiedDetailsAPIModel>() {
            @Override
            public void onResponse(Call<ClassifiedDetailsAPIModel> call, Response<ClassifiedDetailsAPIModel> response) {

                hideLoader();
                Log.e("Details:",new Gson().toJson(response.body()));


                switch (response.code()){

                    case 200:

                        if(response.body()!=null){

                            ClassifiedDetailsAPIModel model=response.body();

                            if(model.status.equalsIgnoreCase("Success")){

                                if(model.data!=null) {

                                    if(getIntent().getStringExtra("type").equalsIgnoreCase("My")) {

                                        if (model.data.result.size() > 0) {

                                            ArrayList<ClassifiedResponseModel> list = new ArrayList<ClassifiedResponseModel>();

                                            for (int i = 0; i < model.data.result.size(); i++) {

                                                ClassifiedResponseModel item = new ClassifiedResponseModel();
                                                item.setId(model.data.result.get(i).id);
                                                item.setSubject(model.data.result.get(i).subject);
                                                item.setDescription(model.data.result.get(i).description);
                                                item.setCategoryName(model.data.result.get(i).categoryName);

                                                if (model.data.result.get(i).responses != null) {

                                                    ArrayList<ClassifiedItemModel> responses = new ArrayList<ClassifiedItemModel>();

                                                    for (int j = 0; j < model.data.result.get(i).responses.size(); j++) {


                                                        ClassifiedItemModel item1 = new ClassifiedItemModel();
                                                        item1.setId(model.data.result.get(i).responses.get(j).id);
                                                        item1.setResponse(model.data.result.get(i).responses.get(j).response);
                                                        item1.setResponseBy(model.data.result.get(i).responses.get(j).responseBy);
                                                        item1.setDate(model.data.result.get(i).responses.get(j).responseDate);
                                                        item1.setType("1");

                                                        responses.add(item1);

                                                        if (model.data.result.get(i).responses.get(j).replies != null) {

                                                            for (int k = 0; k < model.data.result.get(i).responses.get(j).replies.size(); k++) {


                                                                ClassifiedItemModel item2 = new ClassifiedItemModel();
                                                                item2.setId(model.data.result.get(i).responses.get(j).replies.get(k).id);
                                                                item2.setResponse(model.data.result.get(i).responses.get(j).replies.get(k).reply);
                                                                item2.setResponseBy(model.data.result.get(i).responses.get(j).replies.get(k).replyBy);
                                                                item2.setDate(model.data.result.get(i).responses.get(j).replies.get(k).replyDate);
                                                                item2.setType("2");

                                                                responses.add(item2);


                                                            }
                                                        }
                                                        item.setResponse(responses);
                                                    }
                                                }

                                                list.add(item);

                                            }

                                            goToNext(list);

                                        } else {
                                            MyUtility.showAlertMessage(ClassifiedsByCategoryActivity.this, model.message);

                                        }

                                    }else{


                                        if (model.data.otherresult.size() > 0) {

                                            ArrayList<ClassifiedResponseModel> list = new ArrayList<ClassifiedResponseModel>();

                                            for (int i = 0; i < model.data.otherresult.size(); i++) {

                                                ClassifiedResponseModel item = new ClassifiedResponseModel();
                                                item.setId(model.data.otherresult.get(i).id);
                                                item.setSubject(model.data.otherresult.get(i).subject);
                                                item.setDescription(model.data.otherresult.get(i).description);
                                                item.setCategoryName(model.data.otherresult.get(i).categoryName);

                                                if (model.data.otherresult.get(i).responses != null) {

                                                    ArrayList<ClassifiedItemModel> responses = new ArrayList<ClassifiedItemModel>();

                                                    for (int j = 0; j < model.data.otherresult.get(i).responses.size(); j++) {


                                                        ClassifiedItemModel item1 = new ClassifiedItemModel();
                                                        item1.setId(model.data.otherresult.get(i).responses.get(j).id);
                                                        item1.setResponse(model.data.otherresult.get(i).responses.get(j).response);
                                                        item1.setResponseBy(model.data.otherresult.get(i).responses.get(j).responseBy);
                                                        item1.setDate(model.data.otherresult.get(i).responses.get(j).responseDate);
                                                        item1.setType("1");

                                                        responses.add(item1);

                                                        if (model.data.otherresult.get(i).responses.get(j).replies != null) {

                                                            for (int k = 0; k < model.data.otherresult.get(i).responses.get(j).replies.size(); k++) {


                                                                ClassifiedItemModel item2 = new ClassifiedItemModel();
                                                                item2.setId(model.data.otherresult.get(i).responses.get(j).replies.get(k).id);
                                                                item2.setResponse(model.data.otherresult.get(i).responses.get(j).replies.get(k).reply);
                                                                item2.setResponseBy(model.data.otherresult.get(i).responses.get(j).replies.get(k).replyBy);
                                                                item2.setDate(model.data.otherresult.get(i).responses.get(j).replies.get(k).replyDate);
                                                                item2.setType("2");

                                                                responses.add(item2);


                                                            }
                                                        }
                                                        item.setResponse(responses);
                                                    }
                                                }

                                                list.add(item);

                                            }

                                            goToNext(list);

                                        } else {
                                            MyUtility.showAlertMessage(ClassifiedsByCategoryActivity.this, model.message);

                                        }
                                    }

                                }else{

                                    MyUtility.showAlertMessage(ClassifiedsByCategoryActivity.this,model.message);

                                }

                            }else{

                                MyUtility.showAlertMessage(ClassifiedsByCategoryActivity.this,MyUtility.NO_DATA_FOUND);
                            }




                        }else{

                            MyUtility.showAlertMessage(ClassifiedsByCategoryActivity.this,MyUtility.FAILED_TO_GET_DATA);
                        }


                        break;

                    case 500 :

                        MyUtility.showAlertMessage(ClassifiedsByCategoryActivity.this,MyUtility.ERROR_500);


                        break;


                    case 401:

                        MyUtility.showAlertMessage(ClassifiedsByCategoryActivity.this,MyUtility.ERROR_401);

                        break;

                    case 404:

                        MyUtility.showAlertMessage(ClassifiedsByCategoryActivity.this,MyUtility.ERROR_404);

                        break;
                }

            }

            @Override
            public void onFailure(Call<ClassifiedDetailsAPIModel> call, Throwable t) {
                call.cancel();
                hideLoader();

            }
        });

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

    private void goToNext(ArrayList<ClassifiedResponseModel> list) {

        Log.e("Details",new Gson().toJson(list));

        if(!list.isEmpty()){

            Intent i=new Intent(this, ClassifiedResponseActivity.class);
            i.putExtra("title","title");
            i.putExtra("list",list);
            i.putExtra("type",getIntent().getStringExtra("type"));
            startActivity(i);
            overridePendingTransition(R.anim.enter_transition,R.anim.exit_transition);

        }else{

            MyUtility.showAlertMessage(this,MyUtility.NO_DATA_FOUND);
        }
    }


}
