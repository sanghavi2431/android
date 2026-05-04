package `in`.woloo.www.store.cart_request_response

import com.google.gson.annotations.SerializedName

class CartUpdateRequest {

    @SerializedName("quantity")
    var quantity: Int? = null
}

class CartUpdateRequestHygiene {

    @SerializedName("quantity")
    var quantity: Int? = null

    @SerializedName("metadata")
    var metadata : MetaDataLineItemsRequest? = null
}