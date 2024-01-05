package com.financialcompanion.android.greetings.presentation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun greetingsPresentationModule() = module {
    viewModel { GreetingViewModel() }
}