package `in`.woloo.www.services.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.databinding.ServiceDateTimeCartListItemBinding
import `in`.woloo.www.databinding.ServiceDeliveryListItemBinding
import `in`.woloo.www.services.ServiceViewModel
import `in`.woloo.www.services.screens.DateTimeBottomSheetFragment
import `in`.woloo.www.store.cart_request_response.CartLineItems
import `in`.woloo.www.store.cart_request_response.ServicesData

class ServiceDateTimeOfCartAdapter  (private val context: FragmentActivity,
                                     private var cartItemsList: ArrayList<CartLineItems>,
                                     private var servicesItemsList : ArrayList<ServicesData>,
                                     private val servicesViewModel: ServiceViewModel,
                                        private var pos : Int,
                                     private val isFromBottomSheet: Boolean
) : RecyclerView.Adapter<ServiceDateTimeOfCartAdapter.ViewHolder>()
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

        if (isFromBottomSheet == true) {
            holder.binding.editDateTime.visibility = View.GONE



        } else {

            holder.binding.editDateTime.visibility = View.VISIBLE


        }

        holder.binding.editDateTime.setOnClickListener {

            val bottomSheetFragment = DateTimeBottomSheetFragment.newInstance(cartItemsList[pos] , "CART_SCREEN_EDIT" , servicesItemsList[position].serviceDate.toString() , servicesItemsList[position].serviceTime.toString())
            bottomSheetFragment.show(context.supportFragmentManager, bottomSheetFragment.tag)

        }

    }

    override fun getItemCount(): Int = servicesItemsList.size
}
