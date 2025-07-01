package in.woloo.www.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;

public class ToastUtils {
    /**
     * show toast.
     *
     * @param context context
     * @param text    text
     * @param isLong  isLong
     */
    public static void show(Context context, CharSequence text, boolean isLong) {
        try {
            View layout = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
            TextView txtToast = (TextView) layout.findViewById(R.id.txt_toast);
            txtToast.setText(text);
            Toast toast = new Toast(context);
//            toast.setGravity(Gravity.CENTER, 0, 0);
            if (isLong) {
                toast.setDuration(Toast.LENGTH_LONG);
            } else {
                toast.setDuration(Toast.LENGTH_SHORT);
            }
            toast.setView(layout);
            toast.show();
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }

    }

    /**
     * show toast.
     *
     * @param context context
     * @param text    text
     * @param isLong  isLong
     */
    public static void showSlowInternetToast(Context context, CharSequence text, boolean isLong) {
        if (isLong) {
            try {
                Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
                View toastView = toast.getView(); // This'll return the default View of the Toast.
                //And now you can get the TextView of the default View of the Toast.
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                int marginLeftRight=(int)context.getResources().getDimension(R.dimen.margin_20);
                int marginTopBottom=(int)context.getResources().getDimension(R.dimen.margin_15);
                // toastMessage.setGravity(Gravity.CENTER);
                // toastMessage.setPadding(marginLeftRight, marginTopBottom, marginLeftRight, marginTopBottom);
                //toastMessage.setBackgroundColor(context.getResources().getColor(R.color.gray_toast_bg));
                // toastView.setBackgroundColor(context.getResources().getColor(R.color.gray_toast_bg));
                toastView.setBackgroundResource(R.drawable.rounded_toast_bg);
                toastMessage.setTextColor(ContextCompat.getColor(context,R.color.white));
                toast.show();
                //Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                  CommonUtils.printStackTrace(e);
            }
        } else {
            try {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

                  CommonUtils.printStackTrace(e);
            }
        }
    }

    /**
     * show toast.
     *
     * @param context context
     * @param resId   resId
     * @param isLong  isLong
     */
    public static void show(Context context, @StringRes int resId, boolean isLong) {
      /*  if (isLong) {
            Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
        }*/

        show(context,context.getString(resId),isLong);
    }

    /**
     * show toast.
     *
     * @param context context
     * @param text    text
     */
    public static void show(Context context, CharSequence text) {
        show(context, text, false);
    }

    /**
     * show toast.
     *
     * @param context context
     * @param resId   resId
     */
    public static void show(Context context, @StringRes int resId) {
        show(context.getApplicationContext(), resId, false);
    }

    /**
     * show toast.
     *
     * @param context context
     * @param text    text
     * @param isLong  isLong
     */
    public static void s(Context context, CharSequence text, boolean isLong) {
        show(context, text, isLong);
    }

    /**
     * show toast.
     *
     * @param context context
     * @param resId   resId
     * @param isLong  isLong
     */
    public static void s(Context context, @StringRes int resId, boolean isLong) {
        show(context, resId, isLong);
    }

    /**
     * show toast.
     *
     * @param context context
     * @param text    text
     */
    public static void s(Context context, CharSequence text) {
        show(context, text);
    }

    /**
     * show toast.
     *
     * @param context context
     * @param resId   resId
     */
    public static void s(Context context, @StringRes int resId) {
        show(context, resId);
    }

    public static void onShowToast(Context mContext, int drawable, String title, String desc, boolean isLong) {
        try {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View layout = inflater.inflate(R.layout.item_custom_toast, null);

            ImageView ivIcon = layout.findViewById(R.id.ivIcon);
            //ivIcon.setImageResource(drawable);
            TextView tvTitle = layout.findViewById(R.id.tvTitle);
            if(title!=null){
                tvTitle.setText(title);
                tvTitle.setVisibility(View.VISIBLE);
            }
            else {
                tvTitle.setVisibility(View.GONE);
            }
            TextView tvDescription = layout.findViewById(R.id.tvDescription);
            tvDescription.setText(desc);

            Toast toast = new Toast(mContext);
            toast.setGravity(Gravity.TOP | Gravity.RIGHT, 0, 0);
            if (isLong) {
                toast.setDuration(Toast.LENGTH_LONG);
            } else {
                toast.setDuration(Toast.LENGTH_SHORT);
            }

            toast.setView(layout);
            toast.show();
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }

}
