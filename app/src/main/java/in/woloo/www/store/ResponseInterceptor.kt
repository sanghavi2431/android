package `in`.woloo.www.store

import okhttp3.Interceptor
import okhttp3.Response

class ResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

      /*  if (response.code in listOf(404, 500)) {
            ApiEventBus.postApiError(
                response.code,
                response.message,
                request.url.toString()
            )
        }*/

        if (!response.isSuccessful) {
            ApiEventBus.postApiError(response.code, "Error: ${response.message}", request.url.toString())
        }



        return response
    }
}