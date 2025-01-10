package `in`.woloo.www.application_kotlin.api_classes

import `in`.woloo.www.application_kotlin.model.server_request.SendOtpRequest
import `in`.woloo.www.application_kotlin.model.server_request.VerifyOtpRequest
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.mapdirection.GetDistance
import `in`.woloo.www.more.models.UserCoinHistoryModel
import `in`.woloo.www.more.my_history.model.MyHistoryResponse
import `in`.woloo.www.more.period_tracker.model.PeriodTrackerResponse
import `in`.woloo.www.more.refer_woloo_host.model.ReferredWolooListResponse
import `in`.woloo.www.application_kotlin.model.server_response.ReviewListResponse
import `in`.woloo.www.application_kotlin.model.server_response.ReviewOptionsResponse
import `in`.woloo.www.application_kotlin.model.server_response.AuthConfigResponse
import `in`.woloo.www.application_kotlin.model.server_response.PendingReviewStatusResponse
import `in`.woloo.www.application_kotlin.model.server_response.SendOtpResponse
import `in`.woloo.www.application_kotlin.model.server_response.VerifyOtpResponse
import `in`.woloo.www.more.subscribe.models.GetSubscriptionDetailsResponse
import `in`.woloo.www.more.subscribe.models.InitSubscriptionResponse
import `in`.woloo.www.more.subscribe.models.PlanResponse
import `in`.woloo.www.more.trendingblog.model.CategoriesResponse
import `in`.woloo.www.more.trendingblog.model.NearByWolooAndOfferCountResponse

