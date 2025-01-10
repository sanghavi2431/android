package `in`.woloo.www.more.giftSubscription.model

import com.google.gson.annotations.SerializedName

class GetGiftPlansResponse {
    @SerializedName("code")
    val code: Int = 0

    @kotlin.jvm.JvmField
	@SerializedName("data")
    val data: List<DataItem>? = null

    @SerializedName("message")
    val message: String? = null

    @SerializedName("status")
    val status: String? = null

    inner class DataItem {
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
        val isVoucher: Int = 0

        @SerializedName("deleted_at")
        val deletedAt: Any? = null

        @SerializedName("frequency")
        val frequency: String? = null

        @SerializedName("is_recommended")
        val isRecommended: Int = 0

        @SerializedName("is_expired")
        val isExpired: Int = 0

        @SerializedName("apple_product_id")
        val appleProductId: Any? = null

        @SerializedName("updated_at")
        val updatedAt: String? = null

        @SerializedName("price")
        val price: String? = null

        @SerializedName("name")
        val name: String? = null

        @SerializedName("days")
        val days: Int = 0

        @SerializedName("currency")
        val currency: String? = null

        @SerializedName("id")
        val id: Int = 0

        @SerializedName("before_discount_price")
        val beforeDiscountPrice: Any? = null

        @SerializedName("plan_id")
        val planId: String? = null

        @SerializedName("status")
        val status: Int = 0

        @SerializedName("start_at")
        val start_at: String? = null

        @SerializedName("end_at")
        val end_at: String? = null
    }
}