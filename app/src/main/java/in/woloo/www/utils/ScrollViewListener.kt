package `in`.woloo.www.utils

import androidx.recyclerview.widget.RecyclerView

interface ScrollViewListener {
    fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int)
    fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int)
}
