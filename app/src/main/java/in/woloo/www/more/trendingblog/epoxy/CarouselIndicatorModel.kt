package `in`.woloo.www.more.trendingblog.epoxy

import androidx.core.content.ContextCompat
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.CarouselModel_
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.utilities.CircularPagerIndicator
import `in`.woloo.www.utils.Logger

class CarouselIndicatorModel : CarouselModel_() {
    private val indicator: CircularPagerIndicator = CircularPagerIndicator()
    override fun bind(carousel: Carousel) {
        super.bind(carousel)
        carousel.addItemDecoration(indicator)
        Logger.i("MODELS", "" + models().size)
        carousel.setBackgroundColor(
            ContextCompat.getColor(
                carousel.context,
                R.color.red
            )
        ) // Replace 'your_color' with your color resource
    }

    override fun unbind(carousel: Carousel) {
        super.unbind(carousel)
        carousel.removeItemDecoration(indicator)
    }
}
