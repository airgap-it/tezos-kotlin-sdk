package it.airgap.tezos.rpc.internal.cache

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.rpc.http.HttpHeader

// -- Cached --

@InternalTezosSdkApi
public class Cached<T>(private val fetch: suspend (List<HttpHeader>) -> T) {
    private var value: T? = null

    public suspend fun get(headers: List<HttpHeader> = emptyList()): T = value ?: fetch(headers).also { value = it }
    public fun <R> map(transform: (T) -> R): Cached<R> = Cached { headers -> transform(value ?: fetch(headers)) }
    public fun <S> combine(other: Cached<S>): Cached<Pair<T, S>> = Cached { headers -> Pair(get(headers), other.get(headers)) }
}

// -- CachedMap --

@InternalTezosSdkApi
public class CachedMap<K, V>(private val fetch: suspend (K, List<HttpHeader>) -> V) {
    private val values: MutableMap<K, V> = mutableMapOf()

    public suspend fun get(key: K, headers: List<HttpHeader> = emptyList()): V = values.getOrPut(key) { fetch(key, headers) }
}
