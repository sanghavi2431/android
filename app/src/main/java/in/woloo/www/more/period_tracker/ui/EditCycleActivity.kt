package `in`.woloo.www.more.period_tracker.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.model.CreditCoinsRequest
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.application_kotlin.utilities.DayLogUtils
import `in`.woloo.www.application_kotlin.view_models.SharedViewModel
import `in`.woloo.www.application_kotlin.view_models.WolooViewModel
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.more.period_tracker.model.Log
import `in`.woloo.www.more.period_tracker.model.PeriodTrackerResponse
import `in`.woloo.www.more.period_tracker.periodtracker.model.PeriodTrackerRequest
import `in`.woloo.www.more.period_tracker.periodtracker.viewmodel.PeriodTrackerViewModel
import `in`.woloo.www.more.period_tracker.receiver.Period7DaysAlarmReceiver
import `in`.woloo.www.more.period_tracker.receiver.PeriodAlarmReceiver
import `in`.woloo.www.more.thirstreminder.ThirstReminderDialog.ThirstCallbacks
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Logger.d
import `in`.woloo.www.utils.Logger.e
import `in`.woloo.www.utils.Logger.i
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Objects
import java.util.Random
import java.util.TimeZone

class EditCycleActivity : DialogFragment() {
    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: LinearLayout? = null

    private lateinit var sharedViewModel: SharedViewModel


    @JvmField
    @BindView(R.id.day_Tv)
    var tvDay: TextView? = null

    @JvmField
    @BindView(R.id.month_Tv)
    var tvMonth: TextView? = null

    @JvmField
    @BindView(R.id.year_TV)
    var tvYear: TextView? = null

