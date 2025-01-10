package `in`.woloo.www.application_kotlin.api_classes

import android.os.Build
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor


class ApiServiceClientAdapter {

    val apiService: ApiService
        get() = apiServices

    init {
        createAdapter()
    }

    private fun createAdapter() {
        mOkHttpClient = OkHttpClient.Builder()
        mOkHttpClient.connectTimeout(2, TimeUnit.MINUTES)
        mOkHttpClient.readTimeout(2, TimeUnit.MINUTES)
        mOkHttpClient.writeTimeout(2, TimeUnit.MINUTES)
        mOkHttpClient.addInterceptor(RequestInterceptor())
        mOkHttpClient.followRedirects(true)
        mOkHttpClient.followSslRedirects(true)
        mOkHttpClient.retryOnConnectionFailure(true)
        mOkHttpClient.cache(null).build()

//        Netcore Disabled Log
        if (BuildConfig.DEBUG) {
            mOkHttpClient.addInterceptor(ChuckerInterceptor(WolooApplication.instance!!))
        }

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        mOkHttpClient.addInterceptor(interceptor)

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .client(mOkHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BuildConfig.NODE_API_URL)
            .build()
        apiServices = retrofit.create(ApiService::class.java)
    }

    class RequestInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request().newBuilder()
//            request.addHeader("content-type", "application/json")
            val slash = "/"
            val android = "Android"
            val userAgent = android + slash + BuildConfig.VERSION_CODE + "/" + Build.VERSION.RELEASE
            request.addHeader("user-agent", userAgent)
            if(SharedPrefSettings.getPreferences.fetchToken() != null){
                request.addHeader("x-woloo-token", SharedPrefSettings.getPreferences.fetchToken()!!)
            }
            return chain.proceed(request.build())
        }
    }

    companion object {
        private lateinit var apiServices: ApiService
        private var mApiServiceClientAdapter: ApiServiceClientAdapter? = null
        private lateinit var mOkHttpClient: OkHttpClient.Builder
        val instance: ApiServiceClientAdapter
            get() {
                if (mApiServiceClientAdapter == null) {
                    mApiServiceClientAdapter = ApiServiceClientAdapter()
                }
                return mApiServiceClientAdapter!!
            }
    }
}