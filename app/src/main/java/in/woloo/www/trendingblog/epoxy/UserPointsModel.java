package in.woloo.www.trendingblog.epoxy;

import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;

import butterknife.BindView;
import in.woloo.www.BuildConfig;
import in.woloo.www.R;
import in.woloo.www.base.BaseEpoxyHolder;
import in.woloo.www.utils.ImageUtil;
import in.woloo.www.v2.profile.model.UserProfile;

@EpoxyModelClass(layout = R.layout.model_user_points)
public abstract class UserPointsModel extends EpoxyModelWithHolder<UserPointsModel.Holder> {
    @EpoxyAttribute
    String pointsCount;
    @EpoxyAttribute
    String iconUrl;
    @EpoxyAttribute
    UserProfile userProfile;
    @EpoxyAttribute
    BlogController.OnClickBlogViewItems onClickBlogViewItems;

    @Override
    public void bind(@NonNull Holder holder) {
        if (userProfile == null || TextUtils.isEmpty(userProfile.getProfile().getAvatar())) {
            holder.ivUserImage.setImageDrawable(ContextCompat.getDrawable(holder.ivUserImage.getContext(), R.drawable.ic_account_circle));
        } else {
            if (userProfile.getProfile().getAvatar().trim().equals("users/default.png") || userProfile.getProfile().getAvatar().trim().equals("default.png")) {
                ImageUtil.loadImageProfile(holder.ivUserImage.getContext(), holder.ivUserImage, BuildConfig.BASE_URL + "public/userProfile/default.png");
            } else {
                ImageUtil.loadImageProfile(holder.ivUserImage.getContext(), holder.ivUserImage, BuildConfig.BASE_URL + "public/userProfile/" + userProfile.getProfile().getAvatar());
            }
        }
        if (userProfile != null && userProfile.getTotalCoins() != null && userProfile.getTotalCoins() != null) {
            holder.tvPointsCount.setText(String.valueOf(userProfile.getTotalCoins().getTotalCoins()));
        }

        holder.ivUserImage.setOnClickListener(v -> {
            onClickBlogViewItems.onClickUserThumb();
        });

        holder.ibShop.setOnClickListener(v -> {
            onClickBlogViewItems.onClickShop();
        });
    }

    static class Holder extends BaseEpoxyHolder {
        @BindView(R.id.tvPointsCount)
        TextView tvPointsCount;
        @BindView(R.id.civProfileImage)
        ImageView ivUserImage;
        @BindView(R.id.ibShop)
        ImageButton ibShop;
    }
}
