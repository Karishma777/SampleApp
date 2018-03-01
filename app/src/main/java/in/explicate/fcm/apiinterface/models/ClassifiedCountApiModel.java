package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 12/12/17.
 */

public class ClassifiedCountApiModel {

    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

    @SerializedName("Message")
    public String message;


    public class Data{


        @SerializedName("MyClassifiedsCount")
        @Expose
        public ArrayList<MyClassifiedsCount> myClassifiedsCountsult;


        @SerializedName("OthersClassifiedsCount")
        @Expose
        public ArrayList<MyClassifiedsCount> otherClassifiedsCountsult;


    }


    public class MyClassifiedsCount{


        @SerializedName("ID")
        @Expose
        public Integer id;

        @SerializedName("CategoryName")
        @Expose
        public String categoryName;

        @SerializedName("Count")
        @Expose
        public Integer count;
    }

}
