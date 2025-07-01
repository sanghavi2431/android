package in.woloo.www.trendingblog.epoxy;

import android.widget.TextView;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;

import butterknife.BindView;
import in.woloo.www.R;
import in.woloo.www.base.BaseEpoxyHolder;

@EpoxyModelClass(layout = R.layout.model_title)
public abstract class TrendingBlogTextModel extends EpoxyModelWithHolder<TrendingBlogTextModel.Holder> {
    @EpoxyAttribute
    String title;

    @Override
    public void bind(@NonNull Holder holder) {
        holder.tvTitle.setText(title);
    }

    static class Holder extends BaseEpoxyHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
    }
}
