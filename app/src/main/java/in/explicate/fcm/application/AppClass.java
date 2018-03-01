package in.explicate.fcm.application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import in.explicate.fcm.util.Constants;
import in.explicate.fcm.util.FontsOverride;


/**
 * Created by Mahesh on 26/11/17.
 */

public class AppClass extends Application {


    public static SharedPreferences sharedPreferences;


    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences=getSharedPreferences(Constants.APP_PREF, Context.MODE_PRIVATE);

        FontsOverride.setDefaultFont(this, "DEFAULT", "proxima.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "proxima.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "proxima.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "proxima.ttf");
    }

    public static void setFont(float fontSize,String name){

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putFloat(Constants.FONT_SIZE,fontSize);
        editor.putString(Constants.FONT_NAME,name);
        editor.commit();

    }


    public static void setSharedPreferences(String user_id){

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(Constants.USER_ID,user_id);
        editor.commit();

    }





    public static  float getFont(){
        return sharedPreferences.getFloat(Constants.FONT_SIZE,0);
    }

    public static  String getFontName(){
        return sharedPreferences.getString(Constants.FONT_NAME,"");
    }

    public static String getUserId(){

        return sharedPreferences.getString(Constants.USER_ID,"");
    }

    public static void setNotificationTone(String uri,String name){
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(Constants.TONE_NAME,name);
        editor.putString(Constants.TONE_URL,uri);
        editor.commit();
    }

    public static  String getToneURL(){
        return sharedPreferences.getString(Constants.TONE_URL,"");
    }

    public static String getToneName(){

        return sharedPreferences.getString(Constants.TONE_NAME,"");
    }


}
