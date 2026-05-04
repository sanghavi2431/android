package `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.presentation.fragments.loo_discovery.ViewReviewFragment
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.AppConstants

class ViewReviewActivity : AppCompatActivity() {
    @BindView(R.id.flFragments)
    lateinit var flFragments: FrameLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_review)
        ButterKnife.bind(this)
        initViews()
    }

    private fun initViews() {
        try {
            val review = intent.getStringExtra(AppConstants.REVIEW)
            loadFragment(ViewReviewFragment.newInstance(review, ""))
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    fun loadFragment(fragment: Fragment?) {
        try {
            val fragmentManager = (this as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.flFragments, fragment!!)
            fragmentTransaction.commit()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }
}