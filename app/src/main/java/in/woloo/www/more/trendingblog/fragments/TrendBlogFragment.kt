package `in`.woloo.www.more.trendingblog.fragments

import android.Manifest
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.airbnb.epoxy.EpoxyRecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import `in`.woloo.www.R
import `in`.woloo.www.application_kotlin.api_classes.BaseResponse
import `in`.woloo.www.application_kotlin.api_classes.MessageResponse
import `in`.woloo.www.application_kotlin.model.server_request.NearByWolooAndOfferCountRequest
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.SearchActivity
import `in`.woloo.www.application_kotlin.presentation.activities.loo_discovery.WolooDashboard
import `in`.woloo.www.application_kotlin.presentation.fragments.web_view.WebViewFragment
import `in`.woloo.www.application_kotlin.presentation.fragments.web_view.WebViewFragment.Companion.newInstance
import `in`.woloo.www.application_kotlin.view_models.HomeViewModel
import `in`.woloo.www.dashboard.ui.home.HomeFragment
import `in`.woloo.www.more.editprofile.profile.viewmodel.ProfileViewModel
import `in`.woloo.www.more.models.UserProfileMergedResponse
import `in`.woloo.www.more.my_account.MyAccountFragment
import `in`.woloo.www.more.trendingblog.adapter.utils.EndlessRecyclerViewScrollListener
import `in`.woloo.www.more.trendingblog.epoxy.BlogController
import `in`.woloo.www.more.trendingblog.model.BlogByCategoryResponse
import `in`.woloo.www.more.trendingblog.model.CategoriesResponse
import `in`.woloo.www.more.trendingblog.model.NearByWolooAndOfferCountResponse
import `in`.woloo.www.more.trendingblog.model.blog.Blog
import `in`.woloo.www.more.trendingblog.model.blog.BlogsResponse
import `in`.woloo.www.more.trendingblog.model.blog.Category
import `in`.woloo.www.more.trendingblog.mvp.NearByWolooAndOfferCountPresenter
import `in`.woloo.www.more.trendingblog.mvp.NearByWolooAndOfferCountView
import `in`.woloo.www.more.trendingblog.viewmodel.BlogViewModel
import `in`.woloo.www.shopping.config.Config
import `in`.woloo.www.shopping.fragments.ProductDetailsFragment
import `in`.woloo.www.shopping.fragments.ProductListFragment
import `in`.woloo.www.shopping.fragments.ShoppingFragment
import `in`.woloo.www.shopping.model.ProductModel
import `in`.woloo.www.utils.AppConstants
import `in`.woloo.www.utils.Logger
import org.json.JSONArray
import org.json.JSONObject
import `in`.woloo.www.more.trendingblog.epoxy.BlogController.OnClickBlogViewItems

