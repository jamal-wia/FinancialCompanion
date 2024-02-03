package com.financialcompanion.android.auth.presentation

import com.financialcompanion.android.core.domain.extension.genInject
import com.financialcompanion.android.core.presentation.mvvm.BaseViewModel
import com.financialcompanion.android.core.presentation.navigation.AppScreen
import me.aartikov.alligator.AndroidNavigator

class AuthViewModel : BaseViewModel() {

    private val navigator: AndroidNavigator by genInject()

    fun goToAccount() {
        navigator.goForward(AppScreen.AccountScreen)
    }
}