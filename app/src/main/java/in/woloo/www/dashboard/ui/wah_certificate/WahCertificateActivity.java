package in.woloo.www.dashboard.ui.wah_certificate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.ui.wah_certificate.model.WahCertificateDetailsResponse;
import in.woloo.www.dashboard.ui.wah_certificate.mvp.WahCertificatePresenter;
import in.woloo.www.dashboard.ui.wah_certificate.mvp.WahCertificateView;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.login.SplashActivity;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.woloo.model.WahCertificateResponse;
import in.woloo.www.v2.woloo.viewmodel.WolooViewModel;
import in.woloo.www.woloo_host.CreateWolooHostFragment;
import in.woloo.www.woloo_host.mvp.CreateWolooHostPresenter;

public class WahCertificateActivity extends AppCompatActivity {
    protected static SharedPreference mSharedPreference;
    private WahCertificatePresenter wahCertificatePresenter;
    private WolooViewModel wolooViewModel;

    @BindView(R.id.wolooNameTv)
    TextView wolooNameTv;

    @BindView(R.id.CertificateTv)
    TextView CertificateTv;

    @BindView(R.id.DateOfCreationTv)
    TextView DateOfCreationTv;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wah_certificate);
        ButterKnife.bind(this);
        if (mSharedPreference == null) {
            mSharedPreference = new SharedPreference(this);
        }
        String wah_certificate = mSharedPreference.getStoredPreference(this, SharedPreferencesEnum.WAH_CERTIFICATE_CODE.getPreferenceKey(),"");
        ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
        wolooViewModel = new ViewModelProvider(this).get(WolooViewModel.class);
        wolooViewModel.wahCertificate(wah_certificate);
        mSharedPreference.setStoredPreference(this, SharedPreferencesEnum.WAH_CERTIFICATE_CODE.getPreferenceKey(), "");
        setLiveData();
    }

    void setLiveData(){
        wolooViewModel.observewWahCertificate().observe(this, new Observer<BaseResponse<WahCertificateResponse>>() {
            @Override
            public void onChanged(BaseResponse<WahCertificateResponse> wahCertificateResponse) {
                if(wahCertificateResponse != null){
                    try {
                        wolooNameTv.setText(wahCertificateResponse.getData().getName());
                        CertificateTv.setText(wahCertificateResponse.getData().getCode());
                        String inputPattern = "yyyy-MM-dd";
                        String outputPattern = "dd MMM yyyy";
                        Date date = new SimpleDateFormat(inputPattern).parse(wahCertificateResponse.getData().getCreatedAt());
                        DateOfCreationTv.setText(new SimpleDateFormat(outputPattern).format(date));
                    } catch (ParseException e) {
                        CommonUtils.printStackTrace(e);
                    }
                }else{
                    Toast.makeText(WahCertificateActivity.this, WolooApplication.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    WolooApplication.setErrorMessage("");
                }
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    public void setWahCertificateResponse(WahCertificateDetailsResponse wahCertificateResponse) {
        try {
        wolooNameTv.setText(wahCertificateResponse.getData().getName());
        CertificateTv.setText(wahCertificateResponse.getData().getCode());
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd MMM yyyy";
        Date date = new SimpleDateFormat(inputPattern).parse(wahCertificateResponse.getData().getCreatedAt());
        DateOfCreationTv.setText(new SimpleDateFormat(outputPattern).format(date));
        } catch (ParseException e) {
              CommonUtils.printStackTrace(e);
        }
    }
}