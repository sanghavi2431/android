package in.woloo.www.trendingblog.mvp;

import in.woloo.www.more.models.UserProfileMergedResponse;
import in.woloo.www.trendingblog.model.CategoriesResponse;
import in.woloo.www.trendingblog.model.NearByWolooAndOfferCountResponse;
import in.woloo.www.trendingblog.model.blog.BlogsResponse;

public interface NearByWolooAndOfferCountView {
    void nearByWolooAndOfferCountResponse(NearByWolooAndOfferCountResponse nearByWolooAndOfferCountResponse);

    void getCategories(CategoriesResponse categoriesResponse);

    void getBlogs(BlogsResponse blogsResponse);

    void onFavouriteABlog();

    void onLikeABlog();

    void onReadABlog();

    void onBlogReadPointsAdded();

    void setUserProfileMergedResponse(UserProfileMergedResponse userProfileMergedResponse);
}
