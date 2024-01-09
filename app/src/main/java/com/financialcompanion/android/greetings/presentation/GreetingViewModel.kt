package com.financialcompanion.android.greetings.presentation

import com.financialcompanion.android.core.domain.extension.genInject
import com.financialcompanion.android.core.presentation.mvvm.BaseViewModel
import com.financialcompanion.android.core.presentation.navigation.AppScreen
import com.financialcompanion.android.greetings.domain.usecase.GreetingsIsShowedUseCase
import com.financialcompanion.android.greetings.presentation.GreetingViewState.Data
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import me.aartikov.alligator.AndroidNavigator

class GreetingViewModel : BaseViewModel() {

    private val _state = MutableStateFlow<GreetingViewState>(Data)
    val state = _state.asStateFlow()

    private val navigator: AndroidNavigator by genInject()
    private val greetingsIsShowedUseCase: GreetingsIsShowedUseCase by genInject()

    fun startClicked() {
        greetingsIsShowedUseCase.setIsShowed(true)
        navigator.reset(AppScreen.TabNavigationControllerScreen)
    }
}