package `in`.woloo.www.v2.enroute

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.dashboard.WolooDashboard
import `in`.woloo.www.dashboard.ui.home.model.NearByStoreResponse
import `in`.woloo.www.database.preference.SharedPreference
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.home.fragments.HomeCategoryFragment
import `in`.woloo.www.home.fragments.WolooStoreInfoFragment
import `in`.woloo.www.mapdirection.GetDistance
import `in`.woloo.www.mapdirection.GpsTracker
import `in`.woloo.www.mapdirection.model.NavigationRewardsResponse
import `in`.woloo.www.mapdirection.mvp.MapDirectionPresenter
import `in`.woloo.www.mapdirection.mvp.MapDirectionView
import `in`.woloo.www.review.AddReviewActivity
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility
import `in`.woloo.www.v2.base.BaseActivity
import `in`.woloo.www.v2.data.local.SharedPrefSettings
import `in`.woloo.www.v2.data.remote.ApiServiceClientAdapter
import `in`.woloo.www.v2.enroute.model.EnrouteRequest
import `in`.woloo.www.v2.enroute.viewmodel.EnrouteViewModel
import `in`.woloo.www.v2.home.SearchPlacesAdapter
import `in`.woloo.www.v2.home.model.PlaceAutocomplete
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class EnrouteDirectionActivity : BaseActivity(), OnMapReadyCallback, OnMarkerClickListener {

    private lateinit var locationManager: LocationManager
    public var sourceAutoComplete: AutoCompleteTextView? = null
    public var destAutoComplete: AutoCompleteTextView? = null
    public var timeTV: TextView? = null
    public var distanceTV: TextView? = null
    public var timeBottomTV: TextView? = null
    public var distanceBottomTV: TextView? = null
    public var start: TextView? = null
    public var info: TextView? = null
    public var infoBottom: TextView? = null
    public var ivClose: ImageView? = null
    public var ivCurrentLocation: ImageView? = null
    public var ivBack: ImageView? = null

    var mMapView: SupportMapFragment? = null
    private val TAG = EnrouteDirectionActivity::class.java.simpleName
    var mSharedPreference: SharedPreference? = null
    private var wolooId = 0
    private var tag: String = ""
    private var mMap: GoogleMap? = null
    private var mLocationPermissionGranted = true
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    var source: LatLng? = null
    var dest: LatLng? = null //LatLng(19.055229, 72.830829)
    private lateinit var placesClient: PlacesClient
    var destPlacesAdapter: SearchPlacesAdapter? = null
    var sourcePlacesAdapter: SearchPlacesAdapter? = null
    private var getdistance: GetDistance? = null
    private var distanceroad: String? = null
    private var duration: String? = null
    private var first_step: GetDistance.Routes.Legs.Steps? = null
    private var second_step: GetDistance.Routes.Legs.Steps? = null
    private var destination_steps = 0
    private var finaldistance = 0
    private var needToCallDistanceAPI = true
    var points: ArrayList<LatLng> = ArrayList()
    var markerPoints: ArrayList<LatLng> = ArrayList()
    private var marker: Marker? = null
    private var currentpos: LatLng? = null
    var gps: GpsTracker? = null
    /*Calling on onCreateView*/
    val DIALOGID = 2
    private val defaultLocation = LatLng(20.5937, 78.9629)
    lateinit var enrouteViewModel: EnrouteViewModel
    var markerList: ArrayList<Marker> = ArrayList()
    private var destlat = ""
    private var destlong = ""
    private var destName = ""
    private var destAddress = ""

    private var fragment_map: Fragment? = null
    var frm_home_map: FrameLayout? = null
    var frm_home_data: View? = null
    public var tv_woloo: TextView? = null
    public var tv_woloo_data : TextView? = null
    var height = 0
    var isFromClickFlag = true
    var isShowWolooList = false
    private var nearByStoreResponseList: List<NearByStoreResponse.Data> = ArrayList<NearByStoreResponse.Data>()
    private var wolooStoreInfoFragment: WolooStoreInfoFragment? = null
    var bottomLayout : View? = null
    var topInfoLayout : View? = null
    public var ivSrcCancel: ImageView? = null
    public var ivDestCancel: ImageView? = null
    var hasReachedAtDestination = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enroute_direction)
        mMapView = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mMapView?.getMapAsync(this)
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        enrouteViewModel = ViewModelProvider(this).get(EnrouteViewModel::class.java)

        val displayMetrics = resources.displayMetrics
        height = displayMetrics.heightPixels

        val key = CommonUtils.googlemapapikey(this)
        Places.initialize(this, key)
        placesClient = Places.createClient(this)

        try {
            if (intent.hasExtra("wolooId")) {
                destlat = intent.getStringExtra("destlat")!!
                destlong = intent.getStringExtra("destlong")!!
                dest = LatLng(destlat.toDouble(),destlong.toDouble())
                tag = intent.getStringExtra("tag")!!
                destName = intent.getStringExtra("wolooName")!!
                destAddress = intent.getStringExtra("wolooAddress")!!
                wolooId = intent.getIntExtra("wolooId", 0)
//                Toast.makeText(this, "Woloo Id received", Toast.LENGTH_SHORT).show()
            }
        }catch (e : Exception){
              CommonUtils.printStackTrace(e)
        }finally {
            SharedPrefSettings.getPreferences.storeIsDirectionWoloo(false)
            SharedPrefSettings.getPreferences.storeDirectionWoloo(null)
        }

        try {
            initView()
            checkGpsAndRequestLocation()
            getLocationPermission()
            setProgressBar()
            setProgressDialog()
            setNetworkDetector()
            setViewModel()
        } catch (e: Exception) {
              CommonUtils.printStackTrace(e)
        }
    }

    private var progressbar: Dialog? = null
    private fun setProgressDialog(){
        progressbar = Dialog(this, R.style.CustomDialogTime)
        progressbar!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressbar!!.window?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    android.R.color.transparent
                )
            )
        )
        progressbar!!.setCancelable(false)
        progressbar!!.setContentView(R.layout.dialog_progress_overlay)
    }

    private fun setViewModel() {
        enrouteViewModel.observeNearByWoloo().observe(this){
            if(progressbar?.isShowing == true) progressbar?.dismiss()
            Utility.hideKeyboard(this)
            if (it != null) {
                renderNearByWoloos(it.data!!)
                isShowWolooList = true
                showWolooList(isShowWolooList)
                tv_woloo?.visibility = View.GONE
            } else {
                renderNearByWoloos(ArrayList<NearByStoreResponse.Data>())
            }
        }

        enrouteViewModel.observeWolooNavigationReward().observe(this){
            Logger.i(TAG, "navigationRewardSuccess")
            try {
                if (it != null && it.success) {
                    showdialogReachedLocation( true)
                } else {
                    showdialogReachedLocation( false)
                    WolooApplication.setErrorMessage("")
                    // Toast.makeText(getActivity().getApplicationContext(), "You have arrived at your destination.", Toast.LENGTH_SHORT).show();
                }
                val bundle = Bundle()
                //bundle.put
                Utility.logFirebaseEvent(this, bundle, AppConstants.DESTIONATION_REACHED)
            } catch (ex: java.lang.Exception) {
                CommonUtils.printStackTrace(ex);
            }
        }
    }

    private fun renderNearByWoloos(data: ArrayList<NearByStoreResponse.Data>) {
        try {
            markerList = ArrayList<Marker>()
            nearByStoreResponseList  = data
            for (i in data.indices) {
                val item = data[i]
                markerList.add(
                    createMarker(
                        item.lat?.toDouble()!!,
                        item.lng?.toDouble()!!,
                        item.name!!,
                        "",
                        R.drawable.ic_store_mark_dest,
                        i
                    )!!
                )
            }
            showingKeyboard(false)
            try {
                (fragment_map as HomeCategoryFragment).setNearestWalk(data,false,false,true)

            } catch (ex: java.lang.Exception) {
                CommonUtils.printStackTrace(ex);
            }
        } catch (e: java.lang.Exception) {
              CommonUtils.printStackTrace(e)
        }
    }

    private fun setWidthAndHeight(view: FrameLayout, height: Int) {
        var height = height
        Logger.i(TAG, "setWidthAndHeight")
        if (markerList != null) {
            height -= 250

        }
        view.layoutParams.height = height
        view.requestLayout()
    }
    fun showWolooList(isShowingWolooList: Boolean) {
        Logger.i(TAG, "hideAndShow")
        if (!isShowingWolooList) {
            setWidthAndHeight(frm_home_map!!, height)
            frm_home_data?.visibility = View.GONE
            //            nsv.setSmoothScrollingEnabled(false);
//            tv_woloo?.setVisibility(View.GONE)
            frm_home_data?.startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.slide_bottom))
            //            tv_woloo.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slide_bottom));
