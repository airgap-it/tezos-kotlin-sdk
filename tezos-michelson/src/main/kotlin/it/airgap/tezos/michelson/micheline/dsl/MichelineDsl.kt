package it.airgap.tezos.michelson.micheline.dsl

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.internal.context.withTezosContext
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.MichelineNode
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*

/**
 * Creates [MichelineNode] using Micheline DSL.
 *
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/michelson/Micheline/MichelineDsl.Usage` for a sample usage.
 */
public fun micheline(tezos: Tezos = Tezos.Default, builderAction: MichelineMichelsonExpressionBuilder.() -> Unit = {}): MichelineNode = withTezosContext {
    micheline(tezos.michelsonModule.dependencyRegistry.michelsonToMichelineConverter, builderAction)
}

@InternalTezosSdkApi
public interface MichelineDslContext {
    public fun micheline(
        michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
        builderAction: MichelineMichelsonExpressionBuilder.() -> Unit = {},
    ): MichelineNode = MichelineMichelsonExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()

    public fun michelineType(
        michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
        builderAction: MichelineMichelsonTypeExpressionBuilder.() -> Unit = {},
    ): MichelineNode = MichelineMichelsonTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()

    public fun michelineComparableType(
        michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
        builderAction: MichelineMichelsonComparableTypeExpressionBuilder.() -> Unit = {},
    ): MichelineNode = MichelineMichelsonComparableTypeExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()

    public fun michelineData(
        michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
        builderAction: MichelineMichelsonDataExpressionBuilder.() -> Unit = {},
    ): MichelineNode = MichelineMichelsonDataExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()

    public fun michelineInstruction(
        michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
        builderAction: MichelineMichelsonInstructionExpressionBuilder.() -> Unit = {},
    ): MichelineNode = MichelineMichelsonInstructionExpressionBuilder(michelsonToMichelineConverter).apply(builderAction).build()
}
