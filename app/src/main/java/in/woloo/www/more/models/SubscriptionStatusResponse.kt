package `in`.woloo.www.more.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SubscriptionStatusResponse {
    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data {
        @SerializedName("userData")
        @Expose
        var userData: UserData? = null

        @SerializedName("planData")
        @Expose
        var planData: PlanData? = null
    }

    inner class UserData {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("role_id")
        @Expose
        var roleId: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("email")
        @Expose
        var email: String? = null

        @SerializedName("mobile")
        @Expose
        var mobile: Any? = null

        @SerializedName("city")
        @Expose
        var city: Any? = null

        @SerializedName("pincode")
        @Expose
        var pincode: Any? = null

        @SerializedName("address")
        @Expose
        var address: Any? = null

        @SerializedName("avatar")
        @Expose
        var avatar: String? = null

        @SerializedName("fb_id")
        @Expose
        var fbId: Any? = null

        @SerializedName("gp_id")
        @Expose
        var gpId: Any? = null

        @SerializedName("ref_code")
        @Expose
        var refCode: Any? = null

        @SerializedName("sponsor_id")
        @Expose
        var sponsorId: Any? = null

        @SerializedName("woloo_id")
        @Expose
        var wolooId: Any? = null

        @SerializedName("subscription_id")
        @Expose
        var subscriptionId: Int? = null

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

        @SerializedName("settings")
        @Expose
        var settings: List<Any>? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("deleted_at")
        @Expose
        var deletedAt: Any? = null

        @SerializedName("gender")
        @Expose
        var gender: Any? = null

        @SerializedName("is_first_session")
        @Expose
        var isFirstSession: Int? = null
    }

    inner class PlanData {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("frequency")
        @Expose
        var frequency: String? = null

        @SerializedName("days")
        @Expose
        var days: Int? = null

        @SerializedName("image")
        @Expose
        var image: String? = null

        @SerializedName("price")
        @Expose
        var price: String? = null

        @SerializedName("discount")
        @Expose
        var discount: Any? = null

        @SerializedName("is_expired")
        @Expose
        var isExpired: Int? = null

        @SerializedName("status")
        @Expose
        var status: Int? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("deleted_at")
        @Expose
        var deletedAt: Any? = null

        @SerializedName("plan_id")
        @Expose
        var planId: String? = null

        @SerializedName("currency")
        @Expose
        var currency: String? = null
    }
}
