package `in`.woloo.www.application_kotlin.database

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import `in`.woloo.www.application_kotlin.model.server_response.AuthConfigResponse
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.EnrouteDirectionActivity
import `in`.woloo.www.application_kotlin.utilities.GoogleMapUtils
import `in`.woloo.www.v2.splash.UserDetails


class SharedPrefSettings{

    private val sharedPrefClient: SharedPrefClient = SharedPrefClient.getSharedPrefClient
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val SHARED_PREF_NAME = "woloo_shared_pref"

        const val KEY_TOKEN = "token"
        const val KEY_TRANSPORT = "transport_mode"
        const val SUPPLIER_ID = "supplier_id"
        const val USER_DETAILS = "user_details"
        const val IS_LOGGED_IN = "is_logged_in"
        const val REFERRAL_CODE = "referral_code"
        const val AUTH_CONFIG = "auth_config"
        const val IS_SHOWN_ONBOARDING = "is_shown_onboarding"
        const val LOCATION_FOR_NETCORE = "location_for_netcore"
        const val IS_WOLOO_DIRECTIONS = "is_woloo_directions"
        const val DIRECTION_WOLOO= "direction_woloo"
        const val IS_VTION_SCREEN = "is_vtion_screen"
        const val IS_VTION_USER = "is_vtion_user"
        const val SUBSCRIPTION_PLAN = "subscription_plan"
        const val WOLOO_POINTS = "woloo_points"
        const val REGISTER_TOKEN = "register_token"
        const val LOGIN_TOKEN = "login_token"
        const val ADMIN_LOGIN_TOKEN = "admin_login_token"
        const val REGION_ID = "region_id"
        const val CART_ID = "cart_id"
        const val SELECTED_ADDRESS_ID = "address_id"
        const val RECENT_SEARCHED_QUERY = "recent_searched_query"
        const val COINS_USED = "coins_used"
        const val DEFAULT_ADDRESS_ID = "default_address_id"
        const val STORE_CUSTOMER_ID = "store_customer_id"
        const val SHOP_PASSWORD = "shop_password"
        const val SHOP_MOBILE_NUMBER = "shop_mobile_number"
        const val SERVICE_CART_ID = "service_cart_id"
        const val SERVICE_RECENT_SEARCHED_QUERY = "service_recent_searched_query"
        const val IS_RETURNING_FROM_BOTTOM_SHEET  = "isReturningFromBottomSheet"
        const val IS_25_KM  = "is25km"

        const val NOWOLOOCOINSGIVEN  = "NOWOLOOCOINSGIVEN"
        const val NOWOLOOCOINSGIVENSEARCH  = "NOWOLOOCOINSGIVENSEARCH"
        const val NOWOLOOCOINSGIVENENROUTE  = "noCoinsEnroute"


