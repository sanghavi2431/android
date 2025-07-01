package in.woloo.www.home_details.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.utils.ImageUtil;

public class NearByWolooImageAdapter  extends RecyclerView.Adapter<NearByWolooImageAdapter.ViewHolder> {

    private Activity activity;
    private int width = 0, height = 0;
    List<String> imageList;

    public NearByWolooImageAdapter(Activity activity,List<String> imageList) {
        this.activity = activity;
        this.imageList = imageList;
        getWidthAndHeight();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.naer_by_woloo_image_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.llMain.getLayoutParams().width = width;
        //ImageUtil.loadImage(activity, holder.ivPhoto , imageList.get(position));
        Glide.with(activity)
                .load(imageList.get(position))
                .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(16)))
                .placeholder(R.drawable.banner_logo)
                .into(holder.ivPhoto);
    }


    @Override
    public int getItemCount() {
        return imageList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llMain)
        LinearLayout llMain;

        @BindView(R.id.ivPhoto)
        ImageView ivPhoto;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void getWidthAndHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = (int) (displayMetrics.widthPixels - dpToPx(16));
    }

    public int dpToPx(int dp) {
        Resources r = activity.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}