package it.airgap.tezos.contract.internal.converter

import it.airgap.tezos.contract.storage.ContractStorageEntry
import it.airgap.tezos.michelson.micheline.MichelineNode

internal fun MichelineNode.toStorageEntry(type: MichelineNode, michelineToStorageEntryConverter: MichelineToStorageEntryConverter): ContractStorageEntry =
    michelineToStorageEntryConverter.convert(this, MichelineToStorageEntryConverter.Configuration(type))