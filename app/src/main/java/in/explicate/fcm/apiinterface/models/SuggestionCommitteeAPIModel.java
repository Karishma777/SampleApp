package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 22/11/17.
 */

public class SuggestionCommitteeAPIModel {

    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

    @SerializedName("Message")
    public String message;


    public class Data{


        @SerializedName("CommitteeSuggestionslist")
        @Expose
        public ArrayList<Feedbacklist> result;

        @SerializedName("CommitteeSuggestionlist")
        @Expose
        public ArrayList<Feedbacklist> suggresult;


    }


    public class Feedbacklist{


        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("Subject")
         @Expose
        public String subject;


        @SerializedName("CommitteeName")
        @Expose
        public String committeeName;

        @SerializedName("SuggestionDate")
        @Expose
        public String date;
    }


}
