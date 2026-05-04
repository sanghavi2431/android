package `in`.woloo.www.store.user_details

import com.google.gson.annotations.SerializedName

class AddressList {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("address_name")
    var address_name: String? = null
    @SerializedName("is_default_shipping")
    var is_default_shipping: Boolean? = null
    @SerializedName("is_default_billing")
    var is_default_billing: Boolean? = null
    @SerializedName("company")
    var company: String? = null

    @SerializedName("first_name")
    var first_name: String? = null

    @SerializedName("last_name")
    var last_name: String? = null

    @SerializedName("address_1")
    var address_1: String? = null

    @SerializedName("address_2")
    var address_2: String? = null

    @SerializedName("city")
    var city: String? = null

    @SerializedName("country_code")
    var country_code: String? = null

    @SerializedName("province")
    var province: String? = null

    @SerializedName("postal_code")
    var postal_code: String? = null

    @SerializedName("phone")
    var phone: String? = null

    @SerializedName("metadata")
    var metadata: Any? = null

    @SerializedName("customer_id")
    var customer_id: String? = null

    @SerializedName("created_at")
    var created_at: String? = null

    @SerializedName("updated_at")
    var updated_at: String? = null

    @SerializedName("deleted_at")
    var deleted_at: Any? = null

}