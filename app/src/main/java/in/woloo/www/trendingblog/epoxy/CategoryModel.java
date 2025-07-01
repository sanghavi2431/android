package in.woloo.www.trendingblog.epoxy;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;
import com.airbnb.epoxy.EpoxyViewHolder;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import in.woloo.www.R;
import in.woloo.www.base.BaseEpoxyHolder;
import in.woloo.www.period_tracker.model.Log;
import in.woloo.www.trendingblog.model.CategoriesResponse;
import in.woloo.www.trendingblog.model.blog.Category;
import in.woloo.www.utils.ImageUtil;
import in.woloo.www.utils.Logger;

@EpoxyModelClass(layout = R.layout.trend_blog_horizontal_rcy)
public abstract class CategoryModel extends EpoxyModelWithHolder<CategoryModel.Holder> {
    @EpoxyAttribute
    String categoryName;
    @EpoxyAttribute
    String iconUrl;
    @EpoxyAttribute
    int itemPosition;
    @EpoxyAttribute
    int selectedItemPosition;
    @EpoxyAttribute
    BlogController.OnClickBlogViewItems onClickBlogViewItems;
    @EpoxyAttribute
    Category category;

    @Override
    public void bind(@NonNull Holder holder) {
        //ImageUtil.loadImage(holder.ivIcon.getContext(), holder.ivIcon, category.getCategoryIconUrl());
        if (itemPosition > 0) {
            Logger.d("aarati" , category.getCategoryIconUrl() + "  Image Url");
            ImageUtil.loadImage(holder.ivIcon.getContext(), holder.ivIcon, category.getCategoryIconUrl());
            //holder.ivIcon.setImageResource(R.drawable.ic_blog_wellness);
        } else {
            holder.ivIcon.setImageResource(R.drawable.ic_check_box);
        }
        holder.tvCategoryName.setText(categoryName);

        if (selectedItemPosition == itemPosition) {
            holder.horizontalCircleRel.setBackgroundResource(R.drawable.ic_circle_back_img_blog_hor);
            holder.viewTextUnderline.setVisibility(View.VISIBLE);
            holder.tvCategoryName.setTextColor(holder.tvCategoryName.getResources().getColor(R.color.black));
            //holder.ivIcon.setColorFilter(ContextCompat.getColor(holder.ivIcon.getContext(), R.color.black), PorterDuff.Mode.MULTIPLY);
            holder.ivIcon.setAlpha(1.0f);
        } else {
            holder.horizontalCircleRel.setBackgroundResource(R.drawable.circular_background);
            holder.viewTextUnderline.setVisibility(View.GONE);
            holder.tvCategoryName.setTextColor(holder.tvCategoryName.getResources().getColor(R.color.transparent_40));
            //holder.ivIcon.setColorFilter(ContextCompat.getColor(holder.ivIcon.getContext(), R.color.transparent_40), PorterDuff.Mode.MULTIPLY);
            holder.ivIcon.setAlpha(0.6f);
        }
        holder.containRel.setOnClickListener(view -> onClickBlogViewItems.onSelectCategory(itemPosition));
    }

    static class Holder extends BaseEpoxyHolder {
        @BindView(R.id.imageOfBlog)
        AppCompatImageView ivIcon;
        @BindView(R.id.blogTextRcyhor)
        TextView tvCategoryName;
        @BindView(R.id.trendBlogHorMainRel)
        View containRel;
        @BindView(R.id.rel_blog_horizontal)
        RelativeLayout horizontalCircleRel;
        @BindView(R.id.textUnderline)
        View viewTextUnderline;
    }
}
