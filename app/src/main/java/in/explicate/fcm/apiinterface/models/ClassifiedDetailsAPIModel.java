package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 17/12/17.
 */

public class ClassifiedDetailsAPIModel {

    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

    @SerializedName("Message")
    public String message;


    public class Data{


        @SerializedName("MyClassifiedDetails")
        @Expose
        public ArrayList<MyClassifiedDetails> result;

        @SerializedName("OthersClassifiedDetails")
        @Expose
        public ArrayList<MyClassifiedDetails> otherresult;

    }


    public class MyClassifiedDetails{


        @SerializedName("ID")
        @Expose
        public Integer id;

        @SerializedName("Subject")
        @Expose
        public String subject;

        @SerializedName("Description")
        @Expose
        public String description;

        @SerializedName("CategoryName")
        @Expose
        public String categoryName;

        @SerializedName("Responses")
        @Expose
        public ArrayList<Responses> responses;
    }

    public class Responses{


        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("Response")
        @Expose
        public String response;

        @SerializedName("ResponseBy")
        @Expose
        public String responseBy;

        @SerializedName("ResponseDate")
        @Expose
        public String responseDate;

        @SerializedName("Replies")
        @Expose
        public ArrayList<Replies> replies;
    }


    public class Replies{


        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("Reply")
        @Expose
        public String reply;

        @SerializedName("ReplyBy")
        @Expose
        public String replyBy;

        @SerializedName("ReplyDate")
        @Expose
        public String replyDate;

    }

}
