package com.financialcompanion.android

import android.app.Application
import com.financialcompanion.android.core.domain.di.allModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Thread.currentThread().priority = Thread.MAX_PRIORITY
        initKoin()
        initTimber()
    }

    private fun initKoin() {
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@App)
            modules(allModules())
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}