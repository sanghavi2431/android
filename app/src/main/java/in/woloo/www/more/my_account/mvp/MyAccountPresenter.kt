package `in`.woloo.www.more.my_account.mvp

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
import `in`.woloo.www.more.models.UserCoinHistoryModel
import `in`.woloo.www.more.models.UserCoinsResponse
import `in`.woloo.www.networksUtils.NetworkAPICall
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import org.json.JSONObject

class MyAccountPresenter(private val mContext: Context, private val myAccountView: MyAccountView) :
    NetworkAPIResponseCallback {
    private val mCommonUtils: CommonUtils
    private val mJetEncryptor: JetEncryptor
    private val mNetworkAPICall: NetworkAPICall

    init {
        mCommonUtils = CommonUtils()
        mJetEncryptor = JetEncryptor.getInstance()
        mNetworkAPICall = NetworkAPICall()
    }

    val userCoins: Unit
        get() {
            try {
                val mJsObjParam = JSONObject()
                val parserType = object : TypeToken<UserCoinsResponse?>() {}.type
                val networkAPICallModel = NetworkAPICallModel(
                    APIConstants.USER_COINS,
                    AppConstants.POST_REQUEST,
                    AppConstants.APP_TYPE_MOBILE,
                    mJsObjParam,
                    mJetEncryptor
                )
                networkAPICallModel.parserType = parserType
                networkAPICallModel.isShowProgress = true
                mNetworkAPICall.callApplicationWS(mContext as Activity, networkAPICallModel, this)
            } catch (e: Exception) {
                Logger.e(TAG, e.message!!)
            }
        }

    fun getCoinHistory(mPageNumber: Int) {
        try {
            val mJsObjParam = JSONObject()
            try {
                mJsObjParam.put(JSONTagConstant.PAGE_NUMBER, mPageNumber)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
            val parserType = object : TypeToken<UserCoinHistoryModel?>() {}.type
            val networkAPICallModel = NetworkAPICallModel(
                APIConstants.COINHISTORY,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam,
                mJetEncryptor
            )
            networkAPICallModel.parserType = parserType
            networkAPICallModel.isShowProgress = false
            mNetworkAPICall.callApplicationWS(mContext as Activity, networkAPICallModel, this)
        } catch (e: Exception) {
            Logger.e(TAG, e.message!!)
        }
    }

    override fun onSuccessResponse(
        response: JSONObject?,
        networkAPICallModel: NetworkAPICallModel?
    ) {
        when (networkAPICallModel!!.apiURL) {
            APIConstants.USER_COINS -> try {
                val userCoinsResponse: UserCoinsResponse =
                    networkAPICallModel!!.responseObject as UserCoinsResponse
                if (userCoinsResponse != null) {
                    userCoinsResponseFlow(userCoinsResponse)
                }
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            APIConstants.COINHISTORY -> try {
                val userCoinHistoryModel: UserCoinHistoryModel =
                    networkAPICallModel.responseObject as UserCoinHistoryModel
                if (userCoinHistoryModel != null) {
                    userCoinsHistoryFlow(userCoinHistoryModel)
                }
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            else -> {}
        }
    }

    private fun userCoinsResponseFlow(userCoinsResponse: UserCoinsResponse) {
        try {
            myAccountView.userCoinsSuccess(userCoinsResponse)
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun userCoinsHistoryFlow(userCoinHistoryModel: UserCoinHistoryModel) {
        try {
            myAccountView.userCoinsHistorySuccess(userCoinHistoryModel)
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    override fun onFailure(volleyError: VolleyError?, networkAPICallModel: NetworkAPICallModel?) {}
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

    companion object {
        private val TAG = MyAccountPresenter::class.java.simpleName
    }
}
