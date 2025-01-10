package `in`.woloo.www.application_kotlin.presentation.fragments.loo_discovery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.adapters.loo_discovery.NearestWalkAdapter
import `in`.woloo.www.application_kotlin.model.server_response.NearByStoreResponse
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.SearchActivity
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.dashboard.ui.home.HomeFragment
import `in`.woloo.www.utils.Logger

class HomeCategoryFragment : Fragment() {
    @JvmField
    @BindView(R.id.rvHomeCategory)
    var rvHomeCategory: RecyclerView? = null

    @JvmField
    @BindView(R.id.tv_search_more)
    var tv_search_more: TextView? = null

    @JvmField
    @BindView(R.id.tvNoWolooFound)
    var tvNoWolooFound: LinearLayout? = null

    @JvmField
    @BindView(R.id.tv_search)
    var tv_search: TextView? = null

    @JvmField
    @BindView(R.id.pullToRefreshLayout)
    var pullToRefreshLayout: SwipeRefreshLayout? = null

    @JvmField
    @BindView(R.id.bottomMargin)
    var bottomMargin: TextView? = null

    @JvmField
    @BindView(R.id.tv_no_woloo_title)
    var noWolooTitle: TextView? = null

    @JvmField
    @BindView(R.id.dotLayout)
    var dotsLayout: LinearLayout? = null

    var dotViews = java.util.ArrayList<View>()


    @JvmField
    @BindView(R.id.tv_no_woloo_search_text)
    var noWolooSearchText: TextView? = null
    @JvmField
    var pageNumber = 1
    var stopLoading = false
    private val nearByStoreResponseList: MutableList<NearByStoreResponse.Data> = ArrayList()
    private var adapter: NearestWalkAdapter? = null

