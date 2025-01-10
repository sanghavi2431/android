package `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import com.google.gson.Gson
import com.google.zxing.Result
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.more.models.ViewProfileResponse
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility
import `in`.woloo.www.v2.woloo.viewmodel.WolooViewModel
import me.dm7.barcodescanner.zxing.ZXingScannerView

class QRcodeScannerActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {
    @JvmField
    @BindView(R.id.tvTitle)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: ImageView? = null
    var wolooViewModel: WolooViewModel? = null

    //  private ZXingScannerView mScannerView;
    private var barcodeScannerView: DecoratedBarcodeView? = null
    private var viewProfileResponse: ViewProfileResponse? = null
    var mSharedPreference: SharedPreference? = null
    private var wolooCode = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qr)
        ButterKnife.bind(this)
        initViews()
        setLiveData()
        barcodeScannerView = findViewById<DecoratedBarcodeView>(R.id.zxing_barcode_scanner)
        Logger.i(TAG, "onCreate")
        barcodeScannerView?.initializeFromIntent(intent)
        barcodeScannerView?.decodeContinuous(callback)
        Log.d("QR RESULT ", "scan initiated")
        if (checkCameraPermission()) {
            Log.d("Aarati", "PERMISSION GRANTED")
            barcodeScannerView?.resume()
        } else {
            Log.d("Aarati", "PERMISSION Already not GRANTED")
            requestCameraPermission()
        }
        /* ActivityCompat.requestPermissions(QRcodeScannerActivity.this,
                new String[]{Manifest.permission.CAMERA},
                1);
*/
        val contentFrame = findViewById<View>(R.id.content_frame) as FrameLayout
        /* mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);*/
    }

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result != null) {
                handleResult(result.result)
                barcodeScannerView!!.pause()
            }
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {
            // Handle possible result points
        }
    }

    /*calling on initViews*/
    private fun initViews() {
        Logger.i(TAG, "initViews")
        try {
            try {
                val viewProfileString = intent.getStringExtra(AppConstants.VIEW_PROFILE_STRING)
                if (viewProfileString != null) {
                    viewProfileResponse = Gson().fromJson<ViewProfileResponse>(
                        viewProfileString,
                        ViewProfileResponse::class.java
                    )
                }
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
            wolooViewModel = ViewModelProvider(this).get(
                WolooViewModel::class.java
            )
            tvTitle!!.text = resources.getString(R.string.scan_qr_code)
            ivBack!!.setOnClickListener { v: View? -> onBackPressed() }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun setLiveData() {
        wolooViewModel!!.observeScanQRCode().observe(this) { response ->
            if (response != null) {
                try {
                    Logger.i(TAG, "scanQRResponse")
                    //            Toast.makeText(this, scanQRCodeResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    if (response.success) {
                        showScanningSuccessDialog()
                        try {
                        } catch (ex: Exception) {
                            CommonUtils.printStackTrace(ex)
                        }
                    }
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
            }
        }
    }

    /*calling on onResume*/ /*   @Override
    protected void onResume() {
        super.onResume();
        Logger.i(TAG, "onResume");
        //if else condition changed by Aarati
        if (checkCameraPermission()) {
           // startScanning();
           // startQRCodeScanner();
            barcodeScannerView.resume();
        } else {
            requestCameraPermission();
        }
      //  barcodeScannerView.resume();
    }
    */
    /*calling on onPause*/ /*
    @Override
    protected void onPause() {
        Logger.i(TAG, "onPause");
      //  mScannerView.stopCamera();
        barcodeScannerView.pause();
        super.onPause();
    }
*/
    /*calling on onRequestPermissionsResult*/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Logger.i(TAG, "onRequestPermissionsResult")
        when (requestCode) {
            REQUEST_CAMERA_PERMISSION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with selecting an image
                    //startQRCodeScanner();
                    barcodeScannerView!!.resume()
                } else {
                    // Permission denied, show a message to the user
                    Toast.makeText(
                        applicationContext.applicationContext,
                        "Permission required to access camera",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /*calling on handleResult*/
    override fun handleResult(result: Result) {
        try {
            Logger.i(TAG, "handleResult")
            wolooCode = result.text
            val bundle = Bundle()
            bundle.putString(AppConstants.WOLOO_CODE, wolooCode)
            Utility.logFirebaseEvent(this, bundle, AppConstants.QR_SCAN_EVENT)
            val payload = HashMap<String, Any>()
            payload[AppConstants.WOLOO_CODE] = wolooCode
            Utility.logNetcoreEvent(this, payload, AppConstants.QR_SCAN_EVENT)
            mSharedPreference = SharedPreference(this@QRcodeScannerActivity)
            val webview: WebView
            webview = findViewById<View>(R.id.help_webview) as WebView
            webview.loadUrl(wolooCode)
            webview.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    wolooCode = url
                    setData()
                }
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun setData() {
        if (wolooCode.contains("/voucher/")) {
            val voucher = wolooCode.split("/".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[wolooCode
                .split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size - 2]
            mSharedPreference?.setStoredPreference(
                this@QRcodeScannerActivity,
                SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(),
                voucher
            )
            val i = Intent(this@QRcodeScannerActivity, WolooDashboard::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
        } else if (wolooCode.contains("/wahcertificate/")) {
            val linkParam = wolooCode.split("link=".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[1].split("&".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0]

            // Step 2: Split the 'link' parameter value to isolate the part containing '15403'
            val parts = linkParam.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            // Step 3: Extract '15403' from the split parts
            val wahCertificate = parts[parts.size - 1]
            // String wahCertificate = wolooCode.toString().split("/")[wolooCode.toString().split("/").length-3];
            mSharedPreference?.setStoredPreference(
                this@QRcodeScannerActivity,
                SharedPreferencesEnum.WAH_CERTIFICATE_CODE.getPreferenceKey(),
                wahCertificate
            )
            val i = Intent(this@QRcodeScannerActivity, WolooDashboard::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(i)
        } else {
            wolooViewModel!!.scanQRCode(wolooCode)

            // mScannerView.stopCameraPreview();
        }
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {}

    /*calling on showScanningSuccessDialog*/
    fun showScanningSuccessDialog() {
        try {
            Logger.i(TAG, "showScanningSuccessDialog")
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_qr_scanning)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
            val btnCloseDialog = dialog.findViewById<View>(R.id.btnCloseDialog) as TextView
            val tv_qrcodescanning = dialog.findViewById<View>(R.id.tv_qrcodescanning) as TextView
            val authConfigResponse = CommonUtils.authconfig_response(this@QRcodeScannerActivity)
            if (authConfigResponse != null) {
                val getqRCodeScanningSuccessDialog = authConfigResponse.getcUSTOMMESSAGE()!!
                    .getqRCodeScanningSuccessDialog()
                tv_qrcodescanning.text =
                    getqRCodeScanningSuccessDialog!!.replace("\\\\n".toRegex(), "\n")
            }
            btnCloseDialog.setOnClickListener {
                if (dialog.isShowing) dialog.dismiss()
                val i = Intent(this@QRcodeScannerActivity, WolooDashboard::class.java)
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(i)
            }
            dialog.show()
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    private fun checkCameraPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestCameraPermission() {
        Log.d("Aarati", "in request PERMISSION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION)
        }
    }

    companion object {
        var TAG = QRcodeScannerActivity::class.java.simpleName
        private const val REQUEST_CAMERA_PERMISSION = 113
    }
}