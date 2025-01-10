package `in`.woloo.www.more.refer_woloo_host.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.EnrouteDirectionActivity
import `in`.woloo.www.more.refer_woloo_host.model.ReferredWolooListResponse

import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.ImageUtil

class ReferredWolooHostListingAdapter(
    private val context: Context,
    private val dataItems: List<ReferredWolooListResponse.DataItem>
) : RecyclerView.Adapter<ReferredWolooHostListingAdapter.ReferredwolooHostHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReferredwolooHostHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.referred_woloo_host_listing_details, parent, false)
        return ReferredwolooHostHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ReferredwolooHostHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        when (dataItems[position].status) {
            0 -> holder.status_Tv.text = "Under Review"
            1 -> holder.status_Tv.text = "Approved"
            2 -> holder.status_Tv.text = "Rejected"
            else -> {}
        }
        holder.hostTitle_Tv.text = dataItems[position].name
        holder.hostAddress_Tv.text = dataItems[position].address
        holder.cibilScore.text = dataItems[position].code
        holder.tvDistance.text = dataItems[position].city
        holder.tvTime.text = dataItems[position].createdAt
        if (dataItems[position].image!!.size > 0) {
            ImageUtil.loadImage(
                context,
                holder.host_image,
                BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + dataItems[position].image!![0]
            )
        } else {
            val imgUrl =
                BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + AppConstants.DEFAULT_STORE_IMAGE_LANDSCAPE
            ImageUtil.loadImage(context, holder.host_image, imgUrl)
        }
        holder.tvNavigate.setOnClickListener {
            val i = Intent(context, EnrouteDirectionActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            i.putExtra("destlat", dataItems[position].lat)
            i.putExtra("destlong", dataItems[position].lng)
            i.putExtra("wolooId", dataItems[position].id)
            i.putExtra("wolooName", dataItems[position].name)
            i.putExtra("wolooAddress", dataItems[position].address)
            i.putExtra("tag_new_navigate", "refer")
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return dataItems.size
    }

    inner class ReferredwolooHostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var host_image: ImageView
        var tvNavigate: ImageView
        var status_Tv: TextView
        var hostTitle_Tv: TextView
        var hostAddress_Tv: TextView
        var cibilScore: TextView
        var tvTime: TextView
        var tvDistance: TextView

        init {
            status_Tv = itemView.findViewById<View>(R.id.status_Tv) as TextView
            hostTitle_Tv = itemView.findViewById<View>(R.id.host_title_Tv) as TextView
            hostAddress_Tv = itemView.findViewById<View>(R.id.host_address_Tv) as TextView
            host_image = itemView.findViewById<View>(R.id.host_imv) as ImageView
            cibilScore = itemView.findViewById<View>(R.id.bottom_host_limit) as TextView
            tvTime = itemView.findViewById<View>(R.id.tvRequiredTime) as TextView
            tvNavigate = itemView.findViewById<View>(R.id.directionShow) as ImageView
            tvDistance = itemView.findViewById<View>(R.id.tvDistance) as TextView
        }
    }
}
