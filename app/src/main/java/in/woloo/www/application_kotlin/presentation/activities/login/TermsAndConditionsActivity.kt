package `in`.woloo.www.application_kotlin.presentation.activities.login

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.manager.Lifecycle
import com.google.android.gms.common.api.internal.ActivityLifecycleObserver
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.utilities.AccessibilitySettingsObserver
import `in`.woloo.www.application_kotlin.utilities.AppLifecycleObserver
import `in`.woloo.www.application_kotlin.utilities.NotificationSettingsObserver
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.ActivityTermsAndConditionsBinding
import `in`.woloo.www.v2.WebActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TermsAndConditionsActivity : AppCompatActivity() , AccessibilitySettingsObserver.AccessibilityStateChangeListener{
    private var binding: ActivityTermsAndConditionsBinding? = null
    private var observer: AccessibilitySettingsObserver? = null
    private var accessibilityStateReceiver: BroadcastReceiver? = null
    private var notificationStateReceiver: BroadcastReceiver? = null
    private var observerN: NotificationSettingsObserver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndConditionsBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        observer = AccessibilitySettingsObserver(this);
        observer?.register();
        observer!!.setAccessibilityStateChangeListener(this)

        observerN = NotificationSettingsObserver(this);
        observerN?.register();


        accessibilityStateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (AccessibilitySettingsObserver.ACTION_ACCESSIBILITY_STATE_CHANGED
                    == intent.action
                ) {
                    val isEnabled = intent.getBooleanExtra(
                        AccessibilitySettingsObserver.EXTRA_IS_ENABLED, false
                    )
                    onAccessibilityStateChanged(isEnabled)
                }
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
            accessibilityStateReceiver!!,
            IntentFilter(AccessibilitySettingsObserver.ACTION_ACCESSIBILITY_STATE_CHANGED)
        )


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


        binding!!.checkAgree.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            binding!!.tvAgree.isEnabled = isChecked
        }
        binding!!.termsTwo.setOnClickListener {
            val intent = Intent(applicationContext, WebActivity::class.java)
            intent.putExtra("privacy_policy", "https://woloo.in/privacy-policy/")
            startActivity(intent)
        }
        binding!!.termsFour.setOnClickListener {
            val intent = Intent(applicationContext, WebActivity::class.java)
            intent.putExtra("terms_conditions", "https://woloo.in/terms-condition/")
            startActivity(intent)
        }
        binding!!.tvDisagree.setOnClickListener { callGenderActivity() }
        binding!!.tvAgree.setOnClickListener { showConsentDialog() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ACCESSIBILITY_REQUEST_CODE ->
                if (isAccessibilityServiceEnabled) {
                    if (!isNotificationListenerEnabled) {
                      //  showNotificationPermissionDialog(this@TermsAndConditionsActivity)
                    } else {
                        callVitionActivity()
                    }
                } else {
                    callGenderActivity()
                }

            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private val isAccessibilityServiceEnabled: Boolean
        get() {
            val accessibilityEnabled = Settings.Secure.getInt(
                contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED,
                0
            )
            return accessibilityEnabled == 1
        }


    private fun callVitionActivity() {
        val intent = Intent(this, VitionUserDataActivity::class.java)
        startActivity(intent)
        finish()
        val sharedPrefSettings = SharedPrefSettings()
        sharedPrefSettings.storeIsVTIONUser(true)
    }

    private fun callGenderActivity() {
        val intent = Intent(this, SelectGenderActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun callAccessibility() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivityForResult(intent, ACCESSIBILITY_REQUEST_CODE)
    }

    private fun showConsentDialog() {
        try {
            val alertDialogBuilder = AlertDialog.Builder(this@TermsAndConditionsActivity)
            val child: View = layoutInflater.inflate(R.layout.dialog_consent_layout, null)
            alertDialogBuilder.setView(child)
            alertDialogBuilder.setCancelable(false)
            val alertDialog = alertDialogBuilder.create()
            val tvDeny = child.findViewById<TextView>(R.id.tv_disagree_dialog)
            val tvAgree = child.findViewById<TextView>(R.id.tv_agree_dialog)


            // Set fullscreen layout parameters
            alertDialog.setOnShowListener {
                val window = alertDialog.window
                if (window != null) {
                    val params = window.attributes
                    params.gravity = Gravity.BOTTOM
                    window.attributes = params
                }
            }
            tvDeny.setOnClickListener {
                alertDialog.dismiss()
                callGenderActivity()
            }
            tvAgree.setOnClickListener {
                callAccessibility()
                alertDialog.dismiss()
            }
            alertDialog.show()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
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


     override fun onAccessibilityStateChanged(isEnabled: Boolean) {
        if (isEnabled) {
            // Accessibility service was enabled
            Log.e("Terms and Conditions", "Accessibility Service Enabled!")
            lifecycleScope.launch {
                showNotificationPermissionDialog(this@TermsAndConditionsActivity)
            }
        } else {
            // Accessibility service was disabled
            Log.e("Terms and Conditions", "Accessibility Service Disabled!")
        }
    }

    private fun onNotificationStateChanged(isEnabled: Boolean) {
        if (isEnabled) {
            // Accessibility service was enabled
            Log.e("Terms and Conditions", "Notification Service Enabled!")
            callVitionActivity()
        } else {
            // Accessibility service was disabled
            Log.e("Terms and Conditions", "Notification Service Disabled!")
            callGenderActivity()
        }
    }

    suspend fun showNotificationPermissionDialog(context: Context) {
        withContext(Dispatchers.Main) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Notification Permission Required")
            builder.setMessage("Please enable notifications for this app to receive important updates.")

            builder.setPositiveButton("Go to Settings") { dialog, _ ->
                dialog.dismiss()
                checkNotificationPermission()
            }

            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onResume() {
        super.onResume()
        val isEnabled =intent.getBooleanExtra(
            AccessibilitySettingsObserver.EXTRA_IS_ENABLED, false
        )
        onAccessibilityStateChanged(isEnabled)
    }

    override fun onDestroy() {
        super.onDestroy()

        observer!!.unregister()
        observerN!!.unregister()
    }



    companion object {
        private const val ACCESSIBILITY_REQUEST_CODE = 101
    }
}