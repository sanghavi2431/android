package `in`.woloo.www.store.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.databinding.DeliveryDetailsListItemBinding
import `in`.woloo.www.databinding.StoreAddressListItemBinding
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.AddressesCustomAdapter.ViewHolder
import `in`.woloo.www.store.cart_request_response.CartAddRequest
import `in`.woloo.www.store.cart_request_response.CartLineItems
import `in`.woloo.www.store.cart_request_response.CartParams
import `in`.woloo.www.store.cart_request_response.CartUpdateRequest
import `in`.woloo.www.store.categories_response.CategoriesListData

class CartItemsCustomAdapter (private val context: Activity,  private var cartItemsList: ArrayList<CartLineItems>,
                              private val storeViewModel: StoreViewModel ,
                              private val isFromBottomSheet: Boolean
) : RecyclerView.Adapter<CartItemsCustomAdapter.ViewHolder>()
{

   // private val itemList = MutableList(itemCount) { "Item ${it + 1}" }
    lateinit var binding: DeliveryDetailsListItemBinding

    class ViewHolder(val binding: DeliveryDetailsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DeliveryDetailsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartItemsList[position]
        try {
            holder.binding.productName.text = cartItem.product_title
            //  holder.binding.productPrice.text =
            holder.binding.cartCountText.text = cartItem.quantity.toString()
            holder.binding.cartCountTextBottomSheet.text = "Qty: " + cartItem.quantity.toString()


            var variantOption: String? = null
            if(cartItem.variants!!.options!!.isNullOrEmpty())
            {
                holder.binding.variantDetails.text = cartItem.variants!!.title
            }
            else {
              /*  for (i in cartItem.variants!!.options!!.indices) {
                    variantOption =  variantOption + " " + cartItem.variants!!.options!![i].value
                }
                if (variantOption.isNullOrEmpty())
                    holder.binding.variantDetails.text = cartItem.variants!!.options!![0].value
                else
                    holder.binding.variantDetails.text = variantOption*/
                val options = cartItem.variants?.options
                 variantOption = options?.joinToString(" ") { it.value.orEmpty() }.orEmpty()

                holder.binding.variantDetails.text = if (variantOption.isBlank()) {
                    options?.getOrNull(0)?.value.orEmpty()
                } else {
                    variantOption
                }
            }

            holder.binding.productPrice.text =
                "₹ " + cartItem.unit_price.toString() + "/-"
            if(cartItem.compare_at_unit_price != null) {
            holder.binding.productPriceOriginal.apply {
                text =
                    "MRP ₹ " + cartItem.compare_at_unit_price.toString() + "/-"
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }
        else{
            holder.binding.productPriceOriginal.apply {
                text =
                    "MRP ₹ " + cartItem.unit_price.toString() + "/-"
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
        }



            if (isFromBottomSheet == true) {
                binding.removeItemFromCart.visibility = View.GONE
                binding.addCartView.visibility = View.GONE
                binding.quantityLayout.visibility = View.GONE
                binding.cartCountTextBottomSheet.visibility = View.VISIBLE


            } else {

                binding.removeItemFromCart.visibility = View.GONE
                binding.addCartView.visibility = View.GONE
                binding.quantityLayout.visibility = View.VISIBLE
                binding.cartCountTextBottomSheet.visibility = View.GONE

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



            holder.binding.removeItemFromCart.setOnClickListener {
                storeViewModel.getDeleteFromCart(
                    SharedPrefSettings.getPreferences.fetchCartId().toString(), cartItem.line_id!!
                )
                notifyDataSetChanged()
            }

            holder.binding.removeCartItemImage.setOnClickListener {

                val quantityToCalculate: Int = (cartItem.quantity.toString()).toInt()
                if (quantityToCalculate > 1) {
                    val request2 = CartUpdateRequest()
                    request2.quantity = quantityToCalculate - 1
                    storeViewModel.getUpdateToCart(
                        SharedPrefSettings.getPreferences.fetchCartId().toString(),
                        cartItem.line_id.toString(),
                        request2
                    )
                    notifyDataSetChanged()
                    holder.binding.cartCountText.text = cartItem.quantity.toString()
                } else if (quantityToCalculate == 1) {
                    storeViewModel.getDeleteFromCart(
                        SharedPrefSettings.getPreferences.fetchCartId().toString(),
                        cartItem.line_id.toString()
                    )
                    notifyDataSetChanged()
                }
            }


            holder.binding.addCartItemImage.setOnClickListener {
                var quantityToCalculate: Int = 0
                quantityToCalculate = (cartItem.quantity.toString()).toInt()
                if (quantityToCalculate >= 1) {
                    val request2 = CartUpdateRequest()
                    request2.quantity = quantityToCalculate + 1
                    storeViewModel.getUpdateToCart(
                        SharedPrefSettings.getPreferences.fetchCartId().toString(),
                        cartItem.line_id.toString(),
                        request2
                    )
                    notifyDataSetChanged()
                    holder.binding.cartCountText.text = cartItem.quantity.toString()
                } else {
                    val request1 = CartAddRequest()
                    request1.variant_id = cartItem.variant_id
                    request1.quantity = quantityToCalculate + 1
                    storeViewModel.getAddToCart(
                        SharedPrefSettings.getPreferences.fetchCartId().toString(), request1
                    )
                    notifyDataSetChanged()
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    override fun getItemCount(): Int = cartItemsList.size
}