package `in`.woloo.www.more.refer_woloo_host

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import `in`.woloo.www.R
import `in`.woloo.www.app.WolooApplication
import `in`.woloo.www.application_kotlin.database.SharedPrefSettings.Companion.getPreferences
import `in`.woloo.www.application_kotlin.database.SharedPreference
import `in`.woloo.www.more.refer_woloo_host.Adapters.ReferredWolooHostListingAdapter
import `in`.woloo.www.v2.woloo.viewmodel.WolooViewModel

class ReferredWolooHostListing : AppCompatActivity() {
    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: LinearLayout? = null

    @JvmField
    @BindView(R.id.screen_header)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.referWolooTv)
    var referWolooTv: TextView? = null

    @JvmField
    @BindView(R.id.referred_Woloo_host_rv)
    var referred_Woloo_host_rv: RecyclerView? = null

    @JvmField
    @BindView(R.id.referWolooImv)
    var referWolooImv: ImageView? = null
    private var referredWolooHostListingAdapter: ReferredWolooHostListingAdapter? = null
    private var wolooViewModel: WolooViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refered_woloo_host_listing)
        ButterKnife.bind(this)
        wolooViewModel = ViewModelProvider(this).get(
            WolooViewModel::class.java
        )
        initView()
        setLiveData()
    }

    private fun initView() {
        tvTitle!!.text = "Refer a Woloo Host"
        ivBack!!.setOnClickListener { v: View? -> onBackPressed() }
        val mSharedPref = SharedPreference(this)
        val authConfigResponse = getPreferences.fetchAuthConfig()
        referWolooTv!!.text = authConfigResponse!!.getcUSTOMMESSAGE()!!.wolooReferHostText
        referWolooImv!!.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this,
                    referWolooFormActivity::class.java
                )
            )
        }
    }

    fun setLiveData() {
        wolooViewModel!!.observeRecommendWolooList().observe(this) { referredWolooListResponse ->
            if (referredWolooListResponse != null && referredWolooListResponse.data != null) {
                if (referredWolooListResponse.data!!.size > 0) {
                    referred_Woloo_host_rv!!.visibility = View.VISIBLE
                    referredWolooHostListingAdapter = ReferredWolooHostListingAdapter(
                        this@ReferredWolooHostListing,
                        referredWolooListResponse.data!!
                    )
                    referred_Woloo_host_rv!!.setHasFixedSize(true)
                    val linearLayoutManager = LinearLayoutManager(
                        applicationContext
                    )
                    referred_Woloo_host_rv!!.layoutManager = linearLayoutManager
                    referred_Woloo_host_rv!!.adapter = referredWolooHostListingAdapter
                    var underReviewCnt = 0
                    for (i in referredWolooListResponse.data!!.indices) {
                        if (referredWolooListResponse.data!!.get(i).status == 0) underReviewCnt++
                    }
                    if (underReviewCnt >= 3) referWolooImv!!.visibility =
                        View.GONE else referWolooImv!!.visibility = View.VISIBLE
                } else referred_Woloo_host_rv!!.visibility = View.GONE
            } else {
                WolooApplication.errorMessage = ""
            }
        }
    }

    override fun onResume() {
        super.onResume()
        wolooViewModel!!.getRecommendWolooList()
    }
}