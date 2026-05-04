package `in`.woloo.www.more.subscribe.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PlanResponse {
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
    var data: List<Data>? = null

    inner class Data {
        @JvmField
        @SerializedName("id")
        @Expose
        var id: Int? = null

        @JvmField
        @SerializedName("name")
        @Expose
        var name: String? = null

        @JvmField
        @SerializedName("description")
        @Expose
        var description: String? = null

        @JvmField
        @SerializedName("frequency")
        @Expose
        var frequency: String? = null

        @JvmField
        @SerializedName("days")
        @Expose
        var days: Int? = null

        @SerializedName("image")
        @Expose
        var image: String? = null

        @JvmField
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

        @JvmField
        @SerializedName("plan_id")
        @Expose
        var planId: String? = null

        @JvmField
        @SerializedName("currency")
        @Expose
        var currency: String? = null

        @SerializedName("backgroud_color")
        @Expose
        var backgroud_color: String? = null

        @SerializedName("shield_color")
        @Expose
        var shield_color: String? = null

        @JvmField
        @SerializedName("strike_out_price")
        @Expose
        var beforeDiscountPrice: Int? = null
    }
}
