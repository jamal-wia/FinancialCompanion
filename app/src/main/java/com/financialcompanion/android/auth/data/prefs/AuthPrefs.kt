package com.financialcompanion.android.auth.data.prefs

import com.chibatching.kotpref.KotprefModel

object AuthPrefs : KotprefModel() {
    var isAuthorization: Boolean by booleanPref(
        default = false,
        key = "is_authorization"
    )
}