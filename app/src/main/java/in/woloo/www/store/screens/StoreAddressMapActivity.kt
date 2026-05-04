package `in`.woloo.www.store.screens

import android.Manifest
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.adapters.loo_discovery.SearchPlacesAdapter
import `in`.woloo.www.application_kotlin.model.lists_models.PlaceAutocomplete
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.ActivityStoreAddressMapBinding
import `in`.woloo.www.store.user_details.AddressList
import `in`.woloo.www.utils.Logger
import java.util.Locale

class StoreAddressMapActivity: AppCompatActivity() , OnMapReadyCallback {

    private lateinit var binding: ActivityStoreAddressMapBinding
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var placeAdapter: SearchPlacesAdapter? = null
    lateinit var mPlacesClient: PlacesClient
    private var selectedLatLng: LatLng? = null
    var addressNew = ""
    var cityNew = ""
    var pinCodeNew = ""
    var stateNew = ""
    var area = ""
    var building = ""
    private var mapMarker: Marker? = null
    var fromFragment: String? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStoreAddressMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
        val addressJson = intent.getStringExtra("ADDRESS_ID")
            fromFragment = intent.getStringExtra("FROM_FRAGMENT")

        val addressObject = Gson().fromJson(addressJson, AddressList::class.java)

        val bottomSheetFragment = AddEditAddressBottomSheetFragment.newInstance(addressObject , fromFragment!!)
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)

        if(addressObject != null)
        {
            binding.searchAutoComplete.setText(addressObject.city + "," + addressObject.province)

        }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }

        val hintColors = listOf(
            R.color.application_background,
            R.color.colorAccent,
           R.color.blue_theme,
           R.color.green_line,
            R.color.yello_line,
            R.color.red_line// orange
        )

        val blinkAnimation = AlphaAnimation(0.0f, 1.0f).apply {
            duration = 500
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
        }

        var colorIndex = 0
        val colorAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 700
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART

            addUpdateListener { animator ->
                val startColor = hintColors[colorIndex % hintColors.size]
                val endColor = hintColors[(colorIndex + 1) % hintColors.size]
                val fraction = animator.animatedFraction
                val blendedColor = ArgbEvaluator().evaluate(fraction, startColor, endColor) as Int
                binding.searchAutoComplete.setHintTextColor(blendedColor)

                if (fraction == 1f) colorIndex++
            }
        }


        if (binding.searchAutoComplete.text.isNullOrEmpty()) {
            binding.searchAutoComplete.startAnimation(blinkAnimation)
        }

        binding.searchAutoComplete.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    binding.searchAutoComplete.startAnimation(blinkAnimation)
                    colorAnimator.start()
                } else {
                    binding.searchAutoComplete.clearAnimation()
                    colorAnimator.cancel()
                    binding.searchAutoComplete.setHintTextColor(getColor(R.color.application_background))
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })


        try {
            val key = CommonUtils.googlemapapikey(applicationContext)
            Places.initialize(applicationContext, key)
            mPlacesClient = Places.createClient(applicationContext)
            placeAdapter =
                SearchPlacesAdapter(applicationContext, R.layout.item_search_autocomplete, mPlacesClient)
            binding.searchAutoComplete!!.setAdapter(placeAdapter)
            binding.searchAutoComplete?.threshold = 1



        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Initialize Map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setupSearchAutocomplete()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isMyLocationButtonEnabled = true

        checkLocationPermissionAndShowLocation()

        googleMap.setOnMapClickListener { latLng ->
            selectedLatLng = latLng
            moveMapTo(latLng)
            //selectedLatLng = googleMap.cameraPosition.target

            selectedLatLng?.let { latLng ->
                val addressCalculated = getAddressFromLatLng(
                    applicationContext,
                    latLng.latitude,
                    latLng.longitude
                )
                addressNew = addressCalculated
                binding.searchAutoComplete.setText(addressCalculated.toString())
            }

            Log.d("StoreMap", "Selected LatLng: ${selectedLatLng!!.latitude}, ${selectedLatLng!!.longitude}")
            val bottomSheetFragment = AddEditAddressBottomSheetFragment.newInstance(fromFragment!! ,cityNew ,pinCodeNew , stateNew , area , building , addressNew)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }





    }

  /* private fun moveMapTo(latLng: LatLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
    }
*/






    private fun setupSearchAutocomplete() {


        binding.searchAutoComplete.dropDownWidth = ViewGroup.LayoutParams.MATCH_PARENT
        binding.searchAutoComplete.dropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.searchAutoComplete.dropDownHorizontalOffset = 250



        binding.searchAutoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                val place = adapterView.getItemAtPosition(pos) as PlaceAutocomplete
                Logger.i("TAG", "Aarati" + place.placeId)
                binding.searchAutoComplete?.setText(place.address)
                binding.searchAutoComplete?.setSelection(binding.searchAutoComplete!!.length())
                var addressCalculated: String = place.address.toString()
                getLatLngFromPlaceId(place.placeId.toString(), mPlacesClient) { latLng ->
                    if (latLng != null) {
                        Log.d(
                            "MainActivity",
                            "Received LatLng: ${latLng.latitude}, ${latLng.longitude}"
                        )
                        addressCalculated = getAddressFromLatLng(
                            applicationContext,
                            latLng.latitude,
                            latLng.longitude
                        )
                        Log.d("MainActivity", addressCalculated)

                        binding.searchAutoComplete?.setText(place.address)
                        binding.searchAutoComplete?.setSelection(binding.searchAutoComplete?.length()!!)
                        Log.d("Address in search is 1", place.address.toString())

                        Log.d("MainActivity1", addressCalculated)
                        moveMapTo(latLng)

                        val bottomSheetFragment = AddEditAddressBottomSheetFragment.newInstance(fromFragment!! ,cityNew ,pinCodeNew , stateNew , area , building , addressNew)
                        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                    }
                }
            }
    }


    fun getLatLngFromPlaceId(placeId: String, placesClient: PlacesClient, callback: (LatLng?) -> Unit) {
        val placeFields = listOf(Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.builder(placeId, placeFields).build()

        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                val latLng = response.place.latLng
                if (latLng != null) {
                    Log.d("PlaceLatLng", "Latitude: ${latLng.latitude}, Longitude: ${latLng.longitude}")
                } else {
                    Log.e("PlaceLatLng", "LatLng not found for placeId: $placeId")
                }
                callback(latLng) // Return result via callback
            }
            .addOnFailureListener { exception ->
                Log.e("PlaceLatLng", "Error fetching place details: ${exception.message}")
                callback(null) // Return null on failure
            }
    }


    fun checkLocationPermissionAndShowLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    selectedLatLng = currentLatLng
                    moveMapTo(currentLatLng)

                    // ✅ Get address and show bottom sheet
                    val addressCalculated = getAddressFromLatLng(
                        applicationContext,
                        currentLatLng.latitude,
                        currentLatLng.longitude
                    )
                    addressNew = addressCalculated
                    moveMapTo(currentLatLng)
                    binding.searchAutoComplete.setText(addressCalculated.toString())
                    if(intent.getStringExtra("ADDRESS_ID").isNullOrEmpty()) {
                        val bottomSheetFragment = AddEditAddressBottomSheetFragment.newInstance(
                            fromFragment!!,
                            cityNew,
                            pinCodeNew,
                            stateNew,
                            area,
                            building,
                            addressNew
                        )
                        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                    }
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        }
    }


    fun getAddressFromLatLng(context: Context, latitude: Double, longitude: Double): String {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val fullAddress = address.getAddressLine(0) // Get full address
                cityNew = address.locality
                pinCodeNew = address.postalCode
                stateNew = address.adminArea
                area = address.subLocality
                building = address.premises
               // addressNew =
                Log.d("Geocoder", "Address: $fullAddress")
                fullAddress
            } else {
                ""
            }
        } catch (e: Exception) {
            Log.e("Geocoder", "Error: ${e.message}")
            ""
        }


    }


    private fun moveMapTo(latLng: LatLng) {
        googleMap.setOnMapLoadedCallback {
            // Place or move marker
            if (mapMarker == null) {
                mapMarker = googleMap.addMarker(MarkerOptions().position(latLng))
            } else {
                mapMarker?.position = latLng
            }

            // Offset camera for static marker icon position (e.g. 110dp from top)
            val projection = googleMap.projection
            val screenPoint = projection.toScreenLocation(latLng)



            val adjustedLatLng = projection.fromScreenLocation(screenPoint)

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(adjustedLatLng, 14f))
        }
    }



    // Handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkLocationPermissionAndShowLocation()
        }
    }
}



