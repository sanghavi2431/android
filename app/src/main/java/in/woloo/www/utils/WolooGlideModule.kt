package `in`.woloo.www.utils

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

// This class is never used in the app. But it is required tio load images to imageview from Glide (Profile image).
// Class is created by Aarati , @Woloo on 19th July 2024.

@GlideModule
class WolooGlideModule : AppGlideModule() {

}