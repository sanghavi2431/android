package `in`.woloo.www.store.reviews

import com.google.gson.annotations.SerializedName

class ReviewsResponse {
    @SerializedName("success")
    var success: Boolean? = null
    @SerializedName("data")
    var data: Data? = null

}

class Data {
    @SerializedName("product_id")
    var product_id: String? = null

    @SerializedName("reviews")
    var reviews: ArrayList<ReviewListData>? = null
}




