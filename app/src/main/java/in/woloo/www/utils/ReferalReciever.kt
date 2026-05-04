package `in`.woloo.www.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.jetsynthesys.encryptor.BuildConfig
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

/**
 * Created by pawan.jha on 13-07-2018.
 */
class ReferalReciever : BroadcastReceiver() {
    private val sources = arrayOf(
        UTM_CAMPAIGN, UTM_SOURCE, UTM_MEDIUM, UTM_TERM, UTM_CONTENT
    )

    var utm_source: String = "null"
    var utm_campaign: String = "null"
    var utm_medium: String = "null"
    var utm_term: String = "null"
    var utm_content: String = "null"

    override fun onReceive(context: Context, intent: Intent) {
        try {
            Logger.e("REFERAL RECIEVER - ", "LaunchReceiver.onReceive")
            val action = intent.action

            if (action.equals(INSTALL_REFERRER_ACTION, ignoreCase = true)) {
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
                        var rawReferrer = intent.getStringExtra(REFERRER)
                        if (BuildConfig.DEBUG) {
                            rawReferrer = URLDecoder.decode(rawReferrer, "UTF-8")
                        }

                        //                        Log.printLog(TAG, "onReceive: rawReferrer: " + rawReferrer);
                        if (rawReferrer != null) {
                            val getParams = getHashMapFromQuery(rawReferrer)

                            if (!getParams.isEmpty()) {
                                for (sourceType in sources) {
                                    val source = getParams[sourceType]

                                    if (source != null) {
                                        if (sourceType.equals(UTM_CAMPAIGN, ignoreCase = true)) {
                                            utm_campaign = source
                                        }

                                        if (sourceType.equals(UTM_SOURCE, ignoreCase = true)) {
                                            utm_source = source

                                            //                                            if (new Prefs(context).getutmSoucre().isEmpty()) {
//                                                new Prefs(context).setUtmSource(utm_source);
//                                            }
                                        }

                                        if (sourceType.equals(UTM_MEDIUM, ignoreCase = true)) {
                                            utm_medium = source
                                        }

                                        if (sourceType.equals(UTM_TERM, ignoreCase = true)) {
                                            utm_term = source
                                        }

                                        if (sourceType.equals(UTM_CONTENT, ignoreCase = true)) {
                                            utm_content = source

                                            if (utm_content.equals("BJP", ignoreCase = true)) {
//                                                if (new Prefs(context).getSegmentId().isEmpty()) {
//                                                    new Prefs(context).setSegmentId("MTIyMTAyMTY");
//                                                }
                                            }

                                            if (utm_content.equals("Shivsena", ignoreCase = true)) {
//                                                if (new Prefs(context).getSegmentId().isEmpty()) {
//                                                    new Prefs(context).setSegmentId("MjE5MjE5");
//                                                }
                                            }
                                            if (utm_content.equals("NCP", ignoreCase = true)) {
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
                                            } catch (e: Exception) {
                                                Logger.e(TAG, e.message.toString())
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
                    } else {
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
                } catch (e: Exception) {
                    Logger.e(TAG, e.message.toString())
                }
            }
        } catch (e: Exception) {
            Logger.e(TAG, "onReceive: $e")
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

    companion object {
        private val TAG: String = ReferalReciever::class.java.name

        const val INSTALL_REFERRER_ACTION: String = "com.android.vending.INSTALL_REFERRER"
        const val REFERRER: String = "referrer"
        const val REFERRAL_CODE: String = "utm_source"

        const val UTM_CAMPAIGN: String = "utm_campaign"
        const val UTM_SOURCE: String = "utm_source"
        const val UTM_MEDIUM: String = "utm_medium"
        const val UTM_TERM: String = "utm_term"
        const val UTM_CONTENT: String = "utm_content"

        @Throws(UnsupportedEncodingException::class)
        fun getHashMapFromQuery(query: String): Map<String, String> {
            val query_pairs: Map<String, String> = LinkedHashMap()
            val referrermap: MutableMap<String, String> = LinkedHashMap()

            try {
                if (query.contains("&")) {
                    val pairs =
                        query.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

                    for (pair in pairs) {
                        val idx = pair.indexOf("=")
                        referrermap[URLDecoder.decode(pair.substring(0, idx), "UTF-8")] =
                            URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
                    }
                }

                try {
//                Log.printLog(TAG, "getHashMapFromQuery: " + referrermap.toString());
                } catch (e: Exception) {
//                Log.printLog(TAG, "getHashMapFromQuery: " + e.toString());
                }
            } catch (e: Exception) {
//            Logger.e(TAG, true, e);
            }

            return referrermap
        }
    }
}
