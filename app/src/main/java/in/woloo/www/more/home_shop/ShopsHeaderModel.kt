package `in`.woloo.www.more.home_shop

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import `in`.woloo.www.application_kotlin.base.BaseEpoxyHolder
import `in`.woloo.www.R


@EpoxyModelClass(layout = R.layout.shop_screen_heder)
public abstract class ShopsHeaderModel  : EpoxyModelWithHolder<ShopsHeaderModel.Holder>() {
    @EpoxyAttribute
    var title: String? = null
    override fun bind(holder: Holder) {
        /*holder.tvTitle.setText(title)*/
    }

    class Holder : BaseEpoxyHolder() { /*  @BindView(R.id.tvTitle)
        TextView tvTitle;*/
    }
}
