package `in`.woloo.www.store.product_response

import com.google.gson.annotations.SerializedName
import `in`.woloo.www.store.categories_response.MetadataCategories

class VariantsProductListData {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("sku")
    var sku: String? = null

    @SerializedName("created_at")
    var created_at: String? = null

    @SerializedName("updated_at")
    var updated_at: String? = null

    @SerializedName("barcode")
    var barcode: String? = null

    @SerializedName("ean")
    var ean: String? = null

    @SerializedName("upc")
    var upc: String? = null

    @SerializedName("manage_inventory")
    var manage_inventory: String? = null

    @SerializedName("allow_backorder")
    var allow_backorder: String? = null

    @SerializedName("material")
    var material: String? = null

    @SerializedName("metadata")
    var metadata: MetadataCategories? = null

    @SerializedName("weight")
    var weight: String? = null

    @SerializedName("length")
    var length: String? = null

    @SerializedName("height")
    var height: String? = null

    @SerializedName("width")
    var width: String? = null

    @SerializedName("hs_code")
    var hs_code: String? = null

    @SerializedName("origin_country")
    var origin_country: String? = null

    @SerializedName("mid_code")
    var mid_code: String? = null

    @SerializedName("variant_rank")
    var variant_rank: String? = null

    @SerializedName("product_id")
    var product_id: String? = null

    @SerializedName("deleted_at")
    var deleted_at: String? = null

    @SerializedName("options")
    var options: ArrayList<OptionsVariantsProductListData>? = null

    @SerializedName("calculated_price")
    var calculated_price: PriceProductListData? = null

    @SerializedName("inventory_quantity")
    var inventoryQuantity : Int? = null

    @SerializedName("has_restock_subscription")
    var isNotifiedProduct : Boolean? = null

    @SerializedName("has_wishlisted")
    var isWishlisted : Boolean? = null


    @SerializedName("wishlist_item_id")
    var wishlist_item_id : String? = null


}

data class PriceProductListData(
    @SerializedName("id") val id: String?,
    @SerializedName("is_calculated_price_price_list") val isCalculatedPricePriceList: Boolean?,
    @SerializedName("is_calculated_price_tax_inclusive") val isCalculatedPriceTaxInclusive: Boolean?,
    @SerializedName("calculated_amount") val calculatedAmount: Int?,
    @SerializedName("raw_calculated_amount") val rawCalculatedAmount: RawAmount?,
    @SerializedName("is_original_price_price_list") val isOriginalPricePriceList: Boolean?,
    @SerializedName("is_original_price_tax_inclusive") val isOriginalPriceTaxInclusive: Boolean?,
    @SerializedName("original_amount") val originalAmount: Int?,
    @SerializedName("raw_original_amount") val rawOriginalAmount: RawAmount?,
    @SerializedName("currency_code") val currencyCode: String?,
    @SerializedName("calculated_price") val calculatedPrice: PriceDetails?,
    @SerializedName("original_price") val originalPrice: PriceDetails?

)

data class RawAmount(
    @SerializedName("value") val value: String?,
    @SerializedName("precision") val precision: Int?
)

data class PriceDetails(
    @SerializedName("id") val id: String?,
    @SerializedName("price_list_id") val priceListId: String?,
    @SerializedName("price_list_type") val priceListType: String?,
    @SerializedName("min_quantity") val minQuantity: Int?,
    @SerializedName("max_quantity") val maxQuantity: Int?
)



