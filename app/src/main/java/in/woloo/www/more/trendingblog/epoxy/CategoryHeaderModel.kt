package `in`.woloo.www.more.trendingblog.epoxy

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.base.BaseEpoxyHolder

@EpoxyModelClass(layout = R.layout.category_header_blogs)
abstract class CategoryHeaderModel : EpoxyModelWithHolder<CategoryHeaderModel.Holder>() {
    @JvmField
    @EpoxyAttribute
    var categoriesList: String? = null
    override fun bind(holder: Holder) {
        /*holder.tvTitle.setText(title)*/
    }

    class Holder : BaseEpoxyHolder() { /*  @BindView(R.id.tvTitle)
        TextView tvTitle;*/
    }
}
