package in.woloo.www.trendingblog.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.woloo.www.R;
import in.woloo.www.base.BaseActivity;
import in.woloo.www.dailylogscreen.adapter.DailyLogGroupAdapter;
import in.woloo.www.dailylogscreen.adapter.HorizantalItemDailyLogAdapter;
import in.woloo.www.dailylogscreen.models.DailyLogGroupTitle;
import in.woloo.www.dailylogscreen.models.DailyLogSubTitle;

import static androidx.core.content.ContextCompat.getColor;
import static androidx.core.content.ContextCompat.getColorStateList;

public class TrendBlogHorizontalAdapter extends RecyclerView.Adapter<TrendBlogHorizontalAdapter.BlogViewHolder> {
    List<Integer> imagesofTopic;
    List<String> titlesofTopic;
    Context context;
    LayoutInflater inflater;
    int item_index=-1;

    public TrendBlogHorizontalAdapter(Context context,List<Integer> imagesofTopic,List<String> titlesofTopic)
    {
        this.imagesofTopic=imagesofTopic;
        this.titlesofTopic=titlesofTopic;
        this.inflater=LayoutInflater.from(context);
        this.context=context;
    }

    @NonNull
    @NotNull
    @Override
    public BlogViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.trend_blog_horizontal_rcy,parent,false);
        return new TrendBlogHorizontalAdapter.BlogViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull @NotNull BlogViewHolder holder, int position) {
        holder.imageOfBlog.setImageResource(imagesofTopic.get(position));
        holder.textOfBlog.setText(titlesofTopic.get(position));
        holder.horizontalCircleRel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_index=position;
                notifyDataSetChanged();
            }
        });

        if(item_index==position){
            holder.horizontalCircleRel.setBackgroundResource(R.drawable.ic_circle_back_img_blog_hor);
            holder.viewTextUnderline.setVisibility(View.VISIBLE);
            holder.textOfBlog.setTextColor(context.getResources().getColor(R.color.light_gray));
            holder.imageOfBlog.setColorFilter(context.getResources().getColor(R.color.light_gray));
        }
        else
        {
            holder.horizontalCircleRel.setBackgroundResource(R.drawable.circular_background);
            holder.viewTextUnderline.setVisibility(View.GONE);
            holder.textOfBlog.setTextColor(context.getResources().getColor(R.color.light_grey));
            holder.imageOfBlog.setColorFilter(context.getResources().getColor(R.color.light_grey));
        }
    }

    @Override
    public int getItemCount() {
        return imagesofTopic.size();
    }

    public class BlogViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout containRel;
        TextView textOfBlog;
        ImageView imageOfBlog;
        RelativeLayout horizontalCircleRel;
        View viewTextUnderline;

        public BlogViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            context=itemView.getContext();

            viewTextUnderline=itemView.findViewById(R.id.textUnderline);
            containRel=itemView.findViewById(R.id.trendBlogHorMainRel);
            textOfBlog=itemView.findViewById(R.id.blogTextRcyhor);
            imageOfBlog=itemView.findViewById(R.id.imageOfBlog);
            horizontalCircleRel=itemView.findViewById(R.id.rel_blog_horizontal);
        }
    }
}
