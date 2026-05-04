package `in`.woloo.www.store.categories_response

import com.google.gson.annotations.SerializedName

data class MetadataCategories(


    @SerializedName("image") val image: String?,
    @SerializedName("background_color") val background_color: String?,
    @SerializedName("videos") val videoUrl: String?,
    @SerializedName("listing_video") val listingVideoUrl: String?


)
