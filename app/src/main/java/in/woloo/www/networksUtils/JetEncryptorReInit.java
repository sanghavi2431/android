package in.woloo.www.networksUtils;

import android.app.Activity;

import com.jetsynthesys.encryptor.JetEncryptor;
import com.jetsynthesys.encryptor.JobListener;

import in.woloo.www.BuildConfig;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.utils.Logger;


public class JetEncryptorReInit {

    private Activity activity;
    private CommonUtils mUtility = new CommonUtils();
    private JetEncryptor mJetEncryptor;
    private NetworkAPICallModel networkAPICallModel;
    private NetworkAPIResponseCallback networkAPIResponseCallback;
    private NetworkAPICall networkAPICall;

    public JetEncryptorReInit(Activity activity) {
        this.activity = activity;
    }

    public void callApplicationWS(Activity activity, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback, NetworkAPICall networkAPICall) {
        this.activity = activity;
        this.networkAPICallModel = networkAPICallModel;
        this.networkAPIResponseCallback = networkAPIResponseCallback;
        this.networkAPICall = networkAPICall;
        if (mJetEncryptor == null) {
            mJetEncryptor = JetEncryptor.getInstance();
        }
        initJetEncryptor();
    }

    private void initJetEncryptor() {
        try {
            String device_id = mUtility.getDeviceId(activity);
            if (BuildConfig.LIVE_URL.equalsIgnoreCase("3")) {
                mJetEncryptor.initInBackground(activity.getApplicationContext(), jobListener, true, BuildConfig.BASE_URL_ENC, BuildConfig.PORT_ENC, BuildConfig.END_POINT_ENC, BuildConfig.APPLICATION_ID, device_id);
            } else if (BuildConfig.LIVE_URL.equalsIgnoreCase("4")) {
                mJetEncryptor.initInBackground(activity.getApplicationContext(), jobListener, true, BuildConfig.BASE_URL_ENC, BuildConfig.PORT_ENC, BuildConfig.END_POINT_ENC, BuildConfig.APPLICATION_ID + "_pp", device_id);
            } else {
                mJetEncryptor.initInBackground(activity.getApplicationContext(), jobListener, false, BuildConfig.BASE_URL_ENC, BuildConfig.PORT_ENC, BuildConfig.END_POINT_ENC, BuildConfig.APPLICATION_ID, device_id);
            }
        } catch (Exception e) {
            mUtility.printStackTrace(e);
            if (networkAPICall != null)
                networkAPICall.callApplicationWS(activity, networkAPICallModel, networkAPIResponseCallback);
        }
    }

    private static final String TAG = "JetEncryptor";
    private JobListener jobListener = new JobListener() {
        @Override
        public void workStarted(int i) {
            Logger.i(TAG, "workStarted");
        }

        @Override
        public void workFinished(int i) {
            Logger.i(TAG, "workFinished");
            try {
                VolleySingleton.getInstance(activity.getApplicationContext()).setAfterJetEncryptorInitilization(activity.getApplicationContext());
                if (networkAPICall != null)
                    networkAPICall.callApplicationWS(activity, networkAPICallModel, networkAPIResponseCallback);
                mJetEncryptor.cancelInitJob();
            } catch (Exception e) {
                mUtility.printStackTrace(e);
            }
        }

        @Override
        public void workResult(String s) {
            Logger.i(TAG, "workResult");
        }

        @Override
        public void onworkError(String s) {
            Logger.i(TAG, "onworkError");
            if (networkAPICall != null)
                networkAPICall.callApplicationWS(activity, networkAPICallModel, networkAPIResponseCallback);
            mJetEncryptor.cancelInitJob();
        }
    };

}
