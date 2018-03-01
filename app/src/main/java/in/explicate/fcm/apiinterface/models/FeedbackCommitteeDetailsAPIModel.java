package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 22/11/17.
 */

public class FeedbackCommitteeDetailsAPIModel {

    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

    @SerializedName("Message")
    public String message;


    public class Data{


        @SerializedName("CommitteefeedbackDetails")
        @Expose
        public ArrayList<FeedbackDetails> result;

    }


    public class FeedbackDetails{


        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("Subject")
         @Expose
        public String subject;

        @SerializedName("CommitteeName")
        @Expose
        public String committeeName;

        @SerializedName("FeedbackDate")
        @Expose
        public String date;

        @SerializedName("Reply")
        @Expose
        public String reply;

        @SerializedName("ReplyDate")
        @Expose
        public String replyDate;
    }


}
