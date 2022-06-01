package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.ProtocolHash

/**
 * Encodes a [ProtocolHash] to [ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/ProtocolHash/ProtocolHashSamples.Coding#toBytes` for a sample usage.
 */
public fun ProtocolHash.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes a [ProtocolHash] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/ProtocolHash/ProtocolHashSamples.Coding#fromBytes` for a sample usage.
 */
public fun ProtocolHash.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): ProtocolHash = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface ProtocolHashCoderContext {
    public fun ProtocolHash.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun ProtocolHash.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): ProtocolHash =
        encodedBytesCoder.decode(bytes, ProtocolHash)

    public fun ProtocolHash.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): ProtocolHash =
        encodedBytesCoder.decodeConsuming(bytes, ProtocolHash)
}
