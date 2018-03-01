package in.explicate.fcm.TabFragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.StreamHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.explicate.fcm.R;
import in.explicate.fcm.adapter.CommitteeMemberAdapter;
import in.explicate.fcm.adapter.NotificationToneAdapter;
import in.explicate.fcm.application.AppClass;
import in.explicate.fcm.model.ToneModel;
import in.explicate.fcm.ui.ChangePasswordActivity;
import in.explicate.fcm.util.Constants;
import in.explicate.fcm.util.FcmUtility;
import in.explicate.fcm.util.MyUtility;

/**
 * Created by Mahesh on 26/11/17.
 */

public class FragmentSetting extends Fragment {

    private TextView txtFontSize,txtNotification,tvFcmID;
    private  View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        setupViews();



        return rootView;
    }

    private void setupViews() {

        TextView txtUserID=(TextView)rootView.findViewById(R.id.txtUserID);
        txtUserID.setText(AppClass.getUserId());


        txtFontSize=(TextView)rootView.findViewById(R.id.txtFontSize);
        txtFontSize.setText(AppClass.getFontName());

        tvFcmID=(TextView)rootView.findViewById(R.id.txtFCMID);

        if(getFCMId()!=null){

            tvFcmID.setText(getFCMId());

            Log.e("ID",getFCMId());

        }else{

            tvFcmID.setText("Unable to fetch fcm id");
        }


        tvFcmID.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("FCM ID", tvFcmID.getText().toString());
                clipboard.setPrimaryClip(clip);

                FcmUtility.showToast(getActivity(),"Copied to Clipboard");
                return false;
            }
        });


        txtNotification=(TextView)rootView.findViewById(R.id.txtNotification);

        if(AppClass.getToneName().length()>0){

            txtNotification.setText(AppClass.getToneName());

        }else{

            txtNotification.setText("Default");



        }


        LinearLayout linearLayout=(LinearLayout)rootView.findViewById(R.id.llFontSize);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeAppFont();
            }
        });

        LinearLayout llNotification=(LinearLayout)rootView.findViewById(R.id.llNotification);
        llNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<ToneModel> list= MyUtility.getNotifications(getActivity());

                showData(list);

            }
        });




        LinearLayout llpass=(LinearLayout)rootView.findViewById(R.id.llpassword);
        llpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                getActivity().overridePendingTransition(R.anim.enter_transition, R.anim.exit_transition);

            }
        });
    }

    private void showData(final ArrayList<ToneModel> list) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_notifications_adapter, null);

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(alertLayout);


        RecyclerView recyclerView=(RecyclerView)alertLayout.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutParams);


        final NotificationToneAdapter adapter = new NotificationToneAdapter(list,getActivity());
        recyclerView.setAdapter(adapter);


        dialog.setPositiveButton("SELECT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int selection=adapter.getPosition();

                Log.e("Data",new Gson().toJson(list.get(selection)));
                AppClass.setNotificationTone(list.get(selection).getUrl().toString(),list.get(selection).getTitle());
                txtNotification.setText(AppClass.getToneName());
                dialogInterface.dismiss();

            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        final AlertDialog alert=dialog.create();


        alert.show();

    }

    void changeAppFont(){

        Log.e("clicked","ll");

        CharSequence[] fontSizes ={

                "SMALL","MEDIUM","LARGE"
        };

        AlertDialog.Builder ad=new AlertDialog.Builder(getActivity());
        ad.setItems(fontSizes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {

                switch (which){

                    case 0:

                        AppClass.setFont(Constants.SMALL_FONT,"SMALL");
                        txtFontSize.setText("SMALL");
                        refreshFragment();

                        break;

                    case 1:

                        AppClass.setFont(Constants.MEDIUM_FONT,"MEDIUM");
                        txtFontSize.setText("MEDIUM");
                        refreshFragment();

                        break;

                    case 2:

                        AppClass.setFont(Constants.LARGE_FONT,"LARGE");
                        txtFontSize.setText("LARGE");
                        refreshFragment();

                        break;
                }



            }
        });

        AlertDialog dialog=ad.create();
        dialog.show();


    }

    private void refreshFragment() {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();

    }

    private String getFCMId(){

        try{

            return FirebaseInstanceId.getInstance().getToken();


        }catch (Exception e){

            e.printStackTrace();

        }

        return "";

    }


}

