package `in`.woloo.www.store

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.EventLiveData
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.application_kotlin.model.server_response.WebViewResponse
import `in`.woloo.www.application_kotlin.repositories.HomeRepository
import `in`.woloo.www.store.admin_response.CityFromSalesChannelResponse
import `in`.woloo.www.store.auth_request_response.AddCustomerOnMedusaRequest
import `in`.woloo.www.store.auth_request_response.AuthTokenResponse
import `in`.woloo.www.store.auth_request_response.CustomerListResponse
import `in`.woloo.www.store.auth_request_response.RegisterOnMedusaRequest
import `in`.woloo.www.store.auth_request_response.UpdateCustomerRequest
import `in`.woloo.www.store.cart_request_response.AddPromotionsRequest
import `in`.woloo.www.store.cart_request_response.CalculateShippingRequest
import `in`.woloo.www.store.cart_request_response.CalculateShippingResponse
import `in`.woloo.www.store.cart_request_response.CartAddRequest
import `in`.woloo.www.store.cart_request_response.CartRequest
import `in`.woloo.www.store.cart_request_response.CartResponse
import `in`.woloo.www.store.cart_request_response.CartUpdateRequest
import `in`.woloo.www.store.cart_request_response.CompleteCartResponse
import `in`.woloo.www.store.cart_request_response.DeleteFromCartResponse
import `in`.woloo.www.store.cart_request_response.DeletePaymentSessionRequest
import `in`.woloo.www.store.cart_request_response.DeletePaymentSessionResponse
import `in`.woloo.www.store.cart_request_response.InventoryErrorResponse
import `in`.woloo.www.store.cart_request_response.InventoryResponse
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
import `in`.woloo.www.store.reviews.ReviewsResponse
import `in`.woloo.www.store.user_details.AddAddressResponse
import `in`.woloo.www.store.user_details.AddressListResponse
import `in`.woloo.www.store.user_details.CreateUpdateAddressRequest
import `in`.woloo.www.store.user_details.DeleteAddressResponse
import `in`.woloo.www.store.user_details.WishLIstResponse
import `in`.woloo.www.store.user_details.WishListDeleteRequest
import `in`.woloo.www.store.user_details.WishListRequest
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.v2.base.BaseViewModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET

class StoreViewModel :  BaseViewModel() {

    private val mProductList: EventLiveData<ProductListResponse> = EventLiveData()

    private val mProductDetail: EventLiveData<ProductDetailsResponse> = EventLiveData()

    private val mRegionList: EventLiveData<RegionListResponse> = EventLiveData()

    private val mCategoriesList: EventLiveData<CategoriesListResponse> = EventLiveData()

    private val mCollectionsList: EventLiveData<CollectionsListResponse> = EventLiveData()

    private val mDeliveryCodesList: EventLiveData<DeliveryCodesResponse> = EventLiveData()

    private val mAuthRegister: EventLiveData<AuthTokenResponse> = EventLiveData()

    private val mAddCustomer: EventLiveData<CustomerListResponse> = EventLiveData()

    private val mAuthLogin: EventLiveData<AuthTokenResponse> = EventLiveData()

    private val mAddressList: EventLiveData<AddressListResponse> = EventLiveData()

    private val mCartCreate: EventLiveData<CartResponse> = EventLiveData()

    private val mCartList:EventLiveData<CartResponse> = EventLiveData()

    private val _mCheckInventorySuccess = MutableLiveData<InventoryResponse>()
    val mCheckInventorySuccess: LiveData<InventoryResponse> = _mCheckInventorySuccess

    private val _mCheckInventoryError = MutableLiveData<InventoryErrorResponse>()
    val mCheckInventoryError: LiveData<InventoryErrorResponse> = _mCheckInventoryError

    private val mAddToCart: EventLiveData<CartResponse> = EventLiveData()

    private val mRemoveFromCart: EventLiveData<DeleteFromCartResponse> = EventLiveData()

    private val mAddPromotionToCart: EventLiveData<CartResponse> = EventLiveData()

    private val mRemovePromotionFromCart: EventLiveData<CartResponse> = EventLiveData()

    private val mUpdateItemCart: EventLiveData<CartResponse> = EventLiveData()