//            nsv.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.slide_bottom));
        } else {
            val bundle = Bundle()
            val payload = HashMap<String, Any>()

            isFromClickFlag = false
            //loadMore("1", true);
            setWidthAndHeight(frm_home_map!!, height / 2)
            frm_home_data?.setVisibility(View.VISIBLE)

            //            nsv.setSmoothScrollingEnabled(true);
            tv_woloo?.setVisibility(View.VISIBLE)
            frm_home_data?.startAnimation(
                AnimationUtils.loadAnimation(
                    this,
                    R.anim.slide_up
                )
            )
        }
    }

    private fun createMarker(latitude: Double, longitude: Double, title: String, snippet: String, iconResID: Int, index: Int): Marker? {
        Logger.i(TAG, "createMarker")
        mMap?.getUiSettings()?.setZoomGesturesEnabled(true)
        val height = 110
        val width = 90
        val bitmapdraw = resources.getDrawable(R.drawable.ic_store_mark_dest) as BitmapDrawable
        val b = bitmapdraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
        return mMap?.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude)) //    .anchor(0.5f, 0.5f)
                .title(title)
//                .snippet(snippet)
                .zIndex(index.toFloat()) //  .snippet(snippet));
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
        )
    }
    override fun onResume() {
        super.onResume()
             if (SharedPrefSettings.getPreferences.fetchIsDirectionWoloo()) {
                checkIfUserReachedLocation()

            }
        showingKeyboard(false)
        isShowWolooList = false
        showWolooList(isShowWolooList)
        tv_woloo?.visibility = View.VISIBLE

//        isLocationEnabled()
//        checkGpsAndRequestLocation()
    }

    private fun checkIfUserReachedLocation() {
        Logger.i(TAG, "checkIfUserReachedLocation")
        try {
            if (mLocationPermissionGranted) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                val locationResult: Task<Location> = mFusedLocationProviderClient!!.lastLocation
                locationResult.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        val location = task.result
                        //                            Smartech.getInstance(new WeakReference(requireContext())).setUserLocation(location);
                        if (location!=null) {
                            val currentLatLng = LatLng(
                                location.latitude,
                                location.longitude
                            )
                            val url: String = getDirectionsUrl(currentLatLng, dest!!)
                            Logger.e("url", "" + url)
                            val adapter: ApiServiceClientAdapter = ApiServiceClientAdapter()
                            val call: Call<GetDistance> = adapter.apiService.getDirections(url)
                            call.enqueue(object : Callback<GetDistance?> {
                                override fun onResponse(
                                    call: Call<GetDistance?>,
                                    response: Response<GetDistance?>
                                ) {
                                    getdistance = response.body()
                                    val distance = getdistance!!.routes[0].legs[0].distance.value
                                    if (distance < 50) {
                                        enrouteViewModel.getWolooNavigationReward(wolooId)
                                        //showdialogReachedLocation(true)
                                    }
                                    SharedPrefSettings.getPreferences.storeIsDirectionWoloo(false)
                                }

                                override fun onFailure(call: Call<GetDistance?>, t: Throwable) {}
                            })
                        }
                    }
                }
            } else {

            }
        } catch (e: java.lang.Exception) {
            Logger.e("Exception: %s", e.message)
        }



    }

    private fun initView() {
        Logger.i(TAG, "initView")
        // Construct a FusedLocationProviderClient.
        // Construct a FusedLocationProviderClient.
        if (mSharedPreference == null) {
            mSharedPreference =
                SharedPreference(this)
        }

        fragment_map = supportFragmentManager.findFragmentById(R.id.fragment_map)
        frm_home_map = findViewById(R.id.frm_home_map)
        frm_home_data = findViewById(R.id.frm_home_data)

        sourceAutoComplete = findViewById(R.id.etSource)
        destAutoComplete = findViewById(R.id.etDest)
        timeTV = findViewById(R.id.tv_time)
        distanceTV = findViewById(R.id.tv_distance)
        topInfoLayout = findViewById(R.id.ll_top_info)
        bottomLayout = findViewById(R.id.ll_bottom_nav)
        timeBottomTV = findViewById(R.id.tv_time_bottom)
        distanceBottomTV = findViewById(R.id.tv_distance_bottom)
        info = findViewById(R.id.tv_info)
        infoBottom = findViewById(R.id.tv_info_bottom)
        start = findViewById(R.id.tv_start)
        ivClose= findViewById(R.id.iv_close)
        ivCurrentLocation = findViewById(R.id.iv_currentlocation)
        ivBack = findViewById(R.id.ivBack)

        tv_woloo = findViewById(R.id.tv_woloo)
        tv_woloo_data = findViewById(R.id.tv_woloo_data)
        ivSrcCancel = findViewById(R.id.iv_cancel_source)
        ivDestCancel = findViewById(R.id.iv_cancel_dest)
        tv_woloo?.visibility = View.VISIBLE

        sourcePlacesAdapter =
            SearchPlacesAdapter(this, R.layout.item_search_autocomplete, placesClient)
        sourceAutoComplete?.setAdapter(sourcePlacesAdapter)
        sourceAutoComplete?.threshold = 1

        destPlacesAdapter =
            SearchPlacesAdapter(this, R.layout.item_search_autocomplete, placesClient)
        destAutoComplete?.setAdapter(destPlacesAdapter)
        destAutoComplete?.threshold = 1


        sourceAutoComplete?.setOnClickListener { view ->
            showingKeyboard(true);
        }
        destAutoComplete?.setOnClickListener { view ->
            showingKeyboard(true);
        }
        sourceAutoComplete?.setOnTouchListener { view, motionEvent ->
            view.performClick()
            return@setOnTouchListener false
        }
        destAutoComplete?.setOnTouchListener { view, motionEvent ->
            view.performClick()
            return@setOnTouchListener false
        }
        sourceAutoComplete?.addTextChangedListener {
            if (sourceAutoComplete?.text?.isEmpty()==true){
                source = null
            }
            showingKeyboard(true);
        }
        destAutoComplete?.addTextChangedListener {
            if (destAutoComplete?.text?.isEmpty()==true){
                dest = null
            }
            showingKeyboard(true);
        }

        sourceAutoComplete?.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                val place = adapterView.getItemAtPosition(pos) as PlaceAutocomplete
                sourceAutoComplete?.setText(place.address)
                sourceAutoComplete?.setSelection(sourceAutoComplete?.length()!!)
                onPlaceClick(place,true)
                //                Toast.makeText(getContext(), place.address, Toast.LENGTH_SHORT).show();
            }

        destAutoComplete?.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                val place = adapterView.getItemAtPosition(pos) as PlaceAutocomplete
                destAutoComplete?.setText(place.address)
                destAutoComplete?.setSelection(destAutoComplete?.length()!!)
                onPlaceClick(place)
                //                Toast.makeText(getContext(), place.address, Toast.LENGTH_SHORT).show();
            }


        ivSrcCancel?.setOnClickListener {
            sourceAutoComplete?.setText("")
            source=null
            showingKeyboard(true);
            drawPolylineOnMap()
        }
        ivDestCancel?.setOnClickListener {
            destAutoComplete?.setText("")
            dest = null
            showingKeyboard(true);
            drawPolylineOnMap()
        }


        if(tag.isNotEmpty()){
            destAutoComplete?.setText("$destName,$destAddress")
            destAutoComplete?.isEnabled = false

            ivDestCancel?.visibility = View.GONE
            bottomLayout?.visibility = View.VISIBLE
            topInfoLayout?.visibility = View.GONE
            tv_woloo?.visibility = View.GONE
        }else{
            bottomLayout?.visibility = View.GONE
            topInfoLayout?.visibility = View.VISIBLE
        }

        ivBack?.setOnClickListener { finish() }
        ivClose?.setOnClickListener { finish() }
        ivCurrentLocation?.setOnClickListener { isLocationEnabled() }
        start?.setOnClickListener { showdialogGoToMaps() }

        tv_woloo?.setOnClickListener(View.OnClickListener {

            isShowWolooList = !isShowWolooList
            showWolooList(isShowWolooList)
            tv_woloo?.visibility = View.GONE
            showingKeyboard(false);
        })

        tv_woloo_data?.setOnClickListener(View.OnClickListener {

            isShowWolooList = !isShowWolooList
            showWolooList(isShowWolooList)
            tv_woloo?.visibility = View.VISIBLE
            showingKeyboard(false);
        })
    }

    private fun showingKeyboard(isKeyboardShowing: Boolean) {
        if(isKeyboardShowing){
            Logger.i(TAG, "hideAndShow")
            if (frm_home_data?.isVisible == true){
                setWidthAndHeight(frm_home_map!!, height)
                frm_home_data?.startAnimation(
                    AnimationUtils.loadAnimation(this, R.anim.slide_bottom))
                frm_home_data?.visibility = View.GONE
            }
            removeWolooStoreInfo()
            isShowWolooList = false
        }else{
            Utility.hideKeyboard(this);
        }
    }

    private fun onPlaceClick(place: PlaceAutocomplete, isSource : Boolean = false) {
        try {
            val placeId: String = place.placeId.toString()
            val placeFields = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG,
                Place.Field.ADDRESS
            )
            val request = FetchPlaceRequest.builder(placeId, placeFields).build()
            placesClient.fetchPlace(request)
                .addOnSuccessListener { response ->
                    val place = response?.place
                    try {
                        Utility.hideKeyboard(this@EnrouteDirectionActivity)
                    } catch (ex: java.lang.Exception) {
                        ex.printStackTrace()
                    }
                    if(isSource)
                        source = place?.latLng
                    else
                        dest = place?.latLng
                    drawPolylineOnMap()

                }.addOnFailureListener { exception ->
                    if (exception is ApiException) {
                        Toast.makeText(
                            this,
                            exception.message + "",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } catch (ex: java.lang.Exception) {
        }
    }


    fun animateCameraToMarkerPosition(position: Int) {
        if (markerList != null && !markerList.isEmpty()) {
            Logger.i(TAG, "animateCameraToMarkerPosition")
            markerList[position].showInfoWindow()
            // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
            val cameraPosition = CameraPosition.Builder()
                .target(markerList[position].position) // Sets the center of the map to Mountain View
                .zoom(AppConstants.DEFAULT_ZOOM.toFloat()) // Sets the zoom
                .bearing(90f) // Sets the orientation of the camera to east
                .tilt(30f) // Sets the tilt of the camera to 30 degrees
                .build() // Creates a CameraPosition from the builder
            mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null)
        }
    }
    override fun onMarkerClick(marker: Marker): Boolean {
        Logger.d(TAG, "onMarkerClick")
        if (isShowWolooList) {
            isShowWolooList = false
            showWolooList(isShowWolooList)
        }
        if(marker.title?.isNotEmpty() == true) {
            showingKeyboard(false)
            Logger.i(TAG, "onMarkerClick")
            loadMarkerFragmentWithIndex(marker.zIndex.toInt(), nearByStoreResponseList)
            tv_woloo?.visibility = View.GONE
        }
        return false
    }
    fun loadMarkerFragmentWithIndex(index: Int, dataList: List<NearByStoreResponse.Data?>?) {
        Logger.i(TAG, "loadMarkerFragmentWithIndex")
        wolooStoreInfoFragment = WolooStoreInfoFragment()
        wolooStoreInfoFragment?.setDataList(dataList)
        wolooStoreInfoFragment?.setIndex(index)

        setWidthAndHeight(frm_home_map!!, (height / 1.5).toInt())
        loadMarkerFragment(wolooStoreInfoFragment, "WolooStoreInfoFragment")
    }
    fun loadMarkerFragment(fragment: Fragment?, homeTah: String?) {
        Logger.i(TAG, "loadMarkerFragment")
        try {
            val fragmentManager = (this as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(
                R.anim.slide_in_up,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,  // popEnter
                R.anim.slide_out_up // popExit
            )
            fragmentTransaction.replace(R.id.frm_marker_detail, fragment!!, homeTah)
            fragmentTransaction.disallowAddToBackStack()
            fragmentTransaction.commit()
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }
    fun removeWolooStoreInfo() {
        if (wolooStoreInfoFragment != null && wolooStoreInfoFragment!!.isAdded) {
            Logger.i(TAG, "removeWolooStoreInfo")
            try {
                setWidthAndHeight(frm_home_map!!, height)
                val fragmentManager = (this as AppCompatActivity).supportFragmentManager
                fragmentManager.beginTransaction().remove(wolooStoreInfoFragment!!).commit()
                tv_woloo?.visibility = View.VISIBLE
                //  fragmentManager.beginTransaction().remove(getFragmentManager().findFragmentById(R.id.frm_marker_detail)).commit();
            } catch (e: java.lang.Exception) {
                  CommonUtils.printStackTrace(e)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        try {
            mMap = googleMap
            moveCameraToDefaultLocation()

            Logger.i(TAG, "onMapReady")
//            mMap!!.isMyLocationEnabled = true
//            mMap?.uiSettings?.isZoomControlsEnabled = true;
//            mMap!!.uiSettings.isMyLocationButtonEnabled = true
            mMap!!.uiSettings.setAllGesturesEnabled(true)
            isLocationEnabled()
            gps = GpsTracker(this)

            currentpos = LatLng(gps!!.latitude, gps!!.longitude)

            val cameraPosition = CameraPosition.Builder()
                .target(currentpos!!) // Sets the center of the map to Mountain View
                .zoom(AppConstants.DEFAULT_ZOOM.toFloat()) // Sets the zoom
                //                .bearing(30)                // Sets the orientation of the camera to east
                .tilt(30f) // Sets the tilt of the camera to 30 degrees
                .build() // Creates a CameraPosition from the builder

            mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 20, null)
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentpos!!, AppConstants.DEFAULT_ZOOM.toFloat()))
            drawPolylineOnMap()

            if(tag.isEmpty())
                mMap!!.setOnMarkerClickListener(this@EnrouteDirectionActivity)

        } catch (e: java.lang.Exception) {
              CommonUtils.printStackTrace(e)
        }
    }


    fun checkGpsAndRequestLocation() {
        if (isLocationPermissionGranted()) {
            val locationRequest = LocationRequest.create()
            //            locationRequest.setInterval(2000);
//            locationRequest.setFastestInterval(5000);
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.numUpdates = 1
            val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
            builder.setAlwaysShow(true) //this is the key ingredient
            builder.addLocationRequest(locationRequest)
            val result = LocationServices.getSettingsClient(this)
                .checkLocationSettings(builder.build())
            result.addOnCompleteListener { task ->
                try {
                    val response = task.getResult(
                        ApiException::class.java
                    )
                    // All location settings are satisfied. The client can initialize location requests here.
                    getDeviceLocation()
                    updateLocationUI()
                    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                } catch (exception: ApiException) {
                    //                        moveCameraToDefaultLocation()
                    when (exception.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val resolvable = exception as ResolvableApiException
                            startIntentSenderForResult(resolvable.resolution.intentSender, DIALOGID, null, 0, 0, 0, null)
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        } catch (e: IntentSender.SendIntentException) {
                              CommonUtils.printStackTrace(e)
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                    }
                    // Location settings are not satisfied. However, we have no way to fix the
                    // settings so we won't show the dialog.
                }
            }
        } else {
            getLocationPermission()
        }
    }

    fun isLocationPermissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

  /*  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkGpsAndRequestLocation()
                } else {
                    Toast.makeText(this, "Unable to get your location, please try again", Toast.LENGTH_SHORT).show()
                    moveCameraToDefaultLocation()
                }
            }
        }
    }*/

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkGpsAndRequestLocation()
                } else {
                    Toast.makeText(this, "Unable to get your location, please try again", Toast.LENGTH_SHORT).show()
                    moveCameraToDefaultLocation()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            DIALOGID -> when (resultCode) {
                Activity.RESULT_OK -> {
                    getDeviceLocation()
                    updateLocationUI()
                }
                Activity.RESULT_CANCELED -> {
                    moveCameraToDefaultLocation()
//                    Toast.makeText(this, "Unable to get your location, please try again", Toast.LENGTH_SHORT).show()
//                    moveCameraToDefaultLocation()
                }
            }
        }
    }

    private fun moveCameraToDefaultLocation() {
        Logger.i(TAG, "moveCameraToDefaultLocation")
        val cameraUpdate =
            CameraUpdateFactory.newLatLngZoom(defaultLocation, 5F)
        mMap?.moveCamera(cameraUpdate)
    }

    fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true
