package `in`.woloo.www.application_kotlin.model.lists_models

import com.google.gson.annotations.SerializedName

class LocaleRequest {

    @SerializedName("locale")
    var locale: Locale? = null

    class Locale {
        @SerializedName("packageName")
        var packageName: String = ""

        @SerializedName("platform")
        var platform: String = ""
    }

}