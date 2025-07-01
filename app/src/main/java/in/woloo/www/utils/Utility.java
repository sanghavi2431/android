package in.woloo.www.utils;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.android.volley.VolleyError;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;
import com.netcore.android.Smartech;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.login.models.LoginResponse;
import in.woloo.www.more.models.EditProfileResponse;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.v2.splash.UserDetails;

public class Utility {

    public static InstallReferrerClient referrerClient;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void showKeyboard(Activity context) {
        if (context == null)
            return;
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (inputMethodManager != null)
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void locationChecker(GoogleApiClient mGoogleApiClient, final Activity activity) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
//                        Toast.makeText(activity,"All location settings are satisfied",Toast.LENGTH_SHORT).show();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    activity, 1000);
//                            Toast.makeText(activity,"RESOLUTION_REQUIRED",Toast.LENGTH_SHORT).show();
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
//                        Toast.makeText(activity,"Not Satisfied",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    public static void getInstallReferrer(Context mContext) {

        referrerClient = InstallReferrerClient.newBuilder(mContext).build();

        referrerClient.startConnection(new InstallReferrerStateListener() {

            @Override

            public void onInstallReferrerSetupFinished(int responseCode) {


                switch (responseCode) {

                    case InstallReferrerClient.InstallReferrerResponse.OK:
                        try {
                            // Connection established.
                            ReferrerDetails response = referrerClient.getInstallReferrer();

                            String referrerUrl = response.getInstallReferrer();

                            long referrerClickTime = response.getReferrerClickTimestampSeconds();

                            long appInstallTime = response.getInstallBeginTimestampSeconds();

                            boolean instantExperienceLaunched = response.getGooglePlayInstantParam();


//                            ReferalReciever.sendReferrerToServer(mContext, referrerUrl);


                            referrerClient.endConnection();

                        } catch (Exception e) {

                            Logger.e("Exception", e.getMessage());

                        }

                        break;

                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:

                        // API not available on the current Play Store app.

                        break;

                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:

                        // Connection couldn't be established.

                        break;

                }

            }


            @Override

            public void onInstallReferrerServiceDisconnected() {

                // Try to restart the connection on the next request to

                // Google Play by calling the startConnection() method.

            }

        });

    }

    public static void logFirebaseEvent(Context context, Bundle bundle, String event_name) {
        UserDetails userInfo = new CommonUtils().getUserInfo();
        bundle.putString(AppConstants.USER_ID, String.valueOf(userInfo.getId()));
        bundle.putString(AppConstants.DEVICE_PLATFORM, "Android");
        bundle.putString(AppConstants.CURRENT_DATE, getCurrentDate());
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mFirebaseAnalytics.logEvent(event_name, bundle);

        userJourney(context, bundle, event_name);
    }

    public static void logNetcoreEvent(Context context, HashMap<String,Object> payload, String event_name) {
        UserDetails userInfo = new CommonUtils().getUserInfo();
        payload.put(AppConstants.USER_ID, String.valueOf(userInfo.getId()));
        payload.put(AppConstants.DEVICE_PLATFORM, "Android");
        payload.put(AppConstants.CURRENT_DATE, getCurrentDate());
        Logger.i("Netcore",payload.toString());
        Smartech.getInstance(new WeakReference<>(context)).trackEvent(event_name, payload);
    }
    public static void userJourney(Context context, Bundle bundle, String eventName) {
        NetworkAPICall mNetworkAPICall = new NetworkAPICall();
        JSONObject mJsObjParam = new JSONObject();
        try {
            mJsObjParam.put("event_name", eventName);

            if (bundle != null && bundle.size() > 0) {
                JSONObject eventData = new JSONObject();
                for (String key : bundle.keySet()) {
                    eventData.put(key, bundle.get(key));
                }
                mJsObjParam.put("event_data", eventData);
            }
            Logger.i("Utility", mJsObjParam.toString());
        } catch (JSONException e) {
              CommonUtils.printStackTrace(e);
        }

        JetEncryptor mJetEncryptor = JetEncryptor.getInstance();
        Type parserType = new TypeToken<EditProfileResponse>() {
        }.getType();
        NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.USER_JOURNEY, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
        networkAPICallModel.setShowProgress(true);
        networkAPICallModel.setParserType(parserType);
        mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, null);
    }

    public static void logFirebaseMobileEvent(Context context, Bundle bundle, String event_name, String mobile) {
        bundle.putString(AppConstants.MOBILE, mobile);
        bundle.putString(AppConstants.DEVICE_PLATFORM, "Android");
        bundle.putString(AppConstants.CURRENT_DATE, getCurrentDate());
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mFirebaseAnalytics.logEvent(event_name, bundle);
    }
    public static String getCurrentDate() {
        long timestampMilliseconds = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String stringDate = simpleDateFormat.format(new Date(timestampMilliseconds));
        System.out.println(stringDate);
        return stringDate;
    }
}


