package `in`.woloo.www.application_kotlin.adapters.login

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import `in`.woloo.www.R
import `in`.woloo.www.more.trendingblog.model.CategoriesResponse

class InterestedTopicAdapter(
    var context: Context,
    private val categories: List<CategoriesResponse.Category>
) :
    RecyclerView.Adapter<InterestedTopicAdapter.ViewHolder>() {
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
        if (context is OnItemCheckListener) {
            itemCheckListener = context as OnItemCheckListener
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.recycler_gridlayout_design, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        //        ImageUtil.loadImage(holder.topicImage.getContext(), holder.topicImage, category.getCategoryIconUrl());
        Log.d("url is" , category.categoryIconUrl!!);
        Glide.with(context)
            .load(category.categoryIconUrl)
            .error(context.getDrawable(R.drawable.ic__01_hormones))
            .into(holder.topicImage)
        holder.textTitle.text = category.categoryName
        holder.parentView.setOnClickListener { view: View? ->
            if (itemCheckListener != null) {
                itemCheckListener!!.onItemClick(position)
            }
            category.isSelected = !category.isSelected
            if (category.isSelected) {
                holder.cardRelative.setBackgroundResource(R.drawable.yellow_border_white_rect)
            } else {
                holder.cardRelative.setBackgroundResource(R.drawable.new_button_background)
            }
        }
        if (category.isSelected) {
            holder.cardRelative.setBackgroundResource(R.drawable.yellow_border_white_rect)
        } else {
            holder.cardRelative.setBackgroundResource(R.drawable.new_button_background)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class ViewHolder(var parentView: View) : RecyclerView.ViewHolder(
        parentView
    ) {
        var topicImage: ImageView
        var textTitle: TextView
        var cardRelative: LinearLayout

        init {
            textTitle = itemView.findViewById(R.id.itemNameText)
            topicImage = itemView.findViewById(R.id.checkboxItem)
            cardRelative = itemView.findViewById(R.id.relative_card)
        }
    }

    interface OnItemCheckListener {
        fun onItemClick(position: Int)
    }

    companion object {
        private var itemCheckListener: OnItemCheckListener? = null
    }
}
