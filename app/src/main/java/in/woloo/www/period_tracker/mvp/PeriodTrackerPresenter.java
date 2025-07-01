package in.woloo.www.period_tracker.mvp;

import android.app.Activity;
import android.content.Context;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jetsynthesys.encryptor.JetEncryptor;

import org.json.JSONObject;

import java.lang.reflect.Type;

import in.woloo.www.common.CommonUtils;
import in.woloo.www.common.DayLogUtils;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.networksUtils.APIConstants;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.networksUtils.NetworkAPICall;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.networksUtils.NetworkAPIResponseCallback;
import in.woloo.www.period_tracker.model.GetPeriodDataResponse;
import in.woloo.www.period_tracker.model.Log;
import in.woloo.www.period_tracker.model.PeriodTrackerResponse;
import in.woloo.www.utils.AppConstants;

public class PeriodTrackerPresenter implements NetworkAPIResponseCallback {

    private Context mContext;
    private PeriodTrackerView periodTrackerView;
    private CommonUtils mCommonUtils;
    private JetEncryptor mJetEncryptor;
    private NetworkAPICall mNetworkAPICall;
    private SharedPreference sharedPreference;

    public PeriodTrackerPresenter(Context context, PeriodTrackerView periodTrackerView) {
        this.mContext = context;
        this.periodTrackerView = periodTrackerView;
        mJetEncryptor = JetEncryptor.getInstance();
        sharedPreference = new SharedPreference(context);
    }


    public void sendPeriodData() {
        NetworkAPICall mNetworkAPICall = new NetworkAPICall();
        JSONObject mJsObjParam = new JSONObject();
        try {
            mJsObjParam.put(JSONTagConstant.CYCLE_LENGTH, sharedPreference.getStoredPreference(mContext, SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey()));
            mJsObjParam.put(JSONTagConstant.LOG, new JSONObject(new Gson().toJson(DayLogUtils.getInstance().getAsLog())));
            mJsObjParam.put(JSONTagConstant.PERIOD_DATE, sharedPreference.getStoredPreference(mContext, SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey()));
            mJsObjParam.put(JSONTagConstant.LUTEAL_LENGTH, "14");
            mJsObjParam.put(JSONTagConstant.PERIOD_LENGTH, sharedPreference.getStoredPreference(mContext, SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey()));
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
        Type parserType = new TypeToken<PeriodTrackerResponse>() {
        }.getType();
        NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.PERIOD_TRACKER, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
        networkAPICallModel.setShowProgress(true);
        networkAPICallModel.setParserType(parserType);
        mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
    }

    public void getPeriodData() {
        NetworkAPICall mNetworkAPICall = new NetworkAPICall();
        JSONObject mJsObjParam = new JSONObject();
        Type parserType = new TypeToken<GetPeriodDataResponse>() {
        }.getType();
        NetworkAPICallModel networkAPICallModel = new NetworkAPICallModel(APIConstants.GET_USER_PERIOD_TRACKER, AppConstants.POST_REQUEST, AppConstants.APP_TYPE_MOBILE, mJsObjParam, mJetEncryptor);
        networkAPICallModel.setShowProgress(true);
        networkAPICallModel.setParserType(parserType);
        mNetworkAPICall.callApplicationWS((Activity) mContext, networkAPICallModel, this);
    }


    @Override
    public void onNoInternetConnection(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback) {

    }

    @Override
    public void onSuccessResponse(JSONObject response, NetworkAPICallModel networkAPICallModel) {
        switch (networkAPICallModel.getApiURL()) {
            case APIConstants.PERIOD_TRACKER:
                try {
                    PeriodTrackerResponse periodTrackerResponse = (PeriodTrackerResponse) networkAPICallModel.getResponseObject();
                    if (periodTrackerResponse != null && periodTrackerResponse.getStatus().equals(AppConstants.API_SUCCESS)) {
                        PeriodTrackerResponseFlow(periodTrackerResponse);
                    }
                } catch (Exception e) {
                    mCommonUtils.printStackTrace(e);
                }
                break;
            case APIConstants.GET_USER_PERIOD_TRACKER:
                try {
                    GetPeriodDataResponse getPeriodDataResponse = (GetPeriodDataResponse) networkAPICallModel.getResponseObject();
                    getPeriodDataResponseFlow(getPeriodDataResponse);
                } catch (Exception e) {
                    // If no period details found - send it to EditCycle screen
                    getPeriodDataResponseFlow(null);
                    mCommonUtils.printStackTrace(e);
                }
                break;
            default:
                break;
        }
    }

    private void getPeriodDataResponseFlow(GetPeriodDataResponse getPeriodDataResponse) {
        periodTrackerView.getPeriodTrackerDataResponse(getPeriodDataResponse);
    }

    private void PeriodTrackerResponseFlow(PeriodTrackerResponse periodTrackerResponse) {
        try {
            if (periodTrackerResponse != null && periodTrackerResponse.getStatus().equals(AppConstants.API_SUCCESS)) {
                periodTrackerView.setPeriodTrackerResponse(periodTrackerResponse);
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    @Override
    public void onFailure(VolleyError volleyError, NetworkAPICallModel networkAPICallModel) {
        if(networkAPICallModel.getApiURL().equals(APIConstants.GET_USER_PERIOD_TRACKER)){
            getPeriodDataResponseFlow(null);
        }
    }

    @Override
    public void onTimeOutConnection(Activity context, NetworkAPICallModel networkAPICallModel, NetworkAPIResponseCallback networkAPIResponseCallback) {

    }
}
