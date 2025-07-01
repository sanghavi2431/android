package in.woloo.www.period_tracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.trendingblog.model.blog.Blog;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.ImageUtil;
import in.woloo.www.utils.TimeAgoUtils;

public class PeriodTracksBlogsAdapter extends RecyclerView.Adapter<PeriodTracksBlogsAdapter.BlogVerticaViewlHolder> {
    List<Blog>  blogs = new ArrayList<>();
    Context context;
    LayoutInflater inflater;
    private PeriodTrackerBlogsListener rcyListener;

    public PeriodTracksBlogsAdapter(Context context, List<Blog> blogs, PeriodTrackerBlogsListener periodTrackerBlogsListener) {
        this.blogs = blogs;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.rcyListener = periodTrackerBlogsListener;
    }

    @NonNull
    @NotNull
    @Override
    public PeriodTracksBlogsAdapter.BlogVerticaViewlHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.model_blog_item, parent, false);
        return new PeriodTracksBlogsAdapter.BlogVerticaViewlHolder(view, rcyListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BlogVerticaViewlHolder holder, int position) {
        //holder.blogImgMain.setImageResource(blogImages.get(position));
        Blog blog = blogs.get(position);
        holder.tvBlogTitle.setText(blog.getTitle());
        ImageUtil.loadImageBlogs(context, holder.ivBlogItem, AppConstants.CHANGED_BLOGS_IMAGE_URL +blog.getMainImage());

        if (blog.getIsFavourite() > 0) {
            holder.ivFavourite.setImageResource(R.drawable.ic_heart_blog);
        } else {
            holder.ivFavourite.setImageResource(R.drawable.ic_heart_blog_outline);
        }
        if (blog.getIsLiked() > 0) {
            holder.ivLike.setImageResource(R.drawable.ic_star_blog);
        } else {
            holder.ivLike.setImageResource(R.drawable.ic_star_blog_outline);
        }
        if (blog.getIsBlogRead() > 0) {
            holder.rlBlogPoints.setBackgroundTintList(ContextCompat.getColorStateList(holder.rlBlogPoints.getContext(), R.color.gray));
        } else {
            holder.rlBlogPoints.setBackgroundTintList(ContextCompat.getColorStateList(holder.rlBlogPoints.getContext(), R.color.sheildcolor));
        }
        holder.ivBlogItem.setOnClickListener(view -> {
            rcyListener.onClickBlogItem(blog, position);
        });
        holder.ivFavourite.setOnClickListener(view -> {
            rcyListener.onClickBlogFavourite(blog, position);
        });
        holder.ivLike.setOnClickListener(view -> {
            rcyListener.onClickBlogLike(blog, position);
        });
        holder.ivShare.setOnClickListener(view -> {
            rcyListener.onClickBlogShare(blog, position);
        });
        holder.tvBlogUpdatedTime.setText(TimeAgoUtils.getTimeAgo(CommonUtils.getTimeAgo(blog.getUpdatedAt())));
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }

    public class BlogVerticaViewlHolder extends RecyclerView.ViewHolder {
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

        public BlogVerticaViewlHolder(@NonNull @NotNull View itemView, final PeriodTrackerBlogsListener periodTrackerBlogsListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            rcyListener = periodTrackerBlogsListener;

        }

    }

    public interface PeriodTrackerBlogsListener {

        void onClickBlogItem(Blog blog, int position);

        void onClickBlogFavourite(Blog blog, int position);

        void onClickBlogLike(Blog blog, int position);

        void onClickBlogShare(Blog blog, int position);
    }
}
