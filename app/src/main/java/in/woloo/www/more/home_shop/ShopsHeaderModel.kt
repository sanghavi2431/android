package `in`.woloo.www.more.home_shop

import android.net.Uri
import android.view.View
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import `in`.woloo.www.application_kotlin.base.BaseEpoxyHolder
import `in`.woloo.www.R
import `in`.woloo.www.databinding.ShopScreenHederBinding


@EpoxyModelClass(layout = R.layout.shop_screen_heder)
public abstract class ShopsHeaderModel  : EpoxyModelWithHolder<ShopsHeaderModel.Holder>() {



    override fun bind(holder: Holder) {
        val context = holder.binding.playerView.context

        val exoPlayer = ExoPlayer.Builder(context).build()
        holder.binding.playerView.player = exoPlayer

        val videoUrl = "https://woloo-stagging.s3.ap-south-1.amazonaws.com/WhatsApp+Video+2025-01-27+at+18.22.57_5f128558.mp4"
        val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true

        holder.exoPlayer = exoPlayer


    }

    override fun unbind(holder: Holder) {
        // Release ExoPlayer when view is recycled
        holder.exoPlayer?.release()
        holder.exoPlayer = null
    }

    class Holder : BaseEpoxyHolder() {
        lateinit var binding: ShopScreenHederBinding
        var exoPlayer: ExoPlayer? = null

        override fun bindView(itemView: View) {
            super.bindView(itemView)
            binding = ShopScreenHederBinding.bind(itemView)
        }

    }



}
