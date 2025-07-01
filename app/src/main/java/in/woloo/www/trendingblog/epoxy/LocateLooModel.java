package in.woloo.www.trendingblog.epoxy;

import android.view.View;

import androidx.annotation.NonNull;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;

import butterknife.BindView;
import in.woloo.www.R;
import in.woloo.www.base.BaseEpoxyHolder;

@EpoxyModelClass(layout = R.layout.model_locate_loo)
public abstract class LocateLooModel extends EpoxyModelWithHolder<LocateLooModel.Holder> {
    @EpoxyAttribute
    BlogController.OnClickBlogViewItems onClickBlogViewItems;

    @Override
    public void bind(@NonNull Holder holder) {
        holder.cvRoot.setOnClickListener(view -> {
            onClickBlogViewItems.onClickLocateLoo();
        });
    }

    static class Holder extends BaseEpoxyHolder {
        @BindView(R.id.cvRoot)
        View cvRoot;
    }
}
