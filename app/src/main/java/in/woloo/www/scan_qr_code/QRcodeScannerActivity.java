package in.woloo.www.scan_qr_code;

import static android.app.PendingIntent.getActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.camera.CameraSettings;

import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.scan_qr_code.model.ScanQRCodeResponse;
import in.woloo.www.scan_qr_code.mvp.ScanQRCodePresenter;
import in.woloo.www.scan_qr_code.mvp.ScanQRCodeView;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.Utility;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.data.remote.MessageResponse;
import in.woloo.www.v2.woloo.viewmodel.WolooViewModel;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import me.dm7.barcodescanner.zxing.ZXingScannerView.ResultHandler;


public class QRcodeScannerActivity extends AppCompatActivity implements ResultHandler {



    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    WolooViewModel wolooViewModel;
  //  private ZXingScannerView mScannerView;
  private DecoratedBarcodeView barcodeScannerView;


    private ViewProfileResponse viewProfileResponse = null;
    SharedPreference mSharedPreference;
    private String wolooCode = "";
    public static String TAG= QRcodeScannerActivity.class.getSimpleName();


    private static final int REQUEST_CAMERA_PERMISSION = 113;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        ButterKnife.bind(this);
        initViews();
        setLiveData();
        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner);
        Logger.i(TAG, "onCreate");


        barcodeScannerView.initializeFromIntent(getIntent());
        barcodeScannerView.decodeContinuous(callback);


        Log.d("QR RESULT ",  "scan initiated");
        if(checkCameraPermission())
        {
            Log.d("Aarati" , "PERMISSION GRANTED");
            barcodeScannerView.resume();
        }
        else
        {
            Log.d("Aarati" , "PERMISSION Already not GRANTED");
            requestCameraPermission();

        }
       /* ActivityCompat.requestPermissions(QRcodeScannerActivity.this,
                new String[]{Manifest.permission.CAMERA},
                1);
*/
        FrameLayout contentFrame = (FrameLayout) findViewById(R.id.content_frame);
       /* mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);*/

    }


    private final BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result != null) {
                handleResult(result.getResult());
                barcodeScannerView.pause();
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
            // Handle possible result points
        }
    };
    /*calling on initViews*/
    private void initViews() {
        Logger.i(TAG, "initViews");
        try {
            try{
                String viewProfileString = getIntent().getStringExtra(AppConstants.VIEW_PROFILE_STRING);
                if(viewProfileString != null){
                   viewProfileResponse = new Gson().fromJson(viewProfileString,ViewProfileResponse.class);
                }
            }catch (Exception ex){
                 CommonUtils.printStackTrace(ex);
            }
            wolooViewModel = new ViewModelProvider(this).get(WolooViewModel.class);
            tvTitle.setText(getResources().getString(R.string.scan_qr_code));
            ivBack.setOnClickListener(v -> {
                onBackPressed();
            });
        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    private void setLiveData(){
        wolooViewModel.observeScanQRCode().observe(this, new Observer<BaseResponse<MessageResponse>>() {
            @Override
            public void onChanged(BaseResponse<MessageResponse> response) {
                if(response != null){
                    try{
                        Logger.i(TAG, "scanQRResponse");
//            Toast.makeText(this, scanQRCodeResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        if(response.getSuccess()){
                            showScanningSuccessDialog();
                            try{


                            }catch (Exception ex){
                                CommonUtils.printStackTrace(ex);
                            }
                        }
                    }catch (Exception ex){
                        CommonUtils.printStackTrace(ex);
                    }
                }
            }
        });
    }



    /*calling on onResume*/
 /*   @Override
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
    *//*calling on onPause*//*
    @Override
    protected void onPause() {
        Logger.i(TAG, "onPause");
      //  mScannerView.stopCamera();
        barcodeScannerView.pause();
        super.onPause();
    }
*/
    /*calling on onRequestPermissionsResult*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Logger.i(TAG, "onRequestPermissionsResult");
        switch (requestCode) {

            case REQUEST_CAMERA_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with selecting an image
                    //startQRCodeScanner();
                    barcodeScannerView.resume();
                    ;
                } else {
                    // Permission denied, show a message to the user
                    Toast.makeText(getApplicationContext().getApplicationContext(), "Permission required to access camera", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }


    /*calling on handleResult*/
    @Override
    public void handleResult(Result result) {
        try {
            Logger.i(TAG, "handleResult");
            wolooCode = result.getText();
            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.WOLOO_CODE, wolooCode);
            Utility.logFirebaseEvent(this, bundle, AppConstants.QR_SCAN_EVENT);

            HashMap<String,Object> payload = new HashMap<>();
            payload.put(AppConstants.WOLOO_CODE, wolooCode);
            Utility.logNetcoreEvent(this,payload,AppConstants.QR_SCAN_EVENT);

            mSharedPreference = new SharedPreference(QRcodeScannerActivity.this);

            WebView webview;
            webview = (WebView)findViewById(R.id.help_webview);
            webview.loadUrl(wolooCode);
            webview.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    wolooCode = url;
                    setData();
                }
            });

        } catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    private void setData() {
        if (wolooCode.contains("/voucher/")) {
            String voucher = wolooCode.toString().split("/")[wolooCode.toString().split("/").length - 2];
            mSharedPreference.setStoredPreference(QRcodeScannerActivity.this, SharedPreferencesEnum.VOUCHER_CODE.getPreferenceKey(), voucher);
            Intent i = new Intent(QRcodeScannerActivity.this, WolooDashboard.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        } else if (wolooCode.toString().contains("/wahcertificate/"))
        {
            String linkParam = wolooCode.split("link=")[1].split("&")[0];

            // Step 2: Split the 'link' parameter value to isolate the part containing '15403'
            String[] parts = linkParam.split("/");

            // Step 3: Extract '15403' from the split parts
            String wahCertificate = parts[parts.length - 1];
           // String wahCertificate = wolooCode.toString().split("/")[wolooCode.toString().split("/").length-3];
            mSharedPreference.setStoredPreference(QRcodeScannerActivity.this, SharedPreferencesEnum.WAH_CERTIFICATE_CODE.getPreferenceKey(), wahCertificate);
            Intent i = new Intent(QRcodeScannerActivity.this, WolooDashboard.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }
        else {
            wolooViewModel.scanQRCode(wolooCode);

           // mScannerView.stopCameraPreview();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    /*calling on showScanningSuccessDialog*/
    public void showScanningSuccessDialog() {
        try {
            Logger.i(TAG, "showScanningSuccessDialog");
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setContentView(R.layout.dialog_qr_scanning);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            TextView btnCloseDialog = (TextView) dialog.findViewById(R.id.btnCloseDialog);

            TextView tv_qrcodescanning = (TextView) dialog.findViewById(R.id.tv_qrcodescanning);

            AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(QRcodeScannerActivity.this);
            if (authConfigResponse != null) {
                String getqRCodeScanningSuccessDialog = authConfigResponse.getcUSTOMMESSAGE().getqRCodeScanningSuccessDialog();
                tv_qrcodescanning.setText(getqRCodeScanningSuccessDialog.replaceAll("\\\\n","\n"));
            }

            btnCloseDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing())
                        dialog.dismiss();
                    Intent i = new Intent(QRcodeScannerActivity.this, WolooDashboard.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            });
            dialog.show();
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        Log.d("Aarati" , "in request PERMISSION");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);

        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }




}