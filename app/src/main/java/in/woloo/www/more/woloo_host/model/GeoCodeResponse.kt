package `in`.woloo.www.more.woloo_host.model

import com.google.gson.annotations.SerializedName

class GeoCodeResponse {
    @JvmField
	@SerializedName("data")
    val data: List<DataItem>? = null

    @SerializedName("message")
    val message: String? = null

    @JvmField
	@SerializedName("status")
    val status: String? = null

    inner class DataItem {
        @JvmField
		@SerializedName("formatted_address")
        val formattedAddress: String? = null

        @JvmField
		@SerializedName("types")
        val types: List<String>? = null

        @JvmField
		@SerializedName("geometry")
        val geometry: Geometry? = null

        @JvmField
		@SerializedName("address_components")
        val addressComponents: List<AddressComponentsItem>? = null

        @SerializedName("plus_code")
        val plusCode: PlusCode? = null

        @SerializedName("place_id")
        val placeId: String? = null
    }

    inner class AddressComponentsItem {
        @JvmField
		@SerializedName("types")
        val types: List<String>? = null

        @SerializedName("short_name")
        val shortName: String? = null

        @JvmField
		@SerializedName("long_name")
        val longName: String? = null
    }

    inner class Bounds {
        @SerializedName("southwest")
        val southwest: Southwest? = null

        @SerializedName("northeast")
        val northeast: Northeast? = null
    }

    inner class Geometry {
        @SerializedName("viewport")
        val viewport: Viewport? = null

        @SerializedName("bounds")
        val bounds: Bounds? = null

        @JvmField
		@SerializedName("location")
        val location: Location? = null

        @SerializedName("location_type")
        val locationType: String? = null
    }

    inner class Location {
        @SerializedName("lng")
        val lng = 0.0

        @JvmField
		@SerializedName("lat")
        val lat = 0.0
    }

    inner class Northeast {
        @SerializedName("lng")
        val lng = 0.0

        @SerializedName("lat")
        val lat = 0.0
    }

    inner class PlusCode {
        @SerializedName("compound_code")
        val compoundCode: String? = null

        @SerializedName("global_code")
        val globalCode: String? = null
    }

    inner class Southwest {
        @SerializedName("lng")
        val lng = 0.0

        @SerializedName("lat")
        val lat = 0.0
    }

    inner class Viewport {
        @SerializedName("southwest")
        val southwest: Southwest? = null

        @SerializedName("northeast")
        val northeast: Northeast? = null
    }
}