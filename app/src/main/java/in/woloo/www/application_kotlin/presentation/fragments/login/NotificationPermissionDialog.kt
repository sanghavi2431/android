package `in`.woloo.www.application_kotlin.presentation.fragments.login

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.presentation.activities.login.VitionUserDataActivity
import `in`.woloo.www.application_kotlin.utilities.NotificationSettingsObserver
import org.w3c.dom.Text


class NotificationPermissionDialog : AppCompatActivity() {

    lateinit var yes : TextView
    lateinit var no : TextView
    private var notificationStateReceiver: BroadcastReceiver? = null
    private var observerN: NotificationSettingsObserver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_notification_permission_dialog)
        yes = findViewById(R.id.tvLogout)
        yes.setOnClickListener {
            checkNotificationPermission()
        }
        observerN = NotificationSettingsObserver(this);
        observerN?.register();

        notificationStateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (NotificationSettingsObserver.ACTION_NOTIFICATION_LISTENER_STATE_CHANGED
                    == intent.action
                ) {
                    val isEnabled = intent.getBooleanExtra(
                        NotificationSettingsObserver.EXTRA_IS_ENABLED, false
                    )
                    onNotificationStateChanged(isEnabled)
                }
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            notificationStateReceiver!!,
            IntentFilter(NotificationSettingsObserver.ACTION_NOTIFICATION_LISTENER_STATE_CHANGED)
        )



    }

    private fun checkNotificationPermission() {
        if (!isNotificationListenerEnabled) {
            val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(intent)
        } else {
            callVitionActivity()
        }
    }

    private val isNotificationListenerEnabled: Boolean
        get() {
            val pkgName = packageName
            val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
            return flat != null && flat.contains(pkgName)
        }

    private fun onNotificationStateChanged(isEnabled: Boolean) {
            // Accessibility service was enabled
            Log.e("Terms and Conditions", "Notification Service Enabled!")
            callVitionActivity()

    }

    private fun callVitionActivity() {
        startActivity(
            Intent(
                applicationContext,
                VitionUserDataActivity::class.java
            )
        )
        finish()
    }


}