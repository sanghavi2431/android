package `in`.woloo.www.more.period_tracker.model

import androidx.annotation.StringRes
import `in`.woloo.www.more.dailylogscreen.models.DailyLogSymptoms

class DailyLogWithTitle(@StringRes titleName: Int, dailyLogSymptoms: List<DailyLogSymptoms?>) {
    private var dailyLogSymptoms: List<DailyLogSymptoms?>

    @StringRes
    var titleName: Int

    init {
        this.dailyLogSymptoms = dailyLogSymptoms
        this.titleName = titleName
    }

    fun getDailyLogSymptoms(): List<DailyLogSymptoms?> {
        return dailyLogSymptoms
    }

    fun setDailyLogSymptoms(dailyLogSymptoms: List<DailyLogSymptoms>) {
        this.dailyLogSymptoms = dailyLogSymptoms
    }
}
