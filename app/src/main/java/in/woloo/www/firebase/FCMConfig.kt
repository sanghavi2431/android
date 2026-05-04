package `in`.woloo.www.firebase


object FCMConfig {
    // global topic to receive app wide push notifications
    const val TOPIC_GLOBAL: String = "global"

    // broadcast receiver intent filters
    const val REGISTRATION_COMPLETE: String = "registrationComplete"
    const val PUSH_NOTIFICATION: String = "pushNotification"

    // id to handle the notification in the notification tray
    const val NOTIFICATION_ID: Int = 100
    const val NOTIFICATION_ID_BIG_IMAGE: Int = 101

    const val SHARED_PREF: String = "ah_firebase"

    const val contentText: String = "contentText"
    const val imageUrl: String = ""
    const val contentTitle: String = ""
    const val setTickerText: String = ""
    const val tabId: String = ""
    const val contentIdNew: String = ""
    const val clickUrl: String = ""
    const val toreId: String = ""
    const val superStoreId: String = ""
    const val summaryText: String = ""
    const val parentTab: String = ""
    const val contentId: String = ""
    const val pageLevel: String = ""
    const val pageLayout: String = ""
}
