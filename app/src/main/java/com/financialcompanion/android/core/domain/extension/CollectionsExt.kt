package com.financialcompanion.android.core.domain.extension

fun <K, V> Map<K, V>.first(): Map.Entry<K, V> {
    return this.first { _, _ -> true }
}

fun <K, V> Map<K, V>.firstOrNull(): Map.Entry<K, V>? {
    return this.firstOrNull { _, _ -> true }
}

inline fun <K, V> Map<K, V>.first(predicate: (key: K, value: V) -> Boolean): Map.Entry<K, V> {
    return firstOrNull(predicate)
        ?: throw NoSuchElementException("No element in this map by this predicate")
}

inline fun <K, V> Map<K, V>.firstOrNull(predicate: (key: K, value: V) -> Boolean): Map.Entry<K, V>? {
    val iterator = this.iterator()
    while (iterator.hasNext()) {
        val next = iterator.next()
        if (predicate(next.key, next.value))
            return next
    }
    return null
}


fun <K, V> Map<K, V>.last(): Map.Entry<K, V> {
    return lastOrNull()
        ?: throw NoSuchElementException("No element in this map by this predicate")
}

fun <K, V> Map<K, V>.lastOrNull(): Map.Entry<K, V>? {
    val iterator = this.iterator()
    var result: Map.Entry<K, V>? = null
    while (iterator.hasNext()) {
        result = iterator.next()
    }
    return result
}

inline fun <K, V> Map<K, V>.last(predicate: (key: K, value: V) -> Boolean): Map.Entry<K, V> {
    return lastOrNull(predicate)
        ?: throw NoSuchElementException("No element in this map by this predicate")
}

inline fun <K, V> Map<K, V>.lastOrNull(predicate: (key: K, value: V) -> Boolean): Map.Entry<K, V>? {
    val iterator = this.iterator()
    var result: Map.Entry<K, V>? = null
    while (iterator.hasNext()) {
        val next = iterator.next()
        if (predicate(next.key, next.value))
            result = next
    }
    return result
}


infix fun <K, V> Map<K, V>.fromTo(collection: MutableCollection<V>): MutableCollection<V> {
    return fromTo(collection) { entry -> entry.value }
}

inline fun <K, V> Map<K, V>.fromTo(
    collection: MutableCollection<V>,
    transform: (Map.Entry<K, V>) -> V,
): MutableCollection<V> {
    if (isEmpty()) return collection
    val iterator = this.iterator()
    while (iterator.hasNext()) {
        collection.add(transform(iterator.next()))
    }
    return collection
}

inline fun <K, V> MutableMap<K, V>.transformAllValue(transform: (K, V) -> V) {
    if (isEmpty()) return
    val iterator = this.keys.toList().iterator()
    while (iterator.hasNext()) {
        val key = iterator.next()
        this[key] = transform(key, this.getValue(key))
    }
}


fun <T> MutableList<T>.swap(obj1: T, obj2: T): Boolean {
    if (this.size < 2) return false
    var index1 = -1
    var index2 = -1

    val startIterator = this.listIterator()
    var startIteratorIndex = -1
    val endIterator = this.listIterator()
    var endIteratorIndex = this.size

    while (startIterator.hasNext() && endIterator.hasPrevious()) {
        val next = startIterator.next()
        startIteratorIndex++
        val previous = endIterator.previous()
        endIteratorIndex--
        if (startIteratorIndex > endIteratorIndex) break

        if (next == obj1) index1 = startIteratorIndex
        if (next == obj2) index2 = startIteratorIndex
        if (index1 != -1 && index2 != -1) break

        if (previous == obj1) index1 = endIteratorIndex
        if (previous == obj2) index2 = endIteratorIndex
        if (index1 != -1 && index2 != -1) break
    }

    if (index1 == -1 || index2 == -1) return false
    this[index1] = obj2
    this[index2] = obj1
    return true
}

inline fun <T> List<T>.indexOfFirstOrLast(predicate: (T) -> Boolean): Int {
    if (this.isEmpty()) return -1

    val startIterator = this.listIterator()
    var startIteratorIndex = -1
    val endIterator = this.listIterator()
    var endIteratorIndex = this.size

    while (startIterator.hasNext() && endIterator.hasPrevious()) {
        val next = startIterator.next()
        startIteratorIndex++
        val previous = endIterator.previous()
        endIteratorIndex--
        if (startIteratorIndex > endIteratorIndex) return -1

        if (predicate(next)) return startIteratorIndex
        else if (predicate(previous)) return endIteratorIndex
    }

    return -1
}

inline fun <T> MutableList<T>.replaceFirstIf(
    item: MutableList<T>.(currentElement: T) -> T,
    predicate: (T) -> Boolean,
): Boolean {
    val index = this.indexOfFirst(predicate)
    if (index == -1) return false
    this[index] = item(this[index])
    return true
}

inline fun <T> MutableList<T>.removeFirstIf(predicate: (T) -> Boolean): Boolean {
    val index = this.indexOfFirst(predicate)
    if (index == -1) return false
    this.removeAt(index)
    return true
}

fun <T, E> List<T>.cast(): List<E> = this as List<E>

inline fun <reified T> Any.cast(): T = this as T
inline fun <reified T> Any.castOrNull(): T? = this as? T
