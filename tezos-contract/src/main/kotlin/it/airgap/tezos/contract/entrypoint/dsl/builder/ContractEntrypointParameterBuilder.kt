package it.airgap.tezos.contract.entrypoint.dsl.builder

import it.airgap.tezos.contract.entrypoint.ContractEntrypointParameter
import it.airgap.tezos.contract.entrypoint.dsl.ContractEntrypointParameterDslMaker
import it.airgap.tezos.contract.internal.context.TezosContractContext.micheline
import it.airgap.tezos.contract.internal.utils.allIsInstance
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.MichelineMichelsonExpressionBuilder

public interface ContractEntrypointParameterBuilder {
    public val name: String?
    public fun build(): ContractEntrypointParameter
}

@ContractEntrypointParameterDslMaker
public class ContractEntrypointValueParameterBuilder(
    private val michelsonToMichelineConverter: Converter<Michelson, Micheline>,
    private var value: Micheline? = null,
    name: String? = null,
) : ContractEntrypointParameterBuilder {
    override var name: String? = name
        private set

    public val none: ContractEntrypointValueParameterBuilder
        get() = also { value = null }

    public fun value(node: Micheline): ContractEntrypointValueParameterBuilder = also {
        value = node
    }

    public fun value(createExpression: MichelineMichelsonExpressionBuilder.() -> Unit): ContractEntrypointValueParameterBuilder = also {
        value = micheline(michelsonToMichelineConverter, createExpression)
    }

    public infix fun annot(value: String): ContractEntrypointValueParameterBuilder = also {
        name = value
    }

    override fun build(): ContractEntrypointParameter =
        ContractEntrypointParameter.Value(value, name)
}

@ContractEntrypointParameterDslMaker
public class ContractEntrypointObjectParameterBuilder(
    private val michelsonToMichelineConverter: Converter<Michelson, Micheline>,
    name: String? = null,
) : ContractEntrypointParameterBuilder {
    override var name: String? = name
        private set

    private val builders: MutableList<ContractEntrypointParameterBuilder> = mutableListOf()

    public fun value(annot: String? = null, micheline: Micheline? = null, createExpression: MichelineMichelsonExpressionBuilder.() -> Unit = {}): ContractEntrypointValueParameterBuilder =
        ContractEntrypointValueParameterBuilder(michelsonToMichelineConverter, micheline, annot).apply { value(createExpression) }.also { builders.add(it) }

    public fun `object`(annot: String? = null, builderAction: ContractEntrypointObjectParameterBuilder.() -> Unit): ContractEntrypointObjectParameterBuilder =
        ContractEntrypointObjectParameterBuilder(michelsonToMichelineConverter, annot).apply(builderAction).also { builders.add(it) }

    public fun sequence(annot: String? = null, builderAction: ContractEntrypointSequenceParameterBuilder.() -> Unit): ContractEntrypointSequenceParameterBuilder =
        ContractEntrypointSequenceParameterBuilder(michelsonToMichelineConverter, annot).apply(builderAction).also { builders.add(it) }

    public fun map(annot: String? = null, builderAction: ContractEntrypointMapParameterBuilder.() -> Unit): ContractEntrypointMapParameterBuilder =
        ContractEntrypointMapParameterBuilder(michelsonToMichelineConverter, annot).apply(builderAction).also { builders.add(it) }

    override fun build(): ContractEntrypointParameter {
        val names: MutableSet<String> = mutableSetOf()
        val built = builders.foldRight(listOf<ContractEntrypointParameter>()) { prev, acc ->
            if (names.contains(prev.name)) acc
            else {
                prev.name?.let { names.add(it) }
                acc + prev.build()
            }
        }.reversed()

        return ContractEntrypointParameter.Object(built.toMutableList(), name)
    }
}

