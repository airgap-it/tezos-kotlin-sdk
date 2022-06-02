package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.Ed25519BlindedPublicKeyHash

/**
 * Encodes an [Ed25519BlindedPublicKeyHash] to [bytes][ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Ed25519BlindedPublicKeyHash/Ed25519BlindedPublicKeyHashSamples.Coding#toBytes` for a sample usage.
 */
public fun Ed25519BlindedPublicKeyHash.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes an [Ed25519BlindedPublicKeyHash] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Ed25519BlindedPublicKeyHash/Ed25519BlindedPublicKeyHashSamples.Coding#fromBytes` for a sample usage.
 */
public fun Ed25519BlindedPublicKeyHash.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Ed25519BlindedPublicKeyHash = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface Ed25519BlindedPublicKeyHashCoderContext {
    public fun Ed25519BlindedPublicKeyHash.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun Ed25519BlindedPublicKeyHash.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): Ed25519BlindedPublicKeyHash =
        encodedBytesCoder.decode(bytes, Ed25519BlindedPublicKeyHash)

    public fun Ed25519BlindedPublicKeyHash.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): Ed25519BlindedPublicKeyHash =
        encodedBytesCoder.decodeConsuming(bytes, Ed25519BlindedPublicKeyHash)
}
