package in.woloo.www.vtion.utilities;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.woloo.www.R;

public class Utility {

    private static Pattern pattern;
    private static Matcher matcher;

    /*
     * Check is Edit Text null
     */
    public static boolean isEditTextNull(EditText text, Activity context,
                                         String nameOfField) {
        if (!TextUtils.isEmpty(text.getText().toString().trim())) {
            return true;
        } else {
            ShowMessages st = new ShowMessages(context);
            st.makeMessage(MessageList.ENTER + nameOfField);
            text.setHint(MessageList.ENTER + nameOfField);
            text.setHintTextColor(context.getResources().getColor(R.color.dark_yellow));
            return false;
        }
    }

    public static boolean isEditTextNotSelected(EditText text, Activity context,
                                         String nameOfField) {
        if (!TextUtils.isEmpty(text.getText().toString().trim())) {
            return true;
        } else {
            ShowMessages st = new ShowMessages(context);
            st.makeMessage(MessageList.SELECT + nameOfField);
            text.setHint(MessageList.SELECT + nameOfField);
            text.setHintTextColor(context.getResources().getColor(R.color.dark_yellow));
            return false;
        }
    }


}
