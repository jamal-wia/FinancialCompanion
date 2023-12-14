package com.financialcompanion.android.core.domain.di

import android.content.Context
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.get

fun appModule() = module {

}

fun getApplicationContext(): Context = get(Context::class.java)