package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 22/11/17.
 */

public class ActivityProjectDetailAPIModel {

    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

    @SerializedName("Message")
    public String message;


    public class Data{
        @SerializedName("ResActivity Details")
        @Expose
        public ArrayList<ResActivityProject> result;
    }


    public class ResActivityProject{


        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("Subject")
         @Expose
        public String subject;

        @SerializedName("Project")
        @Expose
        public String project;

        @SerializedName("Activity Type")
        @Expose
        public String type;

        @SerializedName("Details")
        @Expose
        public String details;

        @SerializedName("Date")
        @Expose
        public String date;
    }


}
