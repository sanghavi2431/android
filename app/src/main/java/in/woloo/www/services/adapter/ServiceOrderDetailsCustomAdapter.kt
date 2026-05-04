package `in`.woloo.www.services.adapter

import android.app.Activity
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import `in`.woloo.www.databinding.OrderStatusListAdapterBinding
import `in`.woloo.www.store.orders_response.ItemsListDataOrderSet

class ServiceOrderDetailsCustomAdapter (private val context: Activity,
                                        private var orderDetailsList: ArrayList<ItemsListDataOrderSet>,)
    : RecyclerView.Adapter<ServiceOrderDetailsCustomAdapter.ViewHolder>() {

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

            holder.binding.productName.text = order.productTitle
            holder.binding.productPrice.text = order.total.toString()
            holder.binding.variantDetails.text = order.variantTitle
            holder.binding.productQuantity.text = "Qty: " + order.quantity.toString()

            holder.binding.productPrice.text =
                "₹ " + order.unitPrice.toString() + "/-"
            if(order.compareAtUnitPrice != null) {
                holder.binding.productPriceOriginal.apply {
                    text =
                        "MRP ₹ " + order.compareAtUnitPrice.toString() + "/-"
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            }
            else{
                holder.binding.productPriceOriginal.apply {
                    text =
                        "MRP ₹ " + order.unitPrice.toString() + "/-"
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            }

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