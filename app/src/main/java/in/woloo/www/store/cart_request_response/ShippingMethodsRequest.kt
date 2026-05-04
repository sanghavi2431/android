package `in`.woloo.www.store.cart_request_response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.gson.annotations.SerializedName

class ShippingMethodsRequest
{
    @SerializedName("options")
    var options: ArrayList<ShippingMethodsRequestParam>? = null

}

@Parcelize
data class ShippingMethodsRequestParam (

    @SerializedName("id")
    var optionId: String? = null


    ): Parcelable


