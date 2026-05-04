package `in`.woloo.www.application_kotlin.adapters.loo_discovery

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
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
import `in`.woloo.www.application_kotlin.presentation.fragments.loo_discovery.HomeCategoryFragment
import `in`.woloo.www.application_kotlin.presentation.fragments.loo_discovery.ShowMoreFragment
import `in`.woloo.www.application_kotlin.view_models.ScrollListViewModel
import `in`.woloo.www.application_kotlin.view_models.SharedViewModel
import `in`.woloo.www.application_kotlin.view_models.WolooViewModel
import `in`.woloo.www.common.CommonUtils


class NearestWalkAdapterEnroute(
    private val context: Context,
    private val nearByStoreResponseList: List<NearByStoreResponse.Data>,
    private var wolooViewModel: WolooViewModel?,
    private var searchLocation : String ,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    lateinit  var sharedViewModel: SharedViewModel
    private var boundStoreCount: Boolean = false


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        lateinit var view : View

        sharedViewModel   = ViewModelProvider(context as AppCompatActivity).get(SharedViewModel::class.java)
        // scrollViewModel = ViewModelProvider(context as AppCompatActivity).get(ScrollListViewModel::class.java)

        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_TYPE_STORE -> {
                val view = layoutInflater.inflate(R.layout.home_category_fragment_adapter_item, parent, false)
                StoreViewHolder(view)
            }
            ITEM_TYPE_BUTTONS -> {

                val view = layoutInflater.inflate(R.layout.page_layout_2, parent, false)
                ButtonsViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }

    }




    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.i("Check Type", "Binding position: $position")

        if (holder is StoreViewHolder) {
            val nearByStore = nearByStoreResponseList[position]
            holder.setData(nearByStore, context)
            val params = holder.itemView.layoutParams
            params.width = context.resources.displayMetrics.widthPixels * 85 / 100
            holder.itemView.layoutParams = params
            if (position < nearByStoreResponseList.size) {
                holder.setData(nearByStore, context)
            } else {
                // Handle your extra button layout here
                Log.i("onBindViewHolder", "Last position for button layout")
            }
            // notifyItemChanged(nearByStoreResponseList.size)

        } else if (holder is ButtonsViewHolder) {
            val params = holder.itemView.layoutParams
            params.width = context.resources.displayMetrics.widthPixels * 85 / 100
            holder.itemView.layoutParams = params
            holder.itemView.visibility = if (boundStoreCount) View.VISIBLE else View.GONE
            if (boundStoreCount) {
                holder.bindButtons()
            }
        } else
        {
            Log.i("Check Type", "Binding position: $position")
        }

    }

    @SuppressLint("SuspiciousIndentation")
    override fun getItemCount(): Int {

        if(context is EnrouteDirectionActivity){
            return  nearByStoreResponseList.size
        }
        else {
            var count = 0
            if (boundStoreCount)
                count = nearByStoreResponseList.size + 1
            else
                count = nearByStoreResponseList.size

            Log.i("Check Type", "  Adapter Count Total Items: $count")
            return count
        }
    }

    override fun getItemViewType(position: Int): Int {
        Log.i("Check Type" ,"position $position ITEM_TYPE_STORE $ITEM_TYPE_STORE ITEM_TYPE_BUTTONS $ITEM_TYPE_BUTTONS ${nearByStoreResponseList.size}" )
        return if (position < nearByStoreResponseList.size) ITEM_TYPE_STORE else ITEM_TYPE_BUTTONS
    }

    inner class StoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
        @BindView(R.id.byWalkShowLayout)
        var byWalkNavigateLayout: LinearLayout? = null

        @JvmField
        @BindView(R.id.byBikeShowLayout)
        var byBikeNavigateLayout: LinearLayout? = null

        @JvmField
        @BindView(R.id.byCarShowLayout)
        var byCarNavigateLayout: LinearLayout? = null

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





                /*  byWalkTime!!.text = nearByStore.timeByWalk
                  byBikeTime!!.text = nearByStore.timeByBike
                  byCarTime!!.text = nearByStore.timeByCar
  */
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
                /* else {
                     openNowText!!.text = "Open Now"

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
                    Log.d("direction aarati list" , "in Take me here")
                    if (nearByStore.distance == "-") {
                        CommonUtils.showCustomDialog(
                            context,
                            "No route found for the transport mode. Please change mode and try again"
                        )
                    }
                    else {
                        try {
                            val request = CreditCoinsRequest(
                                coins = CommonUtils.authconfig_response(context).getTakeMeHere()!!
                                    .toInt(),
                                remarks = AppConstants.TAKE_ME_HERE_CLICK,
                                type = AppConstants.TAKE_ME_HERE_CLICK,
                                isGift = 0,
                                blogId = 0,
                                wolooId = nearByStore.id,
                                wolooCoins = CommonUtils.authconfig_response(context)
                                    .getTakeMeHere()!!.toInt()
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

                        }catch (e : Exception)
                        {
                            e.printStackTrace()
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
                       // context.startActivity(Intent(context, HomeDetailsActivity::class.java))
                        val intent = Intent(context, HomeDetailsActivity::class.java)
                        intent.putExtra(AppConstants.searchLocationToHost, searchLocation)
                        context.startActivity(intent)
                    } catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }
                }
                boundStoreCount = true
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }
    }

    inner class ButtonsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        @JvmField
        @BindView(R.id.tv2Km)
        var tv2Km: TextView? = null

        @JvmField
        @BindView(R.id.tv4Km)
        var tv4Km: TextView? = null

        @JvmField
        @BindView(R.id.tv5Km)
        var tv5Km: TextView? = null

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
        @BindView(R.id.tv_search_more)
        var tv_search_more: LinearLayout? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun bindButtons() {





            tv_search_more!!.setOnClickListener { v: View? ->

                try{
                    sharedViewModel.onShowMoreClicked()
                }
                catch (e : Exception)
                {
                    e.printStackTrace()
                }
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
        private const val ITEM_TYPE_STORE = 1
        private const val ITEM_TYPE_BUTTONS = 2
    }


}
