package `in`.woloo.www.application_kotlin.model.server_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewListResponse {
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
        @SerializedName("total_review_count")
        @Expose
        var totalReviewCount: Int? = null

        @SerializedName("review_count")
        @Expose
        var reviewCount: Int? = null

        @SerializedName("review")
        @Expose
        var review: List<Review>? = null

        @SerializedName("next")
        @Expose
        var next: Int? = null
    }

    inner class Review {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("user_id")
        @Expose
        var userId: Int? = null

        @SerializedName("woloo_id")
        @Expose
        var wolooId: Int? = null

        @SerializedName("rating")
        @Expose
        var rating: Int? = null

        @SerializedName("remarks")
        @Expose
        var remarks: Any? = null

        @SerializedName("status")
        @Expose
        var status: Int? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("rating_option")
        @Expose
        var ratingOption: String? = null

        @SerializedName("review_description")
        @Expose
        var reviewDescription: String? = null

        @SerializedName("user_details")
        @Expose
        var userDetails: UserDetails? = null
    }

    inner class UserDetails {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("avatar")
        @Expose
        var avatar: String? = null

        @SerializedName("woloo_since")
        @Expose
        var wolooMemberSince: String? = null

        @SerializedName("base_url")
        @Expose
        var baseUrl: String? = null

    }
}
