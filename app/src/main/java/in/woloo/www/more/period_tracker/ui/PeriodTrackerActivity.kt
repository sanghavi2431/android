package `in`.woloo.www.more.period_tracker.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.periodTracker.collapsiblecalendarview.data.Day
import com.periodTracker.collapsiblecalendarview.view.OnSwipeTouchListener
import com.periodTracker.collapsiblecalendarview.widget.CollapsibleCalendar
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.more.dailylogscreen.DailyLogsScreenActivity
import `in`.woloo.www.more.dailylogscreen.models.DailyLogSymptoms
import `in`.woloo.www.more.period_tracker.adapters.ShowDailyLogAdapter
import `in`.woloo.www.more.period_tracker.circular_calender.PERIOD_TYPES
import `in`.woloo.www.more.period_tracker.circular_calender.PeriodCalendarView
import `in`.woloo.www.more.period_tracker.circular_calender.PeriodCalendarViewListener
import `in`.woloo.www.my_history.model.MoodsModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Objects

class PeriodTrackerActivity : AppCompatActivity() {
    @JvmField
    @BindView(R.id.collapsibleCalendarView)
    var collapsibleCalendar: CollapsibleCalendar? = null

    @JvmField
    @BindView(R.id.periodCalendar)
    var periodCalendarView: PeriodCalendarView? = null

    @JvmField
    @BindView(R.id.scrollView)
    var scrollView: ScrollView? = null

    @JvmField
    @BindView(R.id.main_scrollview)
    var main_scrollview: ScrollView? = null

    @JvmField
    @BindView(R.id.dailyLog_Rv)
    var rvDailyLogs: RecyclerView? = null

    @JvmField
    @BindView(R.id.add_daily_log_Imv)
    var imvAddDailyLogs: ImageView? = null

    @JvmField
    @BindView(R.id.add_daily_log_fl)
    var flAddDailyLogs: FrameLayout? = null

    @JvmField
    @BindView(R.id.no_data_available_Tv)
    var tvNoDataAvailable: TextView? = null

