package `in`.woloo.www.v2.giftcard.model

import com.google.gson.annotations.SerializedName

class ValidateGiftCardResponse {
    @SerializedName("showPopUp")
    var showPopUp : Int = 0

    @SerializedName("message")
    var message : String = ""
}