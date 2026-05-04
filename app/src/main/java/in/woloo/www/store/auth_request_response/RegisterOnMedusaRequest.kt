package `in`.woloo.www.store.auth_request_response

import com.google.gson.annotations.SerializedName

data class RegisterOnMedusaRequest (
    @SerializedName("email")
    var email : String = "",

    @SerializedName("password")
    var password : String = ""
)