//            getDeviceLocation()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    fun getDeviceLocation() {
        Logger.i(TAG, "getDeviceLocation")
        try {
            if (mLocationPermissionGranted) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                val locationResult: Task<Location> = mFusedLocationProviderClient!!.lastLocation
                locationResult.addOnCompleteListener { task ->
                    try {
                        if (task.isSuccessful) {
                            // Set the map's camera position to the current location of the device.
                            val location = task.result
                            //                            Smartech.getInstance(new WeakReference(requireContext())).setUserLocation(location);
                            if (location != null) {
                                val currentLatLng = LatLng(

                                    location.latitude,
                                    location.longitude
                                )
                                source = currentLatLng
                                sourceAutoComplete?.setText("Current Location")
                                val update = CameraUpdateFactory.newLatLngZoom(
                                    currentLatLng,
                                    AppConstants.DEFAULT_ZOOM.toFloat()
                                )
                                if (tag.isNotEmpty()) {
                                    sourceAutoComplete?.isEnabled = false
                                    ivSrcCancel?.visibility = View.GONE
                                }

                                //                mMap.clear();
                                mMap?.moveCamera(update)
                                drawPolylineOnMap()
                            } else {
                                val update = CameraUpdateFactory.newLatLngZoom(
                                    defaultLocation,
                                    AppConstants.DEFAULT_ZOOM.toFloat()
                                )
                                mMap?.moveCamera(update)
                            }
                        }
                    } catch (e :Exception){
                          CommonUtils.printStackTrace(e)
                    }

                }
            } else {

            }
        } catch (e: java.lang.Exception) {
            Logger.e("Exception: %s", e.message)
        }
    }

    private fun drawPolylineOnMap() {
//        val bounds = LatLngBounds.builder().build()
        markerPoints.clear()
//        mMap!!.isTrafficEnabled = true
        mMap!!.clear()

        distanceTV?.setText("")
        timeTV?.setText("")
        distanceBottomTV?.setText("")
        timeBottomTV?.setText("")
        showingKeyboard(false)
        renderNearByWoloos(ArrayList<NearByStoreResponse.Data>())

        if(source != null){
            markerPoints.add(source!!)
            val options = MarkerOptions()
            options.position(source!!)
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
            mMap!!.addMarker(options)
        }
        if (dest != null) {
            markerPoints.add(dest!!)
            if (tag.isEmpty()) {
                val options = MarkerOptions()
                options.position(dest!!)
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                mMap!!.addMarker(options)
            }else{
                createMarker(
                    dest!!.latitude,
                    dest!!.longitude,
                    destName,
                    "",
                    R.drawable.ic_store_mark_dest,
                    0)!!
            }
        }
        if(source!=null && dest!=null) {
            progressbar?.show()
            try {
//                val bearing = bearingBetweenLocations(source!!, dest!!).toFloat()
//                marker!!.rotation = bearing
//                getdistanceVal(
//                    source!!.latitude,
//                    source!!.longitude,
//                    dest!!.latitude,
//                    dest!!.longitude
//                ).execute()

                var builder = LatLngBounds.Builder()
                builder = builder.include(source!!)
                builder = builder.include(dest!!)
                val bounds = builder.build()
//                            val bounds = LatLngBounds(
//                                LatLng(route.bounds.southwest.lat,route.bounds.southwest.lng),
//                                LatLng(route.bounds.northeast.lat,route.bounds.northeast.lng))
                val mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100)
                mMap!!.animateCamera(mCameraUpdate)

                val url: String = getDirectionsUrl(source!!, dest!!)
                Logger.e("url", "" + url)
                getDirections(url)
                start?.visibility = View.VISIBLE
                info?.visibility = View.GONE
                infoBottom?.visibility = View.GONE
            } catch (e: java.lang.Exception) {
                  CommonUtils.printStackTrace(e)
            }
        }else{
            if (source!=null){
                val cameraPosition = CameraPosition.Builder()
                    .target(source!!) // Sets the center of the map to Mountain View
                    .zoom(AppConstants.DEFAULT_ZOOM.toFloat()) // Sets the zoom
                    .bearing(90f) // Sets the orientation of the camera to east
                    .tilt(30f) // Sets the tilt of the camera to 30 degrees
                    .build() // C
                mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null)
            }else if(dest!=null){
                val cameraPosition = CameraPosition.Builder()
                    .target(dest!!) // Sets the center of the map to Mountain View
                    .zoom(AppConstants.DEFAULT_ZOOM.toFloat()) // Sets the zoom
                    .bearing(90f) // Sets the orientation of the camera to east
                    .tilt(30f) // Sets the tilt of the camera to 30 degrees
                    .build() // C
                mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null)
            }

            start?.visibility = View.GONE
            info?.visibility = View.VISIBLE

            infoBottom?.visibility = View.VISIBLE
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
            mSharedPreference =
                SharedPreference(this)
        }
        val transport_mode = mSharedPreference!!.getStoredPreference(
            this,
            SharedPreferencesEnum.TRANSPORT_MODE.preferenceKey,
            "0"
        )
        when (transport_mode) {
            "0" -> mode = "mode=driving"
            "1" -> mode = "mode=walking"
            "2" -> mode = "mode=bicycling"
        }
        val departNow = "departure_time=now"
        val trafficModel = "traffic_model=best_guess"
        // Building the parameters to the web service
