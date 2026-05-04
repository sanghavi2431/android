package `in`.woloo.www.more.period_tracker.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            // Start your service or schedule alarms here
            val serviceIntent = Intent(
                context,
                Period7DaysAlarmReceiver::class.java
            )
            context.startService(serviceIntent)
        }
    }
}
