package in.woloo.www.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;
import com.jetsynthesys.encryptor.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;



/**
 * Created by pawan.jha on 13-07-2018.
 */

public class ReferalReciever extends BroadcastReceiver {


    private static final String TAG = ReferalReciever.class.getName();

    public static final String INSTALL_REFERRER_ACTION = "com.android.vending.INSTALL_REFERRER";
    public static final String REFERRER = "referrer";
    public static final String REFERRAL_CODE = "utm_source";

    public static final String UTM_CAMPAIGN = "utm_campaign";
    public static final String UTM_SOURCE = "utm_source";
    public static final String UTM_MEDIUM = "utm_medium";
    public static final String UTM_TERM = "utm_term";
    public static final String UTM_CONTENT = "utm_content";

    private final String[] sources = {
            UTM_CAMPAIGN, UTM_SOURCE, UTM_MEDIUM, UTM_TERM, UTM_CONTENT
    };

    String utm_source = "null";
    String utm_campaign = "null";
    String utm_medium = "null";
    String utm_term = "null";
    String utm_content = "null";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {

            Logger.e("REFERAL RECIEVER - ", "LaunchReceiver.onReceive");
            final String action = intent.getAction();

            if (action.equalsIgnoreCase(INSTALL_REFERRER_ACTION)) {


                try {

                    if (BuildConfig.FLAVOR.contains("DT")) {

//                        if (new Prefs(context).getutmSoucre().isEmpty()) {
//                            new Prefs(context).setUtmSource("Digital_turbine_New");
//                        }
//
//                        if (new Prefs(context).getSegmentId().isEmpty()) {
//                            new Prefs(context).setSegmentId("MTQyMjA");
//                        }

                    } else if (BuildConfig.FLAVOR.contains("BJP")) {
//                        if (new Prefs(context).getutmSoucre().isEmpty()) {
//                            new Prefs(context).setUtmSource("BJP");
//                        }
//
//                        if (new Prefs(context).getSegmentId().isEmpty()) {
//                            new Prefs(context).setSegmentId("MTIyMTAyMTY");
//                        }

                    } else if (BuildConfig.FLAVOR.contains("Shivsena")) {
//                        if (new Prefs(context).getutmSoucre().isEmpty()) {
//                            new Prefs(context).setUtmSource("Shivsena");
//                        }
//                        if (new Prefs(context).getSegmentId().isEmpty()) {
//                            new Prefs(context).setSegmentId("MjE5MjE5");
//                        }
                    } else if (BuildConfig.FLAVOR.contains("NCP")) {
//                        if (new Prefs(context).getutmSoucre().isEmpty()) {
//                            new Prefs(context).setUtmSource("NCP");
//                        }
//
//                        if (new Prefs(context).getSegmentId().isEmpty()) {
//                            new Prefs(context).setSegmentId("MjE0MTMyMTY");
//                        }

                    } else {


                        String rawReferrer = intent.getStringExtra(REFERRER);
                        if (BuildConfig.DEBUG) {
                            rawReferrer = URLDecoder.decode(rawReferrer, "UTF-8");
                        }
//                        Log.printLog(TAG, "onReceive: rawReferrer: " + rawReferrer);

                        if (rawReferrer != null) {

                            Map<String, String> getParams = getHashMapFromQuery(rawReferrer);

                            if (!getParams.isEmpty()) {

                                for (String sourceType : sources) {

                                    String source = getParams.get(sourceType);

                                    if (source != null) {

                                        if (sourceType.equalsIgnoreCase(UTM_CAMPAIGN)) {
                                            utm_campaign = source;
                                        }

                                        if (sourceType.equalsIgnoreCase(UTM_SOURCE)) {
                                            utm_source = source;

//                                            if (new Prefs(context).getutmSoucre().isEmpty()) {
//                                                new Prefs(context).setUtmSource(utm_source);
//                                            }
                                        }

                                        if (sourceType.equalsIgnoreCase(UTM_MEDIUM)) {
                                            utm_medium = source;
                                        }

                                        if (sourceType.equalsIgnoreCase(UTM_TERM)) {
                                            utm_term = source;
                                        }

                                        if (sourceType.equalsIgnoreCase(UTM_CONTENT)) {
                                            utm_content = source;

                                            if (utm_content.equalsIgnoreCase("BJP")) {
//                                                if (new Prefs(context).getSegmentId().isEmpty()) {
//                                                    new Prefs(context).setSegmentId("MTIyMTAyMTY");
//                                                }
                                            }

                                            if (utm_content.equalsIgnoreCase("Shivsena")) {
//                                                if (new Prefs(context).getSegmentId().isEmpty()) {
//                                                    new Prefs(context).setSegmentId("MjE5MjE5");
//                                                }
                                            }
                                            if (utm_content.equalsIgnoreCase("NCP")) {
//                                                if (new Prefs(context).getSegmentId().isEmpty()) {
//                                                    new Prefs(context).setSegmentId("MjE0MTMyMTY");
//                                                }
                                            }

                                            try {
                                                if (utm_content != null && !utm_content.isEmpty()) {
//                                                    if (new Prefs(context).getSegmentId().isEmpty()) {
//                                                        new Prefs(context).setSegmentId(utm_content);
//                                                    }
                                                }
                                            } catch (Exception e) {
                                               Logger.e(TAG, e.getMessage());
                                            }
                                        }
                                    }
                                }

                            }
                        }
                    }
                    if (Build.VERSION.SDK_INT >= 23) {
//                        AppUtils.sendAnalyticsEvent(context, AppConstant.AppInit,
//                                "App_Installed",
//                                FirebaseInstanceId.getInstance().getToken(),
//                                "Firebase Topic- " + BuildConfig.NEW_NEW_INSTALL,
//                                "",
//                                "",
//                                "SCR_Referrer",
//                                "",
//                                "Start",
//                                new Prefs(context).getutmSoucre(),
//                                "UTM_CAMPAIGN: " + utm_campaign,
//                                "UTM_CONTENT: " + utm_content,
//                                "UTM_MEDIUM: " + utm_medium,
//                                "UTM_TERM: " + utm_term,
//                                new Prefs(context).getSegmentId(),
//                                String.valueOf(BuildConfig.VERSION_CODE));
//                        AppUtils.registerAndUnregisterTopic(BuildConfig.NEW_NEW_INSTALL);
                    }
                    else {
//                        FirebaseMessaging.getInstance().unsubscribeFromTopic(BuildConfig.REGISTERED);
//                        FirebaseMessaging.getInstance().unsubscribeFromTopic(BuildConfig.UNREGISTERED);
//                        FirebaseMessaging.getInstance().unsubscribeFromTopic(BuildConfig.REGISTERED);
//                        FirebaseMessaging.getInstance().subscribeToTopic(BuildConfig.NEW_INSTALL);
//                        AppUtils.sendAnalyticsEvent(context, AppConstant.AppInit,
//                                "App_Installed",
//                                FirebaseInstanceId.getInstance().getToken(),
//                                "Firebase Topic- " + BuildConfig.NEW_INSTALL,
//                                "",
//                                "",
//                                "SCR_Referrer",
//                                "",
//                                "Start",
//                                new Prefs(context).getutmSoucre(),
//                                "UTM_CAMPAIGN: " + utm_campaign,
//                                "UTM_CONTENT: " + utm_content,
//                                "UTM_MEDIUM: " + utm_medium,
//                                "UTM_TERM: " + utm_term,
//                                new Prefs(context).getSegmentId(),
//                                String.valueOf(BuildConfig.VERSION_CODE));
                    }
                } catch (Exception e) {
                    Logger.e(TAG, e.getMessage());
                }


            }

        } catch (Exception e) {
            Logger.e(TAG, "onReceive: " + e);
        }

