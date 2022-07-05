package it.airgap.tezos.michelson.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.asHexString
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.toHexString
import it.airgap.tezos.michelson.internal.context.withTezosContext
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.Micheline
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * Encodes this [Micheline] to a JSON[String].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun <T : Micheline> T.toJsonString(tezos: Tezos = Tezos.Default): String = withTezosContext {
    toJsonString(tezos.michelsonModule.dependencyRegistry.michelineJsonCoder)
}

/**
 * Decodes a [Micheline] from a JSON[String].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 */
public fun Micheline.Companion.fromJsonString(json: String, tezos: Tezos = Tezos.Default): Micheline = withTezosContext {
    fromJsonString(json, tezos.michelsonModule.dependencyRegistry.michelineJsonCoder)
}

/**
 * Encodes this [Micheline] to [bytes][ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/Micheline/MichelineSamples.Coding#toBytes` for a sample usage.
 */
public fun <T : Micheline> T.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.michelsonModule.dependencyRegistry.michelineBytesCoder)
}

/**
 * Decodes this [Micheline] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/Micheline/MichelineSamples.Coding#fromBytes` for a sample usage.
 */
public fun Micheline.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Micheline = withTezosContext {
    decodeFromBytes(bytes, tezos.michelsonModule.dependencyRegistry.michelineBytesCoder)
}

/**
 * Encodes this [Micheline] to a hexadecimal [String] representation [with or without hex prefix][withHexPrefix].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/Micheline/MichelineSamples.Coding#toHexString` for a sample usage.
 */
public fun <T : Micheline> T.encodeToString(withHexPrefix: Boolean = false, tezos: Tezos = Tezos.Default): String = withTezosContext {
    encodeToString(tezos.michelsonModule.dependencyRegistry.michelineBytesCoder, withHexPrefix)
}

/**
 * Decodes this [Micheline] from [a hexadecimal String][string].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/Micheline/MichelineSamples.Coding#fromHexString` for a sample usage.
 */
public fun Micheline.Companion.decodeFromString(string: String, tezos: Tezos = Tezos.Default): Micheline = withTezosContext {
    decodeFromString(string, tezos.michelsonModule.dependencyRegistry.michelineBytesCoder)
}

@InternalTezosSdkApi
public interface MichelineCoderContext {
    public fun <T : Micheline> T.toJsonString(michelineJsonCoder: Coder<Micheline, JsonElement>): String =
        michelineJsonCoder.encode(this).toString()

    public fun Micheline.Companion.fromJsonString(json: String, michelineJsonCoder: Coder<Micheline, JsonElement>): Micheline =
        michelineJsonCoder.decode(Json.parseToJsonElement(json))

    public fun <T : Micheline> T.encodeToBytes(michelineBytesCoder: ConsumingBytesCoder<Micheline>): ByteArray =
        michelineBytesCoder.encode(this)

    public fun Micheline.Companion.decodeFromBytes(bytes: ByteArray, michelineBytesCoder: ConsumingBytesCoder<Micheline>): Micheline =
        michelineBytesCoder.decode(bytes)

    public fun Micheline.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, michelineBytesCoder: ConsumingBytesCoder<Micheline>): Micheline =
        michelineBytesCoder.decodeConsuming(bytes)

    public fun <T : Micheline> T.encodeToString(michelineBytesCoder: ConsumingBytesCoder<Micheline>, withHexPrefix: Boolean = false): String =
        encodeToBytes(michelineBytesCoder).toHexString().asString(withHexPrefix)

    public fun Micheline.Companion.decodeFromString(string: String, michelineBytesCoder: ConsumingBytesCoder<Micheline>): Micheline =
        Micheline.decodeFromBytes(string.asHexString().toByteArray(), michelineBytesCoder)
}