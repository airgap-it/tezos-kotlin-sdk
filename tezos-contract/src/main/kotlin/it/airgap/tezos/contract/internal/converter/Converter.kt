package it.airgap.tezos.contract.internal.converter

import it.airgap.tezos.contract.entrypoint.ContractEntrypointArgument
import it.airgap.tezos.contract.storage.ContractStorageEntry
import it.airgap.tezos.michelson.micheline.MichelineNode

// -- MichelineNode -> ContractStorageEntry --

internal fun MichelineNode.toStorageEntry(type: MichelineNode, michelineToStorageEntryConverter: MichelineToStorageEntryConverter): ContractStorageEntry =
    michelineToStorageEntryConverter.convert(this, MichelineToStorageEntryConverter.Configuration(type))

// -- ContractEntrypointArgument -> MichelineNode

internal fun ContractEntrypointArgument.toMicheline(type: MichelineNode, entrypointArgumentToMichelineConverter: EntrypointArgumentToMichelineConverter): MichelineNode =
    entrypointArgumentToMichelineConverter.convert(this, EntrypointArgumentToMichelineConverter.Configuration(type))