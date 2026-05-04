package `in`.woloo.www.more.refer_woloo_host.model

import com.google.gson.annotations.SerializedName

class ReferWolooResponse {
    @SerializedName("data")
    val data: Data? = null

    inner class Data {
        @SerializedName("message")
        val message: String? = null

        @SerializedName("status")
        val status: String? = null
    }
}