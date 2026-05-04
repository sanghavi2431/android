package `in`.woloo.www.application_kotlin.presentation.fragments.loo_discovery

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.adapters.NearestShowMoreAdapter
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.application_kotlin.model.CreditCoinsRequest
import `in`.woloo.www.application_kotlin.model.server_request.NearbyWolooRequest
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.presentation.HomeFragment
import `in`.woloo.www.application_kotlin.view_models.HomeViewModel
import `in`.woloo.www.application_kotlin.view_models.WolooViewModel
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.database.preference.SharedPreferencesEnum
import `in`.woloo.www.databinding.FragmentShowMoreBinding
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent
import org.json.JSONObject
import java.util.Locale


class ShowMoreFragment : DialogFragment() {


    lateinit var homeViewModel: HomeViewModel
    lateinit var binding: FragmentShowMoreBinding
    private var nearByStoreResponseList: ArrayList<NearByStoreResponse.Data> = ArrayList()
    private var nearByStoreResponseListFromApi: ArrayList<NearByStoreResponse.Data> = ArrayList()
    private var bookmarkedWolooList = ArrayList<NearByStoreResponse.Data>()
    var mSharedPreference: SharedPreference? = null
    private var adapter: NearestShowMoreAdapter? = null
    var range : Int = 2
    var dest: LatLng? = LatLng(19.055229, 72.830829)
    var travelMode : Int = 0
    private var wolooWithOffers = true
    private val defaultLocation = LatLng(19.055229, 72.830829)
    private var wolooViewModel: WolooViewModel? = null
    private var isHostOpenedNow: Boolean = false
    private var isHostBookmarkedNow: Boolean = false

    private var searchLocation: String = ""
    private val sharedViewModelShowMore: SharedViewModelStringShowMore by viewModels({ requireParentFragment() })



