package in.woloo.www.firebase;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Random;

import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.login.SplashActivity;


public class NotificationHelper extends ContextWrapper {

    private static final String channelId = "channelId";
    private static final String channelName = "Notifications";

    private static final String channelIdForApp = "channelIdForApp";
    private static final String channelNameForApp = "Notifications";

    private NotificationManager notificationManager;

    public NotificationHelper(Context base) {
        super(base);

    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
        channel.enableLights(true);
        channel.enableVibration(false);
        //channel.setLightColor(R.color.colorPrimary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannelForApp() {
        NotificationChannel channel = new NotificationChannel(channelIdForApp, channelNameForApp, NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.enableVibration(true);
        //channel.setLightColor(R.color.colorPrimary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    public NotificationCompat.Builder getNotificationChannel(String title, String msg) {
        createChannel();
        return new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(msg)
                        .setBigContentTitle(title))
                .setContentTitle(title)
                .setContentText(msg);
                //.setSmallIcon(R.drawable.);
    }


    public NotificationCompat.Builder getNotificationChennelForApp(Bitmap largeIcon,
                                                                   String title,
                                                                   String contentText,
                                                                   String tickerText,
                                                                   String summaryText, String jsonStr_notification, String contentId, String notificationType) {

        Intent intent=new Intent(this, SplashActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelForApp();
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
       // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_icon);//ic_iconocle_app_launcher

        if (largeIcon != null) {
            return new NotificationCompat.Builder(getApplicationContext(), channelIdForApp)
                     .setStyle(new NotificationCompat.BigTextStyle()
                     .bigText(summaryText)
                     //Rahul made below change to avoid multiple type same Title show
                     //.setBigContentTitle(contentText)
                       .setBigContentTitle("Iconocle")
                     )
                    //Rahul change below
                   // .setContentTitle(title)
                    .setContentTitle("Iconocle")
                    //.setContentText(contentText)
                    .setContentText(summaryText)

                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    //.setSmallIcon(R.drawable.ic_app_notification)
                    //.setColor(getResources().getColor(R.color.notification_bg))
                    //.setLargeIcon(bitmap)
                    //.setSmallIcon(R.drawable.ic_notification_icon)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(largeIcon)
                            .setBigContentTitle(summaryText)
                    )
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }

        return new NotificationCompat.Builder(getApplicationContext(), channelIdForApp)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(summaryText)
                        //Rahul change below
                        //.setBigContentTitle(contentText)
                        .setBigContentTitle("Iconocle")
                )
                //Rahul change below
                //.setContentTitle(title)
                .setContentText(summaryText)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                //.setSmallIcon(R.drawable.ic_notification_icon)
                //.setColor(getResources().getColor(R.color.notification_bg))
                //.setLargeIcon(bitmap)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                /*ic_app_notification*/
    }



    public boolean isDashBoardActivityInBackground() {
        boolean isDashBoardActivityInBackground=false;
        try {
            ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
            List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

           if (taskList!=null && taskList.size()>1){
               isDashBoardActivityInBackground=true;
           }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
        return isDashBoardActivityInBackground;
    }

    public int generateRandom() {
        int randomNumber = 0;
        try{
            //rahul created new random function to resolve issue of some notification not trigger
            Random rand = new Random();
            int rand1 = rand.nextInt(99999999);
            long time = new Date().getTime();
            long timeMod = time%1000000; // last 6 digits of Time
            int randFinal = 100000000+rand1; // 9 Digit random Number generated
            long randfinal = randFinal + timeMod; // 9 Digit random Number + last 6 digits of Time.
            randomNumber = (int)randfinal; // Final numer converted into integer
        }catch (Exception e)
        {
              CommonUtils.printStackTrace(e);
            randomNumber = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        }
        return randomNumber;
    }



}