    private val mNotifyProduct: EventLiveData<NotifyResponse> = EventLiveData()

    private val mCityList: EventLiveData<CityFromSalesChannelResponse> = EventLiveData()

    private val mAddAddress: EventLiveData<AddAddressResponse> = EventLiveData()

    private val mUpdateAddress: EventLiveData<AddAddressResponse> = EventLiveData()

    private val mAddReview: EventLiveData<ReviewListData> = EventLiveData()

    private val mGetReview: EventLiveData<ReviewsResponse> = EventLiveData()

    private val mDeleteAddress: EventLiveData<DeleteAddressResponse> = EventLiveData()

    private val mStoreRepository: StoreRepository = StoreRepository()

    private val mAddShipppingBillingAddress: EventLiveData<CartResponse> = EventLiveData()

    private val mShippingOptionsList: EventLiveData<ShippingOptionsResponse> = EventLiveData()

    private val mShippingMethodsToCart: EventLiveData<CartResponse> = EventLiveData()

    private val mPaymentProvider: EventLiveData<PaymentProviderResponse> = EventLiveData()

    private val mPaymentCollection: EventLiveData<PaymentCollectionsResponse> = EventLiveData()

    private val mPaymentSession: EventLiveData<PaymentCollectionsResponse> = EventLiveData()

    private val mCompleteCart: EventLiveData<CompleteCartResponse> = EventLiveData()

    private val mCalculateShippingOptions: EventLiveData<CalculateShippingResponse> = EventLiveData()

    private val mCreateWishlist: EventLiveData<WishLIstResponse> = EventLiveData()
    private val mGetWishList: EventLiveData<WishLIstResponse> = EventLiveData()
    private val mAddItemsToWishList: EventLiveData<WishLIstResponse> = EventLiveData()
    private val mDeleteFromWishList: EventLiveData<WishLIstResponse> = EventLiveData()
    private val mGetOrders: EventLiveData<OrderListResponse> = EventLiveData()
    private val mGetOrderDetails: EventLiveData<OrderListResponse> = EventLiveData()
    private val mDeletePaymentSession: EventLiveData<DeletePaymentSessionResponse> = EventLiveData()
    private val mFilterProductList: EventLiveData<ProductListResponse> = EventLiveData()


    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _paymentSessionResponse = MutableLiveData<ApiResponseData<PaymentCollectionsResponse>>()

    private val _mAddAddressSession = MutableLiveData<ApiResponseData<AddAddressResponse>>()

    private val _mUpdateAddressSession = MutableLiveData<ApiResponseData<AddAddressResponse>>()

    private val _mDeleteAddressSession =MutableLiveData<ApiResponseData<DeleteAddressResponse>>()

    private val _mAddressListSession = MutableLiveData<ApiResponseData<AddressListResponse>>()

    private val mInformRegistered: EventLiveData<BaseResponse<JSONObject>> = EventLiveData()



  /*  fun observeProductList(): MutableLiveData<ProductListResponse> {
        return mProductList
    }

    fun getProductList(){
        mStoreRepository.getProductList(object :
            WebserviceCallback<ApiResponseData<ProductListResponse>> {
            override fun onWebResponse(data: ApiResponseData<ProductListResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mProductList.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mProductList.value = data.data

                }
            }
        })
    }
*/

    fun observeRegionList(): MutableLiveData<RegionListResponse> {
        return mRegionList
    }

