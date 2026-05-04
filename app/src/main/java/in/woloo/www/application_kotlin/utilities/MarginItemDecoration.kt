package `in`.woloo.www.application_kotlin.utilities

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginItemDecoration () : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

      //  val totalWidth = parent.width
        val margin = 1

        outRect.left = margin
        outRect.right = margin
    }
}

