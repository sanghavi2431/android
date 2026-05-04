package `in`.woloo.www.application_kotlin.utilities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.api.ApiException

import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.JsonObject
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard.Companion.mSharedPreference
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.Locale

object GoogleMapUtils {

    fun getAddressFromLatLng(latitude: Double, longitude: Double , fieldName: TextView ,  context: Context) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: MutableList<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses != null) {
                if (addresses.isNotEmpty()) {
                    val address: Address = addresses[0]
                    val addressString = StringBuilder()

                    addressString.append(address.getAddressLine(0))
                    addressString.append("\n")
                    addressString.append(address.locality) // City
                    addressString.append("\n")
                    addressString.append(address.countryName) // Country

                    Log.d("Address", addressString.toString())
                    fieldName?.setText(addressString.toString())

                }
            }
        } catch (e: Exception) {
            Log.e("GeocoderError", "Error while getting address", e)
            fieldName?.setText("Current Location")

        }
    }


    public fun getLatLngFromAddress(address: String , context : Context): LatLng? {
        val geocoder = Geocoder(context, Locale.getDefault())

        return try {
            val addressList = geocoder.getFromLocationName(address, 1)  // Get 1 result
            if (!addressList.isNullOrEmpty()) {
                val location = addressList[0]
                LatLng(location.latitude, location.longitude)  // Return LatLng
            } else {
                null  // No result found
            }
        } catch (e: IOException) {
            e.printStackTrace()  // Handle network or IO exceptions
            null
        }

    }


    public data class DirectionWoloo (var wolooid : Int, var lat: Double, var lng: Double)

    public fun createCustomMarker(text: String , context: Context): Bitmap {
        val layoutInflater = LayoutInflater.from(context)
        val markerView = layoutInflater.inflate(R.layout.custome_onclick_marker_design, null)

        // Set the text on the marker
        val markerText = markerView.findViewById<TextView>(R.id.show_cibil_score)
        markerText.text = text

        // Measure and layout the view
        markerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        markerView.layout(0, 0, markerView.measuredWidth, markerView.measuredHeight)

        // Convert the view to a bitmap
        val bitmap = Bitmap.createBitmap(
            markerView.measuredWidth,
            markerView.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        markerView.draw(canvas)

        return bitmap
    }

    public fun extractDistanceInKm(distance: String?): Double {
        if (distance.isNullOrBlank()) return 0.0

        // Remove commas first (e.g. "1,234 km" → "1234 km")
        val cleaned = distance.replace(",", "")

        return Regex("""[\d.]+""")
            .find(cleaned)
            ?.value
            ?.toDoubleOrNull()
            ?: 0.0
    }


        fun isLocationEnabled(
            activity: Activity,
            locationManager: LocationManager,
            onLocationEnabled: () -> Unit
        ) {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                AlertDialog.Builder(activity)
                    .setTitle("Enable Location")
                    .setCancelable(false)
                    .setMessage(
                        "Your location setting is not enabled. Please enable it in settings menu."
                    )
                    .setPositiveButton("Location Settings") { _, _ ->
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        activity.startActivity(intent)
                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()

            } else {
                onLocationEnabled()
            }
        }

        fun updateLocationUI(
            mMap: GoogleMap?,
            locationPermissionGranted: Boolean,
            onGetDeviceLocation: () -> Unit
        ) {
            if (mMap == null) return

            try {
                if (locationPermissionGranted) {
                    mMap.isMyLocationEnabled = true
                    mMap.uiSettings.isMyLocationButtonEnabled = false
                    onGetDeviceLocation()
                } else {
                    mMap.isMyLocationEnabled = false
                    mMap.uiSettings.isMyLocationButtonEnabled = false
                }
            } catch (e: SecurityException) {
                Logger.e("Location UI Exception: %s", e.message ?: "")
            }
        }



        fun decodePoly(encoded: String): List<LatLng> {
            val poly = ArrayList<LatLng>()
            var index = 0
            val len = encoded.length
            var lat = 0
            var lng = 0

            while (index < len) {
                var b: Int
                var shift = 0
                var result = 0
                do {
                    b = encoded[index++].code - 63
                    result = result or ((b and 0x1f) shl shift)
                    shift += 5
                } while (b >= 0x20)

                val dlat = if (result and 1 != 0)
                    (result shr 1).inv()
                else
                    result shr 1
                lat += dlat

                shift = 0
                result = 0
                do {
                    b = encoded[index++].code - 63
                    result = result or ((b and 0x1f) shl shift)
                    shift += 5
                } while (b >= 0x20)

                val dlng = if (result and 1 != 0)
                    (result shr 1).inv()
                else
                    result shr 1
                lng += dlng

                poly.add(
                    LatLng(
                        lat / 1E5,
                        lng / 1E5
                    )
                )
            }
            return poly
        }


    fun getDirectionsUrl(origin: LatLng, dest: LatLng , context: Context): String {
        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude

        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude

        // Sensor enabled
        val sensor = "sensor=false"
        var mode = "mode=driving"
        if (mSharedPreference == null) {
            mSharedPreference =
                SharedPreference(context)
        }
        val transport_mode = mSharedPreference!!.getStoredPreference(
            context,
            SharedPreferencesEnum.TRANSPORT_MODE.preferenceKey,
            "0"
        )
        when (transport_mode) {
            "0" -> mode = "mode=driving"
            "3" -> mode = "mode=driving"
            "1" -> mode = "mode=walking"
        }
        val departNow = "departure_time=now"
        val trafficModel = "traffic_model=best_guess"
        // Building the parameters to the web service
//        String key = "key=" + getResources().getString(R.string.google_maps_key);
        val key = "key=" + CommonUtils.googlemapapikey(context)
        val parameters = "$departNow&$trafficModel&$str_origin&$str_dest&$sensor&$mode&$key"

        // Output format
        val output = "json"

        Log.i("Check Type", "getDirectionsUrl$departNow&$trafficModel&$str_origin&$str_dest&$sensor&$mode&$key")

        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
    }


        fun clearMap(mMap: GoogleMap?) {
            mMap?.clear()
        }

        fun addSourceMarker(mMap: GoogleMap?, source: LatLng) {
            val options = MarkerOptions()
                .position(source)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
            mMap?.addMarker(options)
        }

        fun addDestinationMarker(
            mMap: GoogleMap?,
            dest: LatLng,
            tag: String,
            createCustomMarker: (() -> Marker?)?
        ) {
            if (tag.isEmpty()) {
                val options = MarkerOptions()
                    .position(dest)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                mMap?.addMarker(options)
            } else {
                createCustomMarker?.invoke()
            }
        }

        fun moveCameraBetweenPoints(
            mMap: GoogleMap?,
            source: LatLng,
            dest: LatLng
        ) {
            val bounds = LatLngBounds.Builder()
                .include(source)
                .include(dest)
                .build()

            mMap?.animateCamera(
                CameraUpdateFactory.newLatLngBounds(bounds, 100)
            )
        }

        fun moveCameraSinglePoint(
            mMap: GoogleMap?,
            point: LatLng
        ) {
            val cameraPosition = CameraPosition.Builder()
                .target(point)
                .zoom(AppConstants.DEFAULT_ZOOM.toFloat())
                .bearing(90f)
                .tilt(30f)
                .build()

            mMap?.animateCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000,
                null
            )
        }


    fun fetchLastLocation(
        context: Context,
        onLocationResult: (Location?) -> Unit
    ) {
        val fusedClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onLocationResult(null)
            return
        }

        fusedClient.lastLocation.addOnCompleteListener { task ->
            onLocationResult(task.result)
        }
    }

    fun moveCamera(
        mMap: GoogleMap?,
        latLng: LatLng
    ) {
        val update = CameraUpdateFactory.newLatLngZoom(
            latLng,
            AppConstants.DEFAULT_ZOOM.toFloat()
        )
        mMap?.moveCamera(update)
    }

    fun resolveSourceLatLng(
        intent: Intent,
        autoCompleteText: String,
        context: Context,
        fallbackLatLng: LatLng
    ): LatLng {
        return if (intent.getStringExtra("SOURCE_ADDRESS") == null) {
            fallbackLatLng
        } else {
            getLatLngFromAddress(autoCompleteText, context) ?: fallbackLatLng
        }
    }


    fun moveCameraToDefaultLocation(
        mMap: GoogleMap?,
        defaultLocation: LatLng
    ) {
        Logger.i("GoogleMapUtils", "moveCameraToDefaultLocation")
        val cameraUpdate =
            CameraUpdateFactory.newLatLngZoom(defaultLocation, 5f)
        mMap?.moveCamera(cameraUpdate)
    }

    fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    fun isLocationPermissionGranted(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(context , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    fun animateCameraToMarkerPosition(
        googleMap: GoogleMap?,
        markerList: List<Marker>,
        position: Int,
        zoom: Float = AppConstants.DEFAULT_ZOOM.toFloat(),
        bearing: Float = 90f,
        tilt: Float = 30f,
        duration: Int = 2000
    ) {
        if (markerList.isNotEmpty() && position in markerList.indices) {
            Logger.i("GoogleMapsUtil", "animateCameraToMarkerPosition")
            val marker = markerList[position]
            marker.showInfoWindow()

            val cameraPosition = CameraPosition.Builder()
                .target(marker.position)
                .zoom(zoom)
                .bearing(bearing)
                .tilt(tilt)
                .build()

            googleMap?.animateCamera(
                CameraUpdateFactory.newCameraPosition(cameraPosition),
                duration,
                null
            )
        }
    }


        fun handlePlaceClick(
            context: Context,
            placesClient: PlacesClient,
            place: PlaceAutocomplete,
            isSource: Boolean = false,
            sourceDestCallback: (LatLng?, Boolean) -> Unit,
            onDrawPolyline: () -> Unit,
            wolooId: Int,
            isDistanceLess: Boolean?
        ) {
            try {
                val placeId = place.toString()
                val placeFields = listOf(
                    Place.Field.ID,
                    Place.Field.NAME,
                    Place.Field.LAT_LNG,
                    Place.Field.ADDRESS
                )
                val request = FetchPlaceRequest.builder(placeId, placeFields).build()

                placesClient.fetchPlace(request)
                    .addOnSuccessListener { response ->
                        val placeResult = response.place

                        // Hide keyboard safely
                        try {
                            Utility.hideKeyboard(context as Activity)
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }

                        // Update source or destination
                        sourceDestCallback(placeResult?.latLng, isSource)

                        Logger.e("GoogleMapsUtil", "Place click handled")

                        // Draw polyline if conditions met
                        if (wolooId != 0 || isDistanceLess == true) {
                            onDrawPolyline()
                        }
                    }
                    .addOnFailureListener { exception ->
                        if (exception is ApiException) {
                            Toast.makeText(context, exception.message ?: "", Toast.LENGTH_SHORT).show()
                        }
                    }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

    fun createMarker(
        context: Context,
        map: GoogleMap?,
        latitude: Double,
        longitude: Double,
        title: String,
        snippet: String,
        iconResID: Int,
        index: Int
    ): Marker? {

        Logger.i("GoogleMapUtils", "createMarker")

        map?.uiSettings?.setZoomGesturesEnabled(true)

        val height = 110
        val width = 90

        val bitmapdraw =
            context.resources.getDrawable(iconResID) as BitmapDrawable

        val b = bitmapdraw.bitmap
        val smallMarker =
            Bitmap.createScaledBitmap(b, width, height, false)

        return map?.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude))
                .zIndex(index.toFloat())
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
        )
    }


}