package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 12/12/17.
 */

public class CategoryApiModel {

    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

    @SerializedName("Message")
    public String message;


    public class Data{


        @SerializedName("ComplaintCategoryList")
        @Expose
        public ArrayList<ComplaintCategoryList> result;

        @SerializedName("ComplaintTypesList")
        @Expose
        public ArrayList<ComplaintCategoryList> resultTypes;


    }


    public class ComplaintCategoryList{


        @SerializedName("Id")
        @Expose
        public Integer id;

        @SerializedName("Name")
        @Expose
        public String name;


    }
}
