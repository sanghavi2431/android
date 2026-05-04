package `in`.woloo.www.more.woloo_host

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.Logger

class CreateWolooHostActivity : AppCompatActivity() {
    @JvmField
    @BindView(R.id.flFragments)
    var flFragments: FrameLayout? = null

    /*calling on onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_woloo_host)
        ButterKnife.bind(this)
        initViews()
        Logger.i(TAG, "onCreate")
    }

    /*calling on initViews*/
    private fun initViews() {
        try {
            Logger.i(TAG, "initViews")
            loadFragment(CreateWolooHostFragment.newInstance("", ""))
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
            fragmentTransaction.replace(R.id.flFragments, fragment!!)
            fragmentTransaction.commit()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    companion object {
        var TAG = CreateWolooHostActivity::class.java.simpleName
    }
}