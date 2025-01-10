package `in`.woloo.www.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.android.volley.DefaultRetryPolicy
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.google.gson.Gson
import com.jetsynthesys.encryptor.JetEncryptor
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.application_kotlin.api_classes.APIConstants
import `in`.woloo.www.application_kotlin.api_classes.ImageMultipartRequest
import `in`.woloo.www.application_kotlin.api_classes.JSONTagConstant
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPICallModel
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPIResponseCallback
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.models.FileUploadResponse
import `in`.woloo.www.networksUtils.NetworkAPICall
import `in`.woloo.www.networksUtils.VolleySingleton
import `in`.woloo.www.networksUtils.VolleySingleton.Companion.getInstance
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.UnsupportedEncodingException
import java.text.SimpleDateFormat
import java.util.Date

class ProfileAPIUtil(
    private val context: Context,
    networkAPIResponseCallback: NetworkAPIResponseCallback
) :
    NetworkAPIResponseCallback {
    private val commonUtils = CommonUtils()
    private val mNetworkAPICall: NetworkAPICall = NetworkAPICall()
    private val networkAPIResponseCallback: NetworkAPIResponseCallback = networkAPIResponseCallback


    fun updateUserProfile(
        activity: Activity,
        bitmap: Bitmap?,
        mJetEncryptor: JetEncryptor,
        iOnCallWSCallBack: IOnCallWSCallBack,
        type: String?
    ) {
        try {
            commonUtils.showProgress(context)
            val queue: RequestQueue =
                VolleySingleton.getInstance(activity.applicationContext).requestQueue!!
            val updateUserUrl = BuildConfig.BASE_URL + APIConstants.FILE_UPLOAD
            val multipartRequest: ImageMultipartRequest = object : ImageMultipartRequest(
                Request.Method.POST,
                updateUserUrl,
                Response.Listener<NetworkResponse> { response ->
                    var jsonString: String? = null
                    try {
                        jsonString = String(
                            response.data,
                            charset(HttpHeaderParser.parseCharset(response.headers))
                        )
                        try {
                            jsonString = String(
                                jsonString.toByteArray(charset("ISO-8859-1")),
                                charset("UTF-8")
                            )
                        } catch (e: UnsupportedEncodingException) {
                            CommonUtils.printStackTrace(e)
                        }
                        val fileUploadResponse: FileUploadResponse =
                            Gson().fromJson<FileUploadResponse>(
                                jsonString,
                                FileUploadResponse::class.java
                            )
                        commonUtils.hideProgress()
                        iOnCallWSCallBack.onSuccessResponse(fileUploadResponse)

                        /*if(bitmap != null && !bitmap.isRecycled()){
                                       bitmap.recycle();
                                    }*/
                    } catch (e: Exception) {
                        CommonUtils.printStackTrace(e)
                        commonUtils.hideProgress()
                    }
                }, Response.ErrorListener { error ->
                    try {
                        if (error.networkResponse == null) {
                            if (error.javaClass == TimeoutError::class.java) {
                                if (activity != null) {
                                    iOnCallWSCallBack.onFailure(error)
                                }
                            }
                        }
                        commonUtils.hideProgress()
                    } catch (e: Exception) {
                        CommonUtils.printStackTrace(e)
                        commonUtils.hideProgress()
                    }
                    CommonUtils.printStackTrace(error)
                }, context
            ) {
                val params1: Map<String, String>?
                    get() {
                        val jsonObject = JSONObject()
                        var encryptedRequest: Map<String, String>? = null
                        try {
                            jsonObject.put(JSONTagConstant.TYPE, type)
                            jsonObject.put(
                                JSONTagConstant.LOCALE, CommonUtils.getCustomLocale(
                                    context, commonUtils.APP_TYPE_MOBILE
                                )
                            )
                            encryptedRequest =
                                getEncryptedMultipartRequest(activity, jsonObject, mJetEncryptor)
                        } catch (e: Exception) {
                            CommonUtils.printStackTrace(e)
                            commonUtils.hideProgress()
                        }
                        return encryptedRequest
                    }

                override val byteData: Map<String, DataPart>
                    get() {
                        val params: MutableMap<String, DataPart> = HashMap<String, DataPart>()
                        var drawable: Drawable? = null
                        if (bitmap != null) {
                            drawable = BitmapDrawable(activity.resources, bitmap)
                            val dateformat = SimpleDateFormat("MMddyyyyhhmmss")
                            val date = Date()
                            val fileName = dateformat.format(date) + ".jpg"
                            if (drawable != null) {
                                params["filenames"] = DataPart(
                                    fileName, getFileDataFromDrawable(drawable),
                                    "image/jpeg"
                                )
                            }
                        }
                        return params
                    }
            }
            multipartRequest.setRetryPolicy(
                DefaultRetryPolicy(
                    AppConstants.TIME_OUT_EXCEPTION_TIME,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
            )
            multipartRequest.setShouldCache(false)
            //Adding request to the queue
            queue.add<NetworkResponse>(multipartRequest)
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
            commonUtils.hideProgress()
        }
    }

    fun getEncryptedMultipartRequest(
        context: Activity,
        postParamsObject: JSONObject,
        mJetEncryptor: JetEncryptor
    ): Map<String, String> {
        val params: MutableMap<String, String> = HashMap()
        var postParamsObjectEnc: JSONObject? = null
        try {
            val localeObject =
                CommonUtils.getCustomLocale(
                    context.applicationContext,
                    AppConstants.APP_TYPE_MOBILE
                )

            val postParamsObjectStr =
                mJetEncryptor.processData(context, postParamsObject.toString())
            postParamsObjectEnc = JSONObject(postParamsObjectStr)

            params["param1"] = postParamsObjectEnc.getString("param1")
            params["param2"] = postParamsObjectEnc.getString("param2")
            params["param3"] = postParamsObjectEnc.getString("param3")
            params["locale[country]"] = localeObject.getString("country")
            params["locale[language]"] = localeObject.getString("language")
            params["locale[platform]"] = localeObject.getString("platform")
            params["locale[version]"] = localeObject.getString("version")
            params["locale[segment]"] = localeObject.getString("segment")
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }

        return params
    }

    private fun getFileDataFromDrawable(drawable: Drawable): ByteArray {
        val bitmap = (drawable as BitmapDrawable).bitmap
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
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

    override fun onSuccessResponse(
        response: JSONObject?,
        networkAPICallModel: NetworkAPICallModel?
    ) {
        when (networkAPICallModel!!.apiURL) {
            APIConstants.FILE_UPLOAD -> //                networkAPIResponseCallback.onSuccessResponse(response,networkAPICallModel);
                try {
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
        }
    }
}
