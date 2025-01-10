package `in`.woloo.www.application_kotlin.utilities

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils.SimpleStringSplitter
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import sdk.vtion.`in`.service.NLService


class NotificationSettingsObserver(context: Context) :
    ContentObserver(Handler(Looper.getMainLooper())) {
    private val context: Context
    private val mainHandler: Handler

    init {
        this.context = context.applicationContext
        mainHandler = Handler(Looper.getMainLooper())
    }

    fun register() {
        context.contentResolver.registerContentObserver(
            Settings.Secure.getUriFor("enabled_notification_listeners"),
            false,
            this
        )
    }

    fun unregister() {
        context.contentResolver.unregisterContentObserver(this)
    }

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        mainHandler.post {
            val isEnabled = isNotificationListenerEnabled(context)

            // Send local broadcast
            val intent =
                Intent(ACTION_NOTIFICATION_LISTENER_STATE_CHANGED)
            intent.putExtra(EXTRA_IS_ENABLED, isEnabled)
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        }
    }

    private fun isNotificationListenerEnabled(context: Context): Boolean {
        val enabledListeners = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        )
        if (enabledListeners != null) {
            val expectedComponentName =
                context.packageName + "/" + NLService::class.java.canonicalName
            val splitter = SimpleStringSplitter(':')
            splitter.setString(enabledListeners)
            while (splitter.hasNext()) {
                val componentName = splitter.next()
                if (componentName.equals(expectedComponentName, ignoreCase = true)) {
                    return true
                }
            }
        }
        return false
    }

    companion object {
        const val ACTION_NOTIFICATION_LISTENER_STATE_CHANGED =
            "ACTION_NOTIFICATION_LISTENER_STATE_CHANGED"
        const val EXTRA_IS_ENABLED = "is_enabled"
    }
}