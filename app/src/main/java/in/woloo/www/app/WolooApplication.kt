package `in`.woloo.www.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
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
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.model.server_response.SearchWolooResponse
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.fcm.DeeplinkReceiver
import `in`.woloo.www.more.models.ViewProfileResponse
import `in`.woloo.www.utils.Logger.d
import `in`.woloo.www.utils.Logger.e
import `in`.woloo.www.utils.Logger.i
import `in`.woloo.www.utils.Logger.v
import `in`.woloo.www.v2.profile.model.UserProfile
import io.hansel.core.logger.HSLLogLevel
import java.lang.ref.WeakReference
import android.content.res.Configuration
import android.os.Bundle
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import com.facebook.FacebookSdk
import java.io.File

class WolooApplication : Application(), SMTNotificationListener {
    @JvmField
    var nearByWoloo: NearByStoreResponse.Data? = null
    var updatedLikeStatus: Int = -1
    var profileResponseKotlin: UserProfile? = null
    private val subscriptionViewProfileResponse: ViewProfileResponse? = null
    private var data: SearchWolooResponse.Data.Woloo? = null
    var TAG: String = WolooApplication::class.java.simpleName

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.fontScale != 1.0f) {
            val config = Configuration(newConfig)
            config.fontScale = 1.0f
            resources.updateConfiguration(config, resources.displayMetrics)
        }
    }

    /*calling on onCreate*/
    @OptIn(UnstableApi::class)
    @SuppressLint("RestrictedApi")
    override fun onCreate() {
        super.onCreate()
        sdkInitialize(applicationContext)
        ExoPlayerCache.init(this)
        instance = this

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity
            }
            override fun onActivityPaused(activity: Activity) {
                if (currentActivity === activity) currentActivity = null
            }
            override fun onActivityDestroyed(activity: Activity) {}
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        })
        //
      //  Log.d("FB SDK 1", applicationId!!)
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

        val logger = AppEventsLogger.newLogger(this)
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

        logCustomAppInstall()

        init(this)
        init(RewordInterceptor)
        i(TAG, "onCreate")
        setUpNetCore()

    }

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
        val deeplinkReceiver = DeeplinkReceiver()
        val filter = IntentFilter("com.smartech.EVENT_PN_INBOX_CLICK")
        //this.registerReceiver(deeplinkReceiver, filter); // commented by Aarati while version upgrade. and following block added
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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

    fun getProfileResponse(): UserProfile? {
        return profileResponseKotlin
    }

    fun setProfileResponse(profileResponseKotlin: UserProfile?) {
        this.profileResponseKotlin = profileResponseKotlin
    }

    var searchedWoloo: SearchWolooResponse.Data.Woloo?
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
            CommonUtils.printStackTrace(e)
        }
    }


    @UnstableApi
    object ExoPlayerCache {

        private const val MAX_CACHE_SIZE = 100L * 1024 * 1024 // 100 MB

        private var _simpleCache: SimpleCache? = null

        fun init(context: Context) {
            if (_simpleCache == null) {
                val cacheDir = File(context.cacheDir, "exo_cache")
                _simpleCache = SimpleCache(cacheDir, LeastRecentlyUsedCacheEvictor(MAX_CACHE_SIZE))
            }
        }

        val simpleCache: SimpleCache
            get() = _simpleCache ?: throw IllegalStateException("ExoPlayerCache not initialized")
    }



    override fun getSmartechNotifications(data: String, from: Int) {
        SmartPush.getInstance(
            WeakReference(
                applicationContext
            )
        ).renderNotification(data, from)
    }

    private fun logCustomAppInstall() {

    }

    companion object {
        @get:Synchronized
        var instance: WolooApplication? = null
            private set

        private const val AF_DEV_KEY = "i6aoJbBqs6pWjzSvo5hbtJ"
        @JvmField
        var errorMessage: String = ""

        var currentActivity: Activity? = null
    }
}
