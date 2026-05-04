package `in`.woloo.www.more.trendingblog.mvp

import `in`.woloo.www.more.models.UserProfileMergedResponse
import `in`.woloo.www.more.trendingblog.model.CategoriesResponse
import `in`.woloo.www.more.trendingblog.model.NearByWolooAndOfferCountResponse
import `in`.woloo.www.more.trendingblog.model.blog.BlogsResponse

interface NearByWolooAndOfferCountView {
    fun nearByWolooAndOfferCountResponse(nearByWolooAndOfferCountResponse: NearByWolooAndOfferCountResponse?)
    fun getCategories(categoriesResponse: CategoriesResponse?)
    fun getBlogs(blogsResponse: BlogsResponse?)
    fun onFavouriteABlog()
    fun onLikeABlog()
    fun onReadABlog()
    fun onBlogReadPointsAdded()
    fun setUserProfileMergedResponse(userProfileMergedResponse: UserProfileMergedResponse?)
}
