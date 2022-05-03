package it.airgap.tezos.rpc.internal.utils

internal fun <K, V> Map<K, MutableList<V>>.next(key: K): V? {
    val values = get(key) ?: return null
    return values.removeFirstOrNull()
}