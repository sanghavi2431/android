package `in`.woloo.www.application_kotlin.presentation.fragments.web_view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.presentation.activities.web_view.WebViewActivity
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.more.fragments.MoreFragment
import `in`.woloo.www.more.home_shop.ContentCommerceFragment
import `in`.woloo.www.more.period_tracker.ui.PeriodTrackerFragment
import `in`.woloo.www.more.trendingblog.fragments.TrendBlogFragment
import `in`.woloo.www.utils.Logger
import java.net.URISyntaxException

class WebViewFragment : Fragment() {
    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: LinearLayout? = null

    @JvmField
    @BindView(R.id.screen_header_needed)
    var tvTitle: TextView? = null


    @JvmField
    @BindView(R.id.screen_description)
    var tvDescription: TextView? = null

    @JvmField
    @BindView(R.id.screen_image)
    var imageViewHeader: ImageView? = null

    @JvmField
    @BindView(R.id.webview)
    var mWebView: WebView? = null

    @JvmField
    @BindView(R.id.progress_bar)
    var progressBar: ProgressBar? = null

    // TODO: Rename and change types of parameters
    private var mWebViewTitle: String? = null
    private var mWebViewURL: String? = null
    private var mWebViewFromFragment: String? = null

    /*calling on onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mWebViewTitle = requireArguments().getString(ARG_PARAM1)
            mWebViewURL = requireArguments().getString(ARG_PARAM2)
            mWebViewFromFragment = requireArguments().getString(ARG_PARAM3)
        }
        Logger.i(TAG, "loadFragment$mWebViewTitle$mWebViewURL $mWebViewFromFragment")
    }

    /*calling on onCreateView*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_web_view, container, false)
        ButterKnife.bind(this, rootView)
        initViews()
        Logger.i(TAG, "onCreateView")
        return rootView
    }

    /*calling on initViews*/
    private fun initViews() {
        Logger.i(TAG, "initViews")
        try {
            initializeWebView()
            ivBack!!.setOnClickListener { v: View? ->
                try {
                    //getActivity().onBackPressed();
                    if (requireActivity() is WebViewActivity) {
                        requireActivity().onBackPressed()
                    }  else {
                        val fm = requireActivity().supportFragmentManager
                       if (requireActivity() is WolooDashboard) {
                            if (mWebViewFromFragment!!.matches(PeriodTrackerFragment.TAG.toRegex())) {
                                (requireActivity() as WolooDashboard).loadFragment(
                                    PeriodTrackerFragment(),
                                    PeriodTrackerFragment.TAG
                                )
                                (requireActivity() as WolooDashboard).changeIcon(
                                    (requireActivity() as WolooDashboard).nav_view?.menu!!
                                        .findItem(R.id.navigation_dash_home)
                                )
                            } else if (mWebViewFromFragment!!.matches("TrendBlogFragment".toRegex())) {
                                (requireActivity() as WolooDashboard).loadFragment(
                                    ContentCommerceFragment(),
                                    ContentCommerceFragment.TAG
                                )
                                (requireActivity() as WolooDashboard).changeIcon(
                                    (requireActivity() as WolooDashboard).nav_view?.menu!!
                                        .findItem(R.id.navigation_dash_home)
                                )
                            }
                            else if (mWebViewFromFragment!!.matches("TrendingBlogFragmentBlogs".toRegex())) {
                                (requireActivity() as WolooDashboard).loadFragment(
                                    TrendBlogFragment(),
                                    TrendBlogFragment.TAG
                                )
                                (requireActivity() as WolooDashboard).changeIcon(
                                    (requireActivity() as WolooDashboard).nav_view?.menu!!
                                        .findItem(R.id.navigation_location)
                                )
                            }
                            else if (mWebViewFromFragment!!.matches(MoreFragment.TAG.toRegex())) {
                                (requireActivity() as WolooDashboard).loadFragment(
                                    MoreFragment(),
                                    MoreFragment.TAG
                                )
                                (requireActivity() as WolooDashboard).changeIcon(
                                    (requireActivity() as WolooDashboard).nav_view?.menu!!
                                        .findItem(R.id.navigation_more)
                                )
                            }
                           else
                            {
                                requireActivity().finish()
                            }
                        } else {
                            requireActivity().finish()
                        }
                    }
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    private fun initializeWebView() {
        try {
            Logger.i(TAG, "initializeWebView")
            val settings = mWebView!!.settings
            //settings.setLoadsImagesAutomatically(true);
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            //settings.setLoadWithOverviewMode(true);
            settings.domStorageEnabled = true
            settings.setGeolocationEnabled(true)
            mWebView!!.webChromeClient = object : WebChromeClient() {
                override fun onGeolocationPermissionsShowPrompt(
                    origin: String,
                    callback: GeolocationPermissions.Callback
                ) {
                    if (callback != null) {
                        callback.invoke(origin, true, false)
                    }
                }
            }

            mWebView!!.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                    Logger.i(TAG, "onPageStarted $url")
                    progressBar!!.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView, url: String) {
                    Logger.i(TAG, "onPageFinished $url")
                    progressBar!!.visibility = View.GONE
                }

                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    // do your handling codes here, which url is the requested url
                    // probably you need to open that url rather than redirect:
                    Logger.i(TAG, "shouldOverrideUrlLoading $url")
                    if (url.startsWith("intent:")) {
                        try {
                            val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                            Logger.i(TAG, intent.action.toString())
                            val fallBackUrl = intent.getStringExtra("browser_fallback_url")
                            if (!TextUtils.isEmpty(fallBackUrl)) {
                                val uri = Uri.parse(fallBackUrl)
                                view.loadUrl(uri.getQueryParameter("url")!!)
                                return true
                            }
                        } catch (e: URISyntaxException) {
                            CommonUtils.printStackTrace(e)
                        }
                    }
                    view.loadUrl(url)
                    return false // then it is not handled by default action
                }
            }
            if (!TextUtils.isEmpty(mWebViewTitle)) {
                tvTitle!!.text = mWebViewTitle
            }
            if (!TextUtils.isEmpty(mWebViewURL)) {
                mWebView!!.loadUrl(mWebViewURL!!)
            }
            if(tvTitle!!.text.matches(Regex("(?i)About")))
            {
                tvDescription?.text = "Check out woloo features and functionalities"
                imageViewHeader?.setImageResource(R.drawable.about_us_icon)
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }


    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            try {
                //getActivity().onBackPressed();
                if (requireActivity() is WebViewActivity) {
                    requireActivity().onBackPressed()
                }  else {
                    val fm = requireActivity().supportFragmentManager
                    if (requireActivity() is WolooDashboard) {
                        if (mWebViewFromFragment!!.matches(PeriodTrackerFragment.TAG.toRegex())) {
                            (requireActivity() as WolooDashboard).loadFragment(
                                PeriodTrackerFragment(),
                                PeriodTrackerFragment.TAG
                            )
                            (requireActivity() as WolooDashboard).changeIcon(
                                (requireActivity() as WolooDashboard).nav_view?.menu!!
                                    .findItem(R.id.navigation_dash_home)
                            )
                        } else if (mWebViewFromFragment!!.matches("TrendBlogFragment".toRegex())) {
                            (requireActivity() as WolooDashboard).loadFragment(
                                ContentCommerceFragment(),
                                ContentCommerceFragment.TAG
                            )
                            (requireActivity() as WolooDashboard).changeIcon(
                                (requireActivity() as WolooDashboard).nav_view?.menu!!
                                    .findItem(R.id.navigation_dash_home)
                            )
                        }
                        else if (mWebViewFromFragment!!.matches("TrendingBlogFragmentBlogs".toRegex())) {
                            (requireActivity() as WolooDashboard).loadFragment(
                                TrendBlogFragment(),
                                TrendBlogFragment.TAG
                            )
                            (requireActivity() as WolooDashboard).changeIcon(
                                (requireActivity() as WolooDashboard).nav_view?.menu!!
                                    .findItem(R.id.navigation_location)
                            )
                        }
                        else if (mWebViewFromFragment!!.matches(MoreFragment.TAG.toRegex())) {
                            (requireActivity() as WolooDashboard).loadFragment(
                                MoreFragment(),
                                MoreFragment.TAG
                            )
                            (requireActivity() as WolooDashboard).changeIcon(
                                (requireActivity() as WolooDashboard).nav_view?.menu!!
                                    .findItem(R.id.navigation_more)
                            )
                        }
                        else
                        {
                            requireActivity().finish()
                        }
                    } else {
                        requireActivity().finish()
                    }
                }
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }


        }

    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val ARG_PARAM3 = "param3"
        @JvmField
        var TAG = WebViewFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(wvTitle: String?, wvURL: String?, sorceFragment: String?): WebViewFragment {
            val fragment = WebViewFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, wvTitle)
            args.putString(ARG_PARAM2, wvURL)
            args.putString(ARG_PARAM3, sorceFragment)
            fragment.arguments = args
            return fragment
        }
    }
}