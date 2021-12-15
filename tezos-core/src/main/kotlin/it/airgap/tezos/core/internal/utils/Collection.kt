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