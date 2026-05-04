package `in`.woloo.www.application_kotlin.utilities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.Logger

class MarkerClickHandler(
    private val context: Context,
    private val map: GoogleMap,
    private val markerDataMap: Map<Marker, NearByStoreResponse.Data>,
    private val nearByList: List<NearByStoreResponse.Data>,
    private val onScrollToIndex: (Int) -> Unit
) {

    private var previousMarker: Marker? = null

    fun handleMarkerClick(clickedMarker: Marker): Boolean {
        try {
            Logger.d("MarkerClickHandler", "onMarkerClick")

            resetPreviousMarkerIcon(clickedMarker)

            val clickedData = markerDataMap[clickedMarker] ?: return false
            val cibilScore = clickedData.cibilScore

            clickedMarker.setIcon(
                BitmapDescriptorFactory.fromBitmap(
                    GoogleMapUtils.createCustomMarker(cibilScore, context)
                )
            )

            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    clickedMarker.position,
                    16f
                )
            )

            previousMarker = clickedMarker

            val index = nearByList.indexOfFirst { it == clickedData }
            if (index != -1) {
                onScrollToIndex(index)
            }

        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }

        return false
    }

    private fun resetPreviousMarkerIcon(clickedMarker: Marker) {
        if (previousMarker != null && previousMarker != clickedMarker) {
            val bitmap = (context.resources.getDrawable(
                R.drawable.ic_store_mark_dest,
                null
            ) as BitmapDrawable).bitmap

            val smallMarker = Bitmap.createScaledBitmap(bitmap, 90, 110, false)
            previousMarker!!.setIcon(
                BitmapDescriptorFactory.fromBitmap(smallMarker)
            )
        }
    }
}
