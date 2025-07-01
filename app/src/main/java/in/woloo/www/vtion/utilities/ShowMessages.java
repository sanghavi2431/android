package in.woloo.www.vtion.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

/**
 * Created by Aarati on 29-Jun-2024.
 * This class customise the toast message.
 */
public class ShowMessages {


        Activity activity;
        int height , width;
        Context context;

        public ShowMessages(Activity activity){
            this.activity = activity;

        }

        public void makeMessage(String message) {
            LayoutInflater li = activity.getLayoutInflater();
            Toast toast = new Toast(activity);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setText(message);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
        }


}
