package `in`.woloo.www.shopping.mvp

import android.app.Activity
import android.content.Context
import com.android.volley.VolleyError
import com.google.gson.reflect.TypeToken
import com.jetsynthesys.encryptor.JetEncryptor
import `in`.woloo.www.application_kotlin.api_classes.APIConstants
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPICallModel
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPIResponseCallback
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.common.CommonUtils.Companion.printStackTrace
import `in`.woloo.www.networksUtils.NetworkAPICall
import `in`.woloo.www.search.mvp.WolooSearchPresenter
import `in`.woloo.www.shopping.model.GeoCodeResponse
import `in`.woloo.www.shopping.model.UserGiftCardResponse
import `in`.woloo.www.shopping.model.UserGiftCardUpdateResponse
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger.e
import org.json.JSONObject

class UserGiftCardPresenter : NetworkAPIResponseCallback {
    private var context: Context
    private var userGiftCardView: UserGiftCardView? = null
    private var userGiftCardViewUpdate: UserGiftCardUpdateView? = null
    private var geoCodeView: GeoCodeView? = null
    private val mNetworkAPICall: NetworkAPICall
    private val mJetEncryptor: JetEncryptor
    private val mCommonUtils: CommonUtils

    constructor(context: Context, userGiftCardView: UserGiftCardView?) {
        this.context = context
        this.userGiftCardView = userGiftCardView
        mNetworkAPICall = NetworkAPICall()
        mJetEncryptor = JetEncryptor.getInstance()
        mCommonUtils = CommonUtils()
    }

    constructor(context: Context, userGiftCardViewUpdate: UserGiftCardUpdateView?) {
        this.context = context
        this.userGiftCardViewUpdate = userGiftCardViewUpdate
        mNetworkAPICall = NetworkAPICall()
        mJetEncryptor = JetEncryptor.getInstance()
        mCommonUtils = CommonUtils()
    }

    constructor(context: Context, geoCodeView: GeoCodeView?) {
        this.context = context
        this.geoCodeView = geoCodeView
        mNetworkAPICall = NetworkAPICall()
        mJetEncryptor = JetEncryptor.getInstance()
        mCommonUtils = CommonUtils()
    }


    val userGiftCard: Unit
        get() {
            try {
                val mJsObjParam = JSONObject()
                val parserType =
                    object : TypeToken<UserGiftCardResponse?>() {
                    }.type
                val networkAPICallModel: NetworkAPICallModel = NetworkAPICallModel(
                    APIConstants.USER_GIFT_CARD_DETAILS,
                    AppConstants.POST_REQUEST,
                    AppConstants.APP_TYPE_MOBILE,
                    mJsObjParam,
                    mJetEncryptor
                )
                networkAPICallModel.parserType = parserType
                networkAPICallModel.isShowProgress = true
                mNetworkAPICall.callApplicationWS(
                    context as Activity,
                    networkAPICallModel,
                    this
                )
            } catch (e: Exception) {
                e(TAG, e.message!!)
            }
        }


    fun updateGiftCard(type: String?, points: Int, orderid: String?) {
        try {
            val mJsObjParam = JSONObject()
            mJsObjParam.put("type", type)
            mJsObjParam.put("coins", points)
            mJsObjParam.put("orderid", orderid)
            val parserType = object : TypeToken<UserGiftCardUpdateResponse?>() {
            }.type
            val networkAPICallModel: NetworkAPICallModel = NetworkAPICallModel(
                APIConstants.USER_GIFT_CARD_UPDATE,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam,
                mJetEncryptor
            )
            networkAPICallModel.parserType = parserType
            networkAPICallModel.isShowProgress = true
            mNetworkAPICall.callApplicationWS(
                context as Activity, networkAPICallModel,
                this
            )
        } catch (e: Exception) {
            e(TAG, e.message!!)
        }
    }


    fun updateGiftCardFail(transaction_id: Int) {
        try {
            val mJsObjParam = JSONObject()
            mJsObjParam.put("transaction_id", transaction_id)

            val parserType = object : TypeToken<UserGiftCardUpdateResponse?>() {
            }.type
            val networkAPICallModel: NetworkAPICallModel = NetworkAPICallModel(
                APIConstants.USER_GIFT_CARD_FAIL,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam,
                mJetEncryptor
            )
            networkAPICallModel.parserType = parserType
            networkAPICallModel.isShowProgress = true
            mNetworkAPICall.callApplicationWS(
                context as Activity, networkAPICallModel,
                this
            )
        } catch (e: Exception) {
            e(TAG, e.message!!)
        }
    }


