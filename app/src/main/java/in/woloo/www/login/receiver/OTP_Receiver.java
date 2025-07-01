package in.woloo.www.login.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.utils.Logger;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OTP_Receiver extends BroadcastReceiver {
    //    private  static in.aabhasjindal.otptextview.OtpTextView editText;
    private static String otp;
    private static EditText one, two, three, four;
    private static TextView txtProceed;

    public void setEditText(EditText one, EditText two, EditText three, EditText four,TextView txtProceed) {

        OTP_Receiver.one = one;
        OTP_Receiver.two = two;
        OTP_Receiver.three = three;
        OTP_Receiver.four = four;
        OTP_Receiver.txtProceed=txtProceed;
    }

    // OnReceive will keep trace when sms is been received in mobile
    @Override
    public void onReceive(Context context, Intent intent) {
        //message will be holding complete sms that is received
        try {
            SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            for (SmsMessage sms : messages) {
                String msg = sms.getMessageBody().trim();
                Logger.e("msg", msg.toString());
//                String[] separated = msg.split(": ");
//                String otp1 = separated[1];

                Pattern pattern = Pattern.compile("(\\d{4})");

                Matcher matcher = pattern.matcher(msg.toString());
                String otp1 = "";

                if (matcher.find()) {
                    otp1 = matcher.group(0);  // 4 digit number
                }


                one.setText(otp1.substring(0, 1));
                two.setText(otp1.substring(1, 2));
                three.setText(otp1.substring(2, 3));
                four.setText(otp1.substring(3, 4));
                txtProceed.performClick();
            }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }
}
