package it.airgap.tezos.contract.internal.converter

import it.airgap.tezos.contract.entrypoint.ContractEntrypointArgument
import it.airgap.tezos.contract.internal.entrypoint.MetaContractEntrypointArgument
import it.airgap.tezos.contract.internal.micheline.MichelineTrace
import it.airgap.tezos.core.internal.converter.ConfigurableConverter
import it.airgap.tezos.core.internal.utils.consume
import it.airgap.tezos.core.internal.utils.consumeAt
import it.airgap.tezos.core.internal.utils.failWithIllegalArgument
import it.airgap.tezos.michelson.*
import it.airgap.tezos.michelson.internal.converter.MichelineToCompactStringConverter
import it.airgap.tezos.michelson.internal.converter.StringToMichelsonPrimConverter
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication

internal class EntrypointArgumentToMichelineConverter(
    private val michelineToCompactStringConverter: MichelineToCompactStringConverter,
    private val stringToMichelsonPrimConverter: StringToMichelsonPrimConverter,
) : ConfigurableConverter<ContractEntrypointArgument, MichelineNode, EntrypointArgumentToMichelineConverter.Configuration> {
    override fun convert(value: ContractEntrypointArgument, configuration: Configuration): MichelineNode = with(configuration) {
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
                val value = value.elements.consume { meta.names.contains(it.name) } ?: failWithValueMetaMismatch(value, meta)
                createMicheline(value, meta)
            }
            is ContractEntrypointArgument.Sequence, is ContractEntrypointArgument.Map -> failWithValueMetaMismatch(value, meta)
        }


    private fun createObjectMicheline(value: ContractEntrypointArgument, meta: MetaContractEntrypointArgument.Object): MichelineNode =
        when {
            meta.type.isPrim(MichelsonType.Pair) || meta.type.isPrim(MichelsonComparableType.Pair) -> createPairMicheline(value, meta)
            meta.type.isPrim(MichelsonType.Option) || meta.type.isPrim(MichelsonComparableType.Option) -> createOptionMicheline(value, meta)
            meta.type.isPrim(MichelsonType.Or) || meta.type.isPrim(MichelsonComparableType.Or) -> createOrMicheline(value, meta)
            else -> TODO()
        }

    private fun createSequenceMicheline(value: ContractEntrypointArgument, meta: MetaContractEntrypointArgument.Sequence): MichelineNode = TODO()
    private fun createMapMicheline(value: ContractEntrypointArgument, meta: MetaContractEntrypointArgument.Map): MichelineNode = TODO()

    private fun createPairMicheline(value: ContractEntrypointArgument, meta: MetaContractEntrypointArgument.Object): MichelineNode =
        when (value) {
            is ContractEntrypointArgument.Value -> if (value.value?.isPrim(MichelsonData.Pair) == true) value.value else failWithValueMetaMismatch(value, meta)
            is ContractEntrypointArgument.Object -> {
                val metaArgs = meta.elements.filter { it.trace.next == null }

                when {
                    metaArgs.size == 2 -> {
                        val firstArgMeta = metaArgs[0]
                        val secondArgMeta = metaArgs[1]

                        val firstArg = firstArgMeta.names.firstNotNullOfOrNull { name -> value.elements.consume { it.name == name } }
                            ?: value.elements.consumeAt(0)
                            ?: failWithValueMetaMismatch(value, meta)

                        val secondArg = secondArgMeta.names.firstNotNullOfOrNull { name -> value.elements.consume { it.name == name } }
                            ?: value.elements.consumeAt(0)
                            ?: failWithValueMetaMismatch(value, meta)

                        MichelinePrimitiveApplication(
                            MichelsonData.Pair,
                            args = listOf(
                                createMicheline(firstArg, firstArgMeta),
                                createMicheline(secondArg, secondArgMeta),
                            ),
                        )
                    }
                    metaArgs.size == 1 && metaArgs[0].trace.isLeft -> {
                        val firstArgMeta = metaArgs[0]
                        val (_, secondArgMeta) = meta.split()

                        val firstArg = firstArgMeta.names.firstNotNullOfOrNull { name -> value.elements.consume { it.name == name } }
                            ?: value.elements.consumeAt(0)
                            ?: failWithValueMetaMismatch(value, meta)

                        MichelinePrimitiveApplication(
                            MichelsonData.Pair,
                            args = listOf(
                                createMicheline(firstArg, firstArgMeta),
                                createMicheline(value, secondArgMeta),
                            ),
                        )
                    }
                    metaArgs.size == 1 && metaArgs[0].trace.isRight -> {
                        val (firstArgMeta, _) = meta.split()
                        val secondArgMeta = metaArgs[0]

                        val secondArg = secondArgMeta.names.firstNotNullOfOrNull { name -> value.elements.consume { it.name == name } }
                            ?: value.elements.consumeAt(firstArgMeta.elements.size)
                            ?: failWithValueMetaMismatch(value, meta)


                        MichelinePrimitiveApplication(
                            MichelsonData.Pair,
                            args = listOf(
                                createMicheline(value, firstArgMeta),
                                createMicheline(secondArg, secondArgMeta),
                            ),
                        )
                    }
                    metaArgs.isEmpty() -> {
                        val (firstArgMeta, secondArgMeta) = meta.split()
                        val firstArg = ContractEntrypointArgument.Object(value.elements.take(firstArgMeta.elements.size).toMutableList())
                        val secondArg = ContractEntrypointArgument.Object(value.elements.takeLast(secondArgMeta.elements.size).toMutableList())

                        MichelinePrimitiveApplication(
                            MichelsonData.Pair,
                            args = listOf(
                                createMicheline(firstArg, firstArgMeta),
                                createMicheline(secondArg, secondArgMeta),
                            ),
                        )
                    }
                    else -> failWithValueMetaMismatch(value, meta)
                }
            }
            is ContractEntrypointArgument.Sequence, is ContractEntrypointArgument.Map -> failWithValueMetaMismatch(value, meta)
        }

    private fun createOptionMicheline(value: ContractEntrypointArgument, meta: MetaContractEntrypointArgument.Object): MichelineNode =
        when (value) {
            is ContractEntrypointArgument.Value -> value.value?.let {
                MichelinePrimitiveApplication(MichelsonData.Some, args = listOf(it), )
            } ?: MichelinePrimitiveApplication(MichelsonData.None)
            is ContractEntrypointArgument.Object -> {
                TODO()
            }
            is ContractEntrypointArgument.Sequence, is ContractEntrypointArgument.Map -> failWithValueMetaMismatch(value, meta)
        }

    private fun createOrMicheline(value: ContractEntrypointArgument, meta: MetaContractEntrypointArgument.Object): MichelineNode =
        when (value) {
            is ContractEntrypointArgument.Value -> if (value.value?.isPrim(MichelsonData.Pair) == true) value.value else failWithValueMetaMismatch(value, meta)
            is ContractEntrypointArgument.Object -> {
                val metaArgs = meta.elements.filter { it.trace.next == null }

                when (metaArgs.size) {
                    1 -> {
                        val argMeta = metaArgs[0]
                        val arg = argMeta.names.firstNotNullOfOrNull { name -> value.elements.consume { it.name == name } }
                            ?: value.elements.consumeAt(0)
                            ?: failWithValueMetaMismatch(value, meta)

                        MichelinePrimitiveApplication(
                            if (argMeta.trace.isLeft) MichelsonData.Left else MichelsonData.Right,
                            args = listOf(
                                createMicheline(arg, argMeta),
                            ),
                        )
                    }
                    else -> {
                        val names = value.elements.mapNotNull { it.name }
                        val requiredMeta = meta.elements.filter { m -> names.any { m.names.contains(it) } }

                        if (requiredMeta.zipWithNext().any { !it.first.trace.hasSameDirection(it.second.trace) }) failWithValueMetaMismatch(value, meta)
                        if (meta.type !is MichelinePrimitiveApplication) failWithValueMetaMismatch(value, meta)

                        val direction = (requiredMeta.first().trace as? MichelineTrace.Node)?.direction ?: failWithValueMetaMismatch(value, meta)

                        val reducedMeta = MetaContractEntrypointArgument.Object(
                            meta.type.args[direction.index],
                            meta.trace.next ?: MichelineTrace.Root(),
                            requiredMeta.mapNotNull { it.relativeToNext() },
                        )

                        MichelinePrimitiveApplication(
                            if (direction == MichelineTrace.Node.Direction.Left) MichelsonData.Left else MichelsonData.Right,
                            args = listOf(
                                createMicheline(value, reducedMeta),
                            ),
                        )
                    }
                }
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
            type.args.flatMapIndexed { index, t ->
                val argument = createMetaEntrypointArgument(t, MichelineTrace.Node(index))

                if (argument is MetaContractEntrypointArgument.Object && argument.names.isEmpty()) argument.elements.map { it.relativeToPrevious(argument.trace) }
                else listOf(argument)
            },
        )
    }

    private fun MetaContractEntrypointArgument.Object.split(): Pair<MetaContractEntrypointArgument.Object, MetaContractEntrypointArgument.Object> {
        if (type !is MichelinePrimitiveApplication) failWithInvalidType(type)

        val first = MetaContractEntrypointArgument.Object(
            type.args[0],
            trace.next ?: MichelineTrace.Root(),
            elements.filter { it.trace.isLeft }.mapNotNull { it.relativeToNext() },
        )

        val second = MetaContractEntrypointArgument.Object(
            type.args[1],
            trace.next ?: MichelineTrace.Root(),
            elements.filter { it.trace.isRight }.mapNotNull { it.relativeToNext() },
        )

        return Pair(first, second)
    }

    private fun MichelineTrace.hasSameDirection(other: MichelineTrace): Boolean =
        when {
            this is MichelineTrace.Root && other is MichelineTrace.Root -> true
            this is MichelineTrace.Node && other is MichelineTrace.Node -> direction == other.direction
            else -> false
        }

    private val MichelineTrace.isLeft: Boolean
        get() = when (this) {
            is MichelineTrace.Root -> false
            is MichelineTrace.Node -> direction == MichelineTrace.Node.Direction.Left
        }

    private val MichelineTrace.isRight: Boolean
        get() = when (this) {
            is MichelineTrace.Root -> false
            is MichelineTrace.Node -> direction == MichelineTrace.Node.Direction.Right
        }

    private fun failWithInvalidType(type: MichelineNode): Nothing =
        failWithIllegalArgument("Micheline type ${type.toCompactExpression(michelineToCompactStringConverter)} is invalid.")

    private fun failWithValueMetaMismatch(value: ContractEntrypointArgument, meta: MetaContractEntrypointArgument): Nothing =
        failWithIllegalArgument("Micheline type ${meta.type.toCompactExpression(michelineToCompactStringConverter)} and value $value mismatch.")

    data class Configuration(val type: MichelineNode)
}