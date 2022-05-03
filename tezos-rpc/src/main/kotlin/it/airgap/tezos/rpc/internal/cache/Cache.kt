package it.airgap.tezos.rpc.internal.cache

import it.airgap.tezos.rpc.http.HttpHeader

internal class Cache<K, V>(private val fetch: suspend (K, List<HttpHeader>) -> V) {
    private val values: MutableMap<K, V> = mutableMapOf()

    suspend fun get(key: K, headers: List<HttpHeader> = emptyList()): V = values.getOrPut(key) { fetch(key, headers) }
}