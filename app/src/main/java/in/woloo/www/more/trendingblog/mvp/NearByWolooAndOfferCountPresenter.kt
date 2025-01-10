package `in`.woloo.www.more.trendingblog.mvp

import android.app.Activity
import android.content.Context
import com.android.volley.VolleyError
import com.google.gson.reflect.TypeToken
import com.jetsynthesys.encryptor.JetEncryptor
import `in`.woloo.www.application_kotlin.api_classes.APIConstants
import `in`.woloo.www.application_kotlin.api_classes.JSONTagConstant
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPICallModel
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPIResponseCallback
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.models.UserProfileMergedResponse
import `in`.woloo.www.more.trendingblog.fragments.TrendBlogFragment
import `in`.woloo.www.more.trendingblog.model.CategoriesResponse
import `in`.woloo.www.more.trendingblog.model.NearByWolooAndOfferCountResponse
import `in`.woloo.www.more.trendingblog.model.blog.Blog
import `in`.woloo.www.more.trendingblog.model.blog.BlogsResponse
import `in`.woloo.www.networksUtils.NetworkAPICall
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import org.json.JSONException
import org.json.JSONObject

class NearByWolooAndOfferCountPresenter(
    private val context: Context,
    private val nearByWolooAndOfferCountView: NearByWolooAndOfferCountView
) : NetworkAPIResponseCallback {
    private val mJetEncryptor: JetEncryptor
    private val mCommonUtils: CommonUtils

    init {
        mJetEncryptor = JetEncryptor.getInstance()
        mCommonUtils = CommonUtils()
    }

    fun getNearByWolooAndOffer(lat: String?, lng: String?) {
        try {
            val mNetworkAPICall = NetworkAPICall()
            val mJsObjParam = JSONObject()
            mJsObjParam.put("lat", lat)
            mJsObjParam.put("lng", lng)
            mJsObjParam.put(JSONTagConstant.KM_RANGE, "6")
            val parserType = object : TypeToken<NearByWolooAndOfferCountResponse?>() {}.type
            val networkAPICallModel = NetworkAPICallModel(
                APIConstants.NEAR_BY_WOLOO_AND_OFFER_COUNT,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam,
                mJetEncryptor
            )
            networkAPICallModel.parserType = parserType
            networkAPICallModel.isShowProgress = true
            mNetworkAPICall.callApplicationWS(context as Activity, networkAPICallModel, this)
        } catch (e: Exception) {
            Logger.e(TrendBlogFragment.TAG, e.message.toString())
        }
    }

    val blogCategories: Unit
        get() {
            val mNetworkAPICall = NetworkAPICall()
            val parserType = object : TypeToken<CategoriesResponse?>() {}.type
            val mJsObjParam = JSONObject()
            val networkAPICallModel = NetworkAPICallModel(
                APIConstants.BLOG_CATEGORIES,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam,
                mJetEncryptor
            )
            networkAPICallModel.parserType = parserType
            networkAPICallModel.isShowProgress = true
            mNetworkAPICall.callApplicationWS(context as Activity, networkAPICallModel, this)
        }

    fun getBlogs(category: String?, page: Int) {
        val mNetworkAPICall = NetworkAPICall()
        val parserType = object : TypeToken<BlogsResponse?>() {}.type
        val mJsObjParam = JSONObject()
        try {
            mJsObjParam.put("category", category) //"non_saved_category":true,
            mJsObjParam.put("non_saved_category", true)
            mJsObjParam.put("page", page)
            val networkAPICallModel = NetworkAPICallModel(
                APIConstants.BLOGS,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam,
                mJetEncryptor
            )
            networkAPICallModel.parserType = parserType
            networkAPICallModel.isShowProgress = true
            mNetworkAPICall.callApplicationWS(context as Activity, networkAPICallModel, this)
        } catch (e: JSONException) {
            CommonUtils.printStackTrace(e)
        }
    }

    fun favouriteABlog(blog: Blog) {
        val mNetworkAPICall = NetworkAPICall()
        val parserType = object : TypeToken<JSONObject?>() {}.type
        val mJsObjParam = JSONObject()
        try {
            mJsObjParam.put("blog_id", blog.id)
            val networkAPICallModel = NetworkAPICallModel(
                APIConstants.FAVOURITE_A_BLOG,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam,
                mJetEncryptor
            )
            networkAPICallModel.parserType = parserType
            networkAPICallModel.isShowProgress = true
            mNetworkAPICall.callApplicationWS(context as Activity, networkAPICallModel, this)
        } catch (e: JSONException) {
            CommonUtils.printStackTrace(e)
        }
    }

    fun likeABlog(blog: Blog) {
        val mNetworkAPICall = NetworkAPICall()
        val parserType = object : TypeToken<JSONObject?>() {}.type
        val mJsObjParam = JSONObject()
        try {
            mJsObjParam.put("blog_id", blog.id)
            val networkAPICallModel = NetworkAPICallModel(
                APIConstants.LIKE_A_BLOG,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam,
                mJetEncryptor
            )
            networkAPICallModel.parserType = parserType
            networkAPICallModel.isShowProgress = true
            mNetworkAPICall.callApplicationWS(context as Activity, networkAPICallModel, this)
        } catch (e: JSONException) {
            CommonUtils.printStackTrace(e)
        }
    }

    fun readABlog(blog: Blog) {
        val mNetworkAPICall = NetworkAPICall()
        val parserType = object : TypeToken<JSONObject?>() {}.type
        val mJsObjParam = JSONObject()
        try {
            mJsObjParam.put("blog_id", blog.id)
            val networkAPICallModel = NetworkAPICallModel(
                APIConstants.READ_A_BLOG,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam,
                mJetEncryptor
            )
            networkAPICallModel.parserType = parserType
            networkAPICallModel.isShowProgress = true
            mNetworkAPICall.callApplicationWS(context as Activity, networkAPICallModel, this)
        } catch (e: JSONException) {
            CommonUtils.printStackTrace(e)
        }
    }

    fun addBlogReadPoints(blog: Blog) {
        val mNetworkAPICall = NetworkAPICall()
        val parserType = object : TypeToken<JSONObject?>() {}.type
        val mJsObjParam = JSONObject()
        try {
            mJsObjParam.put("blog_id", blog.id)
            val networkAPICallModel = NetworkAPICallModel(
                APIConstants.BLOG_READ_POINT,
                AppConstants.POST_REQUEST,
                AppConstants.APP_TYPE_MOBILE,
                mJsObjParam,
                mJetEncryptor
            )
            networkAPICallModel.parserType = parserType
            networkAPICallModel.isShowProgress = true
            mNetworkAPICall.callApplicationWS(context as Activity, networkAPICallModel, this)
        } catch (e: JSONException) {
            CommonUtils.printStackTrace(e)
        }
    }

    val userProfile: Unit
        get() {
            val mNetworkAPICall = NetworkAPICall()
            try {
                val mJsObjParam = JSONObject()
                val parserType = object : TypeToken<UserProfileMergedResponse?>() {}.type
                val networkAPICallModel = NetworkAPICallModel(
                    APIConstants.USER_PROFILE_MERGED,
                    AppConstants.POST_REQUEST,
                    AppConstants.APP_TYPE_MOBILE,
                    mJsObjParam,
                    mJetEncryptor
                )
                networkAPICallModel.parserType = parserType
                networkAPICallModel.isShowProgress = true
                mNetworkAPICall.callApplicationWS(context as Activity, networkAPICallModel, this)
            } catch (e: Exception) {
                Logger.e(TrendBlogFragment.TAG, e.message!!)
            }
        }

    override fun onNoInternetConnection(
        context: Activity?,
        networkAPICallModel: NetworkAPICallModel?,
        networkAPIResponseCallback: NetworkAPIResponseCallback?
    ) {
    }

    override fun onSuccessResponse(
        response: JSONObject?,
        networkAPICallModel: NetworkAPICallModel?
    ) {
        try {
            when (networkAPICallModel!!.apiURL) {
                APIConstants.NEAR_BY_WOLOO_AND_OFFER_COUNT -> {
                    val nearByWolooAndOfferCountResponse: NearByWolooAndOfferCountResponse =
                        networkAPICallModel.responseObject as NearByWolooAndOfferCountResponse
                    if (nearByWolooAndOfferCountResponse != null) {
                        nearByWolooAndOfferCountFlow(nearByWolooAndOfferCountResponse)
                    }
                }

                APIConstants.BLOG_CATEGORIES -> {
                    val categoriesResponse: CategoriesResponse =
                        networkAPICallModel.responseObject as CategoriesResponse
                    if (categoriesResponse != null && categoriesResponse.status == AppConstants.API_SUCCESS) {
                        nearByWolooAndOfferCountView.getCategories(categoriesResponse)
                    }
                }

                APIConstants.BLOGS -> {
                    val blogsResponse = networkAPICallModel.responseObject as BlogsResponse
                    if (blogsResponse != null && blogsResponse.status == AppConstants.API_SUCCESS) {
                        nearByWolooAndOfferCountView.getBlogs(blogsResponse)
                    }
                }

                APIConstants.FAVOURITE_A_BLOG -> {
                    val responseObject = networkAPICallModel.responseObject as JSONObject
                    if (responseObject != null) {
                        nearByWolooAndOfferCountView.onFavouriteABlog()
                    }
                }

                APIConstants.LIKE_A_BLOG -> {
                    val responseObject1 = networkAPICallModel.responseObject as JSONObject
                    if (responseObject1 != null) {
                        nearByWolooAndOfferCountView.onLikeABlog()
                    }
                }

                APIConstants.READ_A_BLOG -> {
                    val responseObject2 = networkAPICallModel.responseObject as JSONObject
                    if (responseObject2 != null) {
                        nearByWolooAndOfferCountView.onReadABlog()
                    }
                }

                APIConstants.BLOG_READ_POINT -> {
                    val responseObject3 = networkAPICallModel.responseObject as JSONObject
                    if (responseObject3 != null) {
                        nearByWolooAndOfferCountView.onBlogReadPointsAdded()
                    }
                }

                APIConstants.USER_PROFILE_MERGED -> {
                    val userProfile: UserProfileMergedResponse =
                        networkAPICallModel.responseObject as UserProfileMergedResponse
                    if (userProfile != null) {
                        nearByWolooAndOfferCountView.setUserProfileMergedResponse(userProfile)
                    }
                }
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    private fun nearByWolooAndOfferCountFlow(nearByWolooAndOfferCountResponse: NearByWolooAndOfferCountResponse?) {
        try {
            if (nearByWolooAndOfferCountResponse != null && nearByWolooAndOfferCountResponse.status == AppConstants.API_SUCCESS) {
                nearByWolooAndOfferCountView.nearByWolooAndOfferCountResponse(
                    nearByWolooAndOfferCountResponse
                )
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    override fun onFailure(volleyError: VolleyError?, networkAPICallModel: NetworkAPICallModel?) {}
    override fun onTimeOutConnection(
        context: Activity?,
        networkAPICallModel: NetworkAPICallModel?,
        networkAPIResponseCallback: NetworkAPIResponseCallback?
    ) {
    }
}
