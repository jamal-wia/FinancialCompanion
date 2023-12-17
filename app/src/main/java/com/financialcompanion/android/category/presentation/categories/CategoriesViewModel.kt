package com.financialcompanion.android.category.presentation.categories

import com.financialcompanion.android.category.data.CategoriesHolder
import com.financialcompanion.android.category.presentation.categories.CategoriesViewState.DataState
import com.financialcompanion.android.category.presentation.categories.CategoriesViewState.LoadingState
import com.financialcompanion.android.core.presentation.mvvm.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CategoriesViewModel : BaseViewModel() {

    private val _state = MutableStateFlow<CategoriesViewState>(LoadingState)
    val state = _state.asStateFlow()

    init {
        _state.update { DataState(CategoriesHolder.categories) }
    }
}