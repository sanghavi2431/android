package in.woloo.www.webview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.payment.fragments.PaymentFragment;
import in.woloo.www.utils.Logger;

public class WebViewActivity extends AppCompatActivity {

    /*@BindView(R.id.frmFragment)
    WebView frmFragment;*/
    public static String TAG = WebViewActivity.class.getSimpleName();
    public static String ARG_BLOG_URL = "ARG_BLOG_URL";

    /*calling on onCreate*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);
        initViews();
        Logger.i(TAG, "onCreate" + getIntent().getData());
    }

    /*calling on initViews*/
    private void initViews() {
        Logger.i(TAG, "initViews");
        try {
            Uri uri = getIntent().getData();
            if (uri != null && uri.getPath().contains("mobile_blog_detail")) {
                loadFragment(WebViewFragment.newInstance("Blog detail", uri.toString() , WebViewActivity.TAG));
            } else if (getIntent().getStringExtra(ARG_BLOG_URL) != null && getIntent().getStringExtra(ARG_BLOG_URL).contains("mobile_blog_detail")) {
                loadFragment(WebViewFragment.newInstance("Blog detail", getIntent().getStringExtra(ARG_BLOG_URL) , WebViewActivity.TAG));
            } else {
                String aboutURL = CommonUtils.getAboutUrl(this);
                loadFragment(WebViewFragment.newInstance("About", aboutURL , WebViewActivity.TAG));
            }
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    /*calling on loadFragment*/
    public void loadFragment(Fragment fragment) {
        try {
            Logger.i(TAG, "loadFragment");
            FragmentManager fragmentManager = ((AppCompatActivity) this).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.frmFragment, fragment);
            fragmentTransaction.commit();
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

}