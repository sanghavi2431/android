package `in`.woloo.www.application_kotlin.presentation.fragments.loo_discovery

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.Marker
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.adapters.loo_discovery.NearestWalkAdapter
import `in`.woloo.www.application_kotlin.adapters.loo_discovery.NearestWalkAdapterEnroute
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.model.CreditCoinsRequest
import `in`.woloo.www.application_kotlin.model.server_request.NearbyWolooRequest
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.presentation.HomeFragment
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.EnrouteDirectionActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.SearchActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.SharedViewModelStringBookMark
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard.Companion.mSharedPreference
import `in`.woloo.www.application_kotlin.utilities.MarginItemDecoration
import `in`.woloo.www.application_kotlin.view_models.HomeViewModel
import `in`.woloo.www.application_kotlin.view_models.ScrollEnrouteListViewModel
import `in`.woloo.www.application_kotlin.view_models.ScrollListViewModel
import `in`.woloo.www.application_kotlin.view_models.SharedViewModel
import `in`.woloo.www.application_kotlin.view_models.WolooViewModel
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.CustomProgressView
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Logger.i
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent
import org.json.JSONObject


class HomeCategoryFragment : Fragment(){
    @JvmField
    @BindView(R.id.rvHomeCategory)
    var rvHomeCategory: RecyclerView? = null

    @JvmField
    @BindView(R.id.tv_search_more)
    var tv_search_more: LinearLayout? = null


    @JvmField
    @BindView(R.id.tvNoWolooFound)
    var tvNoWolooFound: LinearLayout? = null

    @JvmField
    @BindView(R.id.show_more_layout)
    var showMoreLayout: LinearLayout? = null

    @JvmField
    @BindView(R.id.tv_search)
    var tv_search: TextView? = null

   /* @JvmField
    @BindView(R.id.pullToRefreshLayout)
    var pullToRefreshLayout: SwipeRefreshLayout? = null*/

  /*  @JvmField
    @BindView(R.id.bottomMargin)
    var bottomMargin: TextView? = null*/

    @JvmField
    @BindView(R.id.tv2Km)
    var tv2Km: TextView? = null

    @JvmField
    @BindView(R.id.tv5Km)
    var tv5Km: TextView? = null


    @JvmField
    @BindView(R.id.tv4Km)
    var tv4Km: TextView? = null

    @JvmField
    @BindView(R.id.tv8Km)
    var tv8km: TextView? = null

    @JvmField
    @BindView(R.id.tv6Km)
    var tv6Km: TextView? = null

    @JvmField
    @BindView(R.id.tv10Km)
    var tv10km: TextView? = null

    @JvmField
    @BindView(R.id.tv25Km)
    var tv25Km: TextView? = null

    @JvmField
    @BindView(R.id.tv_no_woloo_title)
    var noWolooTitle: TextView? = null

    @JvmField
    @BindView(R.id.dotLayout)
    var dotsLayout: LinearLayout? = null



    var range : Int = 2

    var dotViews = java.util.ArrayList<View>()

    @JvmField
    @BindView(R.id.tv_no_woloo_search_text)
    var noWolooSearchText: TextView? = null

    @JvmField
    @BindView(R.id.process_bar_view)
    var process_bar_view: RelativeLayout? = null

    @JvmField
    var pageNumber = 1
    var stopLoading = false
    private var nearByStoreResponseList: ArrayList<NearByStoreResponse.Data> = ArrayList()
    private var adapter: NearestWalkAdapter? = null
    private var adapter1: NearestWalkAdapterEnroute? = null
    lateinit var homeViewModel: HomeViewModel
     lateinit var scrollViewModel: ScrollListViewModel
    lateinit var scrollEnrouteViewModel: ScrollEnrouteListViewModel
    private lateinit var sharedViewModel: SharedViewModel
    var travelMode : Int = 0
    private lateinit var progressView: CustomProgressView
    private var wolooViewModel: WolooViewModel? = null

    private  var rangeSearching = 2;

    private  var isSearchDialog = false;

    private var searchLocation = "";

    private val sharedViewModelSearch: SharedSearchViewModel by activityViewModels()
    private val sharedViewModelShowMore: SharedViewModelStringShowMore by viewModels({ requireParentFragment() })

    private val sharedViewModelBookMark: SharedViewModelStringBookMark by viewModels({ requireParentFragment() })


