package it.airgap.tezos.rpc.internal.utils

internal inline fun <T, K> Iterable<T>.groupMutableBy(keySelector: (T) -> K): Map<K, MutableList<T>> =
    groupByTo(mutableMapOf(), keySelector)