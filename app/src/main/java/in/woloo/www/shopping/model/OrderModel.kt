package `in`.woloo.www.shopping.model

class OrderModel {
    @JvmField
    var id: String? = null
    @JvmField
    var pro_id: String? = null
    @JvmField
    var title: String? = null
    @JvmField
    var price: String? = null
    var desc: String? = null
    @JvmField
    var qty: String? = null
    @JvmField
    var image: String? = null
    @JvmField
    var date_time: String? = null
    @JvmField
    var order_id: String? = null
    @JvmField
    var status: String? = null
    @JvmField
    var can_return: String? = null
    var can_cancel: String? = null
    @JvmField
    var amount: String? = null

    constructor()

    constructor(
        id: String?,
        pro_id: String?,
        title: String?,
        price: String?,
        desc: String?,
        qty: String?,
        image: String?,
        date_time: String?,
        order_id: String?,
        status: String?,
        can_return: String?,
        can_cancel: String?,
        amount: String?
    ) {
        this.id = id
        this.pro_id = pro_id

        this.title = title
        this.image = image

        this.price = price
        this.desc = desc
        this.qty = qty


        this.date_time = date_time
        this.order_id = order_id
        this.status = status
        this.can_return = can_return
        this.can_cancel = can_cancel
        this.amount = amount
    }
}

