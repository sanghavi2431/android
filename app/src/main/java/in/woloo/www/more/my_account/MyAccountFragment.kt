package `in`.woloo.www.more.my_account

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.giftcard.giftcard.model.UserCoins
import `in`.woloo.www.more.giftcard.giftcard.viewmodel.CoinsViewModel
import `in`.woloo.www.more.models.UserCoinHistoryModel
import `in`.woloo.www.more.models.UserCoinsResponse
import `in`.woloo.www.more.my_account.adapter.CreditHistoryAdapter
import `in`.woloo.www.more.my_account.mvp.MyAccountPresenter
import `in`.woloo.www.more.my_account.mvp.MyAccountView
import `in`.woloo.www.utils.EndlessRecyclerOnScrollListener
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.more.home_shop.ContentCommerceFragment
import `in`.woloo.www.shopping.fragments.ShoppingFragment

class MyAccountFragment : Fragment(), MyAccountView {
    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: LinearLayout? = null

    @JvmField
    @BindView(R.id.screen_header)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.ivShop)
    var ivShop: ImageView? = null

    @JvmField
    @BindView(R.id.rvCreditHistory)
    var rvCreditHistory: RecyclerView? = null

    @JvmField
    @BindView(R.id.tvWolooMoney)
    var tvWolooMoney: TextView? = null

    @JvmField
    @BindView(R.id.tvWolooPoints)
    var tvWolooPoints: TextView? = null

    @JvmField
    @BindView(R.id.toolbar)
    var toolbar: Toolbar? = null

    // TODO: Rename and change types of parameters
    private var isShowBackButton = false
    private val mParam2 = false
    private var myAccountPresenter: MyAccountPresenter? = null
    private var coinsViewModel: CoinsViewModel? = null
    private var usercoinHistoryArrayList: ArrayList<UserCoinHistoryModel.Data.HistoryItem>? = null
    private var creditHistoryAdapter: CreditHistoryAdapter? = null
    private var linearLayoutManager: LinearLayoutManager? = null
    private var endEndlessRecyclerOnScrollListener: EndlessRecyclerOnScrollListener? = null
    private var mPageNumber = 0
    private var mNextPage = 0

    /*calling on onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            isShowBackButton = requireArguments().getBoolean(ARG_PARAM1)
        }
        Logger.i(TAG, "onCreate")
    }

    /*calling on onCreateView*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Logger.i(TAG, "onCreateView")
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.fragment_my_account, container, false)
        ButterKnife.bind(this, root)
        initViews()
        setLiveData()
        /*   if(isShowBackButton){
            ivBack.setVisibility(View.VISIBLE);
        }else {
            ivBack.setVisibility(View.GONE);
        }*/return root
    }

    /*calling on onResume*/
    override fun onResume() {
        Logger.i(TAG, "onResume")
        super.onResume()
        (activity as WolooDashboard?)!!.hideToolbar()
    }

    /*calling on initViews*/
    private fun initViews() {
        Logger.i(TAG, "initViews")
        try {
            usercoinHistoryArrayList = ArrayList<UserCoinHistoryModel.Data.HistoryItem>()
            coinsViewModel = ViewModelProvider(this).get<CoinsViewModel>(
                CoinsViewModel::class.java
            )
            coinsViewModel!!.getUserCoins()
            myAccountPresenter = MyAccountPresenter(requireContext(), this@MyAccountFragment)
            //            myAccountPresenter.getUserCoins(); // TODO Node
            mPageNumber = 1
            //            myAccountPresenter.getCoinHistory(mPageNumber);
            coinsViewModel!!.getCoinHistory(mPageNumber)
            tvTitle!!.text = getString(R.string.my_account)
            ivShop!!.bringToFront()
            ivShop!!.setOnClickListener { v: View? ->
                if (requireActivity() is WolooDashboard) {
                    (requireActivity() as WolooDashboard).loadFragment(
                        ShoppingFragment(),
                        "ShoppingFragment"
                    )
                    //   ((WolooDashboard) requireActivity()).changeIcon(((WolooDashboard) requireActivity()).nav_view.getMenu().findItem(R.id.navigation_home));
                }
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
            setCreditHistoryAdapter()
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setLiveData() {
        coinsViewModel!!.observeUserCoins()
            .observe(viewLifecycleOwner, Observer<BaseResponse<UserCoins>> { userCoinsResponse ->
                Logger.i(TAG, "userCoinsSuccess")
                try {
                    if (userCoinsResponse != null) {
                        if (userCoinsResponse.data != null && userCoinsResponse.data != null) {
                            tvWolooPoints!!.setText(userCoinsResponse!!.data!!.totalCoins.toString() + " Woloo Points")
                            tvWolooMoney!!.text =
                                "Rs. " + userCoinsResponse!!.data!!.giftCoins + " in Gift Cards"
                        }
                    } else {
                        WolooApplication.errorMessage = ""
                    }
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
            })
        coinsViewModel!!.observeCoinHistory().observe(viewLifecycleOwner, Observer<BaseResponse<UserCoinHistoryModel.Data>> { response ->
            Logger.i(TAG, "userCoinsHistorySuccess")
            try {
                if (response != null && response.data != null) {
                    if (mPageNumber == 1) {
                        usercoinHistoryArrayList!!.clear()
                    }
                    //                        usercoinHistoryArrayList.addAll(response.getData().getHistory());
                    for (i in response.data!!.history!!.indices) {
                        Logger.i(TAG, "index : $i")
                        usercoinHistoryArrayList!!.add(response.data!!.history!![i])
                    }
                    creditHistoryAdapter!!.notifyDataSetChanged()
                    try {
                        mNextPage = if (response.data!!.lastPage != null
                            && mPageNumber < response.data!!.lastPage!!
                        ) {
                            mPageNumber + 1
                        } else {
                            0
                        }
                    } catch (ex: Exception) {
                        CommonUtils.printStackTrace(ex)
                    }
                }
            } catch (ex: Exception) {
                CommonUtils.printStackTrace(ex)
            }
        })
    }

    /*calling on setCreditHistoryAdapter*/
    private fun setCreditHistoryAdapter() {
        Logger.i(TAG, "setCreditHistoryAdapter")
        try {
            creditHistoryAdapter = CreditHistoryAdapter(requireContext(), usercoinHistoryArrayList!!)
            linearLayoutManager = LinearLayoutManager(requireContext())
            rvCreditHistory!!.layoutManager = linearLayoutManager
            endEndlessRecyclerOnScrollListener =
                object : EndlessRecyclerOnScrollListener(linearLayoutManager!!) {
                    override fun onLoadMore(current_page: Int) {
                        if (mNextPage != 0) {
                            mPageNumber = mNextPage
                            loadMore()
                        }
                    }
                }
            rvCreditHistory!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItem = linearLayoutManager!!.itemCount
                    val lastVisibleItem = linearLayoutManager!!.findLastVisibleItemPosition()
                    if (lastVisibleItem == totalItem - 1) {
                        if (mNextPage != 0) {
                            mPageNumber = mNextPage
                            loadMore()
                        }
                    }
                }
            })
            rvCreditHistory!!.adapter = creditHistoryAdapter
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling on loadMore*/
    private fun loadMore() {
        Logger.i(TAG, "loadMore")
        try {
//            myAccountPresenter.getCoinHistory(mPageNumber);
            coinsViewModel!!.getCoinHistory(mPageNumber)
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling on userCoinsSuccess*/
    @SuppressLint("SetTextI18n")
    override fun userCoinsSuccess(userCoinsResponse: UserCoinsResponse?) {
        Logger.i(TAG, "userCoinsSuccess")
        try {
            if (userCoinsResponse != null) {
                if (userCoinsResponse.data != null && userCoinsResponse.data!!
                        .totalCoins != null
                ) {
                    tvWolooPoints!!.setText(
                        userCoinsResponse.data!!.totalCoins.toString() + " Woloo Points"
                    )
                }
                if (userCoinsResponse.data != null && userCoinsResponse.data!!
                        .giftCoins != null
                ) {
                    tvWolooMoney!!.text =
                        "Rs. " + userCoinsResponse.data!!.giftCoins + " in Gift Cards"
                }
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*
    calling on userCoinsHistorySuccess
*/
    @SuppressLint("NotifyDataSetChanged")
    override fun userCoinsHistorySuccess(userCoinHistoryModel: UserCoinHistoryModel?) {
        Logger.i(TAG, "userCoinsHistorySuccess")
        try {
            if (userCoinHistoryModel != null) {
                if (mPageNumber == 1) {
                    usercoinHistoryArrayList!!.clear()
                }
                usercoinHistoryArrayList!!.addAll(userCoinHistoryModel.data!!.history!!)
                creditHistoryAdapter!!.notifyDataSetChanged()
                try {
                    mNextPage =
                        if (userCoinHistoryModel.data != null && userCoinHistoryModel.data!!
                                .next != null && userCoinHistoryModel.data!!
                                .next != null
                        ) {
                            userCoinHistoryModel.data!!.next!!
                        } else {
                            0
                        }
                } catch (ex: Exception) {
                    CommonUtils.printStackTrace(ex)
                }
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        var TAG = MyAccountFragment::class.java.simpleName
        @JvmStatic
        fun newInstance(isShowBackButton: Boolean): MyAccountFragment {
            val fragment = MyAccountFragment()
            val args = Bundle()
            args.putBoolean(ARG_PARAM1, isShowBackButton)
            fragment.arguments = args
            return fragment
        }
    }

}