package `in`.woloo.www.more.home_shop

import android.view.View
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import `in`.woloo.www.R


@EpoxyModelClass(layout = R.layout.carousel_with_background)
abstract class CustomCarouselModel : EpoxyModelWithHolder<CustomCarouselModel.Holder>() {

    @EpoxyAttribute
    var models: List<EpoxyModel<*>> = emptyList()

    override fun bind(holder: Holder) {
        holder.recyclerView.setModels(models)
    }

    class Holder : EpoxyHolder() {
        lateinit var recyclerView: Carousel

        override fun bindView(itemView: View) {
            recyclerView = itemView.findViewById(R.id.carousel)
        }
    }
}