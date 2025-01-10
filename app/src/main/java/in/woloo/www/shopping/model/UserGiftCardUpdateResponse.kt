package `in`.woloo.www.shopping.model

import com.google.gson.annotations.SerializedName

class UserGiftCardUpdateResponse {
    @kotlin.jvm.JvmField
	@SerializedName("transaction_id")
    val transaction_id: Int = 0

    @SerializedName("success")
    val isSuccess: Boolean = false

    @SerializedName("message")
    val message: String? = null

    @SerializedName("gift_coins")
    private val giftCoins = 0
}