package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.Ed25519PublicKeyHash

/**
 * Encodes an [Ed25519PublicKeyHash] to [ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Ed25519PublicKeyHash/Ed25519PublicKeyHashSamples.Coding#toBytes` for a sample usage.
 */
public fun Ed25519PublicKeyHash.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes an [Ed25519PublicKeyHash] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Ed25519PublicKeyHash/Ed25519PublicKeyHashSamples.Coding#fromBytes` for a sample usage.
 */
public fun Ed25519PublicKeyHash.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Ed25519PublicKeyHash = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface Ed25519PublicKeyHashCoderContext {
    public fun Ed25519PublicKeyHash.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun Ed25519PublicKeyHash.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): Ed25519PublicKeyHash =
        encodedBytesCoder.decode(bytes, Ed25519PublicKeyHash)

    public fun Ed25519PublicKeyHash.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): Ed25519PublicKeyHash =
        encodedBytesCoder.decodeConsuming(bytes, Ed25519PublicKeyHash)
}
