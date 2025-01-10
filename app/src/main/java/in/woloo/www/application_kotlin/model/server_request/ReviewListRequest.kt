package `in`.woloo.www.application_kotlin.model.server_request

import com.google.gson.annotations.SerializedName

class ReviewListRequest {
    @SerializedName("woloo_id")
    var wolooId : Int = 0

    @SerializedName("pageNumber")
    var pageNumber : Int = 1
}