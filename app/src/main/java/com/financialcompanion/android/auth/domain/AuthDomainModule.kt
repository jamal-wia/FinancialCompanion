package com.financialcompanion.android.auth.domain

import com.financialcompanion.android.auth.domain.usecase.UserIsAuthorizationUseCase
import org.koin.dsl.module

fun authDomainModule() = module {
    factory { UserIsAuthorizationUseCase() }
}