   /* @JvmField
    @BindView(R.id.screen_header)
    var tvTitle: TextView? = null*/

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
    private var wolooViewModel: WolooViewModel? = null



        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.activity_edit_cycle, container)
            ButterKnife.bind(this, view)
            wolooViewModel = ViewModelProvider(this).get<WolooViewModel>(
                WolooViewModel::class.java
            )

            if (dialog != null && dialog!!.window != null) {
                dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
                val params = dialog!!.window!!.attributes
                params.width = WindowManager.LayoutParams.MATCH_PARENT
                params.height = WindowManager.LayoutParams.WRAP_CONTENT
                dialog!!.window!!.setLayout(params.width, params.height)
            }

        mSharedPreference = SharedPreference(requireActivity().applicationContext)
        ButterKnife.bind(requireActivity().applicationContext , view)
        periodTrackerViewModel = ViewModelProvider(this).get<PeriodTrackerViewModel>(
            PeriodTrackerViewModel::class.java
        )



        initView()
        setLiveData()
            return view
    }

    private fun initView() {
     /*   tvTitle!!.text = "Edit Cycle"
        ivBack!!.setOnClickListener { v: View? ->
          dismiss()
        }*/
        var periodLength: String = mSharedPreference!!.getStoredPreference(
            requireActivity().applicationContext,
            SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(),
            "5"
        ).toString()
        var periodCycleLength: String = mSharedPreference!!.getStoredPreference(
            requireActivity().applicationContext, SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), "28"
        ).toString()
        val periodDate: String = mSharedPreference!!.getStoredPreference(
            requireActivity().applicationContext,
            SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(),
            ""
        ).toString()
        if (periodCycleLength == null || periodCycleLength == "null" || periodCycleLength == "" || periodCycleLength == "0") periodCycleLength =
            "28"

        if (periodLength == null || periodLength == "null" || periodLength == "" || periodLength == "0") periodLength =
            "5"


        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        android.util.Log.d("aarati test " , periodCycleLength)
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
                    requireActivity().applicationContext,
                    SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(),
                    startDate
                )
                mSharedPreference!!.setStoredPreference(
                    requireActivity().applicationContext,
                    SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(),
                    period_length_edText!!.text.toString().trim { it <= ' ' })
                mSharedPreference!!.setStoredPreference(
                    requireActivity().applicationContext,
                    SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(),
                    cycle_length_edText!!.text.toString().trim { it <= ' ' })

                val request: PeriodTrackerRequest = PeriodTrackerRequest()
                try {
                    request.cycleLenght = mSharedPreference!!.getStoredPreference(
                        requireActivity().applicationContext,
                        SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey()
                    ).toString()
                    request.log = DayLogUtils.instance!!.asLog
                    request.lutealLength = "14"
                    request.periodLength = mSharedPreference!!.getStoredPreference(
                        requireActivity().applicationContext,
                        SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey()
                    ).toString()
                    request.periodDate = mSharedPreference!!.getStoredPreference(
                        requireActivity().applicationContext,
                        SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey()
                    ).toString()
                    android.util.Log.d(
                        "Aarati",
                        mSharedPreference!!.getStoredPreference(
                            requireActivity().applicationContext,
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
                    requireActivity().applicationContext,
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
                requireActivity().applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val currentDateMillis = System.currentTimeMillis()
            d(
                "Edit cycle Activity aarati ",
                currentDateMillis.toString() + " " + calendarBefore7Days.timeInMillis + " " + (calendarBefore7Days.timeInMillis < currentDateMillis)
            )


            if (calendarBefore2Days.timeInMillis > currentDateMillis) {
                val notificationIntent = Intent(
                    requireActivity().applicationContext,
                    PeriodAlarmReceiver::class.java
                )
                notificationIntent.setAction("in.woloo.www.ACTION_TRIGGER_NOTIFICATION")
                notificationIntent.putExtra("message", "2 days until next Period.")

                val requestCode = Random().nextInt(Int.MAX_VALUE)
                // int requestCode = (int) System.currentTimeMillis(); // Unique request code
                val pendingIntent = PendingIntent.getBroadcast(
                    requireActivity().applicationContext,
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
                    requireActivity().applicationContext,
                    Period7DaysAlarmReceiver::class.java
                )
                notificationIntent2.setAction("in.woloo.www.ACTION_TRIGGER_NOTIFICATION")
                notificationIntent2.putExtra("message", "7 days until next Period.")

                // Create a PendingIntent for the BroadcastReceiver
                val requestCode2 = Random().nextInt(Int.MAX_VALUE)
                //   int requestCode2 = (int) System.currentTimeMillis(); // Unique request code
                val pendingIntent2 = PendingIntent.getBroadcast(
                    requireActivity().applicationContext,
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

                        try {
                            val request = CreditCoinsRequest(
                                coins = CommonUtils.authconfig_response(requireContext())
                                    .getSetPeriodTracker()!!.toInt(),
                                remarks = AppConstants.SET_PERIOD_TRACKER_CLICK,
                                type = AppConstants.SET_PERIOD_TRACKER_CLICK,
                                isGift = 0,
                                blogId = 0
                            )
                            wolooViewModel!!.addCoinstoWolooUser(request)

                            var bundle = Bundle()
                            var payload = HashMap<String, Any>()
                            bundle.putString(AppConstants.SET_PERIOD_TRACKER_CLICK, startDate)
                            bundle.putString(AppConstants.SET_PERIOD_TRACKER_CLICK, startDate)
                            payload[AppConstants.SET_PERIOD_TRACKER_CLICK] = startDate.toString()
                            payload[AppConstants.SET_PERIOD_TRACKER_CLICK] = startDate.toString()
                            logFirebaseEvent(
                                requireActivity(),
                                bundle,
                                AppConstants.SET_PERIOD_TRACKER_CLICK
                            )
                            logNetcoreEvent(
                                requireActivity(),
                                payload,
                                AppConstants.SET_PERIOD_TRACKER_CLICK
                            )
                        }catch (e:Exception)
                        {

                        }
                }
                periodTrackerViewModel!!.getPeriodTracker()
            })


            var coinsObserver: Observer<BaseResponse<JSONObject>>? = null
            coinsObserver = Observer<BaseResponse<JSONObject>> { response ->
                CommonUtils().hideProgress()
               // showSuccessDialog()

                // remove after first trigger
                wolooViewModel?.observeAddCoinstoWolooUser()?.removeObserver(coinsObserver!!)
            }

            wolooViewModel?.observeAddCoinstoWolooUser()
                ?.observe(viewLifecycleOwner, coinsObserver!!)


        periodTrackerViewModel!!.observeGetPeriodTracker().observe(
           this,
            Observer<BaseResponse<PeriodTrackerResponse.Data>> { getPeriodDataResponse ->
                try {
                    if (getPeriodDataResponse != null && getPeriodDataResponse.data != null) {
                        if (mSharedPreference == null) mSharedPreference = SharedPreference(
                            requireActivity().applicationContext
                        )
                        mSharedPreference!!.setStoredPreference(
                            requireActivity().applicationContext,
                            SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(),
                            getPeriodDataResponse.data!!.periodDate
                        )
                        mSharedPreference!!.setStoredPreference(
                            requireActivity().applicationContext,
                            SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(),
                            getPeriodDataResponse.data!!.periodLength.toString()
                        )
                        mSharedPreference!!.setStoredPreference(
                            requireActivity().applicationContext,
                            SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(),
                            getPeriodDataResponse.data!!.cycleLenght.toString()
                        )


                        mSharedPreference!!.setStoredPreference(
                            requireActivity().applicationContext,
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

              //  callback?.onDialogFragmentButtonClicked()

                val parentFragment = parentFragment
                if (parentFragment !is PeriodTrackerFragment) {
                    val periodTrackerFragment = PeriodTrackerFragment()
                    // Begin a transaction to replace the current fragment with XYZFragment
                    (activity as WolooDashboard).loadMenuFragment(periodTrackerFragment, PeriodTrackerFragment.TAG)
                }
                else{
                    sharedViewModel.triggerRefresh()

                }

dismiss()

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
                    requireActivity().applicationContext,
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
                Toast.makeText(requireActivity().applicationContext, "Period Length should be between 4-8 days", Toast.LENGTH_SHORT)
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
                    requireActivity(),
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



    override fun onStart() {
        super.onStart()
        // Set dialog fragment dimensions to full screen
        if (dialog != null) {
            dialog?.window?.let { window ->
                val params = window.attributes
                val displayMetrics = resources.displayMetrics
                val screenWidth = displayMetrics.widthPixels

                params.width = (screenWidth * 0.95).toInt() // 75% of screen width
                params.height = WindowManager.LayoutParams.WRAP_CONTENT
                params.gravity = android.view.Gravity.CENTER // Center the dialog

                window.attributes = params
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        // notify parent fragment
        (parentFragment as? PeriodTrackerFragment)?.onBottomSheetDismissed()
    }

    interface EditCycleCallbacks {
        fun onClickThirstYes()
        fun onClickThirstNo()
    }

    fun showSuccessDialog()
    {
        try {
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_coins_success)
            val displayMetrics = Resources.getSystem().displayMetrics
            val screenWidth = displayMetrics.widthPixels

// Calculate 80% of screen width
            val dialogWidth = (screenWidth).toInt()

// Apply the calculated width and wrap_content height to the dialog window
            dialog.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            dialog.window?.setGravity(Gravity.BOTTOM)
            val btnCloseDialog = dialog.findViewById<View>(R.id.tv_go_back_to_home) as TextView
            val btnShopDialog = dialog.findViewById<View>(R.id.tv_shop_now) as TextView
            val gifImageView = dialog.findViewById<View>(R.id.gifImageView) as ImageView

            val btnSuccessTextDialog = dialog.findViewById<View>(R.id.tv_logout) as TextView
            btnSuccessTextDialog.setText("Woohoo! You Earned ${CommonUtils.authconfig_response(requireContext()).getSetPeriodTracker()!!.toInt()} Woloo Points!")
            btnShopDialog.visibility = View.GONE

            Glide.with(this)
                .load(R.drawable.coins_animate) // your gif in res/drawable
                .into(gifImageView)

            btnCloseDialog.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                    (parentFragment as? PeriodTrackerFragment)?.onBottomSheetDismissed()

                }
            }



           dialog.setOnDismissListener {
               (parentFragment as? PeriodTrackerFragment)?.onBottomSheetDismissed()
           }


            dialog.show()
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }


    companion object {
         var TAG: String = "EditCycleActivity"
    }


}