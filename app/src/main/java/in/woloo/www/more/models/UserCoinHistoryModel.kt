package `in`.woloo.www.more.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserCoinHistoryModel {
    @SerializedName("code")
    var code: Int = 0

    @SerializedName("data")
    var data: Data? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status: String? = null

    inner class Data {
        @SerializedName("total_count")
        var totalCount: Int = 0

        @SerializedName("history_count")
        var historyCount: Int = 0

        @SerializedName("history")
        var history: List<HistoryItem>? = null

        //		@SerializedName("next")
        var next: Int? = null

        @SerializedName("last_page")
        var lastPage: Int? = null

        inner class HistoryItem : Serializable {
            @SerializedName("woloo_details")
            var wolooDetails: WolooDetails? = null

            @SerializedName("sender_receiver_id")
            var senderReceiverId: Int = 0

            @SerializedName("created_at")
            var createdAt: String? = null

            @SerializedName("transaction_type")
            var transactionType: String? = null

            @SerializedName("message")
            var message: String? = null

            @SerializedName("type")
            var type: String? = null

            @SerializedName("is_expired")
            var isExpired: Int = 0

            @SerializedName("is_gift")
            var isGift: Int = 0

            @SerializedName("updated_at")
            var updatedAt: String? = null

            @SerializedName("user_id")
            var userId: Int = 0

            @SerializedName("id")
            var id: Int = 0

            @SerializedName("woloo_id")
            var wolooId: Int = 0

            @SerializedName("value")
            var value: String? = null

            @SerializedName("remarks")
            var remarks: String? = null

            @SerializedName("status")
            var status: Int = 0

            @SerializedName("expired_on")
            var expiredOn: Any? = null

            @SerializedName("sender")
            var sender: Sender? = null

            inner class WolooDetails : Serializable {
                @SerializedName("code")
                var code: String? = null

                @SerializedName("city")
                var city: String? = null

                @SerializedName("description")
                var description: String? = null

                @SerializedName("created_at")
                var createdAt: String? = null

                @SerializedName("title")
                var title: String? = null

                @SerializedName("is_safe_space")
                var isSafeSpace: Int = 0

                @SerializedName("updated_at")
                var updatedAt: String? = null

                @SerializedName("is_feeding_room")
                var isFeedingRoom: Int = 0

                @SerializedName("recommended_by")
                var recommendedBy: Int = 0

                @SerializedName("id")
                var id: Int = 0

                @SerializedName("is_sanitizer_available")
                var isSanitizerAvailable: Int = 0

                @SerializedName("lat")
                var lat: String? = null

                @SerializedName("user_rating")
                var userRating: String? = null


                //				@SerializedName("pincode")
                var pincode: Int = 0

                @SerializedName("address")
                var address: String? = null

                //				@SerializedName("user_review_count")
                var userReviewCount: Int = 0

                @SerializedName("lng")
                var lng: String? = null

                @SerializedName("is_makeup_room_available")
                var isMakeupRoomAvailable: Int = 0

                @SerializedName("restaurant")
                var restaurant: String? = null

                @SerializedName("is_clean_and_hygiene")
                var isCleanAndHygiene: Int = 0

                @SerializedName("is_washroom")
                var isWashroom: Int = 0

                //				@SerializedName("deleted_at")
                var deletedAt: String? = null

                @SerializedName("is_coffee_available")
                var isCoffeeAvailable: Int = 0

                @SerializedName("is_wheelchair_accessible")
                var isWheelchairAccessible: Int = 0

                @SerializedName("is_sanitary_pads_available")
                var isSanitaryPadsAvailable: Int = 0

                @SerializedName("is_franchise")
                var isFranchise: Int = 0

                @SerializedName("is_premium")
                var isPremium: Int = 0

                @SerializedName("user_id")
                var userId: String? = null

                @SerializedName("name")
                var name: String? = null
                    get() = if (field == null || field == "") {
                        ""
                    } else {
                        field
                    }

                @SerializedName("opening_hours")
                var openingHours: String? = null

                @SerializedName("recommended_mobile")
                var recommendedMobile: String? = null

                @SerializedName("segregated")
                var segregated: String? = null

                //				@SerializedName("status")
                var status: Int = 0

                @SerializedName("is_covid_free")
                var isCovidFree: Int = 0
            }

            inner class Sender : Serializable {
                @SerializedName("gender")
                var gender: String? = null

                @SerializedName("city")
                var city: String? = null

                @SerializedName("created_at")
                var createdAt: String? = null

                @SerializedName("is_first_session")
                var isFirstSession: Int = 0

                @SerializedName("ref_code")
                var refCode: String? = null

                @SerializedName("subscription_id")
                var subscriptionId: String? = null

                @SerializedName("updated_at")
                var updatedAt: String? = null

                @SerializedName("role_id")
                var roleId: String? = null

                @SerializedName("id")
                var id: Int = 0

                @SerializedName("woloo_id")
                var wolooId: String? = null

                @SerializedName("email")
                var email: String? = null

                @SerializedName("pincode")
                var pincode: String? = null

                /*public void setSettings(List<String> settings){
                   this.settings = settings;
               }

               public List<String> getSettings(){
                   return settings;
               }*/
                /*@SerializedName("settings")
                                private List<String> settings;*/
                @SerializedName("address")
                var address: String? = null

                @SerializedName("expiry_date")
                var expiryDate: String? = null

                @SerializedName("mobile")
                var mobile: String? = null

                //				@SerializedName("otp")
                var otp: Int = 0

                @SerializedName("avatar")
                var avatar: String? = null

                @SerializedName("sponsor_id")
                var sponsorId: String? = null

                //				@SerializedName("deleted_at")
                var deletedAt: String? = null

                @SerializedName("gp_id")
                var gpId: String? = null

                @SerializedName("fb_id")
                var fbId: String? = null

                @SerializedName("dob")
                var dob: String? = null

                @SerializedName("name")
                var name: String = ""
                    get() {
                        return if (field == null || field == "") {
                            ""
                        } else {
                            field
                        }
                    }

                @SerializedName("voucher_id")
                var voucherId: String? = null

                @SerializedName("status")
                var status: String? = null
            }
        }
    }
}