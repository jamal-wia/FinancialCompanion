package com.financialcompanion.android.core.presentation.extension

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.PluralsRes
import androidx.core.content.ContextCompat
import java.util.Locale

fun Context.color(@ColorRes resId: Int): Int = ContextCompat.getColor(this, resId)

fun Context.drawable(@DrawableRes resId: Int): Drawable = ContextCompat.getDrawable(this, resId)!!

val Context.currentLocale: Locale
    get() = resources.configuration.locales[0]

fun Context.getQuantityString(@PluralsRes idRes: Int, quantity: Int): String {
    return resources.getQuantityString(idRes, quantity)
}

fun Context.getQuantityString(
    @PluralsRes idRes: Int,
    quantity: Int,
    vararg formatArgs: Any
): String = resources.getQuantityString(idRes, quantity, *formatArgs)