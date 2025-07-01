
package in.woloo.www.trendingblog.model.blog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Blog {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("author_id")
    @Expose
    private Integer authorId;
    @SerializedName("main_image")
    @Expose
    private String mainImage;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("detailed_blog_link")
    @Expose
    private String detailedBlogLink;
    @SerializedName("short_link")
    @Expose
    private String shortLink;
    @SerializedName("categories")
    @Expose
    private String categories;
    @SerializedName("sub_categories")
    @Expose
    private String subCategories;
    @SerializedName("like_counts")
    @Expose
    private Integer likeCounts;
    @SerializedName("favourite_counts")
    @Expose
    private Integer favouriteCounts;
    @SerializedName("is_liked")
    @Expose
    private Integer isLiked;
    @SerializedName("is_favourite")
    @Expose
    private Integer isFavourite;
    @SerializedName("is_blog_read")
    @Expose
    private Integer isBlogRead;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailedBlogLink() {
        return detailedBlogLink;
    }

    public void setDetailedBlogLink(String detailedBlogLink) {
        this.detailedBlogLink = detailedBlogLink;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(String subCategories) {
        this.subCategories = subCategories;
    }

    public Integer getLikeCounts() {
        return likeCounts;
    }

    public void setLikeCounts(Integer likeCounts) {
        this.likeCounts = likeCounts;
    }

    public Integer getFavouriteCounts() {
        return favouriteCounts;
    }

    public void setFavouriteCounts(Integer favouriteCounts) {
        this.favouriteCounts = favouriteCounts;
    }

    public Integer getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Integer isLiked) {
        this.isLiked = isLiked;
    }

    public Integer getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(Integer isFavourite) {
        this.isFavourite = isFavourite;
    }

    public Integer getIsBlogRead() {
        return isBlogRead;
    }

    public void setIsBlogRead(Integer isBlogRead) {
        this.isBlogRead = isBlogRead;
    }

    public String getShortLink() {
        return shortLink;
    }

    public void setShortLink(String shortLink) {
        this.shortLink = shortLink;
    }
}
