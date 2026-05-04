package in.woloo.www.shopping.fragments;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.shopping.config.Config;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaymentWebviewFragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentWebviewFragement extends Fragment {

    @BindView(R.id.webview)
    WebView webview;






    private String paymentURL = Config.hostname+"payment_webview.php";




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    public String amount;
    public String address;
    public String gift_card_used_value;
    public String payment_status = "";

    public PaymentWebviewFragement() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubscribeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentWebviewFragement newInstance(String param1, String param2, String param3) {
        PaymentWebviewFragement fragment = new PaymentWebviewFragement();
        Bundle args = new Bundle();
        args.putString("amount", param1);
        args.putString("address", param2);
        args.putString("gift_card_used_value", param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            amount = getArguments().getString("amount");
            address = getArguments().getString("address");
            gift_card_used_value = getArguments().getString("gift_card_used_value");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_payment_webview, container, false);
        ButterKnife.bind(this,root);
        initViews();
        return root;
    }



    @Override
    public void onResume() {
        super.onResume();
        try{

            if(payment_status.equalsIgnoreCase("success"))
            {

                // Call Success Fragment

                payment_status = "";

                SuccessFragment myFragment = new SuccessFragment();
                Bundle b = new Bundle();
                b.putString("amount",amount);
                b.putString("address",address);
                b.putString("gift_card_used_value",gift_card_used_value);
                myFragment.setArguments(b);


                FragmentManager fragmentManager = ((WolooDashboard)getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frm_contant, myFragment,"");

                fragmentTransaction.commit();

            }


            if(payment_status.equalsIgnoreCase("failed"))
            {

                payment_status = "";


                // Call Failure Fragment

                FailureFragment myFragment = new FailureFragment();
                Bundle b = new Bundle();
                b.putString("amount",amount);
                b.putString("address",address);
                b.putString("gift_card_used_value",gift_card_used_value);
                myFragment.setArguments(b);


                FragmentManager fragmentManager = ((WolooDashboard)getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frm_contant, myFragment,"");

                fragmentTransaction.commit();

            }












        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }


    @SuppressLint("JavascriptInterface")
    private void initViews() {
        try{


            webview.getSettings().setJavaScriptEnabled(true);

            webview.getSettings().setPluginState(WebSettings.PluginState.ON);

            webview.addJavascriptInterface( new PaymentInterface(),"PaymentInterface");

            webview.loadUrl(paymentURL+"?user_id="+ShoppingFragment.user_id+"&name="+ShoppingFragment.user_name+"&mobile="+ShoppingFragment.user_phone+"&amount="+amount+"&address="+address);

            webview.setWebViewClient(new WebViewClient());




        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    public class PaymentInterface{
        @JavascriptInterface
        public void success(String data){
            Toast.makeText(getActivity().getApplicationContext(), "Sucess: "+data, Toast.LENGTH_LONG).show();
        }
        @JavascriptInterface
        public void error(String data){
            Toast.makeText(getActivity().getApplicationContext(), "FAilure: "+data, Toast.LENGTH_LONG).show();
        }
    }





    public class WebViewClient extends android.webkit.WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // TODO Auto-generated method stub

               try {

                 if(url.contains("payment_success.php"))
                {
                    payment_status = "success";

					try {
						
                    Uri uri = Uri.parse(url);
                    String server = uri.getAuthority();
                    String path = uri.getPath();
                    String protocol = uri.getScheme();
                    String razorpay_payment_id = uri.getQueryParameter("razorpay_payment_id");
                    String razorpay_order_id = uri.getQueryParameter("razorpay_order_id");
                    String razorpay_signature = uri.getQueryParameter("razorpay_signature");

                    // Call Success Fragment

                    SuccessFragment myFragment = new SuccessFragment();
                    Bundle b = new Bundle();
                    b.putString("amount",amount);
                    b.putString("address",address);
                    b.putString("gift_card_used_value",gift_card_used_value);
                    myFragment.setArguments(b);


                    FragmentManager fragmentManager = ((WolooDashboard)getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frm_contant, myFragment,"");

                    fragmentTransaction.commit();
					
					} catch(Exception e) { 
					
					
					 SuccessFragment myFragment = new SuccessFragment();
                    Bundle b = new Bundle();
                    b.putString("amount",amount);
                    b.putString("address",address);
                    b.putString("gift_card_used_value",gift_card_used_value);
                    myFragment.setArguments(b);


                    FragmentManager fragmentManager = ((WolooDashboard)getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.frm_contant, myFragment,"");

                    fragmentTransaction.commit();
					
					}


                }
                 else if(url.contains("payment_failure.php"))
                 {

                     payment_status = "failed";
					 
					 
					 
					 try {


                     // Call Failure Fragment

                     FailureFragment myFragment = new FailureFragment();
                     Bundle b = new Bundle();
                     b.putString("amount",amount);
                     b.putString("address",address);
                     b.putString("gift_card_used_value",gift_card_used_value);
                     myFragment.setArguments(b);


                     FragmentManager fragmentManager = ((WolooDashboard)getContext()).getSupportFragmentManager();
                     FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                     fragmentTransaction.replace(R.id.frm_contant, myFragment,"");

                     fragmentTransaction.commit();
					 
					 } catch(Exception e) { }

                 }



                else {
                    view.loadUrl(url); }
					
			   }
			   
			 catch(Exception e) { view.loadUrl(url); }
					
					

            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {

            // TODO Auto-generated method stub

            super.onPageFinished(view, url);

        }




    }



}

