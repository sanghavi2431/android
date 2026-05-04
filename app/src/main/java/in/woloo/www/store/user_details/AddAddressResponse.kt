package `in`.woloo.www.store.user_details

import com.google.gson.annotations.SerializedName

class AddAddressResponse {
    @SerializedName("customer")
    var customer: Customer? = null
}


data class Customer(
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("company_name")
    var company_name: String? = null,
    @SerializedName("first_name")
    var first_name: String? = null,
    @SerializedName("last_name")
    var last_name: String? = null,
    @SerializedName("phone")
    var phone: String? = null,
    @SerializedName("metadata")
    var metadata: Any? = null,
    @SerializedName("has_account")
    var has_account: Boolean? = null,
    @SerializedName("deleted_at")
    var deleted_at: Any? = null,
    @SerializedName("created_at")
    var created_at: String? = null,
    @SerializedName("updated_at")
    var updated_at: String? = null,
    @SerializedName("addresses")
    var addresses: ArrayList<AddressList>? = null
)



