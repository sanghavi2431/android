package `in`.woloo.www.services.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import `in`.woloo.www.databinding.ServicesCategoriesListAdapterBinding
import `in`.woloo.www.services.ServiceViewModel
import `in`.woloo.www.services.screens.ServicesProductsListingActivity
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.categories_response.CategoriesListData
import `in`.woloo.www.store.screens.StoreProductListActivity
import `in`.woloo.www.utils.AppConstants


class ServicesCategoriesListAdapter (private val context: Activity,
                                      private var categoriesList: ArrayList<CategoriesListData>,
                                     private val serviceViewModel: ServiceViewModel,
                                     private val listener: OnCategoryItemClickListener
) : RecyclerView.Adapter<ServicesCategoriesListAdapter.ViewHolder>() {

    class ViewHolder(val binding: ServicesCategoriesListAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ServicesCategoriesListAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoriesList[position]

        // Example: Set category name to a TextView
        holder.binding.brandName.text = category.name // Adjust this based on your XML

        Glide.with(holder.itemView.context)
            .load(category.metadata?.image) // URL from API
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache optimization
            .into(holder.binding.serviceCategoryImage)

        holder.binding.root.setOnClickListener {
            serviceViewModel.getCategoryWiseProductList(category.id.toString())

            listener.onCategoryItemClicked(category.id.toString() , category.metadata?.listingVideoUrl!!.toString())

        }

    }

    override fun getItemCount(): Int = categoriesList.size



}

interface OnCategoryItemClickListener {
    fun onCategoryItemClicked(data: String , url: String)
}