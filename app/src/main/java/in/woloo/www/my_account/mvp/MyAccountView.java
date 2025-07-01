package in.woloo.www.my_account.mvp;

import in.woloo.www.more.models.UserCoinHistoryModel;
import in.woloo.www.more.models.UserCoinsResponse;

public interface MyAccountView {
    void userCoinsSuccess(UserCoinsResponse userCoinsResponse);
    void userCoinsHistorySuccess(UserCoinHistoryModel userCoinHistoryModel);


    /*
            calling on userCoinsHistorySuccess
        */
   // void userCoinsHistorySuccess(UserCoinHistoryModel userCoinHistoryModel);
}
