package `in`.woloo.www.application_kotlin.adapters.loo_discovery

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.model.server_response.SearchWolooResponse
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.ImageUtil

class SearchedPhotosAdapter(
    private val context: Context,
    private val offerList: List<SearchWolooResponse.Data.Offer>
) : RecyclerView.Adapter<SearchedPhotosAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View =
            layoutInflater.inflate(R.layout.photos_item, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.ivPhoto!!.clipToOutline = true
        try {
            if (!TextUtils.isEmpty(offerList[position].image)) {
                ImageUtil.loadImage(
                    context,
                    holder.ivPhoto!!,
                    BuildConfig.BASE_URL + AppConstants.DEFAULT_BASE_URL_FOR_IMAGES + offerList[position].image
                )
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    override fun getItemCount(): Int {
        return offerList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.ivPhoto)
        var ivPhoto: ImageView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}
