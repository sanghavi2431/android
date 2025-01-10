package `in`.woloo.www.mapdirection.mvp

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
import `in`.woloo.www.common.CommonUtils.Companion.printStackTrace
import `in`.woloo.www.mapdirection.model.NavigationRewardsResponse
import `in`.woloo.www.networksUtils.NetworkAPICall
import `in`.woloo.www.utils.AppConstants
import org.json.JSONObject

class MapDirectionPresenter(
    private val mContext: Context,
    private val mapDirectionView: MapDirectionView
) :
    NetworkAPIResponseCallback {
    private val mNetworkAPICall: NetworkAPICall
    private val mJetEncryptor: JetEncryptor = JetEncryptor.getInstance()
    private val mCommonUtils =
        CommonUtils()

    init {
        mNetworkAPICall = NetworkAPICall()
    }

    fun getWolooNavigationReward(wolooId: Int) {
        try {
            val mJsObjParam = JSONObject()
            try {
                mJsObjParam.put(JSONTagConstant.WOLOO_ID, wolooId)
            } catch (ex: Exception) {
                printStackTrace(ex)
            }
            val parserType = object : TypeToken<NavigationRewardsResponse?>() {
            }.type
            val networkAPICallModel: NetworkAPICallModel = NetworkAPICallModel(
                APIConstants.NAVIGATION_REWARD,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam,
                mJetEncryptor
            )
            networkAPICallModel.parserType = parserType
            networkAPICallModel.isShowProgress = true
            mNetworkAPICall.callApplicationWS(
                mContext as Activity, networkAPICallModel,
                this
            )
        } catch (ex: Exception) {
            printStackTrace(ex)
        }
    }


    override fun onSuccessResponse(
        response: JSONObject?,
        networkAPICallModel: NetworkAPICallModel?
    ) {
        when (networkAPICallModel!!.apiURL) {
            APIConstants.NAVIGATION_REWARD -> try {
                val navigationRewardsResponse: NavigationRewardsResponse =
                    networkAPICallModel.responseObject as NavigationRewardsResponse
                mapDirectionView.navigationRewardSuccess(navigationRewardsResponse)
            } catch (e: Exception) {
                printStackTrace(e)
            }

            else -> {}
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
