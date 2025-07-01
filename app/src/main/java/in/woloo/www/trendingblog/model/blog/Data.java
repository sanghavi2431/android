
package in.woloo.www.trendingblog.model.blog;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
