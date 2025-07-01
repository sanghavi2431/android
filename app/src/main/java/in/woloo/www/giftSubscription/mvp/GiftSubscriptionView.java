package in.woloo.www.giftSubscription.mvp;

import in.woloo.www.giftSubscription.model.GetGiftPlansResponse;
import in.woloo.www.giftSubscription.model.SendGiftCardResponse;
import in.woloo.www.refer_woloo_host.model.ReferredWolooListResponse;

public interface GiftSubscriptionView {
    void getGiftPlansResponse(GetGiftPlansResponse getGiftPlansResponse);

    void sendGiftCardResponse(SendGiftCardResponse sendGiftCardResponse);
}
