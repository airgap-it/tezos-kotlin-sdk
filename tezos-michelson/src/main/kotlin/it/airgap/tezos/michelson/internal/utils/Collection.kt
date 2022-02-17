package it.airgap.tezos.michelson.internal.utils

internal fun <T> List<T>.second(): T =
    if (size < 2) throw NoSuchElementException("List does not have enough elements.")
    else this[1]

internal fun <T> List<T>.third(): T =
    if (size < 3) throw NoSuchElementException("List does not have enough elements.")
    else this[2]