package `in`.woloo.www.more.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class EditProfileResponse {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("user_data")
    @Expose
    var userData: UserData? = null

    inner class UserData {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("role_id")
        @Expose
        var roleId: Any? = null

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

        @SerializedName("avatar")
        @Expose
        var avatar: String? = null

        @SerializedName("fb_id")
        @Expose
        var fbId: String? = null

        @SerializedName("gp_id")
        @Expose
        var gpId: String? = null

        @SerializedName("ref_code")
        @Expose
        var refCode: String? = null

        @SerializedName("sponsor_id")
        @Expose
        var sponsorId: Any? = null

        @SerializedName("woloo_id")
        @Expose
        var wolooId: Any? = null

        @SerializedName("subscription_id")
        @Expose
        var subscriptionId: Any? = null

        @SerializedName("expiry_date")
        @Expose
        var expiryDate: String? = null

        @SerializedName("voucher_id")
        @Expose
        var voucherId: Any? = null

        @SerializedName("otp")
        @Expose
        var otp: Int? = null

        @SerializedName("status")
        @Expose
        var status: Any? = null

        /*public JSONObject getSettings() {
           return settings;
       }

       public void setSettings(JSONObject settings) {
           this.settings = settings;
       }*/
        /*@SerializedName("settings")
                @Expose
                private JSONObject settings = null;*/
        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("deleted_at")
        @Expose
        var deletedAt: Any? = null
    }
}


