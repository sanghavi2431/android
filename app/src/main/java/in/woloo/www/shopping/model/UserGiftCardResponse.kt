package `in`.woloo.www.shopping.model

import com.google.gson.annotations.SerializedName

class UserGiftCardResponse {
    @SerializedName("woloo_point")
    val wolooPoint: Int = 0

    @SerializedName("success")
    val isSuccess: Boolean = false

    @SerializedName("message")
    val message: String? = null

    @JvmField
	@SerializedName("gift_coins")
    val giftCoins: Int = 0
}