class TrendBlogFragment : Fragment(), NearByWolooAndOfferCountView,
    OnClickBlogViewItems {
    @JvmField
    @BindView(R.id.epoxyRV)
    var epoxyRecyclerView: EpoxyRecyclerView? = null
    var prodName: String? = null
    var prodPrice: String? = null
    var prodDesc: String? = null
    private var nearByWolooAndOfferCountPresenter: NearByWolooAndOfferCountPresenter? = null
    private var homeViewModel: HomeViewModel? = null
    private var profileViewModel: ProfileViewModel? = null
    private var blogViewModel: BlogViewModel? = null
    private var blogController: BlogController? = null
    private val categories: MutableList<Category> = ArrayList()
    private val blogs: MutableList<Blog> = ArrayList()
    private var isCategoryLoaded = false
    private var fusedLocationClient: FusedLocationProviderClient? = null

    //private LoadMoreRecyclerViewScrollListener loadMoreScrollListener;
    private var loadMoreScrollListener: EndlessRecyclerViewScrollListener? = null
    private var page = 1
    private var previousPage = page
    private var category = "all"
    private var currentBlog: Blog? = null
    private val subcategoryUrl = Config.hostname + "get_product_details_api.php"
    private var mRequestQueue: RequestQueue? = null
    val productItems: ArrayList<ProductModel> = ArrayList<ProductModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        (requireActivity() as WolooDashboard).hideToolbar()
        val rootView = inflater.inflate(R.layout.fragment_trend_blog, container, false)
        ButterKnife.bind(this, rootView)
        nearByWolooAndOfferCountPresenter = NearByWolooAndOfferCountPresenter(requireContext(), this)
        homeViewModel = ViewModelProvider(this).get<HomeViewModel>(HomeViewModel::class.java)
        profileViewModel = ViewModelProvider(this).get(
            ProfileViewModel::class.java
        )
        blogViewModel = ViewModelProvider(this).get<BlogViewModel>(BlogViewModel::class.java)
        nearbyWolooAndOffers
        setLiveData()
        //nearByWolooAndOfferCountPresenter.getBlogCategories();
//        nearByWolooAndOfferCountPresenter.getBlogs(category, page);
        blogViewModel!!.getBlogsForUserByCategory(category, page, AppConstants.BLOG_CAT_TYPE_TWO)
        profileViewModel!!.getUserProfile()
        // Epoxy
        val linearLayoutManager = LinearLayoutManager(requireActivity())
        epoxyRecyclerView!!.layoutManager = linearLayoutManager
        /*loadMoreScrollListener = new LoadMoreRecyclerViewScrollListener((LinearLayoutManager) epoxyRecyclerView.getLayoutManager()) {
            @Override
            public void fetchNextPage() {
                Log.i(TAG, "fetchNextPage");
                page++;
                nearByWolooAndOfferCountPresenter.getBlogs("all", page);
            }
        };*/loadMoreScrollListener =
            object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
                override fun onLoadMore(p: Int, totalItemsCount: Int, view: RecyclerView?) {
                    Logger.d(TAG, "onLoadMore $page $totalItemsCount")
                    if (page > previousPage) {
//                    nearByWolooAndOfferCountPresenter.getBlogs(category, page);
                        previousPage =
                            if (WolooDashboard.Companion.blogType.matches("0".toRegex())) {
                                blogViewModel!!.getBlogsForUserByCategory(
                                    category,
                                    page,
                                    AppConstants.BLOG_CAT_TYPE_TWO
                                )
                                page
                            } else {
                                blogViewModel!!.getBlogsForUserByCategory(
                                    category,
                                    page,
                                    AppConstants.BLOG_CAT_TYPE_TWO
                                )
                                page
                            }
                    }
                }
            }
        epoxyRecyclerView!!.addOnScrollListener(loadMoreScrollListener!!)
        blogController = BlogController(requireActivity(), this)
        epoxyRecyclerView!!.setController(blogController!!)
        return rootView
    }

    private fun setLiveData() {
        homeViewModel!!.observeNearByWolooAndOfferCount()
            .observe(viewLifecycleOwner, Observer<BaseResponse<NearByWolooAndOfferCountResponse.Data>> { response ->
                if (response != null && response.data != null) {
                    blogController!!.setNearbyWolooAndOffer(response.data)
                }
            })
        profileViewModel!!.observeUserProfile().observe(viewLifecycleOwner) { response ->
            if (response != null && response.data != null) {
                blogController!!.setUserProfileDetails(response.data)
            }
        }
        blogViewModel!!.observeCtaFavourite().observe(viewLifecycleOwner, Observer<BaseResponse<JSONObject>> { response ->
            if (response != null && response.data != null) {
                onFavouriteABlog()
            }
        })
        blogViewModel!!.observeCtaLikes().observe(viewLifecycleOwner, Observer<BaseResponse<JSONObject>> { response ->
            if (response != null && response.data != null) {
                onLikeABlog()
            }
        })
        blogViewModel!!.observeCtaBlogRead().observe(viewLifecycleOwner, Observer<BaseResponse<JSONObject>> { response ->
            if (response != null && response.data != null) {
                onReadABlog()
            }
        })
        blogViewModel!!.observeBlogReadPoint()
            .observe(viewLifecycleOwner, Observer<BaseResponse<MessageResponse>> { response ->
                if (response != null && response.data != null) {
                    onBlogReadPointsAdded()
                }
            })
        blogViewModel!!.observeBlogsForUserByCategory()
            .observe(viewLifecycleOwner, Observer<BaseResponse<BlogByCategoryResponse>> { response ->
                if (response.success) {
                    // Set categories
                    if (!isCategoryLoaded) {
                        val categoryAll = Category()
                        categoryAll.id = 0
                        categoryAll.categoryName = "All"
                        categories.add(0, categoryAll)
                        categories.addAll(response.data!!.categories!!)
                        blogController!!.setCategories(categories, 0)
                        isCategoryLoaded = true
                    }

                    // Set blogs
                    val blogsTemp: List<Blog> = response.data!!.blogs!!
                    /*  List<Blog> blogsTempShop = new ArrayList<>();
                        List<Blog> blogsTempBlog = new ArrayList<>();
                        List<Blog> blogsTempPeriodTracker = new ArrayList<>();
                        String zero = "0";
                        String one = "1";
                        String two = "2";
    */if (blogsTemp != null) {
                        if (blogsTemp.size == 10) {
                            page++
                        }
                        /*  for(int i = 0 ; i < blogsTemp.size() ; i++)
                        {
                            String blogTypeNew = blogsTemp.get(i).getBlogType();
                            if(blogTypeNew.matches(zero))
                            {
                                blogsTempShop.add(blogsTemp.get(i));
                            }
                            else if(blogTypeNew.matches(two))
                            {
                                blogsTempPeriodTracker.add(blogsTemp.get(i));
                            }
                            else {
                                blogsTempBlog.add(blogsTemp.get(i));
                            }
                        }*/

                        //loadMoreScrollListener.setHasMoreToLoad(blogs.size() == 10);
                        /* if(WolooDashboard.Companion.getBlogType().matches(zero))
                            {
                                blogs.clear();
                                blogs.addAll(blogsTempShop);
                                blogController.setBlogItems(blogs);
                            }
                            else  if(WolooDashboard.Companion.getBlogType().matches(one))
                            {
                                blogs.clear();
                                blogs.addAll(blogsTempBlog);
                                blogController.setBlogItems(blogs);
                            }
                            else   if(WolooDashboard.Companion.getBlogType().matches(two))
                            {
                                blogs.clear();
                                blogs.addAll(blogsTempPeriodTracker);
                                blogController.setBlogItems(blogs);
                            }
                            else {*/blogs.clear()
                        blogs.addAll(blogsTemp)
                        blogController!!.setBlogItems(blogs)
                        //}
                    }
                }
            })
    }

    private val nearbyWolooAndOffers: Unit
        private get() {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(requireActivity(), R.string.location_validation, Toast.LENGTH_SHORT)
                    .show()
            } else {
                fusedLocationClient!!.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        //                        nearByWolooAndOfferCountPresenter.getNearByWolooAndOffer(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
                        val request = NearByWolooAndOfferCountRequest()
                        request.lat = location.latitude
                        request.lng = location.longitude
                        homeViewModel?.getNearByWolooAndOfferCount(request)
                    }
                }
            }
        }

    override fun nearByWolooAndOfferCountResponse(nearByWolooAndOfferCountResponse: NearByWolooAndOfferCountResponse?) {
        if (nearByWolooAndOfferCountResponse!!.status == "success") {
            blogController!!.setNearbyWolooAndOffer(nearByWolooAndOfferCountResponse!!.data)
        }
    }

    override fun getCategories(categoriesResponse: CategoriesResponse?) {
        /*if (categoriesResponse.getStatus().equals("success")) {
            CategoriesResponse response = new CategoriesResponse();
            CategoriesResponse.Category categoryAll = response.new Category();
            categoryAll.setId(0);
            //categoryAll.setSelected(true);
            categoryAll.setCategoryName("All");

            categories.add(0, categoryAll);
            categories.addAll(categoriesResponse.getData().getCategories());
            blogController.setCategories(categories, 0);
        }*/
    }

    override fun getBlogs(blogsResponse: BlogsResponse?) {
        if (blogsResponse!!.status == "success") {
            // Set categories
            if (!isCategoryLoaded) {
                val categoryAll = Category()
                categoryAll.id = 0
                categoryAll.categoryName = "All"
                categories.add(0, categoryAll)
                categories.addAll(blogsResponse!!.data!!.categories!!)
                blogController!!.setCategories(categories, 0)
                isCategoryLoaded = true
            }

            // Set blogs
            val blogs = blogsResponse!!.data!!.blogs
            if (blogs != null) {
                if (blogs.size == 10) {
                    page++
                }
                //loadMoreScrollListener.setHasMoreToLoad(blogs.size() == 10);
                this.blogs.addAll(blogs)
                blogController!!.setBlogItems(this.blogs)
            }
        }
    }

    override fun onFavouriteABlog() {
        blogController!!.setBlogItems(blogs)
    }

    override fun onLikeABlog() {
        blogController!!.setBlogItems(blogs)
    }

    override fun onReadABlog() {
        if (currentBlog != null) {
//            nearByWolooAndOfferCountPresenter.addBlogReadPoints(currentBlog);
            blogViewModel!!.blogReadPoint(currentBlog!!.id.toString())
        }
    }

    override fun onBlogReadPointsAdded() {
        blogController!!.setBlogItems(blogs)
        navigateToWebView()
    }

    override fun setUserProfileMergedResponse(userProfileMergedResponse: UserProfileMergedResponse?) {}
    fun offerPopUpwindow() {
        val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!
            .setBackgroundDrawable(ColorDrawable(requireContext().resources.getColor(R.color.transparent)))
        dialog.setContentView(R.layout.feature_benefits_offer_popup)
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        val cancelImg = dialog.findViewById<ImageView>(R.id.ivClose)
        val visitOurWoloo = dialog.findViewById<View>(R.id.visitWolooButtonOffer)
        cancelImg.setOnClickListener { v: View? -> dialog.dismiss() }
        visitOurWoloo.setOnClickListener { view: View? ->
            dialog.dismiss()
            if (requireActivity() is WolooDashboard) {
                (requireActivity() as WolooDashboard).loadFragment(HomeFragment(), "HomeFragment")
                (requireActivity() as WolooDashboard).changeIcon(
                    (requireActivity() as WolooDashboard).nav_view!!.menu.findItem(
                        R.id.navigation_dash_home
                    )
                )
            }
        }
        dialog.show()
    }

    override fun onClickLocateLoo() {
        offerPopUpwindow()
    }

    override fun onClickBlogItem(blog: Blog?, position: Int) {
        /*int colorInt = Color.parseColor("#FFFFFF");
        CustomTabColorSchemeParams defaultColors = new CustomTabColorSchemeParams.Builder()
                .setToolbarColor(colorInt)
                .build();

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setDefaultColorSchemeParams(defaultColors);
        CustomTabsIntent customTabsIntent = builder.build();

        customTabsIntent.launchUrl(requireActivity(), Uri.parse(blog.getDetailedBlogLink()));*/
        currentBlog = blog
        if (requireActivity() is WolooDashboard) {
            if (blog!!.isBlogRead == 0) { // Not yet read
                blogs[position].isBlogRead = 1
                //                nearByWolooAndOfferCountPresenter.readABlog(blog);
                blogViewModel!!.ctaBlogRead(blog.id.toString())
            } else {
                navigateToWebView()
            }
        }
    }

    private fun navigateToWebView() {
        if (currentBlog != null) {
            val webViewFragment = newInstance(
                currentBlog!!.title, currentBlog!!.shortLink, TAG
            )
            (requireActivity() as WolooDashboard).loadMenuFragment(
                webViewFragment,
                WebViewFragment.TAG
            )
            (requireActivity() as WolooDashboard).changeIcon(
                (requireActivity() as WolooDashboard).nav_view!!.getMenu().findItem(
                    R.id.navigation_dash_home
                )
            )
        }
    }

    override fun onClickBlogLike(blog: Blog?, position: Int) {
        blogs[position].isLiked = if (blog!!.isLiked == 0) 1 else 0
        //        nearByWolooAndOfferCountPresenter.likeABlog(blog);
        blogViewModel!!.ctaLikes(blog.id.toString())
    }

    override fun onClickBlogFavourite(blog: Blog?, position: Int) {
        blogs[position].isFavourite = if (blog!!.isFavourite == 0) 1 else 0
        //        nearByWolooAndOfferCountPresenter.favouriteABlog(blog);
        blogViewModel!!.ctaFavourite(blog.id.toString())
    }

    override fun onClickBlogShare(blog: Blog?, position: Int) {
        if (TextUtils.isEmpty(blog!!.title) || TextUtils.isEmpty(blog.shortLink)) {
            return
        }
        val share = Intent(Intent.ACTION_SEND)
        share.setType("text/plain")
        share.putExtra(
            Intent.EXTRA_TEXT, """
     ${blog.title}
     ${blog.shortLink}
     """.trimIndent()
        )
        startActivity(Intent.createChooser(share, null))
    }

    override fun onClickPagerItemWolooLocation() {
        if (requireActivity() is WolooDashboard) {
            (requireActivity() as WolooDashboard).loadFragment(HomeFragment(), "HomeFragment")
            (requireActivity() as WolooDashboard).changeIcon(
                (requireActivity() as WolooDashboard).nav_view!!.menu.findItem(
                    R.id.navigation_dash_home
                )
            )
        }
    }

    override fun onClickPagerItemWolooOffers() {
        val intent = Intent(requireActivity(), SearchActivity::class.java)
        //        intent.putExtra(SearchWolooActivity.ARG_SHOW_OFFERS, true);
        requireActivity().startActivity(intent)
    }

    override fun onClickPagerItemShopCoupon(code: String?) {
        Logger.i(TAG, code.toString())
        val clipboard =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("woloo_coupon", code)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireActivity(), "Coupon copied!", Toast.LENGTH_SHORT).show()
        if (requireActivity() is WolooDashboard) {
            (requireActivity() as WolooDashboard).loadFragment(
                ShoppingFragment.newInstance(
                    "coupon",
                    code
                ), "ShoppingFragment"
            )
            (requireActivity() as WolooDashboard).changeIcon(
                (requireActivity() as WolooDashboard).nav_view!!.getMenu().findItem(
                    R.id.navigation_inviteFriend
                )
            )
        }
    }

    override fun onSelectCategory(position: Int) {
        Logger.i(TAG, position.toString() + "")
        /*for (CategoriesResponse.Category category : categories) {
            category.setSelected(false);
        }
        categories.get(position).setSelected(true);*/loadMoreScrollListener!!.resetState()
        page = 1
        previousPage = page
        blogController!!.setCategories(categories, position)
        category = if (position == 0) "all" else categories[position].id.toString()
        blogs.clear()
        //        nearByWolooAndOfferCountPresenter.getBlogs(category, page);
        // blogViewModel.getBlogsForUserByCategory(category, page);
        if (WolooDashboard.Companion.blogType.matches("0".toRegex())) {
            blogViewModel!!.getBlogsForUserByCategory(category, page, AppConstants.BLOG_CAT_TYPE_TWO)
        } else {
            blogViewModel!!.getBlogsForUserByCategory(category, page, AppConstants.BLOG_CAT_TYPE_TWO)
        }
    }

    override fun onClickShop() {
        if (requireActivity() is WolooDashboard) {
            (requireActivity() as WolooDashboard).loadFragment(
                ShoppingFragment(),
                "ShoppingFragment"
            )
            (requireActivity() as WolooDashboard).changeIcon(
                (requireActivity() as WolooDashboard).nav_view!!.getMenu().findItem(
                    R.id.navigation_dash_home
                )
            )
        }
    }

    override fun onClickUserThumb() {
        if (requireActivity() is WolooDashboard) {
            (requireActivity() as WolooDashboard).loadFragment(
                MyAccountFragment.newInstance(false),
                MyAccountFragment.TAG
            )
            (requireActivity() as WolooDashboard).changeIcon(
                (requireActivity() as WolooDashboard).nav_view!!.menu.findItem(
                    R.id.navigation_location
                )
            )
        }
    }

    override fun onClickShopNow(cat_id: String?, cat_name: String?, cat_type: String?) {
        val myFragment = ProductListFragment()
        val b = Bundle()
        b.putString("catId", cat_id)
        b.putString("catName", cat_name)
        b.putString("cat_type", "category")
        myFragment.setArguments(b)
        val fragmentManager: FragmentManager =
            (context as WolooDashboard?)!!.supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frm_contant, myFragment, "")
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onClickShowNowProduct(prod_id: String?) {
        val mStringRequest: StringRequest
        mRequestQueue = Volley.newRequestQueue(context)
        mStringRequest = object : StringRequest(
            Method.POST,
            "$subcategoryUrl?product_id=$prod_id",
            Response.Listener<String?> { response ->
                try {
                    val obj = JSONArray(response)
                    val catObj = obj[0] as JSONObject
                    prodName = catObj["name"].toString()
                    prodPrice = catObj["price"].toString()
                    prodDesc = catObj["desc"].toString()
                    val myFragment = ProductDetailsFragment()
                    val b = Bundle()
                    b.putString("proId", prod_id)
                    b.putString("proName", prodName)
                    b.putString("proPrice", prodPrice)
                    b.putString("proDesc", prodDesc)
                    b.putString("from", "product_adapter")
                    myFragment.setArguments(b)
                    val fragmentManager: FragmentManager =
                        (context as WolooDashboard?)!!.supportFragmentManager
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.replace(R.id.frm_contant, myFragment, "")
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                } catch (e: Exception) {

                    // Toast.makeText(getActivity(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            },
            Response.ErrorListener {
                //  Toast.makeText(getActivity(),"Error"+error.getMessage(),Toast.LENGTH_LONG).show();
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                return HashMap()
            }
        }
        mRequestQueue!!.add(mStringRequest)
    }

    companion object {
        const val TAG = "TrendingBlogFragmentBlogs"
    }
}