package `in`.woloo.www.store.cart_request_response

import com.google.gson.annotations.SerializedName
import org.json.JSONObject

class CalculateShippingRequest {
        @SerializedName("cart_id")
        var cartId: String? = null

    @SerializedName("data")
    var data: JSONObject? = null

}