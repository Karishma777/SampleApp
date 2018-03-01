package in.explicate.fcm.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.explicate.fcm.R;
import in.explicate.fcm.model.ToneModel;

/**
 * Created by Mahesh on 15/06/16.
 */
public class MyUtility {

    public static String INTERNET_ERROR="Check Internet Connection";
    public static String ERROR_500 ="Unable to reach server";
    public static String ERROR_401 ="Unauthorised Access";
    public static String ERROR_404 ="Server Not Found";

    public static String FAILED_TO_GET_DATA="Failed to load data";
    public static String NO_DATA_FOUND="NO Data Found";



    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static String DIRECTORY_NAME="Select Wear";


    public static boolean isConnected(Context context) {

        if(context != null) {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netinfo = cm.getActiveNetworkInfo();

            if (netinfo != null && netinfo.isConnectedOrConnecting()) {
                NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
                else return false;
            } else
                return false;
        } else return false;
    }

    public static void internetProblem(View view){

        Snackbar.make(view, INTERNET_ERROR, Snackbar.LENGTH_LONG).show();


    }

    public static void showSnack(View view, String msg){

      //  Snackbar.make(layout,"Hi",Snackbar.LENGTH_SHORT).show();

        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();

    }

    public static void hideKeyboard(View view, Context context) {
        InputMethodManager inputMethodManager =(InputMethodManager)context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    public static void showToast(String msg, Context context){

        Toast toast= Toast.makeText(context,msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    public static String getMobileNo(Context context){

        try{

            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            String num= tm.getLine1Number();

            return num != null && num.length() > 2 ? num.substring(2) : null;

        }catch (Exception e){


            e.printStackTrace();
        }

        return "";

    }



    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public static Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    public static File getOutputMediaFile(){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".jpg");

        return mediaFile;
    }



    public static void showAlertInternet(Activity context){

        LayoutInflater inflater =context.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_error, null);


        AlertDialog.Builder ad=new AlertDialog.Builder(context);
        ad.setView(alertLayout);

        final AlertDialog alert=ad.create();

        TextView msg=(TextView)alertLayout.findViewById(R.id.alertTitle);
        msg.setText(MyUtility.INTERNET_ERROR);

        ImageView close=(ImageView)alertLayout.findViewById(R.id.closeBtn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alert.dismiss();

            }
        });

        alert.show();

    }

    public static void showAlertMessage(Activity context,String msg){



        LayoutInflater inflater =context.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_warn, null);


        AlertDialog.Builder ad=new AlertDialog.Builder(context);
        ad.setView(alertLayout);

        final AlertDialog alert=ad.create();

        TextView msg1=(TextView)alertLayout.findViewById(R.id.alertTitle);
        msg1.setText(msg);

        ImageView close=(ImageView)alertLayout.findViewById(R.id.closeBtn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alert.dismiss();

            }
        });

        alert.show();
    }

    public static void showAlertSuccessMessage(Activity context,String msg){


        LayoutInflater inflater =context.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_warn, null);


        AlertDialog.Builder ad=new AlertDialog.Builder(context);
        ad.setView(alertLayout);

        final AlertDialog alert=ad.create();

        TextView msg1=(TextView)alertLayout.findViewById(R.id.alertTitle);
        msg1.setText(msg);

        ImageView close=(ImageView)alertLayout.findViewById(R.id.closeBtn);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alert.dismiss();

            }
        });

        alert.show();
    }


    public static void callIntent(Context context,String no){

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+no));
        context.startActivity(intent);
    }


    public static void saveContactIntent(Context context,String no,String name,String email){

        Intent intent = new Intent(Contacts.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
        intent.putExtra(Contacts.Intents.Insert.EMAIL, email);
        intent.putExtra(Contacts.Intents.Insert.PHONE, no);
        intent.putExtra(Contacts.Intents.Insert.NAME, name);
        context.startActivity(intent);


    }


    public static ArrayList<ToneModel> getNotifications(Context context) {
        RingtoneManager manager = new RingtoneManager(context);
        manager.setType(RingtoneManager.TYPE_NOTIFICATION);
        Cursor cursor = manager.getCursor();

        ArrayList<ToneModel> list = new ArrayList<>();
        while (cursor.moveToNext()) {

            ToneModel model=new ToneModel();

            String notificationId = cursor.getString(RingtoneManager.ID_COLUMN_INDEX);
            String notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String notificationUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);

            model.setTitle(notificationTitle);
            model.setUrl(Uri.parse( notificationUri+"/"+notificationId));

            list.add(model);
        }

        return list;
    }


}
