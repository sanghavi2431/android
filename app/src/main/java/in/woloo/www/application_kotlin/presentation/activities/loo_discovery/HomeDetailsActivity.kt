package `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.presentation.fragments.loo_discovery.HomeDetailsFragment
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger

class HomeDetailsActivity : AppCompatActivity() {
    private var fromSearch = false

    /*calling onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_details)
        ButterKnife.bind(this)
        initViews()
        Logger.i(TAG, "onCreate")
    }

    /*calling initViews*/
    private fun initViews() {
        Logger.i(TAG, "initViews")
        try {
            fromSearch = intent.getBooleanExtra(AppConstants.FROM_SEARCH, false)
            loadFragment(HomeDetailsFragment.newInstance(fromSearch))
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling loadFragment*/
    fun loadFragment(fragment: Fragment?) {
        try {
            Logger.i(TAG, "loadFragment")
            val fragmentManager = (this as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.flFragments, fragment!!)
            fragmentTransaction.commit()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    companion object {
        var TAG = HomeDetailsActivity::class.java.simpleName
    }
}