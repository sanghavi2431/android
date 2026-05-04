package `in`.woloo.www.store.product_response

import com.google.gson.annotations.SerializedName

data class CollectionProductListData(

    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("handle") val handle: String?,
    @SerializedName("metadata") val metadata: Any?, // Use `Any?` if the type is unknown
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("deleted_at") val deletedAt: String?

)
