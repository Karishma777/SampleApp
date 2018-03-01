package in.explicate.fcm.TabFragments;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.adapter.CurrentNotificationAdapter;
import in.explicate.fcm.database.DbHandler;
import in.explicate.fcm.model.NotificationModel;
import in.explicate.fcm.util.FcmUtility;

public class OTPFragment extends Fragment {

    private EditText editMobile,editOTP;
    private Button submit;
    private TextView otp,lblmobile,lblotp;

    private int clickCount=0;
    private String otpSend="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.otp_layout, container, false);

        editMobile=(EditText)view.findViewById(R.id.editmobile);
        editOTP=(EditText)view.findViewById(R.id.editotp);




        lblmobile=(TextView) view.findViewById(R.id.lblmobile);
        lblotp=(TextView) view.findViewById(R.id.lblotp);



        editOTP.setVisibility(View.GONE);
        lblotp.setVisibility(View.GONE);
        editMobile.setVisibility(View.VISIBLE);
        lblmobile.setVisibility(View.VISIBLE);

        submit=(Button)view.findViewById(R.id.submit);

        otp=(TextView)view.findViewById(R.id.resend);

        otp.setVisibility(View.GONE);

        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendOTP();
            }
        });




        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (clickCount){

                    case  0:


                        if(editMobile.getText().toString() !=null && !editMobile.getText().toString().isEmpty() && editMobile.getText().toString().length() == 10){

                            sendOTP();


                        }else{


                            showAlert("Enter Valid Mobile Number");

                        }

                        break;


                    case 1:


                        if(editOTP.getText().toString() !=null && !editOTP.getText().toString().isEmpty() && editOTP.getText().toString().length() == 4){


                            if(editOTP.getText().toString().equalsIgnoreCase(otpSend)){

                                showAlert("Successfully Verified");
                                editMobile.setVisibility(View.VISIBLE);
                                lblmobile.setVisibility(View.VISIBLE);
                                editOTP.setVisibility(View.GONE);
                                lblotp.setVisibility(View.GONE);

                                otp.setVisibility(View.GONE);

                                editMobile.setText("");
                                editOTP.setText("");

                                submit.setText("Send OTP");
                                clickCount =0;



                            }else{

                                showAlert("Invalid OTP");

                            }




                        }else{


                            showAlert("Enter Valid OTP ");

                        }


                        break;
                }


            }
        });


        clickCount =0;
        editMobile.setVisibility(View.VISIBLE);
        lblmobile.setVisibility(View.VISIBLE);
        editOTP.setVisibility(View.GONE);
        lblotp.setVisibility(View.GONE);
        otp.setVisibility(View.GONE);
        editMobile.setText("");
        editOTP.setText("");
        submit.setText("Send OTP");


        return  view;
    }

    private void sendOTP(){

        ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage("Sending OTP");
        progressDialog.show();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


//Your authentication key
        String authkey = "132136AMIqYB8z583949f5";
//Multiple mobiles numbers separated by comma
        String mobiles = "91"+editMobile.getText().toString();
//Sender ID,While using route4 sender id should be 6 characters long.
        String senderId = "MPCITY";
//Your message to send, Add URL encoding here.

        String val = ""+((int)(Math.random()*9000)+1000);

        String message = "Magarpatta App Testing. Your OTP is "+val;
//define route
        String route="default";

        URLConnection myURLConnection=null;
        URL myURL=null;
        BufferedReader reader=null;

//encoding message
        String encoded_message= URLEncoder.encode(message);

//Send SMS API
        String mainUrl=" https://control.msg91.com/api/sendotp.php?";

//Prepare parameter string
        StringBuilder sbPostData= new StringBuilder(mainUrl);
        sbPostData.append("authkey="+authkey);
        sbPostData.append("&mobile="+mobiles);
        sbPostData.append("&message="+encoded_message);
        sbPostData.append("&sender="+senderId);
        sbPostData.append("&otp="+val);

        Log.e("URL:",sbPostData.toString());
//final string
        mainUrl = sbPostData.toString();


        try
        {
            //prepare connection
            myURL = new URL(mainUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

            //reading response
            String response;
            while ((response = reader.readLine()) != null){

                Log.e("RESPONSE", ""+response);

            }

            //finally close connection
            reader.close();
            //print response

                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();

                    }

                   /* JSONObject jsonObject=new JSONObject(response+"");

                    String type=jsonObject.getString("type");

                    if(type.equalsIgnoreCase("success")){  */

                        otpSend =val;
                        clickCount =1;


                        editMobile.setVisibility(View.VISIBLE);
                        lblmobile.setVisibility(View.VISIBLE);
                        editOTP.setVisibility(View.VISIBLE);
                        lblotp.setVisibility(View.VISIBLE);

                        otp.setVisibility(View.VISIBLE);

                        submit.setText("Verify OTP");

                        editOTP.setFocusable(true);

                        showAlert("OTP Sent Successfully to "+editMobile.getText().toString());


        }
        catch (IOException e)
        {
            if(progressDialog.isShowing()){
                progressDialog.dismiss();

            }
            e.printStackTrace();
        }


    }

    private void showAlert(String msg){

        AlertDialog.Builder ad=new AlertDialog.Builder(getActivity());
        ad.setMessage(msg);
        ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        ad.show();
    }

}
