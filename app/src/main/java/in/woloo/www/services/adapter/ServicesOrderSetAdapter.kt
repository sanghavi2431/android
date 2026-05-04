package `in`.woloo.www.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.databinding.OrderSetListItemBinding
import `in`.woloo.www.services.ServiceViewModel
import `in`.woloo.www.services.screens.ServicesOrderSummaryBottomSheetFragment
import `in`.woloo.www.store.orders_response.OrderSetResponse

class ServicesOrderSetAdapter (
    private val context: FragmentActivity,
    private var orderSets: ArrayList<OrderSetResponse>,
    private val fragmentManager: FragmentManager
) : RecyclerView.Adapter<ServicesOrderSetAdapter.ViewHolder>() {

    lateinit var binding: OrderSetListItemBinding

    class ViewHolder(val binding: OrderSetListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = OrderSetListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderSet = orderSets[position]

        holder.binding.orderId.text = orderSet.id // Or use any identifier you have

        holder.binding.orderId.setOnClickListener{
            val bottomSheetFragment = ServicesOrderSummaryBottomSheetFragment.newInstance(orderSet)
            bottomSheetFragment.show(context.supportFragmentManager, bottomSheetFragment.tag)
        }

        holder.binding.root.setOnClickListener {

        }

        orderSet.orders?.let { orders ->
            val orderListAdapter = ServicesOrderListCustomAdapter(context, ArrayList(orders) , fragmentManager)
            holder.binding.ordersRecycler.layoutManager = LinearLayoutManager(context)
            holder.binding.ordersRecycler.adapter = orderListAdapter
        }
    }

    override fun getItemCount() = orderSets.size
}