    /*calling onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.i(TAG, "onCreate")
        wolooViewModel = ViewModelProvider(this).get<WolooViewModel>(
            WolooViewModel::class.java
        )
        val dialog = childFragmentManager.findFragmentByTag("ShowMoreFragment") as? DialogFragment
        dialog?.dismissAllowingStateLoss()
    }



    /*calling onCreateView*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.fragment_home_category, container, false)
        ButterKnife.bind(this, root)
        progressView = CustomProgressView(requireActivity())
        progressView.show()
        Logger.i(TAG, "onCreateView")
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(rvHomeCategory)

        if (activity is WolooDashboard) {
            scrollViewModel = (activity as WolooDashboard).getScrollViewModel()
        }
        else if (activity is EnrouteDirectionActivity){
            scrollEnrouteViewModel = (activity as EnrouteDirectionActivity).getScrollEnrouteViewModel()
        }
        wolooViewModel?.observeAddCoinstoWolooUser()
            ?.observe(viewLifecycleOwner, Observer<BaseResponse<JSONObject>> { response ->
                CommonUtils().hideProgress()
                mSharedPreference!!.setStoredBooleanPreference(
                    requireContext(),
                    SharedPreferencesEnum.COINS_MODE.preferenceKey, true)

               // showSuccessDialogNoWoloos()
              //  showSuccessDialog()
            })



        initViews()




       /* val bottomSheet = ShowMoreFragment()
        val existingDialog = parentFragmentManager.findFragmentByTag(bottomSheet.tag)
        if (existingDialog is ShowMoreFragment) {
            existingDialog.dismissAllowingStateLoss()
        }*/



        var modeoftravalis = SharedPrefSettings.getPreferences.fetchTransportMode()
        if(modeoftravalis == null)
        {
            travelMode = 0
        }
        else if (modeoftravalis.equals("car"))
        {
            travelMode = 0
        }
        else if(modeoftravalis.equals("bike"))
        {
            travelMode = 3
        }
        else if(modeoftravalis.equals("walk"))
        {
            travelMode = 1
        }
      //  travelMode = SharedPreference(requireActivity()).getStoredPreference(requireActivity(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")!!.toInt()

       val mode = mSharedPreference!!.getStoredPreference(
            getContext(),
            SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey()
        )



