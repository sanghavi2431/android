package `in`.woloo.www.more.my_history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.BookmarkListActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.databinding.FragmentMyHistoryBinding
import `in`.woloo.www.more.home_shop.ContentCommerceFragment
import `in`.woloo.www.more.my_history.adapter.MyHistoryAdapter
import `in`.woloo.www.more.my_history.adapter.MyOfferAdapter
import `in`.woloo.www.more.my_history.model.MyHistoryResponse
import `in`.woloo.www.utils.EmptyRecyclerView
import `in`.woloo.www.utils.EndlessRecyclerOnScrollListener
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.v2.giftcard.viewmodel.HistoryViewModel

class MyHistoryFragment : Fragment() {
    var binding: FragmentMyHistoryBinding? = null
    var ivBack: LinearLayout? = null
    var tvTitle: TextView? = null
    var tvDescription: TextView? = null
    var tvImage: ImageView? = null
    var rvMyHistory: EmptyRecyclerView? = null
    var ll_nodatafound: LinearLayout? = null
    var bookmarksLayout: LinearLayout? = null

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var isFromOffer: Boolean? = null
    private var historyViewModel: HistoryViewModel? = null
    private var mPageNumber = 1
    private var mNextPage = 0
    private val historyList: MutableList<MyHistoryResponse.History> = ArrayList()
    private val myOffersList: ArrayList<NearByStoreResponse.Data?> = ArrayList<NearByStoreResponse.Data?>()
    private var myHistoryAdapter: MyHistoryAdapter? = null
    private var myOfferAdapter: MyOfferAdapter? = null
    private lateinit var endlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener

    /*calling on onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
            isFromOffer = requireArguments().getBoolean("isFromOffer")
        }
        Logger.i(TAG, "onCreate")
    }

    /*calling on onCreateView*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyHistoryBinding.inflate(inflater, container, false)
        historyViewModel = ViewModelProvider(this).get<HistoryViewModel>(
            HistoryViewModel::class.java
        )
        ivBack = binding!!.ivBack
        tvTitle = binding!!.screenHeader
        tvDescription = binding!!.screenDescription
        tvImage = binding!!.screenImage
        rvMyHistory = binding!!.rvMyHistory
        ll_nodatafound = binding!!.llNodatafound
        bookmarksLayout = binding!!.bookmarksLayout
        bookmarksLayout!!.setOnClickListener {
            val i = Intent(requireActivity().applicationContext, BookmarkListActivity::class.java)
            startActivity(i)
        }
        initView()
        setLiveData()
        Logger.i(TAG, "onCreateView")
        return binding!!.root
    }

    /*calling on initView*/
    private fun initView() {
        try {
            Logger.i(TAG, "initView")
            ll_nodatafound!!.visibility = View.GONE
            rvMyHistory!!.visibility = View.VISIBLE
            setAdapter(isFromOffer!!)
            if (isFromOffer!!) {
                tvTitle!!.text = "Offer Cart"
                tvDescription!!.setText(R.string.check_out_offers)
                tvImage!!.setImageResource(R.drawable.offer_cart_icon)
                historyViewModel!!.getMyOffers()
            } else {
                tvTitle!!.text = resources.getString(R.string.my_history)
                tvDescription!!.setText(R.string.check_out)
                tvImage!!.setImageResource(R.drawable.my_history_icon)
                historyViewModel!!.getRewardHistory(mPageNumber)
            }
            ivBack!!.setOnClickListener { v: View? ->
                try {
                    //getActivity().onBackPressed();
                    val fm = requireActivity().supportFragmentManager
                    if (fm.backStackEntryCount > 0) {
                        fm.popBackStack()
                    } else {
                        (requireActivity() as WolooDashboard).loadFragment(
                            ContentCommerceFragment(),
                            ContentCommerceFragment.TAG
                        )
                        (requireActivity() as WolooDashboard).changeIcon(
                            (requireActivity() as WolooDashboard).nav_view!!.getMenu()
                                .findItem(R.id.navigation_dash_home)
                        )
                    }
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    fun setLiveData() {
        historyViewModel!!.observeRewardHistory().observe(
            viewLifecycleOwner,
            Observer<BaseResponse<MyHistoryResponse.Data>> { myHistoryResponse -> //Logger.d("Aarati" , "history is " + myHistoryResponse.toString());
                ll_nodatafound!!.visibility = View.GONE
                rvMyHistory!!.visibility = View.VISIBLE
                try {
                    Logger.d("Aarati", "history is " + myHistoryResponse.data!!.history!!.size)
                    if (mPageNumber == 1) {
                        historyList.clear()
                    }
                    if (myHistoryResponse != null && myHistoryResponse.data != null && myHistoryResponse.data!!.history != null && myHistoryResponse.data!!.history!!.size > 0) {
                        historyList.addAll(myHistoryResponse!!.data!!.history!!)
                        myHistoryAdapter!!.notifyDataSetChanged()
                        Logger.d("Aarati", "history is " + historyList.size)
                        mNextPage =
                            if (myHistoryResponse.data!!.next != null && myHistoryResponse.data!!.next != null) {
                                myHistoryResponse.data!!.next
                            } else {
                                0
                            }
                    } else {
                        WolooApplication.errorMessage = ""
                        if (mPageNumber == 1) {
                            showNoHistory()
                        }
                    }
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
            })
        historyViewModel!!.observeMyOffers().observe(viewLifecycleOwner, Observer<BaseResponse<ArrayList<NearByStoreResponse.Data>>> { response ->
            ll_nodatafound!!.visibility = View.GONE
            rvMyHistory!!.visibility = View.VISIBLE
            try {
                myOffersList.clear()
                if (response != null && response.data != null && response.data!!.size > 0) {
                    myOffersList.addAll(response.data!!)
                    myOfferAdapter!!.notifyDataSetChanged()
                } else {
                    WolooApplication.errorMessage = ""
                    showNoHistory()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        })
    }

    private fun setAdapter(isFromOffer: Boolean) {
        try {
            if (isFromOffer) {
                myOfferAdapter = MyOfferAdapter(requireContext(), myOffersList)
                rvMyHistory!!.setHasFixedSize(true)
                val linearLayoutManager = LinearLayoutManager(context)
                rvMyHistory!!.layoutManager = linearLayoutManager
                rvMyHistory!!.adapter = myOfferAdapter
            } else {
                myHistoryAdapter = MyHistoryAdapter(requireContext(), historyList)
                rvMyHistory!!.setHasFixedSize(true)
                val linearLayoutManager = LinearLayoutManager(context)
                rvMyHistory!!.layoutManager = linearLayoutManager
                endlessRecyclerOnScrollListener =
                    object : EndlessRecyclerOnScrollListener(linearLayoutManager) {
                        override fun onLoadMore(current_page: Int) {
                            mPageNumber = mNextPage
                            if (mNextPage != 0) {
                                historyViewModel!!.getRewardHistory(mPageNumber)
                            }
                        }
                    }
                rvMyHistory!!.addOnScrollListener(endlessRecyclerOnScrollListener)
                rvMyHistory!!.adapter = myHistoryAdapter
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling on showNoHistory*/
    fun showNoHistory() {
        Logger.i(TAG, "showNoHistory")
        Toast.makeText(requireActivity().applicationContext, "No History Available", Toast.LENGTH_SHORT)
            .show()
        ll_nodatafound!!.visibility = View.VISIBLE
        rvMyHistory!!.visibility = View.GONE
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        var TAG = MyHistoryFragment::class.java.simpleName

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyHistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(
            param1: String?,
            param2: String?,
            isFromOffer: Boolean?
        ): MyHistoryFragment {
            val fragment = MyHistoryFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            args.putBoolean("isFromOffer", isFromOffer!!)
            fragment.arguments = args
            return fragment
        }
    }
}