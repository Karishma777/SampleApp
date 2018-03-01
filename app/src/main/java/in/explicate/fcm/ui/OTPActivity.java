package in.explicate.fcm.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import in.explicate.fcm.R;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.OTPModel;
import in.explicate.fcm.apiinterface.models.VerifyOTPModel;
import in.explicate.fcm.util.MyUtility;
import in.explicate.fcm.util.Validations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mobile,otp;
    String emobile,eotp;
    Button button,verify;
    RelativeLayout parent;
    Toolbar toolbar;
    TextView textView;
    public KProgressHUD progressHUD;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        setUpview();

    }

    private void setUpview() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("SEND OTP");
        Intent intent=getIntent();
        number=intent.getStringExtra("Number");


        mobile = (EditText) findViewById(R.id.mobile);
        mobile.setText(number);
        otp = (EditText) findViewById(R.id.editotp);
        button = (Button) findViewById(R.id.send);
        textView = (TextView) findViewById(R.id.resend);
        button.setOnClickListener(this);
        textView.setOnClickListener(this);

        parent = (RelativeLayout) findViewById(R.id.parent);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.send:

                verifyOTP();


                break;

            case R.id.resend:
                 sendOTP();
                break;






        }

    }

    private void verifyOTP() {
        emobile = mobile.getText().toString();
        eotp = otp.getText().toString();
        if (emobile.equalsIgnoreCase("")) {


            MyUtility.showSnack(parent, Validations.ENTER_MOBILE);

        } else if (emobile.length() < 10) {


            MyUtility.showSnack(parent, Validations.ENTER_VALID_Mobile);
        } else if (emobile.length() > 10) {


            MyUtility.showSnack(parent, Validations.ENTER_VALID_Mobilee);
        } else if (eotp.equalsIgnoreCase("")){
            MyUtility.showSnack(parent, Validations.Enter_otp);


        }else {


            if (MyUtility.isConnected(this)) {

               VerifyOTPCallApi();


            } else {

                MyUtility.showSnack(parent, MyUtility.INTERNET_ERROR);
            }
        }



    }

    private void VerifyOTPCallApi() {


        if(!MyUtility.isConnected(OTPActivity.this)){
            MyUtility.showAlertInternet(OTPActivity.this);
            return;
        }


        showPrgressBar();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<VerifyOTPModel> call3 = apiInterface.verifyOTP(eotp,emobile);
        call3.enqueue(new Callback<VerifyOTPModel>() {
            @Override
            public void onResponse(Call<VerifyOTPModel> call, Response<VerifyOTPModel> response) {


                Log.e("Data:",new Gson().toJson(response.body()));

                hidepDialog();

                switch (response.code()){
                    case 200:
                        if (response.body()!=null){
                            VerifyOTPModel otpModel=response.body();
                            if (otpModel.status.equalsIgnoreCase("Success")){
                                Intent intent=new Intent(OTPActivity.this,SignUpActivity.class);
                                startActivity(intent);

                                Toast.makeText(OTPActivity.this, otpModel.getMessage(), Toast.LENGTH_SHORT).show();

                            }else {
                                MyUtility.showAlertMessage(OTPActivity.this,otpModel.message);

                            }


                        }else {
                            MyUtility.showAlertMessage(OTPActivity.this,MyUtility.FAILED_TO_GET_DATA);

                        }

                        break;

                }




            }

            @Override
            public void onFailure(Call<VerifyOTPModel> call, Throwable t) {
                call.cancel();
                hidepDialog();
                MyUtility.showAlertMessage(OTPActivity.this,MyUtility.INTERNET_ERROR);

            }
        });


    }

    private void sendOTP() {
        emobile = mobile.getText().toString();
        if (emobile.equalsIgnoreCase("")) {


            MyUtility.showSnack(parent, Validations.ENTER_MOBILE);

        } else if (emobile.length() < 10) {


            MyUtility.showSnack(parent, Validations.ENTER_VALID_Mobile);
        } else if (emobile.length() > 10) {


            MyUtility.showSnack(parent, Validations.ENTER_VALID_Mobilee);
        } else {


            if (MyUtility.isConnected(this)) {

                SendOTPCallApi();


            } else {

                MyUtility.showSnack(parent, MyUtility.INTERNET_ERROR);
            }
        }
    }

    private void SendOTPCallApi() {

        if (!MyUtility.isConnected(OTPActivity.this)) {
            MyUtility.showAlertInternet(OTPActivity.this);
            return;
        }


        showPrgressBar();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<OTPModel> call3 = apiInterface.resendOTP(emobile);
        call3.enqueue(new Callback<OTPModel>() {
            @Override
            public void onResponse(Call<OTPModel> call, Response<OTPModel> response) {


                Log.e("Data:", new Gson().toJson(response.body()));

                hidepDialog();

                switch (response.code()) {
                    case 200:
                        if (response.body() != null) {
                            OTPModel otpModel = response.body();
                            if (otpModel.status.equalsIgnoreCase("Success")) {

                                Toast.makeText(OTPActivity.this, otpModel.getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                MyUtility.showAlertMessage(OTPActivity.this, otpModel.message);

                            }


                        } else {
                            MyUtility.showAlertMessage(OTPActivity.this, MyUtility.FAILED_TO_GET_DATA);

                        }

                        break;

                }


            }

            @Override
            public void onFailure(Call<OTPModel> call, Throwable t) {
                call.cancel();
                hidepDialog();
                MyUtility.showAlertMessage(OTPActivity.this, MyUtility.INTERNET_ERROR);

            }
        });


    }




    public void showPrgressBar(){

        progressHUD =new KProgressHUD(OTPActivity.this);
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



}







