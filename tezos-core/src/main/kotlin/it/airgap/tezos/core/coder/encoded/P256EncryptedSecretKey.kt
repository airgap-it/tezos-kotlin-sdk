package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.P256EncryptedSecretKey

/**
 * Encodes a [P256EncryptedSecretKey] to [ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/P256EncryptedSecretKey/P256EncryptedSecretKeySamples.Coding#toBytes` for a sample usage.
 */
public fun P256EncryptedSecretKey.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes a [P256EncryptedSecretKey] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/P256EncryptedSecretKey/P256EncryptedSecretKeySamples.Coding#fromBytes` for a sample usage.
 */
public fun P256EncryptedSecretKey.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): P256EncryptedSecretKey = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface P256EncryptedSecretKeyCoderContext {
    public fun P256EncryptedSecretKey.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun P256EncryptedSecretKey.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): P256EncryptedSecretKey =
        encodedBytesCoder.decode(bytes, P256EncryptedSecretKey)

    public fun P256EncryptedSecretKey.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): P256EncryptedSecretKey =
        encodedBytesCoder.decodeConsuming(bytes, P256EncryptedSecretKey)
}
