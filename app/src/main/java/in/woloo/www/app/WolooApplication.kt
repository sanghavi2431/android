package `in`.woloo.www.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.FacebookSdk.addLoggingBehavior
import com.facebook.FacebookSdk.fullyInitialize
import com.facebook.FacebookSdk.sdkInitialize
import com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled
import com.facebook.FacebookSdk.setAutoInitEnabled
import com.facebook.FacebookSdk.setAutoLogAppEventsEnabled
import com.facebook.FacebookSdk.setIsDebugEnabled
import com.facebook.LoggingBehavior
import com.facebook.appevents.AppEventsConstants.EVENT_PARAM_PRODUCT_APPLINK_ANDROID_APP_NAME
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.netcore.android.Smartech
import com.netcore.android.logger.SMTDebugLevel
import com.netcore.android.smartechpush.SmartPush
import com.netcore.android.smartechpush.notification.SMTNotificationListener
import com.netcore.android.smartechpush.notification.SMTNotificationOptions
import dev.b3nedikt.restring.Restring.init
import dev.b3nedikt.reword.RewordInterceptor
import dev.b3nedikt.viewpump.ViewPump.init
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.model.server_response.SearchWolooResponse.Data.Woloo
import `in`.woloo.www.application_kotlin.receiver.DeeplinkReceiver
import `in`.woloo.www.common.CommonUtils.Companion.printStackTrace
import `in`.woloo.www.more.editprofile.profile.model.UserProfile
import `in`.woloo.www.more.models.ViewProfileResponse
import `in`.woloo.www.utils.Logger.d
import `in`.woloo.www.utils.Logger.e
import `in`.woloo.www.utils.Logger.i
import `in`.woloo.www.utils.Logger.v
import io.hansel.core.logger.HSLLogLevel
import java.lang.ref.WeakReference


class WolooApplication : Application(), SMTNotificationListener {
    @JvmField
    var nearByWoloo: NearByStoreResponse.Data? = null
    var updatedLikeStatus: Int = -1
    var profileResponse: UserProfile? = null
    private val subscriptionViewProfileResponse: ViewProfileResponse? = null
    private var data: Woloo? = null
    var TAG: String = WolooApplication::class.java.simpleName

    /*calling on onCreate*/
    @SuppressLint("RestrictedApi")
    override fun onCreate() {
        super.onCreate()
        sdkInitialize(applicationContext)
       // Log.d("FB SDK 1", applicationId!!)
        //MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, this);
        /*AppLinkData.fetchDeferredAppLinkData(this,
                appLinkData -> {
                    Toast.makeText(this, appLinkData.toString(), Toast.LENGTH_SHORT).show();
                }
        );*/
        AppEventsLogger.activateApp(this)

        setAutoLogAppEventsEnabled(true) // Added by Aarati


        setAutoInitEnabled(true) // Added by Aarati

        fullyInitialize() // Added by Aarati

        setAdvertiserIDCollectionEnabled(true)

        val logger: AppEventsLogger = AppEventsLogger.newLogger(this)
        logger.logEvent(EVENT_PARAM_PRODUCT_APPLINK_ANDROID_APP_NAME)
        setIsDebugEnabled(true)
        addLoggingBehavior(LoggingBehavior.APP_EVENTS)


        instance = this
        setCrashlytics()

        val conversionListener: AppsFlyerConversionListener = object : AppsFlyerConversionListener {
            override fun onConversionDataSuccess(conversionData: Map<String, Any>) {
                for (attrName in conversionData.keys) {
                    d("LOG_TAG", "attribute: " + attrName + " = " + conversionData[attrName])
                }
            }

            override fun onConversionDataFail(s: String) {
            }

            override fun onAppOpenAttribution(conversionData: Map<String, String>) {
                for (attrName in conversionData.keys) {
                    d("LOG_TAG", "attribute: " + attrName + " = " + conversionData[attrName])
                }
            }

            override fun onAttributionFailure(s: String) {
            }
        }
        AppsFlyerLib.getInstance().init(
            AF_DEV_KEY, conversionListener,
            this
        )
        AppsFlyerLib.getInstance().start(this)

        init(this)
        init(RewordInterceptor)
        i(TAG, "onCreate")
        setUpNetCore()
    }

