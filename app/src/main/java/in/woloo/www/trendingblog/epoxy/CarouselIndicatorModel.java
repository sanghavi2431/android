package in.woloo.www.trendingblog.epoxy;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.epoxy.Carousel;
import com.airbnb.epoxy.CarouselModel_;
import com.airbnb.epoxy.ModelView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import in.woloo.www.customview.CircularPagerIndicator;
import in.woloo.www.utils.Logger;

public class CarouselIndicatorModel extends CarouselModel_ {
    private final CircularPagerIndicator indicator = new CircularPagerIndicator();

    @Override
    public void bind(Carousel carousel) {
        super.bind(carousel);
        carousel.addItemDecoration(indicator);
        Logger.i("MODELS", "" + models().size());
    }

    @Override
    public void unbind(Carousel carousel) {
        super.unbind(carousel);
        carousel.removeItemDecoration(indicator);
    }


}
