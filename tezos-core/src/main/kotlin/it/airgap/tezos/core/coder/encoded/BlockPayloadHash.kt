package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.BlockPayloadHash

/**
 * Encodes a [BlockPayloadHash] to bytes.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/BlockPayloadHash/BlockPayloadHashSamples.Coding#toBytes` for a sample usage.
 */
public fun BlockPayloadHash.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes a [BlockPayloadHash] from [bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/BlockPayloadHash/BlockPayloadHashSamples.Coding#fromBytes` for a sample usage.
 */
public fun BlockPayloadHash.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): BlockPayloadHash = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface BlockPayloadHashCoderContext {
    public fun BlockPayloadHash.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun BlockPayloadHash.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): BlockPayloadHash =
        encodedBytesCoder.decode(bytes, BlockPayloadHash)

    public fun BlockPayloadHash.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): BlockPayloadHash =
        encodedBytesCoder.decodeConsuming(bytes, BlockPayloadHash)
}
