package `in`.woloo.www.v2.splash.model

import com.google.gson.annotations.SerializedName

class LocaleRequest {

    @SerializedName("locale")
    var locale: LocaleRequest.Locale? = null

    class Locale {
        @SerializedName("packageName")
        var packageName: String = ""

        @SerializedName("platform")
        var platform: String = ""
    }

}