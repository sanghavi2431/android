package in.woloo.www.dashboard.ui.home;

import in.woloo.www.dashboard.ui.home.model.NearByStoreResponse;
import in.woloo.www.networksUtils.NetworkAPICallModel;
import in.woloo.www.splash.AuthConfigResponse;

public interface HomeViewPresenterInterface {
  void onGetNearByStore(NearByStoreResponse nearByStoreResponse, NetworkAPICallModel networkAPICallModel);
  void authConfigSuccess(AuthConfigResponse authConfigResponse);
}
