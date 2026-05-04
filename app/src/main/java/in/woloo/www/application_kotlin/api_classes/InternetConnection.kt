package `in`.woloo.www.application_kotlin.api_classes

import android.app.Activity

interface InternetConnection {
    fun onNoInternetConnection(
        context: Activity?,
        networkAPICallModel: NetworkAPICallModel?,
        networkAPIResponseCallback: NetworkAPIResponseCallback?
    )
}
