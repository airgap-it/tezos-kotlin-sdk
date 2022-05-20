package it.airgap.tezos.core.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi

@InternalTezosSdkApi
public fun interface ConfigurableConverter<in T, out S, in C> {
    public fun convert(value: T, configuration: C): S
}

@InternalTezosSdkApi
public fun interface Converter<in T, out S> : ConfigurableConverter<T, S, Unit> {
    public fun convert(value: T): S

    override fun convert(value: T, configuration: Unit): S = convert(value)
}