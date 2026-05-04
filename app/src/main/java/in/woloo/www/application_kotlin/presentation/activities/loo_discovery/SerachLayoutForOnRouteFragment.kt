package `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.adapters.loo_discovery.SearchPlacesAdapter
import `in`.woloo.www.application_kotlin.model.lists_models.PlaceAutocomplete
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility
import java.util.Arrays
import java.util.Locale


class SerachLayoutForOnRouteFragment : AppCompatActivity() {

    private lateinit var rootView: View

    @BindView(R.id.search_auto_complete)
    lateinit var searchAutoComplete: AutoCompleteTextView

    @BindView(R.id.suggestionRecyclerView)
    lateinit var recyclerViewPlaces: RecyclerView

    private var map: GoogleMap? = null

    lateinit var customeIntent :Intent

    var keyAssigned: String? = null
    var sourceAddress: String? = null
    var destAddress: String? = null


    var placeAdapter: SearchPlacesAdapter? = null
    lateinit var mPlacesClient: PlacesClient
     var cityNew = ""
    var pinCodeNew = ""
    var stateNew = ""
    var area = ""
    var building = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_serach_layout_for_on_route)
        ButterKnife.bind(this)
        customeIntent = intent
        keyAssigned = customeIntent.getStringExtra("EXTRA_KEY")

        sourceAddress = customeIntent.getStringExtra("SOURCE_ADDRESS")
            destAddress = customeIntent.getStringExtra("DEST_ADDRESS")
        searchAutoComplete?.addTextChangedListener(filterTextWatcher)

        searchAutoComplete.dropDownWidth = ViewGroup.LayoutParams.MATCH_PARENT
        searchAutoComplete.dropDownHeight = ViewGroup.LayoutParams.WRAP_CONTENT
        searchAutoComplete.dropDownHorizontalOffset = 250


        searchAutoComplete?.setOnItemClickListener(OnItemClickListener { adapterView, view, pos, id ->
            val place = adapterView.getItemAtPosition(pos) as PlaceAutocomplete
            searchAutoComplete?.setText(place.address)
            searchAutoComplete?.setSelection(searchAutoComplete!!.length())
        })




        //checkGpsAndRequestLocation();

        try {
            val key = CommonUtils.googlemapapikey(applicationContext)
            Places.initialize(applicationContext, key)
            //                Places.initialize(getContext(), getResources().getString(R.string.google_maps_key));
//            searchAutoComplete.addTextChangedListener(filterTextWatcher);
            mPlacesClient = Places.createClient(applicationContext)
            placeAdapter =
                SearchPlacesAdapter(applicationContext, R.layout.item_search_autocomplete, mPlacesClient)
            searchAutoComplete!!.setAdapter(placeAdapter)
            searchAutoComplete?.threshold = 1

        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }


        searchAutoComplete!!.addTextChangedListener(filterTextWatcher)
        searchAutoComplete!!.onItemClickListener =
            OnItemClickListener { adapterView, view, pos, id ->
                val place = adapterView.getItemAtPosition(pos) as PlaceAutocomplete
                Logger.i("TAG", "Aarati" + place.placeId)
                searchAutoComplete!!.setText(place.address)
                searchAutoComplete!!.setSelection(searchAutoComplete!!.length())

            }

        searchAutoComplete!!.addTextChangedListener(filterTextWatcher)
        searchAutoComplete!!.onItemClickListener =
            OnItemClickListener { adapterView, view, pos, id ->
                val place = adapterView.getItemAtPosition(pos) as PlaceAutocomplete
                Logger.i("TAG", "Aarati" + place.placeId)
                searchAutoComplete!!.setText(place.address)
                searchAutoComplete!!.setSelection(searchAutoComplete!!.length())

            }

        searchAutoComplete?.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                val place = adapterView.getItemAtPosition(pos) as PlaceAutocomplete
                Logger.i("TAG", "Aarati" + place.placeId)
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

                        searchAutoComplete?.setText(place.address)
                        searchAutoComplete?.setSelection(searchAutoComplete?.length()!!)
                        Log.d("Address in search is 1" , place.address.toString())
                        Logger.d("New Address is 1" ,keyAssigned.toString())


                        if(keyAssigned!!.equals("become_address" , ignoreCase = true))
                        {
                            Log.d("MainActivity1", addressCalculated)
                            val resultIntent = Intent().apply {
                                putExtra("EXTRA_KEY", keyAssigned)
                                putExtra("PLACE_NEW", place)  // Ensure `place` implements Parcelable
                                putExtra("NEW_ADDRESS", addressCalculated)
                                putExtra("NEW_LATITUDE" ,  latLng.latitude)
                                putExtra("NEW_LONGITUDE" ,  latLng.longitude)
                                putExtra("NEW_CITY" ,  cityNew)
                                putExtra("NEW_PINCODE" ,  pinCodeNew)
                            }
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        }
                        else   if(keyAssigned!!.equals("search_host" , ignoreCase = true))
                        {
                            Log.d("MainActivity1", addressCalculated)
                            val resultIntent = Intent().apply {
                                putExtra("EXTRA_KEY", keyAssigned)
                                putExtra("PLACE_NEW", place)  // Ensure `place` implements Parcelable
                                putExtra("NEW_ADDRESS", addressCalculated)
                                putExtra("NEW_LATITUDE" ,  latLng.latitude)
                                putExtra("NEW_LONGITUDE" ,  latLng.longitude)
                                putExtra("NEW_CITY" ,  cityNew)
                                putExtra("NEW_PINCODE" ,  pinCodeNew)
                                putExtra("SEARCHED_TEXT" ,  searchAutoComplete?.text.toString())
                            }
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        }
                        else if(keyAssigned!!.equals("ADD_EDIT_USER_ADDRESS" , ignoreCase = true))
                            {
                                Log.d("MainActivity1", addressCalculated)
                                val resultIntent = Intent().apply {
                                    putExtra("EXTRA_KEY", keyAssigned)
                                    putExtra("PLACE_NEW", place)  // Ensure `place` implements Parcelable
                                    putExtra("NEW_ADDRESS", addressCalculated)
                                    putExtra("NEW_LATITUDE" ,  latLng.latitude)
                                    putExtra("NEW_LONGITUDE" ,  latLng.longitude)
                                    putExtra("NEW_CITY" ,  cityNew)
                                    putExtra("NEW_STATE" ,  stateNew)
                                    putExtra("NEW_PINCODE" ,  pinCodeNew)
                                    putExtra("NEW_AREA" ,  area)
                                    putExtra("NEW_BUILDING" ,  building)

                                }
                                setResult(Activity.RESULT_OK, resultIntent)
                                finish()
                            }
                        else if(keyAssigned!!.equals("refer_address" , ignoreCase = true))
                        {
                            Log.d("MainActivity1", addressCalculated)
                            val resultIntent = Intent().apply {
                                putExtra("EXTRA_KEY", keyAssigned)
                                putExtra("PLACE_NEW", place)  // Ensure `place` implements Parcelable
                                putExtra("NEW_ADDRESS", addressCalculated)
                                putExtra("NEW_LATITUDE" ,  latLng.latitude)
                                putExtra("NEW_LONGITUDE" ,  latLng.longitude)
                                putExtra("NEW_CITY" ,  cityNew)
                                putExtra("NEW_PINCODE" ,  pinCodeNew)
                            }
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        }
                        else if(keyAssigned!!.equals("rate_a_toilet" , ignoreCase = true))
                        {
                            Log.d("MainActivity1", addressCalculated)
                            val resultIntent = Intent().apply {
                                putExtra("EXTRA_KEY", keyAssigned)
                                putExtra("PLACE_NEW", place)  // Ensure `place` implements Parcelable
                                putExtra("NEW_ADDRESS", addressCalculated)
                                putExtra("NEW_LATITUDE" ,  latLng.latitude)
                                putExtra("NEW_LONGITUDE" ,  latLng.longitude)
                                putExtra("NEW_CITY" ,  cityNew)
                                putExtra("NEW_PINCODE" ,  pinCodeNew)
                            }
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        }
                        else if(keyAssigned!!.equals("profile_address" , ignoreCase = true))
                        {
                            Log.d("MainActivity1", addressCalculated)
                            val resultIntent = Intent().apply {
                                putExtra("EXTRA_KEY", keyAssigned)
                                putExtra("PLACE_NEW", place)  // Ensure `place` implements Parcelable
                                putExtra("NEW_ADDRESS", addressCalculated)
                                putExtra("NEW_LATITUDE" ,  latLng.latitude)
                                putExtra("NEW_LONGITUDE" ,  latLng.longitude)
                                putExtra("NEW_CITY" ,  cityNew)
                                putExtra("NEW_PINCODE" ,  pinCodeNew)
                            }
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        }
                        else if(keyAssigned!!.equals("vtion_address" , ignoreCase = true))
                        {
                            Log.d("MainActivity1", "$addressCalculated $keyAssigned")
                            val resultIntent = Intent().apply {
                                putExtra("EXTRA_KEY", keyAssigned)
                                putExtra("PLACE_NEW", place)  // Ensure `place` implements Parcelable
                                putExtra("NEW_ADDRESS", addressCalculated)
                                putExtra("NEW_LATITUDE" ,  latLng.latitude)
                                putExtra("NEW_LONGITUDE" ,  latLng.longitude)
                                putExtra("NEW_CITY" ,  cityNew)
                                putExtra("NEW_PINCODE" ,  pinCodeNew)
                            }
                            setResult(Activity.RESULT_OK, resultIntent)
                            Handler(Looper.getMainLooper()).postDelayed({
                                finish()
                            }, 200)
                        }
                        else if(keyAssigned!!.equals("select_gender_address" , ignoreCase = true))
                        {
                            Log.d("MainActivity1", addressCalculated)
                            val resultIntent = Intent().apply {
                                putExtra("EXTRA_KEY", keyAssigned)
                                putExtra("PLACE_NEW", place)  // Ensure `place` implements Parcelable
                                putExtra("NEW_ADDRESS", addressCalculated)
                                putExtra("NEW_LATITUDE" ,  latLng.latitude)
                                putExtra("NEW_LONGITUDE" ,  latLng.longitude)
                                putExtra("NEW_CITY" ,  cityNew)
                                putExtra("NEW_PINCODE" ,  pinCodeNew)
                            }
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        }
                        else if(keyAssigned!!.equals("current" , ignoreCase = true))
                        {
                            val intent = Intent(this, EnrouteDirectionActivity::class.java)
                            intent.putExtra("EXTRA_KEY", keyAssigned)
                            intent.putExtra("PLACE_NEW" , place)
                            intent.putExtra("NEW_ADDRESS" , place.address.toString())
                            intent.putExtra("SOURCE_ADDRESS", sourceAddress)
                            intent.putExtra("DEST_ADDRESS", destAddress)
                            startActivity(intent)
                            finish()
                        }
                        else if(keyAssigned!!.equals("destination" , ignoreCase = true))
                        {
                            val intent = Intent(this, EnrouteDirectionActivity::class.java)
                            intent.putExtra("EXTRA_KEY", keyAssigned)
                            intent.putExtra("PLACE_NEW" , place)
                            intent.putExtra("NEW_ADDRESS" , place.address.toString())
                            intent.putExtra("SOURCE_ADDRESS", sourceAddress)
                            intent.putExtra("DEST_ADDRESS", destAddress)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            val intent = Intent(this, EnrouteDirectionActivity::class.java)
                            intent.putExtra("EXTRA_KEY", keyAssigned)
                            intent.putExtra("PLACE_NEW" , place)
                            intent.putExtra("NEW_ADDRESS" , place.address.toString())
                            intent.putExtra("SOURCE_ADDRESS", sourceAddress)
                            intent.putExtra("DEST_ADDRESS", destAddress)
                            startActivity(intent)
                            finish()
                        }


                        //                Toast.makeText(getContext(), place.address, Toast.LENGTH_SHORT).show();


                    } else {
                        Log.e("MainActivity", "Failed to get LatLng")
                        addressCalculated = place.address.toString()
                        searchAutoComplete?.setText(place.address)
                        searchAutoComplete?.setSelection(searchAutoComplete?.length()!!)
                        Log.d("Address in search is 1" , place.address.toString())
                        Logger.d("New Address is 1" ,keyAssigned.toString())


                        if(keyAssigned!!.equals("become_address" , ignoreCase = true))
                        {
                            Log.d("MainActivity1", addressCalculated)
                            val resultIntent = Intent().apply {
                                putExtra("EXTRA_KEY", keyAssigned)
                                putExtra("PLACE_NEW", place)  // Ensure `place` implements Parcelable
                                putExtra("NEW_ADDRESS", addressCalculated)
                                putExtra("NEW_LATITUDE" , 0.0)
                                putExtra("NEW_LONGITUDE" ,  0.0)
                                putExtra("NEW_CITY" ,  "city")
                                putExtra("NEW_PINCODE" ,  "000000")
                            }
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        }
                        else if(keyAssigned!!.equals("ADD_EDIT_USER_ADDRESS" , ignoreCase = true))
                        {
                            Log.d("MainActivity1", addressCalculated)
                            val resultIntent = Intent().apply {
                                putExtra("EXTRA_KEY", keyAssigned)
                                putExtra("PLACE_NEW", place)  // Ensure `place` implements Parcelable
                                putExtra("NEW_ADDRESS", addressCalculated)
                                putExtra("NEW_LATITUDE" , 0.0)
                                putExtra("NEW_LONGITUDE" ,  0.0)
                                putExtra("NEW_CITY" ,  "city")
                                putExtra("NEW_STATE" ,  stateNew)
                                putExtra("NEW_PINCODE" ,  "000000")
                                putExtra("NEW_AREA" ,  area)
                                putExtra("NEW_BUILDING" ,  building)
                            }
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        }
                        else if(keyAssigned!!.equals("refer_address" , ignoreCase = true))
                        {
                            Log.d("MainActivity1", addressCalculated)
                            val resultIntent = Intent().apply {
                                putExtra("EXTRA_KEY", keyAssigned)
                                putExtra("PLACE_NEW", place)  // Ensure `place` implements Parcelable
                                putExtra("NEW_ADDRESS", addressCalculated)
                                putExtra("NEW_LATITUDE" , 0.0)
                                putExtra("NEW_LONGITUDE" ,  0.0)
                                putExtra("NEW_CITY" ,  "city")
                                putExtra("NEW_PINCODE" ,  "000000")
                            }
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        }
                        else if(keyAssigned!!.equals("rate_a_toilet" , ignoreCase = true))
                        {
                            Log.d("MainActivity1", addressCalculated)
                            val resultIntent = Intent().apply {
                                putExtra("EXTRA_KEY", keyAssigned)
                                putExtra("PLACE_NEW", place)  // Ensure `place` implements Parcelable
                                putExtra("NEW_ADDRESS", addressCalculated)
                                putExtra("NEW_LATITUDE" , 0.0)
                                putExtra("NEW_LONGITUDE" ,  0.0)
                                putExtra("NEW_CITY" ,  "city")
                                putExtra("NEW_PINCODE" ,  "000000")
                            }
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        }
                        else if(keyAssigned!!.equals("profile_address" , ignoreCase = true))
                        {
                            Log.d("MainActivity1", addressCalculated)
                            val resultIntent = Intent().apply {
                                putExtra("EXTRA_KEY", keyAssigned)
                                putExtra("PLACE_NEW", place)  // Ensure `place` implements Parcelable
                                putExtra("NEW_ADDRESS", addressCalculated)
                                putExtra("NEW_LATITUDE" , 0.0)
                                putExtra("NEW_LONGITUDE" ,  0.0)
                                putExtra("NEW_CITY" ,  "city")
                                putExtra("NEW_PINCODE" ,  "000000")
                            }
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        }
                        else if(keyAssigned!!.equals("vtion_address" , ignoreCase = true))
                        {
                            Log.d("MainActivity1 2", "$addressCalculated $keyAssigned")
                            val resultIntent = Intent().apply {
                                putExtra("EXTRA_KEY", keyAssigned)
                                putExtra("PLACE_NEW", place)  // Ensure `place` implements Parcelable
                                putExtra("NEW_ADDRESS", addressCalculated)
                                putExtra("NEW_LATITUDE" , 0.0)
                                putExtra("NEW_LONGITUDE" ,  0.0)
                                putExtra("NEW_CITY" ,  "city")
                                putExtra("NEW_PINCODE" ,  "000000")
                            }
                            setResult(Activity.RESULT_OK, resultIntent)
                            Handler(Looper.getMainLooper()).postDelayed({
                            finish()
                            }, 200)
                        }
                        else if(keyAssigned!!.equals("select_gender_address" , ignoreCase = true))
                        {
                            Log.d("MainActivity1", addressCalculated)
                            val resultIntent = Intent().apply {
                                putExtra("EXTRA_KEY", keyAssigned)
                                putExtra("PLACE_NEW", place)  // Ensure `place` implements Parcelable
                                putExtra("NEW_ADDRESS", addressCalculated)
                                putExtra("NEW_LATITUDE" , 0.0)
                                putExtra("NEW_LONGITUDE" ,  0.0)
                                putExtra("NEW_CITY" ,  "city")
                                putExtra("NEW_PINCODE" ,  "000000")
                            }
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        }
                        else if(keyAssigned!!.equals("current" , ignoreCase = true))
                        {
                            val intent = Intent(this, EnrouteDirectionActivity::class.java)
                            intent.putExtra("EXTRA_KEY", keyAssigned)
                            intent.putExtra("PLACE_NEW" , place)
                            intent.putExtra("NEW_ADDRESS" , place.address.toString())
                            intent.putExtra("SOURCE_ADDRESS", sourceAddress)
                            intent.putExtra("DEST_ADDRESS", destAddress)
                            startActivity(intent)
                            finish()
                        }
                        else if(keyAssigned!!.equals("destination" , ignoreCase = true))
                        {
                            val intent = Intent(this, EnrouteDirectionActivity::class.java)
                            intent.putExtra("EXTRA_KEY", keyAssigned)
                            intent.putExtra("PLACE_NEW" , place)
                            intent.putExtra("NEW_ADDRESS" , place.address.toString())
                            intent.putExtra("SOURCE_ADDRESS", sourceAddress)
                            intent.putExtra("DEST_ADDRESS", destAddress)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            val intent = Intent(this, EnrouteDirectionActivity::class.java)
                            intent.putExtra("EXTRA_KEY", keyAssigned)
                            intent.putExtra("PLACE_NEW" , place)
                            intent.putExtra("NEW_ADDRESS" , place.address.toString())
                            intent.putExtra("SOURCE_ADDRESS", sourceAddress)
                            intent.putExtra("DEST_ADDRESS", destAddress)
                            startActivity(intent)
                            finish()
                        }


                        //                Toast.makeText(getContext(), place.address, Toast.LENGTH_SHORT).show();s
                    }
                }

            }


    }


    private val filterTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
//            if (!s.toString().equals("")) {
//                searchOptionsLayout.setVisibility(View.VISIBLE);
//            } else {
//                searchOptionsLayout.setVisibility(View.GONE);
//            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
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


}