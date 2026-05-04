package   `in`.woloo.www.store.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.CollectionsListItemBinding
import `in`.woloo.www.databinding.ProductImageRecyclerBinding
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.cart_request_response.CartAddRequest
import `in`.woloo.www.store.cart_request_response.CartParams
import `in`.woloo.www.store.cart_request_response.CartUpdateRequest
import `in`.woloo.www.store.product_response.ImagesProductListData
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.store.product_response.ValuesProductListData
import `in`.woloo.www.store.screens.StoreProductDetailsActivity
import `in`.woloo.www.store.screens.StoreProductListActivity
import `in`.woloo.www.store.user_details.WishListRequest
import `in`.woloo.www.utils.Logger

class ProductImageRecylcerAdapter (private val context: Activity,
                                   private var productData: ArrayList<ImagesProductListData>,
                                    private var thumbnail: String,
                                    private var colorlist: ArrayList<ValuesProductListData>,
                                   private val selectedColorPosition: Int ,
                                   private val variantId: String,
                                   private val isWishlisted: Boolean,
                                   private val wishlistItemId: String,
                                   private val storeViewModel: StoreViewModel
) : RecyclerView.Adapter<ProductImageRecylcerAdapter.ViewHolder>() {

    lateinit var binding: ProductImageRecyclerBinding

    private var selectedColorHex: String = colorlist.getOrNull(selectedColorPosition)!!.value.toString()
   /* private val thumbnailItem: ImagesProductListData? = productData.find {
        it.url == thumbnail && it.metadata == selectedColorHex
    }*/
    private var filteredProductData: List<ImagesProductListData> = productData.filter {
        //it.metadata == selectedColorHex &&
                it.url != thumbnail
    }


    class ViewHolder(val binding: ProductImageRecyclerBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            ProductImageRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Log.i("Aaarati Product Data 2" , "in Adapter ${isWishlisted}")


            val imageUrl = if (position == 0) {
                thumbnail
            } else {
                filteredProductData[position - 1].url
            }

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.productDetailsImage)

        Log.d("Aaarati Product Data 2" , "After Glide $isWishlisted")
        if(isWishlisted)
        {
            binding.heartImage.setImageResource(R.drawable.favorite_blogs_icon)

        }else
        {
            binding.heartImage.setImageResource(R.drawable.like_blog)

        }

        holder.binding.heartImage.setOnClickListener{
            if(!isWishlisted)
            {
                var request = WishListRequest()
                request.variantId = variantId
                storeViewModel.addWishListItem(request)
                binding.heartImage.setImageResource(R.drawable.favorite_blogs_icon)
            }
            else{

                binding.heartImage.setImageResource(R.drawable.like_blog)
                storeViewModel.deleteWishListItem(wishlistItemId)
            }

        }


    }

    override fun getItemCount(): Int =  filteredProductData.size + 1 // if (thumbnailItem != null) 1 else 0

    fun updateImageList(newProductData: ArrayList<ImagesProductListData>, newThumbnail: String, newSelectedColorPosition: Int) {
        this.productData = newProductData
        this.thumbnail = newThumbnail
        this.selectedColorHex = colorlist.getOrNull(newSelectedColorPosition)?.value.toString()

        // Re-filter based on new selection
        this.filteredProductData = productData.filter {
            it.url != thumbnail
        }

        notifyDataSetChanged()
    }

}



