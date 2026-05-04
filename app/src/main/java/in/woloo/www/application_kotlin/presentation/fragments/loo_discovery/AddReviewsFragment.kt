package `in`.woloo.www.application_kotlin.presentation.fragments.loo_discovery

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.Slider
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.model.CreditCoinsRequest
import `in`.woloo.www.application_kotlin.model.server_response.ReviewOptionsResponse
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.AddReviewActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.AddReviewActivity.Companion
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.AddReviewActivity.Companion.mSharedPreference
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard.Companion.mSharedPreference
import `in`.woloo.www.application_kotlin.utilities.RainbowSlider
import `in`.woloo.www.application_kotlin.view_models.ReviewViewModel
import `in`.woloo.www.application_kotlin.view_models.WolooHostViewModel
import `in`.woloo.www.application_kotlin.view_models.WolooViewModel
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat

class AddReviewsFragment : Fragment() {

    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: LinearLayout? = null

    @JvmField
    @BindView(R.id.ivVeryBad)
    var ivVeryBad: ImageView? = null

    @JvmField
    @BindView(R.id.ivBad)
    var ivBad: ImageView? = null

    @JvmField
    @BindView(R.id.ivAverage)
    var ivAverage: ImageView? = null

    @JvmField
    @BindView(R.id.ivGood)
    var ivGood: ImageView? = null

    @JvmField
    @BindView(R.id.ivLovedIt)
    var ivLovedIt: ImageView? = null


    @JvmField
    @BindView(R.id.write_review_layout)
    var writeReviewLayout: RelativeLayout? = null


    @JvmField
    @BindView(R.id.btnSubmit)
    var btnSubmit: TextView? = null

    @JvmField
    @BindView(R.id.show_cibil_score_text)
    var showCibilScoreText: TextView? = null

    @JvmField
    @BindView(R.id.etReview)
    var etReview: EditText? = null

    @JvmField
    @BindView(R.id.discrete_slider)
    var discreteSlider: RainbowSlider? = null


    @JvmField
    @BindView(R.id.wah_image_display)
    var wahImage: ImageView? = null

    @JvmField
    @BindView(R.id.powder_woloo_name)
    var powderRoomName : TextView? = null

    @JvmField
    @BindView(R.id.btnSkip)
    var btnSkip : TextView? = null



    var wah_certificate: String = ""
    var wah_statusForPopup : Int = 0
    private var lastSelectedRating: ImageView? = null
    private val reviewOption = ArrayList<Int>()
    private var wolooId = 0
    private var mParam2: String? = null
    private var reviewViewModel: ReviewViewModel? = null
    private var wolooViewModel: WolooHostViewModel? = null
    private var wolooViewModel1: WolooViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            wolooId = requireArguments().getInt(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.fragment_add_reviews, container, false)
        ButterKnife.bind(this, root)


