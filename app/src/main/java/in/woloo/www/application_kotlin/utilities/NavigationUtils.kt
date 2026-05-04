package `in`.woloo.www.application_kotlin.utilities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.maps.model.LatLng

class NavigationController(
    private val context: Context,
    private val navigationLauncher: ActivityResultLauncher<Intent>,
    private val locationTracker: LocationTracker
) {

    fun goToMaps(
        destination: LatLng,
        transportMode: String
    ) {
        locationTracker.startTracking(
            destination.latitude,
            destination.longitude
        ) {}

        val mode = when (transportMode) {
            "0" -> "d"
            "1" -> "w"
            "3" -> "l"
            else -> "d"
        }

        val uri = Uri.parse(
            "google.navigation:q=${destination.latitude},${destination.longitude}&mode=$mode"
        )

        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.google.android.apps.maps")
        }

        if (intent.resolveActivity(context.packageManager) != null) {
            navigationLauncher.launch(intent)
        } else {
            Toast.makeText(
                context,
                "Google Maps app not found!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
