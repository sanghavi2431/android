package in.woloo.www.firebase;

public interface NotificationType {

    String USER_STORIES="user_stories";
    String USER_LIVE_QUIZ  ="user_live_quiz";
    String USER_NORMAL_QUIZ  ="user_normal_quiz";



    String FEED_LISTING  ="feed_listing";
    String FEED_LIKE  ="feed_like";
    String USER_FEED_DETAIL  ="user_feed_detail";
    String FEED_COMMENT  ="feed_comment";
    String FEED_COMMENT_REPLY  ="feed_comment_reply";
    String INFLUENCER_FEED_COMMENT_PIN  ="influencer_feed_comment_pin";
    String FEED_COMMENT_LIKE  ="feed_comment_like";
    String INFLUENCER_FEED_COMMENT_LIKE  ="influencer_feed_comment_like";

    String DISCUSSION_LISTING  ="discussion_listing";
    String DISCUSSION_LIKE  ="discussion_like";
    String USER_DISCUSSION_DETAIL  ="user_discussion_detail";
    String DISCUSSION_COMMENT  ="discussion_comment";
    String DISCUSSION_COMMENT_REPLY  ="discussion_comment_reply";
    String INFLUENCER_DISCUSSION_COMMENT_PIN  ="influencer_discussion_comment_pin";
    String DISCUSSION_COMMENT_LIKE  ="discussion_comment_like";
    String INFLUENCR_DISCUSSION_COMMENT_LIKE  ="influencer_discussion_comment_like";
    String USER_VIDEO_DETAIL  ="user_video_detail"; // For Video detail of TV
    String USER_CHANNEL_DETAIL  ="user_channel_detail"; // For Channel detail of TV
    String ICONOCLE_TV_HOME  ="iconocle_tv_home"; // For ICONOCLE TV HOME

    //Follow
    String USERS_FOLLOW_INFLUENECER  ="users_follow_influencer";
    String INFLUENCER_FOLLOWS_INFLUENECER  ="influencer_follow_influencer";

    //Inactivity
    String INFLUENCER_INACTIVITY  ="influencer_inactivity";
    String USER_INACTIVITY  ="user_inactivity";

    String INFLUENCER_EVENT_MARK_ATTENDING = "influencer_event_attending_personal_event";
    String INFLUENCER_EVENT_ATTENDING_PERSONAL_EVENT = "influencer_event_attending_personal_event_modified";
    String INFLUENCER_PERSONAL_EVENT_DELETED = "influencer_personal_event_deleted";
    String INFLUENCER_NEW_EVENT_ADDED = "influencer_new_event_added";
    String INFLUENCER_MAKE_POST = "influencer_create_post_after_event_added";
    String INFLUENCER_EVENT_LIST = "influencer_event_list";


    String INFLUENCER_EVENT_UPDATE="influencer_event_update";



    String INFLUENCER_QUIZ_HISTORY="influencer_live_quiz_summary";
    String USER_QUIZ_LIST="user_quiz_list";
    String USER_QUIZ_BID_POPUP="user_quiz_bid_opens";
    String USER_QUIZ_LOBBY="user_live_quiz_start";


    String STORY_LISTING = "story_listing";

    String INFLUENCER_ICONOCLE_TV_VIDEO_DETAIL = "influencer_iconocle_tv_video_detail";
    String INFLUENCER_ICONOCLE_TV_HOME = "influencer_iconocle_tv_home";
    String USER_ICONOCLE_TV_HOME = "user_iconocle_tv_home";

    String INFLUENCER_FB_ONBOARDING = "influencer_fb_onboarding"; //For Deeplink redirect to Influencer Login
    String INFLUENCER_FB_ONBOARDING_REMINDER = "influencer_fb_onboarding_reminder"; //Reminder to connect Fb
    String INFLUENCER_FB_ONBOARDING_DISCONNECT_REMINDER = "influencer_fb_onboarding_disconnect_reminder"; //Reminder to connect the diconnceted Fb
    String INFLUENCER_FB_ONBOARDING_TOKEN_EXPIRY = "influencer_fb_onboarding_token_expiry"; // Fb onboard Token expired notification
    String INFLUENCER_FB_ONBOARDING_FEED_READY = "influencer_fb_onboarding_feed_ready"; // Fb onboard Feed published live

}
