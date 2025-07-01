package in.woloo.www.dailylogscreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.common.DayLogUtils;
import in.woloo.www.dailylogscreen.adapter.DailyLogGroupAdapter;
import in.woloo.www.dailylogscreen.models.DailyLogGroupTitle;
import in.woloo.www.dailylogscreen.models.DailyLogSubTitle;

import in.woloo.www.dailylogscreen.models.DailyLogSymptoms;
import in.woloo.www.dashboard.ui.period_reminder.PeriodReminderViewModel;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.period_tracker.model.GetPeriodDataResponse;
import in.woloo.www.period_tracker.model.Log;
import in.woloo.www.period_tracker.model.PeriodTrackerResponse;

import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.periodtracker.model.PeriodTrackerRequest;
import in.woloo.www.v2.periodtracker.viewmodel.PeriodTrackerViewModel;

public class DailyLogsScreenActivity extends AppCompatActivity{
    private static final String TAG = DailyLogsScreenActivity.class.getSimpleName();

    @BindView(R.id.dailyLogRecycler)
    RecyclerView dailyLogRecyclerview;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.dailyLogSave)
    TextView dailyLogSave;

    private ArrayList<DailyLogGroupTitle> dailyLogGroupTitlesArray;
    private DailyLogGroupAdapter dailyLogGroupAdapter;
    private SharedPreference mSharedPreference;
//    private PeriodTrackerPresenter periodTrackerPresenter;
    private PeriodTrackerViewModel periodTrackerViewModel;
    //private Log log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_logs_screen);
        Logger.i(TAG, "onCreate");
        ButterKnife.bind(this);

        mSharedPreference = new SharedPreference(this);
