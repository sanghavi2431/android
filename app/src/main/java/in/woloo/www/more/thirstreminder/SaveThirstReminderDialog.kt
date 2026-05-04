package `in`.woloo.www.more.thirstreminder

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTextChanged
import com.bumptech.glide.Glide
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.model.CreditCoinsRequest
import `in`.woloo.www.application_kotlin.view_models.WolooViewModel
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent
import org.json.JSONObject

class SaveThirstReminderDialog : DialogFragment {
    private var callback: SaveThirstCallbacks? = null
    private var hours: Int
    private var wolooViewModel: WolooViewModel? = null

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
        wolooViewModel = ViewModelProvider(this).get<WolooViewModel>(
            WolooViewModel::class.java
        )
        ButterKnife.bind(this, view)
        etThirstFrequency!!.setText(hours.toString())
        // Set transparent background and no title
        if (dialog != null && dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        }
        ivBack!!.setOnClickListener { dismiss() }

        wolooViewModel?.observeAddCoinstoWolooUser()
            ?.observe(viewLifecycleOwner, Observer<BaseResponse<JSONObject>> { response ->
                CommonUtils().hideProgress()
              //  showSuccessDialog()
            })
        return view
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    @OnClick(R.id.tvSave)
    fun onClickSave() {

        if (hours < 1 || hours > 12) {
            CommonUtils.showCustomDialog(requireContext(), "Please enter hours between 1 to 12")
            return
        }

        try {
            val request = CreditCoinsRequest(
                coins = CommonUtils.authconfig_response(requireContext())
                    .getSetThirstReminder()!!.toInt(),
                remarks = AppConstants.SET_THIRST_REMINDER_CLICK,
                type = AppConstants.SET_THIRST_REMINDER_CLICK,
                isGift = 0,
                blogId = 0
            )
            wolooViewModel!!.addCoinstoWolooUser(request)

            val bundle = Bundle()
            val payload = HashMap<String, Any>()
            bundle.putString(AppConstants.SET_THIRST_REMINDER_CLICK, hours.toString())
            payload[AppConstants.SET_THIRST_REMINDER_CLICK] = hours.toString()

            logFirebaseEvent(activity, bundle, AppConstants.SET_THIRST_REMINDER_CLICK)
            logNetcoreEvent(requireActivity(), payload, AppConstants.SET_THIRST_REMINDER_CLICK)

        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }

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
        try {
            if (text.isNullOrEmpty()) {
                hours = 0
                tvThirstTimerMessage!!.text =
                    getString(R.string.thirst_reminder_confirmation_value, hours)
                return
            }

            val input = text.toString().toInt()

          /*  when {
                input < 1 -> {
                    hours = 1
                    etThirstFrequency!!.setText("1")
                    etThirstFrequency!!.setSelection(1)
                }
                input > 12 -> {
                    hours = 12
                    etThirstFrequency!!.setText("12")
                    etThirstFrequency!!.setSelection(2)
                }
                else -> {*/
                    hours = input
             //   }
           // }

            tvThirstTimerMessage!!.text =
                getString(R.string.thirst_reminder_confirmation_value, hours)

        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
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
