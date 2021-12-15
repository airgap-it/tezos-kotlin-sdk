package it.airgap.tezos.michelson.micheline.dsl.builder.node

import it.airgap.tezos.core.internal.utils.asHexString
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.MichelineSequence
import it.airgap.tezos.michelson.micheline.dsl.builder.MichelineBuilder
import it.airgap.tezos.michelson.micheline.dsl.builder.MichelineTransformableBuilder
import it.airgap.tezos.michelson.toMicheline

public class MichelineNodeBuilder<in T : Michelson, in G : Michelson.GrammarType> internal constructor() : MichelineTransformableBuilder() {
    override val value: MichelineNode
        get () {
            val nodes = builders.map { it.build() }
            return nodes.singleOrNull() ?: MichelineSequence(nodes)
        }

    @PublishedApi
    internal val builders: MutableList<MichelineBuilder> = mutableListOf()

    // -- literal --

    public infix fun int(value: String): MichelineBuilder =
        MichelineBuilder { MichelineLiteral.Integer(value) }.also { builders.add(it) }

    public infix fun int(value: Byte): MichelineBuilder = int(value.toString())
    public infix fun int(value: Short): MichelineBuilder = int(value.toString())
    public infix fun int(value: Int): MichelineBuilder = int(value.toString())
    public infix fun int(value: Long): MichelineBuilder = int(value.toString())

    public infix fun string(value: String): MichelineBuilder =
        MichelineBuilder { (MichelineLiteral.String(value)) }.also { builders.add(it) }

    public infix fun bytes(value: String): MichelineBuilder =
        MichelineBuilder { (MichelineLiteral.Bytes(value.asHexString().asString(withPrefix = true))) }.also { builders.add(it) }

    public infix fun bytes(value: ByteArray): MichelineBuilder =
        MichelineBuilder { (MichelineLiteral.Bytes(value)) }.also { builders.add(it) }

    // -- primitive application --

    public fun primitiveApplication(prim: G, builderAction: MichelinePrimitiveApplicationBuilder<T, G>.() -> Unit = {}): MichelinePrimitiveApplicationBuilder<T, G> =
        MichelinePrimitiveApplicationBuilder<T, G>(prim).apply(builderAction).also { builders.add(it) }

    internal fun primitiveApplicationSingleArg(
        prim: G,
        builderAction: MichelinePrimitiveApplicationSingleArgBuilder<T, G>.() -> Unit,
    ): MichelinePrimitiveApplicationSingleArgBuilder<T, G> =
        MichelinePrimitiveApplicationSingleArgBuilder<T, G>(prim).apply(builderAction).also { builders.add(it) }

    // -- sequence --

    public fun sequence(builderAction: MichelineNodeBuilder<T, G>.() -> Unit): MichelineNodeBuilder<T, G> =
        MichelineNodeBuilder<T, G>().apply(builderAction).mapNode { if (it is MichelineSequence) it else MichelineSequence(listOf(it)) }.also { builders.add(it) }

    // -- michelson --

    public infix fun michelson(value: T): MichelineBuilder =
        MichelineBuilder { value.toMicheline() }.also { builders.add(it) }
}
