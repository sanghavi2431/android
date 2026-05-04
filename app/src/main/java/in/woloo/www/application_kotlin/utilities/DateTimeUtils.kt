package `in`.woloo.www.application_kotlin.utilities

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

object DateTimeUtils {
    /**
     * Converts a UTC ISO 8601 date string to "dd MM yyyy HH mm" in IST.
     *
     * @param utcDateString UTC date string (e.g., "2025-06-12T06:01:59.293Z")
     * @return formatted date string in IST (e.g., "12 06 2025 11 31")
     */
    fun convertUtcToIst(utcDateString: String): String {
        return try {
            val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            utcFormat.timeZone = TimeZone.getTimeZone("UTC")

            val date = utcFormat.parse(utcDateString)

            val istFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
            istFormat.timeZone = TimeZone.getTimeZone("Asia/Kolkata")

            date?.let { istFormat.format(it) } ?: ""
        } catch (e: Exception) {
            ""
        }
    }
}