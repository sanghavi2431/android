package `in`.woloo.www.more.editprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.editprofile.mvp.EditProfilePresenter
import `in`.woloo.www.more.editprofile.mvp.EditProfileView
import `in`.woloo.www.utils.Logger

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentEditProfile.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentEditProfile : Fragment(), EditProfileView {
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
    var etCity: EditText? = null

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

    private val gender = ""

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private val editProfilePresenter: EditProfilePresenter? = null

    /*calling onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.i(TAG, "onCreate")
        super.onCreate(savedInstanceState)
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
        val rootView = inflater.inflate(R.layout.fragment_edit_profile_new, container, false)
        ButterKnife.bind(this, rootView)
        initViews()
        return rootView
    }

    /*calling initViews*/
    private fun initViews() {
        Logger.i(TAG, "initViews")
        try {
            tvSubmit!!.setOnClickListener { v: View? ->
                Toast.makeText(
                    requireActivity().applicationContext, "Clicked", Toast.LENGTH_SHORT
                ).show()
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    override fun editProfileSuccess() {
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        var TAG: String = FragmentEditProfile::class.java.simpleName

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentEditProfile.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String?, param2: String?): FragmentEditProfile {
            val fragment = FragmentEditProfile()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}