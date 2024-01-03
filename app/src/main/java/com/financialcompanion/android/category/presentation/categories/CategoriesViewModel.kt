package com.financialcompanion.android.category.presentation.categories

import androidx.lifecycle.viewModelScope
import com.financialcompanion.android.category.domain.usecase.GetAllCategoriesUseCase
import com.financialcompanion.android.category.presentation.categories.CategoriesViewState.DataState
import com.financialcompanion.android.category.presentation.categories.CategoriesViewState.LoadingState
import com.financialcompanion.android.core.domain.extension.genInject
import com.financialcompanion.android.core.presentation.mvvm.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoriesViewModel : BaseViewModel() {

    private val _state = MutableStateFlow<CategoriesViewState>(LoadingState)
    val state = _state.asStateFlow()

    private val getAllCategoriesUseCase by genInject<GetAllCategoriesUseCase>()

    init {
        viewModelScope.launch {
            val categories = getAllCategoriesUseCase.invoke()
            _state.update { DataState(categories) }
        }
    }
}
