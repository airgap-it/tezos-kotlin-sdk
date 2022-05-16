package it.airgap.tezos.contract.internal.converter

import it.airgap.tezos.contract.internal.storage.MetaContractStorageEntry
import it.airgap.tezos.contract.storage.ContractStorageEntry
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.converter.ConfigurableConverter
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.fromStringOrNull
import it.airgap.tezos.michelson.internal.converter.MichelineToCompactStringConverter
import it.airgap.tezos.michelson.internal.converter.StringToMichelsonPrimConverter
import it.airgap.tezos.michelson.internal.packer.MichelinePacker
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.michelson.toCompactExpression
import it.airgap.tezos.rpc.active.block.Block

internal class MichelineToStorageEntryConverter(
    private val rpc: Block,
    private val encodedBytesCoder: EncodedBytesCoder,
    private val michelinePacker: MichelinePacker,
    private val michelineToCompactStringConverter: MichelineToCompactStringConverter,
    private val stringToMichelsonPrimConverter: StringToMichelsonPrimConverter,
) : ConfigurableConverter<MichelineNode, ContractStorageEntry, MichelineToStorageEntryConverter.Configuration> {
    override fun convert(value: MichelineNode, configuration: Configuration): ContractStorageEntry = with(configuration) {
        when (type) {
            is MichelinePrimitiveApplication -> createStorageEntry(type, value)
            else -> failWithInvalidType(type)
        }
    }

    private fun createStorageEntry(type: MichelinePrimitiveApplication, value: MichelineNode): ContractStorageEntry =
        when (val prim = Michelson.Prim.fromStringOrNull(type.prim.value, stringToMichelsonPrimConverter)) {
            is MichelsonType.Prim -> when {
                prim == MichelsonType.BigMap && value is MichelineLiteral.Integer -> createBigMapStorageEntry(type, value)
                (prim == MichelsonType.List || prim == MichelsonType.Set) && value is MichelineSequence -> createSequenceStorageEntry(type, value)
                prim == MichelsonType.Lambda && value is MichelineSequence -> createLambdaStorageEntry(type, value)
                prim == MichelsonType.Map && value is MichelineSequence -> createMapStorageEntry(type, value)
                type.args.isEmpty() -> createValueStorageEntry(type, value)
                value is MichelinePrimitiveApplication -> createObjectStorageEntry(type, value)
                else -> failWithTypeValueMismatch(type, value)
            }
            else -> failWithInvalidType(type)
        }

    private fun createBigMapStorageEntry(type: MichelinePrimitiveApplication, value: MichelineLiteral.Integer): ContractStorageEntry =
        ContractStorageEntry.BigMap(
            value.int,
            value,
            MetaContractStorageEntry.BigMap(
                type,
                encodedBytesCoder,
                michelinePacker,
                this,
                michelineToCompactStringConverter,
            ),
            rpc.context.bigMaps,
        )

    private fun createSequenceStorageEntry(type: MichelinePrimitiveApplication, value: MichelineSequence): ContractStorageEntry {
        if (type.args.size != 1) failWithInvalidType(type)

        return ContractStorageEntry.Sequence(
            value,
            MetaContractStorageEntry.Basic(type),
            value.nodes.map { convert(it, Configuration(type.args.first())) },
        )
    }

    private fun createLambdaStorageEntry(type: MichelinePrimitiveApplication, value: MichelineSequence): ContractStorageEntry =
        ContractStorageEntry.Sequence(
            value,
            MetaContractStorageEntry.Basic(type),
            value.nodes.map { convert(it, Configuration(type)) },
        )

    private fun createMapStorageEntry(type: MichelinePrimitiveApplication, value: MichelineSequence): ContractStorageEntry {
        if (type.args.size != 2) failWithInvalidType(type)

        return ContractStorageEntry.Map(
            value,
            MetaContractStorageEntry.Basic(type),
            value.nodes.associate {
                convert(it, Configuration(type.args[0])) to convert(it, Configuration(type.args[1]))
            },
        )
    }

    private fun createValueStorageEntry(type: MichelinePrimitiveApplication, value: MichelineNode): ContractStorageEntry =
        ContractStorageEntry.Value(value, MetaContractStorageEntry.Basic(type))

    private fun createObjectStorageEntry(type: MichelinePrimitiveApplication, value: MichelinePrimitiveApplication): ContractStorageEntry {
        if (type.args.size != value.args.size) failWithInvalidType(type)

        return ContractStorageEntry.Object(
            value,
            MetaContractStorageEntry.Basic(type),
            value.args.zip(type.args).flatMap { (v, t) ->
                val entry = convert(v, Configuration(t))

                if (entry is ContractStorageEntry.Object && entry.names.isEmpty()) entry.elements
                else listOf(entry)
            },
        )
    }

    private fun failWithInvalidType(type: MichelineNode): Nothing =
        failWithIllegalArgument("Micheline type ${type.toCompactExpression(michelineToCompactStringConverter)} is invalid.")

    private fun failWithTypeValueMismatch(type: MichelineNode, value: MichelineNode): Nothing =
        failWithIllegalArgument("Micheline type ${type.toCompactExpression(michelineToCompactStringConverter)} and value ${value.toCompactExpression(michelineToCompactStringConverter)} mismatch.")

    data class Configuration(val type: MichelineNode)
}