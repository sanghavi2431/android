package `in`.woloo.www.store.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import `in`.woloo.www.databinding.OrderStatusListAdapterBinding
import `in`.woloo.www.store.adapter.OrderDetailsCustomAdapter
import `in`.woloo.www.store.cart_request_response.CartLineItems
import `in`.woloo.www.store.orders_response.ItemsListData

class CartOrderDisplayItemsAdapter (private val context: Activity, private var orderDetailsList: ArrayList<CartLineItems>,) : RecyclerView.Adapter<CartOrderDisplayItemsAdapter.ViewHolder>() {

    lateinit var binding: OrderStatusListAdapterBinding

    class ViewHolder(val binding: OrderStatusListAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            OrderStatusListAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val order = orderDetailsList[position]

            holder.binding.productName.text = order.product_title
            holder.binding.productPrice.text = order.unit_price.toString()
            holder.binding.variantDetails.text = order.variant_title
            holder.binding.productQuantity.text = order.quantity.toString()

            Log.i("Aarati Store cart image " , order.thumbnail.toString())
            if (order.thumbnail != null) {
                Log.i("Aarati Store cart image 1" , order.thumbnail.toString())
                Glide.with(holder.itemView.context)
                    .load(order.thumbnail) // URL from API
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache optimization
                    .into(holder.binding.bottomHostImage)
            }
        }catch (e :Exception)
        {

        }



    }

    override fun getItemCount(): Int = orderDetailsList.size
}