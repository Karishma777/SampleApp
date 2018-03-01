package in.explicate.fcm.TabFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.explicate.fcm.R;
import in.explicate.fcm.database.DbHandler;
import in.explicate.fcm.model.EmergencyContact;
import in.explicate.fcm.model.NotificationModel;

/**
 * Created by Mahesh on 26/11/17.
 */

public class FragmentHome extends Fragment {

    private TextView notificationCount;
    private  View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        setupViews();



        return rootView;
    }

    private void setupViews() {

        notificationCount=(TextView)rootView.findViewById(R.id.notifcationCount);
        notificationCount.setText(getNotificationCount());


        swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                notificationCount.setText(getNotificationCount());
            }
        });


        FloatingActionButton btnRefresh=(FloatingActionButton)rootView.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                notificationCount.setText(getNotificationCount());

            }
        });


    }


    private String getNotificationCount(){

        try{

            swipeRefreshLayout.setRefreshing(false);

            DbHandler dbHandler=new DbHandler(getActivity());
            dbHandler.open();
            List<NotificationModel> list=dbHandler.getNotifications();
            dbHandler.close();

            return  list.size()+"";


        }catch (Exception e){

            e.printStackTrace();

        }

        return "0";

    }


}

