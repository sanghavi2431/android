package `in`.woloo.www.firebase

import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging
import `in`.woloo.www.common.CommonUtils
import `in`.woloo.www.utils.Logger.w

class SubscribeToTopicUtils private constructor(private val mContext: Context) {
    fun getTopicListOfFCM(allTopics: Boolean): List<String> {
        val mTopicList: List<String> = ArrayList()

        try {
            val commonUtils = CommonUtils()

            if (allTopics) {
            } else {
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }

        return mTopicList
    }

    fun FCMSubscribeToTopic(mContext: Context?) {
        try {
            var mTopicList: List<String> = ArrayList()

            mTopicList = getTopicListOfFCM(false)

            for (i in mTopicList.indices) {
                val mTopicName = mTopicList[i]
                FirebaseMessaging.getInstance().subscribeToTopic(mTopicList[i])
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            w(
                                TAG,
                                "FCM subscribeToTopic Successful: $mTopicName"
                            )
                        } else {
                            w(
                                TAG,
                                "FCM subscribeToTopic unSuccessful: $mTopicName"
                            )
                        }
                    }
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    fun FCMUnSubscribeToTopic(mContext: Context?) {
        try {
            var mTopicList: List<String> = ArrayList()

            mTopicList = getTopicListOfFCM(true)

            for (i in mTopicList.indices) {
                val mTopicName = mTopicList[i]
                FirebaseMessaging.getInstance().unsubscribeFromTopic(mTopicList[i])
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            w(
                                TAG,
                                "FCM subscribeToTopic Successful: $mTopicName"
                            )
                        } else {
                            w(
                                TAG,
                                "FCM subscribeToTopic unSuccessful: $mTopicName"
                            )
                        }
                    }
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
    }

    companion object {
        private val TAG: String = SubscribeToTopicUtils::class.java.simpleName
        private var sInstance: SubscribeToTopicUtils? = null
        fun getInstance(mContext: Context): SubscribeToTopicUtils {
            if (sInstance == null) {
                synchronized(SubscribeToTopicUtils::class.java) {
                    if (sInstance == null) {
                        sInstance = SubscribeToTopicUtils(mContext)
                    }
                }
            }
            return sInstance!!
        }
    }
}
