package `in`.woloo.www.store.cart_request_response

import com.google.gson.annotations.SerializedName

class ShippingBillingAddressRequest {

    @SerializedName("shipping_address")
    var shippingAddress: Address? = null

    @SerializedName("billing_address")
    var billingAddress: Address? = null

}

data class Address(

    @SerializedName("first_name")
    var firstName: String? = null,

    @SerializedName("last_name")
    var lastName: String? = null,

    @SerializedName("address_1")
    var address1: String? = null,

    @SerializedName("city")
    var city: String? = null,

    @SerializedName("country_code")
    var countryCode: String? = null,

    @SerializedName("province")
    var province: String? = null,

    @SerializedName("postal_code")
    var postalCode: String? = null,

    @SerializedName("phone")
    var phone: String? = null

)