package in.woloo.www.more.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubscriptionStatusResponse {

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

        @SerializedName("userData")
        @Expose
        private UserData userData;
        @SerializedName("planData")
        @Expose
        private PlanData planData;

        public UserData getUserData() {
            return userData;
        }

        public void setUserData(UserData userData) {
            this.userData = userData;
        }

        public PlanData getPlanData() {
            return planData;
        }

        public void setPlanData(PlanData planData) {
            this.planData = planData;
        }
    }

    public class UserData{

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("role_id")
        @Expose
        private Integer roleId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobile")
        @Expose
        private Object mobile;
        @SerializedName("city")
        @Expose
        private Object city;
        @SerializedName("pincode")
        @Expose
        private Object pincode;
        @SerializedName("address")
        @Expose
        private Object address;
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
        private Object gender;
        @SerializedName("is_first_session")
        @Expose
        private Integer isFirstSession;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getRoleId() {
            return roleId;
        }

        public void setRoleId(Integer roleId) {
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

        public Object getMobile() {
            return mobile;
        }

        public void setMobile(Object mobile) {
            this.mobile = mobile;
        }

        public Object getCity() {
            return city;
        }

        public void setCity(Object city) {
            this.city = city;
        }

        public Object getPincode() {
            return pincode;
        }

        public void setPincode(Object pincode) {
            this.pincode = pincode;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
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

        public Object getGender() {
            return gender;
        }

        public void setGender(Object gender) {
            this.gender = gender;
        }

        public Integer getIsFirstSession() {
            return isFirstSession;
        }

        public void setIsFirstSession(Integer isFirstSession) {
            this.isFirstSession = isFirstSession;
        }
    }

    public class PlanData{
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("frequency")
        @Expose
        private String frequency;
        @SerializedName("days")
        @Expose
        private Integer days;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("discount")
        @Expose
        private Object discount;
        @SerializedName("is_expired")
        @Expose
        private Integer isExpired;
        @SerializedName("status")
        @Expose
        private Integer status;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("deleted_at")
        @Expose
        private Object deletedAt;
        @SerializedName("plan_id")
        @Expose
        private String planId;
        @SerializedName("currency")
        @Expose
        private String currency;

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFrequency() {
            return frequency;
        }

        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        public Integer getDays() {
            return days;
        }

        public void setDays(Integer days) {
            this.days = days;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public Object getDiscount() {
            return discount;
        }

        public void setDiscount(Object discount) {
            this.discount = discount;
        }

        public Integer getIsExpired() {
            return isExpired;
        }

        public void setIsExpired(Integer isExpired) {
            this.isExpired = isExpired;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
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

        public String getPlanId() {
            return planId;
        }

        public void setPlanId(String planId) {
            this.planId = planId;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }
    }

}
