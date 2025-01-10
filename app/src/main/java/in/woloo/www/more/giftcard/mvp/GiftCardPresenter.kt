package `in`.woloo.www.more.giftcard.mvp

import android.app.Activity
import android.content.Context
import com.android.volley.VolleyError
import com.google.gson.reflect.TypeToken
import com.jetsynthesys.encryptor.JetEncryptor
import `in`.woloo.www.application_kotlin.api_classes.APIConstants
import `in`.woloo.www.application_kotlin.api_classes.JSONTagConstant
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPICallModel
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPIResponseCallback
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.giftcard.model.GiftCardModelResponse
import `in`.woloo.www.more.giftcard.model.RequestPointsResponse
import `in`.woloo.www.more.models.UserCoinsResponse
import `in`.woloo.www.networksUtils.NetworkAPICall
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger.e
import org.json.JSONObject

class GiftCardPresenter(private val mContext: Context, private val giftCardView: GiftCardView) :
    NetworkAPIResponseCallback {
    private val mCommonUtil = CommonUtils()

    fun sendGiftCard(amount: String, mobileNumber: String, message: String?) {
        val mNetworkAPICall: NetworkAPICall = NetworkAPICall()
        val mJsObjParam = JSONObject()
        try {
            mJsObjParam.put(JSONTagConstant.AMOUNT, amount.toInt())
            mJsObjParam.put(JSONTagConstant.MOBILE_NUMBER_, mobileNumber.toLong())
            mJsObjParam.put(JSONTagConstant.GIFT_MESSAGE, message)
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
        val parserType = object : TypeToken<GiftCardModelResponse?>() {
        }.type
        val networkAPICallModel: NetworkAPICallModel = NetworkAPICallModel(
            APIConstants.WOLOO_GIFT,
            AppConstants.POST_REQUEST,
            AppConstants.APP_TYPE_MOBILE,
            mJsObjParam,
            JetEncryptor.getInstance()
        )
        networkAPICallModel.isShowProgress = true
        networkAPICallModel.parserType = parserType
        mNetworkAPICall.callApplicationWS(
            mContext as Activity, networkAPICallModel,
            this
        )
    }

    fun RequestPoints(amount: String, mobileNumber: String, message: String?) {
        val mNetworkAPICall: NetworkAPICall = NetworkAPICall()
        val mJsObjParam = JSONObject()
        try {
            mJsObjParam.put(JSONTagConstant.COINS, amount.toInt())
            mJsObjParam.put(JSONTagConstant.MOBILE, mobileNumber.toLong())
            mJsObjParam.put(JSONTagConstant.GIFT_MESSAGE, message)
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
        val parserType = object : TypeToken<RequestPointsResponse?>() {
        }.type
        val networkAPICallModel: NetworkAPICallModel = NetworkAPICallModel(
            APIConstants.REQUEST_POINTS,
            AppConstants.POST_REQUEST,
            AppConstants.APP_TYPE_MOBILE,
            mJsObjParam,
            JetEncryptor.getInstance()
        )
        networkAPICallModel.isShowProgress = true
        networkAPICallModel.parserType = parserType
        mNetworkAPICall.callApplicationWS(
            mContext as Activity, networkAPICallModel,
            this
        )
    }

    val userCoins: Unit
        get() {
            try {
                val mNetworkAPICall: NetworkAPICall = NetworkAPICall()
                val mJsObjParam = JSONObject()
                val parserType =
                    object : TypeToken<UserCoinsResponse?>() {
                    }.type
                val networkAPICallModel: NetworkAPICallModel = NetworkAPICallModel(
                    APIConstants.USER_COINS,
                    AppConstants.POST_REQUEST,
                    AppConstants.APP_TYPE_MOBILE,
                    mJsObjParam,
                    JetEncryptor.getInstance()
                )
                networkAPICallModel.parserType = parserType
                networkAPICallModel.isShowProgress = true
                mNetworkAPICall.callApplicationWS(
                    mContext as Activity,
                    networkAPICallModel,
                    this
                )
            } catch (e: Exception) {
                e("TAG", e.message!!)
            }
        }


    override fun onSuccessResponse(
        response: JSONObject?,
        networkAPICallModel: NetworkAPICallModel?
    ) {
        when (networkAPICallModel!!.apiURL) {
            APIConstants.WOLOO_GIFT -> try {
                val giftCardModelResponse: GiftCardModelResponse =
                    networkAPICallModel!!.responseObject as GiftCardModelResponse
                giftCardResponseFlow(giftCardModelResponse)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            APIConstants.USER_COINS -> try {
                val userCoinsResponse: UserCoinsResponse =
                    networkAPICallModel.responseObject as UserCoinsResponse
                giftCardView.userCoinsResponseSuccess(userCoinsResponse)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            APIConstants.REQUEST_POINTS -> try {
                val requestPointsResponse: RequestPointsResponse =
                    networkAPICallModel.responseObject as RequestPointsResponse
                giftCardView.RequestPointsResponseSuccess(requestPointsResponse)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            else -> {}
        }
    }

    private fun giftCardResponseFlow(giftCardModelResponse: GiftCardModelResponse) {
        try {
            giftCardView.showResult(giftCardModelResponse.data.message)
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    override fun onFailure(volleyError: VolleyError?, networkAPICallModel: NetworkAPICallModel?) {
    }

    override fun onNoInternetConnection(
        context: Activity?,
        networkAPICallModel: NetworkAPICallModel?,
        networkAPIResponseCallback: NetworkAPIResponseCallback?
    ) {
    }

    override fun onTimeOutConnection(
        context: Activity?,
        networkAPICallModel: NetworkAPICallModel?,
        networkAPIResponseCallback: NetworkAPIResponseCallback?
    ) {
    }
}
