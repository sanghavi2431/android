package `in`.woloo.www.application_kotlin.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.widget.EditText
import android.widget.TextView
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.Logger
import java.util.regex.Pattern

class OTPKotlinReceiver : BroadcastReceiver() {
    fun setEditText(
        one: EditText?,
        two: EditText?,
        three: EditText?,
        four: EditText?,
        txtProceed: TextView?
    ) {
        Companion.one = one
        Companion.two = two
        Companion.three = three
        Companion.four = four
        Companion.txtProceed = txtProceed
    }

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (sms in messages) {
                val msg = sms.messageBody.trim { it <= ' ' }
                Logger.e("msg", msg)
                //                String[] separated = msg.split(": ");
//                String otp1 = separated[1];
                val pattern = Pattern.compile("(\\d{4})")
                val matcher = pattern.matcher(msg)
                var otp1 = ""
                if (matcher.find()) {
                    otp1 = matcher.group(0) // 4 digit number
                }
                one!!.setText(otp1.substring(0, 1))
                two!!.setText(otp1.substring(1, 2))
                three!!.setText(otp1.substring(2, 3))
                four!!.setText(otp1.substring(3, 4))
                txtProceed!!.performClick()
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    companion object {
        private val otp: String? = null
        private var one: EditText? = null
        private var two: EditText? = null
        private var three: EditText? = null
        private var four: EditText? = null
        private var txtProceed: TextView? = null
    }
}
