package it.airgap.tezos.michelson.internal.utils

internal fun <T> List<T>.second(): T =
    if (size < 2) throw NoSuchElementException("List does not have enough elements.")
    else this[1]

internal fun <T> List<T>.third(): T =
    if (size < 3) throw NoSuchElementException("List does not have enough elements.")
    else this[2]

internal inline fun <reified R> List<*>.firstInstanceOfOrNull(): R? = nthInstanceOfOrNull(1)
internal inline fun <reified R> List<*>.secondInstanceOfOrNull(): R? = nthInstanceOfOrNull(2)

private inline fun <reified R> List<*>.nthInstanceOfOrNull(n: Int): R? {
    var counter = 0
    for (item in this) {
        if (item is R) {
            if (++counter == n) return item
        }
    }

    return null
}