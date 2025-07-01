package in.woloo.www.networksUtils;

import com.google.gson.internal.bind.JsonTreeReader;

public interface APIConstants {

    String SEND_OTP_API = "api/v1/sendOTP"; // not being used as new api implemented
    String LOGIN_API = "api/v1/login"; // not being used as new api implemented
    String UPDATE_DEVICE_TOKEN ="api/v1/updateDeviceToken"; // not being used

    String NEAR_BY_STORE="api/v1/nearbyWoloo"; // not being used as new api implemented
    String VIEW_PROFILE = "api/v1/viewProfile"; // not being used as new api implemented
    String VOUCHER_CODE = "api/v1/voucherSubscription"; // not being used as new api implemented
    String USER_PROFILE_MERGED = "api/v1/userProfile"; // not being used as new api implemented
    String SEARCH_WOLOO_API = "api/v1/searchWolooPaged"; // not being used
    String EDIT_PROFILE = "api/v1/editProfile"; // Implementing Node API
    String CANCEL_SUBSCRIPTION = "api/v1/cancelSubscription"; // not being used


    String WOLOO_GIFT = "api/v1/woloo_gift"; // not being used
    String REQUEST_POINTS = "api/v1/addCoins"; // not being used as new api implemented
    String USER_COINS = "api/v1/user_coins";  // not being used as new api implemented
    String COINHISTORY = "api/v1/coinHistory"; //implmented in node
    String SUBSCRIPTION_LIST_API = "api/v1/subscription_list"; // not being used
    String MY_SUBSCRIPTION_LIST_API = "api/v1/mySubscription"; // not being used as new api implemented
    String PURCHASE_SUBSCRIPTION = "api/v1/purchaseSubscription"; // not being used
    String SCAN_WOLOO = "api/v1/scanWoloo"; //implmented in node
    String WOLOO_REQUEST = "api/v1/woloo_request"; // not being used
    String USER_OFFER_LIST = "api/v1/userOfferList"; // not being used

    String INVITE = "api/v1/invite"; //implemented node
    String TRANSACTION_LIST = "api/v1/transaction_list";
    String SOS_LIST = "api/v1/sosList"; // NOT BEING USED
    String SOS_CREATE = "api/v1/sosCreate"; // NOT BEING USED

    String SOS_DETAIL = "api/v1/sosDetail"; // NOT BEING USED
    String SOS_EDIT = "api/v1/sosEdit"; // NOT BEING USED
    String SOS_DELETE = "api/v1/sosDelete"; // NOT BEING USED
    String FILE_UPLOAD = "api/v1/fileUpload"; // not being used
    String USER_SUBSCRIPTION = "api/v1/user_subscription"; // not being used

    String GET_REVIEW_OPTIONS = "api/v1/getReviewOptions";  // implemented node
    String ADD_REVIEW = "api/v1/sendReview"; // not being used
    String SUBMIT_REVIEW = "api/v1/submitReview"; // implemented node

    String ADD_WOLOO_HOST = "api/v1/addWoloo"; // Implementing Node API WIP
    String GET_REVIEW_LIST = "api/v1/getReviewList"; // implemented node

    String WOLOOLIKE = "api/v1/wolooLike"; //implemented node
    String WOLOOUNLIKE = "api/v1/wolooUnlike"; //implemeneted node
    String WOLOO_LIKE_STATUS = "api/v1/wolooLikeStatus"; //implemented node
    String WOLOO_REDEEM_OFFER = "api/v1/redeemOffer";

    String GET_PLAN = "api/v1/getPlan"; // node api
    String INIT_SUBSCRIPTION = "api/v1/initSubscription"; //not being used
    String INIT_SUBSCRIPTION_ORDER = "api/v1/initSubscriptionByOrder"; // node api
    String SUBMIT_SUBCRIPTION = "api/v1/submitSubscriptionPurchase"; //node api
    String AUTH_CONFIG = "api/v1/appConfigGet"; // node api

    String NAVIGATION_REWARD = "api/v1/wolooNavigationReward"; // not being used - node api
    String REWARD_HISTORY = "api/v1/wolooRewardHistory"; // not being used - node api
    String MY_OFFERS = "api/v1/myOffers"; //  not being used - node api
    String SUBSCRIPTION_STATUS = "api/v1/userSubscriptionStatus"; // not being used
    String PENDING_REVIEW_STATUS = "api/v1/getPendingReviewStatus"; // 1 implementation left in splash

    String REFERRED_WOLOO_LIST = "api/v1/userRecommendWoloo"; //not being used - node api
    String REFER_WOLOO = "api/v1/recommendWoloo"; //not being used - node api
    String  WAH_CERTIFICATE = "api/v1/wahcertificate"; //not being used - node api
    String  GET_GIFT_PLANS = "api/v1/getGiftPlan";  // not being used in ios
    String  SEND_GIFT_SUBSCRIPTION = "api/v1/sendGiftSubscription"; // not being used in ios
    String GEO_CODE_LOCATION_API = "api/v1/reverseGeocoding"; // not being used - node api

    String USER_GIFT_CARD_DETAILS = "/api/v1/ecomCoinTotal"; // not being used - node api
    String USER_GIFT_CARD_UPDATE = "api/v1/ecomCoinUpdate";// not being used - node api
    String USER_GIFT_CARD_FAIL = "api/v1/ecomTransactionFail";// not being used - node api

    String PERIOD_TRACKER = "api/v1/periodtracker"; // not being used node api

    String GET_USER_PERIOD_TRACKER = "api/v1/viewperiodtracker"; // not being used node api

    String NEAR_BY_WOLOO_AND_OFFER_COUNT = "api/v1/nearByWolooAndOfferCount"; // not being used node api
    String BLOG_CATEGORIES = "api/v1/getCategories"; // Implementing Node API
    String BLOGS = "api/v1/getBlogsForUserByCategory";// not being used - node api
    String SAVE_USER_CATEGORIES = "api/v1/saveUserCategory"; // Implementing Node API
    String FAVOURITE_A_BLOG = "api/v1/ctaFavourite";  // Implementing Node API WIP
    String LIKE_A_BLOG = "api/v1/ctaLikes";  // Implementing Node API WIP
    String READ_A_BLOG = "api/v1/ctaBlogRead"; // Implementing Node API WIP
    String BLOG_READ_POINT = "api/v1/blogReadPoint"; // Implementing Node API WIP
    String USER_JOURNEY = "api/v1/userLog";

    public static final String API_DEEP_LINK_SHORT_URL_API = "https://firebasedynamiclinks.googleapis.com/v1/shortLinks?key=AIzaSyDyJDAP9AhZDNDvFxB82N816xjWG9Lmji0";



}
