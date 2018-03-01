package in.explicate.fcm.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import in.explicate.fcm.R;
import in.explicate.fcm.apiinterface.APIClient;
import in.explicate.fcm.apiinterface.APIInterface;
import in.explicate.fcm.apiinterface.models.GetInfraSearchModel;
import in.explicate.fcm.apiinterface.models.OTPModel;
import in.explicate.fcm.apiinterface.models.RegistrationModel;
import in.explicate.fcm.apiinterface.models.ResidentialProjectApiModel;
import in.explicate.fcm.util.MyUtility;
import in.explicate.fcm.util.Validations;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    EditText name,u_name,password,email,number,fcmid,device_id,osflag,citizn_id,device_name,infra_id;
    Toolbar toolbar;
    String ename,euname,epassword,eemail,enumber,efacmis,edevice_id,eosflag,ecitizn,edevice_name,einfra_id;
    Button button;
    RelativeLayout parent;
    public KProgressHUD progressHUD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUpview();
        getResidentialProject();


    }



    private void getResidentialProject() {


        if(!MyUtility.isConnected(SignUpActivity.this)){
            MyUtility.showAlertInternet(SignUpActivity.this);
            return;
        }


        showPrgressBar();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<ResidentialProjectApiModel> call3 = apiInterface.getResidentialProject();
        call3.enqueue(new Callback<ResidentialProjectApiModel>() {
            @Override
            public void onResponse(Call<ResidentialProjectApiModel> call, Response<ResidentialProjectApiModel> response) {
                Log.e("Data:",new Gson().toJson(response.body()));
                hidepDialog();
                switch (response.code()){
                    case 200:
                        if (response.body()!=null){
                            ResidentialProjectApiModel otpModel=response.body();
                            if (otpModel.status.equalsIgnoreCase("Success")){

                                Toast.makeText(SignUpActivity.this, otpModel.getMessage(), Toast.LENGTH_SHORT).show();

                            }else {
                                MyUtility.showAlertMessage(SignUpActivity.this,otpModel.message);

                            }


                        }else {
                            MyUtility.showAlertMessage(SignUpActivity.this,MyUtility.FAILED_TO_GET_DATA);

                        }

                        break;

                }




            }

            @Override
            public void onFailure(Call<ResidentialProjectApiModel> call, Throwable t) {
                call.cancel();
                hidepDialog();
                MyUtility.showAlertMessage(SignUpActivity.this,MyUtility.INTERNET_ERROR);

            }
        });
    }

    private void setUpview() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("SIGN UP");
        name=(EditText)findViewById(R.id.u_name);
        u_name=(EditText)findViewById(R.id.name);
        password=(EditText)findViewById(R.id.password_edit_field);
        email=(EditText)findViewById(R.id.email);
        number=(EditText)findViewById(R.id.mobile);
        citizn_id=(EditText)findViewById(R.id.citizenID);
        infra_id=(EditText)findViewById(R.id.infra_id);
        button=(Button) findViewById(R.id.register);
        parent=(RelativeLayout) findViewById(R.id.parent);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();

            }
        });

    }

    private void register() {
        MyUtility.hideKeyboard(device_id, SignUpActivity.this);

        ename=name.getText().toString();
        euname=u_name.getText().toString();
        epassword=password.getText().toString();
        eemail=email.getText().toString();
        enumber=number.getText().toString();
        ecitizn=citizn_id.getText().toString();
        einfra_id=infra_id.getText().toString();


        if (ename.equalsIgnoreCase("")) {


            MyUtility.showSnack(parent, Validations.ENTER_USER_NAME);
        }else if (euname.equalsIgnoreCase("")){
            MyUtility.showSnack(parent, Validations.ENTER_NAME);

        }else if (epassword.equalsIgnoreCase("")){
            MyUtility.showSnack(parent, Validations.ENTER_PASSWORD);

        }else if (epassword.length()>15){

            MyUtility.showSnack(parent, Validations.ENTER_PASSWORD_LESS);

        }else if (epassword.length()<6){

            MyUtility.showSnack(parent, Validations.ENTER_PASSWORD_GREATER);

        }else if (einfra_id.equalsIgnoreCase("")){

            MyUtility.showSnack(parent, "Enter InfraID");

        }else if (ecitizn.equalsIgnoreCase("")){

            MyUtility.showSnack(parent, "Enter CitizenID");

        }else if (eemail.equalsIgnoreCase("")){

            MyUtility.showSnack(parent, "Enter Email Id");

        }else if (!eemail.matches(MyUtility.emailPattern)) {


            MyUtility.showSnack(parent, Validations.ENTER_VALID_EMAIL);

        }else if (enumber.equalsIgnoreCase("")) {


            MyUtility.showSnack(parent, Validations.ENTER_MOBILE);

        }else if (enumber.length()<10) {


            MyUtility.showSnack(parent, Validations.ENTER_VALID_Mobile);
        }else if (enumber.length()>10) {


            MyUtility.showSnack(parent, Validations.ENTER_VALID_Mobilee);
        } else {


            if (MyUtility.isConnected(this)) {

                CallApi();
                sendOTP();


            } else {

                MyUtility.showSnack(parent, MyUtility.INTERNET_ERROR);
            }

        }






    }

    private void sendOTP() {


        if(!MyUtility.isConnected(SignUpActivity.this)){
            MyUtility.showAlertInternet(SignUpActivity.this);
            return;
        }


        showPrgressBar();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<OTPModel> call3 = apiInterface.sendOTP(enumber);
        call3.enqueue(new Callback<OTPModel>() {
            @Override
            public void onResponse(Call<OTPModel> call, Response<OTPModel> response) {
                Log.e("Data:",new Gson().toJson(response.body()));
                hidepDialog();
                switch (response.code()){
                    case 200:
                        if (response.body()!=null){
                            OTPModel otpModel=response.body();
                            if (otpModel.status.equalsIgnoreCase("Success")){


                                Toast.makeText(SignUpActivity.this, otpModel.getMessage(), Toast.LENGTH_SHORT).show();

                            }else {
                                MyUtility.showAlertMessage(SignUpActivity.this,otpModel.message);

                            }


                        }else {
                            MyUtility.showAlertMessage(SignUpActivity.this,MyUtility.FAILED_TO_GET_DATA);

                        }

                        break;

                }




            }

            @Override
            public void onFailure(Call<OTPModel> call, Throwable t) {
                call.cancel();
                hidepDialog();
                MyUtility.showAlertMessage(SignUpActivity.this,MyUtility.INTERNET_ERROR);

            }
        });
    }

    private void CallApi() {


        if(!MyUtility.isConnected(SignUpActivity.this)){
            MyUtility.showAlertInternet(SignUpActivity.this);
            return;
        }


        showPrgressBar();

        APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<RegistrationModel> call3 = apiInterface.Register(ename,euname,epassword,einfra_id,ecitizn,eemail,enumber);
        call3.enqueue(new Callback<RegistrationModel>() {
            @Override
            public void onResponse(Call<RegistrationModel> call, Response<RegistrationModel> response) {
                hidepDialog();
                switch (response.code()){
                    case 200:
                        if (response.body()!=null){
                            RegistrationModel otpModel=response.body();
                            if (otpModel.status.equalsIgnoreCase("Success")){
                                Intent intent=new Intent(SignUpActivity.this,OTPActivity.class);
                                intent.putExtra("Number",enumber);
                                startActivity(intent);

                                Toast.makeText(SignUpActivity.this, otpModel.getMessage(), Toast.LENGTH_SHORT).show();

                            }else {
                                MyUtility.showAlertMessage(SignUpActivity.this,otpModel.message);

                            }


                        }else {
                            MyUtility.showAlertMessage(SignUpActivity.this,MyUtility.FAILED_TO_GET_DATA);

                        }

                        break;

                }




            }

            @Override
            public void onFailure(Call<RegistrationModel> call, Throwable t) {
                call.cancel();
                hidepDialog();
                MyUtility.showAlertMessage(SignUpActivity.this,MyUtility.INTERNET_ERROR);

            }
        });



    }



    public void showPrgressBar(){

        progressHUD =new KProgressHUD(SignUpActivity.this);
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

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
