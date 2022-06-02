package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.Secp256K1Element

/**
 * Encodes an [Secp256K1Element] to [bytes][ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Secp256K1Element/Secp256K1ElementSamples.Coding#toBytes` for a sample usage.
 */
public fun Secp256K1Element.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes an [Secp256K1Element] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/Secp256K1Element/Secp256K1ElementSamples.Coding#fromBytes` for a sample usage.
 */
public fun Secp256K1Element.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): Secp256K1Element = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface Secp256K1ElementCoderContext {
    public fun Secp256K1Element.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun Secp256K1Element.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): Secp256K1Element =
        encodedBytesCoder.decode(bytes, Secp256K1Element)

    public fun Secp256K1Element.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): Secp256K1Element =
        encodedBytesCoder.decodeConsuming(bytes, Secp256K1Element)
}
