package `in`.woloo.www.store.cart_request_response

import com.google.gson.annotations.SerializedName

class PaymentProviderResponse {

    @SerializedName("payment_providers")
    var paymentProviders: ArrayList<PaymentProvider>? = null
    @SerializedName("count")
    var count: Int = 0
    @SerializedName("offset")
    var offset: Int = 0
    @SerializedName("limit")
    var limit: Int = 0

}


class PaymentProvider {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("is_enabled")
    var isEnabled: Boolean = false

}