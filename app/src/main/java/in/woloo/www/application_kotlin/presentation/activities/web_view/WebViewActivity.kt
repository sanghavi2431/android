package `in`.woloo.www.application_kotlin.presentation.activities.web_view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.application_kotlin.presentation.fragments.web_view.WebViewFragment

class WebViewActivity : AppCompatActivity() {
    /*calling on onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        ButterKnife.bind(this)
        initViews()
        Logger.i(TAG, "onCreate" + intent.data)
    }

    /*calling on initViews*/
    private fun initViews() {
        Logger.i(TAG, "initViews")
        try {
            val uri = intent.data
            if (uri != null && uri.path!!.contains("mobile_blog_detail")) {
                loadFragment(WebViewFragment.newInstance("Blog detail", uri.toString(), TAG))
            } else if (intent.getStringExtra(ARG_BLOG_URL) != null && intent.getStringExtra(
                    ARG_BLOG_URL
                )!!
                    .contains("mobile_blog_detail")
            ) {
                loadFragment(
                    WebViewFragment.newInstance(
                        "Blog detail", intent.getStringExtra(
                            ARG_BLOG_URL
                        ), TAG
                    )
                )
            } else {
                val aboutURL = CommonUtils.getAboutUrl(this)
                loadFragment(WebViewFragment.newInstance("About", aboutURL, TAG))
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling on loadFragment*/
    fun loadFragment(fragment: Fragment?) {
        try {
            Logger.i(TAG, "loadFragment")
            val fragmentManager = (this as AppCompatActivity).supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.frmFragment, fragment!!)
            fragmentTransaction.commit()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    companion object {

        var TAG = WebViewActivity::class.java.simpleName
        var ARG_BLOG_URL = "ARG_BLOG_URL"
    }
}