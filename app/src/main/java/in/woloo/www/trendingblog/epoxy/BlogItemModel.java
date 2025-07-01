package in.woloo.www.trendingblog.epoxy;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;

import butterknife.BindView;
import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.base.BaseEpoxyHolder;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.trendingblog.model.BlogsResponse;
import in.woloo.www.trendingblog.model.blog.Blog;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.ImageUtil;
import in.woloo.www.utils.TimeAgoUtils;

@EpoxyModelClass(layout = R.layout.model_blog_item)
public abstract class BlogItemModel extends EpoxyModelWithHolder<BlogItemModel.Holder> {
    @EpoxyAttribute
    String imageUrl;
    @EpoxyAttribute
    Blog blog;
    @EpoxyAttribute
    int isLiked;
    @EpoxyAttribute
    int isFavourite;
    @EpoxyAttribute
    int itemPosition;
    @EpoxyAttribute
    BlogController.OnClickBlogViewItems onClickBlogViewItems;
    @EpoxyAttribute
    int isBlogRead;

    @Override
    public void bind(@NonNull Holder holder) {
        holder.tvBlogTitle.setText(blog.getTitle());
        ImageUtil.loadImageBlogs(holder.ivBlogItem.getContext(), holder.ivBlogItem, AppConstants.CHANGED_BLOGS_IMAGE_URL + blog.getMainImage());
        if (isFavourite > 0) {
            holder.ivFavourite.setImageResource(R.drawable.ic_heart_blog);
        } else {
            holder.ivFavourite.setImageResource(R.drawable.ic_heart_blog_outline);
        }
        if (isLiked > 0) {
            holder.ivLike.setImageResource(R.drawable.ic_star_blog);
        } else {
            holder.ivLike.setImageResource(R.drawable.ic_star_blog_outline);
        }
        if (isBlogRead > 0) {
            holder.rlBlogPoints.setBackgroundTintList(ContextCompat.getColorStateList(holder.rlBlogPoints.getContext(), R.color.gray));
        } else {
            holder.rlBlogPoints.setBackgroundTintList(ContextCompat.getColorStateList(holder.rlBlogPoints.getContext(), R.color.sheildcolor));
        }
        holder.ivBlogItem.setOnClickListener(view -> {
            onClickBlogViewItems.onClickBlogItem(blog, itemPosition);
        });
        holder.ivFavourite.setOnClickListener(view -> {
            onClickBlogViewItems.onClickBlogFavourite(blog, itemPosition);
        });
        holder.ivLike.setOnClickListener(view -> {
            onClickBlogViewItems.onClickBlogLike(blog, itemPosition);
        });
        holder.ivShare.setOnClickListener(view -> {
            onClickBlogViewItems.onClickBlogShare(blog, itemPosition);
        });
        holder.tvBlogUpdatedTime.setText(TimeAgoUtils.getTimeAgo(CommonUtils.getTimeAgo(blog.getUpdatedAt())));
    }

    static class Holder extends BaseEpoxyHolder {
        @BindView(R.id.blogImg)
        ImageView ivBlogItem;
        @BindView(R.id.heartimg)
        ImageView ivFavourite;
        @BindView(R.id.starimg)
        ImageView ivLike;
        @BindView(R.id.shareimg)
        ImageView ivShare;
        @BindView(R.id.blogText)
        TextView tvBlogTitle;
        @BindView(R.id.blogTimeText)
        TextView tvBlogUpdatedTime;
        @BindView(R.id.rlBlogPoints)
        View rlBlogPoints;
    }
}