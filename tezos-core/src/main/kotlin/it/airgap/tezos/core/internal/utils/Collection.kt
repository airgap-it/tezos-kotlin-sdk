package it.airgap.tezos.core.internal.utils

import kotlin.math.abs

public inline fun <reified T> Collection<*>.allIsInstance(): Boolean = all { it is T }
public inline fun <reified T> Collection<*>.anyIsInstance(): Boolean = any { it is T }

public inline fun <reified T> MutableList<T?>.replaceOrAdd(index: Int, element: T) {
    if (index != 0 && index > lastIndex){
        addAll(arrayOfNulls<T>(abs(size - index)))
    }

    if (index > lastIndex) {
        add(element)
    } else {
        this[index] = element
    }
}

public fun <T> MutableList<T>.consumeAt(index: Int): T? = if (index > lastIndex) null else this[index].also { removeAt(index) }
public fun <T> MutableList<T>.consumeAt(indices: IntRange): List<T> = mutableListOf<T>().also {
    for (i in indices) {
        consumeAt(indices.first)?.let(it::add) ?: break
    }
}

public fun <T> List<T>.tail(): List<T> {
    val n = if (isNotEmpty()) size - 1 else 0
    return takeLast(n)
}

public fun <T> List<T>.replacingAt(index: Int, newElement: T): List<T> =
    if (index > lastIndex || index < 0) this
    else slice(0 until index) + newElement + slice(index + 1 until size)

public fun <T : Comparable<T>> List<T>.startsWith(elements: List<T>): Boolean =
    slice(elements.indices) == elements