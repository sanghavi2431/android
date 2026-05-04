package `in`.woloo.www.more.my_history.adapter

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonObject
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.interfaces.DirectionsApiService
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.EnrouteDirectionActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard.Companion.mSharedPreference
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.more.my_history.model.MyHistoryResponse
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.ImageUtil
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyHistoryAdapter(
    private val context: Context,
    private val historyList: List<MyHistoryResponse.History> ,
    private val lat: Double,
    private val lng: Double,
    private val lifecycleScope: CoroutineScope,
    private val searchLocation: String,
) : RecyclerView.Adapter<MyHistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View =
            layoutInflater.inflate(R.layout.my_history_item_new, parent, false) //woloo_search_item
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(historyList[position])
        holder.tvRequiredTime!!.visibility = View.GONE
        holder.tvDistance!!.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /*   @BindView(R.id.tvPremium)
        TextView tvPremium;*/
        @JvmField
        @BindView(R.id.ivWolooStore)
        var ivWolooStore: ImageView? = null

        @JvmField
        @BindView(R.id.tvWolooStoreName)
        var tvWolooStoreName: TextView? = null

        @JvmField
        @BindView(R.id.tvRequiredTime)
        var tvRequiredTime: TextView? = null

        @JvmField
        @BindView(R.id.bottom_host_limit)
        var bottom_host_limit: TextView? = null

        /* @BindView(R.id.tvRating)
        TextView tvRating;*/
        /*  @BindView(R.id.ivToilet)
        ImageView ivToilet;

        @BindView(R.id.ivWheelChair)
        ImageView ivWheelChair;

        @BindView(R.id.ivFeedingRoom)
        ImageView ivFeedingRoom;

        @BindView(R.id.ivSanitizer)
        ImageView ivSanitizer;

        @BindView(R.id.ivCoffee)
        ImageView ivCoffee;

        @BindView(R.id.ivMakeupRoom)
        ImageView ivMakeupRoom;

        @BindView(R.id.ivSanitaryPads)
        ImageView ivSanitaryPads;

        @BindView(R.id.ivCovidFree)
        ImageView ivCovidFree;

        @BindView(R.id.ivCleanHygiene)
        ImageView ivCleanHygiene;

        @BindView(R.id.ivSafeSpace)
        ImageView ivSafeSpace;
*/
        @JvmField
        @BindView(R.id.tvAddress)
        var tvAddress: TextView? = null

        @JvmField
        @BindView(R.id.tvDistance)
        var tvDistance: TextView? = null

        @JvmField
        @BindView(R.id.navigate_layout)
        var navigateLayout: LinearLayout? = null

        /*  @BindView(R.id.tvEarnedPoints)
        TextView tvEarnedPoints;*/
        /*   @BindView(R.id.tvRateThisPlace)
        TextView tvRateThisPlace;*/
        init {
            ButterKnife.bind(this, itemView)
        }

        fun setData(history: MyHistoryResponse.History?) {
            try {
                if (history != null) {
                    Logger.d("Aarati", "history is " + history.id)
                    /*  if(history.getWolooDetails().getIsPremium() == null && history.getWolooDetails().getIsPremium() == 1){
                        tvPremium.setVisibility(View.VISIBLE);
                    }else{
                        tvPremium.setVisibility(View.GONE);
                    }*/if (history.wolooDetails!!.image != null && !history.wolooDetails!!.image!!.isEmpty()) {
                        var wolooImg = BuildConfig.NODE_API_URL + history?.wolooDetails?.image?.get(0)
                        Log.d("img url is" , wolooImg)
                        Glide.with(context)
                            .load(wolooImg)
                            .apply(RequestOptions.bitmapTransform(RoundedCorners(27)))
                            .into(ivWolooStore!!)
                        //  ImageUtil.loadImageHistory(context,ivWolooStore, BuildConfig.BASE_URL+ AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+history.getWolooDetails().getImage().get(0));
                    } else {
                        val imgUrl =
                            BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW
                        // ImageUtil.loadImageHistory(context,ivWolooStore,imgUrl);
                    }
                    /*String imgUrl = BuildConfig.BASE_URL+AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW;
                    ImageUtil.loadImage(context,ivWolooStore,imgUrl);*/if (!TextUtils.isEmpty(
                            history.wolooDetails!!.name
                        )
                    ) {
                        tvWolooStoreName!!.text = history.wolooDetails!!.name
                    }
                    if (!TextUtils.isEmpty(history.wolooDetails!!.address)) {
                        tvAddress!!.text = history.wolooDetails!!.address
                    }

                    bottom_host_limit!!.setText(history.wolooDetails!!.cibilScore)
                    bottom_host_limit!!.backgroundTintList =
                        ColorStateList.valueOf(Color.parseColor(history.wolooDetails!!.cibilScoreColor))


                    lifecycleScope.launch {
                        val (distance, duration) = getDistanceAndTime(
                            lat,
                            lng,
                            history.wolooDetails!!.lat!!.toDouble(),
                            history.wolooDetails!!.lng!!.toDouble()
                        )
                        Log.d("DIST TIME Aarati", "distanceTime")
                        tvDistance!!.setText(distance)
                        tvRequiredTime!!.setText(duration)

                    }




                    /*  if(!TextUtils.isEmpty(history.getWolooDetails().getUserRating())){
                        tvRating.setText(history.getWolooDetails().getUserRating());
                    }*/

                    /*
                   if(history.getWolooDetails().getDistance() != null){
                        tvDistance.setText(""+ CommonUtils.getDistace(data.getDistance()));
                    }

                    if(data.getDuration() != null){
                        tvRequiredTime.setText(""+CommonUtils.getTimeForWolooStoreInfo(data.getDuration()));
                    }

                    tvRating.setText(""+data.getUserRating());*/

                    /*  if (history.getWolooDetails().getIsWashroom() != null && history.getWolooDetails().getIsWashroom() == 1) {
                        ivToilet.setVisibility(View.VISIBLE);
                    } else {
                        ivToilet.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getIsWheelchairAccessible() != null && history.getWolooDetails().getIsWheelchairAccessible() == 1){
                        ivWheelChair.setVisibility(View.VISIBLE);
                    }else{
                        ivWheelChair.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getIsFeedingRoom() != null &&  history.getWolooDetails().getIsFeedingRoom() == 1){
                        ivFeedingRoom.setVisibility(View.VISIBLE);
                    }else{
                        ivFeedingRoom.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getIsSanitizerAvailable() != null && history.getWolooDetails().getIsSanitizerAvailable() == 1){
                        ivSanitizer.setVisibility(View.VISIBLE);
                    }else{
                        ivSanitizer.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getIsCoffeeAvailable() != null && history.getWolooDetails().getIsCoffeeAvailable() == 1){
                        ivCoffee.setVisibility(View.VISIBLE);
                    }else{
                        ivCoffee.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getIsMakeupRoomAvailable() != null && history.getWolooDetails().getIsMakeupRoomAvailable() == 1){
                        ivMakeupRoom.setVisibility(View.VISIBLE);
                    }else{
                        ivMakeupRoom.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getIsSanitaryPadsAvailable() != null && history.getWolooDetails().getIsSanitaryPadsAvailable() == 1){
                        ivSanitaryPads.setVisibility(View.VISIBLE);
                    }else{
                        ivSanitaryPads.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getIsCovidFree() != null && history.getWolooDetails().getIsCovidFree() == 1){
                        ivCovidFree.setVisibility(View.VISIBLE);
                    }else{
                        ivCovidFree.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getIsSafeSpace() != null && history.getWolooDetails().getIsSafeSpace() == 1){
                        ivSafeSpace.setVisibility(View.VISIBLE);
                    }else{
                        ivSafeSpace.setVisibility(View.GONE);
                    }

                    if(history.getWolooDetails().getIsCleanAndHygiene() != null && history.getWolooDetails().getIsCleanAndHygiene() == 1){
                        ivCleanHygiene.setVisibility(View.VISIBLE);
                    }else{
                        ivCleanHygiene.setVisibility(View.GONE);
                    }
*/
                    /*   if(!TextUtils.isEmpty(history.getAmount())){
                        tvEarnedPoints.setVisibility(View.VISIBLE);
                        tvEarnedPoints.setText("You have earned "+history.getAmount()+" points");
                    }else{
                        tvEarnedPoints.setVisibility(View.GONE);
                    }

                    if(history.getIsReviewPending()==1)
                        tvRateThisPlace.setVisibility(View.VISIBLE);
                    else
                        tvRateThisPlace.setVisibility(View.GONE);
                    tvRateThisPlace.setOnClickListener(v -> {
                        try{
                            Intent intent = new Intent(context, AddReviewActivity.class);
                            intent.putExtra(AppConstants.WOLOO_ID,history.getWolooId());
                            context.startActivity(intent);
                        }catch (Exception ex){
                             CommonUtils.printStackTrace(ex);
                        }
                    });*/navigateLayout!!.setOnClickListener(object : View.OnClickListener {
                        override fun onClick(v: View) {
                            /* if(history.getWolooDetails().get().equals("-"))
                            {
                                CommonUtils.showCustomDialog(context,"No route found for the transport mode. Please change mode and try again");
                            }
                            else*/
                            run {
                                val params = Bundle()
                                params.putString(
                                    AppConstants.WOLOO_NAME,
                                    history.wolooDetails!!.id.toString()
                                )
                                logFirebaseEvent(
                                    context,
                                    params,
                                    AppConstants.DIRECTION_WOLOO_EVENT
                                )
                                val payload = HashMap<String, Any>()
                                payload[AppConstants.WOLOO_NAME] =
                                    history.wolooDetails!!.id.toString()
                                logNetcoreEvent(
                                    context,
                                    payload,
                                    AppConstants.DIRECTION_WOLOO_EVENT
                                )
                                val i = Intent(context, EnrouteDirectionActivity::class.java)
                                i.putExtra("destlat", history.wolooDetails!!.lat)
                                i.putExtra("destlong", history.wolooDetails!!.lng)
                                i.putExtra("wolooId", history.wolooDetails!!.id)
                                i.putExtra("wolooName", history.wolooDetails!!.name)
                                i.putExtra("wolooAddress", history.wolooDetails!!.address)
                                i.putExtra(AppConstants.searchLocationToHost, searchLocation)
                                i.putExtra(AppConstants.timeToHost, "")
                                i.putExtra(AppConstants.distanceToHost, "")
                                i.putExtra("tag", "direction")
                                i.putExtra("tag_new_navigate", "history")
                                context.startActivity(i)
                            }
                        }
                    })
                }
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }
    }

 suspend fun getDistanceAndTime(
        sourceLat: Double,
        sourceLng: Double,
        destLat: Double,
        destLng: Double
    ): Pair<String, String>  {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(DirectionsApiService::class.java)

        val origin = "$sourceLat,$sourceLng"
        val destination = "$destLat,$destLng"

        val transport_mode = mSharedPreference!!.getStoredPreference(
            context,
            SharedPreferencesEnum.TRANSPORT_MODE.preferenceKey,
            "0"
        )

        val mode = when (transport_mode) {
            "0" -> "driving"
            "3" -> "driving"
            "1" -> "walking"
            else -> "driving"
        }
        val key = CommonUtils.googlemapapikey(context)
     val response: Response<JsonObject>
     if(mode.equals("walking") ) {
         response =
             service.getDirections(origin, destination, key, mode , "")
     }
     else {

         response = service.getDirections(
             origin = origin,
             destination = destination,
             apiKey = key,
             mode = mode,
             departureTime = "now"
         )
     }

        if (response.isSuccessful) {
            val jsonResponse = response.body()
            Log.d("DirectionsApi Aarati", "${service.toString()} Raw response: $jsonResponse")

            // Check if the response is valid
            if (jsonResponse != null) {
                val routes = jsonResponse.getAsJsonArray("routes")
                Log.d("DirectionsApi Aarati", "Routes size: ${routes?.size()}")
                if (routes != null && routes.size() > 0) {
                    val legs = routes[0].asJsonObject.getAsJsonArray("legs")
                    val leg = legs[0].asJsonObject
                    Log.d("DirectionsApi Aarati", "Legs size: ${legs?.size()}")

                    // Get distance and duration from the JSON response
                    val distance = leg.getAsJsonObject("distance").getAsJsonPrimitive("text").asString
                 //   val duration = leg.getAsJsonObject("duration").getAsJsonPrimitive("text").asString

                    val duration = if (mode == "driving" && leg.has("duration_in_traffic")) {
                        leg.getAsJsonObject("duration_in_traffic")
                            .getAsJsonPrimitive("text").asString
                    } else {
                        leg.getAsJsonObject("duration")
                            .getAsJsonPrimitive("text").asString
                    }

                    Log.d("DirectionsApi Aarati", "Distance: $distance, Duration: $duration")


                    return Pair(distance, duration)
                }
            }

            return Pair("0", "0")
        } else {
            return Pair("Error: ${response.errorBody()?.string()}" , "")
        }
    }


}
