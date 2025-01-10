package `in`.woloo.www.common

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.common.util.Base64Utils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.jetsynthesys.encryptor.JetEncryptor
import com.jetsynthesys.encryptor.JetEncryptorJava
import com.netcore.android.Smartech
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.APIConstants
import `in`.woloo.www.application_kotlin.api_classes.JSONTagConstant
import `in`.woloo.www.application_kotlin.database.IntTypeAdapter
import `in`.woloo.www.application_kotlin.database.IpBean
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings.Companion.getPreferences
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.model.lists_models.UserDetails
import `in`.woloo.www.application_kotlin.model.server_response.AuthConfigResponse
import `in`.woloo.www.application_kotlin.model.server_response.LoginResponse
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.more.fragments.EnterMessageFragment
import `in`.woloo.www.more.giftcard.mvp.GiftCardView
import `in`.woloo.www.more.subscribe.razorpay.RazorPayActivity
import `in`.woloo.www.networksUtils.CustomVolleyRequest
import `in`.woloo.www.networksUtils.VolleySingleton
import `in`.woloo.www.networksUtils.VolleySingleton.Companion.getInstance
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.DeepLinkRequestModel
import `in`.woloo.www.utils.Logger.i
import `in`.woloo.www.utils.Logger.v
import `in`.woloo.www.utils.ProgressBarUtils
import io.hansel.hanselsdk.Hansel
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable
import java.lang.ref.WeakReference
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CommonUtils : Serializable {
    private var dialog_progress: Dialog? = null
    var APP_TYPE_MOBILE: String = "mobile"
    var APP_TYPE_TV: String = "tv"

    /*calling  getDeviceId*/
    @SuppressLint("all")
    fun getDeviceId(context: Context): String {
        i(TAG, "getDeviceId")
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /*calling  getEncryptedJsonRequest*/
    fun getEncryptedJsonRequest(
        context: Activity,
        postParamsObject: JSONObject?,
        mJetEncryptor: JetEncryptor?,
        app_type: String?
    ): JSONObject? {
        var postParamsObjectEnc: JSONObject? = JSONObject()
        i(TAG, "getEncryptedJsonRequest")
        try {
            val localeObject =
                getCustomLocale(
                    context.applicationContext,
                    app_type
                )
            if (postParamsObject != null && postParamsObject.length() > 0) {
                v(
                    TAG,
                    "postParamsObject: $postParamsObject"
                )
                if (mJetEncryptor != null) {
                    try {
                        val jetEncryptorJava = JetEncryptorJava()
                        val postParamsObjectStr =
                            jetEncryptorJava.processData(context, postParamsObject.toString())
                        if (postParamsObjectStr != null) {
                            postParamsObjectEnc = JSONObject(postParamsObjectStr)
                            postParamsObjectEnc.put(JSONTagConstant.LOCALE, localeObject)
                        }
                    } catch (e: Exception) {
                        printStackTrace(e)
                        postParamsObjectEnc = null
                    }
                }
            }
        } catch (e: Exception) {
            postParamsObjectEnc = null
            printStackTrace(e)
        }
        return postParamsObjectEnc
    }

    /*calling  getDeviceAllInformation*/
    fun getDeviceAllInformation(context: Context): String {
        i(TAG, "getDeviceAllInformation")
        val stringBuffer = StringBuffer()
        stringBuffer.append("Device ID: " + getDeviceId(context))
        stringBuffer.append(
            """
                
                Device Make: ${deviceMake}
                """.trimIndent()
        )
        stringBuffer.append(
            """
                
                Device Model: ${deviceModel}
                """.trimIndent()
        )
        stringBuffer.append(
            """
                
                Device OS: ${deviceOs}
                """.trimIndent()
        )

        return stringBuffer.toString()
    }

    val deviceMake: String
        /*calling  getDeviceMake*/
        get() {
            i(
                TAG,
                "getDeviceMake"
            )
            return Build.MANUFACTURER
        }

    val deviceModel: String
        /*calling  getDeviceModel*/
        get() {
            i(
                TAG,
                "getDeviceModel"
            )
            return (Build.MANUFACTURER + ": " + Build.MODEL)
        }

    val deviceOs: String
        /*calling  getDeviceOs*/
        get() {
            i(
                TAG,
                "getDeviceOs"
            )
            val builder = StringBuilder()
            builder.append("Android : ").append(Build.VERSION.RELEASE)

            val fields =
                Build.VERSION_CODES::class.java.fields
            for (field in fields) {
                val fieldName = field.name
                var fieldValue = -1

                try {
                    fieldValue = field.getInt(Any())
                } catch (e: IllegalArgumentException) {
                    printStackTrace(e)
                } catch (e: IllegalAccessException) {
                    printStackTrace(e)
                } catch (e: NullPointerException) {
                    printStackTrace(e)
                }

                if (fieldValue == Build.VERSION.SDK_INT) {
                    builder.append(" : ").append(fieldName).append(" : ")
                    builder.append("sdk=").append(fieldValue)
                }
            }

            return builder.toString()
        }

    /*calling  sendAPIErrorLogToServer*/
    fun sendAPIErrorLogToServer(
        context: Context,
        screenName: String,
        requestName: String,
        inputRequest: String,
        outputResponse: String
    ) {
        try {
            i(TAG, "sendAPIErrorLogToServer")
            //if (BuildConfig.LIVE_URL.equalsIgnoreCase("3")) {
            Log(
                "SCREEN NAME " + screenName +
                        " REQUEST NAME " + requestName + " INPUT " + inputRequest + " OUT " + outputResponse
            )

            val queue: RequestQueue = VolleySingleton.getInstance(context).requestQueue!!
            // showProgressDialog();
            // showProgress(context);
            val jsonObjectParams = JSONObject()
            try {
                jsonObjectParams.put("request", "rescue@sendErrorReport")
                jsonObjectParams.put("screen_name", screenName)
                jsonObjectParams.put("request_name", requestName)
                jsonObjectParams.put("input_request", inputRequest.toString())
                jsonObjectParams.put("output_responce", outputResponse.toString())
                jsonObjectParams.put("platform", "android")
                val userCode = ""
                if (userCode != null && !userCode.isEmpty()) jsonObjectParams.put(
                    "user_code",
                    userCode
                )
                jsonObjectParams.put("application_name", "Iconocale")
            } catch (e: JSONException) {
                printStackTrace(e)
            }

            val stringRequest: CustomVolleyRequest = CustomVolleyRequest(
                AppConstants.POST_REQUEST, BuildConfig.RESCUE_API_URL, jsonObjectParams,
                Response.Listener<JSONObject> { response: JSONObject ->
                    Log(
                        "FALLBACK API SUCCESS $response"
                    )
                },
                Response.ErrorListener { error: VolleyError ->
                    try {
                        Log("FALLBACK API ERROR $error")
                    } catch (e: Exception) {
                        printStackTrace(e)
                    }
                }, context, APP_TYPE_MOBILE
            )
            stringRequest.setRetryPolicy(
                DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
            )
            stringRequest.setShouldCache(false)
            queue.add<JSONObject>(stringRequest)

            /*} else {
//            Toast.makeText(context, "FALLBACK API FAILURE", Toast.LENGTH_SHORT).show();
            }*/
        } catch (e: Exception) {
            printStackTrace(e)
        }
    }

    /*calling  showProgress*/
    fun showProgress(context: Context) {
        try {
            i(TAG, "showProgress")
            if (dialog_progress == null) {
                initProgress(context)
            }
        } catch (e: Exception) {
            printStackTrace(e)
        }

        try {
            if (!dialog_progress!!.isShowing && !(context as Activity).isFinishing) {
                dialog_progress!!.show()
            }
        } catch (e: Exception) {
            printStackTrace(e)
        }
    }

    /*calling  initProgress*/
    private fun initProgress(context: Context) {
        try {
            i(TAG, "initProgress")
            dialog_progress = Dialog(context, R.style.CustomDialogTime)
            dialog_progress!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog_progress!!.window!!.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        context,
                        android.R.color.transparent
                    )
                )
            )
            dialog_progress!!.setCancelable(false)
            dialog_progress!!.setContentView(R.layout.dialog_progress_overlay)
            //            dialog_progress.getWindow().getDecorView().setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        } catch (e: Exception) {
            printStackTrace(e)
        }
    }

    /*calling  hideProgress*/
    fun hideProgress() {
        try {
            i(TAG, "hideProgress")
            if (dialog_progress != null) {
                if (dialog_progress!!.isShowing) dialog_progress!!.dismiss()
            }
        } catch (e: Exception) {
            printStackTrace(e)
        }
    }

    /*calling  getUserCode*/
    fun getUserCode(context: Context?): String? {
        i(TAG, "getUserCode")
        val mSharedPref: SharedPreference = SharedPreference(context)
        return mSharedPref.getStoredPreference(
            context,
            SharedPreferencesEnum.USER_CODE.getPreferenceKey()
        )
    }

    /*calling  getSuperStoreId*/
    fun getSuperStoreId(context: Context?): String {
        i(TAG, "getSuperStoreId")
        val mSharedPref: SharedPreference = SharedPreference(context)
        val superStoreId = "639"
        try {
//            superStoreId = mSharedPref.getStoredPreference(context, SharedPreferencesEnum.SUPERSTORE_ID.getPreferenceKey(), "0");
        } catch (e: Exception) {
            printStackTrace(e)
        }
        return superStoreId
    }

    /*calling  getMobileTvType*/
    fun getMobileTvType(context: Context?): String {
        var context = context
        i(TAG, "getMobileTvType")
        if (context == null) {
            context = WolooApplication.instance!!
        }
        var type = "tv"
        try {
            val uiModeManager = context!!.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
            type =
                if (uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_NORMAL) {
                    //   Toast.makeText(context, "TELEVISION", Toast.LENGTH_SHORT).show();
                    "mobile"
                } else {
                    // Toast.makeText(context, "MOBILE", Toast.LENGTH_SHORT).show();
                    "tv"
                }
        } catch (e: Exception) {
            printStackTrace(e)
        }
        //        type = "tv";
        return type
    }

    /*calling  isLoggedIn*/
    fun isLoggedIn(context: Context?): Boolean {
        i(TAG, "isLoggedIn")
        try {
            val mSharedPref: SharedPreference = SharedPreference(context)
            val isLoggedIn: Boolean = mSharedPref.getStoredBooleanPreference(
                context,
                SharedPreferencesEnum.IS_LOGGED_IN.getPreferenceKey(),
                false
            )
            return isLoggedIn
        } catch (ex: Exception) {
            printStackTrace(ex)
        }
        return false
    }

    val isLoggedIn: Boolean
        get() = getPreferences.fetchIsLoggedIn()

    val referralCode: String?
        get() = getPreferences.fetchReferralCode()

    /*calling  getUserInfo*/
    fun getUserInfo(context: Context?): LoginResponse? {
        try {
            i(TAG, "getUserInfo")
            val mSharedPref: SharedPreference = SharedPreference(context)
            val loginInfo: String = mSharedPref.getStoredPreference(
                context,
                SharedPreferencesEnum.USER_INFO.getPreferenceKey()
            ).toString()
            val userInfo: LoginResponse = Gson().fromJson<LoginResponse>(
                loginInfo,
                LoginResponse::class.java
            )
            return userInfo
        } catch (ex: Exception) {
            printStackTrace(ex)
        }
        return null
    }


    var userInfo: UserDetails? = null
        get() {
            try {
                val user =
                    getPreferences.fetchUserDetails()
                return user
            } catch (ex: Exception) {
                printStackTrace(ex)
            }
            return null
        }

    /*calling  clearApplicationData*/
    fun clearApplicationData(context: Context) {
        try {
            i(TAG, "clearApplicationData")
            val mSharedPreference: SharedPreference = SharedPreference(context)
            mSharedPreference.removeOnClearAppData(
                context,
                SharedPreferencesEnum.IS_LOGGED_IN.getPreferenceKey()
            )
            mSharedPreference.removeOnClearAppData(
                context,
                SharedPreferencesEnum.USER_INFO.getPreferenceKey()
            )
            mSharedPreference.removeAllUserData(context)
            getPreferences.clear()
            Smartech.getInstance(WeakReference(context)).logoutAndClearUserIdentity(true)
            Hansel.getUser().clear()
        } catch (e: Exception) {
            printStackTrace(e)
        }
    }

    /*calling  getFirstLaterCaps*/
    fun getFirstLaterCaps(data: String): String? {
        i(TAG, "getFirstLaterCaps")
        var upperString = data
        try {
            upperString =
                data.substring(0, 1).uppercase(Locale.getDefault()) + data.substring(1).lowercase(
                    Locale.getDefault()
                )
        } catch (e: Exception) {
            printStackTrace(e)
        }

        return upperString
    }

    /*calling  getDeviceToken*/
    fun getDeviceToken(context: Context?): String? {
        try {
            i(TAG, "getDeviceToken")
            val mSharedPref: SharedPreference = SharedPreference(context)
            return mSharedPref.getStoredPreference(
                context,
                SharedPreferencesEnum.PUSH_TOKEN.getPreferenceKey()
            )
        } catch (ex: Exception) {
            printStackTrace(ex)
            return ""
        }
    }

    val nonce: ByteArray
        /*calling  getNonce*/
        get() {
            i(
                TAG,
                "getNonce"
            )
            val nonce = ByteArray(16)
            var secureRandomGenerator: SecureRandom? = null
            try {
                secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG")
                secureRandomGenerator.nextBytes(nonce)
            } catch (e: NoSuchAlgorithmException) {
                printStackTrace(e)
            }
            return nonce
        }

    /*calling  showPlayServiceUpdateDialog*/
    fun showPlayServiceUpdateDialog(activity: Activity) {
        try {
            i(TAG, "showPlayServiceUpdateDialog")
            AlertDialog.Builder(activity, R.style.MyDialogTheme)
                .setTitle(activity.applicationContext.getString(R.string.lbl_update_play_message))
                .setCancelable(false)
                .setNegativeButton(
                    activity.applicationContext.getString(R.string.exit_app)
                ) { dialogInterface, i -> // User chose NO
                    activity.finish()
                    System.exit(0)
                }.create().show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
            //             CommonUtils.printStackTrace(e);
        }
    }


    /*calling  showNotAuthorizedDialog*/
    fun showNotAuthorizedDialog(activity: Activity) {
        try {
            i(TAG, "showNotAuthorizedDialog")
            AlertDialog.Builder(activity, R.style.MyDialogTheme)
                .setTitle(activity.applicationContext.getString(R.string.message_not_authorize))
                .setCancelable(false)
                .setNegativeButton(
                    activity.applicationContext.getString(R.string.exit_app)
                ) { dialogInterface, i -> // User chose NO
                    activity.finish()
                    System.exit(0)
                }.create().show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
            //             CommonUtils.printStackTrace(e);
        }
    }


    /*calling  getBase64Encoded*/
    fun getBase64Encoded(original_string: String): String {
        i(TAG, "getBase64Encoded")
        var base64encodedString = ""
        try {
            base64encodedString =
                Base64Utils.encodeUrlSafe(original_string.toByteArray(charset("ISO-8859-1")))
            println("Base64 Encoded String :$base64encodedString")
        } catch (e: Exception) {
            printStackTrace(e)
        }

        return base64encodedString
    }

    /*calling  getBase64Decoded*/
    fun getBase64Decoded(encoded_string: String): String {
        i(TAG, "getBase64Decoded")
        var base64encodedString = ""
        var base64encodedBytes: ByteArray? = null
        try {
            base64encodedBytes = Base64Utils.decodeUrlSafe(encoded_string)
            base64encodedString = String(base64encodedBytes, charset("UTF-8"))
            base64encodedString =
                String(base64encodedString.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))
            base64encodedString =
                base64encodedString.replace("\\?".toRegex(), "").toString().trim { it <= ' ' }
            println("Base64 Decoded String :$base64encodedString")
        } catch (e: Exception) {
            printStackTrace(e)
        }

        return base64encodedString
    }

    /*calling  getListOfInstalledApps*/ //this function is used to get installed application
    fun getListOfInstalledApps(context: Context): List<String> {
        i(TAG, "getListOfInstalledApps")
        val pm = context.packageManager
        //get a list of installed apps.
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        val appNames: MutableList<String> = ArrayList()
        for (packageInfo in packages) {
            appNames.add(packageInfo.packageName)
        }
        return appNames
    }

    companion object {
        private val TAG: String = CommonUtils::class.java.simpleName

        /*calling  getCustomLocale*/
        fun getCustomLocale(context: Context?, APP_TYPE: String?): JSONObject {
            var context = context
            i(TAG, "getCustomLocale")
            if (context == null) {
                context = WolooApplication.instance!!
            }
            val mSharedPreference: SharedPreference = SharedPreference(context)
            val mStrLocale: String = mSharedPreference.getStoredPreference(
                context,
                SharedPreferencesEnum.IP_TO_LOCALE.getPreferenceKey()
            ).toString()

            //        String mStrLocale = mSharedPreference.getProperty(context, SharedPreferencesEnum.IP_TO_LOCALE.getPreferenceKey());
//        String locale = mStrLocale.getData();
            val gson = Gson()
            val ipBean: IpBean = gson.fromJson<IpBean>(mStrLocale, IpBean::class.java)
            var versions: IpBean.Data.Versions? = null
            if (ipBean != null && ipBean.data != null) {
                versions = ipBean.data.versions
            }

            val jsonObject = JSONObject()

            var platform = "android"
            platform = AppConstants.PLATFORM_ANDROID
            try {
                if (mStrLocale != null) {
                    //JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put(JSONTagConstant.VERSION, BuildConfig.VERSION_NAME)
                        jsonObject.put(JSONTagConstant.PLATFORM, platform)
                        // jsonObject.put("language", mSharedPreference.getStoredPreference(context, SharedPreferencesEnum.LANGUAGE_CODE.getPreferenceKey(), "EN"));
                        if (versions!!.countryCode == null || versions.countryCode.equals(
                                "-",
                                ignoreCase = true
                            )
                        ) {
//                        jsonObject.put(JSONTagConstant.COUNTRY, "GB");
                            jsonObject.put(JSONTagConstant.COUNTRY, "IN")
                        } else {
                            jsonObject.put(JSONTagConstant.COUNTRY, versions.countryCode)
                            // jsonObject.put(JSONTagConstant.COUNTRY, "GB");
//                        jsonObject.put(JSONTagConstant.COUNTRY, "IN");
                        }
                        jsonObject.put(JSONTagConstant.SEGMENT, "")
                    } catch (e: JSONException) {
                        printStackTrace(e)
                    }
                } else {
                    // JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put(JSONTagConstant.VERSION, BuildConfig.VERSION_NAME)
                        jsonObject.put(JSONTagConstant.PLATFORM, platform)
                        jsonObject.put(JSONTagConstant.LANGUAGE, "en")
                        //jsonObject.put("language", mSharedPreference.getStoredPreference(context, SharedPreferencesEnum.LANGUAGE_CODE.getPreferenceKey(), "EN"));
                        // jsonObject.put("country", "IN");
//                    jsonObject.put(JSONTagConstant.COUNTRY, "GB");
                        jsonObject.put(JSONTagConstant.COUNTRY, "IN")
                        jsonObject.put(JSONTagConstant.SEGMENT, "")
                    } catch (e: JSONException) {
                        printStackTrace(e)
                    }
                }
                val mApplicationConstant: AppConstants = `in`.woloo.www.utils.AppConstants
                jsonObject.put("language", "en")
            } catch (e: Exception) {
                printStackTrace(e)
            }
            return jsonObject
        }

        /*calling  printStackTrace*/
        @JvmStatic
        fun printStackTrace(e: Exception) {
            e.printStackTrace()
            if (!(BuildConfig.LIVE_URL.equals("3", ignoreCase = true) && !BuildConfig.DEBUG)) {
                e.printStackTrace()
                i(TAG, "printStackTrace " + e.message)
            }
        }

        /*calling  Log*/
        fun Log(values: String) {
            i(TAG, "Log")
            i("CommonUtils $values", "" + values)
        }

        val gson: Gson
            /*calling  getGson*/
            get() {
                i(
                    TAG,
                    "getGson"
                )
                val gson = GsonBuilder()
                    .registerTypeAdapter(Int::class.javaPrimitiveType, IntTypeAdapter())
                    .registerTypeAdapter(Int::class.java, IntTypeAdapter()).create()
                return gson
            }


        /*calling  hideKeyboard*/
        @JvmStatic
        fun hideKeyboard(activity: Activity) {
            i(TAG, "hideKeyboard")
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }

        /*calling  isValidMobileNumber*/
        fun isValidMobileNumber(mobile_number: String): Boolean {
            try {
                i(TAG, "isValidMobileNumber")
                // if(!TextUtils.isEmpty(mobile_number) && TextUtils.isDigitsOnly(mobile_number) && (mobile_number.length() >= 10 && mobile_number.length() < 13)){
                if (!TextUtils.isEmpty(mobile_number) && TextUtils.isDigitsOnly(mobile_number) && (mobile_number.length == 10)) {
                    return true
                }
                return false
            } catch (ex: Exception) {
                printStackTrace(ex)
            }
            return false
        }

        /*calling  getDistace*/
        fun getDistace(distance: String): String {
            var yourFormattedString = "-"
            if (distance != "-") {
                i(TAG, "getDistace")
                //        DecimalFormat f = new DecimalFormat("##.00");
                val formatter = DecimalFormat("##.##")
                yourFormattedString = formatter.format(distance.toDouble()) + "KM"
            }

            return yourFormattedString
        }

        /*calling  getTime*/
        fun getTime(seconds: Double): String {
            try {
                i(TAG, "getTime")
                val hours = seconds.toInt() / 3600
                val minutes = (seconds % 3600).toInt() / 60

                return if (hours == 0) {
                    twoDigitString(minutes) + " Mins"
                } else {
                    twoDigitString(hours) + ":" + twoDigitString(
                        minutes
                    ) + " Hrs"
                }
            } catch (ex: Exception) {
                printStackTrace(ex)
            }
            return ""
        }

        /*calling  getTimeForWolooStoreInfo*/
        fun getTimeForWolooStoreInfo(seconds: String): String {
            if (seconds != "-") {
                try {
                    i(TAG, "getTimeForWolooStoreInfo")
                    val hours = seconds.toDouble() as Int / 3600
                    val minutes = (seconds.toDouble() % 3600).toInt() / 60

                    return if (hours == 0) {
                        twoDigitString(minutes) + " Min"
                    } else {
                        twoDigitString(hours) + ":" + twoDigitString(
                            minutes
                        ) + " Hrs"
                    }
                } catch (ex: Exception) {
                    printStackTrace(ex)
                }
            }
            return seconds
        }

        /*calling  twoDigitString*/
        fun twoDigitString(number: Int): String {
            i(TAG, "twoDigitString")
            if (number == 0) {
                return "00"
            }

            if (number / 10 == 0) {
                return "0$number"
            }

            return number.toString()
        }

        /*calling  getTimeAgo*/
        fun getTimeAgo(updatedAt: String): Long {
            i(TAG, "getTimeAgo")
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            try {
                val date = sdf.parse(updatedAt)
                return date!!.time
            } catch (ex: ParseException) {
                v("Exception", ex.localizedMessage)
                return 0
            }
        }

        /*calling  isSubscriptionExpired*/
        @JvmStatic
        fun isSubscriptionExpired(expiryDate: String?): Boolean {
            i(TAG, "isSubscriptionExpired")
            if (TextUtils.isEmpty(expiryDate)) {
                return true
            }
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            try {
                var date = sdf.parse(expiryDate)
                val c = Calendar.getInstance()
                c.time = date
                c.add(Calendar.DATE, 1)
                date = c.time
                val currentDate = Date()
                if (currentDate.after(date)) {
                    return true
                }
                return false
            } catch (ex: ParseException) {
                v("Exception", ex.localizedMessage)
                return true
            }
        }

        /*calling  geCreditHistoryDate*/
        fun geCreditHistoryDate(dateTime: String, isHeader: Boolean): String {
            try {
                i(TAG, "geCreditHistoryDate")
                val sdf1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                //            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                //sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
                val date = sdf1.parse(dateTime)
                // sdf1.setTimeZone(TimeZone.getDefault());
                return if (isHeader) {
                    SimpleDateFormat("MMMM yyyy").format(date)
                } else {
                    SimpleDateFormat("dd MMMM").format(date)
                }
            } catch (e: Exception) {
                printStackTrace(e)
            }
            return ""
        }

        fun geCreditHistoryDateAndTime(dateTime: String): String {
            try {
                i(TAG, "geCreditHistoryDate")
                val sdf1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                //            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                //sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
                val date = sdf1.parse(dateTime)
                // sdf1.setTimeZone(TimeZone.getDefault());
                return SimpleDateFormat("dd MMMM, hh:mm aaa").format(date)
            } catch (e: Exception) {
                printStackTrace(e)
            }
            return ""
        }


        fun getDDMMYYYYDate(dateTime: String): String {
            try {
                i(TAG, "getDDMMYYYYDate")
                val sdf1 = SimpleDateFormat("yyyy-MM-dd")
                //            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                //sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
                val date = sdf1.parse(dateTime)

                // sdf1.setTimeZone(TimeZone.getDefault());
                return SimpleDateFormat("dd-MM-yyyy").format(date)
            } catch (e: Exception) {
                printStackTrace(e)
            }
            return ""
        }

        /*calling  getAboutUrl*/
        fun getAboutUrl(context: Context?): String? {
            try {
                i(TAG, "getAboutUrl")
                val authConfigResponse = getPreferences.fetchAuthConfig()
                return if (authConfigResponse != null) {
                    authConfigResponse.getuRLS()!!.aboutUrl
                } else {
                    null
                }
            } catch (ex: Exception) {
                printStackTrace(ex)
                return null
            }
        }

        /*calling  getTermsUrl*/
        @JvmStatic
        fun getTermsUrl(context: Context?): String? {
            try {
                val authConfigResponse = getPreferences.fetchAuthConfig()
                i(TAG, "getTermsUrl" + authConfigResponse!!.getuRLS()!!.terms_url)
                return if (authConfigResponse != null) {
                    authConfigResponse.getuRLS()!!.terms_url
                } else {
                    null
                }
            } catch (ex: Exception) {
                printStackTrace(ex)
                return null
            }
        }

        /*calling  authconfig_response*/
        @JvmStatic
        fun authconfig_response(context: Context?): AuthConfigResponse.Data? {
            var authConfigResponse: AuthConfigResponse.Data? = null
            try {
                i(TAG, "authconfig_response")
                authConfigResponse = getPreferences.fetchAuthConfig()
                return authConfigResponse
            } catch (e: JsonSyntaxException) {
                printStackTrace(e)
            }
            return authConfigResponse
        }

        /*calling  navigateToRazorPayFlow*/
        fun navigateToRazorPayFlow(
            mContext: Context,
            planId: String?,
            subscriptionId: String?,
            msg: String?,
            isEmail: Boolean,
            mobile: String?,
            isSubscription: Boolean,
            giftCardView: GiftCardView?,
            isFutureSubscription: Boolean,
            isGiftSub: Boolean
        ) {
            try {
                i(TAG, "navigateToRazorPayFlow")
                val intent = Intent(
                    mContext,
                    RazorPayActivity::class.java
                )
                intent.putExtra(AppConstants.PLAN_ID, planId)
                if (isSubscription) {
                    intent.putExtra(AppConstants.SUBSCRIPTION_ID, subscriptionId)
                    if (isFutureSubscription) intent.putExtra(
                        AppConstants.FUTURE_SUBSCRIPTION,
                        "true"
                    )
                } else {
                    intent.putExtra(AppConstants.ORDER_ID, subscriptionId)
                    intent.putExtra(AppConstants.ORDER_AMOUNT, msg)
                    // intent.putExtra(AppConstants.GIFT_CARD_VIEW_PRESENTER,giftCardView);
                }
                if (isGiftSub) {
                    intent.putExtra(AppConstants.isGiftSub, "isGiftSub")
                    intent.putExtra(AppConstants.gift_numbers, planId)
                }
                intent.putExtra(AppConstants.MSG, msg)
                intent.putExtra(AppConstants.IS_EMAIL, isEmail)
                intent.putExtra(AppConstants.MOBILE, mobile)
                mContext.startActivity(intent)
            } catch (ex: Exception) {
                printStackTrace(ex)
            }
        }

        /*calling  showKeyboardProgramatically*/
        fun showKeyboardProgramatically(mContext: Context, relativeLayout: RelativeLayout) {
            try {
                i(TAG, "showKeyboardProgramatically")
                val inputMethodManager =
                    mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.toggleSoftInputFromWindow(
                    relativeLayout.applicationWindowToken,
                    InputMethodManager.SHOW_FORCED, 0
                )
            } catch (ex: Exception) {
                printStackTrace(ex)
            }
        }

        /*calling  getReferralShareUrl*/
        fun getReferralShareUrl(mContext: Context?, referralCode: String): String {
            try {
                i(TAG, "getReferralShareUrl")
                return authconfig_response(mContext)!!
                    .getuRLS()!!.app_share_url + "?referralCode=" + referralCode
            } catch (ex: Exception) {
                printStackTrace(ex)
                return ""
            }
        }

        /*calling  setEncryptedPayload*/
        fun setEncryptedPayload(mContext: Context?, api: String?, encryptedpayload: String?) {
            try {
                i(TAG, "setEncryptedPayload")
                val mSharedPref: SharedPreference = SharedPreference(mContext)
                var encryptedPayload: String? = mSharedPref.getStoredPreference(
                    mContext,
                    SharedPreferencesEnum.ENCRYPTED_PAYLOAD.getPreferenceKey()
                )
                var map: MutableMap<String?, String?> = HashMap()
                if (!TextUtils.isEmpty(encryptedPayload)) {
                    map = Gson().fromJson(
                        encryptedPayload,
                        map.javaClass
                    ) as MutableMap<String?, String?>
                    if (!map.containsKey(api)) {
                        map[api] = encryptedPayload
                    }
                    encryptedPayload = Gson().toJson(map)
                    mSharedPref.setStoredPreference(
                        mContext,
                        SharedPreferencesEnum.ENCRYPTED_PAYLOAD.getPreferenceKey(),
                        encryptedPayload
                    )
                } else {
                    map[api] = encryptedPayload
                    encryptedPayload = Gson().toJson(map)
                    mSharedPref.setStoredPreference(
                        mContext,
                        SharedPreferencesEnum.ENCRYPTED_PAYLOAD.getPreferenceKey(),
                        encryptedPayload
                    )
                }
            } catch (ex: Exception) {
                printStackTrace(ex)
            }
        }

        /*calling  getEncryptedPayload*/
        fun getEncryptedPayload(mContext: Context?): Map<String, String>? {
            try {
                i(TAG, "getEncryptedPayload")
                val mSharedPref: SharedPreference = SharedPreference(mContext)
                val encryptedPayload: String = mSharedPref.getStoredPreference(
                    mContext,
                    SharedPreferencesEnum.ENCRYPTED_PAYLOAD.getPreferenceKey()
                ).toString()
                if (!TextUtils.isEmpty(encryptedPayload)) {
                    val map: Map<String, String> = HashMap()
                    return Gson().fromJson(encryptedPayload, map.javaClass) as Map<String, String>
                }
            } catch (ex: Exception) {
                printStackTrace(ex)
            }
            return null
        }

        /*calling  calldeeplink*/
        @JvmStatic
        fun calldeeplink(
            context: Context,
            mProgressBar: Dialog?,
            subject: String,
            shareBody: String,
            longUrl: String,
            isWhatsapp: Boolean
        ) {
            i(TAG, "calldeeplink")
            val queue = Volley.newRequestQueue(context)
            var jsonObjectParams = JSONObject()
            val iosInfo: DeepLinkRequestModel.DynamicLinkInfo.IosInfo =
                DeepLinkRequestModel.DynamicLinkInfo.IosInfo("in.woloo.app")
            val androidInfo: DeepLinkRequestModel.DynamicLinkInfo.AndroidInfo =
                DeepLinkRequestModel.DynamicLinkInfo.AndroidInfo("in.woloo.www")
            //new Share
            var shareTitle = subject
            if (!TextUtils.isEmpty(shareTitle)) {
                if (shareTitle.length > 20) {
                    shareTitle = shareTitle.substring(0, 20)
                    shareTitle = "$shareTitle..."
                }
            }

            val dynamicLinkInfo: DeepLinkRequestModel.DynamicLinkInfo =
                DeepLinkRequestModel.DynamicLinkInfo(
                    iosInfo,
                    androidInfo,
                    longUrl,
                    AppConstants.API_DEEP_LINK_DOMAIN_URI_PREFIX,
                    null
                )
            val suffix = DeepLinkRequestModel.Suffix("SHORT")

            val deepLinkRequestModel: DeepLinkRequestModel =
                DeepLinkRequestModel(dynamicLinkInfo, suffix)

            try {
                val gson = gson
                val json_string = gson.toJson(deepLinkRequestModel)
                if (json_string != null) {
                    jsonObjectParams = JSONObject(json_string)
                }
            } catch (e: Exception) {
                printStackTrace(e)
            }

            //JSONONB REQ
            val strReq = JsonObjectRequest(
                Request.Method.POST, APIConstants.API_DEEP_LINK_SHORT_URL_API, jsonObjectParams,
                { response ->
                    try {
                        ProgressBarUtils.dismissProgressDialog(mProgressBar)
                        Log("DEEPLINK API SUCCESS $response")
                        var shortLink = ""
                        val previewLink = ""
                        if (response != null) {
                            shortLink = response.getString("shortLink")
                        }
                        val shareMessage = shortLink
                        val message = shareBody.replace("{link}", shareMessage)
                        if (TextUtils.isEmpty(subject)) {
                            shareDeepLink(
                                context,
                                message,
                                isWhatsapp
                            )
                        } else {
                            shareDeepLink(
                                context,
                                """
                                    $subject
                                    $message
                                    """.trimIndent(),
                                isWhatsapp
                            )
                        }
                    } catch (e: Exception) {
                        printStackTrace(e)
                    }
                },
                {
                    ProgressBarUtils.dismissProgressDialog(
                        mProgressBar
                    )
                })
            strReq.setRetryPolicy(
                DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
            )
            strReq.setShouldCache(false)

            queue.add(strReq)
        }


        /*calling  shareDeepLink*/
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
        fun shareDeepLink(context: Context, message: String, isWhatsapp: Boolean) {
            i(TAG, "shareDeepLink")
            i(TAG, "\n ------------ shareBody ------------")
            i(
                TAG,
                "\n shareBody: $message"
            )
            if (isWhatsapp) {
                try {
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.setType("text/plain")
                    intent.setPackage("com.whatsapp")
                    intent.putExtra(Intent.EXTRA_TEXT, message)
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Some error occured while sharing via whatsapp. Please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                sharingIntent.setType("text/plain")
                //        sharingIntent.setPackage("com.whatsapp");
//        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject.trim());
                sharingIntent.putExtra(Intent.EXTRA_TEXT, message)

                context.startActivity(
                    Intent.createChooser(
                        sharingIntent, context.resources.getString(
                            R.string.app_name
                        ) + " Share"
                    )
                )
            }
        }

        @JvmStatic
        fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
            try {
                packageManager.getPackageInfo(packageName, 0)
                return true
            } catch (e: PackageManager.NameNotFoundException) {
                return false
            }
        }


        /*calling  getDeeplink*/
        fun getDeeplink(
            context: Context,
            mProgressBar: Dialog?,
            subject: String,
            shareBody: String?,
            longUrl: String,
            deepLinkCallback: EnterMessageFragment.DeepLinkCallback
        ) {
            i(TAG, "getDeeplink")
            val queue = Volley.newRequestQueue(context)
            var jsonObjectParams = JSONObject()
            val iosInfo: DeepLinkRequestModel.DynamicLinkInfo.IosInfo =
                DeepLinkRequestModel.DynamicLinkInfo.IosInfo("in.woloo.app")
            val androidInfo: DeepLinkRequestModel.DynamicLinkInfo.AndroidInfo =
                DeepLinkRequestModel.DynamicLinkInfo.AndroidInfo("in.woloo.www")
            //new Share
            var shareTitle = subject
            if (!TextUtils.isEmpty(shareTitle)) {
                if (shareTitle.length > 20) {
                    shareTitle = shareTitle.substring(0, 20)
                    shareTitle = "$shareTitle..."
                }
            }

            val dynamicLinkInfo: DeepLinkRequestModel.DynamicLinkInfo =
                DeepLinkRequestModel.DynamicLinkInfo(
                    iosInfo,
                    androidInfo,
                    longUrl,
                    AppConstants.API_DEEP_LINK_DOMAIN_URI_PREFIX,
                    null
                )
            val suffix = DeepLinkRequestModel.Suffix("SHORT")

            val deepLinkRequestModel: DeepLinkRequestModel =
                DeepLinkRequestModel(dynamicLinkInfo, suffix)

            try {
                val gson = gson
                val json_string = gson.toJson(deepLinkRequestModel)
                if (json_string != null) {
                    jsonObjectParams = JSONObject(json_string)
                }
            } catch (e: Exception) {
                printStackTrace(e)
            }

            //JSONONB REQ
            val strReq = JsonObjectRequest(
                Request.Method.POST, APIConstants.API_DEEP_LINK_SHORT_URL_API, jsonObjectParams,
                { response ->
                    try {
                        ProgressBarUtils.dismissProgressDialog(mProgressBar)
                        Log("DEEPLINK API SUCCESS $response")
                        var shortLink: String? = ""
                        val previewLink = ""
                        if (response != null) {
                            shortLink = response.getString("shortLink")
                        }
                        deepLinkCallback.getDeepLink(shortLink!!)
                    } catch (e: Exception) {
                        printStackTrace(e)
                    }
                },
                {
                    ProgressBarUtils.dismissProgressDialog(
                        mProgressBar
                    )
                })
            strReq.setRetryPolicy(
                DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
            )
            strReq.setShouldCache(false)

            queue.add(strReq)
        }


        /*calling  getAppUpdateDialog*/
        fun getAppUpdateDialog(mContext: Context, cancelable: Boolean, type: Int): Dialog {
            i(TAG, "getAppUpdateDialog")
            val dialog = Dialog(mContext)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(cancelable)

            val factory = LayoutInflater.from(mContext)
            val customPopupView = factory.inflate(
                R.layout.custome_app_update_popup, null
            )

            val mTitleText = customPopupView.findViewById<TextView>(R.id.app_update_text)
            val mCancelButton = customPopupView.findViewById<Button>(R.id.app_update_cancel_button)
            val mUpdateButton = customPopupView.findViewById<Button>(R.id.app_update_update_button)
            mUpdateButton.visibility = View.VISIBLE
            if (type == 1) mCancelButton.visibility = View.GONE
            else mCancelButton.visibility = View.VISIBLE

            dialog.setContentView(customPopupView)
            return dialog
        }

        @JvmStatic
        fun googlemapapikey(context: Context?): String? {
            var authConfigResponse: AuthConfigResponse.Data? = null
            var googlemapapikey: String? = ""
            try {
                i(TAG, "authconfig_response")
                authConfigResponse = getPreferences.fetchAuthConfig()
                if (authConfigResponse != null && authConfigResponse != null && authConfigResponse.google_maps != null) {
                    if (!TextUtils.isEmpty(authConfigResponse.google_maps!!.key)) {
                        googlemapapikey = authConfigResponse.google_maps!!.key
                    }
                }
            } catch (e: Exception) {
                printStackTrace(e)
            }
            return googlemapapikey
        }

        @JvmStatic
        fun showMaintenanceDialog(context: Context, isCancelable: Boolean) {
            try {
                val authConfigResponse = getPreferences.fetchAuthConfig()
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(isCancelable)
                dialog.setCanceledOnTouchOutside(isCancelable)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.transparent_background)))
                dialog.setContentView(R.layout.dialog_maintaince)
                val btnCloseDialog = dialog.findViewById<TextView>(R.id.btnCloseDialog)
                btnCloseDialog.setOnClickListener { v: View? ->
                    dialog.dismiss()
                }
                if (isCancelable) btnCloseDialog.visibility = View.VISIBLE
                else btnCloseDialog.visibility = View.GONE
                val textView = dialog.findViewById<TextView>(R.id.maintenanceTextTv)
                textView.text = authConfigResponse!!.getmAINTENANCESETTINGS()!!.maintenanceMessage
                dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                dialog.show()
            } catch (e: Exception) {
                printStackTrace(e)
            }
        }

        fun showCustomDialog(context: Context, msg: String?) {
            try {
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(false)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.transparent_background)))
                dialog.setContentView(R.layout.dialog_login_failure)
                dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                val btnCloseDialog = dialog.findViewById<View>(R.id.btnCloseDialog) as TextView

                val tv_msg = dialog.findViewById<View>(R.id.tv_msg) as TextView
                tv_msg.text = msg

                btnCloseDialog.setOnClickListener {
                    if (dialog.isShowing) {
                        dialog.dismiss()
                    }
                }
                dialog.show()
            } catch (e: Exception) {
                printStackTrace(e)
            }
        }

        @JvmStatic
        fun showCustomDialogBackClick(context: Context, msg: String?) {
            try {
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setCancelable(false)
                dialog.setCanceledOnTouchOutside(false)
                dialog.window!!.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.transparent_background)))
                dialog.setContentView(R.layout.dialog_login_failure)
                dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
                val btnCloseDialog = dialog.findViewById<View>(R.id.btnCloseDialog) as TextView

                val tv_msg = dialog.findViewById<View>(R.id.tv_msg) as TextView
                tv_msg.text = msg

                btnCloseDialog.setOnClickListener {
                    if (dialog.isShowing) {
                        dialog.dismiss()
                        (context as Activity).onBackPressed()
                    }
                }
                dialog.show()
            } catch (e: Exception) {
                printStackTrace(e)
            }
        }
    }
}
