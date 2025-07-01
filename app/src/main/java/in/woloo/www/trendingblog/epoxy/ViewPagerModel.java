package in.woloo.www.trendingblog.epoxy;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;

import butterknife.BindView;
import in.woloo.www.R;
import in.woloo.www.base.BaseEpoxyHolder;
import in.woloo.www.utils.Logger;

@EpoxyModelClass(layout = R.layout.item_blog_banner)
public abstract class ViewPagerModel extends EpoxyModelWithHolder<ViewPagerModel.Holder> {
    @EpoxyAttribute
    String pageType;//LOCATION, OFFER, SHOP

    @EpoxyAttribute
    String title;

    @EpoxyAttribute
    String subTitle;

    @EpoxyAttribute
    @DrawableRes
    int icon;

    @EpoxyAttribute
    @DrawableRes
    int itemPosition;

    @EpoxyAttribute
    BlogController.OnClickBlogViewItems onClickBlogViewItems;

    @Override
    public void bind(@NonNull Holder holder) {
        holder.tvTitle.setText(title);
        holder.tvSubTitle.setText(subTitle);
        holder.tvCoupon.setText(subTitle);
        holder.ivIcon.setImageResource(icon);
        if (pageType.equalsIgnoreCase("SHOP")) {
            holder.btnCopyCoupon.setVisibility(View.VISIBLE);
            holder.tvCoupon.setVisibility(View.VISIBLE);
            holder.tvSubTitle.setVisibility(View.GONE);
        } else {
            holder.btnCopyCoupon.setVisibility(View.INVISIBLE);
            holder.tvCoupon.setVisibility(View.GONE);
            holder.tvSubTitle.setVisibility(View.VISIBLE);
        }
        holder.btnCopyCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBlogViewItems.onClickPagerItemShopCoupon(holder.tvSubTitle.getText().toString());
            }
        });
        holder.root.setOnClickListener(view -> {
            switch (pageType) {
                case "LOCATION":
                    onClickBlogViewItems.onClickPagerItemWolooLocation();
                    break;
                case "OFFER":
                    onClickBlogViewItems.onClickPagerItemWolooOffers();
                    break;
                case "SHOP":
                    onClickBlogViewItems.onClickPagerItemShopCoupon(holder.tvSubTitle.getText().toString());
                    break;
            }
        });
    }

    static class Holder extends BaseEpoxyHolder {
        @BindView(R.id.root)
        View root;
        @BindView(R.id.banTextMain)
        TextView tvTitle;
        @BindView(R.id.banTextSub)
        TextView tvSubTitle;
        @BindView(R.id.trendLocateSrcimg)
        ImageView ivIcon;
        @BindView(R.id.btnCopyCoupon)
        Button btnCopyCoupon;
        @BindView(R.id.tvCoupon)
        TextView tvCoupon;
    }
}
