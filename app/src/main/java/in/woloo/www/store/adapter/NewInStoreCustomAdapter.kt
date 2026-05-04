package `in`.woloo.www.store.adapter

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.databinding.NewInStoreListItemBinding
import `in`.woloo.www.store.categories_response.CategoriesListData
import `in`.woloo.www.store.screens.StoreProductDetailsActivity
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.store.screens.StoreProductListActivity
import `in`.woloo.www.utils.AppConstants

class NewInStoreCustomAdapter (private val context: Activity, private var productList: List<CategoriesListData>) : RecyclerView.Adapter<NewInStoreCustomAdapter.ViewHolder>() {

    lateinit var binding: NewInStoreListItemBinding
    class ViewHolder(val binding: NewInStoreListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = NewInStoreListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        if (context is WolooDashboard)
        {

        }
        else {
            val screenWidth = Resources.getSystem().displayMetrics.widthPixels
            val itemWidth = (screenWidth / 2) - 40  // Subtract margin (8dp on each side)

            val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            layoutParams.width = itemWidth
            layoutParams.setMargins(20, 0, 8, 20) // Left, Top, Right, Bottom margins (8dp each)

            holder.itemView.layoutParams = layoutParams
        }

        try {

            Glide.with(holder.itemView.context)
                .load(product.metadata?.image) // URL from API
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache optimization
                .error(R.drawable.feather_img)
                .into(holder.binding.productImage)
            /*if(product.thumbnail != null) {
                Glide.with(holder.itemView.context)
                    .load(product.thumbnail) // URL from API
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache optimization
                    .into(holder.binding.productImage)
            }
            else if(product.images!!.get(0).url != null)
            {
                Glide.with(holder.itemView.context)
                    .load(product.images!!.get(0).url) // URL from API
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache optimization
                    .error(R.drawable.feather_img)
                    .into(holder.binding.productImage)
            }*/

            holder.binding.root.setOnClickListener {
                val intent = Intent(context, StoreProductListActivity::class.java)
                intent.putExtra("IS_SHOW_BACK_BUTTON", true)
                intent.putExtra("CATEGORY_ID" , product.id)
                intent.putExtra("FROMSCREEN" , AppConstants.FROM_PERIOD_CATEGORY)
                context.startActivity(intent)
            }

        } catch (e: Exception) {

        }
      /*  val productJson = Gson().toJson(productList[position])
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, StoreProductDetailsActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true) // Pass the boolean value if needed
            intent.putExtra("PRODUCT_DETAILS", productJson)
            context.startActivity(intent)
        }*/

    }

    override fun getItemCount(): Int = productList.size
}