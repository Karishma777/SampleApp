package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 12/12/17.
 */

public class PendingComplaintAPIModel {

    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

    @SerializedName("Message")
    public String message;


    public class Data{


        @SerializedName("MyPendingComplaints")
        @Expose
        public ArrayList<MyPendingComplaints> result;

        @SerializedName("OtherOldComplaints")
        @Expose
        public ArrayList<MyPendingComplaints> resultOLD;

        @SerializedName("OtherPendingComplaints")
        @Expose
        public ArrayList<MyPendingComplaints> otherPendingComplaints;


    }


    public class MyPendingComplaints{

        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("Complaint No")
        @Expose
        public String complaintNo;

        @SerializedName("Complaint Type")
        @Expose
        public String complaintType;

        @SerializedName("CSM Details")
        @Expose
        public String cSMDetails;

        @SerializedName("Status")
        @Expose
        public String status;

        @SerializedName("CSM No")
        @Expose
        public String csmNO;


        @SerializedName("CSM Type")
        @Expose
        public String csmType;


    }

}
