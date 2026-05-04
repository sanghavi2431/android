package in.woloo.www.application_kotlin.interfaces;

import in.woloo.www.application_kotlin.model.server_response.NearByStoreResponse;
import in.woloo.www.application_kotlin.api_classes.NetworkAPICallModel;
import in.woloo.www.application_kotlin.model.server_response.SearchWolooResponse;

public interface WolooSearchView {
    void searchWolooSuccess(SearchWolooResponse searchWolooResponse, String keywords);
    void onGetNearByStore(NearByStoreResponse data, NetworkAPICallModel networkAPICallModel, String keywords);
}