@ContractEntrypointParameterDslMaker
public class ContractEntrypointSequenceParameterBuilder(
    private val michelsonToMichelineConverter: Converter<Michelson, Micheline>,
    name: String? = null,
) : ContractEntrypointParameterBuilder {
    override var name: String? = name
        private set

    private val builders: MutableList<ContractEntrypointParameterBuilder> = mutableListOf()

    public fun value(annot: String? = null, micheline: Micheline? = null, createExpression: MichelineMichelsonExpressionBuilder.() -> Unit = {}): ContractEntrypointValueParameterBuilder =
        ContractEntrypointValueParameterBuilder(michelsonToMichelineConverter, micheline, annot).apply { value(createExpression) }.also { builders.addIfAllInstance(it) }

    public fun `object`(annot: String? = null, builderAction: ContractEntrypointObjectParameterBuilder.() -> Unit): ContractEntrypointObjectParameterBuilder =
        ContractEntrypointObjectParameterBuilder(michelsonToMichelineConverter, annot).apply(builderAction).also { builders.addIfAllInstance(it) }

    public fun sequence(annot: String? = null, builderAction: ContractEntrypointSequenceParameterBuilder.() -> Unit): ContractEntrypointSequenceParameterBuilder =
        ContractEntrypointSequenceParameterBuilder(michelsonToMichelineConverter, annot).apply(builderAction).also { builders.addIfAllInstance(it) }

    public fun map(annot: String? = null, builderAction: ContractEntrypointMapParameterBuilder.() -> Unit): ContractEntrypointMapParameterBuilder =
        ContractEntrypointMapParameterBuilder(michelsonToMichelineConverter, annot).apply(builderAction).also { builders.addIfAllInstance(it) }

    private inline fun <reified T : ContractEntrypointParameterBuilder> MutableList<ContractEntrypointParameterBuilder>.addIfAllInstance(element: T) {
        if (allIsInstance<T>()) {
            add(element)
        }
    }

    override fun build(): ContractEntrypointParameter =
        ContractEntrypointParameter.Sequence(builders.map { it.build() }.toMutableList(), name)
}

@ContractEntrypointParameterDslMaker
public class ContractEntrypointMapParameterBuilder(
    private val michelsonToMichelineConverter: Converter<Michelson, Micheline>,
    name: String? = null,
) : ContractEntrypointParameterBuilder {
    override var name: String? = name
        private set

    private val builders: MutableList<Pair<ContractEntrypointParameterBuilder, ContractEntrypointParameterBuilder>> = mutableListOf()

    public fun key(annot: String? = null, micheline: Micheline? = null, createExpression: MichelineMichelsonExpressionBuilder.() -> Unit = {}): Key =
        Key(ContractEntrypointValueParameterBuilder(michelsonToMichelineConverter, micheline, annot).apply { value(createExpression) })
    public fun value(annot: String? = null, micheline: Micheline? = null, createExpression: MichelineMichelsonExpressionBuilder.() -> Unit = {}): Value =
        Value(ContractEntrypointValueParameterBuilder(michelsonToMichelineConverter, micheline, annot).apply { value(createExpression) })

    public fun keyObject(annot: String? = null, builderAction: ContractEntrypointObjectParameterBuilder.() -> Unit): Key =
        Key(ContractEntrypointObjectParameterBuilder(michelsonToMichelineConverter, name = annot).apply(builderAction))
    public fun valueObject(annot: String? = null, builderAction: ContractEntrypointObjectParameterBuilder.() -> Unit): Value
    = Value(ContractEntrypointObjectParameterBuilder(michelsonToMichelineConverter, name = annot).apply(builderAction))

    public fun keySequence(annot: String? = null, builderAction: ContractEntrypointSequenceParameterBuilder.() -> Unit): Key =
        Key(ContractEntrypointSequenceParameterBuilder(michelsonToMichelineConverter, name = annot).apply(builderAction))
    public fun valueSequence(annot: String? = null, builderAction: ContractEntrypointSequenceParameterBuilder.() -> Unit): Value =
        Value(ContractEntrypointSequenceParameterBuilder(michelsonToMichelineConverter, name = annot).apply(builderAction))

    public fun keyMap(annot: String? = null, builderAction: ContractEntrypointMapParameterBuilder.() -> Unit): Key =
        Key(ContractEntrypointMapParameterBuilder(michelsonToMichelineConverter, name = annot).apply(builderAction))
    public fun valueMap(annot: String? = null, builderAction: ContractEntrypointMapParameterBuilder.() -> Unit): Value =
        Value(ContractEntrypointMapParameterBuilder(michelsonToMichelineConverter, name = annot).apply(builderAction))

    public infix fun Key.pointsTo(value: Value): ContractEntrypointMapParameterBuilder = this@ContractEntrypointMapParameterBuilder.also {
        builders.add(builder to value.builder)
    }

    @JvmInline
    public value class Key(internal val builder: ContractEntrypointParameterBuilder)

    @JvmInline
    public value class Value(internal val builder: ContractEntrypointParameterBuilder)

    override fun build(): ContractEntrypointParameter =
        ContractEntrypointParameter.Map(builders.associate { it.first.build() to it.second.build() }, name)
}
