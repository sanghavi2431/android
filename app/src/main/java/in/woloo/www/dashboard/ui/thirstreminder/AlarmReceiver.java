package in.woloo.www.dashboard.ui.thirstreminder;

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

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    public static int NOTIFICATION_ID = 1010;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    public static final String REMINDER_HOURS = "THIRST_HOURS";

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.i(TAG, "onReceive");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");
        builder.setContentTitle("Thirst reminder");
        builder.setContentText("It's time to drink water!");
        //builder.setLargeIcon(getBitmapFromVectorDrawable(context, R.drawable.ic_thirst));
        builder.setSmallIcon(R.drawable.ic_notification_woloo);
        builder.setColor(ContextCompat.getColor(context, R.color.text_color));
        builder.setAutoCancel(true);
        builder.setOngoing(false);
        builder.setWhen(System.currentTimeMillis());
        builder.setShowWhen(true);
        builder.setChannelId(AlarmReceiver.NOTIFICATION_CHANNEL_ID);

        Intent notifyIntent = new Intent(context, SplashActivity.class);
        notifyIntent.setAction(AppConstants.THIRST_REMINDER_NOTIFICATION);
        // Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Create the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(notifyPendingIntent);

        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Thirst reminder", importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.cancel(NOTIFICATION_ID);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
