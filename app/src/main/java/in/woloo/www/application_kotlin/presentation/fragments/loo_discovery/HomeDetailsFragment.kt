package `in`.woloo.www.application_kotlin.presentation.fragments.loo_discovery

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.location.LocationManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.adapters.loo_discovery.NearByWolooImageAdapter
import `in`.woloo.www.application_kotlin.adapters.loo_discovery.ReviewsAdapter
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.MessageResponse
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.model.server_response.LikeResponse
import `in`.woloo.www.application_kotlin.model.server_response.LikeStatusResponse
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.application_kotlin.interfaces.HomeDetailsView
import `in`.woloo.www.mapdirection.DirectionsJSONParser
import `in`.woloo.www.mapdirection.GpsTracker
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.AddReviewActivity
import `in`.woloo.www.application_kotlin.model.server_response.ReviewListResponse

import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.EndlessRecyclerOnScrollListener
import `in`.woloo.www.utils.EqualSpacingItemDecoration
import `in`.woloo.www.utils.ImageUtil
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.EnrouteDirectionActivity
import `in`.woloo.www.application_kotlin.model.server_request.ReviewListRequest
import `in`.woloo.www.application_kotlin.model.server_request.WolooEngagementRequest
import `in`.woloo.www.application_kotlin.view_models.HomeViewModel
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.math.roundToInt
import `in`.woloo.www.application_kotlin.model.server_response.SearchWolooResponse.Data.Woloo

class HomeDetailsFragment : Fragment(), HomeDetailsView, OnMapReadyCallback {
    @JvmField
    @BindView(R.id.chipsFacilityServices)
    var chipsFacilityServices: ChipGroup? = null

    @JvmField
    @BindView(R.id.rvPhotos)
    var rvPhotos: RecyclerView? = null

    @JvmField
    @BindView(R.id.rvReviews)
    var rvReviews: RecyclerView? = null

