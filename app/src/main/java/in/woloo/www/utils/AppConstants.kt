package `in`.woloo.www.utils

object AppConstants {
    const val PLATFORM_ANDROID: String = "android"
    const val APP_TYPE_MOBILE: String = "mobile"

    const val GET_REQUEST: Int = 0
    const val POST_REQUEST: Int = 1

    const val PUT_EXTRA_WEBVIEW_LOAD: String = "PUT_EXTRA_WEBVIEW_LOAD"
    const val PUT_EXTRA_WEBVIEW_TITLE: String = "PUT_EXTRA_WEBVIEW_TITLE"

    //**************************  **************************//
    const val PUT_LOCAL_BROADCAST: String = "IN_PUBLICAM_JETENGAGE_ICONOCLE_LOCAL_BROADCAST"

    const val API_SUCCESS: String = "success"

    const val DEFAULT_BASE_URL_FOR_IMAGES: String = "storage/app/public/"
    const val DEFAULT_STORE_IMAGE: String = "woloos/woloo_default.png"
    const val DEFAULT_STORE_IMAGE_LANDSCAPE: String = "woloos/woloo_default_landscape.png"
    const val DEFAULT_STORE_IMAGE_LANDSCAPE_NEW: String = "woloos/woloo_default_landscape_new.jpg"
    const val DEFAULT_BASE_URL_FOR_BLOG_IMAGE: String = "public/blog/"

    const val WOLOO_ID: String = "woloo_id"
    const val USER_PROFILE: String = "userProfile"
    const val TIME_OUT_EXCEPTION_TIME: Int = 30000

    const val PLAN_ID: String = "plan_Id"
    const val SUBSCRIPTION_ID: String = "subscription_id"
    const val FUTURE_SUBSCRIPTION: String = "future"
    const val ORDER_ID: String = "order_id"
    const val ORDER_AMOUNT: String = "amount"
    const val GIFT_CARD_VIEW_PRESENTER: String = "giftCardView"
    const val FEMALE: String = "Female"
    const val FROM_SUBSCRIPTION: String = "from_subscription"

    const val NAVIGATION_REWARDS: String = "Woloo Navigation Reward credits"
    const val REGISTRATION_POINTS: String = "Registration Point"
    const val GIFT_RECEIVED: String = "Gift Received"
    const val GIFT_SENT: String = "Gift Sent"
    const val GIFT_POINTS_DEDUCTED: String = "Gift points deducted"

    const val WAH_CERTIFICATE_POINT: String = "WAH Certificate Point"
    const val ADD_COINS: String = "Add Coins"
    const val USING_WOLOO_SERVICE_AT_PARTICULAR_HOST: String =
        "Using Woloo Service at a particular host"
    const val NO_WOLOO_FOUND_REWARD: String = "No woloo found reward"
    const val RECOMMEND_WOLOO_CREDITS: String = "Recommend woloo credits"
    const val APPROVED_RECOMMEND_WOLOO_CREDITS: String = "Approve Recommend woloo credits"
    const val WOLOO_NAVIGATION_REWARD_CREDITS: String = "Woloo Navigation Reward credits"
    const val ECOM_GIFT_DEBIT: String = "Ecom Gift Debit"
    const val ECOM_POINTS_DEBIT: String = "Ecom Points Debit"
    const val GIFT_SUB_SENT: String = "Gift Membership Sent"
    const val GIFT_SUB_RECEIVED: String = "Gift Membership Received"
    const val PURCHASE_MEMBER: String = "Purchase Of Membership"
    const val BLOG_READ_POINT: String = "Blog Read Point"

    const val ID: String = "id"
    const val FROM_SEARCH: String = "from_search"
    const val REFERAL_POINT: String = "Referral Point"
    const val MSG: String = "msg"
    const val isGiftSub: String = "isGiftSub"
    const val gift_numbers: String = "gift_numbers"
    const val IS_EMAIL: String = "isEmail"
    const val MOBILE: String = "mobile"
    const val REVIEW: String = "review"
    const val REFCODE: String = "refcode"

    const val DEFAULT_ZOOM: Int = 13
    const val VIEW_PROFILE_STRING: String = "view_profile_string"
    const val USER_IDENTITY: String = "user_identity"


    const val API_DEEP_LINK_DOMAIN_URI_PREFIX: String = "https://woloo.page.link"
    const val API_DEEP_LINK_SHORT_URL_API: String =
        "https://firebasedynamiclinks.googleapis.com/v1/shortLinks?key=AIzaSyDyJDAP9AhZDNDvFxB82N816xjWG9Lmji0"

    const val SHARE_CONTENT_URL_KEY: String = "?referralCode="


    const val WOLOO_CODE: String = "result"
    const val MEMBERSHIP_ID: String = "membership_id"
    const val HOST_CLICKED_ID: String = "host_click_id"
    const val HOST_CLICKED_LOCATION: String = "host_click_location"
    const val NO_LOCATION: String = "no_location"
    const val USER_ID: String = "user_id"
    const val DEVICE_PLATFORM: String = "platform"
    const val CURRENT_DATE: String = "date"
    const val SEARCH_KEYWORD: String = "keywords"
    const val LOCATION: String = "location"
    const val TRAVEL_MODE: String = "travel_mode"
    const val WOLOO_NAME: String = "woloo_id"
    const val WOLOOS: String = "woloos"
    const val POINTS_ID: String = "points_id"
    const val CURRENT_MEMBERSHIP_ID: String = "current_membership_id"
    const val SENT_GIFT_NUMBERS: String = "sent_gift_numbers"
    const val GIFT_CARD_AMOUNT: String = "amount"
    const val IS_THIRST_REMINDER: String = "is_thirst_reminder"
    const val THIRST_REMINDER_HOURS: String = "thirst_reminder_hours"
    const val PERIOD_DATE: String = "period_date"
    const val CYCLE_LENGTH: String = "cycle_length"
    const val PERIOD_LENGTH: String = "period_length"
    const val LUTEAL_LENGTH: String = "luteal_length"

