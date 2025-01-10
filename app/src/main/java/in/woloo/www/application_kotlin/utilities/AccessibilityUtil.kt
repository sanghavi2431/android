package `in`.woloo.www.application_kotlin.utilities

import android.content.Context
import android.content.ComponentName
import android.text.TextUtils
import android.content.Intent
import android.provider.Settings
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

   /* private boolean isAccessibilityServiceEnabled(Context context) {
        String expectedServiceName = context.getPackageName() + "/" +
                NotificationAccessibility.class.getCanonicalName();
        String enabledServicesSetting = Settings.Secure.getString(
                context.getContentResolver(),
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (enabledServicesSetting != null) {
            TextUtils.SimpleStringSplitter splitter =

    fun promptForAccessibilityPermission(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        context.startActivity(intent)
    }*/
}
