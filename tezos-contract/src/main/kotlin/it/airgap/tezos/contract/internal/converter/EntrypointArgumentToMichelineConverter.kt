package it.airgap.tezos.contract.internal.converter

import it.airgap.tezos.contract.entrypoint.ContractEntrypointArgument
import it.airgap.tezos.contract.internal.entrypoint.MetaContractEntrypointArgument
import it.airgap.tezos.contract.internal.micheline.MichelineTrace
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.core.internal.utils.consume
import it.airgap.tezos.core.internal.utils.consumeAll
import it.airgap.tezos.core.internal.utils.consumeAt
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.MichelsonComparableType
import it.airgap.tezos.michelson.MichelsonData
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.comparator.isPrim
import it.airgap.tezos.michelson.converter.fromStringOrNull
import it.airgap.tezos.michelson.converter.toCompactExpression
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

internal class EntrypointArgumentToMichelineConverter(
    private val michelineToCompactStringConverter: Converter<MichelineNode, String>,
    private val stringToMichelsonPrimConverter: Converter<String, Michelson.Prim>,
) : TypedConverter<ContractEntrypointArgument, MichelineNode> {
    override fun convert(value: ContractEntrypointArgument, type: MichelineNode): MichelineNode {
        val meta = createMetaEntrypointArgument(type)
        return createMicheline(value, meta)
    }

    private fun createMicheline(value: ContractEntrypointArgument, meta: MetaContractEntrypointArgument): MichelineNode =
        when (meta) {
            is MetaContractEntrypointArgument.Value -> createValueMicheline(value, meta)
            is MetaContractEntrypointArgument.Object -> createObjectMicheline(value, meta)
            is MetaContractEntrypointArgument.Sequence -> createSequenceMicheline(value, meta)
            is MetaContractEntrypointArgument.Map -> createMapMicheline(value, meta)
        }

    private fun createValueMicheline(value: ContractEntrypointArgument, meta: MetaContractEntrypointArgument.Value): MichelineNode =
        when (value) {
            is ContractEntrypointArgument.Value -> value.value ?: failWithValueMetaMismatch(value, meta)
            is ContractEntrypointArgument.Object -> {
                val named = meta.findValue(value) ?: failWithValueMetaMismatch(value, meta)
                createMicheline(named, meta)
            }
            is ContractEntrypointArgument.Sequence, is ContractEntrypointArgument.Map -> failWithValueMetaMismatch(value, meta)
        }

    private fun createObjectMicheline(value: ContractEntrypointArgument, meta: MetaContractEntrypointArgument.Object): MichelineNode =
        when {
            meta.type.isPrim(MichelsonType.Pair) || meta.type.isPrim(MichelsonComparableType.Pair) -> createPairMicheline(value, meta)
            meta.type.isPrim(MichelsonType.Option) || meta.type.isPrim(MichelsonComparableType.Option) -> createOptionMicheline(value, meta)
            meta.type.isPrim(MichelsonType.Or) || meta.type.isPrim(MichelsonComparableType.Or) -> createOrMicheline(value, meta)
            else -> createSimpleObjectMicheline(value, meta)
        }

    private fun createSequenceMicheline(value: ContractEntrypointArgument, meta: MetaContractEntrypointArgument.Sequence): MichelineNode =
        when (value) {
            is ContractEntrypointArgument.Value -> value.sequenceOrNull() ?: failWithValueMetaMismatch(value, meta)
            is ContractEntrypointArgument.Object -> {
                val named = meta.findValue(value) ?: failWithValueMetaMismatch(value, meta)
                createMicheline(named, meta)
            }
            is ContractEntrypointArgument.Sequence -> {
                if (meta.elements.size != 1) failWithInvalidType(meta)

                MichelineSequence(value.elements.map { createMicheline(it, meta.elements.first()) })
            }
            is ContractEntrypointArgument.Map -> failWithValueMetaMismatch(value, meta)
        }

    private fun createMapMicheline(value: ContractEntrypointArgument, meta: MetaContractEntrypointArgument.Map): MichelineNode =
        when (value) {
            is ContractEntrypointArgument.Value -> value.sequenceOrNull(MichelsonData.Elt) ?: failWithValueMetaMismatch(value, meta)
            is ContractEntrypointArgument.Object -> {
                val named = meta.findValue(value) ?: failWithValueMetaMismatch(value, meta)
                createMicheline(named, meta)
            }
            is ContractEntrypointArgument.Map -> {
                MichelineSequence(
                    value.map.entries.map {
                        MichelinePrimitiveApplication(
                            MichelsonData.Elt,
                            args = listOf(
                                createMicheline(it.key, meta.key),
                                createMicheline(it.value, meta.value),
                            ),
                        )
                    },
                )
            }
            is ContractEntrypointArgument.Sequence -> failWithValueMetaMismatch(value, meta)
        }

    private fun createPairMicheline(value: ContractEntrypointArgument, meta: MetaContractEntrypointArgument.Object): MichelineNode =
        when (value) {
            is ContractEntrypointArgument.Value -> {
                value.primOrNull(MichelsonData.Pair) ?: run {
                    val namedMeta = value.findNextMeta(meta) ?: failWithValueMetaMismatch(value, meta)
                    createMicheline(value, namedMeta)
                }
            }
            is ContractEntrypointArgument.Object -> {
                if (meta.elements.size != 2) failWithInvalidType(meta)

                MichelinePrimitiveApplication(
                    MichelsonData.Pair,
                    args = listOf(
                        createArgMicheline(value, meta.elements[0]) ?: failWithValueMetaMismatch(value, meta),
                        createArgMicheline(value, meta.elements[1]) ?: failWithValueMetaMismatch(value, meta),
                    )
                )
            }
            is ContractEntrypointArgument.Sequence, is ContractEntrypointArgument.Map -> failWithValueMetaMismatch(value, meta)
        }

    private fun createOptionMicheline(value: ContractEntrypointArgument, meta: MetaContractEntrypointArgument.Object): MichelineNode =
        when (value) {
            is ContractEntrypointArgument.Value -> value.value?.let {
                MichelinePrimitiveApplication(MichelsonData.Some, args = listOf(it))
            } ?: MichelinePrimitiveApplication(MichelsonData.None)
            is ContractEntrypointArgument.Object -> {
                if (meta.elements.size != 1) failWithInvalidType(meta)

                val arg = value.takeIf { it.elements.isNotEmpty() }?.let { createArgMicheline(it, meta.elements[0]) }
                arg?.let {
                    MichelinePrimitiveApplication(
                        MichelsonData.Some,
                        args = listOf(it),
                    )
                } ?: MichelinePrimitiveApplication(MichelsonData.None)
            }
            is ContractEntrypointArgument.Sequence, is ContractEntrypointArgument.Map -> failWithValueMetaMismatch(value, meta)
        }

    private fun createOrMicheline(value: ContractEntrypointArgument, meta: MetaContractEntrypointArgument.Object): MichelineNode =
        when (value) {
            is ContractEntrypointArgument.Value -> {
                value.primOrNull(MichelsonData.Left, MichelsonData.Right) ?: run {
                    val namedMeta = value.findNextMeta(meta) ?: failWithValueMetaMismatch(value, meta)
                    createDirectedMicheline(value, namedMeta)
                }
            }
            is ContractEntrypointArgument.Object -> {
                val reducedMeta = meta.extract(value) ?: failWithValueMetaMismatch(value, meta)
                createDirectedMicheline(value, reducedMeta)
            }
            is ContractEntrypointArgument.Sequence, is ContractEntrypointArgument.Map -> failWithValueMetaMismatch(value, meta)
        }

    private fun createDirectedMicheline(value: ContractEntrypointArgument, meta: MetaContractEntrypointArgument): MichelineNode =
        MichelinePrimitiveApplication(
            meta.trace.directedPrim() ?: failWithInvalidType(meta),
            args = listOf(
                createMicheline(value, meta),
            ),
        )

    private fun createArgMicheline(value: ContractEntrypointArgument.Object, meta: MetaContractEntrypointArgument): MichelineNode? {
        val arg = value.extract(meta) ?: return null
        return createMicheline(arg, meta)
    }

    private fun createSimpleObjectMicheline(value: ContractEntrypointArgument, meta: MetaContractEntrypointArgument.Object): MichelineNode =
        when (value) {
            is ContractEntrypointArgument.Value -> {
                value.value ?: run {
                    val namedMeta = value.findNextMeta(meta) ?: failWithValueMetaMismatch(value, meta)
                    createMicheline(value, namedMeta)
                }
            }
            is ContractEntrypointArgument.Object -> {
                if (meta.elements.size != 1) failWithInvalidType(meta)

                createMicheline(value, meta.elements.first())
            }
            is ContractEntrypointArgument.Sequence, is ContractEntrypointArgument.Map -> failWithValueMetaMismatch(value, meta)
        }


    private fun createMetaEntrypointArgument(type: MichelineNode, trace: MichelineTrace = MichelineTrace.Root()): MetaContractEntrypointArgument =
        when (type) {
            is MichelinePrimitiveApplication -> createMetaEntrypointArgument(type, trace)
            else -> failWithInvalidType(type)
        }

    private fun createMetaEntrypointArgument(type: MichelinePrimitiveApplication, trace: MichelineTrace = MichelineTrace.Root()): MetaContractEntrypointArgument =
        when (val prim = Michelson.Prim.fromStringOrNull(type.prim.value, stringToMichelsonPrimConverter)) {
            is MichelsonType.Prim -> when {
                prim == MichelsonType.BigMap || type.args.isEmpty() -> createValueMetaEntrypointArgument(type, trace)
                prim == MichelsonType.List || prim == MichelsonType.Set -> createSequenceMetaEntrypointArgument(type, trace)
                prim == MichelsonType.Lambda -> createLambdaMetaEntrypointArgument(type, trace)
                prim == MichelsonType.Map -> createMapMetaEntrypointArgument(type, trace)
                type.args.isNotEmpty() -> createObjectMetaEntrypointArgument(type, trace)
                else -> failWithInvalidType(type)
            }
            else -> failWithInvalidType(type)
        }

    private fun createValueMetaEntrypointArgument(type: MichelinePrimitiveApplication, trace: MichelineTrace = MichelineTrace.Root()): MetaContractEntrypointArgument =
        MetaContractEntrypointArgument.Value(type, trace)

    private fun createSequenceMetaEntrypointArgument(type: MichelinePrimitiveApplication, trace: MichelineTrace = MichelineTrace.Root()): MetaContractEntrypointArgument {
        if (type.args.size != 1) failWithInvalidType(type)

        return MetaContractEntrypointArgument.Sequence(type, trace, listOf(createMetaEntrypointArgument(type.args.first(), MichelineTrace.Node(0))))
    }

    private fun createLambdaMetaEntrypointArgument(type: MichelinePrimitiveApplication, trace: MichelineTrace = MichelineTrace.Root()): MetaContractEntrypointArgument =
        MetaContractEntrypointArgument.Sequence(type, trace, listOf(createMetaEntrypointArgument(type)))

    private fun createMapMetaEntrypointArgument(type: MichelinePrimitiveApplication, trace: MichelineTrace = MichelineTrace.Root()): MetaContractEntrypointArgument {
        if (type.args.size != 2) failWithInvalidType(type)

        return MetaContractEntrypointArgument.Map(
            type,
            trace,
            createMetaEntrypointArgument(type.args[0], MichelineTrace.Node(0)),
            createMetaEntrypointArgument(type.args[1], MichelineTrace.Node(1)),
        )
    }

    private fun createObjectMetaEntrypointArgument(type: MichelinePrimitiveApplication, trace: MichelineTrace = MichelineTrace.Root()): MetaContractEntrypointArgument {
        if (type.args.size > 2) failWithInvalidType(type)

        return MetaContractEntrypointArgument.Object(
            type,
            trace,
            type.args.mapIndexed { index, t -> createMetaEntrypointArgument(t, MichelineTrace.Node(index)) },
        )
    }

    private fun ContractEntrypointArgument.Value.primOrNull(vararg prim: MichelsonData.Prim): MichelineNode? =
        if (prim.any { value?.isPrim(it) == true }) value else null

    private fun ContractEntrypointArgument.Value.sequenceOrNull(prim: MichelsonData.Prim? = null): MichelineNode? {
        if (value !is MichelineSequence) return null

        return value.takeIf { prim == null || value.nodes.all { it.isPrim(prim) } }
    }

    private fun ContractEntrypointArgument.findNextMeta(meta: MetaContractEntrypointArgument.Object): MetaContractEntrypointArgument? =
        name?.let {
            when (val trace = meta.trace(it)) {
                is MichelineTrace.Node -> meta.elements[trace.direction.index]
                else -> null
            }
        }

    private fun ContractEntrypointArgument.Object.extract(meta: MetaContractEntrypointArgument): ContractEntrypointArgument? {
        val reducedElements = elements.consumeAll { meta.describes(it) }

        when (reducedElements.size) {
            0 -> {
                return when (meta) {
                    is MetaContractEntrypointArgument.Value -> elements.consumeAt(0)
                    else -> this
                }
            }
            1 -> {
                val value = reducedElements.first()
                val trace = value.name?.let { meta.trace(it) }

                if (trace?.hasNext() == false) return value
            }
        }

        return ContractEntrypointArgument.Object(reducedElements.toMutableList())
    }

    private fun MetaContractEntrypointArgument.findValue(value: ContractEntrypointArgument.Object): ContractEntrypointArgument? =
        value.elements.consume { names.contains(it.name) }

    private fun MetaContractEntrypointArgument.Object.extract(value: ContractEntrypointArgument.Object): MetaContractEntrypointArgument? {
        val direction = flattenTraces.entries
            .mapNotNull { if (value.fields.contains(it.key)) it.value else null }
            .ifEmpty { listOf(MichelineTrace.Node(0)) }
            .map {
                when (it) {
                    is MichelineTrace.Root -> null
                    is MichelineTrace.Node -> it.direction
                }
            }
            .reduceRightOrNull { direction, acc ->
                if (direction != acc) null
                else direction
            } ?: return null

        return elements.getOrNull(direction.index)
    }

    private fun MetaContractEntrypointArgument.describes(value: ContractEntrypointArgument): Boolean =
        when (this) {
            is MetaContractEntrypointArgument.Object -> names.contains(value.name) || namedTraces.containsKey(value.name)
            is MetaContractEntrypointArgument.Value, is MetaContractEntrypointArgument.Sequence, is MetaContractEntrypointArgument.Map -> names.contains(value.name)
        }

    private fun MichelineTrace.directedPrim(): MichelsonData.Prim? =
        when (this) {
            is MichelineTrace.Root -> null
            is MichelineTrace.Node -> when (direction) {
                MichelineTrace.Node.Direction.Left -> MichelsonData.Left
                MichelineTrace.Node.Direction.Right -> MichelsonData.Right
            }
        }

    private fun failWithInvalidType(meta: MetaContractEntrypointArgument): Nothing = failWithInvalidType(meta.type)
    private fun failWithInvalidType(type: MichelineNode): Nothing =
        failWithIllegalArgument("Micheline type ${type.toCompactExpression(michelineToCompactStringConverter)} is invalid.")

    private fun failWithValueMetaMismatch(value: ContractEntrypointArgument, meta: MetaContractEntrypointArgument): Nothing =
        failWithIllegalArgument("Micheline type ${meta.type.toCompactExpression(michelineToCompactStringConverter)} and value $value mismatch.")
}