package `in`.woloo.www.more.editprofile.mvp

import android.app.Activity
import android.content.Context
import com.android.volley.VolleyError
import com.google.gson.reflect.TypeToken
import com.jetsynthesys.encryptor.JetEncryptor
import `in`.woloo.www.application_kotlin.api_classes.APIConstants
import `in`.woloo.www.application_kotlin.api_classes.JSONTagConstant
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPICallModel
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPIResponseCallback
import `in`.woloo.www.application_kotlin.netcore.NetcoreUserDetails
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.models.EditProfileResponse
import `in`.woloo.www.more.subscribe.adapter.SubscribeAdapter
import `in`.woloo.www.more.subscribe.models.InitSubscriptionResponse
import `in`.woloo.www.networksUtils.NetworkAPICall
import `in`.woloo.www.utils.AppConstants
import org.json.JSONObject

class EditProfilePresenter(
    private val mContext: Context,
    private val editProfileView: EditProfileView
) :
    NetworkAPIResponseCallback {
    private val mNetworkAPICall: NetworkAPICall
    private val mJetEncryptor: JetEncryptor = JetEncryptor.getInstance()
    private val mCommonUtils =
        CommonUtils()

    init {
        mNetworkAPICall = NetworkAPICall()
    }

    fun editProfile(context: Context?, mJsObjParam: JSONObject) {
        try {
            val parserType = object : TypeToken<EditProfileResponse?>() {
            }.type
            val networkAPICallModel: NetworkAPICallModel = NetworkAPICallModel(
                APIConstants.EDIT_PROFILE,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam,
                mJetEncryptor
            )
            networkAPICallModel.isShowProgress = true
            networkAPICallModel.parserType = parserType
            mNetworkAPICall.callApplicationWS(
                mContext as Activity, networkAPICallModel,
                this
            )
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    fun initSubscription(
        id: Int,
        planId: String?,
        initSubscriptionCallback: SubscribeAdapter.InitSubscriptionCallback
    ) {
        try {
            val mJsObjParam = JSONObject()
            try {
                mJsObjParam.put(JSONTagConstant.ID, id)
                mJsObjParam.put(JSONTagConstant.PLAN_ID, planId)
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
            val parserType = object : TypeToken<InitSubscriptionResponse?>() {
            }.type
            val networkAPICallModel: NetworkAPICallModel = NetworkAPICallModel(
                APIConstants.INIT_SUBSCRIPTION_ORDER,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam,
                mJetEncryptor
            )
            networkAPICallModel.parserType = parserType
            networkAPICallModel.customObject = initSubscriptionCallback
            networkAPICallModel.isShowProgress = true
            mNetworkAPICall.callApplicationWS(
                mContext as Activity, networkAPICallModel,
                this
            )
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }


    override fun onSuccessResponse(
        response: JSONObject?,
        networkAPICallModel: NetworkAPICallModel?
    ) {
        when (networkAPICallModel!!.apiURL) {
            APIConstants.EDIT_PROFILE -> try {
                val editProfileResponse = networkAPICallModel.responseObject as EditProfileResponse
                //Saving User Profile  Data to Netcore
                NetcoreUserDetails(mContext).updateNetcoreUserProfile()

                editProfileResponseFlow(editProfileResponse)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            APIConstants.INIT_SUBSCRIPTION_ORDER -> try {
                val initSubscriptionCallback: SubscribeAdapter.InitSubscriptionCallback =
                    networkAPICallModel.customObject as SubscribeAdapter.InitSubscriptionCallback
                val initSubscriptionResponse =
                    networkAPICallModel.responseObject as InitSubscriptionResponse
                initSubscriptionCallback.initSubscriptionSuccess(initSubscriptionResponse)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            else -> {}
        }
    }

    private fun editProfileResponseFlow(editProfileResponse: EditProfileResponse?) {
        try {
            if (editProfileResponse != null && editProfileResponse.status == AppConstants.API_SUCCESS) {
                editProfileView.editProfileSuccess()
            }
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