//        String key = "key=" + getResources().getString(R.string.google_maps_key);
        val key = "key=" + CommonUtils.googlemapapikey(this)
        val parameters = "$departNow&$trafficModel&$str_origin&$str_dest&$sensor&$mode&$key"

        // Output format
        val output = "json"

        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
    }

    private fun getDirections(strUrl: String) {
        Logger.i(TAG, "getDistanceOnRoad")
        var result_in_kms = ""
        try {
            val adapter: ApiServiceClientAdapter = ApiServiceClientAdapter()
            val call: Call<GetDistance> = adapter.apiService.getDirections(strUrl)
            call.enqueue(object : Callback<GetDistance?> {
                override fun onResponse(
                    call: Call<GetDistance?>,
                    response: Response<GetDistance?>
                ) {
                    try {
                        getdistance = response.body()
                        if(getdistance?.routes?.isEmpty() == false) {
                            result_in_kms = getdistance!!.routes[0].legs[0].distance.text
                            duration = getdistance!!.routes[0].legs[0].duration.text
                            distanceTV?.setText(result_in_kms)
                            timeTV?.setText(duration)
                            distanceBottomTV?.setText(result_in_kms)
                            timeBottomTV?.setText(duration)

                            if (tag.isEmpty()) {
                                enrouteViewModel.getEnrouteWoloo(
                                    EnrouteRequest(
                                        source!!.latitude,
                                        source!!.longitude,
                                        dest!!.latitude,
                                        dest!!.longitude,
                                        getdistance!!.routes[0].overview_polyline
                                    )
                                )
                            } else{
                                progressbar?.dismiss()
                            }
                            var lineOptions: PolylineOptions? = null
                            val markerOptions = MarkerOptions()
                            points.clear()
                            for (route in getdistance?.routes!!) {
                                for (legs in route.legs!!) {
                                    lineOptions = PolylineOptions()

                                    for (step in legs.steps) {
                                        val pointList = decodePoly(step.polyline.points)
                                        points.addAll(pointList)
                                    }
                                    lineOptions.addAll(points)
                                    lineOptions.width(12f)
                                    lineOptions.color(
                                        ContextCompat.getColor(
                                            this@EnrouteDirectionActivity,
                                            R.color.blue_theme
                                        )
                                    )
                                    lineOptions.geodesic(true)

                                }

                            }
                            var builder = LatLngBounds.Builder()
                            for (latLng in points) {
                                builder = builder.include(latLng)
                            }
                            val bounds = builder.build()
//                            val bounds = LatLngBounds(
//                                LatLng(route.bounds.southwest.lat,route.bounds.southwest.lng),
//                                LatLng(route.bounds.northeast.lat,route.bounds.northeast.lng))
                            val mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100)
                            mMap!!.animateCamera(mCameraUpdate)
                            mMap?.addPolyline(lineOptions!!)

                            Logger.e(TAG, "PolylineOptions 307")
// Drawing polyline in the Google Map for the i-th route

//                            if (tag.isEmpty()) progressbar?.show()
                        }else{
                            if(progressbar?.isShowing == true) progressbar?.dismiss()
                            Toast.makeText(this@EnrouteDirectionActivity,"No Route Available",Toast.LENGTH_SHORT).show()
                            (fragment_map as HomeCategoryFragment).setNearestWalk(ArrayList<NearByStoreResponse.Data>(),false,false,true)
                        }
                    } catch (e: java.lang.Exception) {
                          CommonUtils.printStackTrace(e)
                        if(progressbar?.isShowing == true) progressbar?.dismiss()
                        Toast.makeText(this@EnrouteDirectionActivity,"No Route Available",Toast.LENGTH_SHORT).show()
                        (fragment_map as HomeCategoryFragment).setNearestWalk(ArrayList<NearByStoreResponse.Data>(),false,false,true)
                    }

                }

                override fun onFailure(call: Call<GetDistance?>, t: Throwable) {}
            })
        } catch (e: java.lang.Exception) {
              CommonUtils.printStackTrace(e)
        }
    }

    fun decodePoly(encoded: String): List<LatLng> {
        val poly: MutableList<LatLng> = ArrayList()
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
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }
        return poly
    }

    private fun isLocationEnabled() {
        moveCameraToDefaultLocation()
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle("Enable Location")
            alertDialog.setCancelable(false)
            alertDialog.setMessage("Your location setting is not enabled. Please enable it in settings menu.")
            alertDialog.setPositiveButton(
                "Location Settings"
            ) { dialog, which ->
                val intent =
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
                finish()
            }
            alertDialog.setNegativeButton(
                "Cancel"
            ) { dialog, which ->
                dialog.cancel()
//                finish()
            }
            val alert = alertDialog.create()
            alert.show()
        } else {
            updateLocationUI()
        }
    }

    fun updateLocationUI() {
        Logger.i(TAG, "updateLocationUI")
        if (mMap == null) {
            return
        }
        try {
            if (mLocationPermissionGranted) {
                mMap!!.isMyLocationEnabled = true
                mMap!!.uiSettings.isMyLocationButtonEnabled = false
//                mMap!!.uiSettings.isZoomGesturesEnabled = true
                getDeviceLocation()
            } else {
                mMap!!.isMyLocationEnabled = false
                mMap!!.uiSettings.isMyLocationButtonEnabled = false
            }
        } catch (e: SecurityException) {
            Logger.e("Exception: %s", e.message)
        }
    }

    fun getAddress(lat: Double, lng: Double) {
        Logger.i(TAG, "getAddress")
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lat, lng, 1)
            val obj = addresses?.get(0)!!
            var add = obj.getAddressLine(0)
            add = "$add\n${obj.countryName}".trimIndent()
            add = "$add\n${obj.countryCode}".trimIndent()
            add = "$add\n${obj.adminArea}".trimIndent()
            add = "$add\n${obj.postalCode}".trimIndent()
            add = "$add\n${obj.subAdminArea}".trimIndent()
            add = "$add\n${obj.locality}".trimIndent()
            add = "$add\n${obj.subThoroughfare}".trimIndent()
            Logger.e("Address", "Address$add")
