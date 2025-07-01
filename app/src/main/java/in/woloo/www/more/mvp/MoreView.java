package in.woloo.www.more.mvp;

import in.woloo.www.more.models.SubscriptionStatusResponse;
import in.woloo.www.more.models.UserCoinsResponse;
import in.woloo.www.more.models.UserProfileMergedResponse;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.more.models.VoucherDetailsResponse;

public interface MoreView {
    void setProfileResponse(ViewProfileResponse viewProfileResponse);
    void editProfileSuccess();
    void userCoinsResponseSuccess(UserCoinsResponse userCoinsResponse);
    void setSubscriptionResponse(SubscriptionStatusResponse subscriptionStatusResponse);
    void setUserProfileMergedResponse(UserProfileMergedResponse userProfileMergedResponse);
    void setVoucherResponse(VoucherDetailsResponse voucherDetailsResponse);
}
