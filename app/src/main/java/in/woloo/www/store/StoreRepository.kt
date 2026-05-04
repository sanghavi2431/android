package `in`.woloo.www.store

import com.google.gson.Gson
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData
import `in`.woloo.www.application_kotlin.api_classes.ApiService
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceCallback
import `in`.woloo.www.application_kotlin.api_classes.ApiServiceClientAdapter
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.WebserviceCallback
import `in`.woloo.www.application_kotlin.model.server_response.WebViewResponse
import `in`.woloo.www.application_kotlin.utilities.NetworkUtils
import `in`.woloo.www.common.CommonUtils
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
import `in`.woloo.www.store.cart_request_response.InventoryErrorSummary
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
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreRepository {

    val apiService: ApiService = StoreApiServiceClientAdapter.instance.apiService
    val apiServiceDelivery: ApiService = DeliveryApiServiceClientAdapter.instance.apiService
    val apiServiceLooDiscovery: ApiService = ApiServiceClientAdapter.instance.apiService

    fun getProductList(
        webserviceCallback: WebserviceCallback<ApiResponseData<ProductListResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<ProductListResponse> =
                    apiService.getProductList()
                val callback: ApiServiceCallback<ProductListResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<ProductListResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getProductDetailsWithPrice(id: String ,fields : String ,regionId : String ,
                                   webserviceCallback: WebserviceCallback<ApiResponseData<ProductDetailsResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<ProductDetailsResponse> =
                    apiService.getProductDetailsWithPrice(id ,fields , regionId)
                val callback: ApiServiceCallback<ProductDetailsResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<ProductDetailsResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getProductWithPriceList(fields : String ,regionId : String ,
        webserviceCallback: WebserviceCallback<ApiResponseData<ProductListResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<ProductListResponse> =
                    apiService.getProductWithPriceList(fields , regionId)
                val callback: ApiServiceCallback<ProductListResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<ProductListResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getProductWithPriceListWithQuery(fields : String ,regionId : String , q:String ,
                                webserviceCallback: WebserviceCallback<ApiResponseData<ProductListResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<ProductListResponse> =
                    apiService.getProductWithPriceListWithQuery(fields , regionId , q)
                val callback: ApiServiceCallback<ProductListResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<ProductListResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun getCategoriesList(
        webserviceCallback: WebserviceCallback<ApiResponseData<CategoriesListResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<CategoriesListResponse> =
                    apiService.getCategoriesList()
                val callback: ApiServiceCallback<CategoriesListResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<CategoriesListResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getCollectionsNameImageList(
        webserviceCallback: WebserviceCallback<ApiResponseData<CollectionsListResponse>> , fields : String
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<CollectionsListResponse> =
                    apiService.getCollections(fields , 50)
                val callback: ApiServiceCallback<CollectionsListResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<CollectionsListResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getCollectionWiseProductList(
        webserviceCallback: WebserviceCallback<ApiResponseData<ProductListResponse>> , fields : String
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<ProductListResponse> =
                    apiService.getCollectionWiseProducts("*variants.calculated_price,+variants.inventory_quantity,*categories" , fields)
                val callback: ApiServiceCallback<ProductListResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<ProductListResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getCategoryWiseProductList(
        webserviceCallback: WebserviceCallback<ApiResponseData<ProductListResponse>> , fields : String
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<ProductListResponse> =
                    apiService.getCategoryWiseProducts("*variants.calculated_price,+variants.inventory_quantity,*categories" ,fields)
                val callback: ApiServiceCallback<ProductListResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<ProductListResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getSearchKeywordProductList(
        webserviceCallback: WebserviceCallback<ApiResponseData<ProductListResponse>> , fields : String
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<ProductListResponse> =
                    apiService.getSearchedProducts("*variants.calculated_price,+variants.inventory_quantity,*categories" ,fields)
                val callback: ApiServiceCallback<ProductListResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<ProductListResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getRegisterOnMedusa(request: RegisterOnMedusaRequest,
                            webserviceCallback: WebserviceCallback<ApiResponseData<AuthTokenResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<AuthTokenResponse> =
                    apiService.getRegisteredOnMedusa(request)
                val callback: ApiServiceCallback<AuthTokenResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<AuthTokenResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getAddCustomerOnMedusa(request: AddCustomerOnMedusaRequest,
                                    webserviceCallback: WebserviceCallback<ApiResponseData<CustomerListResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<CustomerListResponse> =
                    apiService.getAddCustomerOnMedusa(request)
                val callback: ApiServiceCallback<CustomerListResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<CustomerListResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getUpdateCustomerOnMedusa(customerId : String ,request: UpdateCustomerRequest,
                               webserviceCallback: WebserviceCallback<ApiResponseData<CustomerListResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<CustomerListResponse> =
                    apiService.getUpdateCustomerOnMedusa(request)
                val callback: ApiServiceCallback<CustomerListResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<CustomerListResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getCustomerAuthPassOnMedusa(request: RegisterOnMedusaRequest,
                            webserviceCallback: WebserviceCallback<ApiResponseData<AuthTokenResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<AuthTokenResponse> =
                    apiService.getCustomerAuthProviderRegistered(request)
                val callback: ApiServiceCallback<AuthTokenResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<AuthTokenResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun informRegisteredToShop(webserviceCallback : WebserviceCallback<ApiResponseData<BaseResponse<JSONObject>>>)
    {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<BaseResponse<JSONObject>> =
                    apiServiceLooDiscovery.informRegisteredToShop()
                val callback: ApiServiceCallback<BaseResponse<JSONObject>> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
          /*  val data = ApiServiceCallback<BaseResponse<JSONObject>>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)*/
        }
    }



    fun getAddressList(webserviceCallback: WebserviceCallback<ApiResponseData<AddressListResponse>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<AddressListResponse> =
                    apiService.getAddressesList("id,address_name,company,customer_id,first_name,last_name,address_1,city,province,postal_code,country_code,phone")
                val callback: ApiServiceCallback<AddressListResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<AddressListResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getCartCreated(request: CartRequest,
                               webserviceCallback: WebserviceCallback<ApiResponseData<CartResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<CartResponse> =
                    apiService.getCartCreated(request)
                val callback: ApiServiceCallback<CartResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<CartResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getCartList(cartId : String ,
                       webserviceCallback: WebserviceCallback<ApiResponseData<CartResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<CartResponse> =
                    apiService.getCartList(  cartId,"items.variant.*,items.variant.options.*,+items.product.*,items.product.images.*")
                val callback: ApiServiceCallback<CartResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<CartResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    /*fun getCheckCartInventory(cartId : String ,
                    webserviceCallback: WebserviceCallback<ApiResponseData<CartResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<CartResponse> =
                    apiService.getCheckCartInventory( cartId)
                val callback: ApiServiceCallback<CartResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<CartResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }*/


    fun getCheckCartInventory(
        cartId: String,
        successCallback: (InventoryResponse) -> Unit,
        errorCallback: (InventoryErrorResponse) -> Unit
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<ResponseBody> = apiService.getCheckCartInventory(cartId)
                call.enqueue(object : Callback<ResponseBody> {
                   /* override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Logger.i("Aarati repo 1", "setLiveData ${response.toString()}")
                        response.body()?.string()?.let { json ->
                            Logger.i("Aarati repo 2", "setLiveData ${json.toString()}")
                            val jsonObject = JSONObject(json)
                            val isSuccess = jsonObject.getBoolean("success")
                            val gson = Gson()
                            Logger.i("Aarati repo 3", "setLiveData ${isSuccess}")
                            if (isSuccess) {
                                val successData = gson.fromJson(json, InventoryResponse::class.java)
                                successCallback(successData)
                            } else {
                                val errorData = gson.fromJson(json, InventoryErrorResponse::class.java)
                                errorCallback(errorData)
                            }
                        }
                    }*/

                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                        Logger.i("Aarati repo 1", "setLiveData ${response.toString()}")

                        val json = if (response.isSuccessful) {
                            response.body()?.string()
                        } else {
                            response.errorBody()?.string()
                        }

                        if (!json.isNullOrEmpty()) {
                            Logger.i("Aarati repo 2", "setLiveData $json")
                            val jsonObject = JSONObject(json)
                            val isSuccess = jsonObject.optBoolean("success", false)
                            val gson = Gson()
                            Logger.i("Aarati repo 3", "setLiveData $isSuccess")

                            if (isSuccess) {
                                val successData = gson.fromJson(json, InventoryResponse::class.java)
                                successCallback(successData)
                            } else {
                                val errorData = gson.fromJson(json, InventoryErrorResponse::class.java)
                                errorCallback(errorData)
                            }
                        } else {
                            Logger.i("Aarati repo", "Empty JSON response")
                            errorCallback(
                                InventoryErrorResponse(
                                    false,
                                    "Empty response",
                                    errors = ArrayList(),
                                    summary = InventoryErrorSummary(0, 0, 0)
                                )
                            )
                        }
                    }



                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Logger.i("Aarati repo 4", "setLiveData ${call.toString()}")
                        errorCallback(InventoryErrorResponse(false, t.localizedMessage ?: "Unknown error",  errors = ArrayList(), InventoryErrorSummary(0, 0, 0)))

                    }
                })
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
        } else {
            Logger.i("Aarati repo 5", "setLiveData ")
            errorCallback(InventoryErrorResponse(false, "No Internet",  errors = ArrayList(), InventoryErrorSummary(0, 0, 0)))
        }
    }




    fun getAddToCart(cartId : String ,request: CartAddRequest,
                       webserviceCallback: WebserviceCallback<ApiResponseData<CartResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<CartResponse> =
                    apiService.getAddToCart(cartId ,"items.variant.*,items.variant.options.*,+items.product.*,items.product.images.*",request)
                val callback: ApiServiceCallback<CartResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<CartResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getUpdateToCart(cartId : String ,lineId : String , request: CartUpdateRequest,
                     webserviceCallback: WebserviceCallback<ApiResponseData<CartResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<CartResponse> =
                    apiService.getUpdateToCart(cartId , lineId ,"items.variant.*,items.variant.options.*,+items.product.*,items.product.images.*",request)
                val callback: ApiServiceCallback<CartResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<CartResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getDeleteFromCart(cartId : String ,lineId : String ,
                        webserviceCallback: WebserviceCallback<ApiResponseData<DeleteFromCartResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<DeleteFromCartResponse> =
                    apiService.getDeleteFromCart(cartId , lineId , "items.variant.*,items.variant.options.*,+items.product.*,items.product.images.*")
                val callback: ApiServiceCallback<DeleteFromCartResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<DeleteFromCartResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getRegionList(
        webserviceCallback: WebserviceCallback<ApiResponseData<RegionListResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<RegionListResponse> =
                    apiService.getRegionsList()
                val callback: ApiServiceCallback<RegionListResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<RegionListResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getApplyPromotionsToCart(cartId : String , request: AddPromotionsRequest,
                        webserviceCallback: WebserviceCallback<ApiResponseData<CartResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<CartResponse> =
                    apiService.getPromotionsToCart(cartId  ,request)
                val callback: ApiServiceCallback<CartResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<CartResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getDeletePromotionsFromCart(cartId : String ,request: AddPromotionsRequest,
                          webserviceCallback: WebserviceCallback<ApiResponseData<CartResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<CartResponse> =
                    apiService.getDeletePromotionsFromCart(cartId , request)
                val callback: ApiServiceCallback<CartResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<CartResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun getDeliveryCodesList(
        webserviceCallback: WebserviceCallback<ApiResponseData<DeliveryCodesResponse>> , fields : String
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<DeliveryCodesResponse> =
                    apiServiceDelivery.getDeliveryPartnersListFromPincode(fields)
                val callback: ApiServiceCallback<DeliveryCodesResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<DeliveryCodesResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun getCitiesFromSalesChannles(
        webserviceCallback: WebserviceCallback<ApiResponseData<CityFromSalesChannelResponse>>, fields : String
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<CityFromSalesChannelResponse> =
                    apiService.getCitiesFromSalesChannles(fields)
                val callback: ApiServiceCallback<CityFromSalesChannelResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<CityFromSalesChannelResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun getAdminAuthPassOnMedusa(request: RegisterOnMedusaRequest,
                                    webserviceCallback: WebserviceCallback<ApiResponseData<AuthTokenResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<AuthTokenResponse> =
                    apiService.getAdminAuthProviderRegistered(request)
                val callback: ApiServiceCallback<AuthTokenResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<AuthTokenResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun getAddAddress(request: CreateUpdateAddressRequest,
                      webserviceCallback: WebserviceCallback<ApiResponseData<AddAddressResponse>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<AddAddressResponse> =
                    apiService.getAddAddresses(request)
                val callback: ApiServiceCallback<AddAddressResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<AddAddressResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }



    fun getDeleteAddress(addressId: String ,
                      webserviceCallback: WebserviceCallback<ApiResponseData<DeleteAddressResponse>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<DeleteAddressResponse> =
                    apiService.getDeleteAddresses(addressId)
                val callback: ApiServiceCallback<DeleteAddressResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<DeleteAddressResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun getUpdateAddress(addressId: String ,request: CreateUpdateAddressRequest,
                         webserviceCallback: WebserviceCallback<ApiResponseData<AddAddressResponse>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<AddAddressResponse> =
                    apiService.getUpdateAddresses(addressId ,request)
                val callback: ApiServiceCallback<AddAddressResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<AddAddressResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun getReviewOfProduct(productID: String ,
                         webserviceCallback: WebserviceCallback<ApiResponseData<ReviewsResponse>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<ReviewsResponse> =
                    apiService.getReviewsForProduct(productID , "1")
                val callback: ApiServiceCallback<ReviewsResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<ReviewsResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun getAddReview(request: AddReviewRequest,
                         webserviceCallback: WebserviceCallback<ApiResponseData<ReviewListData>>) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<ReviewListData> =
                    apiService.getAddReview(request)
                val callback: ApiServiceCallback<ReviewListData> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<ReviewListData>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getShippingBillingAddressToCart(cartId : String , request: ShippingBillingAddressRequest,
                        webserviceCallback: WebserviceCallback<ApiResponseData<CartResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<CartResponse> =
                    apiService.getShippingAndBillingToCart(cartId , request)
                val callback: ApiServiceCallback<CartResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<CartResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getShippingOptionsList(cartId : String,
                                webserviceCallback: WebserviceCallback<ApiResponseData<ShippingOptionsResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<ShippingOptionsResponse> =
                    apiService.getShippingOptionsList(cartId)
                val callback: ApiServiceCallback<ShippingOptionsResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<ShippingOptionsResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getShippingMethodsToCart(optionId : String , request: ShippingMethodsRequest,
                                        webserviceCallback: WebserviceCallback<ApiResponseData<CartResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<CartResponse> =
                    apiService.getShippingMethodsToCart(optionId , request)
                val callback: ApiServiceCallback<CartResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<CartResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun getCalculateShippingOptions(optionId : String, request: CalculateShippingRequest,
                                   webserviceCallback: WebserviceCallback<ApiResponseData<CalculateShippingResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<CalculateShippingResponse> =
                    apiService.getCalculateShippingOptions(optionId , request)
                val callback: ApiServiceCallback<CalculateShippingResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<CalculateShippingResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun getPaymentProvidersList(regionId : String,
                                webserviceCallback: WebserviceCallback<ApiResponseData<PaymentProviderResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<PaymentProviderResponse> =
                    apiService.getPaymentProvidersList(regionId)
                val callback: ApiServiceCallback<PaymentProviderResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<PaymentProviderResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun getPaymentCollection( request: PaymentCollectionRequest,
                                 webserviceCallback: WebserviceCallback<ApiResponseData<PaymentCollectionsResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<PaymentCollectionsResponse> =
                    apiService.getPaymentCollection(request)
                val callback: ApiServiceCallback<PaymentCollectionsResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<PaymentCollectionsResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun getPaymentSession(paymentCollectionId : String , request: PaymentSessionRequest,
                                 webserviceCallback: WebserviceCallback<ApiResponseData<PaymentCollectionsResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<PaymentCollectionsResponse> =
                    apiService.getPaymentSession(paymentCollectionId , request)
                val callback: ApiServiceCallback<PaymentCollectionsResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<PaymentCollectionsResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun deletePaymentSession( request: DeletePaymentSessionRequest,
                          webserviceCallback: WebserviceCallback<ApiResponseData<DeletePaymentSessionResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<DeletePaymentSessionResponse> =
                    apiService.deletePaymentsSession(request)
                val callback: ApiServiceCallback<DeletePaymentSessionResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<DeletePaymentSessionResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun getCompleteCart(cartId : String ,
                                 webserviceCallback: WebserviceCallback<ApiResponseData<CompleteCartResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<CompleteCartResponse> =
                    apiService.getCompleteCart(cartId)
                val callback: ApiServiceCallback<CompleteCartResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<CompleteCartResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun createWishlist(webserviceCallback: WebserviceCallback<ApiResponseData<WishLIstResponse>>)
    {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<WishLIstResponse> =
                    apiService.createWishlist()
                val callback: ApiServiceCallback<WishLIstResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<WishLIstResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getCustomerWishlist(webserviceCallback: WebserviceCallback<ApiResponseData<WishLIstResponse>>)
    {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<WishLIstResponse> =
                    apiService.getCustomerWishlist("inr")
                val callback: ApiServiceCallback<WishLIstResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<WishLIstResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun addWishListItem(request: WishListRequest, webserviceCallback: WebserviceCallback<ApiResponseData<WishLIstResponse>>)
    {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<WishLIstResponse> =
                    apiService.addWishListItem(request)
                val callback: ApiServiceCallback<WishLIstResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<WishLIstResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun deleteWishListItem(itemId: String, webserviceCallback: WebserviceCallback<ApiResponseData<WishLIstResponse>>)
    {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<WishLIstResponse> =
                    apiService.deleteWishListItem(itemId)
                val callback: ApiServiceCallback<WishLIstResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<WishLIstResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getOrders( webserviceCallback: WebserviceCallback<ApiResponseData<OrderListResponse>>)
    {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<OrderListResponse> =
                    apiService.getOrders()
                val callback: ApiServiceCallback<OrderListResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<OrderListResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


    fun getOrderDetails(orderId: String , webserviceCallback: WebserviceCallback<ApiResponseData<OrderListResponse>>)
    {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<OrderListResponse> =
                    apiService.getOrderDetails(orderId)
                val callback: ApiServiceCallback<OrderListResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<OrderListResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }




    fun getFilterProductList(
        categoryId: String? = null,
        optionValue: String? = null,
       // andOptionValue: String? = null,
        webserviceCallback: WebserviceCallback<ApiResponseData<ProductListResponse>>
    ) {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<ProductListResponse> = apiService.getFilterProducts(
                    fields = "*variants.calculated_price,+variants.inventory_quantity",
                    categoryId = categoryId,
                    optionValue = optionValue,
                   // andOptionValue = andOptionValue
                )
                val callback: ApiServiceCallback<ProductListResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<ProductListResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }

    fun getNotifyUserForProduct(request: NotifyRequest,
                                webserviceCallback: WebserviceCallback<ApiResponseData<NotifyResponse>>)
    {
        if (NetworkUtils.isInternetAvailable(WolooApplication.instance!!)) {
            try {
                val call: Call<NotifyResponse> = apiService.getNotifyUserForProduct(request
                )
                val callback: ApiServiceCallback<NotifyResponse> =
                    ApiServiceCallback(webserviceCallback)
                call.enqueue(callback)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

        } else {
            val data = ApiResponseData<NotifyResponse>()
            data.status = ApiResponseData.API_NO_NETWORK
            webserviceCallback.onWebResponse(data)
        }
    }


}