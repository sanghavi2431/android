package in.woloo.www.trendingblog.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import org.jetbrains.annotations.NotNull;

import in.woloo.www.R;
import in.woloo.www.dashboard.WolooDashboard;
import in.woloo.www.search.SearchWolooActivity;
import in.woloo.www.trendingblog.model.NearByWolooAndOfferCountResponse;
import in.woloo.www.v2.search.SearchActivity;

public class TrendPagerAdapter extends PagerAdapter {

    private LayoutInflater layoutInflater;
    public int[] layouts;
    public Context context;
    NearByWolooAndOfferCountResponse nearByWolooAndOfferCountResponse;

    public TrendPagerAdapter(Context context, int[] layouts, NearByWolooAndOfferCountResponse nearByWolooAndOfferCountResponse) {
        this.context = context;
        this.layouts = layouts;
        this.nearByWolooAndOfferCountResponse = nearByWolooAndOfferCountResponse;
        layoutInflater = LayoutInflater.from(context);
    }

    public Object instantiateItem(ViewGroup container, int position) {
        View view;
        if (position < 2)
            view = layoutInflater.inflate(layouts[position], container, false);
        else
            view = layoutInflater.inflate(R.layout.shop_offer_layout, container, false);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position == 0){
                    Intent intent = new Intent(context, WolooDashboard.class);
                    context.startActivity(intent);
                }else if(position == 1){
                    Intent intent = new Intent(context, SearchActivity.class);
                    context.startActivity(intent);
                }
              //  offerPopUpwindow(v);
            }
        });

        if(position==0){
            TextView tv = view.findViewById(R.id.banTextMain);
            tv.setText("we have "+nearByWolooAndOfferCountResponse.getData().getWolooCount()+" woloo host available in your location");
        }/*else if(position==1){
            TextView tv = view.findViewById(R.id.banTextMain2);
            tv.setText("we have "+nearByWolooAndOfferCountResponse.getData().getOfferCount()+" woloo host available with the Offers");
        }*/else {
            TextView tv = view.findViewById(R.id.banTextMain3);
            tv.setText(nearByWolooAndOfferCountResponse.getData().getShopOffer().get(position-2).getDescription());
            TextView tv_new = view.findViewById(R.id.banTextSub3);
            tv_new.setText(nearByWolooAndOfferCountResponse.getData().getShopOffer().get(position-2).getCouponCode());
        }

        container.addView(view, 0);
        return view;
    }

    @Override
    public int getCount() {
        if (nearByWolooAndOfferCountResponse.getData().getShopOffer() != null && nearByWolooAndOfferCountResponse.getData().getShopOffer().size() > 0)
            return layouts.length + nearByWolooAndOfferCountResponse.getData().getShopOffer().size();
        else
            return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View) object);
    }

    public void offerPopUpwindow(View v) {
        context = v.getContext();
        final Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent)));
        dialog.setContentView(R.layout.feature_benefits_offer_popup);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        RelativeLayout relativeLayoutVisitOffer = dialog.findViewById(R.id.offerforVisitRel);
        ImageView cancelImg = dialog.findViewById(R.id.ivClose);

        cancelImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