        rvHomeCategory!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
              //  dotLayoutCreator(listSize, firstVisiblePosition)
              //  updateDotColor(firstVisiblePosition)
                if(activity is WolooDashboard) {
                    scrollViewModel.isProgrammaticScroll.observe(viewLifecycleOwner) { isProgrammatic ->
                        if (isProgrammatic == true) {
                            scrollViewModel.setProgrammaticScroll(false) // Reset after ignoring
                            return@observe
                        }
                    }

                    val visiblePosition = layoutManager!!.findFirstCompletelyVisibleItemPosition()
                    if (visiblePosition != RecyclerView.NO_POSITION)
                    {
                        scrollViewModel.setScrollIndexForMarker(visiblePosition) // Notify HomeFragment
                        Log.d("scroll on pos ", visiblePosition.toString())
                    }
                }
                else if(activity is EnrouteDirectionActivity) {
                    scrollEnrouteViewModel.isProgrammaticScroll.observe(viewLifecycleOwner) { isProgrammatic ->
                        if (isProgrammatic == true) {
                            scrollEnrouteViewModel.setProgrammaticScroll(false) // Reset after ignoring
                            return@observe
                        }
                    }
                    val visiblePosition = layoutManager!!.findFirstCompletelyVisibleItemPosition()
                    if (visiblePosition != RecyclerView.NO_POSITION) {
                        scrollEnrouteViewModel.setScrollIndexForMarker(visiblePosition) // Notify HomeFragment
                        Log.d("scroll on pos ", visiblePosition.toString())
                    }
                }



            }

            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)


                // Check if the last item is fully visible
            }


        })

        sharedViewModel.distanceRange.observe(viewLifecycleOwner, Observer { distance ->
           distanceRangeChanged(distance)

        })

        sharedViewModel.showMoreEvent.observe(viewLifecycleOwner, Observer { show ->
            if (show) {
                val bottomSheet = ShowMoreFragment()
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)

                sharedViewModel.clearShowMoreEvent()

            }
        })


        if(activity is WolooDashboard) {
            scrollViewModel.scrollToIndex.observe(viewLifecycleOwner, Observer { index ->
                Log.d("Aarati List 1", index.toString())
                rvHomeCategory!!.post {
                    /* rvHomeCategory?.smoothScrollToPosition(index)
                rvHomeCategory?.scrollToPosition(index)*/
                    if (index in 0 until (rvHomeCategory!!.adapter?.itemCount ?: 0)) {
                        Log.d("Aarati List", "Valid index. Scrolling to: $index")
                        rvHomeCategory!!.smoothScrollToPosition(index)
                    } else {
                        Log.d("Aarati List", "Invalid index: $index")
                    }

                }
            })
        }
        if(activity is EnrouteDirectionActivity) {
            scrollEnrouteViewModel.scrollToIndex.observe(viewLifecycleOwner, Observer { index ->
                Log.d("Aarati List 1", index.toString())
                rvHomeCategory!!.post {
                    /* rvHomeCategory?.smoothScrollToPosition(index)
                rvHomeCategory?.scrollToPosition(index)*/
                    if (index in 0 until (rvHomeCategory!!.adapter?.itemCount ?: 0)) {
                        Log.d("Aarati List", "Valid index. Scrolling to: $index")
                        rvHomeCategory!!.smoothScrollToPosition(index)
                    } else {
                        Log.d("Aarati List", "Invalid index: $index")
                    }

                }
            })
        }

        tv2Km!!.setOnClickListener {
            nearByStoreResponseList.clear()
            rangeSearching = 2
            distanceRangeChanged(2)


        }

        tv4Km!!.setOnClickListener {
            nearByStoreResponseList.clear()
            rangeSearching = 4
            distanceRangeChanged(4)


        }


        tv5Km!!.setOnClickListener {
            nearByStoreResponseList.clear()
            rangeSearching = 5
            distanceRangeChanged(5)


        }

        tv6Km!!.setOnClickListener {
            nearByStoreResponseList.clear()
            rangeSearching = 6
            distanceRangeChanged(6)


        }

        tv8km!!.setOnClickListener {
            nearByStoreResponseList.clear()
            distanceRangeChanged(8)


        }

        tv10km!!.setOnClickListener {
            nearByStoreResponseList.clear()
            distanceRangeChanged(8)


        }

        tv25Km!!.setOnClickListener {
            SharedPrefSettings.getPreferences.storeIs25KM("YES")
            nearByStoreResponseList.clear()
            distanceRangeChanged(25)

        }



        return root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModelSearch.searchText.observe(viewLifecycleOwner) { text ->
            // Use this text while setting adapter
            searchLocation = text ?: ""
            Log.d("Search Location", searchLocation)

                adapter?.updateSearchLocation(searchLocation)

            sharedViewModelShowMore.setString(searchLocation);

            sharedViewModelBookMark.setString(searchLocation)
        }

    }


    /*calling initViews*/
    @SuppressLint("NotifyDataSetChanged")
    private fun initViews() {
        Logger.i(TAG, "initViews")
        try {
            if (parentFragment is HomeFragment) {
                //bottomMargin!!.visibility = View.VISIBLE
            } else {
               // bottomMargin!!.visibility = View.GONE
                tv_search!!.visibility = View.GONE
                tv_search_more!!.visibility = View.GONE

            }

            sharedViewModelSearch.searchText.observe(viewLifecycleOwner) { text ->
                // Use this text while setting adapter
                searchLocation = text ?: ""
                Log.d("Search Location init", searchLocation)


                adapter?.updateSearchLocation(searchLocation)


            }

                setHomeCategories(searchLocation)



            tv_search!!.setOnClickListener { v: View? ->
                val intent = Intent(context, SearchActivity::class.java)
                intent.putExtra("lat", (parentFragment as HomeFragment?)?.lastKnownLattitude)
                intent.putExtra("lng", (parentFragment as HomeFragment?)?.lastKnownLongitude)
                requireActivity().startActivity(intent)
            }
            tv_search_more!!.setOnClickListener { v: View? ->
               /* val intent = Intent(context, SearchActivity::class.java)
                intent.putExtra("lat", (parentFragment as HomeFragment?)?.lastKnownLattitude)
                intent.putExtra("lng", (parentFragment as HomeFragment?)?.lastKnownLongitude)
                requireActivity().startActivity(intent)*/
                rvHomeCategory!!.visibility = View.VISIBLE
                val bottomSheet = ShowMoreFragment()
                bottomSheet.show(parentFragmentManager, bottomSheet.tag)
            }

            progressView.hide()

        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling setHomeCategories*/
    @SuppressLint("NotifyDataSetChanged")
    private fun setHomeCategories(sloc : String) {
        Logger.i(TAG, "setHomeCategories")
        try {
            Log.d("Check type" , "size is $nearByStoreResponseList")
            showMoreLayout!!.visibility = View.GONE
            Log.d("Search Location Adapter", searchLocation)
            adapter = NearestWalkAdapter(requireContext(), nearByStoreResponseList , wolooViewModel , scrollViewModel , sloc)
            rvHomeCategory!!.isNestedScrollingEnabled = true
            rvHomeCategory!!.setHasFixedSize(false)
            rvHomeCategory!!.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false)
            rvHomeCategory!!.adapter = adapter
            rvHomeCategory!!.adapter!!.notifyDataSetChanged()

            rvHomeCategory!!.addItemDecoration(MarginItemDecoration())


            //            rvHomeCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//                    if (newState != RecyclerView.SCROLL_STATE_IDLE) {
//                        return;
//                    }
//                    if (!recyclerView.canScrollVertically(1)) {
//                        if (!stopLoading) {
//                            pageNumber++;
//                            ((HomeFragment) getParentFragment()).loadMore(String.valueOf(pageNumber),false);
//                        }
//                    }
//                }
//            });
           /* pullToRefreshLayout!!.setOnRefreshListener {
                pageNumber = 1
                //tvNoWolooFound.setVisibility(View.GONE);
                //rvHomeCategory.setVisibility(View.GONE);
//                if(getParentFragment() instanceof HomeFragment) {
//                    ((HomeFragment) getParentFragment()).loadMore(String.valueOf(pageNumber), true);
//                    ((HomeFragment) getParentFragment()).isFromClickFlag = false;
//                }
                pullToRefreshLayout!!.isRefreshing = false
            }*/
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling setNearestWalk*/
    fun setNearestWalk(
        nearByStoreResponseList: List<NearByStoreResponse.Data>,
        openNow: Boolean,
        bookmark: Boolean,
        isEnroute: Boolean
    ) {
        Logger.i("initViews Aarati check 1", "setNearestWalk")
        Logger.e("initViews aarati check 2", " $isEnroute")
        //if (pageNumber == 1)
        this.nearByStoreResponseList.clear()
        if (nearByStoreResponseList.size > 0) this.nearByStoreResponseList.addAll(
            nearByStoreResponseList
        )
        adapter?.notifyDataSetChanged()
        if (this.nearByStoreResponseList.size == 0) {
            if (openNow) {
                noWolooTitle!!.text = resources.getString(R.string.no_woloo_found_at_moment)
            } else if (bookmark) {
                noWolooTitle!!.text = resources.getString(R.string.no_woloo_found_bookmark)
            } else {
                noWolooTitle!!.text = resources.getString(R.string.no_woloo_found)
            }
            tvNoWolooFound!!.visibility = View.GONE
            if (requireActivity() is WolooDashboard && this.nearByStoreResponseList.size == 0) {
                showMoreLayout!!.visibility = View.VISIBLE
                i("Aarati Visibility", "show morelayout 3")
                if(rangeSearching == 2 && searchLocation.isNullOrEmpty()) {
                    tv4Km!!.performClick()
                }
                else if(rangeSearching == 4 && searchLocation.isNullOrEmpty())
                {
                    tv5Km!!.performClick()
                }
                else if(rangeSearching == 5 && searchLocation.isNullOrEmpty())
                {
                    tv6Km!!.performClick()
                }
                else if(rangeSearching == 6  && isSearchDialog == false)
                {
                    var count = 0
                    count = count + 1
                    i("Aarati Test Dialog list", count.toString() + "")
                   // showSearchLocationDialog()
                    var activity =(requireActivity() as WolooDashboard)
                    if (activity.canShowEmptyDialog()) {
                        activity.markEmptyDialogShown()
                        showSearchLocationDialog()
                    }
                    if (!mSharedPreference!!
                            .getStoredBooleanPreference(
                                requireContext(),
                                SharedPreferencesEnum.COINS_MODE.preferenceKey , true
                            )
                    ) {


                        val request = CreditCoinsRequest(
                            coins = CommonUtils.authconfig_response(requireContext())
                                .getNoWolooFound()!!.toInt(),
                            remarks = AppConstants.NO_WOLOO_FOUND_CLICK,
                            type = AppConstants.NO_WOLOO_FOUND_CLICK,
                            isGift = 0,
                            blogId = 0
                        )

                        wolooViewModel!!.addCoinstoWolooUser(request)
                    }

                    var bundle = Bundle()
                    var payload = HashMap<String, Any>()
                    bundle.putString(AppConstants.NO_WOLOO_FOUND_CLICK, "No Woloos Found")
                    bundle.putString(AppConstants.NO_WOLOO_FOUND_CLICK, "No Woloos Found")
                    payload[AppConstants.NO_WOLOO_FOUND_CLICK] = "No Woloos Found"
                    payload[AppConstants.NO_WOLOO_FOUND_CLICK] = "No Woloos Found"
                    logFirebaseEvent(requireActivity(), bundle, AppConstants.NO_WOLOO_FOUND_CLICK)
                    logNetcoreEvent( requireActivity(), payload, AppConstants.NO_WOLOO_FOUND_CLICK)
                }
            }
            else{
                showMoreLayout!!.visibility = View.GONE

            }

            rvHomeCategory!!.visibility = View.GONE
            //pullToRefreshLayout!!.visibility = View.GONE
            if (parentFragment !is HomeFragment) {
                noWolooTitle!!.text = "Sorry, couldn’t find any Woloo Host On-Route"
                noWolooSearchText!!.visibility = View.GONE

            }
            if (isEnroute) {
                Logger.e("initViews", TAG + requireActivity().localClassName)
                tv_search!!.visibility = View.GONE
                tv_search_more!!.visibility = View.GONE

                noWolooTitle!!.visibility = View.GONE
                noWolooSearchText!!.visibility = View.GONE
             //   bottomMargin!!.visibility = View.GONE
                tvNoWolooFound!!.visibility = View.GONE
            }
        } else {
           // dotLayoutCreator(nearByStoreResponseList.size)
         //   pullToRefreshLayout!!.visibility = View.VISIBLE
            showMoreLayout!!.visibility = View.GONE
            rvHomeCategory!!.visibility = View.VISIBLE
            if (parentFragment is HomeFragment) {
                tv_search_more!!.visibility = View.VISIBLE

            }
            tvNoWolooFound!!.visibility = View.GONE
            if (isEnroute) {
                Logger.e("initViews Aarati check", TAG + requireActivity().localClassName)
                tv_search!!.visibility = View.GONE
                tv_search_more!!.visibility = View.GONE

                noWolooTitle!!.visibility = View.GONE
                noWolooSearchText!!.visibility = View.GONE
                tvNoWolooFound!!.visibility = View.GONE

            }
        }
        if (nearByStoreResponseList.size == 0 && pageNumber != 1) {
            stopLoading = true
        }
    }


    /*calling setNearestWalk*/
    fun setNearestWalkEnroute(
        nearByStoreResponseList: List<NearByStoreResponse.Data>,
        openNow: Boolean,
        bookmark: Boolean,
        isEnroute: Boolean
    ) {
        Logger.i("initViews Aarati check 1", "setNearestWalk")
        Logger.e("initViews aarati check 2", " $isEnroute")
        //if (pageNumber == 1)
        this.nearByStoreResponseList.clear()
        if (nearByStoreResponseList.size > 0) this.nearByStoreResponseList.addAll(
            nearByStoreResponseList
        )
        if(adapter != null) {
            adapter?.notifyDataSetChanged()
        }else
        {
            adapter1 = NearestWalkAdapterEnroute(requireContext(), nearByStoreResponseList , wolooViewModel, searchLocation)
            rvHomeCategory!!.isNestedScrollingEnabled = true
            rvHomeCategory!!.setHasFixedSize(false)
            rvHomeCategory!!.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false)
            rvHomeCategory!!.adapter = adapter1
        }
        if (this.nearByStoreResponseList.size == 0) {
            if (openNow) {
                noWolooTitle!!.text = resources.getString(R.string.no_woloo_found_at_moment)
            } else if (bookmark) {
                noWolooTitle!!.text = resources.getString(R.string.no_woloo_found_bookmark)
            } else {
                noWolooTitle!!.text = resources.getString(R.string.no_woloo_found)
            }
            tvNoWolooFound!!.visibility = View.GONE
            if (requireActivity() is WolooDashboard && this.nearByStoreResponseList.size == 0) {
                showMoreLayout!!.visibility = View.VISIBLE
                i("Aarati Visibility", "show morelayout 2")
            }

            rvHomeCategory!!.visibility = View.GONE
            //pullToRefreshLayout!!.visibility = View.GONE
            if (parentFragment !is HomeFragment) {
                noWolooTitle!!.text = "Sorry, couldn’t find any Woloo Host On-Route"
                noWolooSearchText!!.visibility = View.GONE
            }
            if (isEnroute) {
                Logger.e("initViews", TAG + requireActivity().localClassName)
                tv_search!!.visibility = View.GONE
                tv_search_more!!.visibility = View.GONE

                noWolooTitle!!.visibility = View.GONE
                noWolooSearchText!!.visibility = View.GONE
                //   bottomMargin!!.visibility = View.GONE
                tvNoWolooFound!!.visibility = View.GONE
            }
        } else {
            // dotLayoutCreator(nearByStoreResponseList.size)
            //   pullToRefreshLayout!!.visibility = View.VISIBLE
            rvHomeCategory!!.visibility = View.VISIBLE
            if (parentFragment is HomeFragment) {
                tv_search_more!!.visibility = View.VISIBLE

            }
            tvNoWolooFound!!.visibility = View.GONE
            if (isEnroute) {
                Logger.e("initViews Aarati check", TAG + requireActivity().localClassName)
                tv_search!!.visibility = View.GONE
                tv_search_more!!.visibility = View.GONE

                noWolooTitle!!.visibility = View.GONE
                noWolooSearchText!!.visibility = View.GONE
                tvNoWolooFound!!.visibility = View.GONE

            }
        }
        if (nearByStoreResponseList.size == 0 && pageNumber != 1) {
            stopLoading = true
        }
    }

   /* fun dotLayoutCreator(size: Int) {
        for (i in 0 until size) {
            val dot = View(requireActivity().applicationContext)
            val params = LinearLayout.LayoutParams(20, 20)
            params.setMargins(10, 0, 10, 0)
            dot.layoutParams = params
            dot.setBackgroundResource(R.color.white)
            dotsLayout!!.addView(dot)
            dotViews.add(dot)
            updateDotColor(i)
        }
    }

    private fun updateDotColor(selectedIndex: Int) {
        for (i in dotViews.indices) {
            if (i == selectedIndex) {
                dotViews.get(i).setBackgroundResource(R.color.start_theme_color)
            } else {
                dotViews.get(i).setBackgroundResource(R.color.chip_color)
            }
        }
    }*/

  /*  fun dotLayoutCreator(size: Int , selectedIndex : Int) {
        dotViews.clear()
        dotsLayout!!.removeAllViews()

        // Determine the maximum number of dots to show (up to 5)
        val maxDotsToShow = minOf(size, 5)
        for (i in 0 until maxDotsToShow) {
            val dot = View(requireActivity().applicationContext)
            val params = LinearLayout.LayoutParams(20, 20)
            params.setMargins(10, 0, 10, 0)
            dot.layoutParams = params

            // Initially set the unselected dot style (white filled, black border)
            dot.setBackgroundResource(R.drawable.dot_unselected)  // Use the unselected dot drawable
            dotsLayout!!.addView(dot)
            dotViews.add(dot)

            // Update the dot color based on its position (default is unselected)
            updateDotColor(selectedIndex)
        }
    }

    private fun updateDotColor(selectedIndex: Int) {
        for (i in dotViews.indices) {
            if (i == selectedIndex) {
                // Set selected dot to yellow with black border
                dotViews[i].setBackgroundResource(R.drawable.dot_selected)  // Use the selected dot drawable
            } else {
                // Set unselected dot to white with black border
                dotViews[i].setBackgroundResource(R.drawable.dot_unselected)  // Use the unselected dot drawable
            }
        }
    }*/


    private fun dotLayoutCreator(listSize: Int, selectedIndex: Int) {
        // Clear any previously added dots
        dotViews.clear()
        dotsLayout!!.removeAllViews()

        // Define the group size (for example, groups of 3 items)
        val groupSize = (listSize/10)

        // Calculate how many dots are needed (one for each group)
        val numberOfDots = (listSize + groupSize - 1) / groupSize // This will round up to cover all items

        // Loop through and create dots based on the number of groups
        for (i in 0 until numberOfDots) {
            val dot = View(requireActivity().applicationContext)
            val params = LinearLayout.LayoutParams(20, 20)
            params.setMargins(10, 0, 10, 0)
            dot.layoutParams = params

            // Initially set all dots to unselected (white filled, black border)
            dot.setBackgroundResource(R.drawable.dot_unselected)  // Use the unselected dot drawable
            dotsLayout!!.addView(dot)
            dotViews.add(dot)
        }

        // Update the color of the dots based on the selected group
        updateDotColor(selectedIndex, groupSize)
    }

    private fun updateDotColor(selectedIndex: Int, groupSize: Int) {
        // Calculate which group is selected
        val selectedGroupIndex = selectedIndex / groupSize

        // Loop through all the created dot views and set the correct drawable
        for (i in dotViews.indices) {
            if (i == selectedGroupIndex) {
                // Set the selected dot to yellow with a black border
                dotViews[i].setBackgroundResource(R.drawable.dot_selected)  // Use the selected dot drawable
            } else {
                // Set unselected dots to white with a black border
                dotViews[i].setBackgroundResource(R.drawable.dot_unselected)  // Use the unselected dot drawable
            }
        }
    }


    fun displaySearchedLoos() {
        try {
            Log.d("CLICKED", "in searched loos")
            nearByStoreResponseList.clear()
            if(showMoreLayout!!.visibility == View.VISIBLE)
            {
                showMoreLayout!!.visibility = View.GONE
            }

            val parentFragment = parentFragment as? HomeFragment
            parentFragment!!.markerDataMap.clear()
            scrollViewModel.setProgrammaticScroll(true)
            scrollViewModel.setScrollIndex(0)
            scrollViewModel.setScrollIndexForMarker(0)
            parentFragment.nearByStoreResponseList.clear()
            val selected = resources.getDrawable(R.drawable.yello_rectangle_shape)
            val notSelected = resources.getDrawable(R.drawable.rounded_gray_bg)


            adapter!!.notifyDataSetChanged()

        }
        catch (e: Exception)
        {

        }
    }


    fun distanceRangeChanged(changedRange : Int) {
        try {

            travelMode = Integer.parseInt(mSharedPreference!!.getStoredPreference(
                getContext(),
                SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey()))
            Log.d("Aarati CLICKED", "tv2Km in $changedRange , $travelMode")
            nearByStoreResponseList.clear()
            if(showMoreLayout!!.visibility == View.VISIBLE)
            {
                showMoreLayout!!.visibility = View.GONE
            }

            val parentFragment = parentFragment as? HomeFragment
            parentFragment!!.markerDataMap.clear()
            scrollViewModel.setProgrammaticScroll(true)
            scrollViewModel.setScrollIndex(0)
            scrollViewModel.setScrollIndexForMarker(0)
            parentFragment.nearByStoreResponseList.clear()
            range = changedRange
            val selected = resources.getDrawable(R.drawable.yello_rectangle_shape)
            val notSelected = resources.getDrawable(R.drawable.rounded_gray_bg)
            tv2Km!!.background = notSelected
            tv4Km!!.background = notSelected
            tv5Km!!.background = notSelected
            tv6Km!!.background = notSelected
            tv8km!!.background = notSelected
            tv10km!!.background = notSelected
            tv25Km!!.background = notSelected
            Logger.e("range", " $range")
            when (range) {
                2 -> tv2Km!!.background = selected
                4 -> tv4Km!!.background = selected
                5 -> tv5Km!!.background = selected
                6 -> tv6Km!!.background = selected
                8 -> tv8km!!.background = selected
                10 -> tv10km!!.background = selected
                25 -> tv25Km!!.background = selected
                else -> Logger.e("range else", " $range");
            }

            if(parentFragment.isApiCalled == false) {
                parentFragment.getNearByWoloos(
                    parentFragment!!.lastKnownLattitude,
                    parentFragment!!.lastKnownLongitude,
                    travelMode, range,
                    1,
                )

                adapter!!.notifyDataSetChanged()
            }
        }
        catch (e: Exception)
        {

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
        request.showAll = 2

        request.packageName = "in.woloo.app"
        request.isSearch = isSearch
        homeViewModel.getNearbyWoloos(request)
        setLiveData()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setLiveData() {


        val layoutManager = rvHomeCategory!!.layoutManager as LinearLayoutManager?
        scrollViewModel.isProgrammaticScroll.observe(viewLifecycleOwner) { isProgrammatic ->
            if (isProgrammatic == true) {
                scrollViewModel.setProgrammaticScroll(false) // Reset after ignoring
                return@observe
            }
        }

        val visiblePosition = layoutManager!!.findFirstCompletelyVisibleItemPosition()
        if (visiblePosition != RecyclerView.NO_POSITION)
        {
            scrollViewModel.setScrollIndexForMarker(visiblePosition) // Notify HomeFragment
            Log.d("scroll on pos ", visiblePosition.toString())
        }

        scrollViewModel.scrollToIndex.observe(viewLifecycleOwner, Observer { index ->
            Log.d("Aarati List 1", index.toString())
            rvHomeCategory!!.post {
                /* rvHomeCategory?.smoothScrollToPosition(index)
            rvHomeCategory?.scrollToPosition(index)*/
                if (index in 0 until (rvHomeCategory!!.adapter?.itemCount ?: 0)) {
                    Log.d("Aarati List", "Valid index. Scrolling to: $index")
                    rvHomeCategory!!.smoothScrollToPosition(index)
                } else {
                    Log.d("Aarati List", "Invalid index: $index")
                }

            }
        })



        homeViewModel.observeNearByWoloo().observe(viewLifecycleOwner) { arrayListBaseResponse ->


            if (arrayListBaseResponse != null) {
                scrollViewModel.setScrollIndex(0)
                    nearByStoreResponseList = arrayListBaseResponse.data!!
                    Log.d("Check type", "size is $nearByStoreResponseList")
                    showMoreLayout!!.visibility = View.GONE
                    adapter = NearestWalkAdapter(
                        requireActivity(),
                        nearByStoreResponseList,
                        wolooViewModel,
                        scrollViewModel, searchLocation
                    )
                    Logger.e("initViews", " ${nearByStoreResponseList.size}")
                    rvHomeCategory!!.isNestedScrollingEnabled = true
                    rvHomeCategory!!.setHasFixedSize(false)
                    rvHomeCategory!!.layoutManager = LinearLayoutManager(
                        requireActivity(), LinearLayoutManager.HORIZONTAL, false
                    )
                    rvHomeCategory!!.adapter = adapter
                    rvHomeCategory!!.adapter!!.notifyDataSetChanged()
                    rvHomeCategory!!.visibility = View.VISIBLE
                    renderWoloosShowmore()
            } else {

               /* val request = CreditCoinsRequest(
                    coins = CommonUtils.authconfig_response(requireContext()).getNoWolooFound()!!.toInt(),
                    remarks = AppConstants.NO_WOLOO_FOUND_CLICK,
                    type = AppConstants.NO_WOLOO_FOUND_CLICK,
                    isGift = 0,
                    blogId = 0
                )
                wolooViewModel!!.addCoinstoWolooUser(request)
                var bundle = Bundle()
                var payload = HashMap<String, Any>()
                bundle.putString(AppConstants.NO_WOLOO_FOUND_CLICK, "No Woloos Found")
                bundle.putString(AppConstants.NO_WOLOO_FOUND_CLICK, "No Woloos Found")
                payload[AppConstants.NO_WOLOO_FOUND_CLICK] = "No Woloos Found"
                payload[AppConstants.NO_WOLOO_FOUND_CLICK] = "No Woloos Found"
                logFirebaseEvent(requireActivity(), bundle, AppConstants.NO_WOLOO_FOUND_CLICK)
                logNetcoreEvent( requireActivity(), payload, AppConstants.NO_WOLOO_FOUND_CLICK)*/
                nearByStoreResponseList = java.util.ArrayList<NearByStoreResponse.Data>()
            }
            rvHomeCategory!!.adapter?.notifyDataSetChanged()

        }
    }

    fun renderWoloosShowmore()
    {
        val parentFragment = parentFragment as? HomeFragment
        Log.d("Aarati parent fragment" , parentFragment!!.tag.toString() )
        try {
            parentFragment!!.markerList = java.util.ArrayList<Marker>()
            parentFragment.map.clear()

            for (i in nearByStoreResponseList.indices) {
                val data = nearByStoreResponseList[i]
                //  if(nearByStoreResponseList.get(i).isOpenNow.matches("1")) {
                val marker: Marker = parentFragment.createMarker(
                    data.lat!!.toDouble(),
                    data.lng!!.toDouble(),
                    data.title,
                    "",
                    R.drawable.ic_store_mark_dest,
                    i
                )
                parentFragment.markerList.add(marker)

                /* }
                else {
                    markerList.add(createMarker(Double.parseDouble(data.lat), Double.parseDouble(data.lng), data.title, "", R.drawable.try_icon, i));
                }*/
                parentFragment.markerDataMap.put(marker, data)
            }
            parentFragment.animateCameraToMarkerPosition(0)
            if (parentFragment.isFromClickFlag) {
                //hideAndShow(((WolooDashboard) requireActivity()).isOverLay);
                //  ((WolooDashboard) requireActivity()).hideAndShow(((WolooDashboard) requireActivity()).isOverLay);
            } else parentFragment.isFromClickFlag = true
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }



    fun showSuccessDialog(coins: String)
    {
        try {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_coins_success)
            val displayMetrics = Resources.getSystem().displayMetrics
            val screenWidth = displayMetrics.widthPixels

// Calculate 80% of screen width
            val dialogWidth = (screenWidth).toInt()

// Apply the calculated width and wrap_content height to the dialog window
            dialog.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)
            val btnCloseDialog = dialog.findViewById<View>(R.id.tv_go_back_to_home) as TextView
            val btnShopDialog = dialog.findViewById<View>(R.id.tv_shop_now) as TextView
            val gifImageView = dialog.findViewById<View>(R.id.gifImageView) as ImageView

            val btnSuccessTextDialog = dialog.findViewById<View>(R.id.tv_logout) as TextView
            btnSuccessTextDialog.setText("Woohoo! You Earned ${coins} Woloo Points!")
            btnShopDialog.visibility = View.GONE

            Glide.with(this)
                .load(R.drawable.coins_animate) // your gif in res/drawable
                .into(gifImageView)

            btnCloseDialog.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()

                }
            }





            dialog.show()
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }


    fun setChildViewVisibility(visible: Boolean) {
       showMoreLayout!!.visibility = if (visible) View.VISIBLE else View.GONE
    }

    companion object {
        var TAG = "HomeCategoryFragment"
    }


    fun showSuccessDialogNoWoloos()
    {
        try {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_coins_success)
            val displayMetrics = Resources.getSystem().displayMetrics
            val screenWidth = displayMetrics.widthPixels

// Calculate 80% of screen width
            val dialogWidth = (screenWidth)

// Apply the calculated width and wrap_content height to the dialog window
            dialog.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)
            val btnCloseDialog = dialog.findViewById<View>(R.id.tv_go_back_to_home) as TextView
            val btnShopDialog = dialog.findViewById<View>(R.id.tv_shop_now) as TextView
            val gifImageView = dialog.findViewById<View>(R.id.gifImageView) as ImageView
            val btnSuccessTextDialog = dialog.findViewById<View>(R.id.tv_logout) as TextView
            btnSuccessTextDialog.setText("Woohoo! You Earned 50 Woloo Points! Sorry No loos found")
            btnCloseDialog.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }

            Glide.with(this)
                .load(R.drawable.coins_animate) // your gif in res/drawable
                .into(gifImageView)


            dialog.show()
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }



    fun showSearchLocationDialog()
    {
        try {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_search_another_location)
            val displayMetrics = Resources.getSystem().displayMetrics
            val screenWidth = displayMetrics.widthPixels

            val dialogWidth = (screenWidth).toInt()
            dialog.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window?.setGravity(Gravity.CENTER)
            val btnCloseDialog = dialog.findViewById<View>(R.id.tv_go_back_to_home) as TextView


            btnCloseDialog.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }


            dialog.show()
            isSearchDialog = true
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }


}

class SharedSearchViewModel : ViewModel() {
    val searchText = MutableLiveData<String>()
}
