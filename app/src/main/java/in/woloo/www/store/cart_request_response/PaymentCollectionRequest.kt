package `in`.woloo.www.store.cart_request_response

import com.google.gson.annotations.SerializedName

class PaymentCollectionRequest {
    @SerializedName("cart_id")
    var cartId: String? = null
}