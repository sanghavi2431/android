package `in`.woloo.www.application_kotlin.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import `in`.woloo.www.application_kotlin.interfaces.OtpReceivedInterface
import `in`.woloo.www.utils.Logger
import java.util.regex.Pattern

class SmsBroadcastReceiver : BroadcastReceiver() {
    private var otpReceiveInterface: OtpReceivedInterface? = null
    fun setOnOtpListeners(otpReceiveInterface: OtpReceivedInterface?) {
        this.otpReceiveInterface = otpReceiveInterface
    }

    override fun onReceive(context: Context, intent: Intent) {
        Logger.d(TAG, "onReceive: ")
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val mStatus = extras!![SmsRetriever.EXTRA_STATUS] as Status?
            when (mStatus!!.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    // Get SMS message contents'
                    val message = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?
                    Logger.d(TAG, "onReceive: failure $message")
                    if (otpReceiveInterface != null) {
                        val pattern = Pattern.compile("(\\d{4})")
                        val matcher = pattern.matcher(message.toString())
                        var otp1: String? = ""
                        if (matcher.find()) {
                            otp1 = matcher.group(0) // 4 digit number
                        }
                        otpReceiveInterface!!.onOtpReceived(otp1)
                    }
                }

                CommonStatusCodes.TIMEOUT -> {
                    // Waiting for SMS timed out (5 minutes)
                    Logger.d(TAG, "onReceive: failure")
                    if (otpReceiveInterface != null) {
                        otpReceiveInterface!!.onOtpTimeout()
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "SmsBroadcastReceiver"
    }
}
