package in.woloo.www.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.util.Base64Utils;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.jetsynthesys.encryptor.JetEncryptor;
import com.jetsynthesys.encryptor.JetEncryptorJava;
import com.netcore.android.Smartech;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.database.preference.IntTypeAdapter;
import in.woloo.www.database.preference.IpBean;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.giftcard.mvp.GiftCardView;
import in.woloo.www.invite_friend.fragments.EnterMessageFragment;
import in.woloo.www.login.models.LoginResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.CustomVolleyRequest;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.VolleySingleton;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.subscribe.razorpay.RazorPayActivity;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.DeepLinkRequestModel;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.ProgressBarUtils;
import in.woloo.www.v2.data.local.SharedPrefClient;
import in.woloo.www.v2.data.local.SharedPrefSettings;
import in.woloo.www.v2.splash.UserDetails;
import io.hansel.hanselsdk.Hansel;

public class CommonUtils implements Serializable{

    private static final String TAG = CommonUtils.class.getSimpleName();
    private Dialog dialog_progress;
    public String APP_TYPE_MOBILE = "mobile";
    public String APP_TYPE_TV = "tv";
    /*calling  getDeviceId*/
    @SuppressLint("all")
    public String getDeviceId(Context context) {
        Logger.i(TAG, "getDeviceId");
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /*calling  getEncryptedJsonRequest*/
    public JSONObject getEncryptedJsonRequest(Activity context, JSONObject postParamsObject, JetEncryptor mJetEncryptor, String app_type) {
        JSONObject postParamsObjectEnc = new JSONObject();
        Logger.i(TAG, "getEncryptedJsonRequest");
        try {
            JSONObject localeObject;
            localeObject = getCustomLocale(context.getApplicationContext(), app_type);
            if (postParamsObject != null && postParamsObject.length() > 0 && !postParamsObject.equals("")) {
                Logger.v(TAG, "postParamsObject: " + postParamsObject);
                if (mJetEncryptor != null) {
                    try {
                        JetEncryptorJava jetEncryptorJava = new JetEncryptorJava();
                        String postParamsObjectStr = jetEncryptorJava.processData(context, postParamsObject.toString());
                        if (postParamsObjectStr != null) {
                            postParamsObjectEnc = new JSONObject(postParamsObjectStr);
                            postParamsObjectEnc.put(JSONTagConstant.LOCALE, localeObject);
                        }
                    } catch (Exception e) {
                        printStackTrace(e);
                        postParamsObjectEnc = null;
                    }
                }
            }
        } catch (Exception e) {
            postParamsObjectEnc = null;
              CommonUtils.printStackTrace(e);
        }
        return postParamsObjectEnc;
    }

    /*calling  getCustomLocale*/
    public static JSONObject getCustomLocale(Context context, String APP_TYPE) {
        Logger.i(TAG, "getCustomLocale");
        if (context == null) {
            context = WolooApplication.getInstance();
        }
        SharedPreference mSharedPreference = new SharedPreference(context);
        String mStrLocale = mSharedPreference.getStoredPreference(context, SharedPreferencesEnum.IP_TO_LOCALE.getPreferenceKey());
//        String mStrLocale = mSharedPreference.getProperty(context, SharedPreferencesEnum.IP_TO_LOCALE.getPreferenceKey());
//        String locale = mStrLocale.getData();

        Gson gson = new Gson();
        IpBean ipBean = gson.fromJson(mStrLocale, IpBean.class);
        IpBean.Data.Versions versions = null;
        if (ipBean != null && ipBean.getData() != null) {
            versions = ipBean.getData().getVersions();
        }

        JSONObject jsonObject = new JSONObject();

        String platform = "android";
        platform = AppConstants.PLATFORM_ANDROID;
        try {
            if (mStrLocale != null) {
                //JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(JSONTagConstant.VERSION, BuildConfig.VERSION_NAME);
                    jsonObject.put(JSONTagConstant.PLATFORM, platform);
                    // jsonObject.put("language", mSharedPreference.getStoredPreference(context, SharedPreferencesEnum.LANGUAGE_CODE.getPreferenceKey(), "EN"));
                    if (versions.getCountryCode() == null || versions.getCountryCode().equalsIgnoreCase("-")) {
//                        jsonObject.put(JSONTagConstant.COUNTRY, "GB");
                        jsonObject.put(JSONTagConstant.COUNTRY, "IN");
                    } else {
                        jsonObject.put(JSONTagConstant.COUNTRY, versions.getCountryCode());
                        // jsonObject.put(JSONTagConstant.COUNTRY, "GB");
//                        jsonObject.put(JSONTagConstant.COUNTRY, "IN");
                    }
                    jsonObject.put(JSONTagConstant.SEGMENT, "");

                } catch (JSONException e) {
                      CommonUtils.printStackTrace(e);
                }

            } else {

                // JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(JSONTagConstant.VERSION, BuildConfig.VERSION_NAME);
                    jsonObject.put(JSONTagConstant.PLATFORM, platform);
                    jsonObject.put(JSONTagConstant.LANGUAGE, "en");
                    //jsonObject.put("language", mSharedPreference.getStoredPreference(context, SharedPreferencesEnum.LANGUAGE_CODE.getPreferenceKey(), "EN"));
                    // jsonObject.put("country", "IN");
//                    jsonObject.put(JSONTagConstant.COUNTRY, "GB");
                    jsonObject.put(JSONTagConstant.COUNTRY, "IN");
                    jsonObject.put(JSONTagConstant.SEGMENT, "");

                } catch (JSONException e) {
                      CommonUtils.printStackTrace(e);
                }

            }
            AppConstants mApplicationConstant = new AppConstants();
            jsonObject.put("language", "en");
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
        return jsonObject;
    }

