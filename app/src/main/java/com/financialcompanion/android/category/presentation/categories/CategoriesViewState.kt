package com.financialcompanion.android.category.presentation.categories

import com.financialcompanion.android.category.domain.model.CategoryModel

sealed class CategoriesViewState {

    object LoadingState : CategoriesViewState()

    class DataState(val categories: List<CategoryModel>) : CategoriesViewState()

    object EmptyState : CategoriesViewState()

}
