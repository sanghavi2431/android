package `in`.woloo.www.networksUtils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.NetworkResponse
import com.android.volley.Response
import com.android.volley.ServerError
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.jetsynthesys.encryptor.JetEncryptor
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings.Companion.getPreferences
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.Logger
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException

class CustomVolleyRequest : JsonObjectRequest {
    private var mListener: Response.Listener<JSONObject>
    private var mErrorListener: Response.ErrorListener
    var jsonObject: JSONObject?
    var gson: Gson? = null
    private var mContext: Context? = null
    private var mobileTv: String? = null

    //private Utility mUtility;
    private var jetEncryptor: JetEncryptor? = null
    private val TAG: String = CustomVolleyRequest::class.java.simpleName

    constructor(
        method: Int,
        url: String?,
        jsonObject: JSONObject?,
        listener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener,
        context: Context?,
        mobileTv: String?
    ) : super(method, url, jsonObject, listener, errorListener) {
        //  this.mUtility = new Utility();
        this.mListener = listener
        this.mErrorListener = errorListener
        this.jsonObject = jsonObject
        mContext = context
        this.mobileTv = mobileTv
        if (gson == null) {
            gson = Gson()
        }
    }

    constructor(
        url: String?,
        jsonObject: JSONObject?,
        listener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ) : super(url, jsonObject, listener, errorListener) {
        this.mListener = listener
        this.mErrorListener = errorListener
        this.jsonObject = jsonObject
        if (gson == null) {
            gson = Gson()
        }
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<JSONObject> {
        /* try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return UserCoinHistoryModel.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return UserCoinHistoryModel.error(new ParseError(e));
        } catch (JSONException je) {
            return UserCoinHistoryModel.error(new ParseError(je));
        }*/
        return super.parseNetworkResponse(response)
    }

    override fun getBodyContentType(): String {
        return "application/json"
    }

    override fun deliverResponse(response: JSONObject) {
        mListener.onResponse(response)
    }

    override fun parseNetworkError(volleyError: VolleyError): VolleyError {
        try {
            val commonUtils = CommonUtils()

            try {
                if (mContext != null && mContext is Activity) {
                    (mContext as Activity).runOnUiThread { }
                }
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            val response = volleyError.networkResponse
            //Netcore Disabled Log
//            Logger.e(TAG,"deliverError:: "+volleyError.getLocalizedMessage());
//            Logger.e(TAG,"deliverError:: "+volleyError.getMessage());
//            Logger.e(TAG,"deliverError:: "+volleyError.getCause());
//            Logger.e(TAG,"deliverError:: "+volleyError.getNetworkTimeMs());
//            Logger.e(TAG,"deliverError:: "+volleyError.getSuppressed());
//            Logger.e(TAG,"deliverError Header:: "+response.allHeaders);
//            Logger.e(TAG,"deliverError Status Code:: "+response.statusCode);
            if (volleyError is ServerError && response != null) {
                try {
                    //Netcore Disabled Log
//                    Logger.e(TAG,"deliverError 1: "+volleyError.getMessage());
                    val res = String(
                        response.data,
                        charset(HttpHeaderParser.parseCharset(response.headers, "utf-8"))
                    )
                    // Now you can use any deserializer to make sense of data
                    val obj = JSONObject(res)
                    //Netcore Disabled Log
//                    Logger.e(TAG,"deliverError 2: "+obj);
                } catch (e1: UnsupportedEncodingException) {
                    // Couldn't properly decode data to string
                    CommonUtils.printStackTrace(e1)
                } catch (e2: JSONException) {
                    // returned data is not JSONObject?
                    CommonUtils.printStackTrace(e2)
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
        return super.parseNetworkError(volleyError)
    }

    override fun deliverError(error: VolleyError) {
        if (error is TimeoutError) {
            if (mContext != null) {
                Toast.makeText(
                    mContext,
                    mContext!!.resources.getString(R.string.slow_internet_connection_),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        // As of f605da3 the following should work
        val response = error.networkResponse
        if (error is ServerError && response != null) {
            try {
                val res = String(
                    response.data,
                    charset(HttpHeaderParser.parseCharset(response.headers, "utf-8"))
                )
                // Now you can use any deserializer to make sense of data
                val obj = JSONObject(res)
                Logger.e(TAG, "deliverError: $obj")
            } catch (e1: UnsupportedEncodingException) {
                // Couldn't properly decode data to string
                CommonUtils.printStackTrace(e1)
            } catch (e2: JSONException) {
                // returned data is not JSONObject?
                CommonUtils.printStackTrace(e2)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        }
        mErrorListener.onErrorResponse(error)
    }

    @Throws(AuthFailureError::class)
    override fun getHeaders(): Map<String, String> {
        val headers = HashMap<String, String>()
        try {
            jetEncryptor = JetEncryptor.getInstance()
            // headers.put("Authorization", "Bearer " + jetEncryptor.getJwtkey());
            if (CommonUtils().isLoggedIn) {
                val userInfo = CommonUtils().userInfo
                headers["Authorization"] =
                    "Bearer " + getPreferences.fetchToken()
            } else {
                headers["Authorization"] = "Bearer " + jetEncryptor!!.getJwtkey()
            }
            /*  headers.put("Content-Type", "application/json");
            headers.put("Accept", "application/json");*/
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
        return headers
    }
}
