package it.airgap.tezos.contract.internal.converter

import it.airgap.tezos.contract.entrypoint.ContractEntrypointParameter
import it.airgap.tezos.contract.internal.context.TezosContractContext.consume
import it.airgap.tezos.contract.internal.context.TezosContractContext.consumeAll
import it.airgap.tezos.contract.internal.context.TezosContractContext.consumeAt
import it.airgap.tezos.contract.internal.context.TezosContractContext.failWithIllegalArgument
import it.airgap.tezos.contract.internal.context.TezosContractContext.fromStringOrNull
import it.airgap.tezos.contract.internal.context.TezosContractContext.toCompactExpression
import it.airgap.tezos.contract.internal.entrypoint.MetaContractEntrypointParameter
import it.airgap.tezos.contract.internal.micheline.MichelineTrace
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.MichelsonComparableType
import it.airgap.tezos.michelson.MichelsonData
import it.airgap.tezos.michelson.MichelsonType
import it.airgap.tezos.michelson.comparator.isPrim
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.MichelineSequence

internal class EntrypointParameterToMichelineConverter(
    private val michelineToCompactStringConverter: Converter<Micheline, String>,
    private val stringToMichelsonPrimConverter: Converter<String, Michelson.Prim>,
) : TypedConverter<ContractEntrypointParameter, Micheline> {
    override fun convert(value: ContractEntrypointParameter, type: Micheline): Micheline {
        val meta = createMetaEntrypointParameter(type)
        return createMicheline(value, meta)
    }

    private fun createMicheline(value: ContractEntrypointParameter, meta: MetaContractEntrypointParameter): Micheline =
        when (meta) {
            is MetaContractEntrypointParameter.Value -> createValueMicheline(value, meta)
            is MetaContractEntrypointParameter.Object -> createObjectMicheline(value, meta)
            is MetaContractEntrypointParameter.Sequence -> createSequenceMicheline(value, meta)
            is MetaContractEntrypointParameter.Map -> createMapMicheline(value, meta)
        }

    private fun createValueMicheline(value: ContractEntrypointParameter, meta: MetaContractEntrypointParameter.Value): Micheline =
        when (value) {
            is ContractEntrypointParameter.Value -> value.value ?: failWithValueMetaMismatch(value, meta)
            is ContractEntrypointParameter.Object -> {
                val named = meta.findValue(value) ?: failWithValueMetaMismatch(value, meta)
                createMicheline(named, meta)
            }
            is ContractEntrypointParameter.Sequence, is ContractEntrypointParameter.Map -> failWithValueMetaMismatch(value, meta)
        }

    private fun createObjectMicheline(value: ContractEntrypointParameter, meta: MetaContractEntrypointParameter.Object): Micheline =
        when {
            meta.type.isPrim(MichelsonType.Pair) || meta.type.isPrim(MichelsonComparableType.Pair) -> createPairMicheline(value, meta)
            meta.type.isPrim(MichelsonType.Option) || meta.type.isPrim(MichelsonComparableType.Option) -> createOptionMicheline(value, meta)
            meta.type.isPrim(MichelsonType.Or) || meta.type.isPrim(MichelsonComparableType.Or) -> createOrMicheline(value, meta)
            else -> createSimpleObjectMicheline(value, meta)
        }

    private fun createSequenceMicheline(value: ContractEntrypointParameter, meta: MetaContractEntrypointParameter.Sequence): Micheline =
        when (value) {
            is ContractEntrypointParameter.Value -> value.sequenceOrNull() ?: failWithValueMetaMismatch(value, meta)
            is ContractEntrypointParameter.Object -> {
                val named = meta.findValue(value) ?: meta.findFirstInstance<ContractEntrypointParameter.Sequence>(value) ?: failWithValueMetaMismatch(value, meta)
                createMicheline(named, meta)
            }
            is ContractEntrypointParameter.Sequence -> {
                if (meta.elements.size != 1) failWithInvalidType(meta)

                MichelineSequence(value.elements.map { createMicheline(it, meta.elements.first()) })
            }
            is ContractEntrypointParameter.Map -> failWithValueMetaMismatch(value, meta)
        }

    private fun createMapMicheline(value: ContractEntrypointParameter, meta: MetaContractEntrypointParameter.Map): Micheline =
        when (value) {
            is ContractEntrypointParameter.Value -> value.sequenceOrNull(MichelsonData.Elt) ?: failWithValueMetaMismatch(value, meta)
            is ContractEntrypointParameter.Object -> {
                val named = meta.findValue(value) ?: meta.findFirstInstance<ContractEntrypointParameter.Map>(value) ?: failWithValueMetaMismatch(value, meta)
                createMicheline(named, meta)
            }
            is ContractEntrypointParameter.Map -> {
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
            is ContractEntrypointParameter.Sequence -> failWithValueMetaMismatch(value, meta)
        }

    private fun createPairMicheline(value: ContractEntrypointParameter, meta: MetaContractEntrypointParameter.Object): Micheline =
        when (value) {
            is ContractEntrypointParameter.Value -> {
                value.primOrNull(MichelsonData.Pair) ?: run {
                    val namedMeta = value.findNextMeta(meta) ?: failWithValueMetaMismatch(value, meta)
                    createMicheline(value, namedMeta)
                }
            }
            is ContractEntrypointParameter.Object -> {
                if (meta.elements.size != 2) failWithInvalidType(meta)

                MichelinePrimitiveApplication(
                    MichelsonData.Pair,
                    args = listOf(
                        createArgMicheline(value, meta.elements[0]) ?: failWithValueMetaMismatch(value, meta),
                        createArgMicheline(value, meta.elements[1]) ?: failWithValueMetaMismatch(value, meta),
                    )
                )
            }
            is ContractEntrypointParameter.Sequence, is ContractEntrypointParameter.Map -> failWithValueMetaMismatch(value, meta)
        }

    private fun createOptionMicheline(value: ContractEntrypointParameter, meta: MetaContractEntrypointParameter.Object): Micheline =
        when (value) {
            is ContractEntrypointParameter.Value -> value.value?.let {
                MichelinePrimitiveApplication(MichelsonData.Some, args = listOf(it))
            } ?: MichelinePrimitiveApplication(MichelsonData.None)
            is ContractEntrypointParameter.Object -> {
                if (meta.elements.size != 1) failWithInvalidType(meta)

                val arg = value.takeIf { it.elements.isNotEmpty() }?.let { createArgMicheline(it, meta.elements[0]) }
                arg?.let {
                    MichelinePrimitiveApplication(
                        MichelsonData.Some,
                        args = listOf(it),
                    )
                } ?: MichelinePrimitiveApplication(MichelsonData.None)
            }
            is ContractEntrypointParameter.Sequence, is ContractEntrypointParameter.Map -> failWithValueMetaMismatch(value, meta)
        }

    private fun createOrMicheline(value: ContractEntrypointParameter, meta: MetaContractEntrypointParameter.Object): Micheline =
        when (value) {
            is ContractEntrypointParameter.Value -> {
                value.primOrNull(MichelsonData.Left, MichelsonData.Right) ?: run {
                    val namedMeta = value.findNextMeta(meta) ?: failWithValueMetaMismatch(value, meta)
                    createDirectedMicheline(value, namedMeta)
                }
            }
            is ContractEntrypointParameter.Object -> {
                val reducedMeta = meta.extract(value) ?: failWithValueMetaMismatch(value, meta)
                createDirectedMicheline(value, reducedMeta)
            }
            is ContractEntrypointParameter.Sequence, is ContractEntrypointParameter.Map -> failWithValueMetaMismatch(value, meta)
        }

    private fun createDirectedMicheline(value: ContractEntrypointParameter, meta: MetaContractEntrypointParameter): Micheline =
        MichelinePrimitiveApplication(
            meta.trace.directedPrim() ?: failWithInvalidType(meta),
            args = listOf(
                createMicheline(value, meta),
            ),
        )

    private fun createArgMicheline(value: ContractEntrypointParameter.Object, meta: MetaContractEntrypointParameter): Micheline? {
        val arg = value.extract(meta) ?: return null
        return createMicheline(arg, meta)
    }

    private fun createSimpleObjectMicheline(value: ContractEntrypointParameter, meta: MetaContractEntrypointParameter.Object): Micheline =
        when (value) {
            is ContractEntrypointParameter.Value -> {
                value.value ?: run {
                    val namedMeta = value.findNextMeta(meta) ?: failWithValueMetaMismatch(value, meta)
                    createMicheline(value, namedMeta)
                }
            }
            is ContractEntrypointParameter.Object -> {
                if (meta.elements.size != 1) failWithInvalidType(meta)

                createMicheline(value, meta.elements.first())
            }
            is ContractEntrypointParameter.Sequence, is ContractEntrypointParameter.Map -> failWithValueMetaMismatch(value, meta)
        }


    private fun createMetaEntrypointParameter(type: Micheline, trace: MichelineTrace = MichelineTrace.Root()): MetaContractEntrypointParameter =
        when (type) {
            is MichelinePrimitiveApplication -> createMetaEntrypointParameter(type, trace)
            else -> failWithInvalidType(type)
        }

    private fun createMetaEntrypointParameter(type: MichelinePrimitiveApplication, trace: MichelineTrace = MichelineTrace.Root()): MetaContractEntrypointParameter =
        when (val prim = Michelson.Prim.fromStringOrNull(type.prim.value, stringToMichelsonPrimConverter)) {
            is MichelsonType.Prim -> when {
                prim == MichelsonType.BigMap || prim == MichelsonType.Lambda || type.args.isEmpty() -> createValueMetaEntrypointArgument(type, trace)
                prim == MichelsonType.List || prim == MichelsonType.Set -> createSequenceMetaEntrypointArgument(type, trace)
                prim == MichelsonType.Map -> createMapMetaEntrypointArgument(type, trace)
                type.args.isNotEmpty() -> createObjectMetaEntrypointArgument(type, trace)
                else -> failWithInvalidType(type)
            }
            else -> failWithInvalidType(type)
        }

    private fun createValueMetaEntrypointArgument(type: MichelinePrimitiveApplication, trace: MichelineTrace = MichelineTrace.Root()): MetaContractEntrypointParameter =
        MetaContractEntrypointParameter.Value(type, trace)

    private fun createSequenceMetaEntrypointArgument(type: MichelinePrimitiveApplication, trace: MichelineTrace = MichelineTrace.Root()): MetaContractEntrypointParameter {
        if (type.args.size != 1) failWithInvalidType(type)

        return MetaContractEntrypointParameter.Sequence(type, trace, listOf(createMetaEntrypointParameter(type.args.first(), MichelineTrace.Node(0))))
    }

    private fun createMapMetaEntrypointArgument(type: MichelinePrimitiveApplication, trace: MichelineTrace = MichelineTrace.Root()): MetaContractEntrypointParameter {
        if (type.args.size != 2) failWithInvalidType(type)

        return MetaContractEntrypointParameter.Map(
            type,
            trace,
            createMetaEntrypointParameter(type.args[0], MichelineTrace.Node(0)),
            createMetaEntrypointParameter(type.args[1], MichelineTrace.Node(1)),
        )
    }

    private fun createObjectMetaEntrypointArgument(type: MichelinePrimitiveApplication, trace: MichelineTrace = MichelineTrace.Root()): MetaContractEntrypointParameter {
        if (type.args.size > 2) failWithInvalidType(type)

        return MetaContractEntrypointParameter.Object(
            type,
            trace,
            type.args.mapIndexed { index, t -> createMetaEntrypointParameter(t, MichelineTrace.Node(index)) },
        )
    }

    private fun ContractEntrypointParameter.Value.primOrNull(vararg prim: MichelsonData.Prim): Micheline? =
        if (prim.any { value?.isPrim(it) == true }) value else null

    private fun ContractEntrypointParameter.Value.sequenceOrNull(prim: MichelsonData.Prim? = null): Micheline? {
        if (value !is MichelineSequence) return null

        return value.takeIf { prim == null || value.nodes.all { it.isPrim(prim) } }
    }

    private fun ContractEntrypointParameter.findNextMeta(meta: MetaContractEntrypointParameter.Object): MetaContractEntrypointParameter? =
        name?.let {
            when (val trace = meta.trace(it)) {
                is MichelineTrace.Node -> meta.elements[trace.direction.index]
                else -> null
            }
        }

    private fun ContractEntrypointParameter.Object.extract(meta: MetaContractEntrypointParameter): ContractEntrypointParameter? {
        val reducedElements = elements.consumeAll { meta.describes(it) }

        when (reducedElements.size) {
            0 -> {
                return when (meta) {
                    is MetaContractEntrypointParameter.Value -> elements.consumeAt(0)
                    else -> this
                }
            }
            1 -> {
                val value = reducedElements.first()
                val trace = value.name?.let { meta.trace(it) }

                if (trace?.hasNext() == false) return value
            }
        }

        return ContractEntrypointParameter.Object(reducedElements.toMutableList())
    }

    private fun MetaContractEntrypointParameter.findValue(value: ContractEntrypointParameter.Object): ContractEntrypointParameter? =
        value.elements.consume { names.contains(it.name) }

    private inline fun <reified T : ContractEntrypointParameter> MetaContractEntrypointParameter.findFirstInstance(
        value: ContractEntrypointParameter.Object
    ): ContractEntrypointParameter? = value.elements.consume { it is T }

    private fun MetaContractEntrypointParameter.Object.extract(value: ContractEntrypointParameter.Object): MetaContractEntrypointParameter? {
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

    private fun MetaContractEntrypointParameter.describes(value: ContractEntrypointParameter): Boolean =
        when (this) {
            is MetaContractEntrypointParameter.Object -> names.contains(value.name) || namedTraces.containsKey(value.name)
            is MetaContractEntrypointParameter.Value, is MetaContractEntrypointParameter.Sequence, is MetaContractEntrypointParameter.Map -> names.contains(value.name)
        }

    private fun MichelineTrace.directedPrim(): MichelsonData.Prim? =
        when (this) {
            is MichelineTrace.Root -> null
            is MichelineTrace.Node -> when (direction) {
                MichelineTrace.Node.Direction.Left -> MichelsonData.Left
                MichelineTrace.Node.Direction.Right -> MichelsonData.Right
            }
        }

    private fun failWithInvalidType(meta: MetaContractEntrypointParameter): Nothing = failWithInvalidType(meta.type)
    private fun failWithInvalidType(type: Micheline): Nothing =
        failWithIllegalArgument("Micheline type ${type.toCompactExpression(michelineToCompactStringConverter)} is invalid.")

    private fun failWithValueMetaMismatch(value: ContractEntrypointParameter, meta: MetaContractEntrypointParameter): Nothing =
        failWithIllegalArgument("Micheline type ${meta.type.toCompactExpression(michelineToCompactStringConverter)} and value $value mismatch.")
}