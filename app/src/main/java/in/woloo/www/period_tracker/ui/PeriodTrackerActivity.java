package in.woloo.www.period_tracker.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.periodTracker.collapsiblecalendarview.data.Day;
import com.periodTracker.collapsiblecalendarview.view.OnSwipeTouchListener;
import com.periodTracker.collapsiblecalendarview.widget.CollapsibleCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dailylogscreen.DailyLogsScreenActivity;
import in.woloo.www.dailylogscreen.models.DailyLogSymptoms;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.my_history.model.MoodsModel;
import in.woloo.www.period_tracker.adapters.ShowDailyLogAdapter;
import in.woloo.www.period_tracker.circular_calender.PERIOD_TYPES;
import in.woloo.www.period_tracker.circular_calender.PeriodCalendarView;
import in.woloo.www.period_tracker.circular_calender.PeriodCalendarViewListener;

public class PeriodTrackerActivity extends AppCompatActivity {

    @BindView(R.id.collapsibleCalendarView)
    CollapsibleCalendar collapsibleCalendar;

    @BindView(R.id.periodCalendar)
    PeriodCalendarView periodCalendarView;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.main_scrollview)
    ScrollView main_scrollview;

    @BindView(R.id.dailyLog_Rv)
    RecyclerView rvDailyLogs;

    @BindView(R.id.add_daily_log_Imv)
    ImageView imvAddDailyLogs;

    @BindView(R.id.add_daily_log_fl)
    FrameLayout flAddDailyLogs;

    @BindView(R.id.no_data_available_Tv)
    TextView tvNoDataAvailable;

    private int currentMonth = 0;
    private int currentYear = 0;
    private int currentDay = 0;
    public SharedPreference mSharedPreference;
    Calendar periodStartDate;
    ShowDailyLogAdapter showDailyLogAdapter;
    MoodsModel moodsModel;
    ArrayList<List<DailyLogSymptoms>> moodsModelArrayList;
    int REQ_CODE = 11;
    String bleedingString = "", moodString = "", symptomsString = "", sex_and_sex_driveString = "", habitsString = "", premenstruationString = "",
            diseasesandmedicationString = "", menstruationString = "";
    List<DailyLogSymptoms> bleeding = new ArrayList<>();
    List<DailyLogSymptoms> mood = new ArrayList<>();
    List<DailyLogSymptoms> symptoms = new ArrayList<>();
    List<DailyLogSymptoms> sex_and_sex_drive = new ArrayList<>();
    List<DailyLogSymptoms> habits = new ArrayList<>();
    List<DailyLogSymptoms> premenstruation = new ArrayList<>();
    List<DailyLogSymptoms> diseasesandmedication = new ArrayList<>();
    List<DailyLogSymptoms> menstruation = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_period_tracker);
        ButterKnife.bind(this);
        initView();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        try {
            mSharedPreference = new SharedPreference(this);
            periodStartDate = Calendar.getInstance();
            String startDate = mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(), "");
            if (!startDate.equals("")) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    periodStartDate.setTime(Objects.requireNonNull(format.parse(startDate)));
                } catch (ParseException e) {
                      CommonUtils.printStackTrace(e);
                }
            }

            Calendar endCycleCalendar = (Calendar) periodStartDate.clone();
            endCycleCalendar.add(Calendar.DATE, Integer.parseInt(mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), "28")) - 1);

            scrollView.setOnTouchListener(new OnSwipeTouchListener(this) {
                @Override
                public boolean onTouch(@NonNull View v, @NonNull MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            main_scrollview.requestDisallowInterceptTouchEvent(true);
                            // Disable touch on transparent view
                            return false;
                        // Disable touch on transparent view

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            main_scrollview.requestDisallowInterceptTouchEvent(false);
                            return true;

                        default:
                            return true;
                    }
                }

                @Override
                public void onSwipeRight() {
                    collapsibleCalendar.nextDay();
                }

                @Override
                public void onSwipeLeft() {
                    collapsibleCalendar.prevDay();
                }

                @Override
                public void onSwipeTop() {
                    if (collapsibleCalendar.getExpanded()) {
                        collapsibleCalendar.collapse(400);
                    }
                }

                @Override
                public void onSwipeBottom() {
                    if (!collapsibleCalendar.getExpanded()) {
                        collapsibleCalendar.expand(400);
                    }
                }
            });

            Calendar getDate = Calendar.getInstance();
            getDate.set(periodStartDate.get(Calendar.YEAR),periodStartDate.get(Calendar.MONTH),periodStartDate.get(Calendar.DAY_OF_MONTH));

            Calendar startPeriodCalendar = (Calendar) getDate.clone();
            //startPeriodCalendar.add(Calendar.DATE, -1);

            Calendar endPeriodCalendar = (Calendar) getDate.clone();
            endPeriodCalendar.add(Calendar.DATE, Integer.parseInt(mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(), "4")));

            endCycleCalendar = (Calendar) getDate.clone();
            endCycleCalendar.add(Calendar.DATE, Integer.parseInt(mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), "28")) - 1);

            Calendar ovulationCalendarStartCalender = (Calendar) endCycleCalendar.clone();
            ovulationCalendarStartCalender.add(Calendar.DATE, -15);

            Calendar ovulationCalendarEndCalender = (Calendar) ovulationCalendarStartCalender.clone();
            ovulationCalendarEndCalender.add(Calendar.DATE, 3);

            Calendar pregnancyStartDate = (Calendar) ovulationCalendarStartCalender.clone();
            pregnancyStartDate.add(Calendar.DATE, -3);

            Calendar pregnancyEndDate = (Calendar) ovulationCalendarEndCalender.clone();
            pregnancyEndDate.add(Calendar.DATE, 5);

            collapsibleCalendar.setGetDate(getDate);
            collapsibleCalendar.setStartPeriodCalendar(startPeriodCalendar);
            collapsibleCalendar.setEndPeriodCalendar(endPeriodCalendar);
            collapsibleCalendar.setEndCycleCalendar(endCycleCalendar);
            collapsibleCalendar.setOvulationStartCalendar(ovulationCalendarStartCalender);
            collapsibleCalendar.setOvulationEndCalendar(ovulationCalendarEndCalender);
            collapsibleCalendar.setPregnancyStartDate(pregnancyStartDate);
            collapsibleCalendar.setPregnancyEndDate(pregnancyEndDate);
            collapsibleCalendar.setPeriodLength(Integer.parseInt(mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(), "1")));
            collapsibleCalendar.setCycleLength(Integer.parseInt(mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), "28")));
            collapsibleCalendar.setExpandIconVisible(true);
            //Calendar today = new GregorianCalendar();
            collapsibleCalendar.addEventTag(periodStartDate.get(Calendar.YEAR), periodStartDate.get(Calendar.MONTH), periodStartDate.get(Calendar.DAY_OF_MONTH));
            periodStartDate.add(Calendar.DATE, 1);
            collapsibleCalendar.setSelectedDay(new Day(periodStartDate.get(Calendar.YEAR), periodStartDate.get(Calendar.MONTH), periodStartDate.get(Calendar.DAY_OF_MONTH)));
            //collapsibleCalendar.addEventTag(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), Color.BLUE);
            collapsibleCalendar.setParams(new CollapsibleCalendar.Params(0, 90));

           setCircularCalender();

        }catch (Exception ex){
            System.out.println(ex.toString());
        }

        setDailyLog();
    }

    private void setCircularCalender() {
        Calendar c = Calendar.getInstance();
        currentYear = c.get(Calendar.YEAR);
        currentMonth = c.get(Calendar.MONTH);
        currentDay = c.get(Calendar.DATE);

        if(periodCalendarView != null){
            List<Integer> menstruationDays = new ArrayList<>();
            List<Integer> ovulationDays =new ArrayList<>();
            List<Integer> pregnancyDays = new ArrayList<>();

            periodStartDate.add(Calendar.DATE, -1);
            Calendar getDate = Calendar.getInstance();
            getDate.set(periodStartDate.get(Calendar.YEAR),periodStartDate.get(Calendar.MONTH),periodStartDate.get(Calendar.DAY_OF_MONTH));
            //getDate.add(Calendar.DATE, -Integer.parseInt(mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), "28")));

            Calendar startPeriodCalendar = (Calendar) getDate.clone();
            //startPeriodCalendar.add(Calendar.DATE, -1);

            Calendar endPeriodCalendar = (Calendar) getDate.clone();
            endPeriodCalendar.add(Calendar.DATE, Integer.parseInt(mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(), "1")));

            Calendar endCycleCalendar = (Calendar) getDate.clone();
            endCycleCalendar.add(Calendar.DATE, Integer.parseInt(mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), "28")) - 1);

            Calendar ovulationCalendarStartCalender = (Calendar) endCycleCalendar.clone();
            ovulationCalendarStartCalender.add(Calendar.DATE, -15);

            Calendar ovulationCalendarEndCalender = (Calendar) ovulationCalendarStartCalender.clone();
            ovulationCalendarEndCalender.add(Calendar.DATE, 3);

            Calendar pregnancyStartDate = (Calendar) ovulationCalendarStartCalender.clone();
            pregnancyStartDate.add(Calendar.DATE, -3);

            Calendar pregnancyEndDate = (Calendar) ovulationCalendarEndCalender.clone();
            pregnancyEndDate.add(Calendar.DATE, 3);

            startPeriodCalendar.add(Calendar.DATE, -1);
            ovulationCalendarStartCalender.add(Calendar.DATE, -1);
            pregnancyStartDate.add(Calendar.DATE, -1);
            getDate.add(Calendar.DATE, -1);

            int count = 0;
            while (!getDate.after(endCycleCalendar)) {
                if (getDate.after(startPeriodCalendar) && getDate.before(endPeriodCalendar)) {
                    menstruationDays.add(getDate.get(Calendar.DATE));
                    if (Calendar.getInstance().get(Calendar.DATE) == getDate.get(Calendar.DATE) && Calendar.getInstance().get(Calendar.MONTH) == getDate.get(Calendar.MONTH)
                            && Calendar.getInstance().get(Calendar.YEAR) == getDate.get(Calendar.YEAR)) {
                        periodCalendarView.setPeriodType(PERIOD_TYPES.MENSTRUATION);
                    }
                } else if (getDate.after(ovulationCalendarStartCalender) && getDate.before(ovulationCalendarEndCalender)) {
                    ovulationDays.add(getDate.get(Calendar.DATE));
                    if (Calendar.getInstance().get(Calendar.DATE) == getDate.get(Calendar.DATE) && Calendar.getInstance().get(Calendar.MONTH) == getDate.get(Calendar.MONTH)
                            && Calendar.getInstance().get(Calendar.YEAR) == getDate.get(Calendar.YEAR)) {
                        periodCalendarView.setPeriodType(PERIOD_TYPES.OVULATION);
                    }
                } else if (getDate.after(pregnancyStartDate) && getDate.before(pregnancyEndDate)) {
                    pregnancyDays.add(getDate.get(Calendar.DATE));
                    if (Calendar.getInstance().get(Calendar.DATE) == getDate.get(Calendar.DATE) && Calendar.getInstance().get(Calendar.MONTH) == getDate.get(Calendar.MONTH)
                            && Calendar.getInstance().get(Calendar.YEAR) == getDate.get(Calendar.YEAR)) {
                        periodCalendarView.setPeriodType(PERIOD_TYPES.PERIOD);
                    }
                }
                if (Calendar.getInstance().get(Calendar.DATE) == getDate.get(Calendar.DATE) && Calendar.getInstance().get(Calendar.MONTH) == getDate.get(Calendar.MONTH)
                        && Calendar.getInstance().get(Calendar.YEAR) == getDate.get(Calendar.YEAR)) {
                    periodCalendarView.setPeriodDays(count);
                } else count++;
                getDate.add(Calendar.DATE, 1);
            }

            periodCalendarView.setPeriodCycle(menstruationDays, ovulationDays, pregnancyDays);

            periodCalendarView.setCalendar(currentDay, currentMonth, currentYear);
            periodCalendarView.setPeriodCalendarViewListener(new PeriodCalendarViewListener() {
                @Override
                public void onEdit() {
                    finish();
                    startActivity(new Intent(PeriodTrackerActivity.this, EditCycleActivity.class));
                }
            });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE || requestCode == RESULT_OK){

            setAdapterData();
        }
    }

    private void setDailyLog() {
        moodsModel = new MoodsModel();
        moodsModelArrayList = new ArrayList<>();
        showDailyLogAdapter = new ShowDailyLogAdapter(this);

        setAdapterData();

        flAddDailyLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PeriodTrackerActivity.this, DailyLogsScreenActivity.class);
                startActivityForResult(intent, REQ_CODE);
            }
        });
    }

    private void setAdapterData() {

        bleedingString = mSharedPreference.getStoredPreference(getApplicationContext(), SharedPreferencesEnum.BLEEDING.getPreferenceKey(),"");
        moodString = mSharedPreference.getStoredPreference(getApplicationContext(), SharedPreferencesEnum.MOOD.getPreferenceKey(),"");
        symptomsString = mSharedPreference.getStoredPreference(getApplicationContext(), SharedPreferencesEnum.SYMPTOMS.getPreferenceKey(),"");
        sex_and_sex_driveString = mSharedPreference.getStoredPreference(getApplicationContext(), SharedPreferencesEnum.SEX_AND_SEX_DRIVE.getPreferenceKey(),"");
        habitsString = mSharedPreference.getStoredPreference(getApplicationContext(), SharedPreferencesEnum.HABITS.getPreferenceKey(),"");
        premenstruationString = mSharedPreference.getStoredPreference(getApplicationContext(), SharedPreferencesEnum.PREMENSTRUATION.getPreferenceKey(),"");
        diseasesandmedicationString = mSharedPreference.getStoredPreference(getApplicationContext(), SharedPreferencesEnum.DISEASES_AND_MEDICATION.getPreferenceKey(),"");
        menstruationString = mSharedPreference.getStoredPreference(getApplicationContext(), SharedPreferencesEnum.MENSTRUATION.getPreferenceKey(),"");



        if (bleedingString.isEmpty() && moodString.isEmpty() && symptomsString.isEmpty() && sex_and_sex_driveString.isEmpty()){
            tvNoDataAvailable.setVisibility(View.VISIBLE);
        }else {
            tvNoDataAvailable.setVisibility(View.GONE);
            Gson gson = new Gson();
            TypeToken<List<DailyLogSymptoms>> token = new TypeToken<List<DailyLogSymptoms>>() {};
            bleeding = gson.fromJson(bleedingString, token.getType());
            mood = gson.fromJson(moodString, token.getType());
            symptoms = gson.fromJson(symptomsString, token.getType());
            sex_and_sex_drive = gson.fromJson(sex_and_sex_driveString, token.getType());
            habits = gson.fromJson(habitsString, token.getType());
            premenstruation = gson.fromJson(premenstruationString, token.getType());
            diseasesandmedication = gson.fromJson(diseasesandmedicationString, token.getType());
            menstruation = gson.fromJson(menstruationString, token.getType());

            try {
                moodsModelArrayList.clear();
                moodsModel = new MoodsModel();
                moodsModelArrayList = new ArrayList<>();
                showDailyLogAdapter = new ShowDailyLogAdapter(this);

                moodsModelArrayList.add(premenstruation);
                moodsModelArrayList.add(menstruation);
                moodsModelArrayList.add(diseasesandmedication);
                moodsModelArrayList.add(bleeding);
                moodsModelArrayList.add(habits);
                moodsModelArrayList.add(mood);
                moodsModelArrayList.add(sex_and_sex_drive);
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
            /*showDailyLogAdapter.addMoods(moodsModelArrayList);
            rvDailyLogs.setAdapter(showDailyLogAdapter);*/
        }
    }
}