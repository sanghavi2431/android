package `in`.woloo.www.application_kotlin.adapters.loo_discovery

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.HomeDetailsActivity
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.EqualSpacingItemDecoration
import `in`.woloo.www.utils.Utility

class WolooSearchAdapter(
    private val context: Context,
    private val nearByStoreResponseList: List<NearByStoreResponse.Data>,
    var lastKnownLocation: Location?
) : RecyclerView.Adapter<WolooSearchAdapter.ViewHolder>() {
    var keyword = ""
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View =
            layoutInflater.inflate(R.layout.woloo_search_item, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(nearByStoreResponseList[position])
    }

    override fun getItemCount(): Int {
        return nearByStoreResponseList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.tvPremium)
        var tvPremium: TextView? = null

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
        @BindView(R.id.tvRating)
        var tvRating: TextView? = null

        @JvmField
        @BindView(R.id.ivTransportMode)
        var ivTransportMode: ImageView? = null

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
        @BindView(R.id.tvAddress)
        var tvAddress: TextView? = null

        @JvmField
        @BindView(R.id.tvDistance)
        var tvDistance: TextView? = null

        @JvmField
        @BindView(R.id.llParentLayout)
        var llParentLayout: LinearLayout? = null

        @JvmField
        @BindView(R.id.iv_banner_recycle)
        var iv_banner_recycle: RecyclerView? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun setData(data: NearByStoreResponse.Data?) {
            try {
                if (data != null) {
                    val mSharedPreference = SharedPreference(context)
                    val transport_mode: String = mSharedPreference.getStoredPreference(
                        context, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0"
                    ).toString()
                    when (transport_mode) {
                        "0" -> ivTransportMode!!.setImageResource(R.drawable.ic_car)
                        "1" -> ivTransportMode!!.setImageResource(R.drawable.ic_walking_transport_mode)
                        "2" -> ivTransportMode!!.setImageResource(R.drawable.ic_bicycle_transport_mode)
                    }
                    if (data.isPremium != null && data.isPremium == 1) {
                        tvPremium!!.visibility = View.VISIBLE
                    } else {
                        tvPremium!!.visibility = View.GONE
                    }

                    /*if(data.getImage().size()>0){
                        ImageUtil.loadImage(context,ivWolooStore, BuildConfig.BASE_URL+ AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+data.getImage().get(0));
                    }else{
                        String imgUrl = BuildConfig.BASE_URL+AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE;
                        ImageUtil.loadImage(context,ivWolooStore,imgUrl);
                    }*/
                    val images: MutableList<String> = ArrayList()
                    if (data.offer != null) {
                        //setOffers(data);
                        images.add(BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + data.offer!!.image)
                    }
                    if (data.image != null && !data.image.isEmpty()) {
                        for (imageName in data.image) {
                            images.add(data.baseUrl + imageName)
                        }
                    }
                    setImageData(images)
                    if (!TextUtils.isEmpty(data.name)) {
                        tvWolooStoreName!!.text = data.name
                    }
                    if (!TextUtils.isEmpty(data.address)) {
                        tvAddress!!.text = data.address
                    }
                    if (data.distance != null) {
                        tvDistance!!.text = data.distance
                    }
                    if (data.duration != null) {
                        tvRequiredTime!!.text = data.duration
                    }
                    tvRating!!.text = "" + data.userRating
                    if (data.isWashroom != null && data.isWashroom == 1) {
                        ivToilet!!.visibility = View.VISIBLE
                    } else {
                        ivToilet!!.visibility = View.GONE
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
                    llParentLayout!!.setOnClickListener { v: View? ->
                        try {
                            WolooApplication.instance!!.nearByWoloo = data
                            val intent = Intent(context, HomeDetailsActivity::class.java)
                            intent.putExtra(AppConstants.FROM_SEARCH, false)
                            context.startActivity(intent)
                            val bundle = Bundle()
                            val payload = HashMap<String, Any>()
                            try {
                                if (lastKnownLocation != null) {
                                    bundle.putString(
                                        AppConstants.LOCATION,
                                        "(" + lastKnownLocation!!.latitude + "," + lastKnownLocation!!.longitude + ")"
                                    )
                                    payload[AppConstants.LOCATION] =
                                        "(" + lastKnownLocation!!.latitude + "," + lastKnownLocation!!.longitude + ")"
                                }
                            } catch (ex: Exception) {
                            }
                            bundle.putString(AppConstants.SEARCH_KEYWORD, keyword)
                            bundle.putString(AppConstants.HOST_CLICKED_ID, data.id.toString())
                            bundle.putString(
                                AppConstants.HOST_CLICKED_LOCATION,
                                "(" + data.lat + "," + data.lng + ")"
                            )
                            Utility.logFirebaseEvent(
                                context,
                                bundle,
                                AppConstants.SEARCHED_WOLOO_CLICK
                            )
                            payload[AppConstants.SEARCH_KEYWORD] = keyword
                            payload[AppConstants.HOST_CLICKED_ID] = data.id.toString()
                            payload[AppConstants.HOST_CLICKED_LOCATION] =
                                "(" + data.lat + "," + data.lng + ")"
                            Utility.logNetcoreEvent(
                                context,
                                payload,
                                AppConstants.SEARCHED_WOLOO_CLICK
                            )
                        } catch (ex: Exception) {
                            CommonUtils.printStackTrace(ex)
                        }
                    }
                }
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }

        private fun setOffers(nearByWoloo: NearByStoreResponse.Data) {
            val offerList: MutableList<NearByStoreResponse.Data.Offer?> = ArrayList()
            offerList.add(nearByWoloo.offer)
            val adapter = PhotosAdapter(context, offerList)
        }

        /*rvPhotos.setHasFixedSize(true);
                    rvPhotos.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
                    rvPhotos.setAdapter(adapter);*/
        private fun setImageData(image: MutableList<String>?) {
            /*if (image == null)
                image = new ArrayList<>();
            else
                for (int i = 0; i < image.size(); i++) {
                    if (!image.get(i).contains(BuildConfig.BASE_URL)) {
                        image.set(i, BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + image.get(i));
                    }
                }
            for (int i = image.size(); i < 5; i++) {
                image.add(BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW);
            }*/
            if (image != null && image.isEmpty()) {
                image.add(BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW)
            }
            iv_banner_recycle!!.visibility = View.VISIBLE
            val nearByWolooImageAdapter = NearByWolooImageAdapter(context as Activity, image)
            val mLayoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
            iv_banner_recycle!!.layoutManager = mLayoutManager
            iv_banner_recycle!!.adapter = nearByWolooImageAdapter
            iv_banner_recycle!!.isNestedScrollingEnabled = true
            ViewCompat.setNestedScrollingEnabled(iv_banner_recycle!!, true)
            iv_banner_recycle!!.setHasFixedSize(true)
            iv_banner_recycle!!.setItemViewCacheSize(20)
            iv_banner_recycle!!.isDrawingCacheEnabled = true
            iv_banner_recycle!!.addItemDecoration(
                EqualSpacingItemDecoration(
                    dpToPx(2),
                    EqualSpacingItemDecoration.HORIZONTAL
                )
            ) // 16px. In practice, you'll want to use getDimensionPixelSize
            iv_banner_recycle!!.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
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
}
