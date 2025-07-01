package in.woloo.www.refer_woloo_host.mvp;

import in.woloo.www.more.models.ViewProfileResponse;
import in.woloo.www.refer_woloo_host.model.ReferredWolooListResponse;
import in.woloo.www.woloo_host.model.GeoCodeResponse;

public interface ReferredWolooView {
    void referredWolooListResponse(ReferredWolooListResponse referredWolooListResponse);
    void setProfileResponse(ViewProfileResponse viewProfileResponse);
    void referWolooHostSuccess(String message);
    void geoCodeResponseSuccess(GeoCodeResponse geoCodeResponse);

}
