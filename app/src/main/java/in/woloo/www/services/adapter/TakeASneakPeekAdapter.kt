package `in`.woloo.www.services.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.datasource.DefaultDataSourceFactory
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.databinding.ExpressServicesListItemBinding
import `in`.woloo.www.databinding.TakeSneakPeekListItemBinding
import `in`.woloo.www.services.ExpressServiceItem
import `in`.woloo.www.services.TakeSneakPeekServiceItem
import `in`.woloo.www.services.screens.ServicesProductsListingActivity
import `in`.woloo.www.utils.AppConstants

class TakeASneakPeekAdapter(private val items: ArrayList<TakeSneakPeekServiceItem> ,
                            private val exoPlayer: ExoPlayer
) : RecyclerView.Adapter<TakeASneakPeekAdapter.ServiceViewHolder>() {

    private val playerMap = mutableMapOf<Int, ExoPlayer>()

    inner class ServiceViewHolder(val binding: TakeSneakPeekListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TakeSneakPeekListItemBinding.inflate(layoutInflater, parent, false)
        return ServiceViewHolder(binding)
    }

/*    @OptIn(UnstableApi::class)
    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val item = items[position]
        val context = holder.itemView.context
        val dataSourceFactory = DefaultDataSourceFactory(
            context,
            Util.getUserAgent(context, "app"))
        val cacheDataSourceFactory = CacheDataSource.Factory()
            .setCache(WolooApplication.ExoPlayerCache.simpleCache)
            .setUpstreamDataSourceFactory(dataSourceFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

        val mediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory)
            .createMediaSource(MediaItem.fromUri(item.imageResId))
        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

        holder.binding.productVideo.player = exoPlayer
        holder.binding.productVideo.useController = false
        holder.binding.productVideo.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL

        holder.binding.root.setOnClickListener{
            val intent = Intent(context, ServicesProductsListingActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true)
            intent.putExtra("CATEGORY_ID" , item.categori_id)
            intent.putExtra("FROMSCREEN" , AppConstants.FROM_CATEGORIES)
            context.startActivity(intent)
        }

        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                holder.binding.productVideo.visibility = View.GONE
                holder.binding.productImage.visibility = View.VISIBLE
            }
        })

    }*/


    @OptIn(UnstableApi::class)
    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val item = items[position]
        val context = holder.itemView.context

        // Prevent creating multiple players for same item
        val exoPlayer = playerMap[position] ?: ExoPlayer.Builder(context).build().also {
            val dataSourceFactory = DefaultDataSourceFactory(
                context,
                Util.getUserAgent(context, "app")
            )
            val cacheDataSourceFactory = CacheDataSource.Factory()
                .setCache(WolooApplication.ExoPlayerCache.simpleCache)
                .setUpstreamDataSourceFactory(dataSourceFactory)
                .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)

            val mediaSource = ProgressiveMediaSource.Factory(cacheDataSourceFactory)
                .createMediaSource(MediaItem.fromUri(item.imageResId))

            val mediaItem = MediaItem.Builder()
                .setUri(item.imageResId)
                .setMediaId(item.imageResId) // same as uri
                .build()

            it.setMediaSource(mediaSource)
            it.prepare()
            it.playWhenReady = true
            it.repeatMode = Player.REPEAT_MODE_ONE

            playerMap[position] = it  // Save reference
        }

        holder.binding.productVideo.player = exoPlayer
        holder.binding.productVideo.useController = false
        holder.binding.productVideo.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL

        exoPlayer.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                holder.binding.productVideo.visibility = View.GONE
                holder.binding.productImage.visibility = View.VISIBLE
            }
        })

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, ServicesProductsListingActivity::class.java)
            intent.putExtra("IS_SHOW_BACK_BUTTON", true)
            intent.putExtra("CATEGORY_ID", item.categori_id)
            intent.putExtra("FROMSCREEN", AppConstants.FROM_CATEGORIES)
            context.startActivity(intent)
        }
    }

    fun releaseAllPlayers() {
        for ((_, player) in playerMap) {
            player.release()
        }
        playerMap.clear()
    }

    override fun getItemCount(): Int = items.size
}