    /*calling onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.i(TAG, "onCreate")
    }

    /*calling onCreateView*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.fragment_home_category, container, false)
        ButterKnife.bind(this, root)
        initViews()
        Logger.i(TAG, "onCreateView")
        rvHomeCategory!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(@NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager?
                var firstVisiblePosition = layoutManager!!.findFirstVisibleItemPosition()
                var listSize = recyclerView.adapter?.itemCount ?: 0
                dotLayoutCreator(listSize, firstVisiblePosition)
              //  updateDotColor(firstVisiblePosition)
            }
        })
        return root
    }

    /*calling initViews*/
    private fun initViews() {
        Logger.i(TAG, "initViews")
        try {
            if (parentFragment is HomeFragment) {
                bottomMargin!!.visibility = View.VISIBLE
            } else {
                bottomMargin!!.visibility = View.GONE
                tv_search!!.visibility = View.GONE
                tv_search_more!!.visibility = View.GONE
            }
            setHomeCategories()

            tv_search!!.setOnClickListener { v: View? ->
                val intent = Intent(context, SearchActivity::class.java)
                intent.putExtra("lat", (parentFragment as HomeFragment?)?.lastKnownLattitude)
                intent.putExtra("lng", (parentFragment as HomeFragment?)?.lastKnownLongitude)
                requireActivity().startActivity(intent)
            }
            tv_search_more!!.setOnClickListener { v: View? ->
                val intent = Intent(context, SearchActivity::class.java)
                intent.putExtra("lat", (parentFragment as HomeFragment?)?.lastKnownLattitude)
                intent.putExtra("lng", (parentFragment as HomeFragment?)?.lastKnownLongitude)
                requireActivity().startActivity(intent)
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling setHomeCategories*/
    private fun setHomeCategories() {
        Logger.i(TAG, "setHomeCategories")
        try {
            adapter = NearestWalkAdapter(requireContext(), nearByStoreResponseList)
            rvHomeCategory!!.setHasFixedSize(true)
            rvHomeCategory!!.layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false)
            rvHomeCategory!!.adapter = adapter
            //            rvHomeCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//                    if (newState != RecyclerView.SCROLL_STATE_IDLE) {
//                        return;
//                    }
//                    if (!recyclerView.canScrollVertically(1)) {
//                        if (!stopLoading) {
//                            pageNumber++;
//                            ((HomeFragment) getParentFragment()).loadMore(String.valueOf(pageNumber),false);
//                        }
//                    }
//                }
//            });
            pullToRefreshLayout!!.setOnRefreshListener {
                pageNumber = 1
                //tvNoWolooFound.setVisibility(View.GONE);
                //rvHomeCategory.setVisibility(View.GONE);
//                if(getParentFragment() instanceof HomeFragment) {
//                    ((HomeFragment) getParentFragment()).loadMore(String.valueOf(pageNumber), true);
//                    ((HomeFragment) getParentFragment()).isFromClickFlag = false;
//                }
                pullToRefreshLayout!!.isRefreshing = false
            }
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    /*calling setNearestWalk*/
    fun setNearestWalk(
        nearByStoreResponseList: List<NearByStoreResponse.Data>,
        openNow: Boolean,
        bookmark: Boolean,
        isEnroute: Boolean
    ) {
        Logger.i(TAG, "setNearestWalk")
        Logger.e("initViews", " $isEnroute")
        //if (pageNumber == 1)
        this.nearByStoreResponseList.clear()
        if (nearByStoreResponseList.size > 0) this.nearByStoreResponseList.addAll(
            nearByStoreResponseList
        )
        adapter?.notifyDataSetChanged()
        if (this.nearByStoreResponseList.size == 0) {
            if (openNow) {
                noWolooTitle!!.text = resources.getString(R.string.no_woloo_found_at_moment)
            } else if (bookmark) {
                noWolooTitle!!.text = resources.getString(R.string.no_woloo_found_bookmark)
            } else {
                noWolooTitle!!.text = resources.getString(R.string.no_woloo_found)
            }
            tvNoWolooFound!!.visibility = View.VISIBLE
            rvHomeCategory!!.visibility = View.GONE
            pullToRefreshLayout!!.visibility = View.GONE
            if (parentFragment !is HomeFragment) {
                noWolooTitle!!.text = "Sorry, couldn’t find any Woloo Host On-Route"
                noWolooSearchText!!.visibility = View.GONE
            }
            if (isEnroute) {
                Logger.e("initViews", TAG + requireActivity().localClassName)
                tv_search!!.visibility = View.GONE
                tv_search_more!!.visibility = View.GONE
                noWolooTitle!!.visibility = View.GONE
                noWolooSearchText!!.visibility = View.GONE
                bottomMargin!!.visibility = View.GONE
                tvNoWolooFound!!.visibility = View.GONE
            }
        } else {
           // dotLayoutCreator(nearByStoreResponseList.size)
            pullToRefreshLayout!!.visibility = View.VISIBLE
            rvHomeCategory!!.visibility = View.VISIBLE
            if (parentFragment is HomeFragment) {
                tv_search_more!!.visibility = View.GONE
            }
            tvNoWolooFound!!.visibility = View.GONE
            if (isEnroute) {
                Logger.e("initViews", TAG + requireActivity().localClassName)
                tv_search!!.visibility = View.GONE
                tv_search_more!!.visibility = View.GONE
                noWolooTitle!!.visibility = View.GONE
                noWolooSearchText!!.visibility = View.GONE
                tvNoWolooFound!!.visibility = View.GONE

            }
        }
        if (nearByStoreResponseList.size == 0 && pageNumber != 1) {
            stopLoading = true
        }
    }

   /* fun dotLayoutCreator(size: Int) {
        for (i in 0 until size) {
            val dot = View(requireActivity().applicationContext)
            val params = LinearLayout.LayoutParams(20, 20)
            params.setMargins(10, 0, 10, 0)
            dot.layoutParams = params
            dot.setBackgroundResource(R.color.white)
            dotsLayout!!.addView(dot)
            dotViews.add(dot)
            updateDotColor(i)
        }
    }

    private fun updateDotColor(selectedIndex: Int) {
        for (i in dotViews.indices) {
            if (i == selectedIndex) {
                dotViews.get(i).setBackgroundResource(R.color.start_theme_color)
            } else {
                dotViews.get(i).setBackgroundResource(R.color.chip_color)
            }
        }
    }*/

  /*  fun dotLayoutCreator(size: Int , selectedIndex : Int) {
        dotViews.clear()
        dotsLayout!!.removeAllViews()

        // Determine the maximum number of dots to show (up to 5)
        val maxDotsToShow = minOf(size, 5)
        for (i in 0 until maxDotsToShow) {
            val dot = View(requireActivity().applicationContext)
            val params = LinearLayout.LayoutParams(20, 20)
            params.setMargins(10, 0, 10, 0)
            dot.layoutParams = params

            // Initially set the unselected dot style (white filled, black border)
            dot.setBackgroundResource(R.drawable.dot_unselected)  // Use the unselected dot drawable
            dotsLayout!!.addView(dot)
            dotViews.add(dot)

            // Update the dot color based on its position (default is unselected)
            updateDotColor(selectedIndex)
        }
    }

    private fun updateDotColor(selectedIndex: Int) {
        for (i in dotViews.indices) {
            if (i == selectedIndex) {
                // Set selected dot to yellow with black border
                dotViews[i].setBackgroundResource(R.drawable.dot_selected)  // Use the selected dot drawable
            } else {
                // Set unselected dot to white with black border
                dotViews[i].setBackgroundResource(R.drawable.dot_unselected)  // Use the unselected dot drawable
            }
        }
    }*/


    private fun dotLayoutCreator(listSize: Int, selectedIndex: Int) {
        // Clear any previously added dots
        dotViews.clear()
        dotsLayout!!.removeAllViews()

        // Define the group size (for example, groups of 3 items)
        val groupSize = (listSize/10)

        // Calculate how many dots are needed (one for each group)
        val numberOfDots = (listSize + groupSize - 1) / groupSize // This will round up to cover all items

        // Loop through and create dots based on the number of groups
        for (i in 0 until numberOfDots) {
            val dot = View(requireActivity().applicationContext)
            val params = LinearLayout.LayoutParams(20, 20)
            params.setMargins(10, 0, 10, 0)
            dot.layoutParams = params

            // Initially set all dots to unselected (white filled, black border)
            dot.setBackgroundResource(R.drawable.dot_unselected)  // Use the unselected dot drawable
            dotsLayout!!.addView(dot)
            dotViews.add(dot)
        }

        // Update the color of the dots based on the selected group
        updateDotColor(selectedIndex, groupSize)
    }

    private fun updateDotColor(selectedIndex: Int, groupSize: Int) {
        // Calculate which group is selected
        val selectedGroupIndex = selectedIndex / groupSize

        // Loop through all the created dot views and set the correct drawable
        for (i in dotViews.indices) {
            if (i == selectedGroupIndex) {
                // Set the selected dot to yellow with a black border
                dotViews[i].setBackgroundResource(R.drawable.dot_selected)  // Use the selected dot drawable
            } else {
                // Set unselected dots to white with a black border
                dotViews[i].setBackgroundResource(R.drawable.dot_unselected)  // Use the unselected dot drawable
            }
        }
    }


    companion object {
        var TAG = HomeCategoryFragment::class.java.simpleName
    }
}