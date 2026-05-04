package `in`.woloo.www.store.cart_request_response

import com.google.gson.annotations.SerializedName

class CartResponse {
    @SerializedName("cart")
    var cart: CartParams? = null
}

data class CartParams
    (
            @SerializedName("id")
            var id: String = "",
            @SerializedName("currency_code")
            var currency_code: String = "",
            @SerializedName("email")
            var email: String= "",
            @SerializedName("region_id")
            var region_id: String= "",
            @SerializedName("created_at")
            var created_at: String= "",
            @SerializedName("updated_at")
            var updated_at: String= "",
            @SerializedName("completed_at")
            var completed_at: String= "",
            @SerializedName("total")
            var total: String= "",
            @SerializedName("subtotal")
            var subtotal: String= "",



            @SerializedName("tax_total")
            var tax_total: String= "",
            @SerializedName("discount_total")
            var discount_total: String= "",
            @SerializedName("discount_subtotal")
            var discount_subtotal: String= "",
            @SerializedName("discount_tax_total")
            var discount_tax_total: String= "",
            @SerializedName("original_total")
            var original_total: String= "",
            @SerializedName("original_tax_total")
            var original_tax_total: String= "",
            @SerializedName("item_total")
            var item_total: String= "",
            @SerializedName("item_subtotal")
            var item_subtotal: String= "",

            @SerializedName("item_tax_total")
            var item_tax_total: String= "",
            @SerializedName("original_item_total")
            var original_item_total: String= "",
            @SerializedName("original_item_subtotal")
            var original_item_subtotal: String= "",
            @SerializedName("original_item_tax_total")
            var original_item_tax_total: String= "",
            @SerializedName("shipping_address_id")
            var shipping_address_id: String= "",
            @SerializedName("sales_channel_id")
            var sales_channel_id: String= "",
            @SerializedName("metadata")
            var metadata: String= "",
            @SerializedName("credit_lines_total")
            var credit_lines_total: String= "",
            @SerializedName("credit_lines_tax_total")
            var credit_lines_tax_total: String= "",
            @SerializedName("credit_lines_subtotal")
            var credit_lines_subtotal: String= "",
            @SerializedName("original_shipping_total")
            var original_shipping_total: String= "",
            @SerializedName("original_shipping_subtotal")
            var original_shipping_subtotal: String= "",
            @SerializedName("original_shipping_tax_total")
            var original_shipping_tax_total: String= "",
            @SerializedName("shipping_tax_total")
            var shipping_tax_total: String= "",
            @SerializedName("shipping_subtotal")
            var shipping_subtotal: String= "",
            @SerializedName("shipping_total")
            var shipping_total: String= "",
            @SerializedName("customer_id")
            var customer_id: String= "",

            @SerializedName("billing_address")
            var billing_address: Any= "",
            @SerializedName("items")
            var items: ArrayList<CartLineItems>? = null,
            @SerializedName("shipping_methods")
            var shipping_methods: ArrayList<ShippingMethods>? = null,
            @SerializedName("credit_lines")
            var credit_lines: Any= "",
            @SerializedName("promotions")
            var promotions: ArrayList<Promotions>? = null,
            @SerializedName("shipping_address")
            var shipping_address: ShippingAddress? = null,
            @SerializedName("customer")
            var customer: Customer? = null  ,
            @SerializedName("region")
            var region: Region? = null
            )


data class  ShippingAddress(
@SerializedName("id")
var id: String,
@SerializedName("first_name")
var first_name: String,
@SerializedName("last_name")
var last_name: String,
@SerializedName("company")
var company: String,
@SerializedName("address_1")
var address_1: String,
@SerializedName("address_2")
var address_2: String,
@SerializedName("city")
var city: String,
@SerializedName("postal_code")
var postal_code: String,
@SerializedName("country_code")
var country_code: String,
@SerializedName("province")
var province: String,
@SerializedName("phone")
var phone: String
)

data class Customer(

    @SerializedName("id")
var id: String,
@SerializedName("email")
var email: String,
@SerializedName("groups")
var groups: Any

)

data class Region
    (
            @SerializedName("id")
            var id: String,
            @SerializedName("name")
            var name: String,
            @SerializedName("currency_code")
            var currency_code: String,
            @SerializedName("automatic_taxes")
            var automatic_taxes: Boolean,
            @SerializedName("countries")
            var countries: Any
            )


data class ShippingMethods(

        @SerializedName("amount")
        var amount: String,
        @SerializedName("is_tax_inclusive")
        var is_tax_inclusive: Boolean,
                @SerializedName("shipping_option_id")
        var shipping_option_id: String,
        @SerializedName("id")
        var id: String,
@SerializedName("tax_lines")
var taxLines: Any?,
        @SerializedName("adjustments")
        var adjustments: Any?
)

data class Promotions(
        @SerializedName("id")
        var id: String,
        @SerializedName("code")
        var code: String,

)