        private var pref: SharedPrefSettings? = null
        val getPreferences: SharedPrefSettings
            get() {
                if (pref == null) {
                    pref = SharedPrefSettings()
                }
                return pref!!
            }
    }

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    }

    fun setString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String, default: String? = null): String? {
        return sharedPreferences.getString(key, default)
    }

    fun storeToken(token: String) {
        sharedPrefClient.setString(KEY_TOKEN, token)
    }

    fun fetchToken(): String? {
        return sharedPrefClient.getString(KEY_TOKEN)
    }

    fun storeTransportMode(transportmode: String) {
        sharedPrefClient.setString(KEY_TRANSPORT, transportmode)
    }

    fun fetchTransportMode(): String? {
        return sharedPrefClient.getString(KEY_TRANSPORT)
    }

    fun storeLocationForNetcore(locationString: String) {
        sharedPrefClient.setString(LOCATION_FOR_NETCORE, locationString)
    }

    fun fetchLocationForNetcore(): String? {
        return sharedPrefClient.getString(LOCATION_FOR_NETCORE)
    }

    fun storeReferralCode(code: String){
        sharedPrefClient.setString(REFERRAL_CODE, code)
    }

    fun fetchReferralCode(): String? {
        return sharedPrefClient.getString(REFERRAL_CODE)
    }

    fun storeIsLoggedIn(isLoggedIn: Boolean) {
        sharedPrefClient.setBoolean(IS_LOGGED_IN, isLoggedIn)
    }

    fun fetchIsLoggedIn(): Boolean {
        return sharedPrefClient.getBoolean(IS_LOGGED_IN)
    }

    fun storeIsShownOnBoarding(isLoggedIn: Boolean) {
        sharedPrefClient.setBoolean(IS_SHOWN_ONBOARDING, isLoggedIn)
    }

    fun issShownOnBoarding(): Boolean {
        return sharedPrefClient.getBoolean(IS_SHOWN_ONBOARDING)
    }
    fun storeSupplierId(supplierId: Int) {
        sharedPrefClient.setInt(SUPPLIER_ID, supplierId)
    }

    fun fetchSupplierId(): Int {
        return sharedPrefClient.getInt(SUPPLIER_ID, 0)
    }

    fun storeUserDetails(user: UserDetails) {
        sharedPrefClient.setString(USER_DETAILS, Gson().toJson(user))
    }

    fun storeAuthConfig(config: AuthConfigResponse.Data){
        sharedPrefClient.setString(AUTH_CONFIG, Gson().toJson(config))
    }

    fun fetchAuthConfig(): AuthConfigResponse.Data? {
        if (TextUtils.isEmpty(sharedPrefClient.getString(AUTH_CONFIG, ""))) return null
        return Gson().fromJson(sharedPrefClient.getString(AUTH_CONFIG, ""), AuthConfigResponse.Data::class.java)
    }

    fun fetchUserDetails(): UserDetails? {
        if (TextUtils.isEmpty(sharedPrefClient.getString(USER_DETAILS, ""))) return null
        return Gson().fromJson(sharedPrefClient.getString(USER_DETAILS, ""), UserDetails::class.java)
    }

    fun storeIsDirectionWoloo(isDirection: Boolean) {
        sharedPrefClient.setBoolean(IS_WOLOO_DIRECTIONS, isDirection)
    }

    fun fetchIsDirectionWoloo(): Boolean {
        return sharedPrefClient.getBoolean(IS_WOLOO_DIRECTIONS)
    }

    fun fetchDirectionWoloo(): EnrouteDirectionActivity.DirectionWoloo? {
        if (TextUtils.isEmpty(sharedPrefClient.getString(DIRECTION_WOLOO, "")))
            return null
        return Gson().fromJson(sharedPrefClient.getString(DIRECTION_WOLOO, ""),
            EnrouteDirectionActivity.DirectionWoloo::class.java)
    }

    fun storeDirectionWoloo(woloo: EnrouteDirectionActivity.DirectionWoloo?) {
        if(woloo == null)
            sharedPrefClient.removeKey(DIRECTION_WOLOO)
        else
         sharedPrefClient.setString(DIRECTION_WOLOO, Gson().toJson(woloo))
    }

    fun clear() {
        sharedPrefClient.clear()
    }

    fun storeIsVTION(isVtionScreen: Boolean) {
        sharedPrefClient.setBoolean(IS_VTION_SCREEN, isVtionScreen)
    }

    fun fetchIsVTION(): Boolean {
        return sharedPrefClient.getBoolean(IS_VTION_SCREEN)
    }




    fun storeIsVTIONUser(isVtionUser: Boolean) {
        sharedPrefClient.setBoolean(IS_VTION_USER, isVtionUser)
    }

    fun fetchIsVTIONUser(): Boolean {
        return sharedPrefClient.getBoolean(IS_VTION_USER)
    }

    fun storeSubscriptionPlan(code: String){
        sharedPrefClient.setString(SUBSCRIPTION_PLAN, code)
    }

    fun fetchSubscriptionPlan(): String? {
        return sharedPrefClient.getString(SUBSCRIPTION_PLAN)
    }

    fun storeWolooPoints(code: String){
        sharedPrefClient.setString(WOLOO_POINTS, code)
    }

    fun fetchWolooPoints(): String? {
        return sharedPrefClient.getString(WOLOO_POINTS)
    }

    // SHOP AARATI

    fun storeShopRegisterToken(registerToken: String) {
        sharedPrefClient.setString(REGISTER_TOKEN, registerToken)
    }

    fun fetchShopRegisterToken(): String? {
        return sharedPrefClient.getString(REGISTER_TOKEN)
    }

    fun storeShopLoginToken(loginToken: String) {
        sharedPrefClient.setString(LOGIN_TOKEN, loginToken)
    }

    fun fetchShopLoginToken(): String? {
        return sharedPrefClient.getString(LOGIN_TOKEN)
    }

    fun storeShopAdminLoginToken(adminLoginToken: String) {
        sharedPrefClient.setString(ADMIN_LOGIN_TOKEN, adminLoginToken)
    }

    fun fetchShopAdminLoginToken(): String? {
        return sharedPrefClient.getString(ADMIN_LOGIN_TOKEN)
    }

    fun storeRegionId(regionId: String) {
        sharedPrefClient.setString(REGION_ID, regionId)
    }

    fun fetchRegionId(): String? {
        return sharedPrefClient.getString(REGION_ID)
    }

    fun storeCartId(cartId: String) {
        sharedPrefClient.setString(CART_ID, cartId)
    }

    fun fetchCartId(): String? {
        return sharedPrefClient.getString(CART_ID)
    }



    fun storeSelectedAddressId(address_id: String) {
        sharedPrefClient.setString(SELECTED_ADDRESS_ID, address_id)
    }

    fun fetchSelectedAddressId(): String? {
        return sharedPrefClient.getString(SELECTED_ADDRESS_ID)
    }


    fun storeRecentSearchedArray(recentSearchedArray: ArrayList<String>) {
        sharedPrefClient.setArrayList(RECENT_SEARCHED_QUERY, recentSearchedArray)
    }

    fun fetchRecentSearchedArray(): ArrayList<String>? {
        return sharedPrefClient.getArrayList(RECENT_SEARCHED_QUERY)
    }

    fun clearArrayList() {
        return sharedPrefClient.removeArrayList(RECENT_SEARCHED_QUERY)
    }

    fun storeCoinsUsed(coinsUsed: Boolean) {
        sharedPrefClient.setBoolean(COINS_USED, coinsUsed)
    }

    fun fetchCoinsUsed(): Boolean {
        return sharedPrefClient.getBoolean(COINS_USED)
    }

    fun storeDefaultAddressId(default_address_id: String) {
        sharedPrefClient.setString(DEFAULT_ADDRESS_ID, default_address_id)
    }

    fun fetchDefaultAddressId(): String? {
        return sharedPrefClient.getString(DEFAULT_ADDRESS_ID)
    }

    fun storeStoreCustomerId(store_customer_id: String) {
        sharedPrefClient.setString(STORE_CUSTOMER_ID, store_customer_id)
    }

    fun fetchStoreCustomerId(): String? {
        return sharedPrefClient.getString(STORE_CUSTOMER_ID)

    }


    fun storeDecryptedPassword(store_password: String) {
        sharedPrefClient.setString(SHOP_PASSWORD, store_password)
    }

    fun fetchDecryptedPassword(): String? {
        return sharedPrefClient.getString(SHOP_PASSWORD)

    }

    fun storeShopMobileNumber(store_mobile: String) {
        sharedPrefClient.setString(SHOP_MOBILE_NUMBER, store_mobile)
    }

    fun fetchShopMobileNumber(): String? {
        return sharedPrefClient.getString(SHOP_MOBILE_NUMBER)
    }

    // SERVICES AARATI
    fun storeServiceCartId(cartId: String) {
        sharedPrefClient.setString(SERVICE_CART_ID, cartId)
    }

    fun fetchServiceCartId(): String? {
        return sharedPrefClient.getString(SERVICE_CART_ID)
    }

    fun storeServiceRecentSearchedArray(recentSearchedArray: ArrayList<String>) {
        sharedPrefClient.setArrayList(SERVICE_RECENT_SEARCHED_QUERY, recentSearchedArray)
    }

    fun fetchServiceRecentSearchedArray(): ArrayList<String>? {
        return sharedPrefClient.getArrayList(SERVICE_RECENT_SEARCHED_QUERY)
    }

    fun clearServiceArrayList() {
        return sharedPrefClient.removeArrayList(SERVICE_RECENT_SEARCHED_QUERY)
    }

    fun storeIsReturningFromBottomSheet(isReturningFromBottomSheet: Boolean) {
        sharedPrefClient.setBoolean(IS_RETURNING_FROM_BOTTOM_SHEET, isReturningFromBottomSheet)
    }

    fun fetchIsReturningFromBottomSheet(): Boolean {
        return sharedPrefClient.getBoolean(IS_RETURNING_FROM_BOTTOM_SHEET)
    }

    fun storeIs25KM(is25KM: String) {
        sharedPrefClient.setString(IS_25_KM, is25KM)
    }

    fun fetchIs25KM(): String? {
        return sharedPrefClient.getString(IS_25_KM)
    }


    fun storeNoWolooCoinsGiven(NOWOLOOCOINSGIVEN: String) {
        sharedPrefClient.setString(NOWOLOOCOINSGIVEN, NOWOLOOCOINSGIVEN)
    }

    fun fetchNoWolooCoinsGiven(): String? {
        return sharedPrefClient.getString(NOWOLOOCOINSGIVEN)
    }

    fun storeNoWolooCoinsGivenSearch(NOWOLOOCOINSGIVENSEARCH: String) {
        sharedPrefClient.setString(NOWOLOOCOINSGIVENSEARCH, NOWOLOOCOINSGIVENSEARCH)
    }

    fun fetchNoWolooCoinsGivenSearch(): String? {
        return sharedPrefClient.getString(NOWOLOOCOINSGIVENSEARCH)
    }

    fun storeNoWolooCoinsGivenEnroute(NOWOLOOCOINSGIVENENROUTE: String) {
        sharedPrefClient.setString(NOWOLOOCOINSGIVENENROUTE, NOWOLOOCOINSGIVENENROUTE)
    }

    fun fetchNoWolooCoinsGivenEnroute(): String? {
        return sharedPrefClient.getString(NOWOLOOCOINSGIVENENROUTE)
    }


}