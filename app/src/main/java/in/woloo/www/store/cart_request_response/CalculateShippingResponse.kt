package `in`.woloo.www.store.cart_request_response

import com.google.gson.annotations.SerializedName

class CalculateShippingResponse {


    @SerializedName("shipping_option")
    val shippingOption: ArrayList<ShippingOptionCalculate>? = null

}

class ShippingOptionCalculate{

    @SerializedName("id")
    val id: String? = null

    @SerializedName("name")
    val name: String? = null

    @SerializedName("price_type")
    val priceType: String? = null

    @SerializedName("service_zone_id")
    val serviceZoneId: String? = null

    @SerializedName("shipping_profile_id")
    val shippingProfileId: String? = null

    @SerializedName("shipping_option_type_id")
    val shippingOptionTypeId: String? = null

    @SerializedName("calculated_price")
    val calculatedPrice: CalculatedPriceShipping? = null

    @SerializedName("amount")
    val amount: Double? = null

    @SerializedName("is_tax_inclusive")
    val isTaxInclusive: Boolean? = null


}

class CalculatedPriceShipping{

    @SerializedName("is_calculated_price_tax_inclusive")
    val isCalculatedPriceTaxInclusive: Boolean? = null

    @SerializedName("calculated_amount")
    val calculatedAmount: Double? = null
}