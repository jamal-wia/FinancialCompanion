package com.financialcompanion.android.core.domain.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.get

private const val IO = "io"
private const val UI = "ui"
private const val DEFAULT = "default"
private const val APPLICATION_SCOPE = "application_scope"

fun coroutineModule() = module {
    single(named(IO)) { Dispatchers.IO }
    single(named(UI)) { Dispatchers.Main } bind CoroutineDispatcher::class
    single(named(DEFAULT)) { Dispatchers.Default }
    single(named(APPLICATION_SCOPE)) { CoroutineScope(SupervisorJob() + getDefaultDispatcher()) }
    single(named(APPLICATION_SCOPE)) { hashMapOf<Any, Job>() }
}

private fun getCoroutineDispatcher(name: String): CoroutineDispatcher =
    get(CoroutineDispatcher::class.java, named(name))


fun getIoDispatcher() = getCoroutineDispatcher(IO)

fun injectIoDispatcher(): Lazy<CoroutineDispatcher> {
    return lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        getIoDispatcher()
    }
}

fun getUiDispatcher() = getCoroutineDispatcher(UI)

fun injectUiDispatcher(): Lazy<CoroutineDispatcher> {
    return lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        getUiDispatcher()
    }
}

fun getDefaultDispatcher() = getCoroutineDispatcher(DEFAULT)

fun injectDefaultDispatcher(): Lazy<CoroutineDispatcher> {
    return lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        getDefaultDispatcher()
    }
}

fun getApplicationScope(): CoroutineScope =
    get(CoroutineScope::class.java, named(APPLICATION_SCOPE))

fun injectApplicationScope(): Lazy<CoroutineScope> {
    return lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        getApplicationScope()
    }
}

fun getApplicationCoroutinePool(): HashMap<Any, Job> =
    get(HashMap::class.java, named(APPLICATION_SCOPE))

fun injectApplicationCoroutinePool(): Lazy<HashMap<Any, Job>> {
    return lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        getApplicationCoroutinePool()
    }
}