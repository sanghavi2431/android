package `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.presentation.fragments.loo_discovery.AddReviewsFragment
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.AppConstants

class AddReviewActivity : AppCompatActivity() {
    @JvmField
    @BindView(R.id.flFragments)
    var flFragments: FrameLayout? = null
    var hasReachedAtDestination = false
    private var wolooId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)
        ButterKnife.bind(this)
        handleDeepLink(intent)
        initViews()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleDeepLink(intent)
        Log.d("DeepLinkTest", "onNewIntent triggered")
    }

    private fun initViews() {
        try {
            hasReachedAtDestination = false
            val isDeepLinkProcessed = handleDeepLink(intent)
            if (!isDeepLinkProcessed) {
                wolooId = intent.getIntExtra(AppConstants.WOLOO_ID, 0)
            }
            loadFragment(AddReviewsFragment.newInstance(wolooId, ""))
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

    private fun handleDeepLink(intent: Intent?): Boolean {
        if (intent != null && intent.data != null) {
            val deepLink = intent.data
            if (("https" == deepLink!!.scheme || "http" == deepLink.scheme) && "app.woloo.in" == deepLink.host || "/woloo_feedback" == deepLink.path) {
                val wolooIdString = deepLink.getQueryParameter("wolooId")
                if (wolooIdString != null) {
                    try {
                        wolooId = wolooIdString.toInt()
                        loadFragment(AddReviewsFragment.newInstance(wolooId, ""))
                        // Successfully processed the deep link
                        return true
                    } catch (e: NumberFormatException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return false
    }
}