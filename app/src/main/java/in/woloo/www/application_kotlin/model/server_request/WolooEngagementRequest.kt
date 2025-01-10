package `in`.woloo.www.application_kotlin.model.server_request

import com.google.gson.annotations.SerializedName

class WolooEngagementRequest {
    @SerializedName("user_id")
    var userId : String = ""

    @SerializedName("woloo_id")
    var wolooId : String = ""

    @SerializedName("like")
    var like : Int = 0
}