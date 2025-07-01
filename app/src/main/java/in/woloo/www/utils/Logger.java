package in.woloo.www.utils;

import android.util.Log;

import in.woloo.www.BuildConfig;


public class Logger {
    private static final String LOG_PREFIX = "Logger";
    private static final int LOG_MESSAGE_LENGTH = 2000;
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;
    private static boolean showLogs = true;

    //Default constructor.
    private Logger() {
    }


    /**
     * D void.
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void d(final String tag, String message) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.d(tag, message);
        }
    }

    /**
     * D void.
     *
     * @param tag   the tag
     * @param cause the cause
     */
    public static void d(final String tag, Throwable cause) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.d(tag, tag, cause);
        }
    }

    /**
     * V void.
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void v(String tag, String message) {
        if (BuildConfig.DEBUG && showLogs) {
            if (message.length() >= LOG_MESSAGE_LENGTH) {
                for (int i = 0; i < message.length(); i += LOG_MESSAGE_LENGTH) {
                    Log.d(tag, message.substring(i, Math.min(message.length(), i + LOG_MESSAGE_LENGTH)));
                }
            } else {
                Log.v(tag, message);
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
    public static void v(final String tag, String message, Throwable cause) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.v(tag, message, cause);
        }
    }

    /**
     * I void.
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void i(final String tag, String message) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.i(tag, message);
        }
    }

    /**
     * I void.
     *
     * @param tag   the tag
     * @param cause the cause
     */
    public static void i(final String tag, Throwable cause) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.i(tag, tag, cause);
        }
    }

    /**
     * W void.
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void w(final String tag, String message) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.w(tag, message);
        }
    }

    /**
     * W void.
     *
     * @param tag   the tag
     * @param cause the cause
     */
    public static void w(final String tag, Throwable cause) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.w(tag, tag, cause);
        }
    }

    /**
     * E void.
     *
     * @param tag     the tag
     * @param message the message
     */
    public static void e(final String tag, String message) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.e(tag, message);
        }
    }

    /**
     * E void.
     *
     * @param tag   the tag
     * @param cause the cause
     */
    public static void e(final String tag, Throwable cause) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.e(tag, tag, cause);
        }
    }

    public static void e(final String tag,String msg, Throwable cause) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.e(tag, msg, cause);
        }
    }

    public static void logLargeString(String TAG, String message) {
        if (message.length() > LOG_MESSAGE_LENGTH) {
            Logger.v(TAG, message.substring(0, 3000));
            logLargeString(TAG, message.substring(3000));
        } else {
            Logger.i(TAG, message);
        }
    }

    public static void w(String tag, String msg, Exception e) {
        if (BuildConfig.DEBUG && showLogs) {
            Log.w(tag, msg, e);
        }
    }
}

