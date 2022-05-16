package it.airgap.tezos.core.internal.normalizer

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

@InternalTezosSdkApi
public interface Normalizer<T> {
    public fun normalize(value: T): T
}