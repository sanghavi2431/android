package `in`.woloo.www.v2.home.model

import com.google.gson.annotations.SerializedName

class WolooEngagementRequest {
    @SerializedName("user_id")
    var userId : String = ""

    @SerializedName("woloo_id")
    var wolooId : String = ""

    @SerializedName("like")
    var like : Int = 0
}