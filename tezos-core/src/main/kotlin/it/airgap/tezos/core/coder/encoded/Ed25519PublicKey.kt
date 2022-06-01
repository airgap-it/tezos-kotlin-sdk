package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.Ed25519PublicKey

/**
 * Encodes an [Ed25519PublicKey] to [ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Ed25519PublicKey/Ed25519PublicKeySamples.Coding#toBytes` for a sample usage.
 */
public fun Ed25519PublicKey.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes an [Ed25519PublicKey] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Ed25519PublicKey/Ed25519PublicKeySamples.Coding#fromBytes` for a sample usage.
 */
public fun Ed25519PublicKey.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Ed25519PublicKey = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface Ed25519PublicKeyCoderContext {
    public fun Ed25519PublicKey.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun Ed25519PublicKey.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): Ed25519PublicKey =
        encodedBytesCoder.decode(bytes, Ed25519PublicKey)

    public fun Ed25519PublicKey.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): Ed25519PublicKey =
        encodedBytesCoder.decodeConsuming(bytes, Ed25519PublicKey)
}
