package `in`.woloo.www.more.period_tracker.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.periodTracker.collapsiblecalendarview.data.Day
import com.periodTracker.collapsiblecalendarview.view.OnSwipeTouchListener
import com.periodTracker.collapsiblecalendarview.widget.CollapsibleCalendar
import `in`.woloo.www.BuildConfig
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.MessageResponse
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.application_kotlin.presentation.fragments.web_view.WebViewFragment
import `in`.woloo.www.application_kotlin.presentation.fragments.web_view.WebViewFragment.Companion.newInstance
import `in`.woloo.www.application_kotlin.utilities.DayLogUtils
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.more.dailylogscreen.DailyLogsScreenActivity
import `in`.woloo.www.more.dailylogscreen.models.DailyLogSymptoms
import `in`.woloo.www.more.models.UserProfileMergedResponse
import `in`.woloo.www.more.period_tracker.adapters.PeriodTracksBlogsAdapter
import `in`.woloo.www.more.trendingblog.model.blog.Blog
import `in`.woloo.www.more.trendingblog.model.blog.BlogsResponse
import `in`.woloo.www.more.trendingblog.mvp.NearByWolooAndOfferCountView
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger.d
import `in`.woloo.www.utils.Logger.e
import `in`.woloo.www.utils.Logger.i
import `in`.woloo.www.utils.Utility.logNetcoreEvent
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.Objects
import `in`.woloo.www.more.period_tracker.adapters.PeriodTracksBlogsAdapter.PeriodTrackerBlogsListener
import `in`.woloo.www.more.period_tracker.adapters.ShowDailyLogAdapter
import `in`.woloo.www.more.period_tracker.circular_calender.PERIOD_TYPES
import `in`.woloo.www.more.period_tracker.circular_calender.PeriodCalendarView
import `in`.woloo.www.more.period_tracker.circular_calender.PeriodCalendarViewListener
import `in`.woloo.www.more.period_tracker.model.DailyLogWithTitle
import `in`.woloo.www.more.period_tracker.periodtracker.model.PeriodTrackerRequest
import `in`.woloo.www.more.period_tracker.periodtracker.viewmodel.PeriodTrackerViewModel
import `in`.woloo.www.more.period_tracker.receiver.Period7DaysAlarmReceiver
import `in`.woloo.www.more.period_tracker.receiver.PeriodAlarmReceiver
import `in`.woloo.www.more.trendingblog.model.BlogByCategoryResponse
import `in`.woloo.www.more.trendingblog.model.CategoriesResponse
import `in`.woloo.www.more.trendingblog.model.NearByWolooAndOfferCountResponse
import `in`.woloo.www.more.trendingblog.mvp.NearByWolooAndOfferCountPresenter
import `in`.woloo.www.more.trendingblog.viewmodel.BlogViewModel
import org.json.JSONObject

