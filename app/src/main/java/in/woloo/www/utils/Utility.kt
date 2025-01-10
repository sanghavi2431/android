package `in`.woloo.www.utils

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.android.installreferrer.api.InstallReferrerClient
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.reflect.TypeToken
import com.jetsynthesys.encryptor.JetEncryptor
import com.netcore.android.Smartech
import `in`.woloo.www.application_kotlin.api_classes.APIConstants
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPICallModel
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.models.EditProfileResponse
import `in`.woloo.www.networksUtils.NetworkAPICall
import org.json.JSONException
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Utility {
    var referrerClient: InstallReferrerClient? = null
    @JvmStatic
    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard(context: Activity?) {
        if (context == null) return
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    @JvmStatic
    fun logFirebaseEvent(context: Context?, bundle: Bundle, event_name: String?) {
        val (id) = CommonUtils().userInfo!!
        bundle.putString(AppConstants.USER_ID, id.toString())
        bundle.putString(AppConstants.DEVICE_PLATFORM, "Android")
        bundle.putString(AppConstants.CURRENT_DATE, currentDate)
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
        mFirebaseAnalytics.logEvent(event_name!!, bundle)
        userJourney(context, bundle, event_name)
    }

    @JvmStatic
    fun logNetcoreEvent(context: Context, payload: HashMap<String, Any>, event_name: String?) {
        val (id) = CommonUtils().userInfo!!
        payload[AppConstants.USER_ID] = id.toString()
        payload[AppConstants.DEVICE_PLATFORM] = "Android"
        payload[AppConstants.CURRENT_DATE] = currentDate
        Logger.i("Netcore", payload.toString())
        Smartech.getInstance(WeakReference(context)).trackEvent(event_name, payload)
    }



    fun userJourney(context: Context?, bundle: Bundle?, eventName: String?) {
        val mNetworkAPICall = NetworkAPICall()
        val mJsObjParam = JSONObject()
        try {
            mJsObjParam.put("event_name", eventName)
            if (bundle != null && bundle.size() > 0) {
                val eventData = JSONObject()
                for (key in bundle.keySet()) {
                    eventData.put(key, bundle[key])
                }
                mJsObjParam.put("event_data", eventData)
            }
            Logger.i("Utility", mJsObjParam.toString())
        } catch (e: JSONException) {
            CommonUtils.printStackTrace(e)
        }
        val mJetEncryptor = JetEncryptor.getInstance()
        val parserType = object : TypeToken<EditProfileResponse?>() {}.type
        val networkAPICallModel = NetworkAPICallModel(
            APIConstants.USER_JOURNEY,
            AppConstants.POST_REQUEST,
            AppConstants.APP_TYPE_MOBILE,
            mJsObjParam,
            mJetEncryptor
        )
        networkAPICallModel.isShowProgress = true
        networkAPICallModel.parserType = parserType
        (context as Activity?)?.let { mNetworkAPICall.callApplicationWS(it, networkAPICallModel, null) }
    }

    fun logFirebaseMobileEvent(
        context: Context?,
        bundle: Bundle,
        event_name: String?,
        mobile: String?
    ) {
        bundle.putString(AppConstants.MOBILE, mobile)
        bundle.putString(AppConstants.DEVICE_PLATFORM, "Android")
        bundle.putString(AppConstants.CURRENT_DATE, currentDate)
        val mFirebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
        mFirebaseAnalytics.logEvent(event_name!!, bundle)
    }

    val currentDate: String
        get() {
            val timestampMilliseconds = System.currentTimeMillis()
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val stringDate = simpleDateFormat.format(Date(timestampMilliseconds))
            println(stringDate)
            return stringDate
        }


}
