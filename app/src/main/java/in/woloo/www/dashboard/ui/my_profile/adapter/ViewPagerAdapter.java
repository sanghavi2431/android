package in.woloo.www.dashboard.ui.my_profile.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import in.woloo.www.dashboard.ui.my_profile.fragments.PersonalInfoFragment;
import in.woloo.www.dashboard.ui.my_profile.fragments.SOSNumberFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;


    public ViewPagerAdapter(Context context,FragmentManager fm,int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PersonalInfoFragment personalInfoFragment = new PersonalInfoFragment().newInstance("","");
                return personalInfoFragment;
            case 1:
                SOSNumberFragment sosNumberFragment = new SOSNumberFragment().newInstance("","");
                return sosNumberFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
