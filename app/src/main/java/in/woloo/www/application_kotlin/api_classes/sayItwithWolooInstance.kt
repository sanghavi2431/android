package `in`.woloo.www.application_kotlin.api_classes

import `in`.woloo.www.application_kotlin.interfaces.sayitwithwoloo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object sayItwithWolooInstance {

    private const val BASE_URL = "https://api-digitalmessage.coitor.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Log request and response bodies
    }

    // Create an OkHttpClient with the logging interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // Attach the logging interceptor
        .build()


    val api: sayitwithwoloo by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(sayitwithwoloo::class.java)

    }

}