    @JvmField
    @BindView(R.id.tvTitle)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.tvAddress)
    var tvAddress: TextView? = null

    @JvmField
    @BindView(R.id.tv_reviewslabel)
    var tv_reviewslabel: TextView? = null

    @JvmField
    @BindView(R.id.tvDistance)
    var tvDistance: TextView? = null

    @JvmField
    @BindView(R.id.tvRequiredTime)
    var tvRequiredTime: TextView? = null

    @JvmField
    @BindView(R.id.tvRating)
    var tvRating: TextView? = null

    @JvmField
    @BindView(R.id.tvOpeningHours)
    var tvOpeningHours: TextView? = null

    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: LinearLayout? = null

    @JvmField
    @BindView(R.id.tvPremium)
    var tvPremium: TextView? = null

    @JvmField
    @BindView(R.id.tv_direction)
    var tv_direction: TextView? = null

    @JvmField
    @BindView(R.id.tv_like)
    var tv_like: TextView? = null

    @JvmField
    @BindView(R.id.tv_start)
    var tv_start: TextView? = null

    @JvmField
    @BindView(R.id.iv_banner)
    var iv_banner: ImageView? = null

    @JvmField
    @BindView(R.id.iv_banner_recycle)
    var iv_banner_recycle: RecyclerView? = null

    @JvmField
    @BindView(R.id.v_review)
    var v_review: View? = null

    @JvmField
    @BindView(R.id.fl_map)
    var fl_map: FrameLayout? = null

    @JvmField
    @BindView(R.id.tvPhotos)
    var tvPhotos: TextView? = null

    @JvmField
    @BindView(R.id.vwPhotoDevider)
    var vwPhotoDevider: View? = null

    @JvmField
    @BindView(R.id.tvShare)
    var tvShare: TextView? = null

    @JvmField
    @BindView(R.id.directions_layout)
    var directionsLayout: LinearLayout? = null

    @JvmField
    @BindView(R.id.share_now_layout)
    var shareNowLayout: LinearLayout? = null

    @JvmField
    @BindView(R.id.is_like_layout)
    var isLikeLayout: LinearLayout? = null


    @JvmField
    @BindView(R.id.ivTransportMode)
    var ivTransportMode: ImageView? = null

    @JvmField
    @BindView(R.id.btnRedeemOffer)
    var btnRedeemOffer: Button? = null

    @JvmField
    @BindView(R.id.cibil_image)
    var cibilImage: ImageView? = null

    @JvmField
    @BindView(R.id.v_cibil)
    var v_cibil: View? = null

    @JvmField
    @BindView(R.id.cibil_title)
    var cibilTitle: TextView? = null

    @JvmField
    @BindView(R.id.btnAddReview)
    var buttonAddReview: ImageView? = null

    @JvmField
    @BindView(R.id.no_reviews)
    var no_reviews: TextView? = null
    private var nearByWoloo: NearByStoreResponse.Data? = null
    private var reviewAdapter: ReviewsAdapter? = null
    private val reviewList: MutableList<ReviewListResponse.Review> = ArrayList()
    private var endlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener? = null
    private var mPageNumber = 0
    private var mNextPage = 0
    private var buttonClick = 0
    private var mMap: GoogleMap? = null
    private var marker: Marker? = null
    var gps: GpsTracker? = null
    var locationManager: LocationManager? = null
    var destlat: String? = null
    var destlong: String? = null
    private var curlat = 0.0
    private var curlon = 0.0
    private var currentpos: LatLng? = null
    private var markerPoints: ArrayList<MarkerData> = ArrayList<MarkerData>()
    private var origin: LatLng? = null
    private var dest: LatLng? = null
    protected var mSharedPreference: SharedPreference? = null
    private var fromSearch = false
    private var searchedWoloo: Woloo? = null
    private var homeViewModel: HomeViewModel? = null
    var wolooEngagementRequest: WolooEngagementRequest = WolooEngagementRequest()
    var previousLikeStatus = -1
    var updatedLikeStatus = -1

    /*calling onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            fromSearch = requireArguments().getBoolean(AppConstants.FROM_SEARCH, false)
        }
        Logger.i(TAG, "onCreate")
    }

    /*calling onCreateView*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Logger.i(TAG, "onCreateView")
        val rootViews: View = inflater.inflate(R.layout.fragment_home_details, container, false)
        ButterKnife.bind(this, rootViews)
        initViews()
        setLiveData()
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        gps = GpsTracker(requireContext())
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                2000,
//                10, locationListenerGPS);
        return rootViews
    }

    /*calling drawMarker*/
    fun drawMarker(curlat: Double, curlon: Double) {
        Logger.i(TAG, "drawMarker")
        val circleDrawable = resources.getDrawable(R.drawable.ic_loaction)
        val markerIcon = getMarkerIconFromDrawable(circleDrawable)
        marker = mMap!!.addMarker(
            MarkerOptions()
                .position(LatLng(curlat, curlon)) //                .title("My Marker")
                .icon(markerIcon).flat(true)
        )
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(curlat, curlon)))
        animateMarker(marker, LatLng(curlat, curlon), false)
        try {
            val bearing = bearingBetweenLocations(origin, dest).toFloat()
            marker!!.rotation = bearing
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    /*calling animateMarker*/
    fun animateMarker(marker: Marker?, toPosition: LatLng, hideMarker: Boolean) {
        Logger.i(TAG, "animateMarker")
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val proj = mMap!!.projection
        val startPoint = proj.toScreenLocation(marker!!.position)
        val startLatLng = proj.fromScreenLocation(startPoint)
        val duration: Long = 500
        val interpolator: Interpolator = LinearInterpolator()
        handler.post(object : Runnable {
            override fun run() {
                val elapsed = SystemClock.uptimeMillis() - start
                val t = interpolator.getInterpolation(elapsed.toFloat() / duration)
                val lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude
                val lat = t * toPosition.latitude + (1 - t)  * startLatLng.latitude
                marker.position = LatLng(lat, lng)
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16)
                } else {
                    if (hideMarker) {
                        marker.isVisible = false
                    } else {
                        marker.isVisible = true
                    }
                }
            }
        })
    }

    /*calling bearingBetweenLocations*/
    private fun bearingBetweenLocations(latLng1: LatLng?, latLng2: LatLng?): Double {
        Logger.i(TAG, "bearingBetweenLocations")
        val PI = 3.14159
        val lat1 = latLng1!!.latitude * PI / 180
        val long1 = latLng1.longitude * PI / 180
        val lat2 = latLng2!!.latitude * PI / 180
        val long2 = latLng2.longitude * PI / 180
        val dLon = long2 - long1
        val y = Math.sin(dLon) * Math.cos(lat2)
        val x = Math.cos(lat1) * Math.sin(lat2) - (Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon))
        var brng = Math.atan2(y, x)
        brng = Math.toDegrees(brng)
        brng = (brng + 360) % 360
        return brng
    }

    /*calling getMarkerIconFromDrawable*/
    private fun getMarkerIconFromDrawable(drawable: Drawable): BitmapDescriptor {
        Logger.i(TAG, "getMarkerIconFromDrawable")
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    /*calling initViews*/
    fun initViews() {
        try {
            Logger.i(TAG, "initViews")
            homeViewModel = ViewModelProvider(this).get<HomeViewModel>(
                HomeViewModel::class.java
            )
            if (fromSearch) {
                searchedWoloo = WolooApplication.instance!!.searchedWoloo
                setData()
                /*if (searchedWoloo != null && searchedWoloo.getOffer() != null) {
                    //setOffers();
                } else {
                    tvPhotos.setVisibility(View.GONE);
                    vwPhotoDevider.setVisibility(View.GONE);
                    rvPhotos.setVisibility(View.GONE);
                }*/mPageNumber = 1
                reviews
                setReviews()
                tv_start!!.setOnClickListener { goToMaps() }
                tv_like!!.setOnClickListener {
                    val params = Bundle()
                    params.putString(AppConstants.WOLOO_NAME, searchedWoloo?.id.toString())
                    Utility.logFirebaseEvent(activity, params, AppConstants.LIKE_WOLOO_EVENT)
                    val payload = HashMap<String, Any>()
                    payload[AppConstants.WOLOO_NAME] = searchedWoloo?.id.toString()
                    Utility.logNetcoreEvent(requireActivity(), payload, AppConstants.LIKE_WOLOO_EVENT)
                    wolooEngagementRequest.wolooId = searchedWoloo?.id.toString()
                    val (id) = CommonUtils().userInfo!!
                    wolooEngagementRequest.userId = id.toString()
                    if (buttonClick == 0) {
                        buttonClick = 2
                        try {
                            wolooEngagementRequest.like = 1
                            homeViewModel!!.wolooEngagement(wolooEngagementRequest)
                        } catch (ex: Exception) {
                            CommonUtils.printStackTrace(ex)
                        }
                    } else if (buttonClick == 2) {
                        buttonClick = 0
                        try {
                            wolooEngagementRequest.like = 0
                            homeViewModel!!.wolooEngagement(wolooEngagementRequest)
                        } catch (ex: Exception) {
                            CommonUtils.printStackTrace(ex)
                        }
                    }
                }
            } else {
                nearByWoloo = WolooApplication.instance!!.nearByWoloo
                previousLikeStatus = nearByWoloo!!.isLiked
                setData()

                mPageNumber = 1
                reviews
                setReviews()
                tv_start!!.setOnClickListener { goToMaps() }
                if (nearByWoloo!!.isLiked == 1) {
                    buttonClick = 2
                    setLike()
                } else {
                    buttonClick = 0
                    setDislike()
                }
                tv_like!!.setOnClickListener {
                    val params = Bundle()
                    params.putString(AppConstants.WOLOO_NAME, nearByWoloo!!.id.toString())
                    Utility.logFirebaseEvent(activity, params, AppConstants.LIKE_WOLOO_EVENT)
                    val payload = HashMap<String, Any>()
                    payload[AppConstants.WOLOO_NAME] = nearByWoloo!!.id.toString()
                    Utility.logNetcoreEvent(requireActivity(), payload, AppConstants.LIKE_WOLOO_EVENT)
                    wolooEngagementRequest.wolooId = nearByWoloo!!.id.toString()
                    val (id) = CommonUtils().userInfo!!
                    wolooEngagementRequest.userId = id.toString()
                    if (buttonClick == 0) {
                        buttonClick = 2
                        try {
                            updatedLikeStatus = 1
                            wolooEngagementRequest.like = 1
                            homeViewModel!!.wolooEngagement(wolooEngagementRequest)
                            setLike()

                        } catch (ex: Exception) {
                            CommonUtils.printStackTrace(ex)
                        }
                    } else if (buttonClick == 2) {
                        buttonClick = 0
                        try {
                            updatedLikeStatus = 0
                            wolooEngagementRequest.like = 0
                            homeViewModel!!.wolooEngagement(wolooEngagementRequest)
                            setDislike()
                        } catch (ex: Exception) {
                            CommonUtils.printStackTrace(ex)
                        }
                    }
                }
            }
            tvShare!!.setOnClickListener { v: View? ->
                val params = Bundle()
                params.putString(AppConstants.WOLOO_NAME, nearByWoloo!!.id.toString())
                Utility.logFirebaseEvent(activity, params, AppConstants.SHARE_WOLOO_EVENT)
                val payload = HashMap<String, Any>()
                payload[AppConstants.WOLOO_NAME] = nearByWoloo!!.id.toString()
                Utility.logNetcoreEvent(requireActivity(), payload, AppConstants.SHARE_WOLOO_EVENT)
                shareMessage()
                shareNowLayout!!.background = ContextCompat.getDrawable(requireActivity().applicationContext , R.drawable.yellow_border_white_rect)
            }
            if (mSharedPreference == null) {
                mSharedPreference = SharedPreference(context)
            }
            val transport_mode: String = mSharedPreference!!.getStoredPreference(
                context,
                SharedPreferencesEnum.TRANSPORT_MODE.preferenceKey,
                "0"
            ).toString()
            when (transport_mode) {
            /*    "0" -> ivTransportMode!!.setImageResource(R.drawable.ic_car)
                "1" -> ivTransportMode!!.setImageResource(R.drawable.ic_walking_transport_mode)
                "2" -> ivTransportMode!!.setImageResource(R.drawable.ic_bicycle_transport_mode)*/
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    override fun onPause() {
        super.onPause()
        Logger.d(TAG, " WolooHomeFragment onPause")
        if (previousLikeStatus != updatedLikeStatus) {
            WolooApplication.instance!!.updatedLikeStatus
        }
    }

    fun setLiveData() {
        homeViewModel!!.observeWolooEngagement()
            .observe(viewLifecycleOwner, Observer<BaseResponse<JSONObject>> { response ->
                if (response != null && response.success) {
//                    if(wolooEngagementRequest.getLike() == 0)
//                        setDislike();
//                    else
//                        setLike();
                } else {
                    if (!WolooApplication.errorMessage.isEmpty()) {
                        Toast.makeText(
                            context,
                            WolooApplication.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    WolooApplication.errorMessage = ""
                }
            })
        homeViewModel!!.observeReviewList()
            .observe(viewLifecycleOwner, Observer<BaseResponse<ReviewListResponse.Data>> { reviewListResponse ->
                if (reviewListResponse != null && reviewListResponse.data != null) {
                    try {
                        if (reviewListResponse.data!!.review != null && reviewListResponse.data!!.reviewCount == 0) {
                            //Changed by Aarati 29 Aug 24
                            rvReviews!!.visibility = View.GONE
                            tv_reviewslabel!!.visibility = View.VISIBLE
                            v_review!!.visibility = View.VISIBLE
                            no_reviews!!.visibility = View.VISIBLE
                        } else {
                            v_review!!.visibility = View.VISIBLE
                            rvReviews!!.visibility = View.VISIBLE
                            tv_reviewslabel!!.visibility = View.VISIBLE
                            no_reviews!!.visibility = View.GONE
                            if (mPageNumber == 1) {
                                reviewList.clear()
                            }
                            reviewListResponse.data!!.review?.let { reviewList.addAll(it) }
                            reviewAdapter?.notifyDataSetChanged()

                            try {
                                val next = reviewListResponse.data?.next
                                mNextPage = if ( next != null &&  next != 0) {
                                    next
                                } else {
                                    0
                                }
                            } catch (ex: Exception) {
                                CommonUtils.printStackTrace(ex)
                            }


                        }
                    } catch (e: Exception) {
                        CommonUtils.printStackTrace(e)
                    }
                } else {
                    if (WolooApplication.errorMessage.isNotEmpty()) {
                        Toast.makeText(
                            context,
                            WolooApplication.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    WolooApplication.errorMessage = ""
                }
            })
        homeViewModel!!.observeRedeemOffer().observe(viewLifecycleOwner, Observer<BaseResponse<MessageResponse>> { response ->
            if (response != null && response.success) {
                onRedeemSuccess()
            } else {
                if (WolooApplication.errorMessage.isNotEmpty()) {
                    Toast.makeText(context, WolooApplication.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                }
                WolooApplication.errorMessage = ""
            }
        })
    }

    /*calling showMapDirection*/
    fun showMapDirection() {
        try {
            Logger.i(TAG, "showMapDirection")
            val i = Intent(context, EnrouteDirectionActivity::class.java)
            if (fromSearch) {
                i.putExtra("destlat", searchedWoloo?.lat)
                i.putExtra("destlong", searchedWoloo?.lng)
                i.putExtra("wolooId", searchedWoloo?.id)
                i.putExtra("wolooName", searchedWoloo?.name)
                i.putExtra("wolooAddress", searchedWoloo?.address)
                i.putExtra("tag", "start")
            } else {
                i.putExtra("destlat", nearByWoloo!!.lat)
                i.putExtra("destlong", nearByWoloo!!.lng)
                i.putExtra("wolooId", nearByWoloo!!.id)
                i.putExtra("wolooName", nearByWoloo!!.name)
                i.putExtra("wolooAddress", nearByWoloo!!.address)
                i.putExtra("tag", "start")
            }
            requireContext().startActivity(i)
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private val reviews: Unit
        /*calling getReviews*/private get() {
            try {
                Logger.i(TAG, "getReviews")
                val request = ReviewListRequest()
                request.pageNumber = mPageNumber
                if (fromSearch) {
                    request.wolooId = searchedWoloo?.id!!
                } else {
                    request.wolooId = nearByWoloo!!.id!!
                }
                homeViewModel?.getReviewList(request)
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }

    /*calling setData*/
    @SuppressLint("SetTextI18n")
    private fun setData() {
        try {
            Logger.i(TAG, "setData")
            if (fromSearch) {
                tvTitle?.text = searchedWoloo?.name
                tvAddress?.text = searchedWoloo?.address
                tvDistance!!.text = searchedWoloo?.distance
                tvRequiredTime!!.text =
                    "" + CommonUtils.getTimeForWolooStoreInfo(searchedWoloo?.duration!!)
                tvRating!!.text = "" + searchedWoloo?.userRating
                if (searchedWoloo?.cibilScoreImage !== "") {
                    cibilImage?.let {
                        Glide.with(requireContext())
                            .load(searchedWoloo?.cibilScoreImage)
                            .into(it)
                    }
                    cibilImage!!.visibility = View.VISIBLE
                    cibilTitle!!.visibility = View.VISIBLE
                    v_cibil!!.visibility = View.VISIBLE
                } else {
                    cibilImage!!.visibility = View.GONE
                    cibilTitle!!.visibility = View.GONE
                    v_cibil!!.visibility = View.GONE
                }
                if (!TextUtils.isEmpty(searchedWoloo?.openingHours)) tvOpeningHours!!.text =
                    "" + getString(R.string.open_time) + " " + searchedWoloo?.openingHours
                if (searchedWoloo?.isPremium == 1) {
                    tvPremium!!.visibility = View.VISIBLE
                } else {
                    tvPremium!!.visibility = View.GONE
                }
                setCategoryChips()
                tv_direction!!.setOnClickListener {
                    //goToMaps();
                    directionsLayout!!.background = ContextCompat.getDrawable(requireActivity().applicationContext , R.drawable.yellow_border_white_rect)
                    if (searchedWoloo?.distance == "-") {
                        CommonUtils.showCustomDialog(
                            requireActivity(),
                            "No route found for the transport mode. Please change mode and try again"
                        )
                    } else {
                        val params = Bundle()
                        params.putString(AppConstants.WOLOO_NAME, searchedWoloo?.id.toString())
                        Utility.logFirebaseEvent(
                            activity,
                            params,
                            AppConstants.DIRECTION_WOLOO_EVENT
                        )
                        val payload = HashMap<String, Any>()
                        payload[AppConstants.WOLOO_NAME] = searchedWoloo?.id.toString()
                        Utility.logNetcoreEvent(
                            requireActivity(),
                            payload,
                            AppConstants.DIRECTION_WOLOO_EVENT
                        )
                        //                        fl_map.setVisibility(View.VISIBLE);
//                        iv_banner.setVisibility(View.GONE);
                        try {
                            val i = Intent(context, EnrouteDirectionActivity::class.java)
                            if (fromSearch) {
                                i.putExtra("destlat", searchedWoloo?.lat)
                                i.putExtra("destlong", searchedWoloo?.lng)
                                i.putExtra("wolooId", searchedWoloo?.id)
                                i.putExtra("wolooName", searchedWoloo?.name)
                                i.putExtra("wolooAddress", searchedWoloo?.address)
                                i.putExtra("tag", "start")
                            } else {
                                i.putExtra("destlat", nearByWoloo!!.lat)
                                i.putExtra("destlong", nearByWoloo!!.lng)
                                i.putExtra("wolooId", nearByWoloo!!.id)
                                i.putExtra("wolooName", nearByWoloo!!.name)
                                i.putExtra("wolooAddress", nearByWoloo!!.address)
                                i.putExtra("tag", "start")
                            }
                            requireContext().startActivity(i)
                        } catch (e: Exception) {
                            CommonUtils.printStackTrace(e)
                        }
                    }
                }
                /*if (!TextUtils.isEmpty(searchedWoloo.getImage())) {
                    String wolooImage = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + searchedWoloo.getImage();
                    ImageUtil.loadImage(getContext(), iv_banner, wolooImage);
                } else {
                    String imgUrl = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE;
                    ImageUtil.loadImage(getContext(), iv_banner, imgUrl);
                }*/if (nearByWoloo != null && nearByWoloo!!.offer != null) {
                    setOffers()
                } else setImageData(nearByWoloo!!.image)
            } else {
                tvTitle!!.text = nearByWoloo!!.name
                tvAddress!!.text = nearByWoloo!!.address
                tvDistance!!.text = nearByWoloo!!.distance
                tvRequiredTime!!.text = nearByWoloo!!.duration
                tvRating!!.text = "" + nearByWoloo!!.userRating
                if (nearByWoloo!!.cibilScoreImage !== "") {
                    Glide.with(requireContext())
                        .load(nearByWoloo!!.cibilScoreImage)
                        .into(cibilImage!!)
                    cibilImage!!.visibility = View.VISIBLE
                    cibilTitle!!.visibility = View.VISIBLE
                    v_cibil!!.visibility = View.VISIBLE
                } else {
                    cibilImage!!.visibility = View.GONE
                    cibilTitle!!.visibility = View.GONE
                    v_cibil!!.visibility = View.GONE
                }
                if (!TextUtils.isEmpty(nearByWoloo!!.openingHours)) tvOpeningHours!!.text =
                    "" + getString(R.string.open_time) + " " + nearByWoloo!!.openingHours
                if (nearByWoloo!!.isPremium == 1) {
                    tvPremium!!.visibility = View.VISIBLE
                } else {
                    tvPremium!!.visibility = View.GONE
                }
                // set offer button visibility
                if (nearByWoloo!!.offer != null) {
                    btnRedeemOffer!!.visibility = View.VISIBLE
                    btnRedeemOffer!!.setOnClickListener { view: View? ->
                        val offer = nearByWoloo!!.offer
                        offer!!.id?.let { homeViewModel?.redeemOffer(it) }
                    }
                } else {
                    btnRedeemOffer!!.visibility = View.GONE
                }
                setCategoryChips()
                buttonAddReview!!.setOnClickListener {
                    val i = Intent(requireActivity().applicationContext, AddReviewActivity::class.java)
                    i.putExtra(AppConstants.WOLOO_ID, nearByWoloo!!.id)
                    requireActivity().startActivity(i)
                }
                tv_direction!!.setOnClickListener {
                    //goToMaps(); //
                    if (nearByWoloo!!.distance == "-") {
                        CommonUtils.showCustomDialog(
                            requireActivity(),
                            "No route found for the transport mode. Please change mode and try again"
                        )
                    } else {
                        val params = Bundle()
                        val payload = HashMap<String, Any>()
                        if (fromSearch) {
                            params.putString(AppConstants.WOLOO_NAME, searchedWoloo?.id.toString())
                            payload[AppConstants.WOLOO_NAME] = searchedWoloo?.id.toString()
                        } else {
                            params.putString(AppConstants.WOLOO_NAME, nearByWoloo!!.id.toString())
                            payload[AppConstants.WOLOO_NAME] = nearByWoloo!!.id.toString()
                        }
                        Utility.logFirebaseEvent(
                            activity,
                            params,
                            AppConstants.DIRECTION_WOLOO_EVENT
                        )
                        Utility.logNetcoreEvent(
                            requireActivity(),
                            payload,
                            AppConstants.DIRECTION_WOLOO_EVENT
                        )
                        //                        fl_map.setVisibility(View.VISIBLE);
//                        iv_banner.setVisibility(View.GONE);
                        val i = Intent(context, EnrouteDirectionActivity::class.java)
                        if (fromSearch) {
                            i.putExtra("destlat", searchedWoloo?.lat)
                            i.putExtra("destlong", searchedWoloo?.lng)
                            i.putExtra("wolooId", searchedWoloo?.id)
                            i.putExtra("wolooName", searchedWoloo?.name)
                            i.putExtra("wolooAddress", searchedWoloo?.address)
                            i.putExtra("tag", "start")
                        } else {
                            i.putExtra("destlat", nearByWoloo!!.lat)
                            i.putExtra("destlong", nearByWoloo!!.lng)
                            i.putExtra("wolooId", nearByWoloo!!.id)
                            i.putExtra("wolooName", nearByWoloo!!.name)
                            i.putExtra("wolooAddress", nearByWoloo!!.address)
                            i.putExtra("tag", "start")
                        }
                        requireContext().startActivity(i)
                    }
                }
                /*if (nearByWoloo.getImage().size()>0) {
                    String wolooImage = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + nearByWoloo.getImage().get(0);
                    ImageUtil.loadImage(getContext(), iv_banner, wolooImage);
                } else {
                    String imgUrl = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE;
                    //ImageUtil.loadImage(getContext(), iv_banner, imgUrl);
                }*/if (nearByWoloo != null && nearByWoloo!!.offer != null) {
                    setOffers()
                } else setImageData(nearByWoloo!!.image)
                /*String imgUrl = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE;
                ImageUtil.loadImage(getContext(), iv_banner, imgUrl);*/
            }
            ivBack!!.setOnClickListener { v: View? -> requireActivity().onBackPressed() }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun setImageData(image: List<String?>) {
        var image: List<String?>? = image
        iv_banner!!.visibility = View.VISIBLE
        val imgUrl: String? = if (image != null && image.isNotEmpty()) {
            if (!image[0]!!.contains(nearByWoloo!!.baseUrl!!)) {
                nearByWoloo!!.baseUrl + image[0]
            } else {
                image[0]
            }
        } else {
            BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE
        }
        ImageUtil.loadImage(requireContext(), iv_banner!!, imgUrl)
        if (image == null || image.size == 0) {
            image = ArrayList()
            image.add(BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW)
        } else {
            var updatedImageList: List<String?>? = null
            for (i in image.indices) {
                /* if (!image[i]!!.contains(nearByWoloo!!.baseUrl!!)) {
                     image.set(i, nearByWoloo!!.baseUrl + image[i])
                 }*/


                updatedImageList = image.map { img ->
                    if (img != null && nearByWoloo?.baseUrl != null && !img.contains(nearByWoloo?.baseUrl!!)) {
                        nearByWoloo?.baseUrl + img
                    } else {
                        img
                    }
                }
            }


            tvPhotos!!.visibility = View.VISIBLE
            rvPhotos!!.visibility = View.VISIBLE
            val nearByWolooImageAdapter =
                activity?.let { NearByWolooImageAdapter(it, updatedImageList) }
            val mLayoutManager = LinearLayoutManager(
                activity, LinearLayoutManager.HORIZONTAL, false
            )
            rvPhotos!!.layoutManager = mLayoutManager
            rvPhotos!!.adapter = nearByWolooImageAdapter
            rvPhotos!!.isNestedScrollingEnabled = false
            ViewCompat.setNestedScrollingEnabled(rvPhotos!!, false)
            rvPhotos!!.setHasFixedSize(true)
            rvPhotos!!.setItemViewCacheSize(20)
            rvPhotos!!.isDrawingCacheEnabled = true
            rvPhotos!!.addItemDecoration(
                EqualSpacingItemDecoration(
                    dpToPx(2),
                    EqualSpacingItemDecoration.HORIZONTAL
                )
            ) // 16px. In practice, you'll want to use getDimensionPixelSize
            rvPhotos!!.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        }
    }

    fun dpToPx(dp: Int): Int {
        val r = requireActivity().resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            r.displayMetrics
        ).roundToInt()
    }

    fun goToMaps() {
        var lat: String? = ""
        var lng: String? = ""
        var mode = ""
        if (fromSearch) {
            lat = searchedWoloo?.lat
            lng = searchedWoloo?.lng
        } else {
            lat = nearByWoloo!!.lat
            lng = nearByWoloo!!.lng
        }
        val transport_mode: String = mSharedPreference?.getStoredPreference(
            context,
            SharedPreferencesEnum.TRANSPORT_MODE.preferenceKey,
            "0"
        ).toString()
        when (transport_mode) {
            "0" -> mode = "d"
            "1" -> mode = "w"
            "2" -> mode = "l" //b for bycycler & l for two wheeler
        }
        // Create a Uri from an intent string. Use the result to create an Intent.
        val request = "google.navigation:q=$lat,$lng&mode=$mode"
        Logger.e(TAG, request)
        val mapIntentUri = Uri.parse(request)
        // Create an Intent from mapIntentUri. Set the action to ACTION_VIEW
        val mapIntent = Intent(Intent.ACTION_VIEW, mapIntentUri)
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps")
        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent)
    }

    /*calling setReviews*/
    private fun setReviews() {
        try {
            Logger.i(TAG, "setReviews")
            reviewAdapter = ReviewsAdapter(requireContext(), reviewList)
           // rvReviews!!.setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(
                context, RecyclerView.VERTICAL, false
            )
            rvReviews!!.layoutManager = linearLayoutManager
            endlessRecyclerOnScrollListener =
                object : EndlessRecyclerOnScrollListener(linearLayoutManager) {
                    override fun onLoadMore(current_page: Int) {
                        mPageNumber = mNextPage
                        if (mNextPage != 0) {
                            loadMore()
                        }
                    }
                }
            rvReviews!!.addOnScrollListener(endlessRecyclerOnScrollListener!!)
            rvReviews!!.adapter = reviewAdapter
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling loadMore*/
    private fun loadMore() {
        reviews
        Logger.i(TAG, "loadMore")
    }

    /*calling setOffers*/
    private fun setOffers() {
        try {
            Logger.i(TAG, "setOffers")
            if (fromSearch) {
                /*List<SearchWolooResponse.Data.Offer> offerList = new ArrayList<SearchWolooResponse.Data.Offer>();
                offerList.add(searchedWoloo.getOffer());
                SearchedPhotosAdapter adapter = new SearchedPhotosAdapter(getContext(), offerList);
                rvPhotos.setHasFixedSize(true);
                rvPhotos.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                rvPhotos.setAdapter(adapter);*/
                val wolooImage =
                    BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + searchedWoloo?.offer?.image
                ImageUtil.loadImage(requireContext(), iv_banner!!, wolooImage)
            } else {
                /*List<NearByStoreResponse.Data.Offer> offerList = new ArrayList<NearByStoreResponse.Data.Offer>();
                offerList.add(nearByWoloo.getOffer());
                PhotosAdapter adapter = new PhotosAdapter(getContext(), offerList);
                rvPhotos.setHasFixedSize(true);
                rvPhotos.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
                rvPhotos.setAdapter(adapter);*/
                val wolooImage =
                    BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + nearByWoloo!!.offer!!.image
                ImageUtil.loadImage(requireContext(), iv_banner!!, wolooImage)
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling setCategoryChips*/
    fun setCategoryChips() {
        Logger.i(TAG, "setCategoryChips")
        if (fromSearch) {
            if (searchedWoloo?.isSafeSpace == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.safe_space)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_safe_chip)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            if (searchedWoloo?.isCovidFree == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.covid_free)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_covid_free)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            if (searchedWoloo?.isCleanAndHygiene == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.clean_and_hygiene)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_clean_hygienic_icon)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            ///////////////
            if (searchedWoloo?.isWashroom != null && searchedWoloo!!.isWashroom == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_washroom)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_toilet)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            } else {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_washroom)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_indian_toilet)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            if (searchedWoloo?.isFeedingRoom != null && searchedWoloo?.isFeedingRoom == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_feeding_room)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_mom_feeding_baby)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            if (searchedWoloo?.isSanitizerAvailable != null && searchedWoloo?.isSanitizerAvailable == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_sanitizer)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_hand_sanitizer)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            if (searchedWoloo?.isCoffeeAvailable != null && searchedWoloo?.isCoffeeAvailable == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_coffee)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_coffee)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            if (searchedWoloo?.isMakeupRoomAvailable != null && searchedWoloo?.isMakeupRoomAvailable == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_makeup)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_makeup)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            if (searchedWoloo?.isWheelchairAccessible != null && searchedWoloo?.isWheelchairAccessible == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_wheelchair)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_physically_disable)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            if (searchedWoloo?.isSanitaryPadsAvailable != null && searchedWoloo?.isSanitaryPadsAvailable == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_sanitary_pads)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_diaper)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            if (searchedWoloo?.segregated != null && searchedWoloo?.segregated.equals(
                    "YES",
                    ignoreCase = true
                )
            ) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_separate)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_separate_toilet)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            } else {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_unisex)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_unisex_toilet)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            ////////////////
        } else {
            if (nearByWoloo!!.isSafeSpace == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.safe_space)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_safe_chip)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            if (nearByWoloo!!.isCovidFree == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.covid_free)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_covid_free)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            if (nearByWoloo!!.isCleanAndHygiene == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.clean_and_hygiene)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_clean_hygienic_icon)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            ///////////////
            if (nearByWoloo!!.isWashroom != null && nearByWoloo!!.isWashroom == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_washroom)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_toilet)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            } else {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_washroom)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_indian_toilet)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            if (nearByWoloo!!.isFeedingRoom != null && nearByWoloo!!.isFeedingRoom == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_feeding_room)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_mom_feeding_baby)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            if (nearByWoloo!!.isSanitizerAvailable != null && nearByWoloo!!.isSanitizerAvailable == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_sanitizer)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_hand_sanitizer)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            if (nearByWoloo!!.isCoffeeAvailable != null && nearByWoloo!!.isCoffeeAvailable == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_coffee)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_coffee)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            if (nearByWoloo!!.isMakeupRoomAvailable != null && nearByWoloo!!.isMakeupRoomAvailable == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_makeup)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_makeup)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            if (nearByWoloo!!.isWheelchairAccessible != null && nearByWoloo!!.isWheelchairAccessible == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_wheelchair)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_physically_disable)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            if (nearByWoloo!!.isSanitaryPadsAvailable != null && nearByWoloo!!.isSanitaryPadsAvailable == 1) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_sanitary_pads)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_diaper)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            if (nearByWoloo!!.segregated != null && nearByWoloo!!.segregated.equals(
                    "YES",
                    ignoreCase = true
                )
            ) {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_separate)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_unisex_toilet)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            } else {
                val mChip = this.layoutInflater.inflate(
                    R.layout.facility_services_chip_item,
                    null,
                    false
                ) as Chip
                mChip.text = getString(R.string.label_unisex)
                mChip.chipIcon = resources.getDrawable(R.drawable.ic_separate_toilet)
                mChip.chipIconSize = 50f
                mChip.isChipIconVisible = true
                mChip.chipIconTint =
                    ContextCompat.getColorStateList(requireContext(), R.color.white)
                val paddingDp = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10f,
                    resources.displayMetrics
                ).toInt()
                mChip.setPadding(paddingDp, 0, paddingDp, 0)
                chipsFacilityServices!!.addView(mChip)
            }
            ////////////////
        }
    }

    /*calling getReviewList*/
    override fun getReviewList(reviewListResponse: ReviewListResponse?) {
        try {
            Logger.i(TAG, "getReviewList")
            if (reviewListResponse != null) {
                if (reviewListResponse.data != null && reviewListResponse.data!!
                        .review != null && reviewListResponse.data!!.review?.size == 0
                ) {
                    rvReviews!!.visibility = View.GONE
                    tv_reviewslabel!!.visibility = View.GONE
                    v_review!!.visibility = View.GONE
                } else {
                    v_review!!.visibility = View.VISIBLE
                    rvReviews!!.visibility = View.VISIBLE
                    tv_reviewslabel!!.visibility = View.VISIBLE
                    if (mPageNumber == 1) {
                        reviewList.clear()
                    }
                    reviewListResponse.data!!.review?.let { reviewList.addAll(it) }
                    reviewAdapter?.notifyDataSetChanged()
                    try {
                        mNextPage = reviewListResponse.data?.next?.takeIf { it != 0 } ?: 0
                    } catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }

                }
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    override fun getLike_Unlike(likeResponse: LikeResponse?, tv_like: TextView?) {

    }

    override fun likeStatusSuccess(likeStatusResponse: LikeStatusResponse?) {

    }

    /*calling getLike_Unlike*/

    /*calling setLike*/
    fun setLike() {
        try {
            Logger.i(TAG, "setLike")
            isLikeLayout!!.background =  ContextCompat.getDrawable(requireActivity().applicationContext , R.drawable.yellow_border_white_rect)
           /* tv_like!!.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.details_layer_list_liked,
                0,
                0,
                0
            )*/
          /*  tv_like!!.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.yello_rectangle_shape_new)
            tv_like!!.setTextColor(ContextCompat.getColor(requireContext(), R.color.text_color_five))*/
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling setDislike*/
    fun setDislike() {
       /* try {
            Logger.i(TAG, "setDislike")
            tv_like!!.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.details_like_layer_list,
                0,
                0,
                0
            )
            tv_like!!.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.transperent_rectangle_shape)
            tv_like!!.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }*/
        isLikeLayout!!.background =  ContextCompat.getDrawable(requireActivity().applicationContext , R.drawable.yello_rectangle_shape)
    }

    /*calling likeStatusSuccess*/

    override fun onRedeemSuccess() {
        Logger.i(TAG, "onRedeemSuccess")
        //Toast.makeText(requireActivity(), "Offer redeemed!", Toast.LENGTH_SHORT).show();
        showDialog("Offer redeemed! Please visit the Woloo host to redeem the offer.")
    }

    fun showDialog(msg: String?) {
        try {
            val dialog = Dialog(requireActivity())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_login_failure)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val btnCloseDialog = dialog.findViewById<View>(R.id.btnCloseDialog) as TextView
            btnCloseDialog.text = "Goto Offer cart"
            val tv_msg = dialog.findViewById<View>(R.id.tv_msg) as TextView
            tv_msg.text = msg
            btnCloseDialog.setOnClickListener {
                if (dialog.isShowing) {
                    val intent = Intent(requireActivity(), WolooDashboard::class.java)
                    intent.setAction(AppConstants.SHOW_OFFER_CART)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    requireActivity().startActivity(intent)
                    dialog.dismiss()
                }
            }
            dialog.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    /*calling createMarker*/
    private fun createMarker(
        latitude: Double,
        longitude: Double,
        title: String,
        snippet: String,
        iconResID: Int,
        index: Int
    ): Marker? {
        Logger.i(TAG, "createMarker")
        val height = 110
        val width = 90
        val bitmapdraw = resources.getDrawable(R.drawable.ic_store_mark_dest) as BitmapDrawable
        val b = bitmapdraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)
        return mMap!!.addMarker(
            MarkerOptions()
                .position(LatLng(latitude, longitude)) //    .anchor(0.5f, 0.5f)
                //  .title(title)
                .zIndex(index.toFloat()) //  .snippet(snippet));
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker))
        )
    }

    /*calling onMapReady*/
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        try {
            mMap = googleMap
            Logger.e("onMapReady", "OnMapready")
            Logger.i(TAG, "onMapReady")
            gps = GpsTracker(requireContext())
            curlat = gps!!.latitudeC
            curlon = gps!!.longitudeC
            currentpos = LatLng(curlat, curlon)
            if (fromSearch) {
                destlat = searchedWoloo?.lat
                destlong = searchedWoloo?.lng
            } else {
                destlat = nearByWoloo!!.lat
                destlong = nearByWoloo!!.lng
            }
            val cameraPosition = CameraPosition.Builder()
                .target(currentpos!!) // Sets the center of the map to Mountain View
                .zoom(15f) // Sets the zoom
                //                .bearing(30)                // Sets the orientation of the camera to east
                .tilt(30f) // Sets the tilt of the camera to 30 degrees
                .build() // Creates a CameraPosition from the builder
            mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 20, null)
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentpos!!, 10f))
            mMap!!.isMyLocationEnabled = true
            if (markerPoints.size > 1) {
                markerPoints.clear()
                mMap!!.clear()
            }
            val circleDrawable = resources.getDrawable(R.drawable.ic_loaction)
            val markerIcon = getMarkerIconFromDrawable(circleDrawable)
            marker = mMap!!.addMarker(
                MarkerOptions()
                    .position(LatLng(curlat, curlon)) //                    .title("My Marker")
                    .icon(markerIcon).flat(true)
            )
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(LatLng(curlat, curlon)))
            val latLng1 = destlat?.toDouble()?.let {
                destlong?.toDouble()?.let { it1 ->
                    LatLng(
                        it, it1
                    )
                }
            }
            markerPoints.add(MarkerData.LatLngItem(latLng1!!))

            createMarker(
                destlat!!.toDouble(),
                destlong!!.toDouble(),
                "My Destination",
                "",
                R.drawable.ic_store_mark_dest,
                0
            )?.let {
                MarkerData.MarkerItem(
                    it
                )
            }?.let { markerPoints.add(it) }


            val currentpos = LatLng(curlat, curlon)
            //                    LatLng origin = (LatLng) markerPoints.get(0);
            origin = currentpos
            dest = markerPoints[0] as LatLng
            try {
                val bearing = bearingBetweenLocations(origin, dest).toFloat()
                marker!!.rotation = bearing
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
            if (curlat != dest!!.latitude && curlon != dest!!.longitude) getdistanceVal(
                curlat,
                curlon,
                dest!!.latitude,
                dest!!.longitude
            ).execute()
            val url = getDirectionsUrl(origin, dest)
            Logger.e("url", "" + url)
            val downloadTask: DownloadTask = DownloadTask()
            downloadTask.execute(url)
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    /*calling downloadUrl*/
    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {
        Logger.i(TAG, "downloadUrl")
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection!!.connect()
            iStream = urlConnection.inputStream
            val br = BufferedReader(InputStreamReader(iStream))
            val sb = StringBuffer()
            var line: String? = ""
            while (br.readLine().also { line = it } != null) {
                sb.append(line)
            }
            data = sb.toString()
            br.close()
        } catch (e: Exception) {
            Logger.d("Exception", e.toString())
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }

    private inner class DownloadTask : AsyncTask<String?, Void?, String>() {

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            val parserTask: ParserTask = ParserTask()
            parserTask.execute(result)
        }

        override fun doInBackground(vararg params: String?): String {
            var data = ""
            try {
                data = downloadUrl(params[0]!!)
            } catch (e: Exception) {
                Logger.d("Background Task", e.toString())
            }
            return data
        }
    }

    /*calling ParserTask for parsing direction api*/
    private inner class ParserTask :
        AsyncTask<String?, Int?, List<List<HashMap<String, String>>>?>() {
        // Parsing the data in non-ui thread

        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
            var points: ArrayList<*>? = null
            var lineOptions: PolylineOptions? = null
            val markerOptions = MarkerOptions()
            if (result != null) {
                for (i in result.indices) {
                    points = ArrayList<LatLng>()
                    lineOptions = PolylineOptions()
                    val path = result[i]
                    for (j in path.indices) {
                        val point = path[j]
                        val lat = point["lat"]!!.toDouble()
                        val lng = point["lng"]!!.toDouble()
                        val position = LatLng(lat, lng)
                        points.add(position)
                    }
                    lineOptions.addAll(points)
                    lineOptions.width(12f)
                    lineOptions.color(Color.parseColor("#1866D1"))
                    lineOptions.geodesic(true)
                }

                // Drawing polyline in the Google Map for the i-th route
                try {
                    mMap!!.addPolyline(lineOptions!!)
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                    // Toast.makeText(getActivity().getApplicationContext(), "Boundary Crossed!!", Toast.LENGTH_SHORT).show();
                }
            }
        }

        override fun doInBackground(vararg params: String?): List<List<HashMap<String, String>>>? {
            Logger.i(TAG, "ParserTask")
            val jObject: JSONObject
            var routes: List<List<HashMap<String, String>>>? = null
            try {
                jObject = JSONObject(params[0])
                val parser = DirectionsJSONParser()
                routes = parser.parse(jObject)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
            return routes
        }
    }

    /*calling getdistanceVal*/
    private inner class getdistanceVal(
        var sourcelat: Double,
        var sourcelong: Double,
        var destlat: Double,
        var destlong: Double
    ) : AsyncTask<Void?, Void?, Void?>() {
        override fun doInBackground(vararg params: Void?): Void? {
            Logger.i(TAG, "getdistanceVal")
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
//            distanceroad = getDistanceOnRoad(sourcelat, sourcelong, destlat, destlong);
            return null
        }

        override fun onPreExecute() {
            super.onPreExecute()
        }


        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            //            Logger.e("distroad", distanceroad);
//            //this method will be running on UI thread
//            tv_distance.setText(distanceroad);
//            tv_time.setText(duration);
//            try {
//                if (maneuver.equalsIgnoreCase("turn-left")) {
//                    ivDirection.setImageResource(R.drawable.ic_turn_left);
//                    ivarrow.setImageResource(R.drawable.ic_turn_left);
//                } else if (maneuver.equalsIgnoreCase("turn-right")) {
//                    ivDirection.setImageResource(R.drawable.ic_arrow_right);
//                    ivarrow.setImageResource(R.drawable.ic_arrow_right);
//                } else {
//                    ivDirection.setImageResource(R.drawable.ic_keep_moving);
//                    ivarrow.setImageResource(R  .drawable.ic_keep_moving);
//                }
//            } catch (Exception e) {
//                  CommonUtils.printStackTrace(e);
//            }
//
//            tv_nextturnname.setText(maneuver);
//            iv_shortdist.setText(firstdistance);
//            Toast.makeText(getActivity().getApplicationContext(),"Called Dist",Toast.LENGTH_SHORT).show();
//            pdLoading.dismiss();
        }
    }

    /*calling getDirectionsUrl*/
    private fun getDirectionsUrl(
        origin: LatLng?,
        dest: LatLng?
    ): String {
        Logger.i(TAG, "getDirectionsUrl")
        // Origin of route
        val str_origin = "origin=" + origin!!.latitude + "," + origin.longitude

        // Destination of route
        val str_dest = "destination=" + dest!!.latitude + "," + dest.longitude

        // Sensor enabled
        val sensor = "sensor=false"
        var mode = "mode=driving"
        if (mSharedPreference == null) {
            mSharedPreference = SharedPreference(context)
        }
        val transport_mode: String = mSharedPreference!!.getStoredPreference(
            context,
            SharedPreferencesEnum.TRANSPORT_MODE.preferenceKey,
            "0"
        ).toString()
        when (transport_mode) {
            "0" -> mode = "mode=driving"
            "1" -> mode = "mode=walking"
            "2" -> mode = "mode=bicycling"
        }
        // Building the parameters to the web service
        val key =
            "key=" + CommonUtils.googlemapapikey(context)
        //        String key = "key=" + getResources().getString(R.string.google_maps_key);
        val parameters = "$str_origin&$str_dest&$sensor&$mode&$key"

        // Output format
        val output = "json"

        // Building the url to the web service
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
    }

    /*calling shareMessage*/
    fun shareMessage() {
        try {
            Logger.i(TAG, "shareMessage")
            var message = ""
            message = if (fromSearch) {
                searchedWoloo?.name + "\n" + searchedWoloo?.address + "\n" + CommonUtils.authconfig_response(
                    context
                )!!.getuRLS()!!.app_share_url
            } else {
                """
                    ${nearByWoloo!!.name}
                    ${nearByWoloo!!.address}
                    
                    """.trimIndent() + CommonUtils.authconfig_response(
                    context
                )!!.getuRLS()!!.app_share_url
            }
            val share = Intent(Intent.ACTION_SEND)
            share.setType("text/plain")
            share.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(Intent.createChooser(share, "Woloo Share"))

//            Dialog dialog=new Dialog(getContext());
//            CommonUtils.calldeeplink(getContext(),dialog,"Woloo Share",message, CommonUtils.authconfig_response(getContext()).getData().getuRLS().getApp_share_url(),"https://woloo.page.link/share");
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    companion object {
        var TAG = HomeDetailsFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(fromSearch: Boolean): HomeDetailsFragment {
            val fragment = HomeDetailsFragment()
            val args = Bundle().apply {
                putBoolean(AppConstants.FROM_SEARCH, fromSearch)
            }
            fragment.arguments = args
            return fragment
        }
    }

    sealed class MarkerData {
        data class MarkerItem(val marker: Marker) : MarkerData()
        data class LatLngItem(val latLng: LatLng) : MarkerData()
    }

}