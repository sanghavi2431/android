package `in`.woloo.www.application_kotlin.adapters.loo_discovery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.mapdirection.GetDistance
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.EqualSpacingItemDecoration
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.EnrouteDirectionActivity
import `in`.woloo.www.application_kotlin.model.server_request.WolooEngagementRequest
import `in`.woloo.www.application_kotlin.view_models.HomeViewModel
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets

class WolooStoreAdapter(
    private val context: Context,
    private val dataList: List<NearByStoreResponse.Data>?,
    homeViewModel: HomeViewModel
) : RecyclerView.Adapter<WolooStoreAdapter.ViewHolder>() {
    var TAG = "WolooStoreAdapter"
    private var buttonClick = 0
    private val homeViewModel: HomeViewModel
    private val distanceroad: String? = null
    private val getdistance: GetDistance? = null
    private val duration: String? = null

    //    GpsTracker gpsTracker ;
    var wolooSelectedIndex = -1
        private set
    var wolooEngagementRequest: WolooEngagementRequest = WolooEngagementRequest()

    init {
        this.homeViewModel = homeViewModel
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View =
            layoutInflater.inflate(R.layout.woloo_store_images_items, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Logger.i("onBindViewHolder", "" + position)
        holder.setStoreData(dataList!![position], position)
    }

    override fun getItemCount(): Int {
        return if (!dataList.isNullOrEmpty()) {
            dataList.size
        } else 0
    }



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.tv_name)
        var tv_name: TextView? = null

        @JvmField
        @BindView(R.id.txt_address)
        var txt_address: TextView? = null

        @JvmField
        @BindView(R.id.tv_direction)
        var tv_direction: TextView? = null

        @JvmField
        @BindView(R.id.tv_start)
        var tv_start: TextView? = null

        @JvmField
        @BindView(R.id.tv_distance)
        var tv_distance: TextView? = null

        @JvmField
        @BindView(R.id.tv_time)
        var tv_time: TextView? = null

        @JvmField
        @BindView(R.id.tv_like)
        var tv_like: TextView? = null

        @JvmField
        @BindView(R.id.tvShare)
        var tvShare: TextView? = null

        @JvmField
        @BindView(R.id.ivTransportMode)
        var ivTransportMode: ImageView? = null

        @JvmField
        @BindView(R.id.rv_store_image)
        var rv_store_image: RecyclerView? = null

        @JvmField
        @BindView(R.id.ll_bottom)
        var ll_bottom: LinearLayout? = null

        @JvmField
        @BindView(R.id.ivToilet)
        var ivToilet: ImageView? = null

        @JvmField
        @BindView(R.id.ivWheelChair)
        var ivWheelChair: ImageView? = null

        @JvmField
        @BindView(R.id.ivFeedingRoom)
        var ivFeedingRoom: ImageView? = null

        @JvmField
        @BindView(R.id.ivSanitizer)
        var ivSanitizer: ImageView? = null

        @JvmField
        @BindView(R.id.ivCoffee)
        var ivCoffee: ImageView? = null

        @JvmField
        @BindView(R.id.ivMakeupRoom)
        var ivMakeupRoom: ImageView? = null

        @JvmField
        @BindView(R.id.ivSanitaryPads)
        var ivSanitaryPads: ImageView? = null

        @JvmField
        @BindView(R.id.ivCovidFree)
        var ivCovidFree: ImageView? = null

        @JvmField
        @BindView(R.id.ivSafeSpace)
        var ivSafeSpace: ImageView? = null

        @JvmField
        @BindView(R.id.ivCleanHygiene)
        var ivCleanHygiene: ImageView? = null

        @JvmField
        @BindView(R.id.ivSegregatedWashroom)
        var ivSegregatedWashroom: ImageView? = null

        @JvmField
        @BindView(R.id.cibil_image)
        var cibilImage: ImageView? = null

        @JvmField
        @BindView(R.id.cibil_layout)
        var cibilLayout: View? = null

        /*
        @BindViews({R.id.iv_covid_free, R.id.ivCleanHygiene, R.id.iv_mom_feeding_baby, R.id.iv_hand_sanitizer, R.id.iv_coffee, R.id.iv_makeup, R.id.iv_diaper})
        List<ImageView> icon_views;
*/
        init {
            ButterKnife.bind(this, itemView)
        }

        /*calling setLike*/
        fun setLike(tv_like: TextView?) {
            Logger.i(TAG, "setLike")
            try {
                tv_like!!.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.details_layer_list_liked,
                    0,
                    0,
                    0
                )
                tv_like.background = ContextCompat.getDrawable(context, R.drawable.yellow_rectangle)
                tv_like.setTextColor(ContextCompat.getColor(context, R.color.text_color_five))
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }

        /*calling setDislike*/
        fun setDislike(tv_like: TextView?) {
            Logger.i(TAG, "setDislike")
            try {
                tv_like!!.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.details_like_layer_list,
                    0,
                    0,
                    0
                )
                tv_like.background =
                    ContextCompat.getDrawable(context, R.drawable.transparent_rectangle)
                tv_like.setTextColor(ContextCompat.getColor(context, R.color.white))
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }

        fun setStoreData(data: NearByStoreResponse.Data, position: Int) {
            try {
                tv_name!!.text = data.name
                txt_address!!.text = data.address
                if (data.cibilScoreImage.isEmpty()) {
                    cibilLayout!!.visibility = View.GONE
                } else {
                    cibilLayout!!.visibility = View.VISIBLE
                    Glide.with(context)
                        .load(data.cibilScoreImage)
                        .into(cibilImage!!)
                }
                if (data.isLiked == 0) {
                    setDislike(tv_like)
                } else {
                    setLike(tv_like)
                }
                var image: String? = ""
                val stringList: MutableList<String> = ArrayList()
                if (data.offer != null) {
                    image = data.offer!!.image
                    if (!TextUtils.isEmpty(image)) {
                        val wolooOfferImage =
                            BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + image
                        stringList.add(wolooOfferImage)
                    } else if (data.image.size > 0) {
                        val wolooImage =
                            BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + data.image[0]
                        stringList.add(wolooImage)
                    } else {
                        val imgUrl =
                            BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE
                        stringList.add(imgUrl)
                    }
                    val adapter = WolooStoreImagesAdapter(context, stringList)
                    val layoutManager = LinearLayoutManager(
                        context, RecyclerView.HORIZONTAL, false
                    )
                    rv_store_image!!.layoutManager = layoutManager
                    rv_store_image!!.adapter = adapter
                } else { /*if (data.getImage().size()>0) {
                    String wolooImage = BuildConfig.BASE_URL+ AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+data.getImage().get(0);
                    stringList.add(wolooImage);
                }else{
                    String imgUrl = BuildConfig.BASE_URL+AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE;
                    stringList.add(imgUrl);
                }*/
                    setImageData(data.image, data.baseUrl)
                }
                val mSharedPreference = SharedPreference(context)
                val transport_mode: String = mSharedPreference.getStoredPreference(
                    context,
                    SharedPreferencesEnum.TRANSPORT_MODE.preferenceKey,
                    "0"
                ).toString()
                when (transport_mode) {
                    "0" -> ivTransportMode!!.setImageResource(R.drawable.ic_car)
                    "1" -> ivTransportMode!!.setImageResource(R.drawable.ic_walking_transport_mode)
                    "2" -> ivTransportMode!!.setImageResource(R.drawable.ic_bicycle_transport_mode)
                }
                tv_direction!!.setOnClickListener {
                    if (data.distance == "-") {
                        CommonUtils.showCustomDialog(
                            context,
                            "No route found for the transport mode. Please change mode and try again"
                        )
                    } else {
                        val params = Bundle()
                        params.putString(AppConstants.WOLOO_NAME, data.id.toString())
                        Utility.logFirebaseEvent(
                            context,
                            params,
                            AppConstants.DIRECTION_WOLOO_EVENT
                        )
                        val payload = HashMap<String, Any>()
                        payload[AppConstants.WOLOO_NAME] = data.id.toString()
                        Utility.logNetcoreEvent(
                            context,
                            payload,
                            AppConstants.DIRECTION_WOLOO_EVENT
                        )
                        val i = Intent(context, EnrouteDirectionActivity::class.java)
                        i.putExtra("destlat", data.lat)
                        i.putExtra("destlong", data.lng)
                        i.putExtra("wolooId", data.id)
                        i.putExtra("tag", "direction")
                        i.putExtra("wolooName", data.name)
                        i.putExtra("wolooAddress", data.address)
                        context.startActivity(i)
                        //                            String lat = data.getLat(),lng = data.getLng(), mode = "";
//
//
//                            String transport_mode = mSharedPreference.getStoredPreference(context, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");
//                            switch (transport_mode) {
//                                case "0":
//                                    mode = "d";
//                                    break;
//                                case "1":
//                                    mode = "w";
//                                    break;
//                                case "2":
//                                    mode = "l"; //b for bycycler & l for two wheeler
//                                    break;
//                            }
//                            // Create a Uri from an intent string. Use the result to create an Intent.
//                            String request = "google.navigation:q="+lat+","+lng+"&mode="+mode;
//                            Uri mapIntentUri = Uri.parse(request);
//// Create an Intent from mapIntentUri. Set the action to ACTION_VIEW
//                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapIntentUri);
//// Make the Intent explicit by setting the Google Maps package
//                            mapIntent.setPackage("com.google.android.apps.maps");
//// Attempt to start an activity that can handle the Intent
//                            context.startActivity(mapIntent);
                    }
                }


//                ll_bottom.setVisibility(View.INVISIBLE);
                tv_start!!.setOnClickListener {
                    val i = Intent(context, EnrouteDirectionActivity::class.java)
                    i.putExtra("destlat", data.lat)
                    i.putExtra("destlong", data.lng)
                    i.putExtra("wolooId", data.id)
                    i.putExtra("tag", "direction")
                    i.putExtra("wolooName", data.name)
                    i.putExtra("wolooAddress", data.address)
                    context.startActivity(i)
                    //                        String lat = data.getLat(),lng = data.getLng(), mode = "";
//
//
//                        String transport_mode = mSharedPreference.getStoredPreference(context, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0");
//                        switch (transport_mode) {
//                            case "0":
//                                mode = "d";
//                                break;
//                            case "1":
//                                mode = "w";
//                                break;
//                            case "2":
//                                mode = "l"; //b for bycycler & l for two wheeler
//                                break;
//                        }
//                        // Create a Uri from an intent string. Use the result to create an Intent.
//                        String request = "google.navigation:q="+lat+","+lng+"&mode="+mode;
//                        Uri mapIntentUri = Uri.parse(request);
//// Create an Intent from mapIntentUri. Set the action to ACTION_VIEW
//                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapIntentUri);
//// Make the Intent explicit by setting the Google Maps package
//                        mapIntent.setPackage("com.google.android.apps.maps");
//// Attempt to start an activity that can handle the Intent
//                        context.startActivity(mapIntent);
                }
                tv_like!!.setOnClickListener {
                    val params = Bundle()
                    params.putString(AppConstants.WOLOO_NAME, data.id.toString())
                    Utility.logFirebaseEvent(context, params, AppConstants.LIKE_WOLOO_EVENT)
                    val payload = HashMap<String, Any>()
                    payload[AppConstants.WOLOO_NAME] = data.id.toString()
                    Utility.logNetcoreEvent(context, payload, AppConstants.LIKE_WOLOO_EVENT)
                    wolooEngagementRequest.wolooId = dataList!![position].id.toString()
                    val (id) = CommonUtils().userInfo!!
                    wolooEngagementRequest.userId = id.toString()
                    if (buttonClick == 0) {
                        buttonClick = 2
                        try {
                            wolooSelectedIndex = position
                            wolooEngagementRequest.like = 1
                            homeViewModel.wolooEngagement(wolooEngagementRequest)
                            //                                homeDetailsPresenter.like_unlike(dataList.get(position).getId(), APIConstants.WOLOOLIKE, tv_like);
                        } catch (ex: Exception) {
                            CommonUtils.printStackTrace(ex)
                        }
                    } else if (buttonClick == 2) {
                        buttonClick = 0
                        try {
                            wolooSelectedIndex = position
                            wolooEngagementRequest.like = 0
                            homeViewModel.wolooEngagement(wolooEngagementRequest)
                            //                                homeDetailsPresenter.like_unlike(dataList.get(position).getId(), APIConstants.WOLOOUNLIKE, tv_like);
                        } catch (ex: Exception) {
                            CommonUtils.printStackTrace(ex)
                        }
                    }
                }
                //                gpsTracker=new GpsTracker(context);
                tv_distance!!.text = data.distance
                tv_time!!.text = data.duration
                //                if (gpsTracker.canGetLocation()){
//                    try {
//                        new getdistanceVal(gpsTracker.getLatitude(), gpsTracker.getLongitude(), Double.parseDouble(data.getLat()), Double.parseDouble(data.getLng()),tv_distance,tv_time,ll_bottom).execute();
//                    } catch (Exception e) {
//                         CommonUtils.printStackTrace(e);
//                    }
//                }


//                ImageView iv_toilet,iv_physically_disable,iv_mom_feeding_baby,iv_hand_sanitizer,iv_coffee,iv_makeup,iv_diaper;
                /*if (data.getIsSafeSpace().equals(1)) {
                    icon_views.get(0).setVisibility(View.VISIBLE);
                    icon_views.get(1).setVisibility(View.VISIBLE);
                    icon_views.get(2).setVisibility(View.VISIBLE);
                    icon_views.get(3).setVisibility(View.GONE);
                    icon_views.get(4).setVisibility(View.GONE);
                    icon_views.get(5).setVisibility(View.GONE);
                    icon_views.get(6).setVisibility(View.GONE);
                }
                if (data.getIsCovidFree().equals(1)) {
                    icon_views.get(0).setVisibility(View.VISIBLE);
                    icon_views.get(1).setVisibility(View.VISIBLE);
                    icon_views.get(2).setVisibility(View.VISIBLE);
                    icon_views.get(3).setVisibility(View.GONE);
                    icon_views.get(4).setVisibility(View.GONE);
                    icon_views.get(5).setVisibility(View.GONE);
                    icon_views.get(6).setVisibility(View.GONE);
                }
                if (data.getIsCleanAndHygiene().equals(1)) {
                    icon_views.get(0).setVisibility(View.VISIBLE);
                    icon_views.get(1).setVisibility(View.VISIBLE);
                    icon_views.get(2).setVisibility(View.VISIBLE);
                    icon_views.get(3).setVisibility(View.GONE);
                    icon_views.get(4).setVisibility(View.GONE);
                    icon_views.get(5).setVisibility(View.GONE);
                    icon_views.get(6).setVisibility(View.GONE);
                }
*/if (data.isWashroom != null && data.isWashroom == 1) {
                    ivToilet!!.visibility = View.VISIBLE
                    ivToilet!!.setImageResource(R.drawable.ic_toilet)
                } else {
                    ivToilet!!.visibility = View.VISIBLE
                    ivToilet!!.setImageResource(R.drawable.ic_indian_toilet)
                }
                if (data.isWheelchairAccessible != null && data.isWheelchairAccessible == 1) {
                    ivWheelChair!!.visibility = View.VISIBLE
                } else {
                    ivWheelChair!!.visibility = View.GONE
                }
                if (data.isFeedingRoom != null && data.isFeedingRoom == 1) {
                    ivFeedingRoom!!.visibility = View.VISIBLE
                } else {
                    ivFeedingRoom!!.visibility = View.GONE
                }
                if (data.isSanitizerAvailable != null && data.isSanitizerAvailable == 1) {
                    ivSanitizer!!.visibility = View.VISIBLE
                } else {
                    ivSanitizer!!.visibility = View.GONE
                }
                if (data.isCoffeeAvailable != null && data.isCoffeeAvailable == 1) {
                    ivCoffee!!.visibility = View.VISIBLE
                } else {
                    ivCoffee!!.visibility = View.GONE
                }
                if (data.isMakeupRoomAvailable != null && data.isMakeupRoomAvailable == 1) {
                    ivMakeupRoom!!.visibility = View.VISIBLE
                } else {
                    ivMakeupRoom!!.visibility = View.GONE
                }
                if (data.isSanitaryPadsAvailable != null && data.isSanitaryPadsAvailable == 1) {
                    ivSanitaryPads!!.visibility = View.VISIBLE
                } else {
                    ivSanitaryPads!!.visibility = View.GONE
                }
                if (data.isCovidFree != null && data.isCovidFree == 1) {
                    ivCovidFree!!.visibility = View.VISIBLE
                } else {
                    ivCovidFree!!.visibility = View.GONE
                }
                if (data.isSafeSpace != null && data.isSafeSpace == 1) {
                    ivSafeSpace!!.visibility = View.VISIBLE
                } else {
                    ivSafeSpace!!.visibility = View.GONE
                }
                if (data.isCleanAndHygiene != null && data.isCleanAndHygiene == 1) {
                    ivCleanHygiene!!.visibility = View.VISIBLE
                } else {
                    ivCleanHygiene!!.visibility = View.GONE
                }
                if (data.segregated != null && data.segregated.equals("YES", ignoreCase = true)) {
                    ivSegregatedWashroom!!.visibility = View.VISIBLE
                    ivSegregatedWashroom!!.setImageResource(R.drawable.ic_unisex_toilet)
                } else {
                    ivSegregatedWashroom!!.visibility = View.VISIBLE
                    ivSegregatedWashroom!!.setImageResource(R.drawable.ic_separate_toilet)
                }
                tvShare!!.setOnClickListener { v: View? ->
                    val params = Bundle()
                    params.putString(AppConstants.WOLOO_NAME, data.id.toString())
                    Utility.logFirebaseEvent(context, params, AppConstants.SHARE_WOLOO_EVENT)
                    val payload = HashMap<String, Any>()
                    payload[AppConstants.WOLOO_NAME] = data.id.toString()
                    Utility.logNetcoreEvent(context, payload, AppConstants.SHARE_WOLOO_EVENT)
                    shareMessage(data)
                }
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        }

        private fun setImageData(image: List<String?>, baseUrl: String?) {
            var image: List<String?>? = image
            val resultList: List<String> = if (image.isNullOrEmpty()) {
                listOf(BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW)
            } else {
                image.map { img ->
                    when {
                        img.isNullOrEmpty() -> img ?: ""
                        !baseUrl.isNullOrEmpty() && !img.contains(baseUrl) -> baseUrl + img
                        !img.contains(BuildConfig.BASE_URL) -> BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + img
                        else -> img
                    }
                }
            }
            /* if (image == null || image.size == 0) {
                 image = ArrayList()
                 image.add(BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW)
             } else {
                 for (i in image.indices) {
                     if (!TextUtils.isEmpty(baseUrl)) {
                         if (!image[i]!!.contains(baseUrl!!)) {
                             image.set(i, baseUrl + image[i])
                         }
                     } else {
                         if (!image[i]!!.contains(BuildConfig.BASE_URL)) {
                             image.set(
                                 i,
                                 BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + image[i]
                             )
                         }
                     }
                 }
             }*/

            rv_store_image!!.visibility = View.VISIBLE
            val nearByWolooImageAdapter = NearByWolooImageAdapter(context as Activity, image)
            val mLayoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
            rv_store_image!!.layoutManager = mLayoutManager
            rv_store_image!!.adapter = nearByWolooImageAdapter
            rv_store_image!!.isNestedScrollingEnabled = true
            ViewCompat.setNestedScrollingEnabled(rv_store_image!!, true)
            rv_store_image!!.setHasFixedSize(true)
            rv_store_image!!.setItemViewCacheSize(20)
            rv_store_image!!.isDrawingCacheEnabled = true
            rv_store_image!!.addItemDecoration(
                EqualSpacingItemDecoration(
                    dpToPx(2),
                    EqualSpacingItemDecoration.HORIZONTAL
                )
            ) // 16px. In practice, you'll want to use getDimensionPixelSize
            rv_store_image!!.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
            rv_store_image!!.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    val action = e.action
                    when (action) {
                        MotionEvent.ACTION_DOWN -> rv.parent.requestDisallowInterceptTouchEvent(true)
                    }
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
        }

        fun dpToPx(dp: Int): Int {
            val r = context.resources
            return Math.round(
                TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp.toFloat(),
                    r.displayMetrics
                )
            )
        }
    }

    fun shareMessage(data: NearByStoreResponse.Data) {
        try {
            val message = """
     ${data.name}
     ${data.address}
     
     """.trimIndent() + CommonUtils.authconfig_response(
                context
            )!!.getuRLS()!!.app_share_url
            val share = Intent(Intent.ACTION_SEND)
            share.setType("text/plain")
            share.putExtra(Intent.EXTRA_TEXT, message)
            context.startActivity(Intent.createChooser(share, "Woloo Share"))

//            Dialog dialog=new Dialog(context);
//            CommonUtils.calldeeplink(context,dialog,"","",CommonUtils.authconfig_response(context).getData().getuRLS().getApp_share_url(),"https://woloo.page.link/share");
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    companion object {
        const val DEFAULT_BUFFER_SIZE = 8192

        //    private class getdistanceVal extends AsyncTask<Void, Void, Void> {
        //        double sourcelat, sourcelong, destlat, destlong;
        //        TextView tv_distance;
        //        TextView tv_time;
        //        LinearLayout ll_bottom;
        //
        //        private getdistanceVal(double curlat, double curlon, double latitude, double longitude, TextView tv_distance, TextView tv_time, LinearLayout ll_bottom) {
        //            this.sourcelat = curlat;
        //            this.sourcelong = curlon;
        //            this.destlat = latitude;
        //            this.destlong = longitude;
        //            this.tv_distance=tv_distance;
        //            this.tv_time=tv_time;
        //            this.ll_bottom=ll_bottom;
        //        }
        //
        //        @Override
        //        protected void onPreExecute() {
        //            super.onPreExecute();
        //
        //        }
        //
        //        @Override
        //        protected Void doInBackground(Void... params) {
        //                distanceroad = getDistanceOnRoad(sourcelat, sourcelong, destlat, destlong);
        //            return null;
        //        }
        //
        //        @Override
        //        protected void onPostExecute(Void result) {
        //            super.onPostExecute(result);
        //            try {
        //                tv_distance.setText("" + distanceroad);
        //                tv_time.setText("" +duration);
        //                ll_bottom.setVisibility(View.VISIBLE);
        //            } catch (Exception e) {
        //                  CommonUtils.printStackTrace(e);
        //            }
        //        }
        //
        //
        //    }
        //
        //    private String getDistanceOnRoad(double latitude, double longitude,
        //                                     double prelatitute, double prelongitude) {
        //        String result_in_kms = "";
        //        String key = "key=" + context.getResources().getString(R.string.google_maps_key);
        //        String url = "https://maps.google.com/maps/api/directions/json?origin="
        //                + latitude + "," + longitude + "&destination=" + prelatitute
        //                + "," + prelongitude + "&sensor=false&units=metric" + "&" + key;
        //        String tag[] = {"text"};
        //        HttpResponse response = null;
        //        try {
        //            HttpClient httpClient = new DefaultHttpClient();
        //            HttpContext localContext = new BasicHttpContext();
        //            HttpPost httpPost = new HttpPost(url);
        //            response = httpClient.execute(httpPost, localContext);
        //            InputStream is = response.getEntity().getContent();
        //            String result = convertInputStreamToString(is);
        //            Logger.e("resultIS", result);
        //            Gson gson = new Gson();
        //            getdistance = gson.fromJson(result.toString(), GetDistance.class);
        //            duration = getdistance.getRoutes().get(0).getLegs().get(0).getDuration().getText();
        //            result_in_kms = getdistance.getRoutes().get(0).getLegs().get(0).getDistance().getText();
        //
        //        } catch (Exception e) {
        //              CommonUtils.printStackTrace(e);
        //        }
        //        return result_in_kms;
        //    }
        @Throws(IOException::class)
        private fun convertInputStreamToString(`is`: InputStream): String {
            val result = ByteArrayOutputStream()
            val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
            var length: Int
            while (`is`.read(buffer).also { length = it } != -1) {
                result.write(buffer, 0, length)
            }

            // Java 1.1
            return result.toString(StandardCharsets.UTF_8.name())

            // Java 10
            // return result.toString(StandardCharsets.UTF_8);
        }
    }
}
