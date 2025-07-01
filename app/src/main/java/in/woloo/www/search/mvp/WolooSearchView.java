package in.woloo.www.search.mvp;

import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.search.SearchWolooResponse;

public interface WolooSearchView {
    void searchWolooSuccess(SearchWolooResponse searchWolooResponse, String keywords);
    void onGetNearByStore(NearByStoreResponse data, NetworkAPICallModel networkAPICallModel, String keywords);
}
