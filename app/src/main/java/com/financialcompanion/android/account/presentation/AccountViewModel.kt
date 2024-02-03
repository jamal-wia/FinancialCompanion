package com.financialcompanion.android.account.presentation

import com.financialcompanion.android.core.domain.extension.genInject
import com.financialcompanion.android.core.presentation.mvvm.BaseViewModel
import me.aartikov.alligator.AndroidNavigator

class AccountViewModel : BaseViewModel() {

    private val navigator: AndroidNavigator by genInject()

    fun goBackAuth() {
        navigator.goBack()
    }
}