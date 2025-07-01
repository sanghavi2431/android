package in.woloo.www.scan_qr_code;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.scan_qr_code.model.ScanQRCodeResponse;
import in.woloo.www.scan_qr_code.mvp.ScanQRCodePresenter;
import in.woloo.www.scan_qr_code.mvp.ScanQRCodeView;
import in.woloo.www.v2.woloo.viewmodel.WolooViewModel;

public class ScanQRActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;

  /*  @BindView(R.id.scanner_view)
    CodeScannerView scanner_view;*/

    @BindView(R.id.tvTitle)
    TextView tvTitle;

  //  private CodeScanner mCodeScanner;

//    private ScanQRCodePresenter scanQRCodePresenter;
    WolooViewModel wolooViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        try{
//            scanQRCodePresenter = new ScanQRCodePresenter(this,ScanQRActivity.this);
            wolooViewModel = new ViewModelProvider(this).get(WolooViewModel.class);
            wolooViewModel.scanQRCode("woloo");
            tvTitle.setText(getResources().getString(R.string.scan_qr_code));
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public void startScanning(){
        try{
            /*mCodeScanner = new CodeScanner(this, scanner_view);
            mCodeScanner.startPreview();
            mCodeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull final Result result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });*/
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermission()) {
            startScanning();
        } else {
            requestPermission();
        }
    }

    @Override
    protected void onPause() {
     /*   if(mCodeScanner != null){
            mCodeScanner.releaseResources();
        }*/
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startScanning();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                   /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            showMessageOKCancel("You need to allow access permissions", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermission();
                                            }
                                        }
                                    });
                        }
                    }*/
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener onClickListener) {
        try{
            new AlertDialog.Builder(ScanQRActivity.this)
                    .setMessage(message)
                    .setPositiveButton("OK", onClickListener)
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }
}