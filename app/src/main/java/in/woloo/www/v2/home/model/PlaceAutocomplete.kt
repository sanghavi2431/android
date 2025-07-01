package `in`.woloo.www.v2.home.model

class PlaceAutocomplete(
    @JvmField
    var placeId: CharSequence,
    @JvmField
    var area: CharSequence,
    @JvmField
    var address: CharSequence
) {
    override fun toString(): String {
        return area.toString()
    }
}