    @SuppressLint("InlinedApi")
    private fun setUpNetCore() {
        val smartech = Smartech.getInstance(WeakReference(this.applicationContext))
        smartech.initializeSdk(this)
        smartech.trackAppInstallUpdateBySmartech()

        if (BuildConfig.DEBUG) {
            smartech.setDebugLevel(SMTDebugLevel.Level.VERBOSE) //9
            // enabling logs for nudges prodcut
            HSLLogLevel.all.isEnabled = true
            HSLLogLevel.mid.isEnabled = true
            HSLLogLevel.debug.isEnabled = true
        }

        try {
            val smartPush = SmartPush.getInstance(WeakReference(this))
            smartPush.fetchAlreadyGeneratedTokenFromFCM()
            //            smartPush.setDevicePushToken(FirebaseInstanceId.getInstance().getToken());
            e(TAG, "token " + smartPush.getDevicePushToken())
        } catch (e: Exception) {
            e(TAG, "Fetching FCM token failed.")
        }
        v("DeeplinkReceiver", "Before")
        val deeplinkReceiver: DeeplinkReceiver = DeeplinkReceiver()
        val filter = IntentFilter("com.smartech.EVENT_PN_INBOX_CLICK")
        //this.registerReceiver(deeplinkReceiver, filter); // commented by Aarati while version upgrade. and following block added
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            applicationContext.registerReceiver(deeplinkReceiver, filter, RECEIVER_EXPORTED)
        } else {
            applicationContext.registerReceiver(deeplinkReceiver, filter, RECEIVER_EXPORTED)
        }
        v("DeeplinkReceiver", "After.")


        val options = SMTNotificationOptions(this)
        options.brandLogo = "logo" //e.g.logo is sample name for brand logo
        options.largeIcon = "icon_nofification" //e.g.ic_notification is sample name for large icon
        options.smallIcon = "ic_notification_woloo" //e.g.ic_action_play is sample name for icon
        options.smallIconTransparent =
            "ic_notification_woloo" //e.g.ic_action_play is sample name for transparent small icon
        options.transparentIconBgColor = "#414042"
        options.placeHolderIcon =
            "ic_notification" //e.g.ic_notification is sample name for placeholder icon
        SmartPush.getInstance(WeakReference(this)).setNotificationOptions(options)
    }

    var searchedWoloo: Woloo?
        get() = data
        set(data) {
            this.data = data
        }

    /*calling on setCrashlytics*/
    private fun setCrashlytics() {
        i(TAG, "setCrashlytics")
        try {
            val mStrLiveUrl = BuildConfig.LIVE_URL
            e("MSTRLIVE", mStrLiveUrl)
            val crashlytics = FirebaseCrashlytics.getInstance()
            //in firebase there was a crash on this line (NullPointerException)
            if (mStrLiveUrl != null) {
                if (mStrLiveUrl.equals("3", ignoreCase = true)) {
                    e("MSTRLIVE", "CHCK1")
                    crashlytics.isCrashlyticsCollectionEnabled = true
                } else {
                    crashlytics.isCrashlyticsCollectionEnabled = false
                }
            }
        } catch (e: Exception) {
            e("APP EXCEPTION", e.message!!)
            printStackTrace(e)
        }
    }


    override fun getSmartechNotifications(data: String, from: Int) {
        SmartPush.getInstance(
            WeakReference(
                applicationContext
            )
        ).renderNotification(data, from)
    }

    fun setNearByWoloo(nearByWoloo: NearByStoreResponse.Data) {
        this.nearByWoloo = nearByWoloo
    }

    fun getNearByWoloo(): NearByStoreResponse.Data? {
        return nearByWoloo
    }

    companion object {
        @get:Synchronized
        var instance: WolooApplication? = null
            private set

        private const val AF_DEV_KEY = "i6aoJbBqs6pWjzSvo5hbtJ"
        @JvmField
        var errorMessage: String = ""
    }
}
