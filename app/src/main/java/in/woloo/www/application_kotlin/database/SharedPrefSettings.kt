package `in`.woloo.www.application_kotlin.database

import android.text.TextUtils
import com.google.gson.Gson
import `in`.woloo.www.application_kotlin.model.server_response.AuthConfigResponse
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.EnrouteDirectionActivity
import `in`.woloo.www.application_kotlin.model.lists_models.UserDetails


class SharedPrefSettings {

    private val sharedPrefClient: SharedPrefClient = SharedPrefClient.getSharedPrefClient

    companion object {
        const val SHARED_PREF_NAME = "woloo_shared_pref"

        const val KEY_TOKEN = "token"
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



        private var pref: SharedPrefSettings? = null
        val getPreferences: SharedPrefSettings
            get() {
                if (pref == null) {
                    pref = SharedPrefSettings()
                }
                return pref!!
            }
    }

    fun storeToken(token: String) {
        sharedPrefClient.setString(KEY_TOKEN, token)
    }

    fun fetchToken(): String? {
        return sharedPrefClient.getString(KEY_TOKEN)
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

}