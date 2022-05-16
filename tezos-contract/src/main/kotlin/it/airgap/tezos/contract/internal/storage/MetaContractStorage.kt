package it.airgap.tezos.contract.internal.storage

import it.airgap.tezos.contract.internal.converter.MichelineToStorageEntryConverter
import it.airgap.tezos.contract.internal.converter.toStorageEntry
import it.airgap.tezos.contract.storage.ContractStorageEntry
import it.airgap.tezos.core.decodeFromBytes
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.michelson.internal.converter.MichelineToCompactStringConverter
import it.airgap.tezos.michelson.internal.packer.MichelinePacker
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.packToBytes
import it.airgap.tezos.michelson.toCompactExpression

// -- MetaContractStorage --

internal class MetaContractStorage(
    private val type: MichelineNode,
    private val michelineToStorageEntryConverter: MichelineToStorageEntryConverter,
) {
    fun entryFrom(value: MichelineNode): ContractStorageEntry = value.toStorageEntry(type, michelineToStorageEntryConverter)
}

// -- MetaContractStorageEntry --

internal sealed interface MetaContractStorageEntry {
    val type: MichelineNode

    class Basic(override val type: MichelineNode) : MetaContractStorageEntry

    class BigMap(
        override val type: MichelineNode,
        private val encodedBytesCoder: EncodedBytesCoder,
        private val michelinePacker: MichelinePacker,
        private val michelineToStorageEntryConverter: MichelineToStorageEntryConverter,
        private val michelineToCompactStringConverter: MichelineToCompactStringConverter,
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