    @SuppressLint("NotifyDataSetChanged", "ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dialog?.window?.setBackgroundDrawable(ColorDrawable(R.color.transparent))
         binding = FragmentShowMoreBinding.inflate(inflater , container , false)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        wolooViewModel = ViewModelProvider(this).get<WolooViewModel>(
            WolooViewModel::class.java
        )

        isCancelable = true
        binding.tvNoWolooFound.visibility = View.INVISIBLE
        travelMode = SharedPreference(requireActivity()).getStoredPreference(requireActivity(), SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0")!!.toInt()
        initfun()
        setLiveData()
        var parentFragment = parentFragment as HomeFragment

        distanceRangeChanged(2 , parentFragment.lastKnownLattitude , parentFragment.lastKnownLongitude)

        binding.tv2km.setOnClickListener {
            distanceRangeChanged(2 , parentFragment.lastKnownLattitude , parentFragment.lastKnownLongitude)
        }

        binding.tv4km.setOnClickListener {
            distanceRangeChanged(4 , parentFragment.lastKnownLattitude , parentFragment.lastKnownLongitude)
        }

        binding.tv5km.setOnClickListener {
            distanceRangeChanged(5 , parentFragment.lastKnownLattitude , parentFragment.lastKnownLongitude)
        }

        binding.tv6km.setOnClickListener {
            distanceRangeChanged(6 , parentFragment.lastKnownLattitude , parentFragment.lastKnownLongitude)
        }

        binding.tv8km.setOnClickListener {
            distanceRangeChanged(8 , parentFragment.lastKnownLattitude , parentFragment.lastKnownLongitude)
        }

        binding.tv10km.setOnClickListener {
            distanceRangeChanged(10 , parentFragment.lastKnownLattitude , parentFragment.lastKnownLongitude)
        }

        binding.tvAll.setOnClickListener {
            SharedPrefSettings.getPreferences.storeIs25KM("Yes")
            distanceRangeChanged(25 , parentFragment.lastKnownLattitude , parentFragment.lastKnownLongitude)
        }


        binding.cbOpenNow.setOnClickListener {


            if(binding.cbOpenNow.backgroundTintList == ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.image_background)))
            {
                isHostOpenedNow = false
                binding.cbOpenNow.backgroundTintList=
                    ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.start_theme_color))
                binding.wolooRv.adapter?.notifyDataSetChanged()
            }
            else {
                binding.cbOpenNow.backgroundTintList =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.image_background
                        )
                    )
                isHostOpenedNow = true
            }
                getNearByWoloos(
                    parentFragment.lastKnownLattitude,
                    parentFragment.lastKnownLongitude,
                    SharedPreference(requireActivity()).getStoredPreference(
                        context, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0"
                    )!!.toInt(),
                    range,
                    1,
                    true,
                    true
                )

        }


        sharedViewModelShowMore.sharedString.observe(viewLifecycleOwner) { value ->
            Log.d("FragmentB", "Received: $value")
            searchLocation = value
        }

        binding.searchLayout.setOnClickListener {
            dialog!!.dismiss()
        }

        binding.cbWolooWithOffers.setOnClickListener {
            getNearByWoloos(
                parentFragment.lastKnownLattitude,
                parentFragment.lastKnownLongitude,
                SharedPreference(requireActivity()).getStoredPreference(
                    context, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0"
                )!!.toInt(),
                2,
                1,
                true,
                true
            )
        }

        binding.cbBookmarkedWoloo.setOnClickListener {

            if(binding.cbBookmarkedWoloo.backgroundTintList == ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.image_background)))
            {
                isHostBookmarkedNow = false
                binding.cbBookmarkedWoloo.backgroundTintList=
                    ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.start_theme_color))

            }
            else {
                binding.cbBookmarkedWoloo.backgroundTintList =
                    ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireActivity(),
                            R.color.image_background
                        )
                    )
                isHostBookmarkedNow = true

            }
            getNearByWoloos(
                parentFragment.lastKnownLattitude,
                parentFragment.lastKnownLongitude,
                SharedPreference(requireActivity()).getStoredPreference(
                    context, SharedPreferencesEnum.TRANSPORT_MODE.getPreferenceKey(), "0"
                )!!.toInt(),
                range,
                1,
                true,
                true
            )
        }

        binding.backDialog.setOnClickListener{
            dialog!!.dismiss()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("ResourceAsColor", "ResourceType", "SuspiciousIndentation")
    override fun onStart() {
        super.onStart()
        dialog?.setCanceledOnTouchOutside(true)
        val screenHeight = Resources.getSystem().displayMetrics.heightPixels

        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT,  (screenHeight * 0.75).toInt()) // Full width
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // Transparent background for rounded corners
            setGravity(Gravity.BOTTOM) // Position at bottom
        }

         /*   if (dialog != null) {
                dialog!!.window!!.setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (screenHeight * 0.75).toInt()
                )
                dialog?.window?.setBackgroundDrawable(ColorDrawable(R.color.transparent))
                dialog?.window?.setGravity(Gravity.BOTTOM)
            }*/



    }

    fun initfun()
    {
        try {
            if (WolooApplication.instance!!.nearByWoloo != null && WolooApplication.instance!!.updatedLikeStatus != -1) {
                val selectedWoloo: NearByStoreResponse.Data =
                    WolooApplication.instance!!.nearByWoloo!!
                if (nearByStoreResponseListFromApi.contains(selectedWoloo)) {
                    nearByStoreResponseListFromApi[nearByStoreResponseListFromApi.indexOf(
                        selectedWoloo
                    )].isLiked =
                        WolooApplication.instance!!.updatedLikeStatus
                }
                bookmarkedWolooList = ArrayList()
                for (i in nearByStoreResponseListFromApi.indices) {
                    if (nearByStoreResponseListFromApi[i].isLiked == 1) {
                        bookmarkedWolooList.add(nearByStoreResponseListFromApi[i])
                    }
                }
                WolooApplication.instance!!.nearByWoloo = null
                WolooApplication.instance!!.updatedLikeStatus = -1
            }
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    private fun distanceRangeChanged(changedRange : Int , lat : Double , lan : Double) {
        range = changedRange
        val selected = resources.getDrawable(R.drawable.yello_rectangle_shape)
        val notSelected = resources.getDrawable(R.drawable.rounded_gray_bg)
        binding.tv2km.background = notSelected
       binding.tv4km.background = notSelected
        binding.tv5km.background = notSelected
        binding.tv6km.background = notSelected
        binding.tv8km.background = notSelected
        binding.tv10km.background = notSelected
        binding.tvAll.background = notSelected
        Logger.e("range", " $range")
        when (range) {
            2 -> binding.tv2km.background = selected
            4 -> binding.tv4km.background = selected
            5 -> binding.tv5km.background = selected
            6 -> binding.tv6km.background = selected
            8 -> binding.tv8km.background = selected
            10 -> binding.tv10km.background = selected
            25 -> binding.tvAll.background = selected
            else -> Logger.e("range else", " $range");
        }
        getNearByWoloos( lat , lan,  travelMode , range, 1, wolooWithOffers, true)

    }

    private fun getNearByWoloos(lat: Double, lng: Double, mode: Int, range: Int, isSearch: Int, isOffer: Boolean, openNow: Boolean) {
        val request = NearbyWolooRequest()
        request.lat = lat
        request.lng = lng
        request.mode = mode
        request.range = range
        if (isOffer) {
            request.isOffer = 1
        } else {
            request.isOffer = 0
        }
            request.showAll = 2

        request.packageName = "in.woloo.app"
        request.isSearch = isSearch
        homeViewModel.getNearbyWoloos(request)

    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setLiveData() {

        wolooViewModel?.observeAddCoinstoWolooUser()
            ?.observe(viewLifecycleOwner, Observer<BaseResponse<JSONObject>> { response ->
                CommonUtils().hideProgress()
               // showSuccessDialog()
            })


        homeViewModel.observeNearByWoloo().observe(this) { arrayListBaseResponse ->

            nearByStoreResponseListFromApi.clear()
            if (arrayListBaseResponse != null) {
                if(!isHostOpenedNow && !isHostBookmarkedNow) {

                    nearByStoreResponseListFromApi = arrayListBaseResponse.data!!
                }
                else if(!isHostOpenedNow && isHostBookmarkedNow)
                {

                    for (i in arrayListBaseResponse.data!!.indices) {
                        if (arrayListBaseResponse.data!![i].isLiked == 1) {
                            nearByStoreResponseListFromApi.add(arrayListBaseResponse.data!![i])
                        }
                    }
                }
                else if(!isHostBookmarkedNow && isHostOpenedNow)
                {
                    for (i in arrayListBaseResponse.data!!.indices) {
                        if (!arrayListBaseResponse.data!![i].isOpenNow.equals("0" , ignoreCase = true)) {
                            nearByStoreResponseListFromApi.add(arrayListBaseResponse.data!![i])
                        }
                    }
                }
                else if(isHostOpenedNow && isHostBookmarkedNow)
                {
                    for (i in arrayListBaseResponse.data!!.indices) {
                        if (arrayListBaseResponse.data!![i].isLiked == 1 && !arrayListBaseResponse.data!![i].isOpenNow.equals("0" , ignoreCase = true)) {
                            nearByStoreResponseListFromApi.add(arrayListBaseResponse.data!![i])
                        }
                    }
                }
               /* if(nearByStoreResponseListFromApi.size == 0  && isDataAvailable == false)
                {
                    Toast.makeText(requireActivity()  , "No Woloo Found In Your area. Showing Default Location" , Toast.LENGTH_SHORT).show()
                    distanceRangeChanged(2 , defaultLocation.latitude , defaultLocation.longitude)
                    isDataAvailable == true
                }
                else*/ if (nearByStoreResponseListFromApi.size == 0 )
                {
                    binding.tvNoWolooFound.visibility = View.VISIBLE
                    binding.wolooRv.visibility = View.GONE
                }
                else{

                    binding.tvNoWolooFound.visibility = View.GONE
                    binding.wolooRv.visibility = View.VISIBLE

                }
                adapter = NearestShowMoreAdapter(requireActivity(), nearByStoreResponseListFromApi , wolooViewModel , searchLocation)
                binding.wolooRv.setHasFixedSize(true)
                binding.wolooRv.layoutManager =  LinearLayoutManager(
                    requireActivity(), LinearLayoutManager.VERTICAL, false)
                binding.wolooRv.adapter = adapter
            } else {
                if(SharedPrefSettings.getPreferences.fetchIs25KM().equals("YES" , ignoreCase = true)) {
                    val request = CreditCoinsRequest(
                        coins = CommonUtils.authconfig_response(requireContext())
                            .getNoWolooFound()!!.toInt(),
                        remarks = AppConstants.NO_WOLOO_FOUND_CLICK,
                        type = AppConstants.NO_WOLOO_FOUND_CLICK,
                        isGift = 0,
                        blogId = 0
                    )
                    wolooViewModel!!.addCoinstoWolooUser(request)
                    var bundle = Bundle()
                    var payload = HashMap<String, Any>()
                    bundle.putString(AppConstants.NO_WOLOO_FOUND_CLICK, "NO WOLOOS FOUND")
                    bundle.putString(AppConstants.NO_WOLOO_FOUND_CLICK, "NO WOLOOS FOUND")
                    payload[AppConstants.NO_WOLOO_FOUND_CLICK] = "NO WOLOOS FOUND"
                    payload[AppConstants.NO_WOLOO_FOUND_CLICK] = "NO WOLOOS FOUND"
                    logFirebaseEvent(requireActivity(), bundle, AppConstants.NO_WOLOO_FOUND_CLICK)
                    logNetcoreEvent(requireActivity(), payload, AppConstants.NO_WOLOO_FOUND_CLICK)
                    SharedPrefSettings.getPreferences.storeIs25KM("NO")
                }
                nearByStoreResponseListFromApi = ArrayList<NearByStoreResponse.Data>()
            }

        }
    }


    /*override fun onDestroy() {
        super.onDestroy()
        dismiss()
    }*/


    override fun onStop() {
        super.onStop()
        dismissAllowingStateLoss() // auto dismiss if tab/activity is switched
    }

    fun showSuccessDialog(coins : String)
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
            btnSuccessTextDialog.setText("Woohoo! You Earned ${coins} Woloo Points!")
            btnShopDialog.visibility = View.GONE

            Glide.with(this)
                .load(R.drawable.coins_animate) // your gif in res/drawable
                .into(gifImageView)

            btnCloseDialog.setOnClickListener {
                if (dialog.isShowing) {
                    dialog.dismiss()

                }
            }





            dialog.show()
        } catch (e: java.lang.Exception) {
            CommonUtils.printStackTrace(e)
        }
    }




    companion object {



        const val tag:String = "ShowMoreFragment"

        @JvmStatic
        fun newInstance(data: String) =
            ShowMoreFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}

class SharedViewModelStringShowMore : ViewModel() {

    private val _sharedString = MutableLiveData<String>()
    val sharedString: LiveData<String> = _sharedString

    fun setString(value: String) {
        _sharedString.value = value
    }
}
