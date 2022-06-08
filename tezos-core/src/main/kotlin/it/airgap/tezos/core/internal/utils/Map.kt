package it.airgap.tezos.core.internal.utils

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import java.lang.ref.WeakReference

@InternalTezosSdkApi
public interface MapUtilsContext {
    public fun <K, V> MutableMap<K, WeakReference<V>>.getOrPutWeak(key: K, defaultValue: () -> V): V =
        this[key]?.get() ?: defaultValue().also { put(key, WeakReference(it)) }
}