package `in`.woloo.www.store.cart_request_response

import com.google.gson.annotations.SerializedName

class AddPromotionsRequest {

    @SerializedName("promo_codes")
    var promo_codes: ArrayList<String>? = null

}