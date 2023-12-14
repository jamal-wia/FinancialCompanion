package com.financialcompanion.android.core.presentation.di

import android.content.Context
import org.koin.java.KoinJavaComponent.get

fun getApplicationContext(): Context = get(Context::class.java)