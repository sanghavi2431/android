package `in`.woloo.www.application_kotlin.api_classes

class ApiResponseData<T> {

    companion object {
        const val API_SUCCESS = 200
        const val API_NO_NETWORK = 100
        const val API_FAILURE = 400
    }

    var status: Int = 0
    var data: T? = null
    var message: String = ""

}