        Glide.with(requireActivity().applicationContext)
            .load(R.drawable.wah_good_3)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(27)))
            .into(wahImage!!)

      discreteSlider?.addOnChangeListener { slider, value, fromUser ->
          // Update the TextView with the current value of the slider (snap to nearest step)
          Log.d("Value is on change", "Slider value: ${slider.value.toInt()}")
          Logger.i(HomeCategoryFragment.TAG, "onCreateView2 ")
          if(slider.value.toInt() == 1)
          {
              setRatingIcon(1)
          }
          else if(slider.value.toInt() == 2)
          {
              setRatingIcon(2)
          }
          else if(slider.value.toInt() == 3)
          {
              setRatingIcon(3)
          }
          else if(slider.value.toInt() == 4)
          {
              setRatingIcon(4)
          }
          else if(slider.value.toInt() == 5)
          {
              setRatingIcon(5)
          }

      }


        reviewViewModel = ViewModelProvider(this).get<ReviewViewModel>(
            ReviewViewModel::class.java
        )
        wolooViewModel = ViewModelProvider(this).get<WolooHostViewModel>(
            WolooHostViewModel::class.java
        )

        wolooViewModel1 = ViewModelProvider(this).get<WolooViewModel>(
            WolooViewModel::class.java
        )

        wah_certificate = mSharedPreference?.getStoredPreference(
            requireContext(),
            SharedPreferencesEnum.WAH_CERTIFICATE_CODE.preferenceKey,
            ""
        ).toString()
        Log.i("Aarati Dynamic link 2" , wah_certificate)
        if(wah_certificate.isNotEmpty())
        {
            btnSkip!!.visibility = View.VISIBLE
            wolooViewModel!!.wahCertificate(wah_certificate)
        }
        else {

            val pwdcode: String = mSharedPreference?.getStoredPreference(
                requireContext(),
                SharedPreferencesEnum.POWDER_ROOM_CODE.preferenceKey,
                ""
            ).toString()

            Log.i("Aarati Dynamic link 2" , pwdcode)
            if(pwdcode.isNotEmpty()) {
                btnSkip!!.visibility = View.VISIBLE
                wolooViewModel!!.wahCertificate(pwdcode)
            }
        }

        btnSkip!!.setOnClickListener{
            mSharedPreference?.setStoredPreference(
                requireContext(),
                SharedPreferencesEnum.WAH_CERTIFICATE_CODE.preferenceKey,
                ""
            ).toString()
            mSharedPreference?.setStoredPreference(
                requireContext(),
                SharedPreferencesEnum.POWDER_ROOM_CODE.preferenceKey,
                ""
            ).toString()

            val i = Intent(context, WolooDashboard::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(i)
            requireActivity().finish()

        }

     //   reviewViewModel?.getReviewOptions()
        setLiveData()
        ivBack?.setOnClickListener {
          activity?.onBackPressed() }

        ivVeryBad?.setOnClickListener { setRatingIcon(1) }
        ivBad?.setOnClickListener { setRatingIcon(2) }
        ivAverage?.setOnClickListener { setRatingIcon(3) }
        ivGood?.setOnClickListener { setRatingIcon(4) }
        ivLovedIt?.setOnClickListener { setRatingIcon(5) }
        btnSubmit?.setOnClickListener {
                CommonUtils().showProgress(requireContext())
            btnSubmit?.background = ContextCompat.getDrawable(requireActivity().applicationContext , R.drawable.new_button_onclick_background)
            discreteSlider?.value?.let { it1 ->
                if (it1 == 0f) {
                    reviewViewModel?.submitReview(
                        wolooId,
                        1,
                        reviewOption,
                        etReview?.text.toString()
                    )
                }
                else {

                    reviewViewModel?.submitReview(
                        wolooId,
                        it1.toInt(),
                        reviewOption,
                        etReview?.text.toString()
                    )
                }
            }
         
        }
        writeReviewLayout?.setOnClickListener {
            if(etReview?.visibility == View.GONE) {
                etReview?.visibility = View.VISIBLE
            }
        }

        var  cibilScore : String = CommonUtils.authconfig_response(requireActivity())!!.getCibilRangesSaved()!!.threeRange.toString()
        if(requireArguments().getString("RANGE")!!.isNotEmpty())
             cibilScore = requireArguments().getString("RANGE")!!
        if(cibilScore.isNotEmpty())
        showCibilScoreText!!.text = cibilScore!!.substringAfter("-").trim()
        if(requireArguments().getString("RATINGS")!!.isNotEmpty()) {
            setRatingIcon(Integer.parseInt(requireArguments().getString("RATINGS")!!))
        }
        return root
    }



    fun setLiveData() {

        reviewViewModel?.observeSubmitReview()
            ?.observe(viewLifecycleOwner, Observer<BaseResponse<JSONObject>> { response ->
                CommonUtils().hideProgress()
                if (response != null && response.success) {
                    showAddReviewSuccessDialog()
                } else {
                    Toast.makeText(context, WolooApplication.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                    WolooApplication.errorMessage = ""
                }
            })

        wolooViewModel!!.observewWahCertificate().observe(viewLifecycleOwner) { wahCertificateResponse ->
            if (wahCertificateResponse != null) {
                try {
                    powderRoomName!!.visibility = View.VISIBLE
                    powderRoomName!!.text = wahCertificateResponse.data?.wolooObject?.name!!.toString()
                    showCibilScoreText!!.text = wahCertificateResponse.data?.wolooObject?.cibil_score?.substringAfter("-")?.trim().toString()
                    setRatingIcon(wahCertificateResponse.data?.wolooObject?.rating!!)
                    if(wahCertificateResponse.data?.statusForPopup ==1) {
                       wah_statusForPopup = 1
                        showSuccessDialog1(100)
                    }

                } catch (e: ParseException) {
                    CommonUtils.printStackTrace(e)
                }
            }
        }

        wolooViewModel1?.observeAddCoinstoWolooUser()
            ?.observe(viewLifecycleOwner, Observer<BaseResponse<JSONObject>> { response ->
                // CommonUtils().hideProgress()
                try {
                    if(response!=null) {
                        showSuccessDialog(
                            CommonUtils.authconfig_response(requireContext()).getRateAToilet()!!
                                .toInt()
                        )
                        var bundle = Bundle()
                        var payload = HashMap<String, Any>()
                        bundle.putString(AppConstants.RATE_TOILET_CLICK, wolooId.toString())
                        bundle.putString(AppConstants.RATE_TOILET_CLICK, wolooId.toString())
                        payload[AppConstants.RATE_TOILET_CLICK] = wolooId.toString()
                        payload[AppConstants.RATE_TOILET_CLICK] = wolooId.toString()
                        logFirebaseEvent(activity, bundle, AppConstants.RATE_TOILET_CLICK)
                        logNetcoreEvent(requireActivity(), payload, AppConstants.RATE_TOILET_CLICK)
                    }
                    else{
                        val i = Intent(context, WolooDashboard::class.java)
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(i)
                        requireActivity().finish()
                    }
                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            })



    }

    fun showSuccessDialog(value: Int)
    {
        try {
            val dialog = Dialog(requireActivity())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_coins_success)
            val displayMetrics = Resources.getSystem().displayMetrics
            val screenWidth = displayMetrics.widthPixels

// Calculate 80% of screen width
            val dialogWidth = (screenWidth).toInt()

// Apply the calculated width and wrap_content height to the dialog window
            dialog.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
// Calculate 80% of screen width
            //   val dialogWidth = (screenWidth * 0.8).toInt()

// Apply the calculated width and wrap_content height to the dialog window
            //  dialog.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)

            dialog.window?.setGravity(Gravity.BOTTOM)

            val btnCloseDialog = dialog.findViewById<View>(R.id.tv_go_back_to_home) as TextView
            val btnShopDialog = dialog.findViewById<View>(R.id.tv_shop_now) as TextView
            val gifImageView = dialog.findViewById<View>(R.id.gifImageView) as ImageView

            val btnSuccessTextDialog = dialog.findViewById<View>(R.id.tv_logout) as TextView
            btnSuccessTextDialog.setText("Woohoo! You Earned $value Woloo Points!")

            mSharedPreference?.setStoredPreference(
                requireActivity(),
                SharedPreferencesEnum.POWDER_ROOM_CODE.preferenceKey,
                ""
            ).toString()
            mSharedPreference?.setStoredPreference(
                requireActivity(),
                SharedPreferencesEnum.WAH_CERTIFICATE_CODE.preferenceKey,
                ""
            ).toString()

            btnCloseDialog.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                    val i = Intent(context, WolooDashboard::class.java)
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(i)
                    requireActivity().finish()
                }
            }

            Glide.with(this)
                .load(R.drawable.coins_animate) // your gif in res/drawable
                .into(gifImageView)

            btnShopDialog.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }



                val i = Intent(context, WolooDashboard::class.java)
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(i)
                requireActivity().finish()

            }

            dialog.show()
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    fun showSuccessDialog1(value: Int)
    {
        try {
            val dialog = Dialog(requireActivity())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog.setContentView(R.layout.dialog_coins_success)
            val displayMetrics = Resources.getSystem().displayMetrics
            val screenWidth = displayMetrics.widthPixels

// Calculate 80% of screen width
            val dialogWidth = (screenWidth).toInt()

// Apply the calculated width and wrap_content height to the dialog window
            dialog.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
            dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
// Calculate 80% of screen width
            //   val dialogWidth = (screenWidth * 0.8).toInt()

// Apply the calculated width and wrap_content height to the dialog window
            //  dialog.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT)

            dialog.window?.setGravity(Gravity.BOTTOM)

            val btnCloseDialog = dialog.findViewById<View>(R.id.tv_go_back_to_home) as TextView
            val btnShopDialog = dialog.findViewById<View>(R.id.tv_shop_now) as TextView
            val gifImageView = dialog.findViewById<View>(R.id.gifImageView) as ImageView

            val btnSuccessTextDialog = dialog.findViewById<View>(R.id.tv_logout) as TextView
            btnSuccessTextDialog.setText("Woohoo! You Earned $value Woloo Points!")

            btnCloseDialog.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                  /*  val i = Intent(context, WolooDashboard::class.java)
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(i)
                    requireActivity().finish()*/
                }
            }

            Glide.with(this)
                .load(R.drawable.coins_animate) // your gif in res/drawable
                .into(gifImageView)

            btnShopDialog.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }


            }

            dialog.show()
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }



    fun setRatingIcon(rating: Int) {
        try {
            if (lastSelectedRating != null) {
                lastSelectedRating?.setImageResource(R.drawable.empty_star_new)
            }
            when (rating) {
                1 -> {
                    lastSelectedRating = ivVeryBad
                    ivVeryBad?.setImageResource(R.drawable.filled_star_new)
                    ivBad?.setImageResource(R.drawable.empty_star_new)
                    ivAverage?.setImageResource(R.drawable.empty_star_new)
                    ivGood?.setImageResource(R.drawable.empty_star_new)
                    ivLovedIt?.setImageResource(R.drawable.empty_star_new)
                    showCibilScoreText?.setText(CommonUtils.authconfig_response(requireContext()).getCibilRangesSaved()!!.oneRange!!.substringAfter("-").trim())
                  //  wahImage?.setImageResource(R.drawable.wah_poor_1)
                  Glide.with(requireActivity().applicationContext)
                        .load(R.drawable.wah_poor_1)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(27)))
                        .into(wahImage!!)

                    discreteSlider?.value = 1f

                }

                2 -> {
                    lastSelectedRating = ivBad
                    ivVeryBad?.setImageResource(R.drawable.filled_star_new)
                    ivBad?.setImageResource(R.drawable.filled_star_new)
                    ivAverage?.setImageResource(R.drawable.empty_star_new)
                    ivGood?.setImageResource(R.drawable.empty_star_new)
                    ivLovedIt?.setImageResource(R.drawable.empty_star_new)
                    showCibilScoreText?.setText(CommonUtils.authconfig_response(requireContext()).getCibilRangesSaved()!!.twoRange!!.substringAfter("-").trim())
                  //  wahImage?.setImageResource(R.drawable.wah_fair_2)
                    Glide.with(requireActivity().applicationContext)
                        .load(R.drawable.wah_fair_2)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(27)))
                        .into(wahImage!!)
                    discreteSlider?.value = 2f

                }

                3 -> {
                    lastSelectedRating = ivAverage
                    ivVeryBad?.setImageResource(R.drawable.filled_star_new)
                    ivBad?.setImageResource(R.drawable.filled_star_new)
                    ivAverage?.setImageResource(R.drawable.filled_star_new)
                    ivGood?.setImageResource(R.drawable.empty_star_new)
                    ivLovedIt?.setImageResource(R.drawable.empty_star_new)
                   // wahImage?.setImageResource(R.drawable.wah_good_3)
                    showCibilScoreText?.setText(CommonUtils.authconfig_response(requireContext()).getCibilRangesSaved()!!.threeRange!!.substringAfter("-").trim())
                    Glide.with(requireActivity().applicationContext)
                        .load(R.drawable.wah_good_3)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(27)))
                        .into(wahImage!!)
                    discreteSlider?.value = 3f

                }

                4 -> {
                    lastSelectedRating = ivGood
                    ivVeryBad?.setImageResource(R.drawable.filled_star_new)
                    ivBad?.setImageResource(R.drawable.filled_star_new)
                    ivAverage?.setImageResource(R.drawable.filled_star_new)
                    ivGood?.setImageResource(R.drawable.filled_star_new)
                    ivLovedIt?.setImageResource(R.drawable.empty_star_new)
                  //  wahImage?.setImageResource(R.drawable.wah_very_good_4)
                    showCibilScoreText?.setText(CommonUtils.authconfig_response(requireContext()).getCibilRangesSaved()!!.fourRange!!.substringAfter("-").trim())
                    Glide.with(requireActivity().applicationContext)
                        .load(R.drawable.wah_very_good_4)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(27)))
                        .into(wahImage!!)
                    discreteSlider?.value = 4f

                }

                5 -> {
                    lastSelectedRating = ivLovedIt
                    ivVeryBad?.setImageResource(R.drawable.filled_star_new)
                    ivBad?.setImageResource(R.drawable.filled_star_new)
                    ivAverage?.setImageResource(R.drawable.filled_star_new)
                    ivGood?.setImageResource(R.drawable.filled_star_new)
                    ivLovedIt?.setImageResource(R.drawable.filled_star_new)
                    showCibilScoreText?.setText(CommonUtils.authconfig_response(requireContext()).getCibilRangesSaved()!!.fiveRange!!.substringAfter("-").trim())
                  //  wahImage?.setImageResource(R.drawable.wah_excellent_5)
                    Glide.with(requireActivity().applicationContext)
                        .load(R.drawable.wah_excellent_5)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(27)))
                        .into(wahImage!!)
                    discreteSlider?.value = 5f

                }

                else -> {}
            }

        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }



    var dialog: Dialog? = null
    private fun showAddReviewSuccessDialog() {
        try {
            dialog = Dialog(requireContext())
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
          //  dialog?.window?.setBackgroundDrawable(ContextCompat.getColor(requireContext() ,R.color.transparent_background))
            dialog?.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog?.setCancelable(false)
            dialog?.setCanceledOnTouchOutside(false)
            dialog?.setContentView(R.layout.dialog_share_review)
            val tvOkay = dialog?.findViewById<View>(R.id.tvOK) as TextView
            val tv_dialogreview = dialog?.findViewById<View>(R.id.tv_dialogreview) as TextView
            val authConfigResponse = CommonUtils.authconfig_response(context)
            if (authConfigResponse != null) {
                val addReviewSuccessDialogText =
                    authConfigResponse.getcUSTOMMESSAGE()?.addReviewSuccessDialogText
                tv_dialogreview.text = addReviewSuccessDialogText?.replace("\\\\n".toRegex(), "\n")
            }
            tvOkay.setOnClickListener {


                try {
                    val request = CreditCoinsRequest(
                        coins = CommonUtils.authconfig_response(requireContext()).getRateAToilet()!!.toInt(),
                        remarks = AppConstants.RATE_TOILET_CLICK,
                        type = AppConstants.RATE_TOILET_CLICK,
                        isGift = 0,
                        blogId = 0,
                    )
                    wolooViewModel1!!.addCoinstoWolooUser(request)
                }catch (e:Exception)
                {
                    CommonUtils.printStackTrace(e)
                }







                mSharedPreference?.setStoredPreference(
                    requireActivity(),
                    SharedPreferencesEnum.WAH_CERTIFICATE_CODE.preferenceKey,
                    ""
                )

                dialog?.dismiss()


            }
            if (!dialog?.isShowing!!) {
                dialog?.show()
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    override fun onStop() {
        super.onStop()
        mSharedPreference?.setStoredPreference(
            requireContext(),
            SharedPreferencesEnum.WAH_CERTIFICATE_CODE.preferenceKey,
            ""
        ).toString()
        mSharedPreference?.setStoredPreference(
            requireContext(),
            SharedPreferencesEnum.POWDER_ROOM_CODE.preferenceKey,
            ""
        ).toString()
    }



    override fun onDestroy() {
        super.onDestroy()
        mSharedPreference?.setStoredPreference(
            requireContext(),
            SharedPreferencesEnum.WAH_CERTIFICATE_CODE.preferenceKey,
            ""
        ).toString()
        mSharedPreference?.setStoredPreference(
            requireContext(),
            SharedPreferencesEnum.POWDER_ROOM_CODE.preferenceKey,
            ""
        ).toString()
    }

    companion object {
        private val TAG = AddReviewsFragment::class.java.simpleName

        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val ARG_PARAM3 = "param3"


        @JvmStatic
        fun newInstance(param1: Int, param2: String? , cibilScore : String?  ,userRatings : String?): AddReviewsFragment {
            val fragment = AddReviewsFragment()
            val args = Bundle()
            args.putInt(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            args.putString("RANGE" , cibilScore)
            args.putString("RATINGS", userRatings)
            fragment.arguments = args
            return fragment
        }
    }
}