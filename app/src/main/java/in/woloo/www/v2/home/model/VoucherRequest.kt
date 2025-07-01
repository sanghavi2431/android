package `in`.woloo.www.v2.home.model

import com.google.gson.annotations.SerializedName

class VoucherRequest {

    @SerializedName("voucher")
    var voucherCode: String = ""

    @SerializedName("forceApply")
    var forceApply: Boolean = false

}