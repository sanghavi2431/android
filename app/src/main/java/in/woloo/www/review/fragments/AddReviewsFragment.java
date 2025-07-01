package in.woloo.www.review.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.review.models.ReviewOptionsResponse;
import in.woloo.www.review.models.SubmitReviewResponse;
import in.woloo.www.review.mvp.AddReviewPresenter;
import in.woloo.www.review.mvp.AddReviewView;
import in.woloo.www.splash.AuthConfigResponse;
import in.woloo.www.utils.AppConstants;
import in.woloo.www.v2.data.remote.BaseResponse;
import in.woloo.www.v2.review.viewmodel.ReviewViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddReviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddReviewsFragment extends Fragment {


    private static final String TAG = AddReviewsFragment.class.getSimpleName();


    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.chipEditText)
    AppCompatEditText chipEditText;
    @BindView(R.id.fbReviews)
    FlexboxLayout fbReviews;
    @BindView(R.id.cgTags)
    ChipGroup cgTags;
    @BindView(R.id.cgRateYourExperience)
    ChipGroup cgRateYourExperience;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.ivVeryBad)
    ImageView ivVeryBad;
    @BindView(R.id.ivBad)
    ImageView ivBad;
    @BindView(R.id.ivAverage)
    ImageView ivAverage;
    @BindView(R.id.ivGood)
    ImageView ivGood;
    @BindView(R.id.ivLovedIt)
    ImageView ivLovedIt;

    @BindView(R.id.llReviewArea)
    LinearLayout llReviewArea;
    @BindView(R.id.llDynamicReview)
    LinearLayout llDynamicReview;

    @BindView(R.id.btnSubmit)
    TextView btnSubmit;
    @BindView(R.id.etReview)
    EditText etReview;

    @BindView(R.id.rlSuccessOverlay)
    RelativeLayout rlSuccessOverlay;

    @BindView(R.id.tvReviewText)
    TextView tvReviewText;

    @BindView(R.id.tvDynamicReviewTitle)
    TextView tvDynamicReviewTitle;


    private Chip lastSelectedChip = null;
    private ImageView lastSelectedRating = null;
    private ReviewOptionsResponse.Data reviewOptionsResponse = null;

    private int userRating = 0;
    private ArrayList<Integer> reviewOption = new ArrayList<Integer>();



    private int SpannedLength = 0,chipLength = 4;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int wolooId;
    private String mParam2;

    private ReviewViewModel reviewViewModel;

    public AddReviewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddReviewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddReviewsFragment newInstance(int param1, String param2) {
        AddReviewsFragment fragment = new AddReviewsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1) ;
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wolooId = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_reviews, container, false);
        ButterKnife.bind(this,root);
        initViews();
        setLiveData();
        return root;
    }

    private void initViews() {
        try{
            tvTitle.setText(getResources().getString(R.string.add_review));
            reviewOption.clear();
            reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
            reviewViewModel.getReviewOptions();
            chipEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId == EditorInfo.IME_ACTION_DONE) {
                         /*addNewChip(chipEditText.getText().toString());
                         chipEditText.setText("");*/
                    }
                    return false;
                }
            });

            ivBack.setOnClickListener(v -> {
                getActivity().onBackPressed();
            });

            ivVeryBad.setOnClickListener(v -> {
                 setRatingIcon(1);
            });

            ivBad.setOnClickListener(v -> {
                setRatingIcon(2);
            });

            ivAverage.setOnClickListener(v -> {
                setRatingIcon(3);
            });

            ivGood.setOnClickListener(v -> {
                setRatingIcon(4);
            });

            ivLovedIt.setOnClickListener(v -> {
                setRatingIcon(5);
            });

            btnSubmit.setOnClickListener(v -> {
                if(!reviewOption.isEmpty()){
                    new CommonUtils().showProgress(getContext());
                    reviewViewModel.submitReview(wolooId,(int)lastSelectedChip.getTag(),reviewOption,etReview.getText().toString());
                }
            });

        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    void setLiveData(){
        reviewViewModel.observeReviewOptions().observe(getViewLifecycleOwner(), new Observer<BaseResponse<ReviewOptionsResponse.Data>>() {
            @Override
            public void onChanged(BaseResponse<ReviewOptionsResponse.Data> response) {
                if(response!= null && response.getData() != null){
                    try {
                        reviewOptionsResponse = response.getData();
                        setChipsForExperience(reviewOptionsResponse.getRatingOption());
                    }catch(Exception ex){
                         CommonUtils.printStackTrace(ex);
                    }
                }else{
                    Toast.makeText(getContext(), WolooApplication.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    WolooApplication.setErrorMessage("");
                }
            }
        });

        reviewViewModel.observeSubmitReview().observe(getViewLifecycleOwner(), new Observer<BaseResponse<JSONObject>>() {
            @Override
            public void onChanged(BaseResponse<JSONObject> response) {
                new CommonUtils().hideProgress();
                if(response!= null && response.getSuccess()) {
                    showAddReviewSuccessDialog();
                }else{
                    Toast.makeText(getContext(), WolooApplication.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    WolooApplication.setErrorMessage("");
                }
            }
        });
    }

    private void setChipsForExperience(List<ReviewOptionsResponse.RatingOption> ratingOptionList) {
        try {
            addChipsForExperience(ratingOptionList);
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    public void setRatingIcon(int rating){
        try{
            if(lastSelectedRating != null){
                lastSelectedRating.setImageResource(R.drawable.ic_star);
            }
           switch (rating){
               case 1:
                   lastSelectedRating = ivVeryBad;
                   ivVeryBad.setImageResource(R.drawable.ic_rating_very_bad);
                   ivBad.setImageResource(R.drawable.ic_star);
                   ivAverage.setImageResource(R.drawable.ic_star);
                   ivGood.setImageResource(R.drawable.ic_star);
                   ivLovedIt.setImageResource(R.drawable.ic_star);
                   addChipsForImprovement(reviewOptionsResponse.getRatingImprovement());
                   tvReviewText.setText("Very Bad");
                   tvDynamicReviewTitle.setText(getText(R.string.tell_us_improve));
                   break;
               case 2:
                   lastSelectedRating = ivBad;
                   ivVeryBad.setImageResource(R.drawable.ic_rating_bad);
                   ivBad.setImageResource(R.drawable.ic_rating_bad);
                   ivAverage.setImageResource(R.drawable.ic_star);
                   ivGood.setImageResource(R.drawable.ic_star);
                   ivLovedIt.setImageResource(R.drawable.ic_star);
                   addChipsForImprovement(reviewOptionsResponse.getRatingImprovement());
                   tvReviewText.setText("Bad");
                   tvDynamicReviewTitle.setText(getText(R.string.tell_us_improve));
                   break;
               case 3:
                   lastSelectedRating = ivAverage;
                   ivVeryBad.setImageResource(R.drawable.ic_rating_average);
                   ivBad.setImageResource(R.drawable.ic_rating_average);
                   ivAverage.setImageResource(R.drawable.ic_rating_average);
                   ivGood.setImageResource(R.drawable.ic_star);
                   ivLovedIt.setImageResource(R.drawable.ic_star);
                   addChipsForImprovement(reviewOptionsResponse.getRatingImprovement());
                   tvReviewText.setText("Average");
                   tvDynamicReviewTitle.setText(getText(R.string.tell_us_improve));
                   break;
               case 4:
                   lastSelectedRating = ivGood;
                   ivVeryBad.setImageResource(R.drawable.ic_rating_good);
                   ivBad.setImageResource(R.drawable.ic_rating_good);
                   ivAverage.setImageResource(R.drawable.ic_rating_good);
                   ivGood.setImageResource(R.drawable.ic_rating_good);
                   ivLovedIt.setImageResource(R.drawable.ic_star);
                   addChipsForReview(reviewOptionsResponse.getRatingReview());
                   tvReviewText.setText("Good");
                   tvDynamicReviewTitle.setText(getText(R.string.tell_us_what_you_love));
                   break;
               case 5:
                   lastSelectedRating = ivLovedIt;
                   ivVeryBad.setImageResource(R.drawable.ic_rating_loved_it);
                   ivBad.setImageResource(R.drawable.ic_rating_loved_it);
                   ivAverage.setImageResource(R.drawable.ic_rating_loved_it);
                   ivGood.setImageResource(R.drawable.ic_rating_loved_it);
                   ivLovedIt.setImageResource(R.drawable.ic_rating_loved_it);
                   addChipsForReview(reviewOptionsResponse.getRatingReview());
                   tvReviewText.setText("Loved It");
                   tvDynamicReviewTitle.setText(getText(R.string.tell_us_what_you_love));
                   break;
               default:
                   break;
           }
           setRateYourExperience(rating);
        }catch (Exception ex){
            CommonUtils.printStackTrace(ex);
        }
    }

    private void setRateYourExperience(int rating) {
        try{
            for (int i=0; i< cgRateYourExperience.getChildCount();i++){
                Chip chip = (Chip)cgRateYourExperience.getChildAt(i);
                int id = (int) chip.getTag();
                if(rating == id){
                    setChipSelection(chip);
                }
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }


    private void addChipsForImprovement(List<ReviewOptionsResponse.RatingImprovement> ratingImprovements){
        try{
            if(cgTags != null){
                cgTags.removeAllViews();
                fbReviews.removeAllViews();
                reviewOption.clear();
                btnSubmit.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.blank_rectangle_shape));
            }
            for(ReviewOptionsResponse.RatingImprovement ratingImprovement : ratingImprovements){
                Chip chip = new Chip(getContext());
                chip.setText(ratingImprovement.getDisplayName());
                chip.setTag(ratingImprovement.getId());
                cgTags.addView((View)chip, fbReviews.getChildCount() - 1);

                chip.setOnClickListener(v -> {
                    addNewChip(chip.getText().toString(),(int)chip.getTag());
                    cgTags.removeView(chip);
                });
            }
        }catch (Exception ex) {
             CommonUtils.printStackTrace(ex);
        }
    }

    private void addChipsForReview(List<ReviewOptionsResponse.RatingReview> ratingReviews){
        try{
            if(cgTags != null){
                cgTags.removeAllViews();
                fbReviews.removeAllViews();
                reviewOption.clear();
                btnSubmit.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.blank_rectangle_shape));
            }
            for(ReviewOptionsResponse.RatingReview ratingReview : ratingReviews){
                Chip chip = new Chip(getContext());
                chip.setText(ratingReview.getDisplayName());
                chip.setTag(ratingReview.getId());
                cgTags.addView((View)chip, fbReviews.getChildCount() - 1);

                chip.setOnClickListener(v -> {
                    addNewChip(chip.getText().toString(),(int)chip.getTag());
                    cgTags.removeView(chip);
                });
            }
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    private void addChipsForExperience(List<ReviewOptionsResponse.RatingOption> experienceList){
        for(ReviewOptionsResponse.RatingOption ratingOption: experienceList){
            Chip chip = new Chip(getContext());
            chip.setText(ratingOption.getDisplayName());
            chip.setTag(ratingOption.getId());

            cgRateYourExperience.addView((View)chip, fbReviews.getChildCount() - 1);

            chip.setOnClickListener(v -> {
                cgTags.setVisibility(View.VISIBLE);
                setChipSelection(chip);
                setRatingIcon(((int)chip.getTag()));
                if(ratingOption.getOrder() == 1 || ratingOption.getOrder() == 2 || ratingOption.getOrder() == 3){
                    addChipsForImprovement(reviewOptionsResponse.getRatingImprovement());
                }else if(ratingOption.getOrder() == 4 || ratingOption.getOrder() == 5){
                    addChipsForReview(reviewOptionsResponse.getRatingReview());
                }
            });
        }
    }

    public void setChipSelection(Chip chip){
        try{
            if(lastSelectedChip != null){
                lastSelectedChip.setChipBackgroundColor(null);
            }
            chip.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.black)));
            chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.app_background)));
            lastSelectedChip = chip;

            llDynamicReview.setVisibility(View.VISIBLE);
            llReviewArea.setVisibility(View.VISIBLE);

        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }


    private void addNewChip(String chipText,int id) {
        Chip chip = new Chip(getContext());
        chip.setText(chipText);
        chip.setTag(id);
        chip.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.black)));
        chip.setChipBackgroundColor(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.app_background)));
        fbReviews.addView((View)chip, fbReviews.getChildCount() - 1);
        reviewOption.add(id);
        try{
            btnSubmit.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.yello_rectangle_shape));
        }catch (Exception ex){
             CommonUtils.printStackTrace(ex);
        }

        chip.setOnClickListener(v -> {
            /*String chipTxt = chip.getText().toString();
            int reviewId = (int)chip.getTag();
            fbReviews.removeView((View)chip);
            reviewOption.remove(reviewId);
            Chip removedChip = new Chip(getContext());
            removedChip.setText(chipTxt);
            cgTags.addView((View)removedChip, fbReviews.getChildCount() - 1);
            removedChip.setOnClickListener(view -> {
                addNewChip(removedChip.getText().toString(),reviewId);
                cgTags.removeView(removedChip);
            });*/
        });
    }

    Dialog dialog = null;
    public void showAddReviewSuccessDialog(){
        try {
            dialog = new Dialog(getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent_background)));
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setContentView(R.layout.dialog_share_review);
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            TextView tvOkay = (TextView) dialog.findViewById(R.id.tvOK);
            TextView tv_dialogreview = (TextView) dialog.findViewById(R.id.tv_dialogreview);

            AuthConfigResponse.Data authConfigResponse = CommonUtils.authconfig_response(getContext());
            if (authConfigResponse != null) {
                String addReviewSuccessDialogText = authConfigResponse.getcUSTOMMESSAGE().getAddReviewSuccessDialogText();
                tv_dialogreview.setText(addReviewSuccessDialogText.replaceAll("\\\\n","\n"));
            }

            tvOkay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent i = new Intent(getContext(), WolooDashboard.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    getActivity().finish();
                }
            });
            if(!dialog.isShowing()){
                dialog.show();
            }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
    }
}