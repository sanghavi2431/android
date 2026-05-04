package `in`.woloo.www.store.admin_response

import com.google.gson.annotations.SerializedName


class CityFromSalesChannelResponse {
    @SerializedName("stock_locations")
    var stock_locations: ArrayList<StockLocationsListData>? = null

    @SerializedName("count")
    var count: Int? = null

    @SerializedName("offset")
    var offset: Int? = null

    @SerializedName("limit")
    var limit: Int? = null
}