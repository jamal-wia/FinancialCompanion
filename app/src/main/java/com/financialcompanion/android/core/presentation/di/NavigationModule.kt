package com.financialcompanion.android.core.presentation.di

import com.financialcompanion.android.core.presentation.navigation.AppNavigationFactory
import com.jamal_aliev.navigationcontroller.navigator.NavigationControllerHolder
import org.koin.dsl.module

fun navigationModule() = module {
    single {
        NavigationControllerHolder.createNavigator(AppNavigationFactory())
        NavigationControllerHolder.requireNavigator()
    }
}