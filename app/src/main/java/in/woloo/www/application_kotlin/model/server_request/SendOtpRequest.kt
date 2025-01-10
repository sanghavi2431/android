package `in`.woloo.www.application_kotlin.model.server_request

import com.google.gson.annotations.SerializedName

data class SendOtpRequest (

    @SerializedName("mobileNumber")
    var mobile: String,

    @SerializedName("referral_code")
    var referralCode: String

)