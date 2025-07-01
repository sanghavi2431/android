package `in`.woloo.www.v2.home.model

import com.google.gson.annotations.SerializedName

class Voucher {

    @SerializedName("message")
    var message: String = ""

    @SerializedName("isAlreadyApplied")
    var isAlreadyApplied: Boolean = false

    @SerializedName("isAlreadyConsumed")
    var isAlreadyConsumed: Boolean = false

    @SerializedName("isLifetime")
    var isLifetime: Int = 0

    @SerializedName("typeOfVoucher")
    var typeOfVoucher: String = ""

    @SerializedName("days")
    var days: Int = 0

    @SerializedName("expiryNote")
    var expiryNote: String = ""
}