package `in`.woloo.www.v2.login.model

import com.google.gson.annotations.SerializedName

class SendOtpRequest {

    @SerializedName("mobileNumber")
    var mobile: String = ""

    @SerializedName("referral_code")
    var referralCode: String = ""
}
