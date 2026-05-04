package `in`.woloo.www.store.product_response

import com.google.gson.annotations.SerializedName

class ProductListResponse {
    @SerializedName("products")
    var products: ArrayList<ProductListData>? = null

    @SerializedName("count")
    var count: Int? = null

    @SerializedName("offset")
    var offset: Int? = null

    @SerializedName("limit")
    var limit: Int? = null
}

class ProductDetailsResponse {
    @SerializedName("product")
    var products: ProductListData? = null
}


