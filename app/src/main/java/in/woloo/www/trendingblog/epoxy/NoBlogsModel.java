package in.woloo.www.trendingblog.epoxy;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;

import butterknife.BindView;
import in.woloo.www.R;
import in.woloo.www.base.BaseEpoxyHolder;

@EpoxyModelClass(layout = R.layout.model_no_blogs)
public abstract class NoBlogsModel extends EpoxyModelWithHolder<NoBlogsModel.Holder> {

    static class Holder extends BaseEpoxyHolder {
        @BindView(R.id.tvNoBlogs)
        TextView tvNoBlogs;
    }
}
