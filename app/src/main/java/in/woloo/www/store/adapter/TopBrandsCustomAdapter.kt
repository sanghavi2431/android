package `in`.woloo.www.store.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import `in`.woloo.www.databinding.TopBrandsListItemBinding
import `in`.woloo.www.store.screens.StoreProductListActivity
import `in`.woloo.www.store.collections_response.CollectionsListData
import `in`.woloo.www.utils.AppConstants

class TopBrandsCustomAdapter (private val context: Activity,
                              private var collectionsList: ArrayList<CollectionsListData>) : RecyclerView.Adapter<TopBrandsCustomAdapter.ViewHolder>() {

    class ViewHolder(val binding: TopBrandsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TopBrandsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val collection = collectionsList[position]

        // Example: Set category name to a TextView
        holder.binding.brandName.text = collection.title // Adjust this based on your XML

        Glide.with(holder.itemView.context)
            .load(collection.metadata?.image) // URL from API
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache optimization
            .into(holder.binding.brandImage)

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, StoreProductListActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true)
            intent.putExtra("COLLECTION_ID" , collection.id)
            intent.putExtra("FROMSCREEN", AppConstants.FROM_TOP_BRANDS)

            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = collectionsList.size
}