    fun getRegionList(){
        mStoreRepository.getRegionList(object :
            WebserviceCallback<ApiResponseData<RegionListResponse>> {
            override fun onWebResponse(data: ApiResponseData<RegionListResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mRegionList.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mRegionList.value = data.data

                }
            }
        })
    }


    fun observeProductDetailsWithPrice(): MutableLiveData<ProductDetailsResponse> {
        return mProductDetail
    }

    fun  getProductDetailsWithPrice(id : String ,fields:String , regionId : String){
        mStoreRepository. getProductDetailsWithPrice(id , fields ,regionId ,object :
            WebserviceCallback<ApiResponseData<ProductDetailsResponse>> {
            override fun onWebResponse(data: ApiResponseData<ProductDetailsResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mProductDetail.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mProductDetail.value = data.data

                }
            }
        })
    }

    fun observeProductWithPriceList(): MutableLiveData<ProductListResponse> {
        return mProductList
    }

    fun getProductWithPriceList(fields:String , regionId : String){
        mStoreRepository.getProductWithPriceList( fields ,regionId ,object :
            WebserviceCallback<ApiResponseData<ProductListResponse>> {
            override fun onWebResponse(data: ApiResponseData<ProductListResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mProductList.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mProductList.value = data.data

                }
            }
        })
    }

    fun observeProductWithPriceListWithQuery(): MutableLiveData<ProductListResponse> {
        return mProductList
    }

    fun getProductWithPriceListWithQuery(fields:String , regionId : String , q:String){
        mStoreRepository.getProductWithPriceListWithQuery( fields ,regionId , q ,object :
            WebserviceCallback<ApiResponseData<ProductListResponse>> {
            override fun onWebResponse(data: ApiResponseData<ProductListResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mProductList.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mProductList.value = data.data

                }
            }
        })
    }

    fun observeCategoriesList(): MutableLiveData<CategoriesListResponse> {
        return mCategoriesList
    }

    fun getCategoriesList(){
        mStoreRepository.getCategoriesList(object :
            WebserviceCallback<ApiResponseData<CategoriesListResponse>> {
            override fun onWebResponse(data: ApiResponseData<CategoriesListResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mCategoriesList.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mCategoriesList.value = data.data

                }
            }
        })
    }

    fun observeCollectionsList(): MutableLiveData<CollectionsListResponse> {
        return mCollectionsList
    }

    fun getCollectionsList(fields : String ){
        mStoreRepository.getCollectionsNameImageList(
            object :
                WebserviceCallback<ApiResponseData<CollectionsListResponse>> {
                override fun onWebResponse(data: ApiResponseData<CollectionsListResponse>) {
                    if (data.status == ApiResponseData.API_SUCCESS) {
                        mCollectionsList.value = data.data
                    } else {
                        WolooApplication.errorMessage = data.message
                        mCollectionsList.value = data.data

                    }
                }
            }, fields

        )
    }

    fun observeSearchedProductList(): MutableLiveData<ProductListResponse> {
        return mProductList
    }

    fun getSearchedProductList(fields : String ){
        mStoreRepository.getSearchKeywordProductList(object :
            WebserviceCallback<ApiResponseData<ProductListResponse>> {
            override fun onWebResponse(data: ApiResponseData<ProductListResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mProductList.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mProductList.value = data.data

                }
            }
        },fields
        )
    }



    fun observeCollectionWiseProductList(): MutableLiveData<ProductListResponse> {
        return mProductList
    }

    fun getCollectionWiseProductList(fields : String ){
        mStoreRepository.getCollectionWiseProductList(object :
            WebserviceCallback<ApiResponseData<ProductListResponse>> {
            override fun onWebResponse(data: ApiResponseData<ProductListResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mProductList.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mProductList.value = data.data

                }
            }
        } , fields)
    }

    fun observeCategoryWiseProductList(): MutableLiveData<ProductListResponse> {
        return mProductList
    }

    fun getCategoryWiseProductList(fields : String ){
        mStoreRepository.getCategoryWiseProductList(object :
            WebserviceCallback<ApiResponseData<ProductListResponse>> {
            override fun onWebResponse(data: ApiResponseData<ProductListResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mProductList.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mProductList.value = data.data

                }
            }
        } , fields)
    }

    fun observeRegisteredOnMedusa(): MutableLiveData<AuthTokenResponse> {
        return mAuthRegister
    }

    fun getRegisteredOnMedusa(request : RegisterOnMedusaRequest ){
        mStoreRepository.getRegisterOnMedusa(request ,object :
            WebserviceCallback<ApiResponseData<AuthTokenResponse>> {
            override fun onWebResponse(data: ApiResponseData<AuthTokenResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mAuthRegister.value = data.data
                } else {
                    val errorMsg = data.message ?: "An error occurred"
                    if (errorMsg.contains("email already exists")) {
                        _errorMessage.value = "Email is already registered. Please log in."
                    } else {
                        _errorMessage.value = errorMsg
                    }


                }
            }
        } )
    }

    fun observeAddCustomer(): MutableLiveData<CustomerListResponse> {
        return mAddCustomer
    }

    fun getAddCustomer(request : AddCustomerOnMedusaRequest ){
        mStoreRepository.getAddCustomerOnMedusa(request ,object :
            WebserviceCallback<ApiResponseData<CustomerListResponse>> {
            override fun onWebResponse(data: ApiResponseData<CustomerListResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mAddCustomer.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mAddCustomer.value = data.data

                }
            }
        } )
    }

    fun observeUpdateCustomer(): MutableLiveData<CustomerListResponse> {
        return mAddCustomer
    }

    fun getUpdateCustomer(customerId : String ,request : UpdateCustomerRequest ){
        mStoreRepository.getUpdateCustomerOnMedusa(customerId , request ,object :
            WebserviceCallback<ApiResponseData<CustomerListResponse>> {
            override fun onWebResponse(data: ApiResponseData<CustomerListResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mAddCustomer.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mAddCustomer.value = data.data

                }
            }
        } )
    }


    fun observeCustomerAuthPassOnMedusa(): MutableLiveData<AuthTokenResponse> {
        return mAuthLogin
    }

    fun getCustomerAuthPassOnMedusa(request : RegisterOnMedusaRequest ){
        mStoreRepository.getCustomerAuthPassOnMedusa(request ,object :
            WebserviceCallback<ApiResponseData<AuthTokenResponse>> {
            override fun onWebResponse(data: ApiResponseData<AuthTokenResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mAuthLogin.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mAuthLogin.value = data.data

                }
            }
        } )
    }

    fun observeInformRegisterForShop(): MutableLiveData<BaseResponse<JSONObject>> {
        return mInformRegistered
    }

    fun getInformRegisterForShop()
    {
        updateProgress(true)
        mStoreRepository.informRegisteredToShop(object :
            WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>> {
            override fun onWebResponse(data: ApiResponseData<BaseResponse<JSONObject>>) {
                updateProgress(false)
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mInformRegistered.value = data.data
                } else {
                    mInformRegistered.value = data.data
                    notifyNetworkError(data)
                }
            }
        })
    }

    fun observeAddressesList(): MutableLiveData<ApiResponseData<AddressListResponse>> {
        return _mAddressListSession
    }

    fun getAddressesList(){
        mStoreRepository.getAddressList(object :
            WebserviceCallback<ApiResponseData<AddressListResponse>> {
            override fun onWebResponse(data: ApiResponseData<AddressListResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    _mAddressListSession.value = data
                    mAddressList.value = data.data
                } else {
                    _mAddressListSession.value = data
                    WolooApplication.errorMessage = data.message
                    mAddressList.value = data.data
                        val errorMsg = data.message ?: "An error occurred"
                        if (errorMsg.contains("unauthorized")) {
                            _errorMessage.value = "Please log in."
                        } else {
                            _errorMessage.value = errorMsg
                        }


                }
            }
        } )
    }

    fun observeCartCreate(): MutableLiveData<CartResponse> {
        return mCartCreate
    }

    fun getCartCreate(request : CartRequest ){
        mStoreRepository.getCartCreated(request ,object :
            WebserviceCallback<ApiResponseData<CartResponse>> {
            override fun onWebResponse(data: ApiResponseData<CartResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mCartCreate.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mCartCreate.value = data.data

                }
            }
        })
    }

    fun observeCartList(): MutableLiveData<CartResponse> {
        return mCartList
    }

    fun getCartList(cartId : String ){
        mStoreRepository.getCartList(cartId ,object :
            WebserviceCallback<ApiResponseData<CartResponse>> {
            override fun onWebResponse(data: ApiResponseData<CartResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mCartList.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mCartList.value = data.data
                }
            }
        })
    }

    fun getCheckCartInventory(cartId : String ){
            mStoreRepository.getCheckCartInventory(
                cartId,
                successCallback = {
                    Logger.i("Aarati Store succes vm", "setLiveData ${_mCheckInventorySuccess.postValue(it)}}")
                    _mCheckInventorySuccess.postValue(it)
                },
                errorCallback = {
                    Logger.i("Aarati Store error vm", "setLiveData ${_mCheckInventoryError.postValue(it)}}")
                    _mCheckInventoryError.postValue(it)
                }
            )
    }

    fun observeAddToCart(): MutableLiveData<CartResponse> {
        return mAddToCart
    }

    fun getAddToCart(cartId : String ,request : CartAddRequest ){
        mStoreRepository.getAddToCart(cartId ,request ,object :
            WebserviceCallback<ApiResponseData<CartResponse>> {
            override fun onWebResponse(data: ApiResponseData<CartResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mAddToCart.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mAddToCart.value = data.data

                }
            }
        })
    }

    fun observeUpdateToCart(): MutableLiveData<CartResponse> {
        return mUpdateItemCart
    }

    fun getUpdateToCart(cartId : String , lineId : String,request : CartUpdateRequest ){
        mStoreRepository.getUpdateToCart(cartId , lineId ,request ,object :
            WebserviceCallback<ApiResponseData<CartResponse>> {
            override fun onWebResponse(data: ApiResponseData<CartResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mUpdateItemCart.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mUpdateItemCart.value = data.data

                }
            }
        })
    }

    fun observeDeleteFromCart(): MutableLiveData<DeleteFromCartResponse> {
        return mRemoveFromCart
    }

    fun getDeleteFromCart(cartId : String , lineId : String){
        mStoreRepository.getDeleteFromCart(cartId , lineId  ,object :
            WebserviceCallback<ApiResponseData<DeleteFromCartResponse>> {
            override fun onWebResponse(data: ApiResponseData<DeleteFromCartResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mRemoveFromCart.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mRemoveFromCart.value = data.data

                }
            }
        })
    }


    fun observeAddPromotionToCart(): MutableLiveData<CartResponse> {
        return mAddPromotionToCart
    }

    fun getAddPromotionToCart(cartId : String ,request : AddPromotionsRequest ){
        mStoreRepository.getApplyPromotionsToCart(cartId ,request ,object :
            WebserviceCallback<ApiResponseData<CartResponse>> {
            override fun onWebResponse(data: ApiResponseData<CartResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mAddPromotionToCart.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mAddPromotionToCart.value = data.data

                }
            }
        })
    }


    fun observeDeletePromotionFromCart(): MutableLiveData<CartResponse> {
        return mRemovePromotionFromCart
    }

    fun getDeletePromotionFromCart(cartId : String  ,request : AddPromotionsRequest ){
        mStoreRepository.getDeletePromotionsFromCart(cartId  ,request  ,object :
            WebserviceCallback<ApiResponseData<CartResponse>> {
            override fun onWebResponse(data: ApiResponseData<CartResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mRemovePromotionFromCart.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mRemovePromotionFromCart.value = data.data

                }
            }
        })
    }

    fun observeDeliveryCodesList(): MutableLiveData<DeliveryCodesResponse> {
        return mDeliveryCodesList
    }

    fun getDeliveryCodesList(fields : String ){
        mStoreRepository.getDeliveryCodesList(object :
            WebserviceCallback<ApiResponseData<DeliveryCodesResponse>> {
            override fun onWebResponse(data: ApiResponseData<DeliveryCodesResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mDeliveryCodesList.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mDeliveryCodesList.value = data.data

                }
            }
        } , fields)
    }


    fun observeCitySalesChannelList(): MutableLiveData<CityFromSalesChannelResponse> {
        return mCityList
    }

    fun getCitySalesChannelList(fields : String ){
        mStoreRepository.getCitiesFromSalesChannles(object :
            WebserviceCallback<ApiResponseData<CityFromSalesChannelResponse>> {
            override fun onWebResponse(data: ApiResponseData<CityFromSalesChannelResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mCityList.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mCityList.value = data.data

                }
            }
        },fields
        )
    }

    fun observeAdminAuthPassOnMedusa(): MutableLiveData<AuthTokenResponse> {
        return mAuthLogin
    }

    fun getAdminAuthPassOnMedusa(request : RegisterOnMedusaRequest ){
        mStoreRepository.getAdminAuthPassOnMedusa(request ,object :
            WebserviceCallback<ApiResponseData<AuthTokenResponse>> {
            override fun onWebResponse(data: ApiResponseData<AuthTokenResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mAuthLogin.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mAuthLogin.value = data.data

                }
            }
        } )
    }

    fun observeAddAddress(): MutableLiveData<ApiResponseData<AddAddressResponse>> {
        return _mAddAddressSession
    }

    fun getAddAddress(request : CreateUpdateAddressRequest ){
        mStoreRepository.getAddAddress(request ,object :
            WebserviceCallback<ApiResponseData<AddAddressResponse>> {
            override fun onWebResponse(data: ApiResponseData<AddAddressResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    _mAddAddressSession.value = data
                    mAddAddress.value = data.data

                } else {
                    WolooApplication.errorMessage = data.message
                    _mAddAddressSession.value = data
                    mAddAddress.value = data.data

                }
            }
        } )
    }


    fun observeUpdateAddress(): MutableLiveData<ApiResponseData<AddAddressResponse>> {
        return _mUpdateAddressSession
    }

    fun getUpdateAddress(addressId: String ,request : CreateUpdateAddressRequest ){
        mStoreRepository.getUpdateAddress(addressId ,request ,object :
            WebserviceCallback<ApiResponseData<AddAddressResponse>> {
            override fun onWebResponse(data: ApiResponseData<AddAddressResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    _mUpdateAddressSession.value = data
                    mUpdateAddress.value = data.data
                } else {
                    _mUpdateAddressSession.value = data
                    WolooApplication.errorMessage = data.message
                    mUpdateAddress.value = data.data

                }
            }
        } )
    }

    fun observeDeleteAddress(): MutableLiveData<ApiResponseData<DeleteAddressResponse>>{
        return _mDeleteAddressSession
    }

    fun getDeleteAddress(addressId: String  ){
        mStoreRepository.getDeleteAddress(addressId ,object :
            WebserviceCallback<ApiResponseData<DeleteAddressResponse>> {
            override fun onWebResponse(data: ApiResponseData<DeleteAddressResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    _mDeleteAddressSession.value = data
                    mDeleteAddress.value = data.data
                } else {
                    _mDeleteAddressSession.value = data
                    WolooApplication.errorMessage = data.message
                    mDeleteAddress.value = data.data

                }
            }
        } )
    }


    fun observeGetReviewListForProduct(): MutableLiveData<ReviewsResponse> {
        return mGetReview
    }

    fun getReviewsListForProduct(productId: String  ){
        mStoreRepository.getReviewOfProduct(productId ,object :
            WebserviceCallback<ApiResponseData<ReviewsResponse>> {
            override fun onWebResponse(data: ApiResponseData<ReviewsResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mGetReview.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mGetReview.value = data.data

                }
            }
        } )
    }

    fun observeAddReviewForProduct(): MutableLiveData<ReviewListData> {
        return mAddReview
    }

    fun getAddReviewForProduct(request: AddReviewRequest  ){
        mStoreRepository.getAddReview(request ,object :
            WebserviceCallback<ApiResponseData<ReviewListData>> {
            override fun onWebResponse(data: ApiResponseData<ReviewListData>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mAddReview.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mAddReview.value = data.data

                }
            }
        } )
    }

    fun observeAddShippingBillingToCart(): MutableLiveData<CartResponse> {
        return mAddShipppingBillingAddress
    }

    fun getAddShippingBillingToCart(cartId : String ,request: ShippingBillingAddressRequest  ){
        mStoreRepository.getShippingBillingAddressToCart(cartId , request ,object :
            WebserviceCallback<ApiResponseData<CartResponse>> {
            override fun onWebResponse(data: ApiResponseData<CartResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mAddShipppingBillingAddress.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mAddShipppingBillingAddress.value = data.data

                }
            }
        } )
    }

    fun observeShippingOptions(): MutableLiveData<ShippingOptionsResponse> {
        return mShippingOptionsList
    }

    fun getShippingOptions(cartId : String  ){
        mStoreRepository.getShippingOptionsList(cartId ,object :
            WebserviceCallback<ApiResponseData<ShippingOptionsResponse>> {
            override fun onWebResponse(data: ApiResponseData<ShippingOptionsResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mShippingOptionsList.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mShippingOptionsList.value = data.data

                }
            }
        } )
    }

    fun observeShippingMethodsToCart(): MutableLiveData<CartResponse> {
        return mShippingMethodsToCart
    }

    fun getShippingMethodsToCart(optionsId : String ,request: ShippingMethodsRequest  ){
        mStoreRepository.getShippingMethodsToCart(optionsId , request ,object :
            WebserviceCallback<ApiResponseData<CartResponse>> {
            override fun onWebResponse(data: ApiResponseData<CartResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mShippingMethodsToCart.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mShippingMethodsToCart.value = data.data

                }
            }
        } )
    }

     fun observeCalculateShippingOptions(): MutableLiveData<CalculateShippingResponse> {
        return mCalculateShippingOptions
    }

    fun getCalculateShippingOptions(optionsId : String ,request: CalculateShippingRequest  ){
        mStoreRepository.getCalculateShippingOptions(optionsId , request ,object :
            WebserviceCallback<ApiResponseData<CalculateShippingResponse>> {
            override fun onWebResponse(data: ApiResponseData<CalculateShippingResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mCalculateShippingOptions.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mCalculateShippingOptions.value = data.data

                }
            }
        } )
    }

    fun observePaymentProvidersList(): MutableLiveData<PaymentProviderResponse> {
        return mPaymentProvider
    }

    fun getPaymentProvidersList(regionId:String ){
        mStoreRepository.getPaymentProvidersList(regionId ,object :
            WebserviceCallback<ApiResponseData<PaymentProviderResponse>> {
            override fun onWebResponse(data: ApiResponseData<PaymentProviderResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mPaymentProvider.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mPaymentProvider.value = data.data

                }
            }
        } )
    }

    fun observePaymentCollection(): MutableLiveData<PaymentCollectionsResponse> {
        return mPaymentCollection
    }

    fun getPaymentCollection(request: PaymentCollectionRequest  ){
        mStoreRepository.getPaymentCollection( request ,object :
            WebserviceCallback<ApiResponseData<PaymentCollectionsResponse>> {
            override fun onWebResponse(data: ApiResponseData<PaymentCollectionsResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mPaymentCollection.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mPaymentCollection.value = data.data

                }
            }
        } )
    }

    fun observePaymentSession(): MutableLiveData<ApiResponseData<PaymentCollectionsResponse>> {
        return _paymentSessionResponse
    }

    fun getPaymentSession(paymentCollectionId : String ,request: PaymentSessionRequest  ){
        mStoreRepository.getPaymentSession(paymentCollectionId , request ,object :
            WebserviceCallback<ApiResponseData<PaymentCollectionsResponse>> {
            override fun onWebResponse(data: ApiResponseData<PaymentCollectionsResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    _paymentSessionResponse.value = data
                    mPaymentSession.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mPaymentSession.value = data.data
                    _paymentSessionResponse.value = data

                }
            }
        } )
    }

    fun observedeletePaymentSession(): MutableLiveData<DeletePaymentSessionResponse> {
        return mDeletePaymentSession
    }

    fun getDeletePaymentSession(request: DeletePaymentSessionRequest  ){
        mStoreRepository.deletePaymentSession( request ,object :
            WebserviceCallback<ApiResponseData<DeletePaymentSessionResponse>> {
            override fun onWebResponse(data: ApiResponseData<DeletePaymentSessionResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mDeletePaymentSession.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mDeletePaymentSession.value = data.data

                }
            }
        } )
    }

    fun observeCompleteCart(): MutableLiveData<CompleteCartResponse> {
        return mCompleteCart
    }

    fun getCompleteCart(cartId : String  ){
        mStoreRepository.getCompleteCart(cartId ,object :
            WebserviceCallback<ApiResponseData<CompleteCartResponse>> {
            override fun onWebResponse(data: ApiResponseData<CompleteCartResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mCompleteCart.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mCompleteCart.value = data.data

                }
            }
        } )
    }

    fun observeCreateWishlist(): MutableLiveData<WishLIstResponse> {
        return mCreateWishlist
    }

    fun createWishlist( ){
        mStoreRepository.createWishlist(object :
            WebserviceCallback<ApiResponseData<WishLIstResponse>> {
            override fun onWebResponse(data: ApiResponseData<WishLIstResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mCreateWishlist.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mCreateWishlist.value = data.data

                }
            }
        } )
    }

    fun observeAddWishListItem(): MutableLiveData<WishLIstResponse> {
        return mAddItemsToWishList
    }

    fun addWishListItem(request: WishListRequest){
        mStoreRepository.addWishListItem(request , object :
            WebserviceCallback<ApiResponseData<WishLIstResponse>> {
            override fun onWebResponse(data: ApiResponseData<WishLIstResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mAddItemsToWishList.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mAddItemsToWishList.value = data.data

                }
            }
        } )
    }

    fun observeCustomerWishlist(): MutableLiveData<WishLIstResponse> {
        return mGetWishList
    }

   /* fun getCustomerWishlist( ){
        mStoreRepository.getCustomerWishlist(object :
            WebserviceCallback<ApiResponseData<WishLIstResponse>> {
            override fun onWebResponse(data: ApiResponseData<WishLIstResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mGetWishList.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mGetWishList.value = data.data

                }
            }
        } )
    }*/

    fun observeDeleteWishListItem(): MutableLiveData<WishLIstResponse> {
        return mDeleteFromWishList
    }

    fun deleteWishListItem(itemId: String){
        mStoreRepository.deleteWishListItem(itemId ,object :
            WebserviceCallback<ApiResponseData<WishLIstResponse>> {
            override fun onWebResponse(data: ApiResponseData<WishLIstResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mDeleteFromWishList.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mDeleteFromWishList.value = data.data

                }
            }
        } )
    }



    fun observeOrderlist(): MutableLiveData<OrderListResponse> {
        return mGetOrders
    }

    fun getOrders(){
        mStoreRepository.getOrders(object :
            WebserviceCallback<ApiResponseData<OrderListResponse>> {
            override fun onWebResponse(data: ApiResponseData<OrderListResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mGetOrders.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mGetOrders.value = data.data

                }
            }
        } )
    }

    fun observeOrderDetails(): MutableLiveData<OrderListResponse> {
        return mGetOrderDetails
    }

    fun getOrderDetails(orderId: String){
        mStoreRepository.getOrderDetails(orderId , object :
            WebserviceCallback<ApiResponseData<OrderListResponse>> {
            override fun onWebResponse(data: ApiResponseData<OrderListResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mGetOrderDetails.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mGetOrderDetails.value = data.data

                }
            }
        } )
    }


    fun getFilterProductList(
        categoryId: String? = null,
        optionValue: String? = null,
      //  andOptionValue: String? = null
    ) {
        mStoreRepository.getFilterProductList(
            categoryId = categoryId,
            optionValue = optionValue,
            //andOptionValue = andOptionValue,
            webserviceCallback = object : WebserviceCallback<ApiResponseData<ProductListResponse>> {
                override fun onWebResponse(data: ApiResponseData<ProductListResponse>) {
                    if (data.status == ApiResponseData.API_SUCCESS) {
                        mFilterProductList.value = data.data
                    } else {
                        WolooApplication.errorMessage = data.message
                        mFilterProductList.value = data.data
                    }
                }
            }
        )
    }

    fun observeFilterProducts(): MutableLiveData<ProductListResponse> {
        return mFilterProductList
    }


    fun observeNotifyUserForProduct(): MutableLiveData<NotifyResponse> {
        return mNotifyProduct
    }

    fun getNotifyUserForProduct(request : NotifyRequest ){
        mStoreRepository.getNotifyUserForProduct(request ,object :
            WebserviceCallback<ApiResponseData<NotifyResponse>> {
            override fun onWebResponse(data: ApiResponseData<NotifyResponse>) {
                if (data.status == ApiResponseData.API_SUCCESS) {
                    mNotifyProduct.value = data.data
                } else {
                    WolooApplication.errorMessage = data.message
                    mNotifyProduct.value = data.data

                }
            }
        })
    }






}