       /* try {
            in.publicam.vitunes.utils.Log.d("REFERAL RECIEVER - ", "LaunchReceiver.onReceive");
            final String action = intent.getAction();
            Uri uri = intent.getData();

            if (action.equalsIgnoreCase(INSTALL_REFERRER_ACTION)) {

                String rawReferrer = intent.getStringExtra(REFERRER);
                System.out.println("raw- " + rawReferrer);

                if(BuildConfig.FLAVOR.equalsIgnoreCase("DT_prodViTunes")) {

                    if (new Prefs(context).getutmSoucre().isEmpty()) {
                        new Prefs(context).setUtmSource("Digital_turbine");
                    }



                }else {

                    if (rawReferrer != null) {

                        String referrer = URLDecoder.decode(rawReferrer, "UTF-8");

                        if (referrer.contains(REFERRAL_CODE)) {
                            try {
                                String[] referrerValue = referrer.split(REFERRAL_CODE + "=");
                                if (referrerValue[1] != null && !referrerValue[1].equalsIgnoreCase("")) {

                                    if(new Prefs(context).getutmSoucre().isEmpty())
                                        new Prefs(context).setUtmSource(referrerValue[1]);


                                } else {
                                    new Prefs(context).setUtmSource(referrer);

                                }

                            } catch (Exception e) {
                                Logger.e("ReferalReceiver", "onReceive: ", e);
                            }
                        } else {

                            new Prefs(context).setUtmSource(referrer);

                        }

                    }
                }
            }
        } catch (Exception e) {
           Logger.e(TAG, e);
        }*/
    }

    public static Map<String, String> getHashMapFromQuery(String query) throws UnsupportedEncodingException {

        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        Map<String, String> referrermap = new LinkedHashMap<String, String>();

        try {

            if (query.contains("&")) {

                String[] pairs = query.split("&");

                for (String pair : pairs) {
                    int idx = pair.indexOf("=");
                    referrermap.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
                }
            }

            try {
//                Log.printLog(TAG, "getHashMapFromQuery: " + referrermap.toString());
            } catch (Exception e) {
//                Log.printLog(TAG, "getHashMapFromQuery: " + e.toString());
            }
        } catch (Exception e) {
//            Logger.e(TAG, true, e);
        }

        return referrermap;
    }

}
