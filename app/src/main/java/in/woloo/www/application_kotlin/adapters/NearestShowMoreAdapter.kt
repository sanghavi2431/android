package `in`.woloo.www.application_kotlin.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
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
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.HomeDetailsActivity
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Utility
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.model.CreditCoinsRequest
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.EnrouteDirectionActivity
import `in`.woloo.www.application_kotlin.view_models.WolooViewModel
import `in`.woloo.www.common.CommonUtils

class NearestShowMoreAdapter(
    private val context: Context,
    private val nearByStoreResponseList: List<NearByStoreResponse.Data>,
    private var wolooViewModel: WolooViewModel?,
    private var searchLocation : String,
) : RecyclerView.Adapter<NearestShowMoreAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        lateinit var view : View
        val layoutInflater = LayoutInflater.from(parent.context)
        /* if (viewType == BUTTON_TYPE) {
              view = layoutInflater.inflate(R.layout.show_more_button, parent, false)
             FooterViewHolder(view)
         } else {*/
        view =
            layoutInflater.inflate(R.layout.home_category_fragment_adapter_item, parent, false) //nearest_walk

        // }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val nearByStore = nearByStoreResponseList[position]
        holder.setData(nearByStore, context)

    }

    override fun getItemCount(): Int {
        return nearByStoreResponseList.size
    }

    /* override fun getItemViewType(position: Int): Int {
         return if (position == nearByStoreResponseList.size) BUTTON_TYPE else ITEM_TYPE
     }*/

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @JvmField
        @BindView(R.id.bottom_host_name)
        var tvName: TextView? = null

        @JvmField
        @BindView(R.id.cardViewParent)
        var cardParent: CardView? = null

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
        var tv_direction: LinearLayout? = null

        @JvmField
        @BindView(R.id.rlParentLayout)
        var rlParentLayout: FrameLayout? = null

        @JvmField
        @BindView(R.id.tvRequiredTime)
        var tvRequiredTime: TextView? = null

        /*   @JvmField
           @BindView(R.id.offers_available)
           var tvofferesAvailable: TextView? = null*/

        @JvmField
        @BindView(R.id.offers_available)
        var offersAvailable: TextView? = null

        @JvmField
        @BindView(R.id.byWalkShow)
        var byWalkNavigate: ImageView? = null

        @JvmField
        @BindView(R.id.byWalkTime)
        var byWalkTime: TextView? = null

        @JvmField
        @BindView(R.id.byBikeShow)
        var byBikeNavigate: ImageView? = null

        @JvmField
        @BindView(R.id.byBikeTime)
        var byBikeTime: TextView? = null

        @JvmField
        @BindView(R.id.byCarShow)
        var byCarNavigate: ImageView? = null

        @JvmField
        @BindView(R.id.byCarTime)
        var byCarTime: TextView? = null

        @SuppressLint("NonConstantResourceId")
        @JvmField
        @BindView(R.id.open_now_text)
        var openNowText: TextView? = null

        @JvmField
        @BindView(R.id.show_offer_card_view)
        var offerCardView: LinearLayout? = null

        @JvmField
        @BindView(R.id.byWalkShowLayout)
        var byWalkNavigateLayout: LinearLayout? = null

        @JvmField
        @BindView(R.id.byBikeShowLayout)
        var byBikeNavigateLayout: LinearLayout? = null

        @JvmField
        @BindView(R.id.byCarShowLayout)
        var byCarNavigateLayout: LinearLayout? = null

        @JvmField
        @BindView(R.id.take_me_text)
        var take_me_text: TextView? = null

        @JvmField
        @BindView(R.id.take_me_image)
        var take_me_image: ImageView? = null


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

        @SuppressLint("ResourceAsColor")
        fun setData(nearByStore: NearByStoreResponse.Data, context: Context) {
            try {

                try {
                    if(nearByStore.woloo_type != null) {
                        if (nearByStore.woloo_type!!.equals(
                                AppConstants.WOLOO_TYPE_POWDER_ROOM,
                                ignoreCase = true
                            )
                        ) {
                            offersAvailable!!.visibility = View.GONE
                            offerCardView!!.visibility = View.GONE
                            tvName!!.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.button_text_color
                                )
                            )
                            openNowText!!.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.button_text_color
                                )
                            )
                            cardParent!!.setBackgroundTintList(
                                ContextCompat.getColorStateList(
                                    context,
                                    R.color.start_theme_color
                                )
                            )
                            byWalkTime!!.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.button_text_color
                                )
                            )
                            byCarTime!!.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.button_text_color
                                )
                            )
                            byBikeTime!!.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.button_text_color
                                )
                            )
                            byWalkNavigate!!.setImageResource(R.drawable.walk_navigate_icon)
                            byBikeNavigate!!.setImageResource(R.drawable.bike_navigate_icon)
                            byCarNavigate!!.setImageResource(R.drawable.car_navigate_icon)
                            tv_direction!!.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.button_text_color
                            ))
                            take_me_text!!.setTextColor(ContextCompat.getColor(context, R.color.start_theme_color))
                            take_me_image!!.setColorFilter(
                                ContextCompat.getColor(context, R.color.start_theme_color),
                                PorterDuff.Mode.SRC_IN
                            )
                        }
                        else{
                            offersAvailable!!.visibility = View.GONE
                            offerCardView!!.visibility = View.GONE
                            tvName!!.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.start_theme_color
                                )
                            )
                            openNowText!!.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.start_theme_color
                                )
                            )
                            cardParent!!.setBackgroundTintList(
                                ContextCompat.getColorStateList(
                                    context,
                                    R.color.button_text_color
                                )
                            )
                            byWalkTime!!.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.start_theme_color
                                )
                            )
                            byCarTime!!.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.start_theme_color
                                )
                            )
                            byBikeTime!!.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.start_theme_color
                                )
                            )
                            byWalkNavigate!!.setImageResource(R.drawable.walk_navigation)
                            byBikeNavigate!!.setImageResource(R.drawable.bike_navigation)
                            byCarNavigate!!.setImageResource(R.drawable.car_navigation)
                            tv_direction!!.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.start_theme_color
                            ))
                            take_me_text!!.setTextColor(ContextCompat.getColor(context, R.color.button_text_color))
                            take_me_image!!.setColorFilter(
                                ContextCompat.getColor(context, R.color.button_text_color),
                                PorterDuff.Mode.SRC_IN
                            )
                        }
                    }
                }catch (ex :Exception)
                {
                    ex.printStackTrace()
                }

                tvName!!.text = nearByStore.name
                tvAddress!!.text = nearByStore.address

            /*    byWalkTime!!.text = nearByStore.timeByWalk
                byBikeTime!!.text = nearByStore.timeByBike
                byCarTime!!.text = nearByStore.timeByCar*/

                if(SharedPrefSettings.getPreferences.fetchTransportMode().equals("car"))
                {
                    byCarNavigateLayout!!.visibility = View.VISIBLE
                    byBikeNavigateLayout!!.visibility = View.GONE
                    byWalkNavigateLayout!!.visibility = View.GONE
                    setFormattedText(byCarTime!! , nearByStore.duration.toString())

                }
                if(SharedPrefSettings.getPreferences.fetchTransportMode().equals("bike"))
                {
                    byCarNavigateLayout!!.visibility = View.GONE
                    byBikeNavigateLayout!!.visibility = View.VISIBLE
                    byWalkNavigateLayout!!.visibility = View.GONE
                    setFormattedText(byBikeTime!! , nearByStore.duration.toString())
                }
                if(SharedPrefSettings.getPreferences.fetchTransportMode().equals("walk"))
                {
                    byCarNavigateLayout!!.visibility = View.GONE
                    byBikeNavigateLayout!!.visibility = View.GONE
                    byWalkNavigateLayout!!.visibility = View.VISIBLE
                    setFormattedText(byWalkTime!! , nearByStore.duration.toString())
                }


                Log.d("Distance is - " , nearByStore.distance.toString())
                tvDistance!!.text = nearByStore.distance.toString()
                var wolooImg = if (!nearByStore.image.isNullOrEmpty()) {
                    BuildConfig.NODE_API_URL + nearByStore.image[0]
                } else {
                    null
                }
                Log.d("img url is" , wolooImg.toString())
                if (wolooImg != null && !wolooImg.isEmpty()) {
                    Glide.with(context)
                        .load(wolooImg)
                        .placeholder(R.drawable.banner_logo)
                        .error(R.drawable.banner_logo)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(27)))
                        .into(tvImg!!);
                } else {
                    Glide.with(context)
                        .load(R.drawable.banner_logo)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(27)))
                        .into(tvImg!!);
                }

                tvRequiredTime!!.text = nearByStore.duration
                if(!nearByStore.isOfferAvailable!!.matches(Regex("0")))
                {
                    offersAvailable!!.visibility = View.VISIBLE
                    offerCardView!!.visibility = View.VISIBLE

                }

                when (nearByStore.isOpenNow) {
                    "0" -> openNowText?.text = "Closed"
                    "1" -> openNowText?.text = "Open Now"
                    else -> openNowText?.text = "Closed"
                }

                /* if(nearByStore.isOpenNow!!.matches(Regex("0")))
                 {
                     openNowText!!.text = "Closed"

                 }*/
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
try {
    val request = CreditCoinsRequest(
        coins = CommonUtils.authconfig_response(context).getTakeMeHere()!!.toInt(),
        remarks = AppConstants.TAKE_ME_HERE_CLICK,
        type = AppConstants.TAKE_ME_HERE_CLICK,
        isGift = 0,
        blogId = 0,
        wolooId = nearByStore.id,
        wolooCoins = CommonUtils.authconfig_response(context).getTakeMeHere()!!.toInt()
    )
    wolooViewModel!!.addCoinstoWolooUser(request)

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

}catch (e:Exception)
{

}


                        val i = Intent(context, EnrouteDirectionActivity::class.java)
                        i.putExtra("destlat", nearByStore.lat)
                        i.putExtra("destlong", nearByStore.lng)
                        i.putExtra("wolooId", nearByStore.id)
                        i.putExtra("wolooName", nearByStore.name)
                        i.putExtra("wolooAddress", nearByStore.address)
                        i.putExtra(AppConstants.searchLocationToHost, searchLocation)
                        i.putExtra(AppConstants.timeToHost, nearByStore.duration)
                        i.putExtra(AppConstants.distanceToHost, nearByStore.distance)
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
                        //context.startActivity(Intent(context, HomeDetailsActivity::class.java))
                        val intent = Intent(context, HomeDetailsActivity::class.java)
                        intent.putExtra(AppConstants.searchLocationToHost, searchLocation)
                        context.startActivity(intent)
                    } catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }
                }
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }
    }

    fun setFormattedText(textView: TextView, text: String) {
        if (text.contains("min") || text.contains("Min")) {
            val regex = "(?i)\\s*min".toRegex()
            val formattedText = text.replace(regex, " Min")
            textView.text = formattedText
        } else {
            textView.text = text
        }
    }



    companion object {
        /* const val ITEM_TYPE = 1
         const val BUTTON_TYPE = 2*/
    }

    /*inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val button: TextView = itemView.findViewById(R.id.tv_show_more)

        init {
            button.setOnClickListener {
                Toast.makeText(context, "Button Clicked!", Toast.LENGTH_SHORT).show()
                // Handle button click here
            }
        }
    }*/
}
