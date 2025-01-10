package `in`.woloo.www.application_kotlin.utilities

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import `in`.woloo.www.application_kotlin.presentation.activities.login.TermsAndConditionsActivity

class AppLifecycleObserver(private val context: Context) : LifecycleEventObserver {

    private var isDialogNeeded = false

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_START) {
            if (isDialogNeeded) {
               // showNotificationPermissionDialog(context)
                isDialogNeeded = false
            }
        }
    }

    fun setDialogNeeded() {
        isDialogNeeded = true
    }
}