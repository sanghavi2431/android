package `in`.woloo.www.application_kotlin.model.server_response

import com.google.gson.annotations.SerializedName

data class SendOtpResponse (

    @SerializedName("request_id")
    var requestId: String

)