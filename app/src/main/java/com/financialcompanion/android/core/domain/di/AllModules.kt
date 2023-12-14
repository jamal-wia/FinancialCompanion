package com.financialcompanion.android.core.domain.di

import com.financialcompanion.android.core.presentation.di.navigationModule

fun allModules() = listOf(
    appModule(),
    navigationModule(),
    coroutineModule(),
)