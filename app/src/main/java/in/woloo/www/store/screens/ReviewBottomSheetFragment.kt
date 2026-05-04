package `in`.woloo.www.store.screens

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.StoreReviewPopupBinding
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.reviews.AddReviewRequest
import `in`.woloo.www.utils.Logger

class ReviewBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: StoreReviewPopupBinding? = null
    private val binding get() = _binding!!
    private var storeViewModel: StoreViewModel? = null
    var ratingInt : Int = 3
    var productId : String = ""
    var ratingGiven : Int = 5

    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = StoreReviewPopupBinding.inflate(inflater, container, false)
        storeViewModel = ViewModelProvider(this).get<StoreViewModel>(
            StoreViewModel::class.java
        )

        binding.reviewText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Remove border once text is entered
                if (!s.isNullOrEmpty()) {
                    binding.textLayout.setBackgroundResource(R.drawable.new_button_background_square)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivVeryBad?.setOnClickListener { setRatingIcon(1) }
        binding.ivBad?.setOnClickListener { setRatingIcon(2) }
        binding.ivAverage?.setOnClickListener { setRatingIcon(3) }
        binding.ivGood?.setOnClickListener { setRatingIcon(4) }
        binding.ivLovedIt?.setOnClickListener { setRatingIcon(5) }

        productId = arguments?.getString(ARG_PRODUCT_ID).toString()
        val productName = arguments?.getString(ARG_PRODUCT_NAME)
        ratingGiven = arguments?.getInt(ARG_RATINGS_GIVEN)!!
        setRatingIcon(ratingGiven)



        binding.submitHelpButton.setOnClickListener{
            if(!binding.reviewText.text.toString().isEmpty()) {
                var request = AddReviewRequest()
                request.productId = productId
                request.rating = ratingInt
                request.comment = binding.reviewText.text.toString()

                storeViewModel!!.getAddReviewForProduct(request)
                dismiss()
            }
            else{
                binding.textLayout.setBackgroundResource(R.drawable.new_button_square_red)
            }
        }

        storeViewModel!!.observeAddReviewForProduct().observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                Logger.i("Aarati Store", "setLiveData ${it.rating!!}")
                dismiss()
            }
        })



    }


    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.setBackgroundResource(R.color.transparent)  // Use custom color
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setRatingIcon(rating: Int) {
        try {

            when (rating) {
                1 -> {
                    binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                    binding.ivBad.setImageResource(R.drawable.empty_star_new)
                    binding.ivAverage.setImageResource(R.drawable.empty_star_new)
                    binding.ivGood.setImageResource(R.drawable.empty_star_new)
                    binding.ivLovedIt.setImageResource(R.drawable.empty_star_new)
                        ratingInt = 1

                }

                2 -> {
                    binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                    binding.ivBad.setImageResource(R.drawable.filled_star_new)
                    binding.ivAverage.setImageResource(R.drawable.empty_star_new)
                    binding.ivGood.setImageResource(R.drawable.empty_star_new)
                    binding.ivLovedIt.setImageResource(R.drawable.empty_star_new)
                    ratingInt = 2

                }

                3 -> {

                    binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                    binding.ivBad.setImageResource(R.drawable.filled_star_new)
                    binding.ivAverage.setImageResource(R.drawable.filled_star_new)
                    binding.ivGood.setImageResource(R.drawable.empty_star_new)
                    binding.ivLovedIt.setImageResource(R.drawable.empty_star_new)
                    ratingInt = 3

                }

                4 -> {
                    binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                    binding.ivBad.setImageResource(R.drawable.filled_star_new)
                    binding.ivAverage.setImageResource(R.drawable.filled_star_new)
                    binding.ivGood.setImageResource(R.drawable.filled_star_new)
                    binding.ivLovedIt.setImageResource(R.drawable.empty_star_new)
                    ratingInt = 4

                }

                5 -> {

                    binding.ivVeryBad.setImageResource(R.drawable.filled_star_new)
                    binding.ivBad.setImageResource(R.drawable.filled_star_new)
                    binding.ivAverage.setImageResource(R.drawable.filled_star_new)
                    binding.ivGood.setImageResource(R.drawable.filled_star_new)
                    binding.ivLovedIt.setImageResource(R.drawable.filled_star_new)
                    ratingInt = 5

                }

                else -> {}
            }

        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        parentFragmentManager.setFragmentResult("fragment2_dismissed", Bundle())
    }

    companion object {
        private const val ARG_PRODUCT_ID = "product_id"
        private const val ARG_PRODUCT_NAME = "product_name"
        private const val ARG_RATINGS_GIVEN = "ratings_given"

        fun newInstance(productId: String, productName: String , ratingsGiven : Int): ReviewBottomSheetFragment {
            val fragment = ReviewBottomSheetFragment()
            val args = Bundle()
            args.putString(ARG_PRODUCT_ID, productId)
            args.putString(ARG_PRODUCT_NAME, productName)
            args.putInt(ARG_RATINGS_GIVEN , ratingsGiven)
            fragment.arguments = args
            return fragment
        }
    }

}
