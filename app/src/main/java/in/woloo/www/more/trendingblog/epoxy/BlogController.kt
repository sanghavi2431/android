package `in`.woloo.www.more.trendingblog.epoxy

import android.content.Context
import com.airbnb.epoxy.AutoModel
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.EpoxyController
import `in`.woloo.www.R
import `in`.woloo.www.more.editprofile.profile.model.UserProfile
import `in`.woloo.www.more.trendingblog.model.NearByWolooAndOfferCountResponse
import `in`.woloo.www.more.trendingblog.model.blog.Blog
import `in`.woloo.www.more.trendingblog.model.blog.Category
import `in`.woloo.www.utils.Logger

class BlogController(
    private val context: Context,
    private val onClickBlogViewItems: OnClickBlogViewItems
) : EpoxyController() {
    private var blogs: List<Blog>? = null
    private var nearbyWolooAndOffer: NearByWolooAndOfferCountResponse.Data? = null
    private var categories: List<Category>? = null
    private var selectedCategoryPosition = 0 //

    @JvmField
    var noBlogsModel: NoBlogsModel_? = null

    @JvmField
    var blogTextModel: TrendingBlogTextModel_? = null

    @JvmField
    var categoryHeaderModel: CategoryHeaderModel_? = null
    private var userProfile: UserProfile? = null
    private val itemPosition = -1
    fun setNearbyWolooAndOffer(nearbyWolooAndOffer: NearByWolooAndOfferCountResponse.Data?) {
        this.nearbyWolooAndOffer = nearbyWolooAndOffer
        requestModelBuild()
    }

    fun setCategories(categories: List<Category>?, selectedCategoryPosition: Int) {
        this.categories = categories
        this.selectedCategoryPosition = selectedCategoryPosition
        requestModelBuild()
    }

    fun setBlogItems(blogs: List<Blog>?) {
        this.blogs = blogs
        requestModelBuild()
    }

    fun setUserProfileDetails(userProfileMergedResponse: UserProfile?) {
        userProfile = userProfileMergedResponse
        requestModelBuild()
    }

    override fun buildModels() {
        //if (userProfile != null) {
        /* new UserPointsModel_().id("user_points")
                    .onClickBlogViewItems(onClickBlogViewItems)
                    .userProfile(userProfile)
                    .addTo(this);*/

        /* locateLooModel.onClickBlogViewItems(onClickBlogViewItems)
                    .addTo(this);*/
        //}
        if (nearbyWolooAndOffer != null) {
            /*  List<ViewPagerModel_> pagerModels = new ArrayList<>();
            if (nearbyWolooAndOffer.getWolooCount() > 0) {
                itemPosition++;
                ViewPagerModel_ model = new ViewPagerModel_();
                model.id("pager_id" + itemPosition)
                        .pageType("LOCATION")
                        .itemPosition(itemPosition)
                        .onClickBlogViewItems(onClickBlogViewItems)
                        .title(context.getString(R.string.banner_woloo_host_location_count, nearbyWolooAndOffer.getWolooCount()))
                        .subTitle(context.getString(R.string.banner_woloo_host_subtitle)).icon(R.drawable.ic_new_loo_location);
                pagerModels.add(model);
            }
            if (nearbyWolooAndOffer.getOfferCount() > 0) {
                itemPosition++;
                ViewPagerModel_ model = new ViewPagerModel_();
                model.id("pager_id" + itemPosition)
                        .pageType("OFFER")
                        .itemPosition(itemPosition)
                        .onClickBlogViewItems(onClickBlogViewItems)
                        .title(context.getString(R.string.banner_woloo_host_offers_count, nearbyWolooAndOffer.getOfferCount()))
                        .subTitle(context.getString(R.string.banner_woloo_host_subtitle))
                        .icon(R.drawable.ic_discount);
                pagerModels.add(model);
            }
            for (int i = 0; i < nearbyWolooAndOffer.getShopOffer().size(); i++) {
                itemPosition++;
                ViewPagerModel_ model = new ViewPagerModel_();
                model.id("pager_id" + itemPosition)
                        .pageType("SHOP")
                        .itemPosition(itemPosition)
                        .onClickBlogViewItems(onClickBlogViewItems)
                        .title(nearbyWolooAndOffer.getShopOffer().get(i).getDescription())
                        .subTitle(nearbyWolooAndOffer.getShopOffer().get(i).getCouponCode())
                        .icon(R.drawable.ic_shopping_cart);
                pagerModels.add(model);
            }
            new CarouselIndicatorModel().id("view_pagers")
                    .models(pagerModels)
                    .padding(Carousel.Padding.dp(0, 2))
                    .addTo(this);*/
        }
        if (categories != null && categories!!.size > 0) {
            val categoryModels: MutableList<CategoryModel_> = ArrayList()
            /*CategoryModel_ checkAllModel = new CategoryModel_();
            checkAllModel.id(1000)
                    .onClickBlogViewItems(onClickBlogViewItems)
                    .category(categories.get())
                    .itemPosition(0).categoryName("All");
            categoryModels.add(checkAllModel);*/
            var i = 0
            val categoriesSize = categories!!.size
            while (i < categoriesSize) {
                val category = categories!![i]
                val categoryModel_ = CategoryModel_()
                categoryModel_.id(category.id!!.toLong())
                    .onClickBlogViewItems(onClickBlogViewItems)
                    .category(category)
                    .itemPosition(i)
                    .iconUrl(category.categoryIconUrl)
                    .selectedItemPosition(selectedCategoryPosition)
                    .categoryName(category.categoryName)
                categoryModels.add(categoryModel_)
                Logger.d(
                    "mdfd new ",
                    categoryModel_.defaultLayout.toString() + " and " + categoryModel_.layout.toString()
                )
                i++
            }
            categoryHeaderModel!!.categoriesList(context.getString(R.string.trending_blog))
                .addTo(this)
            CarouselModel_().id("blog_categories") // .padding(Carousel.Padding.dp(16, 4, 16, 16, 8))
                .models(categoryModels)
                .addTo(this)
            blogTextModel!!.title(context.getString(R.string.trending_blog))
                .addTo(this)
        }
        if (blogs != null) {
            var i = 0
            val blogsSize = blogs!!.size
            while (i < blogsSize) {
                val blog = blogs!![i]
                BlogItemModel_()
                    .id(blog.id)
                    .onClickBlogViewItems(onClickBlogViewItems)
                    .isFavourite(blog.isFavourite!!)
                    .isLiked(blog.isLiked!!)
                    .isBlogRead(blog.isBlogRead!!)
                    .itemPosition(i)
                    .blog(blog)
                    .addTo(this)
                i++
            }
            noBlogsModel!!.addIf(blogs!!.size == 0, this)
        }
    }

    interface OnClickBlogViewItems {
        fun onClickLocateLoo()
        fun onClickBlogItem(blog: Blog?, position: Int)
        fun onClickBlogLike(blog: Blog?, position: Int)
        fun onClickBlogFavourite(blog: Blog?, position: Int)
        fun onClickBlogShare(blog: Blog?, position: Int)
        fun onClickPagerItemWolooLocation()
        fun onClickPagerItemWolooOffers()
        fun onClickPagerItemShopCoupon(code: String?)
        fun onSelectCategory(position: Int)
        fun onClickShop()
        fun onClickUserThumb()
        fun onClickShopNow(cat_id: String?, cat_name: String?, cat_type: String?)
        fun onClickShowNowProduct(prod_id: String?)
    }
}
