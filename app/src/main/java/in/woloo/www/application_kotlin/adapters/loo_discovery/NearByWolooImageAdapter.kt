package `in`.woloo.www.application_kotlin.adapters.loo_discovery

import android.app.Activity
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import `in`.woloo.www.R

class NearByWolooImageAdapter(private val activity: Activity, var imageList: List<String?>?) :
    RecyclerView.Adapter<NearByWolooImageAdapter.ViewHolder>() {
    private var width = 0
    private val height = 0

    init {
      //  widthAndHeight
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.naer_by_woloo_image_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     //   holder.llMain!!.layoutParams.width = width
        //ImageUtil.loadImage(activity, holder.ivPhoto , imageList.get(position));
        Log.d("String" , imageList?.get(position)!!)
        val imgurl: String?= imageList?.get(position)!!
          /*  if (imgurl.toString().contains()) {
                nearByWoloo!!.baseUrl + imgurl
            } else {
                imgurl
            }*/
        holder.ivPhoto?.let {

                Glide.with(activity)
                .load(imgurl)
                .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(16)))
                .placeholder(R.drawable.banner_logo)
                .into(it)
        }
    }

    override fun getItemCount(): Int {
        return imageList!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(
        view!!
    ) {
        @JvmField
        @BindView(R.id.llMain)
        var llMain: LinearLayout? = null

        @JvmField
        @BindView(R.id.ivPhoto)
        var ivPhoto: ImageView? = null

        init {
            ButterKnife.bind(this, view!!)
        }
    }

    val widthAndHeight: Unit
        get() {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            width = (displayMetrics.widthPixels - dpToPx(16))
        }

    fun dpToPx(dp: Int): Int {
        val r = activity.resources
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                r.displayMetrics
            )
        )
    }
}