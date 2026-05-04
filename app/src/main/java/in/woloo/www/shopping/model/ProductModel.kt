package `in`.woloo.www.shopping.model

class ProductModel {
    @JvmField
    var id: String? = null
    @JvmField
    var title: String? = null
    @JvmField
    var price: String? = null
    @JvmField
    var desc: String? = null
    var qty: String? = null
    private val commission: String? = null
    @JvmField
    var image: String? = null

    constructor()

    constructor(
        id: String?,
        title: String?,
        price: String?,
        desc: String?,
        qty: String?,
        image: String?
    ) {
        this.id = id

        this.title = title
        this.image = image

        this.price = price
        this.desc = desc
        this.qty = qty
    }
}

