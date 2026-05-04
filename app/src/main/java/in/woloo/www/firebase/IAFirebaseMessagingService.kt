package `in`.woloo.www.firebase

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.api_classes.JSONTagConstant
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger.e
import `in`.woloo.www.utils.Logger.w
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class IAFirebaseMessagingService : FirebaseMessagingService() {
    private val notificationUtils: NotificationUtils? = null
    private val bitmap: Bitmap? = null
    private val parentId = 0
    private val childTabId = 0
    private val contentId: String? = null
    private val clickToActionUrl: String? = null
    private val storeId = 0
    private var notificationId: String? = null
    private var superStoreId: String? = null
    private val intSuperStoreId = 0
    private var broadcaster: LocalBroadcastManager? = null

    override fun onCreate() {
        super.onCreate()
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        w(TAG, "++++++++++++++++++ FCM Token ++++++++++++++++++")
        if (!TextUtils.isEmpty(token)) {
            w(
                TAG,
                "FCM Token: $token"
            )
            storeTokenInPref(token)
            //            sendLocalBroadcast(token);
        }
    }

    private fun sendLocalBroadcast(token: String) {
        val intent = Intent(AppConstants.PUT_LOCAL_BROADCAST)
        intent.putExtra(AppConstants.PUT_EXTRA_WEBVIEW_TITLE, token)
        broadcaster!!.sendBroadcast(intent)
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        e(TAG, "From: " + remoteMessage.from)

        if (remoteMessage == null) return

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
//            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            try {
                e(TAG, "Data Payload: " + remoteMessage.data.toString())
                val params = remoteMessage.data
                if (params != null && params.size > 2) {
                    e("params", params.toString())
                    handleDataMessage(params)
                }
            } catch (e: Exception) {
                e(TAG, "Exception: " + e.message)
            }
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private fun sendNotification(
        image: Bitmap?,
        title: String?,
        contentText: String?,
        summaryText: String?,
        tickerText: String?,
        jsonStr_notification: String?, notificationId: String?, notificationType: String
    ) {
        w(
            "FirebaseService", "Title - " + title +
                    ", contentText- " + contentText +
                    ", summaryText- " + summaryText +
                    ", tickerText- " + tickerText +
                    ", jsonStr_notification- " + jsonStr_notification
        )


        val notificationHelper = NotificationHelper(this)
        val notifBuilder = notificationHelper
            .getNotificationChennelForApp(
                image,
                title,
                contentText,
                tickerText,
                summaryText, jsonStr_notification, notificationId, ""
            )

        notificationHelper.manager!!.notify(notificationHelper.generateRandom(), notifBuilder.build())

        val mSharedPreference: SharedPreference = SharedPreference(applicationContext)
        mSharedPreference.setStoredBooleanPreference(
            applicationContext,
            SharedPreferencesEnum.All_NOTIFICATION_SEEN.getPreferenceKey(),
            false
        )
    }

    /*
     *To get a Bitmap image from the URL received
     * */
    private fun getBitmapfromUrl(imageUrl: String?): Bitmap? {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            return BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
            return null
        }
    }


    private fun storeTokenInPref(token: String) {
        val mSharedPreference: SharedPreference = SharedPreference(applicationContext)
        mSharedPreference.setStoredPreference(
            applicationContext,
            SharedPreferencesEnum.PUSH_TOKEN.getPreferenceKey(),
            token
        )
        w(
            TAG,
            "+++++++++++++ storeTokenInPref Get new Instance ID token : +++++++++++++ $token"
        )
    }

    private fun handleNotification(message: String) {
        /*if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(FCMConfig.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }*/
        val notificationHelper = NotificationHelper(this)
        val notifBuilder = notificationHelper
            .getNotificationChennelForApp(
                null,
                getString(R.string.app_name),
                message,
                "",
                "", "", "", ""
            )
        notificationHelper.manager!!.notify(11, notifBuilder.build())
    }

    private fun handleDataMessage(data_notify: Map<String, String>) {
        w(
            TAG,
            "push json: $data_notify"
        )
        try {
            var notificationDatabase: NotificationDB? = null
            if (notificationDatabase == null) notificationDatabase = NotificationDB(
                applicationContext
            )

            val notification = data_notify[JSONTagConstant.REDIRECTION_DATA]
            var notificationType = ""
            try {
                val jsonObj = JSONObject(notification.toString())
                if (jsonObj.has("notificationType")) {
                    notificationType = jsonObj["notificationType"].toString()
                }
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }

            val imageUri = data_notify["imageUrl"]

            var contentTitle: String? = null
            var contentText: String? = null
            var summaryText: String? = null
            var setTickerText: String? = null
            var landingUrl: String? = null
            try {
                contentTitle = data_notify["contentTitle"]
                contentText = data_notify["contentText"]
                superStoreId = data_notify["superStoreId"]
                summaryText = data_notify["summaryText"]
                setTickerText = data_notify["setTickerText"]
                //                landingUrl = data_notify.get("landingPage");
                landingUrl = data_notify[JSONTagConstant.REDIRECTION_DATA]
                notificationId = data_notify["contentId"]
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            w(
                TAG,
                "contentText: $contentText"
            )
            w(
                TAG,
                "contentTitle: $contentTitle"
            )
            w(
                TAG,
                "setTickerText: $setTickerText"
            )

            //            notificationDatabase.addNotifications(contentTitle, summaryText, contentText, imageUri, landingUrl, "0", notificationId);
            var bitmap: Bitmap? = null
            if (!TextUtils.isEmpty(imageUri)) {
                bitmap = getBitmapfromUrl(imageUri)
            }
            sendNotification(
                bitmap,
                contentTitle,
                contentText,
                summaryText,
                setTickerText,
                landingUrl, notificationId, notificationType
            )

            try {
                val commonUtils = CommonUtils()

                /*String moduleName = "";
                if(commonUtils.isCheckInfluencer(this)){
                    moduleName = "Store";
                }else{
                    moduleName = "User";
                }*/
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        } catch (e: Exception) {
            e(TAG, "Exception: " + e.message)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (broadcaster != null) {
            broadcaster = null
        }
    }

    companion object {
        private val TAG: String = IAFirebaseMessagingService::class.java.simpleName
    }
}
