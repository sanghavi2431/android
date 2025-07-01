package in.woloo.www.more.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import in.woloo.www.login.models.LoginResponse;

public class UserProfileMergedResponse implements Serializable {
    @SerializedName("data")
    private Data data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    public void setData(Data data) {
        this.data = data;
    }

    public Data getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public class Data implements Serializable {

        @SerializedName("profile")
        private Profile profile;

        @SerializedName("offerList")
        private List<OfferListItem> offerList;

        @SerializedName("totalCoins")
        private TotalCoins totalCoins;

        @SerializedName("planData")
        private PlanData planData;

        @SerializedName("lifetime_free")
        private String lifetime_free;

        @SerializedName("isFutureSubcriptionExist")
        private String isFutureSubcriptionExist;

        @SerializedName("futureSubcription")
        private FutureSubscription futureSubscription;

        @SerializedName("purchase_by")
        private String purchase_by;

        public String getPurchase_by() {
            return purchase_by;
        }

        public FutureSubscription getFutureSubcription() {
            return futureSubscription;
        }

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

        public void setProfile(Profile profile) {
            this.profile = profile;
        }

        public Profile getProfile() {
            return profile;
        }

        public void setOfferList(List<OfferListItem> offerList) {
            this.offerList = offerList;
        }

        public List<OfferListItem> getOfferList() {
            return offerList;
        }

        public void setTotalCoins(TotalCoins totalCoins) {
            this.totalCoins = totalCoins;
        }

        public TotalCoins getTotalCoins() {
            return totalCoins;
        }

        public void setPlanData(PlanData planData) {
            this.planData = planData;
        }

        public PlanData getPlanData() {
            return planData;
        }
    }

    public class PlanData implements Serializable {

        @SerializedName("image")
        private String image;

        @SerializedName("description")
        private String description;

        @SerializedName("discount")
        private String discount;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("deleted_at")
        private String deletedAt;

        @SerializedName("frequency")
        private String frequency;

        @SerializedName("is_expired")
        private int isExpired;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("price")
        private String price;

        @SerializedName("name")
        private String name;

        @SerializedName("days")
        private String days;

        @SerializedName("currency")
        private String currency;

        @SerializedName("id")
        private int id;

        @SerializedName("plan_id")
        private String planId;

        @SerializedName("start_at")
        private String start_at;

        @SerializedName("end_at")
        private String end_at;

        @SerializedName("is_cancel")
        private boolean is_cancel;

        @SerializedName("status")
        private int status;

        public void setImage(String image) {
            this.image = image;
        }

