package `in`.woloo.www.application_kotlin.utilities

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils.SimpleStringSplitter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import sdk.vtion.`in`.sdkcore.UsersAccessibility



class AccessibilitySettingsObserver(context: Context) :
    ContentObserver(Handler(Looper.getMainLooper())) {
    private val context: Context
    private val mainHandler: Handler
    private var isBroadcastSent = false

    init {
        this.context = context.applicationContext
        mainHandler = Handler(Looper.getMainLooper())
    }

    fun register() {
        context.contentResolver.registerContentObserver(
            Settings.Secure.getUriFor(Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES),
            true,
            this
        )
        startPollingAccessibilityStatus()
    }

    private fun startPollingAccessibilityStatus() {
        mainHandler.postDelayed(object : Runnable {
            override fun run() {
                val isEnabled: Boolean = isAccessibilityServiceEnabled(context)
                if (isEnabled) {
                    // Stop polling
                    if (!isBroadcastSent) {
                        isBroadcastSent = true
                        Log.e("TAGACCS", "Control polling!!!!!!!!!!!!!!!!!!!!!!!")
                        // Send local broadcast
                        sendBroadCast(isEnabled)
                    }
                } else {
                    // Continue polling
                    mainHandler.postDelayed(this, 1000) // Poll every 1 second
                }
            }
        }, 1000) // Initial delay of 1 second
    }

    fun unregister() {
        context.contentResolver.unregisterContentObserver(this)
    }

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)
        if (!isBroadcastSent) {
            isBroadcastSent = true
            Log.e("TAGACCS", "Control!!!!!!!!!!!!!!!!!!")
            mainHandler.post {
                val isEnabled: Boolean =isAccessibilityServiceEnabled(context)

                // Send local broadcast
                sendBroadCast(isEnabled)
            }
        }
    }

    private fun sendBroadCast(isEnabled: Boolean) {
        val intent = Intent(ACTION_ACCESSIBILITY_STATE_CHANGED)
        intent.putExtra(EXTRA_IS_ENABLED, isEnabled)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
        notifyListener(isEnabled)
    }

    private fun isAccessibilityServiceEnabled(context: Context): Boolean {
        val expectedServiceName = context.packageName + "/" +
                UsersAccessibility::class.java.canonicalName
        val enabledServicesSetting = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        if (enabledServicesSetting != null) {
            val splitter = SimpleStringSplitter(':')
            splitter.setString(enabledServicesSetting)
            while (splitter.hasNext()) {
                if (expectedServiceName == splitter.next()) {
                    return true
                }
            }
        }
        return false
    }
    interface AccessibilityStateChangeListener {
        fun onAccessibilityStateChanged(isEnabled: Boolean)
    }

    private var listener: AccessibilityStateChangeListener? = null

    fun setAccessibilityStateChangeListener(listener: AccessibilityStateChangeListener) {
        this.listener = listener
    }

    // Call this method when you want to notify the listener
    private fun notifyListener(isEnabled: Boolean) {
        listener?.onAccessibilityStateChanged(isEnabled)
    }


    companion object {
        const val ACTION_ACCESSIBILITY_STATE_CHANGED =
            "com.yourapp.ACTION_ACCESSIBILITY_STATE_CHANGED"
        const val EXTRA_IS_ENABLED = "is_enabled"
    }
}



