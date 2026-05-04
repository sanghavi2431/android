package `in`.woloo.www.application_kotlin.adapters.loo_discovery

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.Logger

class WolooStoreImagesAdapter(private val context: Context, private val dataList: List<String>) :
    RecyclerView.Adapter<WolooStoreImagesAdapter.ViewHolder?>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem: View =
            layoutInflater.inflate(R.layout.item_store_info_marker, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            Logger.i("onBindViewHolder", "" + position)
            holder.setStoreData(dataList[position])
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    override fun getItemCount(): Int {
        /* if (dataList!=null && dataList.size()>0){
            return dataList.size();
        }*/
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @JvmField
        @BindView(R.id.image_store_offer)
        var image_store_offer: ImageView? = null

        init {
            ButterKnife.bind(this, itemView)
        }

        fun setStoreData(image: String) {
            try {
                //ImageUtil.loadImage(context,image_store_offer,image);
                Glide.with(context)
                    .load(image)
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(16)))
                    .placeholder(R.drawable.banner_logo)
                    .into(image_store_offer!!)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        }
    }
}
