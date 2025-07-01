package `in`.woloo.www.v2.login.model

import com.google.gson.annotations.SerializedName

class VerifyOtpRequest {

    @SerializedName("request_id")
    var requestId: String = ""

    @SerializedName("otp")
    var otp: String = ""

    @SerializedName("referral_code")
    var referralCode: String = ""
}
