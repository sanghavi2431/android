package `in`.woloo.www.more.trendingblog.epoxy

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.base.BaseEpoxyHolder
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard.Companion.blogType
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.trendingblog.model.blog.Blog
import `in`.woloo.www.utils.AppConstants
import  `in`.woloo.www.more.trendingblog.epoxy.BlogController.OnClickBlogViewItems
import `in`.woloo.www.utils.TimeAgoUtils

@EpoxyModelClass(layout = R.layout.model_blog_item)
abstract class BlogItemModel : EpoxyModelWithHolder<BlogItemModel.Holder>() {
    @JvmField
    @EpoxyAttribute
    var imageUrl: String? = null

    @JvmField
    @EpoxyAttribute
    var blog: Blog? = null

    @JvmField
    @EpoxyAttribute
    var isLiked = 0

    @JvmField
    @EpoxyAttribute
    var isFavourite = 0

    @JvmField
    @EpoxyAttribute
    var itemPosition = 0

    @JvmField
    @EpoxyAttribute
    var onClickBlogViewItems: OnClickBlogViewItems? = null

    @JvmField
    @EpoxyAttribute
    var isBlogRead = 0
    override fun bind(holder: Holder) {
        val description = blog!!.title

        // Show truncated description (1.5 lines)
        val visibleDescription =
            if (description!!.length > 100) description.substring(0, 100) + "..." else description
        val seeMoreText = "   SeeMore..."

        // Create SpannableString
        val spannableString = SpannableString(visibleDescription + seeMoreText)

        // Make the "SeeMore..." part bold
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            visibleDescription.length,
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Set the text in TextView
        holder.tvBlogTitle!!.text = spannableString

        // Make "See More..." clickable
        holder.tvBlogTitle!!.movementMethod = LinkMovementMethod.getInstance()
        if (WolooDashboard.Companion.blogType.matches("0".toRegex())) {
            holder.tvShopNow!!.visibility = View.VISIBLE
        } else {
            holder.tvShopNow!!.visibility = View.GONE
        }
        holder.tvShopNow!!.setOnClickListener {
            var shopLinkId: String? = blog!!.isMapToShop
            if (!shopLinkId!!.isEmpty() || shopLinkId != null) {
                if (blog!!.blogType == "0") {
                    val lastUnderscoreIndex = shopLinkId.lastIndexOf("_")
                    if (shopLinkId.contains("cat")) {
                        if (lastUnderscoreIndex != -1) {
                            // Extract everything after the last underscore
                            shopLinkId = shopLinkId.substring(lastUnderscoreIndex + 1)
                            Log.d("category is ", shopLinkId)
                            onClickBlogViewItems!!.onClickShopNow(
                                shopLinkId,
                                blog!!.categories,
                                "category"
                            )
                        }
                    } else if (shopLinkId.contains("prod")) {
                        if (lastUnderscoreIndex != -1) {
                            // Extract everything after the last underscore
                            shopLinkId = shopLinkId.substring(lastUnderscoreIndex + 1)
                            Log.d("product is ", shopLinkId)
                            onClickBlogViewItems!!.onClickShowNowProduct(shopLinkId)
                        }
                    }
                }
            }
        }

        // Optionally, handle the "See More..." click event
        holder.tvBlogTitle!!.setOnClickListener { v: View? ->
            if (holder.tvBlogTitle!!.text.toString().contains("SeeMore...")) {
                // Show the full description or handle it accordingly
                val fullDescription = description // Use the original full description
            }
        }


        //  ImageUtil.loadImageBlogs(holder.ivBlogItem.getContext(), holder.ivBlogItem, BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_BLOG_IMAGE + blog.getMainImage());
        Glide.with(holder.ivBlogItem!!.context)
            .load(BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_BLOG_IMAGE + blog!!.mainImage)
            .error(R.drawable.rectangle_shape)


        /* if (isFavourite > 0) {
            holder.ivFavourite.setImageResource(R.drawable.ic_heart_blog);
        } else {
            holder.ivFavourite.setImageResource(R.drawable.ic_heart_blog_outline);
        }*/if (isLiked > 0) {
            holder.ivLike!!.setImageResource(R.drawable.ic_star_blog)
        } else {
            holder.ivLike!!.setImageResource(R.drawable.ic_star_blog_outline)
        }
        /* if (isBlogRead > 0) {
            holder.rlBlogPoints.setBackgroundTintList(ContextCompat.getColorStateList(holder.rlBlogPoints.getContext(), R.color.gray));
        } else {
            holder.rlBlogPoints.setBackgroundTintList(ContextCompat.getColorStateList(holder.rlBlogPoints.getContext(), R.color.sheildcolor));
        }*/holder.ivBlogItem!!.setOnClickListener { view: View? ->
            onClickBlogViewItems!!.onClickBlogItem(
                blog,
                itemPosition
            )
        }
        holder.ivFavourite!!.setOnClickListener { view: View? ->
            onClickBlogViewItems!!.onClickBlogFavourite(
                blog,
                itemPosition
            )
        }
        holder.ivLike!!.setOnClickListener { view: View? ->
            onClickBlogViewItems!!.onClickBlogLike(
                blog,
                itemPosition
            )
        }
        holder.ivShare!!.setOnClickListener { view: View? ->
            onClickBlogViewItems!!.onClickBlogShare(
                blog,
                itemPosition
            )
        }
        holder.tvBlogUpdatedTime!!.setText(
            TimeAgoUtils.getTimeAgo(
                CommonUtils.getTimeAgo(
                    blog!!.updatedAt!!
                )
            )
        )
    }

    class Holder : BaseEpoxyHolder() {
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

        @JvmField
        @BindView(R.id.shop_now)
        var tvShopNow: TextView? = null
    }
}