import `in`.woloo.www.application_kotlin.model.server_request.EnrouteRequest
import `in`.woloo.www.application_kotlin.model.lists_models.ReverseGeocodeItem
import `in`.woloo.www.application_kotlin.model.server_request.ReverseGeocodeRequest
import `in`.woloo.www.more.period_tracker.periodtracker.model.PeriodTrackerRequest
import `in`.woloo.www.more.editprofile.profile.model.EditProfileResponse
import `in`.woloo.www.more.editprofile.profile.model.ShowProfileResponse
import `in`.woloo.www.more.editprofile.profile.model.UserProfile
import `in`.woloo.www.application_kotlin.model.server_request.SubmitReviewRequest
import `in`.woloo.www.application_kotlin.model.lists_models.LocaleRequest
import `in`.woloo.www.application_kotlin.model.lists_models.Voucher
import `in`.woloo.www.application_kotlin.model.server_request.NearByWolooAndOfferCountRequest
import `in`.woloo.www.application_kotlin.model.server_request.NearbyWolooRequest
import `in`.woloo.www.application_kotlin.model.server_request.ReviewListRequest
import `in`.woloo.www.application_kotlin.model.server_request.VoucherRequest
import `in`.woloo.www.application_kotlin.model.server_request.WolooEngagementRequest
import `in`.woloo.www.more.giftcard.giftcard.model.AddCoinsRequest
import `in`.woloo.www.more.giftcard.giftcard.model.AddCoinsResponse
import `in`.woloo.www.more.giftcard.giftcard.model.UserCoins
import `in`.woloo.www.more.giftcard.giftcard.model.ValidateGiftCardResponse
import `in`.woloo.www.more.subscribe.subscription.model.InitSubscriptionRequest
import `in`.woloo.www.more.subscribe.subscription.model.SubmitSubscriptionPurchaseRequest
import `in`.woloo.www.more.thirstreminder.thirstReminder.model.ThirstReminderRequest
import `in`.woloo.www.more.thirstreminder.thirstReminder.model.ThirstReminderResponse
import `in`.woloo.www.application_kotlin.model.server_response.WahCertificateResponse
import `in`.woloo.www.more.trendingblog.model.BlogByCategoryResponse
import `in`.woloo.www.more.trendingblog.model.EcomCoinUpdateRequest
import `in`.woloo.www.more.trendingblog.model.EcomCoinUpdateResponse
import `in`.woloo.www.more.trendingblog.model.SaveUserCategoryRequest
import `in`.woloo.www.v2.invite.model.InviteRequest
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @POST("/api/wolooHost/nearBy")
    fun getNearbyWoloos(@Body request: NearbyWolooRequest): Call<BaseResponse<ArrayList<NearByStoreResponse.Data>>>

    @POST("/api/wolooGuest/sendOTP")
    fun sendOtp(@Body request: SendOtpRequest): Call<BaseResponse<SendOtpResponse>>

    @POST("/api/wolooGuest/verifyOTP")
    fun verifyOtp(@Body request: VerifyOtpRequest): Call<BaseResponse<VerifyOtpResponse>>

    @POST("/api/wolooGuest/appConfig")
    fun appConfig(@Body request: LocaleRequest): Call<BaseResponse<AuthConfigResponse.Data>>
    @GET
    fun getDirections(@Url url: String): Call<GetDistance>

    @POST("/api/voucher/apply")
    fun applyVoucher(@Body voucher: VoucherRequest): Call<BaseResponse<Voucher>>

    @PUT("/api/wolooGuest")
    fun updateProfile(@Body body: RequestBody): Call<BaseResponse<EditProfileResponse>>

    @GET("/api/wolooGuest/profileStatus")
    fun showProfile(@Query("user_id") userId: String): Call<BaseResponse<ShowProfileResponse>>

    @POST("/api/wolooHost/addCoins")
    fun addCoin(@Body request: AddCoinsRequest): Call<BaseResponse<AddCoinsResponse>>


    @GET("/api/voucher/UserGiftPopUp")
    fun verifyGiftCardId(@Query("id") giftCardId: String): Call<BaseResponse<ValidateGiftCardResponse>>

    @GET("/api/subscription/mySubscription")
    fun getMySubscription(): Call<BaseResponse<GetSubscriptionDetailsResponse.Data>>

    @GET("/api/subscription/getPlan")
    fun getSubscriptionPlan(): Call<BaseResponse<ArrayList<PlanResponse.Data>>>

    @POST("/api/subscription/initSubscriptionByOrder")
    fun initSubscriptionByOrder(@Body request : InitSubscriptionRequest): Call<BaseResponse<InitSubscriptionResponse.Data>>

    @POST("/api/subscription/submitSubscriptionPurchase")
    fun submitSubscriptionPurchase(@Body request : SubmitSubscriptionPurchaseRequest): Call<BaseResponse<InitSubscriptionResponse>>

    @POST("/api/wolooGuest/thirstReminder")
    fun thirstRemainder(@Body request : ThirstReminderRequest): Call<BaseResponse<ThirstReminderResponse>>

    @POST("/api/wolooHost/nearByWolooAndOfferCount")
    fun getNearByWolooAndOfferCount(@Body request : NearByWolooAndOfferCountRequest): Call<BaseResponse<NearByWolooAndOfferCountResponse.Data>>

    @POST("/api/wolooGuest/periodtracker")
    fun periodTracker(@Body request : PeriodTrackerRequest): Call<BaseResponse<PeriodTrackerResponse.Data>>

    @GET("/api/wolooGuest/viewperiodtracker")
    fun getPeriodTracker(): Call<BaseResponse<PeriodTrackerResponse.Data>>

    @POST("/api/wolooHost/enroute")
    fun getEnrouteWoloo(@Body request : EnrouteRequest): Call<BaseResponse<ArrayList<NearByStoreResponse.Data>>>

    @GET("/api/wolooGuest/wolooNavigationReward")
    fun getWolooNavigationReward(@Query("wolooId") wolooId : Int): Call<BaseResponse<JSONObject>>

    //api/wolooHost/user_coins
    @GET("/api/wolooHost/user_coins")
    fun getUserCoins(): Call<BaseResponse<UserCoins>>

    @GET("/api/wolooGuest/profile")
    fun getUserProfile(@Query("id") userId : String): Call<BaseResponse<UserProfile>>

    @POST("/api/wolooHost/woloo_engagements")
    fun wolooEngagements(@Body request : WolooEngagementRequest): Call<BaseResponse<JSONObject>>

    @POST("/api/wolooGuest/getReviewList")
    fun getReviewList(@Body request : ReviewListRequest): Call<BaseResponse<ReviewListResponse.Data>>

    @GET("/api/wolooGuest/coinHistory") //need to implement
    fun getCoinHistory(@Query("pageIndex") pageNumber : Int): Call<BaseResponse<UserCoinHistoryModel.Data>>


    @GET("/api/wolooHost/wolooRewardHistory")
    fun getWolooRewardHistory(@Query("pageNumber") pageNumber : Int): Call<BaseResponse<MyHistoryResponse.Data>>

    @POST("/api/wolooGuest/reverseGeocoding")
    fun reverseGeocoding(@Body request : ReverseGeocodeRequest): Call<BaseResponse<ArrayList<ReverseGeocodeItem>>>

    @GET("/api/wolooGuest/getPendingReviewStatus")
    fun getPendingReviewStatus(): Call<BaseResponse<PendingReviewStatusResponse.Data>>


    @POST("/api/wolooHost/recommendWoloo")
    fun recommendWoloo(@Body body: RequestBody): Call<BaseResponse<MessageResponse>>

    @POST("/api/wolooHost/userRecommendWoloo")
    fun getRecommendWolooList(): Call<BaseResponse<ArrayList<ReferredWolooListResponse.DataItem>>>

    @GET("/api/wolooGuest/getReviewOptions")
    fun getReviewOptions(): Call<BaseResponse<ReviewOptionsResponse.Data>>

    @POST("/api/wolooHost/submitReview")
    fun submitReview(@Body request: SubmitReviewRequest): Call<BaseResponse<JSONObject>>

    @GET("/api/wolooGuest/myOffers")
    fun myOffers(): Call<BaseResponse<ArrayList<NearByStoreResponse.Data>>>

    @POST("/api/wolooGuest/updateDeviceToken")
    fun updateDeviceToken(@Body request : HashMap<String,String>): Call<BaseResponse<String>>

    @POST("/api/wolooHost/addWoloo")
    fun addWoloo(@Body body: RequestBody): Call<BaseResponse<MessageResponse>>

    @POST("/api/wolooGuest/invite")
    fun invite(@Body request : InviteRequest): Call<BaseResponse<JSONObject>>

    @GET("/api/wolooGuest/scanWoloo")
    fun scanWoloo(@Query("name") name : String): Call<BaseResponse<MessageResponse>>

    @GET("/api/wolooGuest/wahcertificate")
    fun wahCertificate(@Query("woloo_id") wolooId : String): Call<BaseResponse<WahCertificateResponse>>

    @GET("/api/wolooGuest/redeemOffer")
    fun redeemOffer(@Query("offer_id") offerId : Int): Call<BaseResponse<MessageResponse>>

