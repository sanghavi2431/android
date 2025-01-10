package `in`.woloo.www.more.subscribe

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.subscribe.fragments.SubscribeFragment
import `in`.woloo.www.utils.Logger

class SubscribeActivity : AppCompatActivity() {
    @JvmField
    @BindView(R.id.flFragments)
    var flFragments: FrameLayout? = null

    /*calling on onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscribe)
        ButterKnife.bind(this)
        initViews()
        Logger.i(TAG, "loadFragment")
    }

    /*calling on initViews*/
    private fun initViews() {
        Logger.i(TAG, "initViews")
        try {
            loadFragment(SubscribeFragment.newInstance("", "", false, "", true, false, ""))
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
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.flFragments, fragment!!)
            fragmentTransaction.commit()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    companion object {
        var TAG = SubscribeActivity::class.java.simpleName
    }
}