package `in`.woloo.www.more.my_history.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.EnrouteDirectionActivity
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.ImageUtil

class MyOfferAdapter(
    private val context: Context,
    private val offersList: ArrayList<NearByStoreResponse.Data?>
) : RecyclerView.Adapter<MyOfferAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View =
            layoutInflater.inflate(R.layout.my_history_item, parent, false) //woloo_search_item
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(offersList[position])
    }

    override fun getItemCount(): Int {
        return offersList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
        @BindView(R.id.navigate_layout)
        var navigateLayout: LinearLayout? = null

        @JvmField
        @BindView(R.id.tvAddress)
        var tvAddress: TextView? = null

        @JvmField
        @BindView(R.id.tvDistance)
        var tvDistance: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        @SuppressLint("SetTextI18n")
        fun setData(history: NearByStoreResponse.Data?) {
            try {
                if (history != null) {
                    if (!history.image.isEmpty()) {
                        ImageUtil.loadImageHistory(
                            context,
                            ivWolooStore!!,
                            BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + history.image[0]
                        )
                    } else {
                        val imgUrl =
                            BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW
                        ImageUtil.loadImageHistory(context, ivWolooStore!!, imgUrl)
                    }
                    /*
                    String imgUrl = BuildConfig.BASE_URL+AppConstants.DEFAULT_BASE_URL_FOR_IMAGES+AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW;
                    ImageUtil.loadImage(context,ivWolooStore,imgUrl);
                    */

//                    if (history.getOffer() != null && !TextUtils.isEmpty(history.getOffer().getImage())) {
//                        ImageUtil.loadImageHistory(context, ivWolooStore, BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + history.getOffer().getImage());
//                    } else if (history.getImage() != null && !history.getImage().isEmpty()) {
//                        ImageUtil.loadImageHistory(context, ivWolooStore, history.getBaseUrl() + history.getImage().get(0));
//                    } else {
//                        String imgUrl = BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE_NEW;
//                        ImageUtil.loadImageHistory(context, ivWolooStore, imgUrl);
//                    }
                    if (!TextUtils.isEmpty(history.name)) {
                        tvWolooStoreName!!.text = history.name
                    }
                    if (!TextUtils.isEmpty(history.address)) {
                        tvAddress!!.text = history.address
                    }
                    navigateLayout!!.setOnClickListener {
                        if (history.distance === "-") {
                            CommonUtils.showCustomDialog(
                                context,
                                "No route found for the transport mode. Please change mode and try again"
                            )
                        } else {
                            val i = Intent(context, EnrouteDirectionActivity::class.java)
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            i.putExtra("destlat", history.lat)
                            i.putExtra("destlong", history.lng)
                            i.putExtra("wolooId", history.id)
                            i.putExtra("wolooName", history.name)
                            i.putExtra("wolooAddress", history.address)
                            i.putExtra("tag_new_navigate", "bookmark")
                            context.startActivity(i)
                        }
                    }
                    if (history.distance != null) {
                        tvDistance!!.text = "" + CommonUtils.getDistace(history.distance!!)
                    }
                    if (history.duration != null) {
                        tvRequiredTime!!.text =
                            "" + CommonUtils.getTimeForWolooStoreInfo(history.duration!!)
                    }

                    // tvRating.setText("" + history.userRating);


                    /*if(!TextUtils.isEmpty(history.getAmount())){
                        tvEarnedPoints.setVisibility(View.VISIBLE);
                        tvEarnedPoints.setText("You have earned "+history.getAmount()+" points");
                    }else{
                        tvEarnedPoints.setVisibility(View.GONE);
                    }*/

                    /*if(history.getIsReviewPending()==1)
                        tvRateThisPlace.setVisibility(View.VISIBLE);
                    else*/
                    // tvRateThisPlace.setVisibility(View.GONE);
                    /*tvRateThisPlace.setOnClickListener(v -> {
                        try{
                            Intent intent = new Intent(context, AddReviewActivity.class);
                            intent.putExtra(AppConstants.WOLOO_ID,history.getWolooId());
                            context.startActivity(intent);
                        }catch (Exception ex){
                             CommonUtils.printStackTrace(ex);
                        }
                    });*/
                }
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }
    }
}
