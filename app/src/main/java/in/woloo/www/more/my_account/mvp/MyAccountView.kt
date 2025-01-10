package `in`.woloo.www.more.my_account.mvp

import `in`.woloo.www.more.models.UserCoinHistoryModel
import `in`.woloo.www.more.models.UserCoinsResponse

interface MyAccountView {
    fun userCoinsSuccess(userCoinsResponse: UserCoinsResponse?)
    fun userCoinsHistorySuccess(userCoinHistoryModel: UserCoinHistoryModel?) /*
            calling on userCoinsHistorySuccess
        */
    // void userCoinsHistorySuccess(UserCoinHistoryModel userCoinHistoryModel);
}
