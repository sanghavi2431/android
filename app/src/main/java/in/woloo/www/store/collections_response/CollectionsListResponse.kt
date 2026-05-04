package `in`.woloo.www.store.collections_response

import com.google.gson.annotations.SerializedName
import `in`.woloo.www.store.categories_response.CategoriesListData

class CollectionsListResponse {
    @SerializedName("collections")
    var collections: ArrayList<CollectionsListData>? = null

    @SerializedName("count")
    var count: Int? = null

    @SerializedName("offset")
    var offset: Int? = null

    @SerializedName("limit")
    var limit: Int? = null
}