package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.Secp256K1PublicKey

/**
 * Encodes an [Secp256K1PublicKey] to bytes.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Secp256K1PublicKey/Secp256K1PublicKeySamples.Coding#toBytes` for a sample usage.
 */
public fun Secp256K1PublicKey.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes an [Secp256K1PublicKey] from [bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Secp256K1PublicKey/Secp256K1PublicKeySamples.Coding#fromBytes` for a sample usage.
 */
public fun Secp256K1PublicKey.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Secp256K1PublicKey = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface Secp256K1PublicKeyCoderContext {
    public fun Secp256K1PublicKey.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun Secp256K1PublicKey.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): Secp256K1PublicKey =
        encodedBytesCoder.decode(bytes, Secp256K1PublicKey)

    public fun Secp256K1PublicKey.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): Secp256K1PublicKey =
        encodedBytesCoder.decodeConsuming(bytes, Secp256K1PublicKey)
}
