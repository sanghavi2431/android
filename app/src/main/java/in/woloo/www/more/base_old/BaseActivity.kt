package `in`.woloo.www.more.base_old

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.view_models.BaseViewModel
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.api_classes.ApiResponseData

open class BaseActivity: AppCompatActivity() {

    //private var progressbar: AppProgressBaseDialog? = null
    private lateinit var viewModel: BaseViewModel
    internal val pref = SharedPrefSettings.getPreferences
    private var progressbar: Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            progressbar = Dialog(this, R.style.CustomDialogTime)
            progressbar!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            progressbar!!.window?.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this,
                        android.R.color.transparent
                    )
                )
            )
            progressbar!!.setCancelable(false)
            progressbar!!.setContentView(R.layout.dialog_progress_overlay)
        } catch (e: Exception) {
              CommonUtils.printStackTrace(e)
        }
    }

    open fun onCreateViewModel(): BaseViewModel? {
        return null
    }

    fun setProgressBar() {
        val viewModel = onCreateViewModel() ?: return
        viewModel.observeProgressDialogLiveData().observe(this, Observer {
            if (this.isFinishing) {
                return@Observer
            }
            if (it.isShow) {
                showProgressBar(true)
            } else {
                showProgressBar(false)
            }
        })
    }

    fun showProgressBar(show: Boolean){
        if (this.isFinishing) {
            return
        }
        if (show) {
            progressbar?.show()
        } else {
            progressbar?.hide()
        }
    }

    fun setNetworkDetector() {
        val viewModel = onCreateViewModel() ?: return
        viewModel.observeNetworkDetectorLiveData().observe(this, Observer {
            when (it.status) {
                ApiResponseData.API_NO_NETWORK -> {
                    displayToast(resources.getString(R.string.no_internet_connection))
                }
                ApiResponseData.API_FAILURE -> {
                    displayToast(it.message)
                }
                else -> {
                    displayToast(it.message)
                }
            }
        })
    }

    fun displayToast(message: String) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(
                this,
                message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}