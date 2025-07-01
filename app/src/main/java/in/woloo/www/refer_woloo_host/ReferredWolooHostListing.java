package in.woloo.www.refer_woloo_host;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.database.preference.SharedPreference;
import in.woloo.www.database.preference.SharedPreferencesEnum;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.refer_woloo_host.Adapters.ReferredWolooHostListingAdapter;
import in.woloo.www.refer_woloo_host.model.ReferredWolooListResponse;
import in.woloo.www.refer_woloo_host.mvp.ReferredWolooPresenter;
import in.woloo.www.refer_woloo_host.mvp.ReferredWolooView;
import in.woloo.www.search.fragments.WolooSearchFragment;
import in.woloo.www.search.mvp.WolooSearchPresenter;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.v2.data.local.SharedPrefSettings;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.woloo.viewmodel.WolooViewModel;
import in.woloo.www.woloo_host.model.GeoCodeResponse;

public class ReferredWolooHostListing extends AppCompatActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.referWolooTv)
    TextView referWolooTv;

    @BindView(R.id.referred_Woloo_host_rv)
    RecyclerView referred_Woloo_host_rv;

    @BindView(R.id.referWolooImv)
    ImageView referWolooImv;

    private ReferredWolooHostListingAdapter referredWolooHostListingAdapter;
    private WolooViewModel wolooViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refered_woloo_host_listing);
        ButterKnife.bind(this);
        wolooViewModel = new ViewModelProvider(this).get(WolooViewModel.class);
        initView();
        setLiveData();
    }

    private void initView() {
        tvTitle.setText("Refer a Woloo Host");
        ivBack.setOnClickListener(v -> {
            onBackPressed();
        });
        SharedPreference mSharedPref = new SharedPreference(this);
        AuthConfigResponse.Data authConfigResponse = SharedPrefSettings.Companion.getGetPreferences().fetchAuthConfig();
        referWolooTv.setText(authConfigResponse.getcUSTOMMESSAGE().getWolooReferHostText());
        referWolooImv.setOnClickListener(v -> {
            startActivity(new Intent(this, referWolooFormActivity.class));
        });
    }

    void setLiveData(){
        wolooViewModel.observeRecommendWolooList().observe(this, new Observer<BaseResponse<ArrayList<ReferredWolooListResponse.DataItem>>>() {
            @Override
            public void onChanged(BaseResponse<ArrayList<ReferredWolooListResponse.DataItem>> referredWolooListResponse) {
                if(referredWolooListResponse != null && referredWolooListResponse.getData() != null) {
                    if (referredWolooListResponse.getData().size() > 0) {
                        referred_Woloo_host_rv.setVisibility(View.VISIBLE);
                        referredWolooHostListingAdapter = new ReferredWolooHostListingAdapter(ReferredWolooHostListing.this, referredWolooListResponse.getData());
                        referred_Woloo_host_rv.setHasFixedSize(true);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                        referred_Woloo_host_rv.setLayoutManager(linearLayoutManager);
                        referred_Woloo_host_rv.setAdapter(referredWolooHostListingAdapter);
                        int underReviewCnt = 0;
                        for (int i = 0; i < referredWolooListResponse.getData().size(); i++) {
                            if (referredWolooListResponse.getData().get(i).getStatus() == 0)
                                underReviewCnt++;
                        }
                        if (underReviewCnt >= 3)
                            referWolooImv.setVisibility(View.GONE);
                        else
                            referWolooImv.setVisibility(View.VISIBLE);
                    } else
                        referred_Woloo_host_rv.setVisibility(View.GONE);
                }else{
                    WolooApplication.setErrorMessage("");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        wolooViewModel.getRecommendWolooList();
    }
}