package `in`.woloo.www.more.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ViewProfileResponse {
    @JvmField
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @JvmField
    @SerializedName("user_data")
    @Expose
    var userData: UserData? = null

    @SerializedName("lifetime_free")
    var lifetime_free: String? = null

    @SerializedName("isFutureSubcriptionExist")
    var isFutureSubcriptionExist: String? = null


    inner class UserData {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("role_id")
        @Expose
        var roleId: String? = null

        @JvmField
        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("email")
        @Expose
        var email: String? = null

        @SerializedName("mobile")
        @Expose
        var mobile: String? = null

        @SerializedName("city")
        @Expose
        var city: String? = null

        @SerializedName("pincode")
        @Expose
        var pincode: String? = null

        @SerializedName("address")
        @Expose
        var address: String? = null

        @JvmField
        @SerializedName("avatar")
        @Expose
        var avatar: String? = null

        @SerializedName("fb_id")
        @Expose
        var fbId: String? = null

        @SerializedName("gp_id")
        @Expose
        var gpId: String? = null

        @JvmField
        @SerializedName("ref_code")
        @Expose
        var refCode: String? = null

        @SerializedName("sponsor_id")
        @Expose
        var sponsorId: String? = null

        @SerializedName("woloo_id")
        @Expose
        var wolooId: String? = null

        @SerializedName("subscription_id")
        @Expose
        var subscriptionId: String? = null

        @JvmField
        @SerializedName("expiry_date")
        @Expose
        var expiryDate: String? = null

        @SerializedName("voucher_id")
        @Expose
        var voucherId: String? = null

        @SerializedName("otp")
        @Expose
        var otp: Int? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        /*public LoginResponse.Setting getSettings() {
           return settings;
       }

       public void setSettings(LoginResponse.Setting settings) {
           this.settings = settings;
       }*/
        /*@SerializedName("settings")
                @Expose
                private LoginResponse.Setting settings;*/
        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("deleted_at")
        @Expose
        var deletedAt: String? = null

        @SerializedName("gender")
        @Expose
        var gender: String? = null

        @SerializedName("dob")
        @Expose
        var dob: String? = null
    }
}
