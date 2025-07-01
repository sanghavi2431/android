package in.woloo.www.vtion.fragments;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import in.woloo.www.R;


public class DialogVisionPrivacyPolicy extends DialogFragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_vision_privacy_policy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

      /*  PDFView pdfView = view.findViewById(R.id.pdfView);
        pdfView.fromAsset("woloo_vtion_pp.pdf").load();
*/

        WebView webView = view.findViewById(R.id.webview_privacy_policy);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/woloo_vtion_privacy_policy.html");
        webView.getSettings().setJavaScriptEnabled(true);

        view.findViewById(R.id.button_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(); // Close the dialog
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Set dialog fragment dimensions to full screen
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        // Handle dialog dismissal here if needed
    }













}
