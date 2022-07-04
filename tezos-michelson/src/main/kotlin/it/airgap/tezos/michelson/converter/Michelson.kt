package it.airgap.tezos.michelson.converter

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.converter.Converter
import it.airgap.tezos.michelson.*
import it.airgap.tezos.michelson.internal.context.withTezosContext
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.Micheline

/**
 * Converts [Michelson] to [Micheline].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/Michelson/MichelsonSamples.Usage#toMicheline` for a sample usage.
 */
public fun <T : Michelson> T.toMicheline(tezos: Tezos = Tezos.Default): Micheline = withTezosContext {
    toMicheline(tezos.michelsonModule.dependencyRegistry.michelsonToMichelineConverter)
}

/**
 * Convenience method to get the current [Michelson] value as [T].
 *
 * @throws IllegalArgumentException if the current value is not [T].
 */
public inline fun <reified T : Michelson> Michelson?.tryAs(): T = withTezosContext {
    this@tryAs as? T ?: failWithIllegalArgument("Michelson value ${this@tryAs?.let { it::class } ?: "null" } is not ${T::class}")
}

/**
 * Creates [Michelson.Prim] from [String][value] or returns `null` if the [value] is invalid.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun Michelson.Prim.Companion.fromStringOrNull(value: String, tezos: Tezos = Tezos.Default): Michelson.Prim? = withTezosContext {
    fromStringOrNull(value, tezos.michelsonModule.dependencyRegistry.stringToMichelsonPrimConverter)
}

/**
 * Creates [MichelsonData.Prim] from [String][value] or returns `null` if the [value] is invalid.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun MichelsonData.Prim.Companion.fromStringOrNull(value: String, tezos: Tezos = Tezos.Default): MichelsonData.Prim? = withTezosContext {
    fromStringOrNull(value, tezos.michelsonModule.dependencyRegistry.stringToMichelsonDataPrimConverter)
}

/**
 * Creates [MichelsonInstruction.Prim] from [String][value] or returns `null` if the [value] is invalid.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun MichelsonInstruction.Prim.Companion.fromStringOrNull(value: String, tezos: Tezos = Tezos.Default): MichelsonInstruction.Prim? = withTezosContext {
    fromStringOrNull(value, tezos.michelsonModule.dependencyRegistry.stringToMichelsonInstructionPrimConverter)
}

/**
 * Creates [MichelsonType.Prim] from [String][value] or returns `null` if the [value] is invalid.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun MichelsonType.Prim.Companion.fromStringOrNull(value: String, tezos: Tezos = Tezos.Default): MichelsonType.Prim? = withTezosContext {
    fromStringOrNull(value, tezos.michelsonModule.dependencyRegistry.stringToMichelsonTypePrimConverter)
}

/**
 * Creates [MichelsonComparableType.Prim] from [String][value] or returns `null` if the [value] is invalid.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun MichelsonComparableType.Prim.Companion.fromStringOrNull(value: String, tezos: Tezos = Tezos.Default): MichelsonComparableType.Prim? = withTezosContext {
    fromStringOrNull(value, tezos.michelsonModule.dependencyRegistry.stringToMichelsonComparableTypePrimConverter)
}

/**
 * Creates [Michelson.Prim] from [ByteArray][value] or returns `null` if the [value] is invalid.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun Michelson.Prim.Companion.fromTagOrNull(value: ByteArray, tezos: Tezos = Tezos.Default): Michelson.Prim? = withTezosContext {
    fromTagOrNull(value, tezos.michelsonModule.dependencyRegistry.tagToMichelsonPrimConverter)
}

/**
 * Creates [MichelsonData.Prim] from [ByteArray][value] or returns `null` if the [value] is invalid.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun MichelsonData.Prim.Companion.fromTagOrNull(value: ByteArray, tezos: Tezos = Tezos.Default): MichelsonData.Prim? = withTezosContext {
    fromTagOrNull(value, tezos.michelsonModule.dependencyRegistry.tagToMichelsonDataPrimConverter)
}

/**
 * Creates [MichelsonInstruction.Prim] from [ByteArray][value] or returns `null` if the [value] is invalid.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun MichelsonInstruction.Prim.Companion.fromTagOrNull(value: ByteArray, tezos: Tezos = Tezos.Default): MichelsonInstruction.Prim? = withTezosContext {
    fromTagOrNull(value, tezos.michelsonModule.dependencyRegistry.tagToMichelsonInstructionPrimConverter)
}

/**
 * Creates [MichelsonType.Prim] from [ByteArray][value] or returns `null` if the [value] is invalid.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun MichelsonType.Prim.Companion.fromTagOrNull(value: ByteArray, tezos: Tezos = Tezos.Default): MichelsonType.Prim? = withTezosContext {
    fromTagOrNull(value, tezos.michelsonModule.dependencyRegistry.tagToMichelsonTypePrimConverter)
}

/**
 * Creates [MichelsonComparableType.Prim] from [ByteArray][value] or returns `null` if the [value] is invalid.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun MichelsonComparableType.Prim.Companion.fromTagOrNull(value: ByteArray, tezos: Tezos = Tezos.Default): MichelsonComparableType.Prim? = withTezosContext {
    fromTagOrNull(value, tezos.michelsonModule.dependencyRegistry.tagToMichelsonComparableTypePrimConverter)
}

public interface MichelsonConverterContext {
    public fun <T : Michelson> T.toMicheline(michelsonToMichelineConverter: Converter<Michelson, Micheline>): Micheline =
        michelsonToMichelineConverter.convert(this)

    public fun Michelson.Prim.Companion.fromStringOrNull(value: String, stringToMichelsonPrimConverter: Converter<String, Michelson.Prim>): Michelson.Prim? =
        runCatching { stringToMichelsonPrimConverter.convert(value) }.getOrNull()

    public fun MichelsonData.Prim.Companion.fromStringOrNull(value: String, stringToMichelsonDataPrimConverter: Converter<String, MichelsonData.Prim>): MichelsonData.Prim? =
        runCatching { stringToMichelsonDataPrimConverter.convert(value) }.getOrNull()

    public fun MichelsonInstruction.Prim.Companion.fromStringOrNull(value: String, stringToMichelsonInstructionPrimConverter: Converter<String, MichelsonInstruction.Prim>): MichelsonInstruction.Prim? =
        runCatching { stringToMichelsonInstructionPrimConverter.convert(value) }.getOrNull()

    public fun MichelsonType.Prim.Companion.fromStringOrNull(value: String, stringToMichelsonTypePrimConverter: Converter<String, MichelsonType.Prim>): MichelsonType.Prim? =
        runCatching { stringToMichelsonTypePrimConverter.convert(value) }.getOrNull()

    public fun MichelsonComparableType.Prim.Companion.fromStringOrNull(value: String, stringToMichelsonComparableTypePrimConverter: Converter<String, MichelsonComparableType.Prim>): MichelsonComparableType.Prim? =
        runCatching { stringToMichelsonComparableTypePrimConverter.convert(value) }.getOrNull()

    public fun Michelson.Prim.Companion.fromTagOrNull(value: ByteArray, tagToMichelsonPrimConverter: Converter<ByteArray, Michelson.Prim>): Michelson.Prim? =
        runCatching { tagToMichelsonPrimConverter.convert(value) }.getOrNull()

    public fun MichelsonData.Prim.Companion.fromTagOrNull(value: ByteArray, tagToMichelsonDataPrimConverter: Converter<ByteArray, MichelsonData.Prim>): MichelsonData.Prim? =
        runCatching { tagToMichelsonDataPrimConverter.convert(value) }.getOrNull()

    public fun MichelsonInstruction.Prim.Companion.fromTagOrNull(value: ByteArray, tagToMichelsonInstructionPrimConverter: Converter<ByteArray, MichelsonInstruction.Prim>): MichelsonInstruction.Prim? =
        runCatching { tagToMichelsonInstructionPrimConverter.convert(value) }.getOrNull()

    public fun MichelsonType.Prim.Companion.fromTagOrNull(value: ByteArray, tagToMichelsonTypePrimConverter: Converter<ByteArray, MichelsonType.Prim>): MichelsonType.Prim? =
        runCatching { tagToMichelsonTypePrimConverter.convert(value) }.getOrNull()

    public fun MichelsonComparableType.Prim.Companion.fromTagOrNull(value: ByteArray, tagToMichelsonComparableTypePrimConverter: Converter<ByteArray, MichelsonComparableType.Prim>): MichelsonComparableType.Prim? =
        runCatching { tagToMichelsonComparableTypePrimConverter.convert(value) }.getOrNull()
}