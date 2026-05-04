package `in`.woloo.www.application_kotlin.api_classes

import retrofit2.Response

interface WebserviceCallback<T> {
    fun onWebResponse(data: T)
}