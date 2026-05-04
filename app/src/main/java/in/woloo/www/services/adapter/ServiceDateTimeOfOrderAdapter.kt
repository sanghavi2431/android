package `in`.woloo.www.services.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.databinding.ServiceDateTimeCartListItemBinding
import `in`.woloo.www.services.ServiceViewModel
import `in`.woloo.www.services.adapter.ServiceDateTimeOfCartAdapter
import `in`.woloo.www.services.screens.DateTimeBottomSheetFragment
import `in`.woloo.www.store.cart_request_response.CartLineItems
import `in`.woloo.www.store.cart_request_response.ServicesData
import `in`.woloo.www.store.orders_response.ItemsListDataOrderSet

class ServiceDateTimeOfOrderAdapter  (private val context: FragmentActivity,
                                      private var cartItemsList: List<ItemsListDataOrderSet>,
                                      private var servicesItemsList : ArrayList<ServicesData>,
                                      private var pos : Int
) : RecyclerView.Adapter<ServiceDateTimeOfOrderAdapter.ViewHolder>()
{

    // private val itemList = MutableList(itemCount) { "Item ${it + 1}" }
    lateinit var binding: ServiceDateTimeCartListItemBinding

    class ViewHolder(val binding: ServiceDateTimeCartListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ServiceDateTimeCartListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //val cartItem = cartItemsList[position]
        holder.binding.serviceDateAllocated.text = servicesItemsList[position].serviceDate
        holder.binding.serviceTimeAllocated.text = servicesItemsList[position].serviceTime

            holder.binding.editDateTime.visibility = View.GONE




    }

    override fun getItemCount(): Int = servicesItemsList.size
}
