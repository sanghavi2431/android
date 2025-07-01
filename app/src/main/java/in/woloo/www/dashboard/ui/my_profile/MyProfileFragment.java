package in.woloo.www.dashboard.ui.my_profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.ui.my_account.MyAccountFragment;
import in.woloo.www.dashboard.ui.my_account.MyAccountViewModel;
import in.woloo.www.dashboard.ui.my_profile.adapter.ViewPagerAdapter;
import in.woloo.www.utils.Logger;


public class MyProfileFragment extends Fragment {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private static final String TAG = MyProfileFragment.class.getSimpleName();

    private MyProfileViewModel myProfileViewModel;
    /*Calling on onCreateView*/
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Logger.i(TAG, "onCreateView");
        myProfileViewModel =
                new ViewModelProvider(this).get(MyProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_profile, container, false);
        ButterKnife.bind(this,root);
        initView();
        myProfileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        return root;
    }
    /*Calling on initView*/
    private void initView() {
        try{
            Logger.i(TAG, "initView");
            tabLayout.addTab(tabLayout.newTab().setText("Personal Info"));
            tabLayout.addTab(tabLayout.newTab().setText("sos number"));

            final ViewPagerAdapter adapter = new ViewPagerAdapter(getContext(),getFragmentManager(),tabLayout.getTabCount());
            viewPager.setAdapter(adapter);

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }
}