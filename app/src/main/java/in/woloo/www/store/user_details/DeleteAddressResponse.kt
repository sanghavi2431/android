package `in`.woloo.www.store.user_details

import com.google.gson.annotations.SerializedName

class DeleteAddressResponse {
    @SerializedName("id")
    var id :String? = null
    @SerializedName("object")
    var object1 :String? = null
    @SerializedName("deleted")
    var deleted :Boolean? = null
    @SerializedName("parent")
    var parent :Parent? = null


}

data class Parent(
    @SerializedName("id")
    val id: String,

    @SerializedName("first_name")
    val first_name: String,

    @SerializedName("last_name")
    val last_name: Boolean,

    @SerializedName("phone")
    val phone: String,

    @SerializedName("metadata")
    val metadata: Any,

    @SerializedName("created_at")
    val created_at: String,

    @SerializedName("updated_at")
    val updated_at: String



)


