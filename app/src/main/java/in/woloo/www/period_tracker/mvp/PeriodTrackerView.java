package in.woloo.www.period_tracker.mvp;

import in.woloo.www.period_tracker.model.GetPeriodDataResponse;
import in.woloo.www.period_tracker.model.PeriodTrackerResponse;

public interface PeriodTrackerView {
    void setPeriodTrackerResponse(PeriodTrackerResponse periodTrackerResponse);
    void getPeriodTrackerDataResponse(GetPeriodDataResponse getPeriodDataResponse);
}
