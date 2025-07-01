package in.woloo.www.trendingblog.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import in.woloo.www.R;
import in.woloo.www.trendingblog.BlogDetailsActivity;

public class TrendBlogVerticalAdapter extends RecyclerView.Adapter<TrendBlogVerticalAdapter.BlogVerticaViewlHolder> {
    List<Integer> blogImages;
    Context context;
    LayoutInflater inflater;
    private TrendBlogVerticalAdapter.RecyclerViewClickListener rcyListener;

    public TrendBlogVerticalAdapter(Context context,List<Integer> blogImages,RecyclerViewClickListener recyclerViewClickListener)
    {
        this.blogImages=blogImages;
        this.inflater=LayoutInflater.from(context);
        this.context=context;
        this.rcyListener=recyclerViewClickListener;
    }

    @NonNull
    @NotNull
    @Override
    public BlogVerticaViewlHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.trend_blog_vertical_rcy_design,parent,false);
        return new TrendBlogVerticalAdapter.BlogVerticaViewlHolder(view,rcyListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BlogVerticaViewlHolder holder, int position) {
        holder.blogImgMain.setImageResource(blogImages.get(position));
        context=holder.itemView.getContext();
        holder.blogImgMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Clicked ps"+position);
                Intent intentBlogDetail=new Intent(context, BlogDetailsActivity.class);
                intentBlogDetail.putExtra("Clicked_blogImage_Position",blogImages.get(position));
                context.startActivity(intentBlogDetail);
            }
        });

    }

    @Override
    public int getItemCount() {
        return blogImages.size();
    }

    public class BlogVerticaViewlHolder extends RecyclerView.ViewHolder  {
        ImageView blogImgMain;
        RelativeLayout mainRelativeContainer;
        RelativeLayout imgRel;
        public BlogVerticaViewlHolder(@NonNull @NotNull View itemView,final RecyclerViewClickListener recyclerViewClickListener) {
            super(itemView);
           rcyListener=recyclerViewClickListener;
           imgRel=itemView.findViewById(R.id.reltopImg);
           blogImgMain=itemView.findViewById(R.id.blogImg);
           mainRelativeContainer=itemView.findViewById(R.id.trendBlogVerMainRel);

        }

    }

    public interface RecyclerViewClickListener {

    }
}
