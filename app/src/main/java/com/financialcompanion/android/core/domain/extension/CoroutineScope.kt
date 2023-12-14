package com.financialcompanion.android.core.domain.extension

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.joda.time.LocalTime

fun CoroutineScope.startSecondDownTimer(
    startTime: LocalTime,
    delaySecond: Int = 1,
    tick: ((LocalTime) -> Unit)? = null,
): Job = launch {
    require(delaySecond >= 1) { "The delay should not be less than 1 second" }
    val delayMillis = delaySecond * 1000L
    var time = startTime
    do {
        delay(delayMillis)
        if (!isActive) break
        time = time.minusSeconds(delaySecond)
        tick?.invoke(time)
    } while (time.secondOfMinute > 0
        || time.minuteOfHour > 0
        || time.hourOfDay > 0
    )
}