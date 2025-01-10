package `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.model.server_response.WahCertificateDetailsResponse
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.v2.woloo.viewmodel.WolooViewModel
import java.text.ParseException
import java.text.SimpleDateFormat

open class WahCertificateActivity : AppCompatActivity() {
    private var wolooViewModel: WolooViewModel? = null

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
        wolooViewModel = ViewModelProvider(this)[WolooViewModel::class.java]
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
                    wolooNameTv?.text = wahCertificateResponse.data?.name
                    CertificateTv?.text = wahCertificateResponse.data?.code
                    val inputPattern = "yyyy-MM-dd"
                    val outputPattern = "dd MMM yyyy"
                    val date =
                        SimpleDateFormat(inputPattern).parse(wahCertificateResponse.data?.createdAt)
                    DateOfCreationTv!!.text = SimpleDateFormat(outputPattern).format(date)
                } catch (e: ParseException) {
                    CommonUtils.printStackTrace(e)
                }
            } else {
                Toast.makeText(
                    this@WahCertificateActivity,
                    WolooApplication.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
                WolooApplication.errorMessage = ""
            }
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