package `in`.woloo.www.more.trendingblog.repository

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceCallback
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.MessageResponse
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.application_kotlin.repositories.BaseRepository
import `in`.woloo.www.application_kotlin.utilities.NetworkUtils
import `in`.woloo.www.blogs_module.BlockBlogRequest
import `in`.woloo.www.blogs_module.CommentRequest
import `in`.woloo.www.blogs_module.CommentResponse
import `in`.woloo.www.blogs_module.FavouriteResult
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.trendingblog.model.CategoriesResponse
import `in`.woloo.www.more.trendingblog.model.BlogByCategoryResponse
import `in`.woloo.www.more.trendingblog.model.EcomCoinUpdateRequest
import `in`.woloo.www.more.trendingblog.model.EcomCoinUpdateResponse
import `in`.woloo.www.more.trendingblog.model.SaveUserCategoryRequest
import `in`.woloo.www.more.trendingblog.model.blog.Blog
import org.json.JSONObject
import retrofit2.Call
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.Dispatchers

class BlogRepository : BaseRepository() {

    fun saveUserCategory(request: SaveUserCategoryRequest, webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<JSONObject>> =
                    apiService.saveUserCategory(request)
                val callback: ApiServiceCallback<BaseResponse<JSONObject>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<JSONObject>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getCategories(webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<CategoriesResponse.Category>>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<ArrayList<CategoriesResponse.Category>>> =
                    apiService.getCategories()
                val callback: ApiServiceCallback<BaseResponse<ArrayList<CategoriesResponse.Category>>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                 CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<ArrayList<CategoriesResponse.Category>>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun ctaFavourite(blogId: String,webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<FavouriteResult>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<FavouriteResult>> =
                    apiService.ctaFavourite(blogId)
                val callback: ApiServiceCallback<BaseResponse<FavouriteResult>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<FavouriteResult>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun ctaLikes(blogId: String,webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<JSONObject>> =
                    apiService.ctaLikes(blogId)
                val callback: ApiServiceCallback<BaseResponse<JSONObject>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<JSONObject>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun ctaBlogRead(blogId: String,webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<JSONObject>> =
                    apiService.ctaBlogRead(blogId)
                val callback: ApiServiceCallback<BaseResponse<JSONObject>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<JSONObject>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun blogReadPoint(blogId: String,webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<MessageResponse>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<MessageResponse>> =
                    apiService.blogReadPoint(blogId)
                val callback: ApiServiceCallback<BaseResponse<MessageResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<MessageResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun ecomCoinUpdate(request: EcomCoinUpdateRequest, webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<EcomCoinUpdateResponse>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<EcomCoinUpdateResponse>> =
                    apiService.ecomCoinUpdate(request)
                val callback: ApiServiceCallback<BaseResponse<EcomCoinUpdateResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<EcomCoinUpdateResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun ecomCoinFail(transactionId: Int,webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<JSONObject>> =
                    apiService.ecomCoinFail(transactionId)
                val callback: ApiServiceCallback<BaseResponse<JSONObject>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<JSONObject>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getBlogsForUserByCategory(request: HashMap<String, Any>,webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<BlogByCategoryResponse>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<BlogByCategoryResponse>> =
                    apiService.getBlogsForUserByCategory(request)
                val callback: ApiServiceCallback<BaseResponse<BlogByCategoryResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<BlogByCategoryResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getBlogsForShopByCategory(request: HashMap<String, Any>,webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<BlogByCategoryResponse>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<BlogByCategoryResponse>> =
                    apiService.getBlogsForShopByCategory(request)
                val callback: ApiServiceCallback<BaseResponse<BlogByCategoryResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<BlogByCategoryResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun postComment(
        request: CommentRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<CommentResponse>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<CommentResponse>> = apiService.postComment(
                   request
                )
                val callback: ApiServiceCallback<BaseResponse<CommentResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            val data = ApiResponseData<BaseResponse<CommentResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getCommentListBlogWise(
      blogId: String ,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<CommentResponse.Data>>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<ArrayList<CommentResponse.Data>>> = apiService.getCommentListBlogWise(
                    blogId
                )
                val callback: ApiServiceCallback<BaseResponse<ArrayList<CommentResponse.Data>>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            val data = ApiResponseData<BaseResponse<ArrayList<CommentResponse.Data>>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun blockBlog(
        blogId: BlockBlogRequest,
        webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<CommentResponse>>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<CommentResponse>> = apiService.blockBlog(
                    blogId
                )
                val callback: ApiServiceCallback<BaseResponse<CommentResponse>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            val data = ApiResponseData<BaseResponse<CommentResponse>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun getBlogDetails(request: BlockBlogRequest,webserviceCallback: WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<Blog>>>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<ArrayList<Blog>>> =
                    apiService.getBlogDetails(request)
                val callback: ApiServiceCallback<BaseResponse<ArrayList<Blog>>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception){
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<BaseResponse<ArrayList<Blog>>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

}