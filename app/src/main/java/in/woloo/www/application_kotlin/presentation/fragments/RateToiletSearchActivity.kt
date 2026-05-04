package `in`.woloo.www.application_kotlin.presentation.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.adapters.NearByRateSearchAdapter
import `in`.woloo.www.application_kotlin.model.server_request.LocationForRate
import `in`.woloo.www.application_kotlin.model.server_request.Pagination
import `in`.woloo.www.application_kotlin.model.server_request.SearchWolooRequest
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.view_models.HomeViewModel
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.ActivityRateToiletSearchBinding
import `in`.woloo.www.databinding.TopBrandsListItemBinding
import `in`.woloo.www.utils.Logger

class RateToiletSearchActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRateToiletSearchBinding
    lateinit var homeViewModel: HomeViewModel
    private var nearByStoreResponseList: ArrayList<NearByStoreResponse.DataSearch> = ArrayList()
    private lateinit var nearByStoreResponseSelected: NearByStoreResponse.DataSearch
    private var nearByStoreNameList: ArrayList<String> = ArrayList()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST = 1001
    var latKnown : String  = "19.055229"
    var lngKonwn : String = "72.830829"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRateToiletSearchBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermissionAndFetch()
        //binding.searchAutoComplete.dropDownHeight = ViewGroup.LayoutParams.MATCH_PARENT
        binding.searchAutoComplete.dropDownWidth = ViewGroup.LayoutParams.MATCH_PARENT
        val topMarginDp = 16
        val scale = resources.displayMetrics.density
        val topMarginPx = (topMarginDp * scale + 0.5f).toInt()
        binding.searchAutoComplete.dropDownVerticalOffset = topMarginPx
        binding.searchAutoComplete.setDropDownBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var searchTextWatcher = object :  TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Optional: Do something before text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Called when the text is being changed
                Log.d("AutoComplete", "Text changed: $s")
            }

            override fun afterTextChanged(s: Editable?) {
                // Optional: Do something after text changes
                val queryText = s.toString()

                val pagination = Pagination(
                    limit = 10,   // Set desired limit
                    page = 1      // Set current page number
                )

                val location =     LocationForRate(
                    lat = latKnown.toString() ,
                    lng = lngKonwn.toString()
                )




                val request = SearchWolooRequest(
                    query = queryText,
                    pagination = pagination,
                    location = location
                )

                homeViewModel.searchNearbyWoloos(request)

            }
        }

        binding.searchAutoComplete.addTextChangedListener(searchTextWatcher)


        binding.searchAutoComplete.setOnItemClickListener { _, _, position, _ ->
            binding.searchAutoComplete.removeTextChangedListener(searchTextWatcher)
            val selectedStore = nearByStoreResponseList[position]
            val selectedName = selectedStore.name.toString()//nearByStoreNameList[position]
            Logger.i("Aarati Rate a toilet", "selectedName $selectedName")
            for(i in nearByStoreResponseList.indices)
            {
                Logger.i("Aarati Rate a toilet", "selectedName from List $nearByStoreResponseList[i].name")
                if(nearByStoreResponseList[i].name == selectedName)
                {
                    nearByStoreResponseSelected = nearByStoreResponseList[i]
                    break
                }
            }
            nearByStoreResponseSelected = nearByStoreResponseList.find { it.name == selectedName }!!
            val resultIntent = Intent().apply {
                putExtra("PLACE_NEW", Gson().toJson(nearByStoreResponseSelected))  // Ensure `place` implements Parcelable

            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()

        }


        homeViewModel.observeSearchNearByWoloo().observe(this) { response ->
            try {
                if(response.data != null) {
                    val wrapper = response?.data
                    nearByStoreResponseList.clear()
                    nearByStoreNameList.clear()
                    nearByStoreResponseList = wrapper?.data ?: arrayListOf()
                    nearByStoreNameList.addAll(nearByStoreResponseList.map { it.name.orEmpty() })
                    Logger.i("RateToiletFragment", "onCreateView2")
                    val adapter = NearByRateSearchAdapter(
                        this,
                        nearByStoreResponseList
                    )
                    binding.searchAutoComplete.setAdapter(adapter)
                    binding.searchAutoComplete.showDropDown()
                }
                else
                {
                    binding.searchAutoComplete.dismissDropDown()
                }
            }catch (e : Exception)
            {
                CommonUtils.printStackTrace(e)
            }


        }
    }

    private fun checkLocationPermissionAndFetch() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Ask for permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST
            )
        } else {
            // Permission already granted
            getCurrentLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d("LocationActivity", "Lat: $latitude, Lng: $longitude")
                    latKnown = latitude.toString()
                    lngKonwn = longitude.toString()

                } else {
                    Log.e("LocationActivity", "Location is null, request new location update")
                    requestNewLocationData()
                }
            }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY, 1000
        ).setMaxUpdates(1).build()

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location ->
            if (location != null) {
                Log.d("LocationActivity", "Updated Lat: ${location.latitude}, Lng: ${location.longitude}")
            }
        }
    }

    // Handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

