package `in`.woloo.www.more.trendingblog.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.EventLiveData
import `in`.woloo.www.application_kotlin.api_classes.MessageResponse
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.application_kotlin.view_models.BaseViewModel
import `in`.woloo.www.blogs_module.BlockBlogRequest
import `in`.woloo.www.blogs_module.CommentRequest
import `in`.woloo.www.blogs_module.CommentResponse
import `in`.woloo.www.blogs_module.FavouriteResult
import `in`.woloo.www.more.trendingblog.model.CategoriesResponse
import `in`.woloo.www.more.trendingblog.model.BlogByCategoryResponse
import `in`.woloo.www.more.trendingblog.model.EcomCoinUpdateRequest
import `in`.woloo.www.more.trendingblog.model.EcomCoinUpdateResponse
import `in`.woloo.www.more.trendingblog.model.SaveUserCategoryRequest
import `in`.woloo.www.more.trendingblog.model.blog.Blog
import `in`.woloo.www.more.trendingblog.repository.BlogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject

class BlogViewModel : BaseViewModel() {

    private val blogRepository: BlogRepository = BlogRepository()
    private val saveUserCategoryResponse : EventLiveData<BaseResponse<JSONObject>> = EventLiveData()
    private val categoriesResponse: EventLiveData<BaseResponse<ArrayList<CategoriesResponse.Category>>> = EventLiveData()
    private val ctaLikesResponse: EventLiveData<BaseResponse<JSONObject>> = EventLiveData()
    private val ctaFavouriteResponse: EventLiveData<BaseResponse<FavouriteResult>> = EventLiveData()
    private val ctaBlogReadResponse: EventLiveData<BaseResponse<JSONObject>> = EventLiveData()
    private val blogReadPointResponse: EventLiveData<BaseResponse<MessageResponse>> = EventLiveData()
    private val ecomCoinUpdateResponse: EventLiveData<BaseResponse<EcomCoinUpdateResponse>> = EventLiveData()
    private val ecomCoinFailResponse: EventLiveData<BaseResponse<JSONObject>> = EventLiveData()


    val blogList = mutableStateListOf<Blog>()
    private val _blogResponse = EventLiveData<BaseResponse<BlogByCategoryResponse>>()
    val blogResponse: EventLiveData<BaseResponse<BlogByCategoryResponse>> get() = _blogResponse
    private val _blogDetailResponse = EventLiveData<BaseResponse<ArrayList<Blog>>>()
    val blogDetailResponse: EventLiveData<BaseResponse<ArrayList<Blog>>> get() = _blogDetailResponse
    var isLoading by mutableStateOf(false)
    var page by mutableStateOf(1)
    var hasMoreData by mutableStateOf(true)
    private var selectedCategory: Any? = null


    private val _commentResponseFlow = MutableStateFlow<ApiResponseData<BaseResponse<CommentResponse>>?>(null)
    val commentResponseFlow: StateFlow<ApiResponseData<BaseResponse<CommentResponse>>?> = _commentResponseFlow

    private val _commentListResponseFlow = MutableStateFlow<ApiResponseData<BaseResponse<ArrayList<CommentResponse.Data>>>?>(null)
    val commentListResponseFlow: StateFlow<ApiResponseData<BaseResponse<ArrayList<CommentResponse.Data>>>?> = _commentListResponseFlow

    private val _blockResponseFlow = MutableStateFlow<ApiResponseData<BaseResponse<CommentResponse>>?>(null)
    val blockResponseFlow: StateFlow<ApiResponseData<BaseResponse<CommentResponse>>?> = _blockResponseFlow



