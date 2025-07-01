package in.woloo.www.invite_friend.fragments.contacts;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.invite_friend.fragments.EnterMessageFragment;
import in.woloo.www.invite_friend.fragments.InviteContactsFragments;


        import android.content.Intent;
        import android.os.Bundle;
        import android.widget.FrameLayout;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentManager;
        import androidx.fragment.app.FragmentTransaction;

        import butterknife.BindView;
        import butterknife.ButterKnife;
        import in.woloo.www.R;
        import in.woloo.www.invite_friend.fragments.InviteContactsFragments;
        import in.woloo.www.subscribe.fragments.SubscribeFragment;


        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentManager;
        import androidx.fragment.app.FragmentTransaction;

        import android.os.Bundle;
        import android.widget.FrameLayout;

        import butterknife.BindView;
        import butterknife.ButterKnife;
        import in.woloo.www.R;
        import in.woloo.www.subscribe.fragments.SubscribeFragment;
import in.woloo.www.utils.AppConstants;

public class EnterMessage extends AppCompatActivity {

    @BindView(R.id.flFragments)
    FrameLayout flFragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entermessage);
        ButterKnife.bind(this);
        initViews();

    }

    private void initViews() {
        try{
            loadFragment(new EnterMessageFragment().newInstance(""+getIntent().getStringExtra("mobilenumber"),""+getIntent().getStringExtra("name"),""+getIntent().getStringExtra(AppConstants.REFCODE)));
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    public void loadFragment(Fragment fragment){
        try{
            FragmentManager fragmentManager = ((AppCompatActivity) this).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.flFragments,fragment);
            fragmentTransaction.commit();
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

}