package in.woloo.www.app;

import static com.facebook.FacebookSdk.AUTO_LOG_APP_EVENTS_ENABLED_PROPERTY;
import static com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled;
import static com.facebook.FacebookSdk.setAutoLogAppEventsEnabled;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.utils.Logger;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.applinks.AppLinkData;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.netcore.android.Smartech;
import com.netcore.android.logger.SMTDebugLevel;
import com.netcore.android.smartechpush.SmartPush;
import com.netcore.android.smartechpush.notification.SMTNotificationListener;
import com.netcore.android.smartechpush.notification.SMTNotificationOptions;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import dev.b3nedikt.restring.Restring;
import dev.b3nedikt.reword.RewordInterceptor;
import dev.b3nedikt.viewpump.ViewPump;
import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.fcm.DeeplinkReceiver;
import in.woloo.www.more.models.UserProfileMergedResponse;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.search.SearchWolooResponse;
import in.woloo.www.utils.Logger;
import in.woloo.www.v2.profile.model.UserProfile;
import io.hansel.core.logger.HSLLogLevel;
import io.hansel.hanselsdk.Hansel;
import io.hansel.hanselsdk.HanselDeepLinkListener;
import io.hansel.ujmtracker.HanselInternalEventsListener;
import io.hansel.ujmtracker.HanselTracker;

public class WolooApplication extends Application implements SMTNotificationListener {

    private static WolooApplication mInstance;

    private static final String AF_DEV_KEY = "i6aoJbBqs6pWjzSvo5hbtJ";
    private NearByStoreResponse.Data nearByWoloo;
    private int likeStatus = -1;
    private UserProfile profileResponse;
    private ViewProfileResponse subscriptionViewProfileResponse;
    private SearchWolooResponse.Data.Woloo data;
    public String TAG = WolooApplication.class.getSimpleName();

    private static String errorMessage = "";

    /*calling on onCreate*/
    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        Log.d("FB SDK 1" , FacebookSdk.getApplicationId());
        //MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, this);
        /*AppLinkData.fetchDeferredAppLinkData(this,
                appLinkData -> {
                    Toast.makeText(this, appLinkData.toString(), Toast.LENGTH_SHORT).show();
                }
        );*/
        AppEventsLogger.activateApp(this);

        setAutoLogAppEventsEnabled(true); // Added by Aarati


        FacebookSdk.setAutoInitEnabled(true); // Added by Aarati

        FacebookSdk.fullyInitialize();// Added by Aarati

