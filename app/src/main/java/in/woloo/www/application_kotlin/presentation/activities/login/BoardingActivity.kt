package `in`.woloo.www.application_kotlin.presentation.activities.login

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import `in`.woloo.www.R
import `in`.woloo.www.databinding.ActivityBoardingBinding
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings.Companion.getPreferences

class BoardingActivity : AppCompatActivity() {
    private lateinit var sliderDot: Array<TextView?>
    private lateinit var layouts: IntArray
    private lateinit var boardingViewPagerAdapter: BoardingViewPagerAdapter
    lateinit var binding : ActivityBoardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Logger.i(TAG, "onCreate")
        layouts = intArrayOf(
            R.layout.boarding_slider_one,
            R.layout.boarding_slider_two,
            R.layout.boarding_slider_three
        )
        topDotActiveInactive(0)
        boardingViewPagerAdapter = BoardingViewPagerAdapter()
        binding.boardingViewPager.adapter = boardingViewPagerAdapter
         binding.boardingViewPager.addOnPageChangeListener(viewPagerPageChangeListener)
        getPreferences.storeIsShownOnBoarding(true)
        binding.skipTextSlider.setOnClickListener {  binding.boardingViewPager.currentItem = 2 }
         binding.nextSliderButton.setOnClickListener {
            val current = getItem(+1)
            if (current < layouts.size) {
                 binding.boardingViewPager.currentItem = current
            } else {
                topDotActiveInactive(current)
            }
        }
         binding.boardingActivityAuthenticate.setOnClickListener {
             binding.boardingActivityAuthenticate.background = ContextCompat.getDrawable(applicationContext , R.drawable.new_button_onclick_background)
            startActivity(Intent(this@BoardingActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun getItem(i: Int): Int {
        return  binding.boardingViewPager.currentItem + i
    }

    private fun topDotActiveInactive(currentSlide: Int) {
        sliderDot = arrayOfNulls(layouts.size)
        val colorsActive = resources.getIntArray(R.array.array_dot_active)
        val colorsInactive = resources.getIntArray(R.array.array_dot_inactive)
        binding.dotSliderLayout.removeAllViews()
        for (i in sliderDot.indices) {
            sliderDot[i] = TextView(this)
            sliderDot[i]!!.text = Html.fromHtml("&#8226;",Html.FROM_HTML_MODE_COMPACT)
            sliderDot[i]!!.textSize = 35f
            sliderDot[i]!!.setTextColor(colorsInactive[currentSlide])
            binding.dotSliderLayout.addView(sliderDot[i])
        }
        if (sliderDot.isNotEmpty()) sliderDot[currentSlide]!!.setTextColor(colorsActive[currentSlide])
    }

    private var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            topDotActiveInactive(position)
            if (position == layouts.size - 1) {
                 binding.skipTextSlider.visibility = View.GONE
                 binding.nextSliderButton.visibility = View.GONE
                 binding.boardingActivityAuthenticate.visibility = View.VISIBLE
            } else {
                 binding.skipTextSlider.visibility = View.VISIBLE
                 binding.nextSliderButton.visibility = View.VISIBLE
                 binding.boardingActivityAuthenticate.visibility = View.GONE
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    inner class BoardingViewPagerAdapter : PagerAdapter() {
        private var layoutInflater: LayoutInflater? = null
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            layoutInflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater!!.inflate(layouts[position], container, false)
            container.addView(view)
            return view
        }

        override fun getCount(): Int {
            return layouts.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View
            container.removeView(view)
        }
    }

    companion object {
        private val TAG = BoardingActivity::class.java.simpleName
    }
}