package `in`.woloo.www.more.dailylogscreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.utilities.DayLogUtils
import `in`.woloo.www.application_kotlin.utilities.DayLogUtils.Companion.instance
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.more.dailylogscreen.adapter.DailyLogGroupAdapter
import `in`.woloo.www.more.dailylogscreen.models.DailyLogGroupTitle
import `in`.woloo.www.more.period_tracker.periodtracker.viewmodel.PeriodTrackerViewModel
import `in`.woloo.www.more.period_tracker.periodtracker.model.PeriodTrackerRequest
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility.logNetcoreEvent

class DailyLogsScreenActivity : AppCompatActivity() {
    @JvmField
    @BindView(R.id.dailyLogRecycler)
    var dailyLogRecyclerview: RecyclerView? = null

    @JvmField
    @BindView(R.id.toolbar)
    var toolbar: Toolbar? = null

    @JvmField
    @BindView(R.id.dailyLogSave)
    var dailyLogSave: TextView? = null

    private var dailyLogGroupTitlesArray: ArrayList<DailyLogGroupTitle>? = null
    private var dailyLogGroupAdapter: DailyLogGroupAdapter? = null
    private var mSharedPreference: SharedPreference? = null

    //    private PeriodTrackerPresenter periodTrackerPresenter;
    private var periodTrackerViewModel: PeriodTrackerViewModel? = null

    //private Log log;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_logs_screen)
        Logger.i(TAG, "onCreate")
        ButterKnife.bind(this)

        mSharedPreference = SharedPreference(this)
        //        periodTrackerPresenter = new PeriodTrackerPresenter(this, this);
        periodTrackerViewModel = ViewModelProvider(this).get<PeriodTrackerViewModel>(
            PeriodTrackerViewModel::class.java
        )
        dailyLogGroupTitlesArray = prepareData()

        dailyLogGroupAdapter = DailyLogGroupAdapter(this, dailyLogGroupTitlesArray!!)
        dailyLogRecyclerview!!.setHasFixedSize(true)
        dailyLogRecyclerview!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        dailyLogRecyclerview!!.adapter = dailyLogGroupAdapter

