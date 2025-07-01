package in.woloo.www.boarding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.dailylogscreen.DailyLogsScreenActivity;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.interestedtopic.InterestedTopicsActivity;
import in.woloo.www.login.SplashActivity;
import in.woloo.www.utils.Logger;
import in.woloo.www.v2.data.local.SharedPrefSettings;
import in.woloo.www.v2.login.activity.LoginActivity;

public class BoardingActivity extends AppCompatActivity {
    private static final String TAG = BoardingActivity.class.getSimpleName();

    @BindView(R.id.dotSliderLayout)
    LinearLayout dotSliderLayout;

    @BindView(R.id.boardingViewPager)
    ViewPager boardingViewPager;

    @BindView(R.id.skipTextSlider)
    TextView skipTextSlider;

    @BindView(R.id.boardingActivityAuthenticate)
    TextView authenticateButton;

    @BindView(R.id.nextSliderButton)
    ImageButton nextSliderButton;

    TextView[] sliderDot;
    private int[] layouts;
    private BoardingViewPagerAdapter boardingViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boarding);
        Logger.i(TAG, "onCreate");
        ButterKnife.bind(this);

        layouts = new int[]{
                R.layout.boarding_slider_one,
                R.layout.boarding_slider_two,
                R.layout.boarding_slider_three};
        topDotActiveInactive(0);
        boardingViewPagerAdapter = new BoardingViewPagerAdapter();
        boardingViewPager.setAdapter(boardingViewPagerAdapter);
        boardingViewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        SharedPrefSettings.Companion.getGetPreferences().storeIsShownOnBoarding(true);

        skipTextSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardingViewPager.setCurrentItem(2);
            }
        });

        nextSliderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int current = getItem(+1);
                if (current < layouts.length) {
                    boardingViewPager.setCurrentItem(current);
                } else {
                    topDotActiveInactive(current);
                }
            }
        });


        authenticateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*startActivity(new Intent(BoardingActivity.this, DailyLogsScreenActivity.class));
                finish();*/
                //startActivity(new Intent(BoardingActivity.this, LoginActivity.class));
                startActivity(new Intent(BoardingActivity.this, LoginActivity.class));
                finish();
            }
        });

    }

    private int getItem(int i) {
        return boardingViewPager.getCurrentItem() + i;
    }

    private void topDotActiveInactive(int currentSlide) {
        sliderDot = new TextView[layouts.length];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotSliderLayout.removeAllViews();
        for (int i = 0; i < sliderDot.length; i++) {
            sliderDot[i] = new TextView(this);
            sliderDot[i].setText(Html.fromHtml("&#8226;"));
            sliderDot[i].setTextSize(35);
            sliderDot[i].setTextColor(colorsInactive[currentSlide]);
            dotSliderLayout.addView(sliderDot[i]);
        }

        if (sliderDot.length > 0)
            sliderDot[currentSlide].setTextColor(colorsActive[currentSlide]);
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            topDotActiveInactive(position);
            if (position == layouts.length - 1) {
                skipTextSlider.setVisibility(View.GONE);
                nextSliderButton.setVisibility(View.GONE);
                authenticateButton.setVisibility(View.VISIBLE);

            } else {
                skipTextSlider.setVisibility(View.VISIBLE);
                nextSliderButton.setVisibility(View.VISIBLE);
                authenticateButton.setVisibility(View.GONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public class BoardingViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public BoardingViewPagerAdapter() {
        }

        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}