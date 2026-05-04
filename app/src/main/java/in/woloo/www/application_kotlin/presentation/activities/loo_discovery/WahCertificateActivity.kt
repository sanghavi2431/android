package `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.model.server_response.WahCertificateDetailsResponse
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.application_kotlin.view_models.WolooHostViewModel
import `in`.woloo.www.utils.AppConstants
import java.text.ParseException
import java.text.SimpleDateFormat

open class WahCertificateActivity : AppCompatActivity() {
    private var wolooViewModel: WolooHostViewModel? = null

    @JvmField
    @BindView(R.id.wolooNameTv)
    var wolooNameTv: TextView? = null

    @JvmField
    @BindView(R.id.CertificateTv)
    var CertificateTv: TextView? = null

    @JvmField
    @BindView(R.id.DateOfCreationTv)
    var DateOfCreationTv: TextView? = null

    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wah_certificate)
        ButterKnife.bind(this)
        if (mSharedPreference == null) {
            mSharedPreference = SharedPreference(this)
        }
        val wah_certificate: String = mSharedPreference?.getStoredPreference(
            this,
            SharedPreferencesEnum.WAH_CERTIFICATE_CODE.preferenceKey,
            ""
        ).toString()
        ivBack!!.setOnClickListener { onBackPressed() }
        wolooViewModel = ViewModelProvider(this)[WolooHostViewModel::class.java]
        wolooViewModel!!.wahCertificate(wah_certificate)
        mSharedPreference?.setStoredPreference(
            this,
            SharedPreferencesEnum.WAH_CERTIFICATE_CODE.preferenceKey,
            ""
        )
        setLiveData()
    }

    fun setLiveData() {
        wolooViewModel!!.observewWahCertificate().observe(this) { wahCertificateResponse ->
            if (wahCertificateResponse != null) {
                try {
                    wolooNameTv?.text = wahCertificateResponse.data?.wolooObject!!.name
                    CertificateTv?.text = wahCertificateResponse.data?.wolooObject!!.code
                    val inputPattern = "yyyy-MM-dd"
                    val outputPattern = "dd MMM yyyy"
                    val date =
                        SimpleDateFormat(inputPattern).parse(wahCertificateResponse.data?.wolooObject!!.createdAt)
                    DateOfCreationTv!!.text = SimpleDateFormat(outputPattern).format(date)
                    if(wahCertificateResponse.data?.statusForPopup ==1) {
                        showSuccessDialog()
                    }
                } catch (e: ParseException) {
                    CommonUtils.printStackTrace(e)
                }
            } else {
                Toast.makeText(
                    this@WahCertificateActivity,
                   "Please try again later",
                    Toast.LENGTH_SHORT
                ).show()
                WolooApplication.errorMessage = ""
            }
        }
    }

    fun showSuccessDialog()
    {
        try {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_coins_success)
            val displayMetrics = Resources.getSystem().displayMetrics
            val screenWidth = displayMetrics.widthPixels

// Calculate 80% of screen width
            val dialogWidth = (screenWidth).toInt()

// Apply the calculated width and wrap_content height to the dialog window
            dialog.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
// Calculate 80% of screen width
         //   val dialogWidth = (screenWidth * 0.8).toInt()

// Apply the calculated width and wrap_content height to the dialog window
          //  dialog.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)

            dialog.window?.setGravity(Gravity.BOTTOM)

            val btnCloseDialog = dialog.findViewById<View>(R.id.tv_go_back_to_home) as TextView
            val btnShopDialog = dialog.findViewById<View>(R.id.tv_shop_now) as TextView
            val gifImageView = dialog.findViewById<View>(R.id.gifImageView) as ImageView

            val btnSuccessTextDialog = dialog.findViewById<View>(R.id.tv_logout) as TextView
            btnSuccessTextDialog.setText("Woohoo! You Earned 100 Woloo Points!")

            btnCloseDialog.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
            }

            Glide.with(this)
                .load(R.drawable.coins_animate) // your gif in res/drawable
                .into(gifImageView)

            btnShopDialog.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }

                this.finish()
                val intent = Intent(this, WolooDashboard::class.java)
                intent.putExtra("StoreListingFragment", "StoreListingFragment")
                startActivity(intent)


            }

            dialog.show()
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun setWahCertificateResponse(wahCertificateResponse: WahCertificateDetailsResponse) {
        try {
            wolooNameTv?.text = wahCertificateResponse.data.name
            CertificateTv?.text = wahCertificateResponse.data.code
            val inputPattern = "yyyy-MM-dd"
            val outputPattern = "dd MMM yyyy"
            val date = SimpleDateFormat(inputPattern).parse(
                wahCertificateResponse.data.createdAt
            )
            DateOfCreationTv!!.text = SimpleDateFormat(outputPattern).format(date)
        } catch (e: ParseException) {
            CommonUtils.printStackTrace(e)
        }
    }

    companion object {
        protected var mSharedPreference: SharedPreference? = null
    }
}