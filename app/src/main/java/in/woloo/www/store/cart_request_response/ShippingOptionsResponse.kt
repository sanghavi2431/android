package `in`.woloo.www.store.cart_request_response

import com.google.gson.annotations.SerializedName

class ShippingOptionsResponse {
    @SerializedName("shipping_options")
    val shipping_options: List<ShippingOption>? = null

}

data class ShippingOption(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("price_type") val priceType: String? = null,
    @SerializedName("service_zone_id") val serviceZoneId: String? = null,
    @SerializedName("shipping_profile_id") val shippingProfileId: String? = null,
    @SerializedName("provider_id") val providerId: String? = null,
    @SerializedName("data") val data: Data? = null,
    @SerializedName("service_zone") val serviceZone: ServiceZone? = null,
    @SerializedName("type") val type: Type? = null,
    @SerializedName("provider") val provider: Provider? = null,
    @SerializedName("rules") val rules: List<Rule>? = null,
    @SerializedName("calculated_price") val calculatedPrice: CalculatedPrice? = null,
    @SerializedName("prices") val prices: List<Price>? = null,
    @SerializedName("amount") val amount: Int? = null,
    @SerializedName("is_tax_inclusive") val isTaxInclusive: Boolean? = null
)

data class Data(
    @SerializedName("id") val id: String? = null
)

data class ServiceZone(
    @SerializedName("fulfillment_set_id") val fulfillmentSetId: String? = null,
    @SerializedName("id") val id: String? = null
)

data class Type(
    @SerializedName("id") val id: String? = null,
    @SerializedName("label") val label: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("code") val code: String? = null
)

data class Provider(
    @SerializedName("id") val id: String? = null,
    @SerializedName("is_enabled") val isEnabled: Boolean? = null
)

data class Rule(
    @SerializedName("attribute") val attribute: String? = null,
    @SerializedName("value") val value: String? = null,
    @SerializedName("operator") val operator: String? = null
)

data class CalculatedPrice(
    @SerializedName("id") val id: String? = null,
    @SerializedName("is_calculated_price_price_list") val isCalculatedPricePriceList: Boolean? = null,
    @SerializedName("is_calculated_price_tax_inclusive") val isCalculatedPriceTaxInclusive: Boolean? = null,
    @SerializedName("calculated_amount") val calculatedAmount: Int? = null,
    @SerializedName("raw_calculated_amount") val rawCalculatedAmount: RawAmount? = null,
    @SerializedName("is_original_price_price_list") val isOriginalPricePriceList: Boolean? = null,
    @SerializedName("is_original_price_tax_inclusive") val isOriginalPriceTaxInclusive: Boolean? = null,
    @SerializedName("original_amount") val originalAmount: Int? = null,
    @SerializedName("raw_original_amount") val rawOriginalAmount: RawAmount? = null,
    @SerializedName("currency_code") val currencyCode: String? = null,
    @SerializedName("calculated_price") val calculatedPrice: PriceDetail? = null,
    @SerializedName("original_price") val originalPrice: PriceDetail? = null
)

data class Price(
    @SerializedName("id") val id: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("currency_code") val currencyCode: String? = null,
    @SerializedName("min_quantity") val minQuantity: Int? = null,
    @SerializedName("max_quantity") val maxQuantity: Int? = null,
    @SerializedName("rules_count") val rulesCount: Int? = null,
    @SerializedName("price_set_id") val priceSetId: String? = null,
    @SerializedName("price_list_id") val priceListId: String? = null,
    @SerializedName("price_list") val priceList: String? = null,
    @SerializedName("raw_amount") val rawAmount: RawAmount? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null,
    @SerializedName("deleted_at") val deletedAt: String? = null,
    @SerializedName("price_rules") val priceRules: List<String>? = null,
    @SerializedName("amount") val amount: Int? = null
)


data class PriceDetail(
    @SerializedName("id") val id: String? = null,
    @SerializedName("price_list_id") val priceListId: String? = null,
    @SerializedName("price_list_type") val priceListType: String? = null,
    @SerializedName("min_quantity") val minQuantity: Int? = null,
    @SerializedName("max_quantity") val maxQuantity: Int? = null
)


