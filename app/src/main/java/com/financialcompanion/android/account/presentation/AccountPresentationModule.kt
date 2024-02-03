package com.financialcompanion.android.account.presentation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun accountPresentationModule() = module {
    viewModel { AccountViewModel() }
}