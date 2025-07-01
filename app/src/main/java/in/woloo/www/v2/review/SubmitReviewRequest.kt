package `in`.woloo.www.v2.review

import com.google.gson.annotations.SerializedName

class SubmitReviewRequest {
    @SerializedName("woloo_id")
    var wolooId: Int = 0

    @SerializedName("rating")
    var rating: Int = 0

    @SerializedName("rating_option")
    var ratingOption: ArrayList<Int> = ArrayList()

    @SerializedName("review_description")
    var reviewDescription: String = ""
}