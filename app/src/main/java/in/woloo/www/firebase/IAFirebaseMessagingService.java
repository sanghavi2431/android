package in.woloo.www.firebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import in.woloo.www.R;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;


public class IAFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = IAFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;
    private Bitmap bitmap;
    private int parentId;
    private int childTabId;
    private String contentId;
    private String clickToActionUrl;
    private int storeId;
    private String notificationId;
    private String superStoreId;
    private int intSuperStoreId;
    private LocalBroadcastManager broadcaster;

    @Override
    public void onCreate() {
        super.onCreate();
        broadcaster = LocalBroadcastManager.getInstance(this);
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Logger.w(TAG, "++++++++++++++++++ FCM Token ++++++++++++++++++");
        if (!TextUtils.isEmpty(token)) {
            Logger.w(TAG, "FCM Token: " + token);
            storeTokenInPref(token);
//            sendLocalBroadcast(token);
        }
    }

    private void sendLocalBroadcast(String token) {
        Intent intent = new Intent(AppConstants.PUT_LOCAL_BROADCAST);
        intent.putExtra(AppConstants.PUT_EXTRA_WEBVIEW_TITLE, token);
        broadcaster.sendBroadcast(intent);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
//            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            try {
                Logger.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
                Map<String, String> params = remoteMessage.getData();
                if (params != null && params.size()>2) {
                    Logger.e("params", params.toString());
                    handleDataMessage(params);
                }
            } catch (Exception e) {
                Logger.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private void sendNotification(Bitmap image,
                                  String title,
                                  String contentText,
                                  String summaryText,
                                  String tickerText,
                                  String jsonStr_notification, String notificationId, String notificationType) {

        Logger.w("FirebaseService", "Title - " + title +
                ", contentText- " + contentText +
                ", summaryText- " + summaryText +
                ", tickerText- " + tickerText +
                ", jsonStr_notification- " + jsonStr_notification);


        NotificationHelper notificationHelper = new NotificationHelper(this);
        NotificationCompat.Builder notifBuilder = notificationHelper
                .getNotificationChennelForApp(image,
                        title,
                        contentText,
                        tickerText,
                        summaryText,jsonStr_notification, notificationId, "");

        notificationHelper.getManager().notify(notificationHelper.generateRandom(), notifBuilder.build());

        SharedPreference mSharedPreference = new SharedPreference(getApplicationContext());
        mSharedPreference.setStoredBooleanPreference(getApplicationContext(), SharedPreferencesEnum.All_NOTIFICATION_SEEN.getPreferenceKey(), false);



    }

    /*
     *To get a Bitmap image from the URL received
     * */
    private Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
            return null;
        }
    }


    private void storeTokenInPref(String token) {
        SharedPreference mSharedPreference = new SharedPreference(getApplicationContext());
        mSharedPreference.setStoredPreference(getApplicationContext(), SharedPreferencesEnum.PUSH_TOKEN.getPreferenceKey(), token);
        Logger.w(TAG, "+++++++++++++ storeTokenInPref Get new Instance ID token : +++++++++++++ " + token);
    }

    private void handleNotification(String message) {
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
        NotificationHelper notificationHelper = new NotificationHelper(this);
        NotificationCompat.Builder notifBuilder = notificationHelper
                .getNotificationChennelForApp(null,
                        getString(R.string.app_name),
                        message,
                        "",
                        "","", "", "");
        notificationHelper.getManager().notify(11, notifBuilder.build());
    }

    private void handleDataMessage(Map<String, String> data_notify) {
        Logger.w(TAG, "push json: " + data_notify.toString());
        try {
            NotificationDB notificationDatabase = null;
            if (notificationDatabase == null)
                notificationDatabase = new NotificationDB(getApplicationContext());

            String notification =data_notify.get(JSONTagConstant.REDIRECTION_DATA);
            String notificationType = "";
            try{
                JSONObject jsonObj = new JSONObject(notification.toString());
                if(jsonObj.has("notificationType")){
                    notificationType = jsonObj.get("notificationType").toString();
                }
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }

            String imageUri = data_notify.get("imageUrl");

            String contentTitle = null;
            String contentText = null;
            String summaryText = null;
            String setTickerText = null;
            String landingUrl = null;
            try {
                contentTitle = data_notify.get("contentTitle");
                contentText = data_notify.get("contentText");
                superStoreId = data_notify.get("superStoreId");
                summaryText = data_notify.get("summaryText");
                setTickerText = data_notify.get("setTickerText");
//                landingUrl = data_notify.get("landingPage");
                landingUrl = data_notify.get(JSONTagConstant.REDIRECTION_DATA);
                notificationId = data_notify.get("contentId");
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }

            Logger.w(TAG, "contentText: " + contentText);
            Logger.w(TAG, "contentTitle: " + contentTitle);
            Logger.w(TAG, "setTickerText: " + setTickerText);

//            notificationDatabase.addNotifications(contentTitle, summaryText, contentText, imageUri, landingUrl, "0", notificationId);

            Bitmap bitmap = null;
            if (!TextUtils.isEmpty(imageUri)) {
                bitmap = getBitmapfromUrl(imageUri);
            }
            sendNotification(bitmap,
                    contentTitle,
                    contentText,
                    summaryText,
                    setTickerText,
                    landingUrl,notificationId, notificationType);

            try{
                CommonUtils commonUtils = new CommonUtils();

                /*String moduleName = "";
                if(commonUtils.isCheckInfluencer(this)){
                    moduleName = "Store";
                }else{
                    moduleName = "User";
                }*/
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }


        } catch (Exception e) {
            Logger.e(TAG, "Exception: " + e.getMessage());
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcaster != null) {
            broadcaster = null;
        }
    }
}
