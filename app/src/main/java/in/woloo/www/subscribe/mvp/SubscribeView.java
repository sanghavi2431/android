package in.woloo.www.subscribe.mvp;

import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.subscribe.models.GetSubscriptionDetailsResponse;
import in.woloo.www.subscribe.models.PlanResponse;

public interface SubscribeView {
    void setMySubscriptionResponse(GetSubscriptionDetailsResponse getSubscriptionDetailsResponse);
}
