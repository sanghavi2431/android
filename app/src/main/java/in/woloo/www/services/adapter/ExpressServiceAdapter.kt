package `in`.woloo.www.services.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import `in`.woloo.www.databinding.ExpressServicesListItemBinding
import `in`.woloo.www.services.ExpressServiceItem
import `in`.woloo.www.store.product_response.ProductListData


class ExpressServiceAdapter(
    private val items: List<ProductListData>
) : RecyclerView.Adapter<ExpressServiceAdapter.ServiceViewHolder>() {

    inner class ServiceViewHolder(val binding: ExpressServicesListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ExpressServicesListItemBinding.inflate(layoutInflater, parent, false)
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val item = items[position]
        holder.binding.headerText.text = item.metadata!!.serviceTitle

        Glide.with(holder.itemView)
            .load(item.metadata!!.serviceImage)
            .into(holder.binding.serviceExpressImage)

        Glide.with(holder.itemView)
            .load(item.metadata!!.backgroundImage)
            .into(holder.binding.productImage)

    }

    override fun getItemCount(): Int = items.size
}
