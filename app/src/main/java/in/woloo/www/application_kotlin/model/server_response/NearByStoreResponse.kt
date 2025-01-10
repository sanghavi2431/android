package `in`.woloo.www.application_kotlin.model.server_response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NearByStoreResponse {
    @JvmField
    @SerializedName("status")
    @Expose
    var status: String? = null

    @JvmField
    @SerializedName("message")
    @Expose
    var message: String? = null

    @JvmField
    @SerializedName("data")
    @Expose
    var data: List<Data>? = null

    class Data {
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

        @JvmField
        @SerializedName("title")
        @Expose
        var title: String? = null

        @JvmField
        @SerializedName("image")
        @Expose
        var image: List<String?> = ArrayList<String?>()

        @JvmField
        @SerializedName("opening_hours")
        @Expose
        var openingHours: String? = null

        @SerializedName("restaurant")
        @Expose
        var restaurant: String? = null

        @JvmField
        @SerializedName("segregated")
        @Expose
        var segregated: String? = null

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
        var userId: Int? = null

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

        @JvmField
        @SerializedName("is_covid_free")
        @Expose
        var isCovidFree: Int? = null

        @JvmField
        @SerializedName("is_safe_space")
        @Expose
        var isSafeSpace: Int? = null

        @JvmField
        @SerializedName("is_clean_and_hygiene")
        @Expose
        var isCleanAndHygiene: Int? = null

        @JvmField
        @SerializedName("is_sanitary_pads_available")
        @Expose
        var isSanitaryPadsAvailable: Int? = null

        @JvmField
        @SerializedName("is_makeup_room_available")
        @Expose
        var isMakeupRoomAvailable: Int? = null

        @JvmField
        @SerializedName("is_coffee_available")
        @Expose
        var isCoffeeAvailable: Int? = null

        @JvmField
        @SerializedName("is_sanitizer_available")
        @Expose
        var isSanitizerAvailable: Int? = null

        @JvmField
        @SerializedName("is_feeding_room")
        @Expose
        var isFeedingRoom: Int? = null

        @JvmField
        @SerializedName("is_wheelchair_accessible")
        @Expose
        var isWheelchairAccessible: Int? = null

        @JvmField
        @SerializedName("is_washroom")
        @Expose
        var isWashroom: Int? = null

        @JvmField
        @SerializedName("is_premium")
        @Expose
        var isPremium: Int? = null

        @JvmField
        @SerializedName("distance")
        @Expose
        var distance: String? = null

        @JvmField
        @SerializedName("duration")
        @Expose
        var duration: String? = null

        @JvmField
        @SerializedName("user_rating")
        @Expose
        var userRating: String? = null

        @SerializedName("user_review_count")
        @Expose
        var userReviewCount: Int? = null

        @JvmField
        @SerializedName("offer")
        @Expose
        var offer: Offer? = null

        @SerializedName("base_url")
        @Expose
        var baseUrl: String? = null
            get() = "$field/"

        @JvmField
        @SerializedName("is_liked")
        @Expose
        var isLiked = 0

        @SerializedName("cibil_score")
        @Expose
        var cibilScore = ""
            get() = "" + field

        @JvmField
        @SerializedName("cibil_score_image")
        @Expose
        var cibilScoreImage = ""

        @JvmField
        @SerializedName("is_offer")
        @Expose
        var isOfferAvailable: String? = null

        @JvmField
        @SerializedName("is_open")
        @Expose
        var isOpenNow: String? = null

        @JvmField
        @SerializedName("cibil_score_colour")
        @Expose
        var cibilScoreColour = ""
        override fun hashCode(): Int {
            return id!!
        }

        override fun equals(obj: Any?): Boolean {
            return super.equals(obj)
        }

        inner class Offer {
            @JvmField
            @SerializedName("id")
            @Expose
            var id: Int? = null

            @SerializedName("title")
            @Expose
            var title: String? = null

            @SerializedName("description")
            @Expose
            var description: String? = null

            @JvmField
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
