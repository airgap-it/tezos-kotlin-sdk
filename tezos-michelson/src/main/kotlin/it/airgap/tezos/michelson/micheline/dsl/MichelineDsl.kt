package it.airgap.tezos.michelson.micheline.dsl

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.internal.context.withTezosContext
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.Micheline
import it.airgap.tezos.michelson.micheline.dsl.builder.expression.*

/**
 * Creates [Micheline] using the [expression builder configuration][createExpression].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/Micheline/Micheline.Usage#create` for a sample usage.
 */
public fun micheline(tezos: Tezos = Tezos.Default, createExpression: MichelineMichelsonExpressionBuilder.() -> Unit = {}): Micheline = withTezosContext {
    micheline(tezos.michelsonModule.dependencyRegistry.michelsonToMichelineConverter, createExpression)
}

@InternalTezosSdkApi
public interface MichelineDslContext {
    public fun micheline(
        michelsonToMichelineConverter: Converter<Michelson, Micheline>,
        createExpression: MichelineMichelsonExpressionBuilder.() -> Unit = {},
    ): Micheline = MichelineMichelsonExpressionBuilder(michelsonToMichelineConverter).apply(createExpression).build()

    public fun michelineType(
        michelsonToMichelineConverter: Converter<Michelson, Micheline>,
        createExpression: MichelineMichelsonTypeExpressionBuilder.() -> Unit = {},
    ): Micheline = MichelineMichelsonTypeExpressionBuilder(michelsonToMichelineConverter).apply(createExpression).build()

    public fun michelineComparableType(
        michelsonToMichelineConverter: Converter<Michelson, Micheline>,
        createExpression: MichelineMichelsonComparableTypeExpressionBuilder.() -> Unit = {},
    ): Micheline = MichelineMichelsonComparableTypeExpressionBuilder(michelsonToMichelineConverter).apply(createExpression).build()

    public fun michelineData(
        michelsonToMichelineConverter: Converter<Michelson, Micheline>,
        createExpression: MichelineMichelsonDataExpressionBuilder.() -> Unit = {},
    ): Micheline = MichelineMichelsonDataExpressionBuilder(michelsonToMichelineConverter).apply(createExpression).build()

    public fun michelineInstruction(
        michelsonToMichelineConverter: Converter<Michelson, Micheline>,
        createExpression: MichelineMichelsonInstructionExpressionBuilder.() -> Unit = {},
    ): Micheline = MichelineMichelsonInstructionExpressionBuilder(michelsonToMichelineConverter).apply(createExpression).build()
}