        setLiveData()
        dailyLogSave!!.setOnClickListener { v: View? ->
            for (dailyLogGroupTitle in dailyLogGroupTitlesArray!!) {
                var logs: MutableList<String>
                when (dailyLogGroupTitle.id) {
                    SharedPreferencesEnum.PREMENSTRUATION -> {
                        logs = ArrayList<String>(dailyLogGroupTitle.subTitle!!.size)
                        for (subTitle in dailyLogGroupTitle.subTitle!!) {
                            if (subTitle.isChecked) {
                                logs.add(subTitle.subTitleName!!)
                            }
                        }
                        instance!!.setDailyLog(
                            SharedPreferencesEnum.PREMENSTRUATION,
                            logs
                        )
                    }

                    SharedPreferencesEnum.MENSTRUATION -> {
                        logs = ArrayList<String>(dailyLogGroupTitle.subTitle!!.size)
                        for (subTitle in dailyLogGroupTitle.subTitle!!) {
                            if (subTitle.isChecked) {
                                logs.add(subTitle.subTitleName!!)
                            }
                        }
                        instance!!.setDailyLog(SharedPreferencesEnum.MENSTRUATION, logs)
                    }

                    SharedPreferencesEnum.MOOD -> {
                        logs = ArrayList<String>(dailyLogGroupTitle.subTitle!!.size)
                        for (subTitle in dailyLogGroupTitle.subTitle!!) {
                            if (subTitle.isChecked) {
                                logs.add(subTitle.subTitleName!!)
                            }
                        }
                        instance!!.setDailyLog(SharedPreferencesEnum.MOOD, logs)
                    }

                    SharedPreferencesEnum.HABITS -> {
                        logs = ArrayList<String>(dailyLogGroupTitle.subTitle!!.size)
                        for (subTitle in dailyLogGroupTitle.subTitle!!) {
                            if (subTitle.isChecked) {
                                logs.add(subTitle.subTitleName!!)
                            }
                        }
                        instance!!.setDailyLog(SharedPreferencesEnum.HABITS, logs)
                    }

                    SharedPreferencesEnum.SEX_AND_SEX_DRIVE -> {
                        logs = ArrayList<String>(dailyLogGroupTitle.subTitle!!.size)
                        for (subTitle in dailyLogGroupTitle.subTitle!!) {
                            if (subTitle.isChecked) {
                                logs.add(subTitle.subTitleName!!)
                            }
                        }
                        instance!!.setDailyLog(
                            SharedPreferencesEnum.SEX_AND_SEX_DRIVE,
                            logs
                        )
                    }

                    SharedPreferencesEnum.DISEASES_AND_MEDICATION -> {
                        logs = ArrayList<String>(dailyLogGroupTitle.subTitle!!.size)
                        for (subTitle in dailyLogGroupTitle.subTitle!!) {
                            if (subTitle.isChecked) {
                                logs.add(subTitle.subTitleName!!)
                            }
                        }
                        instance!!.setDailyLog(
                            SharedPreferencesEnum.DISEASES_AND_MEDICATION,
                            logs
                        )
                    }

                    SharedPreferencesEnum.BLEEDING -> {
                        logs = ArrayList<String>(dailyLogGroupTitle.subTitle!!.size)
                        for (subTitle in dailyLogGroupTitle.subTitle!!) {
                            if (subTitle.isChecked) {
                                logs.add(subTitle.subTitleName!!)
                            }
                        }
                        instance!!.setDailyLog(SharedPreferencesEnum.BLEEDING, logs)
                    }


                    else -> {
                        Logger.i("other branches" , "true")
                    }
                }
            }
            //            periodTrackerPresenter.sendPeriodData();
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
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
            periodTrackerViewModel!!.sendPeriodTrackerData(request)

            val payload =
                HashMap<String, Any>()
            payload[AppConstants.PERIOD_DATE] = request.periodDate
            payload[AppConstants.PERIOD_LENGTH] = request.periodLength
            payload[AppConstants.LUTEAL_LENGTH] = request.lutealLength
            payload[AppConstants.CYCLE_LENGTH] = request.cycleLenght
            logNetcoreEvent(
                this,
                payload,
                AppConstants.PERIOD_TRACKER_UPDATE_CLICK
            )
        }
    }

    private fun setLiveData() {
        periodTrackerViewModel!!.observePeriodTracker().observe(
            this,
            Observer<Any?> { dataBaseResponse ->
                if (dataBaseResponse != null) {
                    val returnIntent = Intent()
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            })
    }

    private fun prepareData(): ArrayList<DailyLogGroupTitle> {
        val dailyLogGroupTitles: ArrayList<DailyLogGroupTitle> = ArrayList<DailyLogGroupTitle>()

        val symptomspreensturation: DailyLogGroupTitle = DailyLogGroupTitle()
        symptomspreensturation.id = SharedPreferencesEnum.PREMENSTRUATION
        symptomspreensturation.groupName = "Symptoms-Mostly During Pre Mensturation"
        symptomspreensturation.subTitle =
            DayLogUtils.instance!!.getCheckedDailyLog(SharedPreferencesEnum.PREMENSTRUATION)
        dailyLogGroupTitles.add(symptomspreensturation)

        val symptomsmensturation: DailyLogGroupTitle = DailyLogGroupTitle()
        symptomsmensturation.id = SharedPreferencesEnum.MENSTRUATION
        symptomsmensturation.groupName = "Symptoms-Mostly During Mensturation"
        symptomsmensturation.subTitle =
            DayLogUtils.instance!!.getCheckedDailyLog(SharedPreferencesEnum.MENSTRUATION)
        dailyLogGroupTitles.add(symptomsmensturation)

        val diseasesandmedication: DailyLogGroupTitle = DailyLogGroupTitle()
        diseasesandmedication.id = SharedPreferencesEnum.DISEASES_AND_MEDICATION
        diseasesandmedication.groupName = "Diseases and Medication"
        diseasesandmedication.subTitle =
            DayLogUtils.instance!!.getCheckedDailyLog(SharedPreferencesEnum.DISEASES_AND_MEDICATION)
        dailyLogGroupTitles.add(diseasesandmedication)

        val habitsgroup: DailyLogGroupTitle = DailyLogGroupTitle()
        habitsgroup.id = SharedPreferencesEnum.HABITS
        habitsgroup.groupName = "Habits"
        habitsgroup.subTitle = DayLogUtils.instance!!.getCheckedDailyLog(SharedPreferencesEnum.HABITS)
        dailyLogGroupTitles.add(habitsgroup)

        val bleeding: DailyLogGroupTitle = DailyLogGroupTitle()
        bleeding.id = SharedPreferencesEnum.BLEEDING
        bleeding.groupName = "Bleeding"
        bleeding.subTitle = DayLogUtils.instance!!.getCheckedDailyLog(SharedPreferencesEnum.BLEEDING)
        dailyLogGroupTitles.add(bleeding)

        val mood: DailyLogGroupTitle = DailyLogGroupTitle()
        mood.id = SharedPreferencesEnum.MOOD
        mood.groupName = "Mood"
        mood.subTitle = DayLogUtils.instance!!.getCheckedDailyLog(SharedPreferencesEnum.MOOD)
        dailyLogGroupTitles.add(mood)

        val sexandsexdrive: DailyLogGroupTitle = DailyLogGroupTitle()
        sexandsexdrive.id = SharedPreferencesEnum.SEX_AND_SEX_DRIVE
        sexandsexdrive.groupName = "Sex and Sex Drive"
        sexandsexdrive.subTitle =
            DayLogUtils.instance!!.getCheckedDailyLog(SharedPreferencesEnum.SEX_AND_SEX_DRIVE)
        dailyLogGroupTitles.add(sexandsexdrive)

        return dailyLogGroupTitles
    }

    @OnClick(R.id.toolbarBackArrow)
    fun onClickBackArrow() {
        finish()
    } //    @Override
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

    companion object {
        private val TAG: String = DailyLogsScreenActivity::class.java.simpleName
    }
}