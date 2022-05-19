package it.airgap.tezos.contract.entrypoint.dsl.builder

import it.airgap.tezos.contract.entrypoint.ContractEntrypointParameter
import it.airgap.tezos.contract.entrypoint.dsl.ContractEntrypointParameterDslMaker
import it.airgap.tezos.core.internal.utils.allIsInstance
import it.airgap.tezos.michelson.micheline.MichelineNode

public interface ContractEntrypointParameterBuilder {
    public val name: String?
    public fun build(): ContractEntrypointParameter
}

@ContractEntrypointParameterDslMaker
public class ContractEntrypointValueParameterBuilder(
    private var value: MichelineNode? = null,
    name: String? = null,
) : ContractEntrypointParameterBuilder {
    override var name: String? = name
        private set

    public val none: ContractEntrypointValueParameterBuilder
        get() = also { value = null }

    public fun micheline(node: MichelineNode): ContractEntrypointValueParameterBuilder = also {
        value = node
    }

    public infix fun annot(value: String): ContractEntrypointValueParameterBuilder = also {
        name = value
    }

    override fun build(): ContractEntrypointParameter =
        ContractEntrypointParameter.Value(value, name)
}

@ContractEntrypointParameterDslMaker
public class ContractEntrypointObjectParameterBuilder(name: String? = null) : ContractEntrypointParameterBuilder {
    override var name: String? = name
        private set

    private val builders: MutableList<ContractEntrypointParameterBuilder> = mutableListOf()

    public fun value(micheline: MichelineNode? = null): ContractEntrypointObjectParameterBuilder =
        value(null, micheline)

    public fun value(annot: String?, micheline: MichelineNode? = null): ContractEntrypointObjectParameterBuilder = also {
        builders.add(ContractEntrypointValueParameterBuilder(micheline, annot))
    }

    public fun value(annot: String? = null, builderAction: ContractEntrypointValueParameterBuilder.() -> Unit): ContractEntrypointObjectParameterBuilder = also {
        builders.add(ContractEntrypointValueParameterBuilder(name = annot).apply(builderAction))
    }

    public fun `object`(annot: String? = null, builderAction: ContractEntrypointObjectParameterBuilder.() -> Unit): ContractEntrypointObjectParameterBuilder = also {
        builders.add(ContractEntrypointObjectParameterBuilder(annot).apply(builderAction))
    }

    public fun sequence(annot: String? = null, builderAction: ContractEntrypointSequenceParameterBuilder.() -> Unit): ContractEntrypointObjectParameterBuilder = also {
        builders.add(ContractEntrypointSequenceParameterBuilder(annot).apply(builderAction))
    }

    public fun map(annot: String? = null, builderAction: ContractEntrypointMapParameterBuilder.() -> Unit): ContractEntrypointObjectParameterBuilder = also {
        builders.add(ContractEntrypointMapParameterBuilder(annot).apply(builderAction))
    }

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
public class ContractEntrypointSequenceParameterBuilder(name: String? = null) : ContractEntrypointParameterBuilder {
    override var name: String? = name
        private set

    private val builders: MutableList<ContractEntrypointParameterBuilder> = mutableListOf()

    public fun value(micheline: MichelineNode? = null): ContractEntrypointSequenceParameterBuilder =
        value(null, micheline)

    public fun value(annot: String?, micheline: MichelineNode? = null): ContractEntrypointSequenceParameterBuilder = also {
        builders.addIfAllInstance(ContractEntrypointValueParameterBuilder(micheline, annot))
    }

    public fun value(annot: String? = null, builderAction: ContractEntrypointValueParameterBuilder.() -> Unit): ContractEntrypointSequenceParameterBuilder = also {
        builders.addIfAllInstance(ContractEntrypointValueParameterBuilder(name = annot).apply(builderAction))
    }

    public fun `object`(annot: String? = null, builderAction: ContractEntrypointObjectParameterBuilder.() -> Unit): ContractEntrypointSequenceParameterBuilder = also {
        builders.addIfAllInstance(ContractEntrypointObjectParameterBuilder(annot).apply(builderAction))
    }

    public fun sequence(annot: String? = null, builderAction: ContractEntrypointSequenceParameterBuilder.() -> Unit): ContractEntrypointSequenceParameterBuilder = also {
        builders.addIfAllInstance(ContractEntrypointSequenceParameterBuilder(annot).apply(builderAction))
    }

    public fun map(annot: String? = null, builderAction: ContractEntrypointMapParameterBuilder.() -> Unit): ContractEntrypointSequenceParameterBuilder = also {
        builders.addIfAllInstance(ContractEntrypointMapParameterBuilder(annot).apply(builderAction))
    }

    private inline fun <reified T : ContractEntrypointParameterBuilder> MutableList<ContractEntrypointParameterBuilder>.addIfAllInstance(element: T) {
        if (allIsInstance<T>()) {
            add(element)
        }
    }

    override fun build(): ContractEntrypointParameter =
        ContractEntrypointParameter.Sequence(builders.map { it.build() }.toMutableList(), name)
}

@ContractEntrypointParameterDslMaker
public class ContractEntrypointMapParameterBuilder(name: String? = null) : ContractEntrypointParameterBuilder {
    override var name: String? = name
        private set

    private val builders: MutableList<Pair<ContractEntrypointParameterBuilder, ContractEntrypointParameterBuilder>> = mutableListOf()

    public fun key(micheline: MichelineNode? = null): Key = key(null, micheline)
    public fun value(micheline: MichelineNode? = null): Value = value(null, micheline)

    public fun key(annot: String?, micheline: MichelineNode? = null): Key = Key(ContractEntrypointValueParameterBuilder(micheline, annot))
    public fun value(annot: String?, micheline: MichelineNode? = null): Value = Value(ContractEntrypointValueParameterBuilder(micheline, annot))

    public fun key(annot: String? = null, builderAction: ContractEntrypointValueParameterBuilder.() -> Unit): Key = Key(ContractEntrypointValueParameterBuilder(name = annot).apply(builderAction))
    public fun value(annot: String? = null, builderAction: ContractEntrypointValueParameterBuilder.() -> Unit): Value = Value(ContractEntrypointValueParameterBuilder(name = annot).apply(builderAction))

    public fun keyObject(annot: String? = null, builderAction: ContractEntrypointObjectParameterBuilder.() -> Unit): Key = Key(ContractEntrypointObjectParameterBuilder(name = annot).apply(builderAction))
    public fun valueObject(annot: String? = null, builderAction: ContractEntrypointObjectParameterBuilder.() -> Unit): Value = Value(ContractEntrypointObjectParameterBuilder(name = annot).apply(builderAction))

    public fun keySequence(annot: String? = null, builderAction: ContractEntrypointSequenceParameterBuilder.() -> Unit): Key = Key(ContractEntrypointSequenceParameterBuilder(name = annot).apply(builderAction))
    public fun valueSequence(annot: String? = null, builderAction: ContractEntrypointSequenceParameterBuilder.() -> Unit): Value = Value(ContractEntrypointSequenceParameterBuilder(name = annot).apply(builderAction))

    public fun keyMap(annot: String? = null, builderAction: ContractEntrypointMapParameterBuilder.() -> Unit): Key = Key(ContractEntrypointMapParameterBuilder(name = annot).apply(builderAction))
    public fun valueMap(annot: String? = null, builderAction: ContractEntrypointMapParameterBuilder.() -> Unit): Value = Value(ContractEntrypointMapParameterBuilder(name = annot).apply(builderAction))

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
