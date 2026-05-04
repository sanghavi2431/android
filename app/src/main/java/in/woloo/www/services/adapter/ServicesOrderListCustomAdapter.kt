package `in`.woloo.www.services.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.databinding.OrderDetailsListItemBinding
import `in`.woloo.www.services.ServiceViewModel
import `in`.woloo.www.store.orders_response.OrdersListData


class ServicesOrderListCustomAdapter (private val context: FragmentActivity,
                                      private var orderDetailsList: ArrayList<OrdersListData>,
                                      private val fragmentManager: FragmentManager,
) : RecyclerView.Adapter<ServicesOrderListCustomAdapter.ViewHolder>() {

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



            val itemsAdapter = ServiceOrdersItemsAdapter(context ,order.items ?: emptyList() , order  , fragmentManager)
            holder.binding.ordersRecycler.layoutManager = LinearLayoutManager(context)
            holder.binding.ordersRecycler.adapter = itemsAdapter



        }catch (e :Exception)
        {

        }



    }

    override fun getItemCount(): Int = orderDetailsList.size

}