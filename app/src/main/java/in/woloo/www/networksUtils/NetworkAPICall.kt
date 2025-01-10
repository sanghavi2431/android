package `in`.woloo.www.networksUtils

import android.app.Activity
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.google.gson.Gson
import com.jetsynthesys.encryptor.JetEncryptor
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.api_classes.JSONTagConstant
import `in`.woloo.www.application_kotlin.api_classes.JetEncryptorReInit
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPICallModel
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPIResponseCallback
import `in`.woloo.www.application_kotlin.api_classes.NetworkStatus
import `in`.woloo.www.application_kotlin.api_classes.NetworkUtils
import `in`.woloo.www.application_kotlin.interfaces.DialogCallListener
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.ToastUtils
import org.json.JSONObject

class NetworkAPICall {
    private val MAX_RETRY_LIMIT = 2


    //private CommonUtils commonUtils = new CommonUtils();
    fun callApplicationWS(
        context: Activity,
        networkAPICallModel: NetworkAPICallModel,
        networkAPIResponseCallback: NetworkAPIResponseCallback?
    ) {
        try {
            val commonUtils: CommonUtils = networkAPICallModel.commonUtils
            val networkUtils = NetworkUtils()
            //Netcore Disabled Log
            Logger.v(
                TAG,
                "callApplicationWS API Name:  ++++++++ : " + networkAPICallModel.apiURL + "   ++++++++ :"
            )
            if (networkUtils.isConnected(context)) {
                try {
                    if (networkAPICallModel.isShowProgress && !networkAPICallModel.isProgressVisible) {
                        commonUtils.showProgress(context)
                        networkAPICallModel.isProgressVisible = true
                    }

                    networkAPICallModel.app_type = commonUtils.getMobileTvType(context)
                    val queue = VolleySingleton.getInstance(context).requestQueue
                    val userStatusUrl = BuildConfig.BASE_URL + networkAPICallModel.apiURL
                    var jsonObject: JSONObject? = null
                    if (networkAPICallModel.app_type != null && networkAPICallModel.app_type.equals(
                            commonUtils.APP_TYPE_TV,
                            ignoreCase = true
                        )
                    ) {
                        jsonObject = networkAPICallModel.jsonObjectRequest
                        jsonObject.put(
                            JSONTagConstant.LOCALE,
                            CommonUtils.getCustomLocale(context, commonUtils.APP_TYPE_TV)
                        )
                    } else {
                        jsonObject = networkAPICallModel.jsonObjectRequest
                        jsonObject.put(
                            JSONTagConstant.LOCALE,
                            CommonUtils.getCustomLocale(context, commonUtils.APP_TYPE_MOBILE)
                        )
                    }
                    if (networkAPICallModel.jetEncryptor == null) {
                        networkAPICallModel.jetEncryptor = JetEncryptor.getInstance()
                    }
                    var encryptedRequest: JSONObject? = networkAPICallModel.encryptedRequest
                    if (encryptedRequest == null && JetEncryptor.getInstance().isInilized) {
                        encryptedRequest = commonUtils.getEncryptedJsonRequest(
                            context,
                            networkAPICallModel.jsonObjectRequest,
                            networkAPICallModel.jetEncryptor,
                            networkAPICallModel.app_type
                        )
                        //                      Logger.i(TAG,"callApplicationWS Volley encryptedRequest ++++++++ : "+encryptedRequest.toString());
                        networkAPICallModel.encryptedRequest = encryptedRequest
                    }
                    /*if (networkAPICallModel.getEncryptedRequest()==null && networkAPICallModel.getJetEncryptor()==null && !JetEncryptor.getInstance().isInilized() && networkAPICallModel.getMax_try()<MAX_RETRY_LIMIT){
                        JetEncryptorReInit jetEncryptorReInit=new JetEncryptorReInit(context);
                        networkAPICallModel.setMax_try(networkAPICallModel.getMax_try()+1);
                        jetEncryptorReInit.callApplicationWS(context,networkAPICallModel,networkAPIResponseCallback,NetworkAPICall.this);
                        Logger.e(TAG," JetEncryptorReInit:  ++++++++ : "+networkAPICallModel.getApiURL()+"   ++++++++ :"+networkAPICallModel.getMax_try());
                        return;
                    }*/
                    try {
                        if (encryptedRequest == null || encryptedRequest.length() == 0 || !JetEncryptor.getInstance().isInilized) {
//                            Netcore Disabled Log
                            Logger.e(
                                TAG,
                                " encryptedRequest API Name:  ++++++++ : " + networkAPICallModel.apiURL + "   ++++++++ :"
                            )
                            Logger.e(
                                TAG,
                                "\n  encryptedRequest jsonObject ++++++++ : $jsonObject"
                            )
                            Logger.e(
                                TAG,
                                " encryptedRequest==null || encryptedRequest.length()==0 " + encryptedRequest + " Encrypter status" + JetEncryptor.getInstance().isInilized
                            )
                            commonUtils.hideProgress()
                            if (networkAPICallModel.max_try < MAX_RETRY_LIMIT) {
                                val jetEncryptorReInit: JetEncryptorReInit =
                                    JetEncryptorReInit(context)
                                networkAPICallModel.max_try = networkAPICallModel.max_try + 1
                                jetEncryptorReInit.callApplicationWS(
                                    context, networkAPICallModel, networkAPIResponseCallback,
                                    this@NetworkAPICall
                                )
                                //Netcore Disabled Log
                                Logger.e(
                                    TAG,
                                    " JetEncryptorReInit:  ++++++++ : " + networkAPICallModel.apiURL + "   ++++++++ :" + networkAPICallModel.max_try
                                )
                            }
                            return
                        }
                        //Netcore Disabled Log
                        Logger.v(
                            TAG,
                            " API Name:  ++++++++ : " + networkAPICallModel.apiURL + "   ++++++++ :"
                        )
                        Logger.v(
                            TAG, """
 Volley jsonObject ++++++++ : ${jsonObject.toString()}"""
                        )
                        Logger.v(
                            TAG,
                            "\nVolley encryptedRequest ++++++++ : $encryptedRequest"
                        )
                    } catch (e: Exception) {
                        CommonUtils.printStackTrace(e)
                    }
                    //Netcore Disabled Log
                    Logger.i("HttpURL", userStatusUrl)
                    Logger.i("Encyrpted data", encryptedRequest.toString())
                    val stringRequest = CustomVolleyRequest(
                        networkAPICallModel.request_type,
                        userStatusUrl,
                        encryptedRequest,
                        Response.Listener<JSONObject> { response: JSONObject ->
                            try {
                                try {
                                    if (response.has("code") && response.getInt("code") == NetworkStatus.JET_ENCRYPTOR_ERROR) {
                                        if (networkAPICallModel.max_try < MAX_RETRY_LIMIT) {
                                            val jetEncryptorReInit: JetEncryptorReInit =
                                                JetEncryptorReInit(context)
                                            networkAPICallModel.max_try =
                                                networkAPICallModel.max_try + 1
                                            jetEncryptorReInit.callApplicationWS(
                                                context,
                                                networkAPICallModel,
                                                networkAPIResponseCallback,
                                                this@NetworkAPICall
                                            )
                                            //Netcore Disabled Log
                                            Logger.e(
                                                TAG,
                                                networkAPICallModel.apiURL + "\n NetworkStatus.JET_ENCRYPTOR_ERROR Volley UserCoinHistoryModel ++++++++ : \n" + response.toString()
                                            )
                                            return@Listener
                                        }
                                    }
                                    //Netcore Disabled Log
                                    Logger.v(
                                        TAG,
                                        networkAPICallModel.apiURL + "\n networkAPICallModel.getApiURL()Volley UserCoinHistoryModel ++++++++ : \n" + response.toString()
                                    )
                                } catch (e: Exception) {
                                    CommonUtils.printStackTrace(e)
                                }
                                commonUtils.hideProgress()
                                if (networkAPICallModel.parserType != null) {
                                    val `object`: Any = Gson().fromJson<Any>(
                                        response.toString(),
                                        networkAPICallModel.parserType
                                    ) //Todo
                                    networkAPICallModel.responseObject = `object`
                                }
                                if (networkAPIResponseCallback != null) networkAPIResponseCallback.onSuccessResponse(
                                    response,
                                    networkAPICallModel
                                )
                            } catch (e: Exception) {
                                if (networkAPIResponseCallback != null) networkAPIResponseCallback.onFailure(
                                    VolleyError(e),
                                    networkAPICallModel
                                )
                                CommonUtils.printStackTrace(e)
                                commonUtils.hideProgress()
                            }
                        },
                        { error: VolleyError? ->
                            try {
                                commonUtils.hideProgress()
                                if (error != null && error.javaClass == TimeoutError::class.java) {
                                    if (context != null) {
                                        if (networkAPICallModel.app_type != null && networkAPICallModel.app_type.equals(
                                                commonUtils.APP_TYPE_MOBILE,
                                                ignoreCase = true
                                            )
                                        ) {
                                            networkUtils.noInternetConnOrServerErrorDialog(
                                                context, context.getString(
                                                    R.string.slow_internet_connection_
                                                ), "", R.drawable.ic_slow_internet, null
                                            )
                                        } else {
                                            ToastUtils.onShowToast(
                                                context,
                                                R.drawable.ic_slow_internet,
                                                context.getString(
                                                    R.string.error
                                                ),
                                                context.getString(R.string.slow_internet_connection_),
                                                false
                                            )
                                        }
                                    }
                                } else {
                                    try {
                                        //Netcore Disabled Log
//                                    Logger.v(TAG, " API ERROR Name:  ++++++++ : " + networkAPICallModel.getApiURL() + "   ++++++++ :");
//                                    if (error != null && error.getLocalizedMessage() != null && !TextUtils.isEmpty(error.getLocalizedMessage())) {
//                                        Logger.v(TAG, " ERROR Name 1:  ++++++++ : " + error.getMessage() + "   ++++++++ :");
//                                        Logger.v(TAG, " ERROR Name 2:  ++++++++ : " + error.getLocalizedMessage() + "   ++++++++ :");
//                                    }
//                                    if (error != null && error.networkResponse != null && error.networkResponse.allHeaders != null) {
//                                        Logger.v(TAG, " ERROR Name 3:  ++++++++ : " + error.networkResponse.allHeaders + "   ++++++++ :");
//                                    }
//                                    if (error != null && error.networkResponse != null && error.networkResponse.data != null) {
//                                        Logger.v(TAG, " ERROR Name 4:  ++++++++ : " + error.networkResponse.data.length + "   ++++++++ :");
//                                        Logger.v(TAG, " ERROR Name 5:  ++++++++ : " + error.getStackTrace().toString() + "   ++++++++ :");
//                                    }
                                    } catch (e: Exception) {
                                        CommonUtils.printStackTrace(e)
                                    }
                                    if (networkAPICallModel.max_try_error < MAX_RETRY_LIMIT) {
                                        /* networkAPICallModel.setMax_try_error(networkAPICallModel.getMax_try_error()+1);
                                    Logger.v(TAG,"API ERROR RETRY:  ++++++++ : "+networkAPICallModel.getApiURL()+"   ++++++++ :");
                                  callApplicationWS(context,networkAPICallModel,networkAPIResponseCallback);*/
                                    } else if (networkAPIResponseCallback != null) {
                                        networkAPIResponseCallback.onFailure(
                                            error,
                                            networkAPICallModel
                                        )
                                    }

                                    try {
                                        if (context != null) {
                                            val activity = context
                                            if (activity != null) {
                                                val stringBuffer = StringBuffer(
                                                    """
                                                User Code: ${commonUtils.getUserCode(context)}
                                                Message: ${error!!.message}
                                                """.trimIndent()
                                                )
                                                val stringBuffer1 = StringBuffer(
                                                    """
                                                
                                                Encrypt Payload: ${networkAPICallModel.encryptedRequest}
                                                """.trimIndent()
                                                )
                                                stringBuffer1.append(
                                                    """
                                                
                                                Payload:${networkAPICallModel.jsonObjectRequest}
                                                """.trimIndent()
                                                )
                                                if (error != null) {
                                                    stringBuffer.append(
                                                        """
                                                    
                                                    Local Message: ${error.localizedMessage}
                                                    """.trimIndent()
                                                    )
                                                }
                                                if (error.networkResponse != null) {
                                                    stringBuffer.append(
                                                        """
                                                    
                                                    All Headers: ${error.networkResponse.allHeaders}
                                                    """.trimIndent()
                                                    )
                                                }
                                                if (error.networkResponse != null) {
                                                    stringBuffer.append(
                                                        """
                                                    
                                                    Headers: ${error.networkResponse.headers}
                                                    """.trimIndent()
                                                    )
                                                }
                                                stringBuffer.append(
                                                    """
                                                
                                                Device Information: ${
                                                        commonUtils.getDeviceAllInformation(
                                                            context
                                                        )
                                                    }
                                                """.trimIndent()
                                                )
                                                commonUtils.sendAPIErrorLogToServer(
                                                    context,
                                                    activity.localClassName,
                                                    userStatusUrl,
                                                    stringBuffer1.toString(),
                                                    "" + stringBuffer.toString()
                                                )
                                            }
                                        }
                                    } catch (e: Exception) {
                                        CommonUtils.printStackTrace(e)
                                        commonUtils.hideProgress()
                                    }
                                }
                            } catch (e: Exception) {
                                CommonUtils.printStackTrace(e)
                                commonUtils.hideProgress()
                            }
                        },
                        context,
                        networkAPICallModel.app_type
                    )
                    stringRequest.setRetryPolicy(
                        DefaultRetryPolicy(
                            networkAPICallModel.timeOut,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                        )
                    )
                    stringRequest.setShouldCache(false)
                    queue?.add(stringRequest)
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                    commonUtils.hideProgress()
                }
            } else {
                if (networkAPICallModel.app_type != null && networkAPICallModel.app_type.equals(
                        commonUtils.APP_TYPE_MOBILE,
                        ignoreCase = true
                    )
                ) {
                    networkUtils.noInternetConnOrServerErrorDialog(context,
                        context.getString(R.string.no_internet_connection_),
                        context.getString(R.string.tap_to_retry),
                        R.drawable.ic_no_internet,
                        object : DialogCallListener {
                            override fun positiveButtonClick() {
                                callApplicationWS(
                                    context,
                                    networkAPICallModel,
                                    networkAPIResponseCallback
                                )
                            }

                            override fun negativeButtonClick() {
                            }
                        })
                } else {
                    ToastUtils.onShowToast(
                        context,
                        R.drawable.ic_no_internet,
                        context.getString(R.string.error),
                        context.getString(
                            R.string.no_internet_connection_
                        ),
                        false
                    )
                }
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
            networkAPICallModel.commonUtils.hideProgress()
        }
    }

    companion object {
        private val TAG = NetworkAPICall::class.java.simpleName + " http"
    }
}
