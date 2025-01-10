package `in`.woloo.www.more.trendingblog.model

import com.google.gson.annotations.SerializedName

class NearByWolooAndOfferCountResponse {
    @JvmField
	@SerializedName("data")
    var data: Data? = null

    @SerializedName("message")
    var message: String? = null

    @SerializedName("status")
    var status: String? = null

    inner class Data {
        @SerializedName("offerCount")
        var offerCount = 0

        @JvmField
		@SerializedName("shopOffer")
        var shopOffer: List<ShopOfferItem>? = null

        @JvmField
		@SerializedName("wolooCount")
        var wolooCount = 0
    }

    inner class ShopOfferItem {
        @SerializedName("end_date")
        var endDate: String? = null

        @JvmField
		@SerializedName("coupon_code")
        var couponCode: String? = null

        @JvmField
		@SerializedName("description")
        var description: String? = null

        @SerializedName("title")
        var title: String? = null

        @SerializedName("product_ids")
        var productIds: String? = null

        @SerializedName("date_time")
        var dateTime: String? = null

        @SerializedName("sub_category_ids")
        var subCategoryIds: Any? = null

        @SerializedName("value_unit")
        var valueUnit: String? = null

        @SerializedName("vendors_ids")
        var vendorsIds: String? = null

        @SerializedName("id")
        var id: String? = null

        @SerializedName("category_ids")
        var categoryIds: String? = null

        @SerializedName("value")
        var value: String? = null

        @SerializedName("start_date")
        var startDate: String? = null

        @SerializedName("status")
        var status: String? = null
    }
}