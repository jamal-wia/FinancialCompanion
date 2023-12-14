package com.financialcompanion.android.core.domain.extension

import android.util.Patterns

inline fun String?.ifNullOrBlank(callback: () -> String): String {
    return if (this.isNullOrBlank()) callback.invoke() else this
}

inline fun String?.doIfNullOrBlank(action: (String?) -> Unit) {
    if (this.isNullOrBlank()) action.invoke(this)
}

inline fun String?.doIfNotNullOrBlank(action: (String) -> Unit) {
    if (!this.isNullOrBlank()) action.invoke(this)
}

fun String?.isNotEmailValid() = !isEmailValid()

fun String?.isEmailValid() = !this.isNullOrBlank() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.removeExtraSpaces(maxCountSpace: UInt): String {
    if (maxCountSpace < 1u) return this
    val regex = java.lang.StringBuilder()
        .append("\\s")
        .append("{")
        .append(maxCountSpace.toInt())
        .append(",")
        .append("}")
        .toString()
        .toRegex()
    return this.replace(regex, " ")
}