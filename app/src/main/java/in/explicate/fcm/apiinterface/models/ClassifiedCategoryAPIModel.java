package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mahesh on 12/12/17.
 */

public class ClassifiedCategoryAPIModel {

    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

    @SerializedName("Message")
    public String message;


    public class Data{
        @SerializedName("ClassifiedCategories")
        @Expose
        public ArrayList<ClassifiedCategories> classifiedCategories;
    }


    public class ClassifiedCategories{


        @SerializedName("ID")
        @Expose
        public Integer id;

        @SerializedName("CategoryName")
        @Expose
        public String categoryName;

        @SerializedName("Description")
        @Expose
        public String description;
    }

}
