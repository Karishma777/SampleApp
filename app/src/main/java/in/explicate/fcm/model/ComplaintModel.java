package in.explicate.fcm.model;

/**
 * Created by Mahesh on 12/12/17.
 */

public class ComplaintModel {

    private Integer id;
    private String complaintNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComplaintNo() {
        return complaintNo;
    }

    public void setComplaintNo(String complaintNo) {
        this.complaintNo = complaintNo;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String complaintType) {
        this.complaintType = complaintType;
    }

    public String getcSMDetails() {
        return cSMDetails;
    }

    public void setcSMDetails(String cSMDetails) {
        this.cSMDetails = cSMDetails;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String complaintType;
    private String cSMDetails;
    private String status;

}
