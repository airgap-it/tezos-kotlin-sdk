package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.Ed25519SecretKey

/**
 * Encodes an [Ed25519SecretKey] to [bytes][ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Ed25519SecretKey/Ed25519SecretKeySamples.Coding#toBytes` for a sample usage.
 */
public fun Ed25519SecretKey.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes an [Ed25519SecretKey] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Ed25519SecretKey/Ed25519SecretKeySamples.Coding#fromBytes` for a sample usage.
 */
public fun Ed25519SecretKey.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Ed25519SecretKey = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface Ed25519SecretKeyCoderContext {
    public fun Ed25519SecretKey.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun Ed25519SecretKey.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): Ed25519SecretKey =
        encodedBytesCoder.decode(bytes, Ed25519SecretKey)

    public fun Ed25519SecretKey.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): Ed25519SecretKey =
        encodedBytesCoder.decodeConsuming(bytes, Ed25519SecretKey)
}
