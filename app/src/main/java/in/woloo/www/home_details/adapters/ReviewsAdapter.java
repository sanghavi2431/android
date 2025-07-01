package in.woloo.www.home_details.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.review.ViewReviewActivity;
import in.woloo.www.review.fragments.ViewReviewFragment;
import in.woloo.www.review.models.ReviewListResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.CircleImageView;
import in.woloo.www.utils.ImageUtil;
import in.woloo.www.utils.TimeAgoUtils;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Context context;
    private List<ReviewListResponse.Review> reviewList;
    private ImageUtil imageUtil;


    public ReviewsAdapter(Context context, List<ReviewListResponse.Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
        imageUtil = new ImageUtil();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.reviews_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         holder.ivProfile.setClipToOutline(true);
         if(reviewList.get(position).getUserDetails().getName()!=null){
             if (!reviewList.get(position).getUserDetails().getName().equalsIgnoreCase("")){
                 holder.tvName.setText(reviewList.get(position).getUserDetails().getName());
             }else{
                 holder.tvName.setText("Guest");
             }

         }else{
             holder.tvName.setText("Guest");
         }

         holder.tvReview.setText(reviewList.get(position).getReviewDescription());
         holder.tvRating.setText(reviewList.get(position).getRating().toString());

         try{
             if (reviewList.get(position).getReviewDescription().length() > 60) {
                 holder.tvReview.setLinkTextColor(context.getResources().getColor(R.color.text_color_five));
                 holder.tvReview.setText(String.format("%s%s",holder.tvReview.getText().subSequence(0, 60), context.getResources().getString(R.string.read_more)));
                 SpannableString spannableString = new SpannableString(holder.tvReview.getText().toString());
                 ClickableSpan clickableSpan = new ClickableSpan() {
                     @Override
                     public void onClick(View textView) {
                         context.startActivity(new Intent(context, ViewReviewActivity.class).putExtra(AppConstants.REVIEW,reviewList.get(position).getReviewDescription()));
                     }
                     @Override
                     public void updateDrawState(TextPaint ds) {
                         super.updateDrawState(ds);
                         ds.setUnderlineText(false);
                     }
                 };
                 spannableString.setSpan(clickableSpan,holder.tvReview.getText().toString().length()-11, holder.tvReview.getText().toString().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                 holder.tvReview.setText(spannableString);
                 holder.tvReview.setMovementMethod(LinkMovementMethod.getInstance());
                 holder.tvReview.setHighlightColor(Color.TRANSPARENT);
             }
         }catch (Exception ex) {
              CommonUtils.printStackTrace(ex);
         }

         holder.tvTimeAgo.setText(TimeAgoUtils.getTimeAgo(CommonUtils.getTimeAgo(reviewList.get(position).getUpdatedAt())));
         if(reviewList.get(position).getUserDetails().getAvatar() == null || reviewList.get(position).getUserDetails().getAvatar().trim().equals("users/default.png") || reviewList.get(position).getUserDetails().getAvatar().trim().equals("default.png")){
            ImageUtil.loadImageProfile(context,holder.ivProfile, BuildConfig.BASE_URL+"public/userProfile/default.png");
         }else{
            ImageUtil.loadImageProfile(context,holder.ivProfile, BuildConfig.BASE_URL+"public/userProfile/"+ reviewList.get(position).getUserDetails().getAvatar());
         }
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivProfile)
        CircleImageView ivProfile;

        @BindView(R.id.tvReview)
        TextView tvReview;

        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvTimeAgo)
        TextView tvTimeAgo;

        @BindView(R.id.tvUserRating)
        TextView tvRating;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
