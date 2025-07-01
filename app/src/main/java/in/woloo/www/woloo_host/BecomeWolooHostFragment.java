package in.woloo.www.woloo_host;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.utils.Logger;
import in.woloo.www.utils.Utility;
import in.woloo.www.woloo_host.mvp.BecomeWolooHostView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BecomeWolooHostFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BecomeWolooHostFragment extends Fragment implements BecomeWolooHostView {

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.VP_viewPager)
    ViewPager viewPager;

    @BindView(R.id.tutorial_slider_tab)
    TabLayout tutorial_slider_tab;

    @BindView(R.id.intrested_Btn)
    Button btnInterest;

    @BindView(R.id.tv_skip)
    TextView tvSkip;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BecomeWolooHostFragment() {
        // Required empty public constructor
    }
    public static String TAG= BecomeWolooHostFragment.class.getSimpleName();
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BecomeWolooHostFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BecomeWolooHostFragment newInstance(String param1, String param2) {
        BecomeWolooHostFragment fragment = new BecomeWolooHostFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    /*calling on onCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Logger.i(TAG, "onCreate");
    }
    /*calling on onCreateView*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_become_woloo_host, container, false);
        ButterKnife.bind(this,root);
        initViews();
        Logger.i(TAG, "onCreateView");
        return root;
    }
    /*calling on initViews*/
    private void initViews() {
        try{
            Logger.i(TAG, "initViews");
              tvTitle.setText(getText(R.string.become_a_woloo_host));
            ivBack.setOnClickListener(v -> {
                //getActivity().onBackPressed();
                FragmentManager fm = requireActivity().getSupportFragmentManager();
                fm.popBackStack();
            });

            ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity());
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(0);
            tutorial_slider_tab.setupWithViewPager(viewPager);
            btnInterest.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                Utility.logFirebaseEvent(getActivity(), bundle, AppConstants.YES_INTERESTED_CLICK);
                HashMap<String,Object> payload = new HashMap<>();
                Utility.logNetcoreEvent(getActivity(),payload,AppConstants.YES_INTERESTED_CLICK);

                startActivity(new Intent(getContext(), CreateWolooHostActivity.class));
            });
            tvSkip.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                Utility.logFirebaseEvent(getActivity(), bundle, AppConstants.YES_INTERESTED_CLICK);
                HashMap<String,Object> payload = new HashMap<>();
                Utility.logNetcoreEvent(getActivity(),payload,AppConstants.YES_INTERESTED_CLICK);

                startActivity(new Intent(getContext(), CreateWolooHostActivity.class));
            });
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    public class ViewPagerAdapter extends PagerAdapter {
        private Context mContext;

        public ViewPagerAdapter(Context context) {
            mContext = context;
        }
        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.layout.slider_1;
                    break;
                case 1:
                    resId = R.layout.slider_2;

                    break;
                case 2:
                    resId = R.layout.slider_3;
                    break;

                case 3:
                    resId = R.layout.slider_4;
                    break;
            }
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup layout = (ViewGroup) inflater.inflate(resId, collection, false);
            collection.addView(layout);
            return layout;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}