package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.P256SecretKey

/**
 * Encodes a [P256SecretKey] to bytes.
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/P256SecretKey/P256SecretKeySamples.Coding#toBytes` for a sample usage.
 */
public fun P256SecretKey.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes a [P256SecretKey] from [bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/P256SecretKey/P256SecretKeySamples.Coding#fromBytes` for a sample usage.
 */
public fun P256SecretKey.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): P256SecretKey = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface P256SecretKeyCoderContext {
    public fun P256SecretKey.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun P256SecretKey.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): P256SecretKey =
        encodedBytesCoder.decode(bytes, P256SecretKey)

    public fun P256SecretKey.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): P256SecretKey =
        encodedBytesCoder.decodeConsuming(bytes, P256SecretKey)
}
