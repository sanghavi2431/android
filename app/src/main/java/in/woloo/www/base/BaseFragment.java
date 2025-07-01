package in.woloo.www.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.jetsynthesys.encryptor.JetEncryptor;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkUtils;
import in.woloo.www.networksUtils.VolleySingleton;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;


public class BaseFragment extends Fragment {
 public CommonUtils commonUtils=new CommonUtils();
    protected static SharedPreference mSharedPreference;
    protected AppConstants mApplicationConstant;
    protected JetEncryptor mJetEncryptor;
    protected NetworkUtils networkUtils;
    protected Gson gson;
    protected RequestQueue queue;

 public NetworkAPICall mNetworkAPICall=new NetworkAPICall();
    public  String TAG= BaseFragment.class.getSimpleName();
    /*calling on onCreate*/
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initializationBase();
        Logger.i(TAG, "onCreate");
    }

    /*calling on initializationBase*/
    private void initializationBase() {
        Logger.i(TAG, "initializationBase");
        if (commonUtils == null) {
            commonUtils = new CommonUtils();
        }

        if (mSharedPreference == null) {
            mSharedPreference = new SharedPreference(getActivity());
        }

        if (mApplicationConstant == null) {
            mApplicationConstant = new AppConstants();
        }

        if (mJetEncryptor == null) {
            mJetEncryptor = JetEncryptor.getInstance();
        }
        if (gson == null) {
            gson = commonUtils.getGson();
        }

        if (queue == null) {
            queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        }

        if (networkUtils == null) {
            networkUtils = new NetworkUtils();
        }
    }

}
