package com.financialcompanion.android.core.domain.di

import com.financialcompanion.android.core.presentation.di.navigationModule
import com.financialcompanion.android.greetings.presentation.greetingsPresentationModule

fun allModules() = listOf(
    appModule(),
    navigationModule(),
    coroutineModule(),

    greetingsPresentationModule()
)