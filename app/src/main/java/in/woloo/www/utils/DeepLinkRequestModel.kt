package `in`.woloo.www.utils

import com.google.gson.annotations.SerializedName

class DeepLinkRequestModel(
    @field:SerializedName("dynamicLinkInfo") val dynamicLinkInfo: DynamicLinkInfo,
    @field:SerializedName(
        "suffix"
    ) val suffix: Suffix
) {
    class DynamicLinkInfo(
        @field:SerializedName("iosInfo") val iosInfo: IosInfo, @field:SerializedName(
            "androidInfo"
        ) val androidInfo: AndroidInfo, @field:SerializedName("link") val link: String,
        @field:SerializedName("domainUriPrefix") val domainUriPrefix: String, @field:SerializedName(
            "socialMetaTagInfo"
        ) private val socialMetaTagInfo: SocialMetaTagInfo?
    ) {
        class IosInfo(@field:SerializedName("iosBundleId") val iosBundleId: String)

        class AndroidInfo(@field:SerializedName("androidPackageName") val androidPackageName: String)

        class SocialMetaTagInfo(
            @field:SerializedName("socialTitle") val socialTitle: String, @field:SerializedName(
                "socialDescription"
            ) val socialDescription: String, @field:SerializedName(
                "socialImageLink"
            ) val socialImageLink: String
        )
    }

    class Suffix(@field:SerializedName("option") val option: String)
}
