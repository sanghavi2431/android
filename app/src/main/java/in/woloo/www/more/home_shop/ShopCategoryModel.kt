package `in`.woloo.www.more.home_shop

import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Optional
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.bumptech.glide.Glide
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.base.BaseEpoxyHolder
import `in`.woloo.www.more.trendingblog.model.blog.Category
import `in`.woloo.www.utils.CircleImageView
import javax.annotation.Nullable


@EpoxyModelClass(layout = R.layout.category_shop_list_item)
abstract class ShopCategoryModel  : EpoxyModelWithHolder<ShopCategoryModel.Holder>() {
    @EpoxyAttribute
    var categoryName: String? = null

    @EpoxyAttribute
    var iconUrl: String? = null

    @EpoxyAttribute
    var itemPosition = 0

    @EpoxyAttribute
    var selectedItemPosition = 0

    @EpoxyAttribute
    var onClickBlogViewItems: ShopBlogsController.OnClickShopBlogViewItems? = null

    @EpoxyAttribute
    var category: Category? = null
    override fun bind(holder: ShopCategoryModel.Holder) {

        //ImageUtil.loadImage(holder.ivIcon.getContext(), holder.ivIcon, category.getCategoryIconUrl());
        val drawableArray = intArrayOf(
            R.drawable.category_bg_one,
            R.drawable.category_bg_two,
            R.drawable.category_bg_three,
            R.drawable.category_bg_four,
            R.drawable.category_bg_five
        )
        val colorIndex = itemPosition % 5
        holder.horizontalCircleRel?.setBackgroundResource(drawableArray[colorIndex])
        if (itemPosition > 0) {
            //   ImageUtil.loadImage(holder.ivIcon.getContext(), holder.ivIcon, category.getCategoryIconUrl());
            //holder.ivIcon.setImageResource(R.drawable.ic_blog_wellness);
            Log.d("url is", category!!.categoryIconUrl!!)
            Glide.with(holder.ivIcon!!.context).load(category!!.categoryIconUrl).into(holder.ivIcon!!)
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
        holder.containRel?.setOnClickListener { view: View? ->
            onClickBlogViewItems!!.onSelectCategory(
                itemPosition
            )
        }
    }

    class Holder : BaseEpoxyHolder() {


        lateinit var ivIcon: CircleImageView



        lateinit var tvCategoryName: TextView

        lateinit var containRel: View

        lateinit var horizontalCircleRel: RelativeLayout

        override fun bindView(itemView: View) {
            super.bindView(itemView)
            ivIcon = itemView.findViewById(R.id.imageOfBlog)
            tvCategoryName = itemView.findViewById(R.id.blogTextRcyhor)
            containRel = itemView.findViewById(R.id.trendBlogHorMainRel)
            horizontalCircleRel = itemView.findViewById(R.id.rel_blog_horizontal)
        }


    }
}
