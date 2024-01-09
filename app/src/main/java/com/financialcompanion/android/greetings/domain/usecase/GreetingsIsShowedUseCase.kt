package com.financialcompanion.android.greetings.domain.usecase

import com.financialcompanion.android.greetings.data.prefs.GreetingsPrefs

class GreetingsIsShowedUseCase {

    fun setIsShowed(value: Boolean) {
        GreetingsPrefs.isShowed = value
    }

    fun getIsShowed(): Boolean {
        return GreetingsPrefs.isShowed
    }
}