    /*calling  printStackTrace*/
    public static void printStackTrace(Exception e) {
        e.printStackTrace();
        if (!(BuildConfig.LIVE_URL.equalsIgnoreCase("3") && !BuildConfig.DEBUG)) {
            e.printStackTrace();
            Logger.i(TAG, "printStackTrace "+e.getMessage());
        }
    }
    /*calling  getDeviceAllInformation*/
    public String getDeviceAllInformation(Context context) {
        Logger.i(TAG, "getDeviceAllInformation");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Device ID: " + getDeviceId(context));
        stringBuffer.append("\nDevice Make: " + getDeviceMake());
        stringBuffer.append("\nDevice Model: " + getDeviceModel());
        stringBuffer.append("\nDevice OS: " + getDeviceOs());

        return stringBuffer.toString();
    }

    /*calling  getDeviceMake*/
    public String getDeviceMake() {
        Logger.i(TAG, "getDeviceMake");
        return Build.MANUFACTURER;

    }

    /*calling  getDeviceModel*/
    public String getDeviceModel()
    {
        Logger.i(TAG, "getDeviceModel");
        return (Build.MANUFACTURER + ": " + Build.MODEL);
    }

    /*calling  getDeviceOs*/
    public String getDeviceOs() {
        Logger.i(TAG, "getDeviceOs");
        StringBuilder builder = new StringBuilder();
        builder.append("Android : ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                 CommonUtils.printStackTrace(e);
            } catch (IllegalAccessException e) {
                 CommonUtils.printStackTrace(e);
            } catch (NullPointerException e) {
                 CommonUtils.printStackTrace(e);
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" : ").append(fieldName).append(" : ");
                builder.append("sdk=").append(fieldValue);
            }
        }

        return builder.toString();
    }

    /*calling  sendAPIErrorLogToServer*/
    public void sendAPIErrorLogToServer(final Context context, String screenName, String requestName, String inputRequest, String outputResponse) {
        try {
            Logger.i(TAG, "sendAPIErrorLogToServer");
            //if (BuildConfig.LIVE_URL.equalsIgnoreCase("3")) {
            Log("SCREEN NAME " + screenName +
                    " REQUEST NAME " + requestName + " INPUT " + inputRequest + " OUT " + outputResponse
            );

            RequestQueue queue = VolleySingleton.getInstance(context).getRequestQueue();
            // showProgressDialog();
            // showProgress(context);
            JSONObject jsonObjectParams = new JSONObject();
            try {
                jsonObjectParams.put("request", "rescue@sendErrorReport");
                jsonObjectParams.put("screen_name", screenName);
                jsonObjectParams.put("request_name", requestName);
                jsonObjectParams.put("input_request", inputRequest.toString());
                jsonObjectParams.put("output_responce", outputResponse.toString());
                jsonObjectParams.put("platform", "android");
                String userCode = "";
                if (userCode != null && !userCode.isEmpty())
                    jsonObjectParams.put("user_code", userCode);
                jsonObjectParams.put("application_name", "Iconocale");
            } catch (JSONException e) {
                 CommonUtils.printStackTrace(e);
            }

            CustomVolleyRequest stringRequest = new CustomVolleyRequest(AppConstants.POST_REQUEST, BuildConfig.RESCUE_API_URL, jsonObjectParams,
                    (response) -> {

                        Log("FALLBACK API SUCCESS " + response);
                    }, error -> {
                try {

                    Log("FALLBACK API ERROR " + error);
                } catch (Exception e) {
                     CommonUtils.printStackTrace(e);
                }
            }, context, APP_TYPE_MOBILE);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            stringRequest.setShouldCache(false);
            queue.add(stringRequest);

            /*} else {
//            Toast.makeText(context, "FALLBACK API FAILURE", Toast.LENGTH_SHORT).show();
            }*/
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
    }

    /*calling  Log*/
    public static void Log(String values)
    {
        Logger.i(TAG, "Log");
        Logger.i("CommonUtils " + values, "" + values);
    }

    /*calling  showProgress*/
    public void showProgress(Context context) {
        try {
            Logger.i(TAG, "showProgress");
            if (dialog_progress == null) {
                initProgress(context);
            }
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }

        try {
            if (!dialog_progress.isShowing() && !((Activity) context).isFinishing()) {
                dialog_progress.show();
            }
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
    }

    /*calling  initProgress*/
    private void initProgress(Context context) {
        try {
            Logger.i(TAG, "initProgress");
            dialog_progress = new Dialog(context, R.style.CustomDialogTime);
            dialog_progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_progress.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));
            dialog_progress.setCancelable(false);
            dialog_progress.setContentView(R.layout.dialog_progress_overlay);
//            dialog_progress.getWindow().getDecorView().setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
    }

    /*calling  hideProgress*/
    public void hideProgress() {
        try {
            Logger.i(TAG, "hideProgress");
            if (dialog_progress != null) {

                if (dialog_progress.isShowing())
                    dialog_progress.dismiss();

            }
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
    }

    /*calling  getUserCode*/
    public String getUserCode(Context context) {
        Logger.i(TAG, "getUserCode");
        SharedPreference mSharedPref = new SharedPreference(context);
        return mSharedPref.getStoredPreference(context, SharedPreferencesEnum.USER_CODE.getPreferenceKey());

    }

    /*calling  getSuperStoreId*/
    public String getSuperStoreId(Context context) {
        Logger.i(TAG, "getSuperStoreId");
        SharedPreference mSharedPref = new SharedPreference(context);
        String superStoreId = "639";
        try {
//            superStoreId = mSharedPref.getStoredPreference(context, SharedPreferencesEnum.SUPERSTORE_ID.getPreferenceKey(), "0");
        } catch (Exception e) {
            printStackTrace(e);
        }
        return superStoreId;
    }

    /*calling  getMobileTvType*/
    public String getMobileTvType(Context context) {
        Logger.i(TAG, "getMobileTvType");
        if (context == null) {
            context = WolooApplication.getInstance();
        }
        String type = "tv";
        try {
            UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
            if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_NORMAL) {
                //   Toast.makeText(context, "TELEVISION", Toast.LENGTH_SHORT).show();
                type = "mobile";
            } else {
                // Toast.makeText(context, "MOBILE", Toast.LENGTH_SHORT).show();
                type = "tv";
            }
        } catch (Exception e) {
            printStackTrace(e);
        }
//        type = "tv";
        return type;
    }

    /*calling  getGson*/
    public static Gson getGson() {
        Logger.i(TAG, "getGson");
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(int.class, new IntTypeAdapter())
                .registerTypeAdapter(Integer.class, new IntTypeAdapter()).create();
        return gson;
    }


    /*calling  hideKeyboard*/
    public static void hideKeyboard(Activity activity) {
        Logger.i(TAG, "hideKeyboard");
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /*calling  isLoggedIn*/
    public boolean isLoggedIn(Context context) {
        Logger.i(TAG, "isLoggedIn");
        try{
            SharedPreference mSharedPref = new SharedPreference(context);
            boolean isLoggedIn = mSharedPref.getStoredBooleanPreference(context, SharedPreferencesEnum.IS_LOGGED_IN.getPreferenceKey(),false);
            return isLoggedIn;
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
        return false;
    }

    public boolean isLoggedIn() {
        return SharedPrefSettings.Companion.getGetPreferences().fetchIsLoggedIn();
    }

    public String getReferralCode(){
        return SharedPrefSettings.Companion.getGetPreferences().fetchReferralCode();
    }

    /*calling  getUserInfo*/
    public LoginResponse getUserInfo(Context context) {
        try{
            Logger.i(TAG, "getUserInfo");
            SharedPreference mSharedPref = new SharedPreference(context);
            String loginInfo = mSharedPref.getStoredPreference(context, SharedPreferencesEnum.USER_INFO.getPreferenceKey());
            LoginResponse userInfo = new Gson().fromJson(loginInfo,LoginResponse.class);
            return userInfo;
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
        return null;
    }

    public UserDetails getUserInfo() {
        try {
            UserDetails user = SharedPrefSettings.Companion.getGetPreferences().fetchUserDetails();
            return user;
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
        return null;
    }

    /*calling  clearApplicationData*/
    public void clearApplicationData(Context context) {
        try {
            Logger.i(TAG, "clearApplicationData");
            SharedPreference mSharedPreference = new SharedPreference(context);
            mSharedPreference.removeOnClearAppData(context, SharedPreferencesEnum.IS_LOGGED_IN.getPreferenceKey());
            mSharedPreference.removeOnClearAppData(context, SharedPreferencesEnum.USER_INFO.getPreferenceKey());
            mSharedPreference.removeAllUserData(context);
            SharedPrefSettings.Companion.getGetPreferences().clear();
            Smartech.getInstance(new WeakReference<>(context)).logoutAndClearUserIdentity(true);
            Hansel.getUser().clear();
        } catch (Exception e) {
            printStackTrace(e);
        }
    }

    /*calling  getFirstLaterCaps*/
    public String getFirstLaterCaps(String data){
        Logger.i(TAG, "getFirstLaterCaps");
        String upperString=data;
        try {
            upperString = data.substring(0, 1).toUpperCase() + data.substring(1).toLowerCase();
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }

        return upperString;
    }

    /*calling  isValidMobileNumber*/
    public static boolean isValidMobileNumber(String mobile_number){
        try{
            Logger.i(TAG, "isValidMobileNumber");
           // if(!TextUtils.isEmpty(mobile_number) && TextUtils.isDigitsOnly(mobile_number) && (mobile_number.length() >= 10 && mobile_number.length() < 13)){
            if(!TextUtils.isEmpty(mobile_number) && TextUtils.isDigitsOnly(mobile_number) && (mobile_number.length() == 10)){
                return true;
            }
            return false;
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
        return false;
    }

    /*calling  getDistace*/
    public static String getDistace(String distance) {
        String yourFormattedString="-";
        if(!distance.equals("-")) {

            Logger.i(TAG, "getDistace");
//        DecimalFormat f = new DecimalFormat("##.00");
            DecimalFormat formatter = new DecimalFormat("##.##");
            yourFormattedString = formatter.format(Double.parseDouble(distance)) + "KM";
        }

        return yourFormattedString;
    }

    /*calling  getTime*/
    public static String getTime(double seconds) {
        try{
            Logger.i(TAG, "getTime");
            int hours = (int)seconds / 3600;
            int minutes = (int)(seconds % 3600) / 60;

            if(hours == 0){
                return twoDigitString(minutes)+" Mins";
            }else {
                return twoDigitString(hours) + ":" + twoDigitString(minutes)+" Hrs";
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
        return "";
    }

    /*calling  getTimeForWolooStoreInfo*/
    public static String getTimeForWolooStoreInfo(String seconds) {
        if(!seconds.equals("-")) {
            try {
                Logger.i(TAG, "getTimeForWolooStoreInfo");
                int hours = (int) Double.parseDouble(seconds) / 3600;
                int minutes = (int) (Double.parseDouble(seconds) % 3600) / 60;

                if (hours == 0) {
                    return twoDigitString(minutes) + " Min";
                } else {
                    return twoDigitString(hours) + ":" + twoDigitString(minutes) + " Hrs";
                }
            } catch (Exception ex) {
                 CommonUtils.printStackTrace(ex);
            }
        }
        return seconds;
    }

    /*calling  twoDigitString*/
    public static String twoDigitString(int number) {
        Logger.i(TAG, "twoDigitString");
        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }

    /*calling  getTimeAgo*/
    public static long getTimeAgo(String updatedAt) {
        Logger.i(TAG, "getTimeAgo");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(updatedAt);
            return date.getTime();
        } catch (ParseException ex) {
            Logger.v("Exception", ex.getLocalizedMessage());
            return 0;
        }
    }

    /*calling  isSubscriptionExpired*/
    public static boolean isSubscriptionExpired(String expiryDate){
        Logger.i(TAG, "isSubscriptionExpired");
        if(TextUtils.isEmpty(expiryDate)){
            return true;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(expiryDate);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(Calendar.DATE, 1);
            date = c.getTime();
            Date currentDate = new Date();
            if(currentDate.after(date)){
                return true;
            }
            return false;
        } catch (ParseException ex) {
            Logger.v("Exception", ex.getLocalizedMessage());
            return true;
        }
    }

    /*calling  geCreditHistoryDate*/
    public static String geCreditHistoryDate(String dateTime,boolean isHeader) {
        try {
            Logger.i(TAG, "geCreditHistoryDate");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            //sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = sdf1.parse(dateTime);
           // sdf1.setTimeZone(TimeZone.getDefault());
            if(isHeader){
                return new SimpleDateFormat("MMMM yyyy").format(date);
            }else {
                return new SimpleDateFormat("dd MMMM").format(date);
            }

        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
        return "";
    }

    public static String geCreditHistoryDateAndTime(String dateTime) {
        try {
            Logger.i(TAG, "geCreditHistoryDate");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            //sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = sdf1.parse(dateTime);
            // sdf1.setTimeZone(TimeZone.getDefault());
            return new SimpleDateFormat("dd MMMM, hh:mm aaa").format(date);

        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
        return "";
    }


    public static String getDDMMYYYYDate(String dateTime) {
        try {
            Logger.i(TAG, "getDDMMYYYYDate");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
//            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            //sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = sdf1.parse(dateTime);
            // sdf1.setTimeZone(TimeZone.getDefault());

            return new SimpleDateFormat("dd-MM-yyyy").format(date);


        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
        return "";
    }
    /*calling  getAboutUrl*/
    public static String getAboutUrl(Context context) {
        try{
            Logger.i(TAG, "getAboutUrl");
            AuthConfigResponse.Data authConfigResponse = SharedPrefSettings.Companion.getGetPreferences().fetchAuthConfig();
            if(authConfigResponse != null){
                return authConfigResponse.getuRLS().getAboutUrl();
            }else{
                return null;
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
            return null;
        }
    }
    /*calling  getTermsUrl*/
    public static String getTermsUrl(Context context) {
        try{
            Logger.i(TAG, "getTermsUrl");
            AuthConfigResponse.Data authConfigResponse = SharedPrefSettings.Companion.getGetPreferences().fetchAuthConfig();
            if(authConfigResponse != null){
                return authConfigResponse.getuRLS().getTerms_url();
            }else{
                return null;
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
            return null;
        }
    }

    /*calling  authconfig_response*/
    public static AuthConfigResponse.Data authconfig_response(Context context){
        AuthConfigResponse.Data authConfigResponse = null;
        try {
            Logger.i(TAG, "authconfig_response");
             authConfigResponse = SharedPrefSettings.Companion.getGetPreferences().fetchAuthConfig();
            return authConfigResponse;
        } catch (JsonSyntaxException e) {
             CommonUtils.printStackTrace(e);
        }
        return authConfigResponse;
    }

    /*calling  navigateToRazorPayFlow*/
    public static void navigateToRazorPayFlow(Context mContext, String planId, String subscriptionId, String msg, boolean isEmail, String mobile, boolean isSubscription, GiftCardView giftCardView, boolean isFutureSubscription, boolean isGiftSub) {
        try {
            Logger.i(TAG, "navigateToRazorPayFlow");
            Intent intent = new Intent(mContext, RazorPayActivity.class);
            intent.putExtra(AppConstants.PLAN_ID, planId);
            if (isSubscription) {
                intent.putExtra(AppConstants.SUBSCRIPTION_ID, subscriptionId);
                if (isFutureSubscription)
                    intent.putExtra(AppConstants.FUTURE_SUBSCRIPTION, "true");
            }
            else
            {
                intent.putExtra(AppConstants.ORDER_ID, subscriptionId);
                intent.putExtra(AppConstants.ORDER_AMOUNT,msg);
               // intent.putExtra(AppConstants.GIFT_CARD_VIEW_PRESENTER,giftCardView);
            }
            if(isGiftSub)
            {
                intent.putExtra(AppConstants.isGiftSub,"isGiftSub");
                intent.putExtra(AppConstants.gift_numbers,planId);
            }
            intent.putExtra(AppConstants.MSG, msg);
            intent.putExtra(AppConstants.IS_EMAIL, isEmail);
            intent.putExtra(AppConstants.MOBILE,mobile);
            mContext.startActivity(intent);
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    /*calling  showKeyboardProgramatically*/
    public static void showKeyboardProgramatically(Context mContext, RelativeLayout relativeLayout){
        try{
            Logger.i(TAG, "showKeyboardProgramatically");
            InputMethodManager inputMethodManager =
                    (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInputFromWindow(
                    relativeLayout.getApplicationWindowToken(),
                    InputMethodManager.SHOW_FORCED, 0);
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }
    /*calling  getDeviceToken*/
    public String getDeviceToken(Context context) {
        try{
            Logger.i(TAG, "getDeviceToken");
            SharedPreference mSharedPref = new SharedPreference(context);
            return mSharedPref.getStoredPreference(context, SharedPreferencesEnum.PUSH_TOKEN.getPreferenceKey());
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
            return "";
        }
    }

    /*calling  getReferralShareUrl*/
    public static String getReferralShareUrl(Context mContext,String referralCode){
        try{
            Logger.i(TAG, "getReferralShareUrl");
            return CommonUtils.authconfig_response(mContext).getuRLS().getApp_share_url()+"?referralCode="+referralCode;
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
            return "";
        }
    }
    /*calling  setEncryptedPayload*/
    public static void setEncryptedPayload(Context mContext,String api,String encryptedpayload){
        try{
            Logger.i(TAG, "setEncryptedPayload");
            SharedPreference mSharedPref = new SharedPreference(mContext);
            String encryptedPayload = mSharedPref.getStoredPreference(mContext, SharedPreferencesEnum.ENCRYPTED_PAYLOAD.getPreferenceKey());
            Map<String,String> map = new HashMap<String,String>();
            if(!TextUtils.isEmpty(encryptedPayload)){
                map = (Map<String,String>) new Gson().fromJson(encryptedPayload, map.getClass());
                if(!map.containsKey(api)){
                   map.put(api,encryptedPayload);
                }
                encryptedPayload = new Gson().toJson(map);
                mSharedPref.setStoredPreference(mContext,SharedPreferencesEnum.ENCRYPTED_PAYLOAD.getPreferenceKey(),encryptedPayload);
            }else{
                map.put(api,encryptedPayload);
                encryptedPayload = new Gson().toJson(map);
                mSharedPref.setStoredPreference(mContext,SharedPreferencesEnum.ENCRYPTED_PAYLOAD.getPreferenceKey(),encryptedPayload);
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    /*calling  getEncryptedPayload*/
    public static Map<String, String> getEncryptedPayload(Context mContext){
        try{
            Logger.i(TAG, "getEncryptedPayload");
            SharedPreference mSharedPref = new SharedPreference(mContext);
            String encryptedPayload = mSharedPref.getStoredPreference(mContext, SharedPreferencesEnum.ENCRYPTED_PAYLOAD.getPreferenceKey());
            if(!TextUtils.isEmpty(encryptedPayload)){
                Map<String,String> map = new HashMap<String,String>();
                return (Map<String,String>) new Gson().fromJson(encryptedPayload, map.getClass());

            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
        return null;
    }

    /*calling  getNonce*/
    public byte[] getNonce() {
        Logger.i(TAG, "getNonce");
        byte[] nonce = new byte[16];
        SecureRandom secureRandomGenerator = null;
        try {
            secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
            secureRandomGenerator.nextBytes(nonce);
        } catch (NoSuchAlgorithmException e) {
             CommonUtils.printStackTrace(e);
        }
        return nonce;
    }

    /*calling  showPlayServiceUpdateDialog*/
    public void showPlayServiceUpdateDialog(final Activity activity) {
        try {
            Logger.i(TAG, "showPlayServiceUpdateDialog");
            new AlertDialog.Builder(activity, R.style.MyDialogTheme)
                    .setTitle(activity.getApplicationContext().getString(R.string.lbl_update_play_message))
                    .setCancelable(false)
                    .setNegativeButton(activity.getApplicationContext().getString(R.string.exit_app), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // User chose NO
                            activity.finish();
                            System.exit(0);
                        }
                    }).create().show();
        } catch (Exception e) {
            new CommonUtils().printStackTrace(e);
//             CommonUtils.printStackTrace(e);
        }
    }


    /*calling  showNotAuthorizedDialog*/
    public void showNotAuthorizedDialog(final Activity activity) {
        try {
            Logger.i(TAG, "showNotAuthorizedDialog");
            new AlertDialog.Builder(activity, R.style.MyDialogTheme)
                    .setTitle(activity.getApplicationContext().getString(R.string.message_not_authorize))
                    .setCancelable(false)
                    .setNegativeButton(activity.getApplicationContext().getString(R.string.exit_app), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // User chose NO
                            activity.finish();
                            System.exit(0);
                        }
                    }).create().show();
        } catch (Exception e) {
            new CommonUtils().printStackTrace(e);
//             CommonUtils.printStackTrace(e);
        }
    }


    /*calling  calldeeplink*/
    public static void calldeeplink(Context context,Dialog mProgressBar,String subject, String shareBody, String longUrl, Boolean isWhatsapp) {
        Logger.i(TAG, "calldeeplink");
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject jsonObjectParams = new JSONObject();
        DeepLinkRequestModel.DynamicLinkInfo.IosInfo iosInfo = new DeepLinkRequestModel.DynamicLinkInfo.IosInfo("in.woloo.app");
        DeepLinkRequestModel.DynamicLinkInfo.AndroidInfo androidInfo = new DeepLinkRequestModel.DynamicLinkInfo.AndroidInfo("in.woloo.www");
        //new Share
        String shareTitle = subject;
        if (!TextUtils.isEmpty(shareTitle))
        {
            if (shareTitle.length()>20)
            {
                shareTitle = shareTitle.substring(0,20);
                shareTitle = shareTitle + "...";
            }
        }

        DeepLinkRequestModel.DynamicLinkInfo dynamicLinkInfo = new DeepLinkRequestModel.DynamicLinkInfo(iosInfo, androidInfo, longUrl, AppConstants.API_DEEP_LINK_DOMAIN_URI_PREFIX, null);
        DeepLinkRequestModel.Suffix suffix = new DeepLinkRequestModel.Suffix("SHORT");

        DeepLinkRequestModel deepLinkRequestModel = new DeepLinkRequestModel(dynamicLinkInfo, suffix);

        try {
            Gson gson = getGson();
            String json_string = gson.toJson(deepLinkRequestModel);
            if (json_string != null) {
                jsonObjectParams = new JSONObject(json_string);
            }
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }

        //JSONONB REQ

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, APIConstants.API_DEEP_LINK_SHORT_URL_API, jsonObjectParams,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ProgressBarUtils.dismissProgressDialog(mProgressBar);
                            Log("DEEPLINK API SUCCESS " + response);
                            String shortLink = "";
                            String previewLink = "";
                            if (response != null) {
                                shortLink = response.getString("shortLink");
                            }
                            String shareMessage = shortLink;
                            String message = shareBody.replace("{link}",shareMessage);
                            if (TextUtils.isEmpty(subject)) {
                                shareDeepLink(context, message, isWhatsapp);
                            }else {
                                shareDeepLink(context, subject + "\n"+message, isWhatsapp);
                            }

                        } catch (Exception e) {
                             CommonUtils.printStackTrace(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressBarUtils.dismissProgressDialog(mProgressBar);
            }
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        strReq.setShouldCache(false);

        queue.add(strReq);

    }


    /*calling  shareDeepLink*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public static void shareDeepLink(Context context, String message, Boolean isWhatsapp) {
        Logger.i(TAG, "shareDeepLink");
        Logger.i(TAG, "\n ------------ shareBody ------------");
        Logger.i(TAG, "\n shareBody: " + message);
        if(isWhatsapp) {
            try {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setPackage("com.whatsapp");
                intent.putExtra(Intent.EXTRA_TEXT, message);
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "Some error occured while sharing via whatsapp. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }else{
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            sharingIntent.setType("text/plain");
//        sharingIntent.setPackage("com.whatsapp");
//        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject.trim());
            sharingIntent.putExtra(Intent.EXTRA_TEXT, message);

            context.startActivity(Intent.createChooser(sharingIntent, context.getResources().getString(R.string.app_name) + " Share"));
        }
    }

    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    /*calling  getBase64Encoded*/
    public String getBase64Encoded(String original_string) {
        Logger.i(TAG, "getBase64Encoded");
        String base64encodedString = "";
        try {
            base64encodedString = Base64Utils.encodeUrlSafe(original_string.getBytes("ISO-8859-1"));
            System.out.println("Base64 Encoded String :" + base64encodedString);

        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }

        return base64encodedString;
    }

    /*calling  getBase64Decoded*/
    public String getBase64Decoded(String encoded_string) {
        Logger.i(TAG, "getBase64Decoded");
        String base64encodedString = "";
        byte[] base64encodedBytes = null;
        try {
            base64encodedBytes = Base64Utils.decodeUrlSafe(encoded_string);
            base64encodedString = new String(base64encodedBytes, "UTF-8");
            base64encodedString = new String(base64encodedString.getBytes("ISO-8859-1"), "UTF-8");
            base64encodedString=base64encodedString.replaceAll("\\?","").toString().trim();
            System.out.println("Base64 Decoded String :" + base64encodedString);
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }

        return base64encodedString;
    }

    /*calling  getDeeplink*/
    public static void getDeeplink(Context context, Dialog mProgressBar, String subject, String shareBody, String longUrl, EnterMessageFragment.DeepLinkCallback deepLinkCallback) {
        Logger.i(TAG, "getDeeplink");
        RequestQueue queue = Volley.newRequestQueue(context);
        JSONObject jsonObjectParams = new JSONObject();
        DeepLinkRequestModel.DynamicLinkInfo.IosInfo iosInfo = new DeepLinkRequestModel.DynamicLinkInfo.IosInfo("in.woloo.app");
        DeepLinkRequestModel.DynamicLinkInfo.AndroidInfo androidInfo = new DeepLinkRequestModel.DynamicLinkInfo.AndroidInfo("in.woloo.www");
        //new Share
        String shareTitle = subject;
        if (!TextUtils.isEmpty(shareTitle))
        {
            if (shareTitle.length()>20)
            {
                shareTitle = shareTitle.substring(0,20);
                shareTitle = shareTitle + "...";
            }
        }

        DeepLinkRequestModel.DynamicLinkInfo dynamicLinkInfo = new DeepLinkRequestModel.DynamicLinkInfo(iosInfo, androidInfo, longUrl, AppConstants.API_DEEP_LINK_DOMAIN_URI_PREFIX, null);
        DeepLinkRequestModel.Suffix suffix = new DeepLinkRequestModel.Suffix("SHORT");

        DeepLinkRequestModel deepLinkRequestModel = new DeepLinkRequestModel(dynamicLinkInfo, suffix);

        try {
            Gson gson = getGson();
            String json_string = gson.toJson(deepLinkRequestModel);
            if (json_string != null) {
                jsonObjectParams = new JSONObject(json_string);
            }
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }

        //JSONONB REQ

        JsonObjectRequest strReq = new JsonObjectRequest(Request.Method.POST, APIConstants.API_DEEP_LINK_SHORT_URL_API, jsonObjectParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ProgressBarUtils.dismissProgressDialog(mProgressBar);
                            Log("DEEPLINK API SUCCESS " + response);
                            String shortLink = "";
                            String previewLink = "";
                            if (response != null) {
                                shortLink = response.getString("shortLink");
                            }
                            deepLinkCallback.getDeepLink(shortLink);
                        } catch (Exception e) {
                             CommonUtils.printStackTrace(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ProgressBarUtils.dismissProgressDialog(mProgressBar);
            }
        });
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        strReq.setShouldCache(false);

        queue.add(strReq);
    }


    /*calling  getListOfInstalledApps*/
    //this function is used to get installed application
    public List<String> getListOfInstalledApps(Context context) {
        Logger.i(TAG, "getListOfInstalledApps");
        final PackageManager pm = context.getPackageManager();
        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        List<String> appNames = new ArrayList<>();
        for (ApplicationInfo packageInfo : packages) {
            appNames.add(packageInfo.packageName);
        }
        return appNames;
    }

    /*calling  getAppUpdateDialog*/
    public static Dialog getAppUpdateDialog(Context mContext, boolean cancelable, int type) {
        Logger.i(TAG, "getAppUpdateDialog");
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(cancelable);

        LayoutInflater factory = LayoutInflater.from(mContext);
        final View customPopupView = factory.inflate(
                R.layout.custome_app_update_popup, null);

        TextView mTitleText = customPopupView.findViewById(R.id.app_update_text);
        Button mCancelButton = customPopupView.findViewById(R.id.app_update_cancel_button);
        Button mUpdateButton = customPopupView.findViewById(R.id.app_update_update_button);
        mUpdateButton.setVisibility(View.VISIBLE);
        if (type == 1)
            mCancelButton.setVisibility(View.GONE);
        else
            mCancelButton.setVisibility(View.VISIBLE);

        dialog.setContentView(customPopupView);
        return dialog;
    }

    public static String googlemapapikey(Context context){
        AuthConfigResponse.Data authConfigResponse = null;
        String googlemapapikey="";
        try {
            Logger.i(TAG, "authconfig_response");
            authConfigResponse = SharedPrefSettings.Companion.getGetPreferences().fetchAuthConfig();
            if(authConfigResponse != null && authConfigResponse != null && authConfigResponse.getGoogle_maps() != null){
                if(!TextUtils.isEmpty(authConfigResponse.getGoogle_maps().getKey())){
                    googlemapapikey=authConfigResponse.getGoogle_maps().getKey();
                }
            }
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
        return googlemapapikey;
    }

    public static void showMaintenanceDialog(Context context,boolean isCancelable){
        try {
            AuthConfigResponse.Data authConfigResponse = SharedPrefSettings.Companion.getGetPreferences().fetchAuthConfig();
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(isCancelable);
            dialog.setCanceledOnTouchOutside(isCancelable);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent_background)));
            dialog.setContentView(R.layout.dialog_maintaince);
            TextView btnCloseDialog = dialog.findViewById(R.id.btnCloseDialog);
            btnCloseDialog.setOnClickListener(v -> {
                dialog.dismiss();
            });
            if(isCancelable)
                btnCloseDialog.setVisibility(View.VISIBLE);
            else
                btnCloseDialog.setVisibility(View.GONE);
            TextView textView = dialog.findViewById(R.id.maintenanceTextTv);
            textView.setText(authConfigResponse.getmAINTENANCESETTINGS().getMaintenanceMessage());
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.show();
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
    }

    public static void showCustomDialog(Context context, String msg) {
            try {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent_background)));
                dialog.setContentView(R.layout.dialog_login_failure);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                TextView btnCloseDialog = (TextView) dialog.findViewById(R.id.btnCloseDialog);

                TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
                tv_msg.setText(msg);

                btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
            } catch (Exception e) {
                 CommonUtils.printStackTrace(e);
            }
    }

    public static void showCustomDialogBackClick(Context context, String msg) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent_background)));
            dialog.setContentView(R.layout.dialog_login_failure);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            TextView btnCloseDialog = (TextView) dialog.findViewById(R.id.btnCloseDialog);

            TextView tv_msg = (TextView) dialog.findViewById(R.id.tv_msg);
            tv_msg.setText(msg);

            btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        ((Activity) context).onBackPressed();
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
             CommonUtils.printStackTrace(e);
        }
    }
}
