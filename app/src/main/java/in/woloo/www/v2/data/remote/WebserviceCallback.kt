package `in`.woloo.www.v2.data.remote

interface WebserviceCallback<T> {
    fun onWebResponse(data: T)
}