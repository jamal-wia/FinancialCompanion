package com.financialcompanion.android.core.domain.di

import com.financialcompanion.android.auth.domain.authDomainModule
import com.financialcompanion.android.auth.presentation.authPresentationModule
import com.financialcompanion.android.core.presentation.di.navigationModule
import com.financialcompanion.android.greetings.domain.greetingDomainModule
import com.financialcompanion.android.greetings.presentation.greetingsPresentationModule

fun allModules() = listOf(
    appModule(),
    navigationModule(),
    coroutineModule(),

    greetingDomainModule(),
    greetingsPresentationModule(),

    authDomainModule(),
    authPresentationModule()
)