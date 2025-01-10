package `in`.woloo.www.application_kotlin.model.server_request

import com.google.gson.annotations.SerializedName

class VoucherRequest {

    @SerializedName("voucher")
    var voucherCode: String = ""

    @SerializedName("forceApply")
    var forceApply: Boolean = false

}