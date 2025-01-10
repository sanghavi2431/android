package `in`.woloo.www.more.mvp

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
import `in`.woloo.www.more.models.SubscriptionStatusResponse
import `in`.woloo.www.more.models.UserCoinsResponse
import `in`.woloo.www.more.models.UserProfileMergedResponse
import `in`.woloo.www.more.models.ViewProfileResponse
import `in`.woloo.www.more.models.VoucherDetailsResponse
import `in`.woloo.www.networksUtils.NetworkAPICall
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import org.json.JSONObject

class MorePresenter(private val mContext: Context, private val moreView: MoreView) :
    NetworkAPIResponseCallback {
    private val mCommonUtils: CommonUtils
    private val mJetEncryptor: JetEncryptor
    private val mNetworkAPICall: NetworkAPICall

    init {
        mCommonUtils = CommonUtils()
        mJetEncryptor = JetEncryptor.getInstance()
        mNetworkAPICall = NetworkAPICall()
    }

    fun editProfile(context: Context?, mJsObjParam: JSONObject?) {
        try {
            val parserType = object : TypeToken<EditProfileResponse?>() {}.type
            val networkAPICallModel = NetworkAPICallModel(
                APIConstants.EDIT_PROFILE,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam!!,
                mJetEncryptor
            )
            networkAPICallModel.isShowProgress = true
            networkAPICallModel.parserType = parserType
            mNetworkAPICall.callApplicationWS(mContext as Activity, networkAPICallModel, this)
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    val subscriptionDetails: Unit
        get() {
            try {
                val mJsObjParam = JSONObject()
                val parserType = object : TypeToken<SubscriptionStatusResponse?>() {}.type
                val networkAPICallModel = NetworkAPICallModel(
                    APIConstants.SUBSCRIPTION_STATUS,
                    AppConstants.POST_REQUEST,
                    AppConstants.APP_TYPE_MOBILE,
                    mJsObjParam,
                    mJetEncryptor
                )
                networkAPICallModel.isShowProgress = true
                networkAPICallModel.parserType = parserType
                mNetworkAPICall.callApplicationWS(mContext as Activity, networkAPICallModel, this)
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }
    val profile: Unit
        get() {
            try {
                val mJsObjParam = JSONObject()
                val parserType = object : TypeToken<ViewProfileResponse?>() {}.type
                val networkAPICallModel = NetworkAPICallModel(
                    APIConstants.VIEW_PROFILE,
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
    val userProfileAll: Unit
        get() {
            try {
                val mJsObjParam = JSONObject()
                val parserType = object : TypeToken<UserProfileMergedResponse?>() {}.type
                val networkAPICallModel = NetworkAPICallModel(
                    APIConstants.USER_PROFILE_MERGED,
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

    fun getVoucherDetails(voucher_code: String?) {
        try {
            val mJsObjParam = JSONObject()
            mJsObjParam.put(JSONTagConstant.VOUCHER_CODE, voucher_code)
            val parserType = object : TypeToken<VoucherDetailsResponse?>() {}.type
            val networkAPICallModel = NetworkAPICallModel(
                APIConstants.VOUCHER_CODE,
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
    val userOffers: Unit
        get() {
            try {
                val mJsObjParam = JSONObject()
                val parserType = object : TypeToken<UserCoinsResponse?>() {}.type
                val networkAPICallModel = NetworkAPICallModel(
                    APIConstants.USER_OFFER_LIST,
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

    fun UploadFile(fileName: String?) {
        try {
            val mJsObjParam = JSONObject()
            try {
                mJsObjParam.put(JSONTagConstant.TYPE, AppConstants.USER_PROFILE)
                mJsObjParam.put(JSONTagConstant.FILE_NAMES, fileName)
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
            val parserType = object : TypeToken<UserCoinsResponse?>() {}.type
            val networkAPICallModel = NetworkAPICallModel(
                APIConstants.FILE_UPLOAD,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam,
                mJetEncryptor
            )
            networkAPICallModel.parserType = parserType
            networkAPICallModel.isShowProgress = true
            mNetworkAPICall.callApplicationWS(mContext as Activity, networkAPICallModel, this)
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    override fun onSuccessResponse(
        response: JSONObject?,
        networkAPICallModel: NetworkAPICallModel?
    ) {
        when (networkAPICallModel!!.apiURL) {
            APIConstants.VIEW_PROFILE -> try {
                val viewProfileResponse: ViewProfileResponse =
                    networkAPICallModel.responseObject as ViewProfileResponse
                if (viewProfileResponse != null) {
                    viewProfileResponseFlow(viewProfileResponse)
                }
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            APIConstants.USER_COINS -> try {
                val userCoinsResponse: UserCoinsResponse =
                    networkAPICallModel.responseObject as UserCoinsResponse
                if (userCoinsResponse != null) {
                    userCoinsResponseFlow(userCoinsResponse)
                }
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            APIConstants.EDIT_PROFILE -> try {
                val editProfileResponse = networkAPICallModel.responseObject as EditProfileResponse
                if (editProfileResponse != null) {
                    //Saving User Profile  Data to Netcore
                    NetcoreUserDetails(mContext).updateNetcoreUserProfile()
                    editProfileResponseFlow(editProfileResponse)
                }
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            APIConstants.SUBSCRIPTION_STATUS -> try {
                val subscriptionStatusResponse: SubscriptionStatusResponse =
                    networkAPICallModel.responseObject as SubscriptionStatusResponse
                if (subscriptionStatusResponse != null) {
                    subscriptionStatusResponseFlow(subscriptionStatusResponse)
                }
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            APIConstants.USER_PROFILE_MERGED -> try {
                val userProfileMergedResponse: UserProfileMergedResponse =
                    networkAPICallModel.responseObject as UserProfileMergedResponse
                if (userProfileMergedResponse != null) {
                    userProfileMergedResponseResponseFlow(userProfileMergedResponse)
                }
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            APIConstants.VOUCHER_CODE -> try {
                val voucherDetailsResponse: VoucherDetailsResponse =
                    networkAPICallModel.responseObject as VoucherDetailsResponse
                if (voucherDetailsResponse != null) {
                    voucherDetailsResponseResponseFlow(voucherDetailsResponse)
                }
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            else -> {}
        }
    }

    private fun voucherDetailsResponseResponseFlow(voucherDetailsResponse: VoucherDetailsResponse?) {
        try {
            if (voucherDetailsResponse != null && voucherDetailsResponse.status == AppConstants.API_SUCCESS) {
                moreView.setVoucherResponse(voucherDetailsResponse)
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun subscriptionStatusResponseFlow(subscriptionStatusResponse: SubscriptionStatusResponse?) {
        try {
            if (subscriptionStatusResponse != null && subscriptionStatusResponse.status == AppConstants.API_SUCCESS) {
                moreView.setSubscriptionResponse(subscriptionStatusResponse)
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun userProfileMergedResponseResponseFlow(userProfileMergedResponse: UserProfileMergedResponse?) {
        try {
            if (userProfileMergedResponse != null && userProfileMergedResponse.status == AppConstants.API_SUCCESS) {
                moreView.setUserProfileMergedResponse(userProfileMergedResponse)
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun editProfileResponseFlow(editProfileResponse: EditProfileResponse) {
        try {
            moreView.editProfileSuccess()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun userCoinsResponseFlow(userCoinsResponse: UserCoinsResponse) {
        try {
            moreView.userCoinsResponseSuccess(userCoinsResponse)
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun viewProfileResponseFlow(viewProfileResponse: ViewProfileResponse?) {
        try {
            if (viewProfileResponse != null && viewProfileResponse.status == AppConstants.API_SUCCESS) {
                moreView.setProfileResponse(viewProfileResponse)
                // TODO Save updated user info?
            }
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
        private val TAG = MorePresenter::class.java.simpleName
    }
}
