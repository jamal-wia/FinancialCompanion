package com.financialcompanion.android

import android.app.Application
import com.chibatching.kotpref.Kotpref
import com.financialcompanion.android.core.domain.di.allModules
import com.financialcompanion.android.core.presentation.navigation.AppNavigationFactory
import com.jamal_aliev.navigationcontroller.navigator.NavigationControllerHolder
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
        initNavigationController()
        initKotpref()
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

    private fun initNavigationController() {
        NavigationControllerHolder.createNavigator(AppNavigationFactory())
    }

    private fun initKotpref() {
        Kotpref.init(this)
    }
}