    //EVENTS NAMES
    const val WOLOO_MARKER_CLICK: String = "woloo_marker_click"
    const val SEARCH_WOLOO_EVENT: String = "search_woloo_click"
    const val DIRECTION_WOLOO_EVENT: String = "direction_woloo_click"
    const val START_WOLOO_EVENT: String = "start_woloo_click"
    const val SHARE_WOLOO_EVENT: String = "share_woloo_click"
    const val LIKE_WOLOO_EVENT: String = "like_woloo_click"
    const val QR_SCAN_EVENT: String = "scan_qr_click"

    const val APP_OPEN_AGAIN: String = "app_open"
    const val MOBILE_OTP: String = "mobile_insert"
    const val MEMBERSHIP_UPGRADE: String = "membership_upgrade"
    const val SHOP_CLICK: String = "shop_click"
    const val DASH_HOME_CLICK: String = "dashboard_home_click"
    const val INVITE_CLICK: String = "invite_click"
    const val MY_ACCOUNT_CLICK: String = "my_account_click"
    const val HOST_NEAR_ME: String = "host_near_you_click"
    const val WOLOO_DETAIL_CLICK: String = "woloo_detail_click"
    const val TRAVEL_MODE_CLICK: String = "travel_mode_click"
    const val SHARE_CLICK: String = "share_click"
    const val INVITE_CONTACT_CLICK: String = "invite_contact_click"
    const val POINT_DETAILS_CLICK: String = "point_detail_click"
    const val UPGRADE_CLICK: String = "upgrade_click"
    const val UPGRADE_PACKAGE_CLICK: String = "upgrade_package_click"
    const val MY_HISTORY_CLICK: String = "my_history_click"
    const val WOLOO_GIFT_CARD_CLICK: String = "woloo_gift_card_click"
    const val GIFT_AMOUNT_SELECTED: String = "gift_amount_selected"
    const val BECOME_HOST_CLICK: String = "become_host_click"
    const val YES_INTERESTED_CLICK: String = "yes_interested_click"
    const val REFER_HOST_CLICK: String = "refer_host_click"
    const val ABOUT_CLICK: String = "about_click"
    const val TERMS_CLICK: String = "terms_click"
    const val CONTACT_US_CLICK: String = "contact_us_click"
    const val DISCONTINUE_CLICK: String = "discontinue_click"
    const val GIFT_SUBSCRIPTION_COMPLETED: String = "gift_subscription_completed"
    const val LOGOUT_CLICK: String = "logout_click"

    //This string is Added by Aarati @woloo on 17 Jul 2024
    const val DELETE_CLICK: String = "delete_click"
    const val DESTIONATION_REACHED: String = "destination_reached"

    const val SEARCHED_WOLOO_CLICK: String = "woloo_clicked_from_searched_woloo"
    const val NO_LOCATION_FOUND: String = "no_location_found"
    const val THIRST_REMINDER_CLICK: String = "thirst_reminder_click"
    const val PERIOD_TRACKER_UPDATE_CLICK: String = "period_tracker_update_click"
    const val PURCHASE_BY_APPLE: String = "apple"
    const val PURCHASE_BY_APPLE_MSG: String =
        "Dear User, You have bought the subscription from IOS so you need to cancel it from the IOS phone, kindly connect with Woloo Support team at support@woloo.in in case of any query."

    // Notification click_action
    const val PERIOD_TRACKER_NOTIFICATION: String = "in.woloo.www.PERIOD_TRACKER_NOTIFICATION"
    const val BLOG_CONTENT_NOTIFICATION: String = "in.woloo.www.BLOG_CONTENT_NOTIFICATION"
    const val NEW_WOLOO_NOTIFICATION: String = "in.woloo.www.NEW_WOLOO_NOTIFICATION"
    const val NEARBY_WOLOO_OFFERS: String = "in.woloo.www.NEARBY_WOLOO_OFFERS"
    const val PRODUCT_OFFER_NOTIFICATION: String = "in.woloo.www.PRODUCT_OFFERS_NOTIFICATION"
    const val THIRST_REMINDER_NOTIFICATION: String = "in.woloo.www.THIRST_REMINDER_NOTIFICATION"

    const val SHOW_OFFER_CART: String = "SHOW_OFFER_CART"
    const val PERIOD_TRACKER: String = "PERIOD_TRACKER"
    const val FREE_TRAIL: String = "Free Trial"

    const val FREE_TRAIL_NORMAL_DAYS: String = "7"

    const val FREE_TRAIL_VTION_DAYS: String = "365"

    const val CHANGED_BLOGS_IMAGE_URL: String =
        "https://woloo-stagging.s3.ap-south-1.amazonaws.com/"

    const val CALL_MOBILE: String = "tel:+91"

    const val MOBILENUMBER: String = "2249741750"

    const val HOSPITAL: String = "hospital"

    const val POLICESTATION: String = "police"

    const val FIRESTATION: String = "fire_station"


    const val LATITUDE: String = "latitude"

    const val LONGITUDE: String = "longitude"

    const val NAVIGATION_COMPLETED: String = "navigation_completed"

    const val BLOG_CAT_TYPE_ONE: String = "shop"

    const val BLOG_CAT_TYPE_TWO: String = "all"

    const val BLOG_CAT_TYPE_THREE: String = "period"
}


