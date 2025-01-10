package `in`.woloo.www.utils

import android.content.Context
import android.text.format.DateUtils
import `in`.woloo.www.common.CommonUtils
import java.text.DateFormat
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

object TimeAgoUtils {
    private val TAG: String = TimeAgoUtils::class.java.simpleName
    private const val SECOND_MILLIS: Long = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS
    private const val WEEK_MILLIS = 7 * DAY_MILLIS
    private const val YEAR_MILLIS = 52 * WEEK_MILLIS
    private const val OLD_DATE = "dd MMMM yyyy, HH:mm"

    // private static final String NEW_DATE = "dd MMM'' yy 'at' h:mm a";
    // private static final String NEW_DATE = "dd MMM yy'' 'at' h:mm a";
    private const val NEW_DATE = "dd MMM ''yy 'at' h:mm a"
    private const val NEW_DATE_TIME = "'at' h:mm a"
    private const val NEW_DATE_TWITTER = "MMM dd yyyy"

    fun getTimeAgoComment(timeStamp: Long, mSubContentTypeTwitter: String?): String {
        var time = timeStamp
        if (time < 1000000000000L) {
            time *= 1000
        }

        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return ""
        }
        time = convertLocalTimeMillis(time)

        val diff = now - time

        return if (diff < MINUTE_MILLIS) {
            "Just now"
        } else if (diff < (2 * MINUTE_MILLIS)) {
            "min ago"
        } else if (diff < (60 * MINUTE_MILLIS)) {
            (diff / MINUTE_MILLIS).toString() + " mins ago"
        } /*else if (diff < (120 * MINUTE_MILLIS)) {
                return "hrs ago";
            }*/ else if (diff < 120 * MINUTE_MILLIS) {
            (diff / HOUR_MILLIS).toString() + " hr " + (diff - ((diff / HOUR_MILLIS) * HOUR_MILLIS)) / MINUTE_MILLIS + " mins ago"
        } else if (diff < (24 * HOUR_MILLIS)) {
            (diff / HOUR_MILLIS).toString() + " hrs ago"
        } /* else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            }*/ else if (diff < (WEEK_MILLIS)) {
            (diff / DAY_MILLIS).toString() + "D ago"
        } else if (diff < (YEAR_MILLIS)) {
            (diff / WEEK_MILLIS).toString() + "W ago"
        } else {
            (diff / YEAR_MILLIS).toString() + "Y ago"
        }
    }

    fun getTimeAgo(timeStamp: Long, mSubContentTypeTwitter: String?): String {
        var time = timeStamp
        if (time < 1000000000000L) {
            time *= 1000
        }

        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return ""
        }

        time = convertLocalTimeMillis(time)

        val diff = now - time
        return if (diff < MINUTE_MILLIS) {
            "Just now"
        } else if (diff < 2 * MINUTE_MILLIS) {
            "min ago"
        } else if (diff < 60 * MINUTE_MILLIS) {
            (diff / MINUTE_MILLIS).toString() + " min ago"
        } else if (diff < 120 * MINUTE_MILLIS) {
            (diff / HOUR_MILLIS).toString() + " hr " + (diff - ((diff / HOUR_MILLIS) * HOUR_MILLIS)) / MINUTE_MILLIS + "min ago"
        } /* else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " hrs ago";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "Yesterday "+getDateTime(timeStamp, mSubContentTypeTwitter);
            }*/ else if (isToday(time)) {
            (diff / HOUR_MILLIS).toString() + " hrs ago"
        } else if (isYesterday(time)) {
            "Yesterday " + getDateTime(time, mSubContentTypeTwitter)
        } else {
            //return getDate(timeStamp, mSubContentTypeTwitter);
            getDate(time, mSubContentTypeTwitter)
        }
    }

    fun isYesterday(timemillis: Long): Boolean {
        var isYesterday = false
        try {
            isYesterday = DateUtils.isToday(timemillis + DateUtils.DAY_IN_MILLIS)
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
        return isYesterday
    }

    fun isToday(timemillis: Long): Boolean {
        var isToday = false
        try {
            //isToday = DateUtils.isToday(timemillis - DateUtils.DAY_IN_MILLIS);
            isToday = DateUtils.isToday(timemillis)
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
        return isToday
    }

    fun getTimeHistory(timeStamp: Long): String {
        return getDate(timeStamp, "")
    }

    fun getDate(timeStamp: Long, mSubContentTypeTwitter: String?): String {
        val date = convertLocalTimeFormat(timeStamp)
        var formatedDate = ""
        /*if (mSubContentTypeTwitter.equalsIgnoreCase(AppConstants.FEED_TYPE_TWITTER)) {
            formatedDate = changeDateFormat(OLD_DATE, NEW_DATE_TWITTER, date);
        } else {
            formatedDate = changeDateFormat(OLD_DATE, NEW_DATE, date);
        }*/
        formatedDate = changeDateFormat(OLD_DATE, NEW_DATE, date)
        return formatedDate
    }

    fun getDateTime(timeStamp: Long, mSubContentTypeTwitter: String?): String {
        val date = convertLocalTimeFormat(timeStamp)
        var formatedDate = ""
        /*if (mSubContentTypeTwitter.equalsIgnoreCase(AppConstants.FEED_TYPE_TWITTER)) {
            formatedDate = changeDateFormat(OLD_DATE, NEW_DATE_TWITTER, date);
        } else {
            formatedDate = changeDateFormat(OLD_DATE, NEW_DATE, date);
        }*/
        formatedDate = changeDateFormat(OLD_DATE, NEW_DATE_TIME, date)
        return formatedDate
    }

    fun changeDateFormat(
        currentFormat: String?,
        requiredFormat: String?,
        dateString: String
    ): String {
        Logger.v(TAG, "dateString: $dateString")
        var result = ""
        val formatterOld = SimpleDateFormat(currentFormat, Locale.getDefault())
        val formatterNew = SimpleDateFormat(requiredFormat, Locale.getDefault())
        val symbols = DateFormatSymbols(Locale.getDefault())
        var date: Date? = null
        try {
            date = formatterOld.parse(dateString)
            if (date != null) {
                symbols.amPmStrings = arrayOf("am", "pm")
                formatterNew.dateFormatSymbols = symbols
                result = formatterNew.format(date)
            }
        } catch (e: ParseException) {
            CommonUtils.printStackTrace(e)
        }
        return result
    }

    fun checkIsPreviousEvent(currentFormat: String?, dateString: String): Boolean {
        var isPreviousEvent = false
        val formatterOld = SimpleDateFormat(currentFormat, Locale.getDefault())
        var date: Date? = null
        var currentDay: Date? = Date()
        try {
            date = formatterOld.parse(dateString)
            currentDay = formatterOld.parse(formatterOld.format(currentDay))
            if (date.before(currentDay)) {
                isPreviousEvent = true
            }
        } catch (e: ParseException) {
            CommonUtils.printStackTrace(e)
        }
        return isPreviousEvent
    }


    fun formatDateFromOnetoAnother(
        date: String,
        givenformat: String?,
        resultformat: String?
    ): String {
        var result = ""
        var sdf: SimpleDateFormat?
        var sdf1: SimpleDateFormat?

        try {
            sdf = SimpleDateFormat(givenformat)
            sdf1 = SimpleDateFormat(resultformat)
            result = sdf1.format(sdf.parse(date))
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
            return ""
        } finally {
            sdf = null
            sdf1 = null
        }
        return result
    }


    fun convertLocalTimeMillis(server_timestamp: Long): Long {
        var local_timestamp = server_timestamp
        val cal = Calendar.getInstance()
        var tz = cal.timeZone
        Logger.w("Get Current Time zone: ", tz.displayName)
        tz = TimeZone.getTimeZone("UTC")
        Logger.w("Set Time zone: ", tz.displayName)
        /* date formatter in local timezone */
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm:ss")
        sdf.timeZone = tz
        /* print your timestamp and double check it's the date you expect */
        val localTime = sdf.format(Date(server_timestamp))
        var localTimeDate: Date? = null
        try {
            localTimeDate = sdf.parse(localTime)
            local_timestamp = localTimeDate.time
            Logger.w("LocalTime : ", "" + localTimeDate)
        } catch (e: ParseException) {
            CommonUtils.printStackTrace(e)
        }

        return local_timestamp
    }

    fun convertLocalTimeFormat(timestamp: Long): String {
        val cal = Calendar.getInstance()
        var tz = cal.timeZone
        Logger.w("Get Current Time zone: ", tz.displayName)
        tz = TimeZone.getTimeZone("UTC")
        Logger.w("Set Time zone: ", tz.displayName)
        /* date formatter in local timezone */
        val sdf = SimpleDateFormat("dd MMM yyyy, HH:mm")
        // sdf.setTimeZone(tz);
        /* print your timestamp and double check it's the date you expect */
        val localTime = sdf.format(Date(timestamp))
        Logger.w("epoch UTC Time: ", localTime)
        return localTime
    }

    fun convertDateFromTimestamp(timestamp: Long): String {
        val cal = Calendar.getInstance()
        var tz = cal.timeZone
        Logger.w("Get Current Time zone: ", tz.displayName)
        tz = TimeZone.getTimeZone("UTC")
        Logger.w("Set Time zone: ", tz.displayName)
        /* date formatter in local timezone */
        val sdf = SimpleDateFormat("dd MMM'' yy")
        sdf.timeZone = tz
        /* print your timestamp and double check it's the date you expect */
        val localTime = sdf.format(Date(timestamp * 1000))
        Logger.w("epoch UTC Time: ", localTime)
        return localTime
    }

    @JvmStatic
    fun getTimeAgo(time: Long): String? {
        var time = time
        try {
            /*
            long time = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date mDate = sdf.parse(Date);
                time = mDate.getTime();
            } catch (Exception e) {
                  CommonUtils.printStackTrace(e);
            }*/
            if (time < 1000000000000L) {
                time *= 1000
            }
            val now = System.currentTimeMillis()
            if (time > now || time <= 0) {
                return null
            }
            val diff = now - time

            return if (diff < MINUTE_MILLIS) {
                "just now"
            } else if (diff < 2 * MINUTE_MILLIS) {
                "a minute ago"
            } else if (diff < 50 * MINUTE_MILLIS) {
                (diff / MINUTE_MILLIS).toString() + " minutes ago"
            } else if (diff < 90 * MINUTE_MILLIS) {
                "an hour ago"
            } else if (diff < 24 * HOUR_MILLIS) {
                (diff / HOUR_MILLIS).toString() + " hours ago"
            } else if (diff < 48 * HOUR_MILLIS) {
                "yesterday"
            } else {
                (diff / DAY_MILLIS).toString() + " days ago"
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
        return null
    }

    fun getDatesBetweenTwoDate(
        dateString1: String,
        dateString2: String,
        currentFormatDate: String?,
        newFormatDate: String?
    ): List<String> {
        val dates: MutableList<String> = ArrayList()
        try {
            val df1: DateFormat = SimpleDateFormat(currentFormatDate)
            val format = SimpleDateFormat(newFormatDate)
            var date1: Date? = null
            var date2: Date? = null
            try {
                date1 = df1.parse(dateString1)
                date2 = df1.parse(dateString2)
            } catch (e: ParseException) {
                CommonUtils.printStackTrace(e)
            }

            val cal1 = Calendar.getInstance()
            cal1.time = date1
            val cal2 = Calendar.getInstance()
            cal2.time = date2
            while (!cal1.after(cal2)) {
                val formatted = format.format(cal1.time)
                dates.add(formatted)
                cal1.add(Calendar.DATE, 1)
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
        return dates
    }

    fun getDateTimeDifferenceBetweenTwoDates(startDateTime: String, endDateTime: String): Long {
        var diffInHours: Long = 0
        try {
            val simpleDateFormat = SimpleDateFormat("dd MMM yyyy hh:mm aa")
            val startDate = simpleDateFormat.parse(startDateTime)
            val endDate = simpleDateFormat.parse(endDateTime)
            val diffInMillisec = endDate!!.time - startDate!!.time

            //            long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillisec);
            diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillisec)
            //            long diffInMin = TimeUnit.MILLISECONDS.toMinutes(diffInMillisec);
//            long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMillisec);
            Logger.v(TAG, "diffInDays: $diffInHours")
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
            diffInHours = 0
        }
        return diffInHours
    }

    fun getUserActivityDate(dateTime: String): String {
        try {
            val sdf1 = SimpleDateFormat("yyyy-MM-dd")
            sdf1.timeZone = TimeZone.getTimeZone("UTC")
            val date = sdf1.parse(dateTime)
            sdf1.timeZone = TimeZone.getDefault()
            val timeInMilliseconds = date!!.time
            return if (isToday(timeInMilliseconds)) {
                "Today"
            } else if (isYesterday(timeInMilliseconds)) {
                "Yesterday"
            } else {
                getFormattedDate(date)
            }
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
        return ""
    }

    fun getFormattedDate(date: Date): String {
        val cal = Calendar.getInstance()
        cal.time = date
        //2nd of march 2015
        val day = cal[Calendar.DATE]

        when (day % 10) {
            1 -> {
                if (day == 11) {
                    return SimpleDateFormat("d'th' MMM yyyy").format(date)
                }
                return SimpleDateFormat("d'st' MMM yyyy").format(date)
            }

            2 -> {
                if (day == 12) {
                    return SimpleDateFormat("d'th' MMM yyyy").format(date)
                }
                return SimpleDateFormat("d'nd' MMM yyyy").format(date)
            }

            3 -> {
                if (day == 13) {
                    return SimpleDateFormat("d'th' MMM yyyy").format(date)
                }
                return SimpleDateFormat("d'rd' MMM yyyy").format(date)
            }

            else -> return SimpleDateFormat("d'th' MMM yyyy").format(date)
        }
    }

    fun getCurrentUTCTime(context: Context?): Long {
        var utcTime = System.currentTimeMillis()
        try {
            val currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            currentTime[Calendar.ZONE_OFFSET] =
                TimeZone.getTimeZone("UTC").rawOffset
            val calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = currentTime[Calendar.HOUR_OF_DAY]
            utcTime = calendar.timeInMillis
        } catch (e: Exception) {
            CommonUtils.printStackTrace(e)
        }
        return utcTime
    }
}