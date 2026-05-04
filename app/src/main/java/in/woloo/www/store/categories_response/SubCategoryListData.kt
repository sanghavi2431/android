package `in`.woloo.www.store.categories_response

import com.google.gson.annotations.SerializedName

data class SubCategoryListData(


    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("handle") val handle: String?,
    @SerializedName("parent_category_id") val parentCategoryId: String?,
    @SerializedName("metadata") val metadata: Any?, // Use `Any?` if the type is unknown
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("mpath") val mpath: String?,
    @SerializedName("is_active") val isActive: Boolean?,
    @SerializedName("is_internal") val isInternal: Boolean?,
    @SerializedName("rank") val rank: String?,


)
