package in.woloo.www.vtion.utilities;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class UserAccessPermissionResponseClass extends AccessibilityService {

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        // Configure your accessibility service here if needed
        Log.d("Access Data" , "Connected");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Handle accessibility events here
        Log.d("Access Data" , "Handle event");
    }

    @Override
    public void onInterrupt() {
        // Handle interruptions (e.g., if the service is stopped)
        Log.d("Access Data" , "Interrupt");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Clean up resources if necessary
        Log.d("Access Data" , "Destroy");
    }
}
