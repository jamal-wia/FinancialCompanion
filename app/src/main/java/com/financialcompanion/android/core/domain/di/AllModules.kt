package com.financialcompanion.android.core.domain.di

import com.financialcompanion.android.category.data.categoryDataModule
import com.financialcompanion.android.category.domain.categoryDomainModule
import com.financialcompanion.android.category.presentation.categoryPresentationModule
import com.financialcompanion.android.auth.domain.authDomainModule
import com.financialcompanion.android.auth.presentation.authPresentationModule
import com.financialcompanion.android.core.presentation.di.navigationModule
import com.financialcompanion.android.greetings.domain.greetingDomainModule
import com.financialcompanion.android.greetings.presentation.greetingsPresentationModule

fun allModules() = listOf(
    appModule(),
    navigationModule(),
    coroutineModule(),

    categoryDataModule(),
    categoryDomainModule(),
    categoryPresentationModule(),

    greetingDomainModule(),
    greetingsPresentationModule(),
    greetingsPresentationModule(),

    authDomainModule(),
    authPresentationModule()
)