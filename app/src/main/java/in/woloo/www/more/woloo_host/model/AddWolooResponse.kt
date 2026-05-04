package `in`.woloo.www.more.woloo_host.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddWolooResponse {
    @kotlin.jvm.JvmField
    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data {
        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("message")
        @Expose
        var message: String? = null
    }
}
