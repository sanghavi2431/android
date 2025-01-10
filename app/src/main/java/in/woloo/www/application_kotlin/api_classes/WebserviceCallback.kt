package `in`.woloo.www.application_kotlin.api_classes

interface WebserviceCallback<T> {
    fun onWebResponse(data: T)
}