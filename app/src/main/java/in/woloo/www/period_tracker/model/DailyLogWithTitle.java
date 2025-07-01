package in.woloo.www.period_tracker.model;

import androidx.annotation.StringRes;

import java.util.List;

import in.woloo.www.dailylogscreen.models.DailyLogSymptoms;

public class DailyLogWithTitle {
    private List<DailyLogSymptoms> dailyLogSymptoms;
    @StringRes
    private int titleName;

    public DailyLogWithTitle(@StringRes int titleName, List<DailyLogSymptoms> dailyLogSymptoms) {
        this.dailyLogSymptoms = dailyLogSymptoms;
        this.titleName = titleName;
    }

    public List<DailyLogSymptoms> getDailyLogSymptoms() {
        return dailyLogSymptoms;
    }

    public void setDailyLogSymptoms(List<DailyLogSymptoms> dailyLogSymptoms) {
        this.dailyLogSymptoms = dailyLogSymptoms;
    }

    public int getTitleName() {
        return titleName;
    }

    public void setTitleName(int titleName) {
        this.titleName = titleName;
    }
}
