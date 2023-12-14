package com.financialcompanion.android.core.domain.extension

import org.joda.time.DateTimeZone
import org.joda.time.Hours
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

fun createLocalDateTime(
    fullDateTime: String,
    pattern: String = "dd.MM.YYYY'T'HH:mm:ss",
): LocalDateTime {
    val formatTime = DateTimeFormat.forPattern(pattern)
    return LocalDateTime.parse(fullDateTime, formatTime)
}

fun LocalDateTime.convertUTCtoZone(dateTimeZone: DateTimeZone? = null): LocalDateTime {
    val nowUTC = LocalDateTime.now(DateTimeZone.UTC)
    val nowZone =
        if (dateTimeZone == null) LocalDateTime.now()
        else LocalDateTime.now(dateTimeZone)

    val hoursBetween = Hours.hoursBetween(nowUTC, nowZone)
    return if (nowUTC < nowZone) this.plusHours(hoursBetween.hours)
    else if (nowUTC > nowZone) this.minusHours(hoursBetween.hours)
    else this
}
