package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.ChainId

/**
 * Encodes a [ChainId] to [ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/ChainId/ChainIdSamples.Coding#toBytes` for a sample usage.
 */
public fun ChainId.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes a [ChainId] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/ChainId/ChainIdSamples.Coding#fromBytes` for a sample usage.
 */
public fun ChainId.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): ChainId = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface ChainIdCoderContext {
    public fun ChainId.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun ChainId.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): ChainId =
        encodedBytesCoder.decode(bytes, ChainId)

    public fun ChainId.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): ChainId =
        encodedBytesCoder.decodeConsuming(bytes, ChainId)
}
