package `in`.woloo.www.application_kotlin.adapters.loo_discovery

import android.content.Context
import android.graphics.Typeface
import android.icu.util.TimeUnit
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.places.AutocompletePrediction
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest

import com.google.android.libraries.places.api.net.PlacesClient
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.model.lists_models.PlaceAutocomplete
import `in`.woloo.www.common.CommonUtils
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeoutException

class SearchPlacesAdapterForRecycler (private val context: Context, private val mPlacesClient: PlacesClient) :
    RecyclerView.Adapter<SearchPlacesAdapterForRecycler.ViewHolder>(), Filterable {

    private var resultList = ArrayList<PlaceAutocomplete>()


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextView = itemView.findViewById(R.id.place_address)

        init {
            itemView.setOnClickListener {
                val place = resultList[adapterPosition]
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_search_autocomplete, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.description.text = resultList[position].address
    }

    override fun getItemCount(): Int = resultList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    resultList.clear()
                    val address = getAutocomplete(mPlacesClient, constraint.toString())
                    address?.let {
                        for (i in address.indices) {
                            val item = address[i]
                            resultList.add(
                                PlaceAutocomplete(
                                    item.placeId,
                                    item.getPrimaryText(StyleSpan(Typeface.BOLD)).toString(),
                                    item.getFullText(StyleSpan(Typeface.BOLD)).toString())
                            )


                        }
                    }
                    filterResults.values = resultList
                    filterResults.count = resultList.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results?.count ?: 0 > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetChanged()
                }
            }
        }
    }

    val TASK_AWAIT = 120L

    fun getAutocomplete(mPlacesClient: PlacesClient, constraint: CharSequence): List<com.google.android.libraries.places.api.model.AutocompletePrediction> {
        var list = listOf<com.google.android.libraries.places.api.model.AutocompletePrediction>()
        val token = AutocompleteSessionToken.newInstance()
        val request = FindAutocompletePredictionsRequest.builder()
//            .setTypeFilter(TypeFilter.CITIES) //.setTypeFilter(TypeFilter.ADDRESS)
            //.setLocationBias(bounds)
            .setCountry("IN")
            .setSessionToken(token)
            .setQuery(constraint.toString())
            .build()
        val prediction = mPlacesClient.findAutocompletePredictions(request)
        try {
            Tasks.await(prediction, TASK_AWAIT, java.util.concurrent.TimeUnit.SECONDS)
        } catch (e: ExecutionException) {
            CommonUtils.printStackTrace(e)
        } catch (e: InterruptedException) {
            CommonUtils.printStackTrace(e)
        } catch (e: TimeoutException) {
            CommonUtils.printStackTrace(e)
        }

        if (prediction.isSuccessful) {
            val findAutocompletePredictionsResponse = prediction.result
            findAutocompletePredictionsResponse?.let {
                list = findAutocompletePredictionsResponse.autocompletePredictions.toList()
            }
            return list
        }
        return list
    }

}
