package `in`.woloo.www.my_history.model

import `in`.woloo.www.more.dailylogscreen.models.DailyLogSymptoms


data class MoodsModel(
    var bleeding: List<DailyLogSymptoms>? = null,
    var mood: List<DailyLogSymptoms>? = null,
    var symptoms: List<DailyLogSymptoms>? = null,
    var sexAndSexDrive: List<DailyLogSymptoms>? = null
)
