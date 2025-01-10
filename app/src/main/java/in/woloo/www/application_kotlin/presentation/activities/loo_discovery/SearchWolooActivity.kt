package `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.presentation.fragments.WolooSearchFragment
import `in`.woloo.www.utils.Logger

class SearchWolooActivity : AppCompatActivity() {
    @JvmField
    @BindView(R.id.frmFragment)
    var frmFragment: FrameLayout? = null

    /*calling on onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_woloo)
        ButterKnife.bind(this)
        initViews()
        Logger.i(TAG, "onCreate")
    }

    /*calling on initViews*/
    private fun initViews() {
        Logger.i(TAG, "initViews")
        try {
            if (intent != null && intent.getBooleanExtra(ARG_SHOW_OFFERS, false)) {
                loadFragment(WolooSearchFragment.newInstance(true))
            } else {
                loadFragment(WolooSearchFragment.newInstance(false))
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling on loadFragment*/
    fun loadFragment(fragment: Fragment?) {
        try {
            Logger.i(TAG, "loadFragment")
            val fragmentManager = (this as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frmFragment, fragment!!)
            fragmentTransaction.commit()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    companion object {
        const val ARG_SHOW_OFFERS = "ARG_SHOW_OFFERS"
        var TAG = SearchWolooActivity::class.java.simpleName
    }
}