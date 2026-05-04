package `in`.woloo.www.store


import android.os.Build
import android.util.Log
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor


class StoreApiServiceCustomerAdapter {

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
            //  if(SharedPrefSettings.getPreferences.fetchToken() != null){
            request.removeHeader("Accept-Encoding")
            request.addHeader("x-publishable-api-key", "pk_03b79693816aae4cb87568dc50b7efaa48e0d51b201040f46ef4528839078f08")
            //  }
            Log.d("aarati token bearer" , SharedPrefSettings.getPreferences.fetchShopLoginToken().toString())

                    request.addHeader(
                        "Authorization",
                        "Bearer " + SharedPrefSettings.getPreferences.fetchShopLoginToken()
                    )

            return chain.proceed(request.build())
        }
    }

    companion object {
        private lateinit var apiServices: ApiService
        private var mApiServiceClientAdapter: StoreApiServiceCustomerAdapter? = null
        private lateinit var mOkHttpClient: OkHttpClient.Builder
        val instance: StoreApiServiceCustomerAdapter
            get() {
                if (mApiServiceClientAdapter == null) {
                    mApiServiceClientAdapter = StoreApiServiceCustomerAdapter()
                }
                return mApiServiceClientAdapter!!
            }
    }
}