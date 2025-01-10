package `in`.woloo.www.more.woloo_host

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.tabs.TabLayout
import `in`.woloo.www.R
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.more.woloo_host.mvp.BecomeWolooHostView
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import `in`.woloo.www.utils.Utility.logFirebaseEvent
import `in`.woloo.www.utils.Utility.logNetcoreEvent

class BecomeWolooHostFragment : Fragment(), BecomeWolooHostView {
    @JvmField
    @BindView(R.id.ivBack)
    var ivBack: LinearLayout? = null

    @JvmField
    @BindView(R.id.screen_header)
    var tvTitle: TextView? = null

    @JvmField
    @BindView(R.id.VP_viewPager)
    var viewPager: ViewPager? = null

    @JvmField
    @BindView(R.id.tutorial_slider_tab)
    var tutorial_slider_tab: TabLayout? = null

    @JvmField
    @BindView(R.id.intrested_Btn)
    var btnInterest: TextView? = null

    @JvmField
    @BindView(R.id.ivSwipe)
    var swipeView: LinearLayout? = null

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    /*calling on onCreate*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
        Logger.i(TAG, "onCreate")
    }

    /*calling on onCreateView*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.fragment_become_woloo_host, container, false)
        ButterKnife.bind(this, root)
        initViews()
        Logger.i(TAG, "onCreateView")
        return root
    }

    /*calling on initViews*/
    private fun initViews() {
        try {
            Logger.i(TAG, "initViews")
            tvTitle!!.text = getText(R.string.become_a_woloo_host)
            ivBack!!.setOnClickListener { v: View? ->
                //getActivity().onBackPressed();
                val fm = requireActivity().supportFragmentManager
                fm.popBackStack()
            }
            val adapter: ViewPagerAdapter = ViewPagerAdapter(
                requireActivity()
            )
            viewPager!!.adapter = adapter
            viewPager!!.currentItem = 0
            viewPager!!.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    if (position == 3) {
                        swipeView!!.visibility = View.GONE
                    } else {
                        swipeView!!.visibility = View.VISIBLE
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {}
            })
            swipeView!!.setOnClickListener {
                if (viewPager!!.currentItem == 0) {
                    viewPager!!.currentItem = 1
                } else if (viewPager!!.currentItem == 1) {
                    viewPager!!.currentItem = 2
                } else if (viewPager!!.currentItem == 2) {
                    viewPager!!.currentItem = 3
                }
            }
            tutorial_slider_tab!!.setupWithViewPager(viewPager)
            btnInterest!!.setOnClickListener { v: View? ->
                val bundle = Bundle()
                logFirebaseEvent(activity, bundle, AppConstants.YES_INTERESTED_CLICK)
                val payload = HashMap<String, Any>()
                logNetcoreEvent(requireActivity(), payload, AppConstants.YES_INTERESTED_CLICK)
                startActivity(Intent(context, CreateWolooHostActivity::class.java))
            }
            /*  tvSkip.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                Utility.logFirebaseEvent(getActivity(), bundle, AppConstants.YES_INTERESTED_CLICK);
                HashMap<String,Object> payload = new HashMap<>();
                Utility.logNetcoreEvent(getActivity(),payload,AppConstants.YES_INTERESTED_CLICK);

                startActivity(new Intent(getContext(), CreateWolooHostActivity.class));
            });*/
        } catch (ex: Exception) {
            CommonUtils.printStackTrace(ex)
        }
    }

    inner class ViewPagerAdapter(private val mContext: Context) : PagerAdapter() {
        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            var resId = 0
            when (position) {
                0 -> resId = R.layout.slider_1
                1 -> resId = R.layout.slider_2
                2 -> resId = R.layout.slider_3
                3 -> resId = R.layout.slider_4
            }
            val inflater = LayoutInflater.from(mContext)
            val layout = inflater.inflate(resId, collection, false) as ViewGroup
            collection.addView(layout)
            return layout
        }

        override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
            collection.removeView(view as View)
        }

        override fun getCount(): Int {
            return 4
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        var TAG = BecomeWolooHostFragment::class.java.simpleName

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BecomeWolooHostFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String?, param2: String?): BecomeWolooHostFragment {
            val fragment = BecomeWolooHostFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}