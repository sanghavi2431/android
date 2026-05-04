package `in`.woloo.www.more.trendingblog.epoxy

import android.widget.TextView
import butterknife.BindView
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.base.BaseEpoxyHolder

@EpoxyModelClass(layout = R.layout.model_no_blogs)
abstract class NoBlogsModel : EpoxyModelWithHolder<NoBlogsModel.Holder?>() {

    class Holder : BaseEpoxyHolder() {
        @JvmField
        @BindView(R.id.tvNoBlogs)
        var tvNoBlogs: TextView? = null
    }
}
