package in.woloo.www.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.utils.Logger;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import in.woloo.www.R;

public class ImageUtil {

    private static final String TAG = ImageUtil.class.getSimpleName();

    public static void loadImageProfile(Context context, ImageView imageView, String imageURL) {
        try {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
            requestOptions.placeholder(R.drawable.ic_profile_placeholder);
//                requestOptions.skipMemoryCache(true);
            Glide.with(context)
                    .load(imageURL)
                    .apply(requestOptions)
                    .apply(RequestOptions.circleCropTransform())
//                        .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);

        } catch (Exception e) {
            Logger.e(TAG, "loadImage: ", e);
        }
    }

    public static void loadImageProfileAccount(Context context, ImageView imageView, String imageURL) {
        try {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.diskCacheStrategy(DiskCacheStrategy.NONE);

//                requestOptions.skipMemoryCache(true);
            Glide.with(context)
                    .load(imageURL)
                    .apply(requestOptions)
//                        .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);

        } catch (Exception e) {
            Logger.e(TAG, "loadImage: ", e);
        }
    }

    public static void loadImage(Context context, ImageView imageView, String imageURL) {
        try {
            if (imageURL != null) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.skipMemoryCache(true);
             //   requestOptions.placeholder(R.drawable.ic_placeholder_rectangle_tv);
            //    requestOptions.error(R.drawable.ic_placeholder_rectangle_tv);
                Glide.with(context)
                        .load(imageURL)
                        .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imageView);
            }
        } catch (Exception e) {
            Logger.e(TAG, "loadImage: ", e);
        }
    }

    public static void loadImageBlogs(Context context, ImageView imageView, String imageURL) {
        try {
            if (imageURL != null) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions = requestOptions.skipMemoryCache(true).transform(new CenterCrop(), new RoundedCorners(16));
                //   requestOptions.placeholder(R.drawable.ic_placeholder_rectangle_tv);
                //    requestOptions.error(R.drawable.ic_placeholder_rectangle_tv);
                Glide.with(context)
                        .load(imageURL)
                        .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imageView);
            }
        } catch (Exception e) {
            Logger.e(TAG, "loadImage: ", e);
        }
    }

    public static void loadImageHistory(Context context, ImageView imageView, String imageURL) {
        try {
            if (imageURL != null) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.skipMemoryCache(true);
                //   requestOptions.placeholder(R.drawable.ic_placeholder_rectangle_tv);
                //    requestOptions.error(R.drawable.ic_placeholder_rectangle_tv);
                Glide.with(context)
                        .load(imageURL)
                        .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA))
                        .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(40)))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(imageView);
            }
        } catch (Exception e) {
            Logger.e(TAG, "loadImage: ", e);
        }
    }

    public static void loadImageBitmap(Context context, String imageURL, LinearLayout linearLayout) {
        try {
            if (imageURL != null) {
                RequestOptions requestOptions = new RequestOptions();
                requestOptions.skipMemoryCache(true);
                //   requestOptions.placeholder(R.drawable.ic_placeholder_rectangle_tv);
                //    requestOptions.error(R.drawable.ic_placeholder_rectangle_tv);
                Glide.with(context)
                        .asBitmap()
                        .load(imageURL)
                        .apply(requestOptions.diskCacheStrategy(DiskCacheStrategy.DATA))
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                try{
                                    Drawable drawable = new BitmapDrawable(context.getResources(),resource);
                                    linearLayout.setBackground(drawable);
                                }catch (Exception ex){
                                     CommonUtils.printStackTrace(ex);
                                }
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
            }
        } catch (Exception e) {
            Logger.e(TAG, "loadImage: ", e);
        }
    }


}