//BLOG APIs
    @GET("/api/blog/ctaFavourite")
    fun ctaFavourite(@Query("blog_id") blogId : String): Call<BaseResponse<JSONObject>>

    @GET("/api/blog/ctaLikes")
    fun ctaLikes(@Query("blog_id") blogId : String): Call<BaseResponse<JSONObject>>

    @GET("/api/blog/ctaBlogRead")
    fun ctaBlogRead(@Query("blog_id") blogId : String): Call<BaseResponse<JSONObject>>

    @GET("/api/blog/blogReadPoint")
    fun blogReadPoint(@Query("blog_id") blogId : String): Call<BaseResponse<MessageResponse>>

    @GET("/api/blog/getCategories")
    fun getCategories(): Call<BaseResponse<ArrayList<CategoriesResponse.Category>>>

    @POST("/api/blog/saveUserCategory")
    fun saveUserCategory(@Body request : SaveUserCategoryRequest): Call<BaseResponse<JSONObject>>

    @POST("/api/blog/ecomCoinUpdate")
    fun ecomCoinUpdate(@Body request : EcomCoinUpdateRequest): Call<BaseResponse<EcomCoinUpdateResponse>>

    @GET("/api/blog/ecomTransactionFail")
    fun ecomCoinFail(@Query("transaction_id") transactionId : Int): Call<BaseResponse<JSONObject>>

    @POST("/api/blog/getBlogsForUserByCategory")
    fun getBlogsForUserByCategory(@Body request : HashMap<String, Any>): Call<BaseResponse<BlogByCategoryResponse>>

    @POST("/api/blog/getBlogsForShop")
    fun getBlogsForShopByCategory(@Body request : HashMap<String, Any>): Call<BaseResponse<BlogByCategoryResponse>>

}