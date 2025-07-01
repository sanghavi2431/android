package in.woloo.www.editprofile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextClassifierEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.woloo.www.R;
import in.woloo.www.app.WolooApplication;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.editprofile.mvp.EditProfilePresenter;
import in.woloo.www.editprofile.mvp.EditProfileView;
import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.networksUtils.JSONTagConstant;
import in.woloo.www.utils.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentEditProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentEditProfile extends Fragment implements EditProfileView {

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
    EditText etCity;

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
    private EditProfilePresenter editProfilePresenter;

    public FragmentEditProfile() {
        // Required empty public constructor
    }
    public static String TAG= FragmentEditProfile.class.getSimpleName();
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentEditProfile.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentEditProfile newInstance(String param1, String param2) {
        FragmentEditProfile fragment = new FragmentEditProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    /*calling onCreate*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Logger.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
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
        View rootView = inflater.inflate(R.layout.fragment_edit_profile_new, container, false);
        ButterKnife.bind(this,rootView);
        initViews();
        return rootView;
    }
    /*calling initViews*/
    private void initViews() {
        Logger.i(TAG, "initViews");
        try{
            tvSubmit.setOnClickListener(v -> {
                Toast.makeText(getActivity().getApplicationContext(),"Clicked",Toast.LENGTH_SHORT).show();
            });
        }catch(Exception ex){
             CommonUtils.printStackTrace(ex);
        }
    }

    @Override
    public void editProfileSuccess() {

    }
}