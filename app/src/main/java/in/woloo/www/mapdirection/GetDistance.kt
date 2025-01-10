package `in`.woloo.www.mapdirection

class GetDistance(
    val geocodedWaypoints: List<GeocodedWaypoints>, val routes: List<Routes>,
    val status: String
) {
    class GeocodedWaypoints(
        val geocoderStatus: String,
        val placeId: String,
        val types: List<String>
    )

    class Routes(
        val bounds: Bounds, val copyrights: String, val legs: List<Legs>,
        val overview_polyline: OverviewPolyline, val summary: String, val warnings: List<Any>,
        val waypointOrder: List<Any>
    ) {
        class Bounds(val northeast: Northeast, val southwest: Southwest) {
            class Northeast(val lat: Double, val lng: Double)

            class Southwest(val lat: Double, val lng: Double)
        }

        class Legs(
            val distance: Distance,
            val duration: Duration,
            val endAddress: String,
            val endLocation: EndLocation,
            val startAddress: String,
            val startLocation: StartLocation,
            val steps: List<Steps>,
            val trafficSpeedEntry: List<Any>,
            val viaWaypoint: List<Any>
        ) {
            class Distance(val text: String, val value: Int)

            class Duration(val text: String, val value: Int)

            class EndLocation(val lat: Double, val lng: Double)

            class StartLocation(val lat: Double, val lng: Double)

            class Steps(
                val distance: Distance,
                val duration: Duration,
                val endLocation: EndLocation,
                val htmlInstructions: String,
                val polyline: Polyline,
                val startLocation: StartLocation,
                val travelMode: String,
                val maneuver: String
            ) {
                class Distance(val text: String, val value: Int)

                class Duration(val text: String, val value: Int)

                class EndLocation(val lat: Double, val lng: Double)

                class Polyline(val points: String)

                class StartLocation(val lat: Double, val lng: Double)
            }
        }

        class OverviewPolyline(val points: String)
    }
}
