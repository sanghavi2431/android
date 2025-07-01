package in.woloo.www.networksUtils;

import android.app.Activity;

public interface NetworkTimeOut {
    void onTimeOutConnection(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback);
}
