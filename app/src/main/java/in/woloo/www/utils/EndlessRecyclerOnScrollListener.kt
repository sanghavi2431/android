package `in`.woloo.www.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


abstract class EndlessRecyclerOnScrollListener : RecyclerView.OnScrollListener {
    private var previousTotal = 0 // The total number of items in the dataset after the last load
    private var loading = true // True if we are still waiting for the last set of data to load.
    private val visibleThreshold =
        5 // The minimum amount of items to have below your current scroll position before loading more.
    var firstVisibleItem: Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    private var scrollViewListener: ScrollViewListener? = null

    private var current_page = 1

    private var mLinearLayoutManager: LinearLayoutManager

    constructor(linearLayoutManager: LinearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager
    }

    constructor(linearLayoutManager: LinearLayoutManager, scrollViewListener: ScrollViewListener?) {
        this.mLinearLayoutManager = linearLayoutManager
        this.scrollViewListener = scrollViewListener
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount
        totalItemCount = mLinearLayoutManager.itemCount
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()

        if (scrollViewListener != null) {
            scrollViewListener!!.onScrolled(recyclerView, dx, dy)
        }

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && ((totalItemCount - visibleItemCount)
                    <= (firstVisibleItem + visibleThreshold))
        ) {
            // End has been reached

            // Do something

            current_page++

            onLoadMore(current_page)

            loading = true
        }
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (scrollViewListener != null) {
            scrollViewListener!!.onScrollStateChanged(recyclerView, newState)
        }
        when (newState) {
            RecyclerView.SCROLL_STATE_IDLE -> println("The RecyclerView is not scrolling")
            RecyclerView.SCROLL_STATE_DRAGGING -> println("Scrolling now")
            RecyclerView.SCROLL_STATE_SETTLING -> println("Scroll Settling")
        }
    }

    fun reset() {
        current_page = 1
        previousTotal = 0
        loading = true
    }

    abstract fun onLoadMore(current_page: Int)

    companion object {
        var TAG: String = EndlessRecyclerOnScrollListener::class.java.simpleName
    }
}