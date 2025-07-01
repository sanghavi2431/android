package in.woloo.www.editprofile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.editprofile.mvp.EditProfilePresenter;
import in.woloo.www.editprofile.mvp.EditProfileView;
import in.woloo.www.more.models.UserProfileMergedResponse;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.utils.Logger;
import in.woloo.www.v2.profile.model.UserProfile;
import in.woloo.www.v2.profile.viewmodel.ProfileViewModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfileFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.etName)
    EditText etName;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etMobile)
    EditText etMobile;

    @BindView(R.id.etCity)
    TextView etCity;

    @BindView(R.id.etPincode)
    EditText etPincode;

    @BindView(R.id.etAddress)
    EditText etAddress;

    @BindView(R.id.rgGender)
    RadioGroup rgGender;

    @BindView(R.id.rbMale)
    RadioButton rbMale;

    @BindView(R.id.rbFemale)
    RadioButton rbFemale;

    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    private String gender = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProfileViewModel profileViewModel;
    public static String TAG = EditProfileFragment.class.getSimpleName();

    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /*calling onCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.i(TAG, "onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    /*calling onCreateView*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Logger.i(TAG, "onCreateView");
        View rooView = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, rooView);
        initView();
        return rooView;
    }

    /*calling initView*/
    private void initView() {
        Logger.i(TAG, "initView");
        try {
            profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
            UserProfile viewProfileResponse = WolooApplication.getInstance().getProfileResponse();
            etName.setText(viewProfileResponse.getProfile().getName());
            etEmail.setText(viewProfileResponse.getProfile().getEmail());
            etMobile.setText(viewProfileResponse.getProfile().getMobile());
            etCity.setText(viewProfileResponse.getProfile().getCity());
            etPincode.setText(viewProfileResponse.getProfile().getPincode());
            etAddress.setText(viewProfileResponse.getProfile().getAddress());
            if (viewProfileResponse.getProfile().getGender().equalsIgnoreCase("Male")) {
                rbMale.setChecked(true);
            }
            if (viewProfileResponse.getProfile().getGender().equalsIgnoreCase("Female")) {
                rbFemale.setChecked(true);
            }
            tvTitle.setText(getString(R.string.edit_profile));
            ivBack.setOnClickListener(v -> {
                // getActivity().onBackPressed();
                FragmentManager fm = requireActivity().getSupportFragmentManager();
                fm.popBackStack();
            });
            tvSubmit.setOnClickListener(this::onClick);
        } catch (Exception ex) {
            CommonUtils.printStackTrace(ex);
        }
    }

    /*calling onClick*/
    @Override
    public void onClick(View v) {
        Logger.i(TAG, "onClick");
//            JSONObject mJsObjParam = new JSONObject();
//            try {
//                mJsObjParam.put(JSONTagConstant.NAME,etName.getText().toString());
//                mJsObjParam.put(JSONTagConstant.CITY,etCity.getText().toString());
//                mJsObjParam.put(JSONTagConstant.PINCODE,etPincode.getText().toString());
//                mJsObjParam.put(JSONTagConstant.ADDRESS,etAddress.getText().toString());
//                mJsObjParam.put(JSONTagConstant.GENDER,gender);
//            } catch (Exception e) {
//                  CommonUtils.printStackTrace(e)
//            }
//            editProfilePresenter.editProfile(getContext(),mJsObjParam);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("id", new CommonUtils().getUserInfo().getId().toString())
                .addFormDataPart("name", etName.getText().toString())
                .addFormDataPart("email", etEmail.getText().toString())
                .addFormDataPart("address", etAddress.getText().toString())
                .addFormDataPart("city", etCity.getText().toString())
                .addFormDataPart("pincode", etPincode.getText().toString())
                .addFormDataPart("gender", gender)
                .build();

        Log.d(TAG, "onClick" + requestBody);

        profileViewModel.updateProfile(requestBody);

    }

    /*calling onCheckedChanged*/
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Logger.i(TAG, "onCheckedChanged");
        switch (checkedId) {
            case R.id.rbMale:
                gender = "Male";
                break;
            case R.id.rbFemale:
                gender = "Female";
                break;
            default:
                break;
        }
    }
}