        public String getImage() {
            return image;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getDiscount() {
            return discount;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setDeletedAt(String deletedAt) {
            this.deletedAt = deletedAt;
        }

        public String getDeletedAt() {
            return deletedAt;
        }

        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        public String getFrequency() {
            return frequency;
        }

        public void setIsExpired(int isExpired) {
            this.isExpired = isExpired;
        }

        public int getIsExpired() {
            return isExpired;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPrice() {
            return price;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getDays() {
            return days;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getCurrency() {
            return currency;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setPlanId(String planId) {
            this.planId = planId;
        }

        public String getPlanId() {
            return planId;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public boolean isIs_cancel() {
            return is_cancel;
        }

        public void setIs_cancel(boolean is_cancel) {
            this.is_cancel = is_cancel;
        }

        public String getEnd_at() {
            return end_at;
        }

        public void setEnd_at(String end_at) {
            this.end_at = end_at;
        }

        public String getStart_at() {
            return start_at;
        }

        public void setStart_at(String start_at) {
            this.start_at = start_at;
        }
    }

    public class Profile implements Serializable {

        @SerializedName("gender")
        private String gender;

        @SerializedName("city")
        private String city;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("is_first_session")
        private int isFirstSession;

        @SerializedName("ref_code")
        private String refCode;

        @SerializedName("subscription_id")
        private String subscriptionId;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("role_id")
        private String roleId;

        @SerializedName("id")
        private int id;

        @SerializedName("woloo_id")
        private String wolooId;

        @SerializedName("email")
        private String email;

        @SerializedName("pincode")
        private String pincode;

        /*@SerializedName("settings")
        private LoginResponse.Setting settings;*/

        @SerializedName("address")
        private String address;

        @SerializedName("expiry_date")
        private String expiryDate;

        @SerializedName("mobile")
        private String mobile;

        @SerializedName("otp")
        private int otp;

        @SerializedName("avatar")
        private String avatar;

        @SerializedName("sponsor_id")
        private String sponsorId;

        @SerializedName("deleted_at")
        private String deletedAt;

        @SerializedName("gp_id")
        private String gpId;

        @SerializedName("fb_id")
        private String fbId;

        @SerializedName("dob")
        private String dob;

        @SerializedName("name")
        private String name;

        @SerializedName("voucher_id")
        private String voucherId;

        @SerializedName("status")
        private String status;

        @SerializedName("gift_subscription_id")
        private String giftSubscriptionId;

        public String getGiftSubscriptionId() {
            return giftSubscriptionId;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getGender() {
            return gender;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCity() {
            return city;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setIsFirstSession(int isFirstSession) {
            this.isFirstSession = isFirstSession;
        }

        public int getIsFirstSession() {
            return isFirstSession;
        }

        public void setRefCode(String refCode) {
            this.refCode = refCode;
        }

        public String getRefCode() {
            return refCode;
        }

        public void setSubscriptionId(String subscriptionId) {
            this.subscriptionId = subscriptionId;
        }

        public String getSubscriptionId() {
            return subscriptionId;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setRoleId(String roleId) {
            this.roleId = roleId;
        }

        public String getRoleId() {
            return roleId;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setWolooId(String wolooId) {
            this.wolooId = wolooId;
        }

        public String getWolooId() {
            return wolooId;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getPincode() {
            return pincode;
        }

        /*public void setSettings(LoginResponse.Setting settings){
            this.settings = settings;
        }

        public LoginResponse.Setting
        getSettings(){
            return settings;
        }*/

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAddress() {
            return address;
        }

        public void setExpiryDate(String expiryDate) {
            this.expiryDate = expiryDate;
        }

        public String getExpiryDate() {
            return expiryDate;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getMobile() {
            return mobile;
        }

        public void setOtp(int otp) {
            this.otp = otp;
        }

        public int getOtp() {
            return otp;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setSponsorId(String sponsorId) {
            this.sponsorId = sponsorId;
        }

        public String getSponsorId() {
            return sponsorId;
        }

        public void setDeletedAt(String deletedAt) {
            this.deletedAt = deletedAt;
        }

        public String getDeletedAt() {
            return deletedAt;
        }

        public void setGpId(String gpId) {
            this.gpId = gpId;
        }

        public String getGpId() {
            return gpId;
        }

        public void setFbId(String fbId) {
            this.fbId = fbId;
        }

        public String getFbId() {
            return fbId;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getDob() {
            return dob;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setVoucherId(String voucherId) {
            this.voucherId = voucherId;
        }

        public String getVoucherId() {
            return voucherId;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }

    public class TotalCoins implements Serializable {

        @SerializedName("total_coins")
        private int totalCoins;

        @SerializedName("gift_coins")
        private int giftCoins;

        public void setTotalCoins(int totalCoins) {
            this.totalCoins = totalCoins;
        }

        public int getTotalCoins() {
            return totalCoins;
        }

        public void setGiftCoins(int giftCoins) {
            this.giftCoins = giftCoins;
        }

        public int getGiftCoins() {
            return giftCoins;
        }
    }

    public class FutureSubscription implements Serializable {

        @SerializedName("image")
        private String image;

        @SerializedName("backgroud_color")
        private String backgroudColor;

        @SerializedName("price_with_gst")
        private String priceWithGst;

        @SerializedName("description")
        private String description;

        @SerializedName("discount")
        private String discount;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("shield_color")
        private String shieldColor;

        @SerializedName("is_voucher")
        private int isVoucher;

        @SerializedName("deleted_at")
        private Object deletedAt;

        @SerializedName("frequency")
        private String frequency;

        @SerializedName("is_recommended")
        private int isRecommended;

        @SerializedName("is_expired")
        private int isExpired;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("price")
        private String price;

        @SerializedName("name")
        private String name;

        @SerializedName("days")
        private String days;

        @SerializedName("currency")
        private String currency;

        @SerializedName("id")
        private int id;

        @SerializedName("before_discount_price")
        private int beforeDiscountPrice;

        @SerializedName("plan_id")
        private String planId;

        @SerializedName("status")
        private int status;

        @SerializedName("start_at")
        private String start_at;

        @SerializedName("end_at")
        private String end_at;

        public String getImage() {
            return image;
        }

        public String getBackgroudColor() {
            return backgroudColor;
        }

        public String getPriceWithGst() {
            return priceWithGst;
        }

        public String getDescription() {
            return description;
        }

        public String getDiscount() {
            return discount;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getShieldColor() {
            return shieldColor;
        }

        public int getIsVoucher() {
            return isVoucher;
        }

        public Object getDeletedAt() {
            return deletedAt;
        }

        public String getFrequency() {
            return frequency;
        }

        public int getIsRecommended() {
            return isRecommended;
        }

        public int getIsExpired() {
            return isExpired;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getPrice() {
            return price;
        }

        public String getName() {
            return name;
        }

        public String getDays() {
            return days;
        }

        public String getCurrency() {
            return currency;
        }

        public int getId() {
            return id;
        }

        public int getBeforeDiscountPrice() {
            return beforeDiscountPrice;
        }

        public String getPlanId() {
            return planId;
        }

        public int getStatus() {
            return status;
        }

        public String getStart_at() {
            return start_at;
        }

        public String getEnd_at() {
            return end_at;
        }
    }

    public class OfferListItem implements Serializable {

        @SerializedName("offer")
        private List<OfferItem> offer;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("user_id")
        private int userId;

        @SerializedName("expiry_date")
        private Object expiryDate;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("id")
        private int id;

        @SerializedName("deleted_at")
        private String deletedAt;

        @SerializedName("offer_id")
        private int offerId;

        @SerializedName("status")
        private int status;

        public List<OfferItem> getOffer() {
            return offer;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public int getUserId() {
            return userId;
        }

        public Object getExpiryDate() {
            return expiryDate;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public int getId() {
            return id;
        }

        public String getDeletedAt() {
            return deletedAt;
        }

        public int getOfferId() {
            return offerId;
        }

        public int getStatus() {
            return status;
        }
    }

    public class OfferItem implements Serializable {

        @SerializedName("end_date")
        private String endDate;

        @SerializedName("image")
        private String image;

        @SerializedName("updated_at")
        private String updatedAt;

        @SerializedName("description")
        private String description;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("id")
        private int id;

        @SerializedName("title")
        private String title;

        @SerializedName("woloo_id")
        private int wolooId;

        @SerializedName("deleted_at")
        private Object deletedAt;

        @SerializedName("start_date")
        private String startDate;

        @SerializedName("status")
        private int status;

        public String getEndDate() {
            return endDate;
        }

        public String getImage() {
            return image;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public String getDescription() {
            return description;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public int getWolooId() {
            return wolooId;
        }

        public Object getDeletedAt() {
            return deletedAt;
        }

        public String getStartDate() {
            return startDate;
        }

        public int getStatus() {
            return status;
        }
    }
}


