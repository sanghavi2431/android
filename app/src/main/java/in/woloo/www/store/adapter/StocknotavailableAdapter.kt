package `in`.woloo.www.store.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.databinding.DeliveryDetailsListItemBinding
import `in`.woloo.www.databinding.StockNotAvailableListItemBinding
import `in`.woloo.www.databinding.StoreAddressListItemBinding
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.AddressesCustomAdapter.ViewHolder
import `in`.woloo.www.store.cart_request_response.CartAddRequest
import `in`.woloo.www.store.cart_request_response.CartLineItems
import `in`.woloo.www.store.cart_request_response.CartParams
import `in`.woloo.www.store.cart_request_response.CartUpdateRequest
import `in`.woloo.www.store.categories_response.CategoriesListData
import `in`.woloo.www.store.product_response.NotifyRequest

class StocknotavailableAdapter (private val context: Activity,  private var cartItemsList: ArrayList<CartLineItems>,
                              private val storeViewModel: StoreViewModel
) : RecyclerView.Adapter<StocknotavailableAdapter.ViewHolder>()
{

    // private val itemList = MutableList(itemCount) { "Item ${it + 1}" }
    lateinit var binding: StockNotAvailableListItemBinding

    class ViewHolder(val binding: StockNotAvailableListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = StockNotAvailableListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartItemsList[position]
        try {
            holder.binding.productName.text = cartItem.product_title
            holder.binding.cartCountText.text = "Qty: ${cartItem.quantity.toString()}"

            Glide.with(context)
                .load(R.drawable.notify_icon_new) // your gif in res/drawable
                .into(binding.checkoutButtonImg)


            var variantOption: String? = null
            if(cartItem.variants!!.options!!.isNullOrEmpty())
            {
                holder.binding.variantDetails.text = cartItem.variants!!.title
            }
            else {
                val options = cartItem.variants?.options
                variantOption = options?.joinToString(" ") { it.value.orEmpty() }.orEmpty()

                holder.binding.variantDetails.text = if (variantOption.isBlank()) {
                    options?.getOrNull(0)?.value.orEmpty()
                } else {
                    variantOption
                }
            }




            try {

                val selectedColor = cartItem.variants!!.options!![0].value?.lowercase() ?: ""

                val matchingImages = cartItem.product!!.images?.filter {
                    it.url?.contains(selectedColor, ignoreCase = true) == true
                }?.map { it.url }
                if (matchingImages!!.isNotEmpty()) {
                    Log.i("Aarati Store cart image 1 new", matchingImages.toString())
                    Glide.with(holder.itemView.context)
                        .load(matchingImages.get(0)) // URL from API
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache optimization
                        .into(holder.binding.bottomHostImage)
                } else if (cartItem.thumbnail != null) {
                    Log.i("Aarati Store cart image 1", cartItem.thumbnail.toString())
                    Glide.with(holder.itemView.context)
                        .load(cartItem.thumbnail) // URL from API
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache optimization
                        .into(holder.binding.bottomHostImage)
                }
            } catch (e: Exception) {

            }


                storeViewModel.getDeleteFromCart(
                    SharedPrefSettings.getPreferences.fetchCartId().toString(), cartItem.line_id!!
                )

            holder.binding.checkoutButton.setOnClickListener{
                val request = NotifyRequest()
                request.variantId = cartItem.variant_id.toString()
                request.userMobileNumber = SharedPrefSettings.getPreferences.fetchUserDetails()?.mobile.toString()
                //   request.salesChannelId = product
                storeViewModel.getNotifyUserForProduct(request)
            }

            holder.binding.checkoutButtonImg.setOnClickListener{
                val request = NotifyRequest()
                request.variantId = cartItem.variant_id.toString()
                request.userMobileNumber = SharedPrefSettings.getPreferences.fetchUserDetails()?.mobile.toString()
                //   request.salesChannelId = product
                storeViewModel.getNotifyUserForProduct(request)
            }



        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    override fun getItemCount(): Int = cartItemsList.size
}