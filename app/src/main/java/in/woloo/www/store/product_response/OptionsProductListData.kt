package `in`.woloo.www.store.product_response

import com.google.gson.annotations.SerializedName

class OptionsProductListData {


    @SerializedName("id")
    var id: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("metadata")
    var metadata: String? = null

    @SerializedName("product_id")
    var product_id: String? = null

    @SerializedName("created_at")
    var created_at: String? = null

    @SerializedName("updated_at")
    var updated_at: String? = null

    @SerializedName("deleted_at")
    var deleted_at: String? = null

    @SerializedName("values")
    var values: ArrayList<ValuesProductListData>? = null

}

