package `in`.woloo.www.application_kotlin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import `in`.woloo.www.R


//class PagerAdapterHorizontal (fragment: Fragment) : FragmentStateAdapter(fragment) {
//    override fun createFragment(position: Int): Fragment {
//        return if (position == 0) {
//            HomeCategoryFragment()
//        } else {
//            ShowMoreFragment()
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return 2
//    }
//}


class PagerAdapterHorizontal(context: Context) :
    RecyclerView.Adapter<PagerAdapterHorizontal.ViewHolder>() {
    private val context: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the layout for each page of the ViewPager
        val view: View = LayoutInflater.from(context).inflate(R.layout.fragment_home_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Set up the data for each page (e.g., updating the RecyclerView inside each page)
    }

    override fun getItemCount(): Int {
        return 2 // Two pages in the ViewPager
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!)
}
