package it.airgap.tezos.core.coder.encoded

import it.airgap.tezos.core.Tezos
import it.airgap.tezos.core.internal.annotation.InternalTezosSdkApi
import it.airgap.tezos.core.internal.coder.encoded.EncodedBytesCoder
import it.airgap.tezos.core.internal.context.withTezosContext
import it.airgap.tezos.core.internal.coreModule
import it.airgap.tezos.core.type.encoded.BlockHash
import it.airgap.tezos.core.type.encoded.SaplingAddress

/**
 * Encodes a [SaplingAddress] to [bytes][ByteArray].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/SaplingAddress/SaplingAddressSamples.Coding#toBytes` for a sample usage.
 */
public fun SaplingAddress.encodeToBytes(tezos: Tezos = Tezos.Default): ByteArray = withTezosContext {
    encodeToBytes(tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

/**
 * Decodes a [SaplingAddress] from [ByteArray][bytes].
 * Takes an optional [tezos] object to provide context. If the argument was omitted, the default [Tezos] instance will be used.
 *
 * See `samples/src/test/kotlin/type/SaplingAddress/SaplingAddressSamples.Coding#fromBytes` for a sample usage.
 */
public fun SaplingAddress.Companion.decodeFromBytes(bytes: ByteArray, tezos: Tezos = Tezos.Default): SaplingAddress = withTezosContext {
    decodeFromBytes(bytes, tezos.coreModule.dependencyRegistry.encodedBytesCoder)
}

@InternalTezosSdkApi
public interface SaplingAddressCoderContext {
    public fun SaplingAddress.encodeToBytes(encodedBytesCoder: EncodedBytesCoder): ByteArray =
        encodedBytesCoder.encode(this)

    public fun SaplingAddress.Companion.decodeFromBytes(bytes: ByteArray, encodedBytesCoder: EncodedBytesCoder): SaplingAddress =
        encodedBytesCoder.decode(bytes, SaplingAddress)

    public fun SaplingAddress.Companion.decodeConsumingFromBytes(bytes: MutableList<Byte>, encodedBytesCoder: EncodedBytesCoder): SaplingAddress =
        encodedBytesCoder.decodeConsuming(bytes, SaplingAddress)
}
