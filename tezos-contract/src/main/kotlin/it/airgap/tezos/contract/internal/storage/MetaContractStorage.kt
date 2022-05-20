package it.airgap.tezos.contract.internal.storage

import it.airgap.tezos.contract.internal.converter.MichelineToStorageEntryConverter
import it.airgap.tezos.contract.internal.converter.TypedConverter
import it.airgap.tezos.contract.internal.converter.toStorageEntry
import it.airgap.tezos.contract.storage.ContractStorageEntry
import it.airgap.tezos.core.coder.encoded.decodeFromBytes
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.michelson.converter.toCompactExpression
import it.airgap.tezos.michelson.internal.packer.Packer
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.packer.packToBytes
import it.airgap.tezos.rpc.internal.cache.Cached

// -- MetaContractStorage --

internal class MetaContractStorage(
    private val type: MichelineNode,
    private val michelineToStorageEntryConverter: TypedConverter<MichelineNode, ContractStorageEntry>,
) {
    fun entryFrom(value: MichelineNode): ContractStorageEntry = value.toStorageEntry(type, michelineToStorageEntryConverter)

    class Factory(private val michelineToStorageEntryConverter: TypedConverter<MichelineNode, ContractStorageEntry>) {
        fun create(type: MichelineNode): MetaContractStorage = MetaContractStorage(type, michelineToStorageEntryConverter)
    }
}

internal typealias LazyMetaContractStorage = Cached<MetaContractStorage>

// -- MetaContractStorageEntry --

internal sealed interface MetaContractStorageEntry {
    val type: MichelineNode

    class Basic(override val type: MichelineNode) : MetaContractStorageEntry

    class BigMap(
        override val type: MichelineNode,
        private val encodedBytesCoder: EncodedBytesCoder,
        private val michelinePacker: Packer<MichelineNode>,
        private val michelineToStorageEntryConverter: MichelineToStorageEntryConverter,
        private val michelineToCompactStringConverter: Converter<MichelineNode, String>,
    ) : MetaContractStorageEntry {

        fun scriptExpr(key: MichelineNode): ScriptExprHash {
            if (type !is MichelinePrimitiveApplication || type.args.size != 2) failWithInvalidType(type)
            val keyBytes = key.packToBytes(type.args.first(), michelinePacker)

            return ScriptExprHash.decodeFromBytes(keyBytes, encodedBytesCoder)
        }

        fun entryFrom(value: MichelineNode): ContractStorageEntry {
            if (type !is MichelinePrimitiveApplication || type.args.size != 2) failWithInvalidType(type)

            return value.toStorageEntry(type.args[1], michelineToStorageEntryConverter)
        }

        private fun failWithInvalidType(type: MichelineNode): Nothing =
            failWithIllegalArgument("Micheline type ${type.toCompactExpression(michelineToCompactStringConverter)} is invalid.")
    }
}