package in.woloo.www.networksUtils;

import android.app.Activity;

    public interface InternetConnection {
        void onNoInternetConnection(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback);
    }
