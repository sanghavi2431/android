package `in`.woloo.www.services.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.databinding.DeliveryDetailsListItemBinding
import `in`.woloo.www.databinding.ServiceDeliveryListItemBinding
import `in`.woloo.www.services.ServiceViewModel
import `in`.woloo.www.services.screens.DateTimeBottomSheetFragment
import `in`.woloo.www.services.screens.ShowSelectedSlotsActivity
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.CartItemsCustomAdapter
import `in`.woloo.www.store.cart_request_response.CartAddRequest
import `in`.woloo.www.store.cart_request_response.CartLineItems
import `in`.woloo.www.store.cart_request_response.CartUpdateRequest
import `in`.woloo.www.store.cart_request_response.CartUpdateRequestHygiene
import `in`.woloo.www.store.cart_request_response.MetaDataLineItemsRequest

class ServicesCartItemCustomAdapter (private val context: FragmentActivity, private var cartItemsList: ArrayList<CartLineItems>,
                                     private val servicesViewModel: ServiceViewModel,
                                     private val isFromBottomSheet: Boolean
) : RecyclerView.Adapter<ServicesCartItemCustomAdapter.ViewHolder>()
{

    // private val itemList = MutableList(itemCount) { "Item ${it + 1}" }
    lateinit var binding: ServiceDeliveryListItemBinding
    var cartItemsCustomAdapter : ServiceDateTimeOfCartAdapter? = null


    class ViewHolder(val binding: ServiceDeliveryListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ServiceDeliveryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cartItem = cartItemsList[position]

        holder.binding.productName.text = cartItem.product_title
        holder.binding.cartCountText.text = cartItem.quantity.toString()
        holder.binding.cartCountTextBottomSheet.text = "Qty: " + cartItem.quantity.toString()


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

        holder.binding.productPrice.text =
            "₹ " + cartItem.unit_price.toString() + "/-"
        /*if(cartItem.compare_at_unit_price != null) {
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
        }*/

        holder.binding.productPriceOriginal.visibility = View.GONE

        cartItemsCustomAdapter =
            ServiceDateTimeOfCartAdapter(context, cartItemsList, cartItem.metadata!!.servieceDataResponse!! , servicesViewModel!! , position , isFromBottomSheet)
        binding.serviceDateTimeRecycler.layoutManager = LinearLayoutManager(context)
        binding.serviceDateTimeRecycler.adapter = cartItemsCustomAdapter

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
            Log.i("Aarati Store cart image " , cartItem.thumbnail.toString())
            if (cartItem.thumbnail != null) {
                Log.i("Aarati Store cart image 1" , cartItem.thumbnail.toString())
                Glide.with(holder.itemView.context)
                    .load(cartItem.thumbnail) // URL from API
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache optimization
                    .into(holder.binding.bottomHostImage)
            }
        }catch (e :Exception)
        {

        }

        holder.binding.removeItemFromCart.setOnClickListener {
            servicesViewModel.getDeleteFromCart( SharedPrefSettings.getPreferences.fetchServiceCartId().toString() , cartItem.line_id!!)
            notifyDataSetChanged()
        }

        holder.binding.removeCartItemImage.setOnClickListener {

            val quantityToCalculate : Int = (cartItem.quantity.toString()).toInt()
            if(quantityToCalculate > 1) {
                val bottomSheetFragment = ShowSelectedSlotsActivity.newInstance(cartItem , "CART_SCREEN_REMOVE")
                bottomSheetFragment.show(context.supportFragmentManager, bottomSheetFragment.tag)

              /*  val request2 = CartUpdateRequestHygiene()
                request2.quantity = quantityToCalculate - 1
                servicesViewModel.getUpdateToCart(
                    SharedPrefSettings.getPreferences.fetchServiceCartId().toString(),
                    cartItem.line_id.toString(),
                    request2
                )
                notifyDataSetChanged()
                holder.binding.cartCountText.text = cartItem.quantity.toString()*/
            }
            else if(quantityToCalculate == 1){
                servicesViewModel.getDeleteFromCart( SharedPrefSettings.getPreferences.fetchServiceCartId().toString() ,cartItem.line_id.toString() )
                notifyDataSetChanged()
            }
        }




        holder.binding.addCartItemImage.setOnClickListener{
            val bottomSheetFragment = DateTimeBottomSheetFragment.newInstance(cartItem , "CART_SCREEN_ADD")
            bottomSheetFragment.show(context.supportFragmentManager, bottomSheetFragment.tag)
           /* var quantityToCalculate : Int = 0
            quantityToCalculate = (cartItem.quantity.toString()).toInt()
            if(quantityToCalculate >= 1)
            {
                val request2 = CartUpdateRequestHygiene()
                request2.quantity = quantityToCalculate + 1
                servicesViewModel.getUpdateToCart(
                    SharedPrefSettings.getPreferences.fetchServiceCartId().toString(),
                    cartItem.line_id.toString(),
                    request2
                )
                notifyDataSetChanged()
                holder.binding.cartCountText.text = cartItem.quantity.toString()
            }
            else {
                val request1 = CartAddRequest()
                request1.variant_id = cartItem.variant_id
                request1.quantity = quantityToCalculate + 1
                servicesViewModel.getAddToCart(
                    SharedPrefSettings.getPreferences.fetchServiceCartId().toString(), request1
                )
                notifyDataSetChanged()
            }*/
        }





    }

    override fun getItemCount(): Int = cartItemsList.size
}