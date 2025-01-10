package `in`.woloo.www.more.dailylogscreen.models

import java.util.Objects

class DailyLogSymptoms {
    var subTitleName: String? = null

    var imageURL: Int = 0

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as DailyLogSymptoms
        return imageURL == that.imageURL && subTitleName == that.subTitleName
    }

    override fun hashCode(): Int {
        return Objects.hash(subTitleName, imageURL)
    }
}
