package `in`.woloo.www.application_kotlin.presentation.fragments.loo_discovery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.adapters.loo_discovery.WolooStoreAdapter
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.EnrouteDirectionActivity
import `in`.woloo.www.application_kotlin.view_models.HomeViewModel
import org.json.JSONObject


class WolooStoreInfoFragment : Fragment() {
    @JvmField
    @BindView(R.id.rvWolooStoreImages)
    var rvWolooStoreImages: RecyclerView? = null

    @JvmField
    @BindView(R.id.tv_woloo)
    var tv_woloo: TextView? = null
    private var index = 0
    private var layoutManager: LinearLayoutManager? = null

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var dataList: List<NearByStoreResponse.Data>? = null
    var homeViewModel: HomeViewModel? = null
    var adapter: WolooStoreAdapter? = null
    fun setDataList(dataList: List<NearByStoreResponse.Data?>?) {
        this.dataList = dataList as List<NearByStoreResponse.Data>?
    }

    fun setIndex(index: Int) {
        this.index = index
    }

    /*calling onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
        Logger.i(TAG, "onCreate")
    }

    /*calling onCreateView*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Logger.d(TAG, "onCreateView")
        val rootView: View = inflater.inflate(R.layout.fragment_woloo_store_info, container, false)
        ButterKnife.bind(this, rootView)
        initViews()
        setLiveData()
        return rootView
    }

    /*calling initViews*/
    private fun initViews() {
        Logger.i(TAG, "initViews")
        try {
            homeViewModel = ViewModelProvider(this).get<HomeViewModel>(
                HomeViewModel::class.java
            )
            adapter = WolooStoreAdapter(requireContext(), dataList, homeViewModel!!)
            rvWolooStoreImages!!.setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            rvWolooStoreImages!!.layoutManager = layoutManager
            rvWolooStoreImages!!.adapter = adapter
            val snapHelper: SnapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(rvWolooStoreImages)

            //rvWolooStoreImages.smoothScrollToPosition(index);
            rvWolooStoreImages!!.scrollToPosition(index)
            tv_woloo!!.setOnClickListener {
                if (activity is WolooDashboard) {
                    (activity as WolooDashboard?)?.removeWolooStoreInfo()
                } else if (activity is EnrouteDirectionActivity) {
                    (activity as EnrouteDirectionActivity?)?.removeWolooStoreInfo()
                }
            }
            rvWolooStoreImages!!.setOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        try {
                            val firstVisiblePosition =
                                layoutManager!!.findFirstVisibleItemPosition()
                            Logger.d(TAG, "firstVisiblePosition $firstVisiblePosition")
                            if (activity is WolooDashboard) {
                                (activity as WolooDashboard?)?.moveMarkerToIndex(firstVisiblePosition)
                            } else {
                                (activity as EnrouteDirectionActivity?)?.animateCameraToMarkerPosition(
                                    firstVisiblePosition
                                )
                            }
                        } catch (e: Exception) {
                            CommonUtils.printStackTrace(e)
                        }
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    fun setLiveData() {
        homeViewModel?.observeWolooEngagement()
            ?.observe(viewLifecycleOwner, Observer<BaseResponse<JSONObject>> { response ->
                if (response != null && response.success) {
                    if (adapter != null) {
                        dataList!![adapter!!.wolooSelectedIndex].isLiked =
                            adapter?.wolooEngagementRequest!!.like
                        adapter?.notifyItemChanged(adapter!!.wolooSelectedIndex)
                    }
                }
            })
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val TAG = "WolooStoreInfoFragment"


        fun newInstance(param1: String?, param2: String?): WolooStoreInfoFragment {
            val fragment = WolooStoreInfoFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}