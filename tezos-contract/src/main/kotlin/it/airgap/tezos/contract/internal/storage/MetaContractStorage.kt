package it.airgap.tezos.contract.internal.storage

import it.airgap.tezos.contract.internal.context.TezosContractContext.failWithIllegalArgument
import it.airgap.tezos.contract.internal.context.TezosContractContext.normalized
import it.airgap.tezos.contract.internal.context.TezosContractContext.packToScriptExpr
import it.airgap.tezos.contract.internal.context.TezosContractContext.toCompactExpression
import it.airgap.tezos.contract.internal.converter.MichelineToStorageEntryConverter
import it.airgap.tezos.contract.internal.converter.TypedConverter
import it.airgap.tezos.contract.internal.converter.toStorageEntry
import it.airgap.tezos.contract.storage.ContractStorageEntry
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.normalizer.Normalizer
import it.airgap.tezos.core.type.encoded.ScriptExprHash
import it.airgap.tezos.michelson.internal.packer.Packer
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.rpc.internal.cache.Cached

// -- MetaContractStorage --

internal class MetaContractStorage(
    private val type: Micheline,
    private val michelineToStorageEntryConverter: TypedConverter<Micheline, ContractStorageEntry>,
) {
    fun entryFrom(value: Micheline): ContractStorageEntry = value.toStorageEntry(type, michelineToStorageEntryConverter)

    class Factory(private val michelineToStorageEntryConverter: TypedConverter<Micheline, ContractStorageEntry>) {
        fun create(type: Micheline): MetaContractStorage = MetaContractStorage(type, michelineToStorageEntryConverter)
    }
}

internal typealias LazyMetaContractStorage = Cached<MetaContractStorage>

// -- MetaContractStorageEntry --

internal sealed interface MetaContractStorageEntry {
    val type: Micheline

    class Basic(override val type: Micheline) : MetaContractStorageEntry

    class BigMap(
        override val type: Micheline,
        private val michelineToScriptExprHashPacker: Packer<Micheline, ScriptExprHash>,
        private val michelineToStorageEntryConverter: MichelineToStorageEntryConverter,
        private val michelineToCompactStringConverter: Converter<Micheline, String>,
        private val michelineNormalizer: Normalizer<Micheline>,
    ) : MetaContractStorageEntry {

        fun scriptExpr(key: Micheline): ScriptExprHash {
            if (type !is MichelinePrimitiveApplication || type.args.size != 2) failWithInvalidType(type)
            return key.packToScriptExpr(type.args.first(), michelineToScriptExprHashPacker)
        }

        fun entryFrom(value: Micheline): ContractStorageEntry {
            if (type !is MichelinePrimitiveApplication || type.args.size != 2) failWithInvalidType(type)

            return value.normalized(michelineNormalizer).toStorageEntry(type.args[1], michelineToStorageEntryConverter)
        }

        private fun failWithInvalidType(type: Micheline): Nothing =
            failWithIllegalArgument("Micheline type ${type.toCompactExpression(michelineToCompactStringConverter)} is invalid.")
    }
}