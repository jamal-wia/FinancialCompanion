package com.financialcompanion.android.core.domain.di

import com.financialcompanion.android.category.data.categoryDataModule
import com.financialcompanion.android.category.domain.categoryDomainModule
import com.financialcompanion.android.category.presentation.categoryPresentationModule
import com.financialcompanion.android.core.presentation.di.navigationModule

fun allModules() = listOf(
    appModule(),
    navigationModule(),
    coroutineModule(),

    categoryDataModule(),
    categoryDomainModule(),
    categoryPresentationModule()
)