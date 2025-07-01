package `in`.woloo.www.v2.login.model

import com.google.gson.annotations.SerializedName
import `in`.woloo.www.v2.splash.UserDetails

class VerifyOtpResponse {

    @SerializedName("token")
    var token: String = ""

    @SerializedName("user_id")
    var supplierId: Int = 0

    @SerializedName("user")
    var user: UserDetails? = null

}