    fun saveUserCategory(categoryList : ArrayList<Int>) {
        updateProgress(true)
        val request = SaveUserCategoryRequest()
        request.categories = categoryList
        blogRepository.saveUserCategory(request,object :
            WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<JSONObject>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    saveUserCategoryResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    saveUserCategoryResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeSaveUserCategory(): EventLiveData<BaseResponse<JSONObject>> {
        return saveUserCategoryResponse
    }

    fun getCategories() {
        updateProgress(true)
        blogRepository.getCategories(object :
            WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<CategoriesResponse.Category>>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<ArrayList<CategoriesResponse.Category>>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    categoriesResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    categoriesResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeGetCategories(): EventLiveData<BaseResponse<ArrayList<CategoriesResponse.Category>>> {
        return categoriesResponse
    }

    fun ctaFavourite(blogId: String) {
        updateProgress(true)
        blogRepository.ctaFavourite(blogId, object :
            WebserviceCallback<ApiResponseData<BaseResponse<FavouriteResult>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<FavouriteResult>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    ctaFavouriteResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    ctaFavouriteResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeCtaFavourite(): EventLiveData<BaseResponse<FavouriteResult>> {
        return ctaFavouriteResponse
    }

    fun ctaLikes(blogId: String) {
        updateProgress(true)
        blogRepository.ctaLikes(blogId, object :
            WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<JSONObject>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    ctaLikesResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    ctaLikesResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeCtaLikes(): EventLiveData<BaseResponse<JSONObject>> {
        return ctaLikesResponse
    }

    fun ctaBlogRead(blogId: String) {
        updateProgress(true)
        blogRepository.ctaBlogRead(blogId, object :
            WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<JSONObject>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    ctaBlogReadResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    ctaBlogReadResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeCtaBlogRead(): EventLiveData<BaseResponse<JSONObject>> {
        return ctaBlogReadResponse
    }

    fun blogReadPoint(blogId: String) {
        updateProgress(true)
        blogRepository.blogReadPoint(blogId, object :
            WebserviceCallback<ApiResponseData<BaseResponse<MessageResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<MessageResponse>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    blogReadPointResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    blogReadPointResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeBlogReadPoint(): EventLiveData<BaseResponse<MessageResponse>> {
        return blogReadPointResponse
    }

    fun ecomCoinUpdate(type : String, coins : Int, orderid: String) {
        val request = EcomCoinUpdateRequest()
        request.orderId = orderid
        request.type = type
        request.coins = coins
        updateProgress(true)
        blogRepository.ecomCoinUpdate(request, object :
            WebserviceCallback<ApiResponseData<BaseResponse<EcomCoinUpdateResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<EcomCoinUpdateResponse>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    ecomCoinUpdateResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    ecomCoinUpdateResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeEcomCoinUpdate(): EventLiveData<BaseResponse<EcomCoinUpdateResponse>> {
        return ecomCoinUpdateResponse
    }

    fun ecomCoinFail(transactionId: Int) {
        updateProgress(true)
        blogRepository.ecomCoinFail(transactionId, object :
            WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<JSONObject>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    ecomCoinFailResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    ecomCoinFailResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeEcomCoinFail(): EventLiveData<BaseResponse<JSONObject>> {
        return ecomCoinFailResponse
    }

    fun getBlogsForUserByCategory(category: Any, page: Int, blog_type: String) {
        updateProgress(true)

        val request = HashMap<String, Any>()
        request.put("category",category)
        request.put("page",page)
        request.put("shop_display" , blog_type)

        blogRepository.getBlogsForUserByCategory(request, object :
            WebserviceCallback<ApiResponseData<BaseResponse<BlogByCategoryResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<BlogByCategoryResponse>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    blogResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    blogResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

   /* fun getBlogsForShopByCategory(category: Any, page: Int, blog_type: String) {
        updateProgress(true)

        val request = HashMap<String, Any>()
        request.put("category",category)
        request.put("page",page)
        request.put("shop_display" , blog_type)

        blogRepository.getBlogsForShopByCategory(request, object :
            WebserviceCallback<ApiResponseData<BaseResponse<BlogByCategoryResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<BlogByCategoryResponse>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    blogResponse.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    blogResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }
*/

    fun getBlogsForShopByCategory(category: Any, page: Int, blogType: String, reset: Boolean = false) {
        if (isLoading) return
        isLoading = true

        val request = hashMapOf(
            "category" to category,
            "page" to page,
            "shop_display" to blogType
        )

        blogRepository.getBlogsForShopByCategory(request, object :
            WebserviceCallback<ApiResponseData<BaseResponse<BlogByCategoryResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<BlogByCategoryResponse>>) {
                isLoading = false
                if (data.status == ApiResponseData.API_SUCCESS) {
                    val newBlogs = data.data?.data?.blogs ?: emptyList()
                    /*if (reset) blogList.clear()
                    blogList.addAll(newBlogs)*/
                    if (reset) blogList.clear()
                    val existingIds = blogList.map { it.id }.toSet()
                    val uniqueNewBlogs = newBlogs.filterNot { it.id in existingIds }
                    blogList.addAll(uniqueNewBlogs)

                    blogResponse.postValue(data.data)
                   // hasMoreData = newBlogs.size >= 8
                    hasMoreData = uniqueNewBlogs.size >= 9
                }
            }
        })
    }

    fun observeBlogsForUserByCategory(): EventLiveData<BaseResponse<BlogByCategoryResponse>> {
        return blogResponse
    }

    fun postComment(request: CommentRequest) {
        blogRepository.postComment(request, object : WebserviceCallback<ApiResponseData<BaseResponse<CommentResponse>>> {
            override fun onWebResponse(result: ApiResponseData<BaseResponse<CommentResponse>>) {
                _commentResponseFlow.value = result
            }
        })
    }


    fun observeCommentAdded(): StateFlow<ApiResponseData<BaseResponse<CommentResponse>>?> {
        return commentResponseFlow
    }


    fun getCommentListBlogWise(blogId: String){
        blogRepository.getCommentListBlogWise(blogId, object : WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<CommentResponse.Data>>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<ArrayList<CommentResponse.Data>>>) {
                _commentListResponseFlow.value = data
            }
        })
    }

    fun observeCommentListBlogWise(): StateFlow<ApiResponseData<BaseResponse<ArrayList<CommentResponse.Data>>>?> {
        return commentListResponseFlow
    }


    fun blockBlog(blogId: BlockBlogRequest) {
        blogRepository.blockBlog(blogId, object : WebserviceCallback<ApiResponseData<BaseResponse<CommentResponse>>> {
            override fun onWebResponse(result: ApiResponseData<BaseResponse<CommentResponse>>) {
                _blockResponseFlow.value = result
            }
        })
    }

    fun getBlogDetails(blogId: BlockBlogRequest) {

        blogRepository.getBlogDetails(blogId, object :
            WebserviceCallback<ApiResponseData<BaseResponse<ArrayList<Blog>>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<ArrayList<Blog>>>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    blogDetailResponse.postValue(data.data)
                }
                else {
                    WolooApplication.errorMessage = data.message
                    notifyNetworkError(data)
                }

            }
        })
    }

    fun observeBlogDetails(): EventLiveData<BaseResponse<ArrayList<Blog>>> {
        return blogDetailResponse
    }

}