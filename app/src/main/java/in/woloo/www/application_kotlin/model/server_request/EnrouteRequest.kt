package `in`.woloo.www.application_kotlin.model.server_request

import com.google.gson.annotations.SerializedName
import `in`.woloo.www.mapdirection.GetDistance.Routes.OverviewPolyline

data class EnrouteRequest(
    var src_lat : Double,
    var src_lng : Double,
    var target_lat : Double,
    var target_lng : Double,
    var overview_polyline: OverviewPolyline)
