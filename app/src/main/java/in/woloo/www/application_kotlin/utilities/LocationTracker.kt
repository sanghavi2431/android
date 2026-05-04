package `in`.woloo.www.application_kotlin.utilities


import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*

class LocationTracker(private val context: Context) {
    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    fun startTracking(destinationLat: Double, destinationLng: Double, onDestinationReached: () -> Unit) {
        val locationRequest = LocationRequest.create().apply {
            interval = 5000 // Check every 5 seconds
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    val distance = FloatArray(1)
                    Location.distanceBetween(
                        location.latitude, location.longitude,
                        destinationLat, destinationLng,
                        distance
                    )

                    Log.d("LocationTracker", "Current distance: ${distance[0]} meters")

                    if (distance[0] < 50) { // If within 50 meters of destination
                        onDestinationReached()
                        stopTracking() // Stop updates when destination is reached
                    }
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback!!,
            Looper.getMainLooper()
        )
    }

    fun stopTracking() {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }
}
