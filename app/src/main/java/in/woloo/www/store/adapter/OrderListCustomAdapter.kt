package `in`.woloo.www.store.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import `in`.woloo.www.R
import `in`.woloo.www.databinding.OrderDetailsListItemBinding
import `in`.woloo.www.store.orders_response.ItemsListData
import `in`.woloo.www.store.orders_response.OrderListResponse
import `in`.woloo.www.store.orders_response.OrdersListData
import `in`.woloo.www.store.screens.ShopOrderDetailsActivity

class OrderListCustomAdapter (private val context: Activity,
                              private var orderDetailsList: ArrayList<OrdersListData>,
                              private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<OrderListCustomAdapter.ViewHolder>() {

    lateinit var binding:OrderDetailsListItemBinding

    class ViewHolder(val binding: OrderDetailsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = OrderDetailsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        try {
            val order = orderDetailsList[position]



            val itemsAdapter = OrdersItemsAdapter(order.items ?: emptyList() , order , context , fragmentManager)
            holder.binding.ordersRecycler.layoutManager = LinearLayoutManager(context)
            holder.binding.ordersRecycler.adapter = itemsAdapter

          //  holder.binding.orderId.text = order.id


        }catch (e :Exception)
        {

        }



    }

    override fun getItemCount(): Int = orderDetailsList.size

}