package `in`.woloo.www.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.Logger.e

object ImageUtil {
    private val TAG: String = ImageUtil::class.java.simpleName

    @JvmStatic
    fun loadImageProfile(context: Context, imageView: ImageView, imageURL: String?) {
        try {
            val requestOptions = RequestOptions()
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)
            requestOptions.placeholder(R.drawable.ic_profile_placeholder)
            //                requestOptions.skipMemoryCache(true);
            Glide.with(context)
                .load(imageURL)
                .apply(requestOptions)
                .apply(RequestOptions.circleCropTransform()) //                        .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        } catch (e: Exception) {
            e(TAG, "loadImage: ", e)
        }
    }

    fun loadImageProfileAccount(context: Context, imageView: ImageView, imageURL: String?) {
        try {
            val requestOptions = RequestOptions()
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE)

            //                requestOptions.skipMemoryCache(true);
            Glide.with(context)
                .load(imageURL)
                .apply(requestOptions) //                        .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        } catch (e: Exception) {
            e(TAG, "loadImage: ", e)
        }
    }

    fun loadImage(context: Context, imageView: ImageView, imageURL: String?) {
        try {
            if (imageURL != null) {
                val requestOptions = RequestOptions()
                requestOptions.skipMemoryCache(true)
                //   requestOptions.placeholder(R.drawable.ic_placeholder_rectangle_tv);
                //    requestOptions.error(R.drawable.ic_placeholder_rectangle_tv);
                Glide.with(context)
                    .load(imageURL)
                    .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView)
            }
        } catch (e: Exception) {
            e(TAG, "loadImage: ", e)
        }
    }

    fun loadImageBlogs(context: Context, imageView: ImageView, imageURL: String?) {
        try {
            if (imageURL != null) {
                var requestOptions = RequestOptions()
                requestOptions =
                    requestOptions.skipMemoryCache(true).transform(CenterCrop(), RoundedCorners(16))
                //   requestOptions.placeholder(R.drawable.ic_placeholder_rectangle_tv);
                //    requestOptions.error(R.drawable.ic_placeholder_rectangle_tv);
                Glide.with(context)
                    .load(imageURL)
                    .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView)
            }
        } catch (e: Exception) {
            e(TAG, "loadImage: ", e)
        }
    }

    fun loadImageHistory(context: Context, imageView: ImageView, imageURL: String?) {
        try {
            if (imageURL != null) {
                val requestOptions = RequestOptions()
                requestOptions.skipMemoryCache(true)
                //   requestOptions.placeholder(R.drawable.ic_placeholder_rectangle_tv);
                //    requestOptions.error(R.drawable.ic_placeholder_rectangle_tv);
                Glide.with(context)
                    .load(imageURL)
                    .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA))
                    .apply(RequestOptions().transform(CenterCrop(), RoundedCorners(40)))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView)
            }
        } catch (e: Exception) {
            e(TAG, "loadImage: ", e)
        }
    }

    fun loadImageBitmap(context: Context, imageURL: String?, linearLayout: LinearLayout) {
        try {
            if (imageURL != null) {
                val requestOptions = RequestOptions()
                requestOptions.skipMemoryCache(true)
                //   requestOptions.placeholder(R.drawable.ic_placeholder_rectangle_tv);
                //    requestOptions.error(R.drawable.ic_placeholder_rectangle_tv);
                Glide.with(context)
                    .asBitmap()
                    .load(imageURL)
                    .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA))
                    .into(object : CustomTarget<Bitmap?>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            try {
                                val drawable: Drawable = BitmapDrawable(context.resources, resource)
                                linearLayout.background = drawable
                            } catch (ex: Exception) {
                                CommonUtils.printStackTrace(ex)
                            }
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })
            }
        } catch (e: Exception) {
            e(TAG, "loadImage: ", e)
        }
    }
}
