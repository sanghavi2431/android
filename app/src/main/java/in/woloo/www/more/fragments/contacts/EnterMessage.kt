package `in`.woloo.www.more.fragments.contacts

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.fragments.EnterMessageFragment
import `in`.woloo.www.utils.AppConstants

class EnterMessage : AppCompatActivity() {
    @JvmField
    @BindView(R.id.flFragments)
    var flFragments: FrameLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.entermessage)
        ButterKnife.bind(this)
        initViews()
    }

    private fun initViews() {
        try {
            loadFragment(
                EnterMessageFragment.newInstance(
                    "" + intent.getStringExtra("mobilenumber"),
                    "" + intent.getStringExtra("name"),
                    "" + intent.getStringExtra(
                        AppConstants.REFCODE
                    )
                )
            )
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    fun loadFragment(fragment: Fragment) {
        try {
            val fragmentManager = (this as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.flFragments, fragment)
            fragmentTransaction.commit()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }
}