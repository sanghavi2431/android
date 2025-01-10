package `in`.woloo.www.more.dailylogscreen.models

import `in`.woloo.www.database.preference.SharedPreferencesEnum

class DailyLogGroupTitle {
    @JvmField
    var id: SharedPreferencesEnum? = null
    @JvmField
    var groupName: String? = null
    @JvmField
    var subTitle: List<DailyLogSubTitle>? = null
}
