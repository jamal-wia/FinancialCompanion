package com.financialcompanion.android.category.presentation

import com.financialcompanion.android.category.presentation.categories.CategoriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun categoryPresentationModule() = module {
    viewModel { CategoriesViewModel() }
}