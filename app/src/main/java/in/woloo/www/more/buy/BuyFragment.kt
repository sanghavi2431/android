package `in`.woloo.www.more.buy

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.Logger

class BuyFragment : Fragment() {
    private var buyViewModel: BuyViewModel? = null

    @JvmField
    @BindView(R.id.ivClassic)
    var ivClassic: ImageView? = null

    @JvmField
    @BindView(R.id.ivSilver)
    var ivSilver: ImageView? = null

    @JvmField
    @BindView(R.id.ivGold)
    var ivGold: ImageView? = null

    @JvmField
    @BindView(R.id.ivElite)
    var ivElite: ImageView? = null

    /*calling  onCreateView*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        Logger.i(TAG, "onCreateView")
        buyViewModel =
            ViewModelProvider(this).get(BuyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_buy, container, false)
        ButterKnife.bind(this, root)
        initView()
        buyViewModel!!.text.observe(
            viewLifecycleOwner
        ) { }
        return root
    }

    /*calling  initView*/
    private fun initView() {
        try {
            Logger.i(TAG, "initView")
            ivClassic!!.setOnClickListener { v: View? ->
                showSubscriptionDialog(getString(R.string.subscription_dialog_msg))
            }
            ivSilver!!.setOnClickListener { v: View? ->
                showSubscriptionDialog(getString(R.string.subscription_dialog_msg))
            }
            ivGold!!.setOnClickListener { v: View? ->
                showSubscriptionDialog(getString(R.string.subscription_dialog_msg))
            }
            ivElite!!.setOnClickListener { v: View? ->
                showSubscriptionDialog(getString(R.string.subscription_dialog_msg))
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling  showSubscriptionDialog*/
    private fun showSubscriptionDialog(msg: String) {
        try {
            Logger.i(TAG, "showSubscriptionDialog")
            val dialog = Dialog(requireActivity())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.subcription_dialog)

            val text = dialog.findViewById<View>(R.id.text_dialog) as TextView
            text.text = msg

            val dialogButton = dialog.findViewById<View>(R.id.btn_dialog) as Button
            dialogButton.setOnClickListener { dialog.dismiss() }

            dialog.show()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    companion object {
        var TAG: String = BuyFragment::class.java.simpleName
    }
}
