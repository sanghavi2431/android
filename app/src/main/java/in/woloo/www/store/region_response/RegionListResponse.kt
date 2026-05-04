package `in`.woloo.www.store.region_response

import com.google.gson.annotations.SerializedName
import `in`.woloo.www.store.product_response.ProductListData

class RegionListResponse {

    @SerializedName("regions")
    var regions: ArrayList<RegionListData>? = null

    @SerializedName("count")
    var count: Int? = null

    @SerializedName("offset")
    var offset: Int? = null

    @SerializedName("limit")
    var limit: Int? = null
}