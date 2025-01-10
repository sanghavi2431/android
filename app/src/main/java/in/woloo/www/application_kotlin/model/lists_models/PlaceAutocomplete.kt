package `in`.woloo.www.application_kotlin.model.lists_models

import java.io.Serializable

class PlaceAutocomplete(
    @JvmField
    var placeId: CharSequence,
    @JvmField
    var area: CharSequence,
    @JvmField
    var address: CharSequence
) : Serializable {
    override fun toString(): String {
        return area.toString()
    }
}