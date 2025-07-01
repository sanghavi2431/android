package in.woloo.www.firebase;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import in.woloo.www.BuildConfig;
import in.woloo.www.common.CommonUtils;
import in.woloo.www.utils.Logger;


public class SubscribeToTopicUtils {

    private static final String TAG = SubscribeToTopicUtils.class.getSimpleName();
    private static SubscribeToTopicUtils sInstance;
    private Context mContext;


    private SubscribeToTopicUtils(Context mContext) {
        this.mContext = mContext;

    }

    public static SubscribeToTopicUtils getInstance(Context mContext) {
        if (sInstance == null) {
            synchronized (SubscribeToTopicUtils.class) {
                if (sInstance == null) {
                    sInstance = new SubscribeToTopicUtils(mContext);
                }
            }
        }
        return sInstance;
    }

    public List<String> getTopicListOfFCM(boolean allTopics)
    {
        List<String> mTopicList = new ArrayList<>();

        try {
            CommonUtils commonUtils = new CommonUtils();

            if (allTopics) {

            }else {

            }

        }catch (Exception e)
        {  CommonUtils.printStackTrace(e);}

        return mTopicList;
    }

    public void FCMSubscribeToTopic(Context mContext) {

        try {
            List<String> mTopicList = new ArrayList<>();

            mTopicList = getTopicListOfFCM(false);

            for (int i = 0; i < mTopicList.size(); i++) {

                String mTopicName = mTopicList.get(i);
                FirebaseMessaging.getInstance().subscribeToTopic(mTopicList.get(i))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Logger.w(TAG, "FCM subscribeToTopic Successful: " + mTopicName);
                                } else {
                                    Logger.w(TAG, "FCM subscribeToTopic unSuccessful: " + mTopicName);
                                }

                            }

                        });
            }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }


    }

    public void FCMUnSubscribeToTopic(Context mContext) {

        try {
            List<String> mTopicList = new ArrayList<>();

            mTopicList = getTopicListOfFCM(true);

            for (int i = 0; i < mTopicList.size(); i++) {

                String mTopicName = mTopicList.get(i);
                FirebaseMessaging.getInstance().unsubscribeFromTopic(mTopicList.get(i))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Logger.w(TAG, "FCM subscribeToTopic Successful: " + mTopicName);
                                } else {
                                    Logger.w(TAG, "FCM subscribeToTopic unSuccessful: " + mTopicName);
                                }

                            }

                        });
            }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }


    }
}
