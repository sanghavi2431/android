package `in`.woloo.www.v2.enroute.model

import `in`.woloo.www.mapdirection.GetDistance.Routes.OverviewPolyline

data class EnrouteRequest(
    var src_lat: Double,
    var src_lng: Double,
    var target_lat: Double,
    var target_lng: Double,
    var overview_polyline: OverviewPolyline
)
