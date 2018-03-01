package in.explicate.fcm.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.squareup.picasso.Picasso;

import in.explicate.fcm.MainActivity;
import in.explicate.fcm.R;
import in.explicate.fcm.application.AppClass;
import in.explicate.fcm.util.FcmUtility;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Mahesh on 28/06/16.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        setUpViews();
    }

    private void setUpViews() {

        ImageView imageView=(ImageView)findViewById(R.id.splashImage);
        Picasso.with(this).load(R.drawable.splash).resize(400,800).centerCrop().into(imageView);


        if (!checkPermission()) {

            requestPermission();


        }else{

            goToNext();
        }

    }

    private void goToNext() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // TODO: Your application init goes here.


                if (AppClass.getUserId().length()>0) {


                    finish();
                    Intent i = new Intent(SplashScreenActivity.this, OTPActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter_transition, R.anim.exit_transition);

                } else {


                    showDialog();


                }
            }
        }, 3000);
    }

    private void showDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashScreenActivity.this);
        alertDialog.setTitle("Message");
        alertDialog.setMessage("Enter User ID");

        final EditText input = new EditText(SplashScreenActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alertDialog.setView(input);
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Next",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       String password = input.getText().toString();
                        if (password.length() == 0) {

                            input.setError("Enter User ID");

                            } else {

                            AppClass.setSharedPreferences(input.getText().toString());
                            finish();
                            Intent intent = new Intent(SplashScreenActivity.this, OTPActivity.class);
                            startActivity(intent);

                        }

                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

}



    @Override
    protected void onStart() {
        super.onStart();
        //getKey();
    }



    private boolean checkPermission() {


        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result3 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        return result2 == PackageManager.PERMISSION_GRANTED && result3 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {


                    boolean readAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (readAccepted && writeAccepted) {

                        goToNext();


                    } else {

                        // Snackbar.make(view, "Permission Denied, You cannot access location data and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (!checkPermission()) {
                                requestPermission();
                            }
                        }

                    }
                }

                break;
        }
    }
}
