package `in`.woloo.www.more.subscribe.models

import com.google.gson.annotations.SerializedName

class GetSubscriptionDetailsResponse {
    @SerializedName("code")
    val code = 0

    @SerializedName("data")
    val data: Data? = null

    @SerializedName("message")
    val message: String? = null

    @SerializedName("status")
    val status: String? = null

    inner class FutureSubscription {
        @SerializedName("image")
        val image: String? = null

        @SerializedName("backgroud_color")
        val backgroudColor: String? = null

        @SerializedName("price_with_gst")
        val priceWithGst: String? = null

        @SerializedName("description")
        val description: String? = null

        @SerializedName("discount")
        val discount: String? = null

        @SerializedName("created_at")
        val createdAt: String? = null

        @SerializedName("shield_color")
        val shieldColor: String? = null

        @SerializedName("is_voucher")
        val isVoucher = 0

        @SerializedName("deleted_at")
        val deletedAt: Any? = null

        @SerializedName("frequency")
        val frequency: String? = null

        @SerializedName("is_recommended")
        val isRecommended = 0

        @SerializedName("is_expired")
        val isExpired = 0

        @SerializedName("updated_at")
        val updatedAt: String? = null

        @SerializedName("price")
        val price: String? = null

        @SerializedName("name")
        val name: String? = null

        @SerializedName("days")
        val days = 0

        @SerializedName("currency")
        val currency: String? = null

        @SerializedName("id")
        val id = 0

        @SerializedName("before_discount_price")
        val beforeDiscountPrice = 0

        @SerializedName("plan_id")
        val planId: String? = null

        @SerializedName("status")
        val status = 0

        @SerializedName("start_at")
        val start_at: String? = null

        @SerializedName("end_at")
        val end_at: String? = null
    }

    inner class Data {
        @SerializedName("futureSubscription")
        val futureSubscription: ArrayList<FutureSubscription>? = null

        @SerializedName("activeSubscription")
        val activeSubscription: ActiveSubscription? = null

        @SerializedName("purchase_by")
        val purchase_by: String? = null
    }

    inner class ActiveSubscription {
        @SerializedName("image")
        val image: String? = null

        @SerializedName("backgroud_color")
        val backgroudColor: Any? = null

        @SerializedName("price_with_gst")
        val priceWithGst: String? = null

        @SerializedName("description")
        val description: String? = null

        @SerializedName("discount")
        val discount: String? = null

        @SerializedName("created_at")
        val createdAt: String? = null

        @SerializedName("shield_color")
        val shieldColor: Any? = null

        @SerializedName("is_voucher")
        val isVoucher = 0

        @SerializedName("deleted_at")
        val deletedAt: Any? = null

        @SerializedName("frequency")
        val frequency: String? = null

        @SerializedName("is_recommended")
        val isRecommended = 0

        @SerializedName("is_expired")
        val isExpired = 0

        @SerializedName("updated_at")
        val updatedAt: String? = null

        @SerializedName("price")
        val price: String? = null

        @SerializedName("name")
        val name: String? = null

        @SerializedName("days")
        val days: String? = null

        @SerializedName("currency")
        val currency: String? = null

        @SerializedName("id")
        val id = 0

        @SerializedName("before_discount_price")
        val beforeDiscountPrice: Int? = null

        @SerializedName("plan_id")
        val planId: String? = null

        @SerializedName("corporate_name")
        val corporate_name: String? = null

        @SerializedName("status")
        val status = 0

        @SerializedName("start_at")
        val start_at: String? = null

        @SerializedName("end_at")
        val end_at: String? = null

        @SerializedName("gifted_by")
        val gifted_by: String? = null
    }
}