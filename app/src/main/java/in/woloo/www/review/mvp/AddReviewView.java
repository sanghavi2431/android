package in.woloo.www.review.mvp;

import in.woloo.www.review.models.ReviewOptionsResponse;
import in.woloo.www.review.models.SubmitReviewResponse;

public interface AddReviewView {
    void reviewOptionsList(ReviewOptionsResponse reviewOptionsResponse);
    void showSubmitReviewResponse(SubmitReviewResponse submitReviewResponse);
}
