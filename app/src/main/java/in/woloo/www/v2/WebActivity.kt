package `in`.woloo.www.v2

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.application_kotlin.presentation.fragments.web_view.WebViewFragment

class WebActivity : AppCompatActivity() {

    var title : String = ""
    var webLink : String = ""

    lateinit var tvTitle : TextView
    lateinit var ivBack: ImageView
    lateinit var mWebView: WebView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        initViews()

    }

    private fun initViews() {

        Logger.i(WebViewFragment.TAG, "initViews")

        tvTitle = findViewById(R.id.tvTitle)
        ivBack = findViewById(R.id.ivBack)
        mWebView = findViewById(R.id.webview)

        if (intent.getStringExtra("privacy_policy") != null) {
            title = "Privacy Policy"
            webLink = intent.getStringExtra("privacy_policy").toString()
        }

        if (intent.getStringExtra("terms_conditions") != null) {
            title = "Terms Conditions"
            webLink = intent.getStringExtra("terms_conditions").toString()
        }


        try {
            initializeWebView()
        } catch (ex: Exception) {
             CommonUtils.printStackTrace(ex);
        }

        ivBack.setOnClickListener { v: View? ->
            finish()
        }
    }


    private fun initializeWebView() {
        try {
            Logger.i(WebViewFragment.TAG, "initializeWebView")
            val settings = mWebView.settings
            //settings.setLoadsImagesAutomatically(true);
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            //settings.setLoadWithOverviewMode(true);
            settings.domStorageEnabled = true
            settings.setGeolocationEnabled(true)
            settings.setSupportZoom(true)

            settings.useWideViewPort = true
            mWebView.setInitialScale(1)

            mWebView.webViewClient = WebViewClient()


            if (!TextUtils.isEmpty(title)) {
                tvTitle.setText(title)
            }
            if (!TextUtils.isEmpty(webLink)) {
                mWebView.loadUrl(webLink)
            }
        } catch (e: Exception) {
             CommonUtils.printStackTrace(e)
        }
    }

}