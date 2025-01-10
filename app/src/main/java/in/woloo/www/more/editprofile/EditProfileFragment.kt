package `in`.woloo.www.more.editprofile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.editprofile.profile.model.UserProfile
import `in`.woloo.www.more.editprofile.profile.viewmodel.ProfileViewModel
import `in`.woloo.www.utils.Logger
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * A simple [Fragment] subclass.
 * Use the [EditProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment(), View.OnClickListener,
    RadioGroup.OnCheckedChangeListener {
    @JvmField
    @BindView(R.id.tvTitle)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: ImageView? = null

    @JvmField
    @BindView(R.id.etName)
    var etName: EditText? = null

    @JvmField
    @BindView(R.id.etEmail)
    var etEmail: EditText? = null

    @JvmField
    @BindView(R.id.etMobile)
    var etMobile: EditText? = null

    @JvmField
    @BindView(R.id.etCity)
    var etCity: TextView? = null

    @JvmField
    @BindView(R.id.etPincode)
    var etPincode: EditText? = null

    @JvmField
    @BindView(R.id.etAddress)
    var etAddress: EditText? = null

    @JvmField
    @BindView(R.id.rgGender)
    var rgGender: RadioGroup? = null

    @JvmField
    @BindView(R.id.rbMale)
    var rbMale: RadioButton? = null

    @JvmField
    @BindView(R.id.rbFemale)
    var rbFemale: RadioButton? = null

    @JvmField
    @BindView(R.id.tvSubmit)
    var tvSubmit: TextView? = null

    private var gender = ""

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var profileViewModel: ProfileViewModel? = null

    /*calling onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.i(TAG, "onCreate")
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    /*calling onCreateView*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Logger.i(TAG, "onCreateView")
        val rooView = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        ButterKnife.bind(this, rooView)
        initView()
        return rooView
    }

    /*calling initView*/
    private fun initView() {
        Logger.i(TAG, "initView")
        try {
            profileViewModel = ViewModelProvider(this).get(
                ProfileViewModel::class.java
            )
            val viewProfileResponse: UserProfile =
                WolooApplication.instance!!.profileResponse!!
            etName!!.setText(viewProfileResponse.profile!!.name)
            etEmail!!.setText(viewProfileResponse.profile!!.email)
            etMobile!!.setText(viewProfileResponse.profile!!.mobile)
            etCity!!.text = viewProfileResponse.profile!!.city
            etPincode!!.setText(viewProfileResponse.profile!!.pincode)
            etAddress!!.setText(viewProfileResponse.profile!!.address)
            if (viewProfileResponse.profile!!.gender.equals("Male", ignoreCase = true)) {
                rbMale!!.isChecked = true
            }
            if (viewProfileResponse.profile!!.gender.equals("Female", ignoreCase = true)) {
                rbFemale!!.isChecked = true
            }
            tvTitle!!.text = getString(R.string.edit_profile)
            ivBack!!.setOnClickListener { v: View? ->
                // getActivity().onBackPressed();
                val fm = requireActivity().supportFragmentManager
                fm.popBackStack()
            }
            tvSubmit!!.setOnClickListener { v: View -> this.onClick(v) }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling onClick*/
    override fun onClick(v: View) {
        Logger.i(TAG, "onClick")

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
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("id", CommonUtils().userInfo!!.id.toString())
            .addFormDataPart("name", etName!!.text.toString())
            .addFormDataPart("email", etEmail!!.text.toString())
            .addFormDataPart("address", etAddress!!.text.toString())
            .addFormDataPart("city", etCity!!.text.toString())
            .addFormDataPart("pincode", etPincode!!.text.toString())
            .addFormDataPart("gender", gender)
            .build()

        Log.d(
            TAG,
            "onClick$requestBody"
        )

        profileViewModel!!.updateProfile(requestBody)
    }

    /*calling onCheckedChanged*/
    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        Logger.i(TAG, "onCheckedChanged")
        when (checkedId) {
            R.id.rbMale -> gender = "Male"
            R.id.rbFemale -> gender = "Female"
            else -> {}
        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        var TAG: String = EditProfileFragment::class.java.simpleName

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): EditProfileFragment {
            val fragment = EditProfileFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}