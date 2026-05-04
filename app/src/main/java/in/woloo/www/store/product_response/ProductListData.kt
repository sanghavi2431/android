package `in`.woloo.www.store.product_response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import `in`.woloo.www.store.categories_response.CategoriesListData


class ProductListData()  {
    @SerializedName("id")
    var id: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("subtitle")
    var subtitle: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("handle")
    var handle: String? = null

    @SerializedName("is_giftcard")
    var is_giftcard: String? = null

    @SerializedName("discountable")
    var discountable: String? = null

    @SerializedName("thumbnail")
    var thumbnail: String? = null

    @SerializedName("collection_id")
    var collection_id: String? = null

    @SerializedName("type_id")
    var type_id: String? = null

    @SerializedName("weight")
    var weight: String? = null

    @SerializedName("length")
    var length: String? = null

    @SerializedName("height")
    var height: String? = null

    @SerializedName("width")
    var width: String? = null

    @SerializedName("hs_code")
    var hs_code: String? = null

    @SerializedName("origin_country")
    var origin_country: String? = null

    @SerializedName("mid_code")
    var mid_code: String? = null

    @SerializedName("material")
    var material: String? = null

    @SerializedName("created_at")
    var created_at: String? = null

    @SerializedName("updated_at")
    var updated_at: String? = null

    @SerializedName("type")
    var type: Any? = null

    @SerializedName("collection")
    var collection: CollectionProductListData? = null

    @SerializedName("options")
    var options: ArrayList<OptionsProductListData>? = null


    @SerializedName("tags")
    var tags: ArrayList<TagsProductListData>? = null


    @SerializedName("metadata")
    var metadata: MetadataProductListData? = null


    @SerializedName("images")
    var images: ArrayList<ImagesProductListData>? = null


    @SerializedName("variants")
    var variants: ArrayList<VariantsProductListData>? = null

    @SerializedName("categories")
    var categories
            : ArrayList<CategoriesListData>? = null

    @SerializedName("average_rating")
    var avarageRating: Double? = null

    @SerializedName("review_count")
    var reviewCount: Int? = null

}

data class  MetadataProductListData
    (
    @SerializedName("service_image")
    var serviceImage: String? = null ,

    @SerializedName("background_image")
    var backgroundImage: String? = null,

            @SerializedName("service_title")
            var serviceTitle: String? = null

)
