package `in`.woloo.www.v2.home.model

import com.google.gson.annotations.SerializedName

class ReviewListRequest {
    @SerializedName("woloo_id")
    var wolooId : Int = 0

    @SerializedName("pageNumber")
    var pageNumber : Int = 1
}