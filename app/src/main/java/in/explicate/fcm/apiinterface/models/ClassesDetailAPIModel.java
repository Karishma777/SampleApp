package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 12/12/17.
 */

public class ClassesDetailAPIModel {

    @SerializedName("Status")
    @Expose
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

    @SerializedName("Message")
    @Expose
    public String message;

    public class Data{

        @SerializedName("ClassDetails")
        @Expose
        public ArrayList<ClassDetails> classes;

        @SerializedName("VendorDetails")
        @Expose
        public ArrayList<VendorDetails> vendorDetails;


    }

    public class VendorDetails{

        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("Name")
        @Expose
        public String name;

        @SerializedName("Address")
        @Expose
        public String address;

        @SerializedName("Contact")
        @Expose
        public String contact;

    }


    public class ClassDetails{

        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("Days")
        @Expose
        public String days;

        @SerializedName("Event")
        @Expose
        public String event;

        @SerializedName("Date")
        @Expose
        public String date;

    }
}
