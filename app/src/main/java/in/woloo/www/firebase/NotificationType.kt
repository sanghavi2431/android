package `in`.woloo.www.firebase

interface NotificationType {
    companion object {
        const val USER_STORIES: String = "user_stories"
        const val USER_LIVE_QUIZ: String = "user_live_quiz"
        const val USER_NORMAL_QUIZ: String = "user_normal_quiz"


        const val FEED_LISTING: String = "feed_listing"
        const val FEED_LIKE: String = "feed_like"
        const val USER_FEED_DETAIL: String = "user_feed_detail"
        const val FEED_COMMENT: String = "feed_comment"
        const val FEED_COMMENT_REPLY: String = "feed_comment_reply"
        const val INFLUENCER_FEED_COMMENT_PIN: String = "influencer_feed_comment_pin"
        const val FEED_COMMENT_LIKE: String = "feed_comment_like"
        const val INFLUENCER_FEED_COMMENT_LIKE: String = "influencer_feed_comment_like"

        const val DISCUSSION_LISTING: String = "discussion_listing"
        const val DISCUSSION_LIKE: String = "discussion_like"
        const val USER_DISCUSSION_DETAIL: String = "user_discussion_detail"
        const val DISCUSSION_COMMENT: String = "discussion_comment"
        const val DISCUSSION_COMMENT_REPLY: String = "discussion_comment_reply"
        const val INFLUENCER_DISCUSSION_COMMENT_PIN: String = "influencer_discussion_comment_pin"
        const val DISCUSSION_COMMENT_LIKE: String = "discussion_comment_like"
        const val INFLUENCR_DISCUSSION_COMMENT_LIKE: String = "influencer_discussion_comment_like"
        const val USER_VIDEO_DETAIL: String = "user_video_detail" // For Video detail of TV
        const val USER_CHANNEL_DETAIL: String = "user_channel_detail" // For Channel detail of TV
        const val ICONOCLE_TV_HOME: String = "iconocle_tv_home" // For ICONOCLE TV HOME

        //Follow
        const val USERS_FOLLOW_INFLUENECER: String = "users_follow_influencer"
        const val INFLUENCER_FOLLOWS_INFLUENECER: String = "influencer_follow_influencer"

        //Inactivity
        const val INFLUENCER_INACTIVITY: String = "influencer_inactivity"
        const val USER_INACTIVITY: String = "user_inactivity"

        const val INFLUENCER_EVENT_MARK_ATTENDING: String =
            "influencer_event_attending_personal_event"
        const val INFLUENCER_EVENT_ATTENDING_PERSONAL_EVENT: String =
            "influencer_event_attending_personal_event_modified"
        const val INFLUENCER_PERSONAL_EVENT_DELETED: String = "influencer_personal_event_deleted"
        const val INFLUENCER_NEW_EVENT_ADDED: String = "influencer_new_event_added"
        const val INFLUENCER_MAKE_POST: String = "influencer_create_post_after_event_added"
        const val INFLUENCER_EVENT_LIST: String = "influencer_event_list"


        const val INFLUENCER_EVENT_UPDATE: String = "influencer_event_update"


        const val INFLUENCER_QUIZ_HISTORY: String = "influencer_live_quiz_summary"
        const val USER_QUIZ_LIST: String = "user_quiz_list"
        const val USER_QUIZ_BID_POPUP: String = "user_quiz_bid_opens"
        const val USER_QUIZ_LOBBY: String = "user_live_quiz_start"


        const val STORY_LISTING: String = "story_listing"

        const val INFLUENCER_ICONOCLE_TV_VIDEO_DETAIL: String =
            "influencer_iconocle_tv_video_detail"
        const val INFLUENCER_ICONOCLE_TV_HOME: String = "influencer_iconocle_tv_home"
        const val USER_ICONOCLE_TV_HOME: String = "user_iconocle_tv_home"

        const val INFLUENCER_FB_ONBOARDING: String =
            "influencer_fb_onboarding" //For Deeplink redirect to Influencer Login
        const val INFLUENCER_FB_ONBOARDING_REMINDER: String =
            "influencer_fb_onboarding_reminder" //Reminder to connect Fb
        const val INFLUENCER_FB_ONBOARDING_DISCONNECT_REMINDER: String =
            "influencer_fb_onboarding_disconnect_reminder" //Reminder to connect the diconnceted Fb
        const val INFLUENCER_FB_ONBOARDING_TOKEN_EXPIRY: String =
            "influencer_fb_onboarding_token_expiry" // Fb onboard Token expired notification
        const val INFLUENCER_FB_ONBOARDING_FEED_READY: String =
            "influencer_fb_onboarding_feed_ready" // Fb onboard Feed published live
    }
}
