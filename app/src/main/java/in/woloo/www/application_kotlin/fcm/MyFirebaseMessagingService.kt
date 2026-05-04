package `in`.woloo.www.application_kotlin.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.netcore.android.smartechpush.SmartPush
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.presentation.activities.login.SplashScreenActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.SearchActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.utils.AppConstants.BLOG_CONTENT_NOTIFICATION
import `in`.woloo.www.utils.AppConstants.NEARBY_WOLOO_OFFERS
import `in`.woloo.www.utils.AppConstants.NEW_WOLOO_NOTIFICATION
import `in`.woloo.www.utils.AppConstants.PERIOD_TRACKER_NOTIFICATION
import `in`.woloo.www.utils.AppConstants.PRODUCT_OFFER_NOTIFICATION
import `in`.woloo.www.utils.Logger.d
import `in`.woloo.www.utils.Logger.w
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.URL

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var numMessages = 0
    var count: Int = 1

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        w(TAG, "++++++++++++++++++ FCM Token ++++++++++++++++++")
        if (!TextUtils.isEmpty(token)) {
            w(
                TAG,
                "FCM Token: $token"
            )
            storeTokenInPref(token)
            SmartPush.getInstance(WeakReference(this)).setDevicePushToken(token)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val notification = remoteMessage.notification
        val data = remoteMessage.data
        d("FROM", remoteMessage.data.toString())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Handling existing PN along with Netcore
            var pushFromSmartPush = false
            try {
                pushFromSmartPush = SmartPush.getInstance(WeakReference(this))
                    .isNotificationFromSmartech(JSONObject(remoteMessage.data.toString()))
            } catch (e: JSONException) {
                throw RuntimeException(e)
            }
            if (pushFromSmartPush) {
                SmartPush.getInstance(
                    WeakReference(
                        applicationContext
                    )
                ).handlePushNotification(remoteMessage.data.toString())
            } else {
                sendNotification(notification!!, data)
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun sendNotification(
        notification: RemoteMessage.Notification,
        data: Map<String, String>
    ) {
        val bundle = Bundle()
        bundle.putString(
            FCM_PARAM,
            data[FCM_PARAM]
        )
        val clickAction = notification.clickAction
        val intent: Intent
        if (clickAction != null && clickAction.equals(NEARBY_WOLOO_OFFERS, ignoreCase = true)) {
            intent = Intent(this, SearchActivity::class.java)
            //            bundle.putBoolean(SearchWolooActivity.ARG_SHOW_OFFERS, true);
        } else if (clickAction != null && clickAction.equals(
                BLOG_CONTENT_NOTIFICATION,
                ignoreCase = true
            )
        ) {
            val blogUrl = data["blog_link"]
            /*if (blogUrl != null) {
                intent = new Intent(this, WebViewActivity.class);
                bundle.putString(WebViewActivity.ARG_BLOG_URL, blogUrl);
            } else {*/
            intent = Intent(this, WolooDashboard::class.java)
            if (blogUrl != null) {
                intent.putExtra("blog_link", blogUrl)
            }
            bundle.putBoolean(WolooDashboard.ARG_IS_SHOW_TRENDING_BLOGS, true)

            //}
        } else if (clickAction != null && clickAction.equals(
                PERIOD_TRACKER_NOTIFICATION,
                ignoreCase = true
            )
        ) {
            intent = Intent(this, WolooDashboard::class.java)
            bundle.putBoolean(WolooDashboard.ARG_IS_SHOW_PERIOD_TRACKER, true)
        } else if (clickAction != null && clickAction.equals(
                NEW_WOLOO_NOTIFICATION,
                ignoreCase = true
            )
        ) {
            intent = Intent(this, WolooDashboard::class.java)
        } else if (clickAction != null && clickAction.equals(
                PRODUCT_OFFER_NOTIFICATION,
                ignoreCase = true
            )
        ) {
            intent = Intent(this, WolooDashboard::class.java)
            val couponCode = data["coupon_code"]
            bundle.putString("coupon_code", couponCode)
            bundle.putBoolean(WolooDashboard.ARG_IS_SHOW_SHOP, true)
        } else {
            intent = Intent(this, SplashScreenActivity::class.java)
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.putExtras(bundle)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            this, getString(R.string.notification_channel_id)
        )
            .setContentTitle(notification.title)
            .setContentText(notification.body)
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
            .setContentIntent(pendingIntent)
            .setContentInfo("Hello")
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round))
            .setColor(getColor(R.color.text_color))
            .setLights(Color.RED, 1000, 300)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setNumber(++numMessages)
            .setSmallIcon(R.drawable.ic_notification_woloo)

        try {
            val picture = data[FCM_PARAM]
            if (picture != null && "" != picture) {
                val url = URL(picture)
                val bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                notificationBuilder.setStyle(
                    NotificationCompat.BigPictureStyle().bigPicture(bigPicture)
                        .setSummaryText(notification.body)
                )
            }
        } catch (e: IOException) {
            CommonUtils.printStackTrace(e)
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.notification_channel_id),
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESC
            channel.setShowBadge(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)

            checkNotNull(notificationManager)
            notificationManager.createNotificationChannel(channel)
        }

        checkNotNull(notificationManager)
        val l = System.currentTimeMillis()
        val i = l.toInt()
        notificationManager.notify(i, notificationBuilder.build())
        //new AAsyncTask(notification).execute();
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

    companion object {
        private const val TAG = "FirebaseMessagingService"
        const val FCM_PARAM: String = "picture"
        private const val CHANNEL_NAME = "FCM"
        private const val CHANNEL_DESC = "Firebase Cloud Messaging"
    }
}