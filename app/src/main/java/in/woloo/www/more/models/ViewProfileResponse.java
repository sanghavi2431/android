package in.woloo.www.more.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import in.woloo.www.login.models.LoginResponse;

public class ViewProfileResponse {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user_data")
    @Expose
    private UserData userData;
    @SerializedName("lifetime_free")
    private String lifetime_free;

    @SerializedName("isFutureSubcriptionExist")
    private String isFutureSubcriptionExist;

    public String getLifetime_free() {
        return lifetime_free;
    }

    public void setLifetime_free(String lifetime_free) {
        this.lifetime_free = lifetime_free;
    }

    public String getIsFutureSubcriptionExist() {
        return isFutureSubcriptionExist;
    }

    public void setIsFutureSubcriptionExist(String isFutureSubcriptionExist) {
        this.isFutureSubcriptionExist = isFutureSubcriptionExist;
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

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }


    public class UserData{

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("role_id")
        @Expose
        private String roleId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
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
        private String fbId;
        @SerializedName("gp_id")
        @Expose
        private String gpId;
        @SerializedName("ref_code")
        @Expose
        private String refCode;
        @SerializedName("sponsor_id")
        @Expose
        private String sponsorId;
        @SerializedName("woloo_id")
        @Expose
        private String wolooId;
        @SerializedName("subscription_id")
        @Expose
        private String subscriptionId;
        @SerializedName("expiry_date")
        @Expose
        private String expiryDate;
        @SerializedName("voucher_id")
        @Expose
        private String voucherId;
        @SerializedName("otp")
        @Expose
        private Integer otp;
        @SerializedName("status")
        @Expose
        private String status;
        /*@SerializedName("settings")
        @Expose
        private LoginResponse.Setting settings;*/
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private String deletedAt;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("dob")
        @Expose
        private String dob;

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getRoleId() {
            return roleId;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
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

        public String getFbId() {
            return fbId;
        }

        public void setFbId(String fbId) {
            this.fbId = fbId;
        }

        public String getGpId() {
            return gpId;
        }

        public void setGpId(String gpId) {
            this.gpId = gpId;
        }

        public String getRefCode() {
            return refCode;
        }

        public void setRefCode(String refCode) {
            this.refCode = refCode;
        }

        public String getSponsorId() {
            return sponsorId;
        }

        public void setSponsorId(String sponsorId) {
            this.sponsorId = sponsorId;
        }

        public String getWolooId() {
            return wolooId;
        }

        public void setWolooId(String wolooId) {
            this.wolooId = wolooId;
        }

        public String getSubscriptionId() {
            return subscriptionId;
        }

        public void setSubscriptionId(String subscriptionId) {
            this.subscriptionId = subscriptionId;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }

        public String getVoucherId() {
            return voucherId;
        }

        public void setVoucherId(String voucherId) {
            this.voucherId = voucherId;
        }

        public Integer getOtp() {
            return otp;
        }

        public void setOtp(Integer otp) {
            this.otp = otp;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        /*public LoginResponse.Setting getSettings() {
            return settings;
        }

        public void setSettings(LoginResponse.Setting settings) {
            this.settings = settings;
        }*/

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

        public String getDeletedAt() {
            return deletedAt;
        }

        public void setDeletedAt(String deletedAt) {
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
