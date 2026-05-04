package `in`.woloo.www.blogs_module

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import `in`.woloo.www.R
import `in`.woloo.www.more.trendingblog.model.blog.MediaItemBlog


class MediaPagerAdapter(
    private val context: Context,
    private val mediaList: List<MediaItemBlog>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_IMAGE = 0
        private const val TYPE_VIDEO = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (mediaList[position]) {
            is MediaItemBlog.Image -> TYPE_IMAGE
            is MediaItemBlog.Video -> TYPE_VIDEO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_IMAGE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_image_page, parent, false)
                ImageViewHolder(view)
            }

            TYPE_VIDEO -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_video_page, parent, false)
                VideoViewHolder(view)
            }

            else -> throw IllegalArgumentException("Unknown view type")
        }
    }

    override fun getItemCount() = mediaList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val media = mediaList[position]) {
            is MediaItemBlog.Image ->{ (holder as ImageViewHolder).bind(media.url)
            Log.i("URL IS " , media.url)
            }
            is MediaItemBlog.Video -> {(holder as VideoViewHolder).bind(media.url)
                Log.i("URL IS " , media.url)}

        }

    }


    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.imageView)
        fun bind(url: String) {
            Glide.with(itemView.context)
                .load(url)
                .into(image)
        }
    }

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val playerView: PlayerView = itemView.findViewById(R.id.videoPlayer)
        private var player: ExoPlayer? = null



        fun bind(url: String) {
            if (player == null) {
                player?.setVideoSurfaceView(null)
                player?.setVideoTextureView(TextureView(context))
                player = ExoPlayer.Builder(itemView.context).build().also { exoPlayer ->

                    playerView.player = exoPlayer
                    val mediaItem = MediaItem.Builder().setUri(Uri.parse(url)).build()
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.prepare()
                    exoPlayer.playWhenReady = true

                    exoPlayer.addListener(object : Player.Listener {
                        override fun onPlayerError(error: PlaybackException) {
                            Log.e("VideoPlayer", "Playback error: ${error.message}")
                        }
                    })
                }
            }
        }

        fun releasePlayer() {
            player?.release()
            player = null
        }
    }

    fun isVideoUrl(url: String): Boolean {
        return url.lowercase().endsWith(".mp4") ||
                url.lowercase().endsWith(".mov") ||
                url.lowercase().endsWith(".webm") ||
                url.lowercase().endsWith(".mkv")
    }


}