        setAdvertiserIDCollectionEnabled(true);

        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent(AppEventsConstants.EVENT_PARAM_PRODUCT_APPLINK_ANDROID_APP_NAME);
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);


        mInstance = this;
        setCrashlytics();

        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener() {

            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {
                for (String attrName : conversionData.keySet()) {
                    Logger.d("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                }
            }

            @Override
            public void onConversionDataFail(String s) {

            }

            @Override
            public void onAppOpenAttribution(Map<String, String> conversionData) {
                for (String attrName : conversionData.keySet()) {
                    Logger.d("LOG_TAG", "attribute: " + attrName + " = " + conversionData.get(attrName));
                }
            }

            @Override
            public void onAttributionFailure(String s) {

            }
        };
        AppsFlyerLib.getInstance().init(AF_DEV_KEY, conversionListener, this);
        AppsFlyerLib.getInstance().start(this);

        Restring.init(this);
        ViewPump.init(RewordInterceptor.INSTANCE);
        Logger.i(TAG, "onCreate");
        setUpNetCore();
    }

    private void setUpNetCore(){
        Smartech smartech = Smartech.getInstance(new WeakReference<>(this.getApplicationContext()));
        smartech.initializeSdk(this);
        smartech.trackAppInstallUpdateBySmartech();

        if (BuildConfig.DEBUG) {
            smartech.setDebugLevel(SMTDebugLevel.Level.VERBOSE); //9
            // enabling logs for nudges prodcut
            HSLLogLevel.all.setEnabled(true);
            HSLLogLevel.mid.setEnabled(true);
            HSLLogLevel.debug.setEnabled(true);
        }

        try {
            SmartPush smartPush = SmartPush.getInstance(new WeakReference<>(this));
            smartPush.fetchAlreadyGeneratedTokenFromFCM();
//            smartPush.setDevicePushToken(FirebaseInstanceId.getInstance().getToken());
            Logger.e(TAG, "token "+ smartPush.getDevicePushToken());
        } catch (Exception e) {
            Logger.e(TAG, "Fetching FCM token failed.");
        }
        Logger.v("DeeplinkReceiver", "Before");
        DeeplinkReceiver deeplinkReceiver = new DeeplinkReceiver();
        IntentFilter filter = new IntentFilter("com.smartech.EVENT_PN_INBOX_CLICK");
        //this.registerReceiver(deeplinkReceiver, filter); // commented by Aarati while version upgrade. and following block added
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().registerReceiver(deeplinkReceiver, filter, RECEIVER_EXPORTED);
        }
        Logger.v("DeeplinkReceiver", "After.");

        SMTNotificationOptions options = new SMTNotificationOptions(this);
        options.setBrandLogo("logo"); //e.g.logo is sample name for brand logo
        options.setLargeIcon("icon_nofification");//e.g.ic_notification is sample name for large icon
        options.setSmallIcon("ic_notification_woloo"); //e.g.ic_action_play is sample name for icon
        options.setSmallIconTransparent("ic_notification_woloo"); //e.g.ic_action_play is sample name for transparent small icon
        options.setTransparentIconBgColor("#414042");
        options.setPlaceHolderIcon("ic_notification");//e.g.ic_notification is sample name for placeholder icon
        SmartPush.getInstance(new WeakReference(this)).setNotificationOptions(options);
    }

    public static synchronized WolooApplication getInstance() {
        return mInstance;
    }


    public void setNearByWoloo(NearByStoreResponse.Data nearByWoloo) {
        this.nearByWoloo = nearByWoloo;
    }

    public NearByStoreResponse.Data getNearByWoloo() {
        return nearByWoloo;
    }

    public int getUpdatedLikeStatus() {
        return likeStatus;
    }

    public void setUpdatedLikeStatus(int likeStatus) {
        this.likeStatus = likeStatus;
    }

    public UserProfile getProfileResponse() {
        return profileResponse;
    }

    public void setProfileResponse(UserProfile profileResponse) {
        this.profileResponse = profileResponse;
    }

    public static void setErrorMessage(String message){
        errorMessage = message;
    }

    public static String getErrorMessage(){
        return errorMessage;
    }

    public void setSearchedWoloo(SearchWolooResponse.Data.Woloo data) {
        this.data = data;
    }

    public SearchWolooResponse.Data.Woloo getSearchedWoloo() {
        return data;
    }

    /*calling on setCrashlytics*/
    private void setCrashlytics() {
        Logger.i(TAG, "setCrashlytics");
        try {
            String mStrLiveUrl = BuildConfig.LIVE_URL;
            Logger.e("MSTRLIVE", mStrLiveUrl);
            FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
            //in firebase there was a crash on this line (NullPointerException)
            if (mStrLiveUrl != null) {
                if (mStrLiveUrl.equalsIgnoreCase("3")) {
                    Logger.e("MSTRLIVE", "CHCK1");
                    crashlytics.setCrashlyticsCollectionEnabled(true);
                } else {
                    crashlytics.setCrashlyticsCollectionEnabled(false);
                }
            }
        } catch (Exception e) {
            Logger.e("APP EXCEPTION", e.getMessage());
              CommonUtils.printStackTrace(e);
        }

    }


    @Override
    public void getSmartechNotifications(@NonNull String data, int from) {
        SmartPush.getInstance(new WeakReference<Context>(getApplicationContext())).renderNotification(data,from);
    }
}
