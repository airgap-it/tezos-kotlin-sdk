package it.airgap.tezos.rpc.internal.cache

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.rpc.http.HttpHeader

// -- Cached --

@InternalTezosSdkApi
public class Cached<V>(private val fetch: suspend (List<HttpHeader>) -> V) {
    private var value: V? = null

    public suspend fun get(headers: List<HttpHeader> = emptyList()): V = value ?: fetch(headers).also { value = it }
    public fun <R> map(transform: (V) -> R): Cached<R> = Cached { headers -> transform(value ?: fetch(headers)) }
}

// -- CachedMap --

@InternalTezosSdkApi
public class CachedMap<K, V>(private val fetch: suspend (K, List<HttpHeader>) -> V) {
    private val values: MutableMap<K, V> = mutableMapOf()

    public suspend fun get(key: K, headers: List<HttpHeader> = emptyList()): V = values.getOrPut(key) { fetch(key, headers) }
}