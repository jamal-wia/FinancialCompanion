package com.financialcompanion.android.greetings.domain

import com.financialcompanion.android.greetings.domain.usecase.GreetingsIsShowedUseCase
import org.koin.dsl.module

fun greetingDomainModule() = module {
    factory { GreetingsIsShowedUseCase() }
}