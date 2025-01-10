package `in`.woloo.www.application_kotlin.netcore

import android.content.Context
import com.netcore.android.Smartech
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.application_kotlin.model.lists_models.UserDetails
import io.hansel.hanselsdk.Hansel
import java.lang.ref.WeakReference

class NetcoreUserDetails (var context : Context){

    fun checkIfUserIdentityIsSet(){
        val userInfo: UserDetails = CommonUtils().userInfo!!
        val identity = Smartech.getInstance(WeakReference(context)).getUserIdentity()

        if (identity !=  userInfo.mobile.toString()){
            setNetcoreUserIdentityAndLogin(userInfo.mobile.toString())
        }
    }

    fun setNetcoreUserIdentityAndLogin(mobile : String) {
        Smartech.getInstance(WeakReference(context)).setUserIdentity(mobile)
        Smartech.getInstance(WeakReference(context)).login(mobile)
        Hansel.getUser().userId = mobile;
    }

    fun updateNetcoreUserProfile(){
        val userInfo: UserDetails = CommonUtils().userInfo!!

        //Saving User Profile  Data to Netcore
        val payload: HashMap<String, Any> = HashMap()
        payload[USER_ID] = userInfo.id!!
        payload[ROLE_ID] = userInfo.roleId.toString()
        payload[MOBILE_NO] = userInfo.mobile.toString()
        payload[APP_VERSION] = BuildConfig.VERSION_NAME
        if (userInfo.name!= null)
            payload[NAME] = userInfo.name.orEmpty()
        if (userInfo.email!= null)
            payload[EMAIL] = userInfo.email.orEmpty()
        if (userInfo.gender!= null)
            payload[GENDER] = userInfo.gender.orEmpty()
       try{
           Logger.e("EXpiry date",userInfo.expiryDate!!)
           Logger.e("isFreeTrial",userInfo.isFreeTrial.toString())
           Logger.e("subscriptionId",userInfo.subscriptionId!!)
           Logger.e("voucherId",userInfo.voucherId!!)
           if (userInfo.dob!= null)
               payload[DOB] = CommonUtils.getDDMMYYYYDate(userInfo.dob!!)
           if (userInfo.expiryDate != null) {
               val expiry = CommonUtils.getDDMMYYYYDate(userInfo.expiryDate!!)
               payload[EXPIRY_DATE] = expiry
               if((userInfo.subscriptionId == null && userInfo.voucherId == null) || userInfo.isFreeTrial == 1 ) {
                   payload[FREE_TRIAL_EXPIRY] = expiry
               }
           }
       }catch (e : Exception){

       }
        Smartech.getInstance(WeakReference(context)).updateUserProfile(payload)
    }

    fun updateNetcoreUserProfile(roleId : String?, name: String?, email:String?,gender : String?,dob:String?,expiryDate: String?,isFreeTrial : Boolean){

        //Saving User Profile  Data to Netcore
        val payload: HashMap<String, Any> = HashMap()
        payload[ROLE_ID] = roleId.toString()
        payload[NAME] = name.orEmpty()
        payload[EMAIL] = email.orEmpty()
        payload[GENDER] = gender.orEmpty()
        try{
            if (dob!= null)
                payload[DOB] = CommonUtils.getDDMMYYYYDate(dob)
            if (expiryDate != null) {
                val expiry = CommonUtils.getDDMMYYYYDate(expiryDate)
                payload[EXPIRY_DATE] = expiry
                if(isFreeTrial ) {
                    payload[FREE_TRIAL_EXPIRY] = expiry
                }
            }
        }catch (e : Exception){

        }
        Smartech.getInstance(WeakReference(context)).updateUserProfile(payload)
    }

    companion object {
        const val USER_ID = "USER_ID"
        const val ROLE_ID = "ROLE_ID"
        const val NAME = "NAME"
        const val EMAIL = "EMAIL"
        const val GENDER = "GENDER"
        const val DOB = "DOB"
        const val EXPIRY_DATE = "EXPIRY_DATE"
        const val FREE_TRIAL_EXPIRY = "FREE_TRIAL_EXPIRY"
        const val MOBILE_NO = "MOBILE_NO"
        const val APP_VERSION = "APP_VERSION"
    }
}