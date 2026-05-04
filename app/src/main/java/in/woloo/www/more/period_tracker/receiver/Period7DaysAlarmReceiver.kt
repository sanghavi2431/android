package `in`.woloo.www.more.period_tracker.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.presentation.activities.login.SplashScreenActivity
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger

class Period7DaysAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Logger.i(TAG, "onReceive EditCycleActivity")
        //        Logger.i(TAG, "Days : "+intent.getIntExtra(REMINDER_DAYS,0));
        val message = intent.getStringExtra("message")
        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        builder.setContentTitle("Period Remainder")
        builder.setContentText(message)
        builder.setLargeIcon(getBitmapFromVectorDrawable(context, R.drawable.ic_period))
        builder.setSmallIcon(R.drawable.ic_notification_woloo)
        builder.setColor(ContextCompat.getColor(context, R.color.text_color))
        builder.setAutoCancel(true)
        builder.setOngoing(false)
        builder.setWhen(System.currentTimeMillis())
        builder.setShowWhen(true)
        //builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        builder.setLargeIcon(getBitmapFromVectorDrawable(context, R.drawable.ic_period))

        val notifyIntent = Intent(
            context,
            SplashScreenActivity::class.java
        )
        notifyIntent.setAction(AppConstants.PERIOD_TRACKER_NOTIFICATION)
        // Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        // Create the PendingIntent
        val notifyPendingIntent = PendingIntent.getActivity(
            context,
            0,
            notifyIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        builder.setContentIntent(notifyPendingIntent)

        val notification = builder.build()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val importance = NotificationManager.IMPORTANCE_HIGH

                val notificationChannel =
                    NotificationChannel(NOTIFICATION_CHANNEL_ID, "Period Tracker", importance)

                notificationManager.createNotificationChannel(notificationChannel)
            }

            notificationManager.cancel(NOTIFICATION_ID)
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    private fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val bitmap = Bitmap.createBitmap(
            drawable!!.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    companion object {
        private const val TAG = "PeriodAlarmReceiver"
        const val NOTIFICATION_ID: Int = 1020
        const val NOTIFICATION_CHANNEL_ID: String = "10001"
        const val REMINDER_DAYS: String = "REMINDER_DAYS"
        const val days: Int = 7
    }
}
