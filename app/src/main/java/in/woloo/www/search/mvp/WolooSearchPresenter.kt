package `in`.woloo.www.search.mvp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.android.volley.VolleyError
import com.google.gson.reflect.TypeToken
import com.jetsynthesys.encryptor.JetEncryptor
import `in`.woloo.www.application_kotlin.api_classes.APIConstants
import `in`.woloo.www.application_kotlin.api_classes.JSONTagConstant
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPICallModel
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPIResponseCallback
import `in`.woloo.www.application_kotlin.api_classes.NetworkStatus
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.interfaces.WolooSearchView
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.model.server_response.SearchWolooResponse
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.networksUtils.NetworkAPICall
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility.hideKeyboard
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent
import org.json.JSONObject

class WolooSearchPresenter(private val context: Context, wolooSearchView: WolooSearchView) :
    NetworkAPIResponseCallback {
    private val wolooSearchView: WolooSearchView = wolooSearchView
    private val mNetworkAPICall: NetworkAPICall
    private val mJetEncryptor: JetEncryptor
    private val mCommonUtils: CommonUtils
    var keywords: String = ""

    init {
        mNetworkAPICall = NetworkAPICall()
        mJetEncryptor = JetEncryptor.getInstance()
        mCommonUtils = CommonUtils()
    }

    fun wolooSearchAPI(lat: String?, lng: String?, name: String?, pageNumber: Int) {
        try {
            val mJsObjParam = JSONObject()
            try {
                mJsObjParam.put("lat", lat)
                mJsObjParam.put("lng", lng)
                mJsObjParam.put("name", name)
                mJsObjParam.put("pageNumber", pageNumber)
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }

            val parserType = object : TypeToken<SearchWolooResponse?>() {
            }.type
            val networkAPICallModel: NetworkAPICallModel = NetworkAPICallModel(
                APIConstants.SEARCH_WOLOO_API,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam,
                mJetEncryptor
            )
            networkAPICallModel.parserType = parserType
            networkAPICallModel.isShowProgress = true
            val timeout = 120 * 1000
            networkAPICallModel.timeOut = timeout
            mNetworkAPICall.callApplicationWS(
                context as Activity, networkAPICallModel,
                this
            )
        } catch (e: Exception) {
            Logger.e(TAG, e.message.toString())
        }
    }

    fun getNearByStore(
        lat: Double,
        lng: Double,
        name: String,
        isOffer: Boolean,
        isSearchWithOnlyOffer: Boolean
    ) {
        val bundle = Bundle()
        keywords = name
        bundle.putString(AppConstants.SEARCH_KEYWORD, name)
        bundle.putString(AppConstants.LOCATION, "($lat,$lng)")
        logFirebaseEvent(context, bundle, AppConstants.SEARCH_WOLOO_EVENT)

        val payload = HashMap<String, Any>()
        payload[AppConstants.SEARCH_KEYWORD] = name
        payload[AppConstants.LOCATION] = "($lat,$lng)"
        logNetcoreEvent(context, payload, AppConstants.SEARCH_WOLOO_EVENT)

        val mNetworkAPICall: NetworkAPICall = NetworkAPICall()
        val mJsObjParam = JSONObject()
        try {
            mJsObjParam.put(JSONTagConstant.LATITUDE, lat)
            mJsObjParam.put(JSONTagConstant.LONGITUDE, lng)
            mJsObjParam.put(JSONTagConstant.PAGE_NUMBER_NEAR_WOLOO, "1")
            mJsObjParam.put(JSONTagConstant.KM_RANGE, "2")
            mJsObjParam.put(
                JSONTagConstant.TRANSPORT_MODE, SharedPreference(context).getStoredPreference(
                    context, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0"
                )
            )
            mJsObjParam.put(JSONTagConstant.IS_SEARCH, "1")
            if (isOffer || isSearchWithOnlyOffer) {
                mJsObjParam.put(JSONTagConstant.IS_OFFER, "1")
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
        val parserType = object : TypeToken<NearByStoreResponse?>() {
        }.type
        val networkAPICallModel: NetworkAPICallModel = NetworkAPICallModel(
            APIConstants.NEAR_BY_STORE,
            AppConstants.POST_REQUEST,
            AppConstants.APP_TYPE_MOBILE,
            mJsObjParam,
            JetEncryptor.getInstance()
        )
        networkAPICallModel.isShowProgress = true
        networkAPICallModel.parserType = parserType
        val timeout = 120 * 1000
        networkAPICallModel.timeOut = timeout
        mNetworkAPICall.callApplicationWS(context as Activity, networkAPICallModel, this)
    }


    override fun onSuccessResponse(
        response: JSONObject?,
        networkAPICallModel: NetworkAPICallModel?
    ) {
        when (networkAPICallModel!!.apiURL) {
            APIConstants.SEARCH_WOLOO_API -> try {
                val searchWolooResponse: SearchWolooResponse =
                    networkAPICallModel.responseObject as SearchWolooResponse
                if (searchWolooResponse != null) {
                    searchWolooResponseFlow(searchWolooResponse)
                }
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            APIConstants.NEAR_BY_STORE -> try {
                val nearByStoreResponse: NearByStoreResponse =
                    networkAPICallModel.responseObject as NearByStoreResponse
                if (nearByStoreResponse.status.equals(
                        NetworkStatus.SUCCESS_STR,
                        ignoreCase = true
                    )
                ) {
                    wolooSearchView.onGetNearByStore(
                        nearByStoreResponse,
                        networkAPICallModel,
                        keywords
                    )
                }
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }

            else -> {}
        }
    }

    private fun searchWolooResponseFlow(searchWolooResponse: SearchWolooResponse) {
        try {
            hideKeyboard((context as Activity))
            wolooSearchView.searchWolooSuccess(searchWolooResponse, keywords)
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
        private val TAG: String = WolooSearchPresenter::class.java.simpleName
    }
}
