package `in`.woloo.www.more.subscribe.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SubscriptionListResponse {
    @SerializedName("subscription")
    @Expose
    var subscription: List<Subscription>? = null

    inner class Subscription {
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("description")
        @Expose
        var description: String? = null

        @SerializedName("frequency")
        @Expose
        var frequency: String? = null

        @SerializedName("days")
        @Expose
        var days: Int? = null

        @SerializedName("image")
        @Expose
        var image: String? = null

        @SerializedName("price")
        @Expose
        var price: String? = null

        @SerializedName("discount")
        @Expose
        var discount: Any? = null

        @SerializedName("is_expired")
        @Expose
        var isExpired: Int? = null

        @SerializedName("status")
        @Expose
        var status: Any? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("deleted_at")
        @Expose
        var deletedAt: Any? = null
    }
}
