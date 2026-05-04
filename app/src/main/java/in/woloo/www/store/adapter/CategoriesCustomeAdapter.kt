package `in`.woloo.www.store.adapter

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import `in`.woloo.www.R
import `in`.woloo.www.databinding.CategoriesListItemBinding
import `in`.woloo.www.store.screens.StoreProductListActivity
import `in`.woloo.www.store.categories_response.CategoriesListData
import `in`.woloo.www.utils.AppConstants

class CategoriesCustomeAdapter(
    private val context: Activity,
    private var categoriesList: ArrayList<CategoriesListData> // Accepting a list of categories
) : RecyclerView.Adapter<CategoriesCustomeAdapter.ViewHolder>() {

    class ViewHolder(val binding: CategoriesListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoriesListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoriesList[position]

        val layoutParams = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams

        if (position == 0) {
            layoutParams.setMargins(
                context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._13sdp), // left
                context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._6sdp),
                context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._6sdp),
                0           // bottom
            )
        } else {
            layoutParams.setMargins(context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._6sdp),
                context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._6sdp),
                context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._6sdp),
                context.resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._6sdp))
        }

        holder.itemView.layoutParams = layoutParams

        if(category.parent_category_id == null) {
            // Example: Set category name to a TextView
            holder.binding.categoryName.text = category.name // Adjust this based on your XML

            Glide.with(holder.itemView.context)
                .load(category.metadata?.image) // URL from API
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache optimization
                .into(holder.binding.categoryImage)
            try {

                if (category.metadata != null) {
                    if (!category.metadata!!.background_color.isNullOrEmpty()) {
                        val colorString = "#${category.metadata!!.background_color}"
                        val color = Color.parseColor(colorString)
                        holder.binding.categoryImage.backgroundTintList =
                            ColorStateList.valueOf(color)
                    }
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }
        holder.binding.root.setOnClickListener {
            val intent = Intent(context, StoreProductListActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true)
            intent.putExtra("CATEGORY_ID" , category.id)
            intent.putExtra("FROMSCREEN" , AppConstants.FROM_CATEGORIES)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = categoriesList.size


}
