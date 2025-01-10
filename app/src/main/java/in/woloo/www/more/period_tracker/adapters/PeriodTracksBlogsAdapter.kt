package `in`.woloo.www.more.period_tracker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.trendingblog.model.blog.Blog
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.ImageUtil
import `in`.woloo.www.utils.TimeAgoUtils

class PeriodTracksBlogsAdapter(
    context: Context,
    blogs: List<Blog>,
    periodTrackerBlogsListener: PeriodTrackerBlogsListener
) :
    RecyclerView.Adapter<PeriodTracksBlogsAdapter.BlogVerticaViewlHolder>() {
    var blogs: List<Blog> = ArrayList()
    var context: Context
    var inflater: LayoutInflater
    private var rcyListener: PeriodTrackerBlogsListener

    init {
        this.blogs = blogs
        this.inflater = LayoutInflater.from(context)
        this.context = context
        this.rcyListener = periodTrackerBlogsListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogVerticaViewlHolder {
        val view = inflater.inflate(R.layout.model_blog_item, parent, false)
        return BlogVerticaViewlHolder(view, rcyListener)
    }

    override fun onBindViewHolder(holder: BlogVerticaViewlHolder, position: Int) {
        //holder.blogImgMain.setImageResource(blogImages.get(position));
        val blog = blogs[position]
        holder.tvBlogTitle!!.text = blog.title
        ImageUtil.loadImageBlogs(
            context,
            holder.ivBlogItem!!,
            AppConstants.CHANGED_BLOGS_IMAGE_URL + blog.mainImage
        )

        if (blog.isFavourite!! > 0) {
            holder.ivFavourite!!.setImageResource(R.drawable.ic_heart_blog)
        } else {
            holder.ivFavourite!!.setImageResource(R.drawable.ic_heart_blog_outline)
        }
        if (blog.isLiked!! > 0) {
            holder.ivLike!!.setImageResource(R.drawable.ic_star_blog)
        } else {
            holder.ivLike!!.setImageResource(R.drawable.ic_star_blog_outline)
        }
        if (blog.isBlogRead!! > 0) {
            holder.rlBlogPoints!!.backgroundTintList = ContextCompat.getColorStateList(
                holder.rlBlogPoints!!.context, R.color.gray
            )
        } else {
            holder.rlBlogPoints!!.backgroundTintList = ContextCompat.getColorStateList(
                holder.rlBlogPoints!!.context, R.color.sheildcolor
            )
        }
        holder.ivBlogItem!!.setOnClickListener { view: View? ->
            rcyListener.onClickBlogItem(blog, position)
        }
        holder.ivFavourite!!.setOnClickListener {
            rcyListener.onClickBlogFavourite(blog, position)
        }
        holder.ivLike!!.setOnClickListener {
            rcyListener.onClickBlogLike(blog, position)
        }
        holder.ivShare!!.setOnClickListener {
            rcyListener.onClickBlogShare(blog, position)
        }
        holder.tvBlogUpdatedTime!!.text = TimeAgoUtils.getTimeAgo(CommonUtils.getTimeAgo(blog.updatedAt!!))
    }

    override fun getItemCount(): Int {
        return blogs.size
    }

    inner class BlogVerticaViewlHolder(
        itemView: View,
        periodTrackerBlogsListener: PeriodTrackerBlogsListener
    ) :
        RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.blogImg)
        var ivBlogItem: ImageView? = null

        @JvmField
        @BindView(R.id.heartimg)
        var ivFavourite: ImageView? = null

        @JvmField
        @BindView(R.id.starimg)
        var ivLike: ImageView? = null

        @JvmField
        @BindView(R.id.shareimg)
        var ivShare: ImageView? = null

        @JvmField
        @BindView(R.id.blogText)
        var tvBlogTitle: TextView? = null

        @JvmField
        @BindView(R.id.blogTimeText)
        var tvBlogUpdatedTime: TextView? = null

        @JvmField
        @BindView(R.id.rlBlogPoints)
        var rlBlogPoints: View? = null

        init {
            ButterKnife.bind(this, itemView)
            rcyListener = periodTrackerBlogsListener
        }
    }

    interface PeriodTrackerBlogsListener {
        fun onClickBlogItem(blog: Blog?, position: Int)

        fun onClickBlogFavourite(blog: Blog?, position: Int)

        fun onClickBlogLike(blog: Blog?, position: Int)

        fun onClickBlogShare(blog: Blog?, position: Int)
    }
}
