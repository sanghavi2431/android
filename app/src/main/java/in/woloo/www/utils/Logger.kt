package `in`.woloo.www.utils

import android.util.Log
import `in`.woloo.www.BuildConfig
import kotlin.math.min

object Logger {
    private const val LOG_PREFIX = "Logger"
    private const val LOG_MESSAGE_LENGTH = 2000
    private const val LOG_PREFIX_LENGTH = LOG_PREFIX.length
    private const val MAX_LOG_TAG_LENGTH = 23
    private const val showLogs = true

    /**
     * D void.
     *
     * @param tag     the tag
     * @param message the message
     */
    @JvmStatic
    fun d(tag: String?, message: String) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.d(tag, message)
        }
    }

    /**
     * D void.
     *
     * @param tag   the tag
     * @param cause the cause
     */
    fun d(tag: String?, cause: Throwable?) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.d(tag, tag, cause)
        }
    }

    /**
     * V void.
     *
     * @param tag     the tag
     * @param message the message
     */
    @JvmStatic
    fun v(tag: String?, message: String) {
        if (BuildConfig.DEBUG && showLogs) {
            if (message.length >= LOG_MESSAGE_LENGTH) {
                var i = 0
                while (i < message.length) {
                    Log.d(
                        tag, message.substring(
                            i, min(
                                message.length.toDouble(),
                                (i + LOG_MESSAGE_LENGTH).toDouble()
                            ).toInt()
                        )
                    )
                    i += LOG_MESSAGE_LENGTH
                }
            } else {
                Log.v(tag, message)
            }
        }
    }

    /**
     * V void.
     *
     * @param tag     the tag
     * @param message the message
     * @param cause   the cause
     */
    fun v(tag: String?, message: String?, cause: Throwable?) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.v(tag, message, cause)
        }
    }

    /**
     * I void.
     *
     * @param tag     the tag
     * @param message the message
     */
    @JvmStatic
    fun i(tag: String?, message: String) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.i(tag, message)
        }
    }

    /**
     * I void.
     *
     * @param tag   the tag
     * @param cause the cause
     */
    fun i(tag: String?, cause: Throwable?) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.i(tag, tag, cause)
        }
    }

    /**
     * W void.
     *
     * @param tag     the tag
     * @param message the message
     */
    @JvmStatic
    fun w(tag: String?, message: String) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.w(tag, message)
        }
    }

    /**
     * W void.
     *
     * @param tag   the tag
     * @param cause the cause
     */
    fun w(tag: String?, cause: Throwable?) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.w(tag, tag, cause)
        }
    }

    /**
     * E void.
     *
     * @param tag     the tag
     * @param message the message
     */
    @JvmStatic
    fun e(tag: String?, message: String) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.e(tag, message)
        }
    }

    /**
     * E void.
     *
     * @param tag   the tag
     * @param cause the cause
     */
    fun e(tag: String?, cause: Throwable?) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.e(tag, tag, cause)
        }
    }

    @JvmStatic
    fun e(tag: String?, msg: String?, cause: Throwable?) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.e(tag, msg, cause)
        }
    }

    fun logLargeString(TAG: String?, message: String) {
        if (message.length > LOG_MESSAGE_LENGTH) {
            v(TAG, message.substring(0, 3000))
            logLargeString(TAG, message.substring(3000))
        } else {
            i(TAG, message)
        }
    }

    fun w(tag: String?, msg: String?, e: Exception?) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.w(tag, msg, e)
        }
    }
}

