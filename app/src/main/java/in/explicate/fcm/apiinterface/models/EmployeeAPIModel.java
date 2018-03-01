package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 22/11/17.
 */

public class EmployeeAPIModel {

    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

    @SerializedName("Message")
     public String message;


    public class Data{


        @SerializedName("EmergencyContactList")
        @Expose
        public ArrayList<ResActivity> result;

        @SerializedName("ModifiedEmergencyContactList")
        @Expose
        public ArrayList<ResActivity> modifiedlist;

        @SerializedName("NewlyCreatedEmergencyContactList")
        @Expose
        public ArrayList<ResActivity> newClist;

    }


    public class ResActivity{


        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("Name")
         @Expose
        public String name;

        @SerializedName("Mobile")
        @Expose
        public String mobile;

        @SerializedName("Email")
        @Expose
        public String email;

        @SerializedName("Description")
        @Expose
        public String description;

        @SerializedName("CreatedOn")
        @Expose
        public String createdOn;

        @SerializedName("ModifiedOn")
        @Expose
        public String modifiedOn;


    }


}
