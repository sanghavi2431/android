package `in`.woloo.www.vtion.utilities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.accessibility.AccessibilityManager

object AccessibilityUtil {

    private const val TAG = "AccessibilityUtil"

    fun isAccessibilityServiceEnabled(context: Context, serviceClassName: String): Boolean {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val enabledServices = Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)

        Log.d(TAG, "Enabled Services: $enabledServices")
        Log.d(TAG, "Component Name to Check: $serviceClassName")

        if (!TextUtils.isEmpty(enabledServices)) {
            val colonSeparatedTokens = enabledServices.split(":")
            for (token in colonSeparatedTokens) {
                val component = ComponentName.unflattenFromString(token)
                if (component != null && component.flattenToString() == serviceClassName) {
                    return true
                }
            }
        }
        return false
    }

    fun promptForAccessibilityPermission(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        context.startActivity(intent)
    }
}
