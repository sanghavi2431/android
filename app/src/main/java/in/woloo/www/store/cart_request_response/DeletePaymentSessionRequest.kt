package `in`.woloo.www.store.cart_request_response

import com.google.gson.annotations.SerializedName

class DeletePaymentSessionRequest {
    @SerializedName("ids")
    var ids: ArrayList<String>? = null
}