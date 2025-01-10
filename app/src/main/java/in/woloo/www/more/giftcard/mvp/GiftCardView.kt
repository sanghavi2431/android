package `in`.woloo.www.more.giftcard.mvp

import `in`.woloo.www.more.giftcard.model.RequestPointsResponse
import `in`.woloo.www.more.models.UserCoinsResponse
import java.io.Serializable

interface GiftCardView : Serializable {
    fun showResult(message: String?)
    fun userCoinsResponseSuccess(userCoinsResponse: UserCoinsResponse?)
    fun RequestPointsResponseSuccess(requestPointsResponse: RequestPointsResponse?)
    fun pointsAddedResponseSuccess()
}
