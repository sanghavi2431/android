package `in`.woloo.www.more.thirstreminder

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTextChanged
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.Logger

class SaveThirstReminderDialog : DialogFragment {
    private var callback: SaveThirstCallbacks? = null
    private var hours: Int

    @JvmField
    @BindView(R.id.tvThirstTimerMessage)
    var tvThirstTimerMessage: TextView? = null

    @JvmField
    @BindView(R.id.etThirstFrequency)
    var etThirstFrequency: EditText? = null

    @JvmField
    @BindView(R.id.cbCancelReminder)
    var cbCancelReminder: CheckBox? = null

    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: LinearLayout? = null

    constructor(hours: Int) {
        this.hours = hours
    }

    constructor() {
        hours = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity is SaveThirstCallbacks) {
            callback = activity as SaveThirstCallbacks?
        }
        setStyle(STYLE_NORMAL, R.style.DialogFragmentAnimation)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_set_thirstreminder, container)
        ButterKnife.bind(this, view)
        etThirstFrequency!!.setText(hours.toString())
        // Set transparent background and no title
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        ivBack!!.setOnClickListener { dismiss() }
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    @OnClick(R.id.tvSave)
    fun onClickSave() {
        if (callback != null) {
            if (cbCancelReminder!!.isChecked) {
                hours = 0
            }
            callback!!.onClickSaveThirstReminder(hours)
        }
        dismiss()
    }

    @OnClick(R.id.ivClose)
    fun onClickClose() {
        dismiss()
    }

    @OnTextChanged(
        value = [R.id.etThirstFrequency],
        callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED
    )
    fun onFrequencyChanged(text: CharSequence?) {
        Logger.i(TAG, text.toString())
        try {
            hours = if (text == null || TextUtils.isEmpty(text)) 0 else text.toString().toInt()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
        tvThirstTimerMessage!!.text = getString(R.string.thirst_reminder_confirmation_value, hours)
    }

    interface SaveThirstCallbacks {
        fun onClickSaveThirstReminder(hours: Int) //void onClickThirstNo();
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
        const val TAG = "SaveThirstReminderDialog"
    }
}
