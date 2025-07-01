package in.woloo.www.period_tracker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            // Start your service or schedule alarms here
            Intent serviceIntent = new Intent(context, Period7DaysAlarmReceiver.class);
            context.startService(serviceIntent);
        }
    }
}
