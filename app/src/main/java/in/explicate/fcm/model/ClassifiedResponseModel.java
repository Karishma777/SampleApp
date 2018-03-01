package in.explicate.fcm.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mahesh on 17/12/17.
 */

public class ClassifiedResponseModel implements Serializable {

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<ClassifiedItemModel> getResponse() {
        return response;
    }

    public void setResponse(ArrayList<ClassifiedItemModel> response) {
        this.response = response;
    }

    private int id;
    private String subject;
    private String description;
    private String categoryName;

    private ArrayList<ClassifiedItemModel> response;



}
