package in.explicate.fcm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.kaopiz.kprogresshud.KProgressHUD;

import in.explicate.fcm.R;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.ChangePasswordModel;
import in.explicate.fcm.apiinterface.models.RegistrationModel;
import in.explicate.fcm.util.MyUtility;
import in.explicate.fcm.util.Validations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Mahesh on 09/12/17.
 */

public class ChangePasswordActivity extends AppCompatActivity {

    EditText old,new_pass,re_pass;
    String eold,enew,erepass;
    Button button;
    RelativeLayout parent;
    public KProgressHUD progressHUD;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setUpViews();
    }

    private void setUpViews() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("CHANGE PASSWORD");

        old=(EditText)findViewById(R.id.old);
        new_pass=(EditText)findViewById(R.id.new_pass);
        re_pass=(EditText)findViewById(R.id.re_new_pass);
        button=(Button) findViewById(R.id.change);
        parent=(RelativeLayout) findViewById(R.id.parent);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changepass();

            }
        });



    }

    private void changepass() {

        eold=old.getText().toString();
        enew=new_pass.getText().toString();
        erepass=re_pass.getText().toString();


        if (eold.equalsIgnoreCase("")) {


            MyUtility.showSnack(parent, "Enter Old Password");
        }else if (enew.equalsIgnoreCase("")){
            MyUtility.showSnack(parent, "Enter New Password");

        }else if (enew.length()<6){
            MyUtility.showSnack(parent, "Password is too Short");

        }else if (enew.length()>15){

            MyUtility.showSnack(parent, "Password is too long");

        }else if (enew.equals(erepass)){

            MyUtility.showSnack(parent, "Password Not Match");

        }else {

            if (MyUtility.isConnected(this)) {

                CallApi();


            } else {

                MyUtility.showSnack(parent, MyUtility.INTERNET_ERROR);
            }
        }
    }

    private void CallApi() {


        if(!MyUtility.isConnected(ChangePasswordActivity.this)){
            MyUtility.showAlertInternet(ChangePasswordActivity.this);
            return;
        }


        showPrgressBar();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ChangePasswordModel> call3 = apiInterface.changePassword(eold,enew,erepass);
        call3.enqueue(new Callback<ChangePasswordModel>() {
            @Override
            public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
                hidepDialog();
                switch (response.code()){
                    case 200:
                        if (response.body()!=null){
                            ChangePasswordModel otpModel=response.body();
                            if (otpModel.status.equalsIgnoreCase("Success")){

                                Toast.makeText(ChangePasswordActivity.this, otpModel.getMessage(), Toast.LENGTH_SHORT).show();

                            }else {
                                MyUtility.showAlertMessage(ChangePasswordActivity.this,otpModel.message);

                            }


                        }else {
                            MyUtility.showAlertMessage(ChangePasswordActivity.this,MyUtility.FAILED_TO_GET_DATA);

                        }

                        break;

                }




            }

            @Override
            public void onFailure(Call<ChangePasswordModel> call, Throwable t) {
                call.cancel();
                hidepDialog();
                MyUtility.showAlertMessage(ChangePasswordActivity.this,MyUtility.INTERNET_ERROR);

            }
        });

    }

    public void showPrgressBar(){

        progressHUD =new KProgressHUD(ChangePasswordActivity.this);
        progressHUD   //.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                // .setLabel("Please wait")
                // .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }
    public  void hidepDialog(){

        if(progressHUD.isShowing()) {
            progressHUD.dismiss();
        }

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



}
