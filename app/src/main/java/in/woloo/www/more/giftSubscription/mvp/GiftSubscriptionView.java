package in.woloo.www.more.giftSubscription.mvp;

import in.woloo.www.more.giftSubscription.model.GetGiftPlansResponse;
import in.woloo.www.more.giftSubscription.model.SendGiftCardResponse;

public interface GiftSubscriptionView {
    void getGiftPlansResponse(GetGiftPlansResponse getGiftPlansResponse);

    void sendGiftCardResponse(SendGiftCardResponse sendGiftCardResponse);
}
