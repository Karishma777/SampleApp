package in.explicate.fcm.ui;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import net.skoumal.fragmentback.BackFragmentHelper;

import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.TabFragments.AddInfrInfoFragment;
import in.explicate.fcm.TabFragments.ComitteeFragment;
import in.explicate.fcm.TabFragments.CouncilFragment;
import in.explicate.fcm.TabFragments.EmergecyContactFragment;
import in.explicate.fcm.TabFragments.FragmentActivityCommon;
import in.explicate.fcm.TabFragments.FragmentActivityProject;
import in.explicate.fcm.TabFragments.FragmentBadminton;
import in.explicate.fcm.TabFragments.FragmentClassfiedMyClassifieds;
import in.explicate.fcm.TabFragments.FragmentClassfiedOtherClassifieds;
import in.explicate.fcm.TabFragments.FragmentClubhouse;
import in.explicate.fcm.TabFragments.FragmentClubhouseRecreational;
import in.explicate.fcm.TabFragments.FragmentDevice;
import in.explicate.fcm.TabFragments.FragmentFeedbackCommittee;
import in.explicate.fcm.TabFragments.FragmentFeedbackMagarpatta;
import in.explicate.fcm.TabFragments.FragmentHome;
import in.explicate.fcm.TabFragments.FragmentMyCSM;
import in.explicate.fcm.TabFragments.FragmentSetting;
import in.explicate.fcm.TabFragments.FragmentStandartProcedure;
import in.explicate.fcm.TabFragments.FragmentSuggestionCommittee;
import in.explicate.fcm.TabFragments.FragmentSuggestionMagarpatta;
import in.explicate.fcm.TabFragments.MyInfrInfoFragment;
import in.explicate.fcm.TabFragments.NewsLetterFragment;
import in.explicate.fcm.TabFragments.OTPFragment;
import in.explicate.fcm.TabFragments.SuggestionFragment;
import in.explicate.fcm.TabFragments.TabFragment;
import in.explicate.fcm.TabFragments.TabFragmentCSM;
import in.explicate.fcm.adapter.CurrentNotificationAdapter;
import in.explicate.fcm.app.Config;
import in.explicate.fcm.application.AppClass;
import in.explicate.fcm.database.DbHandler;
import in.explicate.fcm.model.NotificationModel;
import in.explicate.fcm.util.NotificationUtils;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private  Toolbar toolbar;

    public KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        displaySelectedScreen(R.id.nav_home);

        
        RelativeLayout llhome=(RelativeLayout)navigationView.findViewById(R.id.llmenuhome);
        llhome.setOnClickListener(this);

        RelativeLayout llinfo=(RelativeLayout)navigationView.findViewById(R.id.llmenuinfo);
        llinfo.setOnClickListener(this);


        RelativeLayout llmenusuggestion=(RelativeLayout)navigationView.findViewById(R.id.llmenusuggestion);
        llmenusuggestion.setOnClickListener(this);

        RelativeLayout llmenufeedback=(RelativeLayout)navigationView.findViewById(R.id.llmenufeedback);
        llmenufeedback.setOnClickListener(this);


        RelativeLayout llmenunewsletter=(RelativeLayout)navigationView.findViewById(R.id.llmenunewsletter);
        llmenunewsletter.setOnClickListener(this);

        RelativeLayout llmenucontact=(RelativeLayout)navigationView.findViewById(R.id.llmenucontact);
        llmenucontact.setOnClickListener(this);

        RelativeLayout llmenucommittee=(RelativeLayout)navigationView.findViewById(R.id.llmenucommittee);
        llmenucommittee.setOnClickListener(this);

        RelativeLayout llmenucouuncil=(RelativeLayout)navigationView.findViewById(R.id.llmenucouuncil);
        llmenucouuncil.setOnClickListener(this);

        RelativeLayout llmenusetting=(RelativeLayout)navigationView.findViewById(R.id.llmenusetting);
        llmenusetting.setOnClickListener(this);


        RelativeLayout llmenuclassifieds=(RelativeLayout)navigationView.findViewById(R.id.llmenuclassifieds);
        llmenuclassifieds.setOnClickListener(this);


        RelativeLayout llmenusop=(RelativeLayout)navigationView.findViewById(R.id.llmenusop);
        llmenusop.setOnClickListener(this);

        RelativeLayout llmenuactivity=(RelativeLayout)navigationView.findViewById(R.id.llmenuactivity);
        llmenuactivity.setOnClickListener(this);

        RelativeLayout llmenubadminton=(RelativeLayout)navigationView.findViewById(R.id.llmenubadminton);
        llmenubadminton.setOnClickListener(this);

        RelativeLayout llmenuclubhouse=(RelativeLayout)navigationView.findViewById(R.id.llmenuclubhouse);
        llmenuclubhouse.setOnClickListener(this);

        RelativeLayout llmenucsm=(RelativeLayout)navigationView.findViewById(R.id.llmenucsm);
        llmenucsm.setOnClickListener(this);


        Log.e("USERID", AppClass.getUserId());


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                }
            }
        };

        displayFirebaseRegId();
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(getApplicationContext());

    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {

            case R.id.nav_home:

                fragment = new FragmentHome();
                setTitle("Home");
                toolbar.setSubtitle("Welcome to Magarpatta City");


                break;

            case R.id.nav_my_infra_info:

                fragment = new MyInfrInfoFragment();
                setTitle("My Infra Info");
                toolbar.setSubtitle("Menu/My Infra");


                break;

            case R.id.nav_add_my_infra:

                fragment = new AddInfrInfoFragment();
                setTitle("Add Infra ");
                toolbar.setSubtitle("Menu/Add Infra");

                break;

            case R.id.nav_my_devices:

                fragment = new FragmentDevice();
                setTitle("My Devices ");
                toolbar.setSubtitle("Menu/My Devices");

                break;

            case R.id.nav_my_csm:

                fragment = new FragmentMyCSM();
                setTitle("My CSM ");
                toolbar.setSubtitle("Menu/My CSM");

                break;

            case R.id.nav_availability:

                fragment = new FragmentClubhouse();
                setTitle("Clubhouse Availability ");
                toolbar.setSubtitle("Menu/Availability");

                break;



            case R.id.nav_badminton_availability:

                fragment = new FragmentBadminton();
                setTitle("Badminton Availability");
                toolbar.setSubtitle("Menu/Badminton/Availability");


               /* toolbar.setBackgroundColor(getResources().getColor(R.color.newstoolbar));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.newstoolbar));
                }
                */


                break;

            case R.id.nav_activity_common:

                fragment = new FragmentActivityCommon();
                setTitle("Common");
                toolbar.setSubtitle("Menu/Activity/Common");

              /*  toolbar.setBackgroundColor(getResources().getColor(R.color.newstoolbar));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.newstoolbar));
                }
                */


                break;


            case R.id.nav_activity_my_roject:

                fragment = new FragmentActivityProject();
                setTitle("My Project");
                toolbar.setSubtitle("Menu/Activity/My Project");

              /*  toolbar.setBackgroundColor(getResources().getColor(R.color.newstoolbar));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.newstoolbar));
                }
                */

                break;


            case R.id.nav_standard:

              fragment = new FragmentStandartProcedure();
              setTitle("STANDARD PROCEDURES");

                toolbar.setSubtitle("Menu/Standard Procedure");

              /*  toolbar.setBackgroundColor(getResources().getColor(R.color.newstoolbar));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.newstoolbar));
                }

                */

                break;

            case R.id.nav_settings:


                fragment = new FragmentSetting();
                setTitle("SETTINGS");

                break;

            case R.id.nav_newsletter:


                 fragment = new NewsLetterFragment();
                 setTitle("NEWSLETTER");
                toolbar.setSubtitle("Menu/Newsletter");

             /*   toolbar.setBackgroundColor(getResources().getColor(R.color.newstoolbar));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.newstoolbar));
                }
                */



                break;

            case R.id.nav_suggestion_my_magarpatta:

                fragment = new FragmentSuggestionMagarpatta();
                setTitle("SUGGESTION");

                break;

            case R.id.nav_suggestion_to_committees:

                fragment = new FragmentSuggestionCommittee();
                setTitle("SUGGESTION");

                break;

            case R.id.nav_feedback_my_magarpatta:

                 fragment = new FragmentFeedbackMagarpatta();
                 setTitle("FEEDBACK");


                break;

            case R.id.nav_feedback_to_committees:

                fragment = new FragmentFeedbackCommittee();
                setTitle("FEEDBACK");


                break;


            case R.id.nav_council:

                fragment = new CouncilFragment();

                break;


            case R.id.nav_committes:

                fragment = new ComitteeFragment();

                setTitle("COMMITTEE");
                toolbar.setSubtitle("Menu/Committee");
               /* toolbar.setBackgroundColor(getResources().getColor(R.color.committeetoolbar));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.committeetoolbar));
                }
                */


                break;

            case R.id.nav_contact:

                fragment = new EmergecyContactFragment();

                setTitle("EMERGENCY CONTACT");
                toolbar.setSubtitle("Menu/Emergency Contact");

              /*  toolbar.setBackgroundColor(getResources().getColor(R.color.emptoolbar));


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(R.color.emptoolbar));
                }
                */


                break;

        }

        if(fragment!=null){


            FragmentManager manager1 = getSupportFragmentManager();
            manager1.beginTransaction().replace(R.id.relativelayout_for_fragment, fragment, fragment.getTag()).addToBackStack(null).commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

    }
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {

            super.onBackPressed();

        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());

        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_alert:

                showNotificationDailog();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showNotificationDailog() {

        DbHandler dbHandler=new DbHandler(this);
        dbHandler.open();
        List<NotificationModel> add=dbHandler.getNotifications();
        dbHandler.close();

        Log.e("Notifications",new Gson().toJson(add));

        LayoutInflater inflater =getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_notification, null);

        final Dialog dialog=new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(alertLayout);

        ImageView imgClose=(ImageView)alertLayout.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        RecyclerView recyclerView = (RecyclerView)alertLayout.findViewById(R.id.recyclerView);
        final LinearLayoutManager layoutParams = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutParams);

        TextView tvTextView=(TextView)alertLayout.findViewById(R.id.notextview);
        tvTextView.setVisibility(View.GONE);

        if(add.isEmpty()){
            tvTextView.setVisibility(View.VISIBLE);
        }

        CurrentNotificationAdapter  adapter = new CurrentNotificationAdapter(add,this);
        recyclerView.setAdapter(adapter);

        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.show();

    }

    @Override
    public void onClick(View view) {

        Fragment fragment=null;
        switch (view.getId()){


            case R.id.llmenunewsletter:

                fragment=new NewsLetterFragment();
                appendData("Newsletter","Menu/Newsletter",R.color.toolbarnewsletter,R.color.stausnewsletter);
                openFragment(fragment);

                break;

            case R.id.llmenucontact:

                fragment=new EmergecyContactFragment();
                appendData("Contacts","Menu/Contacts",R.color.toolbarcontact,R.color.stauscontact);
                openFragment(fragment);

                break;

            case R.id.llmenucommittee:

                fragment=new ComitteeFragment();
                appendData("Committee Info","Menu/Committee Info",R.color.toolbarcommittee,R.color.stauscomittee);
                openFragment(fragment);

                break;

            case R.id.llmenucouuncil:

                fragment=new CouncilFragment();
                appendData("Council Info","Menu/Council Info",R.color.toolbarcouncil,R.color.stauscouncil);
                openFragment(fragment);

                break;

            case R.id.llmenusetting:

                fragment=new FragmentSetting();
                appendData("Settings","Menu/Settings",R.color.toolbarcouncil,R.color.stauscouncil);
                openFragment(fragment);

                break;

            case R.id.llmenuhome:

                fragment=new FragmentHome();
                appendData("Home","Welcome to Magarpatta City App",R.color.colorPrimary,R.color.colorPrimaryDark);
                openFragment(fragment);

                break;

            case R.id.llmenusop:

                fragment=new FragmentStandartProcedure();
                appendData("Standard Procedure","Menu/Standard Procedure",R.color.toolbarsop,R.color.staussop);

                openFragment(fragment);

                break;

            case R.id.llmenuinfo:


                showThreeDialog("Info");

                break;

            case R.id.llmenusuggestion:


                showTwoDialog("Suggestion");

                break;

            case R.id.llmenufeedback:


                showTwoDialog("Feedback");

                break;

            case R.id.llmenuclassifieds:


                showTwoDialog("Classifieds");

                break;

            case R.id.llmenuactivity:


                showTwoDialog("Activity");

                break;

            case R.id.llmenubadminton:


                showTwoDialog("Badminton");

                break;

            case R.id.llmenuclubhouse:


                showTwoDialog("Clubhouse");

                break;

            case R.id.llmenucsm:


                showTwoDialog("CSM");

                break;
        }
    }

    private void openFragment(Fragment fragment) {

        if(fragment!=null){

            FragmentManager manager1 = getSupportFragmentManager();
            manager1.beginTransaction().replace(R.id.relativelayout_for_fragment, fragment, fragment.getTag()).addToBackStack(null).commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }

    }

    private void showTwoDialog(final String menu){

        LayoutInflater inflater =getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_submenu_two, null);

        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        ad.setView(alertLayout);

        final AlertDialog alert=ad.create();

        RelativeLayout llone=(RelativeLayout)alertLayout.findViewById(R.id.llonee);
        llone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();

                if(menu.equalsIgnoreCase("Suggestion")){

                    Fragment fragment=new FragmentSuggestionMagarpatta();
                    appendData("To Magarpatta","Menu/Suggestion",R.color.colorPrimary,R.color.colorPrimaryDark);
                    openFragment(fragment);


                }else if(menu.equalsIgnoreCase("Feedback")){

                    Fragment fragment=new FragmentFeedbackMagarpatta();
                    appendData("To Magarpatta","Menu/Feedback/To Magarpatta",R.color.colorPrimary,R.color.colorPrimaryDark);
                    openFragment(fragment);
                }
                else if(menu.equalsIgnoreCase("Classifieds")){

                    Fragment fragment=new FragmentClassfiedMyClassifieds();
                    appendData("My Classifieds","Menu/Classifieds/My Classifieds",R.color.colorPrimary,R.color.colorPrimaryDark);
                    openFragment(fragment);

                }else if(menu.equalsIgnoreCase("Badminton")){

                    Fragment fragment=new FragmentBadminton();
                    appendData("Availability","Menu/Badminton",R.color.colorPrimary,R.color.colorPrimaryDark);
                    openFragment(fragment);

                }else if(menu.equalsIgnoreCase("Activity")){

                    Fragment fragment=new FragmentActivityCommon();
                    appendData("Common Activity","Menu/Activity/Common Activity",R.color.toolbaractivity,R.color.stausactivity);
                    openFragment(fragment);

                }else if(menu.equalsIgnoreCase("CSM")){

                    Fragment fragment=new TabFragmentCSM();
                    appendData("My CSM","Menu/CSM/My CSM",R.color.colorPrimary,R.color.colorPrimaryDark);
                    openFragment(fragment);
                }
                else if(menu.equalsIgnoreCase("Clubhouse")) {

                    Fragment fragment=new FragmentClubhouse();
                    appendData("Booking","Menu/Clubhouse Info/Booking",R.color.colorPrimary,R.color.colorPrimaryDark);
                    openFragment(fragment);
                }
            }
        });

        RelativeLayout lltwo=(RelativeLayout)alertLayout.findViewById(R.id.lltwo);
        lltwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alert.dismiss();

                if(menu.equalsIgnoreCase("Suggestion")){

                    Fragment fragment=new FragmentSuggestionCommittee();
                    appendData("To Committee","Menu/Suggestion/To Committee",R.color.colorPrimary,R.color.colorPrimaryDark);
                    openFragment(fragment);

                }else if(menu.equalsIgnoreCase("Feedback")){

                    Fragment fragment=new FragmentFeedbackCommittee();
                    appendData("To Committee","Menu/Feedback/To Committee",R.color.colorPrimary,R.color.colorPrimaryDark);
                    openFragment(fragment);

                }else if(menu.equalsIgnoreCase("Classifieds")){

                    Fragment fragment=new FragmentClassfiedOtherClassifieds();
                    appendData("Other Classifieds","Menu/Classifieds/Other Classifieds",R.color.colorPrimary,R.color.colorPrimaryDark);
                    openFragment(fragment);

                }else if(menu.equalsIgnoreCase("Badminton")){

                   // Fragment fragment=new FragmentBadminton();
                   // openFragment(fragment);

                }else if(menu.equalsIgnoreCase("Activity")){

                    Fragment fragment=new FragmentActivityProject();
                    appendData("My Project Activity","Menu/Activity/My Project Activity",R.color.toolbaractivity,R.color.stausactivity);
                    openFragment(fragment);

                }else if(menu.equalsIgnoreCase("CSM")){

                    Fragment fragment=new TabFragment();
                    appendData("Other CSM","Menu/CSM/Other CSM",R.color.colorPrimary,R.color.colorPrimaryDark);
                    openFragment(fragment);

                }else if(menu.equalsIgnoreCase("Clubhouse")) {

                    Fragment fragment=new FragmentClubhouseRecreational();
                    appendData("Recreational Classes","Menu/Clubhouse Info/Recreational Classes",R.color.colorPrimary,R.color.colorPrimaryDark);
                    openFragment(fragment);
                }
            }
        });

        ImageView imgone=(ImageView)alertLayout.findViewById(R.id.imgone);
        ImageView imgtwo=(ImageView)alertLayout.findViewById(R.id.imgtwo);

        TextView tvone=(TextView)alertLayout.findViewById(R.id.textone);
        TextView tvtwo=(TextView)alertLayout.findViewById(R.id.texttwo);


        if(menu.equalsIgnoreCase("Suggestion")){

            tvone.setText("To Magarpatta");
            tvtwo.setText("To Committees");


        }else    if(menu.equalsIgnoreCase("Feedback")){

            tvone.setText("To Magarpatta");
            tvtwo.setText("To Committees");
        }
        else    if(menu.equalsIgnoreCase("Classifieds")){

            tvone.setText("My Classifieds");
            tvtwo.setText("Other Classifieds");

        }else if(menu.equalsIgnoreCase("Badminton")){

            tvone.setText("Availability");
            tvtwo.setText("Confirmed Booking");

        }else if(menu.equalsIgnoreCase("Activity")){

            tvone.setText("Common");
            tvtwo.setText("My Project");

        }else if(menu.equalsIgnoreCase("CSM")){

            tvone.setText("My CSM");
            tvtwo.setText("Other CSM");

        }else if(menu.equalsIgnoreCase("Clubhouse")){

            tvone.setText("Booking");
            tvtwo.setText("Recreational Classes");
        }

        alert.show();

    }



    private void showThreeDialog(final String menu){

        LayoutInflater inflater =getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_submenu_three, null);

        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        ad.setView(alertLayout);

        final AlertDialog alert=ad.create();

        RelativeLayout llone=(RelativeLayout)alertLayout.findViewById(R.id.llonee);
        llone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();

                if(menu.equalsIgnoreCase("Info")){

                    Fragment fragment=new MyInfrInfoFragment();
                    appendData("My Residence","Menu/My Info/My Residence",R.color.colorPrimary,R.color.colorPrimaryDark);
                    openFragment(fragment);

                }



            }
        });

        RelativeLayout lltwo=(RelativeLayout)alertLayout.findViewById(R.id.lltwo);
        lltwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alert.dismiss();
                if(menu.equalsIgnoreCase("Info")){

                    Fragment fragment=new AddInfrInfoFragment();
                    appendData("Add Residence","Menu/My Info/Add Residence",R.color.colorPrimary,R.color.colorPrimaryDark);
                    openFragment(fragment);

                }
            }
        });

        RelativeLayout llthree=(RelativeLayout)alertLayout.findViewById(R.id.llthree);
        llthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alert.dismiss();
                if(menu.equalsIgnoreCase("Info")){

                    Fragment fragment=new FragmentDevice();
                    appendData("My Devices","Menu/My Info/My Devices",R.color.colorPrimary,R.color.colorPrimaryDark);
                    openFragment(fragment);

                }
            }
        });


        ImageView imgone=(ImageView)alertLayout.findViewById(R.id.imgone);
        ImageView imgtwo=(ImageView)alertLayout.findViewById(R.id.imgtwo);
        ImageView imgthree=(ImageView)alertLayout.findViewById(R.id.imgthree);


        TextView tvone=(TextView)alertLayout.findViewById(R.id.textone);
        TextView tvtwo=(TextView)alertLayout.findViewById(R.id.texttwo);
        TextView tvthree=(TextView)alertLayout.findViewById(R.id.textthree);



        if(menu.equalsIgnoreCase("Info")){

            tvone.setText("My Residence ");
            tvtwo.setText("Add Residence");
            tvthree.setText("My Devices");

        }
        alert.show();

    }


    private void appendData(String title,String menu,int toolbarColor,int statusColor){

        setTitle(title);
        toolbar.setSubtitle(menu);
        toolbar.setBackgroundColor(getResources().getColor(toolbarColor));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getWindow();
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(getResources().getColor(statusColor));
         }
    }

    public void showPrgressBar(){

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


    public void hideProgressBar(){

        if(progressHUD.isShowing()){
            progressHUD.dismiss();

        }

    }

}
