package in.woloo.www.period_tracker.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import in.woloo.www.R;
import in.woloo.www.login.SplashActivity;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;

public class Period7DaysAlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "PeriodAlarmReceiver";
    public static final int NOTIFICATION_ID = 1020;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    public static final String REMINDER_DAYS = "REMINDER_DAYS";
    public static final int days = 7;

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.i(TAG, "onReceive EditCycleActivity");
//        Logger.i(TAG, "Days : "+intent.getIntExtra(REMINDER_DAYS,0));
        String message = intent.getStringExtra("message");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle("Period Remainder");
        builder.setContentText(message);
        builder.setLargeIcon(getBitmapFromVectorDrawable(context, R.drawable.ic_period));
        builder.setSmallIcon(R.drawable.ic_notification_woloo);
        builder.setColor(ContextCompat.getColor(context, R.color.text_color));
        builder.setAutoCancel(true);
        builder.setOngoing(false);
        builder.setWhen(System.currentTimeMillis());
        builder.setShowWhen(true);
        //builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        builder.setLargeIcon(getBitmapFromVectorDrawable(context, R.drawable.ic_period));

        Intent notifyIntent = new Intent(context, SplashActivity.class);
        notifyIntent.setAction(AppConstants.PERIOD_TRACKER_NOTIFICATION);
        // Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Create the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(notifyPendingIntent);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                int importance = NotificationManager.IMPORTANCE_HIGH;

                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Period Tracker", importance);

                notificationManager.createNotificationChannel(notificationChannel);

            }

            notificationManager.cancel(NOTIFICATION_ID);
            notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

    private Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
