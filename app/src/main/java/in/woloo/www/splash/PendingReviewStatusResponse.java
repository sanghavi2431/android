package in.woloo.www.splash;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PendingReviewStatusResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{

        @SerializedName("woloo_id")
        @Expose
        private Integer wolooId;

        public Integer getWolooId() {
            return wolooId;
        }

        public void setWolooId(Integer wolooId) {
            this.wolooId = wolooId;
        }

    }

}
