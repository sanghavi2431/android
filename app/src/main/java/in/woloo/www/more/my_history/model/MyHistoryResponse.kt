package `in`.woloo.www.more.my_history.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MyHistoryResponse {
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
        @SerializedName("total_count")
        @Expose
        var totalCount = 0

        @SerializedName("history_count")
        @Expose
        var historyCount = 0

        @SerializedName("history")
        @Expose
        var history: List<History>? = null

        @SerializedName("next")
        @Expose
        var next = 0
    }

    inner class History {
        @JvmField
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("woloo_id")
        @Expose
        var wolooId: Int? = null

        @SerializedName("user_id")
        @Expose
        var userId: Int? = null

        @SerializedName("is_review_pending")
        @Expose
        var isReviewPending: Int? = null

        @SerializedName("amount")
        @Expose
        var amount: String? = null

        @SerializedName("type")
        @Expose
        var type: String? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @JvmField
        @SerializedName("woloo_details")
        @Expose
        var wolooDetails: WolooDetails? = null
    }

    inner class WolooDetails {
        @JvmField
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @SerializedName("code")
        @Expose
        var code: String? = null

        @JvmField
        @SerializedName("name")
        @Expose
        var name: String? = null

        @SerializedName("title")
        @Expose
        var title: String? = null

        @JvmField
        @SerializedName("image")
        @Expose
        var image: List<String>? = null

        @SerializedName("opening_hours")
        @Expose
        var openingHours: Any? = null

        @SerializedName("restaurant")
        @Expose
        var restaurant: Any? = null

        @SerializedName("segregated")
        @Expose
        var segregated: Any? = null

        @JvmField
        @SerializedName("address")
        @Expose
        var address: String? = null

        @SerializedName("city")
        @Expose
        var city: String? = null

        @JvmField
        @SerializedName("lat")
        @Expose
        var lat: String? = null

        @JvmField
        @SerializedName("lng")
        @Expose
        var lng: String? = null

        @SerializedName("user_id")
        @Expose
        var userId = 0

        @SerializedName("status")
        @Expose
        var status: Int? = null

        @SerializedName("description")
        @Expose
        var description: Any? = null

        @SerializedName("created_at")
        @Expose
        var createdAt: String? = null

        @SerializedName("updated_at")
        @Expose
        var updatedAt: String? = null

        @SerializedName("deleted_at")
        @Expose
        var deletedAt: Any? = null

        @SerializedName("is_covid_free")
        @Expose
        var isCovidFree: Int? = null

        @SerializedName("is_safe_space")
        @Expose
        var isSafeSpace: Int? = null

        @SerializedName("is_clean_and_hygiene")
        @Expose
        var isCleanAndHygiene: Int? = null

        @SerializedName("is_sanitary_pads_available")
        @Expose
        var isSanitaryPadsAvailable: Int? = null

        @SerializedName("is_makeup_room_available")
        @Expose
        var isMakeupRoomAvailable: Int? = null

        @SerializedName("is_coffee_available")
        @Expose
        var isCoffeeAvailable: Int? = null

        @SerializedName("is_sanitizer_available")
        @Expose
        var isSanitizerAvailable: Int? = null

        @SerializedName("is_feeding_room")
        @Expose
        var isFeedingRoom: Int? = null

        @SerializedName("is_wheelchair_accessible")
        @Expose
        var isWheelchairAccessible: Int? = null

        @SerializedName("is_washroom")
        @Expose
        var isWashroom: Int? = null

        @SerializedName("is_premium")
        @Expose
        var isPremium: Int? = null

        @SerializedName("user_rating")
        @Expose
        var userRating: String? = null

        @SerializedName("user_review_count")
        @Expose
        var userReviewCount: Int? = null
    }
}
