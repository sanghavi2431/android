package in.woloo.www.woloo_info.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.common.CommonUtils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GoBindasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoBindasFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.ivAppLogo)
    ImageView ivAppLogo;
    @BindView(R.id.llContentLayoutOne)
    LinearLayout llContentLayoutOne;
    @BindView(R.id.llContentLayoutTwo)
    LinearLayout llContentLayoutTwo;


    public GoBindasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoBindasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoBindasFragment newInstance(String param1, String param2) {
        GoBindasFragment fragment = new GoBindasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_go_bindas, container, false);
        ButterKnife.bind(this,root);
        initView();
        return root;
    }

    private void initView() {
        try{
            Animation aniSlide = AnimationUtils.loadAnimation(getContext(),R.anim.bottomtoup);
            ivAppLogo.startAnimation(aniSlide);
            aniSlide.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    ivAppLogo.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            Animation aniSlideone = AnimationUtils.loadAnimation(getContext(),R.anim.bottomtoup);
            llContentLayoutOne.startAnimation(aniSlideone);

            aniSlideone.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    llContentLayoutOne.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            Animation aniSlidetwo = AnimationUtils.loadAnimation(getContext(),R.anim.bottomtoup);
            llContentLayoutTwo.startAnimation(aniSlidetwo);

            aniSlidetwo.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    llContentLayoutTwo.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }
}