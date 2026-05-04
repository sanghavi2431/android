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
import `in`.woloo.www.databinding.PeriodEssentialsListItemBinding
import `in`.woloo.www.store.screens.StoreProductDetailsActivity
import `in`.woloo.www.store.categories_response.CategoriesListData
import `in`.woloo.www.store.screens.StoreProductListActivity
import `in`.woloo.www.utils.AppConstants

class PeriodEssentialsCustomeAdapter (private val context: Activity,   private var categoriesList: ArrayList<CategoriesListData>) : RecyclerView.Adapter<PeriodEssentialsCustomeAdapter.ViewHolder>() {

    lateinit var binding: PeriodEssentialsListItemBinding

    class ViewHolder(val binding: PeriodEssentialsListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = PeriodEssentialsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val category = categoriesList[position]

      //  Logger.i("Aarati Store Period Essentials", "setLiveData ${category.name}")

        if(!category.parent_category_id.isNullOrEmpty()) {
            // Example: Set category name to a TextView
         //   Logger.i("Aarati Store Period Essentials", "setLiveData ${category.name}")
            holder.binding.periodCategoryText.text = category.name // Adjust this based on your XML

            Glide.with(holder.itemView.context)
                .load(category.metadata?.image) // URL from API
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Cache optimization
                .into(holder.binding.periodCategoryImage)
            try {

                if (category.metadata != null) {
                    if (!category.metadata!!.background_color.isNullOrEmpty()) {
                        val colorString = "#${category.metadata!!.background_color}"
                        val color = Color.parseColor(colorString)
                        holder.binding.periodCategoryImage.backgroundTintList =
                            ColorStateList.valueOf(color)
                    }
                }


                holder.binding.root.setOnClickListener {
                    val intent = Intent(context, StoreProductListActivity::class.java)
                    intent.putExtra("IS_SHOW_BACK_BUTTON", true)
                    intent.putExtra("CATEGORY_ID" , category.id)
                    intent.putExtra("FROMSCREEN" , AppConstants.FROM_PERIOD_CATEGORY)
                    context.startActivity(intent)
                }

            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }



    }

    override fun getItemCount(): Int = categoriesList.size
}