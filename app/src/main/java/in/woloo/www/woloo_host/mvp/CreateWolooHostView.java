package in.woloo.www.woloo_host.mvp;

import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.woloo_host.model.GeoCodeResponse;

public interface CreateWolooHostView {
    void setProfileResponse(ViewProfileResponse viewProfileResponse);
    void addWolooHostSuccess(String message);
    void geoCodeResponseSuccess(GeoCodeResponse geoCodeResponse);
}
