package com.financialcompanion.android.category.domain

import com.financialcompanion.android.category.domain.usecase.GetAllCategoriesUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun categoryDomainModule() = module {
    factoryOf(::GetAllCategoriesUseCase)
}