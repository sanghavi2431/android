package `in`.woloo.www.v2.home.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.ActivitySearchWoloo2Binding
import `in`.woloo.www.application_kotlin.adapters.loo_discovery.PlacesAutoCompleteAdapter
import `in`.woloo.www.application_kotlin.adapters.loo_discovery.WolooSearchAdapter
import `in`.woloo.www.utils.EndlessRecyclerOnScrollListener
import `in`.woloo.www.more.base_old.BaseActivity
import `in`.woloo.www.application_kotlin.model.lists_models.NearbyWoloo
import `in`.woloo.www.application_kotlin.view_models.HomeViewModel

class SearchWolooActivity : BaseActivity(), PlacesAutoCompleteAdapter.ClickListener {

    private lateinit var binding: ActivitySearchWoloo2Binding
    private lateinit var homeViewModel: HomeViewModel

    private var mAutoCompleteAdapter: PlacesAutoCompleteAdapter? = null
    private val nearByStoreResponseList: ArrayList<NearbyWoloo> = ArrayList()
    private var adapter: WolooSearchAdapter? = null
    private var endEndlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchWoloo2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        homeViewModel =  ViewModelProvider(this)[HomeViewModel::class.java]
        setProgressBar()
        setNetworkDetector()
        init()
        setLiveData()
        addTextWatcher()
    }

    private fun init() {
        try {
            val key = CommonUtils.googlemapapikey(this)
            Places.initialize(this, key)
            mAutoCompleteAdapter = PlacesAutoCompleteAdapter(this)
            binding.rvGoogleNearbyPlaces.layoutManager = LinearLayoutManager(this)
            mAutoCompleteAdapter?.setClickListener(this)
            binding.rvGoogleNearbyPlaces.adapter = mAutoCompleteAdapter
            //mAutoCompleteAdapter?.notifyDataSetChanged()

//            adapter = WolooSearchAdapter(this, nearByStoreResponseList, lastKnownLocation)
//            binding.rvSearchResults.setHasFixedSize(true)
//            val linearLayoutManager = LinearLayoutManager(this)
//            binding.rvSearchResults.layoutManager = linearLayoutManager
//            endEndlessRecyclerOnScrollListener =
//                object : EndlessRecyclerOnScrollListener(linearLayoutManager) {
//                    override fun onLoadMore(current_page: Int) {
//
//                    }
//                }
//            binding.rvSearchResults.adapter = adapter
        } catch (ex: Exception) {
             CommonUtils.printStackTrace(ex);
        }
    }

    private fun addTextWatcher() {
        binding.searchBar.etSearchText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString() != "") {
                    mAutoCompleteAdapter?.filter?.filter(s.toString())
                    binding.llNodatafound.visibility = View.GONE
                    if (binding.rvGoogleNearbyPlaces.visibility == View.GONE) {
                        binding.rvGoogleNearbyPlaces.visibility = View.VISIBLE
                    }
                } else {
                    if (binding.rvGoogleNearbyPlaces.visibility == View.VISIBLE) {
                        binding.rvGoogleNearbyPlaces.visibility = View.GONE
                        nearByStoreResponseList.clear()
                        adapter?.notifyDataSetChanged()
                    }
                }
            }

        })
    }

    private fun setLiveData() {

    }

    override fun click(place: Place?) {

    }

}