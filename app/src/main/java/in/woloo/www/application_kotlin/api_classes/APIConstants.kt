package `in`.woloo.www.application_kotlin.api_classes

interface APIConstants {
    companion object {
        const val API_DEEP_LINK_SHORT_URL_API =
            "https://firebasedynamiclinks.googleapis.com/v1/shortLinks?key=AIzaSyDyJDAP9AhZDNDvFxB82N816xjWG9Lmji0"
        const val SEND_OTP_API = "api/v1/sendOTP" // not being used as new api implemented
        const val LOGIN_API = "api/v1/login" // not being used as new api implemented
        const val UPDATE_DEVICE_TOKEN = "api/v1/updateDeviceToken" // not being used
        const val NEAR_BY_STORE = "api/v1/nearbyWoloo" // not being used as new api implemented
        const val VIEW_PROFILE = "api/v1/viewProfile" // not being used as new api implemented
        const val VOUCHER_CODE =
            "api/v1/voucherSubscription" // not being used as new api implemented
        const val USER_PROFILE_MERGED =
            "api/v1/userProfile" // not being used as new api implemented
        const val SEARCH_WOLOO_API = "api/v1/searchWolooPaged" // not being used
        const val EDIT_PROFILE = "api/v1/editProfile" // Implementing Node API
        const val CANCEL_SUBSCRIPTION = "api/v1/cancelSubscription" // not being used
        const val WOLOO_GIFT = "api/v1/woloo_gift" // not being used
        const val REQUEST_POINTS = "api/v1/addCoins" // not being used as new api implemented
        const val USER_COINS = "api/v1/user_coins" // not being used as new api implemented
        const val COINHISTORY = "api/v1/coinHistory" //implmented in node
        const val SUBSCRIPTION_LIST_API = "api/v1/subscription_list" // not being used
        const val MY_SUBSCRIPTION_LIST_API =
            "api/v1/mySubscription" // not being used as new api implemented
        const val PURCHASE_SUBSCRIPTION = "api/v1/purchaseSubscription" // not being used
        const val SCAN_WOLOO = "api/v1/scanWoloo" //implmented in node
        const val WOLOO_REQUEST = "api/v1/woloo_request" // not being used
        const val USER_OFFER_LIST = "api/v1/userOfferList" // not being used
        const val INVITE = "api/v1/invite" //implemented node
        const val TRANSACTION_LIST = "api/v1/transaction_list"
        const val SOS_LIST = "api/v1/sosList" // NOT BEING USED
        const val SOS_CREATE = "api/v1/sosCreate" // NOT BEING USED
        const val SOS_DETAIL = "api/v1/sosDetail" // NOT BEING USED
        const val SOS_EDIT = "api/v1/sosEdit" // NOT BEING USED
        const val SOS_DELETE = "api/v1/sosDelete" // NOT BEING USED
        const val FILE_UPLOAD = "api/v1/fileUpload" // not being used
        const val USER_SUBSCRIPTION = "api/v1/user_subscription" // not being used
        const val GET_REVIEW_OPTIONS = "api/v1/getReviewOptions" // implemented node
        const val ADD_REVIEW = "api/v1/sendReview" // not being used
        const val SUBMIT_REVIEW = "api/v1/submitReview" // implemented node
        const val ADD_WOLOO_HOST = "api/v1/addWoloo" // Implementing Node API WIP
        const val GET_REVIEW_LIST = "api/v1/getReviewList" // implemented node
        const val WOLOOLIKE = "api/v1/wolooLike" //implemented node
        const val WOLOOUNLIKE = "api/v1/wolooUnlike" //implemeneted node
        const val WOLOO_LIKE_STATUS = "api/v1/wolooLikeStatus" //implemented node
        const val WOLOO_REDEEM_OFFER = "api/v1/redeemOffer"
        const val GET_PLAN = "api/v1/getPlan" // node api
        const val INIT_SUBSCRIPTION = "api/v1/initSubscription" //not being used
        const val INIT_SUBSCRIPTION_ORDER = "api/v1/initSubscriptionByOrder" // node api
        const val SUBMIT_SUBCRIPTION = "api/v1/submitSubscriptionPurchase" //node api
        const val AUTH_CONFIG = "api/v1/appConfigGet" // node api
        const val NAVIGATION_REWARD = "api/v1/wolooNavigationReward" // not being used - node api
        const val REWARD_HISTORY = "api/v1/wolooRewardHistory" // not being used - node api
        const val MY_OFFERS = "api/v1/myOffers" //  not being used - node api
        const val SUBSCRIPTION_STATUS = "api/v1/userSubscriptionStatus" // not being used
        const val PENDING_REVIEW_STATUS =
            "api/v1/getPendingReviewStatus" // 1 implementation left in splash
        const val REFERRED_WOLOO_LIST = "api/v1/userRecommendWoloo" //not being used - node api
        const val REFER_WOLOO = "api/v1/recommendWoloo" //not being used - node api
        const val WAH_CERTIFICATE = "api/v1/wahcertificate" //not being used - node api
        const val GET_GIFT_PLANS = "api/v1/getGiftPlan" // not being used in ios
        const val SEND_GIFT_SUBSCRIPTION = "api/v1/sendGiftSubscription" // not being used in ios
        const val GEO_CODE_LOCATION_API = "api/v1/reverseGeocoding" // not being used - node api
        const val USER_GIFT_CARD_DETAILS = "/api/v1/ecomCoinTotal" // not being used - node api
        const val USER_GIFT_CARD_UPDATE = "api/v1/ecomCoinUpdate" // not being used - node api
        const val USER_GIFT_CARD_FAIL = "api/v1/ecomTransactionFail" // not being used - node api
        const val PERIOD_TRACKER = "api/v1/periodtracker" // not being used node api
        const val GET_USER_PERIOD_TRACKER = "api/v1/viewperiodtracker" // not being used node api
        const val NEAR_BY_WOLOO_AND_OFFER_COUNT =
            "api/v1/nearByWolooAndOfferCount" // not being used node api
        const val BLOG_CATEGORIES = "api/v1/getCategories" // Implementing Node API
        const val BLOGS = "api/v1/getBlogsForUserByCategory" // not being used - node api
        const val SAVE_USER_CATEGORIES = "api/v1/saveUserCategory" // Implementing Node API
        const val FAVOURITE_A_BLOG = "api/v1/ctaFavourite" // Implementing Node API WIP
        const val LIKE_A_BLOG = "api/v1/ctaLikes" // Implementing Node API WIP
        const val READ_A_BLOG = "api/v1/ctaBlogRead" // Implementing Node API WIP
        const val BLOG_READ_POINT = "api/v1/blogReadPoint" // Implementing Node API WIP
        const val USER_JOURNEY = "api/v1/userLog"
    }
}
