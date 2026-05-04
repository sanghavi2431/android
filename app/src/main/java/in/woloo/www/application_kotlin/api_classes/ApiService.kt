package `in`.woloo.www.application_kotlin.api_classes

import `in`.woloo.www.application_kotlin.model.server_request.SendOtpRequest
import `in`.woloo.www.application_kotlin.model.server_request.VerifyOtpRequest
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.mapdirection.GetDistance
import `in`.woloo.www.application_kotlin.model.CreditCoinsRequest
import `in`.woloo.www.application_kotlin.model.RateReviiewRequest
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
import `in`.woloo.www.application_kotlin.model.server_request.SubmitReviewRequest
import `in`.woloo.www.application_kotlin.model.lists_models.LocaleRequest
import `in`.woloo.www.application_kotlin.model.lists_models.Voucher
import `in`.woloo.www.application_kotlin.model.server_request.NearByStoreResultsWrapper
import `in`.woloo.www.application_kotlin.model.server_request.NearByWolooAndOfferCountRequest
import `in`.woloo.www.application_kotlin.model.server_request.NearbyWolooRequest
import `in`.woloo.www.application_kotlin.model.server_request.PurchaseNowRequest
import `in`.woloo.www.application_kotlin.model.server_request.ReviewListRequest
import `in`.woloo.www.application_kotlin.model.server_request.SearchWolooRequest
import `in`.woloo.www.application_kotlin.model.server_request.VoucherRequest
import `in`.woloo.www.application_kotlin.model.server_request.WolooEngagementRequest
import `in`.woloo.www.application_kotlin.model.server_response.PurchaseNowResponse
import `in`.woloo.www.more.giftcard.giftcard.model.AddCoinsRequest
import `in`.woloo.www.more.giftcard.giftcard.model.AddCoinsResponse
import `in`.woloo.www.more.giftcard.giftcard.model.UserCoins
import `in`.woloo.www.more.giftcard.giftcard.model.ValidateGiftCardResponse
import `in`.woloo.www.more.subscribe.subscription.model.InitSubscriptionRequest
import `in`.woloo.www.more.subscribe.subscription.model.SubmitSubscriptionPurchaseRequest
import `in`.woloo.www.more.thirstreminder.thirstReminder.model.ThirstReminderRequest
import `in`.woloo.www.more.thirstreminder.thirstReminder.model.ThirstReminderResponse
import `in`.woloo.www.application_kotlin.model.server_response.WahCertificateResponse
import `in`.woloo.www.application_kotlin.model.server_response.WebViewResponse
import `in`.woloo.www.blogs_module.BlockBlogRequest
import `in`.woloo.www.blogs_module.CommentRequest
import `in`.woloo.www.blogs_module.CommentResponse
import `in`.woloo.www.blogs_module.FavouriteResult
import `in`.woloo.www.more.subscribe.subscription.model.SubmitGiftCardSubscriptionPurchaseRequest
import `in`.woloo.www.more.trendingblog.model.BlogByCategoryResponse
import `in`.woloo.www.more.trendingblog.model.EcomCoinUpdateRequest
import `in`.woloo.www.more.trendingblog.model.EcomCoinUpdateResponse
import `in`.woloo.www.more.trendingblog.model.SaveUserCategoryRequest
import `in`.woloo.www.more.trendingblog.model.blog.Blog
import `in`.woloo.www.store.reviews.ReviewsResponse
import `in`.woloo.www.store.admin_response.CityFromSalesChannelResponse
import `in`.woloo.www.store.auth_request_response.AddCustomerOnMedusaRequest
import `in`.woloo.www.store.auth_request_response.AuthTokenResponse
import `in`.woloo.www.store.user_details.CreateUpdateAddressRequest
import `in`.woloo.www.store.auth_request_response.CustomerListResponse
import `in`.woloo.www.store.auth_request_response.RegisterOnMedusaRequest
import `in`.woloo.www.store.auth_request_response.UpdateCustomerRequest
import `in`.woloo.www.store.cart_request_response.AddPromotionsRequest
import `in`.woloo.www.store.cart_request_response.CalculateShippingRequest
import `in`.woloo.www.store.cart_request_response.CalculateShippingResponse
import `in`.woloo.www.store.cart_request_response.CartAddRequest
import `in`.woloo.www.store.cart_request_response.CartAddRequestHygiene
import `in`.woloo.www.store.cart_request_response.CartRequest
import `in`.woloo.www.store.cart_request_response.CartResponse
import `in`.woloo.www.store.cart_request_response.CartUpdateRequest
import `in`.woloo.www.store.cart_request_response.CartUpdateRequestHygiene
import `in`.woloo.www.store.cart_request_response.CompleteCartResponse
import `in`.woloo.www.store.cart_request_response.DeleteFromCartResponse
import `in`.woloo.www.store.cart_request_response.DeletePaymentSessionRequest
import `in`.woloo.www.store.cart_request_response.DeletePaymentSessionResponse
import `in`.woloo.www.store.cart_request_response.PaymentCollectionRequest
import `in`.woloo.www.store.cart_request_response.PaymentCollectionsResponse
import `in`.woloo.www.store.cart_request_response.PaymentProviderResponse
import `in`.woloo.www.store.cart_request_response.PaymentSessionRequest
import `in`.woloo.www.store.cart_request_response.ShippingBillingAddressRequest
import `in`.woloo.www.store.cart_request_response.ShippingMethodsRequest
import `in`.woloo.www.store.cart_request_response.ShippingOptionsResponse
import `in`.woloo.www.store.collections_response.CollectionsListResponse
import `in`.woloo.www.store.delivery_response.DeliveryCodesResponse
import `in`.woloo.www.store.orders_response.OrderListResponse
import `in`.woloo.www.store.orders_response.OrdersListData
import `in`.woloo.www.store.product_response.CategoriesListResponse
import `in`.woloo.www.store.product_response.NotifyRequest
import `in`.woloo.www.store.product_response.NotifyResponse
import `in`.woloo.www.store.product_response.ProductDetailsResponse
import `in`.woloo.www.store.product_response.ProductListData
import `in`.woloo.www.store.product_response.ProductListResponse
import `in`.woloo.www.store.region_response.RegionListResponse
import `in`.woloo.www.store.reviews.AddReviewRequest
import `in`.woloo.www.store.reviews.ReviewListData
import `in`.woloo.www.store.user_details.AddAddressResponse
import `in`.woloo.www.store.user_details.AddressListResponse
import `in`.woloo.www.store.user_details.DeleteAddressResponse
import `in`.woloo.www.store.user_details.WishLIstResponse
import `in`.woloo.www.store.user_details.WishListDeleteRequest
import `in`.woloo.www.store.user_details.WishListRequest
import `in`.woloo.www.v2.invite.model.InviteRequest
import `in`.woloo.www.v2.profile.model.UserProfile
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @POST("/api/wolooHost/nearBy")
    fun getNearbyWoloos(@Body request: NearbyWolooRequest): Call<BaseResponse<ArrayList<NearByStoreResponse.Data>>>

   /* @POST("/api/wolooHost/search")
    fun searchNearbyWoloos(@Body request: SearchWolooRequest): Call<BaseResponse<ArrayList<NearByStoreResponse.Data>>>
*/

    @POST("/api/wolooHost/search")
    fun searchNearbyWoloos(@Body request: SearchWolooRequest): Call<BaseResponse<NearByStoreResultsWrapper>>

    @POST("/api/wolooHost/search")
    fun searchNearbyWoloosForSearch(@Body request: SearchWolooRequest): Call<BaseResponse<ArrayList<NearByStoreResponse.Data>>>


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

    @POST("/api/subscription/submitGiftSubscriptionPurchase")
    fun submitGiftCardSubscriptionPurchase(@Body request : SubmitGiftCardSubscriptionPurchaseRequest): Call<BaseResponse<InitSubscriptionResponse>>

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

    @POST("/api/wolooHost/creteWolooWithRateToilet")
    fun rateReview(@Body request: RateReviiewRequest): Call<BaseResponse<JSONObject>>

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
    fun ctaFavourite(@Query("blog_id") blogId : String): Call<BaseResponse<FavouriteResult>>

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

    @GET("/api/wolooGuest/about")
    fun getAboutUs(): Call<BaseResponse<WebViewResponse>>

    @GET("/api/wolooGuest/terms")
    fun getTerms(): Call<BaseResponse<WebViewResponse>>

    @GET("store/regions")
    fun getRegionsList(): Call<RegionListResponse>

    @GET("store/products")
    fun getProductList(): Call<ProductListResponse>

    @GET("store/products/{id}")
    fun getProductDetailsWithPrice(
        @Path("id") id: String,
        @Query("fields") fields: String,
        @Query("region_id") region_id: String
    ): Call<ProductDetailsResponse>

    @GET("store/products")
    fun getProductWithPriceList(
        @Query("fields") fields: String,
        @Query("region_id") region_id: String
    ): Call<ProductListResponse>


    @GET("store/products")
    fun getProductWithPriceListWithQuery(
        @Query("fields") fields: String,
        @Query("region_id") region_id: String,
        @Query("q") q: String
    ): Call<ProductListResponse>

    @GET("store/product-categories")
    fun getCategoriesList(): Call<CategoriesListResponse>

    @GET("store/collections")
    fun getCollections(
        @Query("fields") fields: String,
        @Query("limit") limit: Int
    ): Call<CollectionsListResponse>

    @GET("store/products")
    fun getSearchedProducts(
        @Query("fields") price: String,
        @Query("q") fields: String
    ): Call<ProductListResponse>

    @GET("store/products")
    fun getCategoryWiseProducts(
        @Query("fields") price: String,
        @Query("category_id") fields: String
    ): Call<ProductListResponse>

    @GET("store/products")
    fun getCollectionWiseProducts(
        @Query("fields") price: String,
        @Query("collection_id") fields: String
    ): Call<ProductListResponse>

    @POST("auth/customer/emailpass/register")
    fun getRegisteredOnMedusa(@Body request : RegisterOnMedusaRequest): Call<AuthTokenResponse>

    @POST("store/customers")
    fun getAddCustomerOnMedusa(@Body request : AddCustomerOnMedusaRequest): Call<CustomerListResponse>


    @POST("store/customers/me")
    fun getUpdateCustomerOnMedusa(
        @Body request : UpdateCustomerRequest
    ): Call<CustomerListResponse>

    @POST("auth/customer/emailpass")
    fun getCustomerAuthProviderRegistered(@Body request : RegisterOnMedusaRequest): Call<AuthTokenResponse>

    @PATCH("api/wolooGuest/register")
    fun informRegisteredToShop() : Call<BaseResponse<JSONObject>>
    

    @GET("store/customers/me/addresses")
    fun getAddressesList(
        @Query("fields") fields: String
    ): Call<AddressListResponse>

    @POST("store/carts")
    fun getCartCreated(@Body request : CartRequest): Call<CartResponse>

    @GET("store/carts/{cart_id}")
    fun getCartList(
        @Path("cart_id") cartId: String,
        @Query("fields") fields: String
    ): Call<CartResponse>


    @GET("store/carts/{cart_id}/check-inventory")
    fun getCheckCartInventory(
        @Path("cart_id") cartId: String
    ): Call<ResponseBody>


    @POST("store/carts/{cart_id}/line-items")
    fun getAddToCart(
        @Path("cart_id") cartId: String,
        @Query("fields") fields: String,
        @Body request: CartAddRequest
    ): Call<CartResponse>


    @POST("store/carts/{cart_id}/line-items")
    fun getAddToCartHygiene(
        @Path("cart_id") cartId: String,
        @Body request: CartAddRequestHygiene
    ): Call<CartResponse>

    @POST("store/carts/{cart_id}/line-items/{line_id}")
    fun getUpdateToCart(
        @Path("cart_id") cartId: String,
        @Path("line_id") lineId: String,
        @Query("fields") fields: String,
        @Body request: CartUpdateRequest
    ): Call<CartResponse>

    @POST("store/carts/{cart_id}/line-items/{line_id}")
    fun getUpdateToCartHygiene(
        @Path("cart_id") cartId: String,
        @Path("line_id") lineId: String,
        @Query("fields") fields: String,
        @Body request: CartUpdateRequestHygiene
    ): Call<CartResponse>


    @DELETE("store/carts/{cart_id}/line-items/{line_id}")
    fun getDeleteFromCart(
        @Path("cart_id") cartId: String,
        @Path("line_id") lineId: String,
        @Query("fields") fields: String
    ): Call<DeleteFromCartResponse>

    @POST("store/carts/{cart_id}/promotions")
    fun getPromotionsToCart(
        @Path("cart_id") cartId: String,
        @Body request: AddPromotionsRequest
    ): Call<CartResponse>

    //@DELETE("store/carts/{cart_id}/promotions")
    @HTTP(method = "DELETE", path = "store/carts/{cart_id}/promotions", hasBody = true)
    fun getDeletePromotionsFromCart(
        @Path("cart_id") cartId: String,
        @Body request: AddPromotionsRequest
    ): Call<CartResponse>

    @GET("c/api/pin-codes/json")
    fun getDeliveryPartnersListFromPincode(
        @Query("filter_codes") fields: String
    ): Call<DeliveryCodesResponse>

    @GET("admin/stock-locations")
    fun getCitiesFromSalesChannles(
        @Query("sales_channel_id") fields: String
    ): Call<CityFromSalesChannelResponse>


    @POST("auth/user/emailpass")
    fun getAdminAuthProviderRegistered(@Body request : RegisterOnMedusaRequest): Call<AuthTokenResponse>


    @POST("store/customers/me/addresses")
    fun getAddAddresses(@Body request : CreateUpdateAddressRequest): Call<AddAddressResponse>


    @POST("store/customers/me/addresses/{address_id}")
    fun getUpdateAddresses(
        @Path("address_id") address_id: String,
        @Body request : CreateUpdateAddressRequest): Call<AddAddressResponse>

    @DELETE("store/customers/me/addresses/{address_id}")
    fun getDeleteAddresses(@Path("address_id") address_id: String): Call<DeleteAddressResponse>

    @GET("store/products/{prod_id}/reviews")
    fun getReviewsForProduct(
        @Path("prod_id") productId: String,
        @Query("all") fields: String
    ): Call<ReviewsResponse>

    @POST("store/reviews")
    fun getAddReview(
      @Body request: AddReviewRequest
    ): Call<ReviewListData>

    @POST("store/carts/{cart_id}")
    fun getShippingAndBillingToCart(
        @Path("cart_id") cartId: String,
        @Body request: ShippingBillingAddressRequest
    ): Call<CartResponse>

  /*  @POST("store/carts/{cart_id}/shipping-methods")
    fun getShippingMethodsToCart(
        @Path("cart_id") cartId: String,
        @Body request: ShippingMethodsRequest
    ): Call<CartResponse>

*/
    @POST("store/carts/{cart_id}/add-shipping-methods")
    fun getShippingMethodsToCart(
        @Path("cart_id") cartId: String,
        @Body request: ShippingMethodsRequest
    ): Call<CartResponse>

    @GET("store/payment-providers")
    fun getPaymentProvidersList(
        @Query("region_id") region_id: String
    ): Call<PaymentProviderResponse>

   /* @GET("store/shipping-options")
    fun getShippingOptionsList(
        @Query("cart_id") cart_id: String
    ): Call<ShippingOptionsResponse>*/

    @GET("store/shipping-options/address")
    fun getShippingOptionsList(
        @Query("cart_id") cart_id: String
    ): Call<ShippingOptionsResponse>


   /* @GET("store/shipping-options/address?cart_id={{cart-id}}")
    fun getShippingOptionsListWithCartId(
        @Query("cart_id") cart_id: String
    ): Call<ShippingOptionsResponse>*/


    @POST("store/shipping-options/{option_id}/calculate")
    fun getCalculateShippingOptions(
        @Path("option_id") optionId: String,
        @Body request: CalculateShippingRequest
    ): Call<CalculateShippingResponse>

    @POST("store/payment-collections")
    fun getPaymentCollection(
        @Body request: PaymentCollectionRequest
    ): Call<PaymentCollectionsResponse>

    @POST("store/payment-collections/{payment_collection_id}/payment-sessions")
    fun getPaymentSession(
        @Path("payment_collection_id") paymentCollectionId: String,
        @Body request: PaymentSessionRequest
    ): Call<PaymentCollectionsResponse>

   /* @POST("store/carts/{cart_id}/complete-vendor")
    fun getCompleteCart(
        @Path("cart_id") cartId: String,
    ): Call<CompleteCartResponse>*/

    @POST("store/carts/{cart_id}/split-and-complete-cart")
    fun getCompleteCart(
        @Path("cart_id") cartId: String,
    ): Call<CompleteCartResponse>

    @POST("store/carts/{cart_id}/split-and-complete-hygiene-service-cart")
    fun getCompleteCartHygiene(
        @Path("cart_id") cartId: String,
    ): Call<CompleteCartResponse>


    @POST("store/customers/me/wishlists")
    fun createWishlist(): Call<WishLIstResponse>


    @GET("store/customers/me/wishlists/items")
    fun getCustomerWishlist(
        @Query("currency_code") currencyCode: String? = "inr",
    ): Call<WishLIstResponse>

    @POST("store/customers/me/wishlists/items")
    fun addWishListItem(
        @Body request: WishListRequest
    ) : Call<WishLIstResponse>

    @DELETE("store/customers/me/wishlists/items/{id}")
    fun deleteWishListItem(
        @Path("id") itemId: String
    ) : Call<WishLIstResponse>

  /*  @GET("store/orders")
    fun getOrders(): Call<OrderListResponse>*/

    @GET("store/order-sets")
    fun getOrders(): Call<OrderListResponse>

    @GET("store/orders/{id}")
    fun getOrderDetails(
        @Path("id") orderId: String
    ): Call<OrderListResponse>

    @HTTP(method = "DELETE", path = "store/payment/delete-payment", hasBody = true)
    fun deletePaymentsSession(
        @Body request: DeletePaymentSessionRequest
    ): Call<DeletePaymentSessionResponse>

    @GET("store/products")
    fun getFilterProducts(
        @Query("fields") fields: String = "*variants.calculated_price,+variants.inventory_quantity",
        @Query("category_id") categoryId: String? = null,
        @Query("variants[options][value]") optionValue: String? = null,
      //  @Query("\$and[0]variants[options][value]") andOptionValue: String? = null
    ): Call<ProductListResponse>


    @POST("api/blog/comments")
    fun postComment(@Body commentsData : CommentRequest
    ):Call<BaseResponse<CommentResponse>>

    @GET("api/blog/{blog_id}/comments")
    fun getCommentListBlogWise(
        @Path("blog_id") blogId: String
    ): Call<BaseResponse<ArrayList<CommentResponse.Data>>>


    @POST("api/blog/blockBlog")
    fun blockBlog(@Body blockBlogRequest  : BlockBlogRequest
    ):Call<BaseResponse<CommentResponse>>


    @POST("api/blog/getBlogDetail")
    fun getBlogDetails(@Body blockBlogRequest  : BlockBlogRequest
    ):Call<BaseResponse<ArrayList<Blog>>>


    @POST("store/restock-subscriptions")
    fun getNotifyUserForProduct(@Body notifyRequest  : NotifyRequest
    ):Call<NotifyResponse>

    @PUT("/api/wolooGuest/delete")
    fun deleteWolooUser(
        @Query("id") id: Int
    ): Call<BaseResponse<JSONObject>>


    @POST("api/wolooGuest/creditUserCoins")
    fun addCoinstoWolooUser(
        @Body request: CreditCoinsRequest
    ): Call<BaseResponse<JSONObject>>



    @POST("api/wolooHost/powderroom/payment")
    fun getPurchaseNow(@Body purchaseNowRequest: PurchaseNowRequest
    ):Call<BaseResponse<PurchaseNowResponse>>

}