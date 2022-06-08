package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import kotlin.math.abs
import kotlin.reflect.KClass

@InternalTezosSdkApi
public interface CollectionUtilsContext {
    public fun <T: Any> Collection<*>.allIsInstance(targetClass: KClass<T>): Boolean = all { targetClass.isInstance(it) }
    public fun <T: Any> Collection<*>.anyIsInstance(targetClass: KClass<T>): Boolean = any { targetClass.isInstance(it) }

    public fun <T: Any> Collection<*>.firstInstanceOfOrNull(targetClass: KClass<T>): T? = nthInstanceOfOrNull(1, targetClass)
    public fun <T: Any> Collection<*>.secondInstanceOfOrNull(targetClass: KClass<T>): T? = nthInstanceOfOrNull(2, targetClass)

    public fun <T : Any> MutableList<T?>.replaceOrAdd(index: Int, element: T) {
        if (index != 0 && index > lastIndex) {
            addAll(listOfNulls(abs(size - index)))
        }

        if (index > lastIndex) {
            add(element)
        } else {
            this[index] = element
        }
    }

    public fun <T> MutableList<T>.consumeAt(index: Int): T? = if (index > lastIndex) null else this[index].also { removeAt(index) }
    public fun <T> MutableList<T>.consumeUntil(index: Int): MutableList<T> = consumeAt(0 until index)
    public fun <T> MutableList<T>.consumeAt(indices: IntRange): MutableList<T> = mutableListOf<T>().also {
        for (i in indices) {
            consumeAt(indices.first)?.let(it::add) ?: break
        }
    }

    public fun <T> MutableList<T>.consume(predicate: (T) -> Boolean): T? = firstOrNull(predicate).also { remove(it) }
    public fun <T> MutableList<T>.consumeAll(predicate: (T) -> Boolean): List<T> = filter(predicate).also { removeAll(it) }

    public fun <T> List<T>.tail(): List<T> {
        val n = if (isNotEmpty()) size - 1 else 0
        return takeLast(n)
    }

    public fun <T> List<T>.replacingAt(index: Int, newElement: T): List<T> =
        if (index > lastIndex || index < 0) this
        else slice(0 until index) + newElement + slice(index + 1 until size)

    public fun <T : Comparable<T>> List<T>.startsWith(elements: List<T>): Boolean =
        slice(elements.indices) == elements

    public fun List<Byte>.startsWith(bytes: ByteArray): Boolean =
        if (size < bytes.size) false
        else slice(bytes.indices).foldIndexed(true) { index, acc, byte -> acc && byte == bytes[index] }

    public fun <K, V> List<Map<K, V>>.flatten(): Map<K, V> = fold(emptyMap()) { acc, map -> acc + map }

    public fun <T> Set<T>.containsAny(other: Set<T>): Boolean = any { other.contains(it) }
}

// -- private --

@Suppress("UNCHECKED_CAST")
private fun <T : Any> Collection<*>.nthInstanceOfOrNull(n: Int, targetClass: KClass<T>): T? {
    var counter = 0
    for (item in this) {
        if (targetClass.isInstance(item)) {
            if (++counter == n) return item as T
        }
    }

    return null
}

private fun <T> listOfNulls(size: Int): List<T?> = List(size) { null }