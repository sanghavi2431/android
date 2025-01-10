package `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery

import android.content.Intent
import android.os.Bundle
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
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.adapters.loo_discovery.SearchPlacesAdapter
import `in`.woloo.www.application_kotlin.model.lists_models.PlaceAutocomplete
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility
import java.util.Arrays


class SerachLayoutForOnRouteFragment : AppCompatActivity() {

    private lateinit var rootView: View

    @BindView(R.id.search_auto_complete)
    lateinit var searchAutoComplete: AutoCompleteTextView

    @BindView(R.id.suggestionRecyclerView)
    lateinit var recyclerViewPlaces: RecyclerView

    lateinit var customeIntent :Intent

    var keyAssigned: String? = null
    var sourceAddress: String? = null
    var destAddress: String? = null


    var placeAdapter: SearchPlacesAdapter? = null
    lateinit var mPlacesClient: PlacesClient



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
                searchAutoComplete!!.setText(place.address)
                searchAutoComplete!!.setSelection(searchAutoComplete!!.length())

            }

        searchAutoComplete!!.addTextChangedListener(filterTextWatcher)
        searchAutoComplete!!.onItemClickListener =
            OnItemClickListener { adapterView, view, pos, id ->
                val place = adapterView.getItemAtPosition(pos) as PlaceAutocomplete
                searchAutoComplete!!.setText(place.address)
                searchAutoComplete!!.setSelection(searchAutoComplete!!.length())

            }

        searchAutoComplete?.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, pos, id ->
                val place = adapterView.getItemAtPosition(pos) as PlaceAutocomplete
                searchAutoComplete?.setText(place.address)
                searchAutoComplete?.setSelection(searchAutoComplete?.length()!!)
                Log.d("Address in search is 1" , place.address.toString())
                Logger.d("New Address is 1" ,keyAssigned.toString())



                    val intent = Intent(this, EnrouteDirectionActivity::class.java)
                    intent.putExtra("EXTRA_KEY", keyAssigned)
                    intent.putExtra("PLACE_NEW" , place)
                    intent.putExtra("NEW_ADDRESS" , place.address.toString())
                    intent.putExtra("SOURCE_ADDRESS", sourceAddress)
                   intent.putExtra("DEST_ADDRESS", destAddress)
                    startActivity(intent)
                finish()
                //                Toast.makeText(getContext(), place.address, Toast.LENGTH_SHORT).show();
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



}