package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.SaplingSpendingKey

/**
 * Encodes a [SaplingSpendingKey] to [bytes][ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/SaplingSpendingKey/SaplingSpendingKeySamples.Coding#toBytes` for a sample usage.
 */
public fun SaplingSpendingKey.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes a [SaplingSpendingKey] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/SaplingSpendingKey/SaplingSpendingKeySamples.Coding#fromBytes` for a sample usage.
 */
public fun SaplingSpendingKey.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): SaplingSpendingKey = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface SaplingSpendingKeyCoderContext {
    public fun SaplingSpendingKey.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun SaplingSpendingKey.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): SaplingSpendingKey =
        encodedBytesCoder.decode(bytes, SaplingSpendingKey)

    public fun SaplingSpendingKey.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): SaplingSpendingKey =
        encodedBytesCoder.decodeConsuming(bytes, SaplingSpendingKey)
}
