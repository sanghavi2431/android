package in.woloo.www.interestedtopic.adapter;

import in.woloo.www.R;
import in.woloo.www.trendingblog.model.CategoriesResponse;
import in.woloo.www.utils.ImageUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class InterestedTopicAdapter extends RecyclerView.Adapter<InterestedTopicAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<CategoriesResponse.Category> categories;
    private static OnItemCheckListener itemCheckListener;
    Context context;

    public InterestedTopicAdapter(Context context, List<CategoriesResponse.Category> categories) {
        this.inflater = LayoutInflater.from(context);
        this.categories = categories;
        this.context = context;
        if (context instanceof OnItemCheckListener) {
            itemCheckListener = (OnItemCheckListener) context;
        }
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_gridlayout_design, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull InterestedTopicAdapter.ViewHolder holder, int position) {
        CategoriesResponse.Category category = categories.get(position);
//        ImageUtil.loadImage(holder.topicImage.getContext(), holder.topicImage, category.getCategoryIconUrl());
        Glide.with(context)
                .load(category.getCategoryIconUrl())
                .error(context.getDrawable(R.drawable.ic__01_hormones))
                .into(holder.topicImage);
        holder.textTitle.setText(category.getCategoryName());
        holder.parentView.setOnClickListener(view -> {
            if (itemCheckListener != null) {
                itemCheckListener.onItemClick(position);
            }
            category.setSelected(!category.isSelected());
            if (category.isSelected()) {
                holder.imageRelative.setBackgroundResource(R.drawable.circular_background_change);
            } else {
                holder.imageRelative.setBackgroundResource(R.drawable.circular_background);
            }
        });
        if (category.isSelected()) {
            holder.imageRelative.setBackgroundResource(R.drawable.circular_background_change);
        } else {
            holder.imageRelative.setBackgroundResource(R.drawable.circular_background);
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView topicImage;
        TextView textTitle;
        RelativeLayout imageRelative;
        View parentView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            parentView = itemView;
            textTitle = itemView.findViewById(R.id.itemNameText);
            topicImage = itemView.findViewById(R.id.checkboxItem);
            imageRelative = itemView.findViewById(R.id.gridDesignRel1);
        }
    }

    public interface OnItemCheckListener {
        void onItemClick(int position);
    }
}
