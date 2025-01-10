package `in`.woloo.www.application_kotlin.adapters.loo_discovery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
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
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.EnrouteDirectionActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.HomeDetailsActivity
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Utility

class BookmarkListAdapter(
    private val context: Context,
    private val nearByStoreResponseList: List<NearByStoreResponse.Data>
) : RecyclerView.Adapter<BookmarkListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View =
            layoutInflater.inflate(R.layout.my_history_item, parent, false) //nearest_walk
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

        /* @BindView(R.id.tvRating)
        TextView tvRating;*/
        @JvmField
        @BindView(R.id.tvAddress)
        var tvAddress: TextView? = null

        @JvmField
        @BindView(R.id.tvDistance)
        var tvDistance: TextView? = null

        @JvmField
        @BindView(R.id.navigate_layout)
        var navigateLayout: LinearLayout? = null

        protected var mSharedPreference: SharedPreference? = null
        private var selectedTravelMode = "car"

        init {
            ButterKnife.bind(this, itemView)
        }

        fun setData(nearByStore: NearByStoreResponse.Data, context: Context) {
            try {
                tvWolooStoreName!!.text = nearByStore.name
                tvAddress!!.text = nearByStore.address
                tvDistance!!.text = nearByStore.distance
                tvRequiredTime!!.text = nearByStore.duration
                var wolooImg = BuildConfig.NODE_API_URL + nearByStore.image[0]
                Log.d("img url is" , wolooImg)
                Glide.with(context)
                    .load(wolooImg)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(27)))
                    .into(ivWolooStore!!)


                navigateLayout!!.setOnClickListener {
                    if (nearByStore.distance == "-") {
                        CommonUtils.showCustomDialog(
                            context,
                            "No route found for the transport mode. Please change mode and try again"
                        )
                    }
                    else {
                      /*  val params = Bundle()
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
                        )*/




                        val i = Intent(context, EnrouteDirectionActivity::class.java)
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("destlat", nearByStore.lat)
                        i.putExtra("destlong", nearByStore.lng)
                        i.putExtra("wolooId", nearByStore.id)
                        i.putExtra("wolooName", nearByStore.name)
                        i.putExtra("wolooAddress", nearByStore.address)
                        i.putExtra("tag_new_navigate", "bookmark")
                        context.startActivity(i)
                    }
                }



            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }
    }


}
