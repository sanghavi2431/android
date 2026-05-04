package `in`.woloo.www.application_kotlin.presentation.fragments.web_view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.presentation.activities.web_view.WebViewActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.application_kotlin.presentation.MoreFragment
import `in`.woloo.www.application_kotlin.view_models.HomeViewModel
import `in`.woloo.www.more.home_shop.ContentCommerceFragment
import `in`.woloo.www.more.period_tracker.ui.PeriodTrackerFragment
import `in`.woloo.www.more.trendingblog.fragments.TrendBlogFragment
import `in`.woloo.www.utils.Logger
import org.json.JSONObject
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
    private var homeViewModel: HomeViewModel? = null

    /*calling on onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
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
        try {
            if (mWebViewTitle.equals("about", ignoreCase = true)) {
                homeViewModel!!.getAboutUsStatus()
                setLiveData()
                tvTitle!!.setText("About Woloo")
                tvDescription!!.setText("Take a sneak peak into how and why Woloo came into being")
                imageViewHeader!!.setImageResource(R.drawable.about_us_icon)
            } else if (mWebViewTitle.equals("Terms of use", ignoreCase = true)) {
                homeViewModel!!.getTermsStatus()
                setLiveData2()
                tvTitle!!.setText("Terms Of Use")
                tvDescription!!.setText("Terms and Conditions related to using the Woloo App and Website")
                imageViewHeader!!.setImageResource(R.drawable.terms_of_use_icon)
            } else {
                initializeWebView()
                tvTitle!!.setText(mWebViewTitle)
                imageViewHeader!!.setImageResource(R.drawable.blogs_bottom_nav)
            }
        }catch (e : Exception)
        {

        }
        return rootView
    }

    /*calling on initViews*/
    private fun initViews() {
        Logger.i(TAG, "initViews")
        try {

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

    private fun setLiveData()
    {
        homeViewModel!!.observeAboutUsStatus().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                Logger.i(TAG, "setLiveData ${it.data!!.description}")
                // Extract the HTML content from the response (assuming it's inside the "description" key)
                mWebView!!.settings.javaScriptEnabled = true
                mWebView!!.settings.allowContentAccess = true
                mWebView!!.settings.loadsImagesAutomatically = true

                // Enable image loading in the WebView (important for showing images)
                mWebView!!.webViewClient = WebViewClient()  // Links will open in the WebView itself
                mWebView!!.webChromeClient = WebChromeClient()


                var htmlContent = it.data!!.description

                htmlContent = injectBackgroundColor(htmlContent!!)

                htmlContent = fixImageUrls(htmlContent)

                // Load HTML content into WebView
                if (htmlContent != null) {
                    mWebView!!.loadDataWithBaseURL(
                        null,
                        htmlContent,
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
            }
        })
            mWebView!!.setWebViewClient(object : WebViewClient() {
                override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                    super.onReceivedError(view, request, error)
                    Log.e("WebView", "Error loading page: $error")
                }
            })
    }

    private fun setLiveData2()
    {
        homeViewModel!!.observeTermsStatus().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                Logger.i(TAG, "setLiveData ${it.data!!.description}")
                // Extract the HTML content from the response (assuming it's inside the "description" key)
                mWebView!!.settings.javaScriptEnabled = true
                mWebView!!.settings.allowContentAccess = true
                mWebView!!.settings.loadsImagesAutomatically = true

                // Enable image loading in the WebView (important for showing images)
                mWebView!!.webViewClient = WebViewClient()  // Links will open in the WebView itself
                mWebView!!.webChromeClient = WebChromeClient()


                var htmlContent = it.data!!.description

                htmlContent = injectBackgroundColor(htmlContent!!)

                htmlContent = fixImageUrls(htmlContent)

                // Load HTML content into WebView
                if (htmlContent != null) {
                    mWebView!!.loadDataWithBaseURL(
                        null,
                        htmlContent,
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
            }
        })
        mWebView!!.setWebViewClient(object : WebViewClient() {
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                super.onReceivedError(view, request, error)
                Log.e("WebView", "Error loading page: $error")
            }
        })
    }

    private fun injectBackgroundColor(htmlContent: String): String {
        val backgroundColor = "#ffffff" // Set your desired background color here
        val styleTag = "<style>body { background-color: $backgroundColor; }</style>"
        return "<head>$styleTag</head><body>$htmlContent</body>"
    }

    private fun fixImageUrls(htmlContent: String): String {
        // Replace relative URLs with full URLs (you can customize this part based on your API response structure)
        val baseUrl = "https://your-base-url.com" // Set your base URL here
        val imgTagPattern = "<img src=\"(.*?)\""
        val regex = Regex(imgTagPattern)

        var modifiedHtml = htmlContent

        // Replace relative image URLs with the full URL
        regex.findAll(htmlContent).forEach { matchResult ->
            val relativeUrl = matchResult.groupValues[1]
            if (!relativeUrl.startsWith("http")) { // If it's a relative URL
                modifiedHtml = modifiedHtml.replace(relativeUrl, "$baseUrl$relativeUrl")
            }
        }

        return modifiedHtml
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