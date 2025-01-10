package `in`.woloo.www.application_kotlin.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.netcore.android.SMTBundleKeys
import `in`.woloo.www.application_kotlin.deeplink.DeeplinkDispatcherActivity
import `in`.woloo.www.utils.Logger

class DeeplinkReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
//        Logger.i("DeeplinkReceiver", "Deeplink: ");
        if (intent != null) {
            val bundleExtra = intent.extras
            if (bundleExtra != null) {
                if (bundleExtra.containsKey(SMTBundleKeys.SMT_BUNDLE_KEY_CLICK_DEEPLINK)) {
//                    Logger.v("Pn data", new Gson().toJson(bundleExtra));
                    val deepLinkvalue =
                        bundleExtra.getString(SMTBundleKeys.SMT_BUNDLE_KEY_CLICK_DEEPLINK)
                    // handle null or empty check before use the deepLinkValue for redirection
                    Logger.i("DeeplinkReceiver", "Deeplink: $deepLinkvalue")
                    if (!deepLinkvalue!!.isEmpty()) {
                        val dispatcherIntent =
                            Intent(context, DeeplinkDispatcherActivity::class.java)
                        dispatcherIntent.putExtra("deeplink", deepLinkvalue)
                        dispatcherIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(dispatcherIntent)
                    }
                } else {
                    Logger.v("DeeplinkReceiver", "does not have deeplink path.")
                }
                if (bundleExtra.containsKey(SMTBundleKeys.SMT_BUNDLE_KEY_CLICK_CUSTOM_PAYLOAD)) {
                    val customPayloadvalue =
                        bundleExtra.getString(SMTBundleKeys.SMT_BUNDLE_KEY_CLICK_CUSTOM_PAYLOAD)
                    // handle null or empty check before use the customPayloadvalue for redirection
                    Logger.i("DeeplinkReceiver", "Custom Payload: $customPayloadvalue")
                } else {
                    Logger.v("DeeplinkReceiver", "does not have custom payload.")
                }
            }
        }
    }

    companion object {
        var DEEPLINK_KEY = "deeplink"
    }
}
