package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import kotlin.math.abs

@InternalTezosSdkApi
public inline fun <reified T> Collection<*>.allIsInstance(): Boolean = all { it is T }

@InternalTezosSdkApi
public inline fun <reified T> Collection<*>.anyIsInstance(): Boolean = any { it is T }

@InternalTezosSdkApi
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

@InternalTezosSdkApi
public fun <T> MutableList<T>.consumeAt(index: Int): T? = if (index > lastIndex) null else this[index].also { removeAt(index) }

@InternalTezosSdkApi
public fun <T> MutableList<T>.consumeUntil(index: Int): MutableList<T> = consumeAt(0 until index)

@InternalTezosSdkApi
public fun <T> MutableList<T>.consumeAt(indices: IntRange): MutableList<T> = mutableListOf<T>().also {
    for (i in indices) {
        consumeAt(indices.first)?.let(it::add) ?: break
    }
}

@InternalTezosSdkApi
public fun <T> MutableList<T>.consume(predicate: (T) -> Boolean): T? = firstOrNull(predicate).also { remove(it) }

@InternalTezosSdkApi
public fun <T> List<T>.tail(): List<T> {
    val n = if (isNotEmpty()) size - 1 else 0
    return takeLast(n)
}

@InternalTezosSdkApi
public fun <T> List<T>.replacingAt(index: Int, newElement: T): List<T> =
    if (index > lastIndex || index < 0) this
    else slice(0 until index) + newElement + slice(index + 1 until size)

@InternalTezosSdkApi
public fun <T : Comparable<T>> List<T>.startsWith(elements: List<T>): Boolean =
    slice(elements.indices) == elements

@InternalTezosSdkApi
public fun List<Byte>.startsWith(bytes: ByteArray): Boolean =
    if (size < bytes.size) false
    else slice(bytes.indices).foldIndexed(true) { index, acc, byte -> acc && byte == bytes[index] }