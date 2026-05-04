package `in`.woloo.www.firebase

import android.annotation.TargetApi
import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import `in`.woloo.www.application_kotlin.presentation.activities.login.SplashScreenActivity
import `in`.woloo.www.common.CommonUtils
import java.util.Date
import java.util.Random

class NotificationHelper(base: Context?) : ContextWrapper(base) {
    private var notificationManager: NotificationManager? = null

    @TargetApi(Build.VERSION_CODES.O)
    fun createChannel() {
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
        channel.enableLights(true)
        channel.enableVibration(false)
        //channel.setLightColor(R.color.colorPrimary);
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        manager!!.createNotificationChannel(channel)
    }

    @TargetApi(Build.VERSION_CODES.O)
    fun createChannelForApp() {
        val channel = NotificationChannel(
            channelIdForApp,
            channelNameForApp,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.enableLights(true)
        channel.enableVibration(true)
        //channel.setLightColor(R.color.colorPrimary);
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        manager!!.createNotificationChannel(channel)
    }

    val manager: NotificationManager?
        get() {
            if (notificationManager == null) {
                notificationManager =
                    getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            return notificationManager
        }

    fun getNotificationChannel(title: String?, msg: String?): NotificationCompat.Builder {
        createChannel()
        return NotificationCompat.Builder(applicationContext, channelId)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(msg)
                    .setBigContentTitle(title)
            )
            .setContentTitle(title)
            .setContentText(msg)
        //.setSmallIcon(R.drawable.);
    }


    fun getNotificationChennelForApp(
        largeIcon: Bitmap?,
        title: String?,
        contentText: String?,
        tickerText: String?,
        summaryText: String?,
        jsonStr_notification: String?,
        contentId: String?,
        notificationType: String?
    ): NotificationCompat.Builder {
        val intent = Intent(this, SplashScreenActivity::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannelForApp()
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0,
            intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification_icon);//ic_iconocle_app_launcher
        if (largeIcon != null) {
            return NotificationCompat.Builder(applicationContext, channelIdForApp)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText(summaryText) //Rahul made below change to avoid multiple type same Title show
                        //.setBigContentTitle(contentText)
                        .setBigContentTitle("Iconocle")
                ) //Rahul change below
                // .setContentTitle(title)
                .setContentTitle("Iconocle") //.setContentText(contentText)
                .setContentText(summaryText)

                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true) //.setSmallIcon(R.drawable.ic_app_notification)
                //.setColor(getResources().getColor(R.color.notification_bg))
                //.setLargeIcon(bitmap)
                //.setSmallIcon(R.drawable.ic_notification_icon)
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(largeIcon)
                        .setBigContentTitle(summaryText)
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        }

        return NotificationCompat.Builder(applicationContext, channelIdForApp)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(summaryText) //Rahul change below
                    //.setBigContentTitle(contentText)
                    .setBigContentTitle("Iconocle")
            ) //Rahul change below
            //.setContentTitle(title)
            .setContentText(summaryText)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true) //.setSmallIcon(R.drawable.ic_notification_icon)
            //.setColor(getResources().getColor(R.color.notification_bg))
            //.setLargeIcon(bitmap)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        /*ic_app_notification*/
    }


    val isDashBoardActivityInBackground: Boolean
        get() {
            var isDashBoardActivityInBackground = false
            try {
                val mngr =
                    getSystemService(ACTIVITY_SERVICE) as ActivityManager
                val taskList = mngr.getRunningTasks(10)

                if (taskList != null && taskList.size > 1) {
                    isDashBoardActivityInBackground = true
                }
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
            return isDashBoardActivityInBackground
        }

    fun generateRandom(): Int {
        var randomNumber = 0
        try {
            //rahul created new random function to resolve issue of some notification not trigger
            val rand = Random()
            val rand1 = rand.nextInt(99999999)
            val time = Date().time
            val timeMod = time % 1000000 // last 6 digits of Time
            val randFinal = 100000000 + rand1 // 9 Digit random Number generated
            val randfinal = randFinal + timeMod // 9 Digit random Number + last 6 digits of Time.
            randomNumber = randfinal.toInt() // Final numer converted into integer
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
            randomNumber = ((Date().time / 1000L) % Int.MAX_VALUE).toInt()
        }
        return randomNumber
    }


    companion object {
        private const val channelId = "channelId"
        private const val channelName = "Notifications"

        private const val channelIdForApp = "channelIdForApp"
        private const val channelNameForApp = "Notifications"
    }
}
