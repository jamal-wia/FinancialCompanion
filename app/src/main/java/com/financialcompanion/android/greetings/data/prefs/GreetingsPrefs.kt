package com.financialcompanion.android.greetings.data.prefs

import com.chibatching.kotpref.KotprefModel

object GreetingsPrefs : KotprefModel() {
    var isShowed: Boolean by booleanPref(
        default = false,
        key = "is_showed",
    )
}