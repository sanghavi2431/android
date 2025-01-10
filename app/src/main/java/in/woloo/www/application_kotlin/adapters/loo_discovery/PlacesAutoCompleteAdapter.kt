package `in`.woloo.www.application_kotlin.adapters.loo_discovery

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.text.style.CharacterStyle
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.application_kotlin.model.lists_models.PlaceAutocomplete
import java.util.Arrays
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class PlacesAutoCompleteAdapter(private val mContext: Context) :
    RecyclerView.Adapter<PlacesAutoCompleteAdapter.PredictionHolder>(), Filterable {
    private var mResultList: ArrayList<PlaceAutocomplete>? = ArrayList()
    private val STYLE_BOLD: CharacterStyle
    private val STYLE_NORMAL: CharacterStyle
    private val placesClient: PlacesClient
    private var clickListener: ClickListener? = null

    init {
        STYLE_BOLD = StyleSpan(Typeface.BOLD)
        STYLE_NORMAL = StyleSpan(Typeface.NORMAL)
        placesClient = Places.createClient(mContext)
    }

    fun setClickListener(clickListener: ClickListener?) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun click(place: Place?)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val results = FilterResults()
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    mResultList = getPredictions(constraint)
                    if (mResultList != null) {
                        // The API successfully returned results.
                        results.values = mResultList
                        results.count = mResultList!!.size
                    }
                }
                return results
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged()
                } else {
                    // The API did not return any results, invalidate the data set.
                    //notifyDataSetInvalidated();
                }
            }
        }
    }

    private fun getPredictions(constraint: CharSequence): ArrayList<PlaceAutocomplete> {
        val resultList = ArrayList<PlaceAutocomplete>()

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        val token = AutocompleteSessionToken.newInstance()

        //https://gist.github.com/graydon/11198540
        // Use the builder to create a FindAutocompletePredictionsRequest.
        val request =
            FindAutocompletePredictionsRequest.builder() // Call either setLocationBias() OR setLocationRestriction().
                //.setLocationBias(bounds)
                .setCountry("IN") //.setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(constraint.toString())
                .build()
        val autocompletePredictions = placesClient.findAutocompletePredictions(request)

        // This method should have been called off the main UI thread. Block and wait for at most
        // 60s for a result from the API.
        try {
            Tasks.await(autocompletePredictions, 60, TimeUnit.SECONDS)
        } catch (e: ExecutionException) {
            CommonUtils.printStackTrace(e)
        } catch (e: InterruptedException) {
            CommonUtils.printStackTrace(e)
        } catch (e: TimeoutException) {
            CommonUtils.printStackTrace(e)
        }
        return if (autocompletePredictions.isSuccessful) {
            val findAutocompletePredictionsResponse = autocompletePredictions.result
            if (findAutocompletePredictionsResponse != null) for (prediction in findAutocompletePredictionsResponse.autocompletePredictions) {
                Logger.i(TAG, prediction.placeId)
                resultList.add(
                    PlaceAutocomplete(
                        prediction.placeId,
                        prediction.getPrimaryText(STYLE_NORMAL).toString(),
                        prediction.getFullText(STYLE_BOLD).toString()
                    )
                )
            }
            resultList
        } else {
            resultList
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PredictionHolder {
        val layoutInflater =
            mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val convertView: View =
            layoutInflater.inflate(R.layout.place_recycler_item_layout, viewGroup, false)
        return PredictionHolder(convertView)
    }

    override fun onBindViewHolder(mPredictionHolder: PredictionHolder, position: Int) {
        if (!TextUtils.isEmpty(mResultList!![position].address.toString())) {
            mPredictionHolder.address.visibility = View.VISIBLE
            mPredictionHolder.address.text = mResultList!![position].address
        } else {
            mPredictionHolder.address.visibility = View.GONE
        }

        /*if(!TextUtils.isEmpty(mResultList.get(position).area.toString())){
            mPredictionHolder.area.setVisibility(View.VISIBLE);
            mPredictionHolder.area.setText(mResultList.get(position).area);
        }else{
            mPredictionHolder.area.setVisibility(View.GONE);
        }*/
    }

    override fun getItemCount(): Int {
        return mResultList!!.size
    }

    /**
     * Holder for Places Geo Data Autocomplete API results.
     */
    inner class PredictionHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val address: TextView
        private val area: TextView
        private val mRow: LinearLayout

        init {
            area = itemView.findViewById<TextView>(R.id.place_area)
            address = itemView.findViewById<TextView>(R.id.place_address)
            mRow = itemView.findViewById<LinearLayout>(R.id.place_item_view)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            try {
                val item = mResultList!![adapterPosition]
                if (v.id == R.id.place_item_view) {
                    val placeId = item.placeId.toString()
                    val placeFields = Arrays.asList(
                        Place.Field.ID,
                        Place.Field.NAME,
                        Place.Field.LAT_LNG,
                        Place.Field.ADDRESS
                    )
                    val request = FetchPlaceRequest.builder(placeId, placeFields).build()
                    placesClient.fetchPlace(request).addOnSuccessListener { response ->
                        val place = response.place
                        clickListener!!.click(place)
                    }
                        .addOnFailureListener { exception ->
                            if (exception is ApiException) {
                                Toast.makeText(mContext, exception.message + "", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                }
            } catch (ex: Exception) {
            }
        }
    }

    companion object {
        private const val TAG = "PlacesAutoAdapter"
    }
}
