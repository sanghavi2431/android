package in.woloo.www.home_details.mvp;

import android.widget.TextView;

import in.woloo.www.home_details.models.LikeResponse;
import in.woloo.www.home_details.models.LikeStatusResponse;
import in.woloo.www.review.models.ReviewListResponse;

public interface HomeDetailsView {
    void getReviewList(ReviewListResponse reviewListResponse);
    void getLike_Unlike(LikeResponse likeResponse, TextView tv_like);
    void likeStatusSuccess(LikeStatusResponse likeStatusResponse);
    void onRedeemSuccess();
}
