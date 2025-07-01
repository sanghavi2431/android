package in.woloo.www.fcm;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import in.woloo.www.utils.Logger;
import in.woloo.www.v2.DeeplinkDispatcherActivity;

import com.google.gson.Gson;
import com.netcore.android.SMTBundleKeys;

public class DeeplinkReceiver extends BroadcastReceiver {

    public static String DEEPLINK_KEY = "deeplink";

    @Override
    public void onReceive(Context context, Intent intent) {
//        Logger.i("DeeplinkReceiver", "Deeplink: ");

        if (intent != null) {
            Bundle bundleExtra = intent.getExtras();
            if (bundleExtra != null) {

                if (bundleExtra.containsKey(SMTBundleKeys.SMT_BUNDLE_KEY_CLICK_DEEPLINK)) {
//                    Logger.v("Pn data", new Gson().toJson(bundleExtra));
                    String deepLinkvalue = bundleExtra.getString(SMTBundleKeys.SMT_BUNDLE_KEY_CLICK_DEEPLINK);
                    // handle null or empty check before use the deepLinkValue for redirection
                    Logger.i("DeeplinkReceiver", "Deeplink: " + deepLinkvalue);

                    if(!deepLinkvalue.isEmpty()) {
                        Intent dispatcherIntent = new Intent(context, DeeplinkDispatcherActivity.class);
                        dispatcherIntent.putExtra("deeplink", deepLinkvalue);
                        dispatcherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(dispatcherIntent);
                    }
                } else {
                    Logger.v("DeeplinkReceiver", "does not have deeplink path.");
                }

                if (bundleExtra.containsKey(SMTBundleKeys.SMT_BUNDLE_KEY_CLICK_CUSTOM_PAYLOAD)) {
                    String customPayloadvalue = bundleExtra.getString(SMTBundleKeys.SMT_BUNDLE_KEY_CLICK_CUSTOM_PAYLOAD);
                    // handle null or empty check before use the customPayloadvalue for redirection
                    Logger.i("DeeplinkReceiver", "Custom Payload: " + customPayloadvalue);
                } else {
                    Logger.v("DeeplinkReceiver", "does not have custom payload.");
                }

            }
        }
    }
}
