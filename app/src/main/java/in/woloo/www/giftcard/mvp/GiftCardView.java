package in.woloo.www.giftcard.mvp;

import java.io.Serializable;

import in.woloo.www.giftcard.model.RequestPointsResponse;
import in.woloo.www.more.models.UserCoinsResponse;

public interface GiftCardView extends Serializable {
    void showResult(String message);
    void userCoinsResponseSuccess(UserCoinsResponse userCoinsResponse);
    void RequestPointsResponseSuccess(RequestPointsResponse requestPointsResponse);
    void pointsAddedResponseSuccess();
}
