package `in`.woloo.www.v2.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DateToLocalDateConverter {

    fun dateConvert(utcDateTimeStr: String): String {
        val utcDateTimeStr = utcDateTimeStr
        val instant = Instant.parse(utcDateTimeStr)
        val utcZonedDateTime = instant.atZone(ZoneId.of("UTC"))
        val localZoneId = ZoneId.systemDefault()
        val localZonedDateTime = utcZonedDateTime.withZoneSameInstant(localZoneId)
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return localZonedDateTime.format(formatter)
    }

}