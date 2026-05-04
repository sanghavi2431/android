package `in`.woloo.www.services

import android.os.Build
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.GsonBuilder
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiService
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class ServicesApiServiceClientAdapter {

    val apiService: ApiService
    get() = apiServices


    init {
        createAdapter()
    }

    private fun createAdapter() {
        mOkHttpClient = OkHttpClient.Builder()
        mOkHttpClient.connectTimeout(30, TimeUnit.MINUTES)
        mOkHttpClient.readTimeout(30, TimeUnit.MINUTES)
        mOkHttpClient.writeTimeout(30, TimeUnit.MINUTES)
        mOkHttpClient.addInterceptor(RequestInterceptor())
        mOkHttpClient.followRedirects(true)
        mOkHttpClient.followSslRedirects(true)
        mOkHttpClient.retryOnConnectionFailure(true)
        mOkHttpClient.connectionPool(ConnectionPool(0, 1, TimeUnit.MILLISECONDS))
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
            .baseUrl(BuildConfig.SHOP_URL)
            .build()
        apiServices = retrofit.create(ApiService::class.java)
    }

    class RequestInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request().newBuilder()
            val originalRequest = chain.request()
            val url = originalRequest.url.toString()
//            request.addHeader("content-type", "application/json")
            val slash = "/"
            val android = "Android"



            val userAgent = android + slash + BuildConfig.VERSION_CODE + "/" + Build.VERSION.RELEASE
            request.addHeader("user-agent", userAgent)
            request.removeHeader("Accept-Encoding")
            request.addHeader("x-publishable-api-key", "pk_67ce4e90f35529f44006d2a95b330dbabbe576e43d3fd06021ca656ee00806cf")
            if(url.contains("store/customers/me")){
                request.addHeader(
                    "Authorization",
                    "Bearer " + SharedPrefSettings.getPreferences.fetchShopLoginToken()
                )
            }
            else if(url.contains("auth/customer/emailpass")){
                request.addHeader(
                    "Authorization",
                    "Bearer " + SharedPrefSettings.getPreferences.fetchShopRegisterToken()
                )
            }
            else if( url.contains("store/customers")) {
                request.addHeader(
                    "Authorization",
                    "Bearer " + SharedPrefSettings.getPreferences.fetchShopRegisterToken()
                )
            }
            else
            {
                request.addHeader(
                    "Authorization",
                    "Bearer " + SharedPrefSettings.getPreferences.fetchShopLoginToken()
                )
            }
            return chain.proceed(request.build())
        }
    }

    companion object {
    private lateinit var apiServices: ApiService
    private var mApiServiceClientAdapter: ServicesApiServiceClientAdapter? = null
    private lateinit var mOkHttpClient: OkHttpClient.Builder
    val instance: ServicesApiServiceClientAdapter
        get() {
            if (mApiServiceClientAdapter == null) {
                mApiServiceClientAdapter = ServicesApiServiceClientAdapter()
            }
            return mApiServiceClientAdapter!!
        }
}
}