package `in`.woloo.www.more.home_shop

import android.widget.TextView
import butterknife.BindView
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.base.BaseEpoxyHolder

@EpoxyModelClass(layout = R.layout.model_no_blogs)
abstract class NoShopsBlogsModel : EpoxyModelWithHolder<NoShopsBlogsModel.Holder>() {

    class Holder : BaseEpoxyHolder() {


    /*  @JvmField
        @BindView(R.id.tvNoBlogs)
        var tvNoBlogs: TextView? = null*/

    //    val tvNoBlogs by bind<TextView>(R.id.tvNoBlogs)

        @BindView(R.id.tvNoBlogs)
        lateinit var tvNoBlogs: TextView


    }
}