class PeriodTrackerFragment : Fragment(), NearByWolooAndOfferCountView,
    PeriodTrackerBlogsListener {
    @JvmField
    @BindView(R.id.collapsibleCalendarView)
    var collapsibleCalendar: CollapsibleCalendar? = null

    /* @BindView(R.id.periodCalendar)
     PeriodCalendarView periodCalendarView;*/
    @JvmField
    @BindView(R.id.periodCalendar)
    var periodCalendarView: PeriodCalendarView? = null

    @JvmField
    @BindView(R.id.scrollView)
    var scrollView: View? = null

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

    @JvmField
    @BindView(R.id.rvBlogs)
    var rvBlogs: RecyclerView? = null

    @JvmField
    @BindView(R.id.tvPeriodInformation)
    var tvPeriodInformation: TextView? = null

    @JvmField
    @BindView(R.id.show_info_dialog)
    var imageViewShowInfo: ImageView? = null


    private var currentMonth = 0
    private var currentYear = 0
    private var currentDay = 0
    var mSharedPreference: SharedPreference? = null
    private var periodStartDate: Calendar? = null
    private var showDailyLogAdapter: ShowDailyLogAdapter? = null
    private val moodsModelArrayList: MutableList<DailyLogWithTitle> =
        ArrayList<DailyLogWithTitle>() //Key - Resource String value
    private val REQ_CODE = 11
    var presenter: NearByWolooAndOfferCountPresenter? = null
    var blogViewModel: BlogViewModel? = null

    private var blogItemPosition = -1
    private var blog: Blog? = null
    private var blogsAdapter: PeriodTracksBlogsAdapter? = null
    private var blogs: List<Blog>? = ArrayList()
    private var periodTrackerViewModel: PeriodTrackerViewModel? = null

    var notificationStartDate: String? = null
    var notificationCycleLength: Int? = null

    var nextPeriodDate: String = ""
    var previousMonthCount: Int = 0

    var lastPeriodDate: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_period_tracker, container, false)
        ButterKnife.bind(this, rootView)
        presenter = NearByWolooAndOfferCountPresenter(requireContext(), this)

        blogViewModel = ViewModelProvider(this).get<BlogViewModel>(
            BlogViewModel::class.java
        )
        blogViewModel!!.getBlogsForUserByCategory(
            BuildConfig.PERIOD_CATEGORY_ID,
            1,
            AppConstants.BLOG_CAT_TYPE_THREE
        )
        periodTrackerViewModel = ViewModelProvider(this).get<PeriodTrackerViewModel>(
            PeriodTrackerViewModel::class.java
        )
        //        presenter.getBlogs(BuildConfig.PERIOD_CATEGORY_ID, 1);
        showDailyLogAdapter = ShowDailyLogAdapter(requireActivity())
        showDailyLogAdapter!!.addMoods(moodsModelArrayList)
        rvDailyLogs!!.adapter = showDailyLogAdapter
        blogsAdapter = PeriodTracksBlogsAdapter(requireActivity(), blogs!!, this)
        rvBlogs!!.adapter = blogsAdapter
        initView()
        setLogicForCalender()
        setLiveData()
        imageViewShowInfo!!.setOnClickListener { showInfoDialog() }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setLogicForCalender()
        setLiveData()
    }

    override fun onResume() {
        super.onResume()
        d(TAG, "onResume")
        initView()
        setLogicForCalender()
        setLiveData()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        (requireActivity() as WolooDashboard).hideToolbar()
        mSharedPreference = SharedPreference(requireActivity())
        periodStartDate = Calendar.getInstance()


        var startDate: String = mSharedPreference!!.getStoredPreference(
            requireActivity().applicationContext,
            SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(),
            ""
        ).toString()


        try {
            if (startDate != "") {
                val format = SimpleDateFormat("yyyy-MM-dd")
                try {
                    periodStartDate!!.setTime(Objects.requireNonNull(format.parse(startDate)))
                } catch (e: ParseException) {
                    CommonUtils.printStackTrace(e)
                }
            }


            blogViewModel!!.getBlogsForUserByCategory(
                BuildConfig.PERIOD_CATEGORY_ID,
                1,
                AppConstants.BLOG_CAT_TYPE_THREE
            )
            blogsAdapter = PeriodTracksBlogsAdapter(requireActivity(), blogs!!, this)
            rvBlogs!!.adapter = blogsAdapter
            blogsAdapter!!.notifyDataSetChanged()

            val endCycleCalendar = periodStartDate!!.clone() as Calendar
            endCycleCalendar.add(
                Calendar.DATE,
                mSharedPreference!!.getStoredPreference(
                    requireActivity(),
                    SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(),
                    "28"
                )!!.toInt() - 1
            )

            scrollView!!.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {
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


            val currentDate = Date(System.currentTimeMillis())
            Log.d("Aarati calender", "$currentDate current date")

            val startDate1: String = mSharedPreference!!.getStoredPreference(
                requireActivity(),
                SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(),
                ""
            ).toString()
            Log.d("Aarati calender 1", "$startDate1 start date")


            val periodLength: String = mSharedPreference!!.getStoredPreference(
                requireActivity(),
                SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(),
                ""
            ).toString()
            Log.d("Aarati calender", "$periodLength period length ")

            val cycleLength: String = mSharedPreference!!.getStoredPreference(
                requireActivity(),
                SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(),
                ""
            ).toString()
            Log.d("Aarati calender", "$cycleLength cycle length ")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                calculatePreviousMonthDifference(LocalDate.parse(startDate1), LocalDate.now())
            }
            Log.d("Start Date is count ", previousMonthCount.toString())


            //collapsibleCalendar.previousMonthsCount = previousMonthCount.toString();
            val sdf = SimpleDateFormat("yyyy-MM-dd")

            var endPeriodDate: Calendar? = null
            var endPeriod: Date? = null
            var periodEndDate = ""

            // Parse the string to Date
            val startPeriod: Date //= sdf.parse(startDate);
            val formatter = DateTimeFormatter.ofPattern("yyyy-M-d")

            startDate = mSharedPreference!!.getStoredPreference(
                requireActivity(),
                SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(),
                ""
            ).toString()
            val date = LocalDate.parse(startDate, formatter)
            startPeriod = convertToDate(date)


            //Set Period Start Calender
            val startPeriodDate = GregorianCalendar.getInstance()
            startPeriodDate.time = startPeriod

            //Find Period End Date
            endPeriodDate = GregorianCalendar.getInstance()
            endPeriodDate.time = startPeriod
            endPeriodDate.add(Calendar.DATE, (periodLength.toInt() - 1))
            endPeriod = endPeriodDate.time
            periodEndDate = sdf.format(endPeriod)
            Log.d("Aarati calender", "$periodEndDate periodEndDate ")


            //Find cycle End Date
            val cyclePeriodDate = GregorianCalendar.getInstance()
            cyclePeriodDate.time = startPeriod
            cyclePeriodDate.add(Calendar.DATE, (cycleLength.toInt() - 1))
            val cycleEndPeriod = cyclePeriodDate.time
            val periodCycleEndDate = sdf.format(cycleEndPeriod)
            Log.d("Aarati calender", "$periodCycleEndDate periodCycleEndDate ")

            val ovulationCal = GregorianCalendar.getInstance()
            ovulationCal.time = cycleEndPeriod
            ovulationCal.add(Calendar.DATE, -13)
            val ovulationPeriodDate = ovulationCal.time
            val ovulationDate = sdf.format(ovulationPeriodDate)
            Log.d("Aarati calender", "$ovulationDate ovulationDate ")

            val ovulationCalEnd = GregorianCalendar.getInstance()
            ovulationCalEnd.time = cycleEndPeriod
            ovulationCalEnd.add(Calendar.DATE, -13)
            val ovulationPeriodDateEnd = ovulationCalEnd.time
            val ovulationDateEnd = sdf.format(ovulationPeriodDateEnd)
            Log.d("Aarati calender", "$ovulationDateEnd ovulationDateEnd ")


            //need ovulationStartCalender to get ovulation Day
            val ovulationCalStart = GregorianCalendar.getInstance()
            ovulationCalStart.time = cycleEndPeriod
            ovulationCalStart.add(Calendar.DATE, -13)
            val ovulationPeriodDateStart = ovulationCalStart.time
            val ovulationDateStart = sdf.format(ovulationPeriodDateStart)
            Log.d("Aarati calender", "$ovulationDateStart ovulationDateStart ")


            //Find High Fertility Days
            val highFertilityStart = GregorianCalendar.getInstance()
            val highFertilityEnd = GregorianCalendar.getInstance()
            highFertilityStart.time = ovulationPeriodDate
            highFertilityEnd.time = ovulationPeriodDate
            highFertilityStart.add(Calendar.DATE, -6)
            highFertilityEnd.add(Calendar.DATE, 4)


            endPeriodDate.add(Calendar.DATE, 1)
            highFertilityEnd.add(Calendar.DATE, 1)
            ovulationCalEnd.add(Calendar.DATE, 1)

            // ovulationCalEnd.add(Calendar.DATE, -1);

            //   collapsibleCalendar.setGetDate(getDate);
            Log.d("Start Date is ", startDate + "")

            //  Log.d("Start Date is " , startDate1);
            collapsibleCalendar!!.startPeriodCalendar = startPeriodDate
            collapsibleCalendar!!.endPeriodCalendar = endPeriodDate
            collapsibleCalendar!!.endCycleCalendar = endCycleCalendar
            collapsibleCalendar!!.ovulationStartCalendar = ovulationCalStart
            collapsibleCalendar!!.ovulationEndCalendar = ovulationCalEnd
            collapsibleCalendar!!.pregnancyStartDate = highFertilityStart
            collapsibleCalendar!!.pregnancyEndDate = highFertilityEnd
            collapsibleCalendar!!.periodLength = mSharedPreference!!.getStoredPreference(
                requireActivity(),
                SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(),
                "1"
            )!!.toInt()
            collapsibleCalendar!!.cycleLength = mSharedPreference!!.getStoredPreference(
                requireActivity(),
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
            val days = ChronoUnit.DAYS.between(
                convertToLocalDate(startPeriod),
                convertToLocalDate(currentDate)
            )
            collapsibleCalendar!!.params = CollapsibleCalendar.Params((-days.toInt()), 190)

            setLogicForCalender()
        } catch (ex: Exception) {
            println(ex.toString())
        }
        setDailyLog()
    }

    private fun setLiveData() {
        blogViewModel!!.observeCtaFavourite().observe(viewLifecycleOwner,
            Observer<BaseResponse<JSONObject>> { response ->
                if (response.data != null) {
                    onFavouriteABlog()
                }
            })

        periodTrackerViewModel!!.observePeriodTracker().observe(viewLifecycleOwner,
            Observer<Any?> { dataBaseResponse ->
                if (dataBaseResponse != null) {
                    requireActivity().setResult(Activity.RESULT_OK)
                }
            })

        blogViewModel!!.observeCtaLikes().observe(viewLifecycleOwner,
            Observer<BaseResponse<JSONObject>> { response ->
                if (response.data != null) {
                    onLikeABlog()
                }
            })

        blogViewModel!!.observeCtaBlogRead().observe(viewLifecycleOwner,
            Observer<BaseResponse<JSONObject>> { response ->
                if (response.data != null) {
                    onReadABlog()
                }
            })

        blogViewModel!!.observeBlogReadPoint().observe(viewLifecycleOwner,
            Observer<BaseResponse<MessageResponse>> { response ->
                if (response.data != null) {
                    onBlogReadPointsAdded()
                }
            })

        blogViewModel!!.observeBlogsForUserByCategory().observe(viewLifecycleOwner,
            Observer<BaseResponse<BlogByCategoryResponse>> { response ->
                if (response.data != null) {
                    i(TAG, "getBlogs")
                    blogs = response.data!!.blogs
                    blogsListener()
                    blogsAdapter!!.notifyDataSetChanged()
                }
            })
    }


    private fun setDailyLog() {
        setAdapterData()
        flAddDailyLogs!!.setOnClickListener {
            val intent = Intent(
                requireActivity(),
                DailyLogsScreenActivity::class.java
            )
            startActivityForResult(intent, REQ_CODE)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAdapterData() {
        val bleeding: List<DailyLogSymptoms?> =
            DayLogUtils.instance!!.getDailyLog(SharedPreferencesEnum.BLEEDING)
        val mood: List<DailyLogSymptoms?> =
            DayLogUtils.instance!!.getDailyLog(SharedPreferencesEnum.MOOD)
        val sex_and_sex_drive: List<DailyLogSymptoms?> =
            DayLogUtils.instance!!.getDailyLog(SharedPreferencesEnum.SEX_AND_SEX_DRIVE)
        val habits: List<DailyLogSymptoms?> =
            DayLogUtils.instance!!.getDailyLog(SharedPreferencesEnum.HABITS)
        val premenstruation: List<DailyLogSymptoms?> =
            DayLogUtils.instance!!.getDailyLog(SharedPreferencesEnum.PREMENSTRUATION)
        val diseasesandmedication: List<DailyLogSymptoms?> =
            DayLogUtils.instance!!.getDailyLog(SharedPreferencesEnum.DISEASES_AND_MEDICATION)
        val menstruation: List<DailyLogSymptoms?> =
            DayLogUtils.instance!!.getDailyLog(SharedPreferencesEnum.MENSTRUATION)

        moodsModelArrayList.clear()

        if (premenstruation.isNotEmpty()) moodsModelArrayList.add(
            DailyLogWithTitle(
                R.string.premenstruation,
                premenstruation
            )
        )
        if (menstruation.isNotEmpty()) moodsModelArrayList.add(
            DailyLogWithTitle(
                R.string.menstruation,
                menstruation
            )
        )
        if (diseasesandmedication.isNotEmpty()) moodsModelArrayList.add(
            DailyLogWithTitle(
                R.string.diseasesandmedication,
                diseasesandmedication
            )
        )
        if (bleeding.isNotEmpty()) moodsModelArrayList.add(
            DailyLogWithTitle(
                R.string.bleeding,
                bleeding
            )
        )
        if (habits.isNotEmpty()) moodsModelArrayList.add(DailyLogWithTitle(R.string.habits, habits))
        if (mood.isNotEmpty()) moodsModelArrayList.add(DailyLogWithTitle(R.string.mood, mood))
        if (sex_and_sex_drive.isNotEmpty()) moodsModelArrayList.add(
            DailyLogWithTitle(
                R.string.sex,
                sex_and_sex_drive
            )
        )

        if (moodsModelArrayList.isEmpty()) {
            tvNoDataAvailable!!.visibility = View.VISIBLE
        } else {
            tvNoDataAvailable!!.visibility = View.GONE
        }
        showDailyLogAdapter!!.notifyDataSetChanged()
    }

    @OnClick(R.id.ivBack)
    protected fun onPressBack() {
        d(TAG, "onPressBack: ")
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun nearByWolooAndOfferCountResponse(nearByWolooAndOfferCountResponse: NearByWolooAndOfferCountResponse?) {
    }

    override fun getCategories(categoriesResponse: CategoriesResponse?) {
        i(TAG, "getCategories")
    }

    override fun getBlogs(blogsResponse: BlogsResponse?) {
        i(TAG, "getBlogs")
        blogs = blogsResponse!!.data!!.blogs
        blogsAdapter = PeriodTracksBlogsAdapter(requireActivity(), blogs!!, this)
        rvBlogs!!.adapter = blogsAdapter
    }

    fun blogsListener() {
        blogsAdapter = PeriodTracksBlogsAdapter(requireActivity(), blogs!!, this)
        rvBlogs!!.adapter = blogsAdapter
    }


    override fun onFavouriteABlog() {
        if (blogs != null && !blogs!!.isEmpty() && blogItemPosition > -1) {
            val blog = blogs!![blogItemPosition]
            val status = if (blog.isFavourite == 0) {
                1
            } else {
                0
            }
            blog.isFavourite = status
            // blog.isFavourite(status);
            blogsAdapter!!.notifyItemChanged(blogItemPosition)
        }
    }

    override fun onLikeABlog() {
        if (blogs != null && blogs!!.isNotEmpty() && blogItemPosition > -1) {
            val blog = blogs!![blogItemPosition]
            val status = if (blog.isLiked == 0) {
                1
            } else {
                0
            }
            blog.isLiked = status
            // blog.isLiked(status);
            blogsAdapter!!.notifyItemChanged(blogItemPosition)
        }
    }


    override fun onReadABlog() {
        if (blog != null) {
//            presenter.addBlogReadPoints(blog);
            blogViewModel!!.blogReadPoint(blog!!.id.toString())
        }
    }

    override fun onBlogReadPointsAdded() {
        blogsAdapter!!.notifyItemChanged(blogItemPosition)
        navigateToWebView()
    }

    override fun setUserProfileMergedResponse(userProfileMergedResponse: UserProfileMergedResponse?) {
    }

    override fun onClickBlogItem(blog: Blog?, position: Int) {
        blogItemPosition = position
        this.blog = blog
        if (blog!!.isBlogRead == 0) { // Not yet read
            blogs!![position].isBlogRead = 1
            //  blogs.get(position).isBlogRead(1);
//            presenter.readABlog(blog);
            blogViewModel!!.ctaBlogRead(blog.id.toString())
        } else {
            navigateToWebView()
        }
    }

    private fun navigateToWebView() {
        /*requireActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.frm_contant, WebViewFragment.newInstance(blog.getTitle(), blog.getShortLink())).addToBackStack(null).commit();*/
        val webViewFragment = newInstance(
            blog!!.title, blog!!.shortLink, TAG
        )
        (requireActivity() as WolooDashboard).loadMenuFragment(webViewFragment, WebViewFragment.TAG)
        (requireActivity() as WolooDashboard).changeIcon(
            (requireActivity() as WolooDashboard).nav_view!!.menu.findItem(
                R.id.navigation_dash_home
            )
        )
    }

    override fun onClickBlogFavourite(blog: Blog?, position: Int) {
        blogItemPosition = position
        //        presenter.favouriteABlog(blog);
        blogViewModel!!.ctaFavourite(blog!!.id.toString())
    }

    override fun onClickBlogLike(blog: Blog?, position: Int) {
        blogItemPosition = position
        //        presenter.likeABlog(blog);
        blogViewModel!!.ctaLikes(blog!!.id.toString())
    }

    override fun onClickBlogShare(blog: Blog?, position: Int) {
        if (TextUtils.isEmpty(blog!!.title) || TextUtils.isEmpty(blog.shortLink)) {
            return
        }
        val share = Intent(Intent.ACTION_SEND)
        share.setType("text/plain")
        share.putExtra(
            Intent.EXTRA_TEXT, """
     ${blog.title}
     ${blog.shortLink}
     """.trimIndent()
        )
        startActivity(Intent.createChooser(share, null))
    }


    //Added by Aarati to show information @Woloo on 24 July 2024
    private fun showInfoDialog() {
        i(TAG, "showInfoDialog")
        try {
            val alertDialogBuilder = AlertDialog.Builder(
                requireActivity()
            )
            val child =
                layoutInflater.inflate(R.layout.dialog_show_ovulation_phase_information, null)
            alertDialogBuilder.setView(child)
            alertDialogBuilder.setCancelable(true)
            val alertDialog = alertDialogBuilder.create()
            val imgClose = child.findViewById<ImageView>(R.id.close_dialog_info)
            imgClose.setOnClickListener { alertDialog.dismiss() }



            alertDialog.show()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun setLogicForCalender() {
        try {
            val menstruationDays: MutableList<Int> = ArrayList()
            val ovulationDays: MutableList<Int> = ArrayList()
            val highFertilityDays: MutableList<Int> = ArrayList()
            val normalDays: List<Int> = ArrayList()
            val ovulationDay = 0

            val currentDate = Date(System.currentTimeMillis())
            Log.d("Aarati calender", "$currentDate current date")

            val startDate: String = mSharedPreference!!.getStoredPreference(
                requireActivity(),
                SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(),
                ""
            ).toString()
            Log.d("Aarati calender", "$startDate start date")

            val periodLength: String = mSharedPreference!!.getStoredPreference(
                requireActivity(),
                SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(),
                ""
            ).toString()
            Log.d("Aarati calender", "$periodLength period length ")

            val cycleLength: String = mSharedPreference!!.getStoredPreference(
                requireActivity(),
                SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(),
                ""
            ).toString()
            Log.d("Aarati calender", "$cycleLength cycle length ")
            val sdf = SimpleDateFormat("yyyy-MM-dd")

            var endPeriodDate: Calendar? = null
            var endPeriod: Date? = null
            var periodEndDate = ""

            // Parse the string to Date
            val startPeriod = sdf.parse(startDate)

            //Set Period Start Calender
            val startPeriodDate = Calendar.getInstance()
            startPeriodDate.time = startPeriod

            //Find Period End Date
            endPeriodDate = Calendar.getInstance()
            endPeriodDate.time = startPeriod
            endPeriodDate.add(Calendar.DATE, (periodLength.toInt() - 1))
            endPeriod = endPeriodDate.time
            periodEndDate = sdf.format(endPeriod)
            Log.d("Aarati calender", "$periodEndDate periodEndDate ")


            //Find cycle End Date
            val cyclePeriodDate = Calendar.getInstance()
            cyclePeriodDate.time = startPeriod
            cyclePeriodDate.add(Calendar.DATE, (cycleLength.toInt() - 1))
            val cycleEndPeriod = cyclePeriodDate.time
            val periodCycleEndDate = sdf.format(cycleEndPeriod)
            Log.d("Aarati calender", "$periodCycleEndDate periodCycleEndDate ")

            if (currentDate.after(cycleEndPeriod)) {
                //Find if next period started
                val today = LocalDate.now()
                var startDateN = convertToLocalDate(
                    startPeriod!!
                )
                val datesBefore: MutableList<LocalDate> = ArrayList()


                while (!startDateN.isAfter(today)) {
                    datesBefore.add(startDateN)
                    startDateN = startDateN.plusDays(cycleLength.toLong())
                }
                for (date in datesBefore) {
                    Log.d("Aarati Calender Logic i", date.toString() + "" + datesBefore.size)
                }


                if (datesBefore.size > 2) {
                    Log.d("Aarati Calender Logic i", "in if")
                    lastPeriodDate = startDate
                    val newDate = datesBefore[datesBefore.size - 1]
                    val newD = convertToDate(newDate)
                    val startDateNew = sdf.format(newD)
                    nextPeriodDate = startDateNew
                    notificationCycleLength = cycleLength.toInt()
                    showNewDateDialog(startDate, startDateNew, cycleLength, periodLength)
                } else {
                    lastPeriodDate = startDate
                    Log.d("Aarati Calender Logic i", "IN ELSE")
                    Toast.makeText(
                        activity,
                        "2nd Cycle started Already from your enter date $startDate",
                        Toast.LENGTH_SHORT
                    ).show()
                    val startNewPeriodDate = Calendar.getInstance()
                    startNewPeriodDate.time = cycleEndPeriod
                    startNewPeriodDate.add(Calendar.DATE, 1)
                    val startDateNew = sdf.format(startNewPeriodDate.time)
                    nextPeriodDate = startDateNew
                    notificationCycleLength = cycleLength.toInt()
                    Log.d(
                        "Aarati calender",
                        "$startDateNew startPeriodDate After Cycle (New Period started)"
                    )

                    // Write the code for update start day in Shared preference as well as in API(Database)
                    mSharedPreference!!.setStoredPreference(
                        activity,
                        SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(),
                        startDateNew
                    )
                    mSharedPreference!!.setStoredPreference(
                        activity,
                        SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(),
                        periodLength
                    )
                    mSharedPreference!!.setStoredPreference(
                        activity,
                        SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(),
                        cycleLength
                    )

                    val request: PeriodTrackerRequest = PeriodTrackerRequest()
                    try {
                        request.cycleLenght = mSharedPreference!!.getStoredPreference(
                            activity,
                            SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey()
                        ).toString()
                        request.log = DayLogUtils.instance!!.asLog
                        request.lutealLength = "14"
                        request.periodLength = mSharedPreference!!.getStoredPreference(
                            activity,
                            SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey()
                        ).toString()
                        request.periodDate = mSharedPreference!!.getStoredPreference(
                            activity,
                            SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey()
                        ).toString()
                    } catch (e: Exception) {
                        CommonUtils.printStackTrace(e)
                    }
                    periodTrackerViewModel!!.sendPeriodTrackerData(request)

                    val payload = HashMap<String, Any>()
                    payload[AppConstants.PERIOD_DATE] = request.periodDate
                    payload[AppConstants.PERIOD_LENGTH] = request.periodLength
                    payload[AppConstants.LUTEAL_LENGTH] = request.lutealLength
                    payload[AppConstants.CYCLE_LENGTH] = request.cycleLenght
                    logNetcoreEvent(requireActivity(), payload, AppConstants.PERIOD_TRACKER_UPDATE_CLICK)

                    //  setNotificationRemainder();
                    setLogicForCalender()
                }
            } else {
                //Find Ovulation Day


                val ovulationCal = Calendar.getInstance()
                ovulationCal.time = cycleEndPeriod
                ovulationCal.add(Calendar.DATE, -13)
                val ovulationPeriodDate = ovulationCal.time
                val ovulationDate = sdf.format(ovulationPeriodDate)
                Log.d("Aarati calender", "$ovulationDate ovulationDate ")

                //need ovulationEndCalender to get ovulation Day
                val ovulationCalEnd = Calendar.getInstance()
                ovulationCalEnd.time = cycleEndPeriod
                ovulationCalEnd.add(Calendar.DATE, -13)
                val ovulationPeriodDateEnd = ovulationCalEnd.time
                val ovulationDateEnd = sdf.format(ovulationPeriodDateEnd)
                Log.d("Aarati calender", "$ovulationDateEnd ovulationDateEnd ")


                //need ovulationStartCalender to get ovulation Day
                val ovulationCalStart = Calendar.getInstance()
                ovulationCalStart.time = cycleEndPeriod
                ovulationCalStart.add(Calendar.DATE, -13)
                val ovulationPeriodDateStart = ovulationCalStart.time
                val ovulationDateStart = sdf.format(ovulationPeriodDateStart)
                Log.d("Aarati calender", "$ovulationDateStart ovulationDateStart ")


                //Find High Fertility Days
                val highFertilityStart = Calendar.getInstance()
                val highFertilityEnd = Calendar.getInstance()
                highFertilityStart.time = ovulationPeriodDate
                highFertilityEnd.time = ovulationPeriodDate
                highFertilityStart.add(Calendar.DATE, -6)
                highFertilityEnd.add(Calendar.DATE, 4)
                val highFertilityStartDate = highFertilityStart.time
                val highFertilityEndDate = highFertilityEnd.time
                val highFertilityStartDateString = sdf.format(highFertilityStartDate)
                val highFertilityEndDateString = sdf.format(highFertilityEndDate)
                Log.d(
                    "Aarati calender",
                    "$highFertilityStartDateString highFertilityStartDateString "
                )
                Log.d(
                    "Aarati calender",
                    "$highFertilityEndDateString highFertilityEndDateString "
                )

                //add values to arrays of respective types
                val setAllDays = Calendar.getInstance()
                setAllDays.time = startPeriod
                // Value changing for while loop
                startPeriodDate.add(Calendar.DATE, -1)
                endPeriodDate.add(Calendar.DATE, 1)
                highFertilityStart.add(Calendar.DATE, -1)
                highFertilityEnd.add(Calendar.DATE, 1)
                ovulationCalEnd.add(Calendar.DATE, -1)

                lastPeriodDate = startDate
                var startDateN = convertToLocalDate(
                    startPeriod!!
                )
                startDateN = startDateN.plusDays(cycleLength.toLong())
                val nextDate = convertToDate(startDateN)
                nextPeriodDate = sdf.format(nextDate)
                notificationCycleLength = cycleLength.toInt()


                while (!setAllDays.after(cyclePeriodDate)) {
                    val days = ChronoUnit.DAYS.between(
                        convertToLocalDate(
                            startPeriod
                        ), convertToLocalDate(currentDate)
                    )
                    periodCalendarView!!.setPeriodDays(days.toInt() + 1)

                    if (setAllDays.after(startPeriodDate) && setAllDays.before(endPeriodDate)) {
                        menstruationDays.add(setAllDays[Calendar.DATE])
                        if (Calendar.getInstance()[Calendar.DATE] == setAllDays[Calendar.DATE] && Calendar.getInstance()[Calendar.MONTH] == setAllDays[Calendar.MONTH] && Calendar.getInstance()[Calendar.YEAR] == setAllDays[Calendar.YEAR]) {
                            periodCalendarView!!.setPeriodType(PERIOD_TYPES.MENSTRUATION)
                        }
                        Log.d(
                            "Aarati Date Type",
                            periodCalendarView!!.getPeriodType()
                                .toString() + " " + Calendar.getInstance()[Calendar.DATE] + " " + Calendar.getInstance()[Calendar.MONTH]
                        )
                    } else if (setAllDays == ovulationCal) {
                        ovulationDays.add(setAllDays[Calendar.DATE])
                        if (Calendar.getInstance()[Calendar.DATE] == setAllDays[Calendar.DATE] && Calendar.getInstance()[Calendar.MONTH] == setAllDays[Calendar.MONTH] && Calendar.getInstance()[Calendar.YEAR] == setAllDays[Calendar.YEAR]) {
                            periodCalendarView!!.setPeriodType(PERIOD_TYPES.OVULATION)
                        }

                        // ovulationDay = setAllDays.get(Calendar.DATE);
                    } else if (setAllDays.after(highFertilityStart) && setAllDays.before(
                            ovulationCal
                        )
                    ) {
                        highFertilityDays.add(setAllDays[Calendar.DATE])
                        if (Calendar.getInstance()[Calendar.DATE] == setAllDays[Calendar.DATE] && Calendar.getInstance()[Calendar.MONTH] == setAllDays[Calendar.MONTH] && Calendar.getInstance()[Calendar.YEAR] == setAllDays[Calendar.YEAR]) {
                            periodCalendarView!!.setPeriodType(PERIOD_TYPES.PREGNANCY)
                        }
                    } else if (setAllDays.after(ovulationCalEnd) && setAllDays.before(
                            highFertilityEnd
                        )
                    ) {
                        highFertilityDays.add(setAllDays[Calendar.DATE])
                        if (Calendar.getInstance()[Calendar.DATE] == setAllDays[Calendar.DATE] && Calendar.getInstance()[Calendar.MONTH] == setAllDays[Calendar.MONTH] && Calendar.getInstance()[Calendar.YEAR] == setAllDays[Calendar.YEAR]) {
                            periodCalendarView!!.setPeriodType(PERIOD_TYPES.PREGNANCY)
                        }
                    } else {
                        if (Calendar.getInstance()[Calendar.DATE] == setAllDays[Calendar.DATE] && Calendar.getInstance()[Calendar.MONTH] == setAllDays[Calendar.MONTH] && Calendar.getInstance()[Calendar.YEAR] == setAllDays[Calendar.YEAR]) {
                            periodCalendarView!!.setPeriodType(PERIOD_TYPES.PERIOD)
                        }
                    }
                    setAllDays.add(Calendar.DATE, 1)
                }
                for (i in highFertilityDays.indices) {
                    Log.d(
                        "Aarati calender high fertility",
                        "high fertility Day " + highFertilityDays[i].toString()
                    )
                }
                for (i in ovulationDays.indices) {
                    Log.d("Aarati calender ovulation", "ovulation day " + ovulationDays[0])
                }
                for (i in menstruationDays.indices) {
                    Log.d(
                        "Aarati calender menstruation",
                        "menstruation day " + menstruationDays[i].toString()
                    )
                }


                periodCalendarView!!.setPeriodCycle(
                    menstruationDays,
                    ovulationDays,
                    highFertilityDays
                )
                val c = Calendar.getInstance()
                currentYear = c[Calendar.YEAR]
                currentMonth = c[Calendar.MONTH]
                currentDay = c[Calendar.DATE]
                periodCalendarView!!.setCalendar(currentDay, currentMonth, currentYear)
                periodCalendarView!!.setPeriodCalendarViewListener(object :
                    PeriodCalendarViewListener {
                    override fun onEdit() {
                        Log.d("click on edot cyle", "edit cycle")
                        startActivityForResult(
                            Intent(
                                requireActivity(),
                                EditCycleActivity::class.java
                            ), 13
                        )
                    }
                })

                val tvText = "You are currently in <b>" + periodCalendarView!!.getPeriodType()
                    .toString() + "</b> phase." + "<br>Your last period date was " + "<b>" + lastPeriodDate + "</b>" + " and next period cycle starting on " + "<b>" + nextPeriodDate + "</b>."
                tvPeriodInformation!!.text = Html.fromHtml(tvText)
                //  setNotificationRemainder();
            }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 13 && resultCode == Activity.RESULT_OK) {
            // Handle the data from the new activity
            // For example, refresh the fragment
            refreshFragment()
        }
    }

    fun refreshFragment() {
        /* FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Assuming you're using a FragmentContainerView or FrameLayout as your container
        Fragment fragment = new PeriodTrackerFragment();
        if (fragment != null) {
           // fragmentTransaction.remove(fragment);
            fragmentTransaction.replace(R.id.frm_contant, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }*/
        initView()
        setLiveData()
        setLogicForCalender()
    }

    /* @SuppressLint("ScheduleExactAlarm")
    private void setNotificationRemainder() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = formatter.parse(notificationStartDate);
            date.setMinutes(new Date().getMinutes() + 2);
            date.setHours(new Date().getHours());


            // Create Calendar instances for n days and n-7 days
            Calendar calendarBefore2Days = Calendar.getInstance();
            calendarBefore2Days.setTime(date);
            calendarBefore2Days.add(Calendar.DAY_OF_MONTH, notificationCycleLength - 2);
           */
    /* calendarBefore2Days.set(Calendar.HOUR_OF_DAY, 10);
            calendarBefore2Days.set(Calendar.MINUTE, 0);
            calendarBefore2Days.set(Calendar.SECOND, 0);
            calendarBefore2Days.set(Calendar.MILLISECOND, 0);*/
    /*
            //            calendarBefore2Days.set(Calendar.HOUR_OF_DAY, 9);
//            calendarBefore2Days.set(Calendar.MINUTE, 0);
//            calendarBefore2Days.set(Calendar.SECOND, 0);
//            calendarBefore2Days.set(Calendar.MILLISECOND, 0);

            Logger.i("EditCycleActivity", "Before 2 days : " + calendarBefore2Days.getTime().toString());


            Calendar calendarBefore7Days = Calendar.getInstance();
            calendarBefore7Days.setTime(date);
            calendarBefore7Days.add(Calendar.DAY_OF_MONTH, notificationCycleLength - 7);
           */
    /* calendarBefore7Days.set(Calendar.HOUR_OF_DAY, 6);
            calendarBefore7Days.set(Calendar.MINUTE, 17);
            calendarBefore7Days.set(Calendar.SECOND, 0);
            calendarBefore7Days.set(Calendar.MILLISECOND, 0);*/
    /*
            //            calendarBefore7Days.set(Calendar.HOUR_OF_DAY, 9);
//            calendarBefore7Days.set(Calendar.MINUTE, 0);
//            calendarBefore7Days.set(Calendar.SECOND, 0);
//            calendarBefore7Days.set(Calendar.MILLISECOND, 0);
            Logger.i("EditCycleActivity", "Before 7 days : " + calendarBefore7Days.getTime().toString());

            // Schedule notification for 2 days before
            AlarmManager alarmManager = (AlarmManager) getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            // Create an Intent for the BroadcastReceiver
            Intent notificationIntent = new Intent(getActivity(), PeriodAlarmReceiver.class);
            notificationIntent.setAction("in.woloo.www.ACTION_TRIGGER_NOTIFICATION");
            notificationIntent.putExtra("message", "2 days until next Period.");

            // Create a PendingIntent for the BroadcastReceiver
            int requestCode = (int) System.currentTimeMillis(); // Unique request code
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Logger.i("EditCycleActivity", "pendingIntent called");
            // Set the alarm using AlarmManager
            if (alarmManager != null) {

                Logger.i("EditCycleActivity", "Before 2 days : " + calendarBefore2Days.getTimeInMillis());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarBefore2Days.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarBefore2Days.getTimeInMillis(), pendingIntent);
                }

            }
            else
            {
                Logger.i("EditCycleActivity", "Before 2 days : " );
            }


            // Schedule notification for 7 days before

            AlarmManager alarmManager2 = (AlarmManager) getActivity().getApplication().getSystemService(Context.ALARM_SERVICE);

            // Create an Intent for the BroadcastReceiver
            Intent notificationIntent2 = new Intent(getActivity(), Period7DaysAlarmReceiver.class);
            notificationIntent2.setAction("in.woloo.www.ACTION_TRIGGER_NOTIFICATION");
            notificationIntent2.putExtra("message", "7 days until next Period.");

            // Create a PendingIntent for the BroadcastReceiver
            int requestCode2 = (int) System.currentTimeMillis(); // Unique request code
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getActivity(), requestCode2, notificationIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
            Logger.i("EditCycleActivity", "pendingIntent2 called");
            // Set the alarm using AlarmManager
            if (alarmManager2 != null) {
                // alarmManager2.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarBefore7Days.getTimeInMillis(), pendingIntent2);
                Logger.i("EditCycleActivity", "Before 7 days : " + calendarBefore7Days.getTimeInMillis());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager2.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarBefore7Days.getTimeInMillis(), pendingIntent2);
                } else {
                    alarmManager2.setExact(AlarmManager.RTC_WAKEUP, calendarBefore7Days.getTimeInMillis(), pendingIntent2);
                }

            }
            else
            {
                Logger.i("EditCycleActivity", "Before 7 days : " );
            }


        }catch (Exception e){

        }

    }*/
    @SuppressLint("SetTextI18n")
    private fun showNewDateDialog(
        oldDate: String?,
        newDate: String,
        cycleLengthGiven: String?,
        periodLengthGiven: String?
    ) {
        try {
            /*  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            View child = getLayoutInflater().inflate(R.layout.logout_dialog, null);
            alertDialogBuilder.setView(child);
            alertDialogBuilder.setCancelable(false);
            AlertDialog alertDialog = alertDialogBuilder.create();
            TextView tvCancel = child.findViewById(R.id.tvCancel);
            TextView tvLogout = child.findViewById(R.id.tvLogout);
            TextView tv_logout = child.findViewById(R.id.tv_logout);
            tv_logout.setText("Welcome Back\n As per your last period details you entered " + oldDate +" with " + cycleLengthGiven +" days of cycle \nYour current cycle date is " + newDate);
            tvCancel.setText("OKAY");
            tvLogout.setText("EDIT");
            tvLogout.setText("Period Date");


            tvCancel.setOnClickListener(v -> {*/

            mSharedPreference!!.setStoredPreference(
                activity,
                SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(),
                newDate
            )
            mSharedPreference!!.setStoredPreference(
                activity,
                SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(),
                periodLengthGiven
            )
            mSharedPreference!!.setStoredPreference(
                activity,
                SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(),
                cycleLengthGiven
            )

            val request: PeriodTrackerRequest = PeriodTrackerRequest()
            try {
                request.cycleLenght = mSharedPreference!!.getStoredPreference(
                    activity,
                    SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey()
                ).toString()
                request.log = DayLogUtils.instance!!.asLog
                request.lutealLength = "14"
                request.periodLength = mSharedPreference!!.getStoredPreference(
                    activity,
                    SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey()
                ).toString()
                request.periodDate = mSharedPreference!!.getStoredPreference(
                    activity,
                    SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey()
                ).toString()
            } catch (e: Exception) {
                CommonUtils.printStackTrace(e)
            }
            periodTrackerViewModel!!.sendPeriodTrackerData(request)

            val payload = HashMap<String, Any>()
            payload[AppConstants.PERIOD_DATE] = request.periodDate
            payload[AppConstants.PERIOD_LENGTH] = request.periodLength
            payload[AppConstants.LUTEAL_LENGTH] = request.lutealLength
            payload[AppConstants.CYCLE_LENGTH] = request.cycleLenght
            logNetcoreEvent(requireActivity(), payload, AppConstants.PERIOD_TRACKER_UPDATE_CLICK)

            // setNotificationRemainder();
            setLogicForCalender()

            /*  alertDialog.dismiss();
            });
            tvLogout.setOnClickListener(v -> {
                startActivity(new Intent(requireActivity(), EditCycleActivity.class));
                alertDialog.dismiss();
            });
            alertDialog.show();*/
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun calculatePreviousMonthDifference(beforeDate: LocalDate, currentDate: LocalDate): Int {
        if (beforeDate.year == currentDate.year) {
            // Calculate difference in months if years are the same
            val monthsDifference = ChronoUnit.MONTHS.between(
                LocalDate.of(beforeDate.year, beforeDate.monthValue, 1),
                LocalDate.of(currentDate.year, currentDate.monthValue, 1)
            )
            previousMonthCount = monthsDifference.toInt()
        } else {
            // Check if the year is before or after and calculate accordingly
            val yearsDifference = currentDate.year - beforeDate.year
            val monthsInYears = yearsDifference * 12

            // Calculate difference in months considering the year difference
            val monthsDifference = ChronoUnit.MONTHS.between(
                LocalDate.of(beforeDate.year, beforeDate.monthValue, 1),
                LocalDate.of(currentDate.year, currentDate.monthValue, 1)
            )
            previousMonthCount = monthsDifference.toInt() - monthsInYears
        }

        Log.d("Start Date is calculated", previousMonthCount.toString())
        return previousMonthCount
    }

    @SuppressLint("ScheduleExactAlarm", "UseRequireInsteadOfGet")
    private fun setNotificationRemainder() {
        try {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            val date = formatter.parse(nextPeriodDate)
            date!!.minutes = Date().minutes + 2
            date.hours = Date().hours


            // Create Calendar instances for n days and n-7 days
            val calendarBefore2Days = Calendar.getInstance()
            calendarBefore2Days.time = date
            calendarBefore2Days.add(Calendar.DAY_OF_MONTH, notificationCycleLength!! - 2)

            /*  calendarBefore2Days.set(Calendar.HOUR_OF_DAY, 10);
            calendarBefore2Days.set(Calendar.MINUTE, 0);
            calendarBefore2Days.set(Calendar.SECOND, 0);
            calendarBefore2Days.set(Calendar.MILLISECOND, 0);*/
            //            calendarBefore2Days.set(Calendar.HOUR_OF_DAY, 9);
//            calendarBefore2Days.set(Calendar.MINUTE, 0);
//            calendarBefore2Days.set(Calendar.SECOND, 0);
//            calendarBefore2Days.set(Calendar.MILLISECOND, 0);
            i("EditCycleActivity", "Before 2 days : " + calendarBefore2Days.time.toString())


            val calendarBefore7Days = Calendar.getInstance()
            calendarBefore7Days.time = date
            calendarBefore7Days.add(Calendar.DAY_OF_MONTH, notificationCycleLength!! - 7)
            /*  calendarBefore7Days.set(Calendar.HOUR_OF_DAY, 6);
            calendarBefore7Days.set(Calendar.MINUTE, 17);
            calendarBefore7Days.set(Calendar.SECOND, 0);
            calendarBefore7Days.set(Calendar.MILLISECOND, 0);*/
            //            calendarBefore7Days.set(Calendar.HOUR_OF_DAY, 9);
//            calendarBefore7Days.set(Calendar.MINUTE, 0);
//            calendarBefore7Days.set(Calendar.SECOND, 0);
//            calendarBefore7Days.set(Calendar.MILLISECOND, 0);
            i("EditCycleActivity ", "Before 7 days : " + calendarBefore7Days.time.toString())

            // Schedule notification for 2 days before
            val alarmManager =
                requireActivity().applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // Create an Intent for the BroadcastReceiver
            val notificationIntent = Intent(
                requireActivity().applicationContext,
                PeriodAlarmReceiver::class.java
            )
            notificationIntent.setAction("in.woloo.www.ACTION_TRIGGER_NOTIFICATION")
            notificationIntent.putExtra("message", "2 days until next Period.")

            // Create a PendingIntent for the BroadcastReceiver
            val requestCode = System.currentTimeMillis().toInt() // Unique request code
            val pendingIntent = PendingIntent.getBroadcast(
                requireActivity().applicationContext,
                requestCode,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            i("EditCycleActivity PTF 21", "pendingIntent called")
            // Set the alarm using AlarmManager
            if (alarmManager != null) {
                i("EditCycleActivity PTF 21", "Before 2 days : " + calendarBefore2Days.timeInMillis)
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
                i("EditCycleActivity PTF 2", "Before 2 days : ")
            }


            // Schedule notification for 7 days before
            val alarmManager2 =
                requireActivity().applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // Create an Intent for the BroadcastReceiver
            val notificationIntent2 = Intent(
                requireActivity().applicationContext,
                Period7DaysAlarmReceiver::class.java
            )
            notificationIntent2.setAction("in.woloo.www.ACTION_TRIGGER_NOTIFICATION")
            notificationIntent2.putExtra("message", "7 days until next Period.")

            // Create a PendingIntent for the BroadcastReceiver
            val requestCode2 = System.currentTimeMillis().toInt() // Unique request code
            val pendingIntent2 = PendingIntent.getBroadcast(
                requireActivity().applicationContext,
                requestCode2,
                notificationIntent2,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            i("EditCycleActivity PTF 1", "pendingIntent2 called")
            // Set the alarm using AlarmManager
            if (alarmManager2 != null) {
                // alarmManager2.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarBefore7Days.getTimeInMillis(), pendingIntent2);
                i("EditCycleActivity PTF 1", "Before 7 days : " + calendarBefore7Days.timeInMillis)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager2.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendarBefore7Days.timeInMillis,
                        pendingIntent2
                    )
                } else {
                    alarmManager2.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendarBefore7Days.timeInMillis,
                        pendingIntent2
                    )
                }
            } else {
                i("EditCycleActivity PTF", "Before 7 days : ")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            e("EditCycleActivity PTF", "Error setting notification", e)
        }
    }


    companion object {
        const val TAG: String = "PeriodTrackerFragment"

        fun convertToLocalDate(date: Date): LocalDate {
            return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
        }

        fun convertToDate(localDate: LocalDate): Date {
            val localDateTime = localDate.atStartOfDay()
            val instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant()
            return Date.from(instant)
        }
    }
}
