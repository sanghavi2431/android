package `in`.woloo.www.application_kotlin.model.server_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SearchWolooResponse {
    @JvmField
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @JvmField
    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data {
        @SerializedName("total_woloo_count")
        @Expose
        var totalWolooCount: Int? = null

        @SerializedName("woloo_count")
        @Expose
        var wolooCount: Int? = null

        @JvmField
        @SerializedName("woloos")
        @Expose
        var woloos: List<Woloo>? = null

        @JvmField
        @SerializedName("next")
        @Expose
        var next: Int? = null

        inner class Woloo {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("code")
            @Expose
            var code: String? = null

            @SerializedName("name")
            @Expose
            var name: String? = null

            @SerializedName("title")
            @Expose
            var title: Any? = null

            @SerializedName("image")
            @Expose
            var image: String? = null

            @SerializedName("opening_hours")
            @Expose
            var openingHours: String? = null

            @SerializedName("restaurant")
            @Expose
            var restaurant: Any? = null

            @SerializedName("segregated")
            @Expose
            var segregated: String? = null

            @SerializedName("address")
            @Expose
            var address: String? = null

            @SerializedName("city")
            @Expose
            var city: String? = null

            @SerializedName("lat")
            @Expose
            var lat: String? = null

            @SerializedName("lng")
            @Expose
            var lng: String? = null

            @SerializedName("user_id")
            @Expose
            var userId: Any? = null

            @SerializedName("status")
            @Expose
            var status: Int? = null

            @SerializedName("description")
            @Expose
            var description: String? = null

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

            @SerializedName("duration")
            @Expose
            var duration: String? = null

            @SerializedName("distance")
            @Expose
            var distance: String? = null

            @SerializedName("user_rating")
            @Expose
            var userRating: String? = null

            @SerializedName("user_review_count")
            @Expose
            var userReviewCount: Int? = null

            @SerializedName("offer")
            @Expose
            var offer: Offer? = null

            @SerializedName("cibil_score_image")
            @Expose
            var cibilScoreImage = ""
        }

        inner class Offer {
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("title")
            @Expose
            var title: String? = null

            @SerializedName("description")
            @Expose
            var description: String? = null

            @SerializedName("image")
            @Expose
            var image: String? = null

            @SerializedName("woloo_id")
            @Expose
            var wolooId: Int? = null

            @SerializedName("start_date")
            @Expose
            var startDate: String? = null

            @SerializedName("end_date")
            @Expose
            var endDate: String? = null

            @SerializedName("status")
            @Expose
            var status: Int? = null

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
}
