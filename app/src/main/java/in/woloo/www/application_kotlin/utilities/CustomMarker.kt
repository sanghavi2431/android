package `in`.woloo.www.application_kotlin.utilities

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import `in`.woloo.www.R

class CustomMarker (private val context: Context) : GoogleMap.InfoWindowAdapter {



    override fun getInfoWindow(marker: Marker): View? {
        // Use this method if you want to replace the entire InfoWindow.
        val view = LayoutInflater.from(context).inflate(R.layout.custom_marker_pop_up_window, null)

        return view
    }

    override fun getInfoContents(marker: Marker): View? {
        // Inflate your custom layout




        return null
    }
}
