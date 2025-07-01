package in.woloo.www.utils;

import android.content.Context;
import android.provider.Settings;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import in.woloo.www.common.CommonUtils;


public class TimeAgoUtils {
    private static final String TAG = TimeAgoUtils.class.getSimpleName();
    private static final long SECOND_MILLIS = 1000;
    private static final long MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final long WEEK_MILLIS = 7 * DAY_MILLIS;
    private static final long YEAR_MILLIS = 52 * WEEK_MILLIS;
    private static final String OLD_DATE = "dd MMMM yyyy, HH:mm";
   // private static final String NEW_DATE = "dd MMM'' yy 'at' h:mm a";
   // private static final String NEW_DATE = "dd MMM yy'' 'at' h:mm a";
    private static final String NEW_DATE = "dd MMM ''yy 'at' h:mm a";
    private static final String NEW_DATE_TIME = "'at' h:mm a";
    private static final String NEW_DATE_TWITTER = "MMM dd yyyy";

    public static String getTimeAgoComment(long timeStamp, String mSubContentTypeTwitter) {

        long time = timeStamp;
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return "";
        }
        time = convertLocalTimeMillis(time);

        final long diff = now - time;

        if (diff < MINUTE_MILLIS) {
            return "Just now";
        } else if (diff < (2 * MINUTE_MILLIS)) {
            return "min ago";
        } else if (diff < (60 * MINUTE_MILLIS)) {
            return diff / MINUTE_MILLIS + " mins ago";
        } /*else if (diff < (120 * MINUTE_MILLIS)) {
            return "hrs ago";
        }*/ else if (diff < 120 * MINUTE_MILLIS) {
            return diff / HOUR_MILLIS + " hr " + (diff - ((diff / HOUR_MILLIS) * HOUR_MILLIS)) / MINUTE_MILLIS + " mins ago";
        } else if (diff < (24 * HOUR_MILLIS)) {
            return diff / HOUR_MILLIS + " hrs ago";
        }/* else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        }*/ else if (diff < (WEEK_MILLIS)) {
            return diff / DAY_MILLIS + "D ago";
        } else if (diff < (YEAR_MILLIS)) {
            return diff / WEEK_MILLIS + "W ago";
        } else {
            return diff / YEAR_MILLIS + "Y ago";
        }
    }

    public static String getTimeAgo(long timeStamp, String mSubContentTypeTwitter) {
        long time = timeStamp;
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return "";
        }

        time = convertLocalTimeMillis(time);

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "Just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "min ago";
        } else if (diff < 60 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " min ago";
        } else if (diff < 120 * MINUTE_MILLIS) {
            return diff / HOUR_MILLIS + " hr " + (diff - ((diff / HOUR_MILLIS) * HOUR_MILLIS)) / MINUTE_MILLIS + "min ago";
        }/* else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hrs ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "Yesterday "+getDateTime(timeStamp, mSubContentTypeTwitter);
        }*/ else if (isToday(time)) {
            return diff / HOUR_MILLIS + " hrs ago";
        } else if (isYesterday(time)) {
            return "Yesterday " + getDateTime(time, mSubContentTypeTwitter);
        } else {
            //return getDate(timeStamp, mSubContentTypeTwitter);
            return getDate(time, mSubContentTypeTwitter);
        }
    }

    public static boolean isYesterday(long timemillis) {
        boolean isYesterday = false;
        try {
            isYesterday = DateUtils.isToday(timemillis + DateUtils.DAY_IN_MILLIS);
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
        return isYesterday;
    }

    public static boolean isToday(long timemillis) {
        boolean isToday = false;
        try {
            //isToday = DateUtils.isToday(timemillis - DateUtils.DAY_IN_MILLIS);
            isToday = DateUtils.isToday(timemillis);
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
        return isToday;
    }

    public static String getTimeHistory(long timeStamp) {
        return getDate(timeStamp, "");
    }

    public static String getDate(long timeStamp, String mSubContentTypeTwitter) {
        String date = convertLocalTimeFormat(timeStamp);
        String formatedDate = "";
        /*if (mSubContentTypeTwitter.equalsIgnoreCase(AppConstants.FEED_TYPE_TWITTER)) {
            formatedDate = changeDateFormat(OLD_DATE, NEW_DATE_TWITTER, date);
        } else {
            formatedDate = changeDateFormat(OLD_DATE, NEW_DATE, date);
        }*/
        formatedDate = changeDateFormat(OLD_DATE, NEW_DATE, date);
        return formatedDate;
    }

    public static String getDateTime(long timeStamp, String mSubContentTypeTwitter) {
        String date = convertLocalTimeFormat(timeStamp);
        String formatedDate = "";
        /*if (mSubContentTypeTwitter.equalsIgnoreCase(AppConstants.FEED_TYPE_TWITTER)) {
            formatedDate = changeDateFormat(OLD_DATE, NEW_DATE_TWITTER, date);
        } else {
            formatedDate = changeDateFormat(OLD_DATE, NEW_DATE, date);
        }*/
        formatedDate = changeDateFormat(OLD_DATE, NEW_DATE_TIME, date);
        return formatedDate;
    }

    public static String changeDateFormat(String currentFormat, String requiredFormat, String dateString) {
        Logger.v(TAG, "dateString: " + dateString);
        String result = "";
        SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat, Locale.getDefault());
        SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.getDefault());
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
        Date date = null;
        try {
            date = formatterOld.parse(dateString);
            if (date != null) {
                symbols.setAmPmStrings(new String[]{"am", "pm"});
                formatterNew.setDateFormatSymbols(symbols);
                result = formatterNew.format(date);
            }
        } catch (ParseException e) {
              CommonUtils.printStackTrace(e);
        }
        return result;
    }

    public static boolean checkIsPreviousEvent(String currentFormat,String dateString) {
        boolean isPreviousEvent = false;
        SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat, Locale.getDefault());
        Date date = null;
        Date currentDay = new Date();
        try {
            date = formatterOld.parse(dateString);
            currentDay = formatterOld.parse(formatterOld.format(currentDay));
            if(date.before(currentDay)){
                isPreviousEvent = true;
            }
        } catch (ParseException e) {
              CommonUtils.printStackTrace(e);
        }
        return isPreviousEvent;
    }


    public static String formatDateFromOnetoAnother(String date, String givenformat, String resultformat) {

        String result = "";
        SimpleDateFormat sdf;
        SimpleDateFormat sdf1;

        try {
            sdf = new SimpleDateFormat(givenformat);
            sdf1 = new SimpleDateFormat(resultformat);
            result = sdf1.format(sdf.parse(date));
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
            return "";
        } finally {
            sdf = null;
            sdf1 = null;
        }
        return result;
    }


    public static long convertLocalTimeMillis(long server_timestamp) {
        long local_timestamp = server_timestamp;
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        Logger.w("Get Current Time zone: ", tz.getDisplayName());
        tz = TimeZone.getTimeZone("UTC");
        Logger.w("Set Time zone: ", tz.getDisplayName());
        /* date formatter in local timezone */
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm:ss");
        sdf.setTimeZone(tz);
        /* print your timestamp and double check it's the date you expect */
        String localTime = sdf.format(new Date(server_timestamp));
        Date localTimeDate = null;
        try {
            localTimeDate = sdf.parse(localTime);
            local_timestamp = localTimeDate.getTime();
            Logger.w("LocalTime : ", "" + localTimeDate);
        } catch (ParseException e) {
              CommonUtils.printStackTrace(e);
        }

        return local_timestamp;
    }

    public static String convertLocalTimeFormat(long timestamp) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        Logger.w("Get Current Time zone: ", tz.getDisplayName());
        tz = TimeZone.getTimeZone("UTC");
        Logger.w("Set Time zone: ", tz.getDisplayName());
        /* date formatter in local timezone */
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm");
        // sdf.setTimeZone(tz);
        /* print your timestamp and double check it's the date you expect */
        String localTime = sdf.format(new Date(timestamp));
        Logger.w("epoch UTC Time: ", localTime);
        return localTime;
    }

    public static String convertDateFromTimestamp(long timestamp) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        Logger.w("Get Current Time zone: ", tz.getDisplayName());
        tz = TimeZone.getTimeZone("UTC");
        Logger.w("Set Time zone: ", tz.getDisplayName());
        /* date formatter in local timezone */
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM'' yy");
        sdf.setTimeZone(tz);
        /* print your timestamp and double check it's the date you expect */
        String localTime = sdf.format(new Date(timestamp * 1000));
        Logger.w("epoch UTC Time: ", localTime);
        return localTime;
    }

    public static String getTimeAgo(long time) {

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
                time *= 1000;
            }
            long now = System.currentTimeMillis();
            if (time > now || time <= 0) {
                return null;
            }
            final long diff = now - time;

            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes ago";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " hours ago";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " days ago";
            }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
        return null;
    }

    public static List<String> getDatesBetweenTwoDate(String dateString1, String dateString2,String currentFormatDate,String newFormatDate) {
        List<String> dates = new ArrayList<>();
        try {
            DateFormat df1 = new SimpleDateFormat(currentFormatDate);
            SimpleDateFormat format = new SimpleDateFormat(newFormatDate);
            Date date1 = null;
            Date date2 = null;
            try {
                date1 = df1.parse(dateString1);
                date2 = df1.parse(dateString2);
            } catch (ParseException e) {
                  CommonUtils.printStackTrace(e);
            }

            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);
            while (!cal1.after(cal2)) {
                String formatted = format.format(cal1.getTime());
                dates.add(formatted);
                cal1.add(Calendar.DATE, 1);
            }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
        return dates;
    }

    public static long getDateTimeDifferenceBetweenTwoDates(String startDateTime, String endDateTime) {
        long diffInHours = 0;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
            Date startDate = simpleDateFormat.parse(startDateTime);
            Date endDate = simpleDateFormat.parse(endDateTime);
            long diffInMillisec = endDate.getTime() - startDate.getTime();

//            long diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMillisec);
            diffInHours = TimeUnit.MILLISECONDS.toHours(diffInMillisec);
//            long diffInMin = TimeUnit.MILLISECONDS.toMinutes(diffInMillisec);
//            long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMillisec);
            Logger.v(TAG, "diffInDays: " + diffInHours);

        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
            diffInHours = 0;
        }
        return diffInHours;
    }

    public static String getUserActivityDate(String dateTime) {
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            sdf1.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = sdf1.parse(dateTime);
            sdf1.setTimeZone(TimeZone.getDefault());
            long timeInMilliseconds = date.getTime();
            if (isToday(timeInMilliseconds)) {
                return "Today";
            } else if (isYesterday(timeInMilliseconds)) {
                return "Yesterday";
            } else {
                return getFormattedDate(date);
            }
        } catch (Exception e) {
              CommonUtils.printStackTrace(e);
        }
        return "";
    }

    public static String getFormattedDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        //2nd of march 2015
        int day = cal.get(Calendar.DATE);

        switch (day % 10) {
            case 1:
                if(day == 11){
                    return new SimpleDateFormat("d'th' MMM yyyy").format(date);
                }
                return new SimpleDateFormat("d'st' MMM yyyy").format(date);
            case 2:
                if(day == 12){
                    return new SimpleDateFormat("d'th' MMM yyyy").format(date);
                }
                return new SimpleDateFormat("d'nd' MMM yyyy").format(date);
            case 3:
                if(day == 13){
                    return new SimpleDateFormat("d'th' MMM yyyy").format(date);
                }
                return new SimpleDateFormat("d'rd' MMM yyyy").format(date);
            default:
                return new SimpleDateFormat("d'th' MMM yyyy").format(date);
        }
    }

    public static long getCurrentUTCTime(Context context)
    {
        long utcTime= System.currentTimeMillis();
        try{
            Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            currentTime.set(Calendar.ZONE_OFFSET, TimeZone.getTimeZone("UTC").getRawOffset());
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, currentTime.get(Calendar.HOUR_OF_DAY));
            utcTime  = calendar.getTimeInMillis();
        }catch (Exception e)
        {
              CommonUtils.printStackTrace(e);
        }
        return utcTime;
    }
}