    private var currentMonth = 0
    private var currentYear = 0
    private var currentDay = 0
    var mSharedPreference: SharedPreference? = null
    var periodStartDate: Calendar? = null
    var showDailyLogAdapter: ShowDailyLogAdapter? = null
    var moodsModel: MoodsModel? = null
    var moodsModelArrayList: ArrayList<List<DailyLogSymptoms>>? = null
    var REQ_CODE: Int = 11
    var bleedingString: String? = ""
    var moodString: String? = ""
    var symptomsString: String? = ""
    var sex_and_sex_driveString: String? = ""
    var habitsString: String? = ""
    var premenstruationString: String? = ""
    var diseasesandmedicationString: String? = ""
    var menstruationString: String? = ""
    var bleeding: List<DailyLogSymptoms> = ArrayList<DailyLogSymptoms>()
    var mood: List<DailyLogSymptoms> = ArrayList<DailyLogSymptoms>()
    var symptoms: List<DailyLogSymptoms> = ArrayList<DailyLogSymptoms>()
    var sex_and_sex_drive: List<DailyLogSymptoms> = ArrayList<DailyLogSymptoms>()
    var habits: List<DailyLogSymptoms> = ArrayList<DailyLogSymptoms>()
    var premenstruation: List<DailyLogSymptoms> = ArrayList<DailyLogSymptoms>()
    var diseasesandmedication: List<DailyLogSymptoms> = ArrayList<DailyLogSymptoms>()
    var menstruation: List<DailyLogSymptoms> = ArrayList<DailyLogSymptoms>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_period_tracker)
        ButterKnife.bind(this)
        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        try {
            mSharedPreference = SharedPreference(this)
            periodStartDate = Calendar.getInstance()
            val startDate: String = mSharedPreference!!.getStoredPreference(
                this,
                SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(),
                ""
            ).toString()
            if (startDate != "") {
                val format = SimpleDateFormat("yyyy-MM-dd")
                try {
                    periodStartDate!!.setTime(Objects.requireNonNull(format.parse(startDate)))
                } catch (e: ParseException) {
                    CommonUtils.printStackTrace(e)
                }
            }

            var endCycleCalendar = periodStartDate!!!!.clone() as Calendar
            endCycleCalendar.add(
                Calendar.DATE,
                mSharedPreference!!.getStoredPreference(
                    this,
                    SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(),
                    "28"
                )!!.toInt() - 1
            )

            scrollView!!.setOnTouchListener(object : OnSwipeTouchListener(applicationContext) {
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    val action = event.action
                    when (action) {
                        MotionEvent.ACTION_DOWN -> {
                            // Disallow ScrollView to intercept touch events.
                            main_scrollview!!.requestDisallowInterceptTouchEvent(true)
                            // Disable touch on transparent view
                            return false
                        }

                        MotionEvent.ACTION_UP -> {
                            // Allow ScrollView to intercept touch events.
                            main_scrollview!!.requestDisallowInterceptTouchEvent(false)
                            return true
                        }

                        else -> return true
                    }
                }

                override fun onSwipeRight() {
                    collapsibleCalendar!!.nextDay()
                }

                override fun onSwipeLeft() {
                    collapsibleCalendar!!.prevDay()
                }

                override fun onSwipeTop() {
                    if (collapsibleCalendar!!.expanded) {
                        collapsibleCalendar!!.collapse(400)
                    }
                }

                override fun onSwipeBottom() {
                    if (!collapsibleCalendar!!.expanded) {
                        collapsibleCalendar!!.expand(400)
                    }
                }
            })

            val getDate = Calendar.getInstance()
            getDate[periodStartDate!!.get(Calendar.YEAR), periodStartDate!!.get(Calendar.MONTH)] =
                periodStartDate!!.get(Calendar.DAY_OF_MONTH)

            val startPeriodCalendar = getDate.clone() as Calendar

            //startPeriodCalendar.add(Calendar.DATE, -1);
            val endPeriodCalendar = getDate.clone() as Calendar
            endPeriodCalendar.add(
                Calendar.DATE, mSharedPreference!!.getStoredPreference(
                    this, SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(), "4"
                )!!.toInt()
            )

            endCycleCalendar = getDate.clone() as Calendar
            endCycleCalendar.add(
                Calendar.DATE,
                mSharedPreference!!.getStoredPreference(
                    this,
                    SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(),
                    "28"
                )!!.toInt() - 1
            )

            val ovulationCalendarStartCalender = endCycleCalendar.clone() as Calendar
            ovulationCalendarStartCalender.add(Calendar.DATE, -15)

            val ovulationCalendarEndCalender = ovulationCalendarStartCalender.clone() as Calendar
            ovulationCalendarEndCalender.add(Calendar.DATE, 3)

            val pregnancyStartDate = ovulationCalendarStartCalender.clone() as Calendar
            pregnancyStartDate.add(Calendar.DATE, -3)

            val pregnancyEndDate = ovulationCalendarEndCalender.clone() as Calendar
            pregnancyEndDate.add(Calendar.DATE, 5)

            collapsibleCalendar!!.getDate = getDate
            collapsibleCalendar!!.startPeriodCalendar = startPeriodCalendar
            collapsibleCalendar!!.endPeriodCalendar = endPeriodCalendar
            collapsibleCalendar!!.endCycleCalendar = endCycleCalendar
            collapsibleCalendar!!.ovulationStartCalendar = ovulationCalendarStartCalender
            collapsibleCalendar!!.ovulationEndCalendar = ovulationCalendarEndCalender
            collapsibleCalendar!!.pregnancyStartDate = pregnancyStartDate
            collapsibleCalendar!!.pregnancyEndDate = pregnancyEndDate
            collapsibleCalendar!!.periodLength = mSharedPreference!!.getStoredPreference(
                this,
                SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(),
                "1"
            )!!.toInt()
            collapsibleCalendar!!.cycleLength = mSharedPreference!!.getStoredPreference(
                this,
                SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(),
                "28"
            )!!.toInt()
            collapsibleCalendar!!.setExpandIconVisible(true)
            //Calendar today = new GregorianCalendar();
            collapsibleCalendar!!.addEventTag(
                periodStartDate!!.get(Calendar.YEAR), periodStartDate!!.get(
                    Calendar.MONTH
                ), periodStartDate!!.get(Calendar.DAY_OF_MONTH)
            )
            periodStartDate!!.add(Calendar.DATE, 1)
            collapsibleCalendar!!.selectedDay = Day(
                periodStartDate!!.get(Calendar.YEAR), periodStartDate!!.get(
                    Calendar.MONTH
                ), periodStartDate!!.get(Calendar.DAY_OF_MONTH)
            )
            //collapsibleCalendar.addEventTag(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), Color.BLUE);
            collapsibleCalendar!!.params = CollapsibleCalendar.Params(0, 90)

            setCircularCalender()
        } catch (ex: Exception) {
            println(ex.toString())
        }

        setDailyLog()
    }

    private fun setCircularCalender() {
        val c = Calendar.getInstance()
        currentYear = c[Calendar.YEAR]
        currentMonth = c[Calendar.MONTH]
        currentDay = c[Calendar.DATE]

        if (periodCalendarView!! != null) {
            val menstruationDays: MutableList<Int> = ArrayList()
            val ovulationDays: MutableList<Int> = ArrayList()
            val pregnancyDays: MutableList<Int> = ArrayList()

            periodStartDate!!!!.add(Calendar.DATE, -1)
            val getDate = Calendar.getInstance()
            getDate[periodStartDate!!!![Calendar.YEAR], periodStartDate!!!![Calendar.MONTH]] =
                periodStartDate!!!![Calendar.DAY_OF_MONTH]

            //getDate.add(Calendar.DATE, -Integer.parseInt(mSharedPreference!!.getStoredPreference(this, SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), "28")));
            val startPeriodCalendar = getDate.clone() as Calendar

            //startPeriodCalendar.add(Calendar.DATE, -1);
            val endPeriodCalendar = getDate.clone() as Calendar
            endPeriodCalendar.add(
                Calendar.DATE, mSharedPreference!!.getStoredPreference(
                    this, SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(), "1"
                )!!.toInt()
            )

            val endCycleCalendar = getDate.clone() as Calendar
            endCycleCalendar.add(
                Calendar.DATE,
                mSharedPreference!!.getStoredPreference(
                    this,
                    SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(),
                    "28"
                )!!.toInt() - 1
            )

            val ovulationCalendarStartCalender = endCycleCalendar.clone() as Calendar
            ovulationCalendarStartCalender.add(Calendar.DATE, -15)

            val ovulationCalendarEndCalender = ovulationCalendarStartCalender.clone() as Calendar
            ovulationCalendarEndCalender.add(Calendar.DATE, 3)

            val pregnancyStartDate = ovulationCalendarStartCalender.clone() as Calendar
            pregnancyStartDate.add(Calendar.DATE, -3)

            val pregnancyEndDate = ovulationCalendarEndCalender.clone() as Calendar
            pregnancyEndDate.add(Calendar.DATE, 3)

            startPeriodCalendar.add(Calendar.DATE, -1)
            ovulationCalendarStartCalender.add(Calendar.DATE, -1)
            pregnancyStartDate.add(Calendar.DATE, -1)
            getDate.add(Calendar.DATE, -1)

            var count = 0
            while (!getDate.after(endCycleCalendar)) {
                if (getDate.after(startPeriodCalendar) && getDate.before(endPeriodCalendar)) {
                    menstruationDays.add(getDate[Calendar.DATE])
                    if (Calendar.getInstance()[Calendar.DATE] == getDate[Calendar.DATE] && Calendar.getInstance()[Calendar.MONTH] == getDate[Calendar.MONTH] && Calendar.getInstance()[Calendar.YEAR] == getDate[Calendar.YEAR]) {
                        periodCalendarView!!.setPeriodType(PERIOD_TYPES.MENSTRUATION)
                    }
                } else if (getDate.after(ovulationCalendarStartCalender) && getDate.before(
                        ovulationCalendarEndCalender
                    )
                ) {
                    ovulationDays.add(getDate[Calendar.DATE])
                    if (Calendar.getInstance()[Calendar.DATE] == getDate[Calendar.DATE] && Calendar.getInstance()[Calendar.MONTH] == getDate[Calendar.MONTH] && Calendar.getInstance()[Calendar.YEAR] == getDate[Calendar.YEAR]) {
                        periodCalendarView!!.setPeriodType(PERIOD_TYPES.OVULATION)
                    }
                } else if (getDate.after(pregnancyStartDate) && getDate.before(pregnancyEndDate)) {
                    pregnancyDays.add(getDate[Calendar.DATE])
                    if (Calendar.getInstance()[Calendar.DATE] == getDate[Calendar.DATE] && Calendar.getInstance()[Calendar.MONTH] == getDate[Calendar.MONTH] && Calendar.getInstance()[Calendar.YEAR] == getDate[Calendar.YEAR]) {
                        periodCalendarView!!.setPeriodType(PERIOD_TYPES.PERIOD)
                    }
                }
                if (Calendar.getInstance()[Calendar.DATE] == getDate[Calendar.DATE] && Calendar.getInstance()[Calendar.MONTH] == getDate[Calendar.MONTH] && Calendar.getInstance()[Calendar.YEAR] == getDate[Calendar.YEAR]) {
                    periodCalendarView!!.setPeriodDays(count)
                } else count++
                getDate.add(Calendar.DATE, 1)
            }

            periodCalendarView!!.setPeriodCycle(menstruationDays, ovulationDays, pregnancyDays)

            periodCalendarView!!.setCalendar(currentDay, currentMonth, currentYear)
            periodCalendarView!!.setPeriodCalendarViewListener(object : PeriodCalendarViewListener {
                override fun onEdit() {
                    finish()
                    startActivity(
                        Intent(
                            this@PeriodTrackerActivity,
                            EditCycleActivity::class.java
                        )
                    )
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CODE || requestCode == Activity.RESULT_OK) {
            setAdapterData()
        }
    }

    private fun setDailyLog() {
        moodsModel = MoodsModel()
        moodsModelArrayList = ArrayList<List<DailyLogSymptoms>>()
        showDailyLogAdapter = ShowDailyLogAdapter(this)

        setAdapterData()

        flAddDailyLogs!!.setOnClickListener {
            val intent = Intent(
                this@PeriodTrackerActivity,
                DailyLogsScreenActivity::class.java
            )
            startActivityForResult(intent, REQ_CODE)
        }
    }

    private fun setAdapterData() {
        bleedingString = mSharedPreference!!.getStoredPreference(
            applicationContext,
            SharedPreferencesEnum.BLEEDING.getPreferenceKey(),
            ""
        )
        moodString = mSharedPreference!!.getStoredPreference(
            applicationContext,
            SharedPreferencesEnum.MOOD.getPreferenceKey(),
            ""
        )
        symptomsString = mSharedPreference!!.getStoredPreference(
            applicationContext,
            SharedPreferencesEnum.SYMPTOMS.getPreferenceKey(),
            ""
        )
        sex_and_sex_driveString = mSharedPreference!!.getStoredPreference(
            applicationContext,
            SharedPreferencesEnum.SEX_AND_SEX_DRIVE.getPreferenceKey(),
            ""
        )
        habitsString = mSharedPreference!!.getStoredPreference(
            applicationContext,
            SharedPreferencesEnum.HABITS.getPreferenceKey(),
            ""
        )
        premenstruationString = mSharedPreference!!.getStoredPreference(
            applicationContext,
            SharedPreferencesEnum.PREMENSTRUATION.getPreferenceKey(),
            ""
        )
        diseasesandmedicationString = mSharedPreference!!.getStoredPreference(
            applicationContext,
            SharedPreferencesEnum.DISEASES_AND_MEDICATION.getPreferenceKey(),
            ""
        )
        menstruationString = mSharedPreference!!.getStoredPreference(
            applicationContext,
            SharedPreferencesEnum.MENSTRUATION.getPreferenceKey(),
            ""
        )



        if (bleedingString!!.isEmpty() && moodString!!.isEmpty() && symptomsString!!.isEmpty() && sex_and_sex_driveString!!.isEmpty()) {
            tvNoDataAvailable!!.visibility = View.VISIBLE
        } else {
            tvNoDataAvailable!!.visibility = View.GONE
            val gson = Gson()
            val token: TypeToken<List<DailyLogSymptoms>> =
                object : TypeToken<List<DailyLogSymptoms>>() {}
            bleeding = gson.fromJson<List<DailyLogSymptoms>>(bleedingString, token.type)
            mood = gson.fromJson<List<DailyLogSymptoms>>(moodString, token.type)
            symptoms = gson.fromJson<List<DailyLogSymptoms>>(symptomsString, token.type)
            sex_and_sex_drive =
                gson.fromJson<List<DailyLogSymptoms>>(sex_and_sex_driveString, token.type)
            habits = gson.fromJson<List<DailyLogSymptoms>>(habitsString, token.type)
            premenstruation =
                gson.fromJson<List<DailyLogSymptoms>>(premenstruationString, token.type)
            diseasesandmedication =
                gson.fromJson<List<DailyLogSymptoms>>(diseasesandmedicationString, token.type)
            menstruation = gson.fromJson<List<DailyLogSymptoms>>(menstruationString, token.type)

            try {
                moodsModelArrayList!!.clear()
                moodsModel = MoodsModel()
                moodsModelArrayList = ArrayList<List<DailyLogSymptoms>>()
                showDailyLogAdapter = ShowDailyLogAdapter(this)

                moodsModelArrayList!!.add(premenstruation)
                moodsModelArrayList!!.add(menstruation)
                moodsModelArrayList!!.add(diseasesandmedication)
                moodsModelArrayList!!.add(bleeding)
                moodsModelArrayList!!.add(habits)
                moodsModelArrayList!!.add(mood)
                moodsModelArrayList!!.add(sex_and_sex_drive)
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
            /*showDailyLogAdapter.addMoods(moodsModelArrayList);
            rvDailyLogs.setAdapter(showDailyLogAdapter);*/
        }
    }
}