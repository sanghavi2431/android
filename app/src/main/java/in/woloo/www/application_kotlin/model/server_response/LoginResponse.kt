package `in`.woloo.www.application_kotlin.model.server_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponse {
    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data {
        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("user")
        @Expose
        var user: User? = null

        @SerializedName("token")
        @Expose
        var token: String? = null
    }

    inner class User {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("role_id")
        @Expose
        var roleId: String? = null

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
        var sponsorId: String? = null

        @SerializedName("woloo_id")
        @Expose
        var wolooId: String? = null

        @SerializedName("subscription_id")
        @Expose
        var subscriptionId: String? = null

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

        /*public Setting getSettings() {
            return settings;
        }

        public void setSettings(Setting settings) {
            this.settings = settings;
        }*/
        /*@SerializedName("settings")
                 @Expose
                 private Setting settings = null;*/
        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("deleted_at")
        @Expose
        var deletedAt: String? = null

        @SerializedName("is_first_session")
        @Expose
        var is_first_session: Int? = null

        @SerializedName("isFreeTrial")
        @Expose
        var isFreeTrial: Int? = null
        var gender: String? = null
    }

    class Setting {
        @SerializedName("locale")
        val locale: String? = null
    }
}
