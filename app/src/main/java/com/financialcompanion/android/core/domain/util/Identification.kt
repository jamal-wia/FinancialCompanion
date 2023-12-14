package com.financialcompanion.android.core.domain.util

/**
 * @author Jamal Aliev (aliev.djamal.2000@gmail.com)
 */
interface Identification {

    val identifier: Any
    val identifierType: Type

    interface Type
}
