package `in`.woloo.www.more.giftSubscription.model

import com.google.gson.annotations.SerializedName

class SendGiftCardResponse {
    @SerializedName("code")
    val code: Int = 0

    @JvmField
	@SerializedName("message")
    val message: String? = null

    @JvmField
	@SerializedName("order_id")
    val orderId: String? = null

    @JvmField
	@SerializedName("status")
    val status: String? = null
}