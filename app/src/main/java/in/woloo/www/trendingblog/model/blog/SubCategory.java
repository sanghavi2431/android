
package in.woloo.www.trendingblog.model.blog;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
