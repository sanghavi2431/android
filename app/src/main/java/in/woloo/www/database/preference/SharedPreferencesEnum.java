package in.woloo.www.database.preference;

public enum SharedPreferencesEnum {


    IS_LOGGED_IN {
        @Override
        public String getPreferenceKey() {
            return "IS_LOGGED_IN";
        }
    },
    USER_INFO {
        @Override
        public String getPreferenceKey() {
            return "USER_INFO";
        }
    },
    APP_CONFIG {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_APP_CONFIG";
        }
    },
    USER_CODE {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_USER_CODE";
        }
    },
    SUPERSTORE_ID {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_SUPERSTORE_ID";
        }
    }, IP_TO_LOCALE {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_IP_TO_LOCALE";
        }
    }, IS_QUIZ_FIRST_INFLUNCER {
        @Override
        public String getPreferenceKey() {
            return "IS_QUIZ_FIRST_INFLUNCER";
        }
    }, IS_DISCUSSION_FIRST_INFLUNCER {
        @Override
        public String getPreferenceKey() {
            return "IS_DISCUSSION_FIRST_INFLUNCER";
        }
    }, IS_DISCUSSION_FIRST_USER {
        @Override
        public String getPreferenceKey() {
            return "IS_DISCUSSION_FIRST_USER";
        }
    }, IS_LOGGED_IN_INFLUENCER {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_IS_LOGGED_IN_INFLUENCER";
        }
    }, IS_LOGGED_IN_WITH_SESSION {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_IS_LOGGED_IN_WITH_SESSION";
        }
    }, IS_LOGGED_IN_INFLUENCER_FIRST {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_IS_LOGGED_IN_INFLUENCER_FIRST";
        }
    }, IS_LOGGED_IN_USER_FIRST {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_IS_LOGGED_IN_USER_FIRST";
        }
    }, IS_INFLUENCER_SELECTED {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_IS_INFLUENCER_SELECTED";
        }
    }, DEVICE_VERIFIED {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_DEVICE_VERIFIED";
        }
    }, PRIVACY_URL {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_PRIVACY_URL";
        }
    }, TERMS_URL {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_TERMS_URL";
        }
    }, ABOUT_URL {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_ABOUT_URL";
        }
    }, GLOBAL_CAPTION {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_GLOBAL_CAPTION";
        }
    }, VIDEO_SEEK {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_VIDEO_SEEK";
        }
    }, CONTENT_RESPONSE {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_CONTENT_RESPONSE";
        }
    }, HASH_LA_URL {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_HASH_LA_URL";
        }
    }, DOWNLOADED_CONTENT {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_DOWNLOADED_CONTENT";
        }
    }, DOWNLOADED_LIST {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_DOWNLOADED_LIST";
        }
    }, CONSENT_VISIBILITY {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_CONSENT_VISIBILITY";
        }
    }, PUBLIC_KEY {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_PUBLIC_KEY";
        }
    }, PARENT_PACKAGE_ID {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_PARENT_PACKAGE_ID";
        }
    }, IS_ACTIVE_SUBSCRIBED {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_IS_ACTIVE_SUBSCRIBED";
        }
    }, EMAIL_ID {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_EMAIL_ID";
        }
    }, FAQs {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_APP_FAQs";
        }
    }, LANGUAGE_CODE {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_LANGUAGE_CODE";
        }
    }, SHARE_TEXT {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_SHARE_TEXT";
        }
    }, IMAGE_VERTICAL {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_IMAGE_VERTICAL";
        }
    }, IMAGE_HORIZONTAL {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_IMAGE_HORIZONTAL";
        }
    }, IMAGE_TV {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_IMAGE_TV";
        }
    }, VIDEO_THUMB {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_VIDEO_THUMB";
        }
    }, PUSH_TOKEN {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_PUSH_TOKEN_ANDROID";
        }
    }, PREFERENCE_TRANSACTION_JSON {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_PREFERENCE_TRANSACTION_JSON";
        }
    }, PREFERENCE_RUNS_JSON {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_PREFERENCE_RUNS_JSON";
        }
    },
    CONTACT_US {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_CONTACT_US";
        }
    },
    CONSENT_TEXT {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_CONSENT_TEXT";
        }
    }, TEMP_DOWNLOAD_LIST {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_TEMP_DOWNLOAD_LIST";
        }
    }, TEMP_EMAIL {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_TEMP_EMAIL";
        }
    }, LOGIN_RESPONSE {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_LOGIN_RESPONSE";
        }
    }, CONTENT_EXPIRY {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_CONTENT_EXPIRY";
        }
    },
    LOGIN_TYPES_FACEBOOK {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_LOGIN_TYPES_FACEBOOK";
        }
    }, GET_STARTED {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_GET_STARTED";
        }
    },
    LOGIN_TYPES_PASSWORD {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_LOGIN_TYPES_PASSWORD";
        }
    },
    LOGIN_TYPES_GOOGLE {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_LOGIN_TYPES_GOOGLE";
        }
    },
    FREE_PRICE_POINT_EVENT {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_FREE_PRICE_POINT_EVENT";
        }
    },
    FREE_PRICE_POINT_TVOD {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_FREE_PRICE_POINT_TVOD";
        }
    },
    LOGIN_TYPES_TWITTER {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_LOGIN_TYPES_TWITTER";
        }
    }, CONTENT_STATUS {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_CONTENT_STATUS";
        }
    }, TWITTER_ACCOUNT_ID {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_TWITTER_ACCOUNT_ID";
        }
    }, USER_PREFERENCE {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_PREFERENCE_DOWNLOAD_QUALITY";
        }
    }, USER_REFERRAL_DIALOG {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_USER_REFERRAL_DIALOG";
        }
    },
    USER_BOTTOM_MENU_GROUP_ID {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_USER_BOTTOM_MENU_GROUP_ID";
        }
    },
    USER_TOPLEFT_MENU_GROUP_ID {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_USER_TOPLEFT_MENU_GROUP_ID";
        }
    },

    USER_TOPRIGHT_MENU_GROUP_ID {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_USER_TOPRIGHT_MENU_GROUP_ID";
        }
    },
    CELEB_MAIN_GROUP_ID {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_CELEB_MAIN_GROUP_ID";
        }
    }, INFLUENCER_BOTTOM_MENU_GROUP_ID {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_INFLUENCER_BOTTOM_MENU_GROUP_ID";
        }
    },
    HOME_STORE_ID {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_HOME_STORE_ID";
        }
    },
    SHOP_STORE_ID {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_SHOP_STORE_ID";
        }
    }, EVENT_STORE_ID {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_EVENT_STORE_ID";
        }
    },
    ICONOCLE_TV_STORE_ID {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_ICONOCLE_TV_STORE_ID";
        }
    },
    PLAY_STORE_ID {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_PLAY_STORE_ID";
        }
    },
    SETTINGS_STORE_ID {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_SETTINGS_STORE_ID";
        }
    },
    INFLUENCER_ID {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_INFLUENCER_ID";
        }
    },
    INFLUENCER_USER_ID {
        @Override
        public String getPreferenceKey() {
            return "ICONOCLE_INFLUENCER_USER_ID";
        }
    },
    INFLUENCER_DATA {
        @Override
        public String getPreferenceKey() {
            return "INFLUENCER_DATA";
        }
    },
    INFLUENCER_ALL_DATA {
        @Override
        public String getPreferenceKey() {
            return "INFLUENCER_ALL_DATA";
        }
    },
    MQTT_DETAILS {
        @Override
        public String getPreferenceKey() {
            return "INFLUENCER_MQTT_DETAILS";
        }
    }, REFFERAL_CODE {
        @Override
        public String getPreferenceKey() {
            return "REFFERAL_CODE";
        }
    },
    SHARE_URL {
        @Override
        public String getPreferenceKey() {
            return "SHARE_URL";
        }
    }, IS_FROM_LOGGED_USER {
        @Override
        public String getPreferenceKey() {
            return "IS_FROM_LOGGED_USER";
        }
    }, IS_REFFERBY_USER {
        @Override
        public String getPreferenceKey() {
            return "IS_REFFERBY_USER";
        }
    }, SOCIAL_SHARE_MESSAGE {
        @Override
        public String getPreferenceKey() {
            return "SOCIAL_SHARE_MESSAGE";
        }
    }, APP_REFERRAL_URL {
        @Override
        public String getPreferenceKey() {
            return "APP_REFERRAL_URL";
        }
    }, GOOGLE_VERIFICATION_DEVICE_STATUS {
        @Override
        public String getPreferenceKey() {
            return "GOOGLE_VERIFICATION_DEVICE_STATUS";
        }
    }, IP_TO_LOCAL_LMD {
        @Override
        public String getPreferenceKey() {
            return "IP_TO_LOCAL_LMD";
        }
    }, APP_LMD_DATA {
        @Override
        public String getPreferenceKey() {
            return "APP_LMD_DATA";
        }
    }, APP_CONFIG_LMD {
        @Override
        public String getPreferenceKey() {
            return "APP_CONFIG_LMD";
        }
    }, GET_APP_STORE_LMD {
        @Override
        public String getPreferenceKey() {
            return "GET_APP_STORE_LMD";
        }
    }, GET_APP_STORE {
        @Override
        public String getPreferenceKey() {
            return "GET_APP_STORE";
        }
    }, GET_APP_STORE_PAGES_HOME {
        @Override
        public String getPreferenceKey() {
            return "GET_APP_STORE_PAGES_HOME";
        }
    }, DEFAULT_INFLUENCER_STORE_ID {
        @Override
        public String getPreferenceKey() {
            return "DEFAULT_INFLUENCER_STORE_ID";
        }
    }, CALENDAR_FUTURE_DAYS {
        @Override
        public String getPreferenceKey() {
            return "calendar_future_days";
        }
    }, APPLY_INFLUENCER_URL {
        @Override
        public String getPreferenceKey() {
            return "APPLY_INFLUENCER_URL";
        }
    }, APPLY_INFLUENCER_URL_TEXT {
        @Override
        public String getPreferenceKey() {
            return "APPLY_INFLUENCER_URL_TEXT";
        }
    }, USER_PROFILE_DATA {
        @Override
        public String getPreferenceKey() {
            return "USER_PROFILE_DATA";
        }
    }, INFLUENCER_PROFILE_UPDATE {
        @Override
        public String getPreferenceKey() {
            return "INFLUENCER_PROFILE_UPDATE";
        }
    }, PROMO_VIDEO_URL {
        @Override
        public String getPreferenceKey() {
            return "PROMO_VIDEO_URL";
        }
    }, IS_BUILD_INSTALL {
        @Override
        public String getPreferenceKey() {
            return "IS_BUILD_INSTALL";
        }
    }, IS_COACHMARK_VIDEO_VIEWED {
        @Override
        public String getPreferenceKey() {
            return "IS_COACHMARK_VIDEO_VIEWED";
        }
    }, IS_VIDEO_VIEWED_BY_USER {
        @Override
        public String getPreferenceKey() {
            return "IS_VIDEO_VIEWED_BY_USER";
        }
    }, IS_VIDEO_VIEWED_BY_INFLUENCER {
        @Override
        public String getPreferenceKey() {
            return "IS_VIDEO_VIEWED_BY_INFLUENCER";
        }
    }
    //Video trimming upload constants
    , MAX_VIDEO_DURATION_OTHER {
        @Override
        public String getPreferenceKey() {
            return "MAX_VIDEO_DURATION_OTHER";
        }
    }, MAX_VIDEO_DURATION_STORY {
        @Override
        public String getPreferenceKey() {
            return "MAX_VIDEO_DURATION_STORY";
        }
    }, MAX_VIDEO_UPLOAD_SIZE {
        @Override
        public String getPreferenceKey() {
            return "MAX_VIDEO_UPLOAD_SIZE";
        }
    }
    //Image resize conatants
    , IMAGE_RESIZE_CDN_URL {
        @Override
        public String getPreferenceKey() {
            return "IMAGE_RESIZE_CDN_URL";
        }
    }, IMAGE_RESIZE_JSON {
        @Override
        public String getPreferenceKey() {
            return "IMAGE_RESIZE_JSON";
        }
    }, IMAGE_RESIZE_BLUR_JSON {
        @Override
        public String getPreferenceKey() {
            return "IMAGE_RESIZE_BLUR_JSON";
        }
    }, POST_CREATION_MAX_TRIM_DURATION {
        @Override
        public String getPreferenceKey() {
            return "POST_CREATION_MAX_TRIM_DURATION";
        }
    }, DISCUSSION_CREATION_MAX_TRIM_DURATION {
        @Override
        public String getPreferenceKey() {
            return "DISCUSSION_CREATION_MAX_TRIM_DURATION";
        }
    }, STORY_CREATION_MAX_TRIM_DURATION {
        @Override
        public String getPreferenceKey() {
            return "STORY_CREATION_MAX_TRIM_DURATION";
        }
    }, INTRODUCTION_VIDEO_CREATION_MAX_TRIM_DURATION {
        @Override
        public String getPreferenceKey() {
            return "INTRODUCTION_VIDEO_CREATION_MAX_TRIM_DURATION";
        }
    }, VIDEO_COMMENT_CREATION_MAX_TRIM_DURATION {
        @Override
        public String getPreferenceKey() {
            return "VIDEO_COMMENT_CREATION_MAX_TRIM_DURATION";
        }
    }, MIN_VIDEO_DURATION {
        @Override
        public String getPreferenceKey() {
            return "MIN_VIDEO_DURATION";
        }
    }, TV_SEARCH_HISTORY {
        @Override
        public String getPreferenceKey() {
            return "TV_SEARCH_HISTORY";
        }
    }, ADVERTISEMENT_URL {
        @Override
        public String getPreferenceKey() {
            return "ADVERTISEMENT_URL";
        }
    }, IS_ADS_ENABLE {
        @Override
        public String getPreferenceKey() {
            return "IS_ADS_ENABLE";
        }
    }, DISPLAY_ADS_AFTER {
        @Override
        public String getPreferenceKey() {
            return "DISPLAY_ADS_AFTER";
        }
    }, DEFAULT_SHARE_IMAGE_URL {
        @Override
        public String getPreferenceKey() {
            return "DEFAULT_SHARE_IMAGE_URL";
        }
    }, CLOUDINARY_URL {
        @Override
        public String getPreferenceKey() {
            return "CLOUDINARY_URL";
        }
    }, TEST_CLOUD_NAME {
        @Override
        public String getPreferenceKey() {
            return "TEST_CLOUD_NAME";
        }
    }, AD_APP_ID {
        @Override
        public String getPreferenceKey() {
            return "AD_APP_ID";
        }
    }, AD_UNIT_ID_BANNER {
        @Override
        public String getPreferenceKey() {
            return "AD_UNIT_ID_BANNER";
        }
    }, AD_UNIT_ID_NATIVE {
        @Override
        public String getPreferenceKey() {
            return "AD_UNIT_ID_NATIVE";
        }
    }, GLOBAL_SEARCH_HISTORY {
        @Override
        public String getPreferenceKey() {
            return "GLOBAL_SEARCH_HISTORY";
        }
    },
    GLOBAL_SEARCH_STORE_ID {
        @Override
        public String getPreferenceKey() {
            return "GLOBAL_SEARCH_STORE_ID";
        }
    }, GLOBAL_SEARCH_CATEGORIES {
        @Override
        public String getPreferenceKey() {
            return "GLOBAL_SEARCH_CATEGORIES";
        }
    }, AGREEMENT_TEXT {
        @Override
        public String getPreferenceKey() {
            return "AGREEMENT_TEXT";
        }
    }, WELCOME_TEXT {
        @Override
        public String getPreferenceKey() {
            return "WELCOME_TEXT";
        }
    }, IS_FACEBOOK_AGREEMENT_ACCEPTED {
        @Override
        public String getPreferenceKey() {
            return "IS_FACEBOOK_AGREEMENT_ACCEPTED";
        }
    }, IS_ALL_REQUIRED_FACEBOOK_PERMISSIONS_ACCEPTED {
        @Override
        public String getPreferenceKey() {
            return "IS_ALL_REQUIRED_FACEBOOK_PERMISSIONS_ACCEPTED";
        }
    }, CONGRATULATION_TEXT {
        @Override
        public String getPreferenceKey() {
            return "CONGRATULATION_TEXT";
        }
    }, IS_INFLUENCER_FB_ONBOARDED {
        @Override
        public String getPreferenceKey() {
            return "IS_INFLUENCER_FB_ONBOARDED";
        }
    }, AGREEMENT_PRIVACY_TEARMS_URL {
        @Override
        public String getPreferenceKey() {
            return "AGREEMENT_PRIVACY_TEARMS_URL";
        }
    }, FB_DISCONNECT_TEXT {
        @Override
        public String getPreferenceKey() {
            return "FB_DISCONNECT_TEXT";
        }
    }, PERMISSION_REQUIRED_CONNECT_TEXT {
        @Override
        public String getPreferenceKey() {
            return "PERMISSION_REQUIRED_CONNECT_TEXT";
        }
    }, All_NOTIFICATION_SEEN {
        @Override
        public String getPreferenceKey() {
            return "All_NOTIFICATION_SEEN";
        }
    }, AUTH_CONFIG {
        @Override
        public String getPreferenceKey() {
            return "AUTH_CONFIG";
        }
    }, ENCRYPTED_PAYLOAD {
        @Override
        public String getPreferenceKey() {
            return "ENCRYPTED_PAYLOAD";
        }
    }, REFERRAL_CODE {
        @Override
        public String getPreferenceKey() {
            return "REFERRAL_CODE";
        }
    }, VOUCHER_CODE {
        @Override
        public String getPreferenceKey() {
            return "VOUCHER_CODE";
        }
    }, WAH_CERTIFICATE_CODE {
        @Override
        public String getPreferenceKey() {
            return "WAH_CERTIFICATE_CODE";
        }
    },
    TRANSPORT_MODE {
        @Override
        public String getPreferenceKey() {
            return "TRANSPORT_MODE";
        }
    }, FIRST_TIME_APP_LAUNCHED {
        @Override
        public String getPreferenceKey() {
            return "FIRST_TIME_APP_LAUNCHED";
        }
    }, APP_LAUNCHED_ONE_TIME {
        @Override
        public String getPreferenceKey() {
            return "APP_LAUNCHED_ONE_TIME";
        }

    }, GIFT_CARD_DEEP_LINK {
        @Override
        public String getPreferenceKey() {
            return "GIFT_CARD_DEEP_LINK";
        }

    }, PERIOD_STARTING_DATE {
        @Override
        public String getPreferenceKey() {
            return "PERIOD_STARTING_DATE";
        }
    }, PERIOD_LENGTH {
        @Override
        public String getPreferenceKey() {
            return "PERIOD_LENGTH";
        }
    }, PERIOD_CYCLE_LENGTH {
        @Override
        public String getPreferenceKey() {
            return "PERIOD_CYCLE_LENGTH";
        }

    }, BLEEDING {
        @Override
        public String getPreferenceKey() {
            return "BLEEDING";
        }
    }, MOOD {
        @Override
        public String getPreferenceKey() {
            return "MOOD";
        }
    }, SYMPTOMS {
        @Override
        public String getPreferenceKey() {
            return "SYMPTOMS";
        }
    }, SEX_AND_SEX_DRIVE {
        @Override
        public String getPreferenceKey() {
            return "SEX_AND_SEX_DRIVE";
        }
    }, HABITS {
        @Override
        public String getPreferenceKey() {
            return "HABITS";
        }
    }, PREMENSTRUATION {
        @Override
        public String getPreferenceKey() {
            return "PREMENSTRUATION";
        }
    }, DISEASES_AND_MEDICATION {
        @Override
        public String getPreferenceKey() {
            return "DISEASES_AND_MEDICATION";
        }
    }, MENSTRUATION {
        @Override
        public String getPreferenceKey() {
            return "MENSTRUATION";
        }
    }, DAILY_LOG {
        @Override
        public String getPreferenceKey() {
            return "DAILY_LOG";
        }
    }, THIRST_REMINDER {
        @Override
        public String getPreferenceKey() {
            return "THIRST_REMINDER";
        }
    }, GIFT_CARD_ID {
        @Override
        public String getPreferenceKey() {
            return "GIFT_CARD_ID";
        }
    };


    public abstract String getPreferenceKey();
}
