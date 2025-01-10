package `in`.woloo.www.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils

object ToastUtils {
    /**
     * show toast.
     *
     * @param context context
     * @param text    text
     * @param isLong  isLong
     */
    /**
     * show toast.
     *
     * @param context context
     * @param text    text
     */
    @JvmOverloads
    fun show(context: Context?, text: CharSequence?, isLong: Boolean = false) {
        try {
            val layout = LayoutInflater.from(context).inflate(R.layout.custom_toast, null)
            val txtToast = layout.findViewById<View>(R.id.txt_toast) as TextView
            txtToast.text = text
            val toast = Toast(context)
            //            toast.setGravity(Gravity.CENTER, 0, 0);
            if (isLong) {
                toast.duration = Toast.LENGTH_LONG
            } else {
                toast.duration = Toast.LENGTH_SHORT
            }
            toast.view = layout
            toast.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    /**
     * show toast.
     *
     * @param context context
     * @param text    text
     * @param isLong  isLong
     */
    fun showSlowInternetToast(context: Context, text: CharSequence?, isLong: Boolean) {
        if (isLong) {
            try {
                val toast = Toast.makeText(context, text, Toast.LENGTH_LONG)
                val toastView = toast.view // This'll return the default View of the Toast.
                //And now you can get the TextView of the default View of the Toast.
                val toastMessage = toastView!!.findViewById<View>(android.R.id.message) as TextView
                val marginLeftRight = context.resources.getDimension(R.dimen.margin_20).toInt()
                val marginTopBottom = context.resources.getDimension(R.dimen.margin_15).toInt()
                // toastMessage.setGravity(Gravity.CENTER);
                // toastMessage.setPadding(marginLeftRight, marginTopBottom, marginLeftRight, marginTopBottom);
                //toastMessage.setBackgroundColor(context.getResources().getColor(R.color.gray_toast_bg));
                // toastView.setBackgroundColor(context.getResources().getColor(R.color.gray_toast_bg));
                toastView.setBackgroundResource(R.drawable.rounded_toast_bg)
                toastMessage.setTextColor(ContextCompat.getColor(context, R.color.white))
                toast.show()
                //Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            } catch (e: Exception) {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show()
                CommonUtils.printStackTrace(e)
            }
        } else {
            try {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
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
    fun show(context: Context, @StringRes resId: Int, isLong: Boolean) {
        /*  if (isLong) {
            Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
        }*/

        show(context, context.getString(resId), isLong)
    }

    /**
     * show toast.
     *
     * @param context context
     * @param resId   resId
     */
    fun show(context: Context, @StringRes resId: Int) {
        show(context.applicationContext, resId, false)
    }

    /**
     * show toast.
     *
     * @param context context
     * @param text    text
     * @param isLong  isLong
     */
    fun s(context: Context?, text: CharSequence?, isLong: Boolean) {
        show(context, text, isLong)
    }

    /**
     * show toast.
     *
     * @param context context
     * @param resId   resId
     * @param isLong  isLong
     */
    fun s(context: Context, @StringRes resId: Int, isLong: Boolean) {
        show(context, resId, isLong)
    }

    /**
     * show toast.
     *
     * @param context context
     * @param text    text
     */
    fun s(context: Context?, text: CharSequence?) {
        show(context, text)
    }

    /**
     * show toast.
     *
     * @param context context
     * @param resId   resId
     */
    fun s(context: Context, @StringRes resId: Int) {
        show(context, resId)
    }

    fun onShowToast(
        mContext: Context?,
        drawable: Int,
        title: String?,
        desc: String?,
        isLong: Boolean
    ) {
        try {
            val inflater = LayoutInflater.from(mContext)
            val layout = inflater.inflate(R.layout.item_custom_toast, null)

            val ivIcon = layout.findViewById<ImageView>(R.id.ivIcon)
            //ivIcon.setImageResource(drawable);
            val tvTitle = layout.findViewById<TextView>(R.id.tvTitle)
            if (title != null) {
                tvTitle.text = title
                tvTitle.visibility = View.VISIBLE
            } else {
                tvTitle.visibility = View.GONE
            }
            val tvDescription = layout.findViewById<TextView>(R.id.tvDescription)
            tvDescription.text = desc

            val toast = Toast(mContext)
            toast.setGravity(Gravity.TOP or Gravity.RIGHT, 0, 0)
            if (isLong) {
                toast.duration = Toast.LENGTH_LONG
            } else {
                toast.duration = Toast.LENGTH_SHORT
            }

            toast.view = layout
            toast.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }
}
