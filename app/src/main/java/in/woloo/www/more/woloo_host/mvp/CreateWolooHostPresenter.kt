package `in`.woloo.www.more.woloo_host.mvp

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
import `in`.woloo.www.more.models.ViewProfileResponse
import `in`.woloo.www.more.woloo_host.model.AddWolooResponse
import `in`.woloo.www.more.woloo_host.model.GeoCodeResponse
import `in`.woloo.www.networksUtils.NetworkAPICall
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import org.json.JSONArray
import org.json.JSONObject

class CreateWolooHostPresenter(
    private val mContext: Context,
    private val createWolooHostView: CreateWolooHostView
) : NetworkAPIResponseCallback {
    private val mCommonUtils: CommonUtils = CommonUtils()
    private val mJetEncryptor: JetEncryptor
    private val mNetworkAPICall: NetworkAPICall

    init {
        mJetEncryptor = JetEncryptor.getInstance()
        mNetworkAPICall = NetworkAPICall()
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
                Logger.e(TAG, e.message.toString())
            }
        }

    fun getLocation(lat: String?, lng: String?) {
        try {
            val mJsObjParam = JSONObject()
            mJsObjParam.put("lat", lat)
            mJsObjParam.put("lng", lng)
            val parserType = object : TypeToken<GeoCodeResponse?>() {}.type
            val networkAPICallModel = NetworkAPICallModel(
                APIConstants.GEO_CODE_LOCATION_API,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam,
                mJetEncryptor
            )
            networkAPICallModel.parserType = parserType
            networkAPICallModel.isShowProgress = true
            mNetworkAPICall.callApplicationWS(mContext as Activity, networkAPICallModel, this)
        } catch (e: Exception) {
            Logger.e(TAG, e.message.toString())
        }
    }

    fun becomeWolooHost(
        name: String?,
        city: String,
        address: String,
        zipCode: String,
        uploadedImages: JSONArray?,
        lat: Double,
        log: Double
    ) {
        try {
            val mJsObjParam = JSONObject()
            try {
                mJsObjParam.put(JSONTagConstant.NAME, name)
                mJsObjParam.put(JSONTagConstant.CITY, city)
                mJsObjParam.put(JSONTagConstant.ADDRESS, "$address $city $zipCode India")
                mJsObjParam.put(JSONTagConstant.IMAGE, uploadedImages)
                mJsObjParam.put(JSONTagConstant.ZIPCODE, zipCode)
                mJsObjParam.put(JSONTagConstant.LATITUDE, lat)
                mJsObjParam.put(JSONTagConstant.LONGITUDE, log)
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
            val parserType = object : TypeToken<AddWolooResponse?>() {}.type
            val networkAPICallModel = NetworkAPICallModel(
                APIConstants.ADD_WOLOO_HOST,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam,
                mJetEncryptor
            )
            networkAPICallModel.parserType = parserType
            networkAPICallModel.isShowProgress = true
            mNetworkAPICall.callApplicationWS(mContext as Activity, networkAPICallModel, this)
        } catch (e: Exception) {
            Logger.e(TAG, e.message.toString())
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

            APIConstants.ADD_WOLOO_HOST -> try {
                val addWolooResponse: AddWolooResponse =
                    networkAPICallModel.responseObject as AddWolooResponse
                if (addWolooResponse != null) {
                    addWolooHostResponseFlow(addWolooResponse)
                }
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }

            APIConstants.GEO_CODE_LOCATION_API -> try {
                val geoCodeResponse = networkAPICallModel.responseObject as GeoCodeResponse
                geoCodeResponse?.let { geoCodeResponseFlow(it) }
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }

            else -> {}
        }
    }

    private fun geoCodeResponseFlow(geoCodeResponse: GeoCodeResponse?) {
        if (geoCodeResponse != null && geoCodeResponse.status == AppConstants.API_SUCCESS) {
            createWolooHostView.geoCodeResponseSuccess(geoCodeResponse)
        } else CommonUtils.showCustomDialogBackClick(
            mContext,
            "Unable to find Location Service. Please start your location Service Or Reboot your device."
        )
    }

    private fun addWolooHostResponseFlow(addWolooResponse: AddWolooResponse?) {
        try {
            if (addWolooResponse != null && addWolooResponse.data!!.status == AppConstants.API_SUCCESS) {
                createWolooHostView.addWolooHostSuccess(addWolooResponse.data!!.message)
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun viewProfileResponseFlow(viewProfileResponse: ViewProfileResponse?) {
        try {
            if (viewProfileResponse != null && viewProfileResponse.status == AppConstants.API_SUCCESS) {
                createWolooHostView.setProfileResponse(viewProfileResponse)
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
        private val TAG = CreateWolooHostPresenter::class.java.simpleName
    }
}
