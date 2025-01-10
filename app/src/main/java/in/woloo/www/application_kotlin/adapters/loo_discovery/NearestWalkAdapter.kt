package `in`.woloo.www.application_kotlin.adapters.loo_discovery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.HomeDetailsActivity
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Utility
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.EnrouteDirectionActivity

class NearestWalkAdapter(
    private val context: Context,
    private val nearByStoreResponseList: List<NearByStoreResponse.Data>
) : RecyclerView.Adapter<NearestWalkAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View =
            layoutInflater.inflate(R.layout.home_category_fragment_adapter_item, parent, false) //nearest_walk
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nearByStore = nearByStoreResponseList[position]
        holder.setData(nearByStore, context)
    }

    override fun getItemCount(): Int {
        return nearByStoreResponseList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @JvmField
        @BindView(R.id.bottom_host_name)
        var tvName: TextView? = null

        @JvmField
        @BindView(R.id.bottom_host_address)
        var tvAddress: TextView? = null

        @JvmField
        @BindView(R.id.tvDistance)
        var tvDistance: TextView? = null

        @JvmField
        @BindView(R.id.bottom_host_image)
        var tvImg:ImageView? = null

        @JvmField
        @BindView(R.id.directionShow)
        var tv_direction: ImageView? = null

        @JvmField
        @BindView(R.id.rlParentLayout)
        var rlParentLayout: RelativeLayout? = null

        @JvmField
        @BindView(R.id.tvRequiredTime)
        var tvRequiredTime: TextView? = null

     /*   @JvmField
        @BindView(R.id.offers_available)
        var tvofferesAvailable: TextView? = null*/

        @JvmField
        @BindView(R.id.offers_available)
        var offersAvailable: TextView? = null

/*
        @JvmField
        @BindView(R.id.ivToilet)
        var ivToilet: ImageView? = null

        @JvmField
        @BindView(R.id.ivCovidFree)
        var ivCovidFree: ImageView? = null

        @JvmField
        @BindView(R.id.ivCleanHygiene)
        var ivCleanHygiene: ImageView? = null

        @JvmField
        @BindView(R.id.ivSafeSpace)
        var ivSafeSpace: ImageView? = null

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
        @BindView(R.id.ivTransportMode)
        var ivTransportMode: ImageView? = null

        @JvmField
        @BindView(R.id.tvCibilScore)
        var tvCibilScore: TextView? = null

        @JvmField
        @BindView(R.id.tvCibilTitle)
        var tvCibilTitle: TextView? = null

        @JvmField
        @BindView(R.id.tvCibilScoreCV)
        var tvCibilScoreCV: CardView? = null*/
        protected var mSharedPreference: SharedPreference? = null
        private var selectedTravelMode = "car"

        init {
            ButterKnife.bind(this, itemView)
        }

        fun setData(nearByStore: NearByStoreResponse.Data, context: Context) {
            try {
                tvName!!.text = nearByStore.name
                tvAddress!!.text = nearByStore.address
                Log.d("Distance is - " , nearByStore.distance.toString())
                tvDistance!!.text = nearByStore.distance.toString()
                var wolooImg = BuildConfig.NODE_API_URL + nearByStore.image[0]
                Log.d("img url is" , wolooImg)
                Glide.with(context)
                    .load(wolooImg)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(27)))
                    .into(tvImg!!)

                tvRequiredTime!!.text = nearByStore.duration
                if(nearByStore.isOfferAvailable!!.matches(Regex("0")))
                {
                    offersAvailable!!.visibility = View.GONE
                }
                else {
                    offersAvailable!!.visibility = View.VISIBLE
                }
               /* if (nearByStore.cibilScore == "0" || nearByStore.cibilScoreColour.isEmpty()) {
                    tvCibilScoreCV!!.visibility = View.GONE
                    tvCibilTitle!!.visibility = View.GONE
                } else {
                    tvCibilScoreCV!!.visibility = View.VISIBLE
                    tvCibilTitle!!.visibility = View.VISIBLE
                    tvCibilScore!!.text = nearByStore.cibilScore
                    tvCibilScoreCV!!.setCardBackgroundColor(Color.parseColor(nearByStore.cibilScoreColour))
                }*/
                tv_direction!!.setOnClickListener {
                    if (nearByStore.distance == "-") {
                        CommonUtils.showCustomDialog(
                            context,
                            "No route found for the transport mode. Please change mode and try again"
                        )
                    }
                    else {
                        val params = Bundle()
                        params.putString(AppConstants.WOLOO_NAME, nearByStore.id.toString())
                        Utility.logFirebaseEvent(
                            context,
                            params,
                            AppConstants.DIRECTION_WOLOO_EVENT
                        )
                        val payload = HashMap<String, Any>()
                        payload[AppConstants.WOLOO_NAME] = nearByStore.id.toString()
                        Utility.logNetcoreEvent(
                            context,
                            payload,
                            AppConstants.DIRECTION_WOLOO_EVENT
                        )




                        val i = Intent(context, EnrouteDirectionActivity::class.java)
                        i.putExtra("destlat", nearByStore.lat)
                        i.putExtra("destlong", nearByStore.lng)
                        i.putExtra("wolooId", nearByStore.id)
                        i.putExtra("wolooName", nearByStore.name)
                        i.putExtra("wolooAddress", nearByStore.address)
                        i.putExtra("tag", "direction")
                        context.startActivity(i)
                    }
                }

             /*   if(nearByStore.offer == "")
                {

                }
                else
                {

                }*/

            /*    tvofferesAvailable!!.setOnClickListener {

                }*/

               /* if (mSharedPreference == null) {
                    mSharedPreference = SharedPreference(context)
                }
                val transport_mode: String = mSharedPreference?.getStoredPreference(
                    context,
                    SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(),
                    "0"
                ).toString()*/
              /*  when (transport_mode) {
                    "0" -> {
                        ivTransportMode!!.setImageResource(R.drawable.ic_car)
                        selectedTravelMode = "car"
                    }

                    "1" -> {
                        ivTransportMode!!.setImageResource(R.drawable.ic_walking_transport_mode)
                        selectedTravelMode = "walking"
                    }

                    "2" -> {
                        ivTransportMode!!.setImageResource(R.drawable.ic_bicycle_transport_mode)
                        selectedTravelMode = "bicycle"
                    }
                }
                if (nearByStore.isWashroom == 1) {
                    ivToilet!!.visibility = View.VISIBLE
                } else {
                    ivToilet!!.visibility = View.GONE
                }
                if (nearByStore.isWheelchairAccessible == 1) {
                    ivWheelChair!!.visibility = View.VISIBLE
                } else {
                    ivWheelChair!!.visibility = View.GONE
                }
                if (nearByStore.isFeedingRoom == 1) {
                    ivFeedingRoom!!.visibility = View.VISIBLE
                } else {
                    ivFeedingRoom!!.visibility = View.GONE
                }
                if (nearByStore.isSanitizerAvailable == 1) {
                    ivSanitizer!!.visibility = View.VISIBLE
                } else {
                    ivSanitizer!!.visibility = View.GONE
                }
                if (nearByStore.isCoffeeAvailable == 1) {
                    ivCoffee!!.visibility = View.VISIBLE
                } else {
                    ivCoffee!!.visibility = View.GONE
                }
                if (nearByStore.isMakeupRoomAvailable == 1) {
                    ivMakeupRoom!!.visibility = View.VISIBLE
                } else {
                    ivMakeupRoom!!.visibility = View.GONE
                }
                if (nearByStore.isSanitaryPadsAvailable == 1) {
                    ivSanitaryPads!!.visibility = View.VISIBLE
                } else {
                    ivSanitaryPads!!.visibility = View.GONE
                }
                if (nearByStore.isCovidFree == 1) {
                    ivCovidFree!!.visibility = View.VISIBLE
                } else {
                    ivCovidFree!!.visibility = View.GONE
                }
                if (nearByStore.isSafeSpace == 1) {
                    ivSafeSpace!!.visibility = View.VISIBLE
                } else {
                    ivSafeSpace!!.visibility = View.GONE
                }
                if (nearByStore.isCleanAndHygiene == 1) {
                    ivCleanHygiene!!.visibility = View.VISIBLE
                } else {
                    ivCleanHygiene!!.visibility = View.GONE
                }*/
                rlParentLayout!!.setOnClickListener { v: View? ->
                    try {
                        val payload = HashMap<String, Any>()
                        payload[AppConstants.LOCATION] =
                            SharedPrefSettings.Companion.getPreferences.fetchLocationForNetcore().toString()
                        payload[AppConstants.TRAVEL_MODE] = selectedTravelMode
                        payload[AppConstants.HOST_CLICKED_ID] = nearByStore.id.toString()
                        payload[AppConstants.HOST_CLICKED_LOCATION] =
                            "(" + nearByStore.lat + "," + nearByStore.lng + ")"
                        Utility.logNetcoreEvent(context, payload, AppConstants.WOLOO_DETAIL_CLICK)
                        WolooApplication.instance!!.nearByWoloo = nearByStore
                        context.startActivity(Intent(context, HomeDetailsActivity::class.java))
                    } catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }
                }
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }
    }

    companion object {
        var FIRST_ITEM = 100
        var NORMAL_ITEM = 200
    }
}
