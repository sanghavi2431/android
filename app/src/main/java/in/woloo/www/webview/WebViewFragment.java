package in.woloo.www.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URISyntaxException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.dashboard.ui.home.HomeFragment;
import in.woloo.www.period_tracker.ui.PeriodTrackerFragment;
import in.woloo.www.subscribe.SubscribeActivity;
import in.woloo.www.trendingblog.fragments.TrendBlogFragment;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.ProgressBarUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewFragment extends Fragment {

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.webview)
    WebView mWebView;

    @BindView(R.id.progress_bar)
    ProgressBar  progressBar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mWebViewTitle;
    private String mWebViewURL;
    public static String TAG = WebViewFragment.class.getSimpleName();

    private String mWebViewFromFragment;

    public WebViewFragment() {
        // Required empty public constructor
    }

    public static WebViewFragment newInstance(String wvTitle, String wvURL , String sorceFragment) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, wvTitle);
        args.putString(ARG_PARAM2, wvURL);
        args.putString(ARG_PARAM3 , sorceFragment);
        fragment.setArguments(args);
        return fragment;
    }

    /*calling on onCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWebViewTitle = getArguments().getString(ARG_PARAM1);
            mWebViewURL = getArguments().getString(ARG_PARAM2);
            mWebViewFromFragment = getArguments().getString(ARG_PARAM3);
        }
        Logger.i(TAG, "loadFragment" + mWebViewTitle + mWebViewURL);
    }

    /*calling on onCreateView*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_web_view, container, false);
        ButterKnife.bind(this, rootView);
        initViews();
        Logger.i(TAG, "onCreateView");
        return rootView;
    }

    /*calling on initViews*/
    private void initViews() {
        Logger.i(TAG, "initViews");
        try {
            initializeWebView();
            ivBack.setOnClickListener(v -> {
                try {
                    //getActivity().onBackPressed();
                    if (requireActivity() instanceof WebViewActivity) {
                        requireActivity().onBackPressed();
                    }/* else if (requireActivity() instanceof WolooDashboard) {
                        ((WolooDashboard) requireActivity()).loadFragment(new TrendBlogFragment(), TrendBlogFragment.TAG);
                        ((WolooDashboard) requireActivity()).changeIcon(((WolooDashboard) requireActivity()).nav_view.getMenu().findItem(R.id.navigation_dash_home));
                    } */ else {
                         FragmentManager fm = requireActivity().getSupportFragmentManager();
                       /* if (fm.getBackStackEntryCount() >= 0) {
                            fm.popBackStack();
                        } else */ if (requireActivity() instanceof WolooDashboard) {


                                if(mWebViewFromFragment.matches(PeriodTrackerFragment.TAG))
                                {
                                    ((WolooDashboard) requireActivity()).loadFragment(new PeriodTrackerFragment(), PeriodTrackerFragment.TAG);
                                    ((WolooDashboard) requireActivity()).changeIcon(((WolooDashboard) requireActivity()).nav_view.getMenu().findItem(R.id.navigation_dash_home));


                                }
                                else  if(mWebViewFromFragment.matches(TrendBlogFragment.TAG))
                                {
                                    ((WolooDashboard) requireActivity()).loadFragment(new TrendBlogFragment(), TrendBlogFragment.TAG);
                                    ((WolooDashboard) requireActivity()).changeIcon(((WolooDashboard) requireActivity()).nav_view.getMenu().findItem(R.id.navigation_dash_home));


                                }

                             }
                        else {
                            requireActivity().finish();
                        }

                    }
                } catch (Exception ex) {
                     CommonUtils.printStackTrace(ex);
                }
            });
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }


    private void initializeWebView() {
        try {
            Logger.i(TAG, "initializeWebView");
            WebSettings settings = mWebView.getSettings();
            //settings.setLoadsImagesAutomatically(true);
            settings.setJavaScriptEnabled(true);
            settings.setJavaScriptCanOpenWindowsAutomatically(true);
            //settings.setLoadWithOverviewMode(true);
            settings.setDomStorageEnabled(true);
            settings.setGeolocationEnabled(true);

            mWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                    if (callback != null) {
                        callback.invoke(origin, true, false);
                    }
                }
            });
            //settings.setUseWideViewPort(false);
            //settings.setSupportZoom(true);
            //mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    Logger.i(TAG, "onPageStarted " + url);
                    progressBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    Logger.i(TAG, "onPageFinished " + url);
                    progressBar.setVisibility(View.GONE);
                }


                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    // do your handling codes here, which url is the requested url
                    // probably you need to open that url rather than redirect:
                    Logger.i(TAG, "shouldOverrideUrlLoading " + url);
                    if (url.startsWith("intent:")) {

                        try {
                            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                            Logger.i(TAG, intent.getAction());
                            String fallBackUrl = intent.getStringExtra("browser_fallback_url");
                            if (!TextUtils.isEmpty(fallBackUrl)) {
                                Uri uri = Uri.parse(fallBackUrl);
                                view.loadUrl(uri.getQueryParameter("url"));
                                return true;
                            }
                        } catch (URISyntaxException e) {
                              CommonUtils.printStackTrace(e);
                        }
                    }
                    view.loadUrl(url);
                    return false; // then it is not handled by default action
                }
            });

            if (!TextUtils.isEmpty(mWebViewTitle)) {
                tvTitle.setText(mWebViewTitle);
            }
            if (!TextUtils.isEmpty(mWebViewURL)) {
                mWebView.loadUrl(mWebViewURL);
            }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }

}