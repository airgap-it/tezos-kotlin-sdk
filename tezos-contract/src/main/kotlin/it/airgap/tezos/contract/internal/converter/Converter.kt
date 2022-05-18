package it.airgap.tezos.contract.internal.converter

import it.airgap.tezos.contract.entrypoint.ContractEntrypointArgument
import it.airgap.tezos.contract.storage.ContractStorageEntry
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.ConfigurableConverter
import it.airgap.tezos.michelson.micheline.MichelineNode

// -- TypedConverter --

@InternalTezosSdkApi
public interface TypedConverter<in T, out S> : ConfigurableConverter<T, S, TypedConverter.Configuration> {
    public fun convert(value: T, type: MichelineNode): S
    override fun convert(value: T, configuration: Configuration): S = convert(value, configuration.type)

    public data class Configuration(val type: MichelineNode)
}

// -- MichelineNode -> ContractStorageEntry --

internal fun MichelineNode.toStorageEntry(type: MichelineNode, michelineToStorageEntryConverter: TypedConverter<MichelineNode, ContractStorageEntry>): ContractStorageEntry =
    michelineToStorageEntryConverter.convert(this, type)

// -- ContractEntrypointArgument -> MichelineNode

internal fun ContractEntrypointArgument.toMicheline(type: MichelineNode, entrypointArgumentToMichelineConverter: TypedConverter<ContractEntrypointArgument, MichelineNode>): MichelineNode =
    entrypointArgumentToMichelineConverter.convert(this, type)