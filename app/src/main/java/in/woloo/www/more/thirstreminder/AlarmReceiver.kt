package `in`.woloo.www.more.thirstreminder

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

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Logger.i(TAG, "onReceive")
        val builder = NotificationCompat.Builder(context, "default")
        builder.setContentTitle("Thirst reminder")
        builder.setContentText("It's time to drink water!")
        //builder.setLargeIcon(getBitmapFromVectorDrawable(context, R.drawable.ic_thirst));
        builder.setSmallIcon(R.drawable.ic_notification_woloo)
        builder.setColor(ContextCompat.getColor(context, R.color.text_color))
        builder.setAutoCancel(true)
        builder.setOngoing(false)
        builder.setWhen(System.currentTimeMillis())
        builder.setShowWhen(true)
        builder.setChannelId(NOTIFICATION_CHANNEL_ID)
        val notifyIntent = Intent(context, SplashScreenActivity::class.java)
        notifyIntent.setAction(AppConstants.THIRST_REMINDER_NOTIFICATION)
        // Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, "Thirst reminder", importance)
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.cancel(NOTIFICATION_ID)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    fun getBitmapFromVectorDrawable(context: Context?, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(
            context!!, drawableId
        )
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
        private const val TAG = "AlarmReceiver"
        var NOTIFICATION_ID = 1010
        const val NOTIFICATION_CHANNEL_ID = "10001"
        const val REMINDER_HOURS = "THIRST_HOURS"
    }
}
