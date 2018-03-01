package in.explicate.fcm.TabFragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import in.explicate.fcm.R;
import in.explicate.fcm.util.FcmUtility;


/**
 * Created by iFocus on 27-10-2015.
 */
public class TabThreeFragment extends Fragment {

    private TextView mobiledata,wifidata;
    private TextView internal,external;
    final double [] RXOld = new double [1];


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.info_activity, container, false);


        final TextView fcmId=(TextView)view.findViewById(R.id.fcm_id);

        if(getFCMId()!=null){

            fcmId.setText(getFCMId());

            Log.e("ID",getFCMId());

        }else{

            fcmId.setText("Unable to fetch fcm id");
        }


        fcmId.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("FCM ID", fcmId.getText().toString());
                clipboard.setPrimaryClip(clip);

                FcmUtility.showToast(getActivity(),"Copied to Clipboard");
                return false;
            }
        });

        mobiledata=(TextView)view.findViewById(R.id.mobilespeed);
        wifidata=(TextView)view.findViewById(R.id.wifispeed);



        final ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi.isConnectedOrConnecting ()) {

           // Toast.makeText(this, "Wifi", Toast.LENGTH_LONG).show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    try{

                        WifiManager wifiMgr = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);
                        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
                        int speedMbps = wifiInfo.getLinkSpeed();
                        Log.e("Speed Wifi:",speedMbps +"");

                        wifidata.setText("wifi Data Speed:"+speedMbps);


                    }catch (Exception e){

                        e.printStackTrace();
                    }


                    handler.postDelayed(this, 1000);
                }
            }, 1000 );


        } else if (mobile.isConnectedOrConnecting ()) {

            //Toast.makeText(this, "Mobile 3G ", Toast.LENGTH_LONG).show();

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    double overallTraffic = TrafficStats.getMobileRxBytes();
                    double currentDataRate = overallTraffic - RXOld [0];
                    Log.e("Speed:",currentDataRate +"");
                    RXOld [0] = overallTraffic;

                    mobiledata.setText("Mobile Data Speed:"+currentDataRate);

                    handler.postDelayed(this, 1000);
                }
            }, 1000 );

        } else {

            Toast.makeText(getActivity(), "No Network ", Toast.LENGTH_LONG).show();
        }

        internal=(TextView)view.findViewById(R.id.internal);
        external=(TextView)view.findViewById(R.id.external);

        internal.setText("Internal Storage:"+FcmUtility.getAvailableInternalMemorySize()+"/"+FcmUtility.getTotalInternalMemorySize());
        external.setText("External Storage:"+ FcmUtility.getAvailableExternalMemorySize()+"/"+FcmUtility.getTotalExternalMemorySize());

        return  view;
    }

    private String getFCMId(){

        try{

            return FirebaseInstanceId.getInstance().getToken();


        }catch (Exception e){

            e.printStackTrace();

        }

        return null;

    }


}
