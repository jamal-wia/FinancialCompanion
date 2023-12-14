package com.financialcompanion.android.core.domain.extension

import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat

private const val PATTERN_TIME = "HH:mm:ss"
private val formatTime = DateTimeFormat.forPattern(PATTERN_TIME)

fun createLocalTimeFrom(fullDateTime: String, delimiter: String = "T"): LocalTime {
    val time = fullDateTime.substringAfter(delimiter)
    return LocalTime.parse(time, formatTime)
}

fun LocalTime?.haveTime(): Boolean {
    this ?: return false
    return hourOfDay != 0 || minuteOfHour != 0 || secondOfMinute != 0
}
