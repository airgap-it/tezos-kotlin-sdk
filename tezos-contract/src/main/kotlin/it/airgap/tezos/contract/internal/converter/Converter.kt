package it.airgap.tezos.contract.internal.converter

import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.ConfigurableConverter
import it.airgap.tezos.michelson.micheline.Micheline

// -- TypedConverter --

@InternalTezosSdkApi
public interface TypedConverter<in T, out S> : ConfigurableConverter<T, S, TypedConverter.Configuration> {
    public fun convert(value: T, type: Micheline): S
    override fun convert(value: T, configuration: Configuration): S = convert(value, configuration.type)

    public data class Configuration(val type: Micheline)
}
