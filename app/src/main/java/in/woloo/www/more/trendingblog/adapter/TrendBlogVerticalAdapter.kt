package `in`.woloo.www.more.trendingblog.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.R
import `in`.woloo.www.more.trendingblog.BlogDetailsActivity

class TrendBlogVerticalAdapter(
    var context: Context,
    var blogImages: List<Int>,
    private var rcyListener: RecyclerViewClickListener
) : RecyclerView.Adapter<TrendBlogVerticalAdapter.BlogVerticaViewlHolder>() {
    var inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogVerticaViewlHolder {
        val view: View = inflater.inflate(R.layout.trend_blog_vertical_rcy_design, parent, false)
        return BlogVerticaViewlHolder(view, rcyListener)
    }

    override fun onBindViewHolder(
        holder: BlogVerticaViewlHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.blogImgMain.setImageResource(blogImages[position])
        context = holder.itemView.context
        holder.blogImgMain.setOnClickListener {
            println("Clicked ps$position")
            val intentBlogDetail = Intent(context, BlogDetailsActivity::class.java)
            intentBlogDetail.putExtra("Clicked_blogImage_Position", blogImages[position])
            context.startActivity(intentBlogDetail)
        }
    }

    override fun getItemCount(): Int {
        return blogImages.size
    }

    inner class BlogVerticaViewlHolder(
        itemView: View,
        recyclerViewClickListener: RecyclerViewClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        var blogImgMain: ImageView
        var mainRelativeContainer: RelativeLayout
        var imgRel: RelativeLayout

        init {
            rcyListener = recyclerViewClickListener
            imgRel = itemView.findViewById<RelativeLayout>(R.id.reltopImg)
            blogImgMain = itemView.findViewById<ImageView>(R.id.blogImg)
            mainRelativeContainer = itemView.findViewById<RelativeLayout>(R.id.trendBlogVerMainRel)
        }
    }

    interface RecyclerViewClickListener
}
