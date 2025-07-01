package in.woloo.www.fcm;

import static in.woloo.www.utils.AppConstants.BLOG_CONTENT_NOTIFICATION;
import static in.woloo.www.utils.AppConstants.NEARBY_WOLOO_OFFERS;
import static in.woloo.www.utils.AppConstants.NEW_WOLOO_NOTIFICATION;
import static in.woloo.www.utils.AppConstants.PERIOD_TRACKER_NOTIFICATION;
import static in.woloo.www.utils.AppConstants.PRODUCT_OFFER_NOTIFICATION;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.netcore.android.smartechpush.SmartPush;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.Map;

import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.login.SplashActivity;
import in.woloo.www.search.SearchWolooActivity;
import in.woloo.www.utils.Logger;
import in.woloo.www.v2.search.SearchActivity;
import in.woloo.www.webview.WebViewActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingService";
    public static final String FCM_PARAM = "picture";
    private static final String CHANNEL_NAME = "FCM";
    private static final String CHANNEL_DESC = "Firebase Cloud Messaging";
    private int numMessages = 0;
    int count = 1;

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Logger.w(TAG, "++++++++++++++++++ FCM Token ++++++++++++++++++");
        if (!TextUtils.isEmpty(token)) {
            Logger.w(TAG, "FCM Token: " + token);
            storeTokenInPref(token);
            SmartPush.getInstance(new WeakReference<Context>(this)).setDevicePushToken(token);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        Map<String, String> data = remoteMessage.getData();
        Logger.d("FROM", remoteMessage.getData().toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Handling existing PN along with Netcore
            boolean pushFromSmartPush = false;
            try {
                pushFromSmartPush = SmartPush.getInstance(new WeakReference<Context>(this)).isNotificationFromSmartech(new JSONObject(remoteMessage.getData().toString()));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if(pushFromSmartPush){
                SmartPush.getInstance(new WeakReference<>(getApplicationContext())).handlePushNotification(remoteMessage.getData().toString());
            } else {
                sendNotification(notification, data);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        Bundle bundle = new Bundle();
        bundle.putString(FCM_PARAM, data.get(FCM_PARAM));
        String clickAction = notification.getClickAction();
        Intent intent;
        if (clickAction != null && clickAction.equalsIgnoreCase(NEARBY_WOLOO_OFFERS)) {
            intent = new Intent(this, SearchActivity.class);
//            bundle.putBoolean(SearchWolooActivity.ARG_SHOW_OFFERS, true);
        } else if (clickAction != null && clickAction.equalsIgnoreCase(BLOG_CONTENT_NOTIFICATION)) {
            String blogUrl = data.get("blog_link");
            /*if (blogUrl != null) {
                intent = new Intent(this, WebViewActivity.class);
                bundle.putString(WebViewActivity.ARG_BLOG_URL, blogUrl);
            } else {*/
            intent = new Intent(this, WolooDashboard.class);
            if (blogUrl != null) {
                intent.putExtra("blog_link", blogUrl);
            }
            bundle.putBoolean(WolooDashboard.ARG_IS_SHOW_TRENDING_BLOGS, true);
            //}

        } else if (clickAction != null && clickAction.equalsIgnoreCase(PERIOD_TRACKER_NOTIFICATION)) {
            intent = new Intent(this, WolooDashboard.class);
            bundle.putBoolean(WolooDashboard.ARG_IS_SHOW_PERIOD_TRACKER, true);
        } else if (clickAction != null && clickAction.equalsIgnoreCase(NEW_WOLOO_NOTIFICATION)) {
            intent = new Intent(this, WolooDashboard.class);
        } else if (clickAction != null && clickAction.equalsIgnoreCase(PRODUCT_OFFER_NOTIFICATION)) {
            intent = new Intent(this, WolooDashboard.class);
            String couponCode = data.get("coupon_code");
            bundle.putString("coupon_code", couponCode);
            bundle.putBoolean(WolooDashboard.ARG_IS_SHOW_SHOP, true);
        } else {
            intent = new Intent(this, SplashActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
                .setContentIntent(pendingIntent)
                .setContentInfo("Hello")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                .setColor(getColor(R.color.text_color))
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setNumber(++numMessages)
                .setSmallIcon(R.drawable.ic_notification_woloo);

        try {
            String picture = data.get(FCM_PARAM);
            if (picture != null && !"".equals(picture)) {
                URL url = new URL(picture);
                Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                notificationBuilder.setStyle(
                        new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
                );
            }
        } catch (IOException e) {
             CommonUtils.printStackTrace(e);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    getString(R.string.notification_channel_id), CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESC);
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        assert notificationManager != null;
        Long l = new Long(System.currentTimeMillis());
        int i = l.intValue();
        notificationManager.notify(i, notificationBuilder.build());
        //new AAsyncTask(notification).execute();
    }

    private void storeTokenInPref(String token) {
        SharedPreference mSharedPreference = new SharedPreference(getApplicationContext());
        mSharedPreference.setStoredPreference(getApplicationContext(), SharedPreferencesEnum.PUSH_TOKEN.getPreferenceKey(), token);
        Logger.w(TAG, "+++++++++++++ storeTokenInPref Get new Instance ID token : +++++++++++++ " + token);
    }
}