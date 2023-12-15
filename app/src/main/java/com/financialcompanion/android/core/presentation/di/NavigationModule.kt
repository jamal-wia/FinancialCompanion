package com.financialcompanion.android.core.presentation.di

import com.jamal_aliev.navigationcontroller.navigator.NavigationControllerHolder
import org.koin.dsl.module

fun navigationModule() = module {
    factory { NavigationControllerHolder.requireNavigator() }
}