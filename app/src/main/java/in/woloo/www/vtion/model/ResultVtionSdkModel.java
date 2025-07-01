package in.woloo.www.vtion.model;

public class ResultVtionSdkModel {
    private String mobileNumber;
    private String result;

    private String status;
    private Boolean deleteRequest;

    public ResultVtionSdkModel()
    {

    }

    public ResultVtionSdkModel(String mobileNumber , String result , Boolean deleteRequest)
    {
        this.mobileNumber = mobileNumber;
        this.result = result;
        this.deleteRequest = deleteRequest;
    }
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getDeleteRequest() {
        return deleteRequest;
    }

    public void setDeleteRequest(Boolean deleteRequest) {
        this.deleteRequest = deleteRequest;
    }
}
