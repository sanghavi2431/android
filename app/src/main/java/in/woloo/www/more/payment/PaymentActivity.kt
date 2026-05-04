package `in`.woloo.www.more.payment

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.payment.fragments.PaymentFragment
import `in`.woloo.www.utils.Logger.i

class PaymentActivity : AppCompatActivity() {

    @JvmField
    @BindView(R.id.flFragments)
    var flFragments: FrameLayout? = null

    /*calling on onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        ButterKnife.bind(this)
        initViews()
        i(TAG, "onCreate")
    }

    /*calling on initViews*/
    private fun initViews() {
        i(TAG, "initViews")
        try {
            loadFragment(
                PaymentFragment.newInstance(
                    "",
                    ""
                )
            ) //PaymentFragment()  HomeCategoryFragment
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling on loadFragment*/
    fun loadFragment(fragment: Fragment) {
        try {
            i(TAG, "loadFragment")
            val fragmentManager = (this as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.flFragments, fragment)
            fragmentTransaction.commit()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    companion object {
        var TAG: String = PaymentActivity::class.java.simpleName
    }
}