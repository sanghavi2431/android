package in.woloo.www.period_tracker.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.periodTracker.collapsiblecalendarview.data.CalendarAdapter;
import com.periodTracker.collapsiblecalendarview.data.Day;
import com.periodTracker.collapsiblecalendarview.view.OnSwipeTouchListener;
import com.periodTracker.collapsiblecalendarview.widget.CollapsibleCalendar;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.common.DayLogUtils;
import in.woloo.www.dailylogscreen.DailyLogsScreenActivity;
import in.woloo.www.dailylogscreen.models.DailyLogSymptoms;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.more.fragments.MoreFragment;
import in.woloo.www.more.models.UserProfileMergedResponse;
import in.woloo.www.period_tracker.adapters.PeriodTracksBlogsAdapter;
import in.woloo.www.period_tracker.adapters.ShowDailyLogAdapter;
import in.woloo.www.period_tracker.circular_calender.PERIOD_TYPES;
import in.woloo.www.period_tracker.circular_calender.PeriodCalendarView;
import in.woloo.www.period_tracker.circular_calender.PeriodCalendarViewListener;
import in.woloo.www.period_tracker.model.DailyLogWithTitle;
import in.woloo.www.period_tracker.model.PeriodTrackerResponse;
import in.woloo.www.period_tracker.receiver.Period7DaysAlarmReceiver;
import in.woloo.www.period_tracker.receiver.PeriodAlarmReceiver;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.trendingblog.model.CategoriesResponse;
import in.woloo.www.trendingblog.model.NearByWolooAndOfferCountResponse;
import in.woloo.www.trendingblog.model.blog.Blog;
import in.woloo.www.trendingblog.model.blog.BlogsResponse;
import in.woloo.www.trendingblog.mvp.NearByWolooAndOfferCountPresenter;
import in.woloo.www.trendingblog.mvp.NearByWolooAndOfferCountView;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.EmailSender;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.blog.model.BlogByCategoryResponse;
import in.woloo.www.v2.blog.viewmodel.BlogViewModel;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.data.remote.MessageResponse;
import in.woloo.www.v2.login.activity.LoginActivity;
import in.woloo.www.v2.periodtracker.model.PeriodTrackerRequest;
import in.woloo.www.v2.periodtracker.viewmodel.PeriodTrackerViewModel;
import in.woloo.www.webview.WebViewFragment;

public class PeriodTrackerFragment extends Fragment implements NearByWolooAndOfferCountView, PeriodTracksBlogsAdapter.PeriodTrackerBlogsListener {
    public static final String TAG = "PeriodTrackerFragment";

    @BindView(R.id.collapsibleCalendarView)
    CollapsibleCalendar collapsibleCalendar;

   /* @BindView(R.id.periodCalendar)
    PeriodCalendarView periodCalendarView;*/
    @BindView(R.id.periodCalendar)
    PeriodCalendarView periodCalendarView;

    @BindView(R.id.scrollView)
    View scrollView;

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

    @BindView(R.id.rvBlogs)
    RecyclerView rvBlogs;

    @BindView(R.id.tvPeriodInformation)
    TextView tvPeriodInformation;

    @BindView(R.id.show_info_dialog)
    ImageView imageViewShowInfo;

    private int currentMonth = 0;
    private int currentYear = 0;
    private int currentDay = 0;
    public SharedPreference mSharedPreference;
    private Calendar periodStartDate;
    private ShowDailyLogAdapter showDailyLogAdapter;
    private final List<DailyLogWithTitle> moodsModelArrayList = new ArrayList<>();//Key - Resource String value
    private int REQ_CODE = 11;
    NearByWolooAndOfferCountPresenter presenter;
    BlogViewModel blogViewModel;

    private int blogItemPosition = -1;
    private Blog blog = null;
    private PeriodTracksBlogsAdapter blogsAdapter;
    private List<Blog> blogs = new ArrayList<>();
    private PeriodTrackerViewModel periodTrackerViewModel;

    String notificationStartDate;
    Integer notificationCycleLength;

    String nextPeriodDate = "";
     Integer previousMonthCount = 0;

     String lastPeriodDate = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_period_tracker, container, false);
        ButterKnife.bind(this, rootView);
        presenter = new NearByWolooAndOfferCountPresenter(getContext(), this);

        blogViewModel = new ViewModelProvider(this).get(BlogViewModel.class);
        blogViewModel.getBlogsForUserByCategory(BuildConfig.PERIOD_CATEGORY_ID, 1);
        periodTrackerViewModel = new ViewModelProvider(this).get(PeriodTrackerViewModel.class);
