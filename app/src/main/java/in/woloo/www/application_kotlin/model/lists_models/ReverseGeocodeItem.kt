package `in`.woloo.www.application_kotlin.model.lists_models

import com.google.gson.annotations.SerializedName

class ReverseGeocodeItem {
        @SerializedName("formatted_address")
        val formattedAddress: String? = null

        @SerializedName("types")
        val types: List<String>? = null

        @SerializedName("geometry")
        val geometry: Geometry? = null

        @SerializedName("address_components")
        val addressComponents: List<AddressComponentsItem>? = null

        @SerializedName("plus_code")
        val plusCode: PlusCode? = null

        @SerializedName("place_id")
        val placeId: String? = null


    class AddressComponentsItem {
        @SerializedName("types")
        val types: List<String>? = null

        @SerializedName("short_name")
        val shortName: String? = null

        @SerializedName("long_name")
        val longName: String? = null
    }

    class Bounds {
        @SerializedName("southwest")
        val southwest: Southwest? = null

        @SerializedName("northeast")
        val northeast: Northeast? = null
    }

    class Geometry {
        @SerializedName("viewport")
        val viewport: Viewport? = null

        @SerializedName("bounds")
        val bounds: Bounds? = null

        @SerializedName("location")
        val location: Location? = null

        @SerializedName("location_type")
        val locationType: String? = null
    }

    class Location {
        @SerializedName("lng")
        val lng = 0.0

        @SerializedName("lat")
        val lat = 0.0
    }

    class Northeast {
        @SerializedName("lng")
        val lng = 0.0

        @SerializedName("lat")
        val lat = 0.0
    }

    class PlusCode {
        @SerializedName("compound_code")
        val compoundCode: String? = null

        @SerializedName("global_code")
        val globalCode: String? = null
    }

    class Southwest {
        @SerializedName("lng")
        val lng = 0.0

        @SerializedName("lat")
        val lat = 0.0
    }

    class Viewport {
        @SerializedName("southwest")
        val southwest: Southwest? = null

        @SerializedName("northeast")
        val northeast: Northeast? = null
    }
}