package `in`.woloo.www.more.trendingblog.epoxy

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.base.BaseEpoxyHolder

@EpoxyModelClass(layout = R.layout.model_title)
abstract class TrendingBlogTextModel : EpoxyModelWithHolder<TrendingBlogTextModel.Holder>() {
    @JvmField
    @EpoxyAttribute
    var title: String? = null
    override fun bind(holder: Holder) {
        /*holder.tvTitle.setText(title)*/
    }

    class Holder : BaseEpoxyHolder() { /*  @BindView(R.id.tvTitle)
        TextView tvTitle;*/
    }
}