//        presenter.getBlogs(BuildConfig.PERIOD_CATEGORY_ID, 1);
        showDailyLogAdapter = new ShowDailyLogAdapter(requireActivity());
        showDailyLogAdapter.addMoods(moodsModelArrayList);
        rvDailyLogs.setAdapter(showDailyLogAdapter);
        blogsAdapter = new PeriodTracksBlogsAdapter(requireActivity(), blogs, this);
        rvBlogs.setAdapter(blogsAdapter);
        initView();
        setLogicForCalender();
        setLiveData();
        imageViewShowInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog();
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        setLogicForCalender();
        setLiveData();
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d(TAG, "onResume");
        initView();
        setLogicForCalender();
        setLiveData();

    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        ((WolooDashboard) requireActivity()).hideToolbar();
        mSharedPreference = new SharedPreference(requireActivity());
        periodStartDate = Calendar.getInstance();


        String startDate = mSharedPreference.getStoredPreference(requireActivity().getApplicationContext(), SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(), "");


        try {
            if (!startDate.equals("")) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    periodStartDate.setTime(Objects.requireNonNull(format.parse(startDate)));
                } catch (ParseException e) {
                      CommonUtils.printStackTrace(e);
                }
            }


            blogViewModel.getBlogsForUserByCategory(BuildConfig.PERIOD_CATEGORY_ID, 1);
            blogsAdapter = new PeriodTracksBlogsAdapter(requireActivity(), blogs, this);
            rvBlogs.setAdapter(blogsAdapter);
            blogsAdapter.notifyDataSetChanged();

            Calendar endCycleCalendar = (Calendar) periodStartDate.clone();
            endCycleCalendar.add(Calendar.DATE, Integer.parseInt(mSharedPreference.getStoredPreference(requireActivity(), SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), "28")) - 1);

            scrollView.setOnTouchListener(new OnSwipeTouchListener(requireActivity()) {
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



            Date currentDate = new Date(System.currentTimeMillis());
            Log.d("Aarati calender", currentDate + " current date");

            String startDate1 = mSharedPreference.getStoredPreference(requireActivity(), SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(), "");
            Log.d("Aarati calender 1", startDate1 + " start date");



            String periodLength = mSharedPreference.getStoredPreference(requireActivity(), SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(), "");
            Log.d("Aarati calender", periodLength + " period length ");

            String cycleLength = mSharedPreference.getStoredPreference(requireActivity(), SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), "");
            Log.d("Aarati calender", cycleLength + " cycle length ");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                calculatePreviousMonthDifference(LocalDate.parse(startDate1),LocalDate.now());
            }
            Log.d("Start Date is count ", previousMonthCount.toString());
            //collapsibleCalendar.previousMonthsCount = previousMonthCount.toString();


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Calendar endPeriodDate = null;
            Date endPeriod = null;
            String periodEndDate = "";

            // Parse the string to Date
           Date startPeriod;//= sdf.parse(startDate);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");

            startDate = mSharedPreference.getStoredPreference(requireActivity() , SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey() , "");
                LocalDate date = LocalDate.parse(startDate, formatter);
                startPeriod = convertToDate(date);


            //Set Period Start Calender
            Calendar startPeriodDate = GregorianCalendar.getInstance();
            startPeriodDate.setTime(startPeriod);

            //Find Period End Date
            endPeriodDate = GregorianCalendar.getInstance();
            endPeriodDate.setTime(startPeriod);
            endPeriodDate.add(Calendar.DATE, (Integer.valueOf(periodLength) - 1));
            endPeriod = endPeriodDate.getTime();
            periodEndDate = sdf.format(endPeriod);
            Log.d("Aarati calender", periodEndDate + " periodEndDate ");


            //Find cycle End Date
            Calendar cyclePeriodDate = GregorianCalendar.getInstance();
            cyclePeriodDate.setTime(startPeriod);
            cyclePeriodDate.add(Calendar.DATE, (Integer.valueOf(cycleLength) - 1));
            Date cycleEndPeriod = cyclePeriodDate.getTime();
            String periodCycleEndDate = sdf.format(cycleEndPeriod);
            Log.d("Aarati calender", periodCycleEndDate + " periodCycleEndDate ");

            Calendar ovulationCal = GregorianCalendar.getInstance();
            ovulationCal.setTime(cycleEndPeriod);
            ovulationCal.add(Calendar.DATE, -13);
            Date ovulationPeriodDate = ovulationCal.getTime();
            String ovulationDate = sdf.format(ovulationPeriodDate);
            Log.d("Aarati calender", ovulationDate + " ovulationDate ");

            Calendar ovulationCalEnd = GregorianCalendar.getInstance();
            ovulationCalEnd.setTime(cycleEndPeriod);
            ovulationCalEnd.add(Calendar.DATE, -13);
            Date ovulationPeriodDateEnd = ovulationCalEnd.getTime();
            String ovulationDateEnd = sdf.format(ovulationPeriodDateEnd);
            Log.d("Aarati calender", ovulationDateEnd + " ovulationDateEnd ");


            //need ovulationStartCalender to get ovulation Day
            Calendar ovulationCalStart = GregorianCalendar.getInstance();
            ovulationCalStart.setTime(cycleEndPeriod);
            ovulationCalStart.add(Calendar.DATE, -13);
            Date ovulationPeriodDateStart = ovulationCalStart.getTime();
            String ovulationDateStart = sdf.format(ovulationPeriodDateStart);
            Log.d("Aarati calender", ovulationDateStart + " ovulationDateStart ");


            //Find High Fertility Days
            Calendar highFertilityStart = GregorianCalendar.getInstance();
            Calendar highFertilityEnd = GregorianCalendar.getInstance();
            highFertilityStart.setTime(ovulationPeriodDate);
            highFertilityEnd.setTime(ovulationPeriodDate);
            highFertilityStart.add(Calendar.DATE, -6);
            highFertilityEnd.add(Calendar.DATE, 4);


            endPeriodDate.add(Calendar.DATE, 1);
            highFertilityEnd.add(Calendar.DATE, 1);
            ovulationCalEnd.add(Calendar.DATE, 1);
           // ovulationCalEnd.add(Calendar.DATE, -1);

         //   collapsibleCalendar.setGetDate(getDate);
            Log.d("Start Date is ", startDate + "");

          //  Log.d("Start Date is " , startDate1);
            collapsibleCalendar.setStartPeriodCalendar(startPeriodDate);
            collapsibleCalendar.setEndPeriodCalendar(endPeriodDate);
            collapsibleCalendar.setEndCycleCalendar(endCycleCalendar);
            collapsibleCalendar.setOvulationStartCalendar(ovulationCalStart);
            collapsibleCalendar.setOvulationEndCalendar(ovulationCalEnd);
            collapsibleCalendar.setPregnancyStartDate(highFertilityStart);
            collapsibleCalendar.setPregnancyEndDate(highFertilityEnd);
            collapsibleCalendar.setPeriodLength(Integer.parseInt(mSharedPreference.getStoredPreference(requireActivity(), SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(), "1")));
            collapsibleCalendar.setCycleLength(Integer.parseInt(mSharedPreference.getStoredPreference(requireActivity(), SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), "28")));
            collapsibleCalendar.setExpandIconVisible(true);
            //Calendar today = new GregorianCalendar();
            collapsibleCalendar.addEventTag(periodStartDate.get(Calendar.YEAR), periodStartDate.get(Calendar.MONTH), periodStartDate.get(Calendar.DAY_OF_MONTH));
            periodStartDate.add(Calendar.DATE, 1);
            collapsibleCalendar.setSelectedDay(new Day(periodStartDate.get(Calendar.YEAR), periodStartDate.get(Calendar.MONTH), periodStartDate.get(Calendar.DAY_OF_MONTH)));
            //collapsibleCalendar.addEventTag(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH), Color.BLUE);
            long days = ChronoUnit.DAYS.between(convertToLocalDate(startPeriod), convertToLocalDate(currentDate));
            collapsibleCalendar.setParams(new CollapsibleCalendar.Params((-(int) days), 190));

            setLogicForCalender();

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        setDailyLog();
    }

    private void setLiveData(){
        blogViewModel.observeCtaFavourite().observe(getViewLifecycleOwner(), new Observer<BaseResponse<JSONObject>>() {
            @Override
            public void onChanged(BaseResponse<JSONObject> response) {
                if(response != null && response.getData() != null){
                    onFavouriteABlog();
                }
            }
        });

        periodTrackerViewModel.observePeriodTracker().observe(getActivity(), new Observer<BaseResponse<PeriodTrackerResponse.Data>>() {
            @Override
            public void onChanged(BaseResponse<PeriodTrackerResponse.Data> dataBaseResponse) {
                if(dataBaseResponse != null) {
                    getActivity().setResult(Activity.RESULT_OK);
                }

            }
        });

        blogViewModel.observeCtaLikes().observe(getViewLifecycleOwner(), new Observer<BaseResponse<JSONObject>>() {
            @Override
            public void onChanged(BaseResponse<JSONObject> response) {
                if(response != null && response.getData() != null){
                    onLikeABlog();
                }
            }
        });

        blogViewModel.observeCtaBlogRead().observe(getViewLifecycleOwner(), new Observer<BaseResponse<JSONObject>>() {
            @Override
            public void onChanged(BaseResponse<JSONObject> response) {
                if(response != null && response.getData() != null){
                    onReadABlog();
                }
            }
        });

        blogViewModel.observeBlogReadPoint().observe(getViewLifecycleOwner(), new Observer<BaseResponse<MessageResponse>>() {
            @Override
            public void onChanged(BaseResponse<MessageResponse> response) {
                if(response != null && response.getData() != null){
                    onBlogReadPointsAdded();
                }
            }
        });

        blogViewModel.observeBlogsForUserByCategory().observe(getViewLifecycleOwner(), new Observer<BaseResponse<BlogByCategoryResponse>>() {
            @Override
            public void onChanged(BaseResponse<BlogByCategoryResponse> response) {
                if(response != null && response.getData() != null) {
                    Logger.i(TAG, "getBlogs");
                    blogs = response.getData().getBlogs();
                    blogsListener();
                    blogsAdapter.notifyDataSetChanged();
                }
            }
        });
    }



    private void setDailyLog() {
        setAdapterData();
        flAddDailyLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), DailyLogsScreenActivity.class);
                startActivityForResult(intent, REQ_CODE);
            }
        });
    }

    private void setAdapterData() {
        List<DailyLogSymptoms> bleeding = DayLogUtils.getInstance().getDailyLog(SharedPreferencesEnum.BLEEDING);
        List<DailyLogSymptoms> mood = DayLogUtils.getInstance().getDailyLog(SharedPreferencesEnum.MOOD);
        List<DailyLogSymptoms> sex_and_sex_drive = DayLogUtils.getInstance().getDailyLog(SharedPreferencesEnum.SEX_AND_SEX_DRIVE);
        List<DailyLogSymptoms> habits = DayLogUtils.getInstance().getDailyLog(SharedPreferencesEnum.HABITS);
        List<DailyLogSymptoms> premenstruation = DayLogUtils.getInstance().getDailyLog(SharedPreferencesEnum.PREMENSTRUATION);
        List<DailyLogSymptoms> diseasesandmedication = DayLogUtils.getInstance().getDailyLog(SharedPreferencesEnum.DISEASES_AND_MEDICATION);
        List<DailyLogSymptoms> menstruation = DayLogUtils.getInstance().getDailyLog(SharedPreferencesEnum.MENSTRUATION);

        moodsModelArrayList.clear();

        if (!premenstruation.isEmpty())
            moodsModelArrayList.add(new DailyLogWithTitle(R.string.premenstruation, premenstruation));
        if (!menstruation.isEmpty())
            moodsModelArrayList.add(new DailyLogWithTitle(R.string.menstruation, menstruation));
        if (!diseasesandmedication.isEmpty())
            moodsModelArrayList.add(new DailyLogWithTitle(R.string.diseasesandmedication, diseasesandmedication));
        if (!bleeding.isEmpty())
            moodsModelArrayList.add(new DailyLogWithTitle(R.string.bleeding, bleeding));
        if (!habits.isEmpty())
            moodsModelArrayList.add(new DailyLogWithTitle(R.string.habits, habits));
        if (!mood.isEmpty())
            moodsModelArrayList.add(new DailyLogWithTitle(R.string.mood, mood));
        if (!sex_and_sex_drive.isEmpty())
            moodsModelArrayList.add(new DailyLogWithTitle(R.string.sex, sex_and_sex_drive));

        if (moodsModelArrayList.isEmpty()) {
            tvNoDataAvailable.setVisibility(View.VISIBLE);
        } else {
            tvNoDataAvailable.setVisibility(View.GONE);
        }
        showDailyLogAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.ivBack)
    protected void onPressBack() {
        Logger.d(TAG, "onPressBack: ");
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    @Override
    public void nearByWolooAndOfferCountResponse(NearByWolooAndOfferCountResponse nearByWolooAndOfferCountResponse) {

    }

    @Override
    public void getCategories(CategoriesResponse categoriesResponse) {
        Logger.i(TAG, "getCategories");
    }

    @Override
    public void getBlogs(BlogsResponse blogsResponse) {
        Logger.i(TAG, "getBlogs");
        blogs = blogsResponse.getData().getBlogs();
        blogsAdapter = new PeriodTracksBlogsAdapter(requireActivity(), blogs, this);
        rvBlogs.setAdapter(blogsAdapter);
    }

    public void blogsListener()
    {
        blogsAdapter = new PeriodTracksBlogsAdapter(requireActivity(), blogs, this);
        rvBlogs.setAdapter(blogsAdapter);
    }


    @Override
    public void onFavouriteABlog() {
        if (blogs != null && !blogs.isEmpty() && blogItemPosition > -1) {
            Blog blog = blogs.get(blogItemPosition);
            int status;
            if (blog.getIsFavourite() == 0) {
                status = 1;
            } else {
                status = 0;
            }
            blog.setIsFavourite(status);
            blogsAdapter.notifyItemChanged(blogItemPosition);
        }
    }

    @Override
    public void onLikeABlog() {
        if (blogs != null && !blogs.isEmpty() && blogItemPosition > -1) {
            Blog blog = blogs.get(blogItemPosition);
            int status;
            if (blog.getIsLiked() == 0) {
                status = 1;
            } else {
                status = 0;
            }
            blog.setIsLiked(status);
            blogsAdapter.notifyItemChanged(blogItemPosition);
        }
    }


    public void onReadABlog() {
        if(blog != null) {
//            presenter.addBlogReadPoints(blog);
            blogViewModel.blogReadPoint(blog.getId().toString());
        }
    }

    @Override
    public void onBlogReadPointsAdded() {
        blogsAdapter.notifyItemChanged(blogItemPosition);
        navigateToWebView();
    }

    @Override
    public void setUserProfileMergedResponse(UserProfileMergedResponse userProfileMergedResponse) {

    }

    @Override
    public void onClickBlogItem(Blog blog, int position) {
        blogItemPosition = position;
        this.blog = blog;
        if (blog.getIsBlogRead() == 0) {// Not yet read
            blogs.get(position).setIsBlogRead(1);
//            presenter.readABlog(blog);
            blogViewModel.ctaBlogRead(blog.getId().toString());
        } else {
            navigateToWebView();
        }
    }

    private void navigateToWebView() {
        /*requireActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.frm_contant, WebViewFragment.newInstance(blog.getTitle(), blog.getShortLink())).addToBackStack(null).commit();*/
        WebViewFragment webViewFragment = WebViewFragment.newInstance(blog.getTitle(), blog.getShortLink() , PeriodTrackerFragment.TAG);
        ((WolooDashboard) requireActivity()).loadMenuFragment(webViewFragment, WebViewFragment.TAG);
        ((WolooDashboard) requireActivity()).changeIcon(((WolooDashboard) requireActivity()).nav_view.getMenu().findItem(R.id.navigation_home));
    }

    @Override
    public void onClickBlogFavourite(Blog blog, int position) {
        blogItemPosition = position;
//        presenter.favouriteABlog(blog);
        blogViewModel.ctaFavourite(blog.getId().toString());
    }

    @Override
    public void onClickBlogLike(Blog blog, int position) {
        blogItemPosition = position;
//        presenter.likeABlog(blog);
        blogViewModel.ctaLikes(blog.getId().toString());
    }

    @Override
    public void onClickBlogShare(Blog blog, int position) {
        if (TextUtils.isEmpty(blog.getTitle()) || TextUtils.isEmpty(blog.getShortLink())) {
            return;
        }
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, blog.getTitle() + "\n" + blog.getShortLink());
        startActivity(Intent.createChooser(share, null));
    }



    //Added by Aarati to show information @Woloo on 24 July 2024
    private void showInfoDialog() {
        Logger.i(TAG, "showInfoDialog");
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
            View child = getLayoutInflater().inflate(R.layout.dialog_show_ovulation_phase_information, null);
            alertDialogBuilder.setView(child);
            alertDialogBuilder.setCancelable(true);
            AlertDialog alertDialog = alertDialogBuilder.create();
            ImageView imgClose = child.findViewById(R.id.close_dialog_info);
           imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        alertDialog.dismiss();

                }
            });



            alertDialog.show();

        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    private void setLogicForCalender() {

        try {

            List<Integer> menstruationDays = new ArrayList<>();
            List<Integer> ovulationDays = new ArrayList<>();
            List<Integer> highFertilityDays = new ArrayList<>();
            List<Integer> normalDays = new ArrayList<>();
            Integer ovulationDay = 0;

            Date currentDate = new Date(System.currentTimeMillis());
            Log.d("Aarati calender", currentDate + " current date");

            String startDate = mSharedPreference.getStoredPreference(requireActivity(), SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(), "");
            Log.d("Aarati calender", startDate + " start date");

            String periodLength = mSharedPreference.getStoredPreference(requireActivity(), SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(), "");
            Log.d("Aarati calender", periodLength + " period length ");

            String cycleLength = mSharedPreference.getStoredPreference(requireActivity(), SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), "");
            Log.d("Aarati calender", cycleLength + " cycle length ");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Calendar endPeriodDate = null;
            Date endPeriod = null;
            String periodEndDate = "";

            // Parse the string to Date
            Date startPeriod = sdf.parse(startDate);

            //Set Period Start Calender
            Calendar startPeriodDate = Calendar.getInstance();
            startPeriodDate.setTime(startPeriod);

            //Find Period End Date
            endPeriodDate = Calendar.getInstance();
            endPeriodDate.setTime(startPeriod);
            endPeriodDate.add(Calendar.DATE, (Integer.valueOf(periodLength) - 1));
            endPeriod = endPeriodDate.getTime();
            periodEndDate = sdf.format(endPeriod);
            Log.d("Aarati calender", periodEndDate + " periodEndDate ");


            //Find cycle End Date
            Calendar cyclePeriodDate = Calendar.getInstance();
            cyclePeriodDate.setTime(startPeriod);
            cyclePeriodDate.add(Calendar.DATE, (Integer.valueOf(cycleLength) - 1));
            Date cycleEndPeriod = cyclePeriodDate.getTime();
            String periodCycleEndDate = sdf.format(cycleEndPeriod);
            Log.d("Aarati calender", periodCycleEndDate + " periodCycleEndDate ");

            if (currentDate.after(cycleEndPeriod)) {
                //Find if next period started
                LocalDate today = LocalDate.now();
                LocalDate startDateN = convertToLocalDate(startPeriod);
                List<LocalDate> datesBefore = new ArrayList<>();


                while (!startDateN.isAfter(today)) {
                    datesBefore.add(startDateN);
                    startDateN = startDateN.plusDays(Long.parseLong(cycleLength));
                }
                for (LocalDate date : datesBefore) {
                    Log.d("Aarati Calender Logic i" ,date + "" +datesBefore.size());
                }


                if(datesBefore.size() > 2)
                {
                    Log.d("Aarati Calender Logic i" ,"in if");
                    lastPeriodDate = startDate;
                    LocalDate newDate = datesBefore.get(datesBefore.size()-1);
                    Date newD = convertToDate(newDate);
                    String startDateNew = sdf.format(newD);
                    nextPeriodDate = startDateNew;
                    notificationCycleLength = Integer.valueOf(cycleLength);
                    showNewDateDialog(startDate , startDateNew ,cycleLength , periodLength);
                }
                else {
                    lastPeriodDate = startDate;
                    Log.d("Aarati Calender Logic i" ,"IN ELSE");
                    Toast.makeText(getActivity(), "2nd Cycle started Already from your enter date " + startDate, Toast.LENGTH_SHORT).show();
                Calendar startNewPeriodDate = Calendar.getInstance();
                startNewPeriodDate.setTime(cycleEndPeriod);
                startNewPeriodDate.add(Calendar.DATE, 1);
                String startDateNew = sdf.format(startNewPeriodDate.getTime());
                nextPeriodDate = startDateNew;
                    notificationCycleLength = Integer.valueOf(cycleLength);
                Log.d("Aarati calender", startDateNew + " startPeriodDate After Cycle (New Period started)");

                    // Write the code for update start day in Shared preference as well as in API(Database)
                    mSharedPreference.setStoredPreference(getActivity(), SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(), startDateNew);
                    mSharedPreference.setStoredPreference(getActivity(), SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(), periodLength);
                    mSharedPreference.setStoredPreference(getActivity(), SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), cycleLength);

                    PeriodTrackerRequest request = new PeriodTrackerRequest();
                    try {
                        request.setCycleLenght(mSharedPreference.getStoredPreference(getActivity(), SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey()));
                        request.setLog(DayLogUtils.getInstance().getAsLog());
                        request.setLutealLength("14");
                        request.setPeriodLength(mSharedPreference.getStoredPreference(getActivity(), SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey()));
                        request.setPeriodDate(mSharedPreference.getStoredPreference(getActivity(), SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey()));

                    } catch (Exception e) {
                        CommonUtils.printStackTrace(e);
                    }
                    periodTrackerViewModel.sendPeriodTrackerData(request);

                    HashMap<String, Object> payload = new HashMap<>();
                    payload.put(AppConstants.PERIOD_DATE, request.getPeriodDate());
                    payload.put(AppConstants.PERIOD_LENGTH, request.getPeriodLength());
                    payload.put(AppConstants.LUTEAL_LENGTH, request.getLutealLength());
                    payload.put(AppConstants.CYCLE_LENGTH, request.getCycleLenght());
                    Utility.logNetcoreEvent(getActivity(), payload, AppConstants.PERIOD_TRACKER_UPDATE_CLICK);

                  //  setNotificationRemainder();
                    setLogicForCalender();
                }
            }

            else{


            //Find Ovulation Day
            Calendar ovulationCal = Calendar.getInstance();
            ovulationCal.setTime(cycleEndPeriod);
            ovulationCal.add(Calendar.DATE, -13);
            Date ovulationPeriodDate = ovulationCal.getTime();
            String ovulationDate = sdf.format(ovulationPeriodDate);
            Log.d("Aarati calender", ovulationDate + " ovulationDate ");

            //need ovulationEndCalender to get ovulation Day
            Calendar ovulationCalEnd = Calendar.getInstance();
            ovulationCalEnd.setTime(cycleEndPeriod);
            ovulationCalEnd.add(Calendar.DATE, -13);
            Date ovulationPeriodDateEnd = ovulationCalEnd.getTime();
            String ovulationDateEnd = sdf.format(ovulationPeriodDateEnd);
            Log.d("Aarati calender", ovulationDateEnd + " ovulationDateEnd ");


            //need ovulationStartCalender to get ovulation Day
            Calendar ovulationCalStart = Calendar.getInstance();
            ovulationCalStart.setTime(cycleEndPeriod);
            ovulationCalStart.add(Calendar.DATE, -13);
            Date ovulationPeriodDateStart = ovulationCalStart.getTime();
            String ovulationDateStart = sdf.format(ovulationPeriodDateStart);
            Log.d("Aarati calender", ovulationDateStart + " ovulationDateStart ");


            //Find High Fertility Days
            Calendar highFertilityStart = Calendar.getInstance();
            Calendar highFertilityEnd = Calendar.getInstance();
            highFertilityStart.setTime(ovulationPeriodDate);
            highFertilityEnd.setTime(ovulationPeriodDate);
            highFertilityStart.add(Calendar.DATE, -6);
            highFertilityEnd.add(Calendar.DATE, 4);
            Date highFertilityStartDate = highFertilityStart.getTime();
            Date highFertilityEndDate = highFertilityEnd.getTime();
            String highFertilityStartDateString = sdf.format(highFertilityStartDate);
            String highFertilityEndDateString = sdf.format(highFertilityEndDate);
            Log.d("Aarati calender", highFertilityStartDateString + " highFertilityStartDateString ");
            Log.d("Aarati calender", highFertilityEndDateString + " highFertilityEndDateString ");

            //add values to arrays of respective types
            Calendar setAllDays = Calendar.getInstance();
            setAllDays.setTime(startPeriod);
            // Value changing for while loop
            startPeriodDate.add(Calendar.DATE, -1);
            endPeriodDate.add(Calendar.DATE, 1);
            highFertilityStart.add(Calendar.DATE, -1);
            highFertilityEnd.add(Calendar.DATE, 1);
            ovulationCalEnd.add(Calendar.DATE, -1);

                lastPeriodDate = startDate;
                LocalDate startDateN = convertToLocalDate(startPeriod);
                startDateN = startDateN.plusDays(Long.parseLong(cycleLength));
                Date nextDate = convertToDate(startDateN);
                nextPeriodDate = sdf.format(nextDate);
                notificationCycleLength = Integer.valueOf(cycleLength);


            while (!setAllDays.after(cyclePeriodDate)) {

                long days = ChronoUnit.DAYS.between(convertToLocalDate(startPeriod), convertToLocalDate(currentDate));
                periodCalendarView.setPeriodDays((int) days + 1);

                if (setAllDays.after(startPeriodDate) && setAllDays.before(endPeriodDate)) {
                    menstruationDays.add(setAllDays.get(Calendar.DATE));
                    if (Calendar.getInstance().get(Calendar.DATE) == setAllDays.get(Calendar.DATE) && Calendar.getInstance().get(Calendar.MONTH) == setAllDays.get(Calendar.MONTH)
                            && Calendar.getInstance().get(Calendar.YEAR) == setAllDays.get(Calendar.YEAR)) {
                        periodCalendarView.setPeriodType(PERIOD_TYPES.MENSTRUATION);
                    }
                    Log.d("Aarati Date Type", periodCalendarView.getPeriodType().toString() + " " + Calendar.getInstance().get(Calendar.DATE) + " " + Calendar.getInstance().get(Calendar.MONTH));

                } else if (setAllDays.equals(ovulationCal)) {
                    ovulationDays.add(setAllDays.get(Calendar.DATE));
                    if (Calendar.getInstance().get(Calendar.DATE) == setAllDays.get(Calendar.DATE) && Calendar.getInstance().get(Calendar.MONTH) == setAllDays.get(Calendar.MONTH)
                            && Calendar.getInstance().get(Calendar.YEAR) == setAllDays.get(Calendar.YEAR)) {
                        periodCalendarView.setPeriodType(PERIOD_TYPES.OVULATION);
                    }
                    // ovulationDay = setAllDays.get(Calendar.DATE);

                } else if (setAllDays.after(highFertilityStart) && setAllDays.before(ovulationCal)) {
                    highFertilityDays.add(setAllDays.get(Calendar.DATE));
                    if (Calendar.getInstance().get(Calendar.DATE) == setAllDays.get(Calendar.DATE) && Calendar.getInstance().get(Calendar.MONTH) == setAllDays.get(Calendar.MONTH)
                            && Calendar.getInstance().get(Calendar.YEAR) == setAllDays.get(Calendar.YEAR)) {
                        periodCalendarView.setPeriodType(PERIOD_TYPES.PREGNANCY);
                    }

                } else if (setAllDays.after(ovulationCalEnd) && setAllDays.before(highFertilityEnd)) {
                    highFertilityDays.add(setAllDays.get(Calendar.DATE));
                    if (Calendar.getInstance().get(Calendar.DATE) == setAllDays.get(Calendar.DATE) && Calendar.getInstance().get(Calendar.MONTH) == setAllDays.get(Calendar.MONTH)
                            && Calendar.getInstance().get(Calendar.YEAR) == setAllDays.get(Calendar.YEAR)) {
                        periodCalendarView.setPeriodType(PERIOD_TYPES.PREGNANCY);
                    }

                } else {
                    if (Calendar.getInstance().get(Calendar.DATE) == setAllDays.get(Calendar.DATE) && Calendar.getInstance().get(Calendar.MONTH) == setAllDays.get(Calendar.MONTH)
                            && Calendar.getInstance().get(Calendar.YEAR) == setAllDays.get(Calendar.YEAR)) {
                        periodCalendarView.setPeriodType(PERIOD_TYPES.PERIOD);
                    }
                }
                setAllDays.add(Calendar.DATE, 1);

            }
            for (int i = 0; i < highFertilityDays.size(); i++) {
                Log.d("Aarati calender high fertility", "high fertility Day " + highFertilityDays.get(i).toString());


            }
            for (int i = 0; i < ovulationDays.size(); i++) {
                Log.d("Aarati calender ovulation", "ovulation day " + ovulationDays.get(0));

            }
            for (int i = 0; i < menstruationDays.size(); i++) {
                Log.d("Aarati calender menstruation", "menstruation day " + menstruationDays.get(i).toString());


            }


            periodCalendarView.setPeriodCycle(menstruationDays, ovulationDays, highFertilityDays);
            Calendar c = Calendar.getInstance();
            currentYear = c.get(Calendar.YEAR);
            currentMonth = c.get(Calendar.MONTH);
            currentDay = c.get(Calendar.DATE);
            periodCalendarView.setCalendar(currentDay, currentMonth, currentYear);
            periodCalendarView.setPeriodCalendarViewListener(new PeriodCalendarViewListener() {
                @Override
                public void onEdit() {
                    startActivityForResult(new Intent(requireActivity(), EditCycleActivity.class),13);
                }
            });

            String tvText =  "You are currently in <b>" + periodCalendarView.getPeriodType().toString() +"</b> phase." + "<br>Your last period date was " + "<b>" + lastPeriodDate + "</b>" + " and next period cycle starting on " + "<b>" + nextPeriodDate + "</b>.";
            tvPeriodInformation.setText(Html.fromHtml(tvText));
          //  setNotificationRemainder();
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 13 && resultCode == Activity.RESULT_OK) {
            // Handle the data from the new activity
            // For example, refresh the fragment
            refreshFragment();
        }
    }

    public void refreshFragment() {
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
        initView();
        setLiveData();
        setLogicForCalender();
    }

    public static LocalDate convertToLocalDate(@NonNull Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static Date convertToDate(@NonNull LocalDate localDate) {

        LocalDateTime localDateTime = localDate.atStartOfDay();
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
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
           *//* calendarBefore2Days.set(Calendar.HOUR_OF_DAY, 10);
            calendarBefore2Days.set(Calendar.MINUTE, 0);
            calendarBefore2Days.set(Calendar.SECOND, 0);
            calendarBefore2Days.set(Calendar.MILLISECOND, 0);*//*
            //            calendarBefore2Days.set(Calendar.HOUR_OF_DAY, 9);
//            calendarBefore2Days.set(Calendar.MINUTE, 0);
//            calendarBefore2Days.set(Calendar.SECOND, 0);
//            calendarBefore2Days.set(Calendar.MILLISECOND, 0);

            Logger.i("EditCycleActivity", "Before 2 days : " + calendarBefore2Days.getTime().toString());


            Calendar calendarBefore7Days = Calendar.getInstance();
            calendarBefore7Days.setTime(date);
            calendarBefore7Days.add(Calendar.DAY_OF_MONTH, notificationCycleLength - 7);
           *//* calendarBefore7Days.set(Calendar.HOUR_OF_DAY, 6);
            calendarBefore7Days.set(Calendar.MINUTE, 17);
            calendarBefore7Days.set(Calendar.SECOND, 0);
            calendarBefore7Days.set(Calendar.MILLISECOND, 0);*//*
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
    private void showNewDateDialog(String oldDate , String newDate , String cycleLengthGiven , String periodLengthGiven) {
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

                mSharedPreference.setStoredPreference(getActivity(), SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey(), newDate);
                mSharedPreference.setStoredPreference(getActivity(), SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey(), periodLengthGiven);
                mSharedPreference.setStoredPreference(getActivity(), SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey(), cycleLengthGiven);

                PeriodTrackerRequest request = new PeriodTrackerRequest();
                try {
                    request.setCycleLenght(mSharedPreference.getStoredPreference(getActivity(), SharedPreferencesEnum.PERIOD_CYCLE_LENGTH.getPreferenceKey()));
                    request.setLog(DayLogUtils.getInstance().getAsLog());
                    request.setLutealLength("14");
                    request.setPeriodLength(mSharedPreference.getStoredPreference(getActivity(), SharedPreferencesEnum.PERIOD_LENGTH.getPreferenceKey()));
                    request.setPeriodDate(mSharedPreference.getStoredPreference(getActivity(), SharedPreferencesEnum.PERIOD_STARTING_DATE.getPreferenceKey()));

                } catch (Exception e) {
                    CommonUtils.printStackTrace(e);
                }
                periodTrackerViewModel.sendPeriodTrackerData(request);

                HashMap<String, Object> payload = new HashMap<>();
                payload.put(AppConstants.PERIOD_DATE, request.getPeriodDate());
                payload.put(AppConstants.PERIOD_LENGTH, request.getPeriodLength());
                payload.put(AppConstants.LUTEAL_LENGTH, request.getLutealLength());
                payload.put(AppConstants.CYCLE_LENGTH, request.getCycleLenght());
                Utility.logNetcoreEvent(getActivity(), payload, AppConstants.PERIOD_TRACKER_UPDATE_CLICK);

               // setNotificationRemainder();
                setLogicForCalender();
              /*  alertDialog.dismiss();
            });
            tvLogout.setOnClickListener(v -> {
                startActivity(new Intent(requireActivity(), EditCycleActivity.class));
                alertDialog.dismiss();
            });
            alertDialog.show();*/

        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int calculatePreviousMonthDifference(LocalDate beforeDate, LocalDate currentDate) {


        if (beforeDate.getYear() == currentDate.getYear()) {
            // Calculate difference in months if years are the same
            long monthsDifference = ChronoUnit.MONTHS.between(
                    LocalDate.of(beforeDate.getYear(), beforeDate.getMonthValue(), 1),
                    LocalDate.of(currentDate.getYear(), currentDate.getMonthValue(), 1)
            );
            previousMonthCount = (int) monthsDifference;
        } else {
            // Check if the year is before or after and calculate accordingly
            int yearsDifference = currentDate.getYear() - beforeDate.getYear();
            int monthsInYears = yearsDifference * 12;

            // Calculate difference in months considering the year difference
            long monthsDifference = ChronoUnit.MONTHS.between(
                    LocalDate.of(beforeDate.getYear(), beforeDate.getMonthValue(), 1),
                    LocalDate.of(currentDate.getYear(), currentDate.getMonthValue(), 1)
            );
            previousMonthCount = (int) monthsDifference - monthsInYears;
        }

        Log.d("Start Date is calculated", String.valueOf(previousMonthCount));
        return previousMonthCount;
    }

    @SuppressLint("ScheduleExactAlarm")
    private void setNotificationRemainder() {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            Date date = formatter.parse(nextPeriodDate);
            date.setMinutes(new Date().getMinutes() + 2);
            date.setHours(new Date().getHours());


            // Create Calendar instances for n days and n-7 days
            Calendar calendarBefore2Days = Calendar.getInstance();
            calendarBefore2Days.setTime(date);
            calendarBefore2Days.add(Calendar.DAY_OF_MONTH, notificationCycleLength - 2);
          /*  calendarBefore2Days.set(Calendar.HOUR_OF_DAY, 10);
            calendarBefore2Days.set(Calendar.MINUTE, 0);
            calendarBefore2Days.set(Calendar.SECOND, 0);
            calendarBefore2Days.set(Calendar.MILLISECOND, 0);*/
            //            calendarBefore2Days.set(Calendar.HOUR_OF_DAY, 9);
//            calendarBefore2Days.set(Calendar.MINUTE, 0);
//            calendarBefore2Days.set(Calendar.SECOND, 0);
//            calendarBefore2Days.set(Calendar.MILLISECOND, 0);

            Logger.i("EditCycleActivity", "Before 2 days : " + calendarBefore2Days.getTime().toString());


            Calendar calendarBefore7Days = Calendar.getInstance();
            calendarBefore7Days.setTime(date);
            calendarBefore7Days.add(Calendar.DAY_OF_MONTH, notificationCycleLength - 7);
          /*  calendarBefore7Days.set(Calendar.HOUR_OF_DAY, 6);
            calendarBefore7Days.set(Calendar.MINUTE, 17);
            calendarBefore7Days.set(Calendar.SECOND, 0);
            calendarBefore7Days.set(Calendar.MILLISECOND, 0);*/
            //            calendarBefore7Days.set(Calendar.HOUR_OF_DAY, 9);
//            calendarBefore7Days.set(Calendar.MINUTE, 0);
//            calendarBefore7Days.set(Calendar.SECOND, 0);
//            calendarBefore7Days.set(Calendar.MILLISECOND, 0);
            Logger.i("EditCycleActivity ", "Before 7 days : " + calendarBefore7Days.getTime().toString());

            // Schedule notification for 2 days before
            AlarmManager alarmManager = (AlarmManager) getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            // Create an Intent for the BroadcastReceiver
            Intent notificationIntent = new Intent(getActivity().getApplicationContext(), PeriodAlarmReceiver.class);
            notificationIntent.setAction("in.woloo.www.ACTION_TRIGGER_NOTIFICATION");
            notificationIntent.putExtra("message", "2 days until next Period.");

            // Create a PendingIntent for the BroadcastReceiver
            int requestCode = (int) System.currentTimeMillis(); // Unique request code
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), requestCode, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
            Logger.i("EditCycleActivity PTF 21", "pendingIntent called");
            // Set the alarm using AlarmManager
            if (alarmManager != null) {

                Logger.i("EditCycleActivity PTF 21", "Before 2 days : " + calendarBefore2Days.getTimeInMillis());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarBefore2Days.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendarBefore2Days.getTimeInMillis(), pendingIntent);
                }

            }
            else
            {
                Logger.i("EditCycleActivity PTF 2", "Before 2 days : " );
            }


            // Schedule notification for 7 days before

            AlarmManager alarmManager2 = (AlarmManager) getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);

            // Create an Intent for the BroadcastReceiver
            Intent notificationIntent2 = new Intent(getActivity().getApplicationContext(), Period7DaysAlarmReceiver.class);
            notificationIntent2.setAction("in.woloo.www.ACTION_TRIGGER_NOTIFICATION");
            notificationIntent2.putExtra("message", "7 days until next Period.");

            // Create a PendingIntent for the BroadcastReceiver
            int requestCode2 = (int) System.currentTimeMillis(); // Unique request code
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(getActivity().getApplicationContext(), requestCode2, notificationIntent2,  PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
            Logger.i("EditCycleActivity PTF 1", "pendingIntent2 called");
            // Set the alarm using AlarmManager
            if (alarmManager2 != null) {
                // alarmManager2.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarBefore7Days.getTimeInMillis(), pendingIntent2);
                Logger.i("EditCycleActivity PTF 1", "Before 7 days : " + calendarBefore7Days.getTimeInMillis());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager2.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendarBefore7Days.getTimeInMillis(), pendingIntent2);
                } else {
                    alarmManager2.setExact(AlarmManager.RTC_WAKEUP, calendarBefore7Days.getTimeInMillis(), pendingIntent2);
                }

            }
            else
            {
                Logger.i("EditCycleActivity PTF", "Before 7 days : " );
            }


        }catch (Exception e){


            e.printStackTrace();
            Logger.e("EditCycleActivity PTF", "Error setting notification", e);

        }

    }





}
