package it.airgap.tezos.michelson.converter

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.Michelson
import it.airgap.tezos.michelson.internal.context.withTezosContext
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.Micheline

/**
 * Converts [Micheline] to [Michelson]
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/Micheline/MichelineSamples.Usage#toMichelson` for a sample usage.
 */
public fun <T : Micheline> T.toMichelson(tezos: Tezos = Tezos.Default): Michelson = withTezosContext {
    toMichelson(tezos.michelsonModule.dependencyRegistry.michelineToMichelsonConverter)
}

/**
 * Convenience method to get the current [Micheline] value as [T].
 *
 * @throws IllegalArgumentException if the current value is not [T].
 */
public inline fun <reified T : Micheline> Micheline?.tryAs(): T = withTezosContext {
    this@tryAs as? T ?: failWithIllegalArgument("Micheline value ${this@tryAs?.let { it::class } ?: "null" } is not ${T::class}")
}

/**
 * Converts [Micheline] to a [String] expression as specified in [the documentation](https://tezos.gitlab.io/shell/micheline.html).
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/Micheline/MichelineSamples.Usage#toExpression` for a sample usage.
 */
public fun <T : Micheline> T.toExpression(tezos: Tezos = Tezos.Default): String = withTezosContext {
    toExpression(tezos.michelsonModule.dependencyRegistry.michelineToStringConverter)
}

/**
 * Converts [Micheline] to a shortened [String] expression.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/Micheline/MichelineSamples.Usage#toCompactExpression` for a sample usage.
 * 
 * @see [Micheline.toExpression]
 */
public fun <T : Micheline> T.toCompactExpression(tezos: Tezos = Tezos.Default): String = withTezosContext {
    toCompactExpression(tezos.michelsonModule.dependencyRegistry.michelineToCompactStringConverter)
}

@InternalTezosSdkApi
public interface MichelineConverterContext {
    public fun <T : Micheline> T.toMichelson(michelineToMichelsonConverter: Converter<Micheline, Michelson>): Michelson =
        michelineToMichelsonConverter.convert(this)

    public fun <T : Micheline> T.toExpression(michelineToStringConverter: Converter<Micheline, String>): String =
        michelineToStringConverter.convert(this)

    public fun <T : Micheline> T.toCompactExpression(michelineToCompactStringConverter: Converter<Micheline, String>): String =
        michelineToCompactStringConverter.convert(this)
}