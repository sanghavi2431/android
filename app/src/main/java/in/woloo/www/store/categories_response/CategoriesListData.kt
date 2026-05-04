package `in`.woloo.www.store.categories_response

import com.google.gson.annotations.SerializedName

class CategoriesListData {


    @SerializedName("id")
    var id: String? = null

    @SerializedName("name")
    var name: String? = null


    @SerializedName("description")
    var description: String? = null

    @SerializedName("handle")
    var handle: String? = null

    @SerializedName("parent_category_id")
    var parent_category_id: String? = null

    @SerializedName("rank")
    var rank: String? = null

    @SerializedName("created_at")
    var created_at: String? = null

    @SerializedName("updated_at")
    var updated_at: String? = null

    @SerializedName("parent_category")
    var parent_category: CategoriesListData? = null

    @SerializedName("metadata")
    var metadata: MetadataCategories? = null

    @SerializedName("category_children")
    var category_children: ArrayList<SubCategoryListData>? = null



}

