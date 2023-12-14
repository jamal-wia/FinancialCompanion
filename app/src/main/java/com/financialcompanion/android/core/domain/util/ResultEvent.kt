package com.financialcompanion.android.core.domain.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onCompletion

/**
 * @author Jamal Aliev (aliev.djamal.2000@gmail.com)
 * */
abstract class ResultEvent<T>(
    override val identifier: Any = Math.random(),
    override val identifierType: Identification.Type = ResultIdentifier,
) : Identification {

    private val flows = hashMapOf<Any, MutableSharedFlow<T>>()

    fun flow(tagObserver: Any): Flow<T> {
        return flows.getOrPut(tagObserver) { MutableSharedFlow(replay = 1) }
            .asSharedFlow()
            .onCompletion { flows.remove(tagObserver) }
    }

    open suspend fun emit(value: T) {
        flows.keys.toList()
            .forEach { emit(it, value) }
    }

    open suspend fun emit(targetTagObserver: Any?, value: T) {
        if (targetTagObserver == null) return emit(value)
        flows[targetTagObserver]?.emit(value)
    }

    open suspend fun emit(excludeTagObservers: List<Any?>, value: T) {
        flows.keys.toList()
            .filterNot { excludeTagObservers.contains(it) }
            .forEach { emit(it, value) }
    }

    open fun tryEmit(value: T): Boolean {
        var success = false
        flows.keys.toList()
            .forEach {
                if (tryEmit(it, value))
                    success = true
            }
        return success
    }

    open fun tryEmit(targetTagObserver: Any?, value: T): Boolean {
        if (targetTagObserver == null) return tryEmit(value)
        return flows[targetTagObserver]?.tryEmit(value) ?: false
    }

    open fun tryEmit(excludeTagObservers: List<Any?>, value: T): Boolean {
        var success = false
        flows.keys.toList()
            .filterNot { excludeTagObservers.contains(it) }
            .forEach {
                if (tryEmit(it, value))
                    success = true
            }
        return success
    }

    override fun hashCode(): Int {
        return identifier.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is ResultEvent<*>) return false
        return this.hashCode() == other.hashCode()
                && this.identifierType == other.identifierType
    }

    private companion object {
        private object ResultIdentifier : Identification.Type
    }
}

object Events {

}
