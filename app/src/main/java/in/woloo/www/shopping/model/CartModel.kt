package `in`.woloo.www.shopping.model

class CartModel {
    @JvmField
    var pro_id: String? = null
    @JvmField
    var pro_name: String? = null
    @JvmField
    var image: String? = null
    @JvmField
    var price: String? = null
    @JvmField
    var qty: String? = null
    @JvmField
    var customer_margin: String? = null
    @JvmField
    var point_used: String? = null
    @JvmField
    var total_amount: String? = null
    @JvmField
    var coupon_value: String? = null
    @JvmField
    var coupon_value_unit: String? = null

    constructor()

    constructor(
        pro_id: String?,
        pro_name: String?,
        image: String?,
        price: String?,
        qty: String?,
        customer_margin: String?,
        point_used: String?,
        total_amount: String?,
        coupon_value: String?,
        coupon_value_unit: String?
    ) {
        this.pro_id = pro_id
        this.pro_name = pro_name
        this.image = image
        this.price = price
        this.qty = qty
        this.customer_margin = customer_margin
        this.point_used = point_used
        this.total_amount = total_amount
        this.coupon_value = coupon_value
        this.coupon_value_unit = coupon_value_unit
    }
}