    fun getReverseGeocoding(lat: String?, lng: String?) {
        try {
            val mJsObjParam = JSONObject()
            mJsObjParam.put("lat", lat)
            mJsObjParam.put("lng", lng)
            mJsObjParam.put("dev", true)

            val parserType = object : TypeToken<GeoCodeResponse?>() {
            }.type
            val networkAPICallModel: NetworkAPICallModel = NetworkAPICallModel(
                APIConstants.GEO_CODE_LOCATION_API,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam,
                mJetEncryptor
            )
            networkAPICallModel.parserType = parserType
            networkAPICallModel.isShowProgress = true
            mNetworkAPICall.callApplicationWS(
                context as Activity, networkAPICallModel,
                this
            )
        } catch (e: Exception) {
            e(TAG, e.message!!)
        }
    }


    override fun onSuccessResponse(
        response: JSONObject?,
        networkAPICallModel: NetworkAPICallModel?
    ) {
        when (networkAPICallModel!!.apiURL) {
            APIConstants.USER_GIFT_CARD_DETAILS -> try {
                val userGiftCardResponse: UserGiftCardResponse =
                    networkAPICallModel!!.responseObject as UserGiftCardResponse
                if (userGiftCardResponse != null) {
                    userGiftCardResponseFlow(userGiftCardResponse)
                }
            } catch (e: Exception) {
                printStackTrace(e)
            }

            APIConstants.USER_GIFT_CARD_UPDATE -> try {
                val userGiftCardUpdateResponse: UserGiftCardUpdateResponse =
                    networkAPICallModel.responseObject as UserGiftCardUpdateResponse
                if (userGiftCardUpdateResponse != null) {
                    userGiftCardResponseFlowUpdate(userGiftCardUpdateResponse)
                }
            } catch (e: Exception) {
                printStackTrace(e)
            }

            APIConstants.USER_GIFT_CARD_FAIL -> try {
                val userGiftCardUpdateResponse: UserGiftCardUpdateResponse =
                    networkAPICallModel.responseObject as UserGiftCardUpdateResponse
                if (userGiftCardUpdateResponse != null) {
                    userGiftCardResponseFlowUpdate(userGiftCardUpdateResponse)
                }
            } catch (e: Exception) {
                printStackTrace(e)
            }

            APIConstants.GEO_CODE_LOCATION_API -> try {
                val geoCodeResponse = networkAPICallModel.responseObject as GeoCodeResponse
                if (geoCodeResponse != null) {
                    geoCodeResponseFlow(geoCodeResponse)
                }
            } catch (e: Exception) {
                printStackTrace(e)
            }

            else -> {}
        }
    }


    private fun userGiftCardResponseFlowUpdate(userGiftCardUpdateResponse: UserGiftCardUpdateResponse?) {
        try {
            if (userGiftCardUpdateResponse != null && userGiftCardUpdateResponse.isSuccess) {
                userGiftCardViewUpdate!!.setUserCardResponseUpdate(userGiftCardUpdateResponse)
            }
        } catch (ex: Exception) {
            printStackTrace(ex)
        }
    }


    private fun userGiftCardResponseFlow(userGiftCardResponse: UserGiftCardResponse?) {
        try {
            if (userGiftCardResponse != null && userGiftCardResponse.isSuccess) {
                userGiftCardView!!.setUserCardResponse(userGiftCardResponse)
            }
        } catch (ex: Exception) {
            printStackTrace(ex)
        }
    }


    private fun geoCodeResponseFlow(geoCodeResponse: GeoCodeResponse?) {
        try {
            if (geoCodeResponse != null) {
                geoCodeView!!.setGeoCodeResponse(geoCodeResponse)
            }
        } catch (ex: Exception) {
            printStackTrace(ex)
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
        private val TAG: String = WolooSearchPresenter::class.java.getSimpleName()
    }
}