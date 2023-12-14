package com.financialcompanion.android.core.domain.extension

import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.java.KoinJavaComponent

inline fun <reified T> genInject(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
): Lazy<T> {
    return lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        KoinJavaComponent.get(T::class.java, qualifier, parameters)
    }
}