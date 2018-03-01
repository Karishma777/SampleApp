package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 12/12/17.
 */

public class ClassifiedApiModel {
    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

    @SerializedName("Message")
    public String message;


    public class Data{


        @SerializedName("MyClassifieds")
        @Expose
        public ArrayList<MyClassifieds> result;

        @SerializedName("OthersClassifieds")
        @Expose
        public ArrayList<MyClassifieds> othersClassifieds;

    }


    public class MyClassifieds{


        @SerializedName("ID")
        @Expose
        public Integer id;

        @SerializedName("Id")
        @Expose
        public Integer searchId;

        @SerializedName("Subject")
        @Expose
        public String subject;

        @SerializedName("Description")
        @Expose
        public String description;

        @SerializedName("ResponseCount")
        @Expose
        public Integer responseCount;
    }

}
