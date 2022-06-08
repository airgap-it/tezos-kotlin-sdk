package it.airgap.tezos.contract.internal.converter

import it.airgap.tezos.contract.storage.ContractStorageEntry
import it.airgap.tezos.michelson.micheline.MichelineNode

// -- MichelineNode -> ContractStorageEntry --

internal fun MichelineNode.toStorageEntry(type: MichelineNode, michelineToStorageEntryConverter: TypedConverter<MichelineNode, ContractStorageEntry>): ContractStorageEntry =
    michelineToStorageEntryConverter.convert(this, type)