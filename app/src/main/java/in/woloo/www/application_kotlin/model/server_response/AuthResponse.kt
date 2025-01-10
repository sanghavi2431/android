package `in`.woloo.www.application_kotlin.model.server_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AuthResponse {
    @SerializedName("code")
    @Expose
    var code: Int? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data {
        @SerializedName("key")
        @Expose
        var key: String? = null

        @SerializedName("cert")
        @Expose
        var cert: String? = null
    }
}
