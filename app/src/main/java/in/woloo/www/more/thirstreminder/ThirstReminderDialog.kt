package `in`.woloo.www.more.thirstreminder

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import `in`.woloo.www.R

class ThirstReminderDialog : DialogFragment() {
    private var callback: ThirstCallbacks? = null

    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity is ThirstCallbacks) {
            callback = activity as ThirstCallbacks?
        }
        setStyle(STYLE_NORMAL, R.style.DialogFragmentAnimation)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_ask_thirstreminder, container)
        ButterKnife.bind(this, view)
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
            val params = dialog!!.window!!.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog!!.window!!.setLayout(params.width, params.height)
        }
        ivBack!!.setOnClickListener { dismiss() }
        return view
    }

    @OnClick(R.id.tvYes)
    fun onClickYes() {
        if (callback != null) {
            callback!!.onClickThirstYes()
        }
        dismiss()
    }

    @OnClick(R.id.tvNo)
    fun onClickNo() {
        if (callback != null) {
            callback!!.onClickThirstNo()
        }
        dismiss()
    }

    @OnClick(R.id.ivClose)
    fun onClickClose() {
        dismiss()
    }

    interface ThirstCallbacks {
        fun onClickThirstYes()
        fun onClickThirstNo()
    }

    override fun onStart() {
        super.onStart()
        // Set dialog fragment dimensions to full screen
        if (dialog != null) {
            dialog!!.window!!.setLayout(
                (resources.displayMetrics.widthPixels * 0.8).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // Handle dialog dismissal here if needed
    }

    companion object {
        const val TAG = "ThirstReminderDialog"
    }
}
