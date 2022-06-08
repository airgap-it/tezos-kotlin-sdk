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
 * Creates [MichelineNode] using the [expression builder configuration][createExpression].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/Micheline/Micheline.Usage#create` for a sample usage.
 */
public fun micheline(tezos: Tezos = Tezos.Default, createExpression: MichelineMichelsonExpressionBuilder.() -> Unit = {}): MichelineNode = withTezosContext {
    micheline(tezos.michelsonModule.dependencyRegistry.michelsonToMichelineConverter, createExpression)
}

@InternalTezosSdkApi
public interface MichelineDslContext {
    public fun micheline(
        michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
        createExpression: MichelineMichelsonExpressionBuilder.() -> Unit = {},
    ): MichelineNode = MichelineMichelsonExpressionBuilder(michelsonToMichelineConverter).apply(createExpression).build()

    public fun michelineType(
        michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
        createExpression: MichelineMichelsonTypeExpressionBuilder.() -> Unit = {},
    ): MichelineNode = MichelineMichelsonTypeExpressionBuilder(michelsonToMichelineConverter).apply(createExpression).build()

    public fun michelineComparableType(
        michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
        createExpression: MichelineMichelsonComparableTypeExpressionBuilder.() -> Unit = {},
    ): MichelineNode = MichelineMichelsonComparableTypeExpressionBuilder(michelsonToMichelineConverter).apply(createExpression).build()

    public fun michelineData(
        michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
        createExpression: MichelineMichelsonDataExpressionBuilder.() -> Unit = {},
    ): MichelineNode = MichelineMichelsonDataExpressionBuilder(michelsonToMichelineConverter).apply(createExpression).build()

    public fun michelineInstruction(
        michelsonToMichelineConverter: Converter<Michelson, MichelineNode>,
        createExpression: MichelineMichelsonInstructionExpressionBuilder.() -> Unit = {},
    ): MichelineNode = MichelineMichelsonInstructionExpressionBuilder(michelsonToMichelineConverter).apply(createExpression).build()
}
