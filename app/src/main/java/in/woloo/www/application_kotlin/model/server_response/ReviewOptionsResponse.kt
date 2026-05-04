package `in`.woloo.www.application_kotlin.model.server_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReviewOptionsResponse {
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
        @JvmField
        @SerializedName("rating_option")
        @Expose
        var ratingOption: List<RatingOption>? = null

        @JvmField
        @SerializedName("rating_review")
        @Expose
        var ratingReview: List<RatingReview>? = null

        @JvmField
        @SerializedName("rating_improvement")
        @Expose
        var ratingImprovement: List<RatingImprovement>? = null
    }

    inner class RatingImprovement {
        @JvmField
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("key")
        @Expose
        var key: String? = null

        @JvmField
        @SerializedName("display_name")
        @Expose
        var displayName: String? = null

        @SerializedName("value")
        @Expose
        var value: String? = null

        @SerializedName("details")
        @Expose
        var details: Any? = null

        @SerializedName("type")
        @Expose
        var type: String? = null

        @SerializedName("order")
        @Expose
        var order: Int? = null

        @SerializedName("group")
        @Expose
        var group: String? = null
    }

    inner class RatingOption {
        @JvmField
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("key")
        @Expose
        var key: String? = null

        @JvmField
        @SerializedName("display_name")
        @Expose
        var displayName: String? = null

        @SerializedName("value")
        @Expose
        var value: String? = null

        @SerializedName("details")
        @Expose
        var details: Any? = null

        @SerializedName("type")
        @Expose
        var type: String? = null

        @JvmField
        @SerializedName("order")
        @Expose
        var order: Int? = null

        @SerializedName("group")
        @Expose
        var group: String? = null
    }

    inner class RatingReview {
        @JvmField
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("key")
        @Expose
        var key: String? = null

        @JvmField
        @SerializedName("display_name")
        @Expose
        var displayName: String? = null

        @SerializedName("value")
        @Expose
        var value: String? = null

        @SerializedName("details")
        @Expose
        var details: Any? = null

        @SerializedName("type")
        @Expose
        var type: String? = null

        @SerializedName("order")
        @Expose
        var order: Int? = null

        @SerializedName("group")
        @Expose
        var group: String? = null
    }
}
