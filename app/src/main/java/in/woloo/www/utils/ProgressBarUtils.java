package in.woloo.www.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Window;

import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;


public class ProgressBarUtils {
    private static final int TIME_OUT = 800;

    public static Dialog initProgressDialog(Context context) {
        Dialog dialoProgress = null;
        try {
            dialoProgress = new Dialog(context, R.style.NewDialog);
            //dialoProgress = new Dialog(context);
            dialoProgress.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //dialoProgress.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));
            dialoProgress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialoProgress.setContentView(R.layout.dialog_progress_overlay);
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
        return dialoProgress;
    }

    public static void showProgressDialog(Dialog dialoProgress, boolean isCancelable) {
        try {
            if (dialoProgress != null) {
                dialoProgress.setCancelable(isCancelable);
                dialoProgress.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismissProgressDialog(dialoProgress);
                    }
                }, TIME_OUT);
            }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }

    public static void showProgressDialogWithOutTime(Dialog dialoProgress, boolean isCancelable) {
        try {
            if (dialoProgress != null) {
                dialoProgress.setCancelable(isCancelable);
                dialoProgress.show();
            }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }

    public static void dismissProgressDialog(Dialog dialoProgress) {
        try {
            if (dialoProgress != null) {
                if (dialoProgress.isShowing()) {
                    dialoProgress.dismiss();
                }
            }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }


}
