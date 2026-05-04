package `in`.woloo.www.store.user_details

import com.google.gson.annotations.SerializedName

class CreateUpdateAddressRequest {

    @SerializedName("metadata")
    val metadata : Any? = null

    @SerializedName("first_name")
    var first_name : String? = null

    @SerializedName("last_name")
    var last_name : String? = null

    @SerializedName("phone")
    var phone : String? = null

    @SerializedName("company")
    val company : String? = null

    @SerializedName("address_1")
    var address_1 : String? = null

    @SerializedName("address_2")
    var address_2 : String? = null

    @SerializedName("city")
    var city : String? = null

    @SerializedName("country_code")
    val country_code : String? = null

    @SerializedName("province")
    var province : String? = null

    @SerializedName("postal_code")
    var postal_code : String? = null

    @SerializedName("address_name")
    var address_name : String? = null


}