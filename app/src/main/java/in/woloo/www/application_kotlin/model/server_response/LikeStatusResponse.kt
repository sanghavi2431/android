package `in`.woloo.www.application_kotlin.model.server_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LikeStatusResponse {
    @SerializedName("code")
    @Expose
    var code: Int? = null

    @JvmField
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("is_liked")
    @Expose
    var isLiked: Int? = null
}
