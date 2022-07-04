package it.airgap.tezos.michelson.micheline.dsl.builder.node

import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.replaceOrAdd
import it.airgap.tezos.michelson.micheline.MichelineLiteral
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.MichelinePrimitiveApplication
import it.airgap.tezos.michelson.micheline.dsl.builder.MichelineBuilder
import it.airgap.tezos.michelson.micheline.dsl.builder.MichelineTransformableBuilder

public class MichelinePrimitiveApplicationBuilder<in T : Michelson, in G : Michelson.Prim> @PublishedApi internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, Micheline>,
    prim: G,
) : MichelinePrimitiveApplicationSingleArgBuilder<T, G>(michelsonToMichelineConverter, prim) {
    override fun arg(builderAction: MichelineNodeBuilder<T, G>.() -> Unit): MichelineNodeBuilder<T, G> =
        MichelineNodeBuilder<T, G>(michelsonToMichelineConverter).apply(builderAction).also { args.add(it) }
}

public open class MichelinePrimitiveApplicationSingleArgBuilder<in T : Michelson, in G : Michelson.Prim> internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, Micheline>,
    prim: G,
) : MichelinePrimitiveApplicationNoArgsBuilder<G>(michelsonToMichelineConverter, prim) {
    public open fun arg(builderAction: MichelineNodeBuilder<T, G>.() -> Unit): MichelineNodeBuilder<T, G> =
        MichelineNodeBuilder<T, G>(michelsonToMichelineConverter).apply(builderAction).also { args.replaceOrAdd(0, it) }
}

public open class MichelinePrimitiveApplicationNoArgsBuilder<in T : Michelson.Prim> internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, Micheline>,
    prim: T,
) : MichelineTransformableBuilder(michelsonToMichelineConverter) {
    private val prim: MichelinePrimitiveApplication.Primitive = MichelinePrimitiveApplication.Primitive(prim.name)
    override val value: Micheline get() = MichelinePrimitiveApplication(prim, args.mapNotNull { it?.build() }, annots)

    protected val args: MutableList<MichelineBuilder?> = mutableListOf()

    private var annots: List<MichelinePrimitiveApplication.Annotation> = emptyList()
    public fun annots(vararg values: String) {
        annots(values.toList())
    }

    public infix fun annots(values: List<String>) {
        annots = values.map { MichelinePrimitiveApplication.Annotation(it) }
    }

    public infix fun annots(value: String) {
        annots = listOf(MichelinePrimitiveApplication.Annotation(value))
    }
}

public class MichelinePrimitiveApplicationOptionalIntegerArgBuilder<in T : Michelson.Prim> internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, Micheline>,
    prim: T,
) : MichelinePrimitiveApplicationNoArgsBuilder<T>(michelsonToMichelineConverter, prim) {
    public infix fun arg(value: String): MichelineBuilder = MichelineBuilder { MichelineLiteral.Integer(value) }.also { args.replaceOrAdd(0, it) }
    public infix fun arg(value: UByte): MichelineBuilder = arg(value.toString())
    public infix fun arg(value: UShort): MichelineBuilder = arg(value.toString())
    public infix fun arg(value: UInt): MichelineBuilder = arg(value.toString())
    public infix fun arg(value: ULong): MichelineBuilder = arg(value.toString())
}

public class MichelinePrimitiveApplicationIntegerArgBuilder<in T : Michelson.Prim> internal constructor(
    michelsonToMichelineConverter: Converter<Michelson, Micheline>,
    prim: T,
    arg: MichelineLiteral.Integer,
) : MichelinePrimitiveApplicationNoArgsBuilder<T>(michelsonToMichelineConverter, prim) {
    init {
        args.add(MichelineBuilder { arg })
    }
}