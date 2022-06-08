package it.airgap.tezos.michelson.converter

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.internal.context.withTezosContext
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.MichelineNode

/**
 * Converts [MichelineNode] to [Michelson]
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/Micheline/MichelineSamples.Usage#toMichelson` for a sample usage.
 */
public fun <T : MichelineNode> T.toMichelson(tezos: Tezos = Tezos.Default): Michelson = withTezosContext {
    toMichelson(tezos.michelsonModule.dependencyRegistry.michelineToMichelsonConverter)
}

/**
 * Converts [MichelineNode] to a [String] expression as specified in [the documentation](https://tezos.gitlab.io/shell/micheline.html).
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/Micheline/MichelineSamples.Usage#toExpression` for a sample usage.
 */
public fun <T : MichelineNode> T.toExpression(tezos: Tezos = Tezos.Default): String = withTezosContext {
    toExpression(tezos.michelsonModule.dependencyRegistry.michelineToStringConverter)
}

/**
 * Converts [MichelineNode] to a shortened [String] expression.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/Micheline/MichelineSamples.Usage#toCompactExpression` for a sample usage.
 * 
 * @see [MichelineNode.toExpression]
 */
public fun <T : MichelineNode> T.toCompactExpression(tezos: Tezos = Tezos.Default): String = withTezosContext {
    toCompactExpression(tezos.michelsonModule.dependencyRegistry.michelineToCompactStringConverter)
}

@InternalTezosSdkApi
public interface MichelineConverterContext {
    public fun <T : MichelineNode> T.toMichelson(michelineToMichelsonConverter: Converter<MichelineNode, Michelson>): Michelson =
        michelineToMichelsonConverter.convert(this)

    public fun <T : MichelineNode> T.toExpression(michelineToStringConverter: Converter<MichelineNode, String>): String =
        michelineToStringConverter.convert(this)

    public fun <T : MichelineNode> T.toCompactExpression(michelineToCompactStringConverter: Converter<MichelineNode, String>): String =
        michelineToCompactStringConverter.convert(this)
}