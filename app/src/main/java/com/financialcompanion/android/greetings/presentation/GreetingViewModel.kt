package com.financialcompanion.android.greetings.presentation

import com.financialcompanion.android.core.presentation.mvvm.BaseViewModel
import com.financialcompanion.android.greetings.presentation.GreetingViewState.Data
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GreetingViewModel : BaseViewModel() {
    private val _state = MutableStateFlow<GreetingViewState>(Data)
    val state = _state.asStateFlow()
}