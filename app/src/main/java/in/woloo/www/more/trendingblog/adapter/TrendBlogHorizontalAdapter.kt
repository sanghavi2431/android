package `in`.woloo.www.more.trendingblog.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.R

class TrendBlogHorizontalAdapter(
    var context: Context,
    var imagesofTopic: List<Int>,
    var titlesofTopic: List<String>
) : RecyclerView.Adapter<TrendBlogHorizontalAdapter.BlogViewHolder>() {
    var inflater: LayoutInflater
    var item_index = -1

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val view: View = inflater.inflate(R.layout.trend_blog_horizontal_rcy, parent, false)
        return BlogViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(
        holder: BlogViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.imageOfBlog.setImageResource(imagesofTopic[position])
        holder.textOfBlog.text = titlesofTopic[position]
        holder.horizontalCircleRel.setOnClickListener {
            item_index = position
            notifyDataSetChanged()
        }
        if (item_index == position) {
            holder.horizontalCircleRel.setBackgroundResource(R.drawable.light_gray_theme_circle)
            holder.textOfBlog.setTextColor(context.resources.getColor(R.color.black))
            holder.imageOfBlog.setColorFilter(context.resources.getColor(R.color.black))
        } else {
            holder.horizontalCircleRel.setBackgroundResource(R.drawable.light_gray_theme_circle)
            holder.textOfBlog.setTextColor(context.resources.getColor(R.color.black))
            holder.imageOfBlog.setColorFilter(context.resources.getColor(R.color.black))
        }
    }

    override fun getItemCount(): Int {
        return imagesofTopic.size
    }

    inner class BlogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var containRel: LinearLayout
        var textOfBlog: TextView
        var imageOfBlog: ImageView
        var horizontalCircleRel: RelativeLayout

        init {
            context = itemView.context

            /*  viewTextUnderline=itemView.findViewById(R.id.textUnderline);*/containRel =
                itemView.findViewById<LinearLayout>(R.id.trendBlogHorMainRel)
            textOfBlog = itemView.findViewById<TextView>(R.id.blogTextRcyhor)
            imageOfBlog = itemView.findViewById<ImageView>(R.id.imageOfBlog)
            horizontalCircleRel = itemView.findViewById<RelativeLayout>(R.id.rel_blog_horizontal)
        }
    }
}
