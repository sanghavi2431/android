package `in`.woloo.www.application_kotlin.presentation.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.Gson
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.adapters.loo_discovery.SearchPlacesAdapter
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.model.CreditCoinsRequest
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.application_kotlin.view_models.ReviewViewModel
import `in`.woloo.www.application_kotlin.view_models.WolooViewModel
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.FragmentRateToiletBinding
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent
import org.json.JSONObject
import java.util.Locale


class RateToiletFragment : Fragment() {
     lateinit var binding: FragmentRateToiletBinding
    var placeAdapter: SearchPlacesAdapter? = null
    lateinit var mPlacesClient: PlacesClient
    private var lastSelectedRating: ImageView? = null
    private var reviewViewModel: ReviewViewModel? = null
    private var wolooViewModel: WolooViewModel? = null
    var dialog: Dialog? = null
    lateinit var name:String
    lateinit var address :String
    lateinit var latlan : LatLng
    var latitude :Double = 0.0
    var longitude :Double = 0.0
    lateinit var city :String
    lateinit var postalCode : String
    var wolooId : Int? = 0



    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { data ->
try{
                val json = data.getStringExtra("PLACE_NEW")
                val nearByStoreResponseSelected = Gson().fromJson(json, NearByStoreResponse.DataSearch::class.java)
                latlan = LatLng(nearByStoreResponseSelected!!.lat!!.toDouble(), nearByStoreResponseSelected.lng!!.toDouble())

                binding.searchAutoCompleteCurrentLocation.text = nearByStoreResponseSelected.name

                Glide.with(requireActivity())
                    .load(nearByStoreResponseSelected.cibilScoreImage) // or load(R.drawable.your_drawable)
                    .into(binding.wahImageDisplay)
                setRatingIcon(nearByStoreResponseSelected.userRating!!.toInt())
                binding.showCibilScoreText.text = nearByStoreResponseSelected.cibilScore!!.substringAfter("-").trim()

                nearByStoreResponseSelected?.let { store ->
                    wolooId = store.id
                    name = store.name.toString()
                    address = store.address.toString()
                    city = store.city.toString()
                    latitude = store.lat!!.toDouble()
                    longitude = store.lng!!.toDouble()
                    postalCode = store.pinCode.toString()


                    Log.d(
                        "SelectedStore",
                        "Name: $name, Address: $address, Image: $city, Latitude: $latitude, Longitude: $longitude, Postal Code: $postalCode"
                    )


                }
}catch (e : Exception)
{

}

            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRateToiletBinding.inflate(inflater, container, false)


         binding.searchAutoCompleteCurrentLocation.setOnClickListener(
             { v: View? ->
                 val intent = Intent(activity, RateToiletSearchActivity::class.java)
                 resultLauncher.launch(intent)
             }
         )

        try {
            binding.showCibilScoreText.setText(
                CommonUtils.authconfig_response(requireContext())
                    .getCibilRangesSaved()!!.threeRange!!.substringAfter("-").trim()
            )
        }
        catch (e : Exception){

        }
        
        Glide.with(requireActivity().applicationContext)
            .load(R.drawable.wah_good_3)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(27)))
            .into(binding.wahImageDisplay)

        binding.discreteSlider.addOnChangeListener { slider, value, fromUser ->
            // Update the TextView with the current value of the slider (snap to nearest step)
            Log.d("Value is on change", "Slider value: ${slider.value.toInt()}")
            Logger.i(RateToiletFragment.TAG, "onCreateView2 ")
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
        wolooViewModel = ViewModelProvider(this).get<WolooViewModel>(
            WolooViewModel::class.java
        )
        setLiveData()
        binding.ivBack.setOnClickListener {
            activity?.onBackPressed() }

        binding.ivVeryBad.setOnClickListener { setRatingIcon(1) }
        binding.ivBad.setOnClickListener { setRatingIcon(2) }
        binding.ivAverage.setOnClickListener { setRatingIcon(3) }
        binding.ivGood.setOnClickListener { setRatingIcon(4) }
        binding.ivLovedIt.setOnClickListener { setRatingIcon(5) }
        binding.btnSubmit.setOnClickListener {
            if(binding.searchAutoCompleteCurrentLocation.text.isNotEmpty()) {
              //  CommonUtils().showProgress(requireContext())
                binding.btnSubmit.background = ContextCompat.getDrawable(
                    requireActivity().applicationContext,
                    R.drawable.new_button_onclick_background
                )
                binding.discreteSlider.value.let { it1 ->
                    if (it1 == 0f) {
                        reviewViewModel?.rateToiletAddReview(
                           1,
                            binding.etReview.text.toString(),
                            name,
                            address,
                            city,
                            latitude.toString(),
                            longitude.toString(),
                            postalCode
                        )
                    }
                    else {

                        reviewViewModel?.rateToiletAddReview(
                            it1.toInt(),
                            binding.etReview.text.toString(),
                            name,
                            address,
                            city,
                            latitude.toString(),
                            longitude.toString(),
                            postalCode
                        )
                    }



                }
            }
            else{
                Toast.makeText(requireActivity() , "Please Select Location" , Toast.LENGTH_SHORT).show()
            }
           // CommonUtils().hideProgress()
        }
        binding.writeReviewLayout.setOnClickListener {
            if(binding.etReview.visibility == View.GONE) {
                binding.etReview.visibility = View.VISIBLE
            }
        }



        return binding.root
    }


    fun setRatingIcon(rating: Int) {
        try {
            if (lastSelectedRating != null) {
                lastSelectedRating?.setImageResource(R.drawable.empty_star_new)
            }
            when (rating) {
                1 -> {
                    lastSelectedRating = binding.ivVeryBad
                    binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                    binding.ivBad.setImageResource(R.drawable.empty_star_new)
                    binding.ivAverage.setImageResource(R.drawable.empty_star_new)
                    binding.ivGood.setImageResource(R.drawable.empty_star_new)
                    binding.ivLovedIt.setImageResource(R.drawable.empty_star_new)
                    binding.showCibilScoreText.setText(CommonUtils.authconfig_response(requireContext()).getCibilRangesSaved()!!.oneRange!!.substringAfter("-").trim())
                    //  wahImage?.setImageResource(R.drawable.wah_poor_1)
                    Glide.with(requireActivity().applicationContext)
                        .load(R.drawable.wah_poor_1)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(27)))
                        .into(binding.wahImageDisplay)

                    binding.discreteSlider.value = 1f

                }

                2 -> {
                    lastSelectedRating = binding.ivBad
                    binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                    binding.ivBad.setImageResource(R.drawable.filled_star_new)
                    binding.ivAverage.setImageResource(R.drawable.empty_star_new)
                    binding.ivGood.setImageResource(R.drawable.empty_star_new)
                    binding.ivLovedIt.setImageResource(R.drawable.empty_star_new)
                    //  wahImage?.setImageResource(R.drawable.wah_fair_2)
                    binding.showCibilScoreText.setText(CommonUtils.authconfig_response(requireContext()).getCibilRangesSaved()!!.twoRange!!.substringAfter("-").trim())
                    Glide.with(requireActivity().applicationContext)
                        .load(R.drawable.wah_fair_2)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(27)))
                        .into(binding.wahImageDisplay)
                    binding.discreteSlider.value = 2f

                }

                3 -> {
                    lastSelectedRating = binding.ivAverage
                    binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                    binding.ivBad.setImageResource(R.drawable.filled_star_new)
                    binding.ivAverage.setImageResource(R.drawable.filled_star_new)
                    binding.ivGood.setImageResource(R.drawable.empty_star_new)
                    binding.ivLovedIt.setImageResource(R.drawable.empty_star_new)
                    // wahImage?.setImageResource(R.drawable.wah_good_3)
                    binding.showCibilScoreText.setText(CommonUtils.authconfig_response(requireContext()).getCibilRangesSaved()!!.threeRange!!.substringAfter("-").trim())
                    Glide.with(requireActivity().applicationContext)
                        .load(R.drawable.wah_good_3)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(27)))
                        .into(binding.wahImageDisplay)
                    binding.discreteSlider.value = 3f

                }

                4 -> {
                    lastSelectedRating = binding.ivGood
                    binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                    binding.ivBad.setImageResource(R.drawable.filled_star_new)
                    binding.ivAverage.setImageResource(R.drawable.filled_star_new)
                    binding.ivGood.setImageResource(R.drawable.filled_star_new)
                    binding.ivLovedIt.setImageResource(R.drawable.empty_star_new)
                    //  wahImage?.setImageResource(R.drawable.wah_very_good_4)
                    binding.showCibilScoreText.setText(CommonUtils.authconfig_response(requireContext()).getCibilRangesSaved()!!.fourRange!!.substringAfter("-").trim())
                    Glide.with(requireActivity().applicationContext)
                        .load(R.drawable.wah_very_good_4)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(27)))
                        .into(binding.wahImageDisplay)
                    binding.discreteSlider.value = 4f

                }

                5 -> {
                    lastSelectedRating = binding.ivLovedIt
                    binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                    binding.ivBad.setImageResource(R.drawable.filled_star_new)
                    binding.ivAverage.setImageResource(R.drawable.filled_star_new)
                    binding.ivGood.setImageResource(R.drawable.filled_star_new)
                    binding.ivLovedIt.setImageResource(R.drawable.filled_star_new)
                    //  wahImage?.setImageResource(R.drawable.wah_excellent_5)
                    binding.showCibilScoreText.setText(CommonUtils.authconfig_response(requireContext()).getCibilRangesSaved()!!.fiveRange!!.substringAfter("-").trim())
                    Glide.with(requireActivity().applicationContext)
                        .load(R.drawable.wah_excellent_5)
                        .apply(RequestOptions.bitmapTransform(RoundedCorners(27)))
                        .into(binding.wahImageDisplay)
                    binding.discreteSlider.value = 5f

                }

                else -> {}
            }

        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }


    fun setLiveData() {

        reviewViewModel?.observeSubmitReview()
            ?.observe(viewLifecycleOwner, Observer<BaseResponse<JSONObject>> { response ->
              //  CommonUtils().hideProgress()
                if (response.success) {

                    showAddReviewSuccessDialog()
                } else {
                    Toast.makeText(context, WolooApplication.errorMessage, Toast.LENGTH_SHORT)
                        .show()
                    WolooApplication.errorMessage = ""
                }
            })

        wolooViewModel?.observeAddCoinstoWolooUser()
            ?.observe(viewLifecycleOwner, Observer<BaseResponse<JSONObject>> { response ->
               // CommonUtils().hideProgress()
                showSuccessDialog()
                var bundle = Bundle()
                var payload = HashMap<String, Any>()
                bundle.putString(AppConstants.RATE_TOILET_CLICK, name)
                bundle.putString(AppConstants.RATE_TOILET_CLICK, name)
                payload[AppConstants.RATE_TOILET_CLICK] = name
                payload[AppConstants.RATE_TOILET_CLICK] = name
                logFirebaseEvent(activity, bundle, AppConstants.RATE_TOILET_CLICK)
                logNetcoreEvent( requireActivity(), payload, AppConstants.RATE_TOILET_CLICK)
            })

    }

    private fun showAddReviewSuccessDialog() {
        try {
            dialog = Dialog(requireContext())
            dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent_background)))
            dialog?.setCancelable(false)
            dialog?.setCanceledOnTouchOutside(false)
            dialog?.setContentView(R.layout.dialog_share_review)
            val tvOkay = dialog?.findViewById<View>(R.id.tvOK) as TextView
            val tv_dialogreview = dialog?.findViewById<View>(R.id.tv_dialogreview) as TextView
            val gifImageView = dialog?.findViewById<View>(R.id.review_success) as ImageView

            Glide.with(this)
                .load(R.drawable.thanks_rate) // your gif in res/drawable
                .into(gifImageView)

            val authConfigResponse = CommonUtils.authconfig_response(context)
            if (authConfigResponse != null) {
                val addReviewSuccessDialogText =
                    authConfigResponse.getcUSTOMMESSAGE()?.addReviewSuccessDialogText
                tv_dialogreview.text = addReviewSuccessDialogText?.replace("\\\\n".toRegex(), "\n")
            }
            tvOkay.setOnClickListener {
                dialog?.dismiss()
               /* val i = Intent(context, WolooDashboard::class.java)
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(i)
                requireActivity().finish()*/
try {
    val request = CreditCoinsRequest(
        coins = CommonUtils.authconfig_response(requireContext()).getRateAToilet()!!.toInt(),
        remarks = AppConstants.RATE_TOILET_CLICK,
        type = AppConstants.RATE_TOILET_CLICK,
        isGift = 0,
        blogId = 0,
    )
    wolooViewModel!!.addCoinstoWolooUser(request)
}catch (e:Exception)
{
    CommonUtils.printStackTrace(e)
}

            }
            if (!dialog?.isShowing!!) {
                dialog?.show()
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    fun getPostalCodeFromLatLng(latitude: Double, longitude: Double): String? {
        return try {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1) // Get address details
            if (!addresses.isNullOrEmpty()) {
                addresses[0].postalCode // Get postal code from address
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun showSuccessDialog()
    {
        try {
            val dialog = Dialog(requireContext())
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
            dialog.window?.setGravity(Gravity.BOTTOM)
            val btnCloseDialog = dialog.findViewById<View>(R.id.tv_go_back_to_home) as TextView
            val btnShopDialog = dialog.findViewById<View>(R.id.tv_shop_now) as TextView
            val gifImageView = dialog.findViewById<View>(R.id.gifImageView) as ImageView
            val btnSuccessTextDialog = dialog.findViewById<View>(R.id.tv_logout) as TextView
            btnSuccessTextDialog.setText("Woohoo! You Earned ${CommonUtils.authconfig_response(requireContext()).getRateAToilet()!!} Woloo Points!")
            btnShopDialog.visibility = View.GONE

            Glide.with(this)
                .load(R.drawable.coins_animate) // your gif in res/drawable
                .into(gifImageView)

            btnCloseDialog.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()

                    (activity as WolooDashboard).loadFragment(RateToiletFragment(), "RateToiletFragment")
                }
            }





            dialog.show()
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    companion object {

        const val TAG: String = "RateToiletFragment"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RateToiletFragment().apply {
            }
    }
}