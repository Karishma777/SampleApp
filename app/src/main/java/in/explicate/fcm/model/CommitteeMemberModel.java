package in.explicate.fcm.model;

/**
 * Created by Mahesh on 28/11/17.
 */

public class CommitteeMemberModel {

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Integer id;
    private String name;

    public String getInfra() {
        return Infra;
    }

    public void setInfra(String infra) {
        Infra = infra;
    }

    private String Infra;


}
