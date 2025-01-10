package `in`.woloo.www.more.period_tracker.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.utilities.DayLogUtils
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.more.period_tracker.model.Log
import `in`.woloo.www.more.period_tracker.model.PeriodTrackerResponse
import `in`.woloo.www.more.period_tracker.periodtracker.model.PeriodTrackerRequest
import `in`.woloo.www.more.period_tracker.periodtracker.viewmodel.PeriodTrackerViewModel
import `in`.woloo.www.more.period_tracker.receiver.Period7DaysAlarmReceiver
import `in`.woloo.www.more.period_tracker.receiver.PeriodAlarmReceiver
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger.d
import `in`.woloo.www.utils.Logger.e
import `in`.woloo.www.utils.Logger.i
import `in`.woloo.www.utils.Utility.logNetcoreEvent
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Objects
import java.util.Random
import java.util.TimeZone

class EditCycleActivity : AppCompatActivity() {
    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: LinearLayout? = null

    @JvmField
    @BindView(R.id.day_Tv)
    var tvDay: TextView? = null

    @JvmField
    @BindView(R.id.month_Tv)
    var tvMonth: TextView? = null

    @JvmField
    @BindView(R.id.year_TV)
    var tvYear: TextView? = null

    @JvmField
    @BindView(R.id.screen_header)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.save_btn)
    var save_btn: TextView? = null

    @JvmField
    @BindView(R.id.period_length_edText)
    var period_length_edText: EditText? = null

    @JvmField
    @BindView(R.id.cycle_length_edText)
    var cycle_length_edText: EditText? = null

    var mSharedPreference: SharedPreference? = null
    private var periodTrackerViewModel: PeriodTrackerViewModel? = null

    var startDate: String = ""
    var cycleLength: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_cycle)
        mSharedPreference = SharedPreference(this)
        ButterKnife.bind(this)
        periodTrackerViewModel = ViewModelProvider(this).get<PeriodTrackerViewModel>(
            PeriodTrackerViewModel::class.java
        )
        initView()
        setLiveData()
    }

    private fun initView() {
        tvTitle!!.text = "Edit Cycle"
        ivBack!!.setOnClickListener { v: View? ->
            onBackPressed()
        }
        var periodLength: String = mSharedPreference!!.getStoredPreference(
            applicationContext,
            SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(),
            "5"
        ).toString()
        var periodCycleLength: String = mSharedPreference!!.getStoredPreference(
            applicationContext, SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), "28"
        ).toString()
        val periodDate: String = mSharedPreference!!.getStoredPreference(
            applicationContext,
            SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(),
            ""
        ).toString()
        if (periodCycleLength == null || periodCycleLength == "null" || periodCycleLength == "" || periodCycleLength == "0") periodCycleLength =
            "28"

        if (periodLength == null || periodLength == "null" || periodLength == "" || periodLength == "0") periodLength =
            "5"

        cycle_length_edText!!.setText(periodCycleLength)
        period_length_edText!!.setText(periodLength)
        if (!periodDate.isEmpty()) {
            val dateSplit =
                periodDate.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (dateSplit.size == 3) { // "yyyy-MM-dd"
                tvDay!!.text = dateSplit[2]
                tvMonth!!.text = dateSplit[1]
                tvYear!!.text = dateSplit[0]
            } else {
                currentDate
            }
        } else {
            currentDate
        }

        tvDay!!.setOnClickListener { view: View? ->
            periodStartDate
        }

        tvMonth!!.setOnClickListener { view: View? ->
            periodStartDate
        }

        tvYear!!.setOnClickListener { view: View? ->
            periodStartDate
        }

        save_btn!!.setOnClickListener { v: View? ->
            if (validate()) {
                startDate =
                    tvYear!!.text.toString() + "-" + tvMonth!!.text.toString() + "-" + tvDay!!.text.toString()
                android.util.Log.d("Aarati", "$startDate start date to shared")
                cycleLength = cycle_length_edText!!.text.toString().trim { it <= ' ' }.toInt()
                mSharedPreference!!.setStoredPreference(
                    this,
                    SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(),
                    startDate
                )
                mSharedPreference!!.setStoredPreference(
                    this,
                    SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(),
                    period_length_edText!!.text.toString().trim { it <= ' ' })
                mSharedPreference!!.setStoredPreference(
                    this,
                    SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(),
                    cycle_length_edText!!.text.toString().trim { it <= ' ' })

                val request: PeriodTrackerRequest = PeriodTrackerRequest()
                try {
                    request.cycleLenght = mSharedPreference!!.getStoredPreference(
                        this,
                        SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey()
                    ).toString()
                    request.log = DayLogUtils.instance!!.asLog
                    request.lutealLength = "14"
                    request.periodLength = mSharedPreference!!.getStoredPreference(
                        this,
                        SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey()
                    ).toString()
                    request.periodDate = mSharedPreference!!.getStoredPreference(
                        this,
                        SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey()
                    ).toString()
                    android.util.Log.d(
                        "Aarati",
                        mSharedPreference!!.getStoredPreference(
                            this,
                            SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey()
                        ) + " start date from shared"
                    )
                } catch (e: Exception) {
                    CommonUtils.printStackTrace(e)
                }
                periodTrackerViewModel!!.sendPeriodTrackerData(request)

                val payload = HashMap<String, Any>()
                payload[AppConstants.PERIOD_DATE] = request.periodDate
                payload[AppConstants.PERIOD_LENGTH] = request.periodLength
                payload[AppConstants.LUTEAL_LENGTH] = request.lutealLength
                payload[AppConstants.CYCLE_LENGTH] = request.cycleLenght
                logNetcoreEvent(
                    this,
                    payload,
                    AppConstants.PERIOD_TRACKER_UPDATE_CLICK
                )

                setNotificationRemainder()
            }
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setNotificationRemainder() {
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val date = formatter.parse(startDate)
            date!!.minutes = Date().minutes + 2
            date.hours = Date().hours


            val calendarBefore2Days = Calendar.getInstance()
            calendarBefore2Days.time = date
            calendarBefore2Days.add(Calendar.DAY_OF_MONTH, cycleLength - 2)
            i("EditCycleActivity", "Before 2 days : " + calendarBefore2Days.time.toString())

            val calendarBefore7Days = Calendar.getInstance()
            calendarBefore7Days.time = date
            calendarBefore7Days.add(Calendar.DAY_OF_MONTH, cycleLength - 7)
            i("EditCycleActivity", "Before 7 days : " + calendarBefore7Days.time.toString())

            val alarmManager =
                applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val currentDateMillis = System.currentTimeMillis()
            d(
                "Edit cycle Activity aarati ",
                currentDateMillis.toString() + " " + calendarBefore7Days.timeInMillis + " " + (calendarBefore7Days.timeInMillis < currentDateMillis)
            )


            if (calendarBefore2Days.timeInMillis > currentDateMillis) {
                val notificationIntent = Intent(
                    this,
                    PeriodAlarmReceiver::class.java
                )
                notificationIntent.setAction("in.woloo.www.ACTION_TRIGGER_NOTIFICATION")
                notificationIntent.putExtra("message", "2 days until next Period.")

                val requestCode = Random().nextInt(Int.MAX_VALUE)
                // int requestCode = (int) System.currentTimeMillis(); // Unique request code
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    requestCode,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                )
                i("EditCycleActivity", "pendingIntent called")
                if (alarmManager != null) {
                    i("EditCycleActivity", "Before 2 days : " + calendarBefore2Days.timeInMillis)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendarBefore2Days.timeInMillis,
                            pendingIntent
                        )
                    } else {
                        alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            calendarBefore2Days.timeInMillis,
                            pendingIntent
                        )
                    }
                } else {
                    i("EditCycleActivity", "Before 2 days : ")
                }
            } else {
                android.util.Log.d(
                    "aarati TAG",
                    "The given date is in the future or is today: " + calendarBefore7Days.time
                )
            }


            //  AlarmManager alarmManager2 = (AlarmManager) getApplication().getSystemService(Context.ALARM_SERVICE);
            val currentDateMillis1 = System.currentTimeMillis()
            d(
                "Edit cycle Activity aarati ",
                currentDateMillis1.toString() + " " + calendarBefore7Days.timeInMillis + " " + (calendarBefore7Days.timeInMillis < currentDateMillis1)
            )

            if (calendarBefore7Days.timeInMillis > currentDateMillis1) {
                // Create an Intent for the BroadcastReceiver

                val notificationIntent2 = Intent(
                    this,
                    Period7DaysAlarmReceiver::class.java
                )
                notificationIntent2.setAction("in.woloo.www.ACTION_TRIGGER_NOTIFICATION")
                notificationIntent2.putExtra("message", "7 days until next Period.")

                // Create a PendingIntent for the BroadcastReceiver
                val requestCode2 = Random().nextInt(Int.MAX_VALUE)
                //   int requestCode2 = (int) System.currentTimeMillis(); // Unique request code
                val pendingIntent2 = PendingIntent.getBroadcast(
                    this,
                    requestCode2,
                    notificationIntent2,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                )
                i("EditCycleActivity", "pendingIntent2 called")
                // Set the alarm using AlarmManager
                if (alarmManager != null) {
                    // alarmManager2.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarBefore7Days.getTimeInMillis(), pendingIntent2);
                    i("EditCycleActivity", "Before 7 days : " + calendarBefore7Days.timeInMillis)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            calendarBefore7Days.timeInMillis,
                            pendingIntent2
                        )
                    } else {
                        alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            calendarBefore7Days.timeInMillis,
                            pendingIntent2
                        )
                    }
                } else {
                    i("EditCycleActivity", "Before 7 days : ")
                }
            } else {
                android.util.Log.d(
                    "aarati TAG",
                    "The given date is in the future or is today: " + calendarBefore7Days.time
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            e("EditCycleActivity", "Error setting notification", e)
        }
    }

    private fun setLiveData() {
        periodTrackerViewModel!!.observePeriodTracker().observe(
            this,
            Observer<Any?> { dataBaseResponse ->
                if (dataBaseResponse != null) {
                    setResult(Activity.RESULT_OK)
                }
                periodTrackerViewModel!!.getPeriodTracker()
            })

        periodTrackerViewModel!!.observeGetPeriodTracker().observe(
            this,
            Observer<BaseResponse<PeriodTrackerResponse.Data>> { getPeriodDataResponse ->
                try {
                    if (getPeriodDataResponse != null && getPeriodDataResponse.data != null) {
                        if (mSharedPreference == null) mSharedPreference = SharedPreference(
                            baseContext
                        )
                        mSharedPreference!!.setStoredPreference(
                            baseContext,
                            SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(),
                            getPeriodDataResponse.data!!.periodDate
                        )
                        mSharedPreference!!.setStoredPreference(
                            baseContext,
                            SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(),
                            getPeriodDataResponse.data!!.periodLength.toString()
                        )
                        mSharedPreference!!.setStoredPreference(
                            baseContext,
                            SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(),
                            getPeriodDataResponse.data!!.cycleLenght.toString()
                        )


                        mSharedPreference!!.setStoredPreference(
                            applicationContext,
                            SharedPreferencesEnum.DAILY_LOG.getPreferenceKey(),
                            Gson().toJson(getPeriodDataResponse.data!!.log)
                        )

                        val log: Log? =
                            getPeriodDataResponse.data!!.log

                        val periodStartDate = Calendar.getInstance()
                        val startDate: String = getPeriodDataResponse.data!!.periodDate.toString()
                        if (startDate != "") {
                            val format = SimpleDateFormat("yyyy-MM-dd")
                            try {
                                periodStartDate.time =
                                    Objects.requireNonNull(
                                        format.parse(startDate)
                                    )
                            } catch (e: ParseException) {
                                CommonUtils.printStackTrace(e)
                            }
                        }
                    }
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
                finish()
            })
    }

    private fun validate(): Boolean {
        try {
            if (cycle_length_edText!!.text == null || TextUtils.isEmpty(
                    cycle_length_edText!!.text.toString()
                ) || (cycle_length_edText!!.text.toString().trim { it <= ' ' }
                    .toInt() < 21 || cycle_length_edText!!.text.toString().trim { it <= ' ' }
                    .toInt() > 40)
            ) {
                Toast.makeText(
                    this,
                    "Cycle Length Should be between 21-40 days",
                    Toast.LENGTH_SHORT
                ).show()
                cycle_length_edText!!.requestFocus()
                return false
            }
            if (period_length_edText!!.text == null || TextUtils.isEmpty(
                    period_length_edText!!.text.toString()
                ) || period_length_edText!!.text.toString().trim { it <= ' ' }
                    .toInt() < 4 || period_length_edText!!.text.toString().trim { it <= ' ' }
                    .toInt() > 8
            ) {
                Toast.makeText(this, "Period Length should be between 4-8 days", Toast.LENGTH_SHORT)
                    .show()
                //period_length_edText.setError(null);
                period_length_edText!!.requestFocus()
                return false
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
            return false
        }
        return true
    }

    private val periodStartDate: Unit
        get() {
            val calendar = Calendar.getInstance()
            val year = calendar[Calendar.YEAR]
            val month = calendar[Calendar.MONTH]
            val day = calendar[Calendar.DAY_OF_MONTH]

            //calendar.add(Calendar.DAY_OF_MONTH, -35);
            //Following line commented by Aarati
            //long minDate = calendar.getTime().getTime(); // Twice!
            val minDate = Calendar.getInstance()
            val maxDate = Calendar.getInstance()
            minDate.add(Calendar.DAY_OF_MONTH, -35)
            maxDate.add(Calendar.DAY_OF_MONTH, 0)


            val datePicker =
                DatePickerDialog(
                    this,
                    { view, year, month, dayOfMonth ->
                        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd")
                        calendar[year, month] = dayOfMonth
                        val dateString = sdf.format(calendar.time)
                        tvDay!!.text = dayOfMonth.toString()
                        tvMonth!!.text = (month + 1).toString()
                        tvYear!!.text = year.toString()
                    }, year, month, day
                ) // set date picker to current date

            val datePickerD = datePicker.datePicker
            datePickerD.minDate = minDate.timeInMillis
            datePickerD.maxDate = maxDate.timeInMillis

            datePicker.show()
            datePicker.setOnCancelListener { dialog -> dialog.dismiss() }
        }

    private val currentDate: Unit
        get() {
            try {
                val calendar =
                    Calendar.getInstance(TimeZone.getDefault())
                val currentYear = calendar[Calendar.YEAR]
                val currentMonth = calendar[Calendar.MONTH] + 1
                val currentDay = calendar[Calendar.DAY_OF_MONTH]

                tvDay!!.text = currentDay.toString()
                tvMonth!!.text = currentMonth.toString()
                tvYear!!.text = currentYear.toString()
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_OK)
        finish()
    }
}