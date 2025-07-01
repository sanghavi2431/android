package `in`.woloo.www.v2.blog.viewmodel

import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.trendingblog.model.CategoriesResponse
import `in`.woloo.www.v2.base.BaseViewModel
import `in`.woloo.www.v2.blog.model.BlogByCategoryResponse
import `in`.woloo.www.v2.blog.model.EcomCoinUpdateRequest
import `in`.woloo.www.v2.blog.model.EcomCoinUpdateResponse
import `in`.woloo.www.v2.blog.model.SaveUserCategoryRequest
import `in`.woloo.www.v2.blog.repository.BlogRepository
import `in`.woloo.www.v2.data.remote.*
import org.json.JSONObject

class BlogViewModel : BaseViewModel() {

    private val blogRepository: BlogRepository = BlogRepository()
    private val saveUserCategoryResponse : EventLiveData<BaseResponse<JSONObject>> = EventLiveData()
    private val categoriesResponse: EventLiveData<BaseResponse<ArrayList<CategoriesResponse.Category>>> = EventLiveData()
    private val ctaLikesResponse: EventLiveData<BaseResponse<JSONObject>> = EventLiveData()
    private val ctaFavouriteResponse: EventLiveData<BaseResponse<JSONObject>> = EventLiveData()
    private val ctaBlogReadResponse: EventLiveData<BaseResponse<JSONObject>> = EventLiveData()
    private val blogReadPointResponse: EventLiveData<BaseResponse<MessageResponse>> = EventLiveData()
    private val ecomCoinUpdateResponse: EventLiveData<BaseResponse<EcomCoinUpdateResponse>> = EventLiveData()
    private val ecomCoinFailResponse: EventLiveData<BaseResponse<JSONObject>> = EventLiveData()
    private val blogResponse: EventLiveData<BaseResponse<BlogByCategoryResponse>> = EventLiveData()

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
                    WolooApplication.setErrorMessage(data.message)
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
                    WolooApplication.setErrorMessage(data.message)
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
            WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<JSONObject>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    ctaFavouriteResponse.value = data.data
                } else {
                    WolooApplication.setErrorMessage(data.message)
                    ctaFavouriteResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeCtaFavourite(): EventLiveData<BaseResponse<JSONObject>> {
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
                    WolooApplication.setErrorMessage(data.message)
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
                    WolooApplication.setErrorMessage(data.message)
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
                    WolooApplication.setErrorMessage(data.message)
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
                    WolooApplication.setErrorMessage(data.message)
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
                    WolooApplication.setErrorMessage(data.message)
                    ecomCoinFailResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeEcomCoinFail(): EventLiveData<BaseResponse<JSONObject>> {
        return ecomCoinFailResponse
    }

    fun getBlogsForUserByCategory(category: Any, page: Int) {
        updateProgress(true)

        val request = HashMap<String, Any>()
        request.put("category",category)
        request.put("page",page)

        blogRepository.getBlogsForUserByCategory(request, object :
            WebserviceCallback<ApiResponseData<BaseResponse<BlogByCategoryResponse>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<BlogByCategoryResponse>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    blogResponse.value = data.data
                } else {
                    WolooApplication.setErrorMessage(data.message)
                    blogResponse.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeBlogsForUserByCategory(): EventLiveData<BaseResponse<BlogByCategoryResponse>> {
        return blogResponse
    }
}