//        periodTrackerPresenter = new PeriodTrackerPresenter(this, this);
        periodTrackerViewModel = new ViewModelProvider(this).get(PeriodTrackerViewModel.class);
        dailyLogGroupTitlesArray = prepareData();

        dailyLogGroupAdapter = new DailyLogGroupAdapter(this, dailyLogGroupTitlesArray);
        dailyLogRecyclerview.setHasFixedSize(true);
        dailyLogRecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        dailyLogRecyclerview.setAdapter(dailyLogGroupAdapter);

        setLiveData();
        dailyLogSave.setOnClickListener(v -> {
            for (DailyLogGroupTitle dailyLogGroupTitle : dailyLogGroupTitlesArray) {
                List<String> logs;
                switch (dailyLogGroupTitle.id) {
                    case PREMENSTRUATION:
                        logs = new ArrayList<>(dailyLogGroupTitle.subTitle.size());
                        for (DailyLogSubTitle subTitle : dailyLogGroupTitle.subTitle) {
                            if (subTitle.isChecked) {
                                logs.add(subTitle.subTitleName);
                            }
                        }
                        DayLogUtils.getInstance().setDailyLog(SharedPreferencesEnum.PREMENSTRUATION, logs);
                        break;
                    case MENSTRUATION:
                        logs = new ArrayList<>(dailyLogGroupTitle.subTitle.size());
                        for (DailyLogSubTitle subTitle : dailyLogGroupTitle.subTitle) {
                            if (subTitle.isChecked) {
                                logs.add(subTitle.subTitleName);
                            }
                        }
                        DayLogUtils.getInstance().setDailyLog(SharedPreferencesEnum.MENSTRUATION, logs);
                        break;
                    case MOOD:
                        logs = new ArrayList<>(dailyLogGroupTitle.subTitle.size());
                        for (DailyLogSubTitle subTitle : dailyLogGroupTitle.subTitle) {
                            if (subTitle.isChecked) {
                                logs.add(subTitle.subTitleName);
                            }
                        }
                        DayLogUtils.getInstance().setDailyLog(SharedPreferencesEnum.MOOD, logs);
                        break;
                    case HABITS:
                        logs = new ArrayList<>(dailyLogGroupTitle.subTitle.size());
                        for (DailyLogSubTitle subTitle : dailyLogGroupTitle.subTitle) {
                            if (subTitle.isChecked) {
                                logs.add(subTitle.subTitleName);
                            }
                        }
                        DayLogUtils.getInstance().setDailyLog(SharedPreferencesEnum.HABITS, logs);
                        break;
                    case SEX_AND_SEX_DRIVE:
                        logs = new ArrayList<>(dailyLogGroupTitle.subTitle.size());
                        for (DailyLogSubTitle subTitle : dailyLogGroupTitle.subTitle) {
                            if (subTitle.isChecked) {
                                logs.add(subTitle.subTitleName);
                            }
                        }
                        DayLogUtils.getInstance().setDailyLog(SharedPreferencesEnum.SEX_AND_SEX_DRIVE, logs);
                        break;
                    case DISEASES_AND_MEDICATION:
                        logs = new ArrayList<>(dailyLogGroupTitle.subTitle.size());
                        for (DailyLogSubTitle subTitle : dailyLogGroupTitle.subTitle) {
                            if (subTitle.isChecked) {
                                logs.add(subTitle.subTitleName);
                            }
                        }
                        DayLogUtils.getInstance().setDailyLog(SharedPreferencesEnum.DISEASES_AND_MEDICATION, logs);
                        break;
                    case BLEEDING:
                        logs = new ArrayList<>(dailyLogGroupTitle.subTitle.size());
                        for (DailyLogSubTitle subTitle : dailyLogGroupTitle.subTitle) {
                            if (subTitle.isChecked) {
                                logs.add(subTitle.subTitleName);
                            }
                        }
                        DayLogUtils.getInstance().setDailyLog(SharedPreferencesEnum.BLEEDING, logs);
                        break;
                }
            }
//            periodTrackerPresenter.sendPeriodData();
            PeriodTrackerRequest request = new PeriodTrackerRequest();
            try {
                request.setCycleLenght( mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey()));
                request.setLog(DayLogUtils.getInstance().getAsLog());
                request.setLutealLength("14");
                request.setPeriodLength( mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey()));
                request.setPeriodDate( mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey()));
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }
            periodTrackerViewModel.sendPeriodTrackerData(request);

            HashMap<String,Object> payload = new HashMap<>();
            payload.put(AppConstants.PERIOD_DATE,request.getPeriodDate());
            payload.put(AppConstants.PERIOD_LENGTH, request.getPeriodLength());
            payload.put(AppConstants.LUTEAL_LENGTH, request.getLutealLength());
            payload.put(AppConstants.CYCLE_LENGTH, request.getCycleLenght());
            Utility.logNetcoreEvent(this,payload,AppConstants.PERIOD_TRACKER_UPDATE_CLICK);
        });
    }

    private void setLiveData() {
        periodTrackerViewModel.observePeriodTracker().observe(this, new Observer<BaseResponse<PeriodTrackerResponse.Data>>() {
            @Override
            public void onChanged(BaseResponse<PeriodTrackerResponse.Data> dataBaseResponse) {
                if(dataBaseResponse != null) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

    }

    private ArrayList<DailyLogGroupTitle> prepareData() {
        ArrayList<DailyLogGroupTitle> dailyLogGroupTitles = new ArrayList<DailyLogGroupTitle>();

        DailyLogGroupTitle symptomspreensturation = new DailyLogGroupTitle();
        symptomspreensturation.id = SharedPreferencesEnum.PREMENSTRUATION;
        symptomspreensturation.groupName = "Symptoms-Mostly During Pre Mensturation";
        symptomspreensturation.subTitle = DayLogUtils.getInstance().getCheckedDailyLog(SharedPreferencesEnum.PREMENSTRUATION);
        dailyLogGroupTitles.add(symptomspreensturation);

        DailyLogGroupTitle symptomsmensturation = new DailyLogGroupTitle();
        symptomsmensturation.id = SharedPreferencesEnum.MENSTRUATION;
        symptomsmensturation.groupName = "Symptoms-Mostly During Mensturation";
        symptomsmensturation.subTitle = DayLogUtils.getInstance().getCheckedDailyLog(SharedPreferencesEnum.MENSTRUATION);
        dailyLogGroupTitles.add(symptomsmensturation);

        DailyLogGroupTitle diseasesandmedication = new DailyLogGroupTitle();
        diseasesandmedication.id = SharedPreferencesEnum.DISEASES_AND_MEDICATION;
        diseasesandmedication.groupName = "Diseases and Medication";
        diseasesandmedication.subTitle = DayLogUtils.getInstance().getCheckedDailyLog(SharedPreferencesEnum.DISEASES_AND_MEDICATION);
        dailyLogGroupTitles.add(diseasesandmedication);

        DailyLogGroupTitle habitsgroup = new DailyLogGroupTitle();
        habitsgroup.id = SharedPreferencesEnum.HABITS;
        habitsgroup.groupName = "Habits";
        habitsgroup.subTitle = DayLogUtils.getInstance().getCheckedDailyLog(SharedPreferencesEnum.HABITS);
        dailyLogGroupTitles.add(habitsgroup);

        DailyLogGroupTitle bleeding = new DailyLogGroupTitle();
        bleeding.id = SharedPreferencesEnum.BLEEDING;
        bleeding.groupName = "Bleeding";
        bleeding.subTitle = DayLogUtils.getInstance().getCheckedDailyLog(SharedPreferencesEnum.BLEEDING);
        dailyLogGroupTitles.add(bleeding);

        DailyLogGroupTitle mood = new DailyLogGroupTitle();
        mood.id = SharedPreferencesEnum.MOOD;
        mood.groupName = "Mood";
        mood.subTitle = DayLogUtils.getInstance().getCheckedDailyLog(SharedPreferencesEnum.MOOD);
        dailyLogGroupTitles.add(mood);

        DailyLogGroupTitle sexandsexdrive = new DailyLogGroupTitle();
        sexandsexdrive.id = SharedPreferencesEnum.SEX_AND_SEX_DRIVE;
        sexandsexdrive.groupName = "Sex and Sex Drive";
        sexandsexdrive.subTitle = DayLogUtils.getInstance().getCheckedDailyLog(SharedPreferencesEnum.SEX_AND_SEX_DRIVE);
        dailyLogGroupTitles.add(sexandsexdrive);

        return dailyLogGroupTitles;
    }

    @OnClick(R.id.toolbarBackArrow)
    protected void onClickBackArrow() {
        finish();
    }

//    @Override
//    public void setPeriodTrackerResponse(PeriodTrackerResponse periodTrackerResponse) {
//        Intent returnIntent = new Intent();
//        this.setResult(Activity.RESULT_OK, returnIntent);
//        this.finish();
//    }
//
//    @Override
//    public void getPeriodTrackerDataResponse(GetPeriodDataResponse getPeriodDataResponse) {
//
//    }
}