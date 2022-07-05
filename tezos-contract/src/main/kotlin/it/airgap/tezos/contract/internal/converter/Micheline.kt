package it.airgap.tezos.contract.internal.converter

import it.airgap.tezos.contract.storage.ContractStorageEntry
import it.airgap.tezos.michelson.micheline.Micheline

// -- MichelineNode -> ContractStorageEntry --

internal fun Micheline.toStorageEntry(type: Micheline, michelineToStorageEntryConverter: TypedConverter<Micheline, ContractStorageEntry>): ContractStorageEntry =
    michelineToStorageEntryConverter.convert(this, type)