package com.financialcompanion.android.category.data.pref

import com.chibatching.kotpref.KotprefModel

object CategoryPref : KotprefModel() {

    /**
     * This value indicates whether the database of categories has been initialized or not
     * */
    var created by booleanPref(default = false, key = "created")
}