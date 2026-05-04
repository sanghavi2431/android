package `in`.woloo.www.store.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getDrawable
import androidx.media3.common.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.StoreProductCollectionListItemBinding
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.cart_request_response.CartAddRequest
import `in`.woloo.www.store.cart_request_response.CartLineItems
import `in`.woloo.www.store.cart_request_response.CartParams
import `in`.woloo.www.store.cart_request_response.CartResponse
import `in`.woloo.www.store.cart_request_response.CartUpdateRequest
import `in`.woloo.www.store.cart_request_response.ComparedItemsDataForCart
import `in`.woloo.www.store.product_response.NotifyRequest
import `in`.woloo.www.store.screens.StoreProductDetailsActivity
import `in`.woloo.www.store.screens.StoreProductListActivity
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.store.reviews.ReviewsResponse
import `in`.woloo.www.store.screens.StoreListingFragment
import `in`.woloo.www.store.user_details.WishLIstResponse
import `in`.woloo.www.store.user_details.WishListItem
import `in`.woloo.www.store.user_details.WishListItemsClass
import `in`.woloo.www.store.user_details.WishListRequest
import `in`.woloo.www.utils.Logger
import kotlin.math.round

class ProductsCollectionsCustomeAdapter (private val context: Activity ,
                                         private var productList: ArrayList<ProductListData> ,
                                         private val storeViewModel: StoreViewModel) : RecyclerView.Adapter<ProductsCollectionsCustomeAdapter.ViewHolder>() {
    lateinit var binding: StoreProductCollectionListItemBinding
     var cart : CartParams? = null
    var review : ReviewsResponse? = null
    private val recentlyChangedItems = mutableMapOf<Int, Long>()


    fun updateCart(newCart: CartParams?) {
        cart = newCart

        notifyDataSetChanged()
    }


    fun updateReviews(newReviews: ReviewsResponse?) {
        review = newReviews
        notifyDataSetChanged()
    }






    class ViewHolder(val binding: StoreProductCollectionListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = StoreProductCollectionListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        try {
            if(context is StoreProductDetailsActivity)
            {
                binding.addCartView.visibility = View.GONE
                holder.binding.quantityLayout.visibility = View.GONE
            }
            else {
                try {
                    if(cart != null && cart!!.items != null) {
                        var cartItemFound =
                            cart!!.items!!.find { it.variant_id == product.variants!![0].id }
                        var quantityToCalculate: Int = 0
                        if (cartItemFound == null) {

                            binding.addCartView.visibility = View.VISIBLE
                            holder.binding.quantityLayout.visibility = View.GONE

                        } else {

                            binding.addCartView.visibility = View.GONE
                            holder.binding.quantityLayout.visibility = View.VISIBLE
                            holder.binding.cartCountText.text = cartItemFound.quantity.toString()
                        }
                    }
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
                val screenWidth = Resources.getSystem().displayMetrics.widthPixels
                val itemWidth = (screenWidth / 3) - 40  // Subtract margin (8dp on each side)

                val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
                layoutParams.width = itemWidth
                layoutParams.setMargins(20, 0, 8, 20) // Left, Top, Right, Bottom margins (8dp each)

                holder.itemView.layoutParams = layoutParams

            if(!product.tags.isNullOrEmpty())
            {
                holder.binding.tagText.visibility = View.VISIBLE
                holder.binding.tagText.text = product.tags!![0].value
            }
            else
            {
                holder.binding.tagText.visibility = View.GONE
            }

            val changedTime = recentlyChangedItems[position]
            val cartItem = cart?.items?.find { it.variant_id == product.variants?.getOrNull(0)?.id }
            var quantity = cartItem?.quantity ?: 0




            if(review?.data?.reviews?.isNotEmpty() == true)
            {
                holder.binding.reviewCount.text = "("+review!!.data!!.reviews!!.size.toString()+")"
            }
            else{
                holder.binding.reviewCount.text = "(0)"
            }

            Logger.i("Aarati Store Variants" , productList[position].variants!!.size.toString() + " " + product.title)
            if (productList[position].variants!!.size >= 2) {
                holder.binding.variantCountText.visibility = View.VISIBLE
                holder.binding.variantCountText.text = product.variants!!.size.toString() + " options"
            }
            else{
                holder.binding.variantCountText.visibility = View.GONE
            }

            val sizeValue = product.options
                ?.firstOrNull { it.title.equals("size", ignoreCase = true) || it.title.equals("sizes", ignoreCase = true) }
                ?.values
                ?.firstOrNull()
                ?.value

            if (sizeValue != null) {
                Logger.d("Aarati SizeValue", "First size: $sizeValue + ${product.variants!!.get(0).toString()}")
                holder.binding.sizeText.visibility = View.VISIBLE
                holder.binding.sizeText.text = sizeValue
            } else {
                Logger.d("Aarati SizeValue", "Size option not found or no values")
                holder.binding.sizeText.visibility = View.GONE
            }



            if(product.variants!!.get(0).inventoryQuantity == 0)
            {
                holder.binding.productImageOverlay.visibility = View.VISIBLE
                holder.binding.addCartViewText.visibility = View.GONE
                holder.binding.tagText.visibility = View.VISIBLE
                holder.binding.tagText.text = "Out of Stock"
                holder.binding.notifyCartView.visibility = View.VISIBLE
                if(product.variants!!.get(0).isNotifiedProduct == true)
                {
                    holder.binding.notifyCartViewText.text = "Notified"
                }
                else{
                    holder.binding.notifyCartViewText.text = "Notify"
                }
            }
            else{
                holder.binding.productImageOverlay.visibility = View.GONE
                holder.binding.notifyCartView.visibility = View.GONE
                holder.binding.addCartView.visibility = View.VISIBLE
            }

                holder.binding.productName.text = product.title
                    holder.binding.reviewCount.text = "(" + product.reviewCount.toString() +")"

            val stars = round(product.avarageRating!!).toInt()

            if(stars == 1)
            {
                holder.binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                holder.binding.ivBad.setImageResource(R.drawable.empty_star_new)
                holder.binding.ivAverage.setImageResource(R.drawable.empty_star_new)
                holder.binding.ivGood.setImageResource(R.drawable.empty_star_new)
                holder.binding.ivLovedIt.setImageResource(R.drawable.empty_star_new)
            }
            else if(stars == 2)
            {
                holder.binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                holder.binding.ivBad.setImageResource(R.drawable.filled_star_new)
                holder.binding.ivAverage.setImageResource(R.drawable.empty_star_new)
                holder.binding.ivGood.setImageResource(R.drawable.empty_star_new)
                holder.binding.ivLovedIt.setImageResource(R.drawable.empty_star_new)
            }
            else if(stars == 3)
            {
                holder.binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                holder.binding.ivBad.setImageResource(R.drawable.filled_star_new)
                holder.binding.ivAverage.setImageResource(R.drawable.filled_star_new)
                holder.binding.ivGood.setImageResource(R.drawable.empty_star_new)
                holder.binding.ivLovedIt.setImageResource(R.drawable.empty_star_new)
            }
            else if(stars == 4)
            {
                holder.binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                holder.binding.ivBad.setImageResource(R.drawable.filled_star_new)
                holder.binding.ivAverage.setImageResource(R.drawable.filled_star_new)
                holder.binding.ivGood.setImageResource(R.drawable.filled_star_new)
                holder.binding.ivLovedIt.setImageResource(R.drawable.empty_star_new)
            }
            else if(stars == 5)
            {
                holder.binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                holder.binding.ivBad.setImageResource(R.drawable.filled_star_new)
                holder.binding.ivAverage.setImageResource(R.drawable.filled_star_new)
                holder.binding.ivGood.setImageResource(R.drawable.filled_star_new)
                holder.binding.ivLovedIt.setImageResource(R.drawable.filled_star_new)
            }
            else{
                holder.binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                holder.binding.ivBad.setImageResource(R.drawable.filled_star_new)
                holder.binding.ivAverage.setImageResource(R.drawable.filled_star_new)
                holder.binding.ivGood.setImageResource(R.drawable.filled_star_new)
                holder.binding.ivLovedIt.setImageResource(R.drawable.filled_star_new)
            }

              //  holder.binding.productVendor.text = product.collection!!.title
                holder.binding.productPrice.text =
                    "₹" + product.variants!!.get(0).calculated_price!!.calculatedAmount.toString()+"/-"
            holder.binding.productPriceOriginal.apply{
                text =
                    "MRP ₹" + product.variants!!.get(0).calculated_price!!.originalAmount.toString()+"/-"
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
                try {
                    if (product.thumbnail != null) {
                        Glide.with(holder.itemView.context)
                            .load(product.thumbnail) // URL from API
                            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache optimization
                            .into(holder.binding.productImage)
                    } else if (product.images!!.get(0).url != null) {
                        Glide.with(holder.itemView.context)
                            .load(product.images!!.get(0).url) // URL from API
                            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache optimization
                            .error(R.drawable.feather_img)
                            .into(holder.binding.productImage)
                    }
                } catch (e: Exception) {
                }
                val productJson = Gson().toJson(productList[position])
                holder.binding.root.setOnClickListener {
                    if(context is WolooDashboard) {
                        val intent = Intent(context, StoreProductDetailsActivity::class.java)
                        intent.putExtra(
                            "IS_SHOW_BACK_BUTTON",
                            true
                        ) // Pass the boolean value if needed
                        intent.putExtra("PRODUCT_DETAILS", productJson)
                        intent.putExtra("CALLINGACTIVITY", "STORELISTING")
                        context.startActivity(intent)
                    }
                    else
                    {
                        val intent = Intent(context, StoreProductDetailsActivity::class.java)
                        intent.putExtra(
                            "IS_SHOW_BACK_BUTTON",
                            true
                        ) // Pass the boolean value if needed
                        intent.putExtra("PRODUCT_DETAILS", productJson)
                        context.startActivity(intent)
                    }
                }

                val variantId = product.variants?.get(0)?.id


           binding.heartImage.visibility = View.GONE
           /*     binding.heartImage.setImageResource(
                    if (isInWishlist == true) R.drawable.favorite_blogs_icon
                    else R.drawable.like_blog
                )

                holder.binding.heartImage.setOnClickListener {
                    val variantIdClicked = product.variants?.get(0)?.id
                    val wishlistItem = wishlist?.find { it.productVariantId == variantIdClicked }
                    val wishlistId = wishlistItem?.id
                    if (wishlistItem != null) {

                        binding.heartImage.setImageResource(R.drawable.like_blog)
                        storeViewModel.deleteWishListItem(wishlistId!!)
                    } else {

                        binding.heartImage.setImageResource(R.drawable.favorite_blogs_icon)
                        var request = WishListRequest()
                        request.variantId = product.variants!![0].id
                        storeViewModel.addWishListItem(request)
                    }
                    notifyItemChanged(position)
                }
*/
                for (i in product.variants!!.indices) {
                    var cartItemFound =
                        cart?.items?.find { it.variant_id == product.variants!![i].id }
                    if (cartItemFound != null) {
                        holder.binding.cartCountText.text = cartItemFound.quantity.toString()
                        // to revert changes 18 jun next to lines
                        binding.addCartView.visibility = View.GONE
                        holder.binding.quantityLayout.visibility = View.VISIBLE
                        break
                    } else {
                        holder.binding.cartCountText.text = "0"
                        // to revert changes 18 jun next to lines
                        binding.addCartView.visibility = View.VISIBLE
                        holder.binding.quantityLayout.visibility = View.GONE
                    }
                }

            holder.binding.notifyCartView.setOnClickListener {

                val request = NotifyRequest()
                request.variantId = product.variants!!.get(0).id.toString()
                request.userMobileNumber = SharedPrefSettings.getPreferences.fetchUserDetails()?.mobile.toString()
             //   request.salesChannelId = product
                storeViewModel.getNotifyUserForProduct(request)

            }

                holder.binding.removeCartItemImage.setOnClickListener {

                    lateinit var lineId: String
                    var cartItemFound =
                        cart!!.items!!.find { it.variant_id == product.variants!![0].id }
                    if (cartItemFound != null) {
                        lineId = cartItemFound.line_id.toString()
                        Logger.i("Aarati Store line id ADAPTER", "clicked for sending {$lineId}")

                        val quantityToCalculate: Int = (cartItemFound.quantity.toString()).toInt()
                        if (quantityToCalculate > 1) {
                            val request2 = CartUpdateRequest()
                            request2.quantity = quantityToCalculate - 1
                            storeViewModel.getUpdateToCart(
                                SharedPrefSettings.getPreferences.fetchCartId().toString(),
                                lineId,
                                request2
                            )
                            holder.binding.cartCountText.text = cartItemFound.quantity.toString()
                            binding.addCartView.visibility = View.GONE
                            holder.binding.quantityLayout.visibility = View.VISIBLE
                        } else if (quantityToCalculate == 1) {
                            storeViewModel.getDeleteFromCart(
                                SharedPrefSettings.getPreferences.fetchCartId().toString(), lineId
                            )
                           // holder.binding.cartCountText.text = cartItemFound.quantity.toString()
                            holder.binding.quantityLayout.visibility = View.GONE
                            holder.binding.addCartView.visibility = View.VISIBLE
                        } else {
                            Logger.d("aarati Store", "no product in cart")
                        }
                        notifyDataSetChanged()
                    }
                }
                holder.binding.addCartItemImage.setOnClickListener {
                    var cartItemFound =
                        cart!!.items!!.find { it.variant_id == product.variants!![0].id }
                    var quantityToCalculate: Int = 0
                    if (cartItemFound == null) {

                        val request1 = CartAddRequest()
                        request1.variant_id = product.variants?.get(0)?.id.toString()
                        request1.quantity = 1
                        storeViewModel.getAddToCart(
                            SharedPrefSettings.getPreferences.fetchCartId().toString(), request1
                        )
                    } else {
                        val request1 = CartUpdateRequest()
                        request1.quantity = cartItemFound.quantity.toString().toInt() + 1

                        storeViewModel.getUpdateToCart(
                            SharedPrefSettings.getPreferences.fetchCartId().toString(),
                            cartItemFound.line_id.toString(),
                            request1
                        )

                    }
                }

                holder.binding.addCartView.setOnClickListener {
                    try {
                         var cartItemFound : CartLineItems? = null
                        var quantityToCalculate: Int = 0
                        if(cart == null){
                            Logger.d("aarati Store", "cart is null")
                            cartItemFound = null
                        }
                        else
                        {
                            if (cart!!.items != null){
                                Logger.d("aarati Store", "cart is  not null and items are not null")
                                for(i in cart!!.items!!.indices)
                                {
                                    if(cart!!.items!![i].variants != null)
                                    {
                                        Logger.d("aarati Store", "cart is not null , items are not null , variants are not null")
                                        if(cart!!.items!!.find { it -> it.variant_id == product.variants!![0].id } != null) {
                                            cartItemFound =
                                                cart!!.items!!.find { it -> it.variant_id == product.variants!![0].id }!!
                                        }
                                        else {
                                            cartItemFound = null
                                        }
                                    }
                                    else
                                    {
                                        Logger.d("aarati Store", "cart is not null , items are not null , variants are null")
                                        cartItemFound = null
                                    }
                                }


                            }
                            else{
                                Logger.d("aarati Store", "cart is not null and items are null")
                                cartItemFound = null
                            }

                        }

                        if (cartItemFound == null) {
                            // quantityToCalculate = (cartItemFound.quantity.toString()).toInt()

                            val request1 = CartAddRequest()
                            request1.variant_id = product.variants?.get(0)?.id.toString()
                            request1.quantity = 1
                            storeViewModel.getAddToCart(
                                SharedPrefSettings.getPreferences.fetchCartId().toString(), request1
                            )
                            holder.binding.addCartViewText.text = "ADDED!"
                            holder.binding.variantCountText.visibility = View.GONE
                            holder.binding.addCartView.background = ContextCompat.getDrawable(
                                holder.itemView.context,
                                R.drawable.yello_rectangle_shape
                            )

                            val originalText = "Add"
                            val originalBackground = ContextCompat.getDrawable(
                                holder.itemView.context,
                                R.drawable.boarder_yellow_white_background
                            )

// Restore after 10–15 seconds
                            Handler(Looper.getMainLooper()).postDelayed({
                                holder.binding.addCartViewText.text = originalText
                                holder.binding.addCartView.background = originalBackground
                            }, 3000)

                        } else {

                            val request1 = CartUpdateRequest().apply {
                                quantity = cartItemFound.quantity.toString().toInt() + 1
                            }
                            storeViewModel.getUpdateToCart(
                                SharedPrefSettings.getPreferences.fetchCartId().toString(),
                                cartItemFound.line_id.toString(),
                                request1
                            )

                        }


                    }catch (e : Exception)
                    {
                        CommonUtils.printStackTrace(e)
                    }
                }
        }catch (e :Exception)
        {
            CommonUtils.printStackTrace(e)
        }
    }
    override fun getItemCount(): Int = productList.size
}