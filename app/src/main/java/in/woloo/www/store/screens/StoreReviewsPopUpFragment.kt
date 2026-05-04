package `in`.woloo.www.store.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.adapters.loo_discovery.ReviewsAdapter
import `in`.woloo.www.blogs_module.CommentsPopup
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.BlogCommentsPopupBinding
import `in`.woloo.www.databinding.FragmentStoreReviewsPopUpBinding
import `in`.woloo.www.more.trendingblog.viewmodel.BlogViewModel
import `in`.woloo.www.store.StoreViewModel
import `in`.woloo.www.store.adapter.StoreReviewAdapter
import `in`.woloo.www.store.reviews.ReviewListData
import `in`.woloo.www.utils.Logger


class StoreReviewsPopUpFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentStoreReviewsPopUpBinding
    private lateinit var storeViewModel: StoreViewModel
    private var productId: String? = null
    private var productName: String? = null


    override fun getTheme(): Int {
        return R.style.CustomBottomSheetDialogTheme
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentStoreReviewsPopUpBinding.inflate(inflater, container, false)
        storeViewModel = ViewModelProvider(this).get<StoreViewModel>(
            StoreViewModel::class.java
        )

        productId = arguments?.getString(PRODUCT_ID)
        productName = arguments?.getString(PRODUCT_NAME)

        storeViewModel!!.getReviewsListForProduct(productId!!)

        binding.writeReviewImage.setOnClickListener {

            val bottomSheetFragment = ReviewBottomSheetFragment.newInstance(productId!! , productName!! , 3)
            bottomSheetFragment.show(childFragmentManager, bottomSheetFragment.tag)

        }

        storeViewModel!!.observeGetReviewListForProduct().observe(this, Observer { response ->
            response?.let {
                try {
                    Logger.i(
                        "Aarati Store REVIEWs",
                        "setLiveData ${it.data!!.reviews!! + it.data!!.reviews!!.size}"
                    )

                    if(it.data!!.reviews!!.size >= 2) {

                        binding.deliveryDetailsRecycler.layoutManager =
                            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
                        binding.deliveryDetailsRecycler.adapter =
                            StoreReviewAdapter(requireActivity(), it.data!!.reviews!!, storeViewModel!!)

                    }

                }catch (e : Exception)
                {
                    CommonUtils.printStackTrace(e)
                }
            }
        })




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.setFragmentResultListener("fragment2_dismissed", viewLifecycleOwner) { _, _ ->
           storeViewModel.getReviewsListForProduct(productId!!)
        }
    }


    override fun onResume() {
        super.onResume()
        // Refresh your UI or data here
    }


    companion object {

        private const val PRODUCT_ID = "product_id"
        private const val PRODUCT_NAME = "product_name"

        @JvmStatic
        fun newInstance(productId: String, productName: String): StoreReviewsPopUpFragment {
            val fragment = StoreReviewsPopUpFragment()

            StoreReviewsPopUpFragment().apply {
                arguments = Bundle().apply {
                    val args = Bundle().apply {
                        putString(PRODUCT_ID, productId)
                        putString(PRODUCT_NAME, productName)
                    }
                    fragment.arguments = args
                    return fragment
                }
            }
        }
    }



}