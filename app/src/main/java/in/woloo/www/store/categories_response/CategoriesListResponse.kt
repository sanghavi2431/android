package `in`.woloo.www.store.product_response

import com.google.gson.annotations.SerializedName
import `in`.woloo.www.store.categories_response.CategoriesListData

class CategoriesListResponse {
  //  @SerializedName("product_categories")
    @SerializedName("categories")
    var productCategories: ArrayList<CategoriesListData>? = null

    @SerializedName("count")
    var count: Int? = null

    @SerializedName("offset")
    var offset: Int? = null

    @SerializedName("limit")
    var limit: Int? = null
}


