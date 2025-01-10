package `in`.woloo.www.application_kotlin.model.server_response

import com.google.gson.annotations.SerializedName
import `in`.woloo.www.application_kotlin.model.lists_models.UserDetails

data class VerifyOtpResponse (

    @SerializedName("token")
    var token: String = "",

    @SerializedName("user_id")
    var supplierId: Int = 0 ,

    @SerializedName("user")
    var user: UserDetails? = null

)