package in.explicate.fcm.apiinterface.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Explicate1 on 12/18/2017.
 */

public class GetInfraSearchModel {

    @SerializedName("Status")
    public String status;

    @SerializedName("Data")
    @Expose
    public Data data;

    @SerializedName("Message")
    public String message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public  class InfraList{


    @SerializedName("Id")
    @Expose
    public Integer id;

    @SerializedName("Infra")
    @Expose
    public String Infra;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInfra() {
        return Infra;
    }

    public void setInfra(String infra) {
        Infra = infra;
    }
}


    public class Data{


        @SerializedName("InfraList")
        @Expose
        public ArrayList<InfraList> result;

    }
}
