package in.woloo.www.period_tracker.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.common.DayLogUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.dashboard.ui.thirstreminder.AlarmReceiver;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.period_tracker.model.GetPeriodDataResponse;
import in.woloo.www.period_tracker.model.PeriodTrackerResponse;
import in.woloo.www.period_tracker.mvp.PeriodTrackerPresenter;
import in.woloo.www.period_tracker.mvp.PeriodTrackerView;
import in.woloo.www.period_tracker.receiver.Period7DaysAlarmReceiver;
import in.woloo.www.period_tracker.receiver.PeriodAlarmReceiver;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.periodtracker.model.PeriodTrackerRequest;
import in.woloo.www.v2.periodtracker.viewmodel.PeriodTrackerViewModel;

public class EditCycleActivity extends AppCompatActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.day_Tv)
    TextView tvDay;

    @BindView(R.id.month_Tv)
    TextView tvMonth;

    @BindView(R.id.year_TV)
    TextView tvYear;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.save_btn)
    TextView save_btn;

    @BindView(R.id.period_length_edText)
    EditText period_length_edText;

    @BindView(R.id.cycle_length_edText)
    EditText cycle_length_edText;

    public SharedPreference mSharedPreference;
    private PeriodTrackerViewModel periodTrackerViewModel;

    String startDate = "";
    int cycleLength = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cycle);
        mSharedPreference = new SharedPreference(this);
        ButterKnife.bind(this);
        periodTrackerViewModel = new ViewModelProvider(this).get(PeriodTrackerViewModel.class);
        initView();
        setLiveData();
    }

    private void initView() {
        tvTitle.setText("Edit Cycle");
        ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
        String periodLength = mSharedPreference.getStoredPreference(getApplicationContext(), SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(), "5");
        String periodCycleLength = mSharedPreference.getStoredPreference(getApplicationContext(), SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), "28");
        String periodDate = mSharedPreference.getStoredPreference(getApplicationContext(), SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(), "");
        if(periodCycleLength == null || periodCycleLength.equals("null") || periodCycleLength.equals("") || periodCycleLength.equals("0"))
            periodCycleLength = "28";

        if(periodLength == null || periodLength.equals("null") || periodLength.equals("") || periodLength.equals("0"))
            periodLength = "5";

        cycle_length_edText.setText(periodCycleLength);
        period_length_edText.setText(periodLength);
        if (!periodDate.isEmpty()) {
            String[] dateSplit = periodDate.split("-");
            if (dateSplit.length == 3) {// "yyyy-MM-dd"
                tvDay.setText(dateSplit[2]);
                tvMonth.setText(dateSplit[1]);
                tvYear.setText(dateSplit[0]);
            } else {
                getCurrentDate();
            }
        } else {
            getCurrentDate();
        }

        tvDay.setOnClickListener(view -> {
            getPeriodStartDate();
        });

        tvMonth.setOnClickListener(view -> {
            getPeriodStartDate();
        });

        tvYear.setOnClickListener(view -> {
            getPeriodStartDate();
        });

        save_btn.setOnClickListener(v -> {

            if (validate()) {
               startDate = tvYear.getText().toString() + "-" + tvMonth.getText().toString() + "-" + tvDay.getText().toString();
                Log.d("Aarati" ,  startDate + " start date to shared");
               cycleLength = Integer.parseInt(cycle_length_edText.getText().toString().trim());
                mSharedPreference.setStoredPreference(this, SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(), startDate);
                mSharedPreference.setStoredPreference(this, SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(), period_length_edText.getText().toString().trim());
                mSharedPreference.setStoredPreference(this, SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), cycle_length_edText.getText().toString().trim());

                PeriodTrackerRequest request = new PeriodTrackerRequest();
                try {
                    request.setCycleLenght( mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey()));
                    request.setLog(DayLogUtils.getInstance().getAsLog());
                    request.setLutealLength("14");
                    request.setPeriodLength( mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey()));
                    request.setPeriodDate( mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey()));
                    Log.d("Aarati" ,  mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey() ) + " start date from shared");
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

                setNotificationRemainder();
            }

        });
    }

    @SuppressLint("ScheduleExactAlarm")
    private void setNotificationRemainder() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = formatter.parse(startDate);
            date.setMinutes(new Date().getMinutes() + 2);
            date.setHours(new Date().getHours());


            Calendar calendarBefore2Days = Calendar.getInstance();
            calendarBefore2Days.setTime(date);
            calendarBefore2Days.add(Calendar.DAY_OF_MONTH, cycleLength - 2);
            Logger.i("EditCycleActivity", "Before 2 days : " + calendarBefore2Days.getTime().toString());

            Calendar calendarBefore7Days = Calendar.getInstance();
            calendarBefore7Days.setTime(date);
            calendarBefore7Days.add(Calendar.DAY_OF_MONTH, cycleLength - 7);
            Logger.i("EditCycleActivity", "Before 7 days : " + calendarBefore7Days.getTime().toString());

            AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            long currentDateMillis = System.currentTimeMillis();
            Logger.d("Edit cycle Activity aarati " , currentDateMillis + " " + calendarBefore7Days.getTimeInMillis() + " " + (calendarBefore7Days.getTimeInMillis() < currentDateMillis));


            if (calendarBefore2Days.getTimeInMillis() > currentDateMillis) {

                Intent notificationIntent = new Intent(this, PeriodAlarmReceiver.class);
                notificationIntent.setAction("in.woloo.www.ACTION_TRIGGER_NOTIFICATION");
                notificationIntent.putExtra("message", "2 days until next Period.");

                int requestCode = new Random().nextInt(Integer.MAX_VALUE);
                // int requestCode = (int) System.currentTimeMillis(); // Unique request code
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
                Logger.i("EditCycleActivity", "pendingIntent called");
                if (alarmManager != null) {
                    Logger.i("EditCycleActivity", "Before 2 days : " + calendarBefore2Days.getTimeInMillis());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarBefore2Days.getTimeInMillis(), pendingIntent);
                    } else {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarBefore2Days.getTimeInMillis(), pendingIntent);
                    }
                } else {
                    Logger.i("EditCycleActivity", "Before 2 days : ");
                }
            }
              else {
                    Log.d("aarati TAG", "The given date is in the future or is today: " + calendarBefore7Days.getTime());
                }


                //  AlarmManager alarmManager2 = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);

            long currentDateMillis1 = System.currentTimeMillis();
            Logger.d("Edit cycle Activity aarati " , currentDateMillis1 + " " + calendarBefore7Days.getTimeInMillis() + " " + (calendarBefore7Days.getTimeInMillis() < currentDateMillis1));

            if (calendarBefore7Days.getTimeInMillis() > currentDateMillis1) {

                // Create an Intent for the BroadcastReceiver
                Intent notificationIntent2 = new Intent(this, Period7DaysAlarmReceiver.class);
                notificationIntent2.setAction("in.woloo.www.ACTION_TRIGGER_NOTIFICATION");
                notificationIntent2.putExtra("message", "7 days until next Period.");

                // Create a PendingIntent for the BroadcastReceiver
                int requestCode2 = new Random().nextInt(Integer.MAX_VALUE);
                //   int requestCode2 = (int) System.currentTimeMillis(); // Unique request code
                PendingIntent pendingIntent2 = PendingIntent.getBroadcast(this, requestCode2, notificationIntent2, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
                Logger.i("EditCycleActivity", "pendingIntent2 called");
                // Set the alarm using AlarmManager
                if (alarmManager != null) {
                    // alarmManager2.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarBefore7Days.getTimeInMillis(), pendingIntent2);
                    Logger.i("EditCycleActivity", "Before 7 days : " + calendarBefore7Days.getTimeInMillis());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarBefore7Days.getTimeInMillis(), pendingIntent2);
                    } else {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarBefore7Days.getTimeInMillis(), pendingIntent2);
                    }

                } else {
                    Logger.i("EditCycleActivity", "Before 7 days : ");
                }
            }
            else {
                Log.d("aarati TAG", "The given date is in the future or is today: " + calendarBefore7Days.getTime());
            }


        }catch (Exception e){


            e.printStackTrace();
            Logger.e("EditCycleActivity", "Error setting notification", e);

        }

    }

    private void setLiveData() {
        periodTrackerViewModel.observePeriodTracker().observe(this, new Observer<BaseResponse<PeriodTrackerResponse.Data>>() {
            @Override
            public void onChanged(BaseResponse<PeriodTrackerResponse.Data> dataBaseResponse) {
                if(dataBaseResponse != null) {
                    setResult(Activity.RESULT_OK);
                }
                periodTrackerViewModel.getPeriodTracker();

            }
        });

        periodTrackerViewModel.observeGetPeriodTracker().observe(this, new Observer<BaseResponse<PeriodTrackerResponse.Data>>() {
            @Override
            public void onChanged(BaseResponse<PeriodTrackerResponse.Data> getPeriodDataResponse) {
                try {
                    if (getPeriodDataResponse != null && getPeriodDataResponse.getData() != null) {
                        if (mSharedPreference == null)
                            mSharedPreference = new SharedPreference(getBaseContext());
                        mSharedPreference.setStoredPreference(getBaseContext(), SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(), getPeriodDataResponse.getData().getPeriodDate());
                        mSharedPreference.setStoredPreference(getBaseContext(), SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(), String.valueOf(getPeriodDataResponse.getData().getPeriodLength()));
                        mSharedPreference.setStoredPreference(getBaseContext(), SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), String.valueOf(getPeriodDataResponse.getData().getCycleLenght()));


                        mSharedPreference.setStoredPreference(getApplicationContext(), SharedPreferencesEnum.DAILY_LOG.getPreferenceKey(), new Gson().toJson(getPeriodDataResponse.getData().getLog()));

                        in.woloo.www.period_tracker.model.Log log = getPeriodDataResponse.getData().getLog();

                        Calendar periodStartDate = Calendar.getInstance();
                        String startDate = getPeriodDataResponse.getData().getPeriodDate();
                        if (!startDate.equals("")) {
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                periodStartDate.setTime(Objects.requireNonNull(format.parse(startDate)));
                            } catch (ParseException e) {
                                CommonUtils.printStackTrace(e);
                            }
                        }
                    }

                }catch (Exception ex)
                {
                    CommonUtils.printStackTrace(ex);
                }

                finish();
            }

        });


    }

    private boolean validate() {
        try {
            if (cycle_length_edText.getText() == null || TextUtils.isEmpty(cycle_length_edText.getText().toString()) || (Integer.parseInt(cycle_length_edText.getText().toString().trim()) < 21 || Integer.parseInt(cycle_length_edText.getText().toString().trim()) > 40)) {
                Toast.makeText(this, "Cycle Length Should be between 21-40 days", Toast.LENGTH_SHORT).show();
                cycle_length_edText.requestFocus();
                return false;
            }
            if (period_length_edText.getText() == null || TextUtils.isEmpty(period_length_edText.getText().toString()) || Integer.parseInt(period_length_edText.getText().toString().trim()) < 4 || Integer.parseInt(period_length_edText.getText().toString().trim()) > 8) {
                Toast.makeText(this, "Period Length should be between 4-8 days", Toast.LENGTH_SHORT).show();
                //period_length_edText.setError(null);
                period_length_edText.requestFocus();
                return false;
            }
        }catch (Exception e){
            new CommonUtils().printStackTrace(e);
            return false;
        }
        return true;
    }

    private void getPeriodStartDate() {
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        //calendar.add(Calendar.DAY_OF_MONTH, -35);
        //Following line commented by Aarati
        //long minDate = calendar.getTime().getTime(); // Twice!

        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();
        minDate.add(Calendar.DAY_OF_MONTH, -35);
        maxDate.add(Calendar.DAY_OF_MONTH , 0);


        DatePickerDialog datePicker =
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(final DatePicker view, final int year, final int month,
                                          final int dayOfMonth) {
                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        calendar.set(year, month, dayOfMonth);
                        String dateString = sdf.format(calendar.getTime());
                        tvDay.setText(String.valueOf(dayOfMonth));
                        tvMonth.setText(String.valueOf(month + 1));
                        tvYear.setText(String.valueOf(year));
                    }
                }, year, month, day); // set date picker to current date

        DatePicker datePickerD = datePicker.getDatePicker();
        datePickerD.setMinDate(minDate.getTimeInMillis());
        datePickerD.setMaxDate(maxDate.getTimeInMillis());

        datePicker.show();
        datePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(final DialogInterface dialog) {
                dialog.dismiss();
            }
        });
    }

    private void getCurrentDate() {
        try {
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH) + 1;
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

            tvDay.setText(String.valueOf(currentDay));
            tvMonth.setText(String.valueOf(currentMonth));
            tvYear.setText(String.valueOf(currentYear));
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_OK);
        finish();
    }

}