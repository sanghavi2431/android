package `in`.woloo.www.more.trendingblog.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.SearchActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.more.trendingblog.model.NearByWolooAndOfferCountResponse

class TrendPagerAdapter(
    var context: Context,
    var layouts: IntArray,
    nearByWolooAndOfferCountResponse: NearByWolooAndOfferCountResponse
) : PagerAdapter() {
    private val layoutInflater: LayoutInflater
    var nearByWolooAndOfferCountResponse: NearByWolooAndOfferCountResponse

    init {
        this.nearByWolooAndOfferCountResponse = nearByWolooAndOfferCountResponse
        layoutInflater = LayoutInflater.from(context)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view: View
        if (position < 2) view =
            layoutInflater.inflate(layouts[position], container, false) else view =
            layoutInflater.inflate(R.layout.shop_offer_layout, container, false)
        view.setOnClickListener {
            if (position == 0) {
                val intent = Intent(context, WolooDashboard::class.java)
                context.startActivity(intent)
            } else if (position == 1) {
                val intent = Intent(context, SearchActivity::class.java)
                context.startActivity(intent)
            }
            //  offerPopUpwindow(v);
        }
        if (position == 0) {
            val tv = view.findViewById<TextView>(R.id.banTextMain)
            tv.text =
                "we have " + nearByWolooAndOfferCountResponse.data!!.wolooCount + " woloo host available in your location"
        } /*else if(position==1){
            TextView tv = view.findViewById(R.id.banTextMain2);
            tv.setText("we have "+nearByWolooAndOfferCountResponse.getData().getOfferCount()+" woloo host available with the Offers");
        }*/ else {
            val tv = view.findViewById<TextView>(R.id.banTextMain3)
            tv.setText(nearByWolooAndOfferCountResponse.data!!.shopOffer!!.get(position - 2).description)
            val tv_new = view.findViewById<TextView>(R.id.banTextSub3)
            tv_new.setText(nearByWolooAndOfferCountResponse.data!!.shopOffer!!.get(position - 2).couponCode)
        }
        container.addView(view, 0)
        return view
    }

    override fun getCount(): Int {
        return if (nearByWolooAndOfferCountResponse.data!!.shopOffer != null && nearByWolooAndOfferCountResponse.data!!.shopOffer!!.isNotEmpty()) layouts.size + nearByWolooAndOfferCountResponse.data!!.shopOffer!!.size else layouts.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun offerPopUpwindow(v: View) {
        context = v.context
        val dialog = Dialog(context, R.style.CustomAlertDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(context.resources.getColor(R.color.transparent)))
        dialog.setContentView(R.layout.feature_benefits_offer_popup)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        val relativeLayoutVisitOffer = dialog.findViewById<RelativeLayout>(R.id.offerforVisitRel)
        val cancelImg = dialog.findViewById<ImageView>(R.id.ivClose)
        cancelImg.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
}
