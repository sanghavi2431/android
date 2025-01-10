package `in`.woloo.www.more.dailylogscreen.models

import java.io.Serializable

class DailyLogSubTitle : Serializable {
    constructor()

    constructor(subTitleName: String?, imageUrl: Int, isChecked: Boolean) {
        this.subTitleName = subTitleName
        this.imageUrl = imageUrl
        this.isChecked = isChecked
    }

    var id: Int = 0
    @JvmField
    var subTitleName: String? = null
    var imageUrl: Int = 0
    @JvmField
    var isChecked: Boolean = false
}
