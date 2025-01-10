package `in`.woloo.www.shopping.model

class AddressModel {
    @JvmField
    var id: String? = null
    @JvmField
    var name: String? = null
    @JvmField
    var phone: String? = null
    @JvmField
    var pincode: String? = null
    @JvmField
    var city: String? = null
    @JvmField
    var state: String? = null
    @JvmField
    var area: String? = null
    @JvmField
    var flat_building: String? = null
    @JvmField
    var landmark: String? = null

    constructor()

    constructor(
        id: String?,
        name: String?,
        phone: String?,
        pincode: String?,
        city: String?,
        state: String?,
        area: String?,
        flat_building: String?,
        landmark: String?
    ) {
        this.id = id
        this.name = name
        this.phone = phone
        this.pincode = pincode
        this.city = city
        this.state = state
        this.area = area
        this.flat_building = flat_building
        this.landmark = landmark
    }
}
