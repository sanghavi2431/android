package `in`.woloo.www.application_kotlin.utilities

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import `in`.woloo.www.application_kotlin.mapdirection.GpsTracker
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger

class MapInitializer {

    fun initMap(
        activity: Activity,
        mMap: GoogleMap,
        defaultLocation: LatLng,
        wolooId: Int,
        isDistanceLess: Boolean,
        tag: String,
        onDrawPolyline: () -> Unit,
        onMarkerClickListener: GoogleMap.OnMarkerClickListener?
    ) {
        try {
            Logger.i("MapInitializer", "initMap")

            // UI settings
            mMap.uiSettings.apply {
                setAllGesturesEnabled(true)
                isCompassEnabled = false
            }

            // Move to default location first
            GoogleMapUtils.moveCameraToDefaultLocation(mMap, defaultLocation)

            // Location enable check
            GoogleMapUtils.isLocationEnabled(
                activity,
                activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            ) {
                // handled in activity callback
            }

            // GPS current position
            val gps = GpsTracker(activity)
            val currentPos = LatLng(gps.latitudeC, gps.longitudeC)

            val cameraPosition = CameraPosition.Builder()
                .target(currentPos)
                .zoom(AppConstants.DEFAULT_ZOOM.toFloat())
                .tilt(30f)
                .build()

            mMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition),
                20,
                null
            )

            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    currentPos,
                    AppConstants.DEFAULT_ZOOM.toFloat()
                )
            )

            // Draw polyline if needed
            if (wolooId != 0 || isDistanceLess) {
                onDrawPolyline()
            }

            // Marker click listener
            if (tag.isEmpty() && onMarkerClickListener != null) {
                mMap.setOnMarkerClickListener(onMarkerClickListener)
            }

        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }
}
