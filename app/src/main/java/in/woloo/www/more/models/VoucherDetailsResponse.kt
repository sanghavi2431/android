package `in`.woloo.www.more.models

import com.google.gson.annotations.SerializedName

class VoucherDetailsResponse {
    @SerializedName("code")
    val code: Int = 0

    @SerializedName("data")
    val data: Data? = null

    @SerializedName("message")
    val message: String? = null

    @SerializedName("status")
    val status: String? = null

    inner class User {
        @SerializedName("gender")
        val gender: String? = null

        @SerializedName("city")
        val city: String? = null

        @SerializedName("created_at")
        val createdAt: String? = null

        @SerializedName("is_first_session")
        val isFirstSession: Int = 0

        @SerializedName("ref_code")
        val refCode: String? = null

        @SerializedName("subscription_id")
        val subscriptionId: String? = null

        @SerializedName("updated_at")
        val updatedAt: String? = null

        @SerializedName("role_id")
        val roleId: Int = 0

        @SerializedName("id")
        val id: Int = 0

        @SerializedName("woloo_id")
        val wolooId: String? = null

        @SerializedName("email")
        val email: String? = null

        @SerializedName("pincode")
        val pincode: String? = null

        @SerializedName("settings")
        val settings: List<String>? = null

        @SerializedName("address")
        val address: String? = null

        @SerializedName("expiry_date")
        val expiryDate: String? = null

        @SerializedName("mobile")
        val mobile: String? = null

        @SerializedName("otp")
        val otp: Int = 0

        @SerializedName("avatar")
        val avatar: String? = null

        @SerializedName("sponsor_id")
        val sponsorId: String? = null

        @SerializedName("deleted_at")
        val deletedAt: String? = null

        @SerializedName("gp_id")
        val gpId: String? = null

        @SerializedName("fb_id")
        val fbId: String? = null

        @SerializedName("dob")
        val dob: String? = null

        @SerializedName("name")
        val name: String? = null

        @SerializedName("voucher_id")
        val voucherId: Int = 0

        @SerializedName("status")
        val status: String? = null
    }

    inner class Subscription {
        @SerializedName("days")
        val days: String? = null
    }

    inner class Data {
        @SerializedName("subscription")
        val subscription: Subscription? = null

        @SerializedName("user")
        val user: User? = null
    }
}