package in.explicate.fcm.service;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import in.explicate.fcm.MainActivity;
import in.explicate.fcm.app.Config;
import in.explicate.fcm.database.DbHandler;
import in.explicate.fcm.model.NotificationModel;
import in.explicate.fcm.util.NotificationUtils;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getNotification().getBody());

            //  Log.e("DataNotification:",new Gson().toJson(remoteMessage.getNotification()));
           //if (remoteMessage == null)
          // return;

        //Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

        if (remoteMessage.getData().size() > 0) {
          //  Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
          //  Log.e(TAG, "Data : " + remoteMessage.getData().get("endDate").toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                handleDataMessage(json,remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getClickAction());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {

        /*
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        }else{
            // If the app is in background, firebase itself handles the notification
        }*/

        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        resultIntent.putExtra("message", message);

        //showNotificationMessage(getApplicationContext(), titlw, message, resultIntent);

    }

    private void handleDataMessage(JSONObject data,String title,String msg,String click_action) {
        Log.e(TAG, "push json: " + data.toString());

        try {
           // JSONObject data = json.getJSONObject("data");

            String endDate = data.getString("endDate");
            String pk = data.getString("pk");
            String priority = data.getString("priority");

            NotificationModel model=new NotificationModel();
            model.setTitle(title);
            model.setBody(msg);
            model.setClick_action(click_action);
            model.setEnd_date(endDate);
            model.setPk(pk);
            model.setPriority(priority);

            DbHandler dbHandler =new DbHandler(getApplicationContext());
            dbHandler.open();
            dbHandler.insertNotification(model);
            dbHandler.close();



            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", msg);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
                resultIntent.putExtra("message", msg);

                // check for image attachment
              //  if (TextUtils.isEmpty(imageUrl)) {
                    showNotificationMessage(getApplicationContext(), title, msg, resultIntent);
              //  } else {
                    // image is present, show notification with image
              //      showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
              //  }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent, imageUrl);
    }
}
