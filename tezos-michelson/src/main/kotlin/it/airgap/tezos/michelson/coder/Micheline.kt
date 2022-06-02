package it.airgap.tezos.michelson.coder

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.Coder
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.asHexString
import it.airgap.tezos.michelson.internal.context.TezosMichelsonContext.toHexString
import it.airgap.tezos.michelson.internal.context.withTezosContext
import it.airgap.tezos.michelson.internal.michelsonModule
import it.airgap.tezos.michelson.micheline.MichelineNode
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

/**
 * Encodes this [MichelineNode] to a JSON[String].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/michelson/Micheline/MichelineSamples.Coding#toJson` for a sample usage.
 */
public fun <T : MichelineNode> T.toJsonString(tezos: Tezos = Tezos.Default): String = withTezosContext {
    toJsonString(tezos.michelsonModule.dependencyRegistry.michelineJsonCoder)
}

/**
 * Decodes a [MichelineNode] from a JSON[String].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/michelson/Micheline/MichelineSamples.Coding#fromJson` for a sample usage.
 */
public fun MichelineNode.Companion.fromJsonString(json: String, tezos: Tezos = Tezos.Default): MichelineNode = withTezosContext {
    fromJsonString(json, tezos.michelsonModule.dependencyRegistry.michelineJsonCoder)
}

/**
 * Encodes this [MichelineNode] to [bytes][ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/michelson/Micheline/MichelineSamples.Coding#toBytes` for a sample usage.
 */
public fun <T : MichelineNode> T.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.michelsonModule.dependencyRegistry.michelineBytesCoder)
}

/**
 * Decodes this [MichelineNode] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/michelson/Micheline/MichelineSamples.Coding#fromBytes` for a sample usage.
 */
public fun MichelineNode.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): MichelineNode = withTezosContext {
    decodeFromBytes(bytes, tezos.michelsonModule.dependencyRegistry.michelineBytesCoder)
}

/**
 * Encodes this [MichelineNode] to a hexadecimal [String] representation [with or without hex prefix][withHexPrefix].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/michelson/Micheline/MichelineSamples.Coding#toHexString` for a sample usage.
 */
public fun <T : MichelineNode> T.encodeToString(withHexPrefix: Boolean = false, tezos: Tezos = Tezos.Default): String = withTezosContext {
    encodeToString(tezos.michelsonModule.dependencyRegistry.michelineBytesCoder, withHexPrefix)
}

/**
 * Decodes this [MichelineNode] from [a hexadecimal String][string].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/michelson/Micheline/MichelineSamples.Coding#fromHexString` for a sample usage.
 */
public fun MichelineNode.Companion.decodeFromString(string: String, tezos: Tezos = Tezos.Default): MichelineNode = withTezosContext {
    decodeFromString(string, tezos.michelsonModule.dependencyRegistry.michelineBytesCoder)
}

@InternalTezosSdkApi
public interface MichelineCoderContext {
    public fun <T : MichelineNode> T.toJsonString(michelineJsonCoder: Coder<MichelineNode, JsonElement>): String =
        michelineJsonCoder.encode(this).toString()

    public fun MichelineNode.Companion.fromJsonString(json: String, michelineJsonCoder: Coder<MichelineNode, JsonElement>): MichelineNode =
        michelineJsonCoder.decode(Json.parseToJsonElement(json))

    public fun <T : MichelineNode> T.encodeToBytes(michelineBytesCoder: ConsumingBytesCoder<MichelineNode>): ByteArray =
        michelineBytesCoder.encode(this)

    public fun MichelineNode.Companion.decodeFromBytes(bytes: ByteArray, michelineBytesCoder: ConsumingBytesCoder<MichelineNode>): MichelineNode =
        michelineBytesCoder.decode(bytes)

    public fun MichelineNode.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, michelineBytesCoder: ConsumingBytesCoder<MichelineNode>): MichelineNode =
        michelineBytesCoder.decodeConsuming(bytes)

    public fun <T : MichelineNode> T.encodeToString(michelineBytesCoder: ConsumingBytesCoder<MichelineNode>, withHexPrefix: Boolean = false): String =
        encodeToBytes(michelineBytesCoder).toHexString().asString(withHexPrefix)

    public fun MichelineNode.Companion.decodeFromString(string: String, michelineBytesCoder: ConsumingBytesCoder<MichelineNode>): MichelineNode =
        MichelineNode.decodeFromBytes(string.asHexString().toByteArray(), michelineBytesCoder)
}