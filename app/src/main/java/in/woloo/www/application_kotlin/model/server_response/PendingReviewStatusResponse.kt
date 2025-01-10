package `in`.woloo.www.application_kotlin.model.server_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PendingReviewStatusResponse {
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
        @SerializedName("woloo_id")
        @Expose
        var wolooId: Int? = null
    }
}
