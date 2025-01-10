package `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.Window
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.application_kotlin.adapters.loo_discovery.NearestWalkAdapter
import `in`.woloo.www.mapdirection.GpsTracker
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility
import `in`.woloo.www.more.base_old.BaseActivity
import `in`.woloo.www.application_kotlin.adapters.loo_discovery.SearchPlacesAdapter
import `in`.woloo.www.application_kotlin.model.server_request.NearbyWolooRequest
import `in`.woloo.www.application_kotlin.model.lists_models.PlaceAutocomplete
import `in`.woloo.www.application_kotlin.view_models.HomeViewModel
import java.util.*

class SearchActivity() : BaseActivity(), OnMapReadyCallback {

    val TAG = SearchActivity::class.java.simpleName
    var placeAdapter: SearchPlacesAdapter? = null
    var searchAutoComplete: AutoCompleteTextView? = null
    var cbWolooWithOffers: CheckBox? = null
    var cbOpenNow: CheckBox? = null
    var cbBookmarkedWoloo: CheckBox? = null
    var tv2Km: TextView? = null
    var tv4Km: TextView? = null
    var tv6Km: TextView? = null
    var tv8Km: TextView? = null
    var tvAll: TextView? = null
    var tvNoWolooFound : View? = null
    var tvNoWolooText: TextView? = null
    var ivBack : ImageView? = null
    var ivCancel : ImageView? = null
    var wolooRv : RecyclerView? = null
    private var mMap: GoogleMap? = null
    private lateinit var locationManager: LocationManager
    private var nearByStoreResponseList: ArrayList<NearByStoreResponse.Data> = ArrayList()
    private var nearByStoreResponseListFromApi: ArrayList<NearByStoreResponse.Data> = ArrayList()
    private var bookmarkedWolooList = ArrayList<NearByStoreResponse.Data>()
    var mSharedPreference: SharedPreference? = null
    private var mLocationPermissionGranted = true
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    var dest: LatLng? = LatLng(19.055229, 72.830829)
    private lateinit var placesClient: PlacesClient
    var markerList: ArrayList<Marker> = ArrayList()
    private var currentpos: LatLng? = null
    var gps: GpsTracker? = null
    lateinit var homeViewModel: HomeViewModel
    var travelMode : Int = 0
    var range : Int = 2
    private var adapter: NearestWalkAdapter? = null
    var isFromHome : Boolean = false
    private val defaultLocation = LatLng(19.055229, 72.830829)

    /*Calling on onCreateView*/
    val DIALOGID = 2
    private var searchedLocation: String = ""
    private var wolooWithOffers = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
//        fragment_map = supportFragmentManager.findFragmentById(R.id.fragment_map)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        initViews()
        setProgressBar()
        setNetworkDetector()
        setLiveData()
        setProgressDialog()
    }

    override fun onResume() {
        super.onResume()
        Utility.hideKeyboard(this)
        try {
            if (WolooApplication.instance!!.nearByWoloo != null && WolooApplication.instance!!.updatedLikeStatus != -1) {
                val selectedWoloo: NearByStoreResponse.Data =
                    WolooApplication.instance!!.nearByWoloo!!
                if (nearByStoreResponseListFromApi.contains(selectedWoloo)) {
                    nearByStoreResponseListFromApi[nearByStoreResponseListFromApi.indexOf(
                        selectedWoloo
                    )].isLiked =
                        WolooApplication.instance!!.updatedLikeStatus
                }
                bookmarkedWolooList = ArrayList()
                for (i in nearByStoreResponseListFromApi.indices) {
                    if (nearByStoreResponseListFromApi[i].isLiked == 1) {
                        bookmarkedWolooList.add(nearByStoreResponseListFromApi[i])
                    }
                }
                if (cbBookmarkedWoloo!!.isChecked) {
                    renderNearByWoloos(bookmarkedWolooList)
                } else {
                    renderNearByWoloos(nearByStoreResponseListFromApi)
                }
                WolooApplication.instance!!.nearByWoloo = null
                WolooApplication.instance!!.updatedLikeStatus = -1
            }
        } catch (e: java.lang.Exception) {
              CommonUtils.printStackTrace(e)
        }
    }

    private fun initViews() {
                checkGpsAndRequestLocation()

        searchAutoComplete = findViewById(R.id.search_auto_complete)
        cbWolooWithOffers = findViewById(R.id.cbWolooWithOffers)
        cbOpenNow = findViewById(R.id.cbOpenNow)
        cbBookmarkedWoloo = findViewById(R.id.cbBookmarkedWoloo)
        tv2Km = findViewById(R.id.tv_2km)
        tv4Km = findViewById(R.id.tv_4km)
        tv6Km = findViewById(R.id.tv_6km)
        tv8Km = findViewById(R.id.tv_8km)
        tvAll = findViewById(R.id.tv_all)
        tvNoWolooText = findViewById(R.id.tvNoWolooText)
        tvNoWolooFound = findViewById(R.id.tvNoWolooFound)
        wolooRv = findViewById(R.id.wolooRv)
        ivBack = findViewById(R.id.ivBack)
        ivCancel = findViewById(R.id.iv_cancel)

        tvNoWolooFound?.visibility = View.INVISIBLE
        wolooRv?.visibility = View.GONE

        adapter = NearestWalkAdapter(this, nearByStoreResponseList)
        wolooRv?.setHasFixedSize(true)
        wolooRv?.layoutManager = LinearLayoutManager(this)
        wolooRv?.adapter = adapter

        travelMode = SharedPreference(this).getStoredPreference(this, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")!!.toInt()

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val displayMetrics = resources.displayMetrics
//        tv_woloo!!.visibility = View.VISIBLE
//        setWidthAndHeight(frm_home_map!!, height)

        val mapFragment = (supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment)
        mapFragment.getMapAsync(this)

        try {
            val key = CommonUtils.googlemapapikey(this)
            Places.initialize(this, key)
            placesClient = Places.createClient(this)
            placeAdapter = SearchPlacesAdapter(this, R.layout.item_search_autocomplete, placesClient)
            searchAutoComplete!!.setAdapter(placeAdapter)
            searchAutoComplete!!.threshold = 1
        } catch (ex: Exception) {
             CommonUtils.printStackTrace(ex);
        }
        searchAutoComplete!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, pos, id ->
            val place = adapterView.getItemAtPosition(pos) as PlaceAutocomplete
            searchedLocation = place.address.toString()
            searchAutoComplete!!.setText(place.address)
            searchAutoComplete!!.setSelection(searchAutoComplete!!.length())
            onPlaceClick(place)
            //                Toast.makeText(getContext(), place.address, Toast.LENGTH_SHORT).show();
        }

        cbWolooWithOffers?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
            getNearByWoloos(dest!!.latitude, dest!!.longitude, travelMode , range, 1, cbWolooWithOffers!!.isChecked, cbOpenNow!!.isChecked)
            wolooWithOffers = b
        })

        cbOpenNow?.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
            getNearByWoloos(dest!!.latitude, dest!!.longitude, travelMode , range, 1, wolooWithOffers, cbOpenNow!!.isChecked)
        })

        cbBookmarkedWoloo?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                //show bookmarked list
                renderNearByWoloos(bookmarkedWolooList)
            } else {
                //show normal list
                renderNearByWoloos(nearByStoreResponseListFromApi)
            }
        }

        tv2Km?.setOnClickListener {
            distanceRangeChanged(2)
        }

        tv4Km?.setOnClickListener {
            distanceRangeChanged(4)
        }

        tv6Km?.setOnClickListener {
            distanceRangeChanged(6)
        }

        tv8Km?.setOnClickListener {
            distanceRangeChanged(8)
        }

        tvAll?.setOnClickListener {
            distanceRangeChanged(25)
        }

        ivBack?.setOnClickListener { finish() }

        ivCancel?.setOnClickListener { searchAutoComplete?.setText("") }


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

    private fun distanceRangeChanged(changedRange : Int) {
        range = changedRange
        val selected = resources.getDrawable(R.drawable.yello_rectangle_shape)
        val notSelected = resources.getDrawable(R.drawable.rounded_gray_bg)
        tv2Km?.background = notSelected
        tv4Km?.background = notSelected
        tv6Km?.background = notSelected
        tv8Km?.background = notSelected
        tvAll?.background = notSelected
        Logger.e("range", " $range")
        when (range) {
            2 -> tv2Km?.background = selected
            4 -> tv4Km?.background = selected
            6 -> tv6Km?.background = selected
            8 -> tv8Km?.background = selected
            25 -> tvAll?.background = selected
            else -> Logger.e("range else", " $range");
        }
        getNearByWoloos(dest!!.latitude, dest!!.longitude, travelMode , range, 1, wolooWithOffers, cbOpenNow!!.isChecked)

    }

    private fun onPlaceClick(place: PlaceAutocomplete) {

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
//                    Toast.makeText(this, " onPlaceClick", Toast.LENGTH_SHORT).show()
                    val placeTemp = response?.place
                    try {
                        Utility.hideKeyboard(this)
                    } catch (ex: java.lang.Exception) {
                         CommonUtils.printStackTrace(ex);
                    }
                    dest = placeTemp?.latLng
                    getNearByWoloos(dest!!.latitude, dest!!.longitude, travelMode , range, 1, wolooWithOffers, cbOpenNow!!.isChecked)

//                    Toast.makeText(this, "getNearByWoloos onPlaceClick", Toast.LENGTH_SHORT).show()
                    val cameraPosition = CameraPosition.Builder()
                        .target(dest!!) // Sets the center of the map to Mountain View
                        .zoom(AppConstants.DEFAULT_ZOOM.toFloat()) // Sets the zoom
                        //                .bearing(30)                // Sets the orientation of the camera to east
                        .tilt(30f) // Sets the tilt of the camera to 30 degrees
                        .build() // Creates a CameraPosition from the builder


                    mMap?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 20, null)
                    mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(dest!!, AppConstants.DEFAULT_ZOOM.toFloat()))


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

    private fun getNearByWoloos(lat: Double, lng: Double, mode: Int, range: Int, isSearch: Int, isOffer: Boolean, openNow: Boolean) {
        val request = NearbyWolooRequest()
        request.lat = lat
        request.lng = lng
        request.mode = mode
        request.range = range
        if (isOffer) {
            request.isOffer = 1
        } else {
            request.isOffer = 0
        }
        if (openNow) {
            request.showAll = 0
        } else {
            request.showAll = 1
        }
        request.packageName = "in.woloo.app"
        request.isSearch = isSearch
        homeViewModel.getNearbyWoloos(request)
        progressbar?.show()

        val bundle = Bundle()
        bundle.putString(AppConstants.SEARCH_KEYWORD, searchAutoComplete!!.text.toString())
        bundle.putString(AppConstants.LOCATION, "($lat,$lng)")
        Utility.logFirebaseEvent(this, bundle, AppConstants.SEARCH_WOLOO_EVENT)

        val payload = HashMap<String, Any>()
        payload[AppConstants.SEARCH_KEYWORD] = searchAutoComplete!!.text.toString()
        payload[AppConstants.LOCATION] = "($lat,$lng)"
        Utility.logNetcoreEvent(this, payload, AppConstants.SEARCH_WOLOO_EVENT)
    }

    private fun setLiveData() {

        homeViewModel.observeNearByWoloo().observe(this) { arrayListBaseResponse ->
            Utility.hideKeyboard(this@SearchActivity)
            if(progressbar?.isShowing == true) progressbar?.dismiss()
            if (arrayListBaseResponse != null) {
                nearByStoreResponseListFromApi = arrayListBaseResponse.data!!
            } else {
                nearByStoreResponseListFromApi = ArrayList<NearByStoreResponse.Data>()
            }
            bookmarkedWolooList = ArrayList()
            for (i in nearByStoreResponseListFromApi.indices) {
                if (nearByStoreResponseListFromApi.get(i).isLiked == 1) {
                    bookmarkedWolooList.add(nearByStoreResponseListFromApi[i])
                }
            }
            if (cbBookmarkedWoloo!!.isChecked) {
                renderNearByWoloos(bookmarkedWolooList)
            } else {
                renderNearByWoloos(nearByStoreResponseListFromApi)
            }
        }
    }

    private fun getCompleteAddressString(LATITUDE: Double, LONGITUDE: Double): String? {
        var strAdd = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses: MutableList<Address>? = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1) // Changed by Aarati List to Mutable
            if (addresses != null) {
                val returnedAddress: Address = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.getMaxAddressLineIndex()) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString()
                Logger.w(" loction address", strReturnedAddress.toString())
            } else {
                Logger.w(" loction address", "No Address returned!")
            }
        } catch (e: java.lang.Exception) {
              CommonUtils.printStackTrace(e)
            Logger.w("loction address", "Canont get Address!")
        }
        return strAdd
    }

    private fun renderNearByWoloos(data: ArrayList<NearByStoreResponse.Data>) {
        try {
            markerList = ArrayList<Marker>()
            mMap?.clear()
            nearByStoreResponseList.clear()
            nearByStoreResponseList.addAll(data)
            adapter!!.notifyDataSetChanged()
            try {
                if(nearByStoreResponseList.isEmpty()){
                    if (cbOpenNow?.isChecked == true) {
                        tvNoWolooText?.text = resources.getString(R.string.no_woloo_found_at_moment)
                    } else if (cbBookmarkedWoloo?.isChecked == true) {
                        tvNoWolooText?.text = resources.getString(R.string.no_woloo_found_bookmark)
                    } else {
                        tvNoWolooText?.text = resources.getString(R.string.no_woloo_found)
                    }
                    tvNoWolooFound?.visibility = View.VISIBLE
                    wolooRv?.visibility = View.GONE

                    if (dest != null) {
                        val bundle = Bundle()
                        val payload = HashMap<String, Any>()
                        bundle.putString(AppConstants.LOCATION, "(${dest!!.latitude}, ${dest!!.longitude})")
                        bundle.putString(AppConstants.SEARCH_KEYWORD, searchedLocation)
                        payload[AppConstants.LOCATION] ="(${dest!!.latitude}, ${dest!!.longitude})"
                        payload[AppConstants.SEARCH_KEYWORD] = searchedLocation
                        Utility.logFirebaseEvent(this, bundle, AppConstants.NO_LOCATION_FOUND)
                        Utility.logNetcoreEvent(this, payload, AppConstants.NO_LOCATION_FOUND)
                    }

                }else{
                    tvNoWolooFound?.visibility = View.GONE
                    wolooRv?.visibility = View.VISIBLE
                }
            } catch (ex: java.lang.Exception) {
                 CommonUtils.printStackTrace(ex);
            }
            for ((i, data) in nearByStoreResponseList.withIndex()) {
                markerList.add(createMarker(data.lat!!.toDouble(), data.lng!!.toDouble(), data.name.toString() , "", R.drawable.ic_store_mark_dest, i))
            }
            animateCameraToMarkerPosition(0)
        } catch (e: java.lang.Exception) {
              CommonUtils.printStackTrace(e)
        }
    }

    private fun createMarker(latitude: Double, longitude: Double, title: String, snippet: String, iconResID: Int, index: Int): Marker{
        Logger.i(TAG, "createMarker")
        mMap?.uiSettings?.isZoomGesturesEnabled = true
        val height = 110
        val width = 90
        val bitmapdraw = resources.getDrawable(R.drawable.ic_store_mark_dest) as BitmapDrawable
        val b = bitmapdraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
        return mMap?.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude)) //    .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet)
                .zIndex(index.toFloat()) //  .snippet(snippet));
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
        )!!
    }

    fun animateCameraToMarkerPosition(position: Int) {
        if (markerList.isNotEmpty()) {
            Logger.i(TAG, "animateCameraToMarkerPosition")
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


    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        try{
            mMap = googleMap
            Logger.i(TAG, "onMapReady")
            mMap!!.isMyLocationEnabled = true
            mMap!!.uiSettings.isZoomControlsEnabled = true;
            mMap!!.uiSettings.isMyLocationButtonEnabled = true
//            Toast.makeText(this,"OnMapReady",Toast.LENGTH_SHORT).show()
            moveCameraToDefaultLocation(true)
            checkGpsAndRequestLocation()
        }catch (e : java.lang.Exception){
              CommonUtils.printStackTrace(e)
        }
    }

    private fun moveCameraToDefaultLocation(shouldShowNearbyLoos: Boolean) {
        Logger.i(TAG, "moveCameraToDefaultLocation")
        val cameraUpdate =
            CameraUpdateFactory.newLatLngZoom(defaultLocation, AppConstants.DEFAULT_ZOOM.toFloat())
        mMap!!.moveCamera(cameraUpdate)
        if (shouldShowNearbyLoos) {
            getNearByWoloos( defaultLocation.latitude, defaultLocation.longitude, travelMode , range, 1, wolooWithOffers, cbOpenNow!!.isChecked)

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
            result.addOnCompleteListener(object : OnCompleteListener<LocationSettingsResponse?> {
                override fun onComplete(task: Task<LocationSettingsResponse?>) {
                    try {
                        val response = task.getResult(
                            ApiException::class.java
                        )
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        getDeviceLocation()
                        updateLocationUI()
                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    } catch (exception: ApiException) {
                        when (exception.statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                                val resolvable = exception as ResolvableApiException
                                startIntentSenderForResult(
                                    resolvable.resolution.intentSender,
                                    DIALOGID,
                                    null,
                                    0,
                                    0,
                                    0,
                                    null
                                )
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
            })
        } else {
            getLocationPermission()
        }
    }

    fun isLocationPermissionGranted(): Boolean {
        return (ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkGpsAndRequestLocation()
                } else {
                    Toast.makeText(this, "Unable to get your location, please try again", Toast.LENGTH_SHORT).show()
                    moveCameraToDefaultLocation(true)
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
                Activity.RESULT_CANCELED -> moveCameraToDefaultLocation(true)
            }
        }
    }
    private fun isLocationEnabled() {
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
                finish()
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
            if(intent.hasExtra("lat") && intent.hasExtra("lng")){
                isFromHome = true
                dest = LatLng(
                    intent.getDoubleExtra("lat",defaultLocation.latitude),
                    intent.getDoubleExtra("lng",defaultLocation.longitude))
                val destName = getCompleteAddressString(dest!!.latitude,dest!!.longitude)
                if(destName?.isNotEmpty() == true) {
                    searchAutoComplete?.setText(destName)
                    searchedLocation = destName
                }

            }else {
                isLocationEnabled()
                gps = GpsTracker(this)

                currentpos = LatLng(gps!!.latitudeC, gps!!.longitudeC)
                dest = currentpos
            }
//            Toast.makeText(this,"dest : $isFromHome",Toast.LENGTH_SHORT).show()
            getNearByWoloos(dest!!.latitude, dest!!.longitude, travelMode , range, 1, wolooWithOffers, cbOpenNow!!.isChecked)

            val cameraPosition = CameraPosition.Builder()
                .target(dest!!) // Sets the center of the map to Mountain View
                .zoom(AppConstants.DEFAULT_ZOOM.toFloat()) // Sets the zoom
                //                .bearing(30)                // Sets the orientation of the camera to east
                .tilt(30f) // Sets the tilt of the camera to 30 degrees
                .build() // Creates a CameraPosition from the builder
            mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 20, null)
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(dest!!, AppConstants.DEFAULT_ZOOM.toFloat()))

            if (isLocationPermissionGranted()) {
                mMap?.setMyLocationEnabled(true)
                mMap?.getUiSettings()?.setMyLocationButtonEnabled(true)
                mMap?.getUiSettings()?.setZoomGesturesEnabled(true)
                mMap?.setOnMyLocationButtonClickListener(OnMyLocationButtonClickListener {
                    Utility.hideKeyboard(this)
                    val location: Location = mMap?.getMyLocation()!!
                   dest= LatLng(location.latitude,location.longitude)
                    getNearByWoloos(dest!!.latitude, dest!!.longitude, travelMode , range, 1, wolooWithOffers, cbOpenNow!!.isChecked)
                    true
                })

            } else {
                mMap?.setMyLocationEnabled(false)
                mMap?.getUiSettings()?.setMyLocationButtonEnabled(false)
                moveCameraToDefaultLocation(true)
                mMap?.setOnMyLocationClickListener(null)
            }

        } catch (e: SecurityException) {
            Logger.e("Exception: %s", e.message!!)
        }
        Logger.i(TAG, "updateLocationUI")
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

                        if (task.isSuccessful) {
                            // Set the map's camera position to the current location of the device.
//                            val location = task.result
//                            //                            Smartech.getInstance(new WeakReference(requireContext())).setUserLocation(location);
//                            val currentLatLng = LatLng(
//                                location.latitude,
//                                location.longitude
//                            )
//
//
//                            if(!isFromHome) dest = currentLatLng
//                            Toast.makeText(this, "dest : $isFromHome", Toast.LENGTH_SHORT).show()
//
//                            getNearByWoloos(
//                                dest!!.latitude,
//                                dest!!.longitude,
//                                travelMode,
//                                range,
//                                1,
//                                cbWolooWithOffers!!.isChecked,
//                                cbOpenNow!!.isChecked
//                            )
//
//                            val update = CameraUpdateFactory.newLatLngZoom(
//                                dest!!,
//                                AppConstants.DEFAULT_ZOOM.toFloat()
//                            )
//                            //                mMap.clear();
//                            mMap?.moveCamera(update)
                        }

                }
            } else {

            }
        } catch (e: java.lang.Exception) {
            Logger.e("Exception: %s", e.message!!)
        }
    }


}