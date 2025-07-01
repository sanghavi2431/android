package in.woloo.www.trendingblog.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BlogsResponse {
    private String status;
    private Data data;
    private String message;
    private int code;

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return this.data;
    }

    public void setData(Data data) {
        this.data = data;
    }

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
    }

    public class Category {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("category_icon_url")
        @Expose
        private String categoryIconUrl;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryIconUrl() {
            return categoryIconUrl;
        }

        public void setCategoryIconUrl(String categoryIconUrl) {
            this.categoryIconUrl = categoryIconUrl;
        }

    }

    public class SubCategory {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("sub_category")
        @Expose
        private String subCategory;
        @SerializedName("sub_category_icon_url")
        @Expose
        private String subCategoryIconUrl;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(String subCategory) {
            this.subCategory = subCategory;
        }

        public String getSubCategoryIconUrl() {
            return subCategoryIconUrl;
        }

        public void setSubCategoryIconUrl(String subCategoryIconUrl) {
            this.subCategoryIconUrl = subCategoryIconUrl;
        }

    }

    public class Data {
        @SerializedName("blogs")
        @Expose
        private List<Blog> blogs = null;
        @SerializedName("categories")
        @Expose
        private List<Category> categories = null;
        @SerializedName("sub_categories")
        @Expose
        private List<SubCategory> subCategories = null;

        public List<Blog> getBlogs() {
            return blogs;
        }

        public void setBlogs(List<Blog> blogs) {
            this.blogs = blogs;
        }

        public List<Category> getCategories() {
            return categories;
        }

        public void setCategories(List<Category> categories) {
            this.categories = categories;
        }

        public List<SubCategory> getSubCategories() {
            return subCategories;
        }

        public void setSubCategories(List<SubCategory> subCategories) {
            this.subCategories = subCategories;
        }
    }
}
