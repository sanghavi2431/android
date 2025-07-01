package in.woloo.www.invite_friend.fragments.contacts;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.giftSubscription.GiftSubscriptionActivity;
import in.woloo.www.invite_friend.fragments.ImportContactsAsync;
import in.woloo.www.invite_friend.fragments.InviteContactsFragments;
import in.woloo.www.invite_friend.fragments.InviteFriendFragment;
import in.woloo.www.subscribe.fragments.SubscribeFragment;


        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentManager;
        import androidx.fragment.app.FragmentTransaction;

        import android.os.Bundle;
        import android.widget.FrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
        import butterknife.ButterKnife;
        import in.woloo.www.R;
        import in.woloo.www.subscribe.fragments.SubscribeFragment;
import in.woloo.www.utils.AppConstants;
import jagerfield.mobilecontactslibrary.Contact.Contact;

public class InviteContactsActivity extends AppCompatActivity {

    @BindView(R.id.flFragments)
    FrameLayout flFragments;
    private String contactlist="";
    String refcode="";
    boolean isGiftSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invitecontactslayout);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        //contactlist= getIntent().getStringExtra("ARRAYLIST");
        refcode=getIntent().getStringExtra(AppConstants.REFCODE);
        isGiftSub = getIntent().hasExtra("isGiftSub");
        loadFragment(new InviteContactsFragments().newInstance(refcode,isGiftSub));
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