package `in`.woloo.www.application_kotlin.presentation.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPICallModel
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.application_kotlin.view_models.BaseViewModel
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.SearchWolooActivity
import `in`.woloo.www.application_kotlin.model.server_response.SearchWolooResponse.Data.Woloo
import `in`.woloo.www.application_kotlin.model.server_response.SearchWolooResponse
import `in`.woloo.www.application_kotlin.adapters.loo_discovery.PlacesAutoCompleteAdapter
import `in`.woloo.www.application_kotlin.adapters.loo_discovery.WolooSearchAdapter
import `in`.woloo.www.search.mvp.WolooSearchPresenter
import `in`.woloo.www.application_kotlin.interfaces.WolooSearchView
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.EndlessRecyclerOnScrollListener
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility
import `in`.woloo.www.more.base_old.BaseFragment
import `in`.woloo.www.application_kotlin.model.server_request.NearbyWolooRequest
import `in`.woloo.www.application_kotlin.view_models.HomeViewModel


class WolooSearchFragment : BaseFragment(),
    WolooSearchView,
    PlacesAutoCompleteAdapter.ClickListener {
    @JvmField
    @BindView(R.id.rvSearchResults)
    var rvSearchResults: RecyclerView? = null

    @JvmField
    @BindView(R.id.etSearchText)
    var etSearchText: EditText? = null

    @JvmField
    @BindView(R.id.ll_nodatafound)
    var ll_nodatafound: LinearLayout? = null

    @JvmField
    @BindView(R.id.rvGoogleNearbyPlaces)
    var rvGoogleNearbyPlaces: RecyclerView? = null

    @JvmField
    @BindView(R.id.search_no_found_revard_Tv_layout)
    var search_no_found_revard_Tv_layout: LinearLayout? = null

    @JvmField
    @BindView(R.id.woloo_points_Tv)
    var woloo_points: TextView? = null

    @JvmField
    @BindView(R.id.shop_Tv)
    var shop_Tv: TextView? = null

    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: ImageView? = null

    @JvmField
    @BindView(R.id.cbWolooWithOffers)
    var cbWolooWithOffers: CheckBox? = null

    @JvmField
    @BindView(R.id.cbOpenNow)
    var cbOpenNow: CheckBox? = null

    @JvmField
    @BindView(R.id.list_of_woloos_tv)
    var tvListOfWoloos: TextView? = null
    private var isShowOffers = false
    private var mWolooSearchPresenter: WolooSearchPresenter? = null
    private var homeViewModel: HomeViewModel? = null
    private var locationPermissionGranted = false
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var lastKnownLocation: Location? = null
    private var lastKnownLattitude = 0.0
    private var lastKnownLongitude = 0.0
    private val searchWolooList: MutableList<Woloo> = ArrayList<Woloo>()
    private var adapter: WolooSearchAdapter? = null
    private var mPageNumber = 0
    private var mNextPage = 0
    private var endEndlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener? = null
    private var mAutoCompleteAdapter: PlacesAutoCompleteAdapter? = null
    private val nearByStoreResponseList: MutableList<NearByStoreResponse.Data> = ArrayList()
    private var imm: InputMethodManager? = null
    override fun onCreateViewModel(): BaseViewModel? {
        return ViewModelProvider(this).get<HomeViewModel>(HomeViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            isShowOffers = requireArguments().getBoolean(ARG_SHOW_OFFERS, false)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.fragment_woloo_search, container, false)
        ButterKnife.bind(this, root)
        initViews()
        return root
    }

    private fun initViews() {
        try {
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())
            mWolooSearchPresenter = WolooSearchPresenter(requireContext(), this@WolooSearchFragment)
            homeViewModel = ViewModelProvider(this).get<HomeViewModel>(
                HomeViewModel::class.java
            )
            deviceLocation
            setProgressBar()
            setNetworkDetector()
            setSearchResults()
            setLiveData()
            shop_Tv!!.setOnClickListener { v: View? ->
                startActivity(
                    Intent(activity, WolooDashboard::class.java).putExtra("goToShop", "")
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                )
                requireActivity().finish()
            }
            ivBack!!.setOnClickListener { v: View? ->
                Utility.hideKeyboard(requireActivity())
                (activity as SearchWolooActivity?)!!.onBackPressed()
            }
            etSearchText!!.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
                if (etSearchText!!.hasFocus()) {

                    //et1.setCursorVisible(true);
                    etSearchText!!.isActivated = true
                    etSearchText!!.isPressed = true
                }
            }
            //etSearchText.requestFocus();
            etSearchText!!.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? -> false }
            cbWolooWithOffers!!.setOnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
                if (lastKnownLocation == null || TextUtils.isEmpty(
                        etSearchText!!.text.toString()
                    )
                ) {
                    return@setOnCheckedChangeListener
                }
                getNearByWoloos(
                    lastKnownLattitude,
                    lastKnownLongitude,
                    SharedPreference(requireActivity()).getStoredPreference(
                        context, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0"
                    )!!.toInt(),
                    2,
                    1,
                    cbWolooWithOffers!!.isChecked,
                    cbOpenNow!!.isChecked
                )
            }
            cbOpenNow!!.setOnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
                if (lastKnownLocation == null || TextUtils.isEmpty(
                        etSearchText!!.text.toString()
                    )
                ) {
                    return@setOnCheckedChangeListener
                }
                getNearByWoloos(
                    lastKnownLattitude,
                    lastKnownLongitude,
                    SharedPreference(requireActivity()).getStoredPreference(
                        context, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0"
                    )!!.toInt(),
                    2,
                    1,
                    cbWolooWithOffers!!.isChecked,
                    cbOpenNow!!.isChecked
                )
            }

            //Utility.showKeyboard(getActivity());
            if (!isShowOffers) {
                showKeyboard()
            } else {
                hideKeyboard()
            }
            try {
                val key = CommonUtils.googlemapapikey(context)
                Places.initialize(requireContext(), key)
                //                Places.initialize(getContext(), getResources().getString(R.string.google_maps_key));
                etSearchText!!.addTextChangedListener(filterTextWatcher)
                mAutoCompleteAdapter = PlacesAutoCompleteAdapter(requireContext())
                rvGoogleNearbyPlaces!!.layoutManager = LinearLayoutManager(
                    context
                )
                mAutoCompleteAdapter!!.setClickListener(this)
                rvGoogleNearbyPlaces!!.adapter = mAutoCompleteAdapter
                mAutoCompleteAdapter?.notifyDataSetChanged()
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun setLiveData() {
        homeViewModel?.observeNearByWoloo()
            ?.observe(viewLifecycleOwner, Observer<BaseResponse<ArrayList<NearByStoreResponse.Data>>> { arrayListBaseResponse ->
                if (arrayListBaseResponse != null) {
                    arrayListBaseResponse.data?.let { renderNearByWolooList(it, "") }
                }
            })
    }

    private fun getNearByWoloos(
        lat: Double,
        lng: Double,
        mode: Int,
        range: Int,
        isSearch: Int,
        isOffer: Boolean,
        openNow: Boolean
    ) {
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
        homeViewModel?.getNearbyWoloos(request)
    }

    private fun showKeyboard() {
        try {
            etSearchText!!.requestFocus()
            imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun hideKeyboard() {
        try {
            imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private val filterTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (s.toString() != "") {
                mAutoCompleteAdapter?.getFilter()?.filter(s.toString())
                ll_nodatafound!!.visibility = View.GONE
                if (rvGoogleNearbyPlaces!!.visibility == View.GONE) {
                    rvGoogleNearbyPlaces!!.visibility = View.VISIBLE
                }
            } else {
                if (rvGoogleNearbyPlaces!!.visibility == View.VISIBLE) {
                    rvGoogleNearbyPlaces!!.visibility = View.GONE
                    nearByStoreResponseList.clear()
                    adapter?.notifyDataSetChanged()
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }

    private fun setSearchResults() {
        try {
            adapter = WolooSearchAdapter(requireContext(), nearByStoreResponseList, lastKnownLocation)
            rvSearchResults!!.setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(context)
            rvSearchResults!!.layoutManager = linearLayoutManager
            endEndlessRecyclerOnScrollListener =
                object : EndlessRecyclerOnScrollListener(linearLayoutManager) {
                    override fun onLoadMore(current_page: Int) {
                        mPageNumber = mNextPage
                        if (mNextPage != 0) {
                            //loadMore();
                        }
                    }
                }
            //rvSearchResults.addOnScrollListener(endEndlessRecyclerOnScrollListener);
            rvSearchResults!!.adapter = adapter
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun loadMore() {
        try {
            mWolooSearchPresenter?.wolooSearchAPI(
                lastKnownLocation!!.latitude.toString(),
                lastKnownLocation!!.longitude.toString(),
                etSearchText!!.text.toString(),
                mPageNumber
            )
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private val deviceLocation: Unit
        private get() {
            /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
            try {
                locationPermissionGranted = isLocationPermissionGranted()
                if (locationPermissionGranted) {
                    val locationResult = fusedLocationProviderClient!!.lastLocation
                    locationResult.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.result
                            isShowOffers = requireArguments().getBoolean(ARG_SHOW_OFFERS, false)
                            if (isShowOffers) {
                                getNearByWoloos(
                                    lastKnownLocation!!.latitude,
                                    lastKnownLocation!!.longitude,
                                    SharedPreference(requireActivity()).getStoredPreference(
                                        context,
                                        SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(),
                                        "0"
                                    )!!.toInt(),
                                    2,
                                    1,
                                    cbWolooWithOffers!!.isChecked,
                                    cbOpenNow!!.isChecked
                                )
                                //mWolooSearchPresenter.getNearByStore(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), "OFFER", true, cbWolooWithOffers.isChecked());
                            }
                        }
                    }
                }
            } catch (e: SecurityException) {
                Logger.e("Exception: %s", e.message, e)
            }
        }

    fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
    }

    override fun searchWolooSuccess(searchWolooResponse: SearchWolooResponse, keywords: String) {
        try {
            if (searchWolooResponse != null && !TextUtils.isEmpty(searchWolooResponse.status) && searchWolooResponse.status == AppConstants.API_SUCCESS) {
                if (searchWolooResponse.data?.woloos?.size!! > 0) {
//                    if (mPageNumber == 1) {
//                    }
                    searchWolooList.clear()
                    searchWolooList.addAll(searchWolooResponse.data!!.woloos!!)
                    adapter?.notifyDataSetChanged()
                    ll_nodatafound!!.visibility = View.GONE
                    rvSearchResults!!.visibility = View.VISIBLE
                    if (searchWolooResponse.data!!.next != null && searchWolooResponse.data!!.next!! > 1) {
                        mNextPage = searchWolooResponse.data!!.next!!
                    }
                } else {
                    Utility.hideKeyboard(requireActivity())
                    searchWolooList.clear()
                    adapter?.notifyDataSetChanged()
                    //                   Toast.makeText(getActivity().getApplicationContext(),getResources().getString(R.string.search_error),Toast.LENGTH_SHORT).show();
                    ll_nodatafound!!.visibility = View.VISIBLE
                    rvSearchResults!!.visibility = View.GONE
                }
            } else {
//               Toast.makeText(getActivity().getApplicationContext(),searchWolooResponse.getMessage(),Toast.LENGTH_SHORT).show();
                Utility.hideKeyboard(requireActivity())
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    override fun onGetNearByStore(
        data: NearByStoreResponse,
        networkAPICallModel: NetworkAPICallModel,
        keywords: String
    ) {
        try {
            Utility.hideKeyboard(requireActivity())
            if (data.data?.size!! > 0) {
                nearByStoreResponseList.clear()
                rvGoogleNearbyPlaces!!.visibility = View.GONE
                ll_nodatafound!!.visibility = View.GONE
                nearByStoreResponseList.addAll(data.data!!)
                adapter?.lastKnownLocation = lastKnownLocation
                adapter?.keyword = keywords
                adapter?.notifyDataSetChanged()
            } else {
                if (data.message!!.contains("Woloo Points")) {
                    search_no_found_revard_Tv_layout!!.visibility = View.VISIBLE
                    woloo_points!!.setText(
                        data.message!!.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray().get(0))
                } else {
                    search_no_found_revard_Tv_layout!!.visibility = View.GONE
                }
                rvGoogleNearbyPlaces!!.visibility = View.GONE
                Utility.hideKeyboard(requireActivity())
                ll_nodatafound!!.visibility = View.VISIBLE
                nearByStoreResponseList.clear()
                adapter?.notifyDataSetChanged()
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun renderNearByWolooList(data: ArrayList<NearByStoreResponse.Data>, keywords: String) {
        try {
            Utility.hideKeyboard(requireActivity())
            if (data.size > 0) {
                nearByStoreResponseList.clear()
                rvGoogleNearbyPlaces!!.visibility = View.GONE
                ll_nodatafound!!.visibility = View.GONE
                nearByStoreResponseList.addAll(data)
                adapter?.lastKnownLocation = lastKnownLocation
                adapter?.keyword = keywords
                adapter?.notifyDataSetChanged()
                tvListOfWoloos!!.visibility = View.VISIBLE
            } else {
//                if (data.getMessage().contains("Woloo Points")) {
//                    search_no_found_revard_Tv_layout.setVisibility(View.VISIBLE);
//                    woloo_points.setText(data.getMessage().split(" ")[0]);
//                } else {
//                    search_no_found_revard_Tv_layout.setVisibility(View.GONE);
//                }
                rvGoogleNearbyPlaces!!.visibility = View.GONE
                Utility.hideKeyboard(requireActivity())
                ll_nodatafound!!.visibility = View.VISIBLE
                nearByStoreResponseList.clear()
                adapter?.notifyDataSetChanged()
                tvListOfWoloos!!.visibility = View.GONE
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }



    companion object {
        const val ARG_SHOW_OFFERS = "ARG_SHOW_OFFERS"
        fun newInstance(isShowOffers: Boolean): WolooSearchFragment {
            val fragment = WolooSearchFragment()
            val args = Bundle()
            args.putBoolean(ARG_SHOW_OFFERS, isShowOffers)
            fragment.arguments = args
            return fragment
        }

        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    override fun click(place: Place?) {
        try {
            //Toast.makeText(getActivity().getApplicationContext(), place.getAddress()+", "+place.getLatLng().latitude+place.getLatLng().longitude, Toast.LENGTH_SHORT).show();
            try {
                Utility.hideKeyboard(requireActivity())
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
            lastKnownLattitude = place?.latLng!!.latitude
            lastKnownLongitude = place?.latLng!!.longitude
            getNearByWoloos(
                lastKnownLattitude,
                lastKnownLongitude,
                SharedPreference(requireActivity()).getStoredPreference(
                    context, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0"
                )!!.toInt(),
                2,
                1,
                cbWolooWithOffers!!.isChecked,
                cbOpenNow!!.isChecked
            )
            //mWolooSearchPresenter.getNearByStore(lastKnownLattitude, lastKnownLongitude, etSearchText.getText().toString(), false, cbWolooWithOffers.isChecked());
            etSearchText!!.setText(place.address)
            etSearchText!!.setSelection(etSearchText!!.text.length)
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }
}