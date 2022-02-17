package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

// TODO: write annotation processor for converters?
@InternalTezosSdkApi
public interface Converter<in T, out S> {
    public fun convert(value: T): S
}