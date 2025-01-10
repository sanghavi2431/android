package `in`.woloo.www.mapdirection

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import `in`.woloo.www.common.CommonUtils.Companion.printStackTrace
import `in`.woloo.www.utils.Logger.d

class GpsTracker(var mContext: Context) : Service(), LocationListener {
    // flag for GPS status
    var isGPSEnabled: Boolean = false

    // flag for network status
    var isNetworkEnabled: Boolean = false

    // flag for GPS status
    var canGetLocation: Boolean = false

    var locationC: Location? = null // locationC
    var latitudeC: Double = 0.0 // latitudeC
    var longitudeC: Double = 0.0 // longitudeC

    // Declaring a Location Manager
    protected var locationManager: LocationManager? = null

    init {
        getLocation()
    }

    @SuppressLint("MissingPermission")
    fun getLocation(): Location? {
        try {
            locationManager = mContext
                .getSystemService(LOCATION_SERVICE) as LocationManager

            // getting GPS status
            isGPSEnabled = locationManager!!
                .isProviderEnabled(LocationManager.GPS_PROVIDER)

            // getting network status
            isNetworkEnabled = locationManager!!
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true
                if (isNetworkEnabled) {
                    locationManager!!.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                    )
                    d("Network", "Network Enabled")
                    if (locationManager != null) {
                        locationC = locationManager!!
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (locationC != null) {
                            latitudeC = locationC!!.latitude
                            longitudeC = locationC!!.longitude
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (locationC == null) {
                        locationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                        )
                        d("GPS", "GPS Enabled")
                        if (locationManager != null) {
                            locationC = locationManager!!
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (locationC != null) {
                                latitudeC = locationC!!.latitude
                                longitudeC = locationC!!.longitude
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            printStackTrace(e)
        }

        return locationC
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     */
    fun stopUsingGPS() {
        if (locationManager != null) {
            locationManager!!.removeUpdates(this@GpsTracker)
        }
    }

    /**
     * Function to get latitudeC
     */
    fun getLatitude(): Double {
        if (locationC != null) {
            latitudeC = locationC!!.latitude
        }

        // return latitudeC
        return latitudeC
    }

    /**
     * Function to get longitudeC
     */
    fun getLongitude(): Double {
        if (locationC != null) {
            longitudeC = locationC!!.longitude
        }

        // return longitudeC
        return longitudeC
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    fun canGetLocation(): Boolean {
        return this.canGetLocation
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     */
    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(mContext)

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings")

        // Setting Dialog Message
        alertDialog
            .setMessage("GPS is not enabled. Do you want to go to settings menu?")

        // On pressing Settings button
        alertDialog.setPositiveButton(
            "Settings"
        ) { dialog, which ->
            val intent = Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS
            )
            mContext.startActivity(intent)
        }

        // on pressing cancel button
        alertDialog.setNegativeButton(
            "Cancel"
        ) { dialog, which -> dialog.cancel() }

        // Showing Alert Message
        alertDialog.show()
    }

    override fun onLocationChanged(locationC: Location) {
    }

    override fun onProviderDisabled(provider: String) {
    }

    override fun onProviderEnabled(provider: String) {
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
    }

    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    companion object {
        // The minimum distance to change Updates in meters
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 10 meters

        // The minimum time between updates in milliseconds
        private const val MIN_TIME_BW_UPDATES = (1000 * 60 * 1 // 1 minute
                ).toLong()
    }
}