package `in`.woloo.www.store.reviews

import com.google.gson.annotations.SerializedName

class AddReviewRequest {

    @SerializedName("product_id")
    var productId: String? = null

    @SerializedName("rating")
    var rating: Int? = null

    @SerializedName("comment")
    var comment: String? = null
}
