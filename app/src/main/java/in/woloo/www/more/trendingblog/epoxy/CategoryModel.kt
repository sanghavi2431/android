package `in`.woloo.www.more.trendingblog.epoxy

import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.base.BaseEpoxyHolder
import `in`.woloo.www.more.trendingblog.epoxy.BlogController.OnClickBlogViewItems
import `in`.woloo.www.more.trendingblog.model.blog.Category
import `in`.woloo.www.utils.CircleImageView

@EpoxyModelClass(layout = R.layout.trend_blog_horizontal_rcy)
abstract class CategoryModel : EpoxyModelWithHolder<CategoryModel.Holder>() {
    @JvmField
    @EpoxyAttribute
    var categoryName: String? = null

    @JvmField
    @EpoxyAttribute
    var iconUrl: String? = null

    @JvmField
    @EpoxyAttribute
    var itemPosition = 0

    @JvmField
    @EpoxyAttribute
    var selectedItemPosition = 0

    @JvmField
    @EpoxyAttribute
    var onClickBlogViewItems: OnClickBlogViewItems? = null

    @JvmField
    @EpoxyAttribute
    var category: Category? = null

    @JvmField
    @EpoxyAttribute
    var background: Category? = null
    override fun bind(holder: Holder) {
        //ImageUtil.loadImage(holder.ivIcon.getContext(), holder.ivIcon, category.getCategoryIconUrl());
        val drawableArray = intArrayOf(
            R.drawable.category_gray_circle_bg,
            R.drawable.category_gray_circle_bg,
            R.drawable.category_gray_circle_bg,
            R.drawable.category_gray_circle_bg,
            R.drawable.category_gray_circle_bg
        )
        val colorIndex = itemPosition % 5
        holder.horizontalCircleRel!!.setBackgroundResource(drawableArray[colorIndex])
        if (itemPosition > 0) {
            //   ImageUtil.loadImage(holder.ivIcon.getContext(), holder.ivIcon, category.getCategoryIconUrl());
            //holder.ivIcon.setImageResource(R.drawable.ic_blog_wellness);
            Log.d("url is", category!!.categoryIconUrl!!)
            Glide.with(holder.ivIcon!!.context).load(category!!.categoryIconUrl)
                .into(holder.ivIcon!!)
        } else {
            holder.ivIcon!!.setImageResource(R.drawable.ic_check_box)
        }
        holder.tvCategoryName!!.text = categoryName
        if (selectedItemPosition == itemPosition) {
            // holder.horizontalCircleRel.setBackgroundResource(R.drawable.light_gray_theme_circle);
            holder.tvCategoryName!!.setTextColor(holder.tvCategoryName!!.resources.getColor(R.color.black))
            //  holder.ivIcon.setColorFilter(ContextCompat.getColor(holder.ivIcon.getContext(), R.color.black), PorterDuff.Mode.MULTIPLY);
            // holder.ivIcon.setAlpha(1.0f);
        } else {
            //  holder.horizontalCircleRel.setBackgroundResource(R.drawable.light_gray_theme_circle);
            holder.tvCategoryName!!.setTextColor(holder.tvCategoryName!!.resources.getColor(R.color.black))
            //holder.ivIcon.setColorFilter(ContextCompat.getColor(holder.ivIcon.getContext(), R.color.transparent_40), PorterDuff.Mode.MULTIPLY);
            // holder.ivIcon.setAlpha(0.6f);
        }
        holder.containRel!!.setOnClickListener { view: View? ->
            onClickBlogViewItems!!.onSelectCategory(
                itemPosition
            )
        }
    }

    class Holder : BaseEpoxyHolder() {
        @JvmField
        @BindView(R.id.imageOfBlog)
        var ivIcon: CircleImageView? = null

        @JvmField
        @BindView(R.id.blogTextRcyhor)
        var tvCategoryName: TextView? = null

        @JvmField
        @BindView(R.id.trendBlogHorMainRel)
        var containRel: View? = null

        @JvmField
        @BindView(R.id.rel_blog_horizontal)
        var horizontalCircleRel: RelativeLayout? = null
    }
}
