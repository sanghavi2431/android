package `in`.woloo.www.more.fragments.mvp

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
import `in`.woloo.www.more.fragments.models.InviteFriendsResponse
import `in`.woloo.www.more.models.ViewProfileResponse
import `in`.woloo.www.networksUtils.NetworkAPICall
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import org.json.JSONObject

class InviteFriendsPresenter(
    private val mContext: Context,
    private val inviteFriendsView: InviteFriendsView
) :
    NetworkAPIResponseCallback {
    private val mJetEncryptor: JetEncryptor = JetEncryptor.getInstance()
    private val mNetworkAPICall: NetworkAPICall
    init {
        mNetworkAPICall = NetworkAPICall()
    }


    fun inviteContacts(mobileNumber: String?, message: String?, shareUrl: String?) {
        try {
            try {
                val mJsObjParam = JSONObject()
                try {
                    mJsObjParam.put(JSONTagConstant.INVITE_MOBILE_NUMBER, mobileNumber)
                    mJsObjParam.put(JSONTagConstant.MESSAGE, message)
                    mJsObjParam.put(JSONTagConstant.SHARE_URL, shareUrl)
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
                val parserType = object : TypeToken<InviteFriendsResponse?>() {
                }.type
                val networkAPICallModel: NetworkAPICallModel = NetworkAPICallModel(
                    APIConstants.INVITE,
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
            } catch (e: Exception) {
                Logger.e(TAG, e.message.toString())
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }


    val profile: Unit
        get() {
            try {
                val mJsObjParam = JSONObject()
                val parserType =
                    object : TypeToken<ViewProfileResponse?>() {
                    }.type
                val networkAPICallModel: NetworkAPICallModel = NetworkAPICallModel(
                    APIConstants.VIEW_PROFILE,
                    AppConstants.POST_REQUEST,
                    AppConstants.APP_TYPE_MOBILE,
                    mJsObjParam,
                    mJetEncryptor
                )
                networkAPICallModel.parserType = parserType
                networkAPICallModel.isShowProgress = true
                mNetworkAPICall.callApplicationWS(
                    mContext as Activity,
                    networkAPICallModel,
                    this
                )
            } catch (e: Exception) {
                Logger.e(
                    TAG,
                    e.message!!
                )
            }
        }


    override fun onSuccessResponse(
        response: JSONObject?,
        networkAPICallModel: NetworkAPICallModel?
    ) {
        when (networkAPICallModel!!.apiURL) {
            APIConstants.INVITE -> try {
                val inviteFriendsResponse =
                    networkAPICallModel.responseObject as InviteFriendsResponse
                inviteFriendsResponseFlow(inviteFriendsResponse)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            APIConstants.VIEW_PROFILE -> try {
                val viewProfileResponse: ViewProfileResponse =
                    networkAPICallModel.responseObject as ViewProfileResponse
                viewProfileResponseFlow(viewProfileResponse)
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }

            else -> {}
        }
    }

    private fun viewProfileResponseFlow(viewProfileResponse: ViewProfileResponse?) {
        try {
            if (viewProfileResponse != null && viewProfileResponse.status == AppConstants.API_SUCCESS) {
                inviteFriendsView.showRefferalCode(
                    viewProfileResponse.userData!!.refCode,
                    viewProfileResponse.userData!!.expiryDate
                )
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun inviteFriendsResponseFlow(inviteFriendsResponse: InviteFriendsResponse?) {
        try {
            if (inviteFriendsResponse != null && inviteFriendsResponse.status!!.toString() == AppConstants.API_SUCCESS
            ) {
                inviteFriendsView.inviteFriendSuccess(inviteFriendsResponse.message)
            } else {
                CommonUtils.showMaintenanceDialog(mContext, true)
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

    companion object {
        private val TAG: String = InviteFriendsPresenter::class.java.simpleName
    }
}
