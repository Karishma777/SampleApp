package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 17/12/17.
 */

public class ClubhouseEventApiModel {

    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

    @SerializedName("Message")
    public String message;


    public class Data{


        @SerializedName("ClubhouseEventTypesList")
        @Expose
        public ArrayList<ClubhouseEventTypesList> result;

    }


    public class ClubhouseEventTypesList{


        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("Name")
        @Expose
        public String name;

    }

}
