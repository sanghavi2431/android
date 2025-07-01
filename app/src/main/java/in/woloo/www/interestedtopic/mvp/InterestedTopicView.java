package in.woloo.www.interestedtopic.mvp;

import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.search.SearchWolooResponse;
import in.woloo.www.trendingblog.model.CategoriesResponse;

public interface InterestedTopicView {
    void getCategories(CategoriesResponse categoriesResponse);
    void onSaveUserCategories();
}
