package in.woloo.www.trendingblog.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class CategoriesResponse {
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

    public class Category {
        @SerializedName("blog_count")
        private int blogCount;
        private int id;
        @SerializedName("category_icon_url")
        private String categoryIconUrl;
        @SerializedName("category_name")
        private String categoryName;
        private transient boolean isSelected = false;

        public int getId() {
            return this.id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCategoryName() {
            return this.categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getCategoryIconUrl() {
            return this.categoryIconUrl;
        }

        public void setCategoryIconUrl(String categoryIconUrl) {
            this.categoryIconUrl = categoryIconUrl;
        }

        public int getBlogCount() {
            return this.blogCount;
        }

        public void setBlogCount(int blogCount) {
            this.blogCount = blogCount;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }

    public class Data {
        private List<Category> categories;

        public List<Category> getCategories() {
            return this.categories;
        }

        public void setCategories(List<Category> categories) {
            this.categories = categories;
        }
    }
}
