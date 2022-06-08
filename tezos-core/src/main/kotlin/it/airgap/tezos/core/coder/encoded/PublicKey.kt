package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.ConsumingBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.PublicKey

/**
 * Encodes a [PublicKey] to [bytes][ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/PublicKey/PublicKeySamples.Coding#toBytes` for a sample usage.
 */
public fun PublicKey.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.publicKeyBytesCoder)
}

/**
 * Decodes a [PublicKey] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/PublicKey/PublicKeySamples.Coding#fromBytes` for a sample usage.
 */
public fun PublicKey.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): PublicKey = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.publicKeyBytesCoder)
}

@InternalTezosSdkApi
public interface PublicKeyCoderContext {
    public fun PublicKey.encodeToBytes(publicKeyBytesCoder: ConsumingBytesCoder<PublicKey>): ByteArray =
        publicKeyBytesCoder.encode(this)

    public fun PublicKey.Companion.decodeFromBytes(bytes: ByteArray, publicKeyBytesCoder: ConsumingBytesCoder<PublicKey>): PublicKey =
        publicKeyBytesCoder.decode(bytes)

    public fun PublicKey.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, publicKeyBytesCoder: ConsumingBytesCoder<PublicKey>): PublicKey =
        publicKeyBytesCoder.decodeConsuming(bytes)
}
