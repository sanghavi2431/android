package `in`.woloo.www.store.user_details

import com.google.gson.annotations.SerializedName
import `in`.woloo.www.store.cart_request_response.CartLineItems

class WishLIstResponse {

    @SerializedName("wishlist")
    var wishlist: WishListItem? = null

}

class WishListItem {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("customer_id")
    var customer_id: String? = null
    @SerializedName("sales_channel_id")
    var salesChannelId: String? = null
    @SerializedName("created_at")
    var createdAt :String? = null
    @SerializedName("updated_at")
    var updatedAt :String? = null
    @SerializedName("deleted_at")
    var deletedAt :String? = null
    @SerializedName("items")
    var items: ArrayList<WishListItemsClass>? = null


}

data class WishListItemsClass(

    @SerializedName("id")
    val id: String,

    @SerializedName("product_variant_id")
    val productVariantId: String,

    @SerializedName("wishlist_id")
    val wishlistId: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("deleted_at")
    val deletedAt: String?,

    @SerializedName("product_variant")
    val productVariant: ProductVariant
    )

    data class ProductVariant(
        @SerializedName("id")
        val id: String,

        @SerializedName("title")
        val title: String,

        @SerializedName("sku")
        val sku: String?,

        @SerializedName("barcode")
        val barcode: String?,

        @SerializedName("ean")
        val ean: String?,

        @SerializedName("upc")
        val upc: String?,

        @SerializedName("allow_backorder")
        val allowBackorder: Boolean,

        @SerializedName("manage_inventory")
        val manageInventory: Boolean,

        @SerializedName("hs_code")
        val hsCode: String?,

        @SerializedName("origin_country")
        val originCountry: String?,

        @SerializedName("mid_code")
        val midCode: String?,

        @SerializedName("material")
        val material: String?,

        @SerializedName("weight")
        val weight: String?,

        @SerializedName("length")
        val length: String?,

        @SerializedName("height")
        val height: String?,

        @SerializedName("width")
        val width: String?,

        @SerializedName("metadata")
        val metadata: Metadata,

        @SerializedName("variant_rank")
        val variantRank: Int,

        @SerializedName("product_id")
        val productId: String,

        @SerializedName("product")
        val product: Product,

        @SerializedName("created_at")
        val createdAt: String,

        @SerializedName("updated_at")
        val updatedAt: String,

        @SerializedName("deleted_at")
        val deletedAt: String?,

        @SerializedName("calculated_price")
        val calculatedPrice: CalculatedPrice
    )

    data class Metadata(
        @SerializedName("image")
        val image: String
    )

    data class Product(
        @SerializedName("id")
        val id: String,

        @SerializedName("title")
        val title: String,

        @SerializedName("handle")
        val handle: String,

        @SerializedName("subtitle")
        val subtitle: String,

        @SerializedName("description")
        val description: String,

        @SerializedName("is_giftcard")
        val isGiftcard: Boolean,

        @SerializedName("status")
        val status: String,

        @SerializedName("thumbnail")
        val thumbnail: String,

        @SerializedName("weight")
        val weight: String?,

        @SerializedName("length")
        val length: String?,

        @SerializedName("height")
        val height: String?,

        @SerializedName("width")
        val width: String?,

        @SerializedName("origin_country")
        val originCountry: String?,

        @SerializedName("hs_code")
        val hsCode: String?,

        @SerializedName("mid_code")
        val midCode: String?,

        @SerializedName("material")
        val material: String?,

        @SerializedName("discountable")
        val discountable: Boolean,

        @SerializedName("external_id")
        val externalId: String?,

        @SerializedName("metadata")
        val metadata: Any?,  // If unknown structure, use Any

        @SerializedName("type_id")
        val typeId: String?,

        @SerializedName("type")
        val type: String?,

        @SerializedName("collection_id")
        val collectionId: String?,

        @SerializedName("collection")
        val collection: Collection?,

        @SerializedName("created_at")
        val createdAt: String,

        @SerializedName("updated_at")
        val updatedAt: String,

        @SerializedName("deleted_at")
        val deletedAt: String?
    )

    data class Collection(
        @SerializedName("id")
        val id: String
    )

    data class CalculatedPrice(
        @SerializedName("id")
        val id: String,

        @SerializedName("is_calculated_price_price_list")
        val isCalculatedPricePriceList: Boolean,

        @SerializedName("is_calculated_price_tax_inclusive")
        val isCalculatedPriceTaxInclusive: Boolean,

        @SerializedName("calculated_amount")
        val calculatedAmount: Int,

        @SerializedName("raw_calculated_amount")
        val rawCalculatedAmount: RawAmount,

        @SerializedName("is_original_price_price_list")
        val isOriginalPricePriceList: Boolean,

        @SerializedName("is_original_price_tax_inclusive")
        val isOriginalPriceTaxInclusive: Boolean,

        @SerializedName("original_amount")
        val originalAmount: Any,

        @SerializedName("raw_original_amount")
        val rawOriginalAmount: RawAmount,

        @SerializedName("currency_code")
        val currencyCode: String,

        @SerializedName("calculated_price")
        val calculatedPrice: PriceDetail,

        @SerializedName("original_price")
        val originalPrice: PriceDetail
    )

    data class RawAmount(
        @SerializedName("value")
        val value: String,

        @SerializedName("precision")
        val precision: Int
    )

    data class PriceDetail(
        @SerializedName("id")
        val id: String,

        @SerializedName("price_list_id")
        val priceListId: String?,

        @SerializedName("price_list_type")
        val priceListType: String?,

        @SerializedName("min_quantity")
        val minQuantity: String?,

        @SerializedName("max_quantity")
        val maxQuantity: String?
    )

