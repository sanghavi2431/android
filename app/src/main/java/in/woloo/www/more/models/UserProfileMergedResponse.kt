package `in`.woloo.www.more.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserProfileMergedResponse : Serializable {
    @SerializedName("data")
    var data: Data? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status: String? = null

    inner class Data : Serializable {
        @SerializedName("profile")
        var profile: Profile? = null

        @SerializedName("offerList")
        var offerList: List<OfferListItem>? = null

        @SerializedName("totalCoins")
        var totalCoins: TotalCoins? = null

        @SerializedName("planData")
        var planData: PlanData? = null

        @SerializedName("lifetime_free")
        var lifetime_free: String? = null

        @SerializedName("isFutureSubcriptionExist")
        var isFutureSubcriptionExist: String? = null

        @SerializedName("futureSubcription")
        val futureSubcription: FutureSubscription? = null

        @SerializedName("purchase_by")
        val purchase_by: String? = null
    }

    inner class PlanData : Serializable {
        @SerializedName("image")
        var image: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("discount")
        var discount: String? = null

        @SerializedName("created_at")
        var createdAt: String? = null

        @SerializedName("deleted_at")
        var deletedAt: String? = null

        @SerializedName("frequency")
        var frequency: String? = null

        @SerializedName("is_expired")
        var isExpired: Int = 0

        @SerializedName("updated_at")
        var updatedAt: String? = null

        @SerializedName("price")
        var price: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("days")
        var days: String? = null

        @SerializedName("currency")
        var currency: String? = null

        @SerializedName("id")
        var id: Int = 0

        @SerializedName("plan_id")
        var planId: String? = null

        @SerializedName("start_at")
        var start_at: String? = null

        @SerializedName("end_at")
        var end_at: String? = null

        @SerializedName("is_cancel")
        var isIs_cancel: Boolean = false
            private set

        @SerializedName("status")
        var status: Int = 0

        fun setIs_cancel(is_cancel: Boolean) {
            this.isIs_cancel = is_cancel
        }
    }

    inner class Profile : Serializable {
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

        /*public void setSettings(LoginResponse.Setting settings){
           this.settings = settings;
       }

       public LoginResponse.Setting
       getSettings(){
           return settings;
       }*/
        /*@SerializedName("settings")
                private LoginResponse.Setting settings;*/
        @SerializedName("address")
        var address: String? = null

        @SerializedName("expiry_date")
        var expiryDate: String? = null

        @SerializedName("mobile")
        var mobile: String? = null

        @SerializedName("otp")
        var otp: Int = 0

        @SerializedName("avatar")
        var avatar: String? = null

        @SerializedName("sponsor_id")
        var sponsorId: String? = null

        @SerializedName("deleted_at")
        var deletedAt: String? = null

        @SerializedName("gp_id")
        var gpId: String? = null

        @SerializedName("fb_id")
        var fbId: String? = null

        @SerializedName("dob")
        var dob: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("voucher_id")
        var voucherId: String? = null

        @SerializedName("status")
        var status: String? = null

        @SerializedName("gift_subscription_id")
        val giftSubscriptionId: String? = null
    }

    inner class TotalCoins : Serializable {
        @SerializedName("total_coins")
        var totalCoins: Int = 0

        @SerializedName("gift_coins")
        var giftCoins: Int = 0
    }

    inner class FutureSubscription : Serializable {
        @SerializedName("image")
        val image: String? = null

        @SerializedName("backgroud_color")
        val backgroudColor: String? = null

        @SerializedName("price_with_gst")
        val priceWithGst: String? = null

        @SerializedName("description")
        val description: String? = null

        @SerializedName("discount")
        val discount: String? = null

        @SerializedName("created_at")
        val createdAt: String? = null

        @SerializedName("shield_color")
        val shieldColor: String? = null

        @SerializedName("is_voucher")
        val isVoucher: Int = 0

        @SerializedName("deleted_at")
        val deletedAt: Any? = null

        @SerializedName("frequency")
        val frequency: String? = null

        @SerializedName("is_recommended")
        val isRecommended: Int = 0

        @SerializedName("is_expired")
        val isExpired: Int = 0

        @SerializedName("updated_at")
        val updatedAt: String? = null

        @SerializedName("price")
        val price: String? = null

        @SerializedName("name")
        val name: String? = null

        @SerializedName("days")
        val days: String? = null

        @SerializedName("currency")
        val currency: String? = null

        @SerializedName("id")
        val id: Int = 0

        @SerializedName("before_discount_price")
        val beforeDiscountPrice: Int = 0

        @SerializedName("plan_id")
        val planId: String? = null

        @SerializedName("status")
        val status: Int = 0

        @SerializedName("start_at")
        val start_at: String? = null

        @SerializedName("end_at")
        val end_at: String? = null
    }

    inner class OfferListItem : Serializable {
        @SerializedName("offer")
        val offer: List<OfferItem>? = null

        @SerializedName("updated_at")
        val updatedAt: String? = null

        @SerializedName("user_id")
        val userId: Int = 0

        @SerializedName("expiry_date")
        val expiryDate: Any? = null

        @SerializedName("created_at")
        val createdAt: String? = null

        @SerializedName("id")
        val id: Int = 0

        @SerializedName("deleted_at")
        val deletedAt: String? = null

        @SerializedName("offer_id")
        val offerId: Int = 0

        @SerializedName("status")
        val status: Int = 0
    }

    inner class OfferItem : Serializable {
        @SerializedName("end_date")
        val endDate: String? = null

        @SerializedName("image")
        val image: String? = null

        @SerializedName("updated_at")
        val updatedAt: String? = null

        @SerializedName("description")
        val description: String? = null

        @SerializedName("created_at")
        val createdAt: String? = null

        @SerializedName("id")
        val id: Int = 0

        @SerializedName("title")
        val title: String? = null

        @SerializedName("woloo_id")
        val wolooId: Int = 0

        @SerializedName("deleted_at")
        val deletedAt: Any? = null

        @SerializedName("start_date")
        val startDate: String? = null

        @SerializedName("status")
        val status: Int = 0
    }
}


