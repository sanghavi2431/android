package in.woloo.www.subscribe.razorpay.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RazorPayResponse {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user")
    @Expose
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public class User{
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("role_id")
        @Expose
        private Object roleId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private Object email;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("pincode")
        @Expose
        private String pincode;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("avatar")
        @Expose
        private String avatar;
        @SerializedName("fb_id")
        @Expose
        private Object fbId;
        @SerializedName("gp_id")
        @Expose
        private Object gpId;
        @SerializedName("ref_code")
        @Expose
        private Object refCode;
        @SerializedName("sponsor_id")
        @Expose
        private Object sponsorId;
        @SerializedName("woloo_id")
        @Expose
        private Object wolooId;
        @SerializedName("subscription_id")
        @Expose
        private Integer subscriptionId;
        @SerializedName("expiry_date")
        @Expose
        private String expiryDate;
        @SerializedName("voucher_id")
        @Expose
        private Object voucherId;
        @SerializedName("otp")
        @Expose
        private Integer otp;
        @SerializedName("status")
        @Expose
        private Object status;
        @SerializedName("settings")
        @Expose
        private List<Object> settings = null;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;
        @SerializedName("gender")
        @Expose
        private String gender;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Object getRoleId() {
            return roleId;
        }

        public void setRoleId(Object roleId) {
            this.roleId = roleId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getEmail() {
            return email;
        }

        public void setEmail(Object email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public Object getFbId() {
            return fbId;
        }

        public void setFbId(Object fbId) {
            this.fbId = fbId;
        }

        public Object getGpId() {
            return gpId;
        }

        public void setGpId(Object gpId) {
            this.gpId = gpId;
        }

        public Object getRefCode() {
            return refCode;
        }

        public void setRefCode(Object refCode) {
            this.refCode = refCode;
        }

        public Object getSponsorId() {
            return sponsorId;
        }

        public void setSponsorId(Object sponsorId) {
            this.sponsorId = sponsorId;
        }

        public Object getWolooId() {
            return wolooId;
        }

        public void setWolooId(Object wolooId) {
            this.wolooId = wolooId;
        }

        public Integer getSubscriptionId() {
            return subscriptionId;
        }

        public void setSubscriptionId(Integer subscriptionId) {
            this.subscriptionId = subscriptionId;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }

        public Object getVoucherId() {
            return voucherId;
        }

        public void setVoucherId(Object voucherId) {
            this.voucherId = voucherId;
        }

        public Integer getOtp() {
            return otp;
        }

        public void setOtp(Integer otp) {
            this.otp = otp;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public List<Object> getSettings() {
            return settings;
        }

        public void setSettings(List<Object> settings) {
            this.settings = settings;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public Object getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(Object deletedAt) {
            this.deletedAt = deletedAt;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }
    }

}

