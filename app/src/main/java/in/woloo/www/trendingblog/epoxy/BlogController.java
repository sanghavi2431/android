package in.woloo.www.trendingblog.epoxy;

import android.content.Context;

import com.airbnb.epoxy.AutoModel;
import com.airbnb.epoxy.Carousel;
import com.airbnb.epoxy.CarouselModel_;
import com.airbnb.epoxy.EpoxyController;

import java.util.ArrayList;
import java.util.List;

import in.woloo.www.R;
import in.woloo.www.trendingblog.model.NearByWolooAndOfferCountResponse;
import in.woloo.www.trendingblog.model.blog.Blog;
import in.woloo.www.trendingblog.model.blog.Category;
import in.woloo.www.v2.profile.model.UserProfile;

public class BlogController extends EpoxyController {
    private final Context context;
    private final OnClickBlogViewItems onClickBlogViewItems;
    private List<Blog> blogs = null;
    private NearByWolooAndOfferCountResponse.Data nearbyWolooAndOffer;
    private List<Category> categories;
    private int selectedCategoryPosition = 0;//
    @AutoModel
    LocateLooModel_ locateLooModel;
    @AutoModel
    NoBlogsModel_ noBlogsModel;

    @AutoModel
    TrendingBlogTextModel_ blogTextModel;
    private UserProfile userProfile;
    private int itemPosition = -1;

    public BlogController(Context context, OnClickBlogViewItems onClickBlogViewItems) {
        this.context = context;
        this.onClickBlogViewItems = onClickBlogViewItems;
    }

    public void setNearbyWolooAndOffer(NearByWolooAndOfferCountResponse.Data nearbyWolooAndOffer) {
        this.nearbyWolooAndOffer = nearbyWolooAndOffer;
        requestModelBuild();
    }

    public void setCategories(List<Category> categories, int selectedCategoryPosition) {
        this.categories = categories;
        this.selectedCategoryPosition = selectedCategoryPosition;
        requestModelBuild();
    }

    public void setBlogItems(List<Blog> blogs) {
        this.blogs = blogs;
        requestModelBuild();
    }

    public void setUserProfileDetails(UserProfile userProfileMergedResponse) {
        this.userProfile = userProfileMergedResponse;
        requestModelBuild();
    }

    @Override
    protected void buildModels() {
        //if (userProfile != null) {
            new UserPointsModel_().id("user_points")
                    .onClickBlogViewItems(onClickBlogViewItems)
                    .userProfile(userProfile)
                    .addTo(this);

            locateLooModel.onClickBlogViewItems(onClickBlogViewItems)
                    .addTo(this);
        //}

        if (nearbyWolooAndOffer != null) {
            List<ViewPagerModel_> pagerModels = new ArrayList<>();
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
                    .addTo(this);
        }


        if (categories != null && categories.size() > 0) {
            List<CategoryModel_> categoryModels = new ArrayList<>();
            /*CategoryModel_ checkAllModel = new CategoryModel_();
            checkAllModel.id(1000)
                    .onClickBlogViewItems(onClickBlogViewItems)
                    .category(categories.get())
                    .itemPosition(0).categoryName("All");
            categoryModels.add(checkAllModel);*/
            for (int i = 0, categoriesSize = categories.size(); i < categoriesSize; i++) {
                Category category = categories.get(i);
                CategoryModel_ categoryModel_ = new CategoryModel_();
                categoryModel_.id(category.getId())
                        .onClickBlogViewItems(onClickBlogViewItems)
                        .category(category)
                        .itemPosition(i)
                        .iconUrl(category.getCategoryIconUrl())
                        .selectedItemPosition(selectedCategoryPosition)
                        .categoryName(category.getCategoryName());
                categoryModels.add(categoryModel_);
            }
            blogTextModel.title(context.getString(R.string.trending_blog))
                    .addTo(this);

            new CarouselModel_().id("blog_categories")
                    .padding(Carousel.Padding.dp(16, 4, 16, 16, 8))
                    .models(categoryModels)
                    .addTo(this);
        }

        if (blogs != null) {
            for (int i = 0, blogsSize = blogs.size(); i < blogsSize; i++) {
                Blog blog = blogs.get(i);
                new BlogItemModel_()
                        .id(blog.getId())
                        .onClickBlogViewItems(onClickBlogViewItems)
                        .isFavourite(blog.getIsFavourite())
                        .isLiked(blog.getIsLiked())
                        .isBlogRead(blog.getIsBlogRead())
                        .itemPosition(i)
                        .blog(blog)
                        .addTo(this);
            }

            noBlogsModel.addIf(blogs.size() == 0, this);
        }
    }

    public interface OnClickBlogViewItems {
        void onClickLocateLoo();

        void onClickBlogItem(Blog blog, int position);

        void onClickBlogLike(Blog blog, int position);

        void onClickBlogFavourite(Blog blog, int position);

        void onClickBlogShare(Blog blog, int position);

        void onClickPagerItemWolooLocation();

        void onClickPagerItemWolooOffers();

        void onClickPagerItemShopCoupon(String code);

        void onSelectCategory(int position);

        void onClickShop();

        void onClickUserThumb();
    }
}
