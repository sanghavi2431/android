package `in`.woloo.www.store.collections_response

import com.google.gson.annotations.SerializedName


class CollectionsListData {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("metadata")
    var metadata: MetadataCollections? = null

}

data class MetadataCollections(

    @SerializedName("image") val image: String?,


    )