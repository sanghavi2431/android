package `in`.woloo.www.application_kotlin.model.server_request

import com.google.gson.annotations.SerializedName

data class VerifyOtpRequest (

    @SerializedName("request_id")
    var requestId: String,

    @SerializedName("otp")
    var otp: String,

    @SerializedName("referral_code")
    var referralCode: String
)