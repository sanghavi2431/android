package `in`.woloo.www.more.giftcard.giftcard.model

import com.google.gson.annotations.SerializedName

class AddCoinsRequest {
    @SerializedName("mobile")
    var mobile: String = ""

    @SerializedName("coins")
    var coins: Int = 0

    @SerializedName("message")
    var message: String = ""
}