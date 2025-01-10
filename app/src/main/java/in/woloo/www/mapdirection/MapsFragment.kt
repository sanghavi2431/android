package `in`.woloo.www.mapdirection

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import com.android.volley.toolbox.HttpResponse
import com.directions.route.AbstractRouting
import com.directions.route.Route
import com.directions.route.RouteException
import com.directions.route.Routing
import com.directions.route.RoutingListener
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceClientAdapter
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.AddReviewActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils.Companion.authconfig_response
import `in`.woloo.www.common.CommonUtils.Companion.googlemapapikey
import `in`.woloo.www.common.CommonUtils.Companion.isSubscriptionExpired
import `in`.woloo.www.common.CommonUtils.Companion.printStackTrace
import `in`.woloo.www.common.CommonUtils.Companion.showCustomDialogBackClick
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.mapdirection.model.NavigationRewardsResponse
import `in`.woloo.www.mapdirection.mvp.MapDirectionPresenter
import `in`.woloo.www.mapdirection.mvp.MapDirectionView
import `in`.woloo.www.more.fragments.mvp.InviteFriendsPresenter
import `in`.woloo.www.more.fragments.mvp.InviteFriendsView
import `in`.woloo.www.more.subscribe.SubscribeActivity
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger.d
import `in`.woloo.www.utils.Logger.e
import `in`.woloo.www.utils.Logger.i
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class MapsFragment : Fragment(), OnMapReadyCallback, LocationListener,
    InviteFriendsView, MapDirectionView, RoutingListener {
    private var mMap: GoogleMap? = null
    var markerPoints: ArrayList<LatLng> = ArrayList<LatLng>()
    var gps: GpsTracker? = null
    private val currentBitmap: Bitmap? = null
    private val currentLocationMarker: Marker? = null
    private var curlat = 0.0
    private var curlon = 0.0

    @JvmField
    @BindView(R.id.tv_areaname)
    var tv_areaname: TextView? = null


    @JvmField
    @BindView(R.id.tv_distance)
    var tv_distance: TextView? = null


    @JvmField
    @BindView(R.id.tv_time)
    var tv_time: TextView? = null

    @JvmField
    @BindView(R.id.tv_nextturnname)
    var tv_nextturnname: TextView? = null

    @JvmField
    @BindView(R.id.ivDirection)
    var ivDirection: ImageView? = null

    @JvmField
    @BindView(R.id.ivarrow)
    var ivarrow: ImageView? = null

    @JvmField
    @BindView(R.id.iv_close)
    var iv_close: ImageView? = null

    @JvmField
    @BindView(R.id.iv_currentlocation)
    var iv_currentlocation: ImageView? = null


    @JvmField
    @BindView(R.id.iv_shortdist)
    var iv_shortdist: TextView? = null

    @JvmField
    @BindView(R.id.tv_start)
    var tv_start: TextView? = null


    private var distanceroad: String? = null
    private var duration: String? = null
    private val maneuver: String? = null
    private var getdistance: GetDistance? = null

    private var marker: Marker? = null
    private var currentpos: LatLng? = null
    private val isMarkerRotating = false
    private var origin: LatLng? = null
    private var dest: LatLng? = null
    private val firstdistance = ""
    private var ll: LatLng? = null


    var locationManager: LocationManager? = null
    var destlat: String? = null
    var destlong: String? = null
    private var first_step: GetDistance.Routes.Legs.Steps? = null
    private var second_step: GetDistance.Routes.Legs.Steps? = null
    private var destination_steps = 0
    private var finaldistance = 0
    private var needToCallDistanceAPI = true
    var inviteFriendsPresenter: InviteFriendsPresenter? = null
    private var expiryDate = ""
    private val mContext: Context? = null
    private var context: Activity? = null

    private var mapDirectionPresenter: MapDirectionPresenter? = null
    private var wolooId = 0

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val mLocationPermissionGranted = true
    var tagLocation: String? = null
    var mapFragment: SupportMapFragment? = null
    var mapView: View? = null
    private var progressDialog: ProgressDialog? = null
    private var start: LatLng? = null
    private var end: LatLng? = null
    private var polylines: MutableList<Polyline>? = null

    lateinit var points: ArrayList<LatLng>
    private var newlat = 0.0
    private var newlng = 0.0
    var newlatlng: LatLng? = null
    private var handler: Handler? = null
    protected var mSharedPreference: SharedPreference? = null
    private val defaultLocation = LatLng(19.055229, 72.830829)


    /*calling on onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        i(TAG, "onCreate")
        if (arguments != null) {
            destlat = requireArguments().getString("destlat")
            destlong = requireArguments().getString("destlong")
            wolooId = requireArguments().getInt("wolooId")
            tagLocation = requireArguments().getString("tagLocation")
        }
        mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?

        //        mapFragment.getMapAsync(this);
//        mapView = mapFragment.getView();
        context = activity
        profile
    }

    /*calling on onResume*/
    override fun onResume() {
        super.onResume()
        i(TAG, "onResume")
        if (hasReachedAtDestination) {
            hasReachedAtDestination = false
            val intent = Intent(context, AddReviewActivity::class.java)
            intent.putExtra(AppConstants.WOLOO_ID, wolooId)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            requireContext().startActivity(intent)
        }
    }

    private val profile: Unit
        /*calling on getProfile*/
        get() {
            i(TAG, "getProfile")
            try {
                mapDirectionPresenter = MapDirectionPresenter(requireContext(), this)
                inviteFriendsPresenter = InviteFriendsPresenter(
                    requireContext(),
                    this
                )
                inviteFriendsPresenter!!.profile
            } catch (ex: Exception) {
                printStackTrace(ex)
            }
        }

    val deviceLocation: Unit
        /*calling on getDeviceLocation*/
        get() {
            i(TAG, "getDeviceLocation")
            try {
                if (mLocationPermissionGranted) {
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return
                    }
                    val locationResult =
                        mFusedLocationProviderClient!!.lastLocation
                    locationResult.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Set the map's camera position to the current location of the device.
                            val location = task.result
                            //                            Smartech.getInstance(new WeakReference(requireContext())).setUserLocation(location);
                            val currentLatLng =
                                LatLng(
                                    location.latitude,
                                    location.longitude
                                )
                            val update = CameraUpdateFactory.newLatLngZoom(
                                currentLatLng,
                                AppConstants.DEFAULT_ZOOM.toFloat()
                            )
                            mMap!!.moveCamera(update)
                        }
                    }
                } else {
                    val currentLatLng =
                        LatLng(
                            defaultLocation.latitude,
                            defaultLocation.longitude
                        )
                    val update = CameraUpdateFactory.newLatLngZoom(
                        currentLatLng,
                        AppConstants.DEFAULT_ZOOM.toFloat()
                    )
                    mMap!!.moveCamera(update)
                }
            } catch (e: Exception) {
                e("Exception: %s", e.message!!)
            }
        }

    /*calling on onCreateView*/
    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        i(TAG, "onCreateView")
        val rootView = inflater.inflate(R.layout.activity_maps, container, false)
        ButterKnife.bind(this, rootView)
        mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        gps = GpsTracker(requireContext())

        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        tv_start!!.visibility = View.GONE

        try {
            if (tagLocation.equals("start", ignoreCase = true)) {
                locationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    2000,
                    10f, locationListenerGPS
                )
                tv_start!!.visibility = View.GONE
            } else {
                tv_start!!.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            printStackTrace(e)
        }

        tv_start!!.setOnClickListener {
            val payload = HashMap<String, Any>()
            payload[AppConstants.WOLOO_NAME] = wolooId.toString()
            logNetcoreEvent(
                requireActivity(),
                payload,
                AppConstants.START_WOLOO_EVENT
            )

            val i = Intent(context, MapDirection::class.java)
            i.putExtra("destlat", destlat)
            i.putExtra("destlong", destlong)
            i.putExtra("wolooId", wolooId)
            i.putExtra("tagLocation", "start")
            requireActivity().startActivity(i)
            requireActivity().finish()
        }


        iv_close!!.setOnClickListener {
            //                route();
            requireActivity().finish()

            //                try {
            //                    new getdistanceVal(curlat, curlon, dest.latitude, dest.longitude).execute();
            //                } catch (Exception e) {
            //                      CommonUtils.printStackTrace(e);
            //                }
            //
            //                mMap.clear();
            //
            //                String url = getDirectionsUrl(origin, dest);
            //                Logger.e("url", "" + url);
            //
            //                DownloadTask downloadTask = new DownloadTask();
            //
            //                downloadTask.execute(url);
        }

        iv_currentlocation!!.setOnClickListener {
            mFusedLocationProviderClient = LocationServices
                .getFusedLocationProviderClient(requireActivity())
            mMap!!.isMyLocationEnabled = false
            mMap!!.uiSettings.isMyLocationButtonEnabled = false
            mMap!!.uiSettings.isZoomControlsEnabled = false

            //                mMap.clear();
            if (markerPoints.size > 1) {
                markerPoints.clear()
                mMap!!.clear()
                drawMarker(curlat, curlon)
            }

            //                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(curlat, curlon), 16F));
            val latLng1 = LatLng(
                destlat!!.toDouble(), destlong!!.toDouble()
            )
            markerPoints.add(latLng1)
            val options = MarkerOptions()
            options.position(latLng1)
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            mMap!!.addMarker(options)

            updateLocationUI()
            try {
                //                    if (newlat!=0.0 && newlng!=0.0) {
                getdistanceVal(
                    newlat,
                    newlng,
                    dest!!.latitude,
                    dest!!.longitude
                ).execute()
                //                        String url = getDirectionsUrl(newlatlng, dest);
                //                        Logger.e("url", "" + url);
                var str_origin = ""
                try {
                    str_origin = if (newlng != 0.0 && newlng != 0.0) {
                        "origin=$newlat,$newlng"
                    } else {
                        "origin=" + gps!!.getLatitude() + "," + gps!!.getLongitude()
                    }
                } catch (e: Exception) {
                    printStackTrace(e)
                    str_origin = "origin=" + gps!!.getLatitude() + "," + gps!!.getLongitude()
                }


                // Destination of route
                val str_dest = "destination=" + dest!!.latitude + "," + dest!!.longitude

                // Sensor enabled
                val sensor = "sensor=false"
                var mode = "mode=driving"
                if (mSharedPreference == null) {
                    mSharedPreference = SharedPreference(context)
                }
                val transport_mode: String = mSharedPreference!!.getStoredPreference(
                    context,
                    SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(),
                    "0"
                ).toString()
                when (transport_mode) {
                    "0" -> mode = "mode=driving"
                    "1" -> mode = "mode=walking"
                    "2" -> mode = "mode=bicycling"
                }
                // Building the parameters to the web service
                //                    String key = "key=" + getResources().getString(R.string.google_maps_key);
                val key = "key=" + googlemapapikey(context)

                val parameters = "$str_origin&$str_dest&$sensor&$mode&$key"

                // Output format
                val output = "json"

                // Building the url to the web service
                val url =
                    "https://maps.googleapis.com/maps/api/directions/$output?$parameters"


                val downloadTask: DownloadTask =
                    DownloadTask(requireContext())

                downloadTask.execute(url)
                //                    }
            } catch (e: Exception) {
                printStackTrace(e)
            }
        }


        return rootView
    }

    /*calling on route*/
    private fun route() {
        i(TAG, "route")
        start = LatLng(curlat, curlon)
        end = LatLng(destlat!!.toDouble(), destlong!!.toDouble())


        progressDialog = ProgressDialog.show(
            getContext(), "Please wait.",
            "Fetching route information.", true
        )
        val routing = Routing.Builder()
            .travelMode(AbstractRouting.TravelMode.DRIVING)
            .withListener(this)
            .alternativeRoutes(true)
            .waypoints(start, end)
            .build()
        routing.execute()
    }

    /*calling on updateLocationUI*/
    fun updateLocationUI() {
        i(TAG, "updateLocationUI")
        if (mMap == null) {
            return
        }
        try {
            if (mLocationPermissionGranted) {
                mMap!!.isMyLocationEnabled = true
                mMap!!.uiSettings.isMyLocationButtonEnabled = false
                mMap!!.uiSettings.isZoomGesturesEnabled = true
                deviceLocation
            } else {
                mMap!!.isMyLocationEnabled = false
                mMap!!.uiSettings.isMyLocationButtonEnabled = false
            }
        } catch (e: SecurityException) {
            e("Exception: %s", e.message!!)
        }
    }

    /*calling on animateMarker*/
    fun animateMarker(marker: Marker, toPosition: LatLng, hideMarker: Boolean) {
        i(TAG, "animateMarker")
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val proj = mMap!!.projection
        val startPoint = proj.toScreenLocation(marker.position)
        val startLatLng = proj.fromScreenLocation(startPoint)
        val duration: Long = 500

        val interpolator: Interpolator = LinearInterpolator()

        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = interpolator.getInterpolation(elapsed.toFloat() / duration)
                val lng = (t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude)
                val lat = (t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude)
                marker.position = LatLng(lat, lng)

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16)
                } else {
                    if (hideMarker) {
                        marker.isVisible = false
                    } else {
                        marker.isVisible = true
                    }
                }
            }
        })
    }

    /*calling on Locationlistener for location changed and update the marker position*/
    var locationListenerGPS: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            try {
                i(TAG, "onLocationChanged")
                val latitude = location.latitude
                val longitude = location.longitude


                newlat = latitude
                newlng = longitude
                newlatlng = LatLng(newlat, newlng)


                val msg = "Lat: $latitude,Long: $longitude"
                //Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                e("lat", latitude.toString())
                e("long", longitude.toString())

                if (location == null) {
                    Toast.makeText(
                        activity!!.applicationContext,
                        "Cant Find User Location",
                        Toast.LENGTH_SHORT
                    )
                } else {
//                    Smartech.getInstance(new WeakReference(requireContext())).setUserLocation(location);
                    curlat = latitude
                    curlon = longitude
                    currentpos = LatLng(curlat, curlon)
                    if (marker != null) {
                        marker!!.position = currentpos!!
                    } else {
                        drawMarker(curlat, curlon)
                    }
                    try {
                        getAddress(curlat, curlon)
                    } catch (e: Exception) {
                        printStackTrace(e)
                    }

                    /*calling on getdistanceVal*/
                    try {
                        getdistanceVal(curlat, curlon, dest!!.latitude, dest!!.longitude).execute()
                    } catch (e: Exception) {
                        printStackTrace(e)
                    }
                }
            } catch (e: Exception) {
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        }

        override fun onProviderEnabled(provider: String) {
        }

        override fun onProviderDisabled(provider: String) {
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * @param curlat
     * @param curlon
     */
    /*calling on drawMarker*/
    @SuppressLint("UseCompatLoadingForDrawables")
    fun drawMarker(curlat: Double, curlon: Double) {
        i(TAG, "drawMarker")
        val circleDrawable = resources.getDrawable(R.drawable.ic_loaction)
        val markerIcon = getMarkerIconFromDrawable(circleDrawable)

        marker = mMap!!.addMarker(
            MarkerOptions()
                .position(LatLng(curlat, curlon)) //                .title("My Marker")
                .icon(markerIcon).flat(true)
        )


        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(curlat, curlon)))
        animateMarker(marker!!, LatLng(curlat, curlon), false)
        try {
            val bearing = bearingBetweenLocations(origin!!, dest!!).toFloat()
            marker!!.rotation = bearing
        } catch (e: Exception) {
            printStackTrace(e)
        }
    }

    /*calling on bearingBetweenLocations*/
    private fun bearingBetweenLocations(latLng1: LatLng, latLng2: LatLng): Double {
        i(TAG, "bearingBetweenLocations")
        val PI = 3.14159
        val lat1 = latLng1.latitude * PI / 180
        val long1 = latLng1.longitude * PI / 180
        val lat2 = latLng2.latitude * PI / 180
        val long2 = latLng2.longitude * PI / 180

        val dLon = (long2 - long1)

        val y = sin(dLon) * cos(lat2)
        val x = cos(lat1) * sin(lat2) - (sin(lat1) * cos(lat2) * cos(dLon))

        var brng = atan2(y, x)

        brng = Math.toDegrees(brng)
        brng = (brng + 360) % 360

        return brng
    }

    /*calling on getAddress*/
    fun getAddress(lat: Double, lng: Double) {
        i(TAG, "getAddress")
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            val obj = addresses!![0]
            var add = obj.getAddressLine(0)
            add = """
                $add
                ${obj.countryName}
                """.trimIndent()
            add = """
                $add
                ${obj.countryCode}
                """.trimIndent()
            add = """
                $add
                ${obj.adminArea}
                """.trimIndent()
            add = """
                $add
                ${obj.postalCode}
                """.trimIndent()
            add = """
                $add
                ${obj.subAdminArea}
                """.trimIndent()
            add = """
                $add
                ${obj.locality}
                """.trimIndent()
            add = """
                $add
                ${obj.subThoroughfare}
                """.trimIndent()

            e("Address", "Address$add")
            tv_areaname!!.text = obj.locality + ""
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            if (e.toString().contains("Service not Available")) {
                showCustomDialogBackClick(
                    requireActivity(),
                    "Unable to find Location Service. Please start your location Service Or Reboot your device."
                )
            }
            printStackTrace(e)
            //            Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /*calling on getDistanceOnRoad*/
    private fun getDistanceOnRoad(
        latitude: Double, longitude: Double,
        prelatitute: Double, prelongitude: Double
    ): String {
        i(TAG, "getDistanceOnRoad")
        val result_in_kms = arrayOf("")
        //        String key = "key=" + getResources().getString(R.string.google_maps_key);
        var mode = "mode=driving"
        if (mSharedPreference == null) {
            mSharedPreference = SharedPreference(context)
        }
        val transport_mode: String = mSharedPreference!!.getStoredPreference(
            context,
            SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(),
            "0"
        ).toString()
        when (transport_mode) {
            "0" -> mode = "mode=driving"
            "1" -> mode = "mode=walking"
            "2" -> mode = "mode=bicycling"
        }
        val key = "key=" + googlemapapikey(context)
        val url = ("https://maps.google.com/maps/api/directions/json?origin="
                + latitude + "," + longitude + "&destination=" + prelatitute
                + "," + prelongitude + "&sensor=false&" + mode + "&units=metric" + "&" + key)
      //  val tagLocation = arrayOf("text")
        val response: HttpResponse? = null
        try {
            val adapter: ApiServiceClientAdapter = ApiServiceClientAdapter.Companion.instance
            val call: Call<GetDistance> = adapter.apiService.getDirections(url)
            call.enqueue(object : Callback<GetDistance?> {
                override fun onResponse(
                    call: Call<GetDistance?>,
                    response: Response<GetDistance?>
                ) {
                    getdistance = response.body()
                    checkNotNull(getdistance)
                    duration = getdistance!!.routes[0].legs[0].duration.text
                    result_in_kms[0] = getdistance!!.routes[0].legs[0].distance.text

                    try {
                        first_step = getdistance!!.routes[0].legs[0].steps[0]
                    } catch (e: Exception) {
                        printStackTrace(e)
                    }

                    try {
                        second_step = getdistance!!.routes[0].legs[0].steps[1]
                    } catch (e: Exception) {
                        printStackTrace(e)
                    }

                    destination_steps = getdistance!!.routes[0].legs[0].steps.size

                    updateDirections()

                    try {
                        e("distroad", distanceroad!!)
                        tv_distance!!.text = result_in_kms[0]
                        tv_time!!.text = duration
                        iv_shortdist!!.setText(first_step!!.distance.text)
                    } catch (e: Exception) {
                        printStackTrace(e)
                    }
                }

                override fun onFailure(call: Call<GetDistance?>, t: Throwable) {
                }
            })

            //            HttpClient httpClient = new DefaultHttpClient();
//            HttpContext localContext = new BasicHttpContext();
//            HttpPost httpPost = new HttpPost(url);
//            response = httpClient.execute(httpPost, localContext);
//            InputStream is = response.getEntity().getContent();
//            String result = convertInputStreamToString(is);
//            Logger.e("resultIS", result);
//
//            Gson gson = new Gson();
//            getdistance = gson.fromJson(result.toString(), GetDistance.class);
//
//            duration = getdistance.getRoutes().get(0).getLegs().get(0).getDuration().getText();
//            result_in_kms = getdistance.getRoutes().get(0).getLegs().get(0).getDistance().getText();
//
//            try {
//                first_step = getdistance.getRoutes().get(0).getLegs().get(0).getSteps().get(0);
//            } catch (Exception e) {
//                  CommonUtils.printStackTrace(e);
//            }
//
//            try {
//                second_step = getdistance.getRoutes().get(0).getLegs().get(0).getSteps().get(1);
//            } catch (Exception e) {
//                  CommonUtils.printStackTrace(e);
//            }
//
//            destination_steps = getdistance.getRoutes().get(0).getLegs().get(0).getSteps().size();
//
        } catch (e: Exception) {
            printStackTrace(e)
        }
        return result_in_kms[0]
    }

    /*calling on getMarkerIconFromDrawable*/
    private fun getMarkerIconFromDrawable(drawable: Drawable): BitmapDescriptor {
        i(TAG, "getMarkerIconFromDrawable")
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


    /*calling on onMapReady*/
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        try {
            mMap = googleMap
            i(TAG, "onMapReady")
            mMap!!.isMyLocationEnabled = true
            //            mMap.getUiSettings().setZoomControlsEnabled(true);
//            mMap.getUiSettings().setCompassEnabled(true);
//            mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
//            mMap.setBuildingsEnabled(true);
//            mMap.setIndoorEnabled(true);
            mMap!!.uiSettings.isMyLocationButtonEnabled = false
            mMap!!.uiSettings.setAllGesturesEnabled(true)
            gps = GpsTracker(requireContext())
            curlat = gps!!.getLatitude()
            curlon = gps!!.getLongitude()
            currentpos = LatLng(curlat, curlon)


            try {
                getAddress(curlat, curlon)
            } catch (e: Exception) {
                printStackTrace(e)
            }


            val cameraPosition = CameraPosition.Builder()
                .target(currentpos!!) // Sets the center of the map to Mountain View
                .zoom(AppConstants.DEFAULT_ZOOM.toFloat()) // Sets the zoom
                //                .bearing(30)                // Sets the orientation of the camera to east
                .tilt(30f) // Sets the tilt of the camera to 30 degrees
                .build() // Creates a CameraPosition from the builder
            mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 20, null)
            mMap!!.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    currentpos!!,
                    AppConstants.DEFAULT_ZOOM.toFloat()
                )
            )


            if (markerPoints.size > 1) {
                markerPoints.clear()
                mMap!!.clear()
                drawMarker(curlat, curlon)
            }


            //            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                @Override
//                public void onMapClick(LatLng latLng) {
//
//                    if (markerPoints.size() >= 1) {
//                        markerPoints.clear();
//                        mMap.clear();
//                        drawMarker(curlat, curlon);
//                    }

            // Adding new item to the ArrayList


//            Drawable circleDrawable = getResources().getDrawable(R.drawable.ic_loaction);
//            BitmapDescriptor markerIcon = getMarkerIconFromDrawable(circleDrawable);
//
//            marker = mMap.addMarker(new MarkerOptions()
//                            .position(new LatLng(curlat, curlon))
////                    .title("My Marker")
//                            .icon(markerIcon).flat(true)
//            );
//
//
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(curlat, curlon)));
//            animateMarker(marker, new LatLng(curlat, curlon), false);
            val latLng1 = LatLng(
                destlat!!.toDouble(), destlong!!.toDouble()
            )

            markerPoints.add(latLng1)

            // Creating MarkerOptions
            val options = MarkerOptions()

            // Setting the position of the marker
            options.position(latLng1)

            //                if (markerPoints.size() == 1) {
            //                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            //                } else if (markerPoints.size() == 2) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

            //                }

            // Add new marker to the Google Map Android API V2
            mMap!!.addMarker(options)

            // Checks, whether start and end locations are captured
            //                if (markerPoints.size() >= 2) {
            val currentpos = LatLng(curlat, curlon)
            //                    LatLng origin = (LatLng) markerPoints.get(0);
            origin = currentpos
            dest = markerPoints[0] as LatLng

            try {
                val bearing = bearingBetweenLocations(origin!!, dest!!).toFloat()
                marker!!.rotation = bearing
            } catch (e: Exception) {
                printStackTrace(e)
            }


            if (curlat != dest!!.latitude && curlon != dest!!.longitude) try {
                getdistanceVal(curlat, curlon, dest!!.latitude, dest!!.longitude).execute()
            } catch (e: Exception) {
                printStackTrace(e)
            }


            val url = getDirectionsUrl(origin!!, dest!!)
            e("url", "" + url)

            val downloadTask: DownloadTask = DownloadTask(requireContext())

            downloadTask.execute(url)

            //                }
//            });
        } catch (e: Exception) {
            printStackTrace(e)
        }
    }

    /*calling on goToLocation*/
    private fun goToLocation(latitude: Double, longitude: Double, i: Int) {
        i(TAG, "goToLocation")
        val ll = LatLng(latitude, longitude)
        val update = CameraUpdateFactory.newLatLngZoom(ll, AppConstants.DEFAULT_ZOOM.toFloat())
        mMap!!.animateCamera(update)

        if (marker != null) {
            marker!!.remove()
        }

        val options = MarkerOptions()
            .title("Test")
            .draggable(true)
            .position(LatLng(latitude, longitude))
        marker = mMap!!.addMarker(options)
    }

    override fun onLocationChanged(location: Location) {
        //            Toast.makeText(getActivity().getApplicationContext(), location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT);

        ll = LatLng(location.latitude, location.longitude)

        marker!!.position = ll!!
    }



    override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {
    }

    override fun onProviderEnabled(s: String) {
    }

    override fun onProviderDisabled(s: String) {
    }

    /*calling on inviteFriendSuccess*/
    override fun inviteFriendSuccess(msg: String?) {
        i(TAG, "inviteFriendSuccess")
        try {
            Toast.makeText(requireActivity().applicationContext, msg, Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressed()
        } catch (ex: Exception) {
            printStackTrace(ex)
        }
    }



    /*calling on showRefferalCode*/
    override fun showRefferalCode(refCode: String?, expiryDate: String?) {
        i(TAG, "showRefferalCode")
        this.expiryDate = expiryDate.toString()
        e("expiryDatdee", expiryDate.toString())
        e("expiryDatdee", isSubscriptionExpired(expiryDate).toString() + "")
        if (isSubscriptionExpired(expiryDate) == true) {
//Subscribe Dialog
        } else {
//QR Code Dialog
        }
    }

    /*calling on navigationRewardSuccess*/
    override fun navigationRewardSuccess(navigationRewardsResponse: NavigationRewardsResponse?) {
        i(TAG, "navigationRewardSuccess")
        i(TAG, AppConstants.NAVIGATION_COMPLETED)
        val payload = HashMap<String, Any>()
        payload[AppConstants.LATITUDE] = destlat!!
        payload[AppConstants.LONGITUDE] = destlong!!
        payload[AppConstants.WOLOO_ID] = wolooId
        logNetcoreEvent(requireActivity(), payload, AppConstants.NAVIGATION_COMPLETED)
        i("Netcore", AppConstants.NAVIGATION_COMPLETED)
        try {
            if (navigationRewardsResponse != null && navigationRewardsResponse.code == 200) {
                showdialog(false, true)
                val mobileNumber: String = mSharedPreference!!.getStoredPreference(
                    context,
                    SharedPreferencesEnum.USER_CODE.getPreferenceKey(),
                    "0"
                ).toString()
            } else {
                showdialog(false, false)
                // Toast.makeText(getActivity().getApplicationContext(), "You have arrived at your destination.", Toast.LENGTH_SHORT).show();
            }
            val bundle = Bundle()
            logFirebaseEvent(requireActivity(), bundle, AppConstants.DESTIONATION_REACHED)

            //bundle.put
        } catch (ex: Exception) {
            printStackTrace(ex)
        }
    }

    /*calling on onRoutingFailure*/
    override fun onRoutingFailure(e: RouteException?) {
        i(TAG, "onRoutingFailure")
        progressDialog!!.dismiss()
        if (e != null) {
            Toast.makeText(requireActivity().applicationContext, "Error: " + e.message, Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(
                requireActivity().applicationContext,
                "Something went wrong, Try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onRoutingStart() {
    }

    /*calling on onRoutingSuccess*/
    override fun onRoutingSuccess(arrayList: ArrayList<Route>, i: Int) {
        i(TAG, "onRoutingSuccess")
        progressDialog!!.dismiss()
        val center = CameraUpdateFactory.newLatLng(start!!)
        val zoom = CameraUpdateFactory.zoomTo(AppConstants.DEFAULT_ZOOM.toFloat())

        mMap!!.moveCamera(center)

        //        if(polylines.size()>0) {
//            for (Polyline poly : polylines) {
//                poly.remove();
//            }
//        }
        if (points!!.size > 0) {
            for (poly in polylines!!) {
                poly.remove()
            }
        }

        polylines = ArrayList()
        //add route(s) to the map.
        for (i2 in arrayList.indices) {
            //In case of more than 5 alternative routes
//            int colorIndex = i % COLORS.length;

            val polyOptions = PolylineOptions()
            polyOptions.color(ContextCompat.getColor(requireContext(), R.color.blue_theme))
            polyOptions.width((10 + i * 3).toFloat())
            polyOptions.addAll(arrayList[i].points)
            val polyline = mMap!!.addPolyline(polyOptions)
            polylines!!.add(polyline)

            //Toast.makeText(getActivity(), "Route " + (i + 1) + ": distance - " + arrayList.get(i).getDistanceValue() + ": duration - " + arrayList.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
        }

        // Start marker
        var options = MarkerOptions()
        ////        options.position(start);
////        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue));
////        mMap.addMarker(options);
        drawMarker(curlat, curlon)

        // End marker
        options = MarkerOptions()
        options.position(end!!)
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        mMap!!.addMarker(options)
    }

    override fun onRoutingCancelled() {
        i(LOG_TAG, "Routing was cancelled.")
    }

    private inner class DownloadTask(private val mContext: Context) :
        AsyncTask<String?, Void?, String>() {
        override fun doInBackground(vararg url: String?): String {
            var data = ""

            try {
                data = downloadUrl(url[0]!!)
            } catch (e: Exception) {
                d("Background Task", e.toString())
            }
            return data
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            val parserTask: ParserTask = ParserTask(mContext)


            parserTask.execute(result)
        }
    }

    private inner class getdistanceVal(
        var sourcelat: Double,
        var sourcelong: Double,
        var destlat: Double,
        var destlong: Double
    ) :
        AsyncTask<Void?, Void?, Void?>() {
        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void?): Void? {
            i(TAG, "getdistanceVal")
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            try {
                if (needToCallDistanceAPI) {
                    distanceroad = if (sourcelat != 0.0 && sourcelong != 0.0) {
                        getDistanceOnRoad(sourcelat, sourcelong, destlat, destlong)
                    } else {
                        getDistanceOnRoad(
                            gps!!.getLatitude(),
                            gps!!.getLongitude(),
                            destlat,
                            destlong
                        )
                    }
                }
            } catch (e: Exception) {
                printStackTrace(e)
            }


            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            try {
                if (first_step != null && first_step!!.maneuver != null && first_step!!.maneuver
                        .equals("turn-left", ignoreCase = true)
                ) {
                    ivDirection!!.setImageResource(R.drawable.ic_turn_left)
                } else if (first_step != null && first_step!!.maneuver != null && first_step!!.maneuver
                        .equals("turn-right", ignoreCase = true)
                ) {
                    ivDirection!!.setImageResource(R.drawable.ic_arrow_right)
                } else {
                    ivDirection!!.setImageResource(R.drawable.ic_keep_moving)
                }
            } catch (e: Exception) {
                printStackTrace(e)
            }

            try {
                if (second_step != null && second_step!!.maneuver != null && second_step!!.maneuver
                        .equals("turn-left", ignoreCase = true)
                ) {
                    ivarrow!!.setImageResource(R.drawable.ic_turn_left)
                } else if (second_step != null && second_step!!.maneuver != null && second_step!!.maneuver
                        .equals("turn-right", ignoreCase = true)
                ) {
                    ivarrow!!.setImageResource(R.drawable.ic_arrow_right)
                } else {
                    ivarrow!!.setImageResource(R.drawable.ic_keep_moving)
                }
            } catch (e: Exception) {
                printStackTrace(e)
            }

            e("destinationstep", destination_steps.toString())
            try {
                if (first_step != null && destination_steps == 1) {
                    finaldistance = first_step!!.distance.value
                    if (finaldistance <= 50) {
                        needToCallDistanceAPI = false
                        //                        showdialog(true);
                        /*if (CommonUtils.isSubscriptionExpired(expiryDate) == true) {
                            showdialog(true);
                        } else {
                            mapDirectionPresenter.getWolooNavigationReward(wolooId);
                        }*/
                        mapDirectionPresenter!!.getWolooNavigationReward(wolooId)
                    }
                }
            } catch (e: Exception) {
                printStackTrace(e)
            }


            try {
                e("distroad", distanceroad!!)
                tv_distance!!.text = distanceroad
                tv_time!!.text = duration
                iv_shortdist!!.setText(first_step!!.distance.text)
            } catch (e: Exception) {
                printStackTrace(e)
            }
        }
    }

    fun updateDirections() {
        try {
            if (first_step != null && first_step!!.maneuver != null && first_step!!.maneuver
                    .equals("turn-left", ignoreCase = true)
            ) {
                ivDirection!!.setImageResource(R.drawable.ic_turn_left)
            } else if (first_step != null && first_step!!.maneuver != null && first_step!!.maneuver
                    .equals("turn-right", ignoreCase = true)
            ) {
                ivDirection!!.setImageResource(R.drawable.ic_arrow_right)
            } else {
                ivDirection!!.setImageResource(R.drawable.ic_keep_moving)
            }
        } catch (e: Exception) {
            printStackTrace(e)
        }

        try {
            if (second_step != null && second_step!!.maneuver != null && second_step!!.maneuver
                    .equals("turn-left", ignoreCase = true)
            ) {
                ivarrow!!.setImageResource(R.drawable.ic_turn_left)
            } else if (second_step != null && second_step!!.maneuver != null && second_step!!.maneuver
                    .equals("turn-right", ignoreCase = true)
            ) {
                ivarrow!!.setImageResource(R.drawable.ic_arrow_right)
            } else {
                ivarrow!!.setImageResource(R.drawable.ic_keep_moving)
            }
        } catch (e: Exception) {
            printStackTrace(e)
        }

        e("destinationstep", destination_steps.toString())
        try {
            if (first_step != null && destination_steps == 1) {
                finaldistance = first_step!!.distance.value
                if (finaldistance <= 50) {
                    needToCallDistanceAPI = false
                    //                        showdialog(true);
                    /*if (CommonUtils.isSubscriptionExpired(expiryDate) == true) {
                            showdialog(true);
                        } else {
                            mapDirectionPresenter.getWolooNavigationReward(wolooId);
                        }*/
                    mapDirectionPresenter!!.getWolooNavigationReward(wolooId)
                }
            }
        } catch (e: Exception) {
            printStackTrace(e)
        }


        try {
            e("distroad", distanceroad!!)
            tv_distance!!.text = distanceroad
            tv_time!!.text = duration
            iv_shortdist!!.setText(first_step!!.distance.text)
        } catch (e: Exception) {
            printStackTrace(e)
        }
    }

    fun showdialog(is_expired: Boolean, isPoints: Boolean) {
        try {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_destinationarrived)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation

            val tv_text = dialog.findViewById<View>(R.id.tv_text) as TextView
            val tv_subscribe = dialog.findViewById<View>(R.id.tv_subscribe) as TextView
            val tv_text2 = dialog.findViewById<View>(R.id.tv_text2) as TextView
            tv_text2.visibility = View.GONE

            val authConfigResponse = authconfig_response(getContext())
            if (is_expired == false) {
//                tv_text.setText(getResources().getString(R.string.qrcode_scantext));


                if (authConfigResponse != null) {
                    val arrivedDestinationDialogText =
                        authConfigResponse.getcUSTOMMESSAGE()!!.arrivedDestinationText
                    val arrivedDestinationPoints =
                        authConfigResponse.getcUSTOMMESSAGE()!!.arrivedDestinationPoints
                    //                    String arrivedDestinationDialogText = authConfigResponse.getData().getcUSTOMMESSAGE().getArrivedDestinationDialogText();
//                    tv_text.setText(arrivedDestinationDialogText.replaceAll("\\\\n","\n"));
//                    String str = arrivedDestinationDialogText.replaceAll("\\\\n", "\n");
//                    Spannable spannable = new SpannableString(str);
//                    spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#B5AA3A")), str.indexOf("10 Woloo points."), str.indexOf("10 Woloo points.") + "10 Woloo points.".length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    tv_text.setText(spannable);
                    tv_text.text = arrivedDestinationDialogText!!.replace("\\\\n".toRegex(), "\n")
                    if (isPoints) {
                        tv_text2.text = arrivedDestinationPoints
                        tv_text2.visibility = View.VISIBLE
                        tv_subscribe.visibility = View.GONE
                        handler = Handler()
                        handler!!.postDelayed({
                            hasReachedAtDestination = true
                            dialog.dismiss()
                            val intent = Intent(
                                context,
                                AddReviewActivity::class.java
                            )
                            intent.putExtra(AppConstants.WOLOO_ID, wolooId)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            requireContext().startActivity(intent)
                        }, 3000)
                    } else {
                        tv_text2.visibility = View.GONE
                        tv_subscribe.visibility = View.VISIBLE
                        tv_subscribe.text = "HOME"
                    }
                }
                //                tv_subscribe.setText("SCAN QR CODE");
            } else {
//                tv_text.setText(getResources().getString(R.string.mapsdestination_msg));
                if (authConfigResponse != null) {
                    val subscribeNowDialogText =
                        authConfigResponse.getcUSTOMMESSAGE()!!.subscribeNowDialogText
                    tv_text.text = subscribeNowDialogText!!.replace("\\\\n".toRegex(), "\n")
                }
                tv_subscribe.text = "Subscribe Now"
            }


            tv_subscribe.setOnClickListener {
                if (!is_expired) {
                    if (dialog.isShowing) dialog.dismiss()
                    val i = Intent(
                        getContext(),
                        WolooDashboard::class.java
                    )
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(i)
                } else {
                    if (dialog.isShowing) dialog.dismiss()
                    val i = Intent(
                        getContext(),
                        SubscribeActivity::class.java
                    )
                    startActivity(i)
                }
            }

            dialog.show()
        } catch (e: Exception) {
            printStackTrace(e)
        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private inner class ParserTask(private val mContext: Context) :
        AsyncTask<String?, Int?, List<List<HashMap<String, String>>>?>() {
        // Parsing the data in non-ui thread
        override fun doInBackground(vararg jsonData: String?): List<List<HashMap<String, String>>>? {
            val jObject: JSONObject
            var routes: List<List<HashMap<String, String>>>? = null

            try {
                jObject = JSONObject(jsonData[0])
                val parser = DirectionsJSONParser()

                routes = parser.parse(jObject)
            } catch (e: Exception) {
                printStackTrace(e)
            }
            return routes
        }

        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
            var lineOptions: PolylineOptions? = null
            val markerOptions = MarkerOptions()

            for (i in result!!.indices) {
                points = ArrayList<LatLng>()
                lineOptions = PolylineOptions()

                val path = result[i]
                for (j in path.indices) {
                    val point = path[j]
                    val lat = point["lat"]!!.toDouble()
                    val lng = point["lng"]!!.toDouble()
                    var position = LatLng(lat, lng)
                    points.add(position)
                }

                lineOptions.addAll(points)
                lineOptions.width(12f)
                lineOptions.color(ContextCompat.getColor(mContext, R.color.blue_theme))
                lineOptions.geodesic(true)
            }

            // Drawing polyline in the Google Map for the i-th route
            try {
                mMap!!.addPolyline(lineOptions!!)
            } catch (e: Exception) {
                printStackTrace(e)
                //                Toast.makeText(getActivity().getApplicationContext(), "Boundary Crossed!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String {
        // Origin of route

        val str_origin = "origin=" + origin.latitude + "," + origin.longitude

        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude

        // Sensor enabled
        val sensor = "sensor=false"
        var mode = "mode=driving"
        if (mSharedPreference == null) {
            mSharedPreference = SharedPreference(context)
        }
        val transport_mode: String = mSharedPreference!!.getStoredPreference(
            context,
            SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(),
            "0"
        ).toString()
        when (transport_mode) {
            "0" -> mode = "mode=driving"
            "1" -> mode = "mode=walking"
            "2" -> mode = "mode=bicycling"
        }
        // Building the parameters to the web service
//        String key = "key=" + getResources().getString(R.string.google_maps_key);
        val key = "key=" + googlemapapikey(context)

        val parameters = "$str_origin&$str_dest&$sensor&$mode&$key"

        // Output format
        val output = "json"

        // Building the url to the web service
        val url =
            "https://maps.googleapis.com/maps/api/directions/$output?$parameters"


        return url
    }

    /**
     * A method to download json data from url
     */
    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)

            urlConnection = url.openConnection() as HttpURLConnection

            urlConnection.connect()

            iStream = urlConnection!!.inputStream

            val br = BufferedReader(InputStreamReader(iStream))

            val sb = StringBuffer()

            var line: String? = ""
            while ((br.readLine().also { line = it }) != null) {
                sb.append(line)
            }

            data = sb.toString()

            br.close()
        } catch (e: Exception) {
            d("Exception", e.toString())
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }

    companion object {
        const val DEFAULT_BUFFER_SIZE: Int = 8192
        var showLocationHandler: Handler? = null
        var showLocationRunnable: Runnable? = null
        private const val DELAY_TIME = 1000 * 5 //delay time - 1 minute
        private const val LOG_TAG = "MyActivity"
        @JvmField
        var hasReachedAtDestination: Boolean = false
        var TAG: String = MapsFragment::class.java.simpleName

        /*calling on convertInputStreamToString*/
        @Throws(IOException::class)
        private fun convertInputStreamToString(`is`: InputStream): String {
            i(TAG, "convertInputStreamToString")
            val result = ByteArrayOutputStream()
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var length: Int
            while ((`is`.read(buffer).also { length = it }) != -1) {
                result.write(buffer, 0, length)
            }

            // Java 1.1
            return result.toString(StandardCharsets.UTF_8.name())

            // Java 10
            // return result.toString(StandardCharsets.UTF_8);
        }
    }
}
