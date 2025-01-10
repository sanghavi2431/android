package `in`.woloo.www.more.cancelsubscription.mvp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import com.android.volley.VolleyError
import com.jetsynthesys.encryptor.JetEncryptor
import `in`.woloo.www.application_kotlin.api_classes.APIConstants
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPICallModel
import `in`.woloo.www.application_kotlin.api_classes.NetworkAPIResponseCallback
import `in`.woloo.www.application_kotlin.model.server_response.AuthConfigResponse
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.cancelsubscription.model.CancelSubscriptionResponse
import `in`.woloo.www.networksUtils.NetworkAPICall
import `in`.woloo.www.utils.AppConstants
import org.json.JSONObject

class CancelSubscriptionPresenter(
    private val context: Context,
    private val cancelSubscriptionView: CancelSubscriptionView
) :
    NetworkAPIResponseCallback {
    private val mNetworkAPICall: NetworkAPICall
    private val mJetEncryptor: JetEncryptor
    private val mCommonUtils =
        CommonUtils()

    init {
        mNetworkAPICall = NetworkAPICall()
        mJetEncryptor = JetEncryptor.getInstance()
    }

    fun cancelSubscription(reason: String?, remark: String?, supportEmail: AuthConfigResponse.SUPPORT_EMAIL?) {
        /*Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:woloo.in@gmail.com"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"woloo.in@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, reason);
        intent.putExtra(Intent.EXTRA_TEXT, remark);
        try{
            //Intent.createChooser(intent, "Send email");
            context.startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(context, "No email app", Toast.LENGTH_SHORT).show();
        }*/

        val selectorIntent = Intent(Intent.ACTION_SENDTO)
        selectorIntent.setData(Uri.parse("mailto:"))
        var emailAddress = try {
            if (supportEmail != null && !TextUtils.isEmpty(supportEmail.id)) {
                supportEmail.id
            } else {
                DEFAULT_SUPPORT_EMAIL
            }
        } catch (e: NullPointerException) {
            DEFAULT_SUPPORT_EMAIL
        }

        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String?>(emailAddress))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, reason)
        emailIntent.putExtra(Intent.EXTRA_TEXT, remark)
        emailIntent.selector = selectorIntent
        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No email app available", Toast.LENGTH_SHORT).show()
        }

        /*try {
            JSONObject mJsObjParam = new JSONObject();
            mJsObjParam.put("cancel_reason",reason);
            mJsObjParam.put("remark",remark);
            Type parserType = new TypeToken<CancelSubscriptionResponse>() {
            }.getType();
            NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.CANCEL_SUBSCRIPTION, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
            networkAPICallModel.setParserType(parserType);
            networkAPICallModel.setShowProgress(true);
            mNetworkAPICall.callApplicationWS((Activity) context, networkAPICallModel, this);
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }*/
    }


    override fun onSuccessResponse(
        response: JSONObject?,
        networkAPICallModel: NetworkAPICallModel?
    ) {
        when (networkAPICallModel!!.apiURL) {
            APIConstants.CANCEL_SUBSCRIPTION -> try {
                val cancelSubscriptionResponse: CancelSubscriptionResponse =
                    networkAPICallModel.responseObject as CancelSubscriptionResponse
                cancelSubscriptionResponseFlow(cancelSubscriptionResponse)
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }

            else -> {}
        }
    }

    private fun cancelSubscriptionResponseFlow(cancelSubscriptionResponse: CancelSubscriptionResponse?) {
        try {
            if (cancelSubscriptionResponse != null && cancelSubscriptionResponse.status.equals(
                    AppConstants.API_SUCCESS
                )
            ) {
                cancelSubscriptionView.cancelSubscriptionResponse(cancelSubscriptionResponse)
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }


    override fun onFailure(volleyError: VolleyError?, networkAPICallModel: NetworkAPICallModel?) {
    }

    override fun onNoInternetConnection(
        context: Activity?,
        networkAPICallModel: NetworkAPICallModel?,
        networkAPIResponseCallback: NetworkAPIResponseCallback?
    ) {
    }

    override fun onTimeOutConnection(
        context: Activity?,
        networkAPICallModel: NetworkAPICallModel?,
        networkAPIResponseCallback: NetworkAPIResponseCallback?
    ) {
    }

    companion object {
        private const val DEFAULT_SUPPORT_EMAIL = "woloo.in@gmail.com"
    }
}
