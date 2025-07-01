package `in`.woloo.www.v2.blog.repository

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.trendingblog.model.CategoriesResponse
import `in`.woloo.www.v2.base.BaseRepository
import `in`.woloo.www.v2.blog.model.BlogByCategoryResponse
import `in`.woloo.www.v2.blog.model.EcomCoinUpdateRequest
import `in`.woloo.www.v2.blog.model.EcomCoinUpdateResponse
import `in`.woloo.www.v2.blog.model.SaveUserCategoryRequest
import `in`.woloo.www.v2.data.remote.*
import `in`.woloo.www.v2.util.NetworkUtils
import org.json.JSONObject
import retrofit2.Call

class BlogRepository : BaseRepository() {

    fun saveUserCategory(request: SaveUserCategoryRequest, webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<JSONObject>> =
                    apiService.saveUserCategory(request)
                val callback: ApiServiceCallback<BaseResponse<JSONObject>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            val data = ApiResponseData<BaseResponse<JSONObject>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getCategories(webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<CategoriesResponse.Category>>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<ArrayList<CategoriesResponse.Category>>> =
                    apiService.getCategories()
                val callback: ApiServiceCallback<BaseResponse<ArrayList<CategoriesResponse.Category>>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            val data = ApiResponseData<BaseResponse<ArrayList<CategoriesResponse.Category>>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun ctaFavourite(blogId: String, webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<JSONObject>> =
                    apiService.ctaFavourite(blogId)
                val callback: ApiServiceCallback<BaseResponse<JSONObject>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            val data = ApiResponseData<BaseResponse<JSONObject>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun ctaLikes(blogId: String, webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<JSONObject>> =
                    apiService.ctaLikes(blogId)
                val callback: ApiServiceCallback<BaseResponse<JSONObject>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            val data = ApiResponseData<BaseResponse<JSONObject>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun ctaBlogRead(blogId: String, webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<JSONObject>> =
                    apiService.ctaBlogRead(blogId)
                val callback: ApiServiceCallback<BaseResponse<JSONObject>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            val data = ApiResponseData<BaseResponse<JSONObject>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun blogReadPoint(blogId: String, webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<MessageResponse>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<MessageResponse>> =
                    apiService.blogReadPoint(blogId)
                val callback: ApiServiceCallback<BaseResponse<MessageResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            val data = ApiResponseData<BaseResponse<MessageResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun ecomCoinUpdate(request: EcomCoinUpdateRequest, webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<EcomCoinUpdateResponse>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<EcomCoinUpdateResponse>> =
                    apiService.ecomCoinUpdate(request)
                val callback: ApiServiceCallback<BaseResponse<EcomCoinUpdateResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            val data = ApiResponseData<BaseResponse<EcomCoinUpdateResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun ecomCoinFail(transactionId: Int, webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<JSONObject>> =
                    apiService.ecomCoinFail(transactionId)
                val callback: ApiServiceCallback<BaseResponse<JSONObject>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            val data = ApiResponseData<BaseResponse<JSONObject>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getBlogsForUserByCategory(request: HashMap<String, Any>, webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<BlogByCategoryResponse>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.getInstance())) {
            try {
                val call: Call<BaseResponse<BlogByCategoryResponse>> =
                    apiService.getBlogsForUserByCategory(request)
                val callback: ApiServiceCallback<BaseResponse<BlogByCategoryResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            val data = ApiResponseData<BaseResponse<BlogByCategoryResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }
}
