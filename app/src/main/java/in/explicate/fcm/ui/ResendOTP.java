package in.explicate.fcm.ui;

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

import org.w3c.dom.Text;

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

public class ResendOTP extends AppCompatActivity {


    EditText mobile;
    String emobile;
    TextView textView;
    Button button;
    RelativeLayout parent;
    Toolbar toolbar;
    public KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resend_otp);

        setUpview();


    }

    private void setUpview() {


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("VERIFY OTP");

        mobile = (EditText) findViewById(R.id.mobile);
        textView = (TextView) findViewById(R.id.resend);
        button = (Button) findViewById(R.id.send);
        parent = (RelativeLayout) findViewById(R.id.parent);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyOTP();
            }
        });

    }

    private void verifyOTP() {

        emobile = mobile.getText().toString();
        if (emobile.equalsIgnoreCase("")) {


            MyUtility.showSnack(parent, Validations.ENTER_MOBILE);

        } else if (emobile.length() < 10) {


            MyUtility.showSnack(parent, Validations.ENTER_VALID_Mobile);
        } else if (emobile.length() > 10) {


            MyUtility.showSnack(parent, Validations.ENTER_VALID_Mobilee);
        } else {


            if (MyUtility.isConnected(this)) {

                CallApi();


            } else {

                MyUtility.showSnack(parent, MyUtility.INTERNET_ERROR);
            }
        }
    }

    private void CallApi() {


        if (!MyUtility.isConnected(ResendOTP.this)) {
            MyUtility.showAlertInternet(ResendOTP.this);
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

                                Toast.makeText(ResendOTP.this, otpModel.getMessage(), Toast.LENGTH_SHORT).show();

                            } else {
                                MyUtility.showAlertMessage(ResendOTP.this, otpModel.message);

                            }


                        } else {
                            MyUtility.showAlertMessage(ResendOTP.this, MyUtility.FAILED_TO_GET_DATA);

                        }

                        break;

                }


            }

            @Override
            public void onFailure(Call<OTPModel> call, Throwable t) {
                call.cancel();
                hidepDialog();
                MyUtility.showAlertMessage(ResendOTP.this, MyUtility.INTERNET_ERROR);

            }
        });


    }


    public void showPrgressBar() {

        progressHUD = new KProgressHUD(ResendOTP.this);
        progressHUD   //.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                // .setLabel("Please wait")
                // .setDetailsLabel("Downloading data")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();
    }

    public void hidepDialog() {

        if (progressHUD.isShowing()) {
            progressHUD.dismiss();
        }

    }
}