//            tv_areaname.setText(obj.locality + "")
        } catch (e: java.lang.Exception) {
            // TODO Auto-generated catch block
            if (e.toString().contains("Service not Available")) {
                CommonUtils.showCustomDialogBackClick(
                    this,
                    "Unable to find Location Service. Please start your location Service Or Reboot your device."
                )
            }
              CommonUtils.printStackTrace(e)
        }
    }

    fun goToMaps() {
        val lat = dest!!.latitude
        val lng = dest!!.longitude
        var mode = ""
        val transport_mode = mSharedPreference!!.getStoredPreference(this, SharedPreferencesEnum.TRANSPORT_MODE.preferenceKey, "0")
        when (transport_mode) {
            "0" -> mode = "d"
            "1" -> mode = "w"
            "2" -> mode = "l" //b for bycycler & l for two wheeler
        }
        // Create a Uri from an intent string. Use the result to create an Intent.
//        geo:${source!!.latitude},${source!!.longitude}?
        val request = "google.navigation:q=$lat,$lng&mode=$mode"
        Logger.e(TAG, request)
        val mapIntentUri = Uri.parse(request)
        // Create an Intent from mapIntentUri. Set the action to ACTION_VIEW
        val mapIntent = Intent(Intent.ACTION_VIEW, mapIntentUri)
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps")
        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent)
    }

    fun showdialogReachedLocation(isPoints : Boolean = false) {
        try {
            SharedPrefSettings.getPreferences.storeIsDirectionWoloo(false)
            SharedPrefSettings.getPreferences.storeDirectionWoloo(null)
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_destinationarrived)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val message = dialog.findViewById<View>(R.id.tv_text) as TextView
            val action = dialog.findViewById<View>(R.id.tv_subscribe) as TextView
            val tv_text2 = dialog.findViewById<View>(R.id.tv_text2) as TextView
            tv_text2.visibility = View.GONE

            val authConfigResponse = CommonUtils.authconfig_response(this)

//                tv_text.setText(getResources().getString(R.string.qrcode_scantext));
            if (authConfigResponse != null) {
                val arrivedDestinationDialogText =
                    authConfigResponse.getcUSTOMMESSAGE().arrivedDestinationText
                val arrivedDestinationPoints =
                    authConfigResponse.getcUSTOMMESSAGE().arrivedDestinationPoints

                message.text = arrivedDestinationDialogText.replace("\\\\n".toRegex(), "\n")
                if (isPoints) {
                    tv_text2.text = arrivedDestinationPoints
                    tv_text2.visibility = View.VISIBLE
                    action.visibility = View.GONE
                    val handler = Handler()
                    handler.postDelayed({
                        hasReachedAtDestination = true
                        dialog.dismiss()
                        val intent = Intent(
                            this,
                            AddReviewActivity::class.java
                        )
                        intent.putExtra(AppConstants.WOLOO_ID, wolooId)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }, 3000)
                } else {
                    tv_text2.visibility = View.GONE
                    action.visibility = View.VISIBLE
                    action.text = "HOME"
                    action.setOnClickListener {
                        if (dialog.isShowing) dialog.dismiss()
                        val i: Intent = Intent(this, WolooDashboard::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(i)
                    }
                }

            }
            dialog.show()
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }


    fun showdialogGoToMaps() {
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_go_to_googlemaps)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val message = dialog.findViewById<View>(R.id.tv_text) as TextView
            val action = dialog.findViewById<View>(R.id.tv_positive) as TextView
            val cancel = dialog.findViewById<View>(R.id.tv_negative) as TextView

            val authConfigResponse = CommonUtils.authconfig_response(this)
//            message.text = "You will be redirected to Host using Google Maps. You will be awarded with Woloo Points,kindly come back to App to avail the Woloo Points."
            message.text = "Google Maps will be the next page you see. Once you reach at your location, return to the Woloo App to earn your reward points."
            action.visibility = View.VISIBLE
            action.text = "Continue"

            action.setOnClickListener {
                dialog.dismiss()
                SharedPrefSettings.getPreferences.storeIsDirectionWoloo(true)
                SharedPrefSettings.getPreferences.storeDirectionWoloo(DirectionWoloo(wolooId,dest!!.latitude,dest!!.longitude))
                goToMaps()
            }
            cancel.setOnClickListener { dialog.dismiss() }


            dialog.show()
        } catch (e: java.lang.Exception) {
              CommonUtils.printStackTrace(e)
        }
    }

    public data class DirectionWoloo (var wolooid : Int, var lat: Double, var lng: Double)

}