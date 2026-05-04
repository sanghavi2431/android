package `in`.woloo.www.store.cart_request_response

import com.google.gson.annotations.SerializedName

class PaymentSessionRequest {

    @SerializedName("provider_id")
    var providerId: String? = null

}