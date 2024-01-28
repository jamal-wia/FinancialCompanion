package com.financialcompanion.android.auth.presentation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun authPresentationModule() = module {